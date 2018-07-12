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
package org.apache.geode.statistics.cache

import org.apache.geode.internal.cache.tier.sockets.CacheClientNotifierStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerCacheClientNotifierStats(statisticsFactory: StatisticsFactory?,private val clientName: String) :
        MicrometerMeterGroup(statisticsFactory,"CacheClientNotifierStats-$clientName"), CacheClientNotifierStats {

    override fun getGroupTags(): Array<String> = arrayOf("clientName", clientName)

    private val clientNotifierEventsMeter = CounterStatisticMeter("client.notifier.events.count", "Number of events processed by the cache client notifier.", arrayOf("clientName", clientName))
    private val clientNotifierEventProcessedTimer = CounterStatisticMeter("client.notifier.events.processed.time", "Total time spent by the cache client notifier processing events.", arrayOf("clientName", clientName), meterUnit = "nanoseconds")
    private val clientNotifierClientRegistrationMeter = GaugeStatisticMeter("client.notifier.client.registration.count", "Number of clients that have registered for updates.", arrayOf("clientName", clientName))
    private val clientNotifierClientRegistrationTimer = CounterStatisticMeter("client.notifier.client.registration.time", "Total time spent doing client registrations.", arrayOf("clientName", clientName), meterUnit = "nanoseconds")
    private val clientNotifierHealthMonitorRegistrationMeter = GaugeStatisticMeter("client.notifier.healthmonitor.registration.count", "Number of client Register.", arrayOf("clientName", clientName))
    private val clientNotifierHealthMonitorUnregistrationMeter = GaugeStatisticMeter("client.notifier.healthmonitor.unregistration.count", "Number of client UnRegister.", arrayOf("clientName", clientName))
    private val clientNotifierDurableReconnectionMeter = CounterStatisticMeter("client.notifier.durable.reconnection.count", "Number of times the same durable client connects to the server", arrayOf("clientName", clientName))
    private val clientNotifierDurableQueueDropMeter = CounterStatisticMeter("client.notifier.durable.queue.drop.count", "Number of times client queue for a particular durable client is dropped", arrayOf("clientName", clientName))
    private val clientNotifierDurableQueueSizeMeter = CounterStatisticMeter("client.notifier.durable.queue.count", "Number of events enqueued in queue for a durable client ", arrayOf("clientName", clientName))
    private val clientNotifierCQProcessingTimer = CounterStatisticMeter("client.notifier.cq.processing.time", "Total time spent by the cache client notifier processing cqs.", arrayOf("clientName", clientName), meterUnit = "nanoseconds")
    private val clientNotifierCompiledQueryMeter = GaugeStatisticMeter("client.notifier.compiledquery.count", "Number of compiled queries maintained.", arrayOf("clientName", clientName))
    private val clientNotifierCompiledQueryUsedMeter = CounterStatisticMeter("client.notifier.compiledquery.used.count", "Number of times compiled queries are used.", arrayOf("clientName", clientName))

    override fun initializeStaticMeters() {
        registerMeter(clientNotifierEventsMeter)
        registerMeter(clientNotifierEventProcessedTimer)
        registerMeter(clientNotifierClientRegistrationMeter)
        registerMeter(clientNotifierClientRegistrationTimer)
        registerMeter(clientNotifierHealthMonitorRegistrationMeter)
        registerMeter(clientNotifierHealthMonitorUnregistrationMeter)
        registerMeter(clientNotifierDurableReconnectionMeter)
        registerMeter(clientNotifierDurableQueueDropMeter)
        registerMeter(clientNotifierDurableQueueSizeMeter)
        registerMeter(clientNotifierCQProcessingTimer)
        registerMeter(clientNotifierCompiledQueryMeter)
        registerMeter(clientNotifierCompiledQueryUsedMeter)
    }

    override fun close() {
        //noop
    }

    override fun getEvents(): Int = clientNotifierEventsMeter.getValue().toInt()

    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    override fun getEventProcessingTime(): Long = clientNotifierEventProcessedTimer.getValue()

    override fun getClientRegisterRequests(): Int = clientNotifierClientRegistrationMeter.getValue().toInt()

    override fun get_durableReconnectionCount(): Int = clientNotifierDurableReconnectionMeter.getValue().toInt()

    override fun get_queueDroppedCount(): Int = clientNotifierDurableQueueDropMeter.getValue().toInt()

    override fun get_eventEnqueuedWhileClientAwayCount(): Int = clientNotifierDurableQueueSizeMeter.getValue().toInt()

    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    override fun getCqProcessingTime(): Long = clientNotifierCQProcessingTimer.getValue()

    override fun getCompiledQueryCount(): Long = clientNotifierCompiledQueryMeter.getValue()

    override fun getCompiledQueryUsedCount(): Long = clientNotifierCompiledQueryUsedMeter.getValue()

    override fun getClientUnRegisterRequests(): Int = clientNotifierHealthMonitorUnregistrationMeter.getValue().toInt()

    override fun startTime(): Long = NOW_NANOS

    override fun endEvent(start: Long) {
        clientNotifierEventsMeter.increment()
        clientNotifierEventProcessedTimer.increment(NOW_NANOS - start)
    }

    override fun endClientRegistration(start: Long) {
        clientNotifierClientRegistrationMeter.increment()
        clientNotifierClientRegistrationTimer.increment(NOW_NANOS - start)
    }

    override fun endCqProcessing(start: Long) {
        clientNotifierCQProcessingTimer.increment(NOW_NANOS - start)
    }

    override fun incClientRegisterRequests() {
        clientNotifierHealthMonitorRegistrationMeter.increment()
    }

    override fun incDurableReconnectionCount() {
        clientNotifierDurableReconnectionMeter.increment()
    }

    override fun incQueueDroppedCount() {
        clientNotifierDurableQueueDropMeter.increment()
    }

    override fun incEventEnqueuedWhileClientAwayCount() {
        clientNotifierDurableQueueSizeMeter.increment()
    }

    override fun incClientUnRegisterRequests() {
        clientNotifierHealthMonitorUnregistrationMeter.increment()
    }

    override fun incCompiledQueryCount(count: Long) {
        clientNotifierCompiledQueryMeter.increment(count)
    }

    override fun incCompiledQueryUsedCount(count: Long) {
        clientNotifierCompiledQueryUsedMeter.increment(count)
    }
}
