package org.apache.geode.internal.cache;

import org.apache.geode.distributed.internal.PoolStatHelper;
import org.apache.geode.distributed.internal.QueueStatHelper;
import org.apache.geode.statistics.Statistics;

public interface CachePerfStats {
  /**
   * Returns the current NanoTime or, if clock stats are disabled, zero.
   * @since GemFire 5.0
   */
  static long getStatTime() {
    return System.nanoTime();
  }

  int getLoadsInProgress();

  int getLoadsCompleted();

  long getLoadTime();

  int getNetloadsInProgress();

  int getNetloadsCompleted();

  long getNetloadTime();

  int getNetsearchesInProgress();

  int getNetsearchesCompleted();

  long getNetsearchTime();

  int getGetInitialImagesInProgress();

  int getGetInitialImagesCompleted();

  int getDeltaGetInitialImagesCompleted();

  long getGetInitialImageTime();

  int getGetInitialImageKeysReceived();

  int getRegions();

  int getPartitionedRegions();

  int getDestroys();

  int getCreates();

  int getPuts();

  int getPutAlls();

  int getRemoveAlls();

  int getUpdates();

  int getInvalidates();

  int getGets();

  int getMisses();

  int getReliableQueuedOps();

  void incReliableQueuedOps(int inc);

  int getReliableQueueSize();

  void incReliableQueueSize(int inc);

  int getReliableQueueMax();

  void incReliableQueueMax(int inc);

  int getReliableRegions();

  void incReliableRegions(int inc);

  int getReliableRegionsMissing();

  void incReliableRegionsMissing(int inc);

  int getReliableRegionsQueuing();

  void incReliableRegionsQueuing(int inc);

  int getReliableRegionsMissingFullAccess();

  void incReliableRegionsMissingFullAccess(int inc);

  int getReliableRegionsMissingLimitedAccess();

  void incReliableRegionsMissingLimitedAccess(int inc);

  int getReliableRegionsMissingNoAccess();

  void incReliableRegionsMissingNoAccess(int inc);

  void incQueuedEvents(int inc);

  long getQueuedEvents();

  int getDeltaUpdates();

  long getDeltaUpdatesTime();

  int getDeltaFailedUpdates();

  int getDeltasPrepared();

  long getDeltasPreparedTime();

  int getDeltasSent();

  int getDeltaFullValuesSent();

  int getDeltaFullValuesRequested();

  long getTotalCompressionTime();

  long getTotalDecompressionTime();

  long getTotalCompressions();

  long getTotalDecompressions();

  long getTotalPreCompressedBytes();

  long getTotalPostCompressedBytes();

  long startCompression();

  void endCompression(long startTime, long startSize, long endSize);

  long startDecompression();

  void endDecompression(long startTime);

  long startLoad();

  void endLoad(long start);

  long startNetload();

  void endNetload(long start);

  long startNetsearch();

  void endNetsearch(long start);

  long startCacheWriterCall();

  void endCacheWriterCall(long start);

  long startCacheListenerCall();

  void endCacheListenerCall(long start);

  long startGetInitialImage();

  void endGetInitialImage(long start);

  void endNoGIIDone(long start);

  void incDeltaGIICompleted();

  void incGetInitialImageKeysReceived();

  long startIndexUpdate();

  void endIndexUpdate(long start);

  long startIndexInitialization();

  void endIndexInitialization(long start);

  long getIndexInitializationTime();

  void incRegions(int inc);

  void incPartitionedRegions(int inc);

  void incDestroys();

  void incCreates();

  void incInvalidates();

  long startGet();

  void endGet(long start, boolean miss);

  long endPut(long start, boolean isUpdate);

  void endPutAll(long start);

  void endRemoveAll(long start);

  void endQueryExecution(long executionTime);

  void endQueryResultsHashCollisionProbe(long start);

  void incQueryResultsHashCollisions();

  int getTxCommits();

  int getTxCommitChanges();

  long getTxCommitTime();

  long getTxSuccessLifeTime();

  int getTxFailures();

  int getTxFailureChanges();

  long getTxFailureTime();

  long getTxFailedLifeTime();

  int getTxRollbacks();

  int getTxRollbackChanges();

  long getTxRollbackTime();

  long getTxRollbackLifeTime();

  void incTxConflictCheckTime(long delta);

  void txSuccess(long opTime, long txLifeTime, int txChanges);

  void txFailure(long opTime, long txLifeTime, int txChanges);

  void txRollback(long opTime, long txLifeTime, int txChanges);

  void endDeltaUpdate(long start);

  void incDeltaFailedUpdates();

  void endDeltaPrepared(long start);

  void incDeltasSent();

  void incDeltaFullValuesSent();

  void incDeltaFullValuesRequested();

  void close();

  boolean isClosed();

  int getEventQueueSize();

  void incEventQueueSize(int items);

  void incEventQueueThrottleCount(int items);

  void incEventQueueThrottleTime(long nanos);

  void incEventThreads(int items);

  void incEntryCount(int delta);

  long getEntries();

  void incRetries();

  void incDiskTasksWaiting();

  void decDiskTasksWaiting();

  int getDiskTasksWaiting();

  void decDiskTasksWaiting(int count);

  void incEvictorJobsStarted();

  void incEvictorJobsCompleted();

  void incEvictorQueueSize(int delta);

  void incEvictWorkTime(long delta);

  Statistics getStats();

  PoolStatHelper getEventPoolHelper();

  int getClearCount();

  void incClearCount();

  long getConflatedEventsCount();

  void incConflatedEventsCount();

  int getTombstoneCount();

  void incTombstoneCount(int amount);

  int getTombstoneGCCount();

  void incTombstoneGCCount();

  void setReplicatedTombstonesSize(long size);

  long getReplicatedTombstonesSize();

  void setNonReplicatedTombstonesSize(long size);

  long getNonReplicatedTombstonesSize();

  int getClearTimeouts();

  void incClearTimeouts();

  void incPRQueryRetries();

  long getPRQueryRetries();

  QueueStatHelper getEvictionQueueStatHelper();

  void incMetaDataRefreshCount();

  long getMetaDataRefreshCount();

  long getImportedEntriesCount();

  long getImportTime();

  void endImport(long entryCount, long start);

  long getExportedEntriesCount();

  long getExportTime();

  void endExport(long entryCount, long start);

  void incEntryCompressedCount();

  void incEntryCompressedTime(long time);

  void incPostCompressedBytes(long size);

  void incPreCompressedBytes(long size);

  void incDecompressedEntryCount();

  void incDecompressionTime(long time);
}
