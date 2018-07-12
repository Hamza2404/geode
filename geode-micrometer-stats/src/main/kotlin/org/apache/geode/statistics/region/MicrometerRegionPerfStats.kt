package org.apache.geode.statistics.region

import org.apache.geode.internal.cache.CachePerfStats
import org.apache.geode.internal.cache.RegionPerfStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.cache.MicrometerCachePerfStats
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerStatisticsManager

class MicrometerRegionPerfStats(statisticsFactory: StatisticsFactory?, private var cachePerfStats: CachePerfStats, private val regionName: String) :
        MicrometerCachePerfStats(statisticsFactory, regionName), RegionPerfStats {

    override fun incReliableQueuedOps(inc: Int) {
        super.incReliableQueuedOps(inc)
        this.cachePerfStats.incReliableQueuedOps(inc)
    }

    override fun incReliableQueueSize(inc: Int) {
        super.incReliableQueueSize(inc)
        this.cachePerfStats.incReliableQueueSize(inc)
    }

    override fun incReliableRegions(inc: Int) {
        super.incReliableRegions(inc)
        this.cachePerfStats.incReliableRegions(inc)
    }

    override fun incReliableRegionsMissing(inc: Int) {
        super.incReliableRegionsMissing(inc)
        this.cachePerfStats.incReliableRegionsMissing(inc)
    }

    override fun incReliableRegionsQueuing(inc: Int) {
        super.incReliableRegionsQueuing(inc)
        this.cachePerfStats.incReliableRegionsQueuing(inc)
    }

    override fun incReliableRegionsMissingFullAccess(inc: Int) {
        super.incReliableRegionsMissingFullAccess(inc)
        this.cachePerfStats.incReliableRegionsMissingFullAccess(inc)
    }

    override fun incReliableRegionsMissingLimitedAccess(inc: Int) {
        super.incReliableRegionsMissingLimitedAccess(inc)
        this.cachePerfStats.incReliableRegionsMissingLimitedAccess(inc)
    }

    override fun incReliableRegionsMissingNoAccess(inc: Int) {
        super.incReliableRegionsMissingNoAccess(inc)
        this.cachePerfStats.incReliableRegionsMissingNoAccess(inc)
    }

    override fun incQueuedEvents(inc: Int) {
        super.incQueuedEvents(inc)
        this.cachePerfStats.incQueuedEvents(inc)
    }

    override fun startLoad(): Long {
        super.startLoad()
        return this.cachePerfStats.startLoad()
    }

    override fun endLoad(start: Long) {
        super.endLoad(start)
        // need to think about timings
        this.cachePerfStats.endLoad(start)
    }

    override fun startNetload(): Long {
        super.startNetload()
        return this.cachePerfStats.startNetload()
    }

    override fun endNetload(start: Long) {
        super.endNetload(start)
        this.cachePerfStats.endNetload(start)
    }

    override fun startNetsearch(): Long {
        super.startNetsearch()
        return this.cachePerfStats.startNetsearch()
    }

    override fun endNetsearch(start: Long) {
        super.endNetsearch(start)
        this.cachePerfStats.endNetsearch(start)
    }

    override fun startCacheWriterCall(): Long {
        super.startCacheWriterCall()
        return this.cachePerfStats.startCacheWriterCall()
    }

    override fun endCacheWriterCall(start: Long) {
        super.endCacheWriterCall(start)
        this.cachePerfStats.endCacheWriterCall(start)
    }

    override fun startCacheListenerCall(): Long {
        super.startCacheListenerCall()
        return this.cachePerfStats.startCacheListenerCall()
    }

    override fun endCacheListenerCall(start: Long) {
        super.endCacheListenerCall(start)
        this.cachePerfStats.endCacheListenerCall(start)
    }

    override fun startGetInitialImage(): Long {
        super.startGetInitialImage()
        return this.cachePerfStats.startGetInitialImage()
    }

    override fun endGetInitialImage(start: Long) {
        super.endGetInitialImage(start)
        this.cachePerfStats.endGetInitialImage(start)
    }

    override fun endNoGIIDone(start: Long) {
        super.endNoGIIDone(start)
        this.cachePerfStats.endNoGIIDone(start)
    }

    override fun incGetInitialImageKeysReceived() {
        super.incGetInitialImageKeysReceived()
        this.cachePerfStats.incGetInitialImageKeysReceived()
    }

    override fun startIndexUpdate(): Long {
        super.startIndexUpdate()
        return this.cachePerfStats.startIndexUpdate()
    }

    override fun endIndexUpdate(start: Long) {
        super.endIndexUpdate(start)
        this.cachePerfStats.endIndexUpdate(start)
    }

    override fun incRegions(inc: Int) {
        super.incRegions(inc)
        this.cachePerfStats.incRegions(inc)

    }

    override fun incPartitionedRegions(inc: Int) {
        super.incPartitionedRegions(inc)
        this.cachePerfStats.incPartitionedRegions(inc)
    }

    override fun incDestroys() {
        super.incDestroys()
        this.cachePerfStats.incDestroys()
    }

    override fun incCreates() {
        super.incCreates()
        this.cachePerfStats.incCreates()
    }

    override fun incInvalidates() {
        super.incInvalidates()
        this.cachePerfStats.incInvalidates()
    }

    override fun incTombstoneCount(amount: Int) {
        super.incTombstoneCount(amount)
        this.cachePerfStats.incTombstoneCount(amount)
    }

    override fun incTombstoneGCCount() {
        super.incTombstoneGCCount()
        this.cachePerfStats.incTombstoneGCCount()
    }

    override fun incClearTimeouts() {
        super.incClearTimeouts()
        this.cachePerfStats.incClearTimeouts()
    }

    override fun incConflatedEventsCount() {
        super.incConflatedEventsCount()
        this.cachePerfStats.incConflatedEventsCount()
    }

    override fun endGet(start: Long, miss: Boolean) {
        super.endGet(start, miss)
        this.cachePerfStats.endGet(start, miss)
    }

    override fun endPut(start: Long, isUpdate: Boolean): Long {
        val endPut = super.endPut(start, isUpdate)
        this.cachePerfStats.endPut(start, isUpdate)
        return endPut
    }

    override fun endPutAll(start: Long) {
        super.endPutAll(start)
        this.cachePerfStats.endPutAll(start)
    }

    override fun endQueryExecution(executionTime: Long) {
        super.endQueryExecution(executionTime)
        this.cachePerfStats.endQueryExecution(executionTime)
    }

    override fun endQueryResultsHashCollisionProbe(start: Long) {
        super.endQueryResultsHashCollisionProbe(start)
        this.cachePerfStats.endQueryResultsHashCollisionProbe(start)
    }

    override fun incQueryResultsHashCollisions() {
        super.incQueryResultsHashCollisions()
        this.cachePerfStats.incQueryResultsHashCollisions()
    }

    override fun incTxConflictCheckTime(delta: Long) {
        super.incTxConflictCheckTime(delta)
        this.cachePerfStats.incTxConflictCheckTime(delta)
    }

    override fun txSuccess(opTime: Long, txLifeTime: Long, txChanges: Int) {
        super.txSuccess(opTime, txLifeTime, txChanges)
        this.cachePerfStats.txSuccess(opTime, txLifeTime, txChanges)
    }

    override fun txFailure(opTime: Long, txLifeTime: Long, txChanges: Int) {
        super.txFailure(opTime, txLifeTime, txChanges)
        this.cachePerfStats.txFailure(opTime, txLifeTime, txChanges)
    }

    override fun txRollback(opTime: Long, txLifeTime: Long, txChanges: Int) {
        super.txRollback(opTime, txLifeTime, txChanges)
        this.cachePerfStats.txRollback(opTime, txLifeTime, txChanges)
    }

    override fun incEventQueueSize(items: Int) {
        super.incEventQueueSize(items)
        this.cachePerfStats.incEventQueueSize(items)
    }

    override fun incEventQueueThrottleCount(items: Int) {
        super.incEventQueueThrottleCount(items)
        this.cachePerfStats.incEventQueueThrottleCount(items)
    }

    override fun incEventQueueThrottleTime(nanos: Long) {
        super.incEventQueueThrottleTime(nanos)
        this.cachePerfStats.incEventQueueThrottleTime(nanos)
    }

    override fun incEventThreads(items: Int) {
        super.incEventThreads(items)
        this.cachePerfStats.incEventThreads(items)
    }

    override fun incEntryCount(delta: Int) {
        super.incEntryCount(delta)
        this.cachePerfStats.incEntryCount(delta)
    }

    override fun incRetries() {
        super.incRetries()
        this.cachePerfStats.incRetries()
    }

    override fun incDiskTasksWaiting() {
        super.incDiskTasksWaiting()
        this.cachePerfStats.incDiskTasksWaiting()
    }

    override fun decDiskTasksWaiting() {
        super.decDiskTasksWaiting()
        this.cachePerfStats.decDiskTasksWaiting()
    }

    override fun decDiskTasksWaiting(count: Int) {
        super.decDiskTasksWaiting(count)
        this.cachePerfStats.decDiskTasksWaiting(count)
    }

    override fun incEvictorJobsStarted() {
        super.incEvictorJobsStarted()
        this.cachePerfStats.incEvictorJobsStarted()
    }

    override fun incEvictorJobsCompleted() {
        super.incEvictorJobsCompleted()
        this.cachePerfStats.incEvictorJobsCompleted()
    }

    override fun incEvictorQueueSize(delta: Int) {
        super.incEvictorQueueSize(delta)
        this.cachePerfStats.incEvictorQueueSize(delta)
    }

    override fun incEvictWorkTime(delta: Long) {
        super.incEvictWorkTime(delta)
        this.cachePerfStats.incEvictWorkTime(delta)
    }

    override fun incClearCount() {
        super.incClearCount()
        this.cachePerfStats.incClearCount()
    }

    override fun incPRQueryRetries() {
        super.incPRQueryRetries()
        this.cachePerfStats.incPRQueryRetries()
    }

    override fun incMetaDataRefreshCount() {
        super.incMetaDataRefreshCount()
        this.cachePerfStats.incMetaDataRefreshCount()
    }

    override fun endImport(entryCount: Long, start: Long) {
        super.endImport(entryCount, start)
        this.cachePerfStats.endImport(entryCount, start)
    }

    override fun endExport(entryCount: Long, start: Long) {
        super.endExport(entryCount, start)
        this.cachePerfStats.endExport(entryCount, start)
    }

    override fun startCompression(): Long {
        super.startCompression()
        return this.cachePerfStats.startCompression()
    }

    override fun endCompression(startTime: Long, startSize: Long, endSize: Long) {
        super.endCompression(startTime, startSize, endSize)
        this.cachePerfStats.endCompression(startTime, startSize, endSize)
    }

    override fun startDecompression(): Long {
        super.startDecompression()
        return this.cachePerfStats.startDecompression()
    }

    override fun endDecompression(startTime: Long) {
        super.endDecompression(startTime)
        this.cachePerfStats.endDecompression(startTime)
    }
}