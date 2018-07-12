/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.internal.cache;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * GemFire statistics about a {@link DiskRegion}.
 *
 *
 * @since GemFire 3.2
 */
public class DiskRegionStatsImpl implements DiskRegionStats, GFSStatsImplementor {

  private StatisticsType type;

  //////////////////// Statistic "Id" Fields ////////////////////

  private int writesId;
  private int writeTimeId;
  private int bytesWrittenId;
  private int readsId;
  private int readTimeId;
  private int writesInProgressId;
  private int bytesReadId;
  private int removesId;
  private int removeTimeId;
  private int numOverflowOnDiskId;
  private int numEntriesInVMId;
  private int numOverflowBytesOnDiskId;

  private int localInitializationsId;
  private int remoteInitializationsId;



  @Override
  public void initializeStats(StatisticsFactory factory) {
    String statName = "DiskRegionStatistics";
    String statDescription = "Statistics about a Region's use of the disk";

    final String writesDesc =
        "The total number of region entries that have been written to disk. A write is done every time an entry is created on disk or every time its value is modified on disk.";
    final String writeTimeDesc = "The total amount of time spent writing to disk";
    final String bytesWrittenDesc = "The total number of bytes that have been written to disk";
    final String readsDesc = "The total number of region entries that have been read from disk";
    final String readTimeDesc = "The total amount of time spent reading from disk";
    final String bytesReadDesc = "The total number of bytes that have been read from disk";
    final String removesDesc =
        "The total number of region entries that have been removed from disk";
    final String removeTimeDesc = "The total amount of time spent removing from disk";
    final String numOverflowOnDiskDesc =
        "The current number of entries whose value is on disk and is not in memory. This is true of overflowed entries. It is also true of recovered entries that have not yet been faulted in.";
    final String numOverflowBytesOnDiskDesc =
        "The current number bytes on disk and not in memory. This is true of overflowed entries. It is also true of recovered entries that have not yet been faulted in.";
    final String numEntriesInVMDesc =
        "The current number of entries whose value resides in the VM. The value may also have been written to disk.";
    final String localInitializationsDesc =
        "The number of times that this region has been initialized solely from the local disk files (0 or 1)";
    final String remoteInitializationsDesc =
        "The number of times that this region has been initialized by doing GII from a peer (0 or 1)";

    type = factory.createType(statName, statDescription, new StatisticDescriptor[] {
        factory.createLongCounter("writes", writesDesc, "ops"),
        factory.createLongCounter("writeTime", writeTimeDesc, "nanoseconds"),
        factory.createLongCounter("writtenBytes", bytesWrittenDesc, "bytes"),
        factory.createLongCounter("reads", readsDesc, "ops"),
        factory.createLongCounter("readTime", readTimeDesc, "nanoseconds"),
        factory.createLongCounter("readBytes", bytesReadDesc, "bytes"),
        factory.createLongCounter("removes", removesDesc, "ops"),
        factory.createLongCounter("removeTime", removeTimeDesc, "nanoseconds"),
        factory.createLongGauge("entriesOnlyOnDisk", numOverflowOnDiskDesc, "entries"),
        factory.createLongGauge("bytesOnlyOnDisk", numOverflowBytesOnDiskDesc, "bytes"),
        factory.createLongGauge("entriesInVM", numEntriesInVMDesc, "entries"),
        factory.createIntGauge("writesInProgress", "current number of oplog writes that are in progress",
            "writes"),
        factory.createIntGauge("localInitializations", localInitializationsDesc, "initializations"),
        factory.createIntGauge("remoteInitializations", remoteInitializationsDesc, "initializations"),});

    // Initialize id fields
    writesId = type.nameToId("writes");
    writeTimeId = type.nameToId("writeTime");
    bytesWrittenId = type.nameToId("writtenBytes");
    readsId = type.nameToId("reads");
    readTimeId = type.nameToId("readTime");
    bytesReadId = type.nameToId("readBytes");
    writesInProgressId = type.nameToId("writesInProgress");
    removesId = type.nameToId("removes");
    removeTimeId = type.nameToId("removeTime");
    numOverflowOnDiskId = type.nameToId("entriesOnlyOnDisk");
    numOverflowBytesOnDiskId = type.nameToId("bytesOnlyOnDisk");
    numEntriesInVMId = type.nameToId("entriesInVM");

    localInitializationsId = type.nameToId("localInitializations");
    remoteInitializationsId = type.nameToId("remoteInitializations");
  }

  ////////////////////// Instance Fields //////////////////////

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  /////////////////////// Constructors ///////////////////////

  /**
   * Creates a new <code>DiskRegionStatistics</code> for the given region.
   */
  public DiskRegionStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, name);
  }

  ///////////////////// Instance Methods /////////////////////

  @Override
  public void close() {
    this.stats.close();
  }

  /**
   * Returns the total number of region entries that have been written to disk.
   */
  @Override
  public long getWrites() {
    return this.stats.getLong(writesId);
  }

  /**
   * Returns the total number of nanoseconds spent writing to disk
   */
  @Override
  public long getWriteTime() {
    return this.stats.getLong(writeTimeId);
  }

  /**
   * Returns the total number of bytes that have been written to disk
   */
  @Override
  public long getBytesWritten() {
    return this.stats.getLong(bytesWrittenId);
  }

  /**
   * Returns the total number of region entries that have been read from disk.
   */
  @Override
  public long getReads() {
    return this.stats.getLong(readsId);
  }

  /**
   * Returns the total number of nanoseconds spent reading from disk
   */
  @Override
  public long getReadTime() {
    return this.stats.getLong(readTimeId);
  }

  /**
   * Returns the total number of bytes that have been read from disk
   */
  @Override
  public long getBytesRead() {
    return this.stats.getLong(bytesReadId);
  }

  /**
   * Returns the total number of region entries that have been removed from disk.
   */
  @Override
  public long getRemoves() {
    return this.stats.getLong(removesId);
  }

  /**
   * Returns the total number of nanoseconds spent removing from disk
   */
  @Override
  public long getRemoveTime() {
    return this.stats.getLong(removeTimeId);
  }

  /**
   * Returns the current number of entries whose value has been overflowed to disk. This value will
   * decrease when a value is faulted in.
   */
  @Override
  public long getNumOverflowOnDisk() {
    return this.stats.getLong(numOverflowOnDiskId);
  }

  /**
   * Returns the current number of entries whose value has been overflowed to disk. This value will
   * decrease when a value is faulted in.
   */
  @Override
  public long getNumOverflowBytesOnDisk() {
    return this.stats.getLong(numOverflowBytesOnDiskId);
  }

  /**
   * Returns the current number of entries whose value resides in the VM. This value will decrease
   * when the entry is overflowed to disk.
   */
  @Override
  public long getNumEntriesInVM() {
    return this.stats.getLong(numEntriesInVMId);
  }

  /**
   * Increments the current number of entries whose value has been overflowed to disk by a given
   * amount.
   */
  @Override
  public void incNumOverflowOnDisk(long delta) {
    this.stats.incLong(numOverflowOnDiskId, delta);
  }

  /**
   * Increments the current number of entries whose value has been overflowed to disk by a given
   * amount.
   */
  @Override
  public void incNumEntriesInVM(long delta) {
    this.stats.incLong(numEntriesInVMId, delta);
  }

  /**
   * Increments the current number of entries whose value has been overflowed to disk by a given
   * amount.
   */
  @Override
  public void incNumOverflowBytesOnDisk(long delta) {
    this.stats.incLong(numOverflowBytesOnDiskId, delta);
  }

  /**
   * Invoked before data is written to disk.
   *
   * @see DiskRegion#put
   */
  @Override
  public void startWrite() {
    this.stats.incInt(writesInProgressId, 1);
  }

  @Override
  public void incWrittenBytes(long bytesWritten) {
    this.stats.incLong(bytesWrittenId, bytesWritten);
  }

  /**
   * Invoked after data has been written to disk
   *
   * @param start The time at which the write operation started
   */
  @Override
  public void endWrite(long start, long end) {
    this.stats.incInt(writesInProgressId, -1);
    this.stats.incLong(writesId, 1);
    this.stats.incLong(writeTimeId, end - start);
  }

  /**
   * Invoked after data has been read from disk
   *
   * @param start The time at which the read operation started
   * @param bytesRead The number of bytes that were read
   */
  @Override
  public void endRead(long start, long end, long bytesRead) {
    this.stats.incLong(readsId, 1);
    this.stats.incLong(readTimeId, end - start);
    this.stats.incLong(bytesReadId, bytesRead);
  }

  /**
   * Invoked after data has been removed from disk
   *
   * @param start The time at which the read operation started
   */
  @Override
  public void endRemove(long start, long end) {
    this.stats.incLong(removesId, 1);
    this.stats.incLong(removeTimeId, end - start);
  }

  @Override
  public void incInitializations(boolean local) {
    if (local) {
      this.stats.incInt(localInitializationsId, 1);
    } else {
      this.stats.incInt(remoteInitializationsId, 1);
    }
  }

  @Override
  public int getLocalInitializations() {
    return this.stats.getInt(localInitializationsId);
  }

  @Override
  public int getRemoteInitializations() {
    return this.stats.getInt(remoteInitializationsId);
  }

  @Override
  public Statistics getStats() {
    return stats;
  }
}
