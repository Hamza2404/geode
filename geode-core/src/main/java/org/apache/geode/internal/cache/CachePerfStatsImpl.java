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

package org.apache.geode.internal.cache;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.distributed.internal.QueueStatHelper;
import org.apache.geode.internal.NanoTimer;

/**
 * CachePerfStats tracks statistics about GemFire cache performance.
 */
public class CachePerfStatsImpl implements CachePerfStats, GFSStatsImplementor {
  public static boolean enableClockStats = false;

  ////////////////// Static fields ///////////////////////////

  private StatisticsType type;

  protected int loadsInProgressId;
  protected int loadsCompletedId;
  protected int loadTimeId;
  protected int netloadsInProgressId;
  protected int netloadsCompletedId;
  protected int netloadTimeId;
  protected int netsearchesInProgressId;
  protected int netsearchesCompletedId;
  protected int netsearchTimeId;
  protected int cacheWriterCallsInProgressId;
  protected int cacheWriterCallsCompletedId;
  protected int cacheWriterCallTimeId;
  protected int cacheListenerCallsInProgressId;
  protected int cacheListenerCallsCompletedId;
  protected int cacheListenerCallTimeId;
  protected int getInitialImagesInProgressId;
  protected int getInitialImagesCompletedId;
  protected int deltaGetInitialImagesCompletedId;
  protected int getInitialImageTimeId;
  protected int getInitialImageKeysReceivedId;
  protected int regionsId;
  protected int partitionedRegionsId;
  protected int destroysId;
  protected int createsId;
  protected int putsId;
  protected int putTimeId;
  protected int putallsId;
  protected int putallTimeId;
  protected int removeAllsId;
  protected int removeAllTimeId;
  protected int updatesId;
  protected int updateTimeId;
  protected int invalidatesId;
  protected int getsId;
  protected int getTimeId;
  protected int eventQueueSizeId;
  protected int eventQueueThrottleTimeId;
  protected int eventQueueThrottleCountId;
  protected int eventThreadsId;
  protected int missesId;
  protected int queryExecutionsId;
  protected int queryExecutionTimeId;
  protected int queryResultsHashCollisionsId;
  protected int queryResultsHashCollisionProbeTimeId;
  protected int partitionedRegionQueryRetriesId;

  protected int txSuccessLifeTimeId;
  protected int txFailedLifeTimeId;
  protected int txRollbackLifeTimeId;
  protected int txCommitsId;
  protected int txFailuresId;
  protected int txRollbacksId;
  protected int txCommitTimeId;
  protected int txFailureTimeId;
  protected int txRollbackTimeId;
  protected int txCommitChangesId;
  protected int txFailureChangesId;
  protected int txRollbackChangesId;
  protected int txConflictCheckTimeId;

  protected int reliableQueuedOpsId;
  protected int reliableQueueSizeId;
  protected int reliableQueueMaxId;
  protected int reliableRegionsId;
  protected int reliableRegionsMissingId;
  protected int reliableRegionsQueuingId;
  protected int reliableRegionsMissingFullAccessId;
  protected int reliableRegionsMissingLimitedAccessId;
  protected int reliableRegionsMissingNoAccessId;
  protected int entryCountId;
  protected int eventsQueuedId;
  protected int retriesId;

  protected int diskTasksWaitingId;
  protected int evictorJobsStartedId;
  protected int evictorJobsCompletedId;
  protected int evictorQueueSizeId;

  protected int evictWorkTimeId;


  protected int indexUpdateInProgressId;
  protected int indexUpdateCompletedId;
  protected int indexUpdateTimeId;
  protected int clearsId;
  protected int indexInitializationInProgressId;
  protected int indexInitializationCompletedId;
  protected int indexInitializationTimeId;

  /**
   * Id of the meta data refresh statistic
   */
  protected int metaDataRefreshCountId;

  protected int conflatedEventsId;
  protected int tombstoneCountId;
  protected int tombstoneGCCountId;
  protected int tombstoneOverhead1Id;
  protected int tombstoneOverhead2Id;
  protected int clearTimeoutsId;

  protected int deltaUpdatesId;
  protected int deltaUpdatesTimeId;
  protected int deltaFailedUpdatesId;

  protected int deltasPreparedId;
  protected int deltasPreparedTimeId;
  protected int deltasSentId;

  protected int deltaFullValuesSentId;
  protected int deltaFullValuesRequestedId;

  protected int importedEntriesCountId;
  protected int importTimeId;
  protected int exportedEntriesCountId;
  protected int exportTimeId;

  protected int compressionCompressTimeId;
  protected int compressionDecompressTimeId;
  protected int compressionCompressionsId;
  protected int compressionDecompressionsId;
  protected int compressionPreCompressedBytesId;
  protected int compressionPostCompressedBytesId;

  /**
   * The Statistics object that we delegate most behavior to
   */
  protected final Statistics stats;

  private static final String loadsInProgressDesc =
      "Current number of threads in this cache doing a cache load.";
  private static final String
      loadsCompletedDesc =
      "Total number of times a load on this cache has completed (as a result of either a local get() or a remote netload).";
  private static final String loadTimeDesc = "Total time spent invoking loaders on this cache.";
  private static final String netloadsInProgressDesc =
      "Current number of threads doing a network load initiated by a get() in this cache.";
  private static final String netloadsCompletedDesc =
      "Total number of times a network load initiated on this cache has completed.";
  private static final String
      netloadTimeDesc =
      "Total time spent doing network loads on this cache.";
  private static final String netsearchesInProgressDesc =
      "Current number of threads doing a network search initiated by a get() in this cache.";
  private static final String netsearchesCompletedDesc =
      "Total number of times network searches initiated by this cache have completed.";
  private static final String
      netsearchTimeDesc =
      "Total time spent doing network searches for cache values.";
  private static final String cacheWriterCallsInProgressDesc =
      "Current number of threads doing a cache writer call.";
  private static final String cacheWriterCallsCompletedDesc =
      "Total number of times a cache writer call has completed.";
  private static final String
      cacheWriterCallTimeDesc =
      "Total time spent doing cache writer calls.";
  private static final String cacheListenerCallsInProgressDesc =
      "Current number of threads doing a cache listener call.";
  private static final String cacheListenerCallsCompletedDesc =
      "Total number of times a cache listener call has completed.";
  private static final String
      cacheListenerCallTimeDesc =
      "Total time spent doing cache listener calls.";
  private static final String getInitialImagesInProgressDesc =
      "Current number of getInitialImage operations currently in progress.";
  private static final String
      getInitialImagesCompletedDesc =
      "Total number of times getInitialImages (both delta and full GII) initiated by this cache have completed.";
  private static final String deltaGetInitialImagesCompletedDesc =
      "Total number of times delta getInitialImages initiated by this cache have completed.";
  private static final String getInitialImageTimeDesc =
      "Total time spent doing getInitialImages for region creation.";
  private static final String getInitialImageKeysReceivedDesc =
      "Total number of keys received while doing getInitialImage operations.";
  private static final String regionsDesc = "The current number of regions in the cache.";
  private static final String
      partitionedRegionsDesc =
      "The current number of partitioned regions in the cache.";
  private static final String destroysDesc =
      "The total number of times a cache object entry has been destroyed in this cache.";
  private static final String updatesDesc =
      "The total number of updates originating remotely that have been applied to this cache.";
  private static final String updateTimeDesc = "Total time spent performing an update.";
  private static final String
      invalidatesDesc =
      "The total number of times an existing cache object entry value in this cache has been invalidated";
  private static final String getsDesc =
      "The total number of times a successful get has been done on this cache.";
  private static final String
      createsDesc =
      "The total number of times an entry is added to this cache.";
  private static final String
      putsDesc =
      "The total number of times an entry is added or replaced in this cache as a result of a local operation (put(), create(), or get() which results in load, netsearch, or netloading a value). Note that this only counts puts done explicitly on this cache. It does not count updates pushed from other caches.";
  private static final String
      putTimeDesc =
      "Total time spent adding or replacing an entry in this cache as a result of a local operation.  This includes synchronizing on the map, invoking cache callbacks, sending messages to other caches and waiting for responses (if required).";
  private static final String
      putallsDesc =
      "The total number of times a map is added or replaced in this cache as a result of a local operation. Note that this only counts putAlls done explicitly on this cache. It does not count updates pushed from other caches.";
  private static final String
      putallTimeDesc =
      "Total time spent replacing a map in this cache as a result of a local operation.  This includes synchronizing on the map, invoking cache callbacks, sending messages to other caches and waiting for responses (if required).";
  private static final String
      removeAllsDesc =
      "The total number of removeAll operations that originated in this cache. Note that this only counts removeAlls done explicitly on this cache. It does not count removes pushed from other caches.";
  private static final String
      removeAllTimeDesc =
      "Total time spent performing removeAlls that originated in this cache. This includes time spent waiting for the removeAll to be done in remote caches (if required).";
  private static final String getTimeDesc =
      "Total time spent doing get operations from this cache (including netsearch and netload)";
  private static final String
      eventQueueSizeDesc =
      "The number of cache events waiting to be processed.";
  private static final String eventQueueThrottleTimeDesc =
      "The total amount of time, in nanoseconds, spent delayed by the event queue throttle.";
  private static final String eventQueueThrottleCountDesc =
      "The total number of times a thread was delayed in adding an event to the event queue.";
  private static final String
      eventThreadsDesc =
      "The number of threads currently processing events.";
  private static final String
      missesDesc =
      "Total number of times a get on the cache did not find a value already in local memory. The number of hits (i.e. gets that did not miss) can be calculated by subtracting misses from gets.";
  private static final String
      queryExecutionsDesc =
      "Total number of times some query has been executed";
  private static final String queryExecutionTimeDesc = "Total time spent executing queries";
  private static final String
      queryResultsHashCollisionsDesc =
      "Total number of times an hash code collision occurred when inserting an object into an OQL result set or rehashing it";
  private static final String
      queryResultsHashCollisionProbeTimeDesc =
      "Total time spent probing the hashtable in an OQL result set due to hash code collisions, includes reads, writes, and rehashes";
  private static final String partitionedRegionOQLQueryRetriesDesc =
      "Total number of times an OQL Query on a Partitioned Region had to be retried";
  private static final String
      txSuccessLifeTimeDesc =
      "The total amount of time, in nanoseconds, spent in a transaction before a successful commit. The time measured starts at transaction begin and ends when commit is called.";
  private static final String
      txFailedLifeTimeDesc =
      "The total amount of time, in nanoseconds, spent in a transaction before a failed commit. The time measured starts at transaction begin and ends when commit is called.";
  private static final String
      txRollbackLifeTimeDesc =
      "The total amount of time, in nanoseconds, spent in a transaction before an explicit rollback. The time measured starts at transaction begin and ends when rollback is called.";
  private static final String
      txCommitsDesc =
      "Total number times a transaction commit has succeeded.";
  private static final String
      txFailuresDesc =
      "Total number times a transaction commit has failed.";
  private static final String txRollbacksDesc =
      "Total number times a transaction has been explicitly rolled back.";
  private static final String txCommitTimeDesc =
      "The total amount of time, in nanoseconds, spent doing successful transaction commits.";
  private static final String txFailureTimeDesc =
      "The total amount of time, in nanoseconds, spent doing failed transaction commits.";
  private static final String txRollbackTimeDesc =
      "The total amount of time, in nanoseconds, spent doing explicit transaction rollbacks.";
  private static final String
      txCommitChangesDesc =
      "Total number of changes made by committed transactions.";
  private static final String
      txFailureChangesDesc =
      "Total number of changes lost by failed transactions.";
  private static final String txRollbackChangesDesc =
      "Total number of changes lost by explicit transaction rollbacks.";
  private static final String
      txConflictCheckTimeDesc =
      "The total amount of time, in nanoseconds, spent doing conflict checks during transaction commit";
  private static final String reliableQueuedOpsDesc =
      "Current number of cache operations queued for distribution to required roles.";
  private static final String reliableQueueSizeDesc =
      "Current size in megabytes of disk used to queue for distribution to required roles.";
  private static final String
      reliableQueueMaxDesc =
      "Maximum size in megabytes allotted for disk usage to queue for distribution to required roles.";
  private static final String
      reliableRegionsDesc =
      "Current number of regions configured for reliability.";
  private static final String reliableRegionsMissingDesc =
      "Current number regions configured for reliability that are missing required roles.";
  private static final String reliableRegionsQueuingDesc =
      "Current number regions configured for reliability that are queuing for required roles.";
  private static final String
      reliableRegionsMissingFullAccessDesc =
      "Current number of regions configured for reliablity that are missing require roles with full access";
  private static final String
      reliableRegionsMissingLimitedAccessDesc =
      "Current number of regions configured for reliablity that are missing required roles with Limited access";
  private static final String
      reliableRegionsMissingNoAccessDesc =
      "Current number of regions configured for reliablity that are missing required roles with No access";
  private static final String
      clearsDesc =
      "The total number of times a clear has been done on this cache.";
  private static final String nonSingleHopsDesc =
      "Total number of times client request observed more than one hop during operation.";
  private static final String metaDataRefreshCountDesc =
      "Total number of times the meta data is refreshed due to hopping observed.";
  private static final String
      conflatedEventsDesc =
      "Number of events not delivered due to conflation.  Typically this means that the event arrived after a later event was already applied to the cache.";
  private static final String tombstoneCountDesc =
      "Number of destroyed entries that are retained for concurrent modification detection";
  private static final String tombstoneGCCountDesc =
      "Number of garbage-collections performed on destroyed entries";
  private static final String tombstoneOverhead1Desc =
      "Amount of memory consumed by destroyed entries in replicated or partitioned regions";
  private static final String tombstoneOverhead2Desc =
      "Amount of memory consumed by destroyed entries in non-replicated regions";
  private static final String
      clearTimeoutsDesc =
      "Number of timeouts waiting for events concurrent to a clear() operation to be received and applied before performing the clear()";
  private static final String deltaUpdatesDesc =
      "The total number of times entries in this cache are updated through delta bytes.";
  private static final String deltaUpdatesTimeDesc =
      "Total time spent applying the received delta bytes to entries in this cache.";
  private static final String deltaFailedUpdatesDesc =
      "The total number of times entries in this cache failed to be updated through delta bytes.";
  private static final String
      deltasPreparedDesc =
      "The total number of times delta was prepared in this cache.";
  private static final String
      deltasPreparedTimeDesc =
      "Total time spent preparing delta bytes in this cache.";
  private static final String
      deltasSentDesc =
      "The total number of times delta was sent to remote caches. This excludes deltas sent from server to client.";
  private static final String deltaFullValuesSentDesc =
      "The total number of times a full value was sent to a remote cache.";
  private static final String deltaFullValuesRequestedDesc =
      "The total number of times a full value was requested by this cache.";
  private static final String importedEntriesCountDesc =
      "The total number of entries imported from a snapshot file.";
  private static final String
      importTimeDesc =
      "The total time spent importing entries from a snapshot file.";
  private static final String exportedEntriesCountDesc =
      "The total number of entries exported into a snapshot file.";
  private static final String
      exportTimeDesc =
      "The total time spent exporting entries into a snapshot file.";
  private static final String
      compressionCompressTimeDesc =
      "The total time spent compressing data.";
  private static final String
      compressionDecompressTimeDesc =
      "The total time spent decompressing data.";
  private static final String
      compressionCompressionsDesc =
      "The total number of compression operations.";
  private static final String
      compressionDecompressionsDesc =
      "The total number of decompression operations.";
  private static final String compressionPreCompresssedBytesDesc =
      "The total number of bytes before compressing.";
  private static final String compressionPostCompressedBytesDesc =
      "The total number of bytes after compressing.";
  private static final String
      evictByCriteria_evictionsDesc =
      "The total number of entries evicted";
  // total actual evictions (entries evicted)
  private static final String
      evictByCriteria_evictionTimeDesc =
      "Time taken for eviction process";
  // total eviction time including product +user expr.
  private static final String
      evictByCriteria_evictionsInProgressDesc =
      "Total number of evictions in progress";
  private static final String
      evictByCriteria_evaluationsDesc =
      "Total number of evaluations for eviction";
  // total eviction attempts
  private static final String
      evictByCriteria_evaluationTimeDesc =
      "Total time taken for evaluation of user expression during eviction";
// time taken to evaluate user expression.

  void initializeStats(StatisticsFactory factory, String name) {
    type =
        factory.createType("CachePerfStats-" + name, "Statistics about GemFire cache performance",
            new StatisticDescriptor[]{
                factory.createIntGauge("loadsInProgress", loadsInProgressDesc, "operations"),
                factory.createIntCounter("loadsCompleted", loadsCompletedDesc, "operations"),
                factory.createLongCounter("loadTime", loadTimeDesc, "nanoseconds", false),
                factory.createIntGauge("netloadsInProgress", netloadsInProgressDesc, "operations"),
                factory.createIntCounter("netloadsCompleted", netloadsCompletedDesc, "operations"),
                factory.createLongCounter("netloadTime", netloadTimeDesc, "nanoseconds", false),
                factory.createIntGauge("netsearchesInProgress", netsearchesInProgressDesc,
                    "operations"),
                factory.createIntCounter("netsearchesCompleted", netsearchesCompletedDesc,
                    "operations"),
                factory.createLongCounter("netsearchTime", netsearchTimeDesc, "nanoseconds"),
                factory.createIntGauge("cacheWriterCallsInProgress", cacheWriterCallsInProgressDesc,
                    "operations"),
                factory.createIntCounter("cacheWriterCallsCompleted", cacheWriterCallsCompletedDesc,
                    "operations"),
                factory.createLongCounter("cacheWriterCallTime", cacheWriterCallTimeDesc,
                    "nanoseconds"),
                factory.createIntGauge("cacheListenerCallsInProgress",
                    cacheListenerCallsInProgressDesc,
                    "operations"),
                factory.createIntCounter("cacheListenerCallsCompleted",
                    cacheListenerCallsCompletedDesc,
                    "operations"),
                factory.createLongCounter("cacheListenerCallTime", cacheListenerCallTimeDesc,
                    "nanoseconds"),
                factory.createIntGauge("indexUpdateInProgress", "Current number of ops in progress",
                    "operations"),
                factory.createIntCounter("indexUpdateCompleted",
                    "Total number of ops that have completed",
                    "operations"),
                factory.createLongCounter("indexUpdateTime",
                    "Total amount of time spent doing this op",
                    "nanoseconds"),
                factory.createIntGauge("indexInitializationInProgress",
                    "Current number of index initializations in progress", "operations"),
                factory.createIntCounter("indexInitializationCompleted",
                    "Total number of index initializations that have completed", "operations"),
                factory.createLongCounter("indexInitializationTime",
                    "Total amount of time spent initializing indexes", "nanoseconds"),

                factory.createIntGauge("getInitialImagesInProgress", getInitialImagesInProgressDesc,
                    "operations"),
                factory.createIntCounter("getInitialImagesCompleted", getInitialImagesCompletedDesc,
                    "operations"),
                factory.createIntCounter("deltaGetInitialImagesCompleted",
                    deltaGetInitialImagesCompletedDesc,
                    "operations"),
                factory.createLongCounter("getInitialImageTime", getInitialImageTimeDesc,
                    "nanoseconds"),
                factory.createIntCounter("getInitialImageKeysReceived",
                    getInitialImageKeysReceivedDesc,
                    "keys"),
                factory.createIntGauge("regions", regionsDesc, "regions"),
                factory.createIntGauge("partitionedRegions", partitionedRegionsDesc,
                    "partitionedRegions"),
                factory.createIntCounter("destroys", destroysDesc, "operations"),
                factory.createIntCounter("updates", updatesDesc, "operations"),
                factory.createLongCounter("updateTime", updateTimeDesc, "nanoseconds"),
                factory.createIntCounter("invalidates", invalidatesDesc, "operations"),
                factory.createIntCounter("gets", getsDesc, "operations"),
                factory.createIntCounter("misses", missesDesc, "operations"),
                factory.createIntCounter("creates", createsDesc, "operations"),
                factory.createIntCounter("puts", putsDesc, "operations"),
                factory.createLongCounter("putTime", putTimeDesc, "nanoseconds", false),
                factory.createIntCounter("putalls", putallsDesc, "operations"),
                factory.createLongCounter("putallTime", putallTimeDesc, "nanoseconds", false),
                factory.createIntCounter("removeAlls", removeAllsDesc, "operations"),
                factory.createLongCounter("removeAllTime", removeAllTimeDesc, "nanoseconds", false),
                factory.createLongCounter("getTime", getTimeDesc, "nanoseconds", false),
                factory.createIntGauge("eventQueueSize", eventQueueSizeDesc, "messages"),
                factory.createIntGauge("eventQueueThrottleCount", eventQueueThrottleCountDesc,
                    "delays"),
                factory.createLongCounter("eventQueueThrottleTime", eventQueueThrottleTimeDesc,
                    "nanoseconds",
                    false),
                factory.createIntGauge("eventThreads", eventThreadsDesc, "threads"),
                factory.createIntCounter("queryExecutions", queryExecutionsDesc, "operations"),
                factory.createLongCounter("queryExecutionTime", queryExecutionTimeDesc,
                    "nanoseconds"),
                factory.createIntCounter("queryResultsHashCollisions",
                    queryResultsHashCollisionsDesc,
                    "operations"),
                factory.createLongCounter("queryResultsHashCollisionProbeTime",
                    queryResultsHashCollisionProbeTimeDesc, "nanoseconds"),
                factory.createLongCounter("partitionedRegionQueryRetries",
                    partitionedRegionOQLQueryRetriesDesc, "retries"),

                factory.createIntCounter("txCommits", txCommitsDesc, "commits"),
                factory.createIntCounter("txCommitChanges", txCommitChangesDesc, "changes"),
                factory.createLongCounter("txCommitTime", txCommitTimeDesc, "nanoseconds", false),
                factory.createLongCounter("txSuccessLifeTime", txSuccessLifeTimeDesc, "nanoseconds",
                    false),

                factory.createIntCounter("txFailures", txFailuresDesc, "failures"),
                factory.createIntCounter("txFailureChanges", txFailureChangesDesc, "changes"),
                factory.createLongCounter("txFailureTime", txFailureTimeDesc, "nanoseconds", false),
                factory.createLongCounter("txFailedLifeTime", txFailedLifeTimeDesc, "nanoseconds",
                    false),

                factory.createIntCounter("txRollbacks", txRollbacksDesc, "rollbacks"),
                factory.createIntCounter("txRollbackChanges", txRollbackChangesDesc, "changes"),
                factory.createLongCounter("txRollbackTime", txRollbackTimeDesc, "nanoseconds",
                    false),
                factory.createLongCounter("txRollbackLifeTime", txRollbackLifeTimeDesc,
                    "nanoseconds", false),
                factory.createLongCounter("txConflictCheckTime", txConflictCheckTimeDesc,
                    "nanoseconds",
                    false),

                factory.createIntGauge("reliableQueuedOps", reliableQueuedOpsDesc, "operations"),
                factory.createIntGauge("reliableQueueSize", reliableQueueSizeDesc, "megabytes"),
                factory.createIntGauge("reliableQueueMax", reliableQueueMaxDesc, "megabytes"),
                factory.createIntGauge("reliableRegions", reliableRegionsDesc, "regions"),
                factory.createIntGauge("reliableRegionsMissing", reliableRegionsMissingDesc,
                    "regions"),
                factory.createIntGauge("reliableRegionsQueuing", reliableRegionsQueuingDesc,
                    "regions"),
                factory.createIntGauge("reliableRegionsMissingFullAccess",
                    reliableRegionsMissingFullAccessDesc, "regions"),
                factory.createIntGauge("reliableRegionsMissingLimitedAccess",
                    reliableRegionsMissingLimitedAccessDesc, "regions"),
                factory.createIntGauge("reliableRegionsMissingNoAccess",
                    reliableRegionsMissingNoAccessDesc,
                    "regions"),
                factory.createLongGauge("entries",
                    "Current number of entries in the cache. This does not include any entries that are tombstones. See tombstoneCount.",
                    "entries"),
                factory.createLongCounter("eventsQueued",
                    "Number of events attached to " + "other events for callback invocation",
                    "events"),
                factory.createIntCounter("retries",
                    "Number of times a concurrent destroy followed by a create has caused an entry operation to need to retry.",
                    "operations"),
                factory.createIntCounter("clears", clearsDesc, "operations"),
                factory.createIntGauge("diskTasksWaiting",
                    "Current number of disk tasks (oplog compactions, asynchronous recoveries, etc) that are waiting for a thread to run the operation",
                    "operations"),
                factory.createLongCounter("conflatedEvents", conflatedEventsDesc, "operations"),
                factory.createIntGauge("tombstones", tombstoneCountDesc, "entries"),
                factory.createIntCounter("tombstoneGCs", tombstoneGCCountDesc, "operations"),
                factory.createLongGauge("replicatedTombstonesSize", tombstoneOverhead1Desc,
                    "bytes"),
                factory.createLongGauge("nonReplicatedTombstonesSize", tombstoneOverhead2Desc,
                    "bytes"),
                factory.createIntCounter("clearTimeouts", clearTimeoutsDesc, "timeouts"),
                factory.createIntGauge("evictorJobsStarted", "Number of evictor jobs started",
                    "jobs"),
                factory.createIntGauge("evictorJobsCompleted", "Number of evictor jobs completed",
                    "jobs"),
                factory.createIntGauge("evictorQueueSize",
                    "Number of jobs waiting to be picked up by evictor threads", "jobs"),
                factory.createLongCounter("evictWorkTime",
                    "Total time spent doing eviction work in background threads", "nanoseconds",
                    false),
                factory.createLongCounter("nonSingleHopsCount", nonSingleHopsDesc,
                    "Total number of times client request observed more than one hop during operation.",
                    false),
                factory.createLongCounter("metaDataRefreshCount", metaDataRefreshCountDesc,
                    "Total number of times the meta data is refreshed due to hopping.", false),
                factory.createIntCounter("deltaUpdates", deltaUpdatesDesc, "operations"),
                factory.createLongCounter("deltaUpdatesTime", deltaUpdatesTimeDesc, "nanoseconds",
                    false),
                factory.createIntCounter("deltaFailedUpdates", deltaFailedUpdatesDesc,
                    "operations"),
                factory.createIntCounter("deltasPrepared", deltasPreparedDesc, "operations"),
                factory.createLongCounter("deltasPreparedTime", deltasPreparedTimeDesc,
                    "nanoseconds", false),
                factory.createIntCounter("deltasSent", deltasSentDesc, "operations"),
                factory.createIntCounter("deltaFullValuesSent", deltaFullValuesSentDesc,
                    "operations"),
                factory.createIntCounter("deltaFullValuesRequested", deltaFullValuesRequestedDesc,
                    "operations"),

                factory.createLongCounter("importedEntries", importedEntriesCountDesc, "entries"),
                factory.createLongCounter("importTime", importTimeDesc, "nanoseconds"),
                factory.createLongCounter("exportedEntries", exportedEntriesCountDesc, "entries"),
                factory.createLongCounter("exportTime", exportTimeDesc, "nanoseconds"),

                factory.createLongCounter("compressTime", compressionCompressTimeDesc,
                    "nanoseconds"),
                factory.createLongCounter("decompressTime", compressionDecompressTimeDesc,
                    "nanoseconds"),
                factory.createLongCounter("compressions", compressionCompressionsDesc,
                    "operations"),
                factory.createLongCounter("decompressions", compressionDecompressionsDesc,
                    "operations"),
                factory.createLongCounter("preCompressedBytes", compressionPreCompresssedBytesDesc,
                    "bytes"),
                factory.createLongCounter("postCompressedBytes", compressionPostCompressedBytesDesc,
                    "bytes"),

                factory.createLongCounter("evictByCriteria_evictions",
                    evictByCriteria_evictionsDesc,
                    "operations"),
                factory.createLongCounter("evictByCriteria_evictionTime",
                    evictByCriteria_evictionTimeDesc,
                    "nanoseconds"),
                factory.createLongCounter("evictByCriteria_evictionsInProgress",
                    evictByCriteria_evictionsInProgressDesc, "operations"),
                factory.createLongCounter("evictByCriteria_evaluations",
                    evictByCriteria_evaluationsDesc,
                    "operations"),
                factory.createLongCounter("evictByCriteria_evaluationTime",
                    evictByCriteria_evaluationTimeDesc, "nanoseconds")});

    // Initialize id fields
    loadsInProgressId = type.nameToId("loadsInProgress");
    loadsCompletedId = type.nameToId("loadsCompleted");
    loadTimeId = type.nameToId("loadTime");
    netloadsInProgressId = type.nameToId("netloadsInProgress");
    netloadsCompletedId = type.nameToId("netloadsCompleted");
    netloadTimeId = type.nameToId("netloadTime");
    netsearchesInProgressId = type.nameToId("netsearchesInProgress");
    netsearchesCompletedId = type.nameToId("netsearchesCompleted");
    netsearchTimeId = type.nameToId("netsearchTime");
    cacheWriterCallsInProgressId = type.nameToId("cacheWriterCallsInProgress");
    cacheWriterCallsCompletedId = type.nameToId("cacheWriterCallsCompleted");
    cacheWriterCallTimeId = type.nameToId("cacheWriterCallTime");
    cacheListenerCallsInProgressId = type.nameToId("cacheListenerCallsInProgress");
    cacheListenerCallsCompletedId = type.nameToId("cacheListenerCallsCompleted");
    cacheListenerCallTimeId = type.nameToId("cacheListenerCallTime");
    indexUpdateInProgressId = type.nameToId("indexUpdateInProgress");
    indexUpdateCompletedId = type.nameToId("indexUpdateCompleted");
    indexUpdateTimeId = type.nameToId("indexUpdateTime");
    indexInitializationTimeId = type.nameToId("indexInitializationTime");
    indexInitializationInProgressId = type.nameToId("indexInitializationInProgress");
    indexInitializationCompletedId = type.nameToId("indexInitializationCompleted");
    getInitialImagesInProgressId = type.nameToId("getInitialImagesInProgress");
    getInitialImagesCompletedId = type.nameToId("getInitialImagesCompleted");
    deltaGetInitialImagesCompletedId = type.nameToId("deltaGetInitialImagesCompleted");
    getInitialImageTimeId = type.nameToId("getInitialImageTime");
    getInitialImageKeysReceivedId = type.nameToId("getInitialImageKeysReceived");
    regionsId = type.nameToId("regions");
    partitionedRegionsId = type.nameToId("partitionedRegions");
    destroysId = type.nameToId("destroys");
    createsId = type.nameToId("creates");
    putsId = type.nameToId("puts");
    putTimeId = type.nameToId("putTime");
    putallsId = type.nameToId("putalls");
    putallTimeId = type.nameToId("putallTime");
    removeAllsId = type.nameToId("removeAlls");
    removeAllTimeId = type.nameToId("removeAllTime");
    updatesId = type.nameToId("updates");
    updateTimeId = type.nameToId("updateTime");
    invalidatesId = type.nameToId("invalidates");
    getsId = type.nameToId("gets");
    getTimeId = type.nameToId("getTime");
    missesId = type.nameToId("misses");
    eventQueueSizeId = type.nameToId("eventQueueSize");
    eventQueueThrottleTimeId = type.nameToId("eventQueueThrottleTime");
    eventQueueThrottleCountId = type.nameToId("eventQueueThrottleCount");
    eventThreadsId = type.nameToId("eventThreads");
    queryExecutionsId = type.nameToId("queryExecutions");
    queryExecutionTimeId = type.nameToId("queryExecutionTime");
    queryResultsHashCollisionsId = type.nameToId("queryResultsHashCollisions");
    queryResultsHashCollisionProbeTimeId = type.nameToId("queryResultsHashCollisionProbeTime");
    partitionedRegionQueryRetriesId = type.nameToId("partitionedRegionQueryRetries");

    txSuccessLifeTimeId = type.nameToId("txSuccessLifeTime");
    txFailedLifeTimeId = type.nameToId("txFailedLifeTime");
    txRollbackLifeTimeId = type.nameToId("txRollbackLifeTime");
    txCommitsId = type.nameToId("txCommits");
    txFailuresId = type.nameToId("txFailures");
    txRollbacksId = type.nameToId("txRollbacks");
    txCommitTimeId = type.nameToId("txCommitTime");
    txFailureTimeId = type.nameToId("txFailureTime");
    txRollbackTimeId = type.nameToId("txRollbackTime");
    txCommitChangesId = type.nameToId("txCommitChanges");
    txFailureChangesId = type.nameToId("txFailureChanges");
    txRollbackChangesId = type.nameToId("txRollbackChanges");
    txConflictCheckTimeId = type.nameToId("txConflictCheckTime");

    reliableQueuedOpsId = type.nameToId("reliableQueuedOps");
    reliableQueueSizeId = type.nameToId("reliableQueueSize");
    reliableQueueMaxId = type.nameToId("reliableQueueMax");
    reliableRegionsId = type.nameToId("reliableRegions");
    reliableRegionsMissingId = type.nameToId("reliableRegionsMissing");
    reliableRegionsQueuingId = type.nameToId("reliableRegionsQueuing");
    reliableRegionsMissingFullAccessId = type.nameToId("reliableRegionsMissingFullAccess");
    reliableRegionsMissingLimitedAccessId = type.nameToId("reliableRegionsMissingLimitedAccess");
    reliableRegionsMissingNoAccessId = type.nameToId("reliableRegionsMissingNoAccess");
    entryCountId = type.nameToId("entries");

    eventsQueuedId = type.nameToId("eventsQueued");

    retriesId = type.nameToId("retries");
    clearsId = type.nameToId("clears");

    diskTasksWaitingId = type.nameToId("diskTasksWaiting");
    evictorJobsStartedId = type.nameToId("evictorJobsStarted");
    evictorJobsCompletedId = type.nameToId("evictorJobsCompleted");
    evictorQueueSizeId = type.nameToId("evictorQueueSize");
    evictWorkTimeId = type.nameToId("evictWorkTime");

    metaDataRefreshCountId = type.nameToId("metaDataRefreshCount");

    conflatedEventsId = type.nameToId("conflatedEvents");
    tombstoneCountId = type.nameToId("tombstones");
    tombstoneGCCountId = type.nameToId("tombstoneGCs");
    tombstoneOverhead1Id = type.nameToId("replicatedTombstonesSize");
    tombstoneOverhead2Id = type.nameToId("nonReplicatedTombstonesSize");
    clearTimeoutsId = type.nameToId("clearTimeouts");

    deltaUpdatesId = type.nameToId("deltaUpdates");
    deltaUpdatesTimeId = type.nameToId("deltaUpdatesTime");
    deltaFailedUpdatesId = type.nameToId("deltaFailedUpdates");

    deltasPreparedId = type.nameToId("deltasPrepared");
    deltasPreparedTimeId = type.nameToId("deltasPreparedTime");
    deltasSentId = type.nameToId("deltasSent");

    deltaFullValuesSentId = type.nameToId("deltaFullValuesSent");
    deltaFullValuesRequestedId = type.nameToId("deltaFullValuesRequested");

    importedEntriesCountId = type.nameToId("importedEntries");
    importTimeId = type.nameToId("importTime");
    exportedEntriesCountId = type.nameToId("exportedEntries");
    exportTimeId = type.nameToId("exportTime");

    compressionCompressTimeId = type.nameToId("compressTime");
    compressionDecompressTimeId = type.nameToId("decompressTime");
    compressionCompressionsId = type.nameToId("compressions");
    compressionDecompressionsId = type.nameToId("decompressions");
    compressionPreCompressedBytesId = type.nameToId("preCompressedBytes");
    compressionPostCompressedBytesId = type.nameToId("postCompressedBytes");
  }

  //////////////////////// Constructors ////////////////////////

  /**
   * Created specially for bug 39348. Should not be invoked in any other case.
   */
  public CachePerfStatsImpl() {
    stats = null;
  }

  /**
   * Creates a new <code>CachePerfStats</code> and registers itself with the given statistics
   * factory.
   */
  public CachePerfStatsImpl(StatisticsFactory factory) {
    initializeStats(factory, "");
    stats = factory.createAtomicStatistics(type, "cachePerfStats");
  }

  /**
   * Creates a new <code>CachePerfStats</code> and registers itself with the given statistics
   * factory.
   */
  public CachePerfStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory, name);
    stats = factory.createAtomicStatistics(type, "RegionStats-" + name);
  }

  ////////////////////// Accessing Stats //////////////////////

  @Override
  public int getLoadsInProgress() {
    return stats.getInt(loadsInProgressId);
  }

  @Override
  public int getLoadsCompleted() {
    return stats.getInt(loadsCompletedId);
  }

  @Override
  public long getLoadTime() {
    return stats.getLong(loadTimeId);
  }

  @Override
  public int getNetloadsInProgress() {
    return stats.getInt(netloadsInProgressId);
  }

  @Override
  public int getNetloadsCompleted() {
    return stats.getInt(netloadsCompletedId);
  }

  @Override
  public long getNetloadTime() {
    return stats.getLong(netloadTimeId);
  }

  @Override
  public int getNetsearchesInProgress() {
    return stats.getInt(netsearchesInProgressId);
  }

  @Override
  public int getNetsearchesCompleted() {
    return stats.getInt(netsearchesCompletedId);
  }

  @Override
  public long getNetsearchTime() {
    return stats.getLong(netsearchTimeId);
  }

  @Override
  public int getGetInitialImagesInProgress() {
    return stats.getInt(getInitialImagesInProgressId);
  }

  @Override
  public int getGetInitialImagesCompleted() {
    return stats.getInt(getInitialImagesCompletedId);
  }

  @Override
  public int getDeltaGetInitialImagesCompleted() {
    return stats.getInt(deltaGetInitialImagesCompletedId);
  }

  @Override
  public long getGetInitialImageTime() {
    return stats.getLong(getInitialImageTimeId);
  }

  @Override
  public int getGetInitialImageKeysReceived() {
    return stats.getInt(getInitialImageKeysReceivedId);
  }

  @Override
  public int getRegions() {
    return stats.getInt(regionsId);
  }

  @Override
  public int getPartitionedRegions() {
    return stats.getInt(partitionedRegionsId);
  }

  @Override
  public int getDestroys() {
    return stats.getInt(destroysId);
  }

  @Override
  public int getCreates() {
    return stats.getInt(createsId);
  }

  @Override
  public int getPuts() {
    return stats.getInt(putsId);
  }

  @Override
  public int getPutAlls() {
    return stats.getInt(putallsId);
  }

  @Override
  public int getRemoveAlls() {
    return stats.getInt(removeAllsId);
  }

  @Override
  public int getUpdates() {
    return stats.getInt(updatesId);
  }

  @Override
  public int getInvalidates() {
    return stats.getInt(invalidatesId);
  }

  @Override
  public int getGets() {
    return stats.getInt(getsId);
  }

  @Override
  public int getMisses() {
    return stats.getInt(missesId);
  }

  @Override
  public int getReliableQueuedOps() {
    return stats.getInt(reliableQueuedOpsId);
  }

  @Override
  public void incReliableQueuedOps(int inc) {
    stats.incInt(reliableQueuedOpsId, inc);
  }

  @Override
  public int getReliableQueueSize() {
    return stats.getInt(reliableQueueSizeId);
  }

  @Override
  public void incReliableQueueSize(int inc) {
    stats.incInt(reliableQueueSizeId, inc);
  }

  @Override
  public int getReliableQueueMax() {
    return stats.getInt(reliableQueueMaxId);
  }

  @Override
  public void incReliableQueueMax(int inc) {
    stats.incInt(reliableQueueMaxId, inc);
  }

  @Override
  public int getReliableRegions() {
    return stats.getInt(reliableRegionsId);
  }

  @Override
  public void incReliableRegions(int inc) {
    stats.incInt(reliableRegionsId, inc);
  }

  @Override
  public int getReliableRegionsMissing() {
    return stats.getInt(reliableRegionsMissingId);
  }

  @Override
  public void incReliableRegionsMissing(int inc) {
    stats.incInt(reliableRegionsMissingId, inc);
  }

  @Override
  public int getReliableRegionsQueuing() {
    return stats.getInt(reliableRegionsQueuingId);
  }

  @Override
  public void incReliableRegionsQueuing(int inc) {
    stats.incInt(reliableRegionsQueuingId, inc);
  }

  @Override
  public int getReliableRegionsMissingFullAccess() {
    return stats.getInt(reliableRegionsMissingFullAccessId);
  }

  @Override
  public void incReliableRegionsMissingFullAccess(int inc) {
    stats.incInt(reliableRegionsMissingFullAccessId, inc);
  }

  @Override
  public int getReliableRegionsMissingLimitedAccess() {
    return stats.getInt(reliableRegionsMissingLimitedAccessId);
  }

  @Override
  public void incReliableRegionsMissingLimitedAccess(int inc) {
    stats.incInt(reliableRegionsMissingLimitedAccessId, inc);
  }

  @Override
  public int getReliableRegionsMissingNoAccess() {
    return stats.getInt(reliableRegionsMissingNoAccessId);
  }

  @Override
  public void incReliableRegionsMissingNoAccess(int inc) {
    stats.incInt(reliableRegionsMissingNoAccessId, inc);
  }

  @Override
  public void incQueuedEvents(int inc) {
    this.stats.incLong(eventsQueuedId, inc);
  }

  @Override
  public long getQueuedEvents() {
    return this.stats.getInt(eventsQueuedId);
  }

  @Override
  public int getDeltaUpdates() {
    return stats.getInt(deltaUpdatesId);
  }

  @Override
  public long getDeltaUpdatesTime() {
    return stats.getLong(deltaUpdatesTimeId);
  }

  @Override
  public int getDeltaFailedUpdates() {
    return stats.getInt(deltaFailedUpdatesId);
  }

  @Override
  public int getDeltasPrepared() {
    return stats.getInt(deltasPreparedId);
  }

  @Override
  public long getDeltasPreparedTime() {
    return stats.getLong(deltasPreparedTimeId);
  }

  @Override
  public int getDeltasSent() {
    return stats.getInt(deltasSentId);
  }

  @Override
  public int getDeltaFullValuesSent() {
    return stats.getInt(deltaFullValuesSentId);
  }

  @Override
  public int getDeltaFullValuesRequested() {
    return stats.getInt(deltaFullValuesRequestedId);
  }

  @Override
  public long getTotalCompressionTime() {
    return stats.getLong(compressionCompressTimeId);
  }

  @Override
  public long getTotalDecompressionTime() {
    return stats.getLong(compressionDecompressTimeId);
  }

  @Override
  public long getTotalCompressions() {
    return stats.getLong(compressionCompressionsId);
  }

  @Override
  public long getTotalDecompressions() {
    return stats.getLong(compressionDecompressionsId);
  }

  @Override
  public long getTotalPreCompressedBytes() {
    return stats.getLong(compressionPreCompressedBytesId);
  }

  @Override
  public long getTotalPostCompressedBytes() {
    return stats.getLong(compressionPostCompressedBytesId);
  }

  ////////////////////// Updating Stats //////////////////////

  @Override
  public long startCompression() {
    stats.incLong(compressionCompressionsId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endCompression(long startTime, long startSize, long endSize) {
    if (enableClockStats) {
      stats.incLong(compressionCompressTimeId, CachePerfStats.getStatTime() - startTime);
    }
    stats.incLong(compressionPreCompressedBytesId, startSize);
    stats.incLong(compressionPostCompressedBytesId, endSize);
  }

  @Override
  public long startDecompression() {
    stats.incLong(compressionDecompressionsId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endDecompression(long startTime) {
    if (enableClockStats) {
      stats.incLong(compressionDecompressTimeId, CachePerfStats.getStatTime() - startTime);
    }
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startLoad() {
    stats.incInt(loadsInProgressId, 1);
    return NanoTimer.getTime(); // don't use getStatTime so always enabled
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endLoad(long start) {
    // note that load times are used in health checks and
    // should not be disabled by enableClockStats==false
    long ts = NanoTimer.getTime(); // don't use getStatTime so always enabled
    stats.incLong(loadTimeId, ts - start);
    stats.incInt(loadsInProgressId, -1);
    stats.incInt(loadsCompletedId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startNetload() {
    stats.incInt(netloadsInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNetload(long start) {
    if (enableClockStats) {
      stats.incLong(netloadTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(netloadsInProgressId, -1);
    stats.incInt(netloadsCompletedId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startNetsearch() {
    stats.incInt(netsearchesInProgressId, 1);
    return NanoTimer.getTime(); // don't use getStatTime so always enabled
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNetsearch(long start) {
    // note that netsearch is used in health checks and timings should
    // not be disabled by enableClockStats==false
    long ts = NanoTimer.getTime(); // don't use getStatTime so always enabled
    stats.incLong(netsearchTimeId, ts - start);
    stats.incInt(netsearchesInProgressId, -1);
    stats.incInt(netsearchesCompletedId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startCacheWriterCall() {
    stats.incInt(cacheWriterCallsInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endCacheWriterCall(long start) {
    if (enableClockStats) {
      stats.incLong(cacheWriterCallTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(cacheWriterCallsInProgressId, -1);
    stats.incInt(cacheWriterCallsCompletedId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   * @since GemFire 3.5
   */
  @Override
  public long startCacheListenerCall() {
    stats.incInt(cacheListenerCallsInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   * @since GemFire 3.5
   */
  @Override
  public void endCacheListenerCall(long start) {
    if (enableClockStats) {
      stats.incLong(cacheListenerCallTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(cacheListenerCallsInProgressId, -1);
    stats.incInt(cacheListenerCallsCompletedId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startGetInitialImage() {
    stats.incInt(getInitialImagesInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endGetInitialImage(long start) {
    if (enableClockStats) {
      stats.incLong(getInitialImageTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(getInitialImagesInProgressId, -1);
    stats.incInt(getInitialImagesCompletedId, 1);
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endNoGIIDone(long start) {
    if (enableClockStats) {
      stats.incLong(getInitialImageTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(getInitialImagesInProgressId, -1);
  }

  @Override
  public void incDeltaGIICompleted() {
    stats.incInt(deltaGetInitialImagesCompletedId, 1);
  }

  @Override
  public void incGetInitialImageKeysReceived() {
    stats.incInt(getInitialImageKeysReceivedId, 1);
  }

  @Override
  public long startIndexUpdate() {
    stats.incInt(indexUpdateInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endIndexUpdate(long start) {
    long ts = CachePerfStats.getStatTime();
    stats.incLong(indexUpdateTimeId, ts - start);
    stats.incInt(indexUpdateInProgressId, -1);
    stats.incInt(indexUpdateCompletedId, 1);
  }

  @Override
  public long startIndexInitialization() {
    stats.incInt(indexInitializationInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endIndexInitialization(long start) {
    long ts = CachePerfStats.getStatTime();
    stats.incLong(indexInitializationTimeId, ts - start);
    stats.incInt(indexInitializationInProgressId, -1);
    stats.incInt(indexInitializationCompletedId, 1);
  }

  @Override
  public long getIndexInitializationTime() {
    return stats.getLong(indexInitializationTimeId);
  }

  @Override
  public void incRegions(int inc) {
    stats.incInt(regionsId, inc);
  }

  @Override
  public void incPartitionedRegions(int inc) {
    stats.incInt(partitionedRegionsId, inc);
  }

  @Override
  public void incDestroys() {
    stats.incInt(destroysId, 1);
  }

  @Override
  public void incCreates() {
    stats.incInt(createsId, 1);
  }

  @Override
  public void incInvalidates() {
    stats.incInt(invalidatesId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startGet() {
    return CachePerfStats.getStatTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  @Override
  public void endGet(long start, boolean miss) {
    if (enableClockStats) {
      stats.incLong(getTimeId, CachePerfStats.getStatTime() - start);
    }
    stats.incInt(getsId, 1);
    if (miss) {
      stats.incInt(missesId, 1);
    }
  }

  /**
   * @param start the timestamp taken when the operation started
   * @param isUpdate true if the put was an update (origin remote)
   */
  @Override
  public long endPut(long start, boolean isUpdate) {
    long total = 0;
    if (isUpdate) {
      stats.incInt(updatesId, 1);
      if (enableClockStats) {
        total = CachePerfStats.getStatTime() - start;
        stats.incLong(updateTimeId, total);
      }
    } else {
      stats.incInt(putsId, 1);
      if (enableClockStats) {
        total = CachePerfStats.getStatTime() - start;
        stats.incLong(putTimeId, total);
      }
    }
    return total;
  }

  @Override
  public void endPutAll(long start) {
    stats.incInt(putallsId, 1);
    if (enableClockStats) {
      stats.incLong(putallTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void endRemoveAll(long start) {
    stats.incInt(removeAllsId, 1);
    if (enableClockStats) {
      stats.incLong(removeAllTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void endQueryExecution(long executionTime) {
    stats.incInt(queryExecutionsId, 1);
    if (enableClockStats) {
      stats.incLong(queryExecutionTimeId, executionTime);
    }
  }

  @Override
  public void endQueryResultsHashCollisionProbe(long start) {
    if (enableClockStats) {
      stats.incLong(queryResultsHashCollisionProbeTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void incQueryResultsHashCollisions() {
    stats.incInt(queryResultsHashCollisionsId, 1);
  }

  @Override
  public int getTxCommits() {
    return stats.getInt(txCommitsId);
  }

  @Override
  public int getTxCommitChanges() {
    return stats.getInt(txCommitChangesId);
  }

  @Override
  public long getTxCommitTime() {
    return stats.getLong(txCommitTimeId);
  }

  @Override
  public long getTxSuccessLifeTime() {
    return stats.getLong(txSuccessLifeTimeId);
  }

  @Override
  public int getTxFailures() {
    return stats.getInt(txFailuresId);
  }

  @Override
  public int getTxFailureChanges() {
    return stats.getInt(txFailureChangesId);
  }

  @Override
  public long getTxFailureTime() {
    return stats.getLong(txFailureTimeId);
  }

  @Override
  public long getTxFailedLifeTime() {
    return stats.getLong(txFailedLifeTimeId);
  }

  @Override
  public int getTxRollbacks() {
    return stats.getInt(txRollbacksId);
  }

  @Override
  public int getTxRollbackChanges() {
    return stats.getInt(txRollbackChangesId);
  }

  @Override
  public long getTxRollbackTime() {
    return stats.getLong(txRollbackTimeId);
  }

  @Override
  public long getTxRollbackLifeTime() {
    return stats.getLong(txRollbackLifeTimeId);
  }

  @Override
  public void incTxConflictCheckTime(long delta) {
    stats.incLong(txConflictCheckTimeId, delta);
  }

  @Override
  public void txSuccess(long opTime, long txLifeTime, int txChanges) {
    stats.incInt(txCommitsId, 1);
    stats.incInt(txCommitChangesId, txChanges);
    stats.incLong(txCommitTimeId, opTime);
    stats.incLong(txSuccessLifeTimeId, txLifeTime);
  }

  @Override
  public void txFailure(long opTime, long txLifeTime, int txChanges) {
    stats.incInt(txFailuresId, 1);
    stats.incInt(txFailureChangesId, txChanges);
    stats.incLong(txFailureTimeId, opTime);
    stats.incLong(txFailedLifeTimeId, txLifeTime);
  }

  @Override
  public void txRollback(long opTime, long txLifeTime, int txChanges) {
    stats.incInt(txRollbacksId, 1);
    stats.incInt(txRollbackChangesId, txChanges);
    stats.incLong(txRollbackTimeId, opTime);
    stats.incLong(txRollbackLifeTimeId, txLifeTime);
  }

  @Override
  public void endDeltaUpdate(long start) {
    stats.incInt(deltaUpdatesId, 1);
    if (enableClockStats) {
      stats.incLong(deltaUpdatesTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void incDeltaFailedUpdates() {
    stats.incInt(deltaFailedUpdatesId, 1);
  }

  @Override
  public void endDeltaPrepared(long start) {
    stats.incInt(deltasPreparedId, 1);
    if (enableClockStats) {
      stats.incLong(deltasPreparedTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void incDeltasSent() {
    stats.incInt(deltasSentId, 1);
  }

  @Override
  public void incDeltaFullValuesSent() {
    stats.incInt(deltaFullValuesSentId, 1);
  }

  @Override
  public void incDeltaFullValuesRequested() {
    stats.incInt(deltaFullValuesRequestedId, 1);
  }

  /**
   * Closes these stats so that they can not longer be used. The stats are closed when the cache is
   * closed.
   * @since GemFire 3.5
   */
  @Override
  public void close() {
    this.stats.close();
  }

  /**
   * Returns whether or not these stats have been closed
   * @since GemFire 3.5
   */
  @Override
  public boolean isClosed() {
    return this.stats.isClosed();
  }

  @Override
  public int getEventQueueSize() {
    return this.stats.getInt(eventQueueSizeId);
  }

  @Override
  public void incEventQueueSize(int items) {
    this.stats.incInt(eventQueueSizeId, items);
  }

  @Override
  public void incEventQueueThrottleCount(int items) {
    this.stats.incInt(eventQueueThrottleCountId, items);
  }

  @Override
  public void incEventQueueThrottleTime(long nanos) {
    this.stats.incLong(eventQueueThrottleTimeId, nanos);
  }

  @Override
  public void incEventThreads(int items) {
    this.stats.incInt(eventThreadsId, items);
  }

  @Override
  public void incEntryCount(int delta) {
    this.stats.incLong(entryCountId, delta);
  }

  @Override
  public long getEntries() {
    return this.stats.getLong(entryCountId);
  }

  @Override
  public void incRetries() {
    this.stats.incInt(retriesId, 1);
  }

  @Override
  public void incDiskTasksWaiting() {
    this.stats.incInt(diskTasksWaitingId, 1);
  }

  @Override
  public void decDiskTasksWaiting() {
    this.stats.incInt(diskTasksWaitingId, -1);
  }

  @Override
  public int getDiskTasksWaiting() {
    return this.stats.getInt(diskTasksWaitingId);
  }

  @Override
  public void decDiskTasksWaiting(int count) {
    this.stats.incInt(diskTasksWaitingId, -count);
  }

  @Override
  public void incEvictorJobsStarted() {
    this.stats.incInt(evictorJobsStartedId, 1);
  }

  @Override
  public void incEvictorJobsCompleted() {
    this.stats.incInt(evictorJobsCompletedId, 1);
  }

  @Override
  public void incEvictorQueueSize(int delta) {
    this.stats.incInt(evictorQueueSizeId, delta);
  }

  @Override
  public void incEvictWorkTime(long delta) {
    this.stats.incLong(evictWorkTimeId, delta);
  }

  /**
   * Returns the Statistics instance that stores the cache perf stats.
   * @since GemFire 3.5
   */
  @Override
  public Statistics getStats() {
    return this.stats;
  }

  // /**
  // * Returns a helper object so that the event queue can record its
  // * stats to the proper cache perf stats.
  // * @since GemFire 3.5
  // */
  // public ThrottledQueueStatHelper getEventQueueHelper() {
  // return new ThrottledQueueStatHelper() {
  // public void incThrottleCount() {
  // incEventQueueThrottleCount(1);
  // }
  // public void throttleTime(long nanos) {
  // incEventQueueThrottleTime(nanos);
  // }
  // public void add() {
  // incEventQueueSize(1);
  // }
  // public void remove() {
  // incEventQueueSize(-1);
  // }
  // public void remove(int count) {
  // incEventQueueSize(-count);
  // }
  // };
  // }

  /**
   * Returns a helper object so that the event pool can record its stats to the proper cache perf
   * stats.
   * @since GemFire 3.5
   */
  @Override
  public PoolStatHelper getEventPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incEventThreads(1);
      }

      public void endJob() {
        incEventThreads(-1);
      }
    };
  }

  @Override
  public int getClearCount() {
    return stats.getInt(clearsId);
  }

  @Override
  public void incClearCount() {
    this.stats.incInt(clearsId, 1);
  }

  @Override
  public long getConflatedEventsCount() {
    return stats.getLong(conflatedEventsId);
  }

  @Override
  public void incConflatedEventsCount() {
    this.stats.incLong(conflatedEventsId, 1);
  }

  @Override
  public int getTombstoneCount() {
    return this.stats.getInt(tombstoneCountId);
  }

  @Override
  public void incTombstoneCount(int amount) {
    this.stats.incInt(tombstoneCountId, amount);
  }

  @Override
  public int getTombstoneGCCount() {
    return this.stats.getInt(tombstoneGCCountId);
  }

  @Override
  public void incTombstoneGCCount() {
    this.stats.incInt(tombstoneGCCountId, 1);
  }

  @Override
  public void setReplicatedTombstonesSize(long size) {
    this.stats.setLong(tombstoneOverhead1Id, size);
  }

  @Override
  public long getReplicatedTombstonesSize() {
    return this.stats.getLong(tombstoneOverhead1Id);
  }

  @Override
  public void setNonReplicatedTombstonesSize(long size) {
    this.stats.setLong(tombstoneOverhead2Id, size);
  }

  @Override
  public long getNonReplicatedTombstonesSize() {
    return this.stats.getLong(tombstoneOverhead2Id);
  }

  @Override
  public int getClearTimeouts() {
    return this.stats.getInt(clearTimeoutsId);
  }

  @Override
  public void incClearTimeouts() {
    this.stats.incInt(clearTimeoutsId, 1);
  }

  @Override
  public void incPRQueryRetries() {
    this.stats.incLong(partitionedRegionQueryRetriesId, 1);
  }

  @Override
  public long getPRQueryRetries() {
    return this.stats.getLong(partitionedRegionQueryRetriesId);
  }

  @Override
  public QueueStatHelper getEvictionQueueStatHelper() {
    return new QueueStatHelper() {
      public void add() {
        incEvictorQueueSize(1);
      }

      public void remove() {
        incEvictorQueueSize(-1);
      }

      public void remove(int count) {
        incEvictorQueueSize(count * -1);
      }
    };
  }

  @Override
  public void incMetaDataRefreshCount() {
    this.stats.incLong(metaDataRefreshCountId, 1);
  }

  @Override
  public long getMetaDataRefreshCount() {
    return this.stats.getLong(metaDataRefreshCountId);
  }

  @Override
  public long getImportedEntriesCount() {
    return stats.getLong(importedEntriesCountId);
  }

  @Override
  public long getImportTime() {
    return stats.getLong(importTimeId);
  }

  @Override
  public void endImport(long entryCount, long start) {
    stats.incLong(importedEntriesCountId, entryCount);
    if (enableClockStats) {
      stats.incLong(importTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public long getExportedEntriesCount() {
    return stats.getLong(exportedEntriesCountId);
  }

  @Override
  public long getExportTime() {
    return stats.getLong(exportTimeId);
  }

  @Override
  public void endExport(long entryCount, long start) {
    stats.incLong(exportedEntriesCountId, entryCount);
    if (enableClockStats) {
      stats.incLong(exportTimeId, CachePerfStats.getStatTime() - start);
    }
  }

  @Override
  public void incEntryCompressedCount() {
    stats.setInt(compressionCompressionsId, 1);
  }

  @Override
  public void incEntryCompressedTime(long time) {
    stats.setLong(compressionCompressTimeId, time);
  }

  @Override
  public void incPostCompressedBytes(long endSize) {
    stats.setLong(compressionPostCompressedBytesId, endSize);
  }

  @Override
  public void incPreCompressedBytes(long startSize) {
    stats.setLong(compressionPreCompressedBytesId, startSize);
  }

  @Override
  public void incDecompressedEntryCount() {
    stats.setLong(compressionDecompressionsId,1);
  }

  @Override
  public void incDecompressionTime(long startTime) {
    stats.setLong(compressionDecompressTimeId,startTime);
  }
}