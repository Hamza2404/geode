package org.apache.geode.statistics.region

import org.apache.geode.internal.cache.ha.HARegionQueueStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

class MicrometerHARegionQueueStats(statisticsFactory: StatisticsFactory?,private val queueName: String) :
        MicrometerMeterGroup(statisticsFactory,"ClientSubscriptionStats-$queueName"), HARegionQueueStats {

    override fun getGroupTags(): Array<String> = arrayOf("queueName", queueName)

    override fun initializeStaticMeters() {
        registerMeter(haRegionQueueEventsQueuedMeter)
        registerMeter(haRegionQueueEventsConflatedMeter)
        registerMeter(haRegionQueueMarkerEventsConflatedMeter)
        registerMeter(haRegionQueueEventsRemovedMeter)
        registerMeter(haRegionQueueEventsTakenMeter)
        registerMeter(haRegionQueueEventsExpiredMeter)
        registerMeter(haRegionQueueEventsTakenQRMMeter)
        registerMeter(haRegionThreadIdentifiersMeter)
        registerMeter(haRegionQueueEventsDispatchedMeter)
        registerMeter(haRegionQueueEventsVoidRemovalMeter)
        registerMeter(haRegionQueueEventsViolationMeter)
    }

    private val haRegionQueueEventsQueuedMeter = CounterStatisticMeter("ha.region.queue.events.queued.count", "Number of events added to queue.")
    private val haRegionQueueEventsConflatedMeter = CounterStatisticMeter("ha.region.queue.events.conflated.count", "Number of events conflated for the queue.")
    private val haRegionQueueMarkerEventsConflatedMeter = CounterStatisticMeter("ha.region.queue.events.conflated.count", "Number of marker events conflated for the queue.", arrayOf("eventType", "marker"))
    private val haRegionQueueEventsRemovedMeter = CounterStatisticMeter("ha.region.queue.events.removed.count", "Number of events removed from the queue.")
    private val haRegionQueueEventsTakenMeter = CounterStatisticMeter("ha.region.queue.events.taken.count", "Number of events taken from the queue.")
    private val haRegionQueueEventsExpiredMeter = CounterStatisticMeter("ha.region.queue.events.expired.count", "Number of events expired from the queue.")
    private val haRegionQueueEventsTakenQRMMeter = CounterStatisticMeter("ha.region.queue.events.taken.count", "Number of events removed by QRM message.", arrayOf("subscriber", "qrm"))
    private val haRegionThreadIdentifiersMeter = CounterStatisticMeter("ha.region.thread.identifier.count", "Number of ThreadIdenfier objects for the queue.")
    private val haRegionQueueEventsDispatchedMeter = CounterStatisticMeter("ha.region.queue.events.dispatched.count", "Number of events that have been dispatched.")
    private val haRegionQueueEventsVoidRemovalMeter = CounterStatisticMeter("ha.region.queue.events.void.removal.count", "Number of void removals from the queue.")
    private val haRegionQueueEventsViolationMeter = CounterStatisticMeter("ha.region.queue.events.violation.count", "Number of events that has violated sequence.")

    override fun incEventsEnqued() {
        haRegionQueueEventsQueuedMeter.increment()
    }

    override fun incEventsConflated() {
        haRegionQueueEventsConflatedMeter.increment()
    }

    override fun incMarkerEventsConflated() {
        haRegionQueueMarkerEventsConflatedMeter.increment()
    }

    override fun incEventsRemoved() {
        haRegionQueueEventsRemovedMeter.increment()
    }

    override fun incEventsTaken() {
        haRegionQueueEventsTakenMeter.increment()
    }

    override fun incEventsExpired() {
        haRegionQueueEventsExpiredMeter.increment()
    }

    override fun incEventsRemovedByQrm() {
        haRegionQueueEventsTakenQRMMeter.increment()
    }

    override fun incThreadIdentifiers() {
        haRegionThreadIdentifiersMeter.increment()
    }

    override fun decThreadIdentifiers() {
        haRegionThreadIdentifiersMeter.decrement()
    }

    override fun incEventsDispatched() {
        haRegionQueueEventsDispatchedMeter.increment()
    }

    override fun incNumVoidRemovals() {
        haRegionQueueEventsVoidRemovalMeter.increment()
    }

    override fun incNumSequenceViolated() {
        haRegionQueueEventsViolationMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsConflated(): Long = haRegionQueueEventsConflatedMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsEnqued(): Long = haRegionQueueEventsQueuedMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsExpired(): Long = haRegionQueueEventsExpiredMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsRemoved(): Long = haRegionQueueEventsRemovedMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsRemovedByQrm(): Long = haRegionQueueEventsTakenQRMMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsTaken(): Long = haRegionQueueEventsTakenQRMMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getMarkerEventsConflated(): Long = haRegionQueueMarkerEventsConflatedMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getNumVoidRemovals(): Long = haRegionQueueEventsVoidRemovalMeter.getValue()

    override fun close() {
        //noop
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getThreadIdentiferCount(): Int = haRegionThreadIdentifiersMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getEventsDispatched(): Long = haRegionQueueEventsDispatchedMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getNumSequenceViolated(): Long = haRegionQueueEventsViolationMeter.getValue()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun isClosed(): Boolean = false
}
