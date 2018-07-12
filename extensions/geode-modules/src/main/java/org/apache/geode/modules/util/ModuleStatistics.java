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
package org.apache.geode.modules.util;

import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.distributed.DistributedSystem;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * Statistics for modules.
 *
 */
public class ModuleStatistics {

  private StatisticsType type;

  private int cacheHitsId;

  private int cacheMissesId;

  private int hibernateEntityDestroyJobsScheduledId;

  private void initializeStats(StatisticsFactory factory) {
    type = factory.createType("pluginStats", "statistics for hibernate plugin and hibernate L2 cache",
        new StatisticDescriptor[] {
            factory.createLongCounter("cacheHits", "number of times an entity was found in L2 cache",
                "count"),
            factory.createLongCounter("cacheMisses",
                "number of times an entity was NOT found in l2 cache", "count"),
            factory.createLongCounter("hibernateEntityDestroyJobsScheduled",
                "number of entities scheduled for destroy because of version conflict with a remote member",
                "jobs")});

    cacheHitsId = type.nameToId("cacheHits");
    cacheMissesId = type.nameToId("cacheMisses");
    hibernateEntityDestroyJobsScheduledId = type.nameToId("hibernateEntityDestroyJobsScheduled");
  }

  private final Statistics stats;

  private static ModuleStatistics instance;

  private ModuleStatistics(StatisticsFactory factory) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, "PluginStatistics");
  }

  public static ModuleStatistics getInstance(DistributedSystem system) {
    synchronized (ModuleStatistics.class) {
      if (instance == null) {
        instance = new ModuleStatistics(system.getStatisticsFactory());
      }
    }
    return instance;
  }

  public void incCacheHit() {
    stats.incLong(cacheHitsId, 1);
  }

  public long getCacheHits() {
    return stats.getLong(cacheHitsId);
  }

  public void incCacheMiss() {
    stats.incLong(cacheMissesId, 1);
  }

  public long getCacheMiss() {
    return stats.getLong(cacheMissesId);
  }

  public void incHibernateDestroyJobsScheduled() {
    stats.incLong(hibernateEntityDestroyJobsScheduledId, 1);
  }
}
