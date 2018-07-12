package org.apache.geode.statistics.internal.micrometer.impl

import io.micrometer.core.instrument.*
import org.apache.geode.statistics.StatisticDescriptor
import org.apache.geode.statistics.internal.micrometer.ScalarStatisticsMeter
import org.apache.geode.statistics.internal.micrometer.StatisticsMeter
import org.apache.geode.statistics.internal.micrometer.TimedStatisticsMeter
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.LongAdder

abstract class MicrometerStatisticMeter(
        private val meterName: String,
        private val meterDescription: String,
        private val meterUnit: String) : StatisticsMeter, StatisticDescriptor {

    var meterId: Int = -1

    override fun getBaseUnit(): String = unit
    override fun getMetricName(): String = meterName

    override fun getId() = meterId
    override fun getName() = meterName
    override fun getDescription() = meterDescription
    override fun getUnit() = meterUnit

    override fun getType(): Class<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isCounter() = false
    override fun isLargerBetter() = false

    override fun compareTo(other: StatisticDescriptor) = meterName.compareTo((other as MicrometerStatisticMeter).meterName)

    abstract fun register(meterRegistry: MeterRegistry, commonTags: Array<String> = emptyArray())

    override fun hashCode(): Int {
        return meterName.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CounterStatisticMeter

        if (meterName != other.meterName) return false

        return true
    }

    open fun setValue(value: Int) {
        setValue(value.toLong())
    }

    open fun setValue(value: Double) {
        setValue(value.toLong())
    }

    abstract fun setValue(value: Long)

    abstract fun getValue(): Long

    open fun increment(value: Int) {
        increment(value.toLong())
    }

    open fun increment(value: Double) {
        increment(value.toLong())
    }

    open fun increment(value: Long) {
        TODO("a timer metric should be using recordValue method rather than increment")
    }
}

data class GaugeStatisticMeter(val meterName: String,
                               val meterDescription: String,
                               val meterTags: Array<String> = emptyArray(),
                               val meterUnit: String = "") : ScalarStatisticsMeter, MicrometerStatisticMeter(meterName, meterDescription, meterUnit) {

    private lateinit var meter: Gauge
    private val backingValue: LongAdder = LongAdder()

    override fun register(meterRegistry: MeterRegistry, commonTags: Array<String>) {
        meter = Gauge.builder(meterName, backingValue) { backingValue.toDouble() }
                .description(meterDescription).baseUnit(meterUnit).tags(*commonTags).tags(*meterTags).register(meterRegistry)
    }

    override fun increment() {
        backingValue.increment()
    }

    override fun increment(value: Double) {
        increment(value.toLong())
    }

    override fun increment(value: Long) {
        backingValue.add(value)
    }

    override fun increment(value: Int) {
        increment(value.toLong())
    }

    override fun decrement() {
        backingValue.decrement()
    }

    override fun decrement(value: Double) {
        decrement(value.toLong())
    }

    override fun decrement(value: Long) {
        backingValue.add(value)
    }

    override fun decrement(value: Int) {
        decrement(value.toLong())
    }

    override fun setValue(value: Long) {
        backingValue.reset()
        backingValue.add(value)
    }

    override fun getValue() = backingValue.sum()

    override fun hashCode() = super.hashCode()
    override fun equals(other: Any?) = super.equals(other)
}

data class CounterStatisticMeter(val meterName: String,
                                 val meterDescription: String,
                                 val meterTags: Array<String> = emptyArray(),
                                 val meterUnit: String = "") : ScalarStatisticsMeter, MicrometerStatisticMeter(meterName, meterDescription, meterUnit) {

    private lateinit var meter: Counter

    override fun isCounter(): Boolean = true
    override fun register(meterRegistry: MeterRegistry, commonTags: Array<String>) {
        meter = Counter.builder(meterName)
                .description(meterDescription).baseUnit(meterUnit).tags(*commonTags).tags(*meterTags).register(meterRegistry)
    }

    override fun increment() {
        meter.increment()
    }

    override fun increment(value: Double) {
        meter.increment(value)
    }

    override fun increment(value: Long) {
        increment(value.toDouble())
    }

    override fun increment(value: Int) {
        increment(value.toDouble())
    }

    override fun decrement() {
        throw RuntimeException("Decrementing is not supported on a Counter")
    }

    override fun decrement(value: Double) {
        throw RuntimeException("Decrementing is not supported on a Counter")
    }

    override fun decrement(value: Long) {
        throw RuntimeException("Decrementing is not supported on a Counter")
    }

    override fun decrement(value: Int) {
        throw RuntimeException("Decrementing is not supported on a Counter")
    }

    override fun hashCode() = super.hashCode()
    override fun equals(other: Any?) = super.equals(other)

    override fun getValue(): Long = meter.count().toLong()

    override fun setValue(value: Long) {
        meter.increment(value.toDouble())
    }
}

data class TimerStatisticMeter(val meterName: String,
                               val meterDescription: String,
                               val meterTags: Array<String> = emptyArray(),
                               val meterUnit: String = "") : TimedStatisticsMeter, MicrometerStatisticMeter(meterName, meterDescription, meterUnit) {
    private lateinit var meter: Timer

    override fun register(meterRegistry: MeterRegistry, commonTags: Array<String>) {
        meter = Timer.builder(meterName)
                .description(meterDescription).tags(*commonTags).tags(*meterTags).register(meterRegistry)
    }

    override fun recordValue(amount: Long, timeUnit: TimeUnit) {
        meter.record(amount, timeUnit)
    }

    override fun recordValue(duration: Duration) {
        meter.record(duration)
    }

    override fun hashCode() = super.hashCode()
    override fun equals(other: Any?) = super.equals(other)

    override fun getValue(): Long {
        TODO("A timer meter should not be exposing its metrics outside of itself")
    }

    override fun setValue(value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}