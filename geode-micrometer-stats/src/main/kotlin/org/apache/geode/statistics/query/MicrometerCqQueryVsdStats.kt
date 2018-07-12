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
package org.apache.geode.statistics.query

import org.apache.geode.cache.query.internal.CqQueryVsdStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

class MicrometerCqQueryVsdStats(statisticsFactory: StatisticsFactory?,private val cqName: String) :
        MicrometerMeterGroup(statisticsFactory,"CqQueryStats-$cqName"), CqQueryVsdStats {

    override fun getGroupTags(): Array<String> = arrayOf("cqName", cqName)

    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val cqInitialResultsTimer = CounterStatisticMeter("cq.initialResult.time", "The total amount of time, in nanoseconds, it took to do this initial query and send the results to the client.", meterUnit = "nanoseconds")
    private val cqInsertCountMeter = CounterStatisticMeter("cq.operation.count", "Total number of inserts done on this cq.", arrayOf("operation", "insert"))
    private val cqUpdateCountMeter = CounterStatisticMeter("cq.operation.count", "Total number of updates done on this cq.", arrayOf("operation", "update"))
    private val cqDeleteCountMeter = CounterStatisticMeter("cq.operation.count", "Total number of deletes done on this cq.", arrayOf("operation", "delete"))
    private val cqQueuedEventsMeter = GaugeStatisticMeter("cq.events.queued", "Number of events in this cq.")
    private val cqListenerInvocationsMeter = CounterStatisticMeter("cq.listener.invocations", "Total number of CqListener invocations.")
    private val cqEventsQueuedWhilstRegistrationMeter = GaugeStatisticMeter("cq.events.queued.registration", "Number of events queued while CQ registration is in progress. This is not the main cq queue but a temporary internal one used while the cq is starting up.")

    override fun initializeStaticMeters() {
        registerMeter(cqInitialResultsTimer)
        registerMeter(cqInsertCountMeter)
        registerMeter(cqUpdateCountMeter)
        registerMeter(cqDeleteCountMeter)
        registerMeter(cqQueuedEventsMeter)
        registerMeter(cqListenerInvocationsMeter)
        registerMeter(cqEventsQueuedWhilstRegistrationMeter)
    }

    override fun incNumInserts() {
        cqInsertCountMeter.increment()
    }

    override fun incNumUpdates() {
        cqUpdateCountMeter.increment()
    }

    override fun incNumDeletes() {
        cqDeleteCountMeter.increment()
    }

    override fun incNumHAQueuedEvents(incAmount: Long) {
        cqQueuedEventsMeter.increment(incAmount)
    }

    override fun incNumCqListenerInvocations() {
        cqListenerInvocationsMeter.increment()
    }

    override fun incQueuedCqListenerEvents() {
        cqEventsQueuedWhilstRegistrationMeter.increment()
    }

    override fun decQueuedCqListenerEvents() {
        cqEventsQueuedWhilstRegistrationMeter.decrement()
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCqInitialResultsTime(): Long = cqInitialResultsTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun setCqInitialResultsTime(time: Long) {
        cqInitialResultsTimer.setValue(time)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumInserts(): Long = cqInsertCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumUpdates(): Long = cqUpdateCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumDeletes(): Long = cqDeleteCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumEvents(): Long = -1

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incNumEvents() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumHAQueuedEvents(): Long = cqQueuedEventsMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumCqListenerInvocations(): Long = cqListenerInvocationsMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getQueuedCqListenerEvents(): Long = cqEventsQueuedWhilstRegistrationMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun updateStats(cqEvent: Int?) {
        //noop
    }
}
