/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License", arrayOf("regionName",name)); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.geode.statistics.region

import org.apache.geode.internal.cache.PartitionedRegionStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerPartitionedRegionStats(statisticsFactory: StatisticsFactory?, private val regionName: String) :
        MicrometerMeterGroup(statisticsFactory, "PartitionRegionStats-$regionName"), PartitionedRegionStats {

    override fun getGroupTags(): Array<String> = arrayOf("regionName", regionName)

    /**
     * Utility map for partitionRegionTemporarily holding stat start times.
     *
     *
     * This was originally added to avoid having to add a long volunteeringStarted variable to every
     * instance of BucketAdvisor. Majority of BucketAdvisors never volunteer and an instance of
     * BucketAdvisor exists for every bucket defined in a PartitionedRegion which could result in a
     * lot of unused longs. Volunteering is a rare event and thus the performance implications of a
     * HashMap lookup is small and preferrable to so many longs. Key: BucketAdvisor, Value: Long
     */
    private val startTimeMap: MutableMap<Any, Long> = mutableMapOf()

    private val partitionRegionBucketCountMeter = GaugeStatisticMeter("region.partition.bucket.count", "Number of buckets in this node.")
    private val partitionRegionPreferLocalReadMeter = CounterStatisticMeter("region.partition.read.prefer.local.count", "Number of reads satisfied from local store")
    private val partitionRegionPreferRemoteReadMeter = CounterStatisticMeter("region.partition.read.prefer.remote.count", "Number of reads satisfied from remote store")
    private val partitionRegionDataStoreEntryMeter = GaugeStatisticMeter("region.partition.datastore.entry.count", "The number of entries stored in this Cache for the named Partitioned Region. This does not include entries which are tombstones. See CachePerfStats.tombstoneCount.")
    private val partitionRegionDataStoreBytesMeter = GaugeStatisticMeter("region.partition.datastore.bytes", "The current number of bytes stored in this Cache for the named Partitioned Region", meterUnit = "bytes")
    private val partitionRegionPRMetadataSentMeter = CounterStatisticMeter("region.partition.metadata.sent.count", "total number of times meta data refreshed sent on client's request.")
    private val partitionRegionLocalMaxMemoryBytesMeter = GaugeStatisticMeter("region.partition.local.memory.max.bytes", "local max memory in bytes for this region on this member", meterUnit = "bytes")

    private val partitionRegionPutCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of puts completed.", arrayOf("operation", "put"))
    private val partitionRegionPutFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of put operations which had to be retried due to failures.", arrayOf("operation", "put"))
    private val partitionRegionPutRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times put operations had to be retried.", arrayOf("operation", "put"))
    private val partitionRegionPutTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing puts.", arrayOf("operation", "put"), meterUnit = "nanoseconds")

    private val partitionRegionCreateCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of creates completed.", arrayOf("operation", "create"))
    private val partitionRegionCreateFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of create operations which had to be retried due to failures.", arrayOf("operation", "create"))
    private val partitionRegionCreateRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times put operations had to be retried.", arrayOf("operation", "create"))
    private val partitionRegionCreateTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing create operations.", arrayOf("operation", "create"), meterUnit = "nanoseconds")

    private val partitionRegionPutAllCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of putAlls completed.", arrayOf("operation", "putAll"))
    private val partitionRegionPutAllFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of putAll messages which had to be retried due to failures.", arrayOf("operation", "putAll"))
    private val partitionRegionPutAllRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times putAll messages had to be retried.", arrayOf("operation", "putAll"))
    private val partitionRegionPutAllTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing putAlls.", arrayOf("operation", "putAll"), meterUnit = "nanoseconds")

    private val partitionRegionRemoveAllCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of removeAlls completed.", arrayOf("operation", "removeAll"))
    private val partitionRegionRemoveAllFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of removeAll messages which had to be retried due to failures.", arrayOf("operation", "removeAll"))
    private val partitionRegionRemoveAllRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times removeAll messages had to be retried.", arrayOf("operation", "removeAll"))
    private val partitionRegionRemoveAllTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing removeAlls.", arrayOf("operation", "removeAll"), meterUnit = "nanoseconds")

    private val partitionRegionGetCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of gets completed.", arrayOf("operation", "get"))
    private val partitionRegionGetFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of get operations which had to be retried due to failures.", arrayOf("operation", "get"))
    private val partitionRegionGetRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times get operations had to be retried.", arrayOf("operation", "get"))
    private val partitionRegionGetTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing get operations.", arrayOf("operation", "get"), meterUnit = "nanoseconds")

    private val partitionRegionDestroyCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of destroys completed.", arrayOf("operation", "destroy"))
    private val partitionRegionDestroyFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of destroy operations which had to be retried due to failures.", arrayOf("operation", "destroy"))
    private val partitionRegionDestroyRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times destroy operations had to be retried.", arrayOf("operation", "destroy"))
    private val partitionRegionDestroyTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing destroys.", arrayOf("operation", "destroy"), meterUnit = "nanoseconds")

    private val partitionRegionInvalidateCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of invalidates completed.", arrayOf("operation", "invalidate"))
    private val partitionRegionInvalidateFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of invalidate operations which had to be retried due to failures.", arrayOf("operation", "invalidate"))
    private val partitionRegionInvalidateRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times invalidate operations had to be retried.", arrayOf("operation", "invalidate"))
    private val partitionRegionInvalidateTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing invalidates.", arrayOf("operation", "invalidate"), meterUnit = "nanoseconds")

    private val partitionRegionContainsKeyCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of containsKeys completed.", arrayOf("operation", "containsKey"))
    private val partitionRegionContainsKeyFailureRetryMeter = CounterStatisticMeter("region.partition.operation.ops.retried.count", "Number of containsKey or containsValueForKey operations which had to be retried due to failures.", arrayOf("operation", "containsKey"))
    private val partitionRegionContainsKeyRetryMeter = CounterStatisticMeter("region.partition.operation.retries.count", "Total number of times containsKey or containsValueForKey operations had to be retried.", arrayOf("operation", "containsKey"))
    private val partitionRegionContainsKeyTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing containsKey operations.", arrayOf("operation", "containsKey"), meterUnit = "nanoseconds")

    private val partitionRegionContainsValueForKeyCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of containsValueForKeys completed.", arrayOf("operation", "containsValueForKey"))
    private val partitionRegionContainsValueForKeyTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing containsValueForKey operations.", arrayOf("operation", "containsValueForKey"), meterUnit = "nanoseconds")

    private val partitionRegionGetEntryCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Number of getEntry operations completed.", arrayOf("operation", "getEntry"))
    private val partitionRegionGetEntryTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing getEntry operations.", arrayOf("operation", "getEntry"), meterUnit = "nanoseconds")

    private val partitionRegionRedundancyRecoveryInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of redundancy recovery operations in progress for this region.", arrayOf("operation", "redundancy_recovery"))
    private val partitionRegionRedundancyRecoveryCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of redundancy recovery operations performed on this region.", arrayOf("operation", "redundancy_recovery"))
    private val partitionRegionRedundancyRecoveryTimer = TimerStatisticMeter("region.partition.operation.time", "Total number time spent recovering redundancy.", arrayOf("operation", "redundancy_recovery"), meterUnit = "nanoseconds")

    private val partitionRegionBucketCreateInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of bucket create operations being performed for rebalancing.", arrayOf("operation", "bucket_create"))
    private val partitionRegionBucketCreateFailedMeter = CounterStatisticMeter("region.partition.operation.failed.count", "Total number of bucket create operations performed for rebalancing that failed.", arrayOf("operation", "bucket_create"))
    private val partitionRegionBucketCreateCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of bucket create operations performed for rebalancing.", arrayOf("operation", "bucket_create"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val partitionRegionBucketCreateTimer = CounterStatisticMeter("region.partition.operation.time", "Total time spent performing bucket create operations for rebalancing.", arrayOf("operation", "bucket_create"), meterUnit = "nanoseconds")

    private val partitionRegionRebalanceInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of primary transfer operations being performed for rebalancing.", arrayOf("operation", "rebalancing"))
    private val partitionRegionRebalanceFailedMeter = CounterStatisticMeter("region.partition.operation.failed.count", "Total number of primary transfer operations performed for rebalancing that failed.", arrayOf("operation", "rebalancing"))
    private val partitionRegionRebalanceCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of primary transfer operations performed for rebalancing.", arrayOf("operation", "rebalancing"))
    private val partitionRegionRebalanceTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing primary transfer operations for rebalancing.", arrayOf("operation", "rebalancing"), meterUnit = "nanoseconds")

    private val partitionRegionMessagesSentMeter = CounterStatisticMeter("region.partition.messages.sent.count", "Number of PartitionMessages Sent.")
    private val partitionRegionMessagesReceivedMeter = CounterStatisticMeter("region.partition.messages.received.count", "Number of PartitionMessages Received.")
    private val partitionRegionMessagesProcessedMeter = CounterStatisticMeter("region.partition.messages.processed.count", "Number of PartitionMessages Processed.")
    private val partitionRegionMessagesProcessedTimer = TimerStatisticMeter("region.partition.messages.processed.time", "Total time spent on PartitionMessages processing.", meterUnit = "nanoseconds")

    private val partitionRegionApplyReplicationInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of replication operations in progress on this redundant data store.", arrayOf("operation", "apply_replication"))
    private val partitionRegionApplyReplicationCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of replicated values sent from a primary to this redundant data store.", arrayOf("operation", "apply_replication"))
    private val partitionRegionApplyReplicationTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent storing replicated values on this redundant data store.", arrayOf("operation", "apply_replication"), meterUnit = "nanoseconds")

    private val partitionRegionSendReplicationInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of replication operations in progress from this primary.", arrayOf("operation", "send_replication"))
    private val partitionRegionSendReplicationCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of replicated values sent from this primary to a redundant data store.", arrayOf("operation", "send_replication"))
    private val partitionRegionSendReplicationTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent replicating values from this primary to a redundant data store.", arrayOf("operation", "send_replication"), meterUnit = "nanoseconds")

    private val partitionRegionPutRemoteInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of puts in progress that did not originate in the primary.", arrayOf("operation", "putRemote"))
    private val partitionRegionPutRemoteCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of completed puts that did not originate in the primary. These puts require an extra network hop to the primary.", arrayOf("operation", "putRemote"))
    private val partitionRegionPutRemoteTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing puts that did not originate in the primary.", arrayOf("operation", "putRemote"), meterUnit = "nanoseconds")

    private val partitionRegionPutLocalInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of puts in progress that did originate in the primary.", arrayOf("operation", "putLocal"))
    private val partitionRegionPutLocalCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of completed puts that did originate in the primary. These puts are optimal.", arrayOf("operation", "putLocal"))
    private val partitionRegionPutLocalTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent doing puts that did originate in the primary.", arrayOf("operation", "putLocal"), meterUnit = "nanoseconds")

    private val partitionRegionBucketRebalanceInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of bucket create operations being performed for rebalancing.", arrayOf("operation", "rebalancingBucket"))
    private val partitionRegionBucketRebalanceFailedMeter = CounterStatisticMeter("region.partition.operation.failed.count", "Total number of bucket create operations performed for rebalancing that failed.", arrayOf("operation", "rebalancingBucket"))
    private val partitionRegionBucketRebalanceCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of bucket create operations performed for rebalancing.", arrayOf("operation", "rebalancingBucket"))
    private val partitionRegionBucketRebalanceTimer = TimerStatisticMeter("region.partition.operation.time", "Total time spent performing bucket create operations for rebalancing.", arrayOf("operation", "rebalancingBucket"), meterUnit = "nanoseconds")

    private val partitionRegionPrimaryTransferInProgressMeter = GaugeStatisticMeter("region.partition.operation.inprogress.count", "Current number of primary transfer operations being performed for rebalancing.", arrayOf("operation", "rebalance_primary_transfer"))
    private val partitionRegionPrimaryTransferFailedMeter = CounterStatisticMeter("region.partition.operation.failed.count", "Total number of primary transfer operations performed for rebalancing that failed.", arrayOf("operation", "rebalance_primary_transfer"))
    private val partitionRegionPrimaryTransferCompletedMeter = CounterStatisticMeter("region.partition.operation.completed.count", "Total number of primary transfer operations performed for rebalancing.", arrayOf("operation", "rebalance_primary_transfer"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val partitionRegionPrimaryTransferTimer = CounterStatisticMeter("region.partition.operation.time", "Total time spent performing primary transfer operations for rebalancing.", arrayOf("operation", "rebalance_primary_transfer"), meterUnit = "nanoseconds")

    private val partitionRegionPrimaryVolunteerInProgressMeter = GaugeStatisticMeter("region.partition.volunteer.inprogress", "Current number of atpartitionRegionTempts to volunteer for primary of a bucket.")
    private val partitionRegionPrimaryVolunteerLocalMeter = CounterStatisticMeter("region.partition.volunteer.primary", "Total number of atpartitionRegionTempts to volunteer that ended when this member became primary.", arrayOf("primaryLocation", "local"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val partitionRegionPrimaryVolunteerLocalTimer = CounterStatisticMeter("region.partition.volunteer.primary.time", "Total time spent volunteering that ended when this member became primary.", arrayOf("primaryLocation", "local"), meterUnit = "nanoseconds")
    private val partitionRegionPrimaryVolunteerRemoteMeter = CounterStatisticMeter("region.partition.volunteer.primary", "Total number of atpartitionRegionTempts to volunteer that ended when this member discovered other primary.", arrayOf("primaryLocation", "remote"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val partitionRegionPrimaryVolunteerRemoteTimer = CounterStatisticMeter("region.partition.volunteer.primary.time", "Total time spent volunteering that ended when this member discovered other primary.", arrayOf("primaryLocation", "remote"), meterUnit = "nanoseconds")
    private val partitionRegionPrimaryVolunteerClosedMeter = CounterStatisticMeter("region.partition.volunteer.primary.closed.count", "Total number of atpartitionRegionTempts to volunteer that ended when this member's bucket closed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val partitionRegionPrimaryVolunteerClosedTimer = CounterStatisticMeter("region.partition.volunteer.primary.closed.time", "Total time spent volunteering that ended when this member's bucket closed.", meterUnit = "nanoseconds")
    private val partitionRegionPrimaryVolunteerThreadsMeter = GaugeStatisticMeter("region.partition.volunteer.threads.count", "Current number of threads volunteering for primary.")

    private val partitionRegionBucketTotalMeter = GaugeStatisticMeter("region.parition.buckets.total.count", "The total number of buckets.")
    private val partitionRegionBucketPrimaryMeter = GaugeStatisticMeter("region.partition.buckets.primary.count", "Current number of primary buckets hosted locally.")
    private val partitionRegionBucketLowRedundancyMeter = GaugeStatisticMeter("region.partition.buckets.redundancy.low.count", "Current number of buckets without full redundancy.")
    private val partitionRegionBucketNoRedundancyMeter = GaugeStatisticMeter("region.partition.buckets.redundancy.none.count", "Current number of buckets without any copies remaining.")
    private val partitionRegionBucketConfiguredRedundancyMeter = GaugeStatisticMeter("region.partition.buckets.redundancy.config.count", "Configured number of redundant copies for this partitioned region.")
    private val partitionRegionBucketActualRedundancyMeter = GaugeStatisticMeter("region.partition.buckets.redundancy.actual.count", "Actual number of redundant copies for this partitioned region.")

    override fun initializeStaticMeters() {
        registerMeter(partitionRegionBucketCountMeter)
        registerMeter(partitionRegionPreferLocalReadMeter)
        registerMeter(partitionRegionPreferRemoteReadMeter)
        registerMeter(partitionRegionDataStoreEntryMeter)
        registerMeter(partitionRegionDataStoreBytesMeter)
        registerMeter(partitionRegionPRMetadataSentMeter)
        registerMeter(partitionRegionLocalMaxMemoryBytesMeter)

        registerMeter(partitionRegionPutCompletedMeter)
        registerMeter(partitionRegionPutFailureRetryMeter)
        registerMeter(partitionRegionPutRetryMeter)
        registerMeter(partitionRegionPutTimer)

        registerMeter(partitionRegionCreateCompletedMeter)
        registerMeter(partitionRegionCreateFailureRetryMeter)
        registerMeter(partitionRegionCreateRetryMeter)
        registerMeter(partitionRegionCreateTimer)

        registerMeter(partitionRegionPutAllCompletedMeter)
        registerMeter(partitionRegionPutAllFailureRetryMeter)
        registerMeter(partitionRegionPutAllRetryMeter)
        registerMeter(partitionRegionPutAllTimer)

        registerMeter(partitionRegionRemoveAllCompletedMeter)
        registerMeter(partitionRegionRemoveAllFailureRetryMeter)
        registerMeter(partitionRegionRemoveAllRetryMeter)
        registerMeter(partitionRegionRemoveAllTimer)

        registerMeter(partitionRegionGetCompletedMeter)
        registerMeter(partitionRegionGetFailureRetryMeter)
        registerMeter(partitionRegionGetRetryMeter)
        registerMeter(partitionRegionGetTimer)

        registerMeter(partitionRegionDestroyCompletedMeter)
        registerMeter(partitionRegionDestroyFailureRetryMeter)
        registerMeter(partitionRegionDestroyRetryMeter)
        registerMeter(partitionRegionDestroyTimer)

        registerMeter(partitionRegionInvalidateCompletedMeter)
        registerMeter(partitionRegionInvalidateFailureRetryMeter)
        registerMeter(partitionRegionInvalidateRetryMeter)
        registerMeter(partitionRegionInvalidateTimer)

        registerMeter(partitionRegionContainsKeyCompletedMeter)
        registerMeter(partitionRegionContainsKeyFailureRetryMeter)
        registerMeter(partitionRegionContainsKeyRetryMeter)
        registerMeter(partitionRegionContainsKeyTimer)

        registerMeter(partitionRegionContainsValueForKeyCompletedMeter)
        registerMeter(partitionRegionContainsValueForKeyTimer)

        registerMeter(partitionRegionGetEntryCompletedMeter)
        registerMeter(partitionRegionGetEntryTimer)

        registerMeter(partitionRegionRedundancyRecoveryInProgressMeter)
        registerMeter(partitionRegionRedundancyRecoveryCompletedMeter)
        registerMeter(partitionRegionRedundancyRecoveryTimer)

        registerMeter(partitionRegionBucketCreateInProgressMeter)
        registerMeter(partitionRegionBucketCreateFailedMeter)
        registerMeter(partitionRegionBucketCreateCompletedMeter)
        registerMeter(partitionRegionBucketCreateTimer)

        registerMeter(partitionRegionRebalanceInProgressMeter)
        registerMeter(partitionRegionRebalanceFailedMeter)
        registerMeter(partitionRegionRebalanceCompletedMeter)
        registerMeter(partitionRegionRebalanceTimer)

        registerMeter(partitionRegionMessagesSentMeter)
        registerMeter(partitionRegionMessagesReceivedMeter)
        registerMeter(partitionRegionMessagesProcessedMeter)
        registerMeter(partitionRegionMessagesProcessedTimer)

        registerMeter(partitionRegionApplyReplicationInProgressMeter)
        registerMeter(partitionRegionApplyReplicationCompletedMeter)
        registerMeter(partitionRegionApplyReplicationTimer)

        registerMeter(partitionRegionSendReplicationInProgressMeter)
        registerMeter(partitionRegionSendReplicationCompletedMeter)
        registerMeter(partitionRegionSendReplicationTimer)

        registerMeter(partitionRegionPutRemoteInProgressMeter)
        registerMeter(partitionRegionPutRemoteCompletedMeter)
        registerMeter(partitionRegionPutRemoteTimer)

        registerMeter(partitionRegionPutLocalInProgressMeter)
        registerMeter(partitionRegionPutLocalCompletedMeter)
        registerMeter(partitionRegionPutLocalTimer)

        registerMeter(partitionRegionBucketRebalanceInProgressMeter)
        registerMeter(partitionRegionBucketRebalanceFailedMeter)
        registerMeter(partitionRegionBucketRebalanceCompletedMeter)
        registerMeter(partitionRegionBucketRebalanceTimer)

        registerMeter(partitionRegionPrimaryTransferInProgressMeter)
        registerMeter(partitionRegionPrimaryTransferFailedMeter)
        registerMeter(partitionRegionPrimaryTransferCompletedMeter)
        registerMeter(partitionRegionPrimaryTransferTimer)


        registerMeter(partitionRegionPrimaryVolunteerInProgressMeter)
        registerMeter(partitionRegionPrimaryVolunteerLocalMeter)
        registerMeter(partitionRegionPrimaryVolunteerLocalTimer)
        registerMeter(partitionRegionPrimaryVolunteerRemoteMeter)
        registerMeter(partitionRegionPrimaryVolunteerRemoteTimer)
        registerMeter(partitionRegionPrimaryVolunteerClosedMeter)
        registerMeter(partitionRegionPrimaryVolunteerClosedTimer)
        registerMeter(partitionRegionPrimaryVolunteerThreadsMeter)

        registerMeter(partitionRegionBucketTotalMeter)
        registerMeter(partitionRegionBucketPrimaryMeter)
        registerMeter(partitionRegionBucketLowRedundancyMeter)
        registerMeter(partitionRegionBucketNoRedundancyMeter)
        registerMeter(partitionRegionBucketConfiguredRedundancyMeter)
        registerMeter(partitionRegionBucketActualRedundancyMeter)
    }

    override fun endPut(start: Long) {
        endPut(start, 1)
    }

    override fun endPut(start: Long, numInc: Int) {
        partitionRegionPutTimer.recordValue(NOW_NANOS - start)
        partitionRegionPutCompletedMeter.increment(numInc)
    }

    override fun endPutAll(start: Long) {
        endPutAll(start, 1)
    }

    override fun endPutAll(start: Long, numInc: Int) {
        partitionRegionPutAllTimer.recordValue(NOW_NANOS - start)
        partitionRegionPutAllCompletedMeter.increment(numInc)
    }

    override fun endRemoveAll(start: Long) {
        endRemoveAll(start, 1)
    }

    override fun endRemoveAll(start: Long, numInc: Int) {
        partitionRegionRemoveAllTimer.recordValue(NOW_NANOS - start)
        partitionRegionRemoveAllCompletedMeter.increment(numInc)
    }

    override fun endCreate(start: Long) {
        endCreate(start, 1)
    }

    override fun endCreate(start: Long, numInc: Int) {
        partitionRegionCreateTimer.recordValue(NOW_NANOS - start)
        partitionRegionCreateCompletedMeter.increment(numInc)
    }

    override fun endGet(start: Long) {
        endGet(start, 1)
    }

    override fun endGet(start: Long, numInc: Int) {
        partitionRegionGetTimer.recordValue(NOW_NANOS - start)
        partitionRegionGetCompletedMeter.increment(numInc)
    }

    override fun endDestroy(start: Long) {
        partitionRegionDestroyTimer.recordValue(NOW_NANOS - start)
        partitionRegionDestroyCompletedMeter.increment()
    }

    override fun endInvalidate(start: Long) {
        partitionRegionInvalidateTimer.recordValue(NOW_NANOS - start)
        partitionRegionInvalidateCompletedMeter.increment()
    }

    override fun endContainsKey(start: Long) {
        endContainsKey(start, 1)
    }

    override fun endContainsKey(start: Long, numInc: Int) {
        partitionRegionContainsKeyTimer.recordValue(NOW_NANOS - start)
        partitionRegionContainsKeyCompletedMeter.increment(numInc)
    }

    override fun endContainsValueForKey(start: Long) {
        endContainsValueForKey(start, 1)
    }

    override fun endContainsValueForKey(start: Long, numInc: Int) {
        partitionRegionContainsValueForKeyTimer.recordValue(NOW_NANOS - start)
        partitionRegionContainsValueForKeyCompletedMeter.increment(numInc)
    }

    override fun incContainsKeyValueRetries() {
        partitionRegionContainsKeyRetryMeter.increment()
    }

    override fun incContainsKeyValueOpsRetried() {
        partitionRegionContainsKeyFailureRetryMeter.increment()
    }

    override fun incInvalidateRetries() {
        partitionRegionInvalidateRetryMeter.increment()
    }

    override fun incInvalidateOpsRetried() {
        partitionRegionInvalidateFailureRetryMeter.increment()
    }

    override fun incDestroyRetries() {
        partitionRegionDestroyRetryMeter.increment()
    }

    override fun incDestroyOpsRetried() {
        partitionRegionDestroyFailureRetryMeter.increment()
    }

    override fun incPutRetries() {
        partitionRegionPutRetryMeter.increment()
    }

    override fun incPutOpsRetried() {
        partitionRegionPutFailureRetryMeter.increment()
    }

    override fun incGetOpsRetried() {
        partitionRegionGetFailureRetryMeter.increment()
    }

    override fun incGetRetries() {
        partitionRegionGetRetryMeter.increment()
    }

    override fun incCreateOpsRetried() {
        partitionRegionCreateFailureRetryMeter.increment()
    }

    override fun incCreateRetries() {
        partitionRegionCreateRetryMeter.increment()
    }

    override fun incPreferredReadLocal() {
        partitionRegionPreferLocalReadMeter.increment()
    }

    override fun incPreferredReadRemote() {
        partitionRegionPreferRemoteReadMeter.increment()
    }

    override fun startPartitionMessageProcessing(): Long {
        partitionRegionMessagesReceivedMeter.increment()
        return NOW_NANOS
    }

    override fun endPartitionMessagesProcessing(start: Long) {
        partitionRegionMessagesProcessedTimer.recordValue(NOW_NANOS - start)
        partitionRegionMessagesProcessedMeter.increment()
    }

    override fun incPartitionMessagesSent() {
        partitionRegionMessagesSentMeter.increment()
    }

    override fun incBucketCount(delta: Int) {
        partitionRegionBucketCountMeter.increment(delta)
    }

    override fun setBucketCount(i: Int) {
        partitionRegionBucketCountMeter.setValue(i)
    }

    override fun incDataStoreEntryCount(amt: Int) {
        partitionRegionDataStoreEntryMeter.increment(amt)
    }

    override fun incBytesInUse(delta: Long) {
        partitionRegionDataStoreBytesMeter.increment(delta)
    }

    override fun incPutAllRetries() {
        partitionRegionPutAllRetryMeter.increment()
    }

    override fun incPutAllMsgsRetried() {
        partitionRegionPutAllFailureRetryMeter.increment()
    }

    override fun incRemoveAllRetries() {
        partitionRegionRemoveAllRetryMeter.increment()
    }

    override fun incRemoveAllMsgsRetried() {
        partitionRegionRemoveAllFailureRetryMeter.increment()
    }

    override fun startVolunteering(): Long {
        partitionRegionPrimaryVolunteerInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endVolunteeringBecamePrimary(start: Long) {
        partitionRegionPrimaryVolunteerInProgressMeter.decrement()
        partitionRegionPrimaryVolunteerLocalMeter.increment()
        partitionRegionPrimaryVolunteerLocalTimer.increment(NOW_NANOS - start)
    }

    override fun endVolunteeringOtherPrimary(start: Long) {
        partitionRegionPrimaryVolunteerInProgressMeter.decrement()
        partitionRegionPrimaryVolunteerRemoteMeter.increment()
        partitionRegionPrimaryVolunteerRemoteTimer.increment(NOW_NANOS - start)
    }

    override fun endVolunteeringClosed(start: Long) {
        partitionRegionPrimaryVolunteerInProgressMeter.decrement()
        partitionRegionPrimaryVolunteerClosedMeter.decrement()
        partitionRegionPrimaryVolunteerClosedTimer.increment(NOW_NANOS - start)
    }

    override fun incTotalNumBuckets(buckets: Int) {
        partitionRegionBucketTotalMeter.increment(buckets)
    }

    override fun incPrimaryBucketCount(buckets: Int) {
        partitionRegionBucketPrimaryMeter.increment(buckets)
    }

    override fun incVolunteeringThreads(threads: Int) {
        partitionRegionPrimaryVolunteerThreadsMeter.increment(threads)
    }

    override fun incLowRedundancyBucketCount(buckets: Int) {
        partitionRegionBucketLowRedundancyMeter.increment(buckets)
    }

    override fun incNoCopiesBucketCount(buckets: Int) {
        partitionRegionBucketNoRedundancyMeter.increment(buckets)
    }

    override fun setLocalMaxMemory(memory: Long) {
        partitionRegionLocalMaxMemoryBytesMeter.setValue(memory)
    }

    override fun putStartTime(key: Any, startTime: Long) {
        this.startTimeMap[key] = startTime
    }

    override fun removeStartTime(key: Any): Long = this.startTimeMap.remove(key) ?: 0

    override fun endGetEntry(startTime: Long) {
        endGetEntry(startTime, 1)
    }

    override fun endGetEntry(start: Long, numInc: Int) {
        partitionRegionGetEntryTimer.recordValue(NOW_NANOS - start)
        partitionRegionGetEntryCompletedMeter.increment(numInc)
    }

    override fun startRecovery(): Long {
        partitionRegionRedundancyRecoveryInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endRecovery(start: Long) {
        partitionRegionRedundancyRecoveryTimer.recordValue(NOW_NANOS - start)
        partitionRegionRedundancyRecoveryInProgressMeter.decrement()
        partitionRegionRedundancyRecoveryCompletedMeter.increment()
    }

    override fun startBucketCreate(isRebalance: Boolean): Long {
        partitionRegionBucketCreateInProgressMeter.increment()
        if (isRebalance) {
            startRebalanceBucketCreate()
        }
        return NOW_NANOS
    }

    override fun endBucketCreate(start: Long, success: Boolean, isRebalance: Boolean) {
        val ts = NOW_NANOS
        partitionRegionBucketCreateInProgressMeter.decrement()
        partitionRegionBucketCreateTimer.increment(ts)
        if (success) {
            partitionRegionBucketCreateCompletedMeter.increment()
        } else {
            partitionRegionBucketCreateFailedMeter.increment()
        }
        if (isRebalance) {
            endRebalanceBucketCreate(start, ts, success)
        }
    }

    override fun startPrimaryTransfer(isRebalance: Boolean): Long {
        partitionRegionPrimaryTransferInProgressMeter.increment()
        if (isRebalance) {
            startRebalancePrimaryTransfer()
        }
        return NOW_NANOS
    }

    override fun endPrimaryTransfer(start: Long, success: Boolean, isRebalance: Boolean) {
        val ts = NOW_NANOS
        partitionRegionPrimaryTransferInProgressMeter.decrement()
        partitionRegionPrimaryTransferTimer.increment(ts)
        if (success) {
            partitionRegionPrimaryTransferCompletedMeter.increment()
        } else {
            partitionRegionPrimaryTransferFailedMeter.increment()
        }
        if (isRebalance) {
            endRebalancePrimaryTransfer(start, ts, success)
        }
    }

    private fun startRebalanceBucketCreate() {
        partitionRegionBucketCreateInProgressMeter.increment()
    }

    private fun endRebalanceBucketCreate(start: Long, end: Long, success: Boolean) {
        partitionRegionBucketCreateInProgressMeter.decrement()
        partitionRegionBucketCreateTimer.increment(end - start)
        if (success) {
            partitionRegionBucketCreateCompletedMeter.increment()
        } else {
            partitionRegionBucketCreateFailedMeter.increment()
        }
    }

    private fun startRebalancePrimaryTransfer() {
        partitionRegionPrimaryTransferInProgressMeter.increment()
    }

    private fun endRebalancePrimaryTransfer(start: Long, end: Long, success: Boolean) {
        partitionRegionPrimaryTransferInProgressMeter.decrement()
        partitionRegionPrimaryTransferTimer.increment(end - start)
        if (success) {
            partitionRegionPrimaryTransferCompletedMeter.increment()
        } else {
            partitionRegionPrimaryTransferFailedMeter.increment()
        }
    }

    override fun startApplyReplication(): Long {
        partitionRegionApplyReplicationInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endApplyReplication(start: Long) {
        partitionRegionApplyReplicationTimer.recordValue(NOW_NANOS - start)
        partitionRegionApplyReplicationInProgressMeter.decrement()
        partitionRegionApplyReplicationCompletedMeter.increment()

    }

    override fun startSendReplication(): Long {
        partitionRegionSendReplicationInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endSendReplication(start: Long) {
        partitionRegionSendReplicationTimer.recordValue(NOW_NANOS - start)
        partitionRegionSendReplicationInProgressMeter.decrement()
        partitionRegionSendReplicationCompletedMeter.increment()
    }

    override fun startPutRemote(): Long {
        partitionRegionPutRemoteInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endPutRemote(start: Long) {
        partitionRegionPutRemoteTimer.recordValue(NOW_NANOS - start)
        partitionRegionPutRemoteInProgressMeter.decrement()
        partitionRegionPutRemoteCompletedMeter.increment()
    }

    override fun startPutLocal(): Long {
        partitionRegionPutLocalInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endPutLocal(start: Long) {
        partitionRegionPutLocalTimer.recordValue(NOW_NANOS - start)
        partitionRegionPutLocalInProgressMeter.decrement()
        partitionRegionPutLocalCompletedMeter.increment()
    }

    override fun incPRMetaDataSentCount() {
        partitionRegionPRMetadataSentMeter.increment()
    }

    override fun setConfiguredRedundantCopies(copies: Int) {
        partitionRegionBucketConfiguredRedundancyMeter.setValue(copies)
    }

    override fun setActualRedundantCopies(copies: Int) {
        partitionRegionBucketActualRedundancyMeter.setValue(copies)
    }

    override fun close() {
        //noop
    }

    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDataStoreEntryCount(): Int = partitionRegionDataStoreEntryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDataStoreBytesInUse(): Long = partitionRegionDataStoreBytesMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getTotalBucketCount(): Int = partitionRegionBucketTotalMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringInProgress(): Int = partitionRegionPrimaryVolunteerInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringBecamePrimary(): Int = partitionRegionPrimaryVolunteerLocalMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringBecamePrimaryTime(): Long = partitionRegionPrimaryVolunteerLocalTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringOtherPrimary(): Int = partitionRegionPrimaryVolunteerRemoteMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringOtherPrimaryTime(): Long = partitionRegionPrimaryVolunteerRemoteTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringClosed(): Int = partitionRegionPrimaryVolunteerClosedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringClosedTime(): Long = partitionRegionPrimaryVolunteerClosedTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getTotalNumBuckets(): Int = partitionRegionBucketTotalMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPrimaryBucketCount(): Int = partitionRegionBucketPrimaryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getVolunteeringThreads(): Int = partitionRegionPrimaryVolunteerThreadsMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLowRedundancyBucketCount(): Int = partitionRegionBucketLowRedundancyMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getNoCopiesBucketCount(): Int = partitionRegionBucketNoRedundancyMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getConfiguredRedundantCopies(): Int = partitionRegionBucketConfiguredRedundancyMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getActualRedundantCopies(): Int = partitionRegionBucketActualRedundancyMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBucketCreatesInProgress(): Int = partitionRegionBucketCreateInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBucketCreatesCompleted(): Int = partitionRegionBucketCreateCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBucketCreatesFailed(): Int = partitionRegionBucketCreateFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBucketCreateTime(): Long = partitionRegionBucketCreateTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPrimaryTransfersInProgress(): Int = partitionRegionPrimaryTransferInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPrimaryTransfersCompleted(): Int = partitionRegionPrimaryTransferCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPrimaryTransfersFailed(): Int = partitionRegionPrimaryTransferFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPrimaryTransferTime(): Long = partitionRegionPrimaryTransferTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalanceBucketCreatesInProgress(): Int = partitionRegionBucketCreateInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalanceBucketCreatesCompleted(): Int = partitionRegionBucketCreateCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalanceBucketCreatesFailed(): Int = partitionRegionBucketCreateFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalanceBucketCreateTime(): Long = partitionRegionBucketCreateTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalancePrimaryTransfersInProgress(): Int = partitionRegionPrimaryTransferInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalancePrimaryTransfersCompleted(): Int = partitionRegionPrimaryTransferCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalancePrimaryTransfersFailed(): Int = partitionRegionPrimaryTransferFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRebalancePrimaryTransferTime(): Long = partitionRegionPrimaryTransferTimer.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPRMetaDataSentCount(): Long = partitionRegionPRMetadataSentMeter.getValue()
}
