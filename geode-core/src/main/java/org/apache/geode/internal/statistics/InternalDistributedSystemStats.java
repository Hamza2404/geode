package org.apache.geode.internal.statistics;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;

import org.apache.geode.distributed.internal.DistributionConfig;
import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.internal.cache.execute.FunctionServiceStats;
import org.apache.geode.internal.cache.execute.FunctionStats;
import org.apache.geode.internal.cache.execute.FunctionStats;
import org.apache.geode.internal.i18n.LocalizedStrings;
import org.apache.geode.internal.statistics.platform.OsStatisticsFactory;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.statistics.StatsFactory;
import org.apache.geode.statistics.micrometer.MicrometerStatisticsFactoryImpl;

public class InternalDistributedSystemStats
    implements StatisticsFactory, StatisticsManager, OsStatisticsFactory {

  /**
   * The sampler for this DistributedSystem.
   */
  private GemFireStatSampler sampler = null;

  private final CopyOnWriteArrayList<Statistics> statsList = new CopyOnWriteArrayList<>();
  private int statsListModCount = 0;
  private LongAdder statsListUniqueId = new LongAdder();

  // As the function execution stats can be lot in number, its better to put
  // them in a map so that it will be accessible immediately
  private final ConcurrentHashMap<String, FunctionStats>
      functionExecutionStatsMap =
      new ConcurrentHashMap<>();
  private FunctionServiceStats functionServiceStats;

  private boolean statsDisabled;

  // StatisticsTypeFactory methods
  private StatisticsFactory statisticsTypeFactory;

  //TODO, sorry another singleton... BLECH!!
  private static final InternalDistributedSystemStats
      singleton = new InternalDistributedSystemStats();

  private InternalDistributedSystemStats() {
  }

  //TODO Udo: We need to fix the bootstrapping to have DS's and DM's created in order to get the statsfactory created
  public InternalDistributedSystemStats(boolean statsDisabled,
                                        DistributionConfig distributionConfig,
                                        InternalDistributedSystem internalDistributedSystem,
                                        StatisticsTypeFactory statisticsTypeFactory) {
    this.statisticsTypeFactory = (StatisticsFactory) statisticsTypeFactory;
    this.statsDisabled = statsDisabled;
    this.functionServiceStats = new FunctionServiceStats(this, "FunctionExecution");
    if (!statsDisabled && !(statisticsTypeFactory instanceof MicrometerStatisticsFactoryImpl)) {
      this.sampler = new GemFireStatSampler(internalDistributedSystem.getId(), distributionConfig,
          internalDistributedSystem.getCancelCriterion(), this,
          internalDistributedSystem.getDistributionManager());
      this.sampler.start();

    }
  }

  public static InternalDistributedSystemStats createInstance(boolean statsDisabled,
                                                              DistributionConfig distributionConfig,
                                                              InternalDistributedSystem distributedSystem,
                                                              MicrometerStatisticsFactoryImpl statisticsTypeFactory) {

    singleton.statisticsTypeFactory = statisticsTypeFactory;
    singleton.statsDisabled = statsDisabled;
    singleton.functionServiceStats = new FunctionServiceStats(singleton, "FunctionExecution");
    if (!statsDisabled && !(statisticsTypeFactory instanceof MicrometerStatisticsFactoryImpl)) {
      singleton.sampler = new GemFireStatSampler(distributedSystem.getId(), distributionConfig,
          distributedSystem.getCancelCriterion(), singleton,
          distributedSystem.getDistributionManager());
      singleton.sampler.start();

    }
    return singleton;
  }

  public static InternalDistributedSystemStats getSingleton() {
    return singleton;
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
  public String getName() {
    return null;
  }

  @Override
  public long getId() {
    return 0;
  }

  @Override
  public long getStartTime() {
    return 0;
  }

  @Override
  public int getStatisticsCount() {
    return ((List<Statistics>) this.statsList).size();
  }

  @Override
  public Statistics findStatistics(long id) {
    List<Statistics> statsList = this.statsList;
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
    return statsList.toArray(new Statistics[0]);
  }

  // StatisticsFactory methods
  public Statistics createStatistics(StatisticsType type) {
    return createOsStatistics(type, null, 0, 0);
  }

  public Statistics createStatistics(StatisticsType type, String textId) {
    return statisticsTypeFactory.createOsStatistics(type, textId, 0, 0);
  }

  public Statistics createStatistics(StatisticsType type, String textId, long numericId) {
    return createOsStatistics(type, textId, numericId, 0);
  }

  public Statistics[] findStatisticsByType(final StatisticsType type) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (type == vistorStatistic.getType()) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  public Statistics[] findStatisticsByTextId(final String textId) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (vistorStatistic.getTextId().equals(textId)) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  public Statistics[] findStatisticsByNumericId(final long numericId) {
    final ArrayList<Statistics> hits = new ArrayList<>();
    visitStatistics(vistorStatistic -> {
      if (numericId == vistorStatistic.getNumericId()) {
        hits.add(vistorStatistic);
      }
    });
    Statistics[] result = new Statistics[hits.size()];
    return hits.toArray(result);
  }

  public Statistics findStatisticsByUniqueId(final long uniqueId) {
    for (Statistics statistics : this.statsList) {
      if (uniqueId == statistics.getUniqueId()) {
        return statistics;
      }
    }
    return null;
  }

  /**
   * for internal use only. Its called by {@link LocalStatisticsImpl#close}.
   */
  public void destroyStatistics(Statistics stats) {
    synchronized (statsList) {
      if (statsList.remove(stats)) {
        statsListModCount++;
      }
    }
  }

  public Statistics createAtomicStatistics(StatisticsType type) {
    return createAtomicStatistics(type, null, 0);
  }

  public Statistics createAtomicStatistics(StatisticsType type, String textId) {
    return createAtomicStatistics(type, textId, 0);
  }

  public Statistics createAtomicStatistics(StatisticsType type, String textId, long numericId) {
    return statisticsTypeFactory.createAtomicStatistics(type, textId, numericId);
  }

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
  public Statistics createOsStatistics(StatisticsType type, String textId, long numericId, int osStatFlags) {
    if (this.statsDisabled) {
      return new DummyStatisticsImpl(type, textId, numericId);
    }
    statsListUniqueId.increment();
    Statistics result =
        new LocalStatisticsImpl(type, textId, numericId, statsListUniqueId.longValue(), false,
            osStatFlags, this);
    synchronized (statsList) {
      statsList.add(result);
      statsListModCount++;
    }
    return result;
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

  public FunctionStats getFunctionStats(String textId) {
    FunctionStats stats = functionExecutionStatsMap.get(textId);
    if (stats == null) {
      stats = StatsFactory.createFunctionStatsImpl(this, textId);
      FunctionStats oldStats = functionExecutionStatsMap.putIfAbsent(textId, stats);
      if (oldStats != null) {
        stats.close();
        stats = oldStats;
      }
    }
    return stats;
  }


  public FunctionServiceStats getFunctionServiceStats() {
    return functionServiceStats;
  }

  /**
   * For every registered statistic instance call the specified visitor. This method was added to
   * fix bug 40358
   */
  public void visitStatistics(InternalDistributedSystem.StatisticsVisitor visitor) {
    for (Statistics s : this.statsList) {
      visitor.visit(s);
    }
  }

  public Set<String> getAllFunctionExecutionIds() {
    return functionExecutionStatsMap.keySet();
  }

  public void closeStats() {
    // closing the Aggregate stats
    if (functionServiceStats != null) {
      functionServiceStats.close();
    }
    // closing individual function stats
    for (FunctionStats functionstats : functionExecutionStatsMap.values()) {
      functionstats.close();
    }

    if (sampler != null) {
      this.sampler.stop();
    }
  }

  public GemFireStatSampler getStatSampler() {
    return this.sampler;
  }
}
