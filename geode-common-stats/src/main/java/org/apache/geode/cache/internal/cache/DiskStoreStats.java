package org.apache.geode.internal.cache;

import org.apache.geode.statistics.Statistics;

public interface DiskStoreStats {
  void close();

  long getWrites();

  long getWriteTime();

  long getBytesWritten();

  long getReads();

  long getReadTime();

  long getBytesRead();

  long getRemoves();

  long getRemoveTime();

  long getQueueSize();

  void setQueueSize(int value);

  void incQueueSize(int delta);

  void incUncreatedRecoveredRegions(int delta);

  long startWrite();

  long startFlush();

  void incWrittenBytes(long bytesWritten, boolean async);

  long endWrite(long start);

  void endFlush(long start);

  long getFlushes();

  long startRead();

  long endRead(long start, long bytesRead);

  long startRecovery();

  long startCompaction();

  long startOplogRead();

  void endRecovery(long start, long bytesRead);

  void endCompaction(long start);

  void endOplogRead(long start, long bytesRead);

  void incRecoveredEntryCreates();

  void incRecoveredEntryUpdates();

  void incRecoveredEntryDestroys();

  void incRecoveryRecordsSkipped();

  void incRecoveredValuesSkippedDueToLRU();

  long startRemove();

  long endRemove(long start);

  void incOplogReads();

  void incOplogSeeks();

  void incInactiveOplogs(int delta);

  void incCompactableOplogs(int delta);

  void endCompactionDeletes(int count, long delta);

  void endCompactionInsert(long start);

  void endCompactionUpdate(long start);

  long getStatTime();

  void incOpenOplogs();

  void decOpenOplogs();

  void startBackup();

  void endBackup();

  Statistics getStats();
}
