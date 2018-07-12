package org.apache.geode.internal.cache.tier.sockets;

public interface CacheClientNotifierStats {
  void close();

  int getEvents();

  long getEventProcessingTime();

  long startTime();

  void endEvent(long start);

  void endClientRegistration(long start);

  void endCqProcessing(long start);

  void incClientRegisterRequests();

  int getClientRegisterRequests();

  int get_durableReconnectionCount();

  int get_queueDroppedCount();

  int get_eventEnqueuedWhileClientAwayCount();

  long getCqProcessingTime();

  long getCompiledQueryCount();

  long getCompiledQueryUsedCount();

  void incDurableReconnectionCount();

  void incQueueDroppedCount();

  void incEventEnqueuedWhileClientAwayCount();

  void incClientUnRegisterRequests();

  void incCompiledQueryCount(long count);

  void incCompiledQueryUsedCount(long count);

  int getClientUnRegisterRequests();
}
