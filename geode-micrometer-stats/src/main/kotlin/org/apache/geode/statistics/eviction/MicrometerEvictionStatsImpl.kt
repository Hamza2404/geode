package org.apache.geode.statistics.eviction

import org.apache.geode.internal.cache.eviction.EvictionStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter

import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

abstract class MicrometerEvictionStatsImpl(statisticsFactory: StatisticsFactory?,private val regionName: String, private val groupName: String = "EvictionStats-$regionName") :
        MicrometerMeterGroup(statisticsFactory,groupName), EvictionStats {


    override fun getGroupTags(): Array<String> = arrayOf("regionName", regionName)

    override fun initializeStaticMeters() {
        registerMeter(evictionLRUEvictionCountMeter)
        registerMeter(evictionLRUDestroyCountMeter)
        registerMeter(evictionLRUEvaluationsCountMeter)
        registerMeter(evictionLRUGreedyReturnsCountMeter)
    }

    private val evictionLRUEvictionCountMeter = CounterStatisticMeter("eviction.lru.evictions.count", "Number of total entry evictions triggered by LRU.")
    private val evictionLRUDestroyCountMeter = CounterStatisticMeter("eviction.lru.destroy.count", "Number of entries destroyed in the region through both destroy cache operations and eviction.")
    private val evictionLRUEvaluationsCountMeter = CounterStatisticMeter("eviction.lru.evaluation.count", "Number of entries evaluated during LRU operations.")
    private val evictionLRUGreedyReturnsCountMeter = CounterStatisticMeter("eviction.lru.greedyreturns.count", "Number of non-LRU entries evicted during LRU operations")

    override fun getStatistics(): Statistics? {
        //this is a noop for Micrometer stats
        return null
    }

    override fun close() {
        //this is a noop for Micrometer stats
    }

    override fun incEvictions() {
        evictionLRUEvictionCountMeter.increment()
    }

    override fun incEvaluations(delta: Long) {
        evictionLRUEvaluationsCountMeter.increment(delta)
    }

    override fun incDestroys() {
        evictionLRUDestroyCountMeter.increment()
    }

    override fun incGreedyReturns(delta: Long) {
        evictionLRUGreedyReturnsCountMeter.increment(delta)
    }
}