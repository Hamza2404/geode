package org.apache.geode.statistics.internal.micrometer

import java.time.Duration
import java.util.concurrent.TimeUnit

interface StatisticsMeter {
    fun getMetricName(): String
    fun getBaseUnit(): String
}

interface ScalarStatisticsMeter {
    fun increment()
    fun increment(value: Double = 1.0)
    fun increment(value: Long = 1L)
    fun increment(value: Int = 1)
    fun decrement()
    fun decrement(value: Double = -1.0)
    fun decrement(value: Long = -1L)
    fun decrement(value: Int = -1)
}

interface TimedStatisticsMeter {
    fun recordValue(amount: Long, timeUnit: TimeUnit = TimeUnit.NANOSECONDS)
    fun recordValue(duration: Duration)
}