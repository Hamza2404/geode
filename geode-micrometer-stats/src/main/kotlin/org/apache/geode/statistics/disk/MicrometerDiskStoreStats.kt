package org.apache.geode.statistics.disk

import org.apache.geode.internal.cache.DiskStoreStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerDiskStoreStats(statisticsFactory: StatisticsFactory?,private val diskStoreName: String) :
        MicrometerMeterGroup(statisticsFactory,"DiskStoreStats-$diskStoreName"), DiskStoreStats {


    override fun getGroupTags(): Array<String> = arrayOf("diskStoreName", diskStoreName)

    override fun initializeStaticMeters() {
        registerMeter(diskStoreWriteToDiskMeter)
        registerMeter(diskStoreWriteToDiskTimer)
        registerMeter(diskStoreWriteToDiskBytesMeter)
        registerMeter(diskStoreFlushToDiskMeter)
        registerMeter(diskStoreFlushToDiskTimer)
        registerMeter(diskStoreFlushToDiskBytesMeter)
        registerMeter(diskStoreReadFromDiskMeter)
        registerMeter(diskStoreReadFromDiskTimer)
        registerMeter(diskStoreReadFromDiskBytesMeter)
        registerMeter(diskStoreRecoveryInProgressMeter)
        registerMeter(diskStoreRecoveryTimer)
        registerMeter(diskStoreRecoveryBytesMeter)
        registerMeter(diskStoreRecoveryEntriesCreateMeter)
        registerMeter(diskStoreRecoveryEntriesUpdateMeter)
        registerMeter(diskStoreRecoveryEntriesDestroyMeter)
        registerMeter(diskStoreRecoverySkipDueToLRUMeter)
        registerMeter(diskStoreRecoverySkipMeter)
        registerMeter(diskStoreOplogRecoveryMeter)
        registerMeter(diskStoreOplogRecoveryTimer)
        registerMeter(diskStoreOplogRecoveryBytesMeter)
        registerMeter(diskStoreEntryRemovedMeter)
        registerMeter(diskStoreEntryRemovedTimer)
        registerMeter(diskStoreQueueSizeMeter)
        registerMeter(diskStoreCompactInsertMeter)
        registerMeter(diskStoreCompactInsertTimer)
        registerMeter(diskStoreCompactUpdateMeter)
        registerMeter(diskStoreCompactUpdateTimer)
        registerMeter(diskStoreCompactDestroyMeter)
        registerMeter(diskStoreCompactDestroyTimer)
        registerMeter(diskStoreCompactInProgressMeter)
        registerMeter(diskStoreWriteInProgressMeter)
        registerMeter(diskStoreFlushesInProgressMeter)
        registerMeter(diskStoreCompactTimer)
        registerMeter(diskStoreCompactMeter)
        registerMeter(diskStoreOplogOpenMeter)
        registerMeter(diskStoreOplogCompactableMeter)
        registerMeter(diskStoreOplogInactiveMeter)
        registerMeter(diskStoreOplogReadMeter)
        registerMeter(diskStoreOplogSeekMeter)
        registerMeter(diskStoreRegionsNotRecoveredMeter)
        registerMeter(diskStoreBackupInProgressMeter)
        registerMeter(diskStoreBackupCompletedMeter)
    }

    private val diskStoreWriteToDiskMeter = GaugeStatisticMeter("disk.entries.write.count", "The total number of region entries that have been written to disk. A write is done every time an entry is created on disk or every time its value is modified on disk.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreWriteToDiskTimer = CounterStatisticMeter("disk.entries.write.time", "The total amount of time spent writing to disk", meterUnit = "nanoseconds")
    private val diskStoreWriteToDiskBytesMeter = GaugeStatisticMeter("disk.entries.write.bytes", "The total number of bytes that have been written to disk", meterUnit = "bytes")
    private val diskStoreFlushToDiskMeter = GaugeStatisticMeter("disk.entries.flush.count", "The total number of times the an entry has been flushed from the async queue.")
    private val diskStoreFlushToDiskTimer = TimerStatisticMeter("disk.entries.flush.time", "The total amount of time spent doing an async queue flush.", meterUnit = "nanoseconds")
    private val diskStoreFlushToDiskBytesMeter = GaugeStatisticMeter("disk.entries.flush.bytes", "The total number of bytes written to disk by async queue flushes.", meterUnit = "bytes")
    private val diskStoreReadFromDiskMeter = GaugeStatisticMeter("disk.entries.read.count", "The total number of region entries that have been read from disk")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreReadFromDiskTimer = CounterStatisticMeter("disk.entries.read.time", "The total amount of time spent reading from disk", meterUnit = "nanoseconds")
    private val diskStoreReadFromDiskBytesMeter = GaugeStatisticMeter("disk.entries.read.bytes", "The total number of bytes that have been read from disk", meterUnit = "bytes")
    private val diskStoreRecoveryInProgressMeter = GaugeStatisticMeter("disk.recovery.inprogress", "current number of persistent regions being recovered from disk")
    private val diskStoreRecoveryTimer = TimerStatisticMeter("disk.recovery.time", "The total amount of time spent doing a recovery", meterUnit = "nanoseconds")
    private val diskStoreRecoveryBytesMeter = GaugeStatisticMeter("disk.recovery.bytes", "The total number of bytes that have been read from disk during a recovery", meterUnit = "bytes")
    private val diskStoreRecoveryEntriesCreateMeter = GaugeStatisticMeter("disk.recovery.entry.operation.count", "The total number of entry create records processed while recovering oplog data.", arrayOf("operationType", "create"))
    private val diskStoreRecoveryEntriesUpdateMeter = GaugeStatisticMeter("disk.recovery.entry.operation.count", "The total number of entry update records processed while recovering oplog data.", arrayOf("operationType", "update"))
    private val diskStoreRecoveryEntriesDestroyMeter = GaugeStatisticMeter("disk.recovery.entry.operation.count", "The total number of entry destroy records processed while recovering oplog data.", arrayOf("operationType", "destroy"))
    private val diskStoreRecoverySkipDueToLRUMeter = GaugeStatisticMeter("disk.recovery.entry.skip.count", "The total number of entry values that did not need to be recovered due to the LRU.", arrayOf("reason", "lru"))
    private val diskStoreRecoverySkipMeter = GaugeStatisticMeter("disk.recovery.entry.skip.count", "The total number of oplog records skipped during recovery.", arrayOf("reason", "all"))
    private val diskStoreOplogRecoveryMeter = GaugeStatisticMeter("disk.oplog.recovery.count", "The total number of oplogs recovered")
    private val diskStoreOplogRecoveryTimer = TimerStatisticMeter("disk.oplog.recovery.time", "The total amount of time spent doing an oplog recovery", meterUnit = "nanoseconds")
    private val diskStoreOplogRecoveryBytesMeter = GaugeStatisticMeter("disk.oplog.recovery.bytes", "The total number of bytes that have been read from oplogs during a recovery", meterUnit = "bytes")
    private val diskStoreEntryRemovedMeter = GaugeStatisticMeter("disk.entries.remove.count", "The total number of region entries that have been removed from disk")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val diskStoreEntryRemovedTimer = CounterStatisticMeter("disk.entries.remove.time", "The total amount of time spent removing from disk", meterUnit = "nanoseconds")
    private val diskStoreQueueSizeMeter = GaugeStatisticMeter("disk.queue.count", "The current number of entries in the async queue waiting to be flushed to disk")
    private val diskStoreCompactInsertMeter = GaugeStatisticMeter("disk.compact.operation.count", "Total number of times an oplog compact did a db insert", arrayOf("operationType", "insert"))
    private val diskStoreCompactInsertTimer = TimerStatisticMeter("disk.compact.operation.time", "Total amount of time, in nanoseconds, spent doing inserts during a compact", arrayOf("operationType", "insert"), meterUnit = "nanoseconds")
    private val diskStoreCompactUpdateMeter = GaugeStatisticMeter("disk.compact.operation.count", "Total number of times an oplog compact did an update", arrayOf("operationType", "update"))
    private val diskStoreCompactUpdateTimer = TimerStatisticMeter("disk.compact.operation.time", "Total amount of time, in nanoseconds, spent doing updates during a compact", arrayOf("operationType", "update"), meterUnit = "nanoseconds")
    private val diskStoreCompactDestroyMeter = CounterStatisticMeter("disk.compact.operation.count", "Total number of times an oplog compact did a delete", arrayOf("operationType", "destroy"))
    private val diskStoreCompactDestroyTimer = TimerStatisticMeter("disk.compact.operation.time", "Total amount of time, in nanoseconds, spent doing deletes during a compact", arrayOf("operationType", "destroy"), meterUnit = "nanoseconds")
    private val diskStoreCompactInProgressMeter = GaugeStatisticMeter("disk.compact.inprogress.count", "current number of oplog compacts that are in progress")
    private val diskStoreWriteInProgressMeter = GaugeStatisticMeter("disk.oplog.write.inprogress.count", "current number of oplog writes that are in progress")
    private val diskStoreFlushesInProgressMeter = GaugeStatisticMeter("disk.oplog.flush.inprogress.count", "current number of oplog flushes that are in progress")
    private val diskStoreCompactTimer = TimerStatisticMeter("disk.compact.time", "Total amount of time, in nanoseconds, spent compacting oplogs", meterUnit = "nanoseconds")
    private val diskStoreCompactMeter = CounterStatisticMeter("disk.compact.count", "Total number of completed oplog compacts")
    private val diskStoreOplogOpenMeter = GaugeStatisticMeter("disk.oplog.open.count", "Current number of oplogs this disk store has open")
    private val diskStoreOplogCompactableMeter = GaugeStatisticMeter("disk.oplog.compactable.count", "Current number of oplogs ready to be compacted")
    private val diskStoreOplogInactiveMeter = GaugeStatisticMeter("disk.oplog.notactive.count", "Current number of oplogs that are no longer being written but are not ready ready to compact")
    private val diskStoreOplogReadMeter = CounterStatisticMeter("disk.oplog.read.count", "Total number of oplog reads")
    private val diskStoreOplogSeekMeter = CounterStatisticMeter("disk.oplog.seek.count", "Total number of oplog seeks")
    private val diskStoreRegionsNotRecoveredMeter = GaugeStatisticMeter("disk.recovery.regions.notrecovered.count", "The current number of regions that have been recovered but have not yet been created.")
    private val diskStoreBackupInProgressMeter = GaugeStatisticMeter("disk.backup.inprogress.count", "The current number of backups in progress on this disk store")
    private val diskStoreBackupCompletedMeter = GaugeStatisticMeter("disk.backup.completed.count", "The number of backups of this disk store that have been taking while this VM was alive")


    override fun setQueueSize(value: Int) {
        diskStoreQueueSizeMeter.setValue(value)
    }

    override fun incQueueSize(delta: Int) {
        diskStoreQueueSizeMeter.increment(delta)
    }

    override fun incUncreatedRecoveredRegions(delta: Int) {
        diskStoreRegionsNotRecoveredMeter.increment(delta)
    }

    override fun startWrite(): Long {
        diskStoreWriteInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun startFlush(): Long {
        diskStoreFlushesInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun incWrittenBytes(bytesWritten: Long, async: Boolean) {
        if (async) {
            diskStoreFlushToDiskBytesMeter.increment(bytesWritten)
        } else {
            diskStoreWriteToDiskBytesMeter.increment(bytesWritten)
        }
    }

    override fun endWrite(start: Long): Long {
        diskStoreWriteInProgressMeter.decrement()
        diskStoreWriteToDiskMeter.increment()
        diskStoreWriteToDiskTimer.increment(NOW_NANOS - start)
        return NOW_NANOS
    }

    override fun endFlush(start: Long) {
        diskStoreFlushesInProgressMeter.decrement()
        diskStoreFlushToDiskMeter.increment()
        diskStoreFlushToDiskTimer.recordValue(NOW_NANOS - start)
    }

    override fun startRead(): Long = NOW_NANOS

    override fun endRead(start: Long, bytesRead: Long): Long {
        diskStoreReadFromDiskMeter.increment()
        diskStoreReadFromDiskBytesMeter.increment(bytesRead)
        diskStoreReadFromDiskTimer.increment(NOW_NANOS - start)
        return NOW_NANOS
    }

    override fun startRecovery(): Long {
        diskStoreRecoveryInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun startCompaction(): Long {
        diskStoreCompactInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun startOplogRead(): Long = NOW_NANOS

    override fun endRecovery(start: Long, bytesRead: Long) {
        diskStoreRecoveryInProgressMeter.decrement()
        diskStoreRecoveryBytesMeter.increment(bytesRead)
        diskStoreRecoveryTimer.recordValue(NOW_NANOS - start)
    }

    override fun endCompaction(start: Long) {
        diskStoreCompactInProgressMeter.decrement()
        diskStoreCompactMeter.increment()
        diskStoreCompactTimer.recordValue(NOW_NANOS - start)
    }

    override fun endOplogRead(start: Long, bytesRead: Long) {
        diskStoreOplogRecoveryMeter.increment()
        diskStoreOplogRecoveryTimer.recordValue(NOW_NANOS - start)
        diskStoreOplogRecoveryBytesMeter.increment(bytesRead)
    }

    override fun incRecoveredEntryCreates() {
        diskStoreRecoveryEntriesCreateMeter.increment()
    }

    override fun incRecoveredEntryUpdates() {
        diskStoreRecoveryEntriesUpdateMeter.increment()
    }

    override fun incRecoveredEntryDestroys() {
        diskStoreRecoveryEntriesDestroyMeter.increment()
    }

    override fun incRecoveryRecordsSkipped() {
        diskStoreRecoverySkipMeter.increment()
    }

    override fun incRecoveredValuesSkippedDueToLRU() {
        diskStoreRecoverySkipDueToLRUMeter.increment()
    }

    override fun startRemove(): Long = NOW_NANOS


    override fun endRemove(start: Long): Long {
        diskStoreEntryRemovedMeter.increment()
        diskStoreEntryRemovedTimer.increment(NOW_NANOS - start)
        return NOW_NANOS
    }

    override fun incOplogReads() {
        diskStoreOplogReadMeter.increment()
    }

    override fun incOplogSeeks() {
        diskStoreOplogSeekMeter.increment()
    }

    override fun incInactiveOplogs(delta: Int) {
        diskStoreOplogInactiveMeter.increment(delta)
    }

    override fun incCompactableOplogs(delta: Int) {
        diskStoreOplogCompactableMeter.increment(delta)
    }

    override fun endCompactionDeletes(count: Int, delta: Long) {
        diskStoreCompactDestroyMeter.increment(count)
        diskStoreCompactDestroyTimer.recordValue(delta)
    }

    override fun endCompactionInsert(start: Long) {
        diskStoreCompactInsertMeter.increment()
        diskStoreCompactInsertTimer.recordValue(NOW_NANOS - start)
    }

    override fun endCompactionUpdate(start: Long) {
        diskStoreCompactUpdateMeter.increment()
        diskStoreCompactUpdateTimer.recordValue(NOW_NANOS - start)
    }

    override fun getStatTime(): Long = NOW_NANOS

    override fun incOpenOplogs() {
        diskStoreOplogOpenMeter.increment()
    }

    override fun decOpenOplogs() {
        diskStoreOplogOpenMeter.decrement()
    }

    override fun startBackup() {
        diskStoreBackupInProgressMeter.increment()
    }

    override fun endBackup() {
        diskStoreBackupInProgressMeter.decrement()
        diskStoreBackupCompletedMeter.increment()
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getWrites(): Long = diskStoreWriteToDiskMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getWriteTime(): Long = diskStoreWriteToDiskTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBytesWritten(): Long = diskStoreWriteToDiskBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReads(): Long = diskStoreReadFromDiskMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReadTime(): Long = diskStoreReadFromDiskTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBytesRead(): Long = diskStoreReadFromDiskBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoves(): Long = diskStoreEntryRemovedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoveTime(): Long = diskStoreEntryRemovedTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getQueueSize(): Long = diskStoreQueueSizeMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFlushes(): Long = diskStoreFlushToDiskMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}