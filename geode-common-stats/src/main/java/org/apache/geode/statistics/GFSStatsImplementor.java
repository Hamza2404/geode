package org.apache.geode.statistics;

public interface GFSStatsImplementor extends StatsImplementor {
  @Override
  default String getStatsImplementorType(){return "GFS_Stats_Implementor";}

  @Override
  default void initializeImplementor(StatisticsFactory factory) {initializeStats(factory);}

  default void initializeStats(StatisticsFactory factory){}

  @Override
  default void registerStatsImplementor(StatisticsFactory factory) { }
}
