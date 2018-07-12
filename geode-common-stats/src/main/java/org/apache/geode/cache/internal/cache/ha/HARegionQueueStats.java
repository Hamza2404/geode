package org.apache.geode.internal.cache.ha;

public interface HARegionQueueStats {
  /** Name of the events queued statistic */
  String EVENTS_QUEUED = "eventsQueued";

  void close();

  long getEventsEnqued();

  void incEventsEnqued();

  long getEventsConflated();

  void incEventsConflated();

  long getMarkerEventsConflated();

  void incMarkerEventsConflated();

  long getEventsRemoved();

  void incEventsRemoved();

  long getEventsTaken();

  void incEventsTaken();

  long getEventsExpired();

  void incEventsExpired();

  long getEventsRemovedByQrm();

  void incEventsRemovedByQrm();

  int getThreadIdentiferCount();

  void incThreadIdentifiers();

  void decThreadIdentifiers();

  long getEventsDispatched();

  void incEventsDispatched();

  long getNumVoidRemovals();

  void incNumVoidRemovals();

  long getNumSequenceViolated();

  void incNumSequenceViolated();

  boolean isClosed();
}
