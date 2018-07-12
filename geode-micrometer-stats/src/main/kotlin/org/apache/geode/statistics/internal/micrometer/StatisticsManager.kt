package org.apache.geode.statistics.internal.micrometer

import io.micrometer.core.instrument.MeterRegistry

interface StatisticsManager {
    fun registerMeterGroup(groupName:String, meterGroup: StatisticsMeterGroup)
    fun registerMeterRegistry(meterRegistry: MeterRegistry)
}