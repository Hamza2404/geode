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
package org.apache.geode.cache.query.internal.index;

import org.apache.geode.internal.cache.CachePerfStatsImpl;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.cache.CachePerfStats;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * IndexStats tracks statistics about query index use.
 */
public class IndexStats {

  private StatisticsType type;

  private int numKeysId;
  private int numValuesId;
  private int numUpdatesId;
  private int numUsesId;
  private int updateTimeId;
  private int useTimeId;
  private int updatesInProgressId;
  private int usesInProgressId;
  private int readLockCountId;
  private int numMapIndexKeysId;
  private int numBucketIndexesId;

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  private void initializeStats(StatisticsFactory factory) {
    final String numKeysDesc = "Number of keys in this index";
    final String numValuesDesc = "Number of values in this index";
    final String numUpdatesDesc = "Number of updates that have completed on this index";
    final String numUsesDesc = "Number of times this index has been used while executing a query";
    final String updateTimeDesc = "Total time spent updating this index";

    type = factory.createType("IndexStats", "Statistics about a query index",
        new StatisticDescriptor[] {factory.createLongGauge("numKeys", numKeysDesc, "keys"),
            factory.createLongGauge("numValues", numValuesDesc, "values"),
            factory.createLongCounter("numUpdates", numUpdatesDesc, "operations"),
            factory.createLongCounter("numUses", numUsesDesc, "operations"),
            factory.createLongCounter("updateTime", updateTimeDesc, "nanoseconds"),
            factory.createLongCounter("useTime", "Total time spent using this index", "nanoseconds"),
            factory.createIntGauge("updatesInProgress", "Current number of updates in progress.",
                "updates"),
            factory.createIntGauge("usesInProgress", "Current number of uses in progress.", "uses"),
            factory.createIntGauge("readLockCount", "Current number of read locks taken.", "uses"),
            factory.createLongGauge("numMapIndexKeys", "Number of keys in this Map index", "keys"),
            factory.createIntGauge("numBucketIndexes",
                "Number of bucket indexes in the partitioned region", "indexes"),});

    // Initialize id fields
    numKeysId = type.nameToId("numKeys");
    numValuesId = type.nameToId("numValues");
    numUpdatesId = type.nameToId("numUpdates");
    numUsesId = type.nameToId("numUses");
    updateTimeId = type.nameToId("updateTime");
    updatesInProgressId = type.nameToId("updatesInProgress");
    usesInProgressId = type.nameToId("usesInProgress");
    useTimeId = type.nameToId("useTime");
    readLockCountId = type.nameToId("readLockCount");
    numMapIndexKeysId = type.nameToId("numMapIndexKeys");
    numBucketIndexesId = type.nameToId("numBucketIndexes");
  }

  /**
   * Creates a new <code>CachePerfStats</code> and registers itself with the given statistics
   * factory.
   */
  public IndexStats(StatisticsFactory factory, String indexName) {
    initializeStats(factory);
    stats = factory.createAtomicStatistics(type, indexName);
  }

  public long getNumberOfKeys() {
    return stats.getLong(numKeysId);
  }

  public long getNumberOfValues() {
    return stats.getLong(numValuesId);
  }

  public long getNumUpdates() {
    return stats.getLong(numUpdatesId);
  }

  public long getTotalUses() {
    return stats.getLong(numUsesId);
  }

  public long getTotalUpdateTime() {
    return CachePerfStatsImpl.enableClockStats ? stats.getLong(updateTimeId) : 0;
  }

  public long getUseTime() {
    return CachePerfStatsImpl.enableClockStats ? stats.getLong(useTimeId) : 0;
  }

  public int getReadLockCount() {
    return stats.getInt(readLockCountId);
  }

  public long getNumberOfMapIndexKeys() {
    return stats.getLong(numMapIndexKeysId);
  }

  public int getNumberOfBucketIndexes() {
    return stats.getInt(numBucketIndexesId);
  }

  public void incNumUpdates() {
    this.stats.incLong(numUpdatesId, 1);
  }

  public void incNumUpdates(int delta) {
    this.stats.incLong(numUpdatesId, delta);
  }

  public void incNumValues(int delta) {
    this.stats.incLong(numValuesId, delta);
  }

  public void updateNumKeys(long numKeys) {
    this.stats.setLong(numKeysId, numKeys);
  }

  public void incNumKeys(long numKeys) {
    this.stats.incLong(numKeysId, numKeys);
  }

  public void incUpdateTime(long delta) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(updateTimeId, delta);
    }
  }

  public void incNumUses() {
    this.stats.incLong(numUsesId, 1);
  }

  public void incUpdatesInProgress(int delta) {
    this.stats.incInt(updatesInProgressId, delta);
  }

  public void incUsesInProgress(int delta) {
    this.stats.incInt(usesInProgressId, delta);
  }

  public void incUseTime(long delta) {
    if (CachePerfStatsImpl.enableClockStats) {
      this.stats.incLong(useTimeId, delta);
    }
  }

  public void incReadLockCount(int delta) {
    this.stats.incInt(readLockCountId, delta);
  }

  public void incNumMapIndexKeys(long delta) {
    this.stats.incLong(numMapIndexKeysId, delta);
  }

  public void incNumBucketIndexes(int delta) {
    this.stats.incInt(numBucketIndexesId, delta);
  }

  /**
   * Closes these stats so that they can not longer be used. The stats are closed when the cache is
   * closed.
   *
   * @since GemFire 3.5
   */
  void close() {
    this.stats.close();
  }

  /**
   * Returns whether or not these stats have been closed
   *
   * @since GemFire 3.5
   */
  public boolean isClosed() {
    return this.stats.isClosed();
  }

  /**
   * Returns the Statistics instance that stores the cache perf stats.
   *
   * @since GemFire 3.5
   */
  public Statistics getStats() {
    return this.stats;
  }
}
