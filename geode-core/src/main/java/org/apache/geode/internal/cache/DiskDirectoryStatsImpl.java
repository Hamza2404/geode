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
 * GemFire statistics about Disk Directories
 *
 *
 * @since GemFire 3.2
 */
public class DiskDirectoryStatsImpl implements DiskDirectoryStats, GFSStatsImplementor {

  private StatisticsType type;

  //////////////////// Statistic "Id" Fields ////////////////////

  private int diskSpaceId;
  private int maxSpaceId;
  private int volumeSizeId;
  private int volumeFreeSpaceId;
  private int volumeFreeSpaceChecksId;
  private int volumeFreeSpaceTimeId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    String statName = "DiskDirStatistics";
    String statDescription = "Statistics about a single disk directory for a region";

    final String diskSpaceDesc =
        "The total number of bytes currently being used on disk in this directory for oplog files.";
    final String maxSpaceDesc =
        "The configured maximum number of bytes allowed in this directory for oplog files. Note that some product configurations allow this maximum to be exceeded.";
    type = factory.createType(statName, statDescription,
        new StatisticDescriptor[] {factory.createLongGauge("diskSpace", diskSpaceDesc, "bytes"),
            factory.createLongGauge("maximumSpace", maxSpaceDesc, "bytes"),
            factory.createLongGauge("volumeSize", "The total size in bytes of the disk volume", "bytes"),
            factory.createLongGauge("volumeFreeSpace", "The total free space in bytes on the disk volume",
                "bytes"),
            factory.createLongCounter("volumeFreeSpaceChecks", "The total number of disk space checks",
                "checks"),
            factory.createLongCounter("volumeFreeSpaceTime", "The total time spent checking disk usage",
                "nanoseconds")});

    // Initialize id fields
    diskSpaceId = type.nameToId("diskSpace");
    maxSpaceId = type.nameToId("maximumSpace");
    volumeSizeId = type.nameToId("volumeSize");
    volumeFreeSpaceId = type.nameToId("volumeFreeSpace");
    volumeFreeSpaceChecksId = type.nameToId("volumeFreeSpaceChecks");
    volumeFreeSpaceTimeId = type.nameToId("volumeFreeSpaceTime");
  }

  ////////////////////// Instance Fields //////////////////////

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  /////////////////////// Constructors ///////////////////////

  /**
   * Creates a new <code>DiskRegionStatistics</code> for the given region.
   */
  public DiskDirectoryStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createStatistics(type, name);
  }

  ///////////////////// Instance Methods /////////////////////

  @Override
  public void close() {
    this.stats.close();
  }

  /**
   * Returns the current value of the "diskSpace" stat.
   */
  @Override
  public long getDiskSpace() {
    return this.stats.getLong(diskSpaceId);
  }

  @Override
  public void incDiskSpace(long delta) {
    this.stats.incLong(diskSpaceId, delta);
  }

  @Override
  public void setMaxSpace(long v) {
    this.stats.setLong(maxSpaceId, v);
  }

  @Override
  public void addVolumeCheck(long total, long free, long time) {
    stats.setLong(volumeSizeId, total);
    stats.setLong(volumeFreeSpaceId, free);
    stats.incLong(volumeFreeSpaceChecksId, 1);
    stats.incLong(volumeFreeSpaceTimeId, time);
  }

  @Override
  public Statistics getStats() {
    return stats;
  }
}
