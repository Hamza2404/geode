package org.apache.geode.cache.query.internal;

public interface CqQueryVsdStats {
  void close();

  long getCqInitialResultsTime();

  void setCqInitialResultsTime(long time);

  long getNumInserts();

  void incNumInserts();

  long getNumUpdates();

  void incNumUpdates();

  long getNumDeletes();

  void incNumDeletes();

  long getNumEvents();

  void incNumEvents();

  long getNumHAQueuedEvents();

  void incNumHAQueuedEvents(long incAmount);

  long getNumCqListenerInvocations();

  long getQueuedCqListenerEvents();

  void incNumCqListenerInvocations();

  void incQueuedCqListenerEvents();

  void decQueuedCqListenerEvents();

  void updateStats(Integer cqEvent);
}
