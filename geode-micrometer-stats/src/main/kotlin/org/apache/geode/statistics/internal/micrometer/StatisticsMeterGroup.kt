package org.apache.geode.statistics.internal.micrometer

interface StatisticsMeterGroup {
    fun getMeterGroupName(): String
}
