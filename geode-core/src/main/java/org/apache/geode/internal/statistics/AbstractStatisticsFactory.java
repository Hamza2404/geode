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
package org.apache.geode.internal.statistics;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.i18n.LocalizedStrings;

/**
 * An abstract standalone implementation of {@link StatisticsFactory}. It can be used in contexts
 * that do not have the GemFire product or in vm's that do not have a distributed system nor a
 * gemfire connection.
 * @since GemFire 7.0
 */
public abstract class AbstractStatisticsFactory implements StatisticsFactory, StatisticsManager {

  private final long id;
  private final String name;
  private final List<Statistics> statsList = Collections.synchronizedList(new ArrayList<>());
  private int statsListModCount = 0;
  private long statsListUniqueId = 1;
  private final Object statsListUniqueIdLock;
  private final StatisticsTypeFactory statisticsTypeFactory;
  private final long startTime;

  public AbstractStatisticsFactory(StatisticsFactory statisticsFactory, long id, String name, long startTime) {
    this.id = id;
    this.name = name;
    this.startTime = startTime;

    this.statsListUniqueIdLock = new Object();
    this.statisticsTypeFactory = statisticsFactory;
  }

  public void close() {
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public long getId() {
    return this.id;
  }

  @Override
  public long getStartTime() {
    return this.startTime;
  }

  @Override
  public int getStatListModCount() {
    return this.statsListModCount;
  }

  @Override
  public List<Statistics> getStatsList() {
    return this.statsList;
  }

  @Override
  public int getStatisticsCount() {
    return statsList.size();
  }

  @Override
  public Statistics findStatistics(long id) {
    for (Statistics statistics : statsList) {
      if (statistics.getUniqueId() == id) {
        return statistics;
      }
    }
    throw new RuntimeException(
        LocalizedStrings.PureStatSampler_COULD_NOT_FIND_STATISTICS_INSTANCE.toLocalizedString());
  }

  @Override
  public boolean statisticsExists(long id) {
    List<Statistics> statsList = this.statsList;
    for (Statistics statistics : statsList) {
      if (statistics.getUniqueId() == id) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Statistics[] getStatistics() {
    List<Statistics> statsList = this.statsList;
    return statsList.toArray(new Statistics[statsList.size()]);
  }

  // StatisticsFactory methods

  @Override
  public Statistics createStatistics(StatisticsType type) {
    return createOsStatistics(type, null, 0, 0);
  }

  @Override
  public Statistics createStatistics(StatisticsType type, String textId) {
    return createOsStatistics(type, textId, 0, 0);
  }

  @Override
  public Statistics createStatistics(StatisticsType type, String textId, long numericId) {
    return createOsStatistics(type, textId, 0, 0);
  }

  public Statistics createOsStatistics(StatisticsType type, String textId, long numericId,
                                       int osStatFlags) {
    long myUniqueId;
    synchronized (statsListUniqueIdLock) {
      myUniqueId = statsListUniqueId++; // fix for bug 30597
    }
    Statistics result =
        new LocalStatisticsImpl(type, textId, numericId, myUniqueId, false, osStatFlags, this);
    synchronized (statsList) {
      statsList.add(result);
      statsListModCount++;
    }
    return result;
  }

  @Override
  public Statistics[] findStatisticsByType(StatisticsType type) {
    List<Statistics> hits = new ArrayList<Statistics>();
    Iterator<Statistics> it = statsList.iterator();
    while (it.hasNext()) {
      Statistics s = (Statistics) it.next();
      if (type == s.getType()) {
        hits.add(s);
      }
    }
    Statistics[] result = new Statistics[hits.size()];
    return (Statistics[]) hits.toArray(result);
  }

  @Override
  public Statistics[] findStatisticsByTextId(String textId) {
    List<Statistics> hits = new ArrayList<Statistics>();
    Iterator<Statistics> it = statsList.iterator();
    while (it.hasNext()) {
      Statistics s = (Statistics) it.next();
      if (s.getTextId().equals(textId)) {
        hits.add(s);
      }
    }
    Statistics[] result = new Statistics[hits.size()];
    return (Statistics[]) hits.toArray(result);
  }

  @Override
  public Statistics[] findStatisticsByNumericId(long numericId) {
    List<Statistics> hits = new ArrayList<Statistics>();
    Iterator<Statistics> it = statsList.iterator();
    while (it.hasNext()) {
      Statistics s = (Statistics) it.next();
      if (numericId == s.getNumericId()) {
        hits.add(s);
      }
    }
    Statistics[] result = new Statistics[hits.size()];
    return (Statistics[]) hits.toArray(result);
  }

  public Statistics findStatisticsByUniqueId(long uniqueId) {
    Iterator<Statistics> it = statsList.iterator();
    while (it.hasNext()) {
      Statistics s = (Statistics) it.next();
      if (uniqueId == s.getUniqueId()) {
        return s;
      }
    }
    return null;
  }

  /**
   * for internal use only. Its called by {@link LocalStatisticsImpl#close}.
   */
  @Override
  public void destroyStatistics(Statistics stats) {
    synchronized (statsList) {
      if (statsList.remove(stats)) {
        statsListModCount++;
      }
    }
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type) {
    return createAtomicStatistics(type, null, 0);
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type, String textId) {
    return createAtomicStatistics(type, textId, 0);
  }

  @Override
  public Statistics createAtomicStatistics(StatisticsType type, String textId, long numericId) {
    long myUniqueId;
    synchronized (statsListUniqueIdLock) {
      myUniqueId = statsListUniqueId++; // fix for bug 30597
    }
    Statistics result = StatisticsImpl.createAtomicNoOS(type, textId, numericId, myUniqueId, this);
    synchronized (statsList) {
      statsList.add(result);
      statsListModCount++;
    }
    return result;
  }

  // StatisticsTypeFactory methods

  /**
   * Creates or finds a StatisticType for the given shared class.
   */
  @Override
  public StatisticsType createType(String name, String description, StatisticDescriptor[] stats) {
    return statisticsTypeFactory.createType(name, description, stats);
  }

  @Override
  public StatisticsType findType(String name) {
    return statisticsTypeFactory.findType(name);
  }

  @Override
  public StatisticsType[] createTypesFromXml(Reader reader) throws IOException {
    return statisticsTypeFactory.createTypesFromXml(reader);
  }

  @Override
  public StatisticDescriptor createIntCounter(String name, String description, String units) {
    return statisticsTypeFactory.createIntCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createLongCounter(String name, String description, String units) {
    return statisticsTypeFactory.createLongCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createDoubleCounter(String name, String description, String units) {
    return statisticsTypeFactory.createDoubleCounter(name, description, units);
  }

  @Override
  public StatisticDescriptor createIntGauge(String name, String description, String units) {
    return statisticsTypeFactory.createIntGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createLongGauge(String name, String description, String units) {
    return statisticsTypeFactory.createLongGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createDoubleGauge(String name, String description, String units) {
    return statisticsTypeFactory.createDoubleGauge(name, description, units);
  }

  @Override
  public StatisticDescriptor createIntCounter(String name, String description, String units,
                                              boolean largerBetter) {
    return statisticsTypeFactory.createIntCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createLongCounter(String name, String description, String units,
                                               boolean largerBetter) {
    return statisticsTypeFactory.createLongCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createDoubleCounter(String name, String description, String units,
                                                 boolean largerBetter) {
    return statisticsTypeFactory.createDoubleCounter(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createIntGauge(String name, String description, String units,
                                            boolean largerBetter) {
    return statisticsTypeFactory.createIntGauge(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createLongGauge(String name, String description, String units,
                                             boolean largerBetter) {
    return statisticsTypeFactory.createLongGauge(name, description, units, largerBetter);
  }

  @Override
  public StatisticDescriptor createDoubleGauge(String name, String description, String units,
                                               boolean largerBetter) {
    return statisticsTypeFactory.createDoubleGauge(name, description, units, largerBetter);
  }
}
