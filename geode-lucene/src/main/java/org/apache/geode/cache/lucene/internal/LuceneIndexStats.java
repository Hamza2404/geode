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
package org.apache.geode.cache.lucene.internal;


import java.util.function.IntSupplier;

import org.apache.geode.internal.CopyOnWriteHashSet;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;

public class LuceneIndexStats {
  // statistics type
  private StatisticsType statsType;
  private static final String statsTypeName = "LuceneIndexStats";
  private static final String statsTypeDescription = "Statistics about lucene indexes";

  private int queryExecutionsId;
  private int queryExecutionTimeId;
  private int queryExecutionsInProgressId;
  private int queryExecutionTotalHitsId;
  private int repositoryQueryExecutionsId;
  private int repositoryQueryExecutionTimeId;
  private int repositoryQueryExecutionsInProgressId;
  private int repositoryQueryExecutionTotalHitsId;
  private int updatesId;
  private int updateTimeId;
  private int updatesInProgressId;
  private int commitsId;
  private int commitTimeId;
  private int commitsInProgressId;
  private int documentsId;
  private int failedEntriesId;

  private final Statistics stats;
  private final CopyOnWriteHashSet<IntSupplier> documentsSuppliers = new CopyOnWriteHashSet<>();

  private void initializeStats(StatisticsFactory factory) {
    statsType = factory.createType(statsTypeName, statsTypeDescription, new StatisticDescriptor[] {
        factory.createIntCounter("queryExecutions", "Number of lucene queries executed on this member",
            "operations"),
        factory.createLongCounter("queryExecutionTime", "Amount of time spent executing lucene queries",
            "nanoseconds"),
        factory.createIntGauge("queryExecutionsInProgress",
            "Number of query executions currently in progress", "operations"),
        factory.createLongCounter("queryExecutionTotalHits",
            "Total number of documents returned by query executions", "entries"),
        factory.createIntCounter("repositoryQueryExecutions",
            "Number of lucene repository queries executed on this member", "operations"),
        factory.createLongCounter("repositoryQueryExecutionTime",
            "Amount of time spent executing lucene repository queries", "nanoseconds"),
        factory.createIntGauge("repositoryQueryExecutionsInProgress",
            "Number of repository query executions currently in progress", "operations"),
        factory.createLongCounter("repositoryQueryExecutionTotalHits",
            "Total number of documents returned by repository query executions", "entries"),
        factory.createIntCounter("updates",
            "Number of lucene index documents added/removed on this member", "operations"),
        factory.createLongCounter("updateTime",
            "Amount of time spent adding or removing documents from the index", "nanoseconds"),
        factory.createIntGauge("updatesInProgress", "Number of index updates in progress", "operations"),
        factory.createIntCounter("failedEntries", "Number of entries failed to index", "entries"),
        factory.createIntCounter("commits", "Number of lucene index commits on this member",
            "operations"),
        factory.createLongCounter("commitTime", "Amount of time spent in lucene index commits",
            "nanoseconds"),
        factory.createIntGauge("commitsInProgress", "Number of lucene index commits in progress",
            "operations"),
        factory.createIntGauge("documents", "Number of documents in the index", "documents"),});

    queryExecutionsId = statsType.nameToId("queryExecutions");
    queryExecutionTimeId = statsType.nameToId("queryExecutionTime");
    queryExecutionsInProgressId = statsType.nameToId("queryExecutionsInProgress");
    queryExecutionTotalHitsId = statsType.nameToId("queryExecutionTotalHits");
    repositoryQueryExecutionsId = statsType.nameToId("repositoryQueryExecutions");
    repositoryQueryExecutionTimeId = statsType.nameToId("repositoryQueryExecutionTime");
    repositoryQueryExecutionsInProgressId =
        statsType.nameToId("repositoryQueryExecutionsInProgress");
    repositoryQueryExecutionTotalHitsId = statsType.nameToId("repositoryQueryExecutionTotalHits");
    updatesId = statsType.nameToId("updates");
    updateTimeId = statsType.nameToId("updateTime");
    updatesInProgressId = statsType.nameToId("updatesInProgress");
    commitsId = statsType.nameToId("commits");
    commitTimeId = statsType.nameToId("commitTime");
    commitsInProgressId = statsType.nameToId("commitsInProgress");
    documentsId = statsType.nameToId("documents");
    failedEntriesId = statsType.nameToId("failedEntries");
  }

  public LuceneIndexStats(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(statsType, name);
    stats.setIntSupplier(documentsId, this::computeDocumentCount);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  public long startRepositoryQuery() {
    stats.incInt(repositoryQueryExecutionsInProgressId, 1);
    return System.nanoTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  public void endRepositoryQuery(long start, final int totalHits) {
    stats.incLong(repositoryQueryExecutionTimeId, System.nanoTime() - start);
    stats.incInt(repositoryQueryExecutionsInProgressId, -1);
    stats.incInt(repositoryQueryExecutionsId, 1);
    stats.incLong(repositoryQueryExecutionTotalHitsId, totalHits);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  public long startQuery() {
    stats.incInt(queryExecutionsInProgressId, 1);
    return System.nanoTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  public void endQuery(long start, final int totalHits) {
    stats.incLong(queryExecutionTimeId, System.nanoTime() - start);
    stats.incInt(queryExecutionsInProgressId, -1);
    stats.incLong(queryExecutionTotalHitsId, totalHits);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  public long startUpdate() {
    stats.incInt(updatesInProgressId, 1);
    return System.nanoTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  public void endUpdate(long start) {
    stats.incLong(updateTimeId, System.nanoTime() - start);
    stats.incInt(updatesInProgressId, -1);
    stats.incInt(updatesId, 1);
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  public long startCommit() {
    stats.incInt(commitsInProgressId, 1);
    return System.nanoTime();
  }

  /**
   * @param start the timestamp taken when the operation started
   */
  public void endCommit(long start) {
    stats.incLong(commitTimeId, System.nanoTime() - start);
    stats.incInt(commitsInProgressId, -1);
    stats.incInt(commitsId, 1);
  }

  public void incFailedEntries() {
    stats.incInt(failedEntriesId, 1);
  }

  public int getFailedEntries() {
    return stats.getInt(failedEntriesId);
  }

  public void addDocumentsSupplier(IntSupplier supplier) {
    this.documentsSuppliers.add(supplier);
  }

  public void removeDocumentsSupplier(IntSupplier supplier) {
    this.documentsSuppliers.remove(supplier);
  }

  public int getDocuments() {
    return this.stats.getInt(documentsId);
  }

  private int computeDocumentCount() {
    return this.documentsSuppliers.stream().mapToInt(IntSupplier::getAsInt).sum();
  }

  public int getQueryExecutions() {
    return stats.getInt(queryExecutionsId);
  }

  public long getQueryExecutionTime() {
    return stats.getLong(queryExecutionTimeId);
  }

  public int getQueryExecutionsInProgress() {
    return stats.getInt(queryExecutionsInProgressId);
  }

  public long getQueryExecutionTotalHits() {
    return stats.getLong(queryExecutionTotalHitsId);
  }

  public int getUpdates() {
    return stats.getInt(updatesId);
  }

  public long getUpdateTime() {
    return stats.getLong(updateTimeId);
  }

  public int getUpdatesInProgress() {
    return stats.getInt(updatesInProgressId);
  }

  public int getCommits() {
    return stats.getInt(commitsId);
  }

  public long getCommitTime() {
    return stats.getLong(commitTimeId);
  }

  public int getCommitsInProgress() {
    return stats.getInt(commitsInProgressId);
  }

  public Statistics getStats() {
    return this.stats;
  }

  public void incNumberOfQueryExecuted() {
    stats.incInt(queryExecutionsId, 1);
  }
}
