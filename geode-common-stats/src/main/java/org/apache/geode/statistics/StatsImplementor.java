package org.apache.geode.statistics;

public interface StatsImplementor {
  String getStatsImplementorType();
  void initializeImplementor(StatisticsFactory factory);
  void registerStatsImplementor(StatisticsFactory factory);
}
