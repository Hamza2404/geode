package org.apache.geode.statistics.client.connection

import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup

class MicrometerClientSendStats(statisticsFactory: StatisticsFactory?,private val poolName:String) :
        MicrometerMeterGroup(statisticsFactory,"ClientConnectionSendStats-$poolName") {

    override fun getGroupTags(): Array<String> = arrayOf("poolName", poolName)

    //Get
    val operationGetSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress", "Current number of get sends being executed",
            arrayOf("operation", "get"))
    val operationGetSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of get sends that have completed successfully", arrayOf("operation", "get", "status", "completed"))
    val operationGetSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of get sends that have failed", arrayOf("operation", "get", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of get sends that have completed successfully", arrayOf("operation", "get"), meterUnit = "nanoseconds")
    //Put
    val operationPutSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of put sends being executed", arrayOf("operation", "put"))
    val operationPutSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of put sends that have completed successfully", arrayOf("operation", "put", "status", "completed"))
    val operationPutSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of put sends that have failed", arrayOf("operation", "put", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPutSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of put sends that have completed successfully", arrayOf("operation", "put"), meterUnit = "nanoseconds")
    //Destroy
    val operationDestroySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of destroy sends being executed", arrayOf("operation", "destroy"))
    val operationDestroySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of destroy sends that have completed successfully", arrayOf("operation", "destroy", "status", "completed"))
    val operationDestroySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of destroy sends that have failed", arrayOf("operation", "destroy", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationDestroySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of destroy sends that have completed successfully", arrayOf("operation", "destroy"), meterUnit = "nanoseconds")
    //Region Destroy
    val operationRegionDestroySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of region destroy sends being executed", arrayOf("operation", "regionDestroy"))
    val operationRegionDestroySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of region destroy sends that have completed successfully", arrayOf("operation", "regionDestroy", "status", "completed"))
    val operationRegionDestroySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of region destroy sends that have failed", arrayOf("operation", "regionDestroy", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegionDestroySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of region destroy sends that have completed successfully", arrayOf("operation", "regionDestroy"), meterUnit = "nanoseconds")
    //Clear
    val operationClearSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of clear sends being executed", arrayOf("operation", "clear"))
    val operationClearSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of clear sends that have completed successfully", arrayOf("operation", "clear", "status", "completed"))
    val operationClearSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of clear sends that have failed", arrayOf("operation", "clear", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationClearSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of rclear sends that have completed successfully", arrayOf("operation", "clear"), meterUnit = "nanoseconds")
    //ContainsKey
    val operationContainsKeySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of containsKey sends being executed", arrayOf("operation", "containsKey"))
    val operationContainsKeySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of containsKey sends that have completed successfully", arrayOf("operation", "containsKey", "status", "completed"))
    val operationContainsKeySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of containsKey sends that have failed", arrayOf("operation", "containsKey", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationContainsKeySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of containsKey sends that have completed successfully", arrayOf("operation", "containsKey"), meterUnit = "nanoseconds")
    //KeySet
    val operationKeySetSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of keySet sends being executed", arrayOf("operation", "keySet"))
    val operationKeySetSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of keySet sends that have completed successfully", arrayOf("operation", "keySet", "status", "completed"))
    val operationKeySetSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of keySet sends that have failed", arrayOf("operation", "keySet", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationKeySetSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of keySet sends that have completed successfully", arrayOf("operation", "keySet"), meterUnit = "nanoseconds")
    //Commit
    val operationCommitSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of commit sends being executed", arrayOf("operation", "commit"))
    val operationCommitSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of commit sends that have completed successfully", arrayOf("operation", "commit", "status", "completed"))
    val operationCommitSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of commit sends that have failed", arrayOf("operation", "commit", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCommitSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of commit sends that have completed successfully", arrayOf("operation", "commit"), meterUnit = "nanoseconds")
    //Rollback
    val operationRollbackSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of rollback sends being executed", arrayOf("operation", "rollback"))
    val operationRollbackSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of rollback sends that have completed successfully", arrayOf("operation", "rollback", "status", "completed"))
    val operationRollbackSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of rollback sends that have failed", arrayOf("operation", "rollback", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRollbackSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of rollback sends that have completed successfully", arrayOf("operation", "rollback"), meterUnit = "nanoseconds")
    //GetEntry
    val operationGetEntrySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getEntries sends being executed", arrayOf("operation", "getEntries"))
    val operationGetEntrySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getEntries sends that have completed successfully", arrayOf("operation", "getEntries", "status", "completed"))
    val operationGetEntrySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getEntries sends that have failed", arrayOf("operation", "getEntries", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetEntrySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getEntries sends that have completed successfully", arrayOf("operation", "getEntries"), meterUnit = "nanoseconds")
    //TxSynchronization
    val operationTxSynchronizationSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of jtaSynchronization sends being executed", arrayOf("operation", "jtaSynchronization"))
    val operationTxSynchronizationSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of jtaSynchronization sends that have completed successfully", arrayOf("operation", "jtaSynchronization", "status", "completed"))
    val operationTxSynchronizationSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of jtaSynchronization sends that have failed", arrayOf("operation", "jtaSynchronization", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationTxSynchronizationSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of jtaSynchronization sends that have completed successfully", arrayOf("operation", "jtaSynchronization"), meterUnit = "nanoseconds")
    //TxFailover
    val operationTxFailoverSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of txFailover sends being executed", arrayOf("operation", "txFailover"))
    val operationTxFailoverSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of txFailover sends that have completed successfully", arrayOf("operation", "txFailover", "status", "completed"))
    val operationTxFailoverSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of txFailover sends that have failed", arrayOf("operation", "txFailover", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationTxFailoverSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of txFailover sends that have completed successfully", arrayOf("operation", "txFailover"), meterUnit = "nanoseconds")
    //Size
    val operationSizeSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of sizes sends being executed", arrayOf("operation", "sizes"))
    val operationSizeSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of sizes sends that have completed successfully", arrayOf("operation", "sizes", "status", "completed"))
    val operationSizeSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of sizes sends that have failed", arrayOf("operation", "sizes", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationSizeSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of sizes sends that have completed successfully", arrayOf("operation", "sizes"), meterUnit = "nanoseconds")
    //Invalidate
    val operationInvalidateSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of invalidate sends being executed", arrayOf("operation", "invalidate"))
    val operationInvalidateSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of invalidate sends that have completed successfully", arrayOf("operation", "invalidate", "status", "completed"))
    val operationInvalidateSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of invalidate sends that have failed", arrayOf("operation", "invalidate", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationInvalidateSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of invalidate sends that have completed successfully", arrayOf("operation", "invalidate"), meterUnit = "nanoseconds")
    //RegisterInterest
    val operationRegisterInterestSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of registerInterest sends being executed", arrayOf("operation", "registerInterest"))
    val operationRegisterInterestSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerInterest sends that have completed successfully", arrayOf("operation", "registerInterest", "status", "completed"))
    val operationRegisterInterestSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerInterest sends that have failed", arrayOf("operation", "registerInterest", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterInterestSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of registerInterest sends that have completed successfully", arrayOf("operation", "registerInterest"), meterUnit = "nanoseconds")
    //UnregisterInterest
    val operationUnregisterInterestSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of unregisterInterest sends being executed", arrayOf("operation", "unregisterInterest"))
    val operationUnregisterInterestSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of unregisterInterest sends that have completed successfully", arrayOf("operation", "unregisterInterest", "status", "completed"))
    val operationUnregisterInterestSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of unregisterInterest sends that have failed", arrayOf("operation", "unregisterInterest", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationUnregisterInterestSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of unregisterInterest sends that have completed successfully", arrayOf("operation", "unregisterInterest"), meterUnit = "nanoseconds")
    //Query
    val operationQuerySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of queries sends being executed", arrayOf("operation", "queries"))
    val operationQuerySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of queries sends that have completed successfully", arrayOf("operation", "queries", "status", "completed"))
    val operationQuerySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of queries sends that have failed", arrayOf("operation", "queries", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationQuerySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of queries sends that have completed successfully", arrayOf("operation", "queries"), meterUnit = "nanoseconds")
    //CreateCQ
    val operationCreateCQSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of createCQ sends being executed", arrayOf("operation", "createCQ"))
    val operationCreateCQSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of createCQ sends that have completed successfully", arrayOf("operation", "createCQ", "status", "completed"))
    val operationCreateCQSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of createCQ sends that have failed", arrayOf("operation", "createCQ", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCreateCQSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of createCQ sends that have completed successfully", arrayOf("operation", "createCQ"), meterUnit = "nanoseconds")
    //StopCQ
    val operationStopCQSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of stopCQ sends being executed", arrayOf("operation", "stopCQ"))
    val operationStopCQSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of stopCQ sends that have completed successfully", arrayOf("operation", "stopCQ", "status", "completed"))
    val operationStopCQSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of stopCQ sends that have failed", arrayOf("operation", "stopCQ", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationStopCQSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of stopCQ sends that have completed successfully", arrayOf("operation", "stopCQ"), meterUnit = "nanoseconds")
    //CloseCQ
    val operationCloseCQSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of closeCQ sends being executed", arrayOf("operation", "closeCQ"))
    val operationCloseCQSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of closeCQ sends that have completed successfully", arrayOf("operation", "closeCQ", "status", "completed"))
    val operationCloseCQSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of closeCQ sends that have failed", arrayOf("operation", "closeCQ", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCloseCQSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of closeCQ sends that have completed successfully", arrayOf("operation", "closeCQ"), meterUnit = "nanoseconds")
    //GetDurableCQs
    val operationGetDurableCQsSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of GetDurableCQss sends being executed", arrayOf("operation", "GetDurableCQss"))
    val operationGetDurableCQsSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of GetDurableCQss sends that have completed successfully", arrayOf("operation", "GetDurableCQss", "status", "completed"))
    val operationGetDurableCQsSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of GetDurableCQss sends that have failed", arrayOf("operation", "GetDurableCQss", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetDurableCQsSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of GetDurableCQss sends that have completed successfully", arrayOf("operation", "GetDurableCQss"), meterUnit = "nanoseconds")
    //ReadyForEvents
    val operationReadyForEventsSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of readyForEvents sends being executed", arrayOf("operation", "readyForEvents"))
    val operationReadyForEventsSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of readyForEvents sends that have completed successfully", arrayOf("operation", "readyForEvents", "status", "completed"))
    val operationReadyForEventsSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of readyForEvents sends that have failed", arrayOf("operation", "readyForEvents", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationReadyForEventsSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of readyForEvents sends that have completed successfully", arrayOf("operation", "readyForEvents"), meterUnit = "nanoseconds")
    //GatewayBatch
    val operationGatewayBatchSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of gatewayBatches sends being executed", arrayOf("operation", "gatewayBatches"))
    val operationGatewayBatchSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of gatewayBatches sends that have completed successfully", arrayOf("operation", "gatewayBatches", "status", "completed"))
    val operationGatewayBatchSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of gatewayBatches sends that have failed", arrayOf("operation", "gatewayBatches", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGatewayBatchSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of gatewayBatches sends that have completed successfully", arrayOf("operation", "gatewayBatches"), meterUnit = "nanoseconds")
    //MakePrimary
    val operationMakePrimarySendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of makePrimary sends being executed", arrayOf("operation", "makePrimary"))
    val operationMakePrimarySendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of makePrimary sends that have completed successfully", arrayOf("operation", "makePrimary", "status", "completed"))
    val operationMakePrimarySendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of makePrimary sends that have failed", arrayOf("operation", "makePrimary", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationMakePrimarySendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of makePrimary sends that have completed successfully", arrayOf("operation", "makePrimary"), meterUnit = "nanoseconds")
    //PrimaryAck
    val operationPrimaryAckSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of primaryAcks sends being executed", arrayOf("operation", "primaryAcks"))
    val operationPrimaryAckSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of primaryAcks sends that have completed successfully", arrayOf("operation", "primaryAcks", "status", "completed"))
    val operationPrimaryAckSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of primaryAcks sends that have failed", arrayOf("operation", "primaryAcks", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPrimaryAckSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of primaryAcks sends that have completed successfully", arrayOf("operation", "primaryAcks"), meterUnit = "nanoseconds")
    //CloseConnection
    val operationCloseConnectionSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of closeConnection sends being executed", arrayOf("operation", "closeConnection"))
    val operationCloseConnectionSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of closeConnection sends that have completed successfully", arrayOf("operation", "closeConnection", "status", "completed"))
    val operationCloseConnectionSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of closeConnection sends that have failed", arrayOf("operation", "closeConnection", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCloseConnectionSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of closeConnection sends that have completed successfully", arrayOf("operation", "closeConnection"), meterUnit = "nanoseconds")
    //Ping
    val operationPingSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of pings sends being executed", arrayOf("operation", "pings"))
    val operationPingSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of pings sends that have completed successfully", arrayOf("operation", "pings", "status", "completed"))
    val operationPingSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of pings sends that have failed", arrayOf("operation", "pings", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPingSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of pings sends that have completed successfully", arrayOf("operation", "pings"), meterUnit = "nanoseconds")
    //RegisterInstantiators
    val operationRegisterInstantiatorsSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of registerInstantiator sends being executed", arrayOf("operation", "registerInstantiator"))
    val operationRegisterInstantiatorsSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerInstantiator sends that have completed successfully", arrayOf("operation", "registerInstantiator", "status", "completed"))
    val operationRegisterInstantiatorsSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerInstantiator sends that have failed", arrayOf("operation", "registerInstantiator", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterInstantiatorsSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of registerInstantiator sends that have completed successfully", arrayOf("operation", "registerInstantiator"), meterUnit = "nanoseconds")
    //RegisterDataSerializers
    val operationRegisterDataSerializersSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of registerDeserializers sends being executed", arrayOf("operation", "registerDeserializers"))
    val operationRegisterDataSerializersSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerDeserializers sends that have completed successfully", arrayOf("operation", "registerDeserializers", "status", "completed"))
    val operationRegisterDataSerializersSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of registerDeserializers sends that have failed", arrayOf("operation", "registerDeserializers", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterDataSerializersSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of registerDeserializers sends that have completed successfully", arrayOf("operation", "registerDeserializers"), meterUnit = "nanoseconds")
    //PutAll
    val operationPutAllSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of putAll sends being executed", arrayOf("operation", "putAll"))
    val operationPutAllSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of putAll sends that have completed successfully", arrayOf("operation", "putAll", "status", "completed"))
    val operationPutAllSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of putAll sends that have failed", arrayOf("operation", "putAll", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPutAllSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of putAll sends that have completed successfully", arrayOf("operation", "putAll"), meterUnit = "nanoseconds")
    //RemoveAll
    val operationRemoveAllSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of removeAll sends being executed", arrayOf("operation", "removeAll"))
    val operationRemoveAllSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of removeAll sends that have completed successfully", arrayOf("operation", "removeAll", "status", "completed"))
    val operationRemoveAllSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of removeAll sends that have failed", arrayOf("operation", "removeAll", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRemoveAllSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of removeAll sends that have completed successfully", arrayOf("operation", "removeAll"), meterUnit = "nanoseconds")
    //GetAll
    val operationGetAllSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getAll sends being executed", arrayOf("operation", "getAll"))
    val operationGetAllSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getAll sends that have completed successfully", arrayOf("operation", "getAll", "status", "completed"))
    val operationGetAllSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getAll sends that have failed", arrayOf("operation", "getAll", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetAllSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getAll sends that have completed successfully", arrayOf("operation", "getAll"), meterUnit = "nanoseconds")
    //ExecuteFunction
    val operationExecuteFunctionSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of executeFunction sends being executed", arrayOf("operation", "executeFunction"))
    val operationExecuteFunctionSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of executeFunction sends that have completed successfully", arrayOf("operation", "executeFunction", "status", "completed"))
    val operationExecuteFunctionSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of executeFunction sends that have failed", arrayOf("operation", "executeFunction", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationExecuteFunctionSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of executeFunction sends that have completed successfully", arrayOf("operation", "executeFunction"), meterUnit = "nanoseconds")
    //AsyncExecuteFunction
    val operationAsyncExecuteFunctionSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of asyncExecuteFunction sends being executed", arrayOf("operation", "asyncExecuteFunction"))
    val operationAsyncExecuteFunctionSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of asyncExecuteFunction sends that have completed successfully", arrayOf("operation", "asyncExecuteFunction", "status", "completed"))
    val operationAsyncExecuteFunctionSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of asyncExecuteFunction sends that have failed", arrayOf("operation", "asyncExecuteFunction", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationAsyncExecuteFunctionSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of asyncExecuteFunction sends that have completed successfully", arrayOf("operation", "asyncExecuteFunction"), meterUnit = "nanoseconds")
    //GetClientPRMetadata
    val operationGetClientPRMetadataSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getClientPRMetadata sends being executed", arrayOf("operation", "getClientPRMetadata"))
    val operationGetClientPRMetadataSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getClientPRMetadata sends that have completed successfully", arrayOf("operation", "getClientPRMetadata", "status", "completed"))
    val operationGetClientPRMetadataSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getClientPRMetadata sends that have failed", arrayOf("operation", "getClientPRMetadata", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetClientPRMetadataSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getClientPRMetadata sends that have completed successfully", arrayOf("operation", "getClientPRMetadata"), meterUnit = "nanoseconds")
    //GetClientPartitionAttributes
    val operationGetClientPartitionAttributesSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getClientPartitionAttributes sends being executed", arrayOf("operation", "getClientPartitionAttributes"))
    val operationGetClientPartitionAttributesSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getClientPartitionAttributes sends that have completed successfully", arrayOf("operation", "getClientPartitionAttributes", "status", "completed"))
    val operationGetClientPartitionAttributesSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getClientPartitionAttributes sends that have failed", arrayOf("operation", "getClientPartitionAttributes", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetClientPartitionAttributesSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getClientPartitionAttributes sends that have completed successfully", arrayOf("operation", "getClientPartitionAttributes"), meterUnit = "nanoseconds")
    //GetPDXTypeById
    val operationGetPDXTypeByIdSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getPDXTypeById sends being executed", arrayOf("operation", "getPDXTypeById"))
    val operationGetPDXTypeByIdSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getPDXTypeById sends that have completed successfully", arrayOf("operation", "getPDXTypeById", "status", "completed"))
    val operationGetPDXTypeByIdSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getPDXTypeById sends that have failed", arrayOf("operation", "getPDXTypeById", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetPDXTypeByIdSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getPDXTypeById sends that have completed successfully", arrayOf("operation", "getPDXTypeById"), meterUnit = "nanoseconds")
    //GetPDXIdForType
    val operationGetPDXIdForTypeSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of getPDXTypeForType sends being executed", arrayOf("operation", "getPDXTypeForType"))
    val operationGetPDXIdForTypeSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getPDXTypeForType sends that have completed successfully", arrayOf("operation", "getPDXTypeForType", "status", "completed"))
    val operationGetPDXIdForTypeSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of getPDXTypeForType sends that have failed", arrayOf("operation", "getPDXTypeForType", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetPDXIdForTypeSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of getPDXTypeForType sends that have completed successfully", arrayOf("operation", "getPDXTypeForType"), meterUnit = "nanoseconds")
    //AddPdxType
    val operationAddPdxTypeSendInProgressMeter = GaugeStatisticMeter("connection.operation.send.inprogress",
            "Current number of addPDXType sends being executed", arrayOf("operation", "addPDXType"))
    val operationAddPdxTypeSendCompletedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of addPDXType sends that have completed successfully", arrayOf("operation", "addPDXType", "status", "completed"))
    val operationAddPdxTypeSendFailedMeter = CounterStatisticMeter("connection.operation.send",
            "Total number of addPDXType sends that have failed", arrayOf("operation", "addPDXType", "status", "failed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationAddPdxTypeSendTimerMeter = CounterStatisticMeter("connection.operation.send.timer",
            "Total number of addPDXType sends that have completed successfully", arrayOf("operation", "addPDXType"), meterUnit = "nanoseconds")

    override fun initializeStaticMeters() {

        //Get
        registerMeter(operationGetSendInProgressMeter)
        registerMeter(operationGetSendCompletedMeter)
        registerMeter(operationGetSendFailedMeter)
        registerMeter(operationGetSendTimerMeter)
        //Put
        registerMeter(operationPutSendInProgressMeter)
        registerMeter(operationPutSendCompletedMeter)
        registerMeter(operationPutSendFailedMeter)
        registerMeter(operationPutSendTimerMeter)
        //Destroy
        registerMeter(operationDestroySendInProgressMeter)
        registerMeter(operationDestroySendCompletedMeter)
        registerMeter(operationDestroySendFailedMeter)
        registerMeter(operationDestroySendTimerMeter)
        //Region Destroy
        registerMeter(operationRegionDestroySendInProgressMeter)
        registerMeter(operationRegionDestroySendCompletedMeter)
        registerMeter(operationRegionDestroySendFailedMeter)
        registerMeter(operationRegionDestroySendTimerMeter)
        //Clear
        registerMeter(operationClearSendInProgressMeter)
        registerMeter(operationClearSendCompletedMeter)
        registerMeter(operationClearSendFailedMeter)
        registerMeter(operationClearSendTimerMeter)
        //ContainsKey
        registerMeter(operationContainsKeySendInProgressMeter)
        registerMeter(operationContainsKeySendCompletedMeter)
        registerMeter(operationContainsKeySendFailedMeter)
        registerMeter(operationContainsKeySendTimerMeter)
        //KeySet
        registerMeter(operationKeySetSendInProgressMeter)
        registerMeter(operationKeySetSendCompletedMeter)
        registerMeter(operationKeySetSendFailedMeter)
        registerMeter(operationKeySetSendTimerMeter)
        //Commit
        registerMeter(operationCommitSendInProgressMeter)
        registerMeter(operationCommitSendCompletedMeter)
        registerMeter(operationCommitSendFailedMeter)
        registerMeter(operationCommitSendTimerMeter)
        //Rollback
        registerMeter(operationRollbackSendInProgressMeter)
        registerMeter(operationRollbackSendCompletedMeter)
        registerMeter(operationRollbackSendFailedMeter)
        registerMeter(operationRollbackSendTimerMeter)
        //GetEntry
        registerMeter(operationGetEntrySendInProgressMeter)
        registerMeter(operationGetEntrySendCompletedMeter)
        registerMeter(operationGetEntrySendFailedMeter)
        registerMeter(operationGetEntrySendTimerMeter)
        //TxSynchronization
        registerMeter(operationTxSynchronizationSendInProgressMeter)
        registerMeter(operationTxSynchronizationSendCompletedMeter)
        registerMeter(operationTxSynchronizationSendFailedMeter)
        registerMeter(operationTxSynchronizationSendTimerMeter)
        //TxFailover
        registerMeter(operationTxFailoverSendInProgressMeter)
        registerMeter(operationTxFailoverSendCompletedMeter)
        registerMeter(operationTxFailoverSendFailedMeter)
        registerMeter(operationTxFailoverSendTimerMeter)
        //Size
        registerMeter(operationSizeSendInProgressMeter)
        registerMeter(operationSizeSendCompletedMeter)
        registerMeter(operationSizeSendFailedMeter)
        registerMeter(operationSizeSendTimerMeter)
        //Invalidate
        registerMeter(operationInvalidateSendInProgressMeter)
        registerMeter(operationInvalidateSendCompletedMeter)
        registerMeter(operationInvalidateSendFailedMeter)
        registerMeter(operationInvalidateSendTimerMeter)
        //RegisterInterest
        registerMeter(operationRegisterInterestSendInProgressMeter)
        registerMeter(operationRegisterInterestSendCompletedMeter)
        registerMeter(operationRegisterInterestSendFailedMeter)
        registerMeter(operationRegisterInterestSendTimerMeter)
        //UnregisterInterest
        registerMeter(operationUnregisterInterestSendInProgressMeter)
        registerMeter(operationUnregisterInterestSendCompletedMeter)
        registerMeter(operationUnregisterInterestSendFailedMeter)
        registerMeter(operationUnregisterInterestSendTimerMeter)
        //Query
        registerMeter(operationQuerySendInProgressMeter)
        registerMeter(operationQuerySendCompletedMeter)
        registerMeter(operationQuerySendFailedMeter)
        registerMeter(operationQuerySendTimerMeter)
        //CreateCQ
        registerMeter(operationCreateCQSendInProgressMeter)
        registerMeter(operationCreateCQSendCompletedMeter)
        registerMeter(operationCreateCQSendFailedMeter)
        registerMeter(operationCreateCQSendTimerMeter)
        //StopCQ
        registerMeter(operationStopCQSendInProgressMeter)
        registerMeter(operationStopCQSendCompletedMeter)
        registerMeter(operationStopCQSendFailedMeter)
        registerMeter(operationStopCQSendTimerMeter)
        //CloseCQ
        registerMeter(operationCloseCQSendInProgressMeter)
        registerMeter(operationCloseCQSendCompletedMeter)
        registerMeter(operationCloseCQSendFailedMeter)
        registerMeter(operationCloseCQSendTimerMeter)
        //GetDurableCQs
        registerMeter(operationGetDurableCQsSendInProgressMeter)
        registerMeter(operationGetDurableCQsSendCompletedMeter)
        registerMeter(operationGetDurableCQsSendFailedMeter)
        registerMeter(operationGetDurableCQsSendTimerMeter)
        //ReadyForEvents
        registerMeter(operationReadyForEventsSendInProgressMeter)
        registerMeter(operationReadyForEventsSendCompletedMeter)
        registerMeter(operationReadyForEventsSendFailedMeter)
        registerMeter(operationReadyForEventsSendTimerMeter)
        //GatewayBatch
        registerMeter(operationGatewayBatchSendInProgressMeter)
        registerMeter(operationGatewayBatchSendCompletedMeter)
        registerMeter(operationGatewayBatchSendFailedMeter)
        registerMeter(operationGatewayBatchSendTimerMeter)
        //MakePrimary
        registerMeter(operationMakePrimarySendInProgressMeter)
        registerMeter(operationMakePrimarySendCompletedMeter)
        registerMeter(operationMakePrimarySendFailedMeter)
        registerMeter(operationMakePrimarySendTimerMeter)
        //PrimaryAck
        registerMeter(operationPrimaryAckSendInProgressMeter)
        registerMeter(operationPrimaryAckSendCompletedMeter)
        registerMeter(operationPrimaryAckSendFailedMeter)
        registerMeter(operationPrimaryAckSendTimerMeter)
        //CloseConnection
        registerMeter(operationCloseConnectionSendInProgressMeter)
        registerMeter(operationCloseConnectionSendCompletedMeter)
        registerMeter(operationCloseConnectionSendFailedMeter)
        registerMeter(operationCloseConnectionSendTimerMeter)
        //Ping
        registerMeter(operationPingSendInProgressMeter)
        registerMeter(operationPingSendCompletedMeter)
        registerMeter(operationPingSendFailedMeter)
        registerMeter(operationPingSendTimerMeter)
        //RegisterInstantiators
        registerMeter(operationRegisterInstantiatorsSendInProgressMeter)
        registerMeter(operationRegisterInstantiatorsSendCompletedMeter)
        registerMeter(operationRegisterInstantiatorsSendFailedMeter)
        registerMeter(operationRegisterInstantiatorsSendTimerMeter)
        //RegisterDataSerializers
        registerMeter(operationRegisterDataSerializersSendInProgressMeter)
        registerMeter(operationRegisterDataSerializersSendCompletedMeter)
        registerMeter(operationRegisterDataSerializersSendFailedMeter)
        registerMeter(operationRegisterDataSerializersSendTimerMeter)
        //PutAll
        registerMeter(operationPutAllSendInProgressMeter)
        registerMeter(operationPutAllSendCompletedMeter)
        registerMeter(operationPutAllSendFailedMeter)
        registerMeter(operationPutAllSendTimerMeter)
        //RemoveAll
        registerMeter(operationRemoveAllSendInProgressMeter)
        registerMeter(operationRemoveAllSendCompletedMeter)
        registerMeter(operationRemoveAllSendFailedMeter)
        registerMeter(operationRemoveAllSendTimerMeter)
        //GetAll
        registerMeter(operationGetAllSendInProgressMeter)
        registerMeter(operationGetAllSendCompletedMeter)
        registerMeter(operationGetAllSendFailedMeter)
        registerMeter(operationGetAllSendTimerMeter)
        //ExecuteFunction
        registerMeter(operationExecuteFunctionSendInProgressMeter)
        registerMeter(operationExecuteFunctionSendCompletedMeter)
        registerMeter(operationExecuteFunctionSendFailedMeter)
        registerMeter(operationExecuteFunctionSendTimerMeter)
        //AsyncExecuteFunction
        registerMeter(operationAsyncExecuteFunctionSendInProgressMeter)
        registerMeter(operationAsyncExecuteFunctionSendCompletedMeter)
        registerMeter(operationAsyncExecuteFunctionSendFailedMeter)
        registerMeter(operationAsyncExecuteFunctionSendTimerMeter)
        //GetClientPRMetadata
        registerMeter(operationGetClientPRMetadataSendInProgressMeter)
        registerMeter(operationGetClientPRMetadataSendCompletedMeter)
        registerMeter(operationGetClientPRMetadataSendFailedMeter)
        registerMeter(operationGetClientPRMetadataSendTimerMeter)
        //GetClientPartitionAttributes
        registerMeter(operationGetClientPartitionAttributesSendInProgressMeter)
        registerMeter(operationGetClientPartitionAttributesSendCompletedMeter)
        registerMeter(operationGetClientPartitionAttributesSendFailedMeter)
        registerMeter(operationGetClientPartitionAttributesSendTimerMeter)
        //GetPDXTypeById
        registerMeter(operationGetPDXTypeByIdSendInProgressMeter)
        registerMeter(operationGetPDXTypeByIdSendCompletedMeter)
        registerMeter(operationGetPDXTypeByIdSendFailedMeter)
        registerMeter(operationGetPDXTypeByIdSendTimerMeter)
        //GetPDXIdForType
        registerMeter(operationGetPDXIdForTypeSendInProgressMeter)
        registerMeter(operationGetPDXIdForTypeSendCompletedMeter)
        registerMeter(operationGetPDXIdForTypeSendFailedMeter)
        registerMeter(operationGetPDXIdForTypeSendTimerMeter)
        //AddPdxType
        registerMeter(operationAddPdxTypeSendInProgressMeter)
        registerMeter(operationAddPdxTypeSendCompletedMeter)
        registerMeter(operationAddPdxTypeSendFailedMeter)
        registerMeter(operationAddPdxTypeSendTimerMeter)
    }
}