package org.apache.geode.internal.cache;

import org.apache.geode.statistics.Statistics;

public interface DiskRegionStats {
  void close();

  long getWrites();

  long getWriteTime();

  long getBytesWritten();

  long getReads();

  long getReadTime();

  long getBytesRead();

  long getRemoves();

  long getRemoveTime();

  long getNumOverflowOnDisk();

  long getNumOverflowBytesOnDisk();

  long getNumEntriesInVM();

  void incNumOverflowOnDisk(long delta);

  void incNumEntriesInVM(long delta);

  void incNumOverflowBytesOnDisk(long delta);

  void startWrite();

  void incWrittenBytes(long bytesWritten);

  void endWrite(long start, long end);

  void endRead(long start, long end, long bytesRead);

  void endRemove(long start, long end);

  void incInitializations(boolean local);

  int getLocalInitializations();

  int getRemoteInitializations();

  Statistics getStats();
}
