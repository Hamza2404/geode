package org.apache.geode.statistics.cache

import org.apache.geode.internal.cache.tier.sockets.CCUStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup

/**
 * Stats for a CacheClientUpdater. Currently the only thing measured are incoming bytes on the
 * wire
 *
 * @since GemFire 5.7
 */
class MicrometerCCUStatsImpl internal constructor(statisticsFactory: StatisticsFactory?, private val serverLocation: String) :
        CCUStats, MicrometerMeterGroup(statisticsFactory, "CacheClientUpdaterStats-$serverLocation") {

    override fun getGroupTags(): Array<String> = arrayOf("serverLocation", serverLocation)

    private val clientUpdaterReceivedBytesMeter = CounterStatisticMeter("client.updater.received.bytes.count", "Total number of bytes received from the server.", meterUnit = "bytes")
    private val clientUpdateMessagesReceivedMeter = GaugeStatisticMeter("client.updater.received.messages.count", "Current number of message being received off the network or being processed after reception.")
    private val clientUpdateMessagesReceivedBytesMeter = GaugeStatisticMeter("client.updater.received.messages.bytes", "Current number of bytes consumed by messages being received or processed.", meterUnit = "bytes")

    override fun initializeStaticMeters() {
        registerMeter(clientUpdaterReceivedBytesMeter)
        registerMeter(clientUpdateMessagesReceivedMeter)
        registerMeter(clientUpdateMessagesReceivedBytesMeter)
    }

    override fun close() {
        //noop
    }

    override fun incReceivedBytes(bytes: Long) {
        clientUpdaterReceivedBytesMeter.increment(bytes)
    }

    override fun incSentBytes(v: Long) {
        // noop since we never send messages
    }

    private fun incMessagesBytesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedBytesMeter.increment(bytes)
    }

    private fun decMessagesBytesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedBytesMeter.decrement(bytes)
    }

    override fun incMessagesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedMeter.increment()
        incMessagesBytesBeingReceived(bytes)
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        clientUpdateMessagesReceivedMeter.decrement()
        decMessagesBytesBeingReceived(bytes)
    }
}
