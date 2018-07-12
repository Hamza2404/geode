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
package org.apache.geode.statistics.wan

import org.apache.geode.internal.cache.wan.GatewaySenderStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

open class MicrometerGatewaySenderStats @JvmOverloads constructor(statisticsFactory: StatisticsFactory?, private val queueName: String, private val groupName: String = "GatewayReceiverStats-$queueName") :
        MicrometerMeterGroup(statisticsFactory, groupName), GatewaySenderStats {


    override fun getGroupTags(): Array<String> = arrayOf("gatewaySenderName", queueName)
    open val queueStatPrefix: String by lazy { "gateway.sender" }

    private val gatewaySenderEventsReceivedMeter = CounterStatisticMeter("$queueStatPrefix.event.received.count", "Number of events received by this Sender.")
    private val gatewaySenderEventsQueueMeter = CounterStatisticMeter("$queueStatPrefix.event.queued.count", "Number of events added to the event queue.")
    private val gatewaySenderEventsQueueTimer = TimerStatisticMeter("$queueStatPrefix.event.queue.time", "Total time spent queueing events.", meterUnit = "nanoseconds")
    private val gatewaySenderEventQueueSizeMeter = GaugeStatisticMeter("$queueStatPrefix.event.queue.size", "Size of the event queue.", arrayOf("queueType", "primary"))
    private val gatewaySenderSecondaryEventQueueSizeMeter = GaugeStatisticMeter("$queueStatPrefix.event.queue.size", "Size of secondary event queue.", arrayOf("queueType", "secondary"))
    private val gatewaySenderEventsProcessedByPQRMMeter = GaugeStatisticMeter("$queueStatPrefix.event.processed.pqrm.count", "Total number of events processed by Parallel Queue Removal Message(PQRM).")
    private val gatewaySenderTempEventQueueSizeMeter = GaugeStatisticMeter("$queueStatPrefix.event.queue.size", "Size of the temporary events.", arrayOf("queueType", "temp"))
    private val gatewaySenderEventsNotQueuedConflatedMeter = CounterStatisticMeter("$queueStatPrefix.event.notqueued.conflated.count", "Number of events received but not added to the event queue because the queue already contains an event with the event'getQueueStatPrefix() key.")
    private val gatewaySenderEventsConflatedMeter = CounterStatisticMeter("$queueStatPrefix.event.conflated.count", "Number of events conflated from batches.")
    private val gatewaySenderEventsDistributionMeter = CounterStatisticMeter("$queueStatPrefix.event.distribution.count", "Number of events removed from the event queue and sent.")
    private val gatewaySenderEventExceededAlertThresholdMeter = CounterStatisticMeter("$queueStatPrefix.event.alertthreshold.count", "Number of events exceeding the alert threshold.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val gatewaySenderEventsDistributionTimer = CounterStatisticMeter("$queueStatPrefix.event.distribution.time", "Total time spent distributing batches of events to other gateway receivers.", meterUnit = "nanoseconds")
    private val gatewaySenderEventsBatchDistributedMeter = CounterStatisticMeter("$queueStatPrefix.batches.distributed.count", "Number of batches of events removed from the event queue and sent.")
    private val gatewaySenderEventsBatchRedistributedMeter = CounterStatisticMeter("$queueStatPrefix.batches.redistributed.count", "Number of batches of events removed from the event queue and resent.")
    private val gatewaySenderEventsBatchResizedMeter = CounterStatisticMeter("$queueStatPrefix.batches.resized.count", "Number of batches that were resized because they were too large")
    private val gatewaySenderUnprocessedTokenAddedByPrimaryMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.token.secondary.added.count", "Number of tokens added to the secondary'getQueueStatPrefix() unprocessed token map by the primary (though a listener).", arrayOf("actorType", "primary"))
    private val gatewaySenderUnprocessedEventAddedBySecondaryMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.event.secondary.added.count", "Number of events added to the secondary'getQueueStatPrefix() unprocessed event map by the secondary.", arrayOf("actorType", "secondary"))
    private val gatewaySenderUnprocessedEventRemovedByPrimaryMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.event.secondary.removed.count", "Number of events removed from the secondary'getQueueStatPrefix() unprocessed event map by the primary (though a listener).", arrayOf("actorType", "primary"))
    private val gatewaySenderUnprocessedTokenRemovedBySecondaryMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.token.secondary.removed.count", "Number of tokens removed from the secondary'getQueueStatPrefix() unprocessed token map by the secondary.", arrayOf("actorType", "secondary"))
    private val gatewaySenderUnprocessedEventRemovedByTimeoutMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.event.secondary.removed.count", "Number of events removed from the secondary'getQueueStatPrefix() unprocessed event map by a timeout.", arrayOf("actorType", "timeout"))
    private val gatewaySenderUnprocessedTokenRemovedByTimeoutMeter = CounterStatisticMeter("$queueStatPrefix.unprocessed.token.secondary.removed.count", "Number of tokens removed from the secondary'getQueueStatPrefix() unprocessed token map by a timeout.", arrayOf("actorType", "timeout"))
    private val gatewaySenderUnprocessedEventSizeMeter = GaugeStatisticMeter("$queueStatPrefix.unprocessed.event.secondary.size", "Current number of entries in the secondary'getQueueStatPrefix() unprocessed event map.")
    private val gatewaySenderUnprocessedTokenSizeMeter = GaugeStatisticMeter("$queueStatPrefix.unprocessed.token.secondary.size", "Current number of entries in the secondary'getQueueStatPrefix() unprocessed token map.")
    private val gatewaySenderConflationIndexSizeMeter = GaugeStatisticMeter("$queueStatPrefix.conflation.index.size", "Current number of entries in the conflation indexes map.")
    private val gatewaySenderEventsNotQueuedMeter = CounterStatisticMeter("$queueStatPrefix.notQueued.count", "Number of events not added to queue.")
    private val gatewaySenderEventsDroppedSenderNotRunningMeter = CounterStatisticMeter("$queueStatPrefix.events.dropped.count", "Number of events dropped because the primary gateway sender is not running.", arrayOf("reason", "primarySenderNotRunning"))
    private val gatewaySenderEventsDroppedFilteredMeter = CounterStatisticMeter("$queueStatPrefix.events.filtered.count", "Number of events filtered through GatewayEventFilter.")
    private val gatewaySenderLoadBalancesCompletedMeter = CounterStatisticMeter("$queueStatPrefix.loadBalances.completed.count", "Number of load balances completed")
    private val gatewaySenderLoadBalancesInProgressMeter = GaugeStatisticMeter("$queueStatPrefix.loadBalances.inprogress.count", "Number of load balances in progress")
    private val gatewaySenderLoadBalancesTimer = TimerStatisticMeter("$queueStatPrefix.loadBalances.time", "Total time spent load balancing this sender", meterUnit = "nanoseconds")
    private val gatewaySenderSynchronizationEventsQueuedMeter = CounterStatisticMeter("$queueStatPrefix.synchronization.events.queued.count", "Number of synchronization events added to the event queue.")
    private val gatewaySenderSynchronizationEventsSentMeter = CounterStatisticMeter("$queueStatPrefix.synchronization.events.sent.count", "Number of synchronization events provided to other members.")

    override fun initializeStaticMeters() {
        registerMeter(gatewaySenderEventsReceivedMeter)
        registerMeter(gatewaySenderEventsQueueMeter)
        registerMeter(gatewaySenderEventsQueueTimer)
        registerMeter(gatewaySenderEventQueueSizeMeter)
        registerMeter(gatewaySenderSecondaryEventQueueSizeMeter)
        registerMeter(gatewaySenderEventsProcessedByPQRMMeter)
        registerMeter(gatewaySenderTempEventQueueSizeMeter)
        registerMeter(gatewaySenderEventsNotQueuedConflatedMeter)
        registerMeter(gatewaySenderEventsConflatedMeter)
        registerMeter(gatewaySenderEventsDistributionMeter)
        registerMeter(gatewaySenderEventExceededAlertThresholdMeter)
        registerMeter(gatewaySenderEventsDistributionTimer)
        registerMeter(gatewaySenderEventsBatchDistributedMeter)
        registerMeter(gatewaySenderEventsBatchRedistributedMeter)
        registerMeter(gatewaySenderEventsBatchResizedMeter)
        registerMeter(gatewaySenderUnprocessedTokenAddedByPrimaryMeter)
        registerMeter(gatewaySenderUnprocessedEventAddedBySecondaryMeter)
        registerMeter(gatewaySenderUnprocessedEventRemovedByPrimaryMeter)
        registerMeter(gatewaySenderUnprocessedTokenRemovedBySecondaryMeter)
        registerMeter(gatewaySenderUnprocessedEventRemovedByTimeoutMeter)
        registerMeter(gatewaySenderUnprocessedTokenRemovedByTimeoutMeter)
        registerMeter(gatewaySenderUnprocessedEventSizeMeter)
        registerMeter(gatewaySenderUnprocessedTokenSizeMeter)
        registerMeter(gatewaySenderConflationIndexSizeMeter)
        registerMeter(gatewaySenderEventsNotQueuedMeter)
        registerMeter(gatewaySenderEventsDroppedSenderNotRunningMeter)
        registerMeter(gatewaySenderEventsDroppedFilteredMeter)
        registerMeter(gatewaySenderLoadBalancesCompletedMeter)
        registerMeter(gatewaySenderLoadBalancesInProgressMeter)
        registerMeter(gatewaySenderLoadBalancesTimer)
        registerMeter(gatewaySenderSynchronizationEventsQueuedMeter)
        registerMeter(gatewaySenderSynchronizationEventsSentMeter)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsReceived(): Int = gatewaySenderEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsQueued(): Int = gatewaySenderEventsQueueMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsNotQueuedConflated(): Int = gatewaySenderEventsNotQueuedConflatedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsConflatedFromBatches(): Int = gatewaySenderEventsConflatedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventQueueSize(): Int = gatewaySenderEventQueueSizeMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getSecondaryEventQueueSize(): Int = gatewaySenderSecondaryEventQueueSizeMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsProcessedByPQRM(): Int = gatewaySenderEventsProcessedByPQRMMeter.getValue().toInt()

    override fun setEventsProcessedByPQRM(size: Int) {
        gatewaySenderEventsProcessedByPQRMMeter.setValue(size)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getTempEventQueueSize(): Int = gatewaySenderTempEventQueueSizeMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsDistributed(): Int = gatewaySenderEventsDistributionMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsExceedingAlertThreshold(): Int = gatewaySenderEventExceededAlertThresholdMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBatchesDistributed(): Int = gatewaySenderEventsBatchDistributedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBatchesRedistributed(): Int = gatewaySenderEventsBatchRedistributedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBatchesResized(): Int = gatewaySenderEventsBatchResizedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedTokensAddedByPrimary(): Int = gatewaySenderUnprocessedTokenAddedByPrimaryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedEventsAddedBySecondary(): Int = gatewaySenderUnprocessedEventAddedBySecondaryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedEventsRemovedByPrimary(): Int = gatewaySenderUnprocessedEventRemovedByPrimaryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedTokensRemovedBySecondary(): Int = gatewaySenderUnprocessedTokenRemovedBySecondaryMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedEventMapSize(): Int = gatewaySenderUnprocessedEventSizeMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnprocessedTokenMapSize(): Int = gatewaySenderUnprocessedTokenSizeMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsNotQueued(): Int = gatewaySenderEventsNotQueuedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsDroppedDueToPrimarySenderNotRunning(): Int = gatewaySenderEventsDroppedSenderNotRunningMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsFiltered(): Int = gatewaySenderEventsDroppedFilteredMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getConflationIndexesMapSize(): Int = gatewaySenderConflationIndexSizeMeter.getValue().toInt()

    override fun incEventsReceived() {
        gatewaySenderEventsReceivedMeter.increment()
    }

    override fun incEventsExceedingAlertThreshold() {
        gatewaySenderEventExceededAlertThresholdMeter.increment()
    }

    override fun incBatchesRedistributed() {
        gatewaySenderEventsBatchDistributedMeter.increment()
    }

    override fun incBatchesResized() {
        gatewaySenderEventsBatchResizedMeter.increment()
    }

    override fun setQueueSize(size: Int) {
        gatewaySenderEventQueueSizeMeter.setValue(size)
    }

    override fun setSecondaryQueueSize(size: Int) {
        gatewaySenderSecondaryEventQueueSizeMeter.setValue(size)
    }

    override fun setTempQueueSize(size: Int) {
        gatewaySenderTempEventQueueSizeMeter.setValue(size)
    }

    override fun incQueueSize(delta: Int) {
        gatewaySenderEventQueueSizeMeter.increment(delta)
    }

    override fun incQueueSize() {
        gatewaySenderEventQueueSizeMeter.increment()
    }

    override fun incSecondaryQueueSize(delta: Int) {
        gatewaySenderSecondaryEventQueueSizeMeter.increment(delta)
    }

    override fun incSecondaryQueueSize() {
        gatewaySenderSecondaryEventQueueSizeMeter.increment()
    }

    override fun incTempQueueSize(delta: Int) {
        gatewaySenderTempEventQueueSizeMeter.increment(delta)
    }

    override fun incTempQueueSize() {
        gatewaySenderTempEventQueueSizeMeter.increment()
    }

    override fun incEventsProcessedByPQRM(delta: Int) {
        gatewaySenderEventsProcessedByPQRMMeter.increment(delta)
    }

    override fun decQueueSize(delta: Int) {
        gatewaySenderEventQueueSizeMeter.increment(delta)
    }

    override fun decQueueSize() {
        gatewaySenderEventQueueSizeMeter.increment()
    }

    override fun decSecondaryQueueSize(delta: Int) {
        gatewaySenderSecondaryEventQueueSizeMeter.increment(delta)
    }

    override fun decSecondaryQueueSize() {
        gatewaySenderSecondaryEventQueueSizeMeter.increment()
    }

    override fun decTempQueueSize(delta: Int) {
        gatewaySenderTempEventQueueSizeMeter.increment(delta)
    }

    override fun decTempQueueSize() {
        gatewaySenderTempEventQueueSizeMeter.increment()
    }

    override fun incEventsNotQueuedConflated() {
        gatewaySenderEventsNotQueuedConflatedMeter.increment()
    }

    override fun incEventsConflatedFromBatches(numEvents: Int) {
        gatewaySenderEventsConflatedMeter.increment(numEvents)
    }

    override fun incEventsNotQueued() {
        gatewaySenderEventsNotQueuedMeter.increment()
    }

    override fun incEventsDroppedDueToPrimarySenderNotRunning() {
        gatewaySenderEventsDroppedSenderNotRunningMeter.increment()
    }

    override fun incEventsFiltered() {
        gatewaySenderEventsDroppedFilteredMeter.increment()
    }

    override fun incUnprocessedTokensAddedByPrimary() {
        gatewaySenderUnprocessedTokenAddedByPrimaryMeter.increment()
        incUnprocessedTokenMapSize()
    }

    override fun incUnprocessedEventsAddedBySecondary() {
        gatewaySenderUnprocessedEventAddedBySecondaryMeter.increment()
        incUnprocessedEventMapSize()
    }

    override fun incUnprocessedEventsRemovedByPrimary() {
        gatewaySenderUnprocessedEventRemovedByPrimaryMeter.increment()
        decUnprocessedEventMapSize()
    }

    override fun incUnprocessedTokensRemovedBySecondary() {
        gatewaySenderUnprocessedTokenRemovedBySecondaryMeter.increment()
        decUnprocessedTokenMapSize()
    }

    override fun incUnprocessedEventsRemovedByTimeout(count: Int) {
        gatewaySenderUnprocessedEventRemovedByTimeoutMeter.increment(count)
        decUnprocessedEventMapSize(count)
    }

    override fun incUnprocessedTokensRemovedByTimeout(count: Int) {
        gatewaySenderUnprocessedTokenRemovedByTimeoutMeter.increment(count)
        decUnprocessedTokenMapSize(count)
    }

    override fun clearUnprocessedMaps() {
        gatewaySenderUnprocessedEventSizeMeter.setValue(0)
        gatewaySenderUnprocessedTokenSizeMeter.setValue(0)
    }

    private fun incUnprocessedEventMapSize() {
        gatewaySenderUnprocessedEventSizeMeter.increment()
    }

    private fun decUnprocessedEventMapSize(count: Int = 1) {
        gatewaySenderUnprocessedEventSizeMeter.decrement(count)
    }


    private fun incUnprocessedTokenMapSize() {
        gatewaySenderUnprocessedTokenSizeMeter
    }

    private fun decUnprocessedTokenMapSize(decCount: Int = 1) {
        gatewaySenderUnprocessedTokenSizeMeter.decrement(decCount)
    }

    override fun incConflationIndexesMapSize() {
        gatewaySenderConflationIndexSizeMeter.increment()
    }

    override fun decConflationIndexesMapSize() {
        gatewaySenderConflationIndexSizeMeter.decrement()
    }

    override fun startTime(): Long = NOW_NANOS

    override fun endBatch(start: Long, numberOfEvents: Int) {
        // Increment event distribution time
        gatewaySenderEventsDistributionTimer.increment(NOW_NANOS - start)

        // Increment number of batches distributed
        gatewaySenderEventsBatchDistributedMeter.increment()

        // Increment number of events distributed
        gatewaySenderEventsDistributionMeter.increment()
    }

    override fun endPut(start: Long) {
        gatewaySenderEventsQueueTimer.recordValue(NOW_NANOS - start)
        // Increment number of event queued
        gatewaySenderEventsQueueMeter.increment()
    }

    override fun startLoadBalance(): Long {
        gatewaySenderLoadBalancesInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endLoadBalance(start: Long) {
        gatewaySenderLoadBalancesTimer.recordValue(NOW_NANOS - start)
        gatewaySenderLoadBalancesInProgressMeter.decrement()
        gatewaySenderLoadBalancesCompletedMeter.increment()
    }

    override fun incSynchronizationEventsEnqueued() {
        gatewaySenderSynchronizationEventsQueuedMeter.increment()
    }

    override fun incSynchronizationEventsProvided() {
        gatewaySenderSynchronizationEventsSentMeter.increment()
    }

    override fun close() {
        //noop
    }

    override fun getBatchDistributionTime(): Long = gatewaySenderEventsDistributionTimer.getValue()

    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}