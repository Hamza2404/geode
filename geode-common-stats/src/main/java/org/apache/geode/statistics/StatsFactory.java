package org.apache.geode.statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSToken;

import org.apache.geode.cache.client.internal.ConnectionStats;
import org.apache.geode.cache.query.internal.CqQueryVsdStats;
import org.apache.geode.cache.query.internal.cq.CqServiceVsdStats;
import org.apache.geode.distributed.internal.DistributionStats;
import org.apache.geode.distributed.internal.LocatorStats;
import org.apache.geode.distributed.internal.locks.DistributedLockStats;
import org.apache.geode.internal.cache.CachePerfStats;
import org.apache.geode.internal.cache.DiskDirectoryStats;
import org.apache.geode.internal.cache.DiskRegionStats;
import org.apache.geode.internal.cache.DiskStoreStats;
import org.apache.geode.internal.cache.PartitionedRegionStats;
import org.apache.geode.internal.cache.PoolStats;
import org.apache.geode.internal.cache.RegionPerfStats;
import org.apache.geode.internal.cache.control.ResourceManagerStats;
import org.apache.geode.internal.cache.eviction.CountLRUEvictionStats;
import org.apache.geode.internal.cache.eviction.EvictionStats;
import org.apache.geode.internal.cache.eviction.HeapLRUEvictionStats;
import org.apache.geode.internal.cache.eviction.MemoryLRUEvictionStats;
import org.apache.geode.internal.cache.execute.FunctionStats;
import org.apache.geode.internal.cache.ha.HARegionQueueStats;
import org.apache.geode.internal.cache.wan.GatewayReceiverStats;
import org.apache.geode.internal.cache.wan.GatewaySenderStats;
import org.apache.geode.internal.offheap.OffHeapStorageStats;

public class StatsFactory {

  private static StatsFactory statsFactory = new StatsFactory();

  private Class<? extends StatsImplementor> selectedStatsImplementor = null;
  private Map<Class<?>, Class<? extends StatsImplementor>>
      resolvedStatsImplForClass =
      new HashMap<>();
  private List<ClassLoader> classLoadersList = new LinkedList<>();
  private Reflections reflections;

  private StatsFactory() {
    classLoadersList.add(ClasspathHelper.contextClassLoader());
    classLoadersList.add(ClasspathHelper.staticClassLoader());

    reflections = new Reflections(new ConfigurationBuilder()
        .setScanners(new SubTypesScanner(false /* don't exclude Object.class */),
            new ResourcesScanner())
        .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
        .filterInputsBy(new FilterBuilder()
            .includePackage("org.apache.geode.statistics..*", "org.apache.geode.distributed..*")));
    try {
      selectedStatsImplementor =
          (Class<? extends StatsImplementor>) Class
              .forName("org.apache.geode.statistics.micrometer.MicrometerStatsImplementor");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static LocatorStats createLocatorStatsImpl(StatisticsFactory statisticsFactory,
                                                    String locatorName) {
    return (LocatorStats) resolveInstanceFromClass(LocatorStats.class, statisticsFactory,
        locatorName);
  }

  public static DiskRegionStats createDiskRegionStatsImpl(StatisticsFactory statisticsFactory,
                                                          String diskRegionName) {
    return (DiskRegionStats) resolveInstanceFromClass(DiskRegionStats.class, statisticsFactory,
        diskRegionName);
  }

  public static PoolStats createPoolStatsImpl(StatisticsFactory statisticsFactory,
                                              String poolName) {
    return (PoolStats) resolveInstanceFromClass(PoolStats.class, statisticsFactory, poolName);
  }

  public static PartitionedRegionStats createPartitionedRegionStatsImpl(
      StatisticsFactory statisticsFactory, String fullPath) {
    return (PartitionedRegionStats) resolveInstanceFromClass(PartitionedRegionStats.class,
        statisticsFactory, fullPath);
  }

  public static CachePerfStats createCachePerfStatsImpl(StatisticsFactory statisticsFactory,
                                                        String name) {
    return (CachePerfStats) resolveCachePerfInstanceFromClass(CachePerfStats.class,
        statisticsFactory, name);
  }

  public static GatewaySenderStats createGatewaySenderStatsImpl(StatisticsFactory statisticsFactory,
                                                                String id) {
    return (GatewaySenderStats) resolveInstanceFromClass(GatewaySenderStats.class,
        statisticsFactory, id);
  }

  public static DistributionStats createDistributionStatsImpl(StatisticsFactory statisticsFactory,
                                                              String name) {
    return (DistributionStats) resolveInstanceFromClass(DistributionStats.class, statisticsFactory,
        name);
  }

  public static HARegionQueueStats createHARegionQueueStatsImpl(StatisticsFactory factory,
                                                                String regionName) {
    return (HARegionQueueStats) resolveInstanceFromClass(HARegionQueueStats.class, factory,
        regionName);
  }

  public static FunctionStats createFunctionStatsImpl(StatisticsFactory statisticsFactory,
                                                      String functionName) {
    return (FunctionStats) resolveInstanceFromClass(FunctionStats.class, statisticsFactory,
        functionName);
  }

  public static ResourceManagerStats createResourceManagerStatsImpl(
      StatisticsFactory statisticsFactory) {
    return (ResourceManagerStats) resolveInstanceFromClass(ResourceManagerStats.class,
        statisticsFactory, null);
  }

  public static DiskStoreStats createDiskStoreStatsImpl(StatisticsFactory factory, String name) {
    return (DiskStoreStats) resolveInstanceFromClass(DiskStoreStats.class, factory,
        name);
  }

  public static DiskDirectoryStats createDiskDirectoryStatsImpl(StatisticsFactory factory,
                                                                String ownersName) {
    return (DiskDirectoryStats) resolveInstanceFromClass(DiskDirectoryStats.class, factory,
        ownersName);
  }

  public static ConnectionStats createConnectionStatsImpl(StatisticsFactory dummyStatisticsFactory,
                                                          String statName, PoolStats poolStats) {
    return (ConnectionStats) resolveConnectionStatInstanceFromClass(ConnectionStats.class,
        dummyStatisticsFactory,
        statName, poolStats);
  }

  public static GatewayReceiverStats createGatewayReceiverStatsImpl(
      StatisticsFactory statisticsFactory, String name) {
    return (GatewayReceiverStats) resolveInstanceFromClass(GatewayReceiverStats.class,
        statisticsFactory, name);
  }


  private static StatsImplementor resolveInstanceFromClass(Class<?> interfaceClazz,
                                                           StatisticsFactory statisticsFactory,
                                                           String name) {
    resolveClassImplForClass(interfaceClazz);
    return statsFactory.createInstanceFromClass(interfaceClazz, statisticsFactory, name);
  }

  private static StatsImplementor resolveCachePerfInstanceFromClass(Class<?> interfaceClazz,
                                                                    StatisticsFactory statisticsFactory,
                                                                    String name) {
    resolveCachePerfClassImplForClass(interfaceClazz);
    return statsFactory.createInstanceFromClass(interfaceClazz, statisticsFactory, name);
  }

  private static StatsImplementor resolveConnectionStatInstanceFromClass(Class<?> interfaceClazz,
                                                                         StatisticsFactory statisticsFactory,
                                                                         String name,
                                                                         PoolStats poolStats) {
    resolveClassImplForClass(interfaceClazz);
    return statsFactory.createConnectionStatInstanceFromClass(statisticsFactory, name, poolStats);
  }


  private static void resolveClassImplForClass(Class<?> interfaceClazz) {
    Class<? extends StatsImplementor>
        resolvedLocatorClassImpl =
        statsFactory.resolvedStatsImplForClass.get(interfaceClazz);
    if (resolvedLocatorClassImpl == null) {
      statsFactory.reflections.getSubTypesOf(interfaceClazz)
          .stream()
          .filter(aClass -> statsFactory.selectedStatsImplementor.isAssignableFrom(aClass))
          .forEach(aClass -> statsFactory.resolvedStatsImplForClass
              .put(interfaceClazz, (Class<? extends StatsImplementor>) aClass));
    }
  }

  private static void resolveCachePerfClassImplForClass(Class<?> interfaceClazz) {
    Class<? extends StatsImplementor>
        resolvedLocatorClassImpl =
        statsFactory.resolvedStatsImplForClass.get(interfaceClazz);
    if (resolvedLocatorClassImpl == null) {
      statsFactory.reflections.getSubTypesOf(interfaceClazz)
          .stream()
          .filter(aClass -> statsFactory.selectedStatsImplementor.isAssignableFrom(aClass)
              && !RegionPerfStats.class.isAssignableFrom(aClass))
          .forEach(aClass -> statsFactory.resolvedStatsImplForClass
              .put(interfaceClazz, (Class<? extends StatsImplementor>) aClass));
    }
  }

  public static CqQueryVsdStats createCqQueryVsdStatsImpl(StatisticsFactory factory,
                                                          String serverCqName) {
    return (CqQueryVsdStats) resolveInstanceFromClass(CqQueryVsdStats.class, factory, serverCqName);
  }

  public static CqServiceVsdStats createCqServiceVsdStatsImpl(StatisticsFactory factory) {
    return (CqServiceVsdStats) resolveInstanceFromClass(CqQueryVsdStats.class, factory, null);
  }

  public static DistributedLockStats createDLockStatsImpl(StatisticsFactory statisticsFactory,
                                                          long statId) {
    return (DistributedLockStats) resolveInstanceFromClass(DistributedLockStats.class,
        statisticsFactory,
        String.valueOf(statId));
  }

  public static RegionPerfStats createRegionPerfStatsImpl(StatisticsFactory statisticsFactory,
                                                          CachePerfStats cachePerfStats,
                                                          String regionName) {
    return (RegionPerfStats) resolveRegionPerfStatsInstanceFromClass(RegionPerfStats.class,
        statisticsFactory, cachePerfStats, regionName);
  }

  private static RegionPerfStats resolveRegionPerfStatsInstanceFromClass(
      Class<?> interfaceClazz, StatisticsFactory statisticsFactory,
      CachePerfStats cachePerfStats, String regionName) {
    resolveClassImplForClass(interfaceClazz);
    return (RegionPerfStats) statsFactory
        .createRegionPerfStatsImplFromClass(interfaceClazz, statisticsFactory, cachePerfStats,
            regionName);
  }

  public static EvictionStats createHeapLRUStatisticsImpl(StatisticsFactory statisticsFactory,
                                                          String name) {
    return (EvictionStats) resolveInstanceFromClass(HeapLRUEvictionStats.class, statisticsFactory,
        name);
  }

  public static EvictionStats createCountLRUStatisticsImpl(StatisticsFactory statisticsFactory,
                                                           String name) {
    return (EvictionStats) resolveInstanceFromClass(CountLRUEvictionStats.class, statisticsFactory,
        name);
  }

  public static EvictionStats createMemoryLRUStatisticsImpl(StatisticsFactory statisticsFactory,
                                                            String name) {
    return (EvictionStats) resolveInstanceFromClass(MemoryLRUEvictionStats.class, statisticsFactory,
        name);
  }

  public static OffHeapStorageStats createOffHeapStorageStatsImpl(
      StatisticsFactory statisticsFactory,
      String name) {
    return (OffHeapStorageStats) resolveInstanceFromClass(OffHeapStorageStats.class,
        statisticsFactory,
        name);
  }

  private StatsImplementor createRegionPerfStatsImplFromClass(Class<?> interfaceClazz,
                                                              StatisticsFactory statisticsFactory,
                                                              CachePerfStats cachePerfStats,
                                                              String regionName) {
    Class<? extends StatsImplementor> resolvedLocatorClassImpl;
    try {
      resolvedLocatorClassImpl = resolvedStatsImplForClass.get(interfaceClazz);
      StatsImplementor statsImplementor = resolvedLocatorClassImpl
          .getDeclaredConstructor(StatisticsFactory.class, CachePerfStats.class, String.class)
          .newInstance(statisticsFactory, cachePerfStats, regionName);
      statsImplementor.initializeImplementor(statisticsFactory);
      statsImplementor.registerStatsImplementor(statisticsFactory);
      return statsImplementor;
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  private StatsImplementor createConnectionStatInstanceFromClass(
      StatisticsFactory statisticsFactory,
      String locatorName, PoolStats poolStats) {
    Class<? extends StatsImplementor> resolvedLocatorClassImpl;
    try {
      resolvedLocatorClassImpl = resolvedStatsImplForClass.get(ConnectionStats.class);
      StatsImplementor
          statsImplementor =
          resolvedLocatorClassImpl
              .getDeclaredConstructor(StatisticsFactory.class, String.class, PoolStats.class)
              .newInstance(statisticsFactory, locatorName, poolStats);
      statsImplementor.initializeImplementor(statisticsFactory);
      statsImplementor.registerStatsImplementor(statisticsFactory);
      return statsImplementor;
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  private StatsImplementor createInstanceFromClass(Class<?> interfaceClazz,
                                                   StatisticsFactory statisticsFactory,
                                                   String name) {
    Class<? extends StatsImplementor> resolvedLocatorClassImpl;
    try {
      resolvedLocatorClassImpl = resolvedStatsImplForClass.get(interfaceClazz);
      if (resolvedLocatorClassImpl == null) {
        throw new IllegalArgumentException("Excepted to have stats for type: " + interfaceClazz);
      }
      StatsImplementor statsImplementor = resolvedLocatorClassImpl
          .getDeclaredConstructor(StatisticsFactory.class, String.class)
          .newInstance(statisticsFactory, name);
      statsImplementor.initializeImplementor(statisticsFactory);
      statsImplementor.registerStatsImplementor(statisticsFactory);
      return statsImplementor;
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}

