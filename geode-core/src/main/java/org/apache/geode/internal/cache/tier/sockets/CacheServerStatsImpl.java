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
package org.apache.geode.internal.cache.tier.sockets;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.cache.server.ServerLoad;
import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.distributed.internal.PoolStatHelper;

/**
 * Cache Server statistic definitions
 */
public class CacheServerStatsImpl implements CacheServerStats, GFSStatsImplementor {

  private static final String typeName = "CacheServerStats";

  protected Statistics stats;

  // Get request / response statistics
  int getRequestsId;

  int readGetRequestTimeId;

  int processGetTimeId;

  int getResponsesId;

  int writeGetResponseTimeId;

  // PutAll request / response statistics
  int putAllRequestsId;
  int readPutAllRequestTimeId;
  int processPutAllTimeId;
  int putAllResponsesId;
  int writePutAllResponseTimeId;

  // RemoveAll request / response statistics
  int removeAllRequestsId;
  int readRemoveAllRequestTimeId;
  int processRemoveAllTimeId;
  int removeAllResponsesId;
  int writeRemoveAllResponseTimeId;

  // GetAll request / response statistics
  int getAllRequestsId;
  int readGetAllRequestTimeId;
  int processGetAllTimeId;
  int getAllResponsesId;
  int writeGetAllResponseTimeId;

  // Put request / response statistics
  int putRequestsId;

  int readPutRequestTimeId;

  int processPutTimeId;

  int putResponsesId;

  int writePutResponseTimeId;

  // Destroy request / response statistics
  int destroyRequestsId;
  int readDestroyRequestTimeId;
  int processDestroyTimeId;
  int destroyResponsesId;
  int writeDestroyResponseTimeId;

  // Invalidate request / response statistics
  // int invalidateRequestsId;
  // int readInvalidateRequestTimeId;
  // int processInvalidateTimeId;
  // int invalidateResponsesId;
  // int writeInvalidateResponseTimeId;

  // size request / response statistics
  // int sizeRequestsId;
  // int readSizeRequestTimeId;
  // int processSizeTimeId;
  // int sizeResponsesId;
  // int writeSizeResponseTimeId;


  // Query request / response statistics
  int queryRequestsId;

  int readQueryRequestTimeId;

  int processQueryTimeId;

  int queryResponsesId;

  int writeQueryResponseTimeId;

  // CQ commands request / response statistics
  // int processCreateCqTimeId;
  // int processExecuteCqWithIRCqTimeId;
  // int processStopCqTimeId;
  // int processCloseCqTimeId;
  // int processCloseClientCqsTimeId;
  // int processGetCqStatsTimeId;


  // Destroy region request / response statistics
  int destroyRegionRequestsId;

  int readDestroyRegionRequestTimeId;

  int processDestroyRegionTimeId;

  int destroyRegionResponsesId;

  int writeDestroyRegionResponseTimeId;

  // ContainsKey request / response statistics
  int containsKeyRequestsId;
  int readContainsKeyRequestTimeId;
  int processContainsKeyTimeId;
  int containsKeyResponsesId;
  int writeContainsKeyResponseTimeId;

  // Clear region request / response statistics
  int clearRegionRequestsId;

  int readClearRegionRequestTimeId;

  int processClearRegionTimeId;

  int clearRegionResponsesId;

  int writeClearRegionResponseTimeId;


  // Batch processing statistics
  int processBatchRequestsId;

  int readProcessBatchRequestTimeId;

  int processBatchTimeId;

  int processBatchResponsesId;

  int writeProcessBatchResponseTimeId;

  int batchSizeId;

  // Client notification request statistics
  int clientNotificationRequestsId;

  int readClientNotificationRequestTimeId;

  int processClientNotificationTimeId;

  // Update client notification request statistics
  int updateClientNotificationRequestsId;

  int readUpdateClientNotificationRequestTimeId;

  int processUpdateClientNotificationTimeId;

  // Close connection request statistics
  int closeConnectionRequestsId;

  int readCloseConnectionRequestTimeId;

  int processCloseConnectionTimeId;

  // Client ready request / response statistics
  int clientReadyRequestsId;

  int readClientReadyRequestTimeId;

  int processClientReadyTimeId;

  int clientReadyResponsesId;

  int writeClientReadyResponseTimeId;

  // Connection statistics
  int currentClientConnectionsId;
  int currentQueueConnectionsId;

  int currentClientsId;

  int failedConnectionAttemptsId;

  int receivedBytesId;
  int sentBytesId;

  int outOfOrderBatchIdsId;
  int abandonedWriteRequestsId;
  int abandonedReadRequestsId;

  int messagesBeingReceivedId;
  int messageBytesBeingReceivedId;

  int connectionsTimedOutId;
  int threadQueueSizeId;
  int acceptsInProgressId;
  int acceptThreadStartsId;
  int connectionThreadStartsId;
  int connectionThreadsId;

  // Load callback stats
  int connectionLoadId;
  int queueLoadId;
  int loadPerConnectionId;
  int loadPerQueueId;

  protected StatisticsType statType;

  public CacheServerStatsImpl(String ownerName) {
    this(InternalDistributedSystem.getAnyInstance().getStatisticsFactory(), ownerName, typeName, null);
  }

  /**
   * Add a convinience method to pass in a StatisticsFactory for Statistics construction. Helpful
   * for local Statistics operations
   *
   */
  public CacheServerStatsImpl(StatisticsFactory statisticsFactory, String ownerName, String typeName,
                              StatisticDescriptor[] descriptors) {
    if (statisticsFactory == null) {
      // Create statistics later when needed
      return;
    }
    StatisticDescriptor[] serverStatDescriptors = new StatisticDescriptor[] {
        statisticsFactory.createIntCounter("getRequests", "Number of cache client get requests.",
            "operations"),
        statisticsFactory.createLongCounter("readGetRequestTime",
            "Total time spent in reading get requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processGetTime",
            "Total time spent in processing a cache client get request, including the time to get an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("getResponses",
            "Number of get responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeGetResponseTime",
            "Total time spent in writing get responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("putRequests", "Number of cache client put requests.",
            "operations"),
        statisticsFactory.createLongCounter("readPutRequestTime",
            "Total time spent in reading put requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processPutTime",
            "Total time spent in processing a cache client put request, including the time to put an object into the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("putResponses",
            "Number of put responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writePutResponseTime",
            "Total time spent in writing put responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("putAllRequests",
            "Number of cache client putAll requests.", "operations"),
        statisticsFactory.createLongCounter("readPutAllRequestTime",
            "Total time spent in reading putAll requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processPutAllTime",
            "Total time spent in processing a cache client putAll request, including the time to put all objects into the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("putAllResponses",
            "Number of putAll responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writePutAllResponseTime",
            "Total time spent in writing putAll responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("removeAllRequests",
            "Number of cache client removeAll requests.", "operations"),
        statisticsFactory.createLongCounter("readRemoveAllRequestTime",
            "Total time spent in reading removeAll requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processRemoveAllTime",
            "Total time spent in processing a cache client removeAll request, including the time to remove all objects from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("removeAllResponses",
            "Number of removeAll responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeRemoveAllResponseTime",
            "Total time spent in writing removeAll responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("getAllRequests",
            "Number of cache client getAll requests.", "operations"),
        statisticsFactory.createLongCounter("readGetAllRequestTime",
            "Total time spent in reading getAll requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processGetAllTime",
            "Total time spent in processing a cache client getAll request.", "nanoseconds"),
        statisticsFactory.createIntCounter("getAllResponses",
            "Number of getAll responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeGetAllResponseTime",
            "Total time spent in writing getAll responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("destroyRequests",
            "Number of cache client destroy requests.", "operations"),
        statisticsFactory.createLongCounter("readDestroyRequestTime",
            "Total time spent in reading destroy requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processDestroyTime",
            "Total time spent in processing a cache client destroy request, including the time to destroy an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("destroyResponses",
            "Number of destroy responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeDestroyResponseTime",
            "Total time spent in writing destroy responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("invalidateRequests",
            "Number of cache client invalidate requests.", "operations"),
        statisticsFactory.createLongCounter("readInvalidateRequestTime",
            "Total time spent in reading invalidate requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processInvalidateTime",
            "Total time spent in processing a cache client invalidate request, including the time to invalidate an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("invalidateResponses",
            "Number of invalidate responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeInvalidateResponseTime",
            "Total time spent in writing invalidate responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("sizeRequests", "Number of cache client size requests.",
            "operations"),
        statisticsFactory.createLongCounter("readSizeRequestTime",
            "Total time spent in reading size requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processSizeTime",
            "Total time spent in processing a cache client size request, including the time to size an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("sizeResponses",
            "Number of size responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeSizeResponseTime",
            "Total time spent in writing size responses.", "nanoseconds"),


        statisticsFactory.createIntCounter("queryRequests",
            "Number of cache client query requests.", "operations"),
        statisticsFactory.createLongCounter("readQueryRequestTime",
            "Total time spent in reading query requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processQueryTime",
            "Total time spent in processing a cache client query request, including the time to destroy an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("queryResponses",
            "Number of query responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeQueryResponseTime",
            "Total time spent in writing query responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("destroyRegionRequests",
            "Number of cache client destroyRegion requests.", "operations"),
        statisticsFactory.createLongCounter("readDestroyRegionRequestTime",
            "Total time spent in reading destroyRegion requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processDestroyRegionTime",
            "Total time spent in processing a cache client destroyRegion request, including the time to destroy the region from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("destroyRegionResponses",
            "Number of destroyRegion responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeDestroyRegionResponseTime",
            "Total time spent in writing destroyRegion responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("containsKeyRequests",
            "Number of cache client containsKey requests.", "operations"),
        statisticsFactory.createLongCounter("readContainsKeyRequestTime",
            "Total time spent reading containsKey requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processContainsKeyTime",
            "Total time spent processing a containsKey request.", "nanoseconds"),
        statisticsFactory.createIntCounter("containsKeyResponses",
            "Number of containsKey responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeContainsKeyResponseTime",
            "Total time spent writing containsKey responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("processBatchRequests",
            "Number of cache client processBatch requests.", "operations"),
        statisticsFactory.createLongCounter("readProcessBatchRequestTime",
            "Total time spent in reading processBatch requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processBatchTime",
            "Total time spent in processing a cache client processBatch request.", "nanoseconds"),
        statisticsFactory.createIntCounter("processBatchResponses",
            "Number of processBatch responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeProcessBatchResponseTime",
            "Total time spent in writing processBatch responses.", "nanoseconds"),
        statisticsFactory.createLongCounter("batchSize", "The size of the batches received.",
            "bytes"),
        statisticsFactory.createIntCounter("clearRegionRequests",
            "Number of cache client clearRegion requests.", "operations"),
        statisticsFactory.createLongCounter("readClearRegionRequestTime",
            "Total time spent in reading clearRegion requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processClearRegionTime",
            "Total time spent in processing a cache client clearRegion request, including the time to clear the region from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("clearRegionResponses",
            "Number of clearRegion responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeClearRegionResponseTime",
            "Total time spent in writing clearRegion responses.", "nanoseconds"),
        statisticsFactory.createIntCounter("clientNotificationRequests",
            "Number of cache client notification requests.", "operations"),
        statisticsFactory.createLongCounter("readClientNotificationRequestTime",
            "Total time spent in reading client notification requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processClientNotificationTime",
            "Total time spent in processing a cache client notification request.", "nanoseconds"),

        statisticsFactory.createIntCounter("updateClientNotificationRequests",
            "Number of cache client notification update requests.", "operations"),
        statisticsFactory.createLongCounter("readUpdateClientNotificationRequestTime",
            "Total time spent in reading client notification update requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processUpdateClientNotificationTime",
            "Total time spent in processing a client notification update request.", "nanoseconds"),

        statisticsFactory.createIntCounter("clientReadyRequests",
            "Number of cache client ready requests.", "operations"),
        statisticsFactory.createLongCounter("readClientReadyRequestTime",
            "Total time spent in reading cache client ready requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processClientReadyTime",
            "Total time spent in processing a cache client ready request, including the time to destroy an object from the cache.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("clientReadyResponses",
            "Number of client ready responses written to the cache client.", "operations"),
        statisticsFactory.createLongCounter("writeClientReadyResponseTime",
            "Total time spent in writing client ready responses.", "nanoseconds"),

        statisticsFactory.createIntCounter("closeConnectionRequests",
            "Number of cache client close connection requests.", "operations"),
        statisticsFactory.createLongCounter("readCloseConnectionRequestTime",
            "Total time spent in reading close connection requests.", "nanoseconds"),
        statisticsFactory.createLongCounter("processCloseConnectionTime",
            "Total time spent in processing a cache client close connection request.",
            "nanoseconds"),
        statisticsFactory.createIntCounter("failedConnectionAttempts",
            "Number of failed connection attempts.", "attempts"),
        statisticsFactory.createIntGauge("currentClientConnections",
            "Number of sockets accepted and used for client to server messaging.", "sockets"),
        statisticsFactory.createIntGauge("currentQueueConnections",
            "Number of sockets accepted and used for server to client messaging.", "sockets"),
        statisticsFactory.createIntGauge("currentClients",
            "Number of client virtual machines connected.", "clients"),
        statisticsFactory.createIntCounter("outOfOrderGatewayBatchIds",
            "Number of Out of order batch IDs.", "batches"),
        statisticsFactory.createIntCounter("abandonedWriteRequests",
            "Number of write opertations abandond by clients", "requests"),
        statisticsFactory.createIntCounter("abandonedReadRequests",
            "Number of read opertations abandond by clients", "requests"),
        statisticsFactory.createLongCounter("receivedBytes",
            "Total number of bytes received from clients.", "bytes"),
        statisticsFactory.createLongCounter("sentBytes", "Total number of bytes sent to clients.",
            "bytes"),
        statisticsFactory.createIntGauge("messagesBeingReceived",
            "Current number of message being received off the network or being processed after reception.",
            "messages"),
        statisticsFactory.createLongGauge("messageBytesBeingReceived",
            "Current number of bytes consumed by messages being received or processed.", "bytes"),
        statisticsFactory.createIntCounter("connectionsTimedOut",
            "Total number of connections that have been timed out by the server because of client inactivity",
            "connections"),
        statisticsFactory.createIntGauge("threadQueueSize",
            "Current number of connections waiting for a thread to start processing their message.",
            "connections"),
        statisticsFactory.createIntGauge("acceptsInProgress",
            "Current number of server accepts that are attempting to do the initial handshake with the client.",
            "accepts"),
        statisticsFactory.createIntCounter("acceptThreadStarts",
            "Total number of threads created to deal with an accepted socket. Note that this is not the current number of threads.",
            "starts"),
        statisticsFactory.createIntCounter("connectionThreadStarts",
            "Total number of threads created to deal with a client connection. Note that this is not the current number of threads.",
            "starts"),
        statisticsFactory.createIntGauge("connectionThreads",
            "Current number of threads dealing with a client connection.", "threads"),
        statisticsFactory.createDoubleGauge("connectionLoad",
            "The load from client to server connections as reported by the load probe installed in this server",
            "load"),
        statisticsFactory.createDoubleGauge("loadPerConnection",
            "The estimate of how much load is added for each new connection as reported by the load probe installed in this server",
            "load"),
        statisticsFactory.createDoubleGauge("queueLoad",
            "The load from queues as reported by the load probe installed in this server", "load"),
        statisticsFactory.createDoubleGauge("loadPerQueue",
            "The estimate of how much load is added for each new connection as reported by the load probe installed in this server",
            "load")};
    StatisticDescriptor[] alldescriptors = serverStatDescriptors;
    if (descriptors != null) {
      alldescriptors = new StatisticDescriptor[descriptors.length + serverStatDescriptors.length];
      System.arraycopy(descriptors, 0, alldescriptors, 0, descriptors.length);
      System.arraycopy(serverStatDescriptors, 0, alldescriptors, descriptors.length,
          serverStatDescriptors.length);
    }
    statType = statisticsFactory.createType(typeName, typeName, alldescriptors);
    this.stats = statisticsFactory.createAtomicStatistics(statType, ownerName);

    getRequestsId = this.stats.nameToId("getRequests");
    readGetRequestTimeId = this.stats.nameToId("readGetRequestTime");
    processGetTimeId = this.stats.nameToId("processGetTime");
    getResponsesId = this.stats.nameToId("getResponses");
    writeGetResponseTimeId = this.stats.nameToId("writeGetResponseTime");

    putRequestsId = this.stats.nameToId("putRequests");
    readPutRequestTimeId = this.stats.nameToId("readPutRequestTime");
    processPutTimeId = this.stats.nameToId("processPutTime");
    putResponsesId = this.stats.nameToId("putResponses");
    writePutResponseTimeId = this.stats.nameToId("writePutResponseTime");

    putAllRequestsId = this.stats.nameToId("putAllRequests");
    readPutAllRequestTimeId = this.stats.nameToId("readPutAllRequestTime");
    processPutAllTimeId = this.stats.nameToId("processPutAllTime");
    putAllResponsesId = this.stats.nameToId("putAllResponses");
    writePutAllResponseTimeId = this.stats.nameToId("writePutAllResponseTime");

    removeAllRequestsId = this.stats.nameToId("removeAllRequests");
    readRemoveAllRequestTimeId = this.stats.nameToId("readRemoveAllRequestTime");
    processRemoveAllTimeId = this.stats.nameToId("processRemoveAllTime");
    removeAllResponsesId = this.stats.nameToId("removeAllResponses");
    writeRemoveAllResponseTimeId = this.stats.nameToId("writeRemoveAllResponseTime");

    getAllRequestsId = this.stats.nameToId("getAllRequests");
    readGetAllRequestTimeId = this.stats.nameToId("readGetAllRequestTime");
    processGetAllTimeId = this.stats.nameToId("processGetAllTime");
    getAllResponsesId = this.stats.nameToId("getAllResponses");
    writeGetAllResponseTimeId = this.stats.nameToId("writeGetAllResponseTime");

    destroyRequestsId = this.stats.nameToId("destroyRequests");
    readDestroyRequestTimeId = this.stats.nameToId("readDestroyRequestTime");
    processDestroyTimeId = this.stats.nameToId("processDestroyTime");
    destroyResponsesId = this.stats.nameToId("destroyResponses");
    writeDestroyResponseTimeId = this.stats.nameToId("writeDestroyResponseTime");

    queryRequestsId = this.stats.nameToId("queryRequests");
    readQueryRequestTimeId = this.stats.nameToId("readQueryRequestTime");
    processQueryTimeId = this.stats.nameToId("processQueryTime");
    queryResponsesId = this.stats.nameToId("queryResponses");
    writeQueryResponseTimeId = this.stats.nameToId("writeQueryResponseTime");

    destroyRegionRequestsId = this.stats.nameToId("destroyRegionRequests");
    readDestroyRegionRequestTimeId = this.stats.nameToId("readDestroyRegionRequestTime");
    processDestroyRegionTimeId = this.stats.nameToId("processDestroyRegionTime");
    destroyRegionResponsesId = this.stats.nameToId("destroyRegionResponses");
    writeDestroyRegionResponseTimeId = this.stats.nameToId("writeDestroyRegionResponseTime");

    clearRegionRequestsId = this.stats.nameToId("clearRegionRequests");
    readClearRegionRequestTimeId = this.stats.nameToId("readClearRegionRequestTime");
    processClearRegionTimeId = this.stats.nameToId("processClearRegionTime");
    clearRegionResponsesId = this.stats.nameToId("clearRegionResponses");
    writeClearRegionResponseTimeId = this.stats.nameToId("writeClearRegionResponseTime");

    containsKeyRequestsId = this.stats.nameToId("containsKeyRequests");
    readContainsKeyRequestTimeId = this.stats.nameToId("readContainsKeyRequestTime");
    processContainsKeyTimeId = this.stats.nameToId("processContainsKeyTime");
    containsKeyResponsesId = this.stats.nameToId("containsKeyResponses");
    writeContainsKeyResponseTimeId = this.stats.nameToId("writeContainsKeyResponseTime");

    processBatchRequestsId = this.stats.nameToId("processBatchRequests");
    readProcessBatchRequestTimeId = this.stats.nameToId("readProcessBatchRequestTime");
    processBatchTimeId = this.stats.nameToId("processBatchTime");
    processBatchResponsesId = this.stats.nameToId("processBatchResponses");
    writeProcessBatchResponseTimeId = this.stats.nameToId("writeProcessBatchResponseTime");
    batchSizeId = this.stats.nameToId("batchSize");

    clientNotificationRequestsId = this.stats.nameToId("clientNotificationRequests");
    readClientNotificationRequestTimeId = this.stats.nameToId("readClientNotificationRequestTime");
    processClientNotificationTimeId = this.stats.nameToId("processClientNotificationTime");

    updateClientNotificationRequestsId = this.stats.nameToId("updateClientNotificationRequests");
    readUpdateClientNotificationRequestTimeId =
        this.stats.nameToId("readUpdateClientNotificationRequestTime");
    processUpdateClientNotificationTimeId =
        this.stats.nameToId("processUpdateClientNotificationTime");

    clientReadyRequestsId = this.stats.nameToId("clientReadyRequests");
    readClientReadyRequestTimeId = this.stats.nameToId("readClientReadyRequestTime");
    processClientReadyTimeId = this.stats.nameToId("processClientReadyTime");
    clientReadyResponsesId = this.stats.nameToId("clientReadyResponses");
    writeClientReadyResponseTimeId = this.stats.nameToId("writeClientReadyResponseTime");

    closeConnectionRequestsId = this.stats.nameToId("closeConnectionRequests");
    readCloseConnectionRequestTimeId = this.stats.nameToId("readCloseConnectionRequestTime");
    processCloseConnectionTimeId = this.stats.nameToId("processCloseConnectionTime");

    currentClientConnectionsId = this.stats.nameToId("currentClientConnections");
    currentQueueConnectionsId = this.stats.nameToId("currentQueueConnections");
    currentClientsId = this.stats.nameToId("currentClients");
    failedConnectionAttemptsId = this.stats.nameToId("failedConnectionAttempts");

    outOfOrderBatchIdsId = this.stats.nameToId("outOfOrderGatewayBatchIds");

    abandonedWriteRequestsId = this.stats.nameToId("abandonedWriteRequests");
    abandonedReadRequestsId = this.stats.nameToId("abandonedReadRequests");

    receivedBytesId = this.stats.nameToId("receivedBytes");
    sentBytesId = this.stats.nameToId("sentBytes");

    messagesBeingReceivedId = this.stats.nameToId("messagesBeingReceived");
    messageBytesBeingReceivedId = this.stats.nameToId("messageBytesBeingReceived");
    connectionsTimedOutId = this.stats.nameToId("connectionsTimedOut");
    threadQueueSizeId = this.stats.nameToId("threadQueueSize");
    acceptsInProgressId = this.stats.nameToId("acceptsInProgress");
    acceptThreadStartsId = this.stats.nameToId("acceptThreadStarts");
    connectionThreadStartsId = this.stats.nameToId("connectionThreadStarts");
    connectionThreadsId = this.stats.nameToId("connectionThreads");

    connectionLoadId = this.stats.nameToId("connectionLoad");
    queueLoadId = this.stats.nameToId("queueLoad");
    loadPerConnectionId = this.stats.nameToId("loadPerConnection");
    loadPerQueueId = this.stats.nameToId("loadPerQueue");
  }

  @Override
  public void incAcceptThreadsCreated() {
    this.stats.incInt(acceptThreadStartsId, 1);
  }

  @Override
  public void incConnectionThreadsCreated() {
    this.stats.incInt(connectionThreadStartsId, 1);
  }

  @Override
  public void incAcceptsInProgress() {
    this.stats.incInt(acceptsInProgressId, 1);
  }

  @Override
  public void decAcceptsInProgress() {
    this.stats.incInt(acceptsInProgressId, -1);
  }

  @Override
  public void incConnectionThreads() {
    this.stats.incInt(connectionThreadsId, 1);
  }

  @Override
  public void decConnectionThreads() {
    this.stats.incInt(connectionThreadsId, -1);
  }

  @Override
  public void incAbandonedWriteRequests() {
    this.stats.incInt(abandonedWriteRequestsId, 1);
  }

  @Override
  public void incAbandonedReadRequests() {
    this.stats.incInt(abandonedReadRequestsId, 1);
  }

  @Override
  public void incFailedConnectionAttempts() {
    this.stats.incInt(failedConnectionAttemptsId, 1);
  }

  @Override
  public void incConnectionsTimedOut() {
    this.stats.incInt(connectionsTimedOutId, 1);
  }

  @Override
  public void incCurrentClientConnections() {
    this.stats.incInt(currentClientConnectionsId, 1);
  }

  @Override
  public void decCurrentClientConnections() {
    this.stats.incInt(currentClientConnectionsId, -1);
  }

  @Override
  public int getCurrentClientConnections() {
    return this.stats.getInt(currentClientConnectionsId);
  }

  @Override
  public void incCurrentQueueConnections() {
    this.stats.incInt(currentQueueConnectionsId, 1);
  }

  @Override
  public void decCurrentQueueConnections() {
    this.stats.incInt(currentQueueConnectionsId, -1);
  }

  @Override
  public int getCurrentQueueConnections() {
    return this.stats.getInt(currentQueueConnectionsId);
  }

  @Override
  public void incCurrentClients() {
    this.stats.incInt(currentClientsId, 1);
  }

  @Override
  public void decCurrentClients() {
    this.stats.incInt(currentClientsId, -1);
  }

  @Override
  public void incThreadQueueSize() {
    this.stats.incInt(threadQueueSizeId, 1);
  }

  @Override
  public void decThreadQueueSize() {
    this.stats.incInt(threadQueueSizeId, -1);
  }

  @Override
  public void incReadGetRequestTime(long delta) {
    this.stats.incLong(readGetRequestTimeId, delta);
    this.stats.incInt(getRequestsId, 1);
  }

  @Override
  public void incProcessGetTime(long delta) {
    this.stats.incLong(processGetTimeId, delta);
  }

  @Override
  public void incWriteGetResponseTime(long delta) {
    this.stats.incLong(writeGetResponseTimeId, delta);
    this.stats.incInt(getResponsesId, 1);
  }

  @Override
  public void incReadPutAllRequestTime(long delta) {
    this.stats.incLong(readPutAllRequestTimeId, delta);
    this.stats.incInt(putAllRequestsId, 1);
  }

  @Override
  public void incProcessPutAllTime(long delta) {
    this.stats.incLong(processPutAllTimeId, delta);
  }

  @Override
  public void incWritePutAllResponseTime(long delta) {
    this.stats.incLong(writePutAllResponseTimeId, delta);
    this.stats.incInt(putAllResponsesId, 1);
  }

  @Override
  public void incReadRemoveAllRequestTime(long delta) {
    this.stats.incLong(readRemoveAllRequestTimeId, delta);
    this.stats.incInt(removeAllRequestsId, 1);
  }

  @Override
  public void incProcessRemoveAllTime(long delta) {
    this.stats.incLong(processRemoveAllTimeId, delta);
  }

  @Override
  public void incWriteRemoveAllResponseTime(long delta) {
    this.stats.incLong(writeRemoveAllResponseTimeId, delta);
    this.stats.incInt(removeAllResponsesId, 1);
  }

  @Override
  public void incReadGetAllRequestTime(long delta) {
    this.stats.incLong(readGetAllRequestTimeId, delta);
    this.stats.incInt(getAllRequestsId, 1);
  }

  @Override
  public void incProcessGetAllTime(long delta) {
    this.stats.incLong(processGetAllTimeId, delta);
  }

  @Override
  public void incWriteGetAllResponseTime(long delta) {
    this.stats.incLong(writeGetAllResponseTimeId, delta);
    this.stats.incInt(getAllResponsesId, 1);
  }

  @Override
  public void incReadPutRequestTime(long delta) {
    this.stats.incLong(readPutRequestTimeId, delta);
    this.stats.incInt(putRequestsId, 1);
  }

  @Override
  public void incProcessPutTime(long delta) {
    this.stats.incLong(processPutTimeId, delta);
  }

  @Override
  public void incWritePutResponseTime(long delta) {
    this.stats.incLong(writePutResponseTimeId, delta);
    this.stats.incInt(putResponsesId, 1);
  }

  @Override
  public void incReadDestroyRequestTime(long delta) {
    this.stats.incLong(readDestroyRequestTimeId, delta);
    this.stats.incInt(destroyRequestsId, 1);
  }

  @Override
  public void incProcessDestroyTime(long delta) {
    this.stats.incLong(processDestroyTimeId, delta);
  }

  @Override
  public void incWriteDestroyResponseTime(long delta) {
    this.stats.incLong(writeDestroyResponseTimeId, delta);
    this.stats.incInt(destroyResponsesId, 1);
  }



  @Override
  public void incReadInvalidateRequestTime(long delta) {
    // this.stats.incLong(readInvalidateRequestTimeId, delta);
    // this.stats.incInt(invalidateRequestsId, 1);
  }

  @Override
  public void incProcessInvalidateTime(long delta) {
    // this.stats.incLong(processInvalidateTimeId, delta);
  }

  @Override
  public void incWriteInvalidateResponseTime(long delta) {
    // this.stats.incLong(writeInvalidateResponseTimeId, delta);
    // this.stats.incInt(invalidateResponsesId, 1);
  }



  @Override
  public void incReadSizeRequestTime(long delta) {
    // this.stats.incLong(readSizeRequestTimeId, delta);
    // this.stats.incInt(sizeRequestsId, 1);
  }

  @Override
  public void incProcessSizeTime(long delta) {
    // this.stats.incLong(processSizeTimeId, delta);
  }

  @Override
  public void incWriteSizeResponseTime(long delta) {
    // this.stats.incLong(writeSizeResponseTimeId, delta);
    // this.stats.incInt(sizeResponsesId, 1);
  }



  @Override
  public void incReadQueryRequestTime(long delta) {
    this.stats.incLong(readQueryRequestTimeId, delta);
    this.stats.incInt(queryRequestsId, 1);
  }

  @Override
  public void incProcessQueryTime(long delta) {
    this.stats.incLong(processQueryTimeId, delta);
  }

  @Override
  public void incWriteQueryResponseTime(long delta) {
    this.stats.incLong(writeQueryResponseTimeId, delta);
    this.stats.incInt(queryResponsesId, 1);
  }

  @Override
  public void incProcessCreateCqTime(long delta) {
    // this.stats.incLong(processCreateCqTimeId, delta);
  }

  @Override
  public void incProcessCloseCqTime(long delta) {
    // this.stats.incLong(processCloseCqTimeId, delta);
  }

  @Override
  public void incProcessExecuteCqWithIRTime(long delta) {
    // this.stats.incLong(processExecuteCqWithIRCqTimeId, delta);
  }

  @Override
  public void incProcessStopCqTime(long delta) {
    // this.stats.incLong(processStopCqTimeId, delta);
  }

  @Override
  public void incProcessCloseClientCqsTime(long delta) {
    // this.stats.incLong(processCloseClientCqsTimeId, delta);
  }

  @Override
  public void incProcessGetCqStatsTime(long delta) {
    // this.stats.incLong(processGetCqStatsTimeId, delta);
  }

  @Override
  public void incReadDestroyRegionRequestTime(long delta) {
    this.stats.incLong(readDestroyRegionRequestTimeId, delta);
    this.stats.incInt(destroyRegionRequestsId, 1);
  }

  @Override
  public void incProcessDestroyRegionTime(long delta) {
    this.stats.incLong(processDestroyRegionTimeId, delta);
  }

  @Override
  public void incWriteDestroyRegionResponseTime(long delta) {
    this.stats.incLong(writeDestroyRegionResponseTimeId, delta);
    this.stats.incInt(destroyRegionResponsesId, 1);
  }

  @Override
  public void incReadContainsKeyRequestTime(long delta) {
    this.stats.incLong(readContainsKeyRequestTimeId, delta);
    this.stats.incInt(containsKeyRequestsId, 1);
  }

  @Override
  public void incProcessContainsKeyTime(long delta) {
    this.stats.incLong(processContainsKeyTimeId, delta);
  }

  @Override
  public void incWriteContainsKeyResponseTime(long delta) {
    this.stats.incLong(writeContainsKeyResponseTimeId, delta);
    this.stats.incInt(containsKeyResponsesId, 1);
  }

  @Override
  public void incReadClearRegionRequestTime(long delta) {
    this.stats.incLong(readClearRegionRequestTimeId, delta);
    this.stats.incInt(clearRegionRequestsId, 1);
  }

  @Override
  public void incProcessClearRegionTime(long delta) {
    this.stats.incLong(processClearRegionTimeId, delta);
  }

  @Override
  public void incWriteClearRegionResponseTime(long delta) {
    this.stats.incLong(writeClearRegionResponseTimeId, delta);
    this.stats.incInt(clearRegionResponsesId, 1);
  }

  @Override
  public void incReadProcessBatchRequestTime(long delta) {
    this.stats.incLong(readProcessBatchRequestTimeId, delta);
    this.stats.incInt(processBatchRequestsId, 1);
  }

  @Override
  public void incWriteProcessBatchResponseTime(long delta) {
    this.stats.incLong(writeProcessBatchResponseTimeId, delta);
    this.stats.incInt(processBatchResponsesId, 1);
  }

  @Override
  public void incProcessBatchTime(long delta) {
    this.stats.incLong(processBatchTimeId, delta);
  }

  @Override
  public void incBatchSize(long size) {
    this.stats.incLong(batchSizeId, size);
  }

  @Override
  public void incReadClientNotificationRequestTime(long delta) {
    this.stats.incLong(readClientNotificationRequestTimeId, delta);
    this.stats.incInt(clientNotificationRequestsId, 1);
  }

  @Override
  public void incProcessClientNotificationTime(long delta) {
    this.stats.incLong(processClientNotificationTimeId, delta);
  }

  @Override
  public void incReadUpdateClientNotificationRequestTime(long delta) {
    this.stats.incLong(readUpdateClientNotificationRequestTimeId, delta);
    this.stats.incInt(updateClientNotificationRequestsId, 1);
  }

  @Override
  public void incProcessUpdateClientNotificationTime(long delta) {
    this.stats.incLong(processUpdateClientNotificationTimeId, delta);
  }

  @Override
  public void incReadCloseConnectionRequestTime(long delta) {
    this.stats.incLong(readCloseConnectionRequestTimeId, delta);
    this.stats.incInt(closeConnectionRequestsId, 1);
  }

  @Override
  public void incProcessCloseConnectionTime(long delta) {
    this.stats.incLong(processCloseConnectionTimeId, delta);
  }

  @Override
  public void incOutOfOrderBatchIds() {
    this.stats.incInt(outOfOrderBatchIdsId, 1);
  }

  @Override
  public void incReceivedBytes(long v) {
    this.stats.incLong(receivedBytesId, v);
  }

  @Override
  public void incSentBytes(long v) {
    this.stats.incLong(sentBytesId, v);
  }

  @Override
  public void incMessagesBeingReceived(int bytes) {
    stats.incInt(messagesBeingReceivedId, 1);
    if (bytes > 0) {
      stats.incLong(messageBytesBeingReceivedId, bytes);
    }
  }

  @Override
  public void decMessagesBeingReceived(int bytes) {
    stats.incInt(messagesBeingReceivedId, -1);
    if (bytes > 0) {
      stats.incLong(messageBytesBeingReceivedId, -bytes);
    }
  }

  @Override
  public void incReadClientReadyRequestTime(long delta) {
    this.stats.incLong(readClientReadyRequestTimeId, delta);
    this.stats.incInt(clientReadyRequestsId, 1);
  }

  @Override
  public void incProcessClientReadyTime(long delta) {
    this.stats.incLong(processClientReadyTimeId, delta);
  }

  @Override
  public void incWriteClientReadyResponseTime(long delta) {
    this.stats.incLong(writeClientReadyResponseTimeId, delta);
    this.stats.incInt(clientReadyResponsesId, 1);
  }

  @Override
  public void setLoad(float connectionLoad, float loadPerConnection, float queueLoad, float loadPerQueue) {
    this.stats.setDouble(connectionLoadId, connectionLoad);
    this.stats.setDouble(queueLoadId, queueLoad);
    this.stats.setDouble(loadPerConnectionId, loadPerConnection);
    this.stats.setDouble(loadPerQueueId, loadPerQueue);
  }

  @Override
  public double getQueueLoad() {
    return this.stats.getDouble(queueLoadId);
  }

  @Override
  public double getLoadPerQueue() {
    return this.stats.getDouble(loadPerQueueId);
  }

  @Override
  public double getConnectionLoad() {
    return this.stats.getDouble(connectionLoadId);
  }

  @Override
  public double getLoadPerConnection() {
    return this.stats.getDouble(loadPerConnectionId);
  }

  @Override
  public int getProcessBatchRequests() {
    return this.stats.getInt(processBatchRequestsId);
  }

  @Override
  public void close() {
    this.stats.close();
  }

  @Override
  public PoolStatHelper getCnxPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incConnectionThreads();
      }

      public void endJob() {
        decConnectionThreads();
      }
    };
  }

  @Override
  public Statistics getStats() {
    return stats;
  }
}
