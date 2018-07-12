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
package org.apache.geode.internal.cache.control;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.distributed.internal.QueueStatHelper;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * Contains methods for manipulating resource manager statistics.
 */
public class ResourceManagerStatsImpl implements ResourceManagerStats, GFSStatsImplementor {
  // static fields
  private StatisticsType type;

  private int rebalancesInProgressId;
  private int rebalancesCompletedId;
  private int autoRebalanceAttemptsId;
  private int rebalanceTimeId;
  private int rebalanceBucketCreatesInProgressId;
  private int rebalanceBucketCreatesCompletedId;
  private int rebalanceBucketCreatesFailedId;
  private int rebalanceBucketCreateTimeId;
  private int rebalanceBucketCreateBytesId;
  private int rebalanceBucketRemovesInProgressId;
  private int rebalanceBucketRemovesCompletedId;
  private int rebalanceBucketRemovesFailedId;
  private int rebalanceBucketRemovesTimeId;
  private int rebalanceBucketRemovesBytesId;
  private int rebalanceBucketTransfersInProgressId;
  private int rebalanceBucketTransfersCompletedId;
  private int rebalanceBucketTransfersFailedId;
  private int rebalanceBucketTransfersTimeId;
  private int rebalanceBucketTransfersBytesId;
  private int rebalancePrimaryTransfersInProgressId;
  private int rebalancePrimaryTransfersCompletedId;
  private int rebalancePrimaryTransfersFailedId;
  private int rebalancePrimaryTransferTimeId;
  private int rebalanceMembershipChanges;
  private int heapCriticalEventsId;
  private int offHeapCriticalEventsId;
  private int heapSafeEventsId;
  private int offHeapSafeEventsId;
  private int evictionStartEventsId;
  private int offHeapEvictionStartEventsId;
  private int evictionStopEventsId;
  private int offHeapEvictionStopEventsId;
  private int criticalThresholdId;
  private int offHeapCriticalThresholdId;
  private int evictionThresholdId;
  private int offHeapEvictionThresholdId;
  private int tenuredHeapUsageId;
  private int resourceEventsDeliveredId;
  private int resourceEventQueueSizeId;
  private int thresholdEventProcessorThreadJobsId;
  private int numThreadsStuckId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    type = factory.createType("ResourceManagerStats", "Statistics about resource management",
        new StatisticDescriptor[]{
            factory.createIntGauge("rebalancesInProgress",
                "Current number of cache rebalance operations being directed by this process.",
                "operations"),
            factory.createIntCounter("rebalancesCompleted",
                "Total number of cache rebalance operations directed by this process.",
                "operations"),
            factory.createIntCounter("autoRebalanceAttempts",
                "Total number of cache auto-rebalance attempts.", "operations"),
            factory.createLongCounter("rebalanceTime",
                "Total time spent directing cache rebalance operations.", "nanoseconds", false),

            factory.createIntGauge("rebalanceBucketCreatesInProgress",
                "Current number of bucket create operations being directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketCreatesCompleted",
                "Total number of bucket create operations directed for rebalancing.", "operations"),
            factory.createIntCounter("rebalanceBucketCreatesFailed",
                "Total number of bucket create operations directed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalanceBucketCreateTime",
                "Total time spent directing bucket create operations for rebalancing.",
                "nanoseconds", false),
            factory.createLongCounter("rebalanceBucketCreateBytes",
                "Total bytes created while directing bucket create operations for rebalancing.",
                "bytes", false),

            factory.createIntGauge("rebalanceBucketRemovesInProgress",
                "Current number of bucket remove operations being directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketRemovesCompleted",
                "Total number of bucket remove operations directed for rebalancing.", "operations"),
            factory.createIntCounter("rebalanceBucketRemovesFailed",
                "Total number of bucket remove operations directed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalanceBucketRemovesTime",
                "Total time spent directing bucket remove operations for rebalancing.",
                "nanoseconds", false),
            factory.createLongCounter("rebalanceBucketRemovesBytes",
                "Total bytes removed while directing bucket remove operations for rebalancing.",
                "bytes", false),

            factory.createIntGauge("rebalanceBucketTransfersInProgress",
                "Current number of bucket transfer operations being directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketTransfersCompleted",
                "Total number of bucket transfer operations directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketTransfersFailed",
                "Total number of bucket transfer operations directed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalanceBucketTransfersTime",
                "Total time spent directing bucket transfer operations for rebalancing.",
                "nanoseconds", false),
            factory.createLongCounter("rebalanceBucketTransfersBytes",
                "Total bytes transfered while directing bucket transfer operations for rebalancing.",
                "bytes", false),

            factory.createIntGauge("rebalancePrimaryTransfersInProgress",
                "Current number of primary transfer operations being directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalancePrimaryTransfersCompleted",
                "Total number of primary transfer operations directed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalancePrimaryTransfersFailed",
                "Total number of primary transfer operations directed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalancePrimaryTransferTime",
                "Total time spent directing primary transfer operations for rebalancing.",
                "nanoseconds", false),
            factory.createIntCounter("rebalanceMembershipChanges",
                "The number of times that membership has changed during a rebalance", "events"),

            factory.createIntGauge("heapCriticalEvents",
                "Total number of times the heap usage went over critical threshold.", "events"),
            factory.createIntGauge("offHeapCriticalEvents",
                "Total number of times off-heap usage went over critical threshold.", "events"),
            factory.createIntGauge("heapSafeEvents",
                "Total number of times the heap usage fell below critical threshold.", "events"),
            factory.createIntGauge("offHeapSafeEvents",
                "Total number of times off-heap usage fell below critical threshold.", "events"),
            factory.createIntGauge("evictionStartEvents",
                "Total number of times heap usage went over eviction threshold.", "events"),
            factory.createIntGauge("offHeapEvictionStartEvents",
                "Total number of times off-heap usage went over eviction threshold.", "events"),
            factory.createIntGauge("evictionStopEvents",
                "Total number of times heap usage fell below eviction threshold.", "events"),
            factory.createIntGauge("offHeapEvictionStopEvents",
                "Total number of times off-heap usage fell below eviction threshold.", "events"),
            factory.createLongGauge("criticalThreshold",
                "The currently set heap critical threshold value in bytes", "bytes"),
            factory.createLongGauge("offHeapCriticalThreshold",
                "The currently set off-heap critical threshold value in bytes", "bytes"),
            factory.createLongGauge("evictionThreshold",
                "The currently set heap eviction threshold value in bytes", "bytes"),
            factory.createLongGauge("offHeapEvictionThreshold",
                "The currently set off-heap eviction threshold value in bytes", "bytes"),
            factory.createLongGauge("tenuredHeapUsed", "Total memory used in the tenured/old space",
                "bytes"),
            factory.createIntCounter("resourceEventsDelivered",
                "Total number of resource events delivered to listeners", "events"),
            factory.createIntGauge("resourceEventQueueSize",
                "Pending events for thresholdEventProcessor thread", "events"),
            factory.createIntGauge("thresholdEventProcessorThreadJobs",
                "Number of jobs currently being processed by the thresholdEventProcessorThread",
                "jobs"),
            factory.createIntGauge("numThreadsStuck",
                "Number of running threads that have not changed state within the thread-monitor-time-limit-ms interval.",
                "stuck Threads")});

    rebalancesInProgressId = type.nameToId("rebalancesInProgress");
    rebalancesCompletedId = type.nameToId("rebalancesCompleted");
    autoRebalanceAttemptsId = type.nameToId("autoRebalanceAttempts");
    rebalanceTimeId = type.nameToId("rebalanceTime");
    rebalanceBucketCreatesInProgressId = type.nameToId("rebalanceBucketCreatesInProgress");
    rebalanceBucketCreatesCompletedId = type.nameToId("rebalanceBucketCreatesCompleted");
    rebalanceBucketCreatesFailedId = type.nameToId("rebalanceBucketCreatesFailed");
    rebalanceBucketCreateTimeId = type.nameToId("rebalanceBucketCreateTime");
    rebalanceBucketCreateBytesId = type.nameToId("rebalanceBucketCreateBytes");
    rebalanceBucketRemovesInProgressId = type.nameToId("rebalanceBucketRemovesInProgress");
    rebalanceBucketRemovesCompletedId = type.nameToId("rebalanceBucketRemovesCompleted");
    rebalanceBucketRemovesFailedId = type.nameToId("rebalanceBucketRemovesFailed");
    rebalanceBucketRemovesTimeId = type.nameToId("rebalanceBucketRemovesTime");
    rebalanceBucketRemovesBytesId = type.nameToId("rebalanceBucketRemovesBytes");
    rebalanceBucketTransfersInProgressId = type.nameToId("rebalanceBucketTransfersInProgress");
    rebalanceBucketTransfersCompletedId = type.nameToId("rebalanceBucketTransfersCompleted");
    rebalanceBucketTransfersFailedId = type.nameToId("rebalanceBucketTransfersFailed");
    rebalanceBucketTransfersTimeId = type.nameToId("rebalanceBucketTransfersTime");
    rebalanceBucketTransfersBytesId = type.nameToId("rebalanceBucketTransfersBytes");
    rebalancePrimaryTransfersInProgressId = type.nameToId("rebalancePrimaryTransfersInProgress");
    rebalancePrimaryTransfersCompletedId = type.nameToId("rebalancePrimaryTransfersCompleted");
    rebalancePrimaryTransfersFailedId = type.nameToId("rebalancePrimaryTransfersFailed");
    rebalancePrimaryTransferTimeId = type.nameToId("rebalancePrimaryTransferTime");
    rebalanceMembershipChanges = type.nameToId("rebalanceMembershipChanges");
    heapCriticalEventsId = type.nameToId("heapCriticalEvents");
    offHeapCriticalEventsId = type.nameToId("offHeapCriticalEvents");
    heapSafeEventsId = type.nameToId("heapSafeEvents");
    offHeapSafeEventsId = type.nameToId("offHeapSafeEvents");
    evictionStartEventsId = type.nameToId("evictionStartEvents");
    offHeapEvictionStartEventsId = type.nameToId("offHeapEvictionStartEvents");
    evictionStopEventsId = type.nameToId("evictionStopEvents");
    offHeapEvictionStopEventsId = type.nameToId("offHeapEvictionStopEvents");
    criticalThresholdId = type.nameToId("criticalThreshold");
    offHeapCriticalThresholdId = type.nameToId("offHeapCriticalThreshold");
    evictionThresholdId = type.nameToId("evictionThreshold");
    offHeapEvictionThresholdId = type.nameToId("offHeapEvictionThreshold");
    tenuredHeapUsageId = type.nameToId("tenuredHeapUsed");
    resourceEventsDeliveredId = type.nameToId("resourceEventsDelivered");
    resourceEventQueueSizeId = type.nameToId("resourceEventQueueSize");
    thresholdEventProcessorThreadJobsId = type.nameToId("thresholdEventProcessorThreadJobs");
    numThreadsStuckId = type.nameToId("numThreadsStuck");
  }

  private final Statistics stats;

  public ResourceManagerStatsImpl(StatisticsFactory factory) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, "ResourceManagerStats");
  }

  @Override
  public void close() {
    this.stats.close();
  }

  @Override
  public long startRebalance() {
    this.stats.incInt(rebalancesInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void incAutoRebalanceAttempts() {
    this.stats.incInt(autoRebalanceAttemptsId, 1);
  }

  @Override
  public void endRebalance(long start) {
    long elapsed = System.nanoTime() - start;
    this.stats.incInt(rebalancesInProgressId, -1);
    this.stats.incInt(rebalancesCompletedId, 1);
    this.stats.incLong(rebalanceTimeId, elapsed);
  }

  @Override
  public void startBucketCreate(int regions) {
    this.stats.incInt(rebalanceBucketCreatesInProgressId, regions);
  }

  @Override
  public void endBucketCreate(int regions, boolean success, long bytes, long elapsed) {
    this.stats.incInt(rebalanceBucketCreatesInProgressId, -regions);
    this.stats.incLong(rebalanceBucketCreateTimeId, elapsed);
    if (success) {
      this.stats.incInt(rebalanceBucketCreatesCompletedId, regions);
      this.stats.incLong(rebalanceBucketCreateBytesId, bytes);
    } else {
      this.stats.incInt(rebalanceBucketCreatesFailedId, regions);
    }
  }

  @Override
  public void startBucketRemove(int regions) {
    this.stats.incInt(rebalanceBucketRemovesInProgressId, regions);
  }

  @Override
  public void endBucketRemove(int regions, boolean success, long bytes, long elapsed) {
    this.stats.incInt(rebalanceBucketRemovesInProgressId, -regions);
    this.stats.incLong(rebalanceBucketRemovesTimeId, elapsed);
    if (success) {
      this.stats.incInt(rebalanceBucketRemovesCompletedId, regions);
      this.stats.incLong(rebalanceBucketRemovesBytesId, bytes);
    } else {
      this.stats.incInt(rebalanceBucketRemovesFailedId, regions);
    }
  }

  @Override
  public void startBucketTransfer(int regions) {
    this.stats.incInt(rebalanceBucketTransfersInProgressId, regions);
  }

  @Override
  public void endBucketTransfer(int regions, boolean success, long bytes, long elapsed) {
    this.stats.incInt(rebalanceBucketTransfersInProgressId, -regions);
    this.stats.incLong(rebalanceBucketTransfersTimeId, elapsed);
    if (success) {
      this.stats.incInt(rebalanceBucketTransfersCompletedId, regions);
      this.stats.incLong(rebalanceBucketTransfersBytesId, bytes);
    } else {
      this.stats.incInt(rebalanceBucketTransfersFailedId, regions);
    }
  }

  @Override
  public void startPrimaryTransfer(int regions) {
    this.stats.incInt(rebalancePrimaryTransfersInProgressId, regions);
  }

  @Override
  public void endPrimaryTransfer(int regions, boolean success, long elapsed) {
    this.stats.incInt(rebalancePrimaryTransfersInProgressId, -regions);
    this.stats.incLong(rebalancePrimaryTransferTimeId, elapsed);
    if (success) {
      this.stats.incInt(rebalancePrimaryTransfersCompletedId, regions);
    } else {
      this.stats.incInt(rebalancePrimaryTransfersFailedId, regions);
    }
  }

  @Override
  public void incRebalanceMembershipChanges(int delta) {
    this.stats.incInt(rebalanceMembershipChanges, 1);
  }

  @Override
  public int getRebalanceMembershipChanges() {
    return this.stats.getInt(rebalanceMembershipChanges);
  }

  @Override
  public int getRebalancesInProgress() {
    return this.stats.getInt(rebalancesInProgressId);
  }

  @Override
  public int getRebalancesCompleted() {
    return this.stats.getInt(rebalancesCompletedId);
  }

  @Override
  public int getAutoRebalanceAttempts() {
    return this.stats.getInt(autoRebalanceAttemptsId);
  }

  @Override
  public long getRebalanceTime() {
    return this.stats.getLong(rebalanceTimeId);
  }

  @Override
  public int getRebalanceBucketCreatesInProgress() {
    return this.stats.getInt(rebalanceBucketCreatesInProgressId);
  }

  @Override
  public int getRebalanceBucketCreatesCompleted() {
    return this.stats.getInt(rebalanceBucketCreatesCompletedId);
  }

  @Override
  public int getRebalanceBucketCreatesFailed() {
    return this.stats.getInt(rebalanceBucketCreatesFailedId);
  }

  @Override
  public long getRebalanceBucketCreateTime() {
    return this.stats.getLong(rebalanceBucketCreateTimeId);
  }

  @Override
  public long getRebalanceBucketCreateBytes() {
    return this.stats.getLong(rebalanceBucketCreateBytesId);
  }

  @Override
  public int getRebalanceBucketTransfersInProgress() {
    return this.stats.getInt(rebalanceBucketTransfersInProgressId);
  }

  @Override
  public int getRebalanceBucketTransfersCompleted() {
    return this.stats.getInt(rebalanceBucketTransfersCompletedId);
  }

  @Override
  public int getRebalanceBucketTransfersFailed() {
    return this.stats.getInt(rebalanceBucketTransfersFailedId);
  }

  @Override
  public long getRebalanceBucketTransfersTime() {
    return this.stats.getLong(rebalanceBucketTransfersTimeId);
  }

  @Override
  public long getRebalanceBucketTransfersBytes() {
    return this.stats.getLong(rebalanceBucketTransfersBytesId);
  }

  @Override
  public int getRebalancePrimaryTransfersInProgress() {
    return this.stats.getInt(rebalancePrimaryTransfersInProgressId);
  }

  @Override
  public int getRebalancePrimaryTransfersCompleted() {
    return this.stats.getInt(rebalancePrimaryTransfersCompletedId);
  }

  @Override
  public int getRebalancePrimaryTransfersFailed() {
    return this.stats.getInt(rebalancePrimaryTransfersFailedId);
  }

  @Override
  public long getRebalancePrimaryTransferTime() {
    return this.stats.getLong(rebalancePrimaryTransferTimeId);
  }

  @Override
  public void incResourceEventsDelivered() {
    this.stats.incInt(resourceEventsDeliveredId, 1);
  }

  @Override
  public int getResourceEventsDelivered() {
    return this.stats.getInt(resourceEventsDeliveredId);
  }

  @Override
  public void incHeapCriticalEvents() {
    this.stats.incInt(heapCriticalEventsId, 1);
  }

  @Override
  public int getHeapCriticalEvents() {
    return this.stats.getInt(heapCriticalEventsId);
  }

  @Override
  public void incOffHeapCriticalEvents() {
    this.stats.incInt(offHeapCriticalEventsId, 1);
  }

  @Override
  public int getOffHeapCriticalEvents() {
    return this.stats.getInt(offHeapCriticalEventsId);
  }

  @Override
  public void incHeapSafeEvents() {
    this.stats.incInt(heapSafeEventsId, 1);
  }

  @Override
  public int getHeapSafeEvents() {
    return this.stats.getInt(heapSafeEventsId);
  }

  @Override
  public void incOffHeapSafeEvents() {
    this.stats.incInt(offHeapSafeEventsId, 1);
  }

  @Override
  public int getOffHeapSafeEvents() {
    return this.stats.getInt(offHeapSafeEventsId);
  }

  @Override
  public void incEvictionStartEvents() {
    this.stats.incInt(evictionStartEventsId, 1);
  }

  @Override
  public int getEvictionStartEvents() {
    return this.stats.getInt(evictionStartEventsId);
  }

  @Override
  public void incOffHeapEvictionStartEvents() {
    this.stats.incInt(offHeapEvictionStartEventsId, 1);
  }

  @Override
  public int getOffHeapEvictionStartEvents() {
    return this.stats.getInt(offHeapEvictionStartEventsId);
  }

  @Override
  public void incEvictionStopEvents() {
    this.stats.incInt(evictionStopEventsId, 1);
  }

  @Override
  public int getEvictionStopEvents() {
    return this.stats.getInt(evictionStopEventsId);
  }

  @Override
  public void incOffHeapEvictionStopEvents() {
    this.stats.incInt(offHeapEvictionStopEventsId, 1);
  }

  @Override
  public int getOffHeapEvictionStopEvents() {
    return this.stats.getInt(offHeapEvictionStopEventsId);
  }

  @Override
  public void changeCriticalThreshold(long newValue) {
    this.stats.setLong(criticalThresholdId, newValue);
  }

  @Override
  public long getCriticalThreshold() {
    return this.stats.getLong(criticalThresholdId);
  }

  @Override
  public void changeOffHeapCriticalThreshold(long newValue) {
    this.stats.setLong(offHeapCriticalThresholdId, newValue);
  }

  @Override
  public long getOffHeapCriticalThreshold() {
    return this.stats.getLong(offHeapCriticalThresholdId);
  }

  @Override
  public void changeEvictionThreshold(long newValue) {
    this.stats.setLong(evictionThresholdId, newValue);
  }

  @Override
  public long getEvictionThreshold() {
    return this.stats.getLong(evictionThresholdId);
  }

  @Override
  public void changeOffHeapEvictionThreshold(long newValue) {
    this.stats.setLong(offHeapEvictionThresholdId, newValue);
  }

  @Override
  public long getOffHeapEvictionThreshold() {
    return this.stats.getLong(offHeapEvictionThresholdId);
  }

  @Override
  public void changeTenuredHeapUsed(long newValue) {
    this.stats.setLong(tenuredHeapUsageId, newValue);
  }

  @Override
  public long getTenuredHeapUsed() {
    return this.stats.getLong(tenuredHeapUsageId);
  }

  @Override
  public void incResourceEventQueueSize(int delta) {
    this.stats.incInt(resourceEventQueueSizeId, delta);
  }

  @Override
  public int getResourceEventQueueSize() {
    return this.stats.getInt(resourceEventQueueSizeId);
  }

  @Override
  public void incThresholdEventProcessorThreadJobs(int delta) {
    this.stats.incInt(thresholdEventProcessorThreadJobsId, delta);
  }

  @Override
  public int getThresholdEventProcessorThreadJobs() {
    return this.stats.getInt(thresholdEventProcessorThreadJobsId);
  }

  /**
   * @return a {@link QueueStatHelper} so that we can record number of events in the
   * thresholdEventProcessor queue.
   */
  @Override
  public QueueStatHelper getResourceEventQueueStatHelper() {
    return new QueueStatHelper() {
      @Override
      public void add() {
        incResourceEventQueueSize(1);
      }

      @Override
      public void remove() {
        incResourceEventQueueSize(-1);
      }

      @Override
      public void remove(int count) {
        incResourceEventQueueSize(-1 * count);
      }
    };
  }

  @Override
  public PoolStatHelper getResourceEventPoolStatHelper() {
    return new PoolStatHelper() {
      @Override
      public void endJob() {
        incThresholdEventProcessorThreadJobs(-1);
      }

      @Override
      public void startJob() {
        incThresholdEventProcessorThreadJobs(1);
      }
    };
  }

  /**
   * Returns the value of ThreadStuck (how many (if at all) stuck threads are in the system)
   */
  @Override
  public int getNumThreadStuck() {
    return this.stats.getInt(numThreadsStuckId);
  }

  /**
   * Sets the value of Thread Stuck
   */
  @Override
  public void setNumThreadStuck(int value) {
    this.stats.setInt(numThreadsStuckId, value);
  }
}
