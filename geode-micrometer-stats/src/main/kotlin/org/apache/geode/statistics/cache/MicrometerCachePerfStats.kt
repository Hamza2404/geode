package org.apache.geode.statistics.cache

import org.apache.geode.distributed.internal.PoolStatHelper
import org.apache.geode.distributed.internal.QueueStatHelper
import org.apache.geode.internal.cache.CachePerfStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

open class MicrometerCachePerfStats @JvmOverloads constructor(statisticsFactory: StatisticsFactory?,private val regionName: String? = null) :
        MicrometerMeterGroup(statisticsFactory,"CachePerfStats${regionName?.let { "-$it" }
                ?: ""}"), CachePerfStats {

    override fun getGroupTags() = regionName?.let { arrayOf("regionName", regionName) }
            ?: arrayOf("regionName", "allRegions")

    private val cachePerfStatsPrefix: String by lazy {
        regionName?.let { "region" } ?: "cachePerf"
    }

    private val regionLoadsInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.loads.inprogress", "Current number of threads in this cache doing a cache load.")
    private val regionLoadsCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.loads.completed", "Total number of times a load on this cache has completed (as a result of either a local get() or a remote netload).")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionLoadsTimer = CounterStatisticMeter("$cachePerfStatsPrefix.load.time", "Total time spent invoking loaders on this cache.", meterUnit = "nanoseconds")
    private val regionNetLoadInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.netload.inprogress", "Current number of threads doing a network load initiated by a get() in this cache.")
    private val regionNetLoadCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.netload.completed", "Total number of times a network load initiated on this cache has completed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionNetLoadTimer = CounterStatisticMeter("$cachePerfStatsPrefix.netloadTime", "Total time spent doing network loads on this cache.", meterUnit = "nanoseconds")
    private val regionNetSearchInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.netsearch.inprogress", "Current number of threads doing a network search initiated by a get() in this cache.")
    private val regionNetSearchCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.netsearch.completed", "Total number of times network searches initiated by this cache have completed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionNetSearchTimer = CounterStatisticMeter("$cachePerfStatsPrefix.netsearch.time", "Total time spent doing network searches for cache values.", meterUnit = "nanoseconds")
    private val regionCacheWriterInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.cachewriter.inprogress", "Current number of threads doing a cache writer call.")
    private val regionCacheWriterCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.cachewriter.completed", "Total number of times a cache writer call has completed.")
    private val regionCacheWriteTimer = TimerStatisticMeter("$cachePerfStatsPrefix.cachewriter.time", "Total time spent doing cache writer calls.", meterUnit = "nanoseconds")

    private val regionCacheListenerInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.cachelistener.inprogress", "Current number of threads doing a cache listener call.")
    private val regionCacheListenerCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.cachelistener.completed", "Total number of times a cache listener call has completed.")
    private val regionCacheListenerTimer = TimerStatisticMeter("$cachePerfStatsPrefix.cachelistener.time", "Total time spent doing cache listener calls.", meterUnit = "nanoseconds")
    private val regionIndexUpdateInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.index.operations.inprogress", "Current number of ops in progress", arrayOf("operation", "update"))
    private val regionIndexUpdateCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.index.operations.completed", "Total number of ops that haves completed", arrayOf("operation", "update"))
    private val regionIndexUpdateTimeMeter = TimerStatisticMeter("$cachePerfStatsPrefix.index.operations.time", "Total amount of time spent doing this op", arrayOf("operation", "update"), meterUnit = "nanoseconds")
    private val regionIndexInitializationInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.index.operations.inprogress", "Current number of index initializations in progress", arrayOf("operation", "initialization"))
    private val regionIndexInitializationCompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.index.operations.completed", "Total number of index initializations that have completed", arrayOf("operation", "initialization"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionIndexInitializationTimeMeter = CounterStatisticMeter("$cachePerfStatsPrefix.index.operations.time", "Total amount of time spent initializing indexes", arrayOf("operation", "initialization"), meterUnit = "nanoseconds")
    private val regionGIIInProgressMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.getinitialimages.inprogress", "Current number of getInitialImage operations currently in progress.")
    private val regionGIICompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.getinitialimages.completed", "Total number of times getInitialImages (both delta and full GII) initiated by this cache have completed.")
    private val regionDeltaGIICompletedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.getinitialimages.delta.completed", "Total number of times delta getInitialImages initiated by this cache have completed.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionGIITimer = CounterStatisticMeter("$cachePerfStatsPrefix.getinitialimages.time", "Total time spent doing getInitialImages for region creation.", meterUnit = "nanoseconds")
    private val regionGIIKeysReceivedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.getinitialimages.keys.received", "Total number of keys received while doing getInitialImage operations.")
    private val numberOfRegionsMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.regions.count", "The current number of regions in the cache.", arrayOf("regionType", "all"))
    private val numberOfPartitionedRegionsMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.regions.count", "The current number of partitioned regions in the cache.", arrayOf("regionType", "partitioned"))
    private val regionDestroyOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times a cache object entry has been destroyed in this cache.", arrayOf("operationType", "destroy"))
    private val regionUpdateOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of updates originating remotely that have been applied to this cache.", arrayOf("operationType", "update"))
    private val regionUpdateOperationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.operations.time", "Total time spent performing an update.", arrayOf("operationType", "update"), meterUnit = "nanoseconds")
    private val regionInvalidateOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times an existing cache object entry value in this cache has been invalidated", arrayOf("operationType", "invalidate"))
    private val regionGetOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times a successful get has been done on this cache.", arrayOf("operationType", "get"))
    private val regionGetOperationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.operations.time", "Total time spent doing get operations from this cache (including netsearch and netload)", arrayOf("operationType", "get"), meterUnit = "nanoseconds")
    private val regionMissesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.misses.count", "Total number of times a get on the cache did not find a value already in local memory. The number of hits (i.e. gets that did not miss) can be calculated by subtracting misses from gets.")
    private val regionCreateOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times an entry is added to this cache.", arrayOf("operationType", "create"))
    private val regionPutOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times an entry is added or replaced in this cache as a result of a local operation (put() create() or get() which results in load, netsearch, or netloading a value). Note that this only counts puts done explicitly on this cache. It does not count updates pushed from other caches.", arrayOf("operationType", "put"))
    private val regionPutOperationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.operations.time", "Total time spent adding or replacing an entry in this cache as a result of a local operation.  This includes synchronizing on the map, invoking cache callbacks, sending messages to other caches and waiting for responses (if required).", arrayOf("operationType", "put"), meterUnit = "nanoseconds")
    private val regionPutAllOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times a map is added or replaced in this cache as a result of a local operation. Note that this only counts putAlls done explicitly on this cache. It does not count updates pushed from other caches.", arrayOf("operationType", "putAll"))
    private val regionPutAllOperationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.operations.time", "Total time spent replacing a map in this cache as a result of a local operation.  This includes synchronizing on the map, invoking cache callbacks, sending messages to other caches and waiting for responses (if required).", arrayOf("operationType", "putAll"), meterUnit = "nanoseconds")
    private val regionRemoveAllOperationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of removeAll operations that originated in this cache. Note that this only counts removeAlls done explicitly on this cache. It does not count removes pushed from other caches.", arrayOf("operationType", "removeAll"))
    private val regionRemoveAllOperationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.operations.time", "Total time spent performing removeAlls that originated in this cache. This includes time spent waiting for the removeAll to be done in remote caches (if required).", arrayOf("operationType", "destroy"), meterUnit = "nanoseconds")

    private val regionEventQueueSizeMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eventqueue.size", "The number of cache events waiting to be processed.")
    private val regionEventQueueThrottleMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eventqueue.throttle.count", "The total number of times a thread was delayed in adding an event to the event queue.")
    private val regionEventQueueThrottleTimer = TimerStatisticMeter("$cachePerfStatsPrefix.eventqueue.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the event queue throttle.", meterUnit = "nanoseconds")
    private val regionEventQueueThreadMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eventqueue.thread.count", "The number of threads currently processing events.")

    private val regionQueryExecutionMeter = CounterStatisticMeter("$cachePerfStatsPrefix.query.execution.count", "Total number of times some query has been executed")
    private val regionQueryExecutionTimer = TimerStatisticMeter("$cachePerfStatsPrefix.query.execution.time", "Total time spent executing queries", meterUnit = "nanoseconds")
    private val regionQueryResultsHashCollisionsMeter = CounterStatisticMeter("$cachePerfStatsPrefix.query.results.hashcollisions.count", "Total number of times an hash code collision occurred when inserting an object into an OQL result set or rehashing it")
    private val regionQueryResultsHashCollisionsTimer = TimerStatisticMeter("$cachePerfStatsPrefix.query.results.hashcollisions.time", "Total time spent probing the hashtable in an OQL result set due to hash code collisions, includes reads, writes, and rehashes", meterUnit = "nanoseconds")
    private val regionQueryExecutionRetryMeter = CounterStatisticMeter("$cachePerfStatsPrefix.query.retries.count", "Total number of times an OQL Query on a Partitioned Region had to be retried")

    private val regionTransactionCommitMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.count", "Total number times a transaction commit has succeeded.", arrayOf("commitState", "all"))
    private val regionTransactionCommitChangesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.changes", "Total number of changes made by committed transactions.", arrayOf("commitState", "all"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionTransactionCommitTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.time", "The total amount of time, in nanoseconds, spent doing successful transaction commits.", arrayOf("commitState", "all"), meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionTransactionCommitSuccessTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.open.time", "The total amount of time, in nanoseconds, spent in a transaction before a successful commit. The time measured starts at transaction begin and ends when commit is called.", arrayOf("commitState", "success"), meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionTransactionCommitFailedTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.open.time", "The total amount of time, in nanoseconds, spent in a transaction before a failed commit. The time measured starts at transaction begin and ends when commit is called.", arrayOf("commitState", "failed"), meterUnit = "nanoseconds")
    private val regionTransactionFailureMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.count", "Total number times a transaction commit has failed.", arrayOf("commitState", "failed"))
    private val regionTransactionFailureChangesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.changes", "Total number of changes lost by failed transactions.", arrayOf("commitState", "failed"), "changes")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionTransactionFailureTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.commit.time", "The total amount of time, in nanoseconds, spent doing failed transaction commits.", arrayOf("commitState", "failed"), meterUnit = "nanoseconds")
    private val regionTransactionRollbackMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.rollback.count", "Total number times a transaction has been explicitly rolled back.")
    private val regionTransactionRollbackChangesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.rollback.changes", "Total number of changes lost by explicit transaction rollbacks.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionTransactionRollbackTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.rollback.time", "The total amount of time, in nanoseconds, spent doing explicit transaction rollbacks.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionOpenTransactionRollbackTimer = CounterStatisticMeter("$cachePerfStatsPrefix.transaction.open.time", "The total amount of time, in nanoseconds, spent in a transaction before an explicit rollback. The time measured starts at transaction begin and ends when rollback is called.", arrayOf("commitState", "rollback"), meterUnit = "nanoseconds")
    private val regionTransactionConflictCheckTimer = TimerStatisticMeter("$cachePerfStatsPrefix.transaction.conflictcheck.time", "The total amount of time, in nanoseconds, spent doing conflict checks during transaction commit", meterUnit = "nanoseconds")

    private val regionReliableQueuedOperationsMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queued.operations.count", "Current number of cache operations queued for distribution to required roles.")
    private val regionReliableQueuedOperationsMegaBytesMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queued.operations.bytes", "Current size in megabytes of disk used to queue for distribution to required roles.", meterUnit = "megabytes")
    private val regionReliableRegionMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.count", "Current number of regions configured for reliability.")
    private val regionReliableRegionMissingMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.missing", "Current number regions configured for reliability that are missing required roles.")
    private val regionReliableRegionQueuingMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.queuing", "Current number regions configured for reliability that are queuing for required roles.")
    private val regionReliableRegionMissingFullAccessMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.missing.access", "Current number of regions configured for reliablity that are missing require roles with full access", arrayOf("access", "full"))
    private val regionReliableRegionMissingLimitedAccessMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.missing.access", "Current number of regions configured for reliablity that are missing required roles with Limited access", arrayOf("access", "full"))
    private val regionReliableRegionMissingNoAccessMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.reliable.queues.$cachePerfStatsPrefix.missing.access", "Current number of regions configured for reliablity that are missing required roles with No access", arrayOf("access", "none"))

    private val regionEntryCounterMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.entries", "Current number of entries in the cache. This does not include any entries that are tombstones. See tombstoneCount.")
    private val regionEventsQueuedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.linked.events.queued", "Number of events attached to other events for callback invocation")
    private val regionOperationRetryMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operation.retries.count", "Number of times a concurrent destroy followed by a create has caused an entry operation to need to retry.")
    private val regionClearMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.count", "The total number of times a clear has been done on this cache.", arrayOf("operationType", "clear"))

    private val diskTasksWaitingMeter = GaugeStatisticMeter("disk.tasks.waiting", "Current number of disk tasks (oplog compactions, asynchronous recoveries, etc) that are waiting for a thread to run the operation")
    private val regionConflatedEventsMeter = CounterStatisticMeter("$cachePerfStatsPrefix.events.conflated.count", "Number of events not delivered due to conflation.  Typically this means that the event arrived after a later event was already applied to the cache.")
    private val regionTombstoneEntryMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.entries.tombstones", "Number of destroyed entries that are retained for concurrent modification detection")
    private val regionTombstoneEntryGCMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.tombstones.gc", "Number of garbage-collections performed on destroyed entries")
    private val regionReplicatedTombstoneBytesMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.tombstones.replicated.bytes", "Amount of memory consumed by destroyed entries in replicated or partitioned regions", meterUnit = "bytes")
    private val regionNonReplicatedTombstoneBytesMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.tombstones.nonreplicated.bytes", "Amount of memory consumed by destroyed entries in non-replicated regions", meterUnit = "bytes")
    private val regionOperationClearTimeoutMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operation.clear.timeout", "Number of timeouts waiting for events concurrent to a clear() operation to be received and applied before performing the clear()")
    private val regionEvictionJobsStartedMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eviction.job", "Number of evictor jobs started", arrayOf("status", "started"))
    private val regionEvictionJobsCompletedMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eviction.job", "Number of evictor jobs completed", arrayOf("status", "completed"))
    private val regionEvictionQueueSizeMeter = GaugeStatisticMeter("$cachePerfStatsPrefix.eviction.queue.size", "Number of jobs waiting to be picked up by evictor threads")
    private val regionEvictionTimer = TimerStatisticMeter("$cachePerfStatsPrefix.eviction.time", "Total time spent doing eviction work in background threads", meterUnit = "nanoseconds")

    private val regionNonSingleHopCountMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operation.nonsingle.hop.count", "Total number of times client request observed more than one hop during operation.")
    private val regionMetaDataRefreshCountMeter = CounterStatisticMeter("$cachePerfStatsPrefix.metadata.refresh.count", "Total number of times the meta data is refreshed due to hopping observed.")

    private val regionDeltaUpdateMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.count", "The total number of times entries in this cache are updated through delta bytes.", arrayOf("operationType", "update"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionDeltaUpdateTimer = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.time", "Total time spent applying the received delta bytes to entries in this cache.", arrayOf("operationType", "update"), meterUnit = "nanoseconds")
    private val regionDeltaUpdateFailedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.failed", "The total number of times entries in this cache failed to be updated through delta bytes.", arrayOf("operationType", "update"))
    private val regionDeltaPreparedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.prepared", "The total number of times delta was prepared in this cache.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionDeltaPreparedTimer = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.prepared.time", "Total time spent preparing delta bytes in this cache.", meterUnit = "nanoseconds")
    private val regionDeltaSentMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.sent", "The total number of times delta was sent to remote caches. This excludes deltas sent from server to client.")
    private val regionDeltaSentFullValuesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operations.delta.sent.full", "The total number of times a full value was sent to a remote cache.")
    private val regionDeltaFullValuesRequestedMeter = CounterStatisticMeter("$cachePerfStatsPrefix.operatations.delta.request.full", "The total number of times a full value was requested by this cache.")
    private val regionImportedEntryMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.imported.count", "The total number of entries imported from a snapshot file.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionImportedEntryTimer = CounterStatisticMeter("$cachePerfStatsPrefix.entries.imported.time", "The total time spent importing entries from a snapshot file.", meterUnit = "nanoseconds")
    private val regionExportedEntryMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.exported.count", "The total number of entries exported into a snapshot file.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionExportedEntryTimer = CounterStatisticMeter("$cachePerfStatsPrefix.entries.exported.time", "The total time spent exporting entries into a snapshot file.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionEntryCompressTimer = CounterStatisticMeter("$cachePerfStatsPrefix.entries.compress.time", "The total time spent compressing data.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val regionEntryDecompressTimer = CounterStatisticMeter("$cachePerfStatsPrefix.entries.decompress.time", "The total time spent decompressing data.", meterUnit = "nanoseconds")
    private val regionEntryCompressMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.compress.count", "The total number of compression operations.")
    private val regionEntryDecompressMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.decompress.count", "The total number of decompression operations.")
    private val regionCompressionBeforeBytesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.compress.before.bytes", "The total number of bytes before compressing.", meterUnit = "bytes")
    private val regionCompressionAfterBytesMeter = CounterStatisticMeter("$cachePerfStatsPrefix.entries.compress.after.bytes", "The total number of bytes after compressing.", meterUnit = "bytes")
    private val regionEvictionByCriteriaMeter = CounterStatisticMeter("$cachePerfStatsPrefix.evition.criteria.count", "The total number of entries evicted")
    private val regionEvictionByCriteriaTimer = TimerStatisticMeter("$cachePerfStatsPrefix.evition.criteria.time", "Time taken for eviction process", meterUnit = "nanoseconds")
    private val regionEvictionByCriteriaInProgressMeter = CounterStatisticMeter("$cachePerfStatsPrefix.evition.criteria.inprogress", "Total number of evictions in progress")
    private val regionEvictionByCriteriaEvaluationMeter = CounterStatisticMeter("$cachePerfStatsPrefix.evition.criteria.evaluation.count", "Total number of evaluations for eviction")
    private val regionEvictionByCriteriaEvaluationTimer = TimerStatisticMeter("$cachePerfStatsPrefix.evition.criteria.evaluation.time", "Total time taken for evaluation of user expression during eviction", meterUnit = "nanoseconds")

    override fun initializeStaticMeters() {
        registerMeter(regionLoadsInProgressMeter)
        registerMeter(regionLoadsCompletedMeter)
        registerMeter(regionLoadsTimer)
        registerMeter(regionNetLoadInProgressMeter)
        registerMeter(regionNetLoadCompletedMeter)
        registerMeter(regionNetLoadTimer)
        registerMeter(regionNetSearchInProgressMeter)
        registerMeter(regionNetSearchCompletedMeter)
        registerMeter(regionNetSearchTimer)
        registerMeter(regionCacheWriterInProgressMeter)
        registerMeter(regionCacheWriterCompletedMeter)
        registerMeter(regionCacheWriteTimer)

        registerMeter(regionCacheListenerInProgressMeter)
        registerMeter(regionCacheListenerCompletedMeter)
        registerMeter(regionCacheListenerTimer)
        registerMeter(regionIndexUpdateInProgressMeter)
        registerMeter(regionIndexUpdateCompletedMeter)
        registerMeter(regionIndexUpdateTimeMeter)
        registerMeter(regionIndexInitializationInProgressMeter)
        registerMeter(regionIndexInitializationCompletedMeter)
        registerMeter(regionIndexInitializationTimeMeter)
        registerMeter(regionGIIInProgressMeter)
        registerMeter(regionGIICompletedMeter)
        registerMeter(regionDeltaGIICompletedMeter)
        registerMeter(regionGIITimer)
        registerMeter(regionGIIKeysReceivedMeter)
        registerMeter(numberOfRegionsMeter)
        registerMeter(numberOfPartitionedRegionsMeter)
        registerMeter(regionDestroyOperationMeter)
        registerMeter(regionUpdateOperationMeter)
        registerMeter(regionUpdateOperationTimer)
        registerMeter(regionInvalidateOperationMeter)
        registerMeter(regionGetOperationMeter)
        registerMeter(regionGetOperationTimer)
        registerMeter(regionMissesMeter)
        registerMeter(regionCreateOperationMeter)
        registerMeter(regionPutOperationMeter)
        registerMeter(regionPutOperationTimer)
        registerMeter(regionPutAllOperationMeter)
        registerMeter(regionPutAllOperationTimer)
        registerMeter(regionRemoveAllOperationMeter)
        registerMeter(regionRemoveAllOperationTimer)

        registerMeter(regionEventQueueSizeMeter)
        registerMeter(regionEventQueueThrottleMeter)
        registerMeter(regionEventQueueThrottleTimer)
        registerMeter(regionEventQueueThreadMeter)

        registerMeter(regionQueryExecutionMeter)
        registerMeter(regionQueryExecutionTimer)
        registerMeter(regionQueryResultsHashCollisionsMeter)
        registerMeter(regionQueryResultsHashCollisionsTimer)
        registerMeter(regionQueryExecutionRetryMeter)

        registerMeter(regionTransactionCommitMeter)
        registerMeter(regionTransactionCommitChangesMeter)
        registerMeter(regionTransactionCommitTimer)
        registerMeter(regionTransactionCommitSuccessTimer)
        registerMeter(regionTransactionCommitFailedTimer)
        registerMeter(regionTransactionFailureMeter)
        registerMeter(regionTransactionFailureChangesMeter)
        registerMeter(regionTransactionFailureTimer)
        registerMeter(regionTransactionRollbackMeter)
        registerMeter(regionTransactionRollbackChangesMeter)
        registerMeter(regionTransactionRollbackTimer)
        registerMeter(regionOpenTransactionRollbackTimer)
        registerMeter(regionTransactionConflictCheckTimer)

        registerMeter(regionReliableQueuedOperationsMeter)
        registerMeter(regionReliableQueuedOperationsMegaBytesMeter)
        registerMeter(regionReliableRegionMeter)
        registerMeter(regionReliableRegionMissingMeter)
        registerMeter(regionReliableRegionQueuingMeter)
        registerMeter(regionReliableRegionMissingFullAccessMeter)
        registerMeter(regionReliableRegionMissingLimitedAccessMeter)
        registerMeter(regionReliableRegionMissingNoAccessMeter)

        registerMeter(regionEntryCounterMeter)
        registerMeter(regionEventsQueuedMeter)
        registerMeter(regionOperationRetryMeter)
        registerMeter(regionClearMeter)

        registerMeter(diskTasksWaitingMeter)
        registerMeter(regionConflatedEventsMeter)
        registerMeter(regionTombstoneEntryMeter)
        registerMeter(regionTombstoneEntryGCMeter)
        registerMeter(regionReplicatedTombstoneBytesMeter)
        registerMeter(regionNonReplicatedTombstoneBytesMeter)
        registerMeter(regionOperationClearTimeoutMeter)
        registerMeter(regionEvictionJobsStartedMeter)
        registerMeter(regionEvictionJobsCompletedMeter)
        registerMeter(regionEvictionQueueSizeMeter)
        registerMeter(regionEvictionTimer)

        registerMeter(regionNonSingleHopCountMeter)
        registerMeter(regionMetaDataRefreshCountMeter)

        registerMeter(regionDeltaUpdateMeter)
        registerMeter(regionDeltaUpdateTimer)
        registerMeter(regionDeltaUpdateFailedMeter)
        registerMeter(regionDeltaPreparedMeter)
        registerMeter(regionDeltaPreparedTimer)
        registerMeter(regionDeltaSentMeter)
        registerMeter(regionDeltaSentFullValuesMeter)
        registerMeter(regionDeltaFullValuesRequestedMeter)
        registerMeter(regionImportedEntryMeter)
        registerMeter(regionImportedEntryTimer)
        registerMeter(regionExportedEntryMeter)
        registerMeter(regionExportedEntryTimer)
        registerMeter(regionEntryCompressTimer)
        registerMeter(regionEntryDecompressTimer)
        registerMeter(regionEntryCompressMeter)
        registerMeter(regionEntryDecompressMeter)
        registerMeter(regionCompressionBeforeBytesMeter)
        registerMeter(regionCompressionAfterBytesMeter)
        registerMeter(regionEvictionByCriteriaMeter)
        registerMeter(regionEvictionByCriteriaTimer)
        registerMeter(regionEvictionByCriteriaInProgressMeter)
        registerMeter(regionEvictionByCriteriaEvaluationMeter)
        registerMeter(regionEvictionByCriteriaEvaluationTimer)
    }

    override fun incReliableQueuedOps(inc: Int) {
        regionReliableQueuedOperationsMeter.increment(inc)
    }

    override fun incReliableQueueSize(inc: Int) {
        regionReliableQueuedOperationsMegaBytesMeter.increment(inc)
    }

    override fun incReliableRegions(inc: Int) {
        regionReliableRegionMeter.increment(inc)
    }

    override fun incReliableRegionsMissing(inc: Int) {
        regionReliableRegionMissingMeter.increment(inc)
    }

    override fun incReliableRegionsQueuing(inc: Int) {
        regionReliableRegionQueuingMeter.increment(inc)
    }

    override fun incReliableRegionsMissingFullAccess(inc: Int) {
        regionReliableRegionMissingFullAccessMeter.increment(inc)
    }

    override fun incReliableRegionsMissingLimitedAccess(inc: Int) {
        regionReliableRegionMissingLimitedAccessMeter.increment(inc)
    }

    override fun incReliableRegionsMissingNoAccess(inc: Int) {
        regionReliableRegionMissingNoAccessMeter.increment(inc)
    }

    override fun incQueuedEvents(inc: Int) {
        regionEventsQueuedMeter.increment(inc)
    }

    override fun startCompression(): Long {
        incEntryCompressedCount()
        return NOW_NANOS
    }

    override fun incEntryCompressedCount() {
        regionEntryCompressMeter.increment()
    }

    override fun endCompression(startTime: Long, startSize: Long, endSize: Long) {
        incEntryCompressedTime(NOW_NANOS - startTime)
        incPreCompressedBytes(startSize)
        incPostCompressedBytes(endSize)
    }

    override fun incEntryCompressedTime(time: Long) {
        regionEntryCompressTimer.increment(time)
    }

    override fun incPostCompressedBytes(endSize: Long) {
        regionCompressionAfterBytesMeter.increment(endSize)
    }

    override fun incPreCompressedBytes(startSize: Long) {
        regionCompressionBeforeBytesMeter.increment(startSize)
    }

    override fun startDecompression(): Long {
        incDecompressedEntryCount()
        return NOW_NANOS
    }

    override fun incDecompressedEntryCount() {
        regionEntryDecompressMeter.increment()
    }

    override fun endDecompression(startTime: Long) {
        incDecompressionTime(startTime)
    }

    override fun incDecompressionTime(startTime: Long) {
        regionEntryDecompressTimer.increment(NOW_NANOS - startTime)
    }

    override fun startLoad(): Long {
        regionLoadsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endLoad(start: Long) {
        regionLoadsTimer.increment(NOW_NANOS - start)
        regionLoadsInProgressMeter.decrement()
        regionLoadsCompletedMeter.increment()
    }

    override fun startNetload(): Long {
        regionNetLoadInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endNetload(start: Long) {
        regionNetLoadTimer.increment(NOW_NANOS - start)
        regionNetLoadInProgressMeter.decrement()
        regionNetLoadCompletedMeter.increment()
    }

    override fun startNetsearch(): Long {
        regionNetSearchInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endNetsearch(start: Long) {
        regionNetSearchTimer.increment(NOW_NANOS - start)
        regionNetSearchInProgressMeter.decrement()
        regionNetSearchCompletedMeter.increment()
    }

    override fun startCacheWriterCall(): Long {
        regionCacheWriterInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endCacheWriterCall(start: Long) {
        regionCacheWriteTimer.recordValue(NOW_NANOS - start)
        regionCacheWriterCompletedMeter.increment()
        regionCacheWriterInProgressMeter.decrement()
    }

    override fun startCacheListenerCall(): Long {
        regionCacheListenerInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endCacheListenerCall(start: Long) {
        regionCacheListenerTimer.recordValue(NOW_NANOS - start)
        regionCacheListenerInProgressMeter.decrement()
        regionCacheListenerCompletedMeter.increment()
    }

    override fun startGetInitialImage(): Long {
        regionGIIInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endGetInitialImage(start: Long) {
        regionGIITimer.increment(NOW_NANOS - start)
        regionGIIInProgressMeter.decrement()
        regionGIICompletedMeter.increment()
    }

    override fun endNoGIIDone(start: Long) {
        regionGIITimer.increment(NOW_NANOS - start)
        regionGIIInProgressMeter.decrement()
    }

    override fun incDeltaGIICompleted() {
        regionDeltaGIICompletedMeter.increment()
    }

    override fun incGetInitialImageKeysReceived() {
        regionGIIKeysReceivedMeter.increment()
    }

    override fun startIndexUpdate(): Long {
        regionIndexUpdateInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endIndexUpdate(start: Long) {
        regionIndexUpdateTimeMeter.recordValue(NOW_NANOS - start)
        regionIndexUpdateInProgressMeter.decrement()
        regionIndexUpdateCompletedMeter.increment()
    }

    override fun startIndexInitialization(): Long {
        regionIndexInitializationInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endIndexInitialization(start: Long) {
        regionIndexInitializationTimeMeter.increment(NOW_NANOS - start)
        regionIndexInitializationInProgressMeter.decrement()
        regionIndexInitializationCompletedMeter.increment()
    }

    override fun incRegions(inc: Int) {
        numberOfRegionsMeter.increment(inc)
    }

    override fun incPartitionedRegions(inc: Int) {
        numberOfPartitionedRegionsMeter.increment(inc)
    }

    override fun incDestroys() {
        regionDestroyOperationMeter.increment()
    }

    override fun incCreates() {
        regionCreateOperationMeter.increment()
    }

    override fun incInvalidates() {
        regionInvalidateOperationMeter.increment()
    }

    override fun startGet(): Long {
        return NOW_NANOS
    }

    override fun endGet(start: Long, miss: Boolean) {
        regionGetOperationTimer.recordValue(NOW_NANOS - start)
        regionGetOperationMeter.increment()
        if (miss) {
            regionMissesMeter.increment()
        }
    }

    override fun endPut(start: Long, isUpdate: Boolean): Long {
        val total: Long
        if (isUpdate) {
            regionUpdateOperationMeter.increment()
            total = NOW_NANOS - start
            regionUpdateOperationTimer.recordValue(total)
        } else {
            regionPutOperationMeter.increment()
            total = NOW_NANOS - start
            regionPutOperationTimer.recordValue(total)
        }
        return total
    }

    override fun endPutAll(start: Long) {
        regionPutAllOperationMeter.increment()
        regionPutAllOperationTimer.recordValue(NOW_NANOS - start)
    }

    override fun endRemoveAll(start: Long) {
        regionRemoveAllOperationMeter.increment()
        regionRemoveAllOperationTimer.recordValue(NOW_NANOS - start)
    }

    override fun endQueryExecution(executionTime: Long) {
        regionQueryExecutionMeter.increment()
        regionQueryExecutionTimer.recordValue(executionTime)
    }

    override fun endQueryResultsHashCollisionProbe(start: Long) {
        regionQueryResultsHashCollisionsTimer.recordValue(NOW_NANOS - start)
    }

    override fun incQueryResultsHashCollisions() {
        regionQueryResultsHashCollisionsMeter.increment()
    }

    override fun incTxConflictCheckTime(delta: Long) {
        regionTransactionConflictCheckTimer.recordValue(delta)
    }

    override fun txSuccess(opTime: Long, txLifeTime: Long, txChanges: Int) {
        regionTransactionCommitMeter.increment()
        regionTransactionCommitChangesMeter.increment(txChanges)
        regionTransactionCommitTimer.increment(opTime)
        regionTransactionCommitSuccessTimer.increment(txLifeTime)
    }

    override fun txFailure(opTime: Long, txLifeTime: Long, txChanges: Int) {
        regionTransactionFailureMeter.increment()
        regionTransactionFailureChangesMeter.increment(txChanges)
        regionTransactionFailureTimer.increment(opTime)
        regionTransactionCommitFailedTimer.increment(txLifeTime)
    }

    override fun txRollback(opTime: Long, txLifeTime: Long, txChanges: Int) {
        regionTransactionRollbackMeter.increment()
        regionTransactionRollbackChangesMeter.increment(txChanges)
        regionTransactionRollbackTimer.increment(opTime)
        regionOpenTransactionRollbackTimer.increment(txLifeTime)
    }

    override fun endDeltaUpdate(start: Long) {
        regionDeltaUpdateMeter.increment()
        regionDeltaUpdateTimer.increment(NOW_NANOS - start)
    }

    override fun incDeltaFailedUpdates() {
        regionDeltaUpdateFailedMeter.increment()
    }

    override fun endDeltaPrepared(start: Long) {
        regionDeltaPreparedMeter.increment()
        regionDeltaPreparedTimer.increment(NOW_NANOS - start)
    }

    override fun incDeltasSent() {
        regionDeltaSentMeter.increment()
    }

    override fun incDeltaFullValuesSent() {
        regionDeltaSentFullValuesMeter.increment()
    }

    override fun incDeltaFullValuesRequested() {
        regionDeltaFullValuesRequestedMeter.increment()
    }

    override fun incEventQueueSize(items: Int) {
        regionEventQueueSizeMeter.increment(items)
    }

    override fun incEventQueueThrottleCount(items: Int) {
        regionEventQueueThrottleMeter.increment(items)
    }

    override fun incEventQueueThrottleTime(nanos: Long) {
        regionEventQueueThrottleTimer.recordValue(nanos)
    }

    override fun incEventThreads(items: Int) {
        regionEventQueueThreadMeter.increment(items)
    }

    override fun incEntryCount(delta: Int) {
        regionEntryCounterMeter.increment(delta)
    }

    override fun incRetries() {
        regionOperationRetryMeter.increment()
    }

    override fun incDiskTasksWaiting() {
        diskTasksWaitingMeter.increment()
    }

    override fun decDiskTasksWaiting() {
        diskTasksWaitingMeter.decrement()
    }

    override fun decDiskTasksWaiting(count: Int) {
        diskTasksWaitingMeter.decrement(count)
    }

    override fun incEvictorJobsStarted() {
        regionEvictionJobsStartedMeter.increment()
    }

    override fun incEvictorJobsCompleted() {
        regionEvictionJobsCompletedMeter.increment()
    }

    override fun incEvictorQueueSize(delta: Int) {
        regionEvictionQueueSizeMeter.increment(delta)
    }

    override fun incEvictWorkTime(delta: Long) {
        regionEvictionTimer.recordValue(delta)
    }

    override fun getEventPoolHelper(): PoolStatHelper {
        return object : PoolStatHelper {
            override fun startJob() {
                incEventThreads(1)
            }

            override fun endJob() {
                incEventThreads(-1)
            }
        }
    }

    override fun incClearCount() {
        regionClearMeter.increment()
    }

    override fun incConflatedEventsCount() {
        regionConflatedEventsMeter.increment()
    }

    override fun incTombstoneCount(amount: Int) {
        regionTombstoneEntryMeter.increment(amount)
    }

    override fun incTombstoneGCCount() {
        regionTombstoneEntryGCMeter.increment()
    }

    override fun setReplicatedTombstonesSize(size: Long) {
        regionReplicatedTombstoneBytesMeter.increment(size)
    }

    override fun setNonReplicatedTombstonesSize(size: Long) {
        regionNonReplicatedTombstoneBytesMeter.increment(size)
    }

    override fun incClearTimeouts() {
        regionOperationClearTimeoutMeter.increment()
    }

    override fun incPRQueryRetries() {
        regionQueryExecutionRetryMeter.increment()
    }

    override fun getEvictionQueueStatHelper(): QueueStatHelper {
        return object : QueueStatHelper {
            override fun add() {
                incEvictorQueueSize(1)
            }

            override fun remove() {
                incEvictorQueueSize(-1)
            }

            override fun remove(count: Int) {
                incEvictorQueueSize(count * -1)
            }
        }
    }

    override fun incMetaDataRefreshCount() {
        regionMetaDataRefreshCountMeter.increment()
    }

    override fun endImport(entryCount: Long, start: Long) {
        regionImportedEntryMeter.increment(entryCount)
        regionImportedEntryTimer.increment(NOW_NANOS - start)
    }

    override fun endExport(entryCount: Long, start: Long) {
        regionExportedEntryMeter.increment(entryCount)
        regionExportedEntryTimer.increment(NOW_NANOS - start)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getReliableRegionsMissing(): Int = regionReliableRegionMissingMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetsearchesCompleted(): Int = regionNetLoadCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getEventQueueSize(): Int = regionEventQueueSizeMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadsCompleted(): Int = regionLoadsCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGets(): Int = regionGetOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadsInProgress(): Int = regionNetLoadInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadTime(): Long = regionLoadsTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetloadsInProgress(): Int = regionNetLoadInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetloadsCompleted(): Int = regionNetLoadCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetloadTime(): Long = regionNetLoadTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetsearchesInProgress(): Int = regionNetSearchInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNetsearchTime(): Long = regionNetSearchTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetInitialImagesInProgress(): Int = regionGIIInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetInitialImagesCompleted(): Int = regionGIICompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaGetInitialImagesCompleted(): Int = regionDeltaGIICompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetInitialImageTime(): Long = regionGIITimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetInitialImageKeysReceived(): Int = regionGIIKeysReceivedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRegions(): Int = numberOfRegionsMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPartitionedRegions(): Int = numberOfPartitionedRegionsMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDestroys(): Int = regionDestroyOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCreates(): Int = regionCreateOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPuts(): Int = regionPutOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPutAlls(): Int = regionPutAllOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getRemoveAlls(): Int = regionRemoveAllOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUpdates(): Int = regionUpdateOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getInvalidates(): Int = regionInvalidateOperationMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMisses(): Int = regionMissesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableQueuedOps(): Int = regionReliableQueuedOperationsMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableQueueSize(): Int = regionReliableQueuedOperationsMegaBytesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableQueueMax(): Int = -1

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun incReliableQueueMax(inc: Int) {
        //noop as the max can be queried from the metric
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableRegions(): Int = regionReliableRegionMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableRegionsQueuing(): Int = regionReliableRegionQueuingMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableRegionsMissingFullAccess(): Int = regionReliableRegionMissingFullAccessMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableRegionsMissingLimitedAccess(): Int = regionReliableRegionMissingLimitedAccessMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReliableRegionsMissingNoAccess(): Int = regionReliableRegionMissingNoAccessMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getQueuedEvents(): Long = regionEventQueueSizeMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaUpdates(): Int = regionDeltaUpdateMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaUpdatesTime(): Long = regionDeltaUpdateTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaFailedUpdates(): Int = regionDeltaUpdateFailedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltasPrepared(): Int = regionDeltaPreparedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltasPreparedTime(): Long = regionDeltaPreparedTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltasSent(): Int = regionDeltaSentMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaFullValuesSent(): Int = regionDeltaSentFullValuesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDeltaFullValuesRequested(): Int = regionDeltaFullValuesRequestedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalCompressionTime(): Long = regionEntryCompressTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalDecompressionTime(): Long = regionEntryDecompressTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalCompressions(): Long = regionEntryCompressMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalDecompressions(): Long = regionEntryDecompressMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalPreCompressedBytes(): Long = regionCompressionBeforeBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTotalPostCompressedBytes(): Long = regionCompressionAfterBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getIndexInitializationTime(): Long = regionIndexInitializationTimeMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxCommits(): Int = regionTransactionCommitMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxCommitChanges(): Int = regionTransactionCommitChangesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxCommitTime(): Long = regionTransactionCommitTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxSuccessLifeTime(): Long = regionTransactionCommitSuccessTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxFailures(): Int = regionTransactionFailureMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxFailureChanges(): Int = regionTransactionFailureChangesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxFailureTime(): Long = regionTransactionFailureTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxFailedLifeTime(): Long = regionTransactionCommitFailedTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxRollbacks(): Int = regionTransactionRollbackMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxRollbackChanges(): Int = regionTransactionRollbackChangesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxRollbackTime(): Long = regionTransactionRollbackTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTxRollbackLifeTime(): Long = regionOpenTransactionRollbackTimer.getValue()

    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun isClosed(): Boolean {
        TODO("not implemented")
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getEntries(): Long = regionEntryCounterMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDiskTasksWaiting(): Int = diskTasksWaitingMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented")
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getClearCount(): Int = regionClearMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getConflatedEventsCount(): Long = regionConflatedEventsMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTombstoneCount(): Int = regionTombstoneEntryMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTombstoneGCCount(): Int = regionTombstoneEntryGCMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplicatedTombstonesSize(): Long = regionReplicatedTombstoneBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNonReplicatedTombstonesSize(): Long = regionNonReplicatedTombstoneBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getClearTimeouts(): Int = regionOperationClearTimeoutMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPRQueryRetries(): Long = regionQueryExecutionRetryMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMetaDataRefreshCount(): Long = regionMetaDataRefreshCountMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getImportedEntriesCount(): Long = regionImportedEntryMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getImportTime(): Long = regionImportedEntryTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getExportedEntriesCount(): Long = regionExportedEntryMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getExportTime(): Long = regionExportedEntryTimer.getValue()
}
