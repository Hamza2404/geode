package org.apache.geode.internal.cache;

import org.apache.geode.statistics.Statistics;

public interface DiskDirectoryStats {
  void close();

  long getDiskSpace();

  void incDiskSpace(long delta);

  void setMaxSpace(long v);

  void addVolumeCheck(long total, long free, long time);

  Statistics getStats();
}
