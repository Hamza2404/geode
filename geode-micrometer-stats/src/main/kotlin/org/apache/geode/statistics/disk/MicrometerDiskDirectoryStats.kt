package org.apache.geode.statistics.disk

import org.apache.geode.internal.cache.DiskDirectoryStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

class MicrometerDiskDirectoryStats(statisticsFactory: StatisticsFactory?,private val owner: String) :
        MicrometerMeterGroup(statisticsFactory,"DiskDirectoryStats-$owner"), DiskDirectoryStats {

    override fun getGroupTags(): Array<String> = arrayOf("directoryOwner", owner)

    override fun initializeStaticMeters() {
        registerMeter(diskDirectoryDiskSpaceCountMeter)
        registerMeter(diskDirectoryDiskMaxSpaceMeter)
        registerMeter(diskDirectoryVolumeSizeMeter)
        registerMeter(diskDirectoryVolumeFreeMeter)
        registerMeter(diskDirectoryVolumeFreeCheckMeter)
        registerMeter(diskDirectoryVolumeFreeCheckTimer)
    }

    private val diskDirectoryDiskSpaceCountMeter = GaugeStatisticMeter("directory.disk.space.count", "The total number of bytes currently being used on disk in this directory for oplog files.")
    private val diskDirectoryDiskMaxSpaceMeter = GaugeStatisticMeter("directory.disk.space.max", "The configured maximum number of bytes allowed in this directory for oplog files. Note that some product configurations allow this maximum to be exceeded.")
    private val diskDirectoryVolumeSizeMeter = GaugeStatisticMeter("directory.volume.size", "The total size in bytes of the disk volume")
    private val diskDirectoryVolumeFreeMeter = GaugeStatisticMeter("directory.volume.free.size", "The total free space in bytes on the disk volume")
    private val diskDirectoryVolumeFreeCheckMeter = GaugeStatisticMeter("directory.volume.free.check.count", "The total number of disk space checks")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskDirectoryVolumeFreeCheckTimer = CounterStatisticMeter("directory.volume.free.check.time", "The total time spent checking disk usage", meterUnit = "nanoseconds")


    override fun incDiskSpace(delta: Long) {
        diskDirectoryDiskSpaceCountMeter.increment(delta)
    }

    override fun setMaxSpace(v: Long) {
        diskDirectoryDiskMaxSpaceMeter.setValue(v)
    }

    override fun addVolumeCheck(total: Long, free: Long, time: Long) {
        diskDirectoryVolumeFreeCheckMeter.increment()
        diskDirectoryVolumeFreeCheckTimer.increment(time)
        diskDirectoryVolumeSizeMeter.increment(total)
        diskDirectoryVolumeFreeMeter.increment(free)
    }

    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDiskSpace(): Long = diskDirectoryDiskSpaceCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}