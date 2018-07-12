package org.apache.geode.statistics.client.connection

import org.apache.geode.cache.client.internal.ConnectionStats
import org.apache.geode.internal.cache.PoolStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.ScalarStatisticsMeter
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerConnectionStats(private val statisticsFactory: StatisticsFactory?, private val poolName: String, private var poolStats: PoolStats) :
        MicrometerMeterGroup(statisticsFactory, "ClientConnectionStats-$poolName"), ConnectionStats {

    private val clientStats: MicrometerClientStats = MicrometerClientStats(statisticsFactory, poolName)
    private val clientSendStats: MicrometerClientSendStats = MicrometerClientSendStats(statisticsFactory, poolName)

    override fun registerStatsImplementor(factory: StatisticsFactory?) {
        registerMeterGroup(clientStats)
        registerMeterGroup(clientSendStats)
        super.registerStatsImplementor(factory)

    }

    override fun initializeStaticMeters() {
        clientStats.initializeStaticMeters()
        clientSendStats.initializeStaticMeters()
    }

    private fun startOperation(clientInProgressMeter: ScalarStatisticsMeter, clientSendInProgressMeter: ScalarStatisticsMeter): Long {
        clientInProgressMeter.increment()
        clientSendInProgressMeter.increment()
        startClientOp()
        return NOW_NANOS
    }

    private fun endOperationStats(startTime: Long, timedOut: Boolean, failed: Boolean,
                                  inProgressMeter: ScalarStatisticsMeter, failureMeter: ScalarStatisticsMeter,
                                  successMeter: ScalarStatisticsMeter, timer: CounterStatisticMeter,
                                  timeoutMeter: ScalarStatisticsMeter) {
        val timeInNanos = NOW_NANOS - startTime
        endClientOp(timeInNanos, timedOut, failed)
        inProgressMeter.decrement()
        when {
            failed -> {
                failureMeter.increment()
                timer.increment(timeInNanos)
                return
            }
            timedOut -> {
                timeoutMeter.increment()
                timer.increment(timeInNanos)
                return
            }
            else -> {
                successMeter.increment()
                timer.increment(timeInNanos)

            }
        }

    }

    private fun endOperationSendStats(startTime: Long, failed: Boolean,
                                      inProgressMeter: ScalarStatisticsMeter, failureMeter: ScalarStatisticsMeter,
                                      successMeter: ScalarStatisticsMeter, timer: CounterStatisticMeter) {
        val timeInNanos = NOW_NANOS - startTime
        endClientOpSend(timeInNanos, failed)
        inProgressMeter.decrement()
        if (failed) {
            failureMeter.increment()
        } else {
            successMeter.increment()
        }
        timer.increment(timeInNanos)
    }

    override fun startGet(): Long = startOperation(clientStats.operationGetInProgressMeter, clientSendStats.operationGetSendInProgressMeter)

    override fun endGetSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetSendInProgressMeter,
                clientSendStats.operationGetSendFailedMeter, clientSendStats.operationGetSendCompletedMeter,
                clientSendStats.operationGetSendTimerMeter)
    }

    override fun endGet(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetInProgressMeter,
                clientStats.operationGetFailureCountMeter, clientStats.operationGetCountMeter,
                clientStats.operationGetTimer, clientStats.operationGetTimeoutCountMeter)
    }

    override fun startPut(): Long = startOperation(clientStats.operationPutInProgressMeter, clientSendStats.operationPutSendInProgressMeter)

    override fun endPutSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationPutSendInProgressMeter,
                clientSendStats.operationPutSendFailedMeter, clientSendStats.operationPutSendCompletedMeter,
                clientSendStats.operationPutSendTimerMeter)
    }

    override fun endPut(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationPutInProgressMeter,
                clientStats.operationPutFailureCountMeter, clientStats.operationPutCountMeter,
                clientStats.operationPutTimer, clientStats.operationPutTimeoutCountMeter)
    }

    override fun startDestroy(): Long = startOperation(clientStats.operationDestroyInProgressMeter, clientSendStats.operationDestroySendInProgressMeter)

    override fun endDestroySend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationDestroySendInProgressMeter,
                clientSendStats.operationDestroySendFailedMeter, clientSendStats.operationDestroySendCompletedMeter,
                clientSendStats.operationDestroySendTimerMeter)
    }

    override fun endDestroy(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationDestroyInProgressMeter,
                clientStats.operationDestroyFailureCountMeter, clientStats.operationDestroyCountMeter,
                clientStats.operationDestroyTimer, clientStats.operationDestroyTimeoutCountMeter)
    }

    override fun startDestroyRegion(): Long = startOperation(clientStats.operationRegionDestroyInProgressMeter, clientSendStats.operationRegionDestroySendInProgressMeter)

    override fun endDestroyRegionSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRegionDestroySendInProgressMeter,
                clientSendStats.operationRegionDestroySendFailedMeter, clientSendStats.operationRegionDestroySendCompletedMeter,
                clientSendStats.operationDestroySendTimerMeter)
    }

    override fun endDestroyRegion(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRegionDestroyInProgressMeter,
                clientStats.operationRegionDestroyFailureCountMeter, clientStats.operationRegionDestroyCountMeter,
                clientStats.operationRegionDestroyTimer, clientStats.operationRegionDestroyTimeoutCountMeter)
    }

    override fun startClear(): Long = startOperation(clientStats.operationClearInProgressMeter, clientSendStats.operationClearSendInProgressMeter)

    override fun endClearSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationClearSendInProgressMeter,
                clientSendStats.operationClearSendFailedMeter, clientSendStats.operationClearSendCompletedMeter,
                clientSendStats.operationClearSendTimerMeter)
    }

    override fun endClear(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationClearInProgressMeter,
                clientStats.operationClearFailureCountMeter, clientStats.operationClearCountMeter,
                clientStats.operationClearTimer, clientStats.operationClearTimeoutCountMeter)
    }

    override fun startContainsKey(): Long = startOperation(clientStats.operationContainsKeysInProgressMeter, clientSendStats.operationContainsKeySendInProgressMeter)

    override fun endContainsKeySend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationContainsKeySendInProgressMeter,
                clientSendStats.operationContainsKeySendFailedMeter, clientSendStats.operationContainsKeySendCompletedMeter,
                clientSendStats.operationContainsKeySendTimerMeter)
    }

    override fun endContainsKey(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationContainsKeysInProgressMeter,
                clientStats.operationContainsKeysFailureCountMeter, clientStats.operationContainsKeysCountMeter,
                clientStats.operationContainsKeysTimer, clientStats.operationContainsKeysTimeoutCountMeter)
    }

    override fun startKeySet(): Long = startOperation(clientStats.operationKeySetInProgressMeter, clientSendStats.operationKeySetSendInProgressMeter)

    override fun endKeySetSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationKeySetSendInProgressMeter,
                clientSendStats.operationKeySetSendFailedMeter, clientSendStats.operationKeySetSendCompletedMeter,
                clientSendStats.operationKeySetSendTimerMeter)
    }

    override fun endKeySet(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationKeySetInProgressMeter,
                clientStats.operationKeySetFailureCountMeter, clientStats.operationKeySetCountMeter,
                clientStats.operationKeySetTimer, clientStats.operationKeySetTimeoutCountMeter)
    }

    override fun startRegisterInterest(): Long = startOperation(clientStats.operationRegisterInterestInProgressMeter, clientSendStats.operationRegisterInterestSendInProgressMeter)

    override fun endRegisterInterestSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRegisterInterestSendInProgressMeter,
                clientSendStats.operationRegisterInterestSendFailedMeter, clientSendStats.operationRegisterInterestSendCompletedMeter,
                clientSendStats.operationRegisterInterestSendTimerMeter)
    }

    override fun endRegisterInterest(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRegisterInterestInProgressMeter,
                clientStats.operationRegisterInterestFailureCountMeter, clientStats.operationRegisterInterestCountMeter,
                clientStats.operationRegisterInterestTimer, clientStats.operationRegisterInterestTimeoutCountMeter)
    }

    override fun startUnregisterInterest(): Long = startOperation(clientStats.operationUnregisterInterestInProgressMeter, clientSendStats.operationUnregisterInterestSendInProgressMeter)

    override fun endUnregisterInterestSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationUnregisterInterestSendInProgressMeter,
                clientSendStats.operationUnregisterInterestSendFailedMeter, clientSendStats.operationUnregisterInterestSendCompletedMeter,
                clientSendStats.operationUnregisterInterestSendTimerMeter)
    }

    override fun endUnregisterInterest(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationUnregisterInterestInProgressMeter,
                clientStats.operationUnregisterInterestFailureCountMeter, clientStats.operationUnregisterInterestCountMeter,
                clientStats.operationUnregisterInterestTimer, clientStats.operationUnregisterInterestTimeoutCountMeter)
    }

    override fun startQuery(): Long = startOperation(clientStats.operationQueryInProgressMeter, clientSendStats.operationQuerySendInProgressMeter)

    override fun endQuerySend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationQuerySendInProgressMeter,
                clientSendStats.operationQuerySendFailedMeter, clientSendStats.operationQuerySendCompletedMeter,
                clientSendStats.operationQuerySendTimerMeter)
    }

    override fun endQuery(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationQueryInProgressMeter,
                clientStats.operationQueryFailureCountMeter, clientStats.operationQueryCountMeter,
                clientStats.operationQueryTimer, clientStats.operationQueryTimeoutCountMeter)
    }

    override fun startCreateCQ(): Long = startOperation(clientStats.operationCreateCQInProgressMeter, clientSendStats.operationCreateCQSendInProgressMeter)

    override fun endCreateCQSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationCreateCQSendInProgressMeter,
                clientSendStats.operationCreateCQSendFailedMeter, clientSendStats.operationCreateCQSendCompletedMeter,
                clientSendStats.operationCreateCQSendTimerMeter)
    }

    override fun endCreateCQ(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationCreateCQInProgressMeter,
                clientStats.operationCreateCQFailureCountMeter, clientStats.operationCreateCQCountMeter,
                clientStats.operationCreateCQTimer, clientStats.operationCreateCQTimeoutCountMeter)
    }

    override fun startStopCQ(): Long = startOperation(clientStats.operationStopCQInProgressMeter, clientSendStats.operationStopCQSendInProgressMeter)

    override fun endStopCQSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationStopCQSendInProgressMeter,
                clientSendStats.operationStopCQSendFailedMeter, clientSendStats.operationStopCQSendCompletedMeter,
                clientSendStats.operationStopCQSendTimerMeter)
    }

    override fun endStopCQ(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationStopCQInProgressMeter,
                clientStats.operationStopCQFailureCountMeter, clientStats.operationStopCQCountMeter,
                clientStats.operationStopCQTimer, clientStats.operationStopCQTimeoutCountMeter)
    }

    override fun startCloseCQ(): Long = startOperation(clientStats.operationCloseCQInProgressMeter, clientSendStats.operationCloseCQSendInProgressMeter)

    override fun endCloseCQSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationCloseCQSendInProgressMeter,
                clientSendStats.operationCloseCQSendFailedMeter, clientSendStats.operationCloseCQSendCompletedMeter,
                clientSendStats.operationCloseCQSendTimerMeter)
    }

    override fun endCloseCQ(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationCloseCQInProgressMeter,
                clientStats.operationCloseCQFailureCountMeter, clientStats.operationCloseCQCountMeter,
                clientStats.operationCloseCQTimer, clientStats.operationCloseCQTimeoutCountMeter)
    }

    override fun startGetDurableCQs(): Long = startOperation(clientStats.operationGetDurableCQsInProgressMeter, clientSendStats.operationGetDurableCQsSendInProgressMeter)

    override fun endGetDurableCQsSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetDurableCQsSendInProgressMeter,
                clientSendStats.operationGetDurableCQsSendFailedMeter, clientSendStats.operationGetDurableCQsSendCompletedMeter,
                clientSendStats.operationGetDurableCQsSendTimerMeter)
    }

    override fun endGetDurableCQs(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetDurableCQsInProgressMeter,
                clientStats.operationGetDurableCQsFailureCountMeter, clientStats.operationGetDurableCQsCountMeter,
                clientStats.operationGetDurableCQsTimer, clientStats.operationGetDurableCQsTimeoutCountMeter)
    }

    override fun startGatewayBatch(): Long = startOperation(clientStats.operationGatewayBatchInProgressMeter, clientSendStats.operationGatewayBatchSendInProgressMeter)

    override fun endGatewayBatchSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGatewayBatchSendInProgressMeter,
                clientSendStats.operationGatewayBatchSendFailedMeter, clientSendStats.operationGatewayBatchSendCompletedMeter,
                clientSendStats.operationGatewayBatchSendTimerMeter)
    }

    override fun endGatewayBatch(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGatewayBatchInProgressMeter,
                clientStats.operationGatewayBatchFailureCountMeter, clientStats.operationGatewayBatchCountMeter,
                clientStats.operationGatewayBatchTimer, clientStats.operationGatewayBatchTimeoutCountMeter)
    }

    override fun startReadyForEvents(): Long = startOperation(clientStats.operationReadyForEventsInProgressMeter, clientSendStats.operationReadyForEventsSendInProgressMeter)

    override fun endReadyForEventsSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationReadyForEventsSendInProgressMeter,
                clientSendStats.operationReadyForEventsSendFailedMeter, clientSendStats.operationReadyForEventsSendCompletedMeter,
                clientSendStats.operationReadyForEventsSendTimerMeter)
    }

    override fun endReadyForEvents(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationReadyForEventsInProgressMeter,
                clientStats.operationReadyForEventsFailureCountMeter, clientStats.operationReadyForEventsCountMeter,
                clientStats.operationReadyForEventsTimer, clientStats.operationReadyForEventsTimeoutCountMeter)
    }

    override fun startMakePrimary(): Long = startOperation(clientStats.operationMakePrimaryInProgressMeter, clientSendStats.operationMakePrimarySendInProgressMeter)

    override fun endMakePrimarySend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationMakePrimarySendInProgressMeter,
                clientSendStats.operationMakePrimarySendFailedMeter, clientSendStats.operationMakePrimarySendCompletedMeter,
                clientSendStats.operationMakePrimarySendTimerMeter)
    }

    override fun endMakePrimary(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationMakePrimaryInProgressMeter,
                clientStats.operationMakePrimaryFailureCountMeter, clientStats.operationMakePrimaryCountMeter,
                clientStats.operationMakePrimaryTimer, clientStats.operationMakePrimaryTimeoutCountMeter)
    }

    fun startCloseConnection(): Long = startOperation(clientStats.operationCloseConnectionInProgressMeter, clientSendStats.operationCloseConnectionSendInProgressMeter)

    fun endCloseConnectionSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationCloseConnectionSendInProgressMeter,
                clientSendStats.operationCloseConnectionSendFailedMeter, clientSendStats.operationCloseConnectionSendCompletedMeter,
                clientSendStats.operationCloseConnectionSendTimerMeter)
    }

    fun endCloseConnection(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationCloseConnectionInProgressMeter,
                clientStats.operationCloseConnectionFailureCountMeter, clientStats.operationCloseConnectionCountMeter,
                clientStats.operationCloseConnectionTimer, clientStats.operationCloseConnectionTimeoutCountMeter)
    }

    override fun startPrimaryAck(): Long = startOperation(clientStats.operationPrimaryAckInProgressMeter, clientSendStats.operationPrimaryAckSendInProgressMeter)

    override fun endPrimaryAckSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationPrimaryAckSendInProgressMeter,
                clientSendStats.operationPrimaryAckSendFailedMeter, clientSendStats.operationPrimaryAckSendCompletedMeter,
                clientSendStats.operationPrimaryAckSendTimerMeter)
    }

    override fun endPrimaryAck(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationPrimaryAckInProgressMeter,
                clientStats.operationPrimaryAckFailureCountMeter, clientStats.operationPrimaryAckCountMeter,
                clientStats.operationPrimaryAckTimer, clientStats.operationPrimaryAckTimeoutCountMeter)
    }

    override fun startPing(): Long = startOperation(clientStats.operationPingInProgressMeter, clientSendStats.operationPingSendInProgressMeter)

    override fun endPingSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationPingSendInProgressMeter,
                clientSendStats.operationPingSendFailedMeter, clientSendStats.operationPingSendCompletedMeter,
                clientSendStats.operationPingSendTimerMeter)
    }

    override fun endPing(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationPingInProgressMeter,
                clientStats.operationPingFailureCountMeter, clientStats.operationPingCountMeter,
                clientStats.operationPingTimer, clientStats.operationPingTimeoutCountMeter)
    }

    override fun startRegisterInstantiators(): Long = startOperation(clientStats.operationRegisterInstantiatorsInProgressMeter, clientSendStats.operationRegisterInstantiatorsSendInProgressMeter)

    override fun endRegisterInstantiatorsSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRegisterInstantiatorsSendInProgressMeter,
                clientSendStats.operationRegisterInstantiatorsSendFailedMeter, clientSendStats.operationRegisterInstantiatorsSendCompletedMeter,
                clientSendStats.operationRegisterInstantiatorsSendTimerMeter)
    }

    override fun endRegisterInstantiators(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRegisterInstantiatorsInProgressMeter,
                clientStats.operationRegisterInstantiatorsFailureCountMeter, clientStats.operationRegisterInstantiatorsCountMeter,
                clientStats.operationRegisterInstantiatorsTimer, clientStats.operationRegisterInstantiatorsTimeoutCountMeter)
    }

    override fun startRegisterDataSerializers(): Long = startOperation(clientStats.operationRegisterDataSerializersInProgressMeter, clientSendStats.operationRegisterDataSerializersSendInProgressMeter)

    override fun endRegisterDataSerializersSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRegisterDataSerializersSendInProgressMeter,
                clientSendStats.operationRegisterDataSerializersSendFailedMeter, clientSendStats.operationRegisterDataSerializersSendCompletedMeter,
                clientSendStats.operationRegisterDataSerializersSendTimerMeter)
    }

    override fun endRegisterDataSerializers(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRegisterDataSerializersInProgressMeter,
                clientStats.operationRegisterDataSerializersFailureCountMeter, clientStats.operationRegisterDataSerializersCountMeter,
                clientStats.operationRegisterDataSerializersTimer, clientStats.operationRegisterDataSerializersTimeoutCountMeter)
    }

    override fun startPutAll(): Long = startOperation(clientStats.operationPutAllInProgressMeter, clientSendStats.operationPutAllSendInProgressMeter)

    override fun endPutAllSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationPutAllSendInProgressMeter,
                clientSendStats.operationPutAllSendFailedMeter, clientSendStats.operationPutAllSendCompletedMeter,
                clientSendStats.operationPutAllSendTimerMeter)
    }

    override fun endPutAll(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationPutAllInProgressMeter,
                clientStats.operationPutAllFailureCountMeter, clientStats.operationPutAllCountMeter,
                clientStats.operationPutAllTimer, clientStats.operationPutAllTimeoutCountMeter)
    }

    override fun startRemoveAll(): Long = startOperation(clientStats.operationRemoveAllInProgressMeter, clientSendStats.operationRemoveAllSendInProgressMeter)

    override fun endRemoveAllSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRemoveAllSendInProgressMeter,
                clientSendStats.operationRemoveAllSendFailedMeter, clientSendStats.operationRemoveAllSendCompletedMeter,
                clientSendStats.operationRemoveAllSendTimerMeter)
    }

    override fun endRemoveAll(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRemoveAllInProgressMeter,
                clientStats.operationRemoveAllFailureCountMeter, clientStats.operationRemoveAllCountMeter,
                clientStats.operationRemoveAllTimer, clientStats.operationRemoveAllTimeoutCountMeter)
    }

    override fun startGetAll(): Long = startOperation(clientStats.operationGetAllInProgressMeter, clientSendStats.operationGetAllSendInProgressMeter)

    override fun endGetAllSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetAllSendInProgressMeter,
                clientSendStats.operationGetAllSendFailedMeter, clientSendStats.operationGetAllSendCompletedMeter,
                clientSendStats.operationGetAllSendTimerMeter)
    }

    override fun endGetAll(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetAllInProgressMeter,
                clientStats.operationGetAllFailureCountMeter, clientStats.operationGetAllCountMeter,
                clientStats.operationGetAllTimer, clientStats.operationGetAllTimeoutCountMeter)
    }


    override fun incConnections(value: Int) {
        this.clientStats.connectionsCurrentMeter.increment(value.toDouble())
        if (value > 0) {
            this.clientStats.connectionsCreateMeter.increment(value.toDouble())
        } else if (value < 0) {
            this.clientStats.connectionsDisconnectMeter.increment(value.toDouble())
        }
        this.poolStats.incConnections(value)
    }

    override fun startClientOp() {
        this.poolStats.startClientOp()
    }

    override fun endClientOpSend(duration: Long, failed: Boolean) {
        this.poolStats.endClientOpSend(duration, failed)
    }

    override fun endClientOp(duration: Long, timedOut: Boolean, failed: Boolean) {
        this.poolStats.endClientOp(duration, timedOut, failed)
    }

    override fun incReceivedBytes(value: Long) {
        this.clientStats.receivedBytesMeter.increment(value.toDouble())
    }

    override fun incSentBytes(value: Long) {
        this.clientStats.sentBytesMeter.increment(value.toDouble())
    }

    override fun incMessagesBeingReceived(bytes: Int) {
        clientStats.messagesReceivedMeter.increment()
        if (bytes > 0) {
            clientStats.messagesReceivedBytesMeter.increment(bytes.toDouble())
        }
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        TODO("not implemented")
    }

    override fun startExecuteFunction(): Long = startOperation(clientStats.operationExecuteFunctionInProgressMeter, clientSendStats.operationExecuteFunctionSendInProgressMeter)

    override fun endExecuteFunctionSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationExecuteFunctionSendInProgressMeter,
                clientSendStats.operationExecuteFunctionSendFailedMeter, clientSendStats.operationExecuteFunctionSendCompletedMeter,
                clientSendStats.operationExecuteFunctionSendTimerMeter)
    }

    override fun endExecuteFunction(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationExecuteFunctionInProgressMeter,
                clientStats.operationExecuteFunctionFailureCountMeter, clientStats.operationExecuteFunctionCountMeter,
                clientStats.operationExecuteFunctionTimer, clientStats.operationExecuteFunctionTimeoutCountMeter)
    }

    override fun startGetClientPRMetadata(): Long = startOperation(clientStats.operationGetClientPRMetadataInProgressMeter, clientSendStats.operationGetClientPRMetadataSendInProgressMeter)

    override fun endGetClientPRMetadataSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetClientPRMetadataSendInProgressMeter,
                clientSendStats.operationGetClientPRMetadataSendFailedMeter, clientSendStats.operationGetClientPRMetadataSendCompletedMeter,
                clientSendStats.operationGetClientPRMetadataSendTimerMeter)
    }

    override fun endGetClientPRMetadata(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetClientPRMetadataInProgressMeter,
                clientStats.operationGetClientPRMetadataFailureCountMeter, clientStats.operationGetClientPRMetadataCountMeter,
                clientStats.operationGetClientPRMetadataTimer, clientStats.operationGetClientPRMetadataTimeoutCountMeter)
    }

    override fun startGetClientPartitionAttributes(): Long = startOperation(clientStats.operationGetClientPartitionAttributesInProgressMeter, clientSendStats.operationGetClientPartitionAttributesSendInProgressMeter)

    override fun endGetClientPartitionAttributesSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetClientPartitionAttributesSendInProgressMeter,
                clientSendStats.operationGetClientPartitionAttributesSendFailedMeter, clientSendStats.operationGetClientPartitionAttributesSendCompletedMeter,
                clientSendStats.operationGetClientPartitionAttributesSendTimerMeter)
    }

    override fun endGetClientPartitionAttributes(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetClientPartitionAttributesInProgressMeter,
                clientStats.operationGetClientPartitionAttributesFailureCountMeter, clientStats.operationGetClientPartitionAttributesCountMeter,
                clientStats.operationGetClientPartitionAttributesTimer, clientStats.operationGetClientPartitionAttributesTimeoutCountMeter)
    }

    override fun startGetPDXTypeById(): Long = startOperation(clientStats.operationGetPDXTypeByIdInProgressMeter, clientSendStats.operationGetPDXTypeByIdSendInProgressMeter)

    override fun endGetPDXTypeByIdSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetPDXTypeByIdSendInProgressMeter,
                clientSendStats.operationGetPDXTypeByIdSendFailedMeter, clientSendStats.operationGetPDXTypeByIdSendCompletedMeter,
                clientSendStats.operationGetPDXTypeByIdSendTimerMeter)
    }

    override fun endGetPDXTypeById(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetPDXTypeByIdInProgressMeter,
                clientStats.operationGetPDXTypeByIdFailureCountMeter, clientStats.operationGetPDXTypeByIdCountMeter,
                clientStats.operationGetPDXTypeByIdTimer, clientStats.operationGetPDXTypeByIdTimeoutCountMeter)
    }

    override fun startGetPDXIdForType(): Long = startOperation(clientStats.operationGetPDXIdForTypeInProgressMeter, clientSendStats.operationGetPDXIdForTypeSendInProgressMeter)

    override fun endGetPDXIdForTypeSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetPDXIdForTypeSendInProgressMeter,
                clientSendStats.operationGetPDXIdForTypeSendFailedMeter, clientSendStats.operationGetPDXIdForTypeSendCompletedMeter,
                clientSendStats.operationGetPDXIdForTypeSendTimerMeter)
    }

    override fun endGetPDXIdForType(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetPDXIdForTypeInProgressMeter,
                clientStats.operationGetPDXIdForTypeFailureCountMeter, clientStats.operationGetPDXIdForTypeCountMeter,
                clientStats.operationGetPDXIdForTypeTimer, clientStats.operationGetPDXIdForTypeTimeoutCountMeter)
    }

    override fun startAddPdxType(): Long = startOperation(clientStats.operationAddPdxTypeInProgressMeter, clientSendStats.operationAddPdxTypeSendInProgressMeter)

    override fun endAddPdxTypeSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationAddPdxTypeSendInProgressMeter,
                clientSendStats.operationAddPdxTypeSendFailedMeter, clientSendStats.operationAddPdxTypeSendCompletedMeter,
                clientSendStats.operationAddPdxTypeSendTimerMeter)
    }

    override fun endAddPdxType(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationAddPdxTypeInProgressMeter,
                clientStats.operationAddPdxTypeFailureCountMeter, clientStats.operationAddPdxTypeCountMeter,
                clientStats.operationAddPdxTypeTimer, clientStats.operationAddPdxTypeTimeoutCountMeter)
    }

    override fun startSize(): Long = startOperation(clientStats.operationSizeInProgressMeter, clientSendStats.operationSizeSendInProgressMeter)

    override fun endSizeSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationSizeSendInProgressMeter,
                clientSendStats.operationSizeSendFailedMeter, clientSendStats.operationSizeSendCompletedMeter,
                clientSendStats.operationSizeSendTimerMeter)
    }

    override fun endSize(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationSizeInProgressMeter,
                clientStats.operationSizeFailureCountMeter, clientStats.operationSizeCountMeter,
                clientStats.operationSizeTimer, clientStats.operationSizeTimeoutCountMeter)
    }

    override fun startInvalidate(): Long = startOperation(clientStats.operationInvalidateInProgressMeter, clientSendStats.operationInvalidateSendInProgressMeter)

    override fun endInvalidateSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationInvalidateSendInProgressMeter,
                clientSendStats.operationInvalidateSendFailedMeter, clientSendStats.operationInvalidateSendCompletedMeter,
                clientSendStats.operationInvalidateSendTimerMeter)
    }

    override fun endInvalidate(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationInvalidateInProgressMeter,
                clientStats.operationInvalidateFailureCountMeter, clientStats.operationInvalidateCountMeter,
                clientStats.operationInvalidateTimer, clientStats.operationInvalidateTimeoutCountMeter)
    }

    override fun startCommit(): Long = startOperation(clientStats.operationCommitInProgressMeter, clientSendStats.operationCommitSendInProgressMeter)

    override fun endCommitSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationCommitSendInProgressMeter,
                clientSendStats.operationCommitSendFailedMeter, clientSendStats.operationCommitSendCompletedMeter,
                clientSendStats.operationCommitSendTimerMeter)
    }

    override fun endCommit(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationCommitInProgressMeter,
                clientStats.operationCommitFailureCountMeter, clientStats.operationCommitCountMeter,
                clientStats.operationCommitTimer, clientStats.operationCommitTimeoutCountMeter)
    }

    override fun startGetEntry(): Long = startOperation(clientStats.operationGetEntryInProgressMeter, clientSendStats.operationGetEntrySendInProgressMeter)

    override fun endGetEntrySend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationGetEntrySendInProgressMeter,
                clientSendStats.operationGetEntrySendFailedMeter, clientSendStats.operationGetEntrySendCompletedMeter,
                clientSendStats.operationGetEntrySendTimerMeter)
    }

    override fun endGetEntry(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationGetEntryInProgressMeter,
                clientStats.operationGetEntryFailureCountMeter, clientStats.operationGetEntryCountMeter,
                clientStats.operationGetEntryTimer, clientStats.operationGetEntryTimeoutCountMeter)
    }

    override fun startRollback(): Long = startOperation(clientStats.operationRollbackInProgressMeter, clientSendStats.operationRollbackSendInProgressMeter)

    override fun endRollbackSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationRollbackSendInProgressMeter,
                clientSendStats.operationRollbackSendFailedMeter, clientSendStats.operationRollbackSendCompletedMeter,
                clientSendStats.operationRollbackSendTimerMeter)
    }

    override fun endRollback(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationRollbackInProgressMeter,
                clientStats.operationRollbackFailureCountMeter, clientStats.operationRollbackCountMeter,
                clientStats.operationRollbackTimer, clientStats.operationRollbackTimeoutCountMeter)
    }

    override fun startTxFailover(): Long = startOperation(clientStats.operationTxFailoverInProgressMeter, clientSendStats.operationTxFailoverSendInProgressMeter)

    override fun endTxFailoverSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationTxFailoverSendInProgressMeter,
                clientSendStats.operationTxFailoverSendFailedMeter, clientSendStats.operationTxFailoverSendCompletedMeter,
                clientSendStats.operationTxFailoverSendTimerMeter)
    }

    override fun endTxFailover(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationTxFailoverInProgressMeter,
                clientStats.operationTxFailoverFailureCountMeter, clientStats.operationTxFailoverCountMeter,
                clientStats.operationTxFailoverTimer, clientStats.operationTxFailoverTimeoutCountMeter)
    }

    override fun startTxSynchronization(): Long = startOperation(clientStats.operationTxSynchronizationInProgressMeter, clientSendStats.operationTxSynchronizationSendInProgressMeter)

    override fun endTxSynchronizationSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationTxSynchronizationSendInProgressMeter,
                clientSendStats.operationTxSynchronizationSendFailedMeter, clientSendStats.operationTxSynchronizationSendCompletedMeter,
                clientSendStats.operationTxSynchronizationSendTimerMeter)
    }

    override fun endTxSynchronization(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationTxSynchronizationInProgressMeter,
                clientStats.operationTxSynchronizationFailureCountMeter, clientStats.operationTxSynchronizationCountMeter,
                clientStats.operationTxSynchronizationTimer, clientStats.operationTxSynchronizationTimeoutCountMeter)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetDurableCqs(): Int = clientStats.operationGetDurableCQsCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGets(): Int = clientStats.operationGetCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getGetDuration(): Long = clientStats.operationGetTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPuts(): Int = clientStats.operationPutCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getPutDuration(): Long = clientStats.operationPutTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun startCloseCon(): Long {
        clientStats.operationCloseConnectionInProgressMeter.increment()
        clientSendStats.operationCloseConnectionSendInProgressMeter.increment()
        startClientOp()
        return NOW_NANOS
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun endCloseConSend(startTime: Long, failed: Boolean) {
        endOperationSendStats(startTime, failed, clientSendStats.operationCloseConnectionSendInProgressMeter,
                clientSendStats.operationCloseConnectionSendFailedMeter, clientSendStats.operationCloseConnectionSendCompletedMeter,
                clientSendStats.operationCloseConnectionSendTimerMeter)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun endCloseCon(startTime: Long, timedOut: Boolean, failed: Boolean) {
        endOperationStats(startTime, timedOut, failed, clientStats.operationCloseConnectionInProgressMeter,
                clientStats.operationCloseConnectionTimeoutCountMeter, clientStats.operationCloseConnectionFailureCountMeter,
                clientStats.operationCloseConnectionCountMeter, clientStats.operationCloseConnectionTimer)
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getConnections(): Int = clientStats.connectionsCurrentMeter.getValue().toInt()

    //This value is -1 because it will be captured by the metric `connection.operation.count`
    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOps(): Int = -1

    override fun close() {
        //noop in micrometer
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getExecuteFunctions(): Int = clientStats.operationExecuteFunctionInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getExecuteFunctionDuration(): Long = clientStats.operationExecuteFunctionTimer.getValue()
}