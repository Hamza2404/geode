package org.apache.geode.internal.cache.wan;

import org.apache.geode.statistics.Statistics;

public interface GatewaySenderStats {

  void close();

  int getEventsReceived();

  void incEventsReceived();

  int getEventsQueued();

  int getEventsNotQueuedConflated();

  int getEventsConflatedFromBatches();

  int getEventQueueSize();

  int getSecondaryEventQueueSize();

  int getEventsProcessedByPQRM();

  int getTempEventQueueSize();

  int getEventsDistributed();

  int getEventsExceedingAlertThreshold();

  void incEventsExceedingAlertThreshold();

  long getBatchDistributionTime();

  int getBatchesDistributed();

  int getBatchesRedistributed();

  int getBatchesResized();

  void incBatchesRedistributed();

  void incBatchesResized();

  void setQueueSize(int size);

  void setSecondaryQueueSize(int size);

  void setEventsProcessedByPQRM(int size);

  void setTempQueueSize(int size);

  void incQueueSize();

  void incSecondaryQueueSize();

  void incTempQueueSize();

  void incQueueSize(int delta);

  void incSecondaryQueueSize(int delta);

  void incEventsProcessedByPQRM(int delta);

  void incTempQueueSize(int delta);

  void decQueueSize();

  void decSecondaryQueueSize();

  void decTempQueueSize();

  void decQueueSize(int delta);

  void decSecondaryQueueSize(int delta);

  void decTempQueueSize(int delta);

  void incEventsNotQueuedConflated();

  void incEventsConflatedFromBatches(int numEvents);

  int getUnprocessedTokensAddedByPrimary();

  int getUnprocessedEventsAddedBySecondary();

  int getUnprocessedEventsRemovedByPrimary();

  int getUnprocessedTokensRemovedBySecondary();

  int getUnprocessedEventMapSize();

  int getUnprocessedTokenMapSize();

  void incEventsNotQueued();

  int getEventsNotQueued();

  void incEventsDroppedDueToPrimarySenderNotRunning();

  int getEventsDroppedDueToPrimarySenderNotRunning();

  void incEventsFiltered();

  int getEventsFiltered();

  void incUnprocessedTokensAddedByPrimary();

  void incUnprocessedEventsAddedBySecondary();

  void incUnprocessedEventsRemovedByPrimary();

  void incUnprocessedTokensRemovedBySecondary();

  void incUnprocessedEventsRemovedByTimeout(int count);

  void incUnprocessedTokensRemovedByTimeout(int count);

  void clearUnprocessedMaps();

  void incConflationIndexesMapSize();

  void decConflationIndexesMapSize();

  int getConflationIndexesMapSize();

  long startTime();

  void endBatch(long start, int numberOfEvents);

  void endPut(long start);

  long startLoadBalance();

  void endLoadBalance(long start);

  void incSynchronizationEventsEnqueued();

  void incSynchronizationEventsProvided();

  Statistics getStats();
}
