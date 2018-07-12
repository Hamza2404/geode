/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
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
package org.apache.geode.cache.lucene.internal.filesystem;

import java.util.function.LongSupplier;

import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

public class FileSystemStats {
  private StatisticsType statsType;
  private static final String statsTypeName = "FileSystemStats";
  private static final String statsTypeDescription =
      "Statistics about in memory file system implementation";

  private final Statistics stats;

  private int readBytesId;
  private int writtenBytesId;
  private int fileCreatesId;
  private int temporaryFileCreatesId;
  private int fileDeletesId;
  private int fileRenamesId;
  private int bytesId;

  private void initializeStats(StatisticsFactory factory) {
    statsType = factory.createType(statsTypeName, statsTypeDescription,
        new StatisticDescriptor[] {
            factory.createLongCounter("readBytes", "Number of bytes written", "bytes"),
            factory.createLongCounter("writtenBytes", "Number of bytes read", "bytes"),
            factory.createIntCounter("fileCreates", "Number of files created", "files"),
            factory.createIntCounter("temporaryFileCreates", "Number of temporary files created",
                "files"),
            factory.createIntCounter("fileDeletes", "Number of files deleted", "files"),
            factory.createIntCounter("fileRenames", "Number of files renamed", "files"),
            factory.createIntGauge("files", "Number of files on this member", "files"),
            factory.createIntGauge("chunks", "Number of file chunks on this member", "chunks"),
            factory.createLongGauge("bytes", "Number of bytes on this member", "bytes"),});

    readBytesId = statsType.nameToId("readBytes");
    writtenBytesId = statsType.nameToId("writtenBytes");
    fileCreatesId = statsType.nameToId("fileCreates");
    temporaryFileCreatesId = statsType.nameToId("temporaryFileCreates");
    fileDeletesId = statsType.nameToId("fileDeletes");
    fileRenamesId = statsType.nameToId("fileRenames");
    bytesId = statsType.nameToId("bytes");
  }

  public FileSystemStats(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(statsType, name);
  }

  public void incReadBytes(int delta) {
    stats.incLong(readBytesId, delta);
  }

  public void incWrittenBytes(int delta) {
    stats.incLong(writtenBytesId, delta);
  }

  public void incFileCreates(final int delta) {
    stats.incInt(fileCreatesId, delta);
  }

  public void incTemporaryFileCreates(final int delta) {
    stats.incInt(temporaryFileCreatesId, delta);
  }

  public void incFileDeletes(final int delta) {
    stats.incInt(fileDeletesId, delta);
  }

  public void incFileRenames(final int delta) {
    stats.incInt(fileRenamesId, delta);
  }

  public void setBytesSupplier(LongSupplier supplier) {
    stats.setLongSupplier(bytesId, supplier);
  }

  public long getBytes() {
    return stats.getLong(bytesId);
  }
}
