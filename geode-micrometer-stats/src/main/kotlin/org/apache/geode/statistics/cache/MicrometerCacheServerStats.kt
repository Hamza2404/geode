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

import org.apache.geode.distributed.internal.PoolStatHelper
import org.apache.geode.internal.cache.tier.sockets.CacheServerStats
import org.apache.geode.internal.cache.tier.sockets.MessageStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

open class MicrometerCacheServerStats @JvmOverloads constructor(statisticsFactory: StatisticsFactory?,private val ownerName: String, typeName: String = "CacheServerStats-$ownerName") :
        MicrometerMeterGroup(statisticsFactory,"$typeName-$ownerName"), CacheServerStats {

    override fun getGroupTags(): Array<String> = arrayOf("owner", ownerName)

    private val cacheServerClientGetRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client get requests.", arrayOf("operation", "get"))
    private val cacheServerClientGetRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading get requests.", arrayOf("operation", "get"), meterUnit = "nanoseconds")
    private val cacheServerGetProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client get request, including the time to get an object from the cache.", arrayOf("operation", "get"), meterUnit = "nanoseconds")
    private val cacheServerGetResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of get responses written to the cache client.", arrayOf("operation", "get"))
    private val cacheServerGetResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing get responses.", arrayOf("operation", "get"), meterUnit = "nanoseconds")

    private val cacheServerClientPutRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client put requests.", arrayOf("operation", "put"))
    private val cacheServerClientPutRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading put requests.", arrayOf("operation", "put"), meterUnit = "nanoseconds")
    private val cacheServerPutProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client put request, including the time to put an object into the cache.", arrayOf("operation", "put"), meterUnit = "nanoseconds")
    private val cacheServerPutResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of put responses written to the cache client.", arrayOf("operation", "put"))
    private val cacheServerPutResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing put responses.", arrayOf("operation", "put"), meterUnit = "nanoseconds")

    private val cacheServerClientPutAllRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client putAll requests.", arrayOf("operation", "putAll"))
    private val cacheServerClientPutAllRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading putAll requests.", arrayOf("operation", "putAll"), meterUnit = "nanoseconds")
    private val cacheServerPutAllProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client putAll request, including the time to put all objects into the cache.", arrayOf("operation", "putAll"), meterUnit = "nanoseconds")
    private val cacheServerPutAllResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of putAll responses written to the cache client.", arrayOf("operation", "putAll"))
    private val cacheServerPutAllResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing putAll responses.", arrayOf("operation", "putAll"), meterUnit = "nanoseconds")

    private val cacheServerClientRemoveAllRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client removeAll requests.", arrayOf("operation", "removeAll"))
    private val cacheServerClientRemoveAllRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading removeAll requests.", arrayOf("operation", "removeAll"), meterUnit = "nanoseconds")
    private val cacheServerRemoveAllProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client removeAll request, including the time to remove all objects from the cache.", arrayOf("operation", "removeAll"), meterUnit = "nanoseconds")
    private val cacheServerRemoveAllResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of removeAll responses written to the cache client.", arrayOf("operation", "removeAll"))
    private val cacheServerRemoveAllResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing removeAll responses.", arrayOf("operation", "removeAll"), meterUnit = "nanoseconds")

    private val cacheServerClientGetAllRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client getAll requests.", arrayOf("operation", "getAll"))
    private val cacheServerClientGetAllRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading getAll requests.", arrayOf("operation", "getAll"), meterUnit = "nanoseconds")
    private val cacheServerGetAllProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client getAll request.", arrayOf("operation", "getAll"), meterUnit = "nanoseconds")
    private val cacheServerGetAllResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of getAll responses written to the cache client.", arrayOf("operation", "getAll"))
    private val cacheServerGetAllResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing getAll responses.", arrayOf("operation", "getAll"), meterUnit = "nanoseconds")

    private val cacheServerClientDestroyRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client destroy requests.", arrayOf("operation", "destroy"))
    private val cacheServerClientDestroyRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading destroy requests.", arrayOf("operation", "destroy"), meterUnit = "nanoseconds")
    private val cacheServerDestroyProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client destroy request, including the time to destroy an object from the cache.", arrayOf("operation", "destroy"), meterUnit = "nanoseconds")
    private val cacheServerDestroyResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of destroy responses written to the cache client.", arrayOf("operation", "destroy"))
    private val cacheServerDestroyResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing destroy responses.", arrayOf("operation", "destroy"), meterUnit = "nanoseconds")

    private val cacheServerClientInvaldiateRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client invalidate requests.", arrayOf("operation", "invalidate"))
    private val cacheServerClientInvaldiateRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading invalidate requests.", arrayOf("operation", "invalidate"), meterUnit = "nanoseconds")
    private val cacheServerInvaldiateProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client invalidate request, including the time to invalidate an object from the cache.", arrayOf("operation", "invalidate"), meterUnit = "nanoseconds")
    private val cacheServerInvaldiateResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of invalidate responses written to the cache client.", arrayOf("operation", "invalidate"))
    private val cacheServerInvaldiateResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing invalidate responses.", arrayOf("operation", "invalidate"), meterUnit = "nanoseconds")

    private val cacheServerClientSizeRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client size requests.", arrayOf("operation", "size"))
    private val cacheServerClientSizeRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading size requests.", arrayOf("operation", "size"), meterUnit = "nanoseconds")
    private val cacheServerSizeProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client size request, including the time to size an object from the cache.", arrayOf("operation", "size"), meterUnit = "nanoseconds")
    private val cacheServerSizeResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of size responses written to the cache client.", arrayOf("operation", "size"))
    private val cacheServerSizeResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing size responses.", arrayOf("operation", "size"), meterUnit = "nanoseconds")

    private val cacheServerClientQueryRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client query requests.", arrayOf("operation", "query"))
    private val cacheServerClientQueryRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading query requests.", arrayOf("operation", "query"), meterUnit = "nanoseconds")
    private val cacheServerQueryProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client query request, including the time to destroy an object from the cache.", arrayOf("operation", "query"), meterUnit = "nanoseconds")
    private val cacheServerQueryResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of query responses written to the cache client.", arrayOf("operation", "query"))
    private val cacheServerQueryResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing query responses.", arrayOf("operation", "query"), meterUnit = "nanoseconds")

    private val cacheServerClientDestroyRegionRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client destroyRegion requests.", arrayOf("operation", "destroyRegion"))
    private val cacheServerClientDestroyRegionRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading destroyRegion requests.", arrayOf("operation", "destroyRegion"), meterUnit = "nanoseconds")
    private val cacheServerClientDestroyRegionProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client destroyRegion request, including the time to destroy the region from the cache.", arrayOf("operation", "destroyRegion"), meterUnit = "nanoseconds")
    private val cacheServerClientDestroyRegionResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of destroyRegion responses written to the cache client.", arrayOf("operation", "destroyRegion"))
    private val cacheServerClientDestroyRegionResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing destroyRegion responses.", arrayOf("operation", "destroyRegion"), meterUnit = "nanoseconds")

    private val cacheServerClientContainsKeyRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client containsKey requests.", arrayOf("operation", "containsKey"))
    private val cacheServerClientContainsKeyRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent reading containsKey requests.", arrayOf("operation", "containsKey"), meterUnit = "nanoseconds")
    private val cacheServerClientContainsKeyProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent processing a containsKey request.", arrayOf("operation", "containsKey"), meterUnit = "nanoseconds")
    private val cacheServerClientContainsKeyResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of containsKey responses written to the cache client.", arrayOf("operation", "containsKey"))
    private val cacheServerClientContainsKeyResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent writing containsKey responses.", arrayOf("operation", "containsKey"), meterUnit = "nanoseconds")

    private val cacheServerProcessBatchRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client processBatch requests.", arrayOf("operation", "processBatch"))
    private val cacheServerProcessBatchRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading processBatch requests.", arrayOf("operation", "processBatch"), meterUnit = "nanoseconds")
    private val cacheServerProcessBatchProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client processBatch request.", arrayOf("operation", "processBatch"), meterUnit = "nanoseconds")
    private val cacheServerProcessBatchResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of processBatch responses written to the cache client.", arrayOf("operation", "processBatch"))
    private val cacheServerProcessBatchResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing processBatch responses.", arrayOf("operation", "processBatch"), meterUnit = "nanoseconds")
    private val cacheServerProcessBatchSizeMeter = CounterStatisticMeter("cacheserver.client.batch.size", "The size of the batches received.", arrayOf("operation", "processBatch"), meterUnit = "bytes")

    private val cacheServerClientClearRegionRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client clearRegion requests.", arrayOf("operation", "clearRegion"))
    private val cacheServerClientClearRegionRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading clearRegion requests.", arrayOf("operation", "clearRegion"), meterUnit = "nanoseconds")
    private val cacheServerClearRegionProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client clearRegion request, including the time to destroy the region from the cache.", arrayOf("operation", "clearRegion"), meterUnit = "nanoseconds")
    private val cacheServerClearRegionResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of clearRegion responses written to the cache client.", arrayOf("operation", "clearRegion"))
    private val cacheServerClearRegionResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing clearRegion responses.", arrayOf("operation", "clearRegion"), meterUnit = "nanoseconds")


    private val cacheServerClientNotificationRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client notification requests.", arrayOf("operation", "clientNotification"))
    private val cacheServerClientNotificationRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading client notification requests.", arrayOf("operation", "clientNotification"), meterUnit = "nanoseconds")
    private val cacheServerClientNotificationProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client notification request.", arrayOf("operation", "clientNotification"), meterUnit = "nanoseconds")

    private val cacheServerUpdateClientNotificationRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client notification update requests.", arrayOf("operation", "updateClientNotification"))
    private val cacheServerUpdateClientNotificationRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading client notification update requests.", arrayOf("operation", "updateClientNotification"), meterUnit = "nanoseconds")
    private val cacheServerUpdateClientNotificationProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a client notification update request.", arrayOf("operation", "updateClientNotification"), meterUnit = "nanoseconds")

    private val cacheServerClientReadyRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client ready requests.", arrayOf("operation", "clientReady"))
    private val cacheServerClientReadyRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading cache client ready requests.", arrayOf("operation", "clientReady"), meterUnit = "nanoseconds")
    private val cacheServerClientReadyProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client ready request, including the time to destroy an object from the cache.", arrayOf("operation", "clientReady"), meterUnit = "nanoseconds")
    private val cacheServerClientReadyResponseWrittenMeter = CounterStatisticMeter("cacheserver.client.responses.written.count", "Number of client ready responses written to the cache client.", arrayOf("operation", "clientReady"))
    private val cacheServerClientReadyResponseWrittenTimer = TimerStatisticMeter("cacheserver.client.responses.written.time", "Total time spent in writing client ready responses.", arrayOf("operation", "clientReady"), meterUnit = "nanoseconds")

    private val cacheServerClientCloseConnectionRequestMeter = CounterStatisticMeter("cacheserver.client.requests.count", "Number of cache client close connection requests.", arrayOf("operation", "closeConnection"))
    private val cacheServerClientCloseConnectionRequestTimer = TimerStatisticMeter("cacheserver.client.requests.time", "Total time spent in reading close connection requests.", arrayOf("operation", "closeConnection"), meterUnit = "nanoseconds")
    private val cacheServerClientCloseConnectionProcessTimer = TimerStatisticMeter("cacheserver.operation.process.time", "Total time spent in processing a cache client close connection request.", arrayOf("operation", "closeConnection"), meterUnit = "nanoseconds")


    private val cacheServerFailedConnectionAttemptsMeter = CounterStatisticMeter("cacheserver.client.connection.failed.count", "Number of failed connection attempts.")
    private val cacheServerCurrentConnectionMeter = GaugeStatisticMeter("cacheserver.client.connection.count", "Number of sockets accepted and used for client to server messaging.", arrayOf("connectionType", "messaging"))
    private val cacheServerCurrentQueueConnectionMeter = GaugeStatisticMeter("cacheserver.client.connection.count", "Number of sockets accepted and used for server to client queue messaging.", arrayOf("connectionType", "queue"))
    private val cacheServerClientCountMeter = GaugeStatisticMeter("cacheserver.client.count", "Number of client virtual machines connected.")
    private val cacheServerOutOfOrderGatewayBatchIdMeter = CounterStatisticMeter("cacheserver.gateway.batch.outoforder.count", "Number of Out of order batch IDs.")

    private val cacheServerClientWriteRequestAbandondMeter = CounterStatisticMeter("cacheserver.client.requests.abandond.count", "Number of write opertations abandond by clients", arrayOf("requestType", "write"))
    private val cacheServerClientReadRequestAbandondMeter = CounterStatisticMeter("cacheserver.client.requests.abandond.count", "Number of read opertations abandond by clients", arrayOf("requestType", "read"))
    private val cacheServerReceivedBytesMeter = CounterStatisticMeter("cacheserver.client.bytes", "Total number of bytes received from clients.", arrayOf("direction", "received"), meterUnit = "bytes")
    private val cacheServerSentBytesMeter = CounterStatisticMeter("cacheserver.client.bytes", "Total number of bytes sent to clients.", arrayOf("direction", "sent"), meterUnit = "bytes")
    private val cacheServerMessagesReceivedMeter = GaugeStatisticMeter("cacheserver.client.messages.received.count", "Current number of message being received off the network or being processed after reception.")
    private val cacheServerMessagesReceivedBytesMeter = GaugeStatisticMeter("cacheserver.client.messages.received.bytes", "Current number of bytes consumed by messages being received or processed.", meterUnit = "bytes")
    private val cacheServerConnectionTimeoutMeter = CounterStatisticMeter("cacheserver.client.connection.timeout.count", "Total number of connections that have been timed out by the server because of client inactivity")
    private val cacheServerThreadQueueSizeMeter = GaugeStatisticMeter("cacheserver.client.thread.queue.size", "Current number of connections waiting for a thread to start processing their message.")
    private val cacheServerConnectionAcceptInProgressMeter = GaugeStatisticMeter("cacheserver.client.connection.accept.inprogress.count", "Current number of server accepts that are attempting to do the initial handshake with the client.")
    private val cacheServerConnectionAcceptStartMeter = CounterStatisticMeter("cacheserver.client.connection.accept.start.count", "Total number of threads created to deal with an accepted socket. Note that this is not the current number of threads.")
    private val cacheServerConnectionStartMeter = CounterStatisticMeter("cacheserver.client.connection.start.count", "Total number of threads created to deal with a client connection. Note that this is not the current number of threads.")
    private val cacheServerConnectionThreadsMeter = GaugeStatisticMeter("cacheserver.client.connection.thread.count", "Current number of threads dealing with a client connection.")
    private val cacheServerConnectionLoadMeter = GaugeStatisticMeter("cacheserver.client.connection.load.count", "The load from client to server connections as reported by the load probe installed in this server")
    private val cacheServerLoadPerConnectionMeter = GaugeStatisticMeter("cacheserver.client.load.connection.count", "The estimate of how much load is added for each new connection as reported by the load probe installed in this server")
    private val cacheServerQueueLoadMeter = GaugeStatisticMeter("cacheserver.client.queue.load.count", "The load from queues as reported by the load probe installed in this server")
    private val cacheServerLoadPerQueueMeter = GaugeStatisticMeter("cacheserver.client.load.queue.count", "The estimate of how much load is added for each new connection as reported by the load probe installed in this server")

    override fun initializeStaticMeters() {
        registerMeter(cacheServerClientGetRequestMeter)
        registerMeter(cacheServerClientGetRequestTimer)
        registerMeter(cacheServerGetProcessTimer)
        registerMeter(cacheServerGetResponseWrittenMeter)
        registerMeter(cacheServerGetResponseWrittenTimer)

        registerMeter(cacheServerClientPutRequestMeter)
        registerMeter(cacheServerClientPutRequestTimer)
        registerMeter(cacheServerPutProcessTimer)
        registerMeter(cacheServerPutResponseWrittenMeter)
        registerMeter(cacheServerPutResponseWrittenTimer)

        registerMeter(cacheServerClientPutAllRequestMeter)
        registerMeter(cacheServerClientPutAllRequestTimer)
        registerMeter(cacheServerPutAllProcessTimer)
        registerMeter(cacheServerPutAllResponseWrittenMeter)
        registerMeter(cacheServerPutAllResponseWrittenTimer)

        registerMeter(cacheServerClientRemoveAllRequestMeter)
        registerMeter(cacheServerClientRemoveAllRequestTimer)
        registerMeter(cacheServerRemoveAllProcessTimer)
        registerMeter(cacheServerRemoveAllResponseWrittenMeter)
        registerMeter(cacheServerRemoveAllResponseWrittenTimer)

        registerMeter(cacheServerClientGetAllRequestMeter)
        registerMeter(cacheServerClientGetAllRequestTimer)
        registerMeter(cacheServerGetAllProcessTimer)
        registerMeter(cacheServerGetAllResponseWrittenMeter)
        registerMeter(cacheServerGetAllResponseWrittenTimer)

        registerMeter(cacheServerClientDestroyRequestMeter)
        registerMeter(cacheServerClientDestroyRequestTimer)
        registerMeter(cacheServerDestroyProcessTimer)
        registerMeter(cacheServerDestroyResponseWrittenMeter)
        registerMeter(cacheServerDestroyResponseWrittenTimer)

        registerMeter(cacheServerClientInvaldiateRequestMeter)
        registerMeter(cacheServerClientInvaldiateRequestTimer)
        registerMeter(cacheServerInvaldiateProcessTimer)
        registerMeter(cacheServerInvaldiateResponseWrittenMeter)
        registerMeter(cacheServerInvaldiateResponseWrittenTimer)

        registerMeter(cacheServerClientSizeRequestMeter)
        registerMeter(cacheServerClientSizeRequestTimer)
        registerMeter(cacheServerSizeProcessTimer)
        registerMeter(cacheServerSizeResponseWrittenMeter)
        registerMeter(cacheServerSizeResponseWrittenTimer)

        registerMeter(cacheServerClientQueryRequestMeter)
        registerMeter(cacheServerClientQueryRequestTimer)
        registerMeter(cacheServerQueryProcessTimer)
        registerMeter(cacheServerQueryResponseWrittenMeter)
        registerMeter(cacheServerQueryResponseWrittenTimer)

        registerMeter(cacheServerClientDestroyRegionRequestMeter)
        registerMeter(cacheServerClientDestroyRegionRequestTimer)
        registerMeter(cacheServerClientDestroyRegionProcessTimer)
        registerMeter(cacheServerClientDestroyRegionResponseWrittenMeter)
        registerMeter(cacheServerClientDestroyRegionResponseWrittenTimer)

        registerMeter(cacheServerClientContainsKeyRequestMeter)
        registerMeter(cacheServerClientContainsKeyRequestTimer)
        registerMeter(cacheServerClientContainsKeyProcessTimer)
        registerMeter(cacheServerClientContainsKeyResponseWrittenMeter)
        registerMeter(cacheServerClientContainsKeyResponseWrittenTimer)

        registerMeter(cacheServerProcessBatchRequestMeter)
        registerMeter(cacheServerProcessBatchRequestTimer)
        registerMeter(cacheServerProcessBatchProcessTimer)
        registerMeter(cacheServerProcessBatchResponseWrittenMeter)
        registerMeter(cacheServerProcessBatchResponseWrittenTimer)
        registerMeter(cacheServerProcessBatchSizeMeter)

        registerMeter(cacheServerClientClearRegionRequestMeter)
        registerMeter(cacheServerClientClearRegionRequestTimer)
        registerMeter(cacheServerClearRegionProcessTimer)
        registerMeter(cacheServerClearRegionResponseWrittenMeter)
        registerMeter(cacheServerClearRegionResponseWrittenTimer)


        registerMeter(cacheServerClientNotificationRequestMeter)
        registerMeter(cacheServerClientNotificationRequestTimer)
        registerMeter(cacheServerClientNotificationProcessTimer)

        registerMeter(cacheServerUpdateClientNotificationRequestMeter)
        registerMeter(cacheServerUpdateClientNotificationRequestTimer)
        registerMeter(cacheServerUpdateClientNotificationProcessTimer)

        registerMeter(cacheServerClientReadyRequestMeter)
        registerMeter(cacheServerClientReadyRequestTimer)
        registerMeter(cacheServerClientReadyProcessTimer)
        registerMeter(cacheServerClientReadyResponseWrittenMeter)
        registerMeter(cacheServerClientReadyResponseWrittenTimer)

        registerMeter(cacheServerClientCloseConnectionRequestMeter)
        registerMeter(cacheServerClientCloseConnectionRequestTimer)
        registerMeter(cacheServerClientCloseConnectionProcessTimer)


        registerMeter(cacheServerFailedConnectionAttemptsMeter)
        registerMeter(cacheServerCurrentConnectionMeter)
        registerMeter(cacheServerCurrentQueueConnectionMeter)
        registerMeter(cacheServerClientCountMeter)
        registerMeter(cacheServerOutOfOrderGatewayBatchIdMeter)

        registerMeter(cacheServerClientWriteRequestAbandondMeter)
        registerMeter(cacheServerClientReadRequestAbandondMeter)
        registerMeter(cacheServerReceivedBytesMeter)
        registerMeter(cacheServerSentBytesMeter)
        registerMeter(cacheServerMessagesReceivedMeter)
        registerMeter(cacheServerMessagesReceivedBytesMeter)
        registerMeter(cacheServerConnectionTimeoutMeter)
        registerMeter(cacheServerThreadQueueSizeMeter)
        registerMeter(cacheServerConnectionAcceptInProgressMeter)
        registerMeter(cacheServerConnectionAcceptStartMeter)
        registerMeter(cacheServerConnectionStartMeter)
        registerMeter(cacheServerConnectionThreadsMeter)
        registerMeter(cacheServerConnectionLoadMeter)
        registerMeter(cacheServerLoadPerConnectionMeter)
        registerMeter(cacheServerQueueLoadMeter)
        registerMeter(cacheServerLoadPerQueueMeter)
    }

    override fun incAcceptThreadsCreated() {
        cacheServerConnectionAcceptStartMeter.increment()
    }

    override fun incConnectionThreadsCreated() {
        cacheServerConnectionStartMeter.increment()
    }

    override fun incAcceptsInProgress() {
        cacheServerConnectionAcceptInProgressMeter.increment()
    }

    override fun decAcceptsInProgress() {
        cacheServerConnectionAcceptInProgressMeter.decrement()
    }

    override fun incConnectionThreads() {
        cacheServerConnectionThreadsMeter.increment()
    }

    override fun decConnectionThreads() {
        cacheServerConnectionThreadsMeter.decrement()
    }

    override fun incAbandonedWriteRequests() {
        cacheServerClientWriteRequestAbandondMeter.increment()
    }

    override fun incAbandonedReadRequests() {
        cacheServerClientReadRequestAbandondMeter.increment()
    }

    override fun incFailedConnectionAttempts() {
        cacheServerFailedConnectionAttemptsMeter.increment()
    }

    override fun incConnectionsTimedOut() {
        cacheServerConnectionTimeoutMeter.increment()
    }

    override fun incCurrentClientConnections() {
        cacheServerCurrentConnectionMeter.increment()
    }

    override fun decCurrentClientConnections() {
        cacheServerCurrentConnectionMeter.decrement()
    }

    override fun incCurrentQueueConnections() {
        cacheServerCurrentQueueConnectionMeter.increment()
    }

    override fun decCurrentQueueConnections() {
        cacheServerCurrentQueueConnectionMeter.decrement()
    }

    override fun incCurrentClients() {
        cacheServerClientCountMeter.increment()
    }

    override fun decCurrentClients() {
        cacheServerClientCountMeter.decrement()
    }

    override fun incThreadQueueSize() {
        cacheServerThreadQueueSizeMeter.increment()
    }

    override fun decThreadQueueSize() {
        cacheServerThreadQueueSizeMeter.decrement()
    }

    override fun incReadGetRequestTime(delta: Long) {
        cacheServerClientGetRequestTimer.recordValue(delta)
        cacheServerClientGetRequestMeter.increment()
    }

    override fun incProcessGetTime(delta: Long) {
        cacheServerGetProcessTimer.recordValue(delta)
    }

    override fun incWriteGetResponseTime(delta: Long) {
        cacheServerGetResponseWrittenTimer.recordValue(delta)
        cacheServerGetResponseWrittenMeter.increment()
    }

    override fun incReadPutAllRequestTime(delta: Long) {
        cacheServerClientPutAllRequestTimer.recordValue(delta)
        cacheServerClientPutAllRequestMeter.increment()
    }

    override fun incProcessPutAllTime(delta: Long) {
        cacheServerPutAllProcessTimer.recordValue(delta)
    }

    override fun incWritePutAllResponseTime(delta: Long) {
        cacheServerPutAllResponseWrittenTimer.recordValue(delta)
        cacheServerPutAllResponseWrittenMeter.increment()
    }

    override fun incReadRemoveAllRequestTime(delta: Long) {
        cacheServerClientRemoveAllRequestTimer.recordValue(delta)
        cacheServerClientRemoveAllRequestMeter.increment()
    }

    override fun incProcessRemoveAllTime(delta: Long) {
        cacheServerRemoveAllProcessTimer.recordValue(delta)
    }

    override fun incWriteRemoveAllResponseTime(delta: Long) {
        cacheServerRemoveAllResponseWrittenTimer.recordValue(delta)
        cacheServerRemoveAllResponseWrittenMeter.increment()
    }

    override fun incReadGetAllRequestTime(delta: Long) {
        cacheServerClientGetAllRequestTimer.recordValue(delta)
        cacheServerClientGetAllRequestMeter.increment()
    }

    override fun incProcessGetAllTime(delta: Long) {
        cacheServerGetAllProcessTimer.recordValue(delta)
    }

    override fun incWriteGetAllResponseTime(delta: Long) {
        cacheServerGetAllResponseWrittenTimer.recordValue(delta)
        cacheServerGetAllResponseWrittenMeter.increment()
    }

    override fun incReadPutRequestTime(delta: Long) {
        cacheServerClientPutRequestTimer.recordValue(delta)
        cacheServerClientPutRequestMeter.increment()
    }

    override fun incProcessPutTime(delta: Long) {
        cacheServerPutProcessTimer.recordValue(delta)
    }

    override fun incWritePutResponseTime(delta: Long) {
        cacheServerPutResponseWrittenTimer.recordValue(delta)
        cacheServerPutResponseWrittenMeter.increment()
    }

    override fun incReadDestroyRequestTime(delta: Long) {
        cacheServerClientDestroyRequestTimer.recordValue(delta)
        cacheServerClientDestroyRequestMeter.increment()
    }

    override fun incProcessDestroyTime(delta: Long) {
        cacheServerDestroyProcessTimer.recordValue(delta)
    }

    override fun incWriteDestroyResponseTime(delta: Long) {
        cacheServerDestroyResponseWrittenTimer.recordValue(delta)
        cacheServerDestroyResponseWrittenMeter.increment()
    }


    override fun incReadInvalidateRequestTime(delta: Long) {
        cacheServerClientInvaldiateRequestTimer.recordValue(delta)
        cacheServerClientInvaldiateRequestMeter.increment()
    }

    override fun incProcessInvalidateTime(delta: Long) {
        cacheServerInvaldiateProcessTimer.recordValue(delta)
    }

    override fun incWriteInvalidateResponseTime(delta: Long) {
        cacheServerInvaldiateResponseWrittenTimer.recordValue(delta)
        cacheServerInvaldiateResponseWrittenMeter.increment()
    }

    override fun incReadSizeRequestTime(delta: Long) {
        cacheServerClientSizeRequestTimer.recordValue(delta)
        cacheServerClientSizeRequestMeter.increment()
    }

    override fun incProcessSizeTime(delta: Long) {
        cacheServerSizeProcessTimer.recordValue(delta)
    }

    override fun incWriteSizeResponseTime(delta: Long) {
        cacheServerSizeResponseWrittenTimer.recordValue(delta)
        cacheServerSizeResponseWrittenMeter.increment()
    }

    override fun incReadQueryRequestTime(delta: Long) {
        cacheServerClientQueryRequestTimer.recordValue(delta)
        cacheServerClientQueryRequestMeter.increment()
    }

    override fun incProcessQueryTime(delta: Long) {
        cacheServerQueryProcessTimer.recordValue(delta)
    }

    override fun incWriteQueryResponseTime(delta: Long) {
        cacheServerQueryResponseWrittenTimer.recordValue(delta)
        cacheServerQueryResponseWrittenMeter.increment()
    }

    override fun incReadDestroyRegionRequestTime(delta: Long) {
        cacheServerClientDestroyRegionRequestTimer.recordValue(delta)
        cacheServerClientDestroyRegionRequestMeter.increment()
    }

    override fun incProcessDestroyRegionTime(delta: Long) {
        cacheServerClientDestroyRegionProcessTimer.recordValue(delta)
    }

    override fun incWriteDestroyRegionResponseTime(delta: Long) {
        cacheServerClientDestroyRegionResponseWrittenTimer.recordValue(delta)
        cacheServerClientDestroyRegionResponseWrittenMeter.increment()
    }

    override fun incReadContainsKeyRequestTime(delta: Long) {
        cacheServerClientContainsKeyRequestTimer.recordValue(delta)
        cacheServerClientContainsKeyRequestMeter.increment()
    }

    override fun incProcessContainsKeyTime(delta: Long) {
        cacheServerClientContainsKeyProcessTimer.recordValue(delta)
    }

    override fun incWriteContainsKeyResponseTime(delta: Long) {
        cacheServerClientContainsKeyResponseWrittenTimer.recordValue(delta)
        cacheServerClientContainsKeyResponseWrittenMeter.increment()
    }

    override fun incReadClearRegionRequestTime(delta: Long) {
        cacheServerClientClearRegionRequestTimer.recordValue(delta)
        cacheServerClientClearRegionRequestMeter.increment()
    }

    override fun incProcessClearRegionTime(delta: Long) {
        cacheServerClearRegionProcessTimer.recordValue(delta)
    }

    override fun incWriteClearRegionResponseTime(delta: Long) {
        cacheServerClearRegionResponseWrittenTimer.recordValue(delta)
        cacheServerClearRegionResponseWrittenMeter.increment()
    }

    override fun incReadProcessBatchRequestTime(delta: Long) {
        cacheServerProcessBatchRequestTimer.recordValue(delta)
        cacheServerProcessBatchRequestMeter.increment()
    }

    override fun incWriteProcessBatchResponseTime(delta: Long) {
        cacheServerProcessBatchResponseWrittenTimer.recordValue(delta)
        cacheServerProcessBatchResponseWrittenMeter.increment()
    }

    override fun incProcessBatchTime(delta: Long) {
        cacheServerProcessBatchProcessTimer.recordValue(delta)
    }

    override fun incBatchSize(size: Long) {
        cacheServerProcessBatchSizeMeter.increment(size)
    }

    override fun incReadClientNotificationRequestTime(delta: Long) {
        cacheServerClientNotificationRequestTimer.recordValue(delta)
        cacheServerClientNotificationRequestMeter.increment()
    }

    override fun incProcessClientNotificationTime(delta: Long) {
        cacheServerClientNotificationProcessTimer.recordValue(delta)
    }

    override fun incReadUpdateClientNotificationRequestTime(delta: Long) {
        cacheServerUpdateClientNotificationRequestTimer.recordValue(delta)
        cacheServerUpdateClientNotificationRequestMeter.increment()
    }

    override fun incProcessUpdateClientNotificationTime(delta: Long) {
        cacheServerUpdateClientNotificationProcessTimer.recordValue(delta)
    }

    override fun incReadCloseConnectionRequestTime(delta: Long) {
        cacheServerClientCloseConnectionRequestTimer.recordValue(delta)
        cacheServerClientCloseConnectionRequestMeter.increment()
    }

    override fun incProcessCloseConnectionTime(delta: Long) {
        cacheServerClientCloseConnectionProcessTimer.recordValue(delta)
    }

    override fun incOutOfOrderBatchIds() {
        cacheServerOutOfOrderGatewayBatchIdMeter.increment()
    }

    override fun incReceivedBytes(bytes: Long) {
        cacheServerReceivedBytesMeter.increment(bytes)
    }

    override fun incSentBytes(bytes: Long) {
        cacheServerSentBytesMeter.increment(bytes)
    }

    override fun incMessagesBeingReceived(bytes: Int) {
        cacheServerMessagesReceivedMeter.increment()
        cacheServerMessagesReceivedBytesMeter.increment(bytes)
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        cacheServerMessagesReceivedMeter.decrement()
        cacheServerMessagesReceivedBytesMeter.decrement(bytes)
    }

    override fun incReadClientReadyRequestTime(delta: Long) {
        cacheServerClientReadyRequestTimer.recordValue(delta)
        cacheServerClientReadyRequestMeter.increment()
    }

    override fun incProcessClientReadyTime(delta: Long) {
        cacheServerClientReadyProcessTimer.recordValue(delta)
    }

    override fun incWriteClientReadyResponseTime(delta: Long) {
        cacheServerClientReadyResponseWrittenTimer.recordValue(delta)
        cacheServerClientReadyResponseWrittenMeter.increment()
    }

    override fun setLoad(connectionLoad: Float, loadPerConnection: Float, queueLoad: Float, loadPerQueue: Float) {
        cacheServerConnectionLoadMeter.increment(connectionLoad.toDouble())
        cacheServerLoadPerConnectionMeter.increment(loadPerConnection.toDouble())
        cacheServerQueueLoadMeter.increment(queueLoad.toDouble())
        cacheServerLoadPerQueueMeter.increment(loadPerQueue.toDouble())
    }

    override fun getCnxPoolHelper(): PoolStatHelper = object : PoolStatHelper {
        override fun startJob() {
            incConnectionThreads()
        }

        override fun endJob() {
            decConnectionThreads()
        }
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCurrentClientConnections(): Int = cacheServerClientCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCurrentQueueConnections(): Int = cacheServerCurrentQueueConnectionMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessCreateCqTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessCloseCqTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessExecuteCqWithIRTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessStopCqTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessCloseClientCqsTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incProcessGetCqStatsTime(delta: Long) {
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getQueueLoad(): Double = cacheServerQueueLoadMeter.getValue().toDouble()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadPerQueue(): Double = cacheServerLoadPerQueueMeter.getValue().toDouble()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getConnectionLoad(): Double = cacheServerConnectionLoadMeter.getValue().toDouble()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadPerConnection(): Double = cacheServerLoadPerConnectionMeter.getValue().toDouble()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getProcessBatchRequests(): Int = cacheServerProcessBatchRequestMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
