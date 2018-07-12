package org.apache.geode.statistics.micrometer

import org.apache.geode.statistics.StatsImplementor

interface MicrometerStatsImplementor:StatsImplementor {
    override fun getStatsImplementorType(): String = "Micrometer"
}