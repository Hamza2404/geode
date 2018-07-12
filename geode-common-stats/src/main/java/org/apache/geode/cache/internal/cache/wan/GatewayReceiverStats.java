package org.apache.geode.internal.cache.wan;

import org.apache.geode.internal.cache.tier.sockets.CacheServerStats;

public interface GatewayReceiverStats extends CacheServerStats {
  void incDuplicateBatchesReceived();

  int getDuplicateBatchesReceived();

  void incOutoforderBatchesReceived();

  int getOutoforderBatchesReceived();

  void incEarlyAcks();

  int getEarlyAcks();

  void incEventsReceived(int delta);

  int getEventsReceived();

  void incCreateRequest();

  int getCreateRequest();

  void incUpdateRequest();

  int getUpdateRequest();

  void incDestroyRequest();

  int getDestroyRequest();

  void incUnknowsOperationsReceived();

  int getUnknowsOperationsReceived();

  void incExceptionsOccurred();

  int getExceptionsOccurred();

  void incEventsRetried();

  int getEventsRetried();
}
