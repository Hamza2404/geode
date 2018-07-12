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

import static org.apache.geode.distributed.ConfigurationProperties.LOCATORS;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.distributed.internal.DistributionStats;

/**
 * GemFire statistics about a Pool
 * @since GemFire 5.7
 */
public class PoolStatsImpl implements PoolStats, GFSStatsImplementor {

  private StatisticsType _type;

  //////////////////// Statistic "Id" Fields ////////////////////

  private static final String INITIAL_CONTACTS = "initialContactCount"; // gauge
  private static final String KNOWN_LOCATORS = LOCATORS; // gauge
  private static final String REQUESTS_TO_LOCATOR = "locatorRequests"; // counter
  private static final String RESPONSES_FROM_LOCATOR = "locatorResponses"; // counter
  private static final String ENDPOINTS_KNOWN = "servers"; // gauge
  private static final String SUBSCRIPTION_SERVERS = "subscriptionServers"; // gauge

  private int _INITIAL_CONTACTS;
  private int _KNOWN_LOCATORS;
  private int _REQUESTS_TO_LOCATOR;
  private int _RESPONSES_FROM_LOCATOR;
  private int _ENDPOINTS_KNOWN;
  private int _SUBSCRIPTION_SERVERS;
  private int _PREFILL_CONNECT;
  private int _LOAD_CONDITIONING_CHECK;
  private int _LOAD_CONDITIONING_EXTENSIONS;
  private int _IDLE_CHECK;
  private int _LOAD_CONDITIONING_CONNECT;
  private int _LOAD_CONDITIONING_DISCONNECT;
  private int _LOAD_CONDITIONING_REPLACE_TIMEOUT;
  private int _IDLE_EXPIRE;
  private int _CONNECTION_WAIT_IN_PROGRESS;
  private int _CONNECTION_WAITS;
  private int _CONNECTION_WAIT_TIME;
  private int connectionsId;
  // private int conCountId;
  private int poolConnectionsId;
  private int connectsId;
  private int disconnectsId;
  private int clientOpInProgressId;
  private int clientOpSendInProgressId;
  private int clientOpSendId;
  private int clientOpSendFailedId;
  private int clientOpSendDurationId;
  private int clientOpId;
  private int clientOpTimedOutId;
  private int clientOpFailedId;
  private int clientOpDurationId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    String statName = "PoolStats";

    _type = factory.createType(statName, statName,
        new StatisticDescriptor[]{
            factory.createIntGauge(INITIAL_CONTACTS, "Number of contacts initially by user",
                "contacts"),
            factory.createIntGauge(KNOWN_LOCATORS, "Current number of locators discovered",
                LOCATORS),
            factory.createIntGauge(ENDPOINTS_KNOWN, "Current number of servers discovered",
                "servers"),
            factory.createIntGauge(SUBSCRIPTION_SERVERS,
                "Number of servers hosting this clients subscriptions", "servers"),
            factory.createLongCounter(REQUESTS_TO_LOCATOR,
                "Number of requests from this connection pool to a locator", "requests"),
            factory.createLongCounter(RESPONSES_FROM_LOCATOR,
                "Number of responses from the locator to this connection pool", "responses"),

            factory.createIntGauge("connections", "Current number of connections", "connections"),
            // factory.createIntGauge("conCount", "Current number of connections", "connections"),
            factory.createIntGauge("poolConnections", "Current number of pool connections",
                "connections"),
            factory.createIntCounter("connects",
                "Total number of times a connection has been created.",
                "connects"),
            factory.createIntCounter("disconnects",
                "Total number of times a connection has been destroyed.", "disconnects"),
            factory.createIntCounter("minPoolSizeConnects",
                "Total number of connects done to maintain minimum pool size.", "connects"),
            factory.createIntCounter("loadConditioningConnects",
                "Total number of connects done due to load conditioning.", "connects"),
            factory.createIntCounter("loadConditioningReplaceTimeouts",
                "Total number of times a load conditioning connect was done but was not used.",
                "timeouts"),
            factory.createIntCounter("idleDisconnects",
                "Total number of disconnects done due to idle expiration.", "disconnects"),
            factory.createIntCounter("loadConditioningDisconnects",
                "Total number of disconnects done due to load conditioning expiration.",
                "disconnects"),
            factory.createIntCounter("idleChecks",
                "Total number of checks done for idle expiration.",
                "checks"),
            factory.createIntCounter("loadConditioningChecks",
                "Total number of checks done for load conditioning expiration.", "checks"),
            factory.createIntCounter("loadConditioningExtensions",
                "Total number of times a connection's load conditioning has been extended because the servers are still balanced.",
                "extensions"),
            factory.createIntGauge("connectionWaitsInProgress",
                "Current number of threads waiting for a connection", "threads"),
            factory.createIntCounter("connectionWaits",
                "Total number of times a thread completed waiting for a connection (by timing out or by getting a connection).",
                "waits"),
            factory.createLongCounter("connectionWaitTime",
                "Total number of nanoseconds spent waiting for a connection.", "nanoseconds"),
            factory.createIntGauge("clientOpsInProgress",
                "Current number of clientOps being executed",
                "clientOps"),
            factory.createIntGauge("clientOpSendsInProgress",
                "Current number of clientOp sends being executed", "sends"),
            factory.createIntCounter("clientOpSends",
                "Total number of clientOp sends that have completed successfully", "sends"),
            factory.createIntCounter("clientOpSendFailures",
                "Total number of clientOp sends that have failed", "sends"),
            factory.createIntCounter("clientOps",
                "Total number of clientOps completed successfully",
                "clientOps"),
            factory.createIntCounter("clientOpFailures",
                "Total number of clientOp attempts that have failed", "clientOps"),
            factory.createIntCounter("clientOpTimeouts",
                "Total number of clientOp attempts that have timed out", "clientOps"),
            factory.createLongCounter("clientOpSendTime",
                "Total amount of time, in nanoseconds spent doing clientOp sends", "nanoseconds"),
            factory.createLongCounter("clientOpTime",
                "Total amount of time, in nanoseconds spent doing clientOps", "nanoseconds"),});

    // Initialize id fields
    _INITIAL_CONTACTS = _type.nameToId(INITIAL_CONTACTS);
    _KNOWN_LOCATORS = _type.nameToId(KNOWN_LOCATORS);
    _REQUESTS_TO_LOCATOR = _type.nameToId(REQUESTS_TO_LOCATOR);
    _RESPONSES_FROM_LOCATOR = _type.nameToId(RESPONSES_FROM_LOCATOR);
    _ENDPOINTS_KNOWN = _type.nameToId(ENDPOINTS_KNOWN);
    _SUBSCRIPTION_SERVERS = _type.nameToId(SUBSCRIPTION_SERVERS);
    _PREFILL_CONNECT = _type.nameToId("minPoolSizeConnects");
    _LOAD_CONDITIONING_CHECK = _type.nameToId("loadConditioningChecks");
    _LOAD_CONDITIONING_EXTENSIONS = _type.nameToId("loadConditioningExtensions");
    _IDLE_CHECK = _type.nameToId("idleChecks");
    _LOAD_CONDITIONING_CONNECT = _type.nameToId("loadConditioningConnects");
    _LOAD_CONDITIONING_REPLACE_TIMEOUT = _type.nameToId("loadConditioningReplaceTimeouts");
    _LOAD_CONDITIONING_DISCONNECT = _type.nameToId("loadConditioningDisconnects");
    _IDLE_EXPIRE = _type.nameToId("idleDisconnects");
    _CONNECTION_WAIT_IN_PROGRESS = _type.nameToId("connectionWaitsInProgress");
    _CONNECTION_WAITS = _type.nameToId("connectionWaits");
    _CONNECTION_WAIT_TIME = _type.nameToId("connectionWaitTime");

    connectionsId = _type.nameToId("connections");
    // conCountId = _type.nameToId("conCount");
    poolConnectionsId = _type.nameToId("poolConnections");
    connectsId = _type.nameToId("connects");
    disconnectsId = _type.nameToId("disconnects");

    clientOpInProgressId = _type.nameToId("clientOpsInProgress");
    clientOpSendInProgressId = _type.nameToId("clientOpSendsInProgress");
    clientOpSendId = _type.nameToId("clientOpSends");
    clientOpSendFailedId = _type.nameToId("clientOpSendFailures");
    clientOpSendDurationId = _type.nameToId("clientOpSendTime");
    clientOpId = _type.nameToId("clientOps");
    clientOpTimedOutId = _type.nameToId("clientOpTimeouts");
    clientOpFailedId = _type.nameToId("clientOpFailures");
    clientOpDurationId = _type.nameToId("clientOpTime");
  }

  ////////////////////// Instance Fields //////////////////////

  /**
   * The Statistics object that we delegate most behavior to
   */
  private final Statistics _stats;

  /////////////////////// Constructors ///////////////////////

  public PoolStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this._stats = factory.createAtomicStatistics(_type, name);
  }

  ///////////////////// Instance Methods /////////////////////

  @Override
  public void close() {
    this._stats.close();
  }

  @Override
  public long startTime() {
    return System.nanoTime();
  }

  @Override
  public void setInitialContacts(int ic) {
    this._stats.setInt(_INITIAL_CONTACTS, ic);
  }

  @Override
  public void setServerCount(int sc) {
    this._stats.setInt(_ENDPOINTS_KNOWN, sc);
  }

  @Override
  public void setSubscriptionCount(int qc) {
    this._stats.setInt(_SUBSCRIPTION_SERVERS, qc);
  }

  @Override
  public void setLocatorCount(int lc) {
    this._stats.setInt(_KNOWN_LOCATORS, lc);
  }

  @Override
  public long getLocatorRequests() {
    return this._stats.getLong(_REQUESTS_TO_LOCATOR);
  }

  @Override
  public void incLocatorRequests() {
    this._stats.incLong(_REQUESTS_TO_LOCATOR, 1);
  }

  @Override
  public void incLocatorResponses() {
    this._stats.incLong(_RESPONSES_FROM_LOCATOR, 1);
  }

  @Override
  public void setLocatorRequests(long rl) {
    this._stats.setLong(_REQUESTS_TO_LOCATOR, rl);
  }

  @Override
  public void setLocatorResponses(long rl) {
    this._stats.setLong(_RESPONSES_FROM_LOCATOR, rl);
  }

  // public void incConCount(int delta) {
  // this._stats.incInt(conCountId, delta);
  // }
  @Override
  public void incConnections(int delta) {
    this._stats.incInt(connectionsId, delta);
    if (delta > 0) {
      this._stats.incInt(connectsId, delta);
    } else if (delta < 0) {
      this._stats.incInt(disconnectsId, -delta);
    }
  }

  @Override
  public void incPoolConnections(int delta) {
    this._stats.incInt(poolConnectionsId, delta);
  }

  @Override
  public int getPoolConnections() {
    return this._stats.getInt(poolConnectionsId);
  }

  @Override
  public int getConnects() {
    return this._stats.getInt(connectsId);
  }

  @Override
  public int getDisconnects() {
    return this._stats.getInt(disconnectsId);
  }

  @Override
  public void incPrefillConnect() {
    this._stats.incInt(_PREFILL_CONNECT, 1);
  }

  @Override
  public int getLoadConditioningCheck() {
    return this._stats.getInt(_LOAD_CONDITIONING_CHECK);
  }

  @Override
  public void incLoadConditioningCheck() {
    this._stats.incInt(_LOAD_CONDITIONING_CHECK, 1);
  }

  @Override
  public int getLoadConditioningExtensions() {
    return this._stats.getInt(_LOAD_CONDITIONING_EXTENSIONS);
  }

  @Override
  public void incLoadConditioningExtensions() {
    this._stats.incInt(_LOAD_CONDITIONING_EXTENSIONS, 1);
  }

  @Override
  public void incIdleCheck() {
    this._stats.incInt(_IDLE_CHECK, 1);
  }

  @Override
  public int getLoadConditioningConnect() {
    return this._stats.getInt(_LOAD_CONDITIONING_CONNECT);
  }

  @Override
  public void incLoadConditioningConnect() {
    this._stats.incInt(_LOAD_CONDITIONING_CONNECT, 1);
  }

  @Override
  public int getLoadConditioningReplaceTimeouts() {
    return this._stats.getInt(_LOAD_CONDITIONING_REPLACE_TIMEOUT);
  }

  @Override
  public void incLoadConditioningReplaceTimeouts() {
    this._stats.incInt(_LOAD_CONDITIONING_REPLACE_TIMEOUT, 1);
  }

  @Override
  public int getLoadConditioningDisconnect() {
    return this._stats.getInt(_LOAD_CONDITIONING_DISCONNECT);
  }

  @Override
  public void incLoadConditioningDisconnect() {
    this._stats.incInt(_LOAD_CONDITIONING_DISCONNECT, 1);
  }

  @Override
  public int getIdleExpire() {
    return this._stats.getInt(_IDLE_EXPIRE);
  }

  @Override
  public void incIdleExpire(int delta) {
    this._stats.incInt(_IDLE_EXPIRE, delta);
  }

  @Override
  public long beginConnectionWait() {
    this._stats.incInt(_CONNECTION_WAIT_IN_PROGRESS, 1);
    return PoolStats.getStatTime();
  }

  @Override
  public void endConnectionWait(long start) {
    long duration = PoolStats.getStatTime() - start;
    this._stats.incInt(_CONNECTION_WAIT_IN_PROGRESS, -1);
    this._stats.incInt(_CONNECTION_WAITS, 1);
    this._stats.incLong(_CONNECTION_WAIT_TIME, duration);
  }

  @Override
  public void startClientOp() {
    this._stats.incInt(clientOpInProgressId, 1);
    this._stats.incInt(clientOpSendInProgressId, 1);
  }

  @Override
  public void endClientOpSend(long duration, boolean failed) {
    this._stats.incInt(clientOpSendInProgressId, -1);
    int endClientOpSendId;
    if (failed) {
      endClientOpSendId = clientOpSendFailedId;
    } else {
      endClientOpSendId = clientOpSendId;
    }
    this._stats.incInt(endClientOpSendId, 1);
    this._stats.incLong(clientOpSendDurationId, duration);
  }

  @Override
  public void endClientOp(long duration, boolean timedOut, boolean failed) {
    this._stats.incInt(clientOpInProgressId, -1);
    int endClientOpId;
    if (timedOut) {
      endClientOpId = clientOpTimedOutId;
    } else if (failed) {
      endClientOpId = clientOpFailedId;
    } else {
      endClientOpId = clientOpId;
    }
    this._stats.incInt(endClientOpId, 1);
    this._stats.incLong(clientOpDurationId, duration);
  }
}
