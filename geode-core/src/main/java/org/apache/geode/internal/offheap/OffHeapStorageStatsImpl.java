package org.apache.geode.internal.offheap;

import org.apache.geode.distributed.internal.DistributionStatsImpl;
import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;

public class OffHeapStorageStatsImpl implements OffHeapStorageStats, GFSStatsImplementor {

  // statistics type
  private StatisticsType statsType;
  private final Statistics stats;
  private static final String statsTypeName = "OffHeapStorageStats";
  private static final String statsTypeDescription = "Statistics about off-heap memory storage.";

  public OffHeapStorageStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(statsType, "OffHeapStorageStats-" + name);
  }

  // statistics instance
  private static final String statsName = "offHeapStorageStats";

  // statistics fields
  private int freeMemoryId;
  private int maxMemoryId;
  private int usedMemoryId;
  private int objectsId;
  private int readsId;
  private int defragmentationId;
  private int fragmentsId;
  private int largestFragmentId;
  private int defragmentationTimeId;
  private int fragmentationId;
  private int defragmentationsInProgressId;

  @Override
  public void initializeStats(StatisticsFactory factory) {

    final String usedMemoryDesc =
        "The amount of off-heap memory, in bytes, that is being used to store data.";
    final String defragmentationDesc =
        "The total number of times off-heap memory has been defragmented.";
    final String defragmentationsInProgressDesc =
        "Current number of defragment operations currently in progress.";
    final String defragmentationTimeDesc = "The total time spent defragmenting off-heap memory.";
    final String
        fragmentationDesc =
        "The percentage of off-heap free memory that is fragmented.  Updated every time a defragmentation is performed.";
    final String
        fragmentsDesc =
        "The number of fragments of free off-heap memory. Updated every time a defragmentation is done.";
    final String freeMemoryDesc =
        "The amount of off-heap memory, in bytes, that is not being used.";
    final String
        largestFragmentDesc =
        "The largest fragment of memory found by the last defragmentation of off heap memory. Updated every time a defragmentation is done.";
    final String objectsDesc = "The number of objects stored in off-heap memory.";
    final String
        readsDesc =
        "The total number of reads of off-heap memory. Only reads of a full object increment this statistic. If only a part of the object is read this statistic is not incremented.";
    final String
        maxMemoryDesc =
        "The maximum amount of off-heap memory, in bytes. This is the amount of memory allocated at startup and does not change.";

    final String usedMemory = "usedMemory";
    final String defragmentations = "defragmentations";
    final String defragmentationsInProgress = "defragmentationsInProgress";
    final String defragmentationTime = "defragmentationTime";
    final String fragmentation = "fragmentation";
    final String fragments = "fragments";
    final String freeMemory = "freeMemory";
    final String largestFragment = "largestFragment";
    final String objects = "objects";
    final String reads = "reads";
    final String maxMemory = "maxMemory";

    statsType = factory.createType(statsTypeName, statsTypeDescription,
        new StatisticDescriptor[]{factory.createLongGauge(usedMemory, usedMemoryDesc, "bytes"),
            factory.createIntCounter(defragmentations, defragmentationDesc, "operations"),
            factory.createIntGauge(defragmentationsInProgress, defragmentationsInProgressDesc,
                "operations"),
            factory.createLongCounter(defragmentationTime, defragmentationTimeDesc, "nanoseconds",
                false),
            factory.createIntGauge(fragmentation, fragmentationDesc, "percentage"),
            factory.createLongGauge(fragments, fragmentsDesc, "fragments"),
            factory.createLongGauge(freeMemory, freeMemoryDesc, "bytes"),
            factory.createIntGauge(largestFragment, largestFragmentDesc, "bytes"),
            factory.createIntGauge(objects, objectsDesc, "objects"),
            factory.createLongCounter(reads, readsDesc, "operations"),
            factory.createLongGauge(maxMemory, maxMemoryDesc, "bytes"),});

    usedMemoryId = statsType.nameToId(usedMemory);
    defragmentationId = statsType.nameToId(defragmentations);
    defragmentationsInProgressId = statsType.nameToId(defragmentationsInProgress);
    defragmentationTimeId = statsType.nameToId(defragmentationTime);
    fragmentationId = statsType.nameToId(fragmentation);
    fragmentsId = statsType.nameToId(fragments);
    freeMemoryId = statsType.nameToId(freeMemory);
    largestFragmentId = statsType.nameToId(largestFragment);
    objectsId = statsType.nameToId(objects);
    readsId = statsType.nameToId(reads);
    maxMemoryId = statsType.nameToId(maxMemory);
  }

  public void incFreeMemory(long value) {
    this.stats.incLong(freeMemoryId, value);
  }

  public void incMaxMemory(long value) {
    this.stats.incLong(maxMemoryId, value);
  }

  public void incUsedMemory(long value) {
    this.stats.incLong(usedMemoryId, value);
  }

  public void incObjects(int value) {
    this.stats.incInt(objectsId, value);
  }

  public long getFreeMemory() {
    return this.stats.getLong(freeMemoryId);
  }

  public long getMaxMemory() {
    return this.stats.getLong(maxMemoryId);
  }

  public long getUsedMemory() {
    return this.stats.getLong(usedMemoryId);
  }

  public int getObjects() {
    return this.stats.getInt(objectsId);
  }

  @Override
  public void incReads() {
    this.stats.incLong(readsId, 1);
  }

  @Override
  public long getReads() {
    return this.stats.getLong(readsId);
  }

  private void incDefragmentations() {
    this.stats.incInt(defragmentationId, 1);
  }

  @Override
  public int getDefragmentations() {
    return this.stats.getInt(defragmentationId);
  }

  @Override
  public void setFragments(long value) {
    this.stats.setLong(fragmentsId, value);
  }

  @Override
  public long getFragments() {
    return this.stats.getLong(fragmentsId);
  }

  @Override
  public void setLargestFragment(int value) {
    this.stats.setInt(largestFragmentId, value);
  }

  @Override
  public int getLargestFragment() {
    return this.stats.getInt(largestFragmentId);
  }

  @Override
  public int getDefragmentationsInProgress() {
    return this.stats.getInt(defragmentationsInProgressId);
  }

  @Override
  public long startDefragmentation() {
    this.stats.incInt(defragmentationsInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endDefragmentation(long start) {
    incDefragmentations();
    this.stats.incInt(defragmentationsInProgressId, -1);
    if (DistributionStatsImpl.enableClockStats) {
      stats.incLong(defragmentationTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public long getDefragmentationTime() {
    return stats.getLong(defragmentationTimeId);
  }

  @Override
  public void setFragmentation(int value) {
    this.stats.setInt(fragmentationId, value);
  }

  @Override
  public int getFragmentation() {
    return this.stats.getInt(fragmentationId);
  }

  public Statistics getStats() {
    return this.stats;
  }

  @Override
  public void close() {
    this.stats.close();
  }

  @Override
  public void initialize(OffHeapStorageStats oldStats) {
    setFreeMemory(oldStats.getFreeMemory());
    setMaxMemory(oldStats.getMaxMemory());
    setUsedMemory(oldStats.getUsedMemory());
    setObjects(oldStats.getObjects());
    setReads(oldStats.getReads());
    setDefragmentations(oldStats.getDefragmentations());
    setDefragmentationsInProgress(oldStats.getDefragmentationsInProgress());
    setFragments(oldStats.getFragments());
    setLargestFragment(oldStats.getLargestFragment());
    setDefragmentationTime(oldStats.getDefragmentationTime());
    setFragmentation(oldStats.getFragmentation());

    oldStats.close();
  }

  private void setDefragmentationTime(long value) {
    stats.setLong(defragmentationTimeId, value);
  }

  private void setDefragmentations(int value) {
    this.stats.setInt(defragmentationId, value);
  }

  private void setDefragmentationsInProgress(int value) {
    this.stats.setInt(defragmentationsInProgressId, value);
  }

  private void setReads(long value) {
    this.stats.setLong(readsId, value);
  }

  private void setObjects(int value) {
    this.stats.setInt(objectsId, value);
  }

  private void setUsedMemory(long value) {
    this.stats.setLong(usedMemoryId, value);
  }

  private void setMaxMemory(long value) {
    this.stats.setLong(maxMemoryId, value);
  }

  private void setFreeMemory(long value) {
    this.stats.setLong(freeMemoryId, value);
  }
}
