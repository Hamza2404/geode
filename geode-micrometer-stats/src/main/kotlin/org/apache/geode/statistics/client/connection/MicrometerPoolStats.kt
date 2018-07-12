package org.apache.geode.statistics.client.connection

import org.apache.geode.internal.cache.PoolStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerPoolStats(statisticsFactory: StatisticsFactory?,val poolName: String) :
        MicrometerMeterGroup(statisticsFactory,"PoolStats-$poolName"), PoolStats {

    override fun getGroupTags(): Array<String> = arrayOf("poolName", poolName)

    private val initialContactsMeter = GaugeStatisticMeter("pool.initial.contacts", "Number of contacts initially by user")
    private val locatorsDiscoveredMeter = GaugeStatisticMeter("pool.locators.discovered", "Current number of locators discovered")
    private val serversDiscoveredMeter = GaugeStatisticMeter("pool.servers.discovered", "Current number of servers discovered")
    private val subscriptionServersMeter = GaugeStatisticMeter("pool.servers.subscription", "Number of servers hosting this clients subscriptions")
    private val requestsToLocatorMeter = CounterStatisticMeter("pool.locator.requests", "Number of requests from this connection pool to a locator")
    private val responsesFromLocatorMeter = CounterStatisticMeter("pool.locator.responses", "Number of responses received by pool from locator")
    private val currentConnectionCountMeter = GaugeStatisticMeter("pool.connection.count", "Current number of connections")
    private val currentPoolConnectionCountMeter = GaugeStatisticMeter("pool.connection.pooled.count", "Current number of pool connections")
    private val connectionCreateMeter = CounterStatisticMeter("pool.connection.create", "Total number of times a connection has been created.")
    private val connectionDisconnectMeter = CounterStatisticMeter("pool.connection.disconnect", "Total number of times a connection has been destroyed.")
    private val minPoolConnectCountMeter = CounterStatisticMeter("pool.connection.create.min", "Total number of connects done to maintain minimum pool size.")
    private val loadConditioningConnectCountMeter = CounterStatisticMeter("pool.connection.create.loadconditioning", "Total number of connects done due to load conditioning.")
    private val loadConditioningReplaceCountMeter = CounterStatisticMeter("pool.connection.loadconditioning.replace", "Total number of times a load conditioning connect was done but was not used.")
    private val idleConnectionDisconnectCountMeter = CounterStatisticMeter("pool.connection.disconnect.idle", "Total number of disconnects done due to idle expiration.")
    private val loadConditioningDisconnectCountMeter = CounterStatisticMeter("pool.connection.disconnect.loadconditioning", "Total number of disconnects done due to load conditioning expiration.")
    private val idleConnectionCheckCountMeter = CounterStatisticMeter("pool.connection.check.idle", "Total number of checks done for idle expiration.")
    private val loadConditioningCheckCountMeter = CounterStatisticMeter("pool.connection.check.loadconditioning", "Total number of checks done for load conditioning expiration.")
    private val loadConditioningExtensionCountMeter = CounterStatisticMeter("pool.connection.extension.loadconditioning", "Total number of times a connection's load conditioning has been extended because the servers are still balanced.")
    private val connectionWaitInProgressMeter = GaugeStatisticMeter("pool.connection.wait.inprogress", "Current number of threads waiting for a connection")
    private val connectionWaitMeter = CounterStatisticMeter("pool.connection.wait.count", "Total number of times a thread completed waiting for a connection (by timing out or by getting a connection).")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val connectionWaitTimeMeter = CounterStatisticMeter("pool.connection.wait.time", "Total number of nanoseconds spent waiting for a connection.", meterUnit = "nanoseconds")
    private val clientOpsInProgressMeter = GaugeStatisticMeter("pool.connection.ops.inprogress", "Current number of clientOps being executed")
    private val clientOpSendsInProgressMeter = GaugeStatisticMeter("pool.connection.ops.sends.inprogress", "Current number of clientOp sends being executed")
    private val clientOpSendsCountMeter = CounterStatisticMeter("pool.connection.ops.sends.count", "Total number of clientOp sends that have completed successfully")
    private val clientOpSendsFailuresMeter = CounterStatisticMeter("pool.connection.ops.sends.count", "Total number of clientOp sends that have failed")
    private val clientOpSuccessMeter = CounterStatisticMeter("pool.connection.ops.success", "Total number of clientOps completed successfully")
    private val clientOpFailureMeter = CounterStatisticMeter("pool.connection.ops.failure", "Total number of clientOp attempts that have failed")
    private val clientOpTimeoutMeter = CounterStatisticMeter("pool.connection.ops.timeout", "Total number of clientOp attempts that have timed out")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val clientOpSendTimeMeter = CounterStatisticMeter("pool.connection.ops.sends.time", "Total amount of time, in nanoseconds spent doing clientOp sends", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val clientOpTimeMeter = CounterStatisticMeter("pool.connection.ops.time", "Total amount of time, in nanoseconds spent doing clientOps", meterUnit = "nanoseconds")

    override fun initializeStaticMeters() {
        registerMeter(initialContactsMeter)
        registerMeter(locatorsDiscoveredMeter)
        registerMeter(serversDiscoveredMeter)
        registerMeter(subscriptionServersMeter)
        registerMeter(requestsToLocatorMeter)
        registerMeter(responsesFromLocatorMeter)
        registerMeter(currentConnectionCountMeter)
        registerMeter(currentPoolConnectionCountMeter)
        registerMeter(connectionCreateMeter)
        registerMeter(connectionDisconnectMeter)
        registerMeter(minPoolConnectCountMeter)
        registerMeter(loadConditioningConnectCountMeter)
        registerMeter(loadConditioningReplaceCountMeter)
        registerMeter(idleConnectionDisconnectCountMeter)
        registerMeter(loadConditioningDisconnectCountMeter)
        registerMeter(idleConnectionCheckCountMeter)
        registerMeter(loadConditioningCheckCountMeter)
        registerMeter(loadConditioningExtensionCountMeter)
        registerMeter(connectionWaitInProgressMeter)
        registerMeter(connectionWaitMeter)
        registerMeter(connectionWaitTimeMeter)
        registerMeter(clientOpsInProgressMeter)
        registerMeter(clientOpSendsInProgressMeter)
        registerMeter(clientOpSendsCountMeter)
        registerMeter(clientOpSendsFailuresMeter)
        registerMeter(clientOpSuccessMeter)
        registerMeter(clientOpFailureMeter)
        registerMeter(clientOpTimeoutMeter)
        registerMeter(clientOpSendTimeMeter)
        registerMeter(clientOpTimeMeter)
    }

    override fun setInitialContacts(value: Int) {
        initialContactsMeter.setValue(value.toDouble())
    }

    override fun setServerCount(value: Int) {
        serversDiscoveredMeter.setValue(value.toDouble())
    }

    override fun setSubscriptionCount(value: Int) {
        subscriptionServersMeter.setValue(value.toDouble())
    }

    override fun setLocatorCount(value: Int) {
        locatorsDiscoveredMeter.setValue(value.toDouble())
    }

    override fun incLocatorRequests() {
        requestsToLocatorMeter.increment()
    }

    override fun incLocatorResponses() {
        responsesFromLocatorMeter.increment()
    }

    override fun setLocatorRequests(value: Long) {
        requestsToLocatorMeter.increment(value.toDouble())
    }

    override fun setLocatorResponses(value: Long) {
        responsesFromLocatorMeter.increment(value.toDouble())
    }

    override fun incConnections(value: Int) {
        currentConnectionCountMeter.increment(value.toDouble())
        if (value > 0) {
            connectionCreateMeter.increment(value.toDouble())
        } else if (value < 0) {
            connectionDisconnectMeter.increment(value.toDouble())
        }
    }

    override fun incPoolConnections(value: Int) {
        currentPoolConnectionCountMeter.increment(value.toDouble())
    }

    override fun incPrefillConnect() {
        minPoolConnectCountMeter.increment()
    }

    override fun incLoadConditioningCheck() {
        loadConditioningCheckCountMeter.increment()
    }

    override fun incLoadConditioningExtensions() {
        loadConditioningExtensionCountMeter.increment()
    }

    override fun incIdleCheck() {
        idleConnectionCheckCountMeter.increment()
    }

    override fun incLoadConditioningConnect() {
        loadConditioningConnectCountMeter.increment()
    }

    override fun incLoadConditioningReplaceTimeouts() {
        loadConditioningReplaceCountMeter.increment()
    }

    override fun incLoadConditioningDisconnect() {
        loadConditioningDisconnectCountMeter.increment()
    }

    override fun incIdleExpire(value: Int) {
        idleConnectionDisconnectCountMeter.increment(value.toDouble())
    }

    override fun beginConnectionWait(): Long {
        connectionWaitInProgressMeter.increment()
        return System.nanoTime()
    }

    override fun endConnectionWait(start: Long) {
        val duration = System.nanoTime() - start
        connectionWaitInProgressMeter.decrement()
        connectionWaitMeter.increment()
        connectionWaitTimeMeter.increment(duration)
    }

    override fun startClientOp() {
        clientOpsInProgressMeter.increment()
        clientOpSendsInProgressMeter.increment()
    }

    override fun endClientOpSend(duration: Long, failed: Boolean) {
        clientOpSendsInProgressMeter.decrement()
        if (failed) {
            clientOpSendsFailuresMeter.increment()
        } else {
            clientOpSendsCountMeter.increment()
        }
        clientOpSendTimeMeter.increment(duration)
    }

    override fun endClientOp(duration: Long, timedOut: Boolean, failed: Boolean) {
        clientOpsInProgressMeter.decrement()
        when {
            timedOut -> clientOpTimeoutMeter.increment()
            failed -> clientOpFailureMeter.increment()
            else -> clientOpSuccessMeter.increment()
        }
        clientOpTimeMeter.increment(duration)
    }


    override fun close() {
        //noop
    }

    override fun startTime(): Long = NOW_NANOS

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLocatorRequests(): Long = requestsToLocatorMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPoolConnections(): Int = currentPoolConnectionCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getConnects(): Int = connectionCreateMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDisconnects(): Int = connectionDisconnectMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadConditioningCheck(): Int = loadConditioningCheckCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadConditioningExtensions(): Int = loadConditioningExtensionCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadConditioningConnect(): Int = loadConditioningConnectCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadConditioningReplaceTimeouts(): Int = loadConditioningReplaceCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getLoadConditioningDisconnect(): Int = loadConditioningDisconnectCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getIdleExpire(): Int = idleConnectionDisconnectCountMeter.getValue().toInt()
}