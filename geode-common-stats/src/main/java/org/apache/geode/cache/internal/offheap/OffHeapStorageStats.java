package org.apache.geode.internal.offheap;

import org.apache.geode.statistics.Statistics;

public interface OffHeapStorageStats {
  void incFreeMemory(long value);

  void incMaxMemory(long value);

  void incUsedMemory(long value);

  void incObjects(int value);

  void incReads();

  void setFragments(long value);

  void setLargestFragment(int value);

  long startDefragmentation();

  void endDefragmentation(long start);

  void setFragmentation(int value);

  long getFreeMemory();

  long getMaxMemory();

  long getUsedMemory();

  long getReads();

  int getObjects();

  int getDefragmentations();

  int getDefragmentationsInProgress();

  long getFragments();

  int getLargestFragment();

  int getFragmentation();

  long getDefragmentationTime();

  Statistics getStats();

  void close();

  void initialize(OffHeapStorageStats stats);
}
