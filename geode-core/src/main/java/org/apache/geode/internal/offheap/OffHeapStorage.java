/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.internal.offheap;

import java.lang.reflect.Method;

import org.apache.geode.distributed.internal.DistributionStatsImpl;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.cache.CacheException;
import org.apache.geode.distributed.DistributedSystem;
import org.apache.geode.distributed.internal.DistributionConfig;
import org.apache.geode.distributed.internal.DistributionStats;
import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.distributed.internal.InternalLocator;
import org.apache.geode.internal.ClassPathLoader;
import org.apache.geode.internal.i18n.LocalizedStrings;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;
import org.apache.geode.statistics.StatsFactory;

/**
 * Enables off-heap storage by creating a MemoryAllocator.
 * @since Geode 1.0
 */
//public class OffHeapStorage implements OffHeapStorageStats{
public class OffHeapStorage {
  public static final String STAY_CONNECTED_ON_OUTOFOFFHEAPMEMORY_PROPERTY =
      DistributionConfig.GEMFIRE_PREFIX + "offheap.stayConnectedOnOutOfOffHeapMemory";

  private final OffHeapStorageStats stats;

  private OffHeapStorage(StatisticsFactory factory) {
    this.stats = StatsFactory.createOffHeapStorageStatsImpl(factory,"offHeapMemoryStats");
  }

  public static long parseOffHeapMemorySize(String value) {
    final long parsed = parseLongWithUnits(value, 0L, 1024 * 1024);
    if (parsed < 0) {
      return 0;
    }
    return parsed;
  }

  public static long calcMaxSlabSize(long offHeapMemorySize) {
    final String offHeapSlabConfig =
        System.getProperty(DistributionConfig.GEMFIRE_PREFIX + "OFF_HEAP_SLAB_SIZE");
    long result = 0;
    if (offHeapSlabConfig != null && !offHeapSlabConfig.equals("")) {
      result = parseLongWithUnits(offHeapSlabConfig, MAX_SLAB_SIZE, 1024 * 1024);
      if (result > offHeapMemorySize) {
        result = offHeapMemorySize;
      }
    } else { // calculate slabSize
      if (offHeapMemorySize < MAX_SLAB_SIZE) {
        // just one slab
        result = offHeapMemorySize;
      } else {
        result = MAX_SLAB_SIZE;
      }
    }
    assert result > 0 && result <= MAX_SLAB_SIZE && result <= offHeapMemorySize;
    return result;
  }

  /**
   * Validates that the running VM is compatible with off heap storage. Throws a {@link
   * CacheException} if incompatible.
   */
  @SuppressWarnings("serial")
  private static void validateVmCompatibility() {
    try {
      // Do we have the Unsafe class? Throw ClassNotFoundException if not.
      Class<?> klass = ClassPathLoader.getLatest().forName("sun.misc.Unsafe");

      // Okay, we have the class. Do we have the copyMemory method (not all JVMs support it)? Throw
      // NoSuchMethodException if not.
      @SuppressWarnings("unused")
      Method copyMemory = klass.getMethod("copyMemory", Object.class, long.class, Object.class,
          long.class, long.class);
    } catch (ClassNotFoundException e) {
      throw new CacheException(
          LocalizedStrings.MEMSCALE_JVM_INCOMPATIBLE_WITH_OFF_HEAP.toLocalizedString("product"),
          e) {
      };
    } catch (NoSuchMethodException e) {
      throw new CacheException(
          LocalizedStrings.MEMSCALE_JVM_INCOMPATIBLE_WITH_OFF_HEAP.toLocalizedString("product"),
          e) {
      };
    }
  }

  /**
   * Constructs a MemoryAllocator for off-heap storage.
   * @return MemoryAllocator for off-heap storage
   */
  public static MemoryAllocator createOffHeapStorage(StatisticsFactory sf, long offHeapMemorySize,
                                                     DistributedSystem system) {
    if (offHeapMemorySize == 0 || Boolean.getBoolean(InternalLocator.FORCE_LOCATOR_DM_TYPE)) {
      // Checking the FORCE_LOCATOR_DM_TYPE is a quick hack to keep our locator from allocating off
      // heap memory.
      return null;
    }

    if (offHeapMemorySize < MIN_SLAB_SIZE) {
      throw new IllegalArgumentException("The amount of off heap memory must be at least "
          + MIN_SLAB_SIZE + " but it was set to " + offHeapMemorySize);
    }

    // Ensure that using off-heap will work with this JVM.
    validateVmCompatibility();

    if (system == null) {
      throw new IllegalArgumentException("InternalDistributedSystem is null");
    }
    // ooohml provides the hook for disconnecting and closing cache on OutOfOffHeapMemoryException
    OutOfOffHeapMemoryListener ooohml =
        new DisconnectingOutOfOffHeapMemoryListener((InternalDistributedSystem) system);
    return basicCreateOffHeapStorage(sf, offHeapMemorySize, ooohml);
  }

  static MemoryAllocator basicCreateOffHeapStorage(StatisticsFactory statisticsFactory, long offHeapMemorySize,
                                                   OutOfOffHeapMemoryListener ooohml) {
    final OffHeapStorageStats stats = StatsFactory.createOffHeapStorageStatsImpl(statisticsFactory,"offHeapStorageStats");

    // determine off-heap and slab sizes
    final long maxSlabSize = calcMaxSlabSize(offHeapMemorySize);

    final int slabCount = calcSlabCount(maxSlabSize, offHeapMemorySize);

    return MemoryAllocatorImpl.create(ooohml, stats, slabCount, offHeapMemorySize, maxSlabSize);
  }

  private static final long MAX_SLAB_SIZE = Integer.MAX_VALUE;
  static final long MIN_SLAB_SIZE = 1024;

  // non-private for unit test access
  static int calcSlabCount(long maxSlabSize, long offHeapMemorySize) {
    long result = offHeapMemorySize / maxSlabSize;
    if ((offHeapMemorySize % maxSlabSize) >= MIN_SLAB_SIZE) {
      result++;
    }
    if (result > Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
          "The number of slabs of off heap memory exceeded the limit of " + Integer.MAX_VALUE
              + ". Decrease the amount of off heap memory or increase the maximum slab size using gemfire.OFF_HEAP_SLAB_SIZE.");
    }
    return (int) result;
  }

  private static long parseLongWithUnits(String v, long defaultValue, int defaultMultiplier) {
    if (v == null || v.equals("")) {
      return defaultValue;
    }
    int unitMultiplier = defaultMultiplier;
    if (v.toLowerCase().endsWith("g")) {
      unitMultiplier = 1024 * 1024 * 1024;
      v = v.substring(0, v.length() - 1);
    } else if (v.toLowerCase().endsWith("m")) {
      unitMultiplier = 1024 * 1024;
      v = v.substring(0, v.length() - 1);
    }
    try {
      long result = Long.parseLong(v);
      result *= unitMultiplier;
      return result;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Memory size must be specified as <n>[g|m], where <n> is the size and [g|m] specifies the units in gigabytes or megabytes.");
    }
  }


}
