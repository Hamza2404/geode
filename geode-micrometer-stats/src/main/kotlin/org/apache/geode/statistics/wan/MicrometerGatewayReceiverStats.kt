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

import org.apache.geode.internal.cache.wan.GatewayReceiverStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.cache.MicrometerCacheServerStats
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

//By inheritence this is a MicrometerMeterGroup
class MicrometerGatewayReceiverStats(statisticsFactory: StatisticsFactory?, private val owner: String) :
        MicrometerCacheServerStats(statisticsFactory, owner, "GatewayReceiverStats-$owner"), GatewayReceiverStats {

    override fun getGroupTags(): Array<String> = arrayOf("owner", owner)

    override fun initializeStaticMeters() {
        registerMeter(gatewayReceiverBatchesDuplicateMeter)
        registerMeter(gatewayReceiverBatchesOutOfOrderMeter)
        registerMeter(gatewayReceiverEarlyAcksMeter)
        registerMeter(gatewayReceiverTotalEventsReceivedMeter)
        registerMeter(gatewayReceiverCreateEventsReceivedMeter)
        registerMeter(gatewayReceiverUpdateEventsReceivedMeter)
        registerMeter(gatewayReceiverDestroyEventsReceivedMeter)
        registerMeter(gatewayReceiverUnknownEventsReceivedMeter)
        registerMeter(gatewayReceiverExceptionsMeter)
        registerMeter(gatewayReceiverEventsRetriedMeter)
    }

    private val gatewayReceiverBatchesDuplicateMeter = CounterStatisticMeter("gateway.receiver.batches.duplicate.count", "number of batches which have already been seen by this GatewayReceiver")
    private val gatewayReceiverBatchesOutOfOrderMeter = CounterStatisticMeter("gateway.receiver.batches.outoforder.count", "number of batches which are out of order on this GatewayReceiver")
    private val gatewayReceiverEarlyAcksMeter = CounterStatisticMeter("gateway.receiver.earlyAcks.count", "number of early acknowledgements sent to gatewaySenders")
    private val gatewayReceiverTotalEventsReceivedMeter = CounterStatisticMeter("gateway.receiver.events.received.count", "total number events across the batched received by this GatewayReceiver")
    private val gatewayReceiverCreateEventsReceivedMeter = CounterStatisticMeter("gateway.receiver.events.received.count", "total number of create operations received by this GatewayReceiver", arrayOf("operationType", "create"))
    private val gatewayReceiverUpdateEventsReceivedMeter = CounterStatisticMeter("gateway.receiver.events.received.count", "total number of update operations received by this GatewayReceiver", arrayOf("operationType", "update"))
    private val gatewayReceiverDestroyEventsReceivedMeter = CounterStatisticMeter("gateway.receiver.events.received.count", "total number of destroy operations received by this GatewayReceiver", arrayOf("operationType", "destroy"))
    private val gatewayReceiverUnknownEventsReceivedMeter = CounterStatisticMeter("gateway.receiver.events.received.count", "total number of unknown operations received by this GatewayReceiver", arrayOf("operationType", "unknown"))
    private val gatewayReceiverExceptionsMeter = CounterStatisticMeter("gateway.receiver.exceptions.count", "number of exceptions occurred while porcessing the batches")
    private val gatewayReceiverEventsRetriedMeter = CounterStatisticMeter("gateway.receiver.events.retried", "total number events retried by this GatewayReceiver due to exceptions")

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDuplicateBatchesReceived(): Int = gatewayReceiverBatchesDuplicateMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getOutoforderBatchesReceived(): Int = gatewayReceiverBatchesOutOfOrderMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEarlyAcks(): Int = gatewayReceiverEarlyAcksMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsReceived(): Int = gatewayReceiverTotalEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getCreateRequest(): Int = gatewayReceiverCreateEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUpdateRequest(): Int = gatewayReceiverUpdateEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyRequest(): Int = gatewayReceiverDestroyEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getUnknowsOperationsReceived(): Int = gatewayReceiverUnknownEventsReceivedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getExceptionsOccurred(): Int = gatewayReceiverExceptionsMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsRetried(): Int = gatewayReceiverEventsRetriedMeter.getValue().toInt()

    override fun incDuplicateBatchesReceived() {
        gatewayReceiverBatchesDuplicateMeter.increment()
    }

    override fun incOutoforderBatchesReceived() {
        gatewayReceiverBatchesOutOfOrderMeter.increment()
    }

    override fun incEarlyAcks() {
        gatewayReceiverEarlyAcksMeter.increment()
    }

    override fun incEventsReceived(delta: Int) {
        gatewayReceiverTotalEventsReceivedMeter.increment()
    }

    override fun incCreateRequest() {
        gatewayReceiverCreateEventsReceivedMeter.increment()
    }

    override fun incUpdateRequest() {
        gatewayReceiverUpdateEventsReceivedMeter.increment()
    }

    override fun incDestroyRequest() {
        gatewayReceiverDestroyEventsReceivedMeter.increment()
    }

    override fun incUnknowsOperationsReceived() {
        gatewayReceiverUnknownEventsReceivedMeter.increment()
    }

    override fun incExceptionsOccurred() {
        gatewayReceiverExceptionsMeter.increment()
    }

    override fun incEventsRetried() {
        gatewayReceiverEventsRetriedMeter.increment()
    }
}
