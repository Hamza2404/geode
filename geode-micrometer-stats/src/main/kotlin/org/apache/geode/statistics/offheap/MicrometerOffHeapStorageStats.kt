package org.apache.geode.statistics.offheap

import org.apache.geode.internal.offheap.OffHeapStorageStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.util.NOW_NANOS
import java.util.concurrent.TimeUnit

class MicrometerOffHeapStorageStats(statisticsFactory: StatisticsFactory?, private val name: String) :
        MicrometerMeterGroup(statisticsFactory, "OffHeapMemoryStats-$name"), OffHeapStorageStats {
    override fun getGroupTags(): Array<String> = arrayOf("offheap", name)

    override fun initializeStaticMeters() {
        registerMeter(offheapMemoryUsedBytesMeter)
        registerMeter(offheapMemoryMaxBytesMeter)
        registerMeter(offheapMemoryDefragmentationMeter)
        registerMeter(offheapMemoryDefragmentationInProgressMeter)
        registerMeter(offheapMemoryDefragmentationTimer)
        registerMeter(offheapMemoryFragmentationMeter)
        registerMeter(offheapMemoryFragmentMeter)
        registerMeter(offheapMemoryFreeBytesMeter)
        registerMeter(offheapMemoryLargestFragmentBytesMeter)
        registerMeter(offheapMemoryObjectCountMeter)
        registerMeter(offheapMemoryReadCountMeter)
    }

    private val offheapMemoryUsedBytesMeter = GaugeStatisticMeter("offheap.memory.used.bytes", "The amount of off-heap memory, in bytes, that is being used to store data.", meterUnit = "bytes")
    private val offheapMemoryMaxBytesMeter = GaugeStatisticMeter("offheap.memory.max.bytes", "The maximum amount of off-heap memory, in bytes. This is the amount of memory allocated at startup and does not change.")
    private val offheapMemoryDefragmentationMeter = CounterStatisticMeter("offheap.memory.defragmentations.count", "The total number of times off-heap memory has been defragmented.")
    private val offheapMemoryDefragmentationInProgressMeter = GaugeStatisticMeter("offheap.memory.defragmentations.inprogress.count", "Current number of defragment operations currently in progress.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val offheapMemoryDefragmentationTimer = CounterStatisticMeter("offheap.memory.defragmentation.time", "The total time spent defragmenting off-heap memory.", meterUnit = "nanoseconds")
    private val offheapMemoryFragmentationMeter = GaugeStatisticMeter("offheap.memory.fragmentation.percentage", "The percentage of off-heap free memory that is fragmented.  Updated every time a defragmentation is performed.")
    private val offheapMemoryFragmentMeter = GaugeStatisticMeter("offheap.memory.fragments.count", "The number of fragments of free off-heap memory. Updated every time a defragmentation is done.")
    private val offheapMemoryFreeBytesMeter = GaugeStatisticMeter("offheap.memory.free.bytes", "The amount of off-heap memory, in bytes, that is not being used.")
    private val offheapMemoryLargestFragmentBytesMeter = GaugeStatisticMeter("offheap.memory.largest.fragment.bytes", "The largest fragment of memory found by the last defragmentation of off heap memory. Updated every time a defragmentation is done.")
    private val offheapMemoryObjectCountMeter = GaugeStatisticMeter("offheap.memory.object.count", "The number of objects stored in off-heap memory.")
    private val offheapMemoryReadCountMeter = CounterStatisticMeter("offheap.memory.read.count", "The total number of reads of off-heap memory. Only reads of a full object increment this statistic. If only a part of the object is read this statistic is not incremented.")

    override fun incFreeMemory(value: Long) {
        offheapMemoryFreeBytesMeter.increment(value)
    }

    override fun incMaxMemory(value: Long) {
        offheapMemoryMaxBytesMeter.increment(value)
    }

    override fun incUsedMemory(value: Long) {
        offheapMemoryUsedBytesMeter.increment(value)
    }

    override fun incObjects(value: Int) {
        offheapMemoryObjectCountMeter.increment(value)
    }

    override fun incReads() {
        offheapMemoryReadCountMeter.increment()
    }

    private fun incDefragmentations() {
        offheapMemoryDefragmentationMeter.increment()
    }

    override fun setFragments(value: Long) {
        offheapMemoryFragmentMeter.setValue(value)
    }

    override fun setLargestFragment(value: Int) {
        offheapMemoryLargestFragmentBytesMeter.setValue(value)
    }

    override fun startDefragmentation(): Long {
        offheapMemoryDefragmentationInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endDefragmentation(start: Long) {
        incDefragmentations()
        offheapMemoryDefragmentationInProgressMeter.decrement()
        offheapMemoryDefragmentationTimer.increment(NOW_NANOS - start)
    }

    override fun setFragmentation(value: Int) {
        offheapMemoryFragmentationMeter.setValue(value)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFreeMemory(): Long = offheapMemoryFreeBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMaxMemory(): Long = offheapMemoryMaxBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUsedMemory(): Long = offheapMemoryUsedBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReads(): Long = offheapMemoryReadCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getObjects(): Int = offheapMemoryObjectCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDefragmentations(): Int = offheapMemoryDefragmentationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDefragmentationsInProgress(): Int = offheapMemoryDefragmentationInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFragments(): Long = offheapMemoryFragmentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLargestFragment(): Int = offheapMemoryLargestFragmentBytesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFragmentation(): Int =offheapMemoryFragmentationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDefragmentationTime(): Long =offheapMemoryDefragmentationTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("This is not applicable for Micrometer")
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun initialize(stats: OffHeapStorageStats?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}