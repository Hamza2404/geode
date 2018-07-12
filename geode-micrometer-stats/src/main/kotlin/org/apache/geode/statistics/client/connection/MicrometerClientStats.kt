package org.apache.geode.statistics.client.connection

import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup

class MicrometerClientStats(statisticsFactory: StatisticsFactory?,private val poolName:String) :
        MicrometerMeterGroup(statisticsFactory,"ClientConnectionStats-$poolName") {

    override fun getGroupTags(): Array<String> = arrayOf("poolName", poolName)

    //Get
    val operationGetInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of gets being executed", arrayOf("operationType", "get"))
    val operationGetCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gets completed successfully", arrayOf("operationType", "get", "status", "success"))
    val operationGetFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gets that have failed", arrayOf("operationType", "get", "status", "failure"))
    val operationGetTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gets that have timed out", arrayOf("operationType", "get", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing gets", arrayOf("operationType", "get"), "nanoseconds")
    //Put
    val operationPutInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of puts being executed", arrayOf("operationType", "put"))
    val operationPutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of puts completed successfully", arrayOf("operationType", "put", "status", "success"))
    val operationPutFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of puts that have failed", arrayOf("operationType", "put", "status", "failure"))
    val operationPutTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of puts that have timed out", arrayOf("operationType", "put", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPutTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing puts", arrayOf("operationType", "put"), "nanoseconds")
    //Destroys
    val operationDestroyInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of destroys being executed", arrayOf("operationType", "destroy"))
    val operationDestroyCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of destroys completed successfully", arrayOf("operationType", "destroy", "status", "success"))
    val operationDestroyFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of destroys that have failed", arrayOf("operationType", "destroy", "status", "failure"))
    val operationDestroyTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of destroys that have timed out", arrayOf("operationType", "destroy", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationDestroyTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing destroys", arrayOf("operationType", "destroy"), "nanoseconds")
    //Region Destroys
    val operationRegionDestroyInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of region destroys being executed", arrayOf("operationType", "region-destroy"))
    val operationRegionDestroyCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of region destroys completed successfully", arrayOf("operationType", "region-destroy", "status", "success"))
    val operationRegionDestroyFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of region destroys that have failed", arrayOf("operationType", "region-destroy", "status", "failure"))
    val operationRegionDestroyTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of region destroys that have timed out", arrayOf("operationType", "region-destroy", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegionDestroyTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing region destroys", arrayOf("operationType", "region-destroy"), "nanoseconds")
    //Clear
    val operationClearInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of clears being executed", arrayOf("operationType", "clear"))
    val operationClearCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of clears completed successfully", arrayOf("operationType", "clear", "status", "success"))
    val operationClearFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of clears that have failed", arrayOf("operationType", "clear", "status", "failure"))
    val operationClearTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of clears that have timed out", arrayOf("operationType", "clear", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationClearTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing clears", arrayOf("operationType", "clear"), "nanoseconds")
    //Contains Keys
    val operationContainsKeysInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of containsKeys being executed", arrayOf("operationType", "containsKeys"))
    val operationContainsKeysCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of containsKeys completed successfully", arrayOf("operationType", "containsKeys", "status", "success"))
    val operationContainsKeysFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of containsKeys that have failed", arrayOf("operationType", "containsKeys", "status", "failure"))
    val operationContainsKeysTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of containsKeys that have timed out", arrayOf("operationType", "containsKeys", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationContainsKeysTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing containsKeys", arrayOf("operationType", "containsKeys"), "nanoseconds")
    //keySet
    val operationKeySetInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of keySets being executed", arrayOf("operationType", "keySet"))
    val operationKeySetCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of keySets completed successfully", arrayOf("operationType", "keySet", "status", "success"))
    val operationKeySetFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of keySets that have failed", arrayOf("operationType", "keySet", "status", "failure"))
    val operationKeySetTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of keySets that have timed out", arrayOf("operationType", "keySet", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationKeySetTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing keySets", arrayOf("operationType", "keySet"), "nanoseconds")
    //commit
    val operationCommitInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of commits being executed", arrayOf("operationType", "commit"))
    val operationCommitCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of commits completed successfully", arrayOf("operationType", "commit", "status", "success"))
    val operationCommitFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of commits that have failed", arrayOf("operationType", "commit", "status", "failure"))
    val operationCommitTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of commits that have timed out", arrayOf("operationType", "commit", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCommitTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing commits", arrayOf("operationType", "commit"), "nanoseconds")
    //rollback
    val operationRollbackInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of rollbacks being executed", arrayOf("operationType", "rollback"))
    val operationRollbackCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of rollbacks completed successfully", arrayOf("operationType", "rollback", "status", "success"))
    val operationRollbackFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of rollbacks that have failed", arrayOf("operationType", "rollback", "status", "failure"))
    val operationRollbackTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of rollbacks that have timed out", arrayOf("operationType", "rollback", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRollbackTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing rollbacks", arrayOf("operationType", "rollback"), "nanoseconds")
    //getEntries
    val operationGetEntryInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getEntries being executed", arrayOf("operationType", "getEntries"))
    val operationGetEntryCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getEntries completed successfully", arrayOf("operationType", "getEntries", "status", "success"))
    val operationGetEntryFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getEntries that have failed", arrayOf("operationType", "getEntries", "status", "failure"))
    val operationGetEntryTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getEntries that have timed out", arrayOf("operationType", "getEntries", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetEntryTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getEntries", arrayOf("operationType", "getEntries"), "nanoseconds")
    //jtaSynchronization
    val operationTxSynchronizationInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of jtaSynchronization being executed", arrayOf("operationType", "jtaSynchronization"))
    val operationTxSynchronizationCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of jtaSynchronization completed successfully", arrayOf("operationType", "jtaSynchronization", "status", "success"))
    val operationTxSynchronizationFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of jtaSynchronization that have failed", arrayOf("operationType", "jtaSynchronization", "status", "failure"))
    val operationTxSynchronizationTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of jtaSynchronization that have timed out", arrayOf("operationType", "jtaSynchronization", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationTxSynchronizationTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing jtaSynchronization", arrayOf("operationType", "jtaSynchronization"), "nanoseconds")
    //txFailovers
    val operationTxFailoverInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of txFailovers being executed", arrayOf("operationType", "txFailovers"))
    val operationTxFailoverCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of txFailovers completed successfully", arrayOf("operationType", "txFailovers", "status", "success"))
    val operationTxFailoverFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of txFailovers that have failed", arrayOf("operationType", "txFailovers", "status", "failure"))
    val operationTxFailoverTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of txFailovers that have timed out", arrayOf("operationType", "txFailovers", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationTxFailoverTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing txFailovers", arrayOf("operationType", "txFailovers"), "nanoseconds")
    //sizes
    val operationSizeInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of sizes being executed", arrayOf("operationType", "sizes"))
    val operationSizeCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of sizes completed successfully", arrayOf("operationType", "sizes", "status", "success"))
    val operationSizeFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of sizes that have failed", arrayOf("operationType", "sizes", "status", "failure"))
    val operationSizeTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of sizes that have timed out", arrayOf("operationType", "sizes", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationSizeTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing sizes", arrayOf("operationType", "sizes"), "nanoseconds")
    //invalidates
    val operationInvalidateInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of invalidates being executed", arrayOf("operationType", "invalidates"))
    val operationInvalidateCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of invalidates completed successfully", arrayOf("operationType", "invalidates", "status", "success"))
    val operationInvalidateFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of invalidates that have failed", arrayOf("operationType", "invalidates", "status", "failure"))
    val operationInvalidateTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of invalidates that have timed out", arrayOf("operationType", "invalidates", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationInvalidateTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing invalidates", arrayOf("operationType", "invalidates"), "nanoseconds")
    //registerInterest
    val operationRegisterInterestInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of registerInterest being executed", arrayOf("operationType", "registerInterest"))
    val operationRegisterInterestCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInterest completed successfully", arrayOf("operationType", "registerInterest", "status", "success"))
    val operationRegisterInterestFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInterest that have failed", arrayOf("operationType", "registerInterest", "status", "failure"))
    val operationRegisterInterestTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInterest that have timed out", arrayOf("operationType", "registerInterest", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterInterestTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing registerInterest", arrayOf("operationType", "registerInterest"), "nanoseconds")
    //unregisterInterest
    val operationUnregisterInterestInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of unregisterInterest being executed", arrayOf("operationType", "unregisterInterest"))
    val operationUnregisterInterestCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of unregisterInterest completed successfully", arrayOf("operationType", "unregisterInterest", "status", "success"))
    val operationUnregisterInterestFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of unregisterInterest that have failed", arrayOf("operationType", "unregisterInterest", "status", "failure"))
    val operationUnregisterInterestTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of unregisterInterest that have timed out", arrayOf("operationType", "unregisterInterest", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationUnregisterInterestTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing unregisterInterest", arrayOf("operationType", "unregisterInterest"), "nanoseconds")
    //queries
    val operationQueryInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of queries being executed", arrayOf("operationType", "queries"))
    val operationQueryCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of queries completed successfully", arrayOf("operationType", "queries", "status", "success"))
    val operationQueryFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of queries that have failed", arrayOf("operationType", "queries", "status", "failure"))
    val operationQueryTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of queries that have timed out", arrayOf("operationType", "queries", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationQueryTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing queries", arrayOf("operationType", "queries"), "nanoseconds")
    //createCQ
    val operationCreateCQInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of createCQ being executed", arrayOf("operationType", "createCQ"))
    val operationCreateCQCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of createCQ completed successfully", arrayOf("operationType", "createCQ", "status", "success"))
    val operationCreateCQFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of createCQ that have failed", arrayOf("operationType", "createCQ", "status", "failure"))
    val operationCreateCQTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of createCQ that have timed out", arrayOf("operationType", "createCQ", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCreateCQTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing createCQ", arrayOf("operationType", "createCQ"), "nanoseconds")
    //stopCQ
    val operationStopCQInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of stopCQ being executed", arrayOf("operationType", "stopCQ"))
    val operationStopCQCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of stopCQ completed successfully", arrayOf("operationType", "stopCQ", "status", "success"))
    val operationStopCQFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of stopCQ that have failed", arrayOf("operationType", "stopCQ", "status", "failure"))
    val operationStopCQTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of stopCQ that have timed out", arrayOf("operationType", "stopCQ", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationStopCQTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing stopCQ", arrayOf("operationType", "stopCQ"), "nanoseconds")
    //closeCQ
    val operationCloseCQInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of closeCQ being executed", arrayOf("operationType", "closeCQ"))
    val operationCloseCQCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCQ completed successfully", arrayOf("operationType", "closeCQ", "status", "success"))
    val operationCloseCQFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCQ that have failed", arrayOf("operationType", "closeCQ", "status", "failure"))
    val operationCloseCQTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCQ that have timed out", arrayOf("operationType", "closeCQ", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCloseCQTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing closeCQ", arrayOf("operationType", "closeCQ"), "nanoseconds")
    //getDurableCQ
    val operationGetDurableCQsInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getDurableCQ being executed", arrayOf("operationType", "getDurableCQ"))
    val operationGetDurableCQsCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getDurableCQ completed successfully", arrayOf("operationType", "getDurableCQ", "status", "success"))
    val operationGetDurableCQsFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getDurableCQ that have failed", arrayOf("operationType", "getDurableCQ", "status", "failure"))
    val operationGetDurableCQsTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getDurableCQ that have timed out", arrayOf("operationType", "getDurableCQ", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetDurableCQsTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getDurableCQ", arrayOf("operationType", "getDurableCQ"), "nanoseconds")
    //readyForEvents
    val operationReadyForEventsInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of readyForEvents being executed", arrayOf("operationType", "readyForEvents"))
    val operationReadyForEventsCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of readyForEvents completed successfully", arrayOf("operationType", "readyForEvents", "status", "success"))
    val operationReadyForEventsFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of readyForEvents that have failed", arrayOf("operationType", "readyForEvents", "status", "failure"))
    val operationReadyForEventsTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of readyForEvents that have timed out", arrayOf("operationType", "readyForEvents", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationReadyForEventsTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing readyForEvents", arrayOf("operationType", "readyForEvents"), "nanoseconds")
    //gatewayBatches
    val operationGatewayBatchInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of gatewayBatches being executed", arrayOf("operationType", "gatewayBatches"))
    val operationGatewayBatchCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gatewayBatches completed successfully", arrayOf("operationType", "gatewayBatches", "status", "success"))
    val operationGatewayBatchFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gatewayBatches that have failed", arrayOf("operationType", "gatewayBatches", "status", "failure"))
    val operationGatewayBatchTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of gatewayBatches that have timed out", arrayOf("operationType", "gatewayBatches", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGatewayBatchTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing gatewayBatches", arrayOf("operationType", "gatewayBatches"), "nanoseconds")
    //makePrimarys
    val operationMakePrimaryInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of makePrimarys being executed", arrayOf("operationType", "makePrimarys"))
    val operationMakePrimaryCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of makePrimarys completed successfully", arrayOf("operationType", "makePrimarys", "status", "success"))
    val operationMakePrimaryFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of makePrimarys that have failed", arrayOf("operationType", "makePrimarys", "status", "failure"))
    val operationMakePrimaryTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of makePrimarys that have timed out", arrayOf("operationType", "makePrimarys", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationMakePrimaryTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing makePrimarys", arrayOf("operationType", "makePrimarys"), "nanoseconds")
    //primaryAcks
    val operationPrimaryAckInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of primaryAcks being executed", arrayOf("operationType", "primaryAcks"))
    val operationPrimaryAckCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of primaryAcks completed successfully", arrayOf("operationType", "primaryAcks", "status", "success"))
    val operationPrimaryAckFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of primaryAcks that have failed", arrayOf("operationType", "primaryAcks", "status", "failure"))
    val operationPrimaryAckTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of primaryAcks that have timed out", arrayOf("operationType", "primaryAcks", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPrimaryAckTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing primaryAcks", arrayOf("operationType", "primaryAcks"), "nanoseconds")
    //primaryAcks
    val operationCloseConnectionInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of closeCons being executed", arrayOf("operationType", "closeCons"))
    val operationCloseConnectionCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCons completed successfully", arrayOf("operationType", "closeCons", "status", "success"))
    val operationCloseConnectionFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCons that have failed", arrayOf("operationType", "closeCons", "status", "failure"))
    val operationCloseConnectionTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of closeCons that have timed out", arrayOf("operationType", "closeCons", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationCloseConnectionTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing closeCons", arrayOf("operationType", "closeCons"), "nanoseconds")
    //pings
    val operationPingInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of pings being executed", arrayOf("operationType", "pings"))
    val operationPingCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of pings completed successfully", arrayOf("operationType", "pings", "status", "success"))
    val operationPingFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of pings that have failed", arrayOf("operationType", "pings", "status", "failure"))
    val operationPingTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of pings that have timed out", arrayOf("operationType", "pings", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPingTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing pings", arrayOf("operationType", "pings"), "nanoseconds")
    //registerInstantiators
    val operationRegisterInstantiatorsInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of registerInstantiators being executed", arrayOf("operationType", "registerInstantiators"))
    val operationRegisterInstantiatorsCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInstantiators completed successfully", arrayOf("operationType", "registerInstantiators", "status", "success"))
    val operationRegisterInstantiatorsFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInstantiators that have failed", arrayOf("operationType", "registerInstantiators", "status", "failure"))
    val operationRegisterInstantiatorsTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerInstantiators that have timed out", arrayOf("operationType", "registerInstantiators", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterInstantiatorsTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing registerInstantiators", arrayOf("operationType", "registerInstantiators"), "nanoseconds")
    //registerDataSerializers
    val operationRegisterDataSerializersInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of registerDataSerializers being executed", arrayOf("operationType", "registerDataSerializers"))
    val operationRegisterDataSerializersCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerDataSerializers completed successfully", arrayOf("operationType", "registerDataSerializers", "status", "success"))
    val operationRegisterDataSerializersFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerDataSerializers that have failed", arrayOf("operationType", "registerDataSerializers", "status", "failure"))
    val operationRegisterDataSerializersTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of registerDataSerializers that have timed out", arrayOf("operationType", "registerDataSerializers", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRegisterDataSerializersTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing registerDataSerializers", arrayOf("operationType", "registerDataSerializers"), "nanoseconds")
    //putAll
    val operationPutAllInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of putAll being executed", arrayOf("operationType", "putAll"))
    val operationPutAllCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of putAll completed successfully", arrayOf("operationType", "putAll", "status", "success"))
    val operationPutAllFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of putAll that have failed", arrayOf("operationType", "putAll", "status", "failure"))
    val operationPutAllTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of putAll that have timed out", arrayOf("operationType", "putAll", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationPutAllTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing putAll", arrayOf("operationType", "putAll"), "nanoseconds")
    //removeAll
    val operationRemoveAllInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of removeAll being executed", arrayOf("operationType", "removeAll"))
    val operationRemoveAllCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of removeAll completed successfully", arrayOf("operationType", "removeAll", "status", "success"))
    val operationRemoveAllFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of removeAll that have failed", arrayOf("operationType", "removeAll", "status", "failure"))
    val operationRemoveAllTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of removeAll that have timed out", arrayOf("operationType", "removeAll", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationRemoveAllTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing removeAll", arrayOf("operationType", "removeAll"), "nanoseconds")
    //getAll
    val operationGetAllInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getAll being executed", arrayOf("operationType", "getAll"))
    val operationGetAllCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getAll completed successfully", arrayOf("operationType", "getAll", "status", "success"))
    val operationGetAllFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getAll that have failed", arrayOf("operationType", "getAll", "status", "failure"))
    val operationGetAllTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getAll that have timed out", arrayOf("operationType", "getAll", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetAllTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getAll", arrayOf("operationType", "getAll"), "nanoseconds")
    //executeFunction
    val operationExecuteFunctionInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of executeFunction being executed", arrayOf("operationType", "executeFunction"))
    val operationExecuteFunctionCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of executeFunction completed successfully", arrayOf("operationType", "executeFunction", "status", "success"))
    val operationExecuteFunctionFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of executeFunction that have failed", arrayOf("operationType", "executeFunction", "status", "failure"))
    val operationExecuteFunctionTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of executeFunction that have timed out", arrayOf("operationType", "executeFunction", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationExecuteFunctionTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing executeFunction", arrayOf("operationType", "executeFunction"), "nanoseconds")
    //asyncExecuteFunction
    val operationAsyncExecuteFunctionInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of asyncExecuteFunction being executed", arrayOf("operationType", "asyncExecuteFunction"))
    val operationAsyncExecuteFunctionCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of asyncExecuteFunction completed successfully", arrayOf("operationType", "asyncExecuteFunction", "status", "success"))
    val operationAsyncExecuteFunctionFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of asyncExecuteFunction that have failed", arrayOf("operationType", "asyncExecuteFunction", "status", "failure"))
    val operationAsyncExecuteFunctionTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of asyncExecuteFunction that have timed out", arrayOf("operationType", "asyncExecuteFunction", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationAsyncExecuteFunctionTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing asyncExecuteFunction", arrayOf("operationType", "asyncExecuteFunction"), "nanoseconds")
    //getClientPRMetadata
    val operationGetClientPRMetadataInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getClientPRMetadata being executed", arrayOf("operationType", "getClientPRMetadata"))
    val operationGetClientPRMetadataCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPRMetadata completed successfully", arrayOf("operationType", "getClientPRMetadata", "status", "success"))
    val operationGetClientPRMetadataFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPRMetadata that have failed", arrayOf("operationType", "getClientPRMetadata", "status", "failure"))
    val operationGetClientPRMetadataTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPRMetadata that have timed out", arrayOf("operationType", "getClientPRMetadata", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetClientPRMetadataTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getClientPRMetadata", arrayOf("operationType", "getClientPRMetadata"), "nanoseconds")
    //getClientPartitionAttributes
    val operationGetClientPartitionAttributesInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getClientPartitionAttributes being executed", arrayOf("operationType", "getClientPartitionAttributes"))
    val operationGetClientPartitionAttributesCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPartitionAttributes completed successfully", arrayOf("operationType", "getClientPartitionAttributes", "status", "success"))
    val operationGetClientPartitionAttributesFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPartitionAttributes that have failed", arrayOf("operationType", "getClientPartitionAttributes", "status", "failure"))
    val operationGetClientPartitionAttributesTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getClientPartitionAttributes that have timed out", arrayOf("operationType", "getClientPartitionAttributes", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetClientPartitionAttributesTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getClientPartitionAttributes", arrayOf("operationType", "getClientPartitionAttributes"), "nanoseconds")
    //getPDXTypeById
    val operationGetPDXTypeByIdInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getPDXTypeById being executed", arrayOf("operationType", "getPDXTypeById"))
    val operationGetPDXTypeByIdCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXTypeById completed successfully", arrayOf("operationType", "getPDXTypeById", "status", "success"))
    val operationGetPDXTypeByIdFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXTypeById that have failed", arrayOf("operationType", "getPDXTypeById", "status", "failure"))
    val operationGetPDXTypeByIdTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXTypeById that have timed out", arrayOf("operationType", "getPDXTypeById", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetPDXTypeByIdTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getPDXTypeById", arrayOf("operationType", "getPDXTypeById"), "nanoseconds")
    //getPDXIdForType
    val operationGetPDXIdForTypeInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of getPDXIdForType being executed", arrayOf("operationType", "getPDXIdForType"))
    val operationGetPDXIdForTypeCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXIdForType completed successfully", arrayOf("operationType", "getPDXIdForType", "status", "success"))
    val operationGetPDXIdForTypeFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXIdForType that have failed", arrayOf("operationType", "getPDXIdForType", "status", "failure"))
    val operationGetPDXIdForTypeTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of getPDXIdForType that have timed out", arrayOf("operationType", "getPDXIdForType", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationGetPDXIdForTypeTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing getPDXIdForType", arrayOf("operationType", "getPDXIdForType"), "nanoseconds")
    //addPdxType
    val operationAddPdxTypeInProgressMeter = GaugeStatisticMeter("connection.operation.inprogress",
            "Current number of addPdxType being executed", arrayOf("operationType", "addPdxType"))
    val operationAddPdxTypeCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of addPdxType completed successfully", arrayOf("operationType", "addPdxType", "status", "success"))
    val operationAddPdxTypeFailureCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of addPdxType that have failed", arrayOf("operationType", "addPdxType", "status", "failure"))
    val operationAddPdxTypeTimeoutCountMeter = CounterStatisticMeter("connection.operation.count",
            "Total number of addPdxType that have timed out", arrayOf("operationType", "addPdxType", "status", "timeout"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    val operationAddPdxTypeTimer = CounterStatisticMeter("connection.operation.timer",
            "Total amount of time, in nanoseconds spent doing addPdxType", arrayOf("operationType", "addPdxType"), "nanoseconds")
    //Connections
    val connectionsCurrentMeter = GaugeStatisticMeter("connection.current.count", "Current number of connections")
    val connectionsCreateMeter = CounterStatisticMeter("connection.count",
            "Total number of times a connection has been created.", arrayOf("status", "disconnect"))
    val connectionsDisconnectMeter = CounterStatisticMeter("connection.count",
            "Total number of times a connection has been destroyed.", arrayOf("status", "disconnect"))

    val receivedBytesMeter = CounterStatisticMeter("connection.received.bytes",
            "Total number of bytes received (as responses) from server over a client-to-server connection.", meterUnit = "bytes")
    val sentBytesMeter = CounterStatisticMeter("connection.sent.bytes",
            "Total number of bytes sent to server over a client-to-server connection.", meterUnit = "bytes")
    val messagesReceivedMeter = GaugeStatisticMeter("connection.received.messages",
            "Current number of message being received off the network or being processed after reception over a client-to-server connection.")
    val messagesReceivedBytesMeter = GaugeStatisticMeter("connection.received.messages.bytes",
            "Current number of bytes consumed by messages being received or processed over a client-to-server connection.", meterUnit = "bytes")

    override fun initializeStaticMeters() {
        //Get
        registerMeter(operationGetInProgressMeter)
        registerMeter(operationGetCountMeter)
        registerMeter(operationGetFailureCountMeter)
        registerMeter(operationGetTimeoutCountMeter)
        registerMeter(operationGetTimer)
        //Put
        registerMeter(operationPutInProgressMeter)
        registerMeter(operationPutCountMeter)
        registerMeter(operationPutFailureCountMeter)
        registerMeter(operationPutTimeoutCountMeter)
        registerMeter(operationPutTimer)
        //Destroys
        registerMeter(operationDestroyInProgressMeter)
        registerMeter(operationDestroyCountMeter)
        registerMeter(operationDestroyFailureCountMeter)
        registerMeter(operationDestroyTimeoutCountMeter)
        registerMeter(operationDestroyTimer)
        //Region Destroys
        registerMeter(operationRegionDestroyInProgressMeter)
        registerMeter(operationRegionDestroyCountMeter)
        registerMeter(operationRegionDestroyFailureCountMeter)
        registerMeter(operationRegionDestroyTimeoutCountMeter)
        registerMeter(operationRegionDestroyTimer)
        //Clear
        registerMeter(operationClearInProgressMeter)
        registerMeter(operationClearCountMeter)
        registerMeter(operationClearFailureCountMeter)
        registerMeter(operationClearTimeoutCountMeter)
        registerMeter(operationClearTimer)
        //Contains Keys
        registerMeter(operationContainsKeysInProgressMeter)
        registerMeter(operationContainsKeysCountMeter)
        registerMeter(operationContainsKeysFailureCountMeter)
        registerMeter(operationContainsKeysTimeoutCountMeter)
        registerMeter(operationContainsKeysTimer)
        //keySet
        registerMeter(operationKeySetInProgressMeter)
        registerMeter(operationKeySetCountMeter)
        registerMeter(operationKeySetFailureCountMeter)
        registerMeter(operationKeySetTimeoutCountMeter)
        registerMeter(operationKeySetTimer)
        //commit
        registerMeter(operationCommitInProgressMeter)
        registerMeter(operationCommitCountMeter)
        registerMeter(operationCommitFailureCountMeter)
        registerMeter(operationCommitTimeoutCountMeter)
        registerMeter(operationCommitTimer)
        //rollback
        registerMeter(operationRollbackInProgressMeter)
        registerMeter(operationRollbackCountMeter)
        registerMeter(operationRollbackFailureCountMeter)
        registerMeter(operationRollbackTimeoutCountMeter)
        registerMeter(operationRollbackTimer)
        //getEntries
        registerMeter(operationGetEntryInProgressMeter)
        registerMeter(operationGetEntryCountMeter)
        registerMeter(operationGetEntryFailureCountMeter)
        registerMeter(operationGetEntryTimeoutCountMeter)
        registerMeter(operationGetEntryTimer)
        //jtaSynchronization
        registerMeter(operationTxSynchronizationInProgressMeter)
        registerMeter(operationTxSynchronizationCountMeter)
        registerMeter(operationTxSynchronizationFailureCountMeter)
        registerMeter(operationTxSynchronizationTimeoutCountMeter)
        registerMeter(operationTxSynchronizationTimer)
        //txFailovers
        registerMeter(operationTxFailoverInProgressMeter)
        registerMeter(operationTxFailoverCountMeter)
        registerMeter(operationTxFailoverFailureCountMeter)
        registerMeter(operationTxFailoverTimeoutCountMeter)
        registerMeter(operationTxFailoverTimer)
        //sizes
        registerMeter(operationSizeInProgressMeter)
        registerMeter(operationSizeCountMeter)
        registerMeter(operationSizeFailureCountMeter)
        registerMeter(operationSizeTimeoutCountMeter)
        registerMeter(operationSizeTimer)
        //invalidates
        registerMeter(operationInvalidateInProgressMeter)
        registerMeter(operationInvalidateCountMeter)
        registerMeter(operationInvalidateFailureCountMeter)
        registerMeter(operationInvalidateTimeoutCountMeter)
        registerMeter(operationInvalidateTimer)
        //registerInterest
        registerMeter(operationRegisterInterestInProgressMeter)
        registerMeter(operationRegisterInterestCountMeter)
        registerMeter(operationRegisterInterestFailureCountMeter)
        registerMeter(operationRegisterInterestTimeoutCountMeter)
        registerMeter(operationRegisterInterestTimer)
        //unregisterInterest
        registerMeter(operationUnregisterInterestInProgressMeter)
        registerMeter(operationUnregisterInterestCountMeter)
        registerMeter(operationUnregisterInterestFailureCountMeter)
        registerMeter(operationUnregisterInterestTimeoutCountMeter)
        registerMeter(operationUnregisterInterestTimer)
        //queries
        registerMeter(operationQueryInProgressMeter)
        registerMeter(operationQueryCountMeter)
        registerMeter(operationQueryFailureCountMeter)
        registerMeter(operationQueryTimeoutCountMeter)
        registerMeter(operationQueryTimer)
        //createCQ
        registerMeter(operationCreateCQInProgressMeter)
        registerMeter(operationCreateCQCountMeter)
        registerMeter(operationCreateCQFailureCountMeter)
        registerMeter(operationCreateCQTimeoutCountMeter)
        registerMeter(operationCreateCQTimer)
        //stopCQ
        registerMeter(operationStopCQInProgressMeter)
        registerMeter(operationStopCQCountMeter)
        registerMeter(operationStopCQFailureCountMeter)
        registerMeter(operationStopCQTimeoutCountMeter)
        registerMeter(operationStopCQTimer)
        //closeCQ
        registerMeter(operationCloseCQInProgressMeter)
        registerMeter(operationCloseCQCountMeter)
        registerMeter(operationCloseCQFailureCountMeter)
        registerMeter(operationCloseCQTimeoutCountMeter)
        registerMeter(operationCloseCQTimer)
        //getDurableCQ
        registerMeter(operationGetDurableCQsInProgressMeter)
        registerMeter(operationGetDurableCQsCountMeter)
        registerMeter(operationGetDurableCQsFailureCountMeter)
        registerMeter(operationGetDurableCQsTimeoutCountMeter)
        registerMeter(operationGetDurableCQsTimer)
        //readyForEvents
        registerMeter(operationReadyForEventsInProgressMeter)
        registerMeter(operationReadyForEventsCountMeter)
        registerMeter(operationReadyForEventsFailureCountMeter)
        registerMeter(operationReadyForEventsTimeoutCountMeter)
        registerMeter(operationReadyForEventsTimer)
        //gatewayBatch
        registerMeter(operationGatewayBatchInProgressMeter)
        registerMeter(operationGatewayBatchCountMeter)
        registerMeter(operationGatewayBatchFailureCountMeter)
        registerMeter(operationGatewayBatchTimeoutCountMeter)
        registerMeter(operationGatewayBatchTimer)
        //makePrimarys
        registerMeter(operationMakePrimaryInProgressMeter)
        registerMeter(operationMakePrimaryCountMeter)
        registerMeter(operationMakePrimaryFailureCountMeter)
        registerMeter(operationMakePrimaryTimeoutCountMeter)
        registerMeter(operationMakePrimaryTimer)
        //primaryAcks
        registerMeter(operationPrimaryAckInProgressMeter)
        registerMeter(operationPrimaryAckCountMeter)
        registerMeter(operationPrimaryAckFailureCountMeter)
        registerMeter(operationPrimaryAckTimeoutCountMeter)
        registerMeter(operationPrimaryAckTimer)
        //primaryAcks
        registerMeter(operationCloseConnectionInProgressMeter)
        registerMeter(operationCloseConnectionCountMeter)
        registerMeter(operationCloseConnectionFailureCountMeter)
        registerMeter(operationCloseConnectionTimeoutCountMeter)
        registerMeter(operationCloseConnectionTimer)
        //pings
        registerMeter(operationPingInProgressMeter)
        registerMeter(operationPingCountMeter)
        registerMeter(operationPingFailureCountMeter)
        registerMeter(operationPingTimeoutCountMeter)
        registerMeter(operationPingTimer)
        //registerInstantiators
        registerMeter(operationRegisterInstantiatorsInProgressMeter)
        registerMeter(operationRegisterInstantiatorsCountMeter)
        registerMeter(operationRegisterInstantiatorsFailureCountMeter)
        registerMeter(operationRegisterInstantiatorsTimeoutCountMeter)
        registerMeter(operationRegisterInstantiatorsTimer)
        //registerDataSerializers
        registerMeter(operationRegisterDataSerializersInProgressMeter)
        registerMeter(operationRegisterDataSerializersCountMeter)
        registerMeter(operationRegisterDataSerializersFailureCountMeter)
        registerMeter(operationRegisterDataSerializersTimeoutCountMeter)
        registerMeter(operationRegisterDataSerializersTimer)
        //putAll
        registerMeter(operationPutAllInProgressMeter)
        registerMeter(operationPutAllCountMeter)
        registerMeter(operationPutAllFailureCountMeter)
        registerMeter(operationPutAllTimeoutCountMeter)
        registerMeter(operationPutAllTimer)
        //removeAll
        registerMeter(operationRemoveAllInProgressMeter)
        registerMeter(operationRemoveAllCountMeter)
        registerMeter(operationRemoveAllFailureCountMeter)
        registerMeter(operationRemoveAllTimeoutCountMeter)
        registerMeter(operationRemoveAllTimer)
        //getAll
        registerMeter(operationGetAllInProgressMeter)
        registerMeter(operationGetAllCountMeter)
        registerMeter(operationGetAllFailureCountMeter)
        registerMeter(operationGetAllTimeoutCountMeter)
        registerMeter(operationGetAllTimer)
        //executeFunction
        registerMeter(operationExecuteFunctionInProgressMeter)
        registerMeter(operationExecuteFunctionCountMeter)
        registerMeter(operationExecuteFunctionFailureCountMeter)
        registerMeter(operationExecuteFunctionTimeoutCountMeter)
        registerMeter(operationExecuteFunctionTimer)
        //asyncExecuteFunction
        registerMeter(operationAsyncExecuteFunctionInProgressMeter)
        registerMeter(operationAsyncExecuteFunctionCountMeter)
        registerMeter(operationAsyncExecuteFunctionFailureCountMeter)
        registerMeter(operationAsyncExecuteFunctionTimeoutCountMeter)
        registerMeter(operationAsyncExecuteFunctionTimer)
        //getClientPRMetadata
        registerMeter(operationGetClientPRMetadataInProgressMeter)
        registerMeter(operationGetClientPRMetadataCountMeter)
        registerMeter(operationGetClientPRMetadataFailureCountMeter)
        registerMeter(operationGetClientPRMetadataTimeoutCountMeter)
        registerMeter(operationGetClientPRMetadataTimer)
        //getClientPartitionAttributes
        registerMeter(operationGetClientPartitionAttributesInProgressMeter)
        registerMeter(operationGetClientPartitionAttributesCountMeter)
        registerMeter(operationGetClientPartitionAttributesFailureCountMeter)
        registerMeter(operationGetClientPartitionAttributesTimeoutCountMeter)
        registerMeter(operationGetClientPartitionAttributesTimer)
        //getPDXTypeById
        registerMeter(operationGetPDXTypeByIdInProgressMeter)
        registerMeter(operationGetPDXTypeByIdCountMeter)
        registerMeter(operationGetPDXTypeByIdFailureCountMeter)
        registerMeter(operationGetPDXTypeByIdTimeoutCountMeter)
        registerMeter(operationGetPDXTypeByIdTimer)
        //getPDXIdForType
        registerMeter(operationGetPDXIdForTypeInProgressMeter)
        registerMeter(operationGetPDXIdForTypeCountMeter)
        registerMeter(operationGetPDXIdForTypeFailureCountMeter)
        registerMeter(operationGetPDXIdForTypeTimeoutCountMeter)
        registerMeter(operationGetPDXIdForTypeTimer)
        //addPdxType
        registerMeter(operationAddPdxTypeInProgressMeter)
        registerMeter(operationAddPdxTypeCountMeter)
        registerMeter(operationAddPdxTypeFailureCountMeter)
        registerMeter(operationAddPdxTypeTimeoutCountMeter)
        registerMeter(operationAddPdxTypeTimer)
        //Connections
        registerMeter(connectionsCurrentMeter)
        registerMeter(connectionsCreateMeter)
        registerMeter(connectionsDisconnectMeter)
        registerMeter(receivedBytesMeter)
        registerMeter(sentBytesMeter)
        registerMeter(messagesReceivedMeter)
        registerMeter(messagesReceivedBytesMeter)
    }
}