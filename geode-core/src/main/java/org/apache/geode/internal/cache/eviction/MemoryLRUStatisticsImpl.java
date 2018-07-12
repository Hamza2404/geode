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
package org.apache.geode.internal.cache.eviction;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

public class MemoryLRUStatisticsImpl implements MemoryLRUEvictionStats, GFSStatsImplementor {
  private StatisticsType statType;
  private int limitId;
  private int counterId;
  private int evictionsId;
  private int destroysId;
  private int evaluationsId;
  private int greedyReturnsId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    final String bytesAllowedDesc = "Number of total bytes allowed in this region.";
    final String byteCountDesc = "Number of bytes in region.";
    final String lruEvictionsDesc = "Number of total entry evictions triggered by LRU.";
    final String
        lruDestroysDesc =
        "Number of entries destroyed in the region through both destroy cache operations and eviction.";
    final String lruEvaluationsDesc = "Number of entries evaluated during LRU operations.";
    final String lruGreedyReturnsDesc = "Number of non-LRU entries evicted during LRU operations";

    statType = factory.createType("MemLRUStatistics", "Statistics relates to memory based eviction",
        new StatisticDescriptor[]{
            factory.createLongGauge("bytesAllowed", bytesAllowedDesc, "bytes"),
            factory.createLongGauge("byteCount", byteCountDesc, "bytes"),
            factory.createLongCounter("lruEvictions", lruEvictionsDesc, "entries"),
            factory.createLongCounter("lruDestroys", lruDestroysDesc, "entries"),
            factory.createLongCounter("lruEvaluations", lruEvaluationsDesc, "entries"),
            factory.createLongCounter("lruGreedyReturns", lruGreedyReturnsDesc, "entries")});

    limitId = statType.nameToId("bytesAllowed");
    counterId = statType.nameToId("byteCount");
    evictionsId = statType.nameToId("lruEvictions");
    destroysId = statType.nameToId("lruDestroys");
    evaluationsId = statType.nameToId("lruEvaluations");
    greedyReturnsId = statType.nameToId("lruGreedyReturns");
  }

  private final Statistics stats;

  public MemoryLRUStatisticsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(statType, "MemLRUStatistics-" + name);
  }

  @Override
  public Statistics getStatistics() {
    return this.stats;
  }

  @Override
  public void close() {
    this.stats.close();
  }

  @Override
  public void incEvictions() {
    this.stats.incLong(evictionsId, 1);
  }

  @Override
  public void updateCounter(long delta) {
    this.stats.incLong(counterId, delta);
  }

  @Override
  public void incDestroys() {
    this.stats.incLong(destroysId, 1);
  }

  @Override
  public void setLimit(long newValue) {
    this.stats.setLong(limitId, newValue);
  }

  @Override
  public void setCounter(long newValue) {
    this.stats.setLong(counterId, newValue);
  }

  @Override
  public void incEvaluations(long delta) {
    this.stats.incLong(evaluationsId, delta);
  }

  @Override
  public void incGreedyReturns(long delta) {
    this.stats.incLong(greedyReturnsId, delta);
  }

}
