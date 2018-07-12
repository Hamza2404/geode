package org.apache.geode.statistics.cache

import org.apache.geode.internal.cache.tier.sockets.MessageStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerCacheClientProxyStats(statisticsFactory: StatisticsFactory?,private val clientName: String) :
        MicrometerMeterGroup(statisticsFactory,"CacheClientProxyStats-$clientName"), MessageStats {

    override fun getGroupTags(): Array<String> = arrayOf("clientName", clientName)

    private val clientProxyMessagesReceivedMeter = CounterStatisticMeter("client.proxy.messages.received.count", "Number of client messages received.")
    private val clientProxyMessageQueuedMeter = CounterStatisticMeter("client.proxy.messages.queued.count", "Number of client messages added to the message queue.")
    private val clientProxyMessageQueingFailedMeter = CounterStatisticMeter("client.proxy.messages.queued.failed.count", "Number of client messages attempted but failed to be added to the message queue.")
    private val clientProxyMessageNotQueuedOriginatorMeter = CounterStatisticMeter("client.proxy.messages.notqueued.originator.count", "Number of client messages received but not added to the message queue because the receiving proxy represents the client originating the message.")
    private val clientProxyMessageNotQueuedNoInterestMeter = CounterStatisticMeter("client.proxy.messages.notqueued.nointerest", "Number of client messages received but not added to the message queue because the client represented by the receiving proxy was not interested in the message's key.")
    private val clientProxyMessageQueueSizeMeter = GaugeStatisticMeter("client.proxy.messages.queued.count", "Size of the message queue.")
    private val clientProxyMessageProcessedMeter = CounterStatisticMeter("client.proxy.messages.processed.count", "Number of client messages removed from the message queue and sent.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val clientProxyMessageProcessingTimer = CounterStatisticMeter("client.proxy.messages.processing.time", "Total time spent sending messages to clients.", meterUnit = "nanoseconds")
    private val clientProxyDeltaMessagesSentMeter = CounterStatisticMeter("client.proxy.delta.messages.sent.count", "Number of client messages containing only delta bytes dispatched to the client.", arrayOf("messageSize", "delta"))
    private val clientProxyDeltaFullMessagesSentMeter = CounterStatisticMeter("client.proxy.delta.messages.sent.count", "Number of client messages dispatched in reponse to failed delta at client.", arrayOf("messageSize", "full"))
    private val clientProxyCQCountMeter = GaugeStatisticMeter("client.proxy.cq.count", "Number of CQs on the client.")
    private val clientProxyBytesSent = CounterStatisticMeter("client.proxy.sent.bytes", "Total number of bytes sent to client.", meterUnit = "bytes")

    override fun initializeStaticMeters() {
        registerMeter(clientProxyMessagesReceivedMeter)
        registerMeter(clientProxyMessageQueuedMeter)
        registerMeter(clientProxyMessageQueingFailedMeter)
        registerMeter(clientProxyMessageNotQueuedOriginatorMeter)
        registerMeter(clientProxyMessageNotQueuedNoInterestMeter)
        registerMeter(clientProxyMessageQueueSizeMeter)
        registerMeter(clientProxyMessageProcessedMeter)
        registerMeter(clientProxyMessageProcessingTimer)
        registerMeter(clientProxyDeltaMessagesSentMeter)
        registerMeter(clientProxyDeltaFullMessagesSentMeter)
        registerMeter(clientProxyCQCountMeter)
        registerMeter(clientProxyBytesSent)
    }

    fun incMessagesReceived() {
        clientProxyMessagesReceivedMeter.increment()
    }

    fun incMessagesQueued() {
        clientProxyMessageQueuedMeter.increment()
    }

    fun incMessagesNotQueuedOriginator() {
        clientProxyMessageNotQueuedOriginatorMeter.increment()
    }

    fun incMessagesNotQueuedNotInterested() {
        clientProxyMessageNotQueuedNoInterestMeter.increment()
    }

    fun incMessagesFailedQueued() {
        clientProxyMessageQueingFailedMeter.increment()
    }

    fun incCqCount() {
        clientProxyCQCountMeter.increment()
    }

    fun decCqCount() {
        clientProxyCQCountMeter.decrement()
    }

    fun setQueueSize(size: Int) {
        clientProxyMessageQueueSizeMeter.setValue(size)
    }

    fun startTime(): Long = NOW_NANOS

    fun endMessage(start: Long) {
        clientProxyMessageProcessedMeter.increment()
        clientProxyMessageProcessingTimer.increment(NOW_NANOS - start)
    }

    fun incDeltaMessagesSent() {
        clientProxyDeltaMessagesSentMeter.increment()
    }

    fun incDeltaFullMessagesSent() {
        clientProxyDeltaFullMessagesSentMeter.increment()
    }

    override fun incSentBytes(bytes: Long) {
        clientProxyBytesSent.increment(bytes)
    }

    override fun incReceivedBytes(bytes: Long) {
        //noop
    }

    override fun incMessagesBeingReceived(bytes: Int) {
        //noop
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    fun getCqCount(): Long = clientProxyCQCountMeter.getValue()
}