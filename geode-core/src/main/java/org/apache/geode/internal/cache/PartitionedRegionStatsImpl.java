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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.cache.Region;

/**
 * Represents a statistics type that can be archived to vsd. Loading of this class automatically
 * triggers statistics archival.
 * <p>
 *
 * A singleton instance can be requested with the initSingleton(...) and getSingleton() methods.
 * <p>
 *
 * Individual instances can be created with the constructor.
 * <p>
 *
 * To manipulate the statistic values, use (inc|dec|set|get)&lt;fieldName&gt; methods.
 * @since GemFire 5.0
 */
public class PartitionedRegionStatsImpl implements PartitionedRegionStats, GFSStatsImplementor {

  private StatisticsType type;

  private int dataStoreEntryCountId;
  private int dataStoreBytesInUseId;
  private int bucketCountId;

  private int putsCompletedId;
  private int putOpsRetriedId;
  private int putRetriesId;

  private int createsCompletedId;
  private int createOpsRetriedId;
  private int createRetriesId;

  private int preferredReadLocalId;
  private int preferredReadRemoteId;

  private int getsCompletedId;
  private int getOpsRetriedId;
  private int getRetriesId;

  private int destroysCompletedId;
  private int destroyOpsRetriedId;
  private int destroyRetriesId;

  private int invalidatesCompletedId;
  private int invalidateOpsRetriedId;
  private int invalidateRetriesId;

  private int containsKeyCompletedId;
  private int containsKeyOpsRetriedId;
  private int containsKeyRetriesId;

  private int containsValueForKeyCompletedId;

  private int partitionMessagesSentId;
  private int partitionMessagesReceivedId;
  private int partitionMessagesProcessedId;

  private int putTimeId;
  private int createTimeId;
  private int getTimeId;
  private int destroyTimeId;
  private int invalidateTimeId;
  private int containsKeyTimeId;
  private int containsValueForKeyTimeId;
  private int partitionMessagesProcessingTimeId;

  private static final String PUTALLS_COMPLETED = "putAllsCompleted";
  private static final String PUTALL_MSGS_RETRIED = "putAllMsgsRetried";
  private static final String PUTALL_RETRIES = "putAllRetries";
  private static final String PUTALL_TIME = "putAllTime";

  private int fieldId_PUTALLS_COMPLETED;
  private int fieldId_PUTALL_MSGS_RETRIED;
  private int fieldId_PUTALL_RETRIES;
  private int fieldId_PUTALL_TIME;

  private static final String REMOVE_ALLS_COMPLETED = "removeAllsCompleted";
  private static final String REMOVE_ALL_MSGS_RETRIED = "removeAllMsgsRetried";
  private static final String REMOVE_ALL_RETRIES = "removeAllRetries";
  private static final String REMOVE_ALL_TIME = "removeAllTime";

  private int fieldId_REMOVE_ALLS_COMPLETED;
  private int fieldId_REMOVE_ALL_MSGS_RETRIED;
  private int fieldId_REMOVE_ALL_RETRIES;
  private int fieldId_REMOVE_ALL_TIME;

  private int volunteeringInProgressId; // count of volunteering in progress
  private int volunteeringBecamePrimaryId; // ended as primary
  private int volunteeringBecamePrimaryTimeId; // time spent that ended as primary
  private int volunteeringOtherPrimaryId; // ended as not primary
  private int volunteeringOtherPrimaryTimeId; // time spent that ended as not primary
  private int volunteeringClosedId; // ended as closed
  private int volunteeringClosedTimeId; // time spent that ended as closed

  private int applyReplicationCompletedId;
  private int applyReplicationInProgressId;
  private int applyReplicationTimeId;
  private int sendReplicationCompletedId;
  private int sendReplicationInProgressId;
  private int sendReplicationTimeId;
  private int putRemoteCompletedId;
  private int putRemoteInProgressId;
  private int putRemoteTimeId;
  private int putLocalCompletedId;
  private int putLocalInProgressId;
  private int putLocalTimeId;

  private int totalNumBucketsId; // total number of buckets
  private int primaryBucketCountId; // number of hosted primary buckets
  private int volunteeringThreadsId; // number of threads actively volunteering
  private int lowRedundancyBucketCountId; // number of buckets currently without full
  // redundancy
  private int noCopiesBucketCountId; // number of buckets currently without any
  // redundancy

  private int configuredRedundantCopiesId;
  private int actualRedundantCopiesId;

  private int getEntriesCompletedId;
  private int getEntryTimeId;

  private int recoveriesInProgressId;
  private int recoveriesCompletedId;
  private int recoveriesTimeId;
  private int bucketCreatesInProgressId;
  private int bucketCreatesCompletedId;
  private int bucketCreatesFailedId;
  private int bucketCreateTimeId;

  private int rebalanceBucketCreatesInProgressId;
  private int rebalanceBucketCreatesCompletedId;
  private int rebalanceBucketCreatesFailedId;
  private int rebalanceBucketCreateTimeId;

  private int primaryTransfersInProgressId;
  private int primaryTransfersCompletedId;
  private int primaryTransfersFailedId;
  private int primaryTransferTimeId;

  private int rebalancePrimaryTransfersInProgressId;
  private int rebalancePrimaryTransfersCompletedId;
  private int rebalancePrimaryTransfersFailedId;
  private int rebalancePrimaryTransferTimeId;

  private int prMetaDataSentCountId;

  private int localMaxMemoryId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    final boolean largerIsBetter = true;
    type = factory.createType("PartitionedRegionStats",
        "Statistics for operations and connections in the Partitioned Region",
        new StatisticDescriptor[]{

            factory.createIntGauge("bucketCount", "Number of buckets in this node.", "buckets"),
            factory.createIntCounter("putsCompleted", "Number of puts completed.", "operations",
                largerIsBetter),
            factory.createIntCounter("putOpsRetried",
                "Number of put operations which had to be retried due to failures.", "operations",
                false),
            factory.createIntCounter("putRetries",
                "Total number of times put operations had to be retried.", "retry attempts", false),
            factory.createIntCounter("createsCompleted", "Number of creates completed.",
                "operations",
                largerIsBetter),
            factory.createIntCounter("createOpsRetried",
                "Number of create operations which had to be retried due to failures.",
                "operations", false),
            factory.createIntCounter("createRetries",
                "Total number of times put operations had to be retried.", "retry attempts", false),
            factory.createIntCounter("preferredReadLocal",
                "Number of reads satisfied from local store",
                "operations", largerIsBetter),
            factory.createIntCounter(PUTALLS_COMPLETED, "Number of putAlls completed.",
                "operations",
                largerIsBetter),
            factory.createIntCounter(PUTALL_MSGS_RETRIED,
                "Number of putAll messages which had to be retried due to failures.", "operations",
                false),
            factory.createIntCounter(PUTALL_RETRIES,
                "Total number of times putAll messages had to be retried.", "retry attempts",
                false),
            factory.createLongCounter(PUTALL_TIME, "Total time spent doing putAlls.", "nanoseconds",
                !largerIsBetter),
            factory.createIntCounter(REMOVE_ALLS_COMPLETED, "Number of removeAlls completed.",
                "operations", largerIsBetter),
            factory.createIntCounter(REMOVE_ALL_MSGS_RETRIED,
                "Number of removeAll messages which had to be retried due to failures.",
                "operations", false),
            factory.createIntCounter(REMOVE_ALL_RETRIES,
                "Total number of times removeAll messages had to be retried.", "retry attempts",
                false),
            factory.createLongCounter(REMOVE_ALL_TIME, "Total time spent doing removeAlls.",
                "nanoseconds", !largerIsBetter),
            factory.createIntCounter("preferredReadRemote",
                "Number of reads satisfied from remote store",
                "operations", false),
            factory.createIntCounter("getsCompleted", "Number of gets completed.", "operations",
                largerIsBetter),
            factory.createIntCounter("getOpsRetried",
                "Number of get operations which had to be retried due to failures.", "operations",
                false),
            factory.createIntCounter("getRetries",
                "Total number of times get operations had to be retried.", "retry attempts", false),
            factory.createIntCounter("destroysCompleted", "Number of destroys completed.",
                "operations",
                largerIsBetter),
            factory.createIntCounter("destroyOpsRetried",
                "Number of destroy operations which had to be retried due to failures.",
                "operations", false),
            factory.createIntCounter("destroyRetries",
                "Total number of times destroy operations had to be retried.", "retry attempts",
                false),
            factory.createIntCounter("invalidatesCompleted", "Number of invalidates completed.",
                "operations", largerIsBetter),

            factory.createIntCounter("invalidateOpsRetried",
                "Number of invalidate operations which had to be retried due to failures.",
                "operations", false),
            factory.createIntCounter("invalidateRetries",
                "Total number of times invalidate operations had to be retried.", "retry attempts",
                false),
            factory.createIntCounter("containsKeyCompleted", "Number of containsKeys completed.",
                "operations", largerIsBetter),

            factory.createIntCounter("containsKeyOpsRetried",
                "Number of containsKey or containsValueForKey operations which had to be retried due to failures.",
                "operations", false),
            factory.createIntCounter("containsKeyRetries",
                "Total number of times containsKey or containsValueForKey operations had to be retried.",
                "operations", false),
            factory.createIntCounter("containsValueForKeyCompleted",
                "Number of containsValueForKeys completed.", "operations", largerIsBetter),
            factory.createIntCounter("PartitionMessagesSent", "Number of PartitionMessages Sent.",
                "operations", largerIsBetter),
            factory.createIntCounter("PartitionMessagesReceived",
                "Number of PartitionMessages Received.",
                "operations", largerIsBetter),
            factory.createIntCounter("PartitionMessagesProcessed",
                "Number of PartitionMessages Processed.", "operations", largerIsBetter),
            factory.createLongCounter("putTime", "Total time spent doing puts.", "nanoseconds",
                false),
            factory.createLongCounter("createTime", "Total time spent doing create operations.",
                "nanoseconds", false),
            factory.createLongCounter("getTime", "Total time spent performing get operations.",
                "nanoseconds", false),
            factory.createLongCounter("destroyTime", "Total time spent doing destroys.",
                "nanoseconds",
                false),
            factory.createLongCounter("invalidateTime", "Total time spent doing invalidates.",
                "nanoseconds", false),
            factory.createLongCounter("containsKeyTime",
                "Total time spent performing containsKey operations.", "nanoseconds", false),
            factory.createLongCounter("containsValueForKeyTime",
                "Total time spent performing containsValueForKey operations.", "nanoseconds",
                false),
            factory.createLongCounter("partitionMessagesProcessingTime",
                "Total time spent on PartitionMessages processing.", "nanoseconds", false),
            factory.createIntGauge("dataStoreEntryCount",
                "The number of entries stored in this Cache for the named Partitioned Region. This does not include entries which are tombstones. See CachePerfStats.tombstoneCount.",
                "entries"),
            factory.createLongGauge("dataStoreBytesInUse",
                "The current number of bytes stored in this Cache for the named Partitioned Region",
                "bytes"),
            factory.createIntGauge("volunteeringInProgress",
                "Current number of attempts to volunteer for primary of a bucket.", "operations"),
            factory.createIntCounter("volunteeringBecamePrimary",
                "Total number of attempts to volunteer that ended when this member became primary.",
                "operations"),
            factory.createLongCounter("volunteeringBecamePrimaryTime",
                "Total time spent volunteering that ended when this member became primary.",
                "nanoseconds", false),
            factory.createIntCounter("volunteeringOtherPrimary",
                "Total number of attempts to volunteer that ended when this member discovered other primary.",
                "operations"),
            factory.createLongCounter("volunteeringOtherPrimaryTime",
                "Total time spent volunteering that ended when this member discovered other primary.",
                "nanoseconds", false),
            factory.createIntCounter("volunteeringClosed",
                "Total number of attempts to volunteer that ended when this member's bucket closed.",
                "operations"),
            factory.createLongCounter("volunteeringClosedTime",
                "Total time spent volunteering that ended when this member's bucket closed.",
                "nanoseconds", false),
            factory.createIntGauge("totalNumBuckets", "The total number of buckets.", "buckets"),
            factory.createIntGauge("primaryBucketCount",
                "Current number of primary buckets hosted locally.", "buckets"),
            factory.createIntGauge("volunteeringThreads",
                "Current number of threads volunteering for primary.", "threads"),
            factory.createIntGauge("lowRedundancyBucketCount",
                "Current number of buckets without full redundancy.", "buckets"),
            factory.createIntGauge("noCopiesBucketCount",
                "Current number of buckets without any copies remaining.", "buckets"),
            factory.createIntGauge("configuredRedundantCopies",
                "Configured number of redundant copies for this partitioned region.", "copies"),
            factory.createIntGauge("actualRedundantCopies",
                "Actual number of redundant copies for this partitioned region.", "copies"),
            factory.createIntCounter("getEntryCompleted",
                "Number of getEntry operations completed.",
                "operations", largerIsBetter),
            factory.createLongCounter("getEntryTime",
                "Total time spent performing getEntry operations.",
                "nanoseconds", false),

            factory.createIntGauge("recoveriesInProgress",
                "Current number of redundancy recovery operations in progress for this region.",
                "operations"),
            factory.createIntCounter("recoveriesCompleted",
                "Total number of redundancy recovery operations performed on this region.",
                "operations"),
            factory.createLongCounter("recoveryTime",
                "Total number time spent recovering redundancy.",
                "operations"),
            factory.createIntGauge("bucketCreatesInProgress",
                "Current number of bucket create operations being performed for rebalancing.",
                "operations"),
            factory.createIntCounter("bucketCreatesCompleted",
                "Total number of bucket create operations performed for rebalancing.",
                "operations"),
            factory.createIntCounter("bucketCreatesFailed",
                "Total number of bucket create operations performed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("bucketCreateTime",
                "Total time spent performing bucket create operations for rebalancing.",
                "nanoseconds", false),
            factory.createIntGauge("primaryTransfersInProgress",
                "Current number of primary transfer operations being performed for rebalancing.",
                "operations"),
            factory.createIntCounter("primaryTransfersCompleted",
                "Total number of primary transfer operations performed for rebalancing.",
                "operations"),
            factory.createIntCounter("primaryTransfersFailed",
                "Total number of primary transfer operations performed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("primaryTransferTime",
                "Total time spent performing primary transfer operations for rebalancing.",
                "nanoseconds", false),

            factory.createIntCounter("applyReplicationCompleted",
                "Total number of replicated values sent from a primary to this redundant data store.",
                "operations", largerIsBetter),
            factory.createIntGauge("applyReplicationInProgress",
                "Current number of replication operations in progress on this redundant data store.",
                "operations", !largerIsBetter),
            factory.createLongCounter("applyReplicationTime",
                "Total time spent storing replicated values on this redundant data store.",
                "nanoseconds", !largerIsBetter),
            factory.createIntCounter("sendReplicationCompleted",
                "Total number of replicated values sent from this primary to a redundant data store.",
                "operations", largerIsBetter),
            factory.createIntGauge("sendReplicationInProgress",
                "Current number of replication operations in progress from this primary.",
                "operations", !largerIsBetter),
            factory.createLongCounter("sendReplicationTime",
                "Total time spent replicating values from this primary to a redundant data store.",
                "nanoseconds", !largerIsBetter),
            factory.createIntCounter("putRemoteCompleted",
                "Total number of completed puts that did not originate in the primary. These puts require an extra network hop to the primary.",
                "operations", largerIsBetter),
            factory.createIntGauge("putRemoteInProgress",
                "Current number of puts in progress that did not originate in the primary.",
                "operations", !largerIsBetter),
            factory.createLongCounter("putRemoteTime",
                "Total time spent doing puts that did not originate in the primary.", "nanoseconds",
                !largerIsBetter),
            factory.createIntCounter("putLocalCompleted",
                "Total number of completed puts that did originate in the primary. These puts are optimal.",
                "operations", largerIsBetter),
            factory.createIntGauge("putLocalInProgress",
                "Current number of puts in progress that did originate in the primary.",
                "operations", !largerIsBetter),
            factory.createLongCounter("putLocalTime",
                "Total time spent doing puts that did originate in the primary.", "nanoseconds",
                !largerIsBetter),

            factory.createIntGauge("rebalanceBucketCreatesInProgress",
                "Current number of bucket create operations being performed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketCreatesCompleted",
                "Total number of bucket create operations performed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalanceBucketCreatesFailed",
                "Total number of bucket create operations performed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalanceBucketCreateTime",
                "Total time spent performing bucket create operations for rebalancing.",
                "nanoseconds", false),
            factory.createIntGauge("rebalancePrimaryTransfersInProgress",
                "Current number of primary transfer operations being performed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalancePrimaryTransfersCompleted",
                "Total number of primary transfer operations performed for rebalancing.",
                "operations"),
            factory.createIntCounter("rebalancePrimaryTransfersFailed",
                "Total number of primary transfer operations performed for rebalancing that failed.",
                "operations"),
            factory.createLongCounter("rebalancePrimaryTransferTime",
                "Total time spent performing primary transfer operations for rebalancing.",
                "nanoseconds", false),
            factory.createLongCounter("prMetaDataSentCount",
                "total number of times meta data refreshed sent on client's request.", "operation",
                false),

            factory.createLongGauge("localMaxMemory",
                "local max memory in bytes for this region on this member", "bytes")

        });

    bucketCountId = type.nameToId("bucketCount");

    putsCompletedId = type.nameToId("putsCompleted");
    putOpsRetriedId = type.nameToId("putOpsRetried");
    putRetriesId = type.nameToId("putRetries");
    createsCompletedId = type.nameToId("createsCompleted");
    createOpsRetriedId = type.nameToId("createOpsRetried");
    createRetriesId = type.nameToId("createRetries");
    getsCompletedId = type.nameToId("getsCompleted");
    preferredReadLocalId = type.nameToId("preferredReadLocal");
    preferredReadRemoteId = type.nameToId("preferredReadRemote");
    getOpsRetriedId = type.nameToId("getOpsRetried");
    getRetriesId = type.nameToId("getRetries");
    destroysCompletedId = type.nameToId("destroysCompleted");
    destroyOpsRetriedId = type.nameToId("destroyOpsRetried");
    destroyRetriesId = type.nameToId("destroyRetries");
    invalidatesCompletedId = type.nameToId("invalidatesCompleted");
    invalidateOpsRetriedId = type.nameToId("invalidateOpsRetried");
    invalidateRetriesId = type.nameToId("invalidateRetries");
    containsKeyCompletedId = type.nameToId("containsKeyCompleted");
    containsKeyOpsRetriedId = type.nameToId("containsKeyOpsRetried");
    containsKeyRetriesId = type.nameToId("containsKeyRetries");
    containsValueForKeyCompletedId = type.nameToId("containsValueForKeyCompleted");
    partitionMessagesSentId = type.nameToId("PartitionMessagesSent");
    partitionMessagesReceivedId = type.nameToId("PartitionMessagesReceived");
    partitionMessagesProcessedId = type.nameToId("PartitionMessagesProcessed");
    fieldId_PUTALLS_COMPLETED = type.nameToId(PUTALLS_COMPLETED);
    fieldId_PUTALL_MSGS_RETRIED = type.nameToId(PUTALL_MSGS_RETRIED);
    fieldId_PUTALL_RETRIES = type.nameToId(PUTALL_RETRIES);
    fieldId_PUTALL_TIME = type.nameToId(PUTALL_TIME);
    fieldId_REMOVE_ALLS_COMPLETED = type.nameToId(REMOVE_ALLS_COMPLETED);
    fieldId_REMOVE_ALL_MSGS_RETRIED = type.nameToId(REMOVE_ALL_MSGS_RETRIED);
    fieldId_REMOVE_ALL_RETRIES = type.nameToId(REMOVE_ALL_RETRIES);
    fieldId_REMOVE_ALL_TIME = type.nameToId(REMOVE_ALL_TIME);
    putTimeId = type.nameToId("putTime");
    createTimeId = type.nameToId("createTime");
    getTimeId = type.nameToId("getTime");
    destroyTimeId = type.nameToId("destroyTime");
    invalidateTimeId = type.nameToId("invalidateTime");
    containsKeyTimeId = type.nameToId("containsKeyTime");
    containsValueForKeyTimeId = type.nameToId("containsValueForKeyTime");
    partitionMessagesProcessingTimeId = type.nameToId("partitionMessagesProcessingTime");
    dataStoreEntryCountId = type.nameToId("dataStoreEntryCount");
    dataStoreBytesInUseId = type.nameToId("dataStoreBytesInUse");

    volunteeringInProgressId = type.nameToId("volunteeringInProgress");
    volunteeringBecamePrimaryId = type.nameToId("volunteeringBecamePrimary");
    volunteeringBecamePrimaryTimeId = type.nameToId("volunteeringBecamePrimaryTime");
    volunteeringOtherPrimaryId = type.nameToId("volunteeringOtherPrimary");
    volunteeringOtherPrimaryTimeId = type.nameToId("volunteeringOtherPrimaryTime");
    volunteeringClosedId = type.nameToId("volunteeringClosed");
    volunteeringClosedTimeId = type.nameToId("volunteeringClosedTime");

    totalNumBucketsId = type.nameToId("totalNumBuckets");
    primaryBucketCountId = type.nameToId("primaryBucketCount");
    volunteeringThreadsId = type.nameToId("volunteeringThreads");
    lowRedundancyBucketCountId = type.nameToId("lowRedundancyBucketCount");
    noCopiesBucketCountId = type.nameToId("noCopiesBucketCount");

    getEntriesCompletedId = type.nameToId("getEntryCompleted");
    getEntryTimeId = type.nameToId("getEntryTime");

    configuredRedundantCopiesId = type.nameToId("configuredRedundantCopies");
    actualRedundantCopiesId = type.nameToId("actualRedundantCopies");

    recoveriesCompletedId = type.nameToId("recoveriesCompleted");
    recoveriesInProgressId = type.nameToId("recoveriesInProgress");
    recoveriesTimeId = type.nameToId("recoveryTime");
    bucketCreatesInProgressId = type.nameToId("bucketCreatesInProgress");
    bucketCreatesCompletedId = type.nameToId("bucketCreatesCompleted");
    bucketCreatesFailedId = type.nameToId("bucketCreatesFailed");
    bucketCreateTimeId = type.nameToId("bucketCreateTime");
    primaryTransfersInProgressId = type.nameToId("primaryTransfersInProgress");
    primaryTransfersCompletedId = type.nameToId("primaryTransfersCompleted");
    primaryTransfersFailedId = type.nameToId("primaryTransfersFailed");
    primaryTransferTimeId = type.nameToId("primaryTransferTime");

    rebalanceBucketCreatesInProgressId = type.nameToId("rebalanceBucketCreatesInProgress");
    rebalanceBucketCreatesCompletedId = type.nameToId("rebalanceBucketCreatesCompleted");
    rebalanceBucketCreatesFailedId = type.nameToId("rebalanceBucketCreatesFailed");
    rebalanceBucketCreateTimeId = type.nameToId("rebalanceBucketCreateTime");
    rebalancePrimaryTransfersInProgressId = type.nameToId("rebalancePrimaryTransfersInProgress");
    rebalancePrimaryTransfersCompletedId = type.nameToId("rebalancePrimaryTransfersCompleted");
    rebalancePrimaryTransfersFailedId = type.nameToId("rebalancePrimaryTransfersFailed");
    rebalancePrimaryTransferTimeId = type.nameToId("rebalancePrimaryTransferTime");

    applyReplicationCompletedId = type.nameToId("applyReplicationCompleted");
    applyReplicationInProgressId = type.nameToId("applyReplicationInProgress");
    applyReplicationTimeId = type.nameToId("applyReplicationTime");
    sendReplicationCompletedId = type.nameToId("sendReplicationCompleted");
    sendReplicationInProgressId = type.nameToId("sendReplicationInProgress");
    sendReplicationTimeId = type.nameToId("sendReplicationTime");
    putRemoteCompletedId = type.nameToId("putRemoteCompleted");
    putRemoteInProgressId = type.nameToId("putRemoteInProgress");
    putRemoteTimeId = type.nameToId("putRemoteTime");
    putLocalCompletedId = type.nameToId("putLocalCompleted");
    putLocalInProgressId = type.nameToId("putLocalInProgress");
    putLocalTimeId = type.nameToId("putLocalTime");

    prMetaDataSentCountId = type.nameToId("prMetaDataSentCount");

    localMaxMemoryId = type.nameToId("localMaxMemory");
  }

  private final Statistics stats;

  /**
   * Utility map for temporarily holding stat start times.
   * <p>
   * This was originally added to avoid having to add a long volunteeringStarted variable to every
   * instance of BucketAdvisor. Majority of BucketAdvisors never volunteer and an instance of
   * BucketAdvisor exists for every bucket defined in a PartitionedRegion which could result in a
   * lot of unused longs. Volunteering is a rare event and thus the performance implications of a
   * HashMap lookup is small and preferrable to so many longs. Key: BucketAdvisor, Value: Long
   */
  private final Map startTimeMap;

  public static long startTime() {
    return CachePerfStats.getStatTime();
  }

  public static long getStatTime() {
    return CachePerfStats.getStatTime();
  }

  public PartitionedRegionStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, name /* fixes bug 42343 */);

    if (CachePerfStatsImpl.enableClockStats) {
      this.startTimeMap = new ConcurrentHashMap();
    } else {
      this.startTimeMap = Collections.EMPTY_MAP;
    }
  }

  @Override
  public void close() {
    this.stats.close();
  }

  @Override
  public Statistics getStats() {
    return this.stats;
  }

  // ------------------------------------------------------------------------
  // region op stats
  // ------------------------------------------------------------------------

  @Override
  public void endPut(long start) {
    endPut(start, 1);
  }

  /**
   * This method sets the end time for putAll and updates the counters
   */
  @Override
  public void endPutAll(long start) {
    endPutAll(start, 1);
  }

  @Override
  public void endRemoveAll(long start) {
    endRemoveAll(start, 1);
  }

  @Override
  public void endCreate(long start) {
    endCreate(start, 1);
  }

  @Override
  public void endGet(long start) {
    endGet(start, 1);
  }

  @Override
  public void endContainsKey(long start) {
    endContainsKey(start, 1);
  }

  @Override
  public void endContainsValueForKey(long start) {
    endContainsValueForKey(start, 1);
  }

  @Override
  public void endPut(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      long delta = CachePerfStats.getStatTime() - start;
      this.stats.incLong(putTimeId, delta);
    }
    this.stats.incInt(putsCompletedId, numInc);
  }

  /**
   * This method sets the end time for putAll and updates the counters
   */
  @Override
  public void endPutAll(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      long delta = CachePerfStats.getStatTime() - start;
      this.stats.incLong(fieldId_PUTALL_TIME, delta);
      // this.putStatsHistogram.endOp(delta);

    }
    this.stats.incInt(fieldId_PUTALLS_COMPLETED, numInc);
  }

  @Override
  public void endRemoveAll(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      long delta = CachePerfStats.getStatTime() - start;
      this.stats.incLong(fieldId_REMOVE_ALL_TIME, delta);
    }
    this.stats.incInt(fieldId_REMOVE_ALLS_COMPLETED, numInc);
  }

  @Override
  public void endCreate(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(createTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(createsCompletedId, numInc);
  }

  @Override
  public void endGet(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      final long delta = CachePerfStats.getStatTime() - start;
      this.stats.incLong(getTimeId, delta);
    }
    this.stats.incInt(getsCompletedId, numInc);
  }

  @Override
  public void endDestroy(long start) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(destroyTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(destroysCompletedId, 1);
  }

  @Override
  public void endInvalidate(long start) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(invalidateTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(invalidatesCompletedId, 1);
  }

  @Override
  public void endContainsKey(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(containsKeyTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(containsKeyCompletedId, numInc);
  }

  @Override
  public void endContainsValueForKey(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(containsValueForKeyTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(containsValueForKeyCompletedId, numInc);
  }

  @Override
  public void incContainsKeyValueRetries() {
    this.stats.incInt(containsKeyRetriesId, 1);
  }

  @Override
  public void incContainsKeyValueOpsRetried() {
    this.stats.incInt(containsKeyOpsRetriedId, 1);
  }

  @Override
  public void incInvalidateRetries() {
    this.stats.incInt(invalidateRetriesId, 1);
  }

  @Override
  public void incInvalidateOpsRetried() {
    this.stats.incInt(invalidateOpsRetriedId, 1);
  }

  @Override
  public void incDestroyRetries() {
    this.stats.incInt(destroyRetriesId, 1);
  }

  @Override
  public void incDestroyOpsRetried() {
    this.stats.incInt(destroyOpsRetriedId, 1);
  }

  @Override
  public void incPutRetries() {
    this.stats.incInt(putRetriesId, 1);
  }

  @Override
  public void incPutOpsRetried() {
    this.stats.incInt(putOpsRetriedId, 1);
  }

  @Override
  public void incGetOpsRetried() {
    this.stats.incInt(getOpsRetriedId, 1);
  }

  @Override
  public void incGetRetries() {
    this.stats.incInt(getRetriesId, 1);
  }

  @Override
  public void incCreateOpsRetried() {
    this.stats.incInt(createOpsRetriedId, 1);
  }

  @Override
  public void incCreateRetries() {
    this.stats.incInt(createRetriesId, 1);
  }

  // ------------------------------------------------------------------------
  // preferred read stats
  // ------------------------------------------------------------------------

  @Override
  public void incPreferredReadLocal() {
    this.stats.incInt(preferredReadLocalId, 1);
  }

  @Override
  public void incPreferredReadRemote() {
    this.stats.incInt(preferredReadRemoteId, 1);
  }

  // ------------------------------------------------------------------------
  // messaging stats
  // ------------------------------------------------------------------------

  @Override
  public long startPartitionMessageProcessing() {
    this.stats.incInt(partitionMessagesReceivedId, 1);
    return startTime();
  }

  @Override
  public void endPartitionMessagesProcessing(long start) {
    if (CachePerfStatsImpl.enableClockStats) {
      long delta = CachePerfStats.getStatTime() - start;
      this.stats.incLong(partitionMessagesProcessingTimeId, delta);
    }
    this.stats.incInt(partitionMessagesProcessedId, 1);
  }

  @Override
  public void incPartitionMessagesSent() {
    this.stats.incInt(partitionMessagesSentId, 1);
  }

  // ------------------------------------------------------------------------
  // datastore stats
  // ------------------------------------------------------------------------

  @Override
  public void incBucketCount(int delta) {
    this.stats.incInt(bucketCountId, delta);
  }

  @Override
  public void setBucketCount(int i) {
    this.stats.setInt(bucketCountId, i);
  }

  @Override
  public void incDataStoreEntryCount(int amt) {
    this.stats.incInt(dataStoreEntryCountId, amt);
  }

  @Override
  public int getDataStoreEntryCount() {
    return this.stats.getInt(dataStoreEntryCountId);
  }

  @Override
  public void incBytesInUse(long delta) {
    this.stats.incLong(dataStoreBytesInUseId, delta);
  }

  @Override
  public long getDataStoreBytesInUse() {
    return this.stats.getLong(dataStoreBytesInUseId);
  }

  @Override
  public int getTotalBucketCount() {
    int bucketCount = this.stats.getInt(bucketCountId);
    return bucketCount;
  }

  @Override
  public void incPutAllRetries() {
    this.stats.incInt(fieldId_PUTALL_RETRIES, 1);
  }

  @Override
  public void incPutAllMsgsRetried() {
    this.stats.incInt(fieldId_PUTALL_MSGS_RETRIED, 1);
  }

  @Override
  public void incRemoveAllRetries() {
    this.stats.incInt(fieldId_REMOVE_ALL_RETRIES, 1);
  }

  @Override
  public void incRemoveAllMsgsRetried() {
    this.stats.incInt(fieldId_REMOVE_ALL_MSGS_RETRIED, 1);
  }

  // ------------------------------------------------------------------------
  // stats for volunteering/discovering/becoming primary
  // ------------------------------------------------------------------------

  @Override
  public int getVolunteeringInProgress() {
    return this.stats.getInt(volunteeringInProgressId);
  }

  @Override
  public int getVolunteeringBecamePrimary() {
    return this.stats.getInt(volunteeringBecamePrimaryId);
  }

  @Override
  public long getVolunteeringBecamePrimaryTime() {
    return this.stats.getLong(volunteeringBecamePrimaryTimeId);
  }

  @Override
  public int getVolunteeringOtherPrimary() {
    return this.stats.getInt(volunteeringOtherPrimaryId);
  }

  @Override
  public long getVolunteeringOtherPrimaryTime() {
    return this.stats.getLong(volunteeringOtherPrimaryTimeId);
  }

  @Override
  public int getVolunteeringClosed() {
    return this.stats.getInt(volunteeringClosedId);
  }

  @Override
  public long getVolunteeringClosedTime() {
    return this.stats.getLong(volunteeringClosedTimeId);
  }

  @Override
  public long startVolunteering() {
    this.stats.incInt(volunteeringInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endVolunteeringBecamePrimary(long start) {
    long ts = CachePerfStats.getStatTime();
    this.stats.incInt(volunteeringInProgressId, -1);
    this.stats.incInt(volunteeringBecamePrimaryId, 1);
    if (CachePerfStatsImpl.enableClockStats) {
      long time = ts - start;
      this.stats.incLong(volunteeringBecamePrimaryTimeId, time);
    }
  }

  @Override
  public void endVolunteeringOtherPrimary(long start) {
    long ts = CachePerfStats.getStatTime();
    this.stats.incInt(volunteeringInProgressId, -1);
    this.stats.incInt(volunteeringOtherPrimaryId, 1);
    if (CachePerfStatsImpl.enableClockStats) {
      long time = ts - start;
      this.stats.incLong(volunteeringOtherPrimaryTimeId, time);
    }
  }

  @Override
  public void endVolunteeringClosed(long start) {
    long ts = CachePerfStats.getStatTime();
    this.stats.incInt(volunteeringInProgressId, -1);
    this.stats.incInt(volunteeringClosedId, 1);
    if (CachePerfStatsImpl.enableClockStats) {
      long time = ts - start;
      this.stats.incLong(volunteeringClosedTimeId, time);
    }
  }

  @Override
  public int getTotalNumBuckets() {
    return this.stats.getInt(totalNumBucketsId);
  }

  @Override
  public void incTotalNumBuckets(int val) {
    this.stats.incInt(totalNumBucketsId, val);
  }

  @Override
  public int getPrimaryBucketCount() {
    return this.stats.getInt(primaryBucketCountId);
  }

  @Override
  public void incPrimaryBucketCount(int val) {
    this.stats.incInt(primaryBucketCountId, val);
  }

  @Override
  public int getVolunteeringThreads() {
    return this.stats.getInt(volunteeringThreadsId);
  }

  @Override
  public void incVolunteeringThreads(int val) {
    this.stats.incInt(volunteeringThreadsId, val);
  }

  @Override
  public int getLowRedundancyBucketCount() {
    return this.stats.getInt(lowRedundancyBucketCountId);
  }

  @Override
  public int getNoCopiesBucketCount() {
    return this.stats.getInt(noCopiesBucketCountId);
  }

  @Override
  public void incLowRedundancyBucketCount(int val) {
    this.stats.incInt(lowRedundancyBucketCountId, val);
  }

  @Override
  public void incNoCopiesBucketCount(int val) {
    this.stats.incInt(noCopiesBucketCountId, val);
  }

  @Override
  public int getConfiguredRedundantCopies() {
    return this.stats.getInt(configuredRedundantCopiesId);
  }

  @Override
  public void setConfiguredRedundantCopies(int val) {
    this.stats.setInt(configuredRedundantCopiesId, val);
  }

  @Override
  public void setLocalMaxMemory(long l) {
    this.stats.setLong(localMaxMemoryId, l);
  }

  @Override
  public int getActualRedundantCopies() {
    return this.stats.getInt(actualRedundantCopiesId);
  }

  @Override
  public void setActualRedundantCopies(int val) {
    this.stats.setInt(actualRedundantCopiesId, val);
  }

  // ------------------------------------------------------------------------
  // startTimeMap methods
  // ------------------------------------------------------------------------

  /**
   * Put stat start time in holding map for later removal and use by caller
   */
  @Override
  public void putStartTime(Object key, long startTime) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.startTimeMap.put(key, Long.valueOf(startTime));
    }
  }

  /**
   * Remove stat start time from holding map to complete a clock stat
   */
  @Override
  public long removeStartTime(Object key) {
    Long startTime = (Long) this.startTimeMap.remove(key);
    return startTime == null ? 0 : startTime.longValue();
  }

  /**
   * Statistic to track the {@link Region#getEntry(Object)} call
   * @param startTime the time the getEntry operation started
   */
  @Override
  public void endGetEntry(long startTime) {
    endGetEntry(startTime, 1);
  }

  /**
   * This method sets the end time for update and updates the counters
   */
  @Override
  public void endGetEntry(long start, int numInc) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(getEntryTimeId, CachePerfStats.getStatTime() - start);
    }
    this.stats.incInt(getEntriesCompletedId, numInc);
  }

  // ------------------------------------------------------------------------
  // bucket creation, primary transfer stats (see also rebalancing stats below)
  // ------------------------------------------------------------------------
  @Override
  public long startRecovery() {
    this.stats.incInt(recoveriesInProgressId, 1);
    return PartitionedRegionStatsImpl.getStatTime();
  }

  @Override
  public void endRecovery(long start) {
    long ts = PartitionedRegionStatsImpl.getStatTime();
    this.stats.incInt(recoveriesInProgressId, -1);
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(recoveriesTimeId, ts - start);
    }
    this.stats.incInt(recoveriesCompletedId, 1);
  }

  @Override
  public long startBucketCreate(boolean isRebalance) {
    this.stats.incInt(bucketCreatesInProgressId, 1);
    if (isRebalance) {
      startRebalanceBucketCreate();
    }
    return PartitionedRegionStatsImpl.getStatTime();
  }

  @Override
  public void endBucketCreate(long start, boolean success, boolean isRebalance) {
    long ts = PartitionedRegionStatsImpl.getStatTime();
    this.stats.incInt(bucketCreatesInProgressId, -1);
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(bucketCreateTimeId, ts - start);
    }
    if (success) {
      this.stats.incInt(bucketCreatesCompletedId, 1);
    } else {
      this.stats.incInt(bucketCreatesFailedId, 1);
    }
    if (isRebalance) {
      endRebalanceBucketCreate(start, ts, success);
    }
  }

  @Override
  public long startPrimaryTransfer(boolean isRebalance) {
    this.stats.incInt(primaryTransfersInProgressId, 1);
    if (isRebalance) {
      startRebalancePrimaryTransfer();
    }
    return PartitionedRegionStatsImpl.getStatTime();
  }

  @Override
  public void endPrimaryTransfer(long start, boolean success, boolean isRebalance) {
    long ts = PartitionedRegionStatsImpl.getStatTime();
    this.stats.incInt(primaryTransfersInProgressId, -1);
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(primaryTransferTimeId, ts - start);
    }
    if (success) {
      this.stats.incInt(primaryTransfersCompletedId, 1);
    } else {
      this.stats.incInt(primaryTransfersFailedId, 1);
    }
    if (isRebalance) {
      endRebalancePrimaryTransfer(start, ts, success);
    }
  }

  @Override
  public int getBucketCreatesInProgress() {
    return this.stats.getInt(bucketCreatesInProgressId);
  }

  @Override
  public int getBucketCreatesCompleted() {
    return this.stats.getInt(bucketCreatesCompletedId);
  }

  @Override
  public int getBucketCreatesFailed() {
    return this.stats.getInt(bucketCreatesFailedId);
  }

  @Override
  public long getBucketCreateTime() {
    return this.stats.getLong(bucketCreateTimeId);
  }

  @Override
  public int getPrimaryTransfersInProgress() {
    return this.stats.getInt(primaryTransfersInProgressId);
  }

  @Override
  public int getPrimaryTransfersCompleted() {
    return this.stats.getInt(primaryTransfersCompletedId);
  }

  @Override
  public int getPrimaryTransfersFailed() {
    return this.stats.getInt(primaryTransfersFailedId);
  }

  @Override
  public long getPrimaryTransferTime() {
    return this.stats.getLong(primaryTransferTimeId);
  }

  // ------------------------------------------------------------------------
  // rebalancing stats
  // ------------------------------------------------------------------------

  private void startRebalanceBucketCreate() {
    this.stats.incInt(rebalanceBucketCreatesInProgressId, 1);
  }

  private void endRebalanceBucketCreate(long start, long end, boolean success) {
    this.stats.incInt(rebalanceBucketCreatesInProgressId, -1);
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(rebalanceBucketCreateTimeId, end - start);
    }
    if (success) {
      this.stats.incInt(rebalanceBucketCreatesCompletedId, 1);
    } else {
      this.stats.incInt(rebalanceBucketCreatesFailedId, 1);
    }
  }

  private void startRebalancePrimaryTransfer() {
    this.stats.incInt(rebalancePrimaryTransfersInProgressId, 1);
  }

  private void endRebalancePrimaryTransfer(long start, long end, boolean success) {
    this.stats.incInt(rebalancePrimaryTransfersInProgressId, -1);
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(rebalancePrimaryTransferTimeId, end - start);
    }
    if (success) {
      this.stats.incInt(rebalancePrimaryTransfersCompletedId, 1);
    } else {
      this.stats.incInt(rebalancePrimaryTransfersFailedId, 1);
    }
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
  public long startApplyReplication() {
    stats.incInt(applyReplicationInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endApplyReplication(long start) {
    long delta = CachePerfStats.getStatTime() - start;
    stats.incInt(applyReplicationInProgressId, -1);
    stats.incInt(applyReplicationCompletedId, 1);
    stats.incLong(applyReplicationTimeId, delta);
  }

  @Override
  public long startSendReplication() {
    stats.incInt(sendReplicationInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endSendReplication(long start) {
    long delta = CachePerfStats.getStatTime() - start;
    stats.incInt(sendReplicationInProgressId, -1);
    stats.incInt(sendReplicationCompletedId, 1);
    stats.incLong(sendReplicationTimeId, delta);
  }

  @Override
  public long startPutRemote() {
    stats.incInt(putRemoteInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endPutRemote(long start) {
    long delta = CachePerfStats.getStatTime() - start;
    stats.incInt(putRemoteInProgressId, -1);
    stats.incInt(putRemoteCompletedId, 1);
    stats.incLong(putRemoteTimeId, delta);
  }

  @Override
  public long startPutLocal() {
    stats.incInt(putLocalInProgressId, 1);
    return CachePerfStats.getStatTime();
  }

  @Override
  public void endPutLocal(long start) {
    long delta = CachePerfStats.getStatTime() - start;
    stats.incInt(putLocalInProgressId, -1);
    stats.incInt(putLocalCompletedId, 1);
    stats.incLong(putLocalTimeId, delta);
  }

  @Override
  public void incPRMetaDataSentCount() {
    this.stats.incLong(prMetaDataSentCountId, 1);
  }

  @Override
  public long getPRMetaDataSentCount() {
    return this.stats.getLong(prMetaDataSentCountId);
  }
}
