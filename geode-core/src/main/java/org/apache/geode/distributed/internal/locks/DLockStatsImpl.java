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

package org.apache.geode.distributed.internal.locks;

import org.apache.geode.distributed.internal.DistributionStatsImpl;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.distributed.internal.QueueStatHelper;

/**
 * This class maintains statistics in GemFire about the distributed lock service.
 *
 *
 */
public class DLockStatsImpl implements DistributedLockStats {

  // -------------------------------------------------------------------------
  // Statistic "Id" Fields
  // -------------------------------------------------------------------------

  private StatisticsType type;

  private int grantorsId;
  private int servicesId;
  private int tokensId;
  private int grantTokensId;
  private int requestQueuesId;
  private int serialQueueSizeId;
  private int serialThreadsId;
  private int waitingQueueSizeId;
  private int waitingThreadsId;
  private int lockWaitsInProgressId;
  private int lockWaitsCompletedId;
  private int lockWaitTimeId;
  private int lockWaitsFailedId;
  private int lockWaitFailedTimeId;
  private int grantWaitsInProgressId;
  private int grantWaitsCompletedId;
  private int grantWaitTimeId;
  private int grantWaitsNotGrantorId;
  private int grantWaitNotGrantorTimeId;
  private int grantWaitsTimeoutId;
  private int grantWaitTimeoutTimeId;
  private int grantWaitsNotHolderId;
  private int grantWaitNotHolderTimeId;
  private int grantWaitsFailedId;
  private int grantWaitFailedTimeId;
  private int grantWaitsSuspendedId;
  private int grantWaitSuspendedTimeId;
  private int grantWaitsDestroyedId;
  private int grantWaitDestroyedTimeId;
  private int createGrantorsInProgressId;
  private int createGrantorsCompletedId;
  private int createGrantorTimeId;
  private int serviceCreatesInProgressId;
  private int serviceCreatesCompletedId;
  private int serviceCreateLatchTimeId;
  private int serviceInitLatchTimeId;
  private int grantorWaitsInProgressId;
  private int grantorWaitsCompletedId;
  private int grantorWaitTimeId;
  private int grantorWaitsFailedId;
  private int grantorWaitFailedTimeId;
  private int grantorThreadsInProgressId;
  private int grantorThreadsCompletedId;
  private int grantorThreadExpireAndGrantLocksTimeId;
  private int grantorThreadHandleRequestTimeoutsTimeId;
  private int grantorThreadRemoveUnusedTokensTimeId;
  private int grantorThreadTimeId;
  private int pendingRequestsId;
  private int destroyReadWaitsInProgressId;
  private int destroyReadWaitsCompletedId;
  private int destroyReadWaitTimeId;
  private int destroyReadWaitsFailedId;
  private int destroyReadWaitFailedTimeId;
  private int destroyWriteWaitsInProgressId;
  private int destroyWriteWaitsCompletedId;
  private int destroyWriteWaitTimeId;
  private int destroyWriteWaitsFailedId;
  private int destroyWriteWaitFailedTimeId;
  private int destroyReadsId;
  private int destroyWritesId;
  private int lockReleasesInProgressId;
  private int lockReleasesCompletedId;
  private int lockReleaseTimeId;
  private int becomeGrantorRequestsId;
  private int freeResourcesCompletedId;
  private int freeResourcesFailedId;

  /** returns the current nano time, if time stats are enabled */
  static long getStatTime() {
    return System.nanoTime();
  }

  private void initializeStats(StatisticsFactory factory) {
    String statName = "DLockStats";
    String statDescription = "Statistics on the gemfire distribution lock service.";

    final String grantorsDesc = "The current number of lock grantors hosted by this system member.";
    final String servicesDesc = "The current number of lock services used by this system member.";
    final String tokensDesc = "The current number of lock tokens used by this system member.";
    final String grantTokensDesc = "The current number of grant tokens used by local grantors.";
    final String requestQueuesDesc =
        "The current number of lock request queues used by this system member.";
    final String serialQueueSizeDesc =
        "The number of serial distribution messages currently waiting to be processed.";
    final String serialThreadsDesc =
        "The number of threads currently processing serial/ordered messages.";
    final String waitingQueueSizeDesc =
        "The number of distribution messages currently waiting for some other resource before they can be processed.";
    final String waitingThreadsDesc =
        "The number of threads currently processing messages that had to wait for a resource.";
    final String lockWaitsInProgressDesc =
        "Current number of threads waiting for a distributed lock.";
    final String lockWaitsCompletedDesc =
        "Total number of times distributed lock wait has completed by successfully obtained the lock.";
    final String lockWaitTimeDesc =
        "Total time spent waiting for a distributed lock that was obtained.";
    final String lockWaitsFailedDesc =
        "Total number of times distributed lock wait has completed by failing to obtain the lock.";
    final String lockWaitFailedTimeDesc =
        "Total time spent waiting for a distributed lock that we failed to obtain.";
    final String grantWaitsInProgressDesc =
        "Current number of distributed lock requests being granted.";
    final String grantWaitsCompletedDesc =
        "Total number of times granting of a lock request has completed by successfully granting the lock.";
    final String grantWaitTimeDesc = "Total time spent attempting to grant a distributed lock.";
    final String grantWaitsNotGrantorDesc =
        "Total number of times granting of lock request failed because not grantor.";
    final String grantWaitNotGrantorTimeDesc =
        "Total time spent granting of lock requests that failed because not grantor.";
    final String grantWaitsTimeoutDesc =
        "Total number of times granting of lock request failed because timeout.";
    final String grantWaitTimeoutTimeDesc =
        "Total time spent granting of lock requests that failed because timeout.";
    final String grantWaitsNotHolderDesc =
        "Total number of times granting of lock request failed because reentrant was not holder.";
    final String grantWaitNotHolderTimeDesc =
        "Total time spent granting of lock requests that failed because reentrant was not holder.";
    final String grantWaitsFailedDesc =
        "Total number of times granting of lock request failed because try locks failed.";
    final String grantWaitFailedTimeDesc =
        "Total time spent granting of lock requests that failed because try locks failed.";
    final String grantWaitsSuspendedDesc =
        "Total number of times granting of lock request failed because lock service was suspended.";
    final String grantWaitSuspendedTimeDesc =
        "Total time spent granting of lock requests that failed because lock service was suspended.";
    final String grantWaitsDestroyedDesc =
        "Total number of times granting of lock request failed because lock service was destroyed.";
    final String grantWaitDestroyedTimeDesc =
        "Total time spent granting of lock requests that failed because lock service was destroyed.";
    final String createGrantorsInProgressDesc =
        "Current number of initial grantors being created in this process.";
    final String createGrantorsCompletedDesc =
        "Total number of initial grantors created in this process.";
    final String createGrantorTimeDesc =
        "Total time spent waiting create the intial grantor for lock services.";
    final String serviceCreatesInProgressDesc =
        "Current number of lock services being created in this process.";
    final String serviceCreatesCompletedDesc =
        "Total number of lock services created in this process.";
    final String serviceCreateLatchTimeDesc =
        "Total time spent creating lock services before releasing create latches.";
    final String serviceInitLatchTimeDesc =
        "Total time spent creating lock services before releasing init latches.";
    final String grantorWaitsInProgressDesc =
        "Current number of threads waiting for grantor latch to open.";
    final String grantorWaitsCompletedDesc =
        "Total number of times waiting threads completed waiting for the grantor latch to open.";
    final String grantorWaitTimeDesc =
        "Total time spent waiting for the grantor latch which resulted in success.";
    final String grantorWaitsFailedDesc =
        "Total number of times waiting threads failed to finish waiting for the grantor latch to open.";
    final String grantorWaitFailedTimeDesc =
        "Total time spent waiting for the grantor latch which resulted in failure.";
    final String grantorThreadsInProgressDesc =
        "Current iterations of work performed by grantor thread(s).";
    final String grantorThreadsCompletedDesc =
        "Total number of iterations of work performed by grantor thread(s).";
    final String grantorThreadExpireAndGrantLocksTimeDesc =
        "Total time spent by grantor thread(s) performing expireAndGrantLocks tasks.";
    final String grantorThreadHandleRequestTimeoutsTimeDesc =
        "Total time spent by grantor thread(s) performing handleRequestTimeouts tasks.";
    final String grantorThreadRemoveUnusedTokensTimeDesc =
        "Total time spent by grantor thread(s) performing removeUnusedTokens tasks.";
    final String grantorThreadTimeDesc =
        "Total time spent by grantor thread(s) performing all grantor tasks.";
    final String pendingRequestsDesc =
        "The current number of pending lock requests queued by grantors in this process.";
    final String destroyReadWaitsInProgressDesc =
        "Current number of threads waiting for a DLockService destroy read lock.";
    final String destroyReadWaitsCompletedDesc =
        "Total number of times a DLockService destroy read lock wait has completed successfully.";
    final String destroyReadWaitTimeDesc =
        "Total time spent waiting for a DLockService destroy read lock that was obtained.";
    final String destroyReadWaitsFailedDesc =
        "Total number of times a DLockService destroy read lock wait has completed unsuccessfully.";
    final String destroyReadWaitFailedTimeDesc =
        "Total time spent waiting for a DLockService destroy read lock that was not obtained.";
    final String destroyWriteWaitsInProgressDesc =
        "Current number of thwrites waiting for a DLockService destroy write lock.";
    final String destroyWriteWaitsCompletedDesc =
        "Total number of times a DLockService destroy write lock wait has completed successfully.";
    final String destroyWriteWaitTimeDesc =
        "Total time spent waiting for a DLockService destroy write lock that was obtained.";
    final String destroyWriteWaitsFailedDesc =
        "Total number of times a DLockService destroy write lock wait has completed unsuccessfully.";
    final String destroyWriteWaitFailedTimeDesc =
        "Total time spent waiting for a DLockService destroy write lock that was not obtained.";
    final String destroyReadsDesc =
        "The current number of DLockService destroy read locks held by this process.";
    final String destroyWritesDesc =
        "The current number of DLockService destroy write locks held by this process.";
    final String lockReleasesInProgressDesc =
        "Current number of threads releasing a distributed lock.";
    final String lockReleasesCompletedDesc =
        "Total number of times distributed lock release has completed.";
    final String lockReleaseTimeDesc = "Total time spent releasing a distributed lock.";
    final String becomeGrantorRequestsDesc =
        "Total number of times this member has explicitly requested to become lock grantor.";
    final String freeResourcesCompletedDesc =
        "Total number of times this member has freed resources for a distributed lock.";
    final String freeResourcesFailedDesc =
        "Total number of times this member has attempted to free resources for a distributed lock which remained in use.";

    type = factory.createType(statName, statDescription, new StatisticDescriptor[] {
        factory.createIntGauge("grantors", grantorsDesc, "grantors"),
        factory.createIntGauge("services", servicesDesc, "services"),
        factory.createIntGauge("tokens", tokensDesc, "tokens"),
        factory.createIntGauge("grantTokens", grantTokensDesc, "grantTokens"),
        factory.createIntGauge("requestQueues", requestQueuesDesc, "requestQueues"),
        factory.createIntGauge("serialQueueSize", serialQueueSizeDesc, "messages"),
        factory.createIntGauge("serialThreads", serialThreadsDesc, "threads"),
        factory.createIntGauge("waitingQueueSize", waitingQueueSizeDesc, "messages"),
        factory.createIntGauge("waitingThreads", waitingThreadsDesc, "threads"),
        factory.createIntGauge("lockWaitsInProgress", lockWaitsInProgressDesc, "operations"),
        factory.createIntCounter("lockWaitsCompleted", lockWaitsCompletedDesc, "operations"),
        factory.createLongCounter("lockWaitTime", lockWaitTimeDesc, "nanoseconds", false),
        factory.createIntCounter("lockWaitsFailed", lockWaitsFailedDesc, "operations"),
        factory.createLongCounter("lockWaitFailedTime", lockWaitFailedTimeDesc, "nanoseconds", false),
        factory.createIntGauge("grantWaitsInProgress", grantWaitsInProgressDesc, "operations"),
        factory.createIntCounter("grantWaitsCompleted", grantWaitsCompletedDesc, "operations"),
        factory.createLongCounter("grantWaitTime", grantWaitTimeDesc, "nanoseconds", false),
        factory.createIntCounter("grantWaitsNotGrantor", grantWaitsNotGrantorDesc, "operations"),
        factory.createLongCounter("grantWaitNotGrantorTime", grantWaitNotGrantorTimeDesc, "nanoseconds",
            false),
        factory.createIntCounter("grantWaitsTimeout", grantWaitsTimeoutDesc, "operations"),
        factory.createLongCounter("grantWaitTimeoutTime", grantWaitTimeoutTimeDesc, "nanoseconds", false),
        factory.createIntCounter("grantWaitsNotHolder", grantWaitsNotHolderDesc, "operations"),
        factory.createLongCounter("grantWaitNotHolderTime", grantWaitNotHolderTimeDesc, "nanoseconds",
            false),
        factory.createIntCounter("grantWaitsFailed", grantWaitsFailedDesc, "operations"),
        factory.createLongCounter("grantWaitFailedTime", grantWaitFailedTimeDesc, "nanoseconds", false),
        factory.createIntCounter("grantWaitsSuspended", grantWaitsSuspendedDesc, "operations"),
        factory.createLongCounter("grantWaitSuspendedTime", grantWaitSuspendedTimeDesc, "nanoseconds",
            false),
        factory.createIntCounter("grantWaitsDestroyed", grantWaitsDestroyedDesc, "operations"),
        factory.createLongCounter("grantWaitDestroyedTime", grantWaitDestroyedTimeDesc, "nanoseconds",
            false),
        factory.createIntGauge("createGrantorsInProgress", createGrantorsInProgressDesc, "operations"),
        factory.createIntCounter("createGrantorsCompleted", createGrantorsCompletedDesc, "operations"),
        factory.createLongCounter("createGrantorTime", createGrantorTimeDesc, "nanoseconds", false),
        factory.createIntGauge("serviceCreatesInProgress", serviceCreatesInProgressDesc, "operations"),
        factory.createIntCounter("serviceCreatesCompleted", serviceCreatesCompletedDesc, "operations"),
        factory.createLongCounter("serviceCreateLatchTime", serviceCreateLatchTimeDesc, "nanoseconds",
            false),
        factory.createLongCounter("serviceInitLatchTime", serviceInitLatchTimeDesc, "nanoseconds", false),
        factory.createIntGauge("grantorWaitsInProgress", grantorWaitsInProgressDesc, "operations"),
        factory.createIntCounter("grantorWaitsCompleted", grantorWaitsCompletedDesc, "operations"),
        factory.createLongCounter("grantorWaitTime", grantorWaitTimeDesc, "nanoseconds", false),
        factory.createIntCounter("grantorWaitsFailed", grantorWaitsFailedDesc, "operations"),
        factory.createLongCounter("grantorWaitFailedTime", grantorWaitFailedTimeDesc, "nanoseconds",
            false),
        factory.createIntGauge("grantorThreadsInProgress", grantorThreadsInProgressDesc, "operations"),
        factory.createIntCounter("grantorThreadsCompleted", grantorThreadsCompletedDesc, "operations"),
        factory.createLongCounter("grantorThreadExpireAndGrantLocksTime",
            grantorThreadExpireAndGrantLocksTimeDesc, "nanoseconds", false),
        factory.createLongCounter("grantorThreadHandleRequestTimeoutsTime",
            grantorThreadHandleRequestTimeoutsTimeDesc, "nanoseconds", false),
        factory.createLongCounter("grantorThreadRemoveUnusedTokensTime",
            grantorThreadRemoveUnusedTokensTimeDesc, "nanoseconds", false),
        factory.createLongCounter("grantorThreadTime", grantorThreadTimeDesc, "nanoseconds", false),
        factory.createIntGauge("pendingRequests", pendingRequestsDesc, "pendingRequests"),
        factory.createIntGauge("destroyReadWaitsInProgress", destroyReadWaitsInProgressDesc,
            "operations"),
        factory.createIntCounter("destroyReadWaitsCompleted", destroyReadWaitsCompletedDesc,
            "operations"),
        factory.createLongCounter("destroyReadWaitTime", destroyReadWaitTimeDesc, "nanoseconds", false),
        factory.createIntCounter("destroyReadWaitsFailed", destroyReadWaitsFailedDesc, "operations"),
        factory.createLongCounter("destroyReadWaitFailedTime", destroyReadWaitFailedTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("destroyWriteWaitsInProgress", destroyWriteWaitsInProgressDesc,
            "operations"),
        factory.createIntCounter("destroyWriteWaitsCompleted", destroyWriteWaitsCompletedDesc,
            "operations"),
        factory.createLongCounter("destroyWriteWaitTime", destroyWriteWaitTimeDesc, "nanoseconds", false),
        factory.createIntCounter("destroyWriteWaitsFailed", destroyWriteWaitsFailedDesc, "operations"),
        factory.createLongCounter("destroyWriteWaitFailedTime", destroyWriteWaitFailedTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("destroyReads", destroyReadsDesc, "destroyReads"),
        factory.createIntGauge("destroyWrites", destroyWritesDesc, "destroyWrites"),
        factory.createIntGauge("lockReleasesInProgress", lockReleasesInProgressDesc, "operations"),
        factory.createIntCounter("lockReleasesCompleted", lockReleasesCompletedDesc, "operations"),
        factory.createLongCounter("lockReleaseTime", lockReleaseTimeDesc, "nanoseconds", false),
        factory.createIntCounter("becomeGrantorRequests", becomeGrantorRequestsDesc, "operations"),
        factory.createIntCounter("freeResourcesCompleted", freeResourcesCompletedDesc, "operations"),
        factory.createIntCounter("freeResourcesFailed", freeResourcesFailedDesc, "operations"),});

    // Initialize id fields
    grantorsId = type.nameToId("grantors");
    servicesId = type.nameToId("services");
    tokensId = type.nameToId("tokens");
    grantTokensId = type.nameToId("grantTokens");
    requestQueuesId = type.nameToId("requestQueues");
    serialQueueSizeId = type.nameToId("serialQueueSize");
    serialThreadsId = type.nameToId("serialThreads");
    waitingQueueSizeId = type.nameToId("waitingQueueSize");
    waitingThreadsId = type.nameToId("waitingThreads");
    lockWaitsInProgressId = type.nameToId("lockWaitsInProgress");
    lockWaitsCompletedId = type.nameToId("lockWaitsCompleted");
    lockWaitTimeId = type.nameToId("lockWaitTime");
    lockWaitsFailedId = type.nameToId("lockWaitsFailed");
    lockWaitFailedTimeId = type.nameToId("lockWaitFailedTime");
    grantWaitsInProgressId = type.nameToId("grantWaitsInProgress");
    grantWaitsCompletedId = type.nameToId("grantWaitsCompleted");
    grantWaitTimeId = type.nameToId("grantWaitTime");
    grantWaitsNotGrantorId = type.nameToId("grantWaitsNotGrantor");
    grantWaitNotGrantorTimeId = type.nameToId("grantWaitNotGrantorTime");
    grantWaitsTimeoutId = type.nameToId("grantWaitsTimeout");
    grantWaitTimeoutTimeId = type.nameToId("grantWaitTimeoutTime");
    grantWaitsNotHolderId = type.nameToId("grantWaitsNotHolder");
    grantWaitNotHolderTimeId = type.nameToId("grantWaitNotHolderTime");
    grantWaitsFailedId = type.nameToId("grantWaitsFailed");
    grantWaitFailedTimeId = type.nameToId("grantWaitFailedTime");
    grantWaitsSuspendedId = type.nameToId("grantWaitsSuspended");
    grantWaitSuspendedTimeId = type.nameToId("grantWaitSuspendedTime");
    grantWaitsDestroyedId = type.nameToId("grantWaitsDestroyed");
    grantWaitDestroyedTimeId = type.nameToId("grantWaitDestroyedTime");
    createGrantorsInProgressId = type.nameToId("createGrantorsInProgress");
    createGrantorsCompletedId = type.nameToId("createGrantorsCompleted");
    createGrantorTimeId = type.nameToId("createGrantorTime");
    serviceCreatesInProgressId = type.nameToId("serviceCreatesInProgress");
    serviceCreatesCompletedId = type.nameToId("serviceCreatesCompleted");
    serviceCreateLatchTimeId = type.nameToId("serviceCreateLatchTime");
    serviceInitLatchTimeId = type.nameToId("serviceInitLatchTime");
    grantorWaitsInProgressId = type.nameToId("grantorWaitsInProgress");
    grantorWaitsCompletedId = type.nameToId("grantorWaitsCompleted");
    grantorWaitTimeId = type.nameToId("grantorWaitTime");
    grantorWaitsFailedId = type.nameToId("grantorWaitsFailed");
    grantorWaitFailedTimeId = type.nameToId("grantorWaitFailedTime");
    grantorThreadsInProgressId = type.nameToId("grantorThreadsInProgress");
    grantorThreadsCompletedId = type.nameToId("grantorThreadsCompleted");
    grantorThreadExpireAndGrantLocksTimeId = type.nameToId("grantorThreadExpireAndGrantLocksTime");
    grantorThreadHandleRequestTimeoutsTimeId =
        type.nameToId("grantorThreadHandleRequestTimeoutsTime");
    grantorThreadRemoveUnusedTokensTimeId = type.nameToId("grantorThreadRemoveUnusedTokensTime");
    grantorThreadTimeId = type.nameToId("grantorThreadTime");
    pendingRequestsId = type.nameToId("pendingRequests");
    destroyReadWaitsInProgressId = type.nameToId("destroyReadWaitsInProgress");
    destroyReadWaitsCompletedId = type.nameToId("destroyReadWaitsCompleted");
    destroyReadWaitTimeId = type.nameToId("destroyReadWaitTime");
    destroyReadWaitsFailedId = type.nameToId("destroyReadWaitsFailed");
    destroyReadWaitFailedTimeId = type.nameToId("destroyReadWaitFailedTime");
    destroyWriteWaitsInProgressId = type.nameToId("destroyWriteWaitsInProgress");
    destroyWriteWaitsCompletedId = type.nameToId("destroyWriteWaitsCompleted");
    destroyWriteWaitTimeId = type.nameToId("destroyWriteWaitTime");
    destroyWriteWaitsFailedId = type.nameToId("destroyWriteWaitsFailed");
    destroyWriteWaitFailedTimeId = type.nameToId("destroyWriteWaitFailedTime");
    destroyReadsId = type.nameToId("destroyReads");
    destroyWritesId = type.nameToId("destroyWrites");
    lockReleasesInProgressId = type.nameToId("lockReleasesInProgress");
    lockReleasesCompletedId = type.nameToId("lockReleasesCompleted");
    lockReleaseTimeId = type.nameToId("lockReleaseTime");
    becomeGrantorRequestsId = type.nameToId("becomeGrantorRequests");
    freeResourcesCompletedId = type.nameToId("freeResourcesCompleted");
    freeResourcesFailedId = type.nameToId("freeResourcesFailed");
  } // static block

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new <code>DLockStats</code> and registers itself with the given statistics factory.
   */
  public DLockStatsImpl(StatisticsFactory factory, long statId) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, "dlockStats", statId);
  }

  // -------------------------------------------------------------------------
  // Instance methods
  // -------------------------------------------------------------------------

  public void close() {
    this.stats.close();
  }

  // time for call to lock() to complete
  public int getLockWaitsInProgress() {
    return stats.getInt(lockWaitsInProgressId);
  }

  public int getLockWaitsCompleted() {
    return stats.getInt(lockWaitsCompletedId);
  }

  public int getLockWaitsFailed() {
    return stats.getInt(lockWaitsFailedId);
  }

  public long getLockWaitTime() {
    return stats.getLong(lockWaitTimeId);
  }

  public long getLockWaitFailedTime() {
    return stats.getLong(lockWaitFailedTimeId);
  }

  public long startLockWait() {
    stats.incInt(lockWaitsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endLockWait(long start, boolean success) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(lockWaitsInProgressId, -1);
    if (success) {
      stats.incInt(lockWaitsCompletedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(lockWaitTimeId, ts - start);
      }
    } else {
      stats.incInt(lockWaitsFailedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(lockWaitFailedTimeId, ts - start);
      }
    }
  }

  // incSerialQueueSize everytime getWaitingQueueHelper add/remove called
  public int getWaitingQueueSize() {
    return this.stats.getInt(waitingQueueSizeId);
  }

  public void incWaitingQueueSize(int messages) { // TODO: prolly no callers
    this.stats.incInt(waitingQueueSizeId, messages);
  }

  // incSerialQueueSize everytime getSerialQueueHelper add/remove called
  public int getSerialQueueSize() {
    return this.stats.getInt(serialQueueSizeId);
  }

  public void incSerialQueueSize(int messages) { // TODO: prolly no callers
    this.stats.incInt(serialQueueSizeId, messages);
  }

  // incNumSerialThreads everytime we execute with dlock getSerialExecutor()
  public int getNumSerialThreads() {
    return this.stats.getInt(serialThreadsId);
  }

  public void incNumSerialThreads(int threads) { // TODO: no callers!
    this.stats.incInt(serialThreadsId, threads);
  }

  // incWaitingThreads for every invoke of getWaitingPoolHelper startJob/endJob
  public int getWaitingThreads() {
    return this.stats.getInt(waitingThreadsId);
  }

  public void incWaitingThreads(int threads) { // TODO: prolly no callers
    this.stats.incInt(waitingThreadsId, threads);
  }

  // current number of lock services used by this system member
  public int getServices() {
    return this.stats.getInt(servicesId);
  }

  public void incServices(int val) {
    this.stats.incInt(servicesId, val);
  }

  // current number of lock grantors hosted by this system member
  public int getGrantors() {
    return this.stats.getInt(grantorsId);
  }

  public void incGrantors(int val) {
    this.stats.incInt(grantorsId, val);
  }

  // current number of lock tokens used by this system member
  public int getTokens() {
    return this.stats.getInt(tokensId);
  }

  public void incTokens(int val) {
    this.stats.incInt(tokensId, val);
  }

  // current number of grant tokens used by local grantors
  public int getGrantTokens() {
    return this.stats.getInt(grantTokensId);
  }

  public void incGrantTokens(int val) {
    this.stats.incInt(grantTokensId, val);
  }

  // current number of lock request queues used by this system member
  public int getRequestQueues() {
    return this.stats.getInt(requestQueuesId);
  }

  public void incRequestQueues(int val) {
    this.stats.incInt(requestQueuesId, val);
  }

  // time for granting of lock requests to complete
  public int getGrantWaitsInProgress() {
    return stats.getInt(grantWaitsInProgressId);
  }

  public int getGrantWaitsCompleted() {
    return stats.getInt(grantWaitsCompletedId);
  }

  public int getGrantWaitsFailed() {
    return stats.getInt(grantWaitsFailedId);
  }

  public int getGrantWaitsSuspended() {
    return stats.getInt(grantWaitsSuspendedId);
  }

  public int getGrantWaitsDestroyed() {
    return stats.getInt(grantWaitsDestroyedId);
  }

  public long getGrantWaitTime() {
    return stats.getLong(grantWaitTimeId);
  }

  public long getGrantWaitFailedTime() {
    return stats.getLong(grantWaitFailedTimeId);
  }

  public long startGrantWait() {
    stats.incInt(grantWaitsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endGrantWait(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsCompletedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitTimeId, ts - start);
    }
  }

  public void endGrantWaitNotGrantor(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsNotGrantorId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitNotGrantorTimeId, ts - start);
    }
  }

  public void endGrantWaitTimeout(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsTimeoutId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitTimeoutTimeId, ts - start);
    }
  }

  public void endGrantWaitNotHolder(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsNotHolderId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitNotHolderTimeId, ts - start);
    }
  }

  public void endGrantWaitFailed(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsFailedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitFailedTimeId, ts - start);
    }
  }

  public void endGrantWaitSuspended(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsSuspendedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitSuspendedTimeId, ts - start);
    }
  }

  public void endGrantWaitDestroyed(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantWaitsInProgressId, -1);
    stats.incInt(grantWaitsDestroyedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantWaitDestroyedTimeId, ts - start);
    }
  }

  // time for creating initial grantor for lock service
  public int getCreateGrantorsInProgress() {
    return stats.getInt(createGrantorsInProgressId);
  }

  public int getCreateGrantorsCompleted() {
    return stats.getInt(createGrantorsCompletedId);
  }

  public long getCreateGrantorTime() {
    return stats.getLong(createGrantorTimeId);
  }

  public long startCreateGrantor() { // TODO: no callers!
    stats.incInt(createGrantorsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endCreateGrantor(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(createGrantorsInProgressId, -1);
    stats.incInt(createGrantorsCompletedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(createGrantorTimeId, ts - start);
    }
  }

  // time for creating each lock service
  public int getServiceCreatesInProgress() {
    return stats.getInt(serviceCreatesInProgressId);
  }

  public int getServiceCreatesCompleted() {
    return stats.getInt(serviceCreatesCompletedId);
  }

  public long startServiceCreate() { // TODO: no callers!
    stats.incInt(serviceCreatesInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void serviceCreateLatchReleased(long start) {
    if (DistributionStatsImpl.enableClockStats) {
      long ts = DLockStatsImpl.getStatTime();
      stats.incLong(serviceCreateLatchTimeId, ts - start);
    }
  }

  public void serviceInitLatchReleased(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(serviceCreatesInProgressId, -1);
    stats.incInt(serviceCreatesCompletedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(serviceInitLatchTimeId, ts - start);
    }
  }

  public long getServiceCreateLatchTime() {
    return stats.getLong(serviceCreateLatchTimeId);
  }

  public long getServiceInitLatchTime() {
    return stats.getLong(serviceInitLatchTimeId);
  }

  // time spent waiting grantor latches
  public int getGrantorWaitsInProgress() {
    return stats.getInt(grantorWaitsInProgressId);
  }

  public int getGrantorWaitsCompleted() {
    return stats.getInt(grantorWaitsCompletedId);
  }

  public int getGrantorWaitsFailed() {
    return stats.getInt(grantorWaitsFailedId);
  }

  public long getGrantorWaitTime() {
    return stats.getLong(grantorWaitTimeId);
  }

  public long getGrantorWaitFailedTime() {
    return stats.getLong(grantorWaitFailedTimeId);
  }

  public long startGrantorWait() {
    stats.incInt(grantorWaitsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endGrantorWait(long start, boolean success) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantorWaitsInProgressId, -1);
    if (success) {
      stats.incInt(grantorWaitsCompletedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(grantorWaitTimeId, ts - start);
      }
    } else {
      stats.incInt(grantorWaitsFailedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(grantorWaitFailedTimeId, ts - start);
      }
    }
  }

  // time spent by grantor threads
  public int getGrantorThreadsInProgress() {
    return stats.getInt(grantorThreadsInProgressId);
  }

  public int getGrantorThreadsCompleted() {
    return stats.getInt(grantorThreadsCompletedId);
  }

  public long getGrantorThreadTime() {
    return stats.getLong(grantorThreadTimeId);
  }

  public long getGrantorThreadExpireAndGrantLocksTime() {
    return stats.getLong(grantorThreadExpireAndGrantLocksTimeId);
  }

  public long getGrantorThreadHandleRequestTimeoutsTime() {
    return stats.getLong(grantorThreadHandleRequestTimeoutsTimeId);
  }

  public long getGrantorThreadRemoveUnusedTokensTime() {
    return stats.getLong(grantorThreadRemoveUnusedTokensTimeId);
  }

  public long startGrantorThread() {
    stats.incInt(grantorThreadsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public long endGrantorThreadExpireAndGrantLocks(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incLong(grantorThreadExpireAndGrantLocksTimeId, ts - start);
    return DLockStatsImpl.getStatTime();
  }

  public long endGrantorThreadHandleRequestTimeouts(long timing) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incLong(grantorThreadHandleRequestTimeoutsTimeId, ts - timing);
    return DLockStatsImpl.getStatTime();
  }

  public void endGrantorThreadRemoveUnusedTokens(long timing) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incLong(grantorThreadRemoveUnusedTokensTimeId, ts - timing);
  }

  public void endGrantorThread(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(grantorThreadsInProgressId, -1);
    stats.incInt(grantorThreadsCompletedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(grantorThreadTimeId, ts - start);
    }
  }

  // current number of requests waiting in lock grantor queues
  public int getPendingRequests() {
    return this.stats.getInt(pendingRequestsId);
  }

  public void incPendingRequests(int val) {
    this.stats.incInt(pendingRequestsId, val);
  }

  // acquisition of destroyReadLock in DLockService
  public int getDestroyReadWaitsInProgress() {
    return stats.getInt(destroyReadWaitsInProgressId);
  }

  public int getDestroyReadWaitsCompleted() {
    return stats.getInt(destroyReadWaitsCompletedId);
  }

  public int getDestroyReadWaitsFailed() {
    return stats.getInt(destroyReadWaitsFailedId);
  }

  public long getDestroyReadWaitTime() {
    return stats.getLong(destroyReadWaitTimeId);
  }

  public long getDestroyReadWaitFailedTime() {
    return stats.getLong(destroyReadWaitFailedTimeId);
  }

  public long startDestroyReadWait() { // TODO: no callers!
    stats.incInt(destroyReadWaitsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endDestroyReadWait(long start, boolean success) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(destroyReadWaitsInProgressId, -1);
    if (success) {
      stats.incInt(destroyReadWaitsCompletedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(destroyReadWaitTimeId, ts - start);
      }
    } else {
      stats.incInt(destroyReadWaitsFailedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(destroyReadWaitFailedTimeId, ts - start);
      }
    }
  }

  // acquisition of destroyWriteLock in DLockService
  public int getDestroyWriteWaitsInProgress() {
    return stats.getInt(destroyWriteWaitsInProgressId);
  }

  public int getDestroyWriteWaitsCompleted() {
    return stats.getInt(destroyWriteWaitsCompletedId);
  }

  public int getDestroyWriteWaitsFailed() {
    return stats.getInt(destroyWriteWaitsFailedId);
  }

  public long getDestroyWriteWaitTime() {
    return stats.getLong(destroyWriteWaitTimeId);
  }

  public long getDestroyWriteWaitFailedTime() {
    return stats.getLong(destroyWriteWaitFailedTimeId);
  }

  public long startDestroyWriteWait() { // TODO: no callers!
    stats.incInt(destroyWriteWaitsInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endDestroyWriteWait(long start, boolean success) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(destroyWriteWaitsInProgressId, -1);
    if (success) {
      stats.incInt(destroyWriteWaitsCompletedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(destroyWriteWaitTimeId, ts - start);
      }
    } else {
      stats.incInt(destroyWriteWaitsFailedId, 1);
      if (DistributionStatsImpl.enableClockStats) {
        stats.incLong(destroyWriteWaitFailedTimeId, ts - start);
      }
    }
  }

  // current number of DLockService destroy read locks held by this process
  public int getDestroyReads() {
    return this.stats.getInt(destroyReadsId);
  }

  public void incDestroyReads(int val) { // TODO: no callers!
    this.stats.incInt(destroyReadsId, val);
  }

  // current number of DLockService destroy write locks held by this process
  public int getDestroyWrites() {
    return this.stats.getInt(destroyWritesId);
  }

  public void incDestroyWrites(int val) { // TODO: no callers!
    this.stats.incInt(destroyWritesId, val);
  }

  // time for call to unlock() to complete
  public int getLockReleasesInProgress() {
    return stats.getInt(lockReleasesInProgressId);
  }

  public int getLockReleasesCompleted() {
    return stats.getInt(lockReleasesCompletedId);
  }

  public long getLockReleaseTime() {
    return stats.getLong(lockReleaseTimeId);
  }

  public long startLockRelease() {
    stats.incInt(lockReleasesInProgressId, 1);
    return DLockStatsImpl.getStatTime();
  }

  public void endLockRelease(long start) {
    long ts = DLockStatsImpl.getStatTime();
    stats.incInt(lockReleasesInProgressId, -1);
    stats.incInt(lockReleasesCompletedId, 1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(lockReleaseTimeId, ts - start);
    }
  }

  // total number of times this member has requested to become grantor
  public int getBecomeGrantorRequests() {
    return this.stats.getInt(becomeGrantorRequestsId);
  }

  public void incBecomeGrantorRequests() {
    this.stats.incInt(becomeGrantorRequestsId, 1);
  }

  public int getFreeResourcesCompleted() {
    return this.stats.getInt(freeResourcesCompletedId);
  }

  public void incFreeResourcesCompleted() {
    this.stats.incInt(freeResourcesCompletedId, 1);
  }

  public int getFreeResourcesFailed() {
    return this.stats.getInt(freeResourcesFailedId);
  }

  public void incFreeResourcesFailed() {
    this.stats.incInt(freeResourcesFailedId, 1);
  }

  // -------------------------------------------------------------------------
  // StatHelpers for dedicated dlock executors
  // -------------------------------------------------------------------------

  /**
   * Returns a helper object so that the serial queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  public QueueStatHelper getSerialQueueHelper() {
    return new QueueStatHelper() {
      public void add() {
        incSerialQueueSize(1);
      }

      public void remove() {
        incSerialQueueSize(-1);
      }

      public void remove(int count) {
        incSerialQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the waiting pool can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  public PoolStatHelper getWaitingPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incWaitingThreads(1);
      }

      public void endJob() {
        incWaitingThreads(-1);
      }
    };
  }

  /**
   * Returns a helper object so that the waiting queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  public QueueStatHelper getWaitingQueueHelper() {
    return new QueueStatHelper() {
      public void add() {
        incWaitingQueueSize(1);
      }

      public void remove() {
        incWaitingQueueSize(-1);
      }

      public void remove(int count) {
        incWaitingQueueSize(-count);
      }
    };
  }

  public Statistics getStats() {
    return stats;
  }

}
