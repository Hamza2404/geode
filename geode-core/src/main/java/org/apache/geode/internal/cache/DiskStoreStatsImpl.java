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

import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.distributed.internal.DistributionStats;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * GemFire statistics about a {@link DiskStoreImpl}.
 *
 *
 * @since GemFire prPersistSprint2
 */
public class DiskStoreStatsImpl implements DiskStoreStats{

  private StatisticsType type;

  //////////////////// Statistic "Id" Fields ////////////////////

  private int writesId;
  private int writeTimeId;
  private int bytesWrittenId;
  private int flushesId;
  private int flushTimeId;
  private int bytesFlushedId;
  private int readsId;
  private int readTimeId;
  private int recoveriesInProgressId;
  private int recoveryTimeId;
  private int recoveredBytesId;
  private int recoveredEntryCreatesId;
  private int recoveredEntryUpdatesId;
  private int recoveredEntryDestroysId;
  private int recoveredValuesSkippedDueToLRUId;
  private int recoveryRecordsSkippedId;
  private int compactsInProgressId;
  private int writesInProgressId;
  private int flushesInProgressId;
  private int compactTimeId;
  private int compactsId;
  private int oplogRecoveriesId;
  private int oplogRecoveryTimeId;
  private int oplogRecoveredBytesId;
  private int bytesReadId;
  private int removesId;
  private int removeTimeId;
  private int queueSizeId;

  private int compactInsertsId;
  private int compactInsertTimeId;
  private int compactUpdatesId;
  private int compactUpdateTimeId;
  private int compactDeletesId;
  private int compactDeleteTimeId;

  private int openOplogsId;
  private int inactiveOplogsId;
  private int compactableOplogsId;

  private int oplogReadsId;
  private int oplogSeeksId;

  private int uncreatedRecoveredRegionsId;
  private int backupsInProgress;
  private int backupsCompleted;

  private void initializeStats(StatisticsFactory factory) {
    String statName = "DiskStoreStatistics";
    String statDescription = "Statistics about a Region's use of the disk";

    final String writesDesc =
        "The total number of region entries that have been written to disk. A write is done every time an entry is created on disk or every time its value is modified on disk.";
    final String writeTimeDesc = "The total amount of time spent writing to disk";
    final String bytesWrittenDesc = "The total number of bytes that have been written to disk";
    final String flushesDesc =
        "The total number of times the an entry has been flushed from the async queue.";
    final String flushTimeDesc = "The total amount of time spent doing an async queue flush.";
    final String bytesFlushedDesc =
        "The total number of bytes written to disk by async queue flushes.";
    final String readsDesc = "The total number of region entries that have been read from disk";
    final String readTimeDesc = "The total amount of time spent reading from disk";
    final String bytesReadDesc = "The total number of bytes that have been read from disk";
    final String recoveryTimeDesc = "The total amount of time spent doing a recovery";
    final String recoveredBytesDesc =
        "The total number of bytes that have been read from disk during a recovery";
    final String oplogRecoveriesDesc = "The total number of oplogs recovered";
    final String oplogRecoveryTimeDesc = "The total amount of time spent doing an oplog recovery";
    final String oplogRecoveredBytesDesc =
        "The total number of bytes that have been read from oplogs during a recovery";
    final String removesDesc =
        "The total number of region entries that have been removed from disk";
    final String removeTimeDesc = "The total amount of time spent removing from disk";
    final String queueSizeDesc =
        "The current number of entries in the async queue waiting to be flushed to disk";
    final String backupsInProgressDesc =
        "The current number of backups in progress on this disk store";
    final String backupsCompletedDesc =
        "The number of backups of this disk store that have been taking while this VM was alive";

    type = factory.createType(statName, statDescription,
        new StatisticDescriptor[] {factory.createLongCounter("writes", writesDesc, "ops"),
            factory.createLongCounter("writeTime", writeTimeDesc, "nanoseconds"),
            factory.createLongCounter("writtenBytes", bytesWrittenDesc, "bytes"),
            factory.createLongCounter("flushes", flushesDesc, "ops"),
            factory.createLongCounter("flushTime", flushTimeDesc, "nanoseconds"),
            factory.createLongCounter("flushedBytes", bytesFlushedDesc, "bytes"),
            factory.createLongCounter("reads", readsDesc, "ops"),
            factory.createLongCounter("readTime", readTimeDesc, "nanoseconds"),
            factory.createLongCounter("readBytes", bytesReadDesc, "bytes"),
            factory.createIntGauge("recoveriesInProgress",
                "current number of persistent regions being recovered from disk", "ops"),
            factory.createLongCounter("recoveryTime", recoveryTimeDesc, "nanoseconds"),
            factory.createLongCounter("recoveredBytes", recoveredBytesDesc, "bytes"),
            factory.createLongCounter("recoveredEntryCreates",
                "The total number of entry create records processed while recovering oplog data.",
                "ops"),
            factory.createLongCounter("recoveredEntryUpdates",
                "The total number of entry update records processed while recovering oplog data.",
                "ops"),
            factory.createLongCounter("recoveredEntryDestroys",
                "The total number of entry destroy records processed while recovering oplog data.",
                "ops"),
            factory.createLongCounter("recoveredValuesSkippedDueToLRU",
                "The total number of entry values that did not need to be recovered due to the LRU.",
                "values"),

            factory.createLongCounter("recoveryRecordsSkipped",
                "The total number of oplog records skipped during recovery.", "ops"),

            factory.createIntCounter("oplogRecoveries", oplogRecoveriesDesc, "ops"),
            factory.createLongCounter("oplogRecoveryTime", oplogRecoveryTimeDesc, "nanoseconds"),
            factory.createLongCounter("oplogRecoveredBytes", oplogRecoveredBytesDesc, "bytes"),
            factory.createLongCounter("removes", removesDesc, "ops"),
            factory.createLongCounter("removeTime", removeTimeDesc, "nanoseconds"),
            factory.createIntGauge("queueSize", queueSizeDesc, "entries"),
            factory.createLongCounter("compactInserts",
                "Total number of times an oplog compact did a db insert", "inserts"),
            factory.createLongCounter("compactInsertTime",
                "Total amount of time, in nanoseconds, spent doing inserts during a compact",
                "nanoseconds"),
            factory.createLongCounter("compactUpdates",
                "Total number of times an oplog compact did an update", "updates"),
            factory.createLongCounter("compactUpdateTime",
                "Total amount of time, in nanoseconds, spent doing updates during a compact",
                "nanoseconds"),
            factory.createLongCounter("compactDeletes",
                "Total number of times an oplog compact did a delete", "deletes"),
            factory.createLongCounter("compactDeleteTime",
                "Total amount of time, in nanoseconds, spent doing deletes during a compact",
                "nanoseconds"),
            factory.createIntGauge("compactsInProgress",
                "current number of oplog compacts that are in progress", "compacts"),
            factory.createIntGauge("writesInProgress",
                "current number of oplog writes that are in progress", "writes"),
            factory.createIntGauge("flushesInProgress",
                "current number of oplog flushes that are in progress", "flushes"),
            factory.createLongCounter("compactTime",
                "Total amount of time, in nanoseconds, spent compacting oplogs", "nanoseconds"),
            factory.createIntCounter("compacts", "Total number of completed oplog compacts", "compacts"),
            factory.createIntGauge("openOplogs", "Current number of oplogs this disk store has open",
                "oplogs"),
            factory.createIntGauge("compactableOplogs", "Current number of oplogs ready to be compacted",
                "oplogs"),
            factory.createIntGauge("inactiveOplogs",
                "Current number of oplogs that are no longer being written but are not ready ready to compact",
                "oplogs"),
            factory.createLongCounter("oplogReads", "Total number of oplog reads", "reads"),
            factory.createLongCounter("oplogSeeks", "Total number of oplog seeks", "seeks"),
            factory.createIntGauge("uncreatedRecoveredRegions",
                "The current number of regions that have been recovered but have not yet been created.",
                "regions"),
            factory.createIntGauge("backupsInProgress", backupsInProgressDesc, "backups"),
            factory.createIntCounter("backupsCompleted", backupsCompletedDesc, "backups"),});

    // Initialize id fields
    writesId = type.nameToId("writes");
    writeTimeId = type.nameToId("writeTime");
    bytesWrittenId = type.nameToId("writtenBytes");
    flushesId = type.nameToId("flushes");
    flushTimeId = type.nameToId("flushTime");
    bytesFlushedId = type.nameToId("flushedBytes");
    readsId = type.nameToId("reads");
    readTimeId = type.nameToId("readTime");
    bytesReadId = type.nameToId("readBytes");
    recoveriesInProgressId = type.nameToId("recoveriesInProgress");
    recoveryTimeId = type.nameToId("recoveryTime");
    recoveredBytesId = type.nameToId("recoveredBytes");
    recoveredEntryCreatesId = type.nameToId("recoveredEntryCreates");
    recoveredEntryUpdatesId = type.nameToId("recoveredEntryUpdates");
    recoveredEntryDestroysId = type.nameToId("recoveredEntryDestroys");
    recoveredValuesSkippedDueToLRUId = type.nameToId("recoveredValuesSkippedDueToLRU");
    recoveryRecordsSkippedId = type.nameToId("recoveryRecordsSkipped");

    compactsInProgressId = type.nameToId("compactsInProgress");
    writesInProgressId = type.nameToId("writesInProgress");
    flushesInProgressId = type.nameToId("flushesInProgress");
    compactTimeId = type.nameToId("compactTime");
    compactsId = type.nameToId("compacts");
    oplogRecoveriesId = type.nameToId("oplogRecoveries");
    oplogRecoveryTimeId = type.nameToId("oplogRecoveryTime");
    oplogRecoveredBytesId = type.nameToId("oplogRecoveredBytes");
    removesId = type.nameToId("removes");
    removeTimeId = type.nameToId("removeTime");
    queueSizeId = type.nameToId("queueSize");

    compactDeletesId = type.nameToId("compactDeletes");
    compactDeleteTimeId = type.nameToId("compactDeleteTime");
    compactInsertsId = type.nameToId("compactInserts");
    compactInsertTimeId = type.nameToId("compactInsertTime");
    compactUpdatesId = type.nameToId("compactUpdates");
    compactUpdateTimeId = type.nameToId("compactUpdateTime");
    oplogReadsId = type.nameToId("oplogReads");
    oplogSeeksId = type.nameToId("oplogSeeks");

    openOplogsId = type.nameToId("openOplogs");
    inactiveOplogsId = type.nameToId("inactiveOplogs");
    compactableOplogsId = type.nameToId("compactableOplogs");
    uncreatedRecoveredRegionsId = type.nameToId("uncreatedRecoveredRegions");
    backupsInProgress = type.nameToId("backupsInProgress");
    backupsCompleted = type.nameToId("backupsCompleted");
  }

  ////////////////////// Instance Fields //////////////////////

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  /////////////////////// Constructors ///////////////////////

  /**
   * Creates a new <code>DiskStoreStatistics</code> for the given region.
   */
  public DiskStoreStatsImpl(StatisticsFactory factory, String name) {
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
   * Return the current number of entries in the async queue
   */
  @Override
  public long getQueueSize() {
    return this.stats.getInt(queueSizeId);
  }

  @Override
  public void setQueueSize(int value) {
    this.stats.setInt(queueSizeId, value);
  }

  @Override
  public void incQueueSize(int delta) {
    this.stats.incInt(queueSizeId, delta);
  }

  @Override
  public void incUncreatedRecoveredRegions(int delta) {
    this.stats.incInt(uncreatedRecoveredRegionsId, delta);
  }

  /**
   * Invoked before data is written to disk.
   *
   * @return The timestamp that marks the start of the operation
   *
   * @see DiskRegion#put
   */
  @Override
  public long startWrite() {
    this.stats.incInt(writesInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public long startFlush() {
    this.stats.incInt(flushesInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void incWrittenBytes(long bytesWritten, boolean async) {
    this.stats.incLong(async ? bytesFlushedId : bytesWrittenId, bytesWritten);
  }

  /**
   * Invoked after data has been written to disk
   *
   * @param start The time at which the write operation started
   */
  @Override
  public long endWrite(long start) {
    this.stats.incInt(writesInProgressId, -1);
    long end = System.nanoTime();
    this.stats.incLong(writesId, 1);
    this.stats.incLong(writeTimeId, end - start);
    return end;
  }

  @Override
  public void endFlush(long start) {
    this.stats.incInt(flushesInProgressId, -1);
    long end = System.nanoTime();
    this.stats.incLong(flushesId, 1);
    this.stats.incLong(flushTimeId, end - start);
  }

  @Override
  public long getFlushes() {
    return this.stats.getLong(flushesId);
  }

  /**
   * Invoked before data is read from disk.
   *
   * @return The timestamp that marks the start of the operation
   *
   * @see DiskRegion#get
   */
  @Override
  public long startRead() {
    return System.nanoTime();
  }

  /**
   * Invoked after data has been read from disk
   *
   * @param start The time at which the read operation started
   * @param bytesRead The number of bytes that were read
   */
  @Override
  public long endRead(long start, long bytesRead) {
    long end = System.nanoTime();
    this.stats.incLong(readsId, 1);
    this.stats.incLong(readTimeId, end - start);
    this.stats.incLong(bytesReadId, bytesRead);
    return end;
  }

  /**
   * Invoked before data is recovered from disk.
   *
   * @return The timestamp that marks the start of the operation
   *
   */
  @Override
  public long startRecovery() {
    this.stats.incInt(recoveriesInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public long startCompaction() {
    this.stats.incInt(compactsInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public long startOplogRead() {
    return System.nanoTime();
  }

  /**
   * Invoked after data has been recovered from disk
   *
   * @param start The time at which the recovery operation started
   * @param bytesRead The number of bytes that were recovered
   */
  @Override
  public void endRecovery(long start, long bytesRead) {
    this.stats.incInt(recoveriesInProgressId, -1);
    long end = System.nanoTime();
    this.stats.incLong(recoveryTimeId, end - start);
    this.stats.incLong(recoveredBytesId, bytesRead);
  }

  @Override
  public void endCompaction(long start) {
    this.stats.incInt(compactsInProgressId, -1);
    long end = System.nanoTime();
    this.stats.incInt(compactsId, 1);
    this.stats.incLong(compactTimeId, end - start);
  }

  @Override
  public void endOplogRead(long start, long bytesRead) {
    long end = System.nanoTime();
    this.stats.incInt(oplogRecoveriesId, 1);
    this.stats.incLong(oplogRecoveryTimeId, end - start);
    this.stats.incLong(oplogRecoveredBytesId, bytesRead);
  }

  @Override
  public void incRecoveredEntryCreates() {
    this.stats.incLong(recoveredEntryCreatesId, 1);
  }

  @Override
  public void incRecoveredEntryUpdates() {
    this.stats.incLong(recoveredEntryUpdatesId, 1);
  }

  @Override
  public void incRecoveredEntryDestroys() {
    this.stats.incLong(recoveredEntryDestroysId, 1);
  }

  @Override
  public void incRecoveryRecordsSkipped() {
    this.stats.incLong(recoveryRecordsSkippedId, 1);
  }

  @Override
  public void incRecoveredValuesSkippedDueToLRU() {
    this.stats.incLong(recoveredValuesSkippedDueToLRUId, 1);
  }

  /**
   * Invoked before data is removed from disk.
   *
   * @return The timestamp that marks the start of the operation
   *
   * @see DiskRegion#remove
   */
  @Override
  public long startRemove() {
    return System.nanoTime();
  }

  /**
   * Invoked after data has been removed from disk
   *
   * @param start The time at which the read operation started
   */
  @Override
  public long endRemove(long start) {
    long end = System.nanoTime();
    this.stats.incLong(removesId, 1);
    this.stats.incLong(removeTimeId, end - start);
    return end;
  }

  @Override
  public void incOplogReads() {
    this.stats.incLong(oplogReadsId, 1);
  }

  @Override
  public void incOplogSeeks() {
    this.stats.incLong(oplogSeeksId, 1);
  }

  @Override
  public void incInactiveOplogs(int delta) {
    this.stats.incInt(inactiveOplogsId, delta);
  }

  @Override
  public void incCompactableOplogs(int delta) {
    this.stats.incInt(compactableOplogsId, delta);
  }

  @Override
  public void endCompactionDeletes(int count, long delta) {
    this.stats.incLong(compactDeletesId, count);
    this.stats.incLong(compactDeleteTimeId, delta);
  }

  @Override
  public void endCompactionInsert(long start) {
    this.stats.incLong(compactInsertsId, 1);
    this.stats.incLong(compactInsertTimeId, getStatTime() - start);
  }

  @Override
  public void endCompactionUpdate(long start) {
    this.stats.incLong(compactUpdatesId, 1);
    this.stats.incLong(compactUpdateTimeId, getStatTime() - start);
  }

  @Override
  public long getStatTime() {
    return System.nanoTime();
  }

  @Override
  public void incOpenOplogs() {
    this.stats.incInt(openOplogsId, 1);
  }

  @Override
  public void decOpenOplogs() {
    this.stats.incInt(openOplogsId, -1);
  }

  @Override
  public void startBackup() {
    this.stats.incInt(backupsInProgress, 1);
  }

  @Override
  public void endBackup() {
    this.stats.incInt(backupsInProgress, -1);
    this.stats.incInt(backupsCompleted, 1);
  }

  @Override
  public Statistics getStats() {
    return stats;
  }
}
