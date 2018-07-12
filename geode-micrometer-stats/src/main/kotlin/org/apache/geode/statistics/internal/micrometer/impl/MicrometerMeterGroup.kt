package org.apache.geode.statistics.internal.micrometer.impl

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.MeterBinder
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.StatisticsMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

abstract class MicrometerMeterGroup(private val statisticsFactory: StatisticsFactory?, private val groupName: String) : StatisticsMeterGroup, MeterBinder,
        MicrometerStatsImplementor {
    private val registeredMeters = mutableListOf<MicrometerStatisticMeter>()
    private val registeredMeterGroups = mutableListOf<MicrometerMeterGroup>()

    private val commonGroupTags: Array<String> by lazy { getGroupTags() }

    abstract fun initializeStaticMeters()

    open fun getGroupTags(): Array<String> = emptyArray()

    override fun registerStatsImplementor(factory: StatisticsFactory?) {
        MicrometerStatisticsManager.registerMeterGroup(this.groupName,this)
    }

    override fun initializeImplementor(factory: StatisticsFactory?) {
        initializeStaticMeters()
    }

    override fun getMeterGroupName(): String = groupName
    override fun bindTo(registry: MeterRegistry) {
        registeredMeters.forEach {it.register(registry, commonGroupTags) }
        registeredMeterGroups.forEach { micrometerMeterGroup -> micrometerMeterGroup.registeredMeters.forEach { it.register(registry, commonGroupTags) } }
    }

    protected fun registerMeter(meter: MicrometerStatisticMeter) {
        registeredMeters.add(meter)
    }

    protected fun registerMeterGroup(meterGroup: MicrometerMeterGroup) {
        registeredMeterGroups.add(meterGroup)
    }
}