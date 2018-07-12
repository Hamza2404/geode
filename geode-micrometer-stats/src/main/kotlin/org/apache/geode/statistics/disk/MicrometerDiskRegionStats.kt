package org.apache.geode.statistics.disk

import org.apache.geode.internal.cache.DiskRegionStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

class MicrometerDiskRegionStats(statisticsFactory: StatisticsFactory?,private val diskRegionName: String) :
        MicrometerMeterGroup(statisticsFactory,"DiskRegionStats-$diskRegionName"), DiskRegionStats {


    override fun getGroupTags(): Array<String> = arrayOf("diskRegionName", diskRegionName)

    override fun initializeStaticMeters() {
        registerMeter(diskStoreWriteMeter)
        registerMeter(diskStoreWriteTimer)
        registerMeter(diskStoreWriteBytesMeter)
        registerMeter(diskStoreWriteInProgressMeter)
        registerMeter(diskStoreReadMeter)
        registerMeter(diskStoreReadTimer)
        registerMeter(diskStoreReadBytesMeter)
        registerMeter(diskStoreRemoveMeter)
        registerMeter(diskStoreRemoveTimer)
        registerMeter(diskStoreEntriesOnDiskOnlyMeter)
        registerMeter(diskStoreEntriesOnDiskOnlyBytesMeter)
        registerMeter(diskStoreEntriesInVMMeter)
        registerMeter(diskStoreRegionLocalInitializationMeter)
        registerMeter(diskStoreRegionRemoteInitializationMeter)
    }

    private val diskStoreWriteMeter = CounterStatisticMeter("diskregion.entry.write.count", "The total number of region entries that have been written to diskregion. A write is done every time an entry is created on disk or every time its value is modified on diskregion.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreWriteTimer = CounterStatisticMeter("diskregion.entry.write.time", "The total amount of time spent writing to disk", meterUnit = "nanoseconds")
    private val diskStoreWriteBytesMeter = CounterStatisticMeter("diskregion.write.bytes", "The total number of bytes that have been written to disk", meterUnit = "bytes")
    private val diskStoreWriteInProgressMeter = GaugeStatisticMeter("diskregion.write.inprogress.count", "current number of oplog writes that are in progress")
    private val diskStoreReadMeter = CounterStatisticMeter("diskregion.entry.read.count", "The total number of region entries that have been read from disk")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreReadTimer = CounterStatisticMeter("diskregion.entry.read.time", "The total amount of time spent reading from disk", meterUnit = "nanoseconds")
    private val diskStoreReadBytesMeter = CounterStatisticMeter("diskregion.read.bytes", "The total number of bytes that have been read from disk", meterUnit = "bytes")
    private val diskStoreRemoveMeter = CounterStatisticMeter("diskregion.entry.remove.count", "The total number of region entries that have been removed from disk")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreRemoveTimer = CounterStatisticMeter("diskregion.entry.remove.time", "The total amount of time spent removing from disk", meterUnit = "nanoseconds")
    private val diskStoreEntriesOnDiskOnlyMeter = GaugeStatisticMeter("diskregion.entry.diskregion.count", "The current number of entries whose value is on disk and is not in memory. This is true of overflowed entries. It is also true of recovered entries that have not yet been faulted in.")
    private val diskStoreEntriesOnDiskOnlyBytesMeter = GaugeStatisticMeter("diskregion.entry.diskregion.bytes", "The current number bytes on disk and not in memory. This is true of overflowed entries. It is also true of recovered entries that have not yet been faulted in.", meterUnit = "bytes")
    private val diskStoreEntriesInVMMeter = GaugeStatisticMeter("diskregion.entry.vm.count", "The current number of entries whose value resides in the VM. The value may also have been written to diskregion.")
    private val diskStoreRegionLocalInitializationMeter = GaugeStatisticMeter("diskregion.region.init.local.count", "The number of times that this region has been initialized solely from the local disk files (0 or 1)")
    private val diskStoreRegionRemoteInitializationMeter = GaugeStatisticMeter("diskregion.region.init.gii.count", "The number of times that this region has been initialized by doing GII from a peer (0 or 1)")


    override fun incNumOverflowOnDisk(delta: Long) {
        diskStoreEntriesOnDiskOnlyMeter.increment(delta)
    }

    override fun incNumEntriesInVM(delta: Long) {
        diskStoreEntriesInVMMeter.increment(delta)
    }

    override fun incNumOverflowBytesOnDisk(delta: Long) {
        diskStoreEntriesOnDiskOnlyBytesMeter.increment(delta)
    }

    override fun startWrite() {
        diskStoreWriteInProgressMeter.increment()
    }

    override fun incWrittenBytes(bytesWritten: Long) {
        diskStoreWriteBytesMeter.increment(bytesWritten)
    }

    override fun endWrite(start: Long, end: Long) {
        diskStoreWriteInProgressMeter.decrement()
        diskStoreWriteMeter.increment()
        diskStoreWriteTimer.increment(end - start)
    }

    override fun endRead(start: Long, end: Long, bytesRead: Long) {
        diskStoreReadMeter.increment()
        diskStoreReadTimer.increment(end - start)
        diskStoreReadBytesMeter.increment(bytesRead)
    }

    override fun endRemove(start: Long, end: Long) {
        diskStoreRemoveMeter.increment()
        diskStoreRemoveTimer.increment(end - start)
    }

    override fun incInitializations(local: Boolean) {
        if (local) {
            diskStoreRegionLocalInitializationMeter.increment()
        } else {
            diskStoreRegionRemoteInitializationMeter.increment()
        }
    }

    override fun getNumOverflowOnDisk(): Long {
        return diskStoreEntriesOnDiskOnlyMeter.getValue()
    }

    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getWrites(): Long = diskStoreWriteMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getWriteTime(): Long = diskStoreWriteTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBytesWritten(): Long = diskStoreWriteBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReads(): Long = diskStoreReadMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReadTime(): Long = diskStoreReadTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBytesRead(): Long = diskStoreReadBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoves(): Long = diskStoreRemoveMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoveTime(): Long = diskStoreRemoveTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumOverflowBytesOnDisk(): Long = diskStoreEntriesOnDiskOnlyBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumEntriesInVM(): Long = diskStoreEntriesInVMMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLocalInitializations(): Int =diskStoreRegionLocalInitializationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoteInitializations(): Int = diskStoreRegionRemoteInitializationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}