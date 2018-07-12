package org.apache.geode.internal.cache.tier.sockets;

import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.statistics.Statistics;

public interface CacheServerStats extends MessageStats {
  void incAcceptThreadsCreated();

  void incConnectionThreadsCreated();

  void incAcceptsInProgress();

  void decAcceptsInProgress();

  void incConnectionThreads();

  void decConnectionThreads();

  void incAbandonedWriteRequests();

  void incAbandonedReadRequests();

  void incFailedConnectionAttempts();

  void incConnectionsTimedOut();

  void incCurrentClientConnections();

  void decCurrentClientConnections();

  int getCurrentClientConnections();

  void incCurrentQueueConnections();

  void decCurrentQueueConnections();

  int getCurrentQueueConnections();

  void incCurrentClients();

  void decCurrentClients();

  void incThreadQueueSize();

  void decThreadQueueSize();

  void incReadGetRequestTime(long delta);

  void incProcessGetTime(long delta);

  void incWriteGetResponseTime(long delta);

  void incReadPutAllRequestTime(long delta);

  void incProcessPutAllTime(long delta);

  void incWritePutAllResponseTime(long delta);

  void incReadRemoveAllRequestTime(long delta);

  void incProcessRemoveAllTime(long delta);

  void incWriteRemoveAllResponseTime(long delta);

  void incReadGetAllRequestTime(long delta);

  void incProcessGetAllTime(long delta);

  void incWriteGetAllResponseTime(long delta);

  void incReadPutRequestTime(long delta);

  void incProcessPutTime(long delta);

  void incWritePutResponseTime(long delta);

  void incReadDestroyRequestTime(long delta);

  void incProcessDestroyTime(long delta);

  void incWriteDestroyResponseTime(long delta);

  void incReadInvalidateRequestTime(long delta);

  void incProcessInvalidateTime(long delta);

  void incWriteInvalidateResponseTime(long delta);

  void incReadSizeRequestTime(long delta);

  void incProcessSizeTime(long delta);

  void incWriteSizeResponseTime(long delta);

  void incReadQueryRequestTime(long delta);

  void incProcessQueryTime(long delta);

  void incWriteQueryResponseTime(long delta);

  void incProcessCreateCqTime(long delta);

  void incProcessCloseCqTime(long delta);

  void incProcessExecuteCqWithIRTime(long delta);

  void incProcessStopCqTime(long delta);

  void incProcessCloseClientCqsTime(long delta);

  void incProcessGetCqStatsTime(long delta);

  void incReadDestroyRegionRequestTime(long delta);

  void incProcessDestroyRegionTime(long delta);

  void incWriteDestroyRegionResponseTime(long delta);

  void incReadContainsKeyRequestTime(long delta);

  void incProcessContainsKeyTime(long delta);

  void incWriteContainsKeyResponseTime(long delta);

  void incReadClearRegionRequestTime(long delta);

  void incProcessClearRegionTime(long delta);

  void incWriteClearRegionResponseTime(long delta);

  void incReadProcessBatchRequestTime(long delta);

  void incWriteProcessBatchResponseTime(long delta);

  void incProcessBatchTime(long delta);

  void incBatchSize(long size);

  void incReadClientNotificationRequestTime(long delta);

  void incProcessClientNotificationTime(long delta);

  void incReadUpdateClientNotificationRequestTime(long delta);

  void incProcessUpdateClientNotificationTime(long delta);

  void incReadCloseConnectionRequestTime(long delta);

  void incProcessCloseConnectionTime(long delta);

  void incOutOfOrderBatchIds();

  void incReceivedBytes(long v);

  void incSentBytes(long v);

  void incMessagesBeingReceived(int bytes);

  void decMessagesBeingReceived(int bytes);

  void incReadClientReadyRequestTime(long delta);

  void incProcessClientReadyTime(long delta);

  void incWriteClientReadyResponseTime(long delta);

  void setLoad(float connectionLoad, float loadPerConnection, float queueLoad, float loadPerQueue);

  double getQueueLoad();

  double getLoadPerQueue();

  double getConnectionLoad();

  double getLoadPerConnection();

  int getProcessBatchRequests();

  void close();

  PoolStatHelper getCnxPoolHelper();

  Statistics getStats();
}
