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
package org.apache.geode.statistics.resourcemanger

import org.apache.geode.distributed.internal.PoolStatHelper
import org.apache.geode.distributed.internal.QueueStatHelper
import org.apache.geode.internal.cache.control.ResourceManagerStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerResourceManagerStats() :
        MicrometerMeterGroup(statisticsFactory = null,groupName = "ResourceManagerStats"), ResourceManagerStats, MicrometerStatsImplementor {

    constructor(statisticsFactory: StatisticsFactory?, name: String?): this()

    private val resourceRebalanceInProgressMeter = GaugeStatisticMeter("manager.resources.rebalance.inprogress.count", "Current number of cache rebalance operations being directed by this process.")
    private val resourceRebalanceCompletedMeter = CounterStatisticMeter("manager.resources.rebalance.completed.count", "Total number of cache rebalance operations directed by this process.")
    private val resourceAutoRebalanceAttemptsMeter = CounterStatisticMeter("manager.resources.rebalance.auto.attempts.count", "Total number of cache auto-rebalance attempts.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val resourceRebalanceTimer = CounterStatisticMeter("manager.resources.rebalance.time", "Total time spent directing cache rebalance operations.", meterUnit = "nanoseconds")

    private val resourceRebalanceBucketCreateInProgressMeter = GaugeStatisticMeter("manager.resources.rebalance.bucket.create.inprogress.count", "Current number of bucket create operations being directed for rebalancing.")
    private val resourceRebalanceBucketCreateCompletedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.create.complete.count", "Total number of bucket create operations directed for rebalancing.")
    private val resourceRebalanceBucketCreateFailedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.create.failed.count", "Total number of bucket create operations directed for rebalancing that failed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val resourceRebalanceBucketCreateTimer = CounterStatisticMeter("manager.resources.rebalance.bucket.create.time", "Total time spent directing bucket create operations for rebalancing.", meterUnit = "nanoseconds")
    private val resourceRebalanceBucketCreateBytesMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.create.bytes", "Total bytes created while directing bucket create operations for rebalancing.", meterUnit = "bytes")

    private val resourceRebalanceBucketRemoveInProgressMeter = GaugeStatisticMeter("manager.resources.rebalance.bucket.remove.inprogress.count", "Current number of bucket remove operations being directed for rebalancing.")
    private val resourceRebalanceBucketRemoveCompletedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.remove.complete.count", "Total number of bucket remove operations directed for rebalancing.")
    private val resourceRebalanceBucketRemoveFailedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.remove.failed.count", "Total number of bucket remove operations directed for rebalancing that failed.")
    private val resourceRebalanceBucketRemoveTimer = TimerStatisticMeter("manager.resources.rebalance.bucket.remove.time", "Total time spent directing bucket remove operations for rebalancing.", meterUnit = "nanoseconds")
    private val resourceRebalanceBucketRemoveBytesMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.remove.bytes", "Total bytes removed while directing bucket remove operations for rebalancing.", meterUnit = "bytes")

    private val resourceRebalanceBucketTransferInProgressMeter = GaugeStatisticMeter("manager.resources.rebalance.bucket.transfer.inprogress.count", "Current number of bucket transfer operations being directed for rebalancing.")
    private val resourceRebalanceBucketTransferCompletedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.transfer.complete.count", "Total number of bucket transfer operations directed for rebalancing.")
    private val resourceRebalanceBucketTransferFailedMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.transfer.failed.count", "Total number of bucket transfer operations directed for rebalancing that failed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val resourceRebalanceBucketTransferTimer = CounterStatisticMeter("manager.resources.rebalance.bucket.transfer.time", "Total time spent directing bucket transfer operations for rebalancing.", meterUnit = "nanoseconds")
    private val resourceRebalanceBucketTransferBytesMeter = CounterStatisticMeter("manager.resources.rebalance.bucket.transfer.bytes", "Total bytes transfered while directing bucket transfer operations for rebalancing.", meterUnit = "bytes")

    private val resourceRebalancePrimaryTransferInProgressMeter = GaugeStatisticMeter("manager.resources.rebalance.primary.transfer.inprogress.count", "Current number of primary transfer operations being directed for rebalancing.")
    private val resourceRebalancePrimaryTransferCompletedMeter = CounterStatisticMeter("manager.resources.rebalance.primary.transfer.complete.count", "Total number of primary transfer operations directed for rebalancing.")
    private val resourceRebalancePrimaryTransferFailedMeter = CounterStatisticMeter("manager.resources.rebalance.primary.transfer.failed.count", "Total number of primary transfer operations directed for rebalancing that failed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val resourceRebalancePrimaryTransferTimer = CounterStatisticMeter("manager.resources.rebalance.primary.transfer.time", "Total time spent directing primary transfer operations for rebalancing.", meterUnit = "nanoseconds")
    private val resourceRebalancePrimaryTransferChangeMeter = CounterStatisticMeter("manager.resources.rebalance.primary.transfer.change.count", "The number of times that membership has changed during a rebalance")

    private val resourceHeapAboveCriticalThresholdMeter = GaugeStatisticMeter("manager.resource.heap.event.count", "Total number of times the heap usage went over critical threshold.", arrayOf("eventType", "critical", "thresholdTrigger", "above"))
    private val resourceOffheapAboveCriticalThresholdMeter = GaugeStatisticMeter("manager.resource.offheap.event.count", "Total number of times off-heap usage went over critical threshold.", arrayOf("eventType", "critical", "thresholdTrigger", "above"))
    private val resourceHeapBelowCriticalThresholdMeter = GaugeStatisticMeter("manager.resource.heap.event.count", "Total number of times the heap usage fell below critical threshold.", arrayOf("eventType", "critical", "thresholdTrigger", "below"))
    private val resourceOffheapBelowCriticalThresholdMeter = GaugeStatisticMeter("manager.resource.offheap.event.count", "Total number of times off-heap usage fell below critical threshold.", arrayOf("eventType", "critical", "thresholdTrigger", "below"))
    private val resourceHeapAboveEvictionStartMeter = GaugeStatisticMeter("manager.resource.heap.start.count", "Total number of times heap usage went over eviction threshold.", arrayOf("eventType", "eviction"))
    private val resourceOffheapAboveEvictionStartMeter = GaugeStatisticMeter("manager.resource.offheap.start.count", "Total number of times off-heap usage went over eviction threshold.", arrayOf("eventType", "eviction"))
    private val resourceHeapAboveEvictionStopMeter = GaugeStatisticMeter("manager.resource.heap.stop.count", "Total number of times heap usage fell below eviction threshold.", arrayOf("eventType", "eviction"))
    private val resourceOffheapAboveEvictionStopMeter = GaugeStatisticMeter("manager.resource.offheap.stop.count", "Total number of times off-heap usage fell below eviction threshold.", arrayOf("eventType", "eviction"))

    private val resourceHeapCriticalThresholdMaxBytesMeter = GaugeStatisticMeter("manager.resource.heap.threshold.bytes", "The currently set heap critical threshold value in bytes", arrayOf("eventType", "critical"))
    private val resourceOffheapCriticalThresholdMaxBytesMeter = GaugeStatisticMeter("manager.resource.offheap.threshold.bytes", "The currently set off-heap critical threshold value in bytes", arrayOf("eventType", "critical"))
    private val resourceHeapEvictionThresholdMaxBytesMeter = GaugeStatisticMeter("manager.resource.heap.threshold.bytes", "The currently set heap eviction threshold value in bytes", arrayOf("eventType", "eviction"))
    private val resourceOffheapEvictionThresholdMaxBytesMeter = GaugeStatisticMeter("manager.resource.offheap.threshold.bytes", "The currently set off-heap eviction threshold value in bytes", arrayOf("eventType", "eviction"))

    private val resourceHeapTenuredUsedBytesMeter = GaugeStatisticMeter("manager.resource.heap.tenured.used.bytes", "Total memory used in the tenured/old space", meterUnit = "bytes")
    private val resourceEventsDeliveredMeter = CounterStatisticMeter("manager.resource.events.delivered.count", "Total number of resource events delivered to listeners")
    private val resourceEventQueueSizeMeter = GaugeStatisticMeter("manager.resource.events.queue.size", "Pending events for thresholdEventProcessor thread")
    private val resourceEventProcessorThreadsMeter = GaugeStatisticMeter("manager.resource.events.processor.thread.count", "Number of jobs currently being processed by the thresholdEventProcessorThread")
    private val resourceThreadsStuckMeter = GaugeStatisticMeter("manager.resource.threads.stuck.count", "Number of running threads that have not changed state within the thread-monitor-time-limit-ms interval.")

    override fun initializeStaticMeters() {
        registerMeter(resourceRebalanceInProgressMeter)
        registerMeter(resourceRebalanceCompletedMeter)
        registerMeter(resourceAutoRebalanceAttemptsMeter)
        registerMeter(resourceRebalanceTimer)

        registerMeter(resourceRebalanceBucketCreateInProgressMeter)
        registerMeter(resourceRebalanceBucketCreateCompletedMeter)
        registerMeter(resourceRebalanceBucketCreateFailedMeter)
        registerMeter(resourceRebalanceBucketCreateTimer)
        registerMeter(resourceRebalanceBucketCreateBytesMeter)

        registerMeter(resourceRebalanceBucketRemoveInProgressMeter)
        registerMeter(resourceRebalanceBucketRemoveCompletedMeter)
        registerMeter(resourceRebalanceBucketRemoveFailedMeter)
        registerMeter(resourceRebalanceBucketRemoveTimer)
        registerMeter(resourceRebalanceBucketRemoveBytesMeter)

        registerMeter(resourceRebalanceBucketTransferInProgressMeter)
        registerMeter(resourceRebalanceBucketTransferCompletedMeter)
        registerMeter(resourceRebalanceBucketTransferFailedMeter)
        registerMeter(resourceRebalanceBucketTransferTimer)
        registerMeter(resourceRebalanceBucketTransferBytesMeter)

        registerMeter(resourceRebalancePrimaryTransferInProgressMeter)
        registerMeter(resourceRebalancePrimaryTransferCompletedMeter)
        registerMeter(resourceRebalancePrimaryTransferFailedMeter)
        registerMeter(resourceRebalancePrimaryTransferTimer)
        registerMeter(resourceRebalancePrimaryTransferChangeMeter)

        registerMeter(resourceHeapAboveCriticalThresholdMeter)
        registerMeter(resourceOffheapAboveCriticalThresholdMeter)
        registerMeter(resourceHeapBelowCriticalThresholdMeter)
        registerMeter(resourceOffheapBelowCriticalThresholdMeter)
        registerMeter(resourceHeapAboveEvictionStartMeter)
        registerMeter(resourceOffheapAboveEvictionStartMeter)
        registerMeter(resourceHeapAboveEvictionStopMeter)
        registerMeter(resourceOffheapAboveEvictionStopMeter)

        registerMeter(resourceHeapCriticalThresholdMaxBytesMeter)
        registerMeter(resourceOffheapCriticalThresholdMaxBytesMeter)
        registerMeter(resourceHeapEvictionThresholdMaxBytesMeter)
        registerMeter(resourceOffheapEvictionThresholdMaxBytesMeter)

        registerMeter(resourceHeapTenuredUsedBytesMeter)
        registerMeter(resourceEventsDeliveredMeter)
        registerMeter(resourceEventQueueSizeMeter)
        registerMeter(resourceEventProcessorThreadsMeter)
        registerMeter(resourceThreadsStuckMeter)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancesInProgress(): Int = resourceRebalanceInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancesCompleted(): Int = resourceRebalanceCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAutoRebalanceAttempts(): Int = resourceAutoRebalanceAttemptsMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketCreatesInProgress(): Int = resourceRebalanceBucketCreateInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketCreatesCompleted(): Int = resourceRebalanceBucketCreateCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketCreatesFailed(): Int = resourceRebalanceBucketCreateFailedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketCreateBytes(): Long = resourceRebalanceBucketCreateBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketTransfersInProgress(): Int = resourceRebalanceBucketTransferInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketTransfersCompleted(): Int = resourceRebalanceBucketTransferCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketTransfersFailed(): Int = resourceRebalanceBucketTransferFailedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketTransfersBytes(): Long = resourceRebalanceBucketTransferBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancePrimaryTransfersInProgress(): Int = resourceRebalancePrimaryTransferInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancePrimaryTransfersCompleted(): Int = resourceRebalancePrimaryTransferCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancePrimaryTransfersFailed(): Int = resourceRebalancePrimaryTransferFailedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getResourceEventsDelivered(): Int = resourceEventsDeliveredMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeapCriticalEvents(): Int = resourceHeapAboveCriticalThresholdMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapCriticalEvents(): Int = resourceOffheapAboveCriticalThresholdMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeapSafeEvents(): Int = resourceHeapBelowCriticalThresholdMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapSafeEvents(): Int = resourceOffheapBelowCriticalThresholdMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getEvictionStartEvents(): Int = resourceHeapAboveEvictionStartMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapEvictionStartEvents(): Int = resourceOffheapAboveEvictionStartMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getEvictionStopEvents(): Int = resourceHeapAboveEvictionStopMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapEvictionStopEvents(): Int = resourceOffheapAboveEvictionStopMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCriticalThreshold(): Long = resourceHeapCriticalThresholdMaxBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapCriticalThreshold(): Long = resourceOffheapCriticalThresholdMaxBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getEvictionThreshold(): Long = resourceHeapEvictionThresholdMaxBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOffHeapEvictionThreshold(): Long = resourceOffheapEvictionThresholdMaxBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTenuredHeapUsed(): Long = resourceHeapTenuredUsedBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getResourceEventQueueSize(): Int = resourceEventQueueSizeMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getThresholdEventProcessorThreadJobs(): Int = resourceEventProcessorThreadsMeter.getValue().toInt()

    override fun getResourceEventQueueStatHelper(): QueueStatHelper = object : QueueStatHelper {
        override fun add() {
            incResourceEventQueueSize(1)
        }

        override fun remove() {
            incResourceEventQueueSize(-1)
        }

        override fun remove(count: Int) {
            incResourceEventQueueSize(-1 * count)
        }
    }

    override fun getResourceEventPoolStatHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun endJob() {
                    incThresholdEventProcessorThreadJobs(-1)
                }

                override fun startJob() {
                    incThresholdEventProcessorThreadJobs(1)
                }
            }


    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumThreadStuck(): Int = resourceThreadsStuckMeter.getValue().toInt()

    override fun setNumThreadStuck(value: Int) {
        resourceThreadsStuckMeter.setValue(value)
    }


    override fun startRebalance(): Long {
        resourceRebalanceInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun incAutoRebalanceAttempts() {
        resourceAutoRebalanceAttemptsMeter.increment()
    }

    override fun endRebalance(start: Long) {
        resourceRebalanceTimer.increment(NOW_NANOS - start)
        resourceRebalanceInProgressMeter.decrement()
        resourceRebalanceCompletedMeter.increment()
    }

    override fun startBucketCreate(regions: Int) {
        resourceRebalanceBucketCreateInProgressMeter.increment(regions)
    }

    override fun endBucketCreate(regions: Int, success: Boolean, bytes: Long, elapsed: Long) {
        resourceRebalanceBucketCreateInProgressMeter.decrement(regions)
        resourceRebalanceBucketCreateTimer.increment(elapsed)
        if (success) {
            resourceRebalanceBucketCreateCompletedMeter.increment(regions)
            resourceRebalanceBucketCreateBytesMeter.increment(bytes)
        } else {
            resourceRebalanceBucketCreateFailedMeter.increment(regions)
        }
    }

    override fun startBucketRemove(regions: Int) {
        resourceRebalanceBucketRemoveInProgressMeter.increment(regions)
    }

    override fun endBucketRemove(regions: Int, success: Boolean, bytes: Long, elapsed: Long) {
        resourceRebalanceBucketRemoveInProgressMeter.decrement(regions)
        resourceRebalanceBucketRemoveTimer.recordValue(elapsed)
        if (success) {
            resourceRebalanceBucketRemoveCompletedMeter.increment(regions)
            resourceRebalanceBucketRemoveBytesMeter.increment(bytes)
        } else {
            resourceRebalanceBucketRemoveFailedMeter.increment(regions)
        }
    }

    override fun startBucketTransfer(regions: Int) {
        resourceRebalanceBucketTransferInProgressMeter.increment(regions)
    }

    override fun endBucketTransfer(regions: Int, success: Boolean, bytes: Long, elapsed: Long) {
        resourceRebalanceBucketTransferInProgressMeter.decrement(regions)
        resourceRebalanceBucketTransferTimer.increment(elapsed)
        if (success) {
            resourceRebalanceBucketTransferCompletedMeter.increment(regions)
            resourceRebalanceBucketTransferBytesMeter.increment(bytes)
        } else {
            resourceRebalanceBucketTransferFailedMeter.increment(regions)
        }
    }

    override fun startPrimaryTransfer(regions: Int) {
        resourceRebalancePrimaryTransferInProgressMeter.increment(regions)
    }

    override fun endPrimaryTransfer(regions: Int, success: Boolean, elapsed: Long) {
        resourceRebalancePrimaryTransferInProgressMeter.decrement(regions)
        resourceRebalancePrimaryTransferTimer.increment(elapsed)
        if (success) {
            resourceRebalancePrimaryTransferCompletedMeter.increment(regions)
        } else {
            resourceRebalancePrimaryTransferFailedMeter.increment(regions)
        }
    }

    override fun incRebalanceMembershipChanges(delta: Int) {
        resourceRebalancePrimaryTransferChangeMeter.increment(delta)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceMembershipChanges(): Int = resourceRebalancePrimaryTransferChangeMeter.getValue().toInt()

    override fun incResourceEventsDelivered() {
        resourceEventsDeliveredMeter.increment()
    }

    override fun incHeapCriticalEvents() {
        resourceHeapAboveCriticalThresholdMeter.increment()
    }

    override fun incOffHeapCriticalEvents() {
        resourceOffheapAboveCriticalThresholdMeter.increment()
    }

    override fun incHeapSafeEvents() {
        resourceHeapBelowCriticalThresholdMeter.increment()
    }

    override fun incOffHeapSafeEvents() {
        resourceOffheapBelowCriticalThresholdMeter.increment()
    }

    override fun incEvictionStartEvents() {
        resourceHeapAboveEvictionStartMeter.increment()
    }

    override fun incOffHeapEvictionStartEvents() {
        resourceOffheapAboveEvictionStartMeter.increment()
    }

    override fun incEvictionStopEvents() {
        resourceHeapAboveEvictionStopMeter.increment()
    }

    override fun incOffHeapEvictionStopEvents() {
        resourceOffheapAboveEvictionStopMeter.increment()
    }

    override fun changeCriticalThreshold(newValue: Long) {
        resourceHeapCriticalThresholdMaxBytesMeter.setValue(newValue)
    }

    override fun changeOffHeapCriticalThreshold(newValue: Long) {
        resourceOffheapCriticalThresholdMaxBytesMeter.setValue(newValue)
    }

    override fun changeEvictionThreshold(newValue: Long) {
        resourceHeapEvictionThresholdMaxBytesMeter.setValue(newValue)
    }

    override fun changeOffHeapEvictionThreshold(newValue: Long) {
        resourceOffheapEvictionThresholdMaxBytesMeter.setValue(newValue)
    }

    override fun changeTenuredHeapUsed(newValue: Long) {
        resourceHeapTenuredUsedBytesMeter.setValue(newValue)
    }

    override fun incResourceEventQueueSize(delta: Int) {
        resourceEventQueueSizeMeter.increment(delta)
    }

    override fun incThresholdEventProcessorThreadJobs(delta: Int) {
        resourceEventProcessorThreadsMeter.increment(delta)
    }

    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceTime(): Long = resourceRebalanceTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketCreateTime(): Long = resourceRebalanceBucketCreateTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalanceBucketTransfersTime(): Long = resourceRebalanceBucketTransferTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRebalancePrimaryTransferTime(): Long = resourceRebalancePrimaryTransferTimer.getValue()

}