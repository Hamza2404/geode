package org.apache.geode.internal.cache.control;

import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.distributed.internal.QueueStatHelper;

public interface ResourceManagerStats {
  void close();

  long startRebalance();

  void incAutoRebalanceAttempts();

  void endRebalance(long start);

  void startBucketCreate(int regions);

  void endBucketCreate(int regions, boolean success, long bytes, long elapsed);

  void startBucketRemove(int regions);

  void endBucketRemove(int regions, boolean success, long bytes, long elapsed);

  void startBucketTransfer(int regions);

  void endBucketTransfer(int regions, boolean success, long bytes, long elapsed);

  void startPrimaryTransfer(int regions);

  void endPrimaryTransfer(int regions, boolean success, long elapsed);

  void incRebalanceMembershipChanges(int delta);

  int getRebalanceMembershipChanges();

  int getRebalancesInProgress();

  int getRebalancesCompleted();

  int getAutoRebalanceAttempts();

  long getRebalanceTime();

  int getRebalanceBucketCreatesInProgress();

  int getRebalanceBucketCreatesCompleted();

  int getRebalanceBucketCreatesFailed();

  long getRebalanceBucketCreateTime();

  long getRebalanceBucketCreateBytes();

  int getRebalanceBucketTransfersInProgress();

  int getRebalanceBucketTransfersCompleted();

  int getRebalanceBucketTransfersFailed();

  long getRebalanceBucketTransfersTime();

  long getRebalanceBucketTransfersBytes();

  int getRebalancePrimaryTransfersInProgress();

  int getRebalancePrimaryTransfersCompleted();

  int getRebalancePrimaryTransfersFailed();

  long getRebalancePrimaryTransferTime();

  void incResourceEventsDelivered();

  int getResourceEventsDelivered();

  void incHeapCriticalEvents();

  int getHeapCriticalEvents();

  void incOffHeapCriticalEvents();

  int getOffHeapCriticalEvents();

  void incHeapSafeEvents();

  int getHeapSafeEvents();

  void incOffHeapSafeEvents();

  int getOffHeapSafeEvents();

  void incEvictionStartEvents();

  int getEvictionStartEvents();

  void incOffHeapEvictionStartEvents();

  int getOffHeapEvictionStartEvents();

  void incEvictionStopEvents();

  int getEvictionStopEvents();

  void incOffHeapEvictionStopEvents();

  int getOffHeapEvictionStopEvents();

  void changeCriticalThreshold(long newValue);

  long getCriticalThreshold();

  void changeOffHeapCriticalThreshold(long newValue);

  long getOffHeapCriticalThreshold();

  void changeEvictionThreshold(long newValue);

  long getEvictionThreshold();

  void changeOffHeapEvictionThreshold(long newValue);

  long getOffHeapEvictionThreshold();

  void changeTenuredHeapUsed(long newValue);

  long getTenuredHeapUsed();

  void incResourceEventQueueSize(int delta);

  int getResourceEventQueueSize();

  void incThresholdEventProcessorThreadJobs(int delta);

  int getThresholdEventProcessorThreadJobs();

  QueueStatHelper getResourceEventQueueStatHelper();

  PoolStatHelper getResourceEventPoolStatHelper();

  int getNumThreadStuck();

  void setNumThreadStuck(int value);
}
