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
package org.apache.geode.internal.cache.tier.sockets;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.distributed.internal.DistributionStats;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * GemFire statistics about a {@link CacheClientNotifier}. These statistics are related to cache
 * server client notifications.
 *
 *
 * @since GemFire 4.1.2
 */
public class CacheClientNotifierStatsImpl implements CacheClientNotifierStats, GFSStatsImplementor {

  private StatisticsType _type;

  //////////////////// Statistic "Id" Fields ////////////////////

  private static final String EVENTS = "events";
  private static final String EVENT_PROCESSING_TIME = "eventProcessingTime";
  private static final String CLIENT_REGISTRATIONS = "clientRegistrations";
  private static final String DURABLE_RECONNECTION_COUNT = "durableReconnectionCount";
  private static final String QUEUE_DROPPED_COUNT = "queueDroppedCount";
  private static final String EVENTS_ENQUEUED_WHILE_CLIENT_AWAY_COUNT =
      "eventsEnqueuedWhileClientAwayCount";
  private static final String CLIENT_REGISTRATION_TIME = "clientRegistrationTime";
  private static final String CQ_PROCESSING_TIME = "cqProcessingTime";
  private static final String COMPILED_QUERY_COUNT = "compiledQueryCount";
  private static final String COMPILED_QUERY_USED_COUNT = "compiledQueryUsedCount";

  private int _eventsId;
  private int _eventProcessingTimeId;
  private int _clientRegistrationsId;
  private int _clientRegistrationTimeId;

  // Register and Unregister stats.
  private int _clientHealthMonitorRegisterId;
  private int _durableReconnectionCount;
  private int _queueDroppedCount;
  private int _eventEnqueuedWhileClientAwayCount;
  private int _clientHealthMonitorUnRegisterId;

  // CQ process stat.
  private int _cqProcessingTimeId;

  // Compiled query count.
  private int _compiledQueryCount;

  private int _compiledQueryUsedCount;


  @Override
  public void initializeStats(StatisticsFactory factory) {
    String statName = "CacheClientNotifierStatistics";

    _type = factory.createType(statName, statName, new StatisticDescriptor[] {factory.createIntCounter(EVENTS,
        "Number of events processed by the cache client notifier.", "operations"),

        factory.createLongCounter(EVENT_PROCESSING_TIME,
            "Total time spent by the cache client notifier processing events.", "nanoseconds"),

        factory.createIntCounter(CLIENT_REGISTRATIONS,
            "Number of clients that have registered for updates.", "operations"),

        factory.createLongCounter(CLIENT_REGISTRATION_TIME,
            "Total time spent doing client registrations.", "nanoseconds"),

        factory.createIntGauge("clientHealthMonitorRegister", "Number of client Register.", "registered"),

        factory.createIntGauge("clientHealthMonitorUnRegister", "Number of client UnRegister.",
            "unregistered"),

        factory.createIntCounter(DURABLE_RECONNECTION_COUNT,
            "Number of times the same durable client connects to the server", "operations"),

        factory.createIntCounter(QUEUE_DROPPED_COUNT,
            "Number of times client queue for a particular durable client is dropped",
            "operations"),

        factory.createIntCounter(EVENTS_ENQUEUED_WHILE_CLIENT_AWAY_COUNT,
            "Number of events enqueued in queue for a durable client ", "operations"),

        factory.createLongCounter(CQ_PROCESSING_TIME,
            "Total time spent by the cache client notifier processing cqs.", "nanoseconds"),

        factory.createLongGauge(COMPILED_QUERY_COUNT, "Number of compiled queries maintained.",
            "maintained"),

        factory.createLongCounter(COMPILED_QUERY_USED_COUNT, "Number of times compiled queries are used.",
            "used"),

    });

    // Initialize id fields
    _eventsId = _type.nameToId(EVENTS);
    _eventProcessingTimeId = _type.nameToId(EVENT_PROCESSING_TIME);
    _clientRegistrationsId = _type.nameToId(CLIENT_REGISTRATIONS);
    _clientRegistrationTimeId = _type.nameToId(CLIENT_REGISTRATION_TIME);

    _clientHealthMonitorRegisterId = _type.nameToId("clientHealthMonitorRegister");
    _clientHealthMonitorUnRegisterId = _type.nameToId("clientHealthMonitorUnRegister");


    _durableReconnectionCount = _type.nameToId(DURABLE_RECONNECTION_COUNT);
    _queueDroppedCount = _type.nameToId(QUEUE_DROPPED_COUNT);
    _eventEnqueuedWhileClientAwayCount = _type.nameToId(EVENTS_ENQUEUED_WHILE_CLIENT_AWAY_COUNT);

    _cqProcessingTimeId = _type.nameToId(CQ_PROCESSING_TIME);
    _compiledQueryCount = _type.nameToId(COMPILED_QUERY_COUNT);
    _compiledQueryUsedCount = _type.nameToId(COMPILED_QUERY_USED_COUNT);
  }

  ////////////////////// Instance Fields //////////////////////

  /** The Statistics object that we delegate most behavior to */
  private final Statistics _stats;

  /////////////////////// Constructors ///////////////////////

  /**
   * Creates a new <code>CacheClientNotifierStats</code>.
   */
  public CacheClientNotifierStatsImpl(StatisticsFactory factory) {
    initializeStats(factory);
    this._stats = factory.createAtomicStatistics(_type, "cacheClientNotifierStats");
  }

  ///////////////////// Instance Methods /////////////////////

  @Override
  public void close() {
    this._stats.close();
  }

  /**
   * Returns the current value of the "events" stat.
   */
  @Override
  public int getEvents() {
    return this._stats.getInt(_eventsId);
  }

  /**
   * Returns the current value of the "eventProcessingTime" stat.
   */
  @Override
  public long getEventProcessingTime() {
    return this._stats.getLong(_eventProcessingTimeId);
  }

  @Override
  public long startTime() {
    return System.nanoTime();
  }

  @Override
  public void endEvent(long start) {
    long ts = System.nanoTime();
    // Increment number of notifications
    this._stats.incInt(_eventsId, 1);

    if (start != 0L && ts != 0L) {
      // Increment notification time
      long elapsed = ts - start;
      this._stats.incLong(_eventProcessingTimeId, elapsed);
    }
  }

  @Override
  public void endClientRegistration(long start) {
    long ts = System.nanoTime();

    // Increment number of notifications
    this._stats.incInt(_clientRegistrationsId, 1);

    if (start != 0L && ts != 0L) {
      // Increment notification time
      long elapsed = ts - start;
      this._stats.incLong(_clientRegistrationTimeId, elapsed);
    }
  }

  @Override
  public void endCqProcessing(long start) {
    long ts = System.nanoTime();
    if (start != 0L && ts != 0L) {
      this._stats.incLong(_cqProcessingTimeId, (ts - start));
    }
  }

  @Override
  public void incClientRegisterRequests() {
    this._stats.incInt(_clientHealthMonitorRegisterId, 1);
  }

  @Override
  public int getClientRegisterRequests() {
    return this._stats.getInt(_clientHealthMonitorRegisterId);
  }

  @Override
  public int get_durableReconnectionCount() {
    return this._stats.getInt(_durableReconnectionCount);
  }

  @Override
  public int get_queueDroppedCount() {
    return this._stats.getInt(_queueDroppedCount);
  }

  @Override
  public int get_eventEnqueuedWhileClientAwayCount() {
    return this._stats.getInt(_eventEnqueuedWhileClientAwayCount);
  }

  @Override
  public long getCqProcessingTime() {
    return this._stats.getLong(_cqProcessingTimeId);
  }

  @Override
  public long getCompiledQueryCount() {
    return this._stats.getLong(_compiledQueryCount);
  }

  @Override
  public long getCompiledQueryUsedCount() {
    return this._stats.getLong(_compiledQueryUsedCount);
  }

  @Override
  public void incDurableReconnectionCount() {
    this._stats.incInt(_durableReconnectionCount, 1);
  }

  @Override
  public void incQueueDroppedCount() {
    this._stats.incInt(_queueDroppedCount, 1);
  }

  @Override
  public void incEventEnqueuedWhileClientAwayCount() {
    this._stats.incInt(_eventEnqueuedWhileClientAwayCount, 1);
  }

  @Override
  public void incClientUnRegisterRequests() {
    this._stats.incInt(_clientHealthMonitorUnRegisterId, 1);
  }

  @Override
  public void incCompiledQueryCount(long count) {
    this._stats.incLong(_compiledQueryCount, count);
  }

  @Override
  public void incCompiledQueryUsedCount(long count) {
    this._stats.incLong(_compiledQueryUsedCount, count);
  }

  @Override
  public int getClientUnRegisterRequests() {
    return this._stats.getInt(_clientHealthMonitorUnRegisterId);
  }

}
