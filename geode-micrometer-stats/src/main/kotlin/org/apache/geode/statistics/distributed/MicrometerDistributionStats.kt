package org.apache.geode.statistics.distributed

import org.apache.geode.distributed.internal.*
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerDistributionStats(statisticsFactory: StatisticsFactory?,private val processID: String) :
        MicrometerMeterGroup(statisticsFactory,"DistributionStats-$processID"), DistributionStats {

    override fun getGroupTags(): Array<String> = arrayOf("processID", processID.toString())

    override fun initializeStaticMeters() {
        registerMeter(distributionMessageSentMeter)
        registerMeter(distributionMessageTransactionCommitMeter)
        registerMeter(distributionMessageTransactionCommitWaitMeter)
        registerMeter(distributionMessageTimer)
        registerMeter(distributionBroadcastMessageMeter)
        registerMeter(distributionBroadcastMessageTimer)
        registerMeter(distributionMessageReceivedMeter)
        registerMeter(distributionMessageReceivedBytesMeter)
        registerMeter(distributionMessageSentBytesMeter)
        registerMeter(distributionMessageProcessedMeter)
        registerMeter(distributionMessageTimeoutMeter)
        registerMeter(distributionMessageProcessedTimer)
        registerMeter(distributionMessageDispatchTimer)
        registerMeter(distributionQueueWaitingMeter)
        registerMeter(distributionQueueOverflowMeter)
        registerMeter(distributionQueueOverflowThrottleMeter)
        registerMeter(distributionQueueOverflowThrottleTimer)
        registerMeter(distributionQueueHighPriorityMeter)
        registerMeter(distributionQueueHighPriorityThrottleMeter)
        registerMeter(distributionQueueHighPriorityThrottleTimer)
        registerMeter(distributionQueuePartitionedMeter)
        registerMeter(distributionQueuePartitionedThrottleMeter)
        registerMeter(distributionQueuePartitionedThrottleTimer)
        registerMeter(distributionQueueFunctionMeter)
        registerMeter(distributionQueueFunctionThrottleMeter)
        registerMeter(distributionQueueFunctionThrottleTimer)
        registerMeter(distributionQueueSerialMeter)
        registerMeter(distributionQueueSerialThrottleMeter)
        registerMeter(distributionQueueSerialThrottleTimer)
        registerMeter(distributionQueueSerialBytesMeter)
        registerMeter(distributionQueueSerialPoolThreadsMeter)
        registerMeter(distributionThreadSerialMeter)
        registerMeter(distributionThreadNormalPriorityMeter)
        registerMeter(distributionThreadHighPriorityMeter)
        registerMeter(distributionThreadPartitionedMeter)
        registerMeter(distributionThreadFunctionMeter)
        registerMeter(distributionThreadWaitingMeter)
        registerMeter(distributionChannelReceivedTimer)
        registerMeter(distributionChannelUDPTimer)
        registerMeter(distributionChannelDispatchingTimer)
        registerMeter(distributionChannelDistributeTimer)
        registerMeter(distributionNodesMeter)
        registerMeter(distributionReplyThreadMeter)
        registerMeter(distributionReplyThreadCompletedMeter)
        registerMeter(distributionReplyThreadWaitingTimer)
        registerMeter(socketReceiverCountMeter)
        registerMeter(socketSenderSharedOrderedCountMeter)
        registerMeter(socketSenderSharedUnOrderedCountMeter)
        registerMeter(socketSenderThreadOrderedCountMeter)
        registerMeter(socketSenderThreadUnOrderedCountMeter)
        registerMeter(socketReceiverFailedMeter)
        registerMeter(socketSenderFailedMeter)
        registerMeter(socketReconnectMeter)
        registerMeter(socketSenderExpiredMeter)
        registerMeter(socketWritesInProgressMeter)
        registerMeter(socketWritesTimer)
        registerMeter(socketWritesCompletedMeter)
        registerMeter(socketWritesBytesMeter)
        registerMeter(socketUniCastReadMeter)
        registerMeter(socketUniCastReadBytesMeter)
        registerMeter(socketUniCastWriteMeter)
        registerMeter(socketUniCastWriteBytesMeter)
        registerMeter(socketUniCastRetransmitMeter)
        registerMeter(socketMultiCastReadMeter)
        registerMeter(socketMultiCastReadBytesMeter)
        registerMeter(socketMultiCastWriteMeter)
        registerMeter(socketMultiCastWriteBytesMeter)
        registerMeter(socketMultiCastRetransmitMeter)
        registerMeter(socketMultiCastRetransmitRequestsMeter)
        registerMeter(serializationTimer)
        registerMeter(serializationMeter)
        registerMeter(serializationBytesMeter)
        registerMeter(serializationPdxMeter)
        registerMeter(serializationPdxBytesMeter)
        registerMeter(deserializationTimer)
        registerMeter(deserializationMeter)
        registerMeter(deserializationBytesMeter)
        registerMeter(deserializationPdxMeter)
        registerMeter(deserializationPdxBytesMeter)
        registerMeter(messageSerializationTimer)
        registerMeter(messageDeserializationTimer)
        registerMeter(messageUDPEncryptionTimer)
        registerMeter(messageUDPDecryptionTimer)
        registerMeter(deserializationPdxObjectMeter)
        registerMeter(deserializationPdxObjectTimer)
        registerMeter(pdxObjectCreateMeter)
        registerMeter(batchSendTimer)
        registerMeter(batchWaitTimer)
        registerMeter(batchCopyTimer)
        registerMeter(batchFlushTimer)
        registerMeter(socketAsyncWriteInProgressMeter)
        registerMeter(socketAsyncWriteCompletedMeter)
        registerMeter(socketAsyncWriteRetriesMeter)
        registerMeter(socketAsyncWriteTimer)
        registerMeter(socketAsyncWriteBytesMeter)
        registerMeter(asyncQueueAddTimer)
        registerMeter(asyncQueueRemoveTimer)
        registerMeter(asyncQueueCountMeter)
        registerMeter(asyncQueueFlushesInProgressMeter)
        registerMeter(asyncQueueFlushedCompletedMeter)
        registerMeter(asyncQueueFlushesTimer)
        registerMeter(asyncQueueTimeOutExceededMeter)
        registerMeter(asyncQueueSizeExceededMeter)
        registerMeter(asyncQueueDistributionTimeOutExceededMeter)
        registerMeter(asyncQueueSizeBytesMeter)
        registerMeter(asyncQueueQueuedMessagesMeter)
        registerMeter(asyncQueueDequeuedMessagesMeter)
        registerMeter(asyncQueueConflatedMessagesMeter)
        registerMeter(asyncQueueThreadMeter)
        registerMeter(asyncQueueThreadInProgressMeter)
        registerMeter(asyncQueueThreadCompletedMeter)
        registerMeter(asyncQueueThreadTimer)
        registerMeter(receiverThreadsOwnedByNonReceiverMeter)
        registerMeter(receiverThreadsOwnedByReceiverMeter)
        registerMeter(receiverDirectBufferSizeMeter)
        registerMeter(receiverHeapBufferSizeMeter)
        registerMeter(senderDirectBufferSizeMeter)
        registerMeter(senderHeapBufferSizeMeter)
        registerMeter(socketLocksInProgressMeter)
        registerMeter(socketLocksMeter)
        registerMeter(socketLockTimer)
        registerMeter(bufferAcquiresInProgressMeter)
        registerMeter(bufferAcquiresMeter)
        registerMeter(bufferAcquireTimer)
        registerMeter(messageBeingReceivedMeter)
        registerMeter(messageBeingReceivedBytedMeter)
        registerMeter(serialThreadStartMeter)
        registerMeter(viewThreadStartMeter)
        registerMeter(processingThreadStartMeter)
        registerMeter(highPriorityThreadStartMeter)
        registerMeter(waitingThreadStartMeter)
        registerMeter(partitionedRegionThreadStartMeter)
        registerMeter(functionExecutionThreadStartMeter)
        registerMeter(serialPoolThreadStartMeter)
        registerMeter(threadOwnedMessagesSentMeter)
        registerMeter(replayHandOffTimer)
        registerMeter(partitionedRegionThreadJobsMeter)
        registerMeter(functionThreadJobsMeter)
        registerMeter(threadCountForViewMessageMeter)
        registerMeter(threadJobsForSerialThreadsMeter)
        registerMeter(threadJobsForViewThreadsMeter)
        registerMeter(threadJobsForSerialPoolThreadsMeter)
        registerMeter(threadJobsForProcessingThreadsMeter)
        registerMeter(threadJobsForHighPriorityThreadsMeter)
        registerMeter(threadJobsForWaitingThreadsMeter)
        registerMeter(elderCountMeter)
        registerMeter(initialImageMessageInFlightMeter)
        registerMeter(initialImageMessageInProgressMeter)

        // For GMSHealthMonitor
        registerMeter(heartBeatRequestSentMeter)
        registerMeter(heartBeatRequestReceivedMeter)
        registerMeter(heartBeatSendMeter)
        registerMeter(heartBeatReceivedMeter)
        registerMeter(suspectSentMeter)
        registerMeter(suspectReceivedMeter)
        registerMeter(finalCheckRequestSentMeter)
        registerMeter(finalCheckRequestReceivedMeter)
        registerMeter(finalCheckResponseSentMeter)
        registerMeter(finalCheckResponseReceivedMeter)
        registerMeter(tcpFinalCheckRequestSentMeter)
        registerMeter(tcpFinalCheckRequestReceivedMeter)
        registerMeter(tcpFinalCheckResponseSentMeter)
        registerMeter(tcpFinalCheckResponseReceivedMeter)
        registerMeter(udpFinalCheckRequestSentMeter)
//        registerMeter(udpFinalCheckRequestReceivedMeter)
//        registerMeter(udpFinalCheckResponseSentMeter)
        registerMeter(udpFinalCheckResponseReceivedMeter)
    }

    private val distributionMessageSentMeter = CounterStatisticMeter("distribution.messages", "The number of distribution messages that this GemFire system has sent. This includes broadcastMessages.", arrayOf("direction", "sent","messageType", "all","status", "valid"))
    private val distributionMessageTransactionCommitMeter = CounterStatisticMeter("distribution.message.created", "The number of transaction commit messages that this GemFire system has created to be sent. " + "Note that it is possible for a commit to only create one message even though it will end up being sent to multiple recipients.", arrayOf("type", "transaction-commit"))
    private val distributionMessageTransactionCommitWaitMeter = CounterStatisticMeter("distribution.message.transaction.wait.count", "The number of transaction commits that had to wait for a response before they could complete.")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionMessageTimer = CounterStatisticMeter("distribution.messages.time", "The total amount of time this distribution manager has spent sending messages. This includes broadcastMessagesTime.", arrayOf("direction", "sent","messageType", "broadcast","status", "valid"), meterUnit = "nanoseconds")
    private val distributionBroadcastMessageMeter = CounterStatisticMeter("distribution.messages", "The number of distribution messages that this GemFire system has broadcast. A broadcast message is one sent to every other manager in the group.", arrayOf("direction", "sent", "messageType", "broadcast","status", "valid"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionBroadcastMessageTimer = CounterStatisticMeter("distribution.messages.time", "The total amount of time this distribution manager has spent broadcasting messages. A broadcast message is one sent to every other manager in the group.", arrayOf("messageType", "broadcast", "direction", "sent","status", "valid"), meterUnit = "nanoseconds")
    private val distributionMessageReceivedMeter = CounterStatisticMeter("distribution.messages", "The number of distribution messages that this GemFire system has received.", arrayOf("direction", "received","messageType", "broadcast","status", "valid"))
    private val distributionMessageReceivedBytesMeter = CounterStatisticMeter("distribution.messages.bytes", "The number of distribution message bytes that this GemFire system has received.", arrayOf("direction", "received"), meterUnit = "bytes")
    private val distributionMessageSentBytesMeter = CounterStatisticMeter("distribution.messages.bytes", "The number of distribution message bytes that this GemFire system has sent.", arrayOf("direction", "sent"), meterUnit = "bytes")
    private val distributionMessageProcessedMeter = CounterStatisticMeter("distribution.messages", "The number of distribution messages that this GemFire system has processed.", arrayOf("direction", "received","status", "processed","messageType", "broadcast"))
    private val distributionMessageTimeoutMeter = CounterStatisticMeter("distribution.messages", "Total number of message replies that have timed out.", arrayOf("direction", "received","status", "timeout","messageType", "broadcast"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionMessageProcessedTimer = CounterStatisticMeter("distribution.messages.time", "The amount of time this distribution manager has spent in message.process().", arrayOf("direction", "received","status", "processed","messageType", "broadcast"), meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionMessageDispatchTimer = CounterStatisticMeter("distribution.messages.dispatch.time", "The amount of time this distribution manager has spent dispatching message to processor threads.", meterUnit = "nanoseconds")
    private val distributionQueueWaitingMeter = GaugeStatisticMeter("distribution.message.queued.waiting", "The number of distribution messages currently waiting for some other resource before they can be processed.")
    private val distributionQueueOverflowMeter = GaugeStatisticMeter("distribution.message.queued.overflow", "The number of normal distribution messages currently waiting to be processed.", arrayOf("priority", "normal","type","all"))
    private val distributionQueueOverflowThrottleMeter = GaugeStatisticMeter("distribution.message.queued.overflow.throttle", "The total number of times a thread was delayed in adding a normal message to the overflow queue.", arrayOf("priority", "normal","type","all"))
    private val distributionQueueOverflowThrottleTimer = TimerStatisticMeter("distribution.message.queued.overflow.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the overflow queue throttle.", arrayOf("priority", "normal","type","all"), meterUnit = "nanoseconds")
    private val distributionQueueHighPriorityMeter = GaugeStatisticMeter("distribution.message.queued.overflow", "The number of high priority distribution messages currently waiting to be processed.", arrayOf("priority", "high","type","all"))
    private val distributionQueueHighPriorityThrottleMeter = GaugeStatisticMeter("distribution.message.queued.overflow.throttle", "The total number of times a thread was delayed in adding a normal message to the high priority queue.", arrayOf("priority", "high","type","all"))
    private val distributionQueueHighPriorityThrottleTimer = TimerStatisticMeter("distribution.message.queued.overflow.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the high priority queue throttle.", arrayOf("priority", "high","type","all"), meterUnit = "nanoseconds")
    private val distributionQueuePartitionedMeter = GaugeStatisticMeter("distribution.message.queued.overflow", "The number of high priority distribution messages currently waiting to be processed.", arrayOf("priority", "high", "type", "partitioned"))
    private val distributionQueuePartitionedThrottleMeter = GaugeStatisticMeter("distribution.message.queued.overflow.throttle", "The total number of times a thread was delayed in adding a normal message to the high priority queue.", arrayOf("priority", "high", "type", "partitioned"))
    private val distributionQueuePartitionedThrottleTimer = TimerStatisticMeter("distribution.message.queued.overflow.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the high priority queue throttle.", arrayOf("priority", "high", "type", "partitioned"), meterUnit = "nanoseconds")
    private val distributionQueueFunctionMeter = GaugeStatisticMeter("distribution.message.queued.overflow", "The number of high priority distribution messages currently waiting to be processed.", arrayOf("priority", "high", "type", "function"))
    private val distributionQueueFunctionThrottleMeter = GaugeStatisticMeter("distribution.message.queued.overflow.throttle", "The total number of times a thread was delayed in adding a normal message to the high priority queue.", arrayOf("priority", "high", "type", "function"))
    private val distributionQueueFunctionThrottleTimer = TimerStatisticMeter("distribution.message.queued.overflow.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the high priority queue throttle.", arrayOf("priority", "high", "type", "function"), meterUnit = "nanoseconds")
    private val distributionQueueSerialMeter = GaugeStatisticMeter("distribution.message.queued.overflow", "The number of serial distribution messages currently waiting to be processed.", arrayOf("priority", "high", "type", "serial"))
    private val distributionQueueSerialThrottleMeter = GaugeStatisticMeter("distribution.message.queued.overflow.throttle", "The total number of times a thread was delayed in adding a ordered message to the serial queue.", arrayOf("priority", "high", "type", "serial"))
    private val distributionQueueSerialThrottleTimer = TimerStatisticMeter("distribution.message.queued.overflow.throttle.time", "The total amount of time, in nanoseconds, spent delayed by the serial queue throttle.", arrayOf("priority", "high", "type", "serial"), meterUnit = "nanoseconds")
    private val distributionQueueSerialBytesMeter = GaugeStatisticMeter("distribution.message.queued.overflow.bytes", "The approximate number of bytes consumed by serial distribution messages currently waiting to be processed.", arrayOf("priority", "high", "type", "serial"), meterUnit = "bytes")
    private val distributionQueueSerialPoolThreadsMeter = CounterStatisticMeter("distribution.message.threads", "The number of threads created in the SerialQueuedExecutorPool.")
    private val distributionThreadSerialMeter = GaugeStatisticMeter("distribution.message.threads", "The number of threads currently processing serial/ordered messages.", arrayOf("priority", "high", "type", "serial"))
    private val distributionThreadNormalPriorityMeter = GaugeStatisticMeter("distribution.message.threads", "The number of threads currently processing normal messages.", arrayOf("priority", "normal","type","all"))
    private val distributionThreadHighPriorityMeter = GaugeStatisticMeter("distribution.message.threads", "The number of threads currently processing high priority messages.", arrayOf("priority", "high","type","all"))
    private val distributionThreadPartitionedMeter = GaugeStatisticMeter("distribution.message.threads", "The number of threads currently processing partitioned region messages.", arrayOf("priority", "high", "type", "partitioned"))
    private val distributionThreadFunctionMeter = GaugeStatisticMeter("distribution.message.threads", "The number of threads currently processing function execution messages.", arrayOf("priority", "high", "type", "function"))
    private val distributionThreadWaitingMeter = GaugeStatisticMeter("distribution.message.threads.waiting", "The number of threads currently processing messages that had to wait for a resource.")
    private val distributionChannelReceivedTimer = TimerStatisticMeter("distribution.message.channel.time", "The total amount of time received messages spent in the distribution channel", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionChannelUDPTimer = CounterStatisticMeter("distribution.message.channel.udp.time", "The total amount of time spent deserializing and dispatching UDP messages in the message-reader thread.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionChannelDispatchingTimer = CounterStatisticMeter("distribution.message.reply.time", "The amount of time spent processing reply messages. This includes both processedMessagesTime and messageProcessingScheduleTime.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionChannelDistributeTimer = CounterStatisticMeter("distribution.message.distribute.time", "The amount of time it takes to prepare a message and send it on the network.  This includes sentMessagesTime.", meterUnit = "nanoseconds")
    private val distributionNodesMeter = GaugeStatisticMeter("distribution.nodes", "The current number of nodes in this distributed system.")
    private val distributionReplyThreadMeter = GaugeStatisticMeter("distribution.thread.reply", "Current number of threads waiting for a reply.", arrayOf("status", "waiting","type","all"))
    private val distributionReplyThreadCompletedMeter = CounterStatisticMeter("distribution.thread.reply", "Total number of times waits for a reply have completed.", arrayOf("status", "completed","type","all"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val distributionReplyThreadWaitingTimer = CounterStatisticMeter("distribution.thread.reply.time", "Total time spent waiting for a reply to a message.", arrayOf("status", "waiting","type","all"), meterUnit = "nanoseconds")
    private val socketReceiverCountMeter = GaugeStatisticMeter("distribution.sockets", "Current number of sockets dedicated to receiving messages.", arrayOf("direction", "receiving","ordered","all","type","all"))
    private val socketSenderSharedOrderedCountMeter = GaugeStatisticMeter("distribution.sockets", "Current number of shared sockets dedicated to sending ordered messages.", arrayOf("ordered", "true", "type", "shared", "direction", "sending"))
    private val socketSenderSharedUnOrderedCountMeter = GaugeStatisticMeter("distribution.sockets", "Current number of shared sockets dedicated to sending unordered messages.", arrayOf("ordered", "false", "type", "shared", "direction", "sending"))
    private val socketSenderThreadOrderedCountMeter = GaugeStatisticMeter("distribution.sockets", "Current number of thread sockets dedicated to sending ordered messages.", arrayOf("ordered", "true", "type", "thread", "direction", "sending"))
    private val socketSenderThreadUnOrderedCountMeter = GaugeStatisticMeter("distribution.sockets", "Current number of thread sockets dedicated to sending unordered messages.", arrayOf("ordered", "false", "type", "thread", "direction", "sending"))
    private val socketReceiverFailedMeter = CounterStatisticMeter("distribution.socket", "Total number of times an accept (receiver creation) of a connect from some other member has failed", arrayOf("type", "receiver", "status", "failed"))
    private val socketSenderFailedMeter = CounterStatisticMeter("distribution.socket", "Total number of times a connect (sender creation) to some other member has failed.", arrayOf("type", "sender", "status", "failed"))
    private val socketReconnectMeter = CounterStatisticMeter("distribution.socket", "Total number of times an established connection was lost and a reconnect was attempted.", arrayOf("status", "reconnect","type","all"))
    private val socketSenderExpiredMeter = CounterStatisticMeter("distribution.socket", "Total number of times an unshared sender socket has remained idle long enough that its lease expired.", arrayOf("type", "sender", "status", "expired"))
    private val socketWritesInProgressMeter = GaugeStatisticMeter("distribution.socket.writes.inprogress", "Current number of synchronous/blocking socket write calls in progress.", arrayOf("type", "sync"))
    private val socketWritesTimer = TimerStatisticMeter("distribution.socket.writes.time", "Total amount of time, in nanoseconds, spent in synchronous/blocking socket write calls.", arrayOf("type", "sync"), meterUnit = "nanoseconds")
    private val socketWritesCompletedMeter = CounterStatisticMeter("distribution.socket.writes.completed", "Total number of completed synchronous/blocking socket write calls.", arrayOf("type", "sync"))
    private val socketWritesBytesMeter = CounterStatisticMeter("distribution.socket.writes.bytes", "Total number of bytes sent out in synchronous/blocking mode on sockets.", arrayOf("type", "sync"), meterUnit = "bytes")
    private val socketUniCastReadMeter = CounterStatisticMeter("distribution.socket.unicast", "Total number of unicast datagrams received", arrayOf("type", "read"))
    private val socketUniCastReadBytesMeter = CounterStatisticMeter("distribution.socket.unicast.bytes", "Total number of bytes received in unicast datagrams", arrayOf("type", "read"), meterUnit = "bytes")
    private val socketUniCastWriteMeter = CounterStatisticMeter("distribution.socket.unicast", "Total number of unicast datagram socket write calls.", arrayOf("type", "write"))
    private val socketUniCastWriteBytesMeter = CounterStatisticMeter("distribution.socket.unicast.bytes", "Total number of bytes sent out on unicast datagram sockets.", arrayOf("type", "write"), meterUnit = "bytes")
    private val socketUniCastRetransmitMeter = CounterStatisticMeter("distribution.socket.unicast", "Total number of unicast datagram socket retransmissions", arrayOf("type", "retransmit"))
    private val socketMultiCastReadMeter = CounterStatisticMeter("distribution.socket.multicast", "Total number of multicast datagrams received", arrayOf("type", "read"))
    private val socketMultiCastReadBytesMeter = CounterStatisticMeter("distribution.socket.multicast.bytes", "Total number of bytes received in multicast datagrams", arrayOf("type", "read"))
    private val socketMultiCastWriteMeter = CounterStatisticMeter("distribution.socket.multicast", "Total number of multicast datagram socket write calls.", arrayOf("type", "write"))
    private val socketMultiCastWriteBytesMeter = CounterStatisticMeter("distribution.socket.multicast.bytes", "Total number of bytes sent out on multicast datagram sockets.", arrayOf("type", "write"))
    private val socketMultiCastRetransmitMeter = CounterStatisticMeter("distribution.socket.multicast", "Total number of multicast datagram socket retransmissions", arrayOf("type", "retransmit"))
    private val socketMultiCastRetransmitRequestsMeter = CounterStatisticMeter("distribution.socket.multicast", "Total number of multicast datagram socket retransmission requests sent to other processes", arrayOf("type", "retransmit-request"))
    private val serializationTimer = TimerStatisticMeter("serialization.time", "Total amount of time, in nanoseconds, spent serializing objects. This includes pdx serializations.", meterUnit = "nanoseconds")
    private val serializationMeter = CounterStatisticMeter("serialization.count", "Total number of object serialization calls. This includes pdx serializations.",arrayOf("type", "all"))
    private val serializationBytesMeter = CounterStatisticMeter("serialization.byte", "Total number of bytes produced by object serialization. This includes pdx serializations.",arrayOf("type", "all"), meterUnit = "bytes")
    private val serializationPdxMeter = CounterStatisticMeter("serialization.count", "Total number of pdx serializations.", arrayOf("type", "pdx"))
    private val serializationPdxBytesMeter = CounterStatisticMeter("serialization.bytes", "Total number of bytes produced by pdx serialization.", arrayOf("type", "pdx"), meterUnit = "bytes")
    private val deserializationTimer = TimerStatisticMeter("deserialization.time", "Total amount of time, in nanoseconds, spent deserializing objects. This includes deserialization that results in a PdxInstance.", meterUnit = "nanoseconds")
    private val deserializationMeter = CounterStatisticMeter("deserialization.count", "Total number of object deserialization calls. This includes deserialization that results in a PdxInstance.",arrayOf("type", "all"))
    private val deserializationBytesMeter = CounterStatisticMeter("deserialization.bytes", "Total number of bytes read by object deserialization. This includes deserialization that results in a PdxInstance.",arrayOf("type", "all"), meterUnit = "bytes")
    private val deserializationPdxMeter = CounterStatisticMeter("deserialization.count", "Total number of pdx deserializations.", arrayOf("type", "pdx"))
    private val deserializationPdxBytesMeter = CounterStatisticMeter("deserialization.bytes", "Total number of bytes read by pdx deserialization.", arrayOf("type", "pdx"), meterUnit = "bytes")
    private val messageSerializationTimer = TimerStatisticMeter("serialization.message.timer", "Total amount of time, in nanoseconds, spent serializing messages.", meterUnit = "nanoseconds")
    private val messageDeserializationTimer = TimerStatisticMeter("deserialization.message.time", "Total amount of time, in nanoseconds, spent deserializing messages.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val messageUDPEncryptionTimer = CounterStatisticMeter("udp.message.encryption.time", "Total amount of time, in nanoseconds, spent encrypting udp messages.", meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val messageUDPDecryptionTimer = CounterStatisticMeter("udp.message.deencryption.time", "Total amount of time, in nanoseconds, spent decrypting udp messages.", meterUnit = "nanoseconds")
    private val deserializationPdxObjectMeter = CounterStatisticMeter("pdx.deserialization.object.count", "Total number of times getObject has been called on a PdxInstance.")
    private val deserializationPdxObjectTimer = TimerStatisticMeter("pdx.deserialization.object.time", "Total amount of time, in nanoseconds, spent deserializing PdxInstances by calling getObject.", meterUnit = "nanoseconds")
    private val pdxObjectCreateMeter = CounterStatisticMeter("pdx.deserialization.object.create", "Total number of times a deserialization created a PdxInstance.")
    private val batchSendTimer = TimerStatisticMeter("batch.send.time", "Total amount of time, in nanoseconds, spent queueing and flushing message batches", meterUnit = "nanoseconds")
    private val batchWaitTimer = TimerStatisticMeter("batch.wait.time", "Reserved for future use", arrayOf("type", "wait"), meterUnit = "nanoseconds")
    private val batchCopyTimer = TimerStatisticMeter("batch.copy.time", "Total amount of time, in nanoseconds, spent copying messages for batched transmission", arrayOf("type", "copy"), meterUnit = "nanoseconds")
    private val batchFlushTimer = TimerStatisticMeter("batch.flush.time", "Total amount of time, in nanoseconds, spent flushing batched messages to the network", arrayOf("type", "flush"), meterUnit = "nanoseconds")
    private val socketAsyncWriteInProgressMeter = GaugeStatisticMeter("distribution.socket.async.inprogress", "Current number of non-blocking socket write calls in progress.", arrayOf("type", "async"))
    private val socketAsyncWriteCompletedMeter = CounterStatisticMeter("distribution.socket.async.completed", "Total number of non-blocking socket write calls completed.", arrayOf("type", "async"))
    private val socketAsyncWriteRetriesMeter = CounterStatisticMeter("distribution.socket.async.retries", "Total number of retries needed to write a single block of data using non-blocking socket write calls.", arrayOf("type", "async"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val socketAsyncWriteTimer = CounterStatisticMeter("distribution.socket.async.time", "Total amount of time, in nanoseconds, spent in non-blocking socket write calls.", arrayOf("type", "async"), meterUnit = "nanoseconds")
    private val socketAsyncWriteBytesMeter = CounterStatisticMeter("distribution.socket.async.bytes", "Total number of bytes sent out on non-blocking sockets.", arrayOf("type", "async"), meterUnit = "bytes")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val asyncQueueAddTimer = CounterStatisticMeter("distribution.queue.async.time", "Total amount of time, in nanoseconds, spent in adding messages to async queue.", arrayOf("type", "async", "operation", "add"), meterUnit = "nanoseconds")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val asyncQueueRemoveTimer = CounterStatisticMeter("distribution.queue.async.time", "Total amount of time, in nanoseconds, spent in removing messages from async queue.", arrayOf("type", "async", "operation", "remove"), meterUnit = "nanoseconds")
    private val asyncQueueCountMeter = GaugeStatisticMeter("distribution.queue.async.count", "The current number of queues for asynchronous messaging.")
    private val asyncQueueFlushesInProgressMeter = GaugeStatisticMeter("distribution.queue.async.flush.inprogress", "Current number of asynchronous queues being flushed.", arrayOf("type", "async"))
    private val asyncQueueFlushedCompletedMeter = CounterStatisticMeter("distribution.queue.async.flush.completed", "Total number of asynchronous queue flushes completed.", arrayOf("type", "async"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val asyncQueueFlushesTimer = CounterStatisticMeter("distribution.queue.async.flush.time", "Total time spent flushing asynchronous queues.", arrayOf("type", "async"), meterUnit = "nanoseconds")
    private val asyncQueueTimeOutExceededMeter = CounterStatisticMeter("distribution.queue.async.timeout.exceeded", "Total number of asynchronous queues that have timed out by being blocked for more than async-queue-timeout milliseconds.", arrayOf("type", "async"))
    private val asyncQueueSizeExceededMeter = CounterStatisticMeter("distribution.queue.async.size.exceeded", "Total number of asynchronous queues that have exceeded max size.", arrayOf("type", "async"))
    private val asyncQueueDistributionTimeOutExceededMeter = CounterStatisticMeter("distribution.queue.async.distribution.timeout.exceeded", "Total number of times the async-distribution-timeout has been exceeded during a socket write.", arrayOf("type", "async"))
    private val asyncQueueSizeBytesMeter = GaugeStatisticMeter("distribution.queue.async.bytes", "The current size in bytes used for asynchronous queues.", arrayOf("type", "async"), meterUnit = "bytes")
    private val asyncQueueQueuedMessagesMeter = CounterStatisticMeter("distribution.queue.async.queued", "The total number of queued messages used for asynchronous queues.", arrayOf("type", "async"))
    private val asyncQueueDequeuedMessagesMeter = CounterStatisticMeter("distribution.queue.async.dequeued", "The total number of queued messages that have been removed from the queue and successfully sent.", arrayOf("type", "async"))
    private val asyncQueueConflatedMessagesMeter = CounterStatisticMeter("distribution.queue.async.conflated", "The total number of queued conflated messages used for asynchronous queues.", arrayOf("type", "async"))
    private val asyncQueueThreadMeter = GaugeStatisticMeter("distribution.queue.async.thread.max", "Total number of asynchronous message queue threads.", arrayOf("type", "async"))
    private val asyncQueueThreadInProgressMeter = GaugeStatisticMeter("distribution.queue.async.thread.inprogress", "Current iterations of work performed by asynchronous message queue threads.", arrayOf("type", "async"))
    private val asyncQueueThreadCompletedMeter = CounterStatisticMeter("distribution.queue.async.thread.completed", "Total number of iterations of work performed by asynchronous message queue threads.", arrayOf("type", "async"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val asyncQueueThreadTimer = CounterStatisticMeter("distribution.queue.async.thread.time", "Total time spent by asynchronous message queue threads performing iterations.", arrayOf("type", "async"), meterUnit = "nanoseconds")
    private val receiverThreadsOwnedByNonReceiverMeter = GaugeStatisticMeter("receivers.tread.owned.nonthread", "Number of receiver threads owned by non-receiver threads in other members.")
    private val receiverThreadsOwnedByReceiverMeter = GaugeStatisticMeter("receivers.tread.owned.thread", "Number of receiver threads owned in turn by receiver threads in other members")
    private val receiverDirectBufferSizeMeter = GaugeStatisticMeter("receiver.buffer.direct.size", "Current number of bytes allocated from direct memory as buffers for incoming messages.", meterUnit = "bytes")
    private val receiverHeapBufferSizeMeter = GaugeStatisticMeter("receiver.buffer.heap.size", "Current number of bytes allocated from Java heap memory as buffers for incoming messages.", meterUnit = "bytes")
    private val senderDirectBufferSizeMeter = GaugeStatisticMeter("sender.buffer.direct.size", "Current number of bytes allocated from direct memory as buffers for outgoing messages.", meterUnit = "bytes")
    private val senderHeapBufferSizeMeter = GaugeStatisticMeter("sender.buffer.heap.size", "Current number of bytes allocated from Java heap memory as buffers for outoing messages.", meterUnit = "bytes")
    private val socketLocksInProgressMeter = GaugeStatisticMeter("socket.locks.inprogress", "Current number of threads waiting to lock a socket")
    private val socketLocksMeter = CounterStatisticMeter("socket.locks", "Total number of times a socket has been locked.")
    private val socketLockTimer = TimerStatisticMeter("socket.lock.time", "Total amount of time, in nanoseconds, spent locking a socket", meterUnit = "nanoseconds")
    private val bufferAcquiresInProgressMeter = GaugeStatisticMeter("buffer.acquires.inprogress", "Current number of threads waiting to acquire a buffer")
    private val bufferAcquiresMeter = CounterStatisticMeter("buffer.acquires", "Total number of times a buffer has been acquired.")
    private val bufferAcquireTimer = TimerStatisticMeter("buffer.acquire.time", "Total amount of time, in nanoseconds, spent acquiring a socket", meterUnit = "nanoseconds")
    private val messageBeingReceivedMeter = GaugeStatisticMeter("messages.being.received", "Current number of message being received off the network or being processed after reception.")
    private val messageBeingReceivedBytedMeter = GaugeStatisticMeter("message.being.received.bytes", "Current number of bytes consumed by messages being received or processed.", meterUnit = "bytes")
    private val serialThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the serial message executor.", arrayOf("type", "serial"))
    private val viewThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the view message executor.", arrayOf("type", "view"))
    private val processingThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the pool processing normal messages.", arrayOf("type", "processing"))
    private val highPriorityThreadStartMeter = CounterStatisticMeter("thread.startshighPriorityThreadStarts", "Total number of times a thread has been created for the pool handling high priority messages.", arrayOf("type", "highPriority"))
    private val waitingThreadStartMeter = CounterStatisticMeter("thread.startswaitingThreadStarts", "Total number of times a thread has been created for the waiting pool.", arrayOf("type", "waiting"))
    private val partitionedRegionThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the pool handling partitioned region messages.", arrayOf("type", "partitionedRegion"))
    private val functionExecutionThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the pool handling function execution messages.", arrayOf("type", "functions"))
    private val serialPoolThreadStartMeter = CounterStatisticMeter("thread.starts", "Total number of times a thread has been created for the serial pool(s).", arrayOf("type", "serialPool"))
    private val threadOwnedMessagesSentMeter = CounterStatisticMeter("thread.owned.messages.sent", "Total number of messages sent on thread owned senders")
    private val replayHandOffTimer = TimerStatisticMeter("reply.handoff.time", "Total number of seconds to switch thread contexts from processing thread to application thread.", meterUnit = "nanoseconds")
    private val partitionedRegionThreadJobsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by partitioned region threads", arrayOf("type", "partitionedRegion"))
    private val functionThreadJobsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by function execution threads", arrayOf("type", "functions"))
    private val threadCountForViewMessageMeter = GaugeStatisticMeter("thread.count", "The number of threads currently processing view messages.", arrayOf("type", "view"))
    private val threadJobsForSerialThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by serial threads.", arrayOf("type", "serial"))
    private val threadJobsForViewThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by view threads.", arrayOf("type", "view"))
    private val threadJobsForSerialPoolThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by pooled serial processor threads.", arrayOf("type", "serialPool"))
    private val threadJobsForProcessingThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by pooled message processor threads.", arrayOf("type", "processing"))
    private val threadJobsForHighPriorityThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by high priority processor threads.", arrayOf("type", "highPriority"))
    private val threadJobsForWaitingThreadsMeter = GaugeStatisticMeter("thread.jobs", "The number of messages currently being processed by waiting pooly processor threads.", arrayOf("type", "waiting"))
    private val elderCountMeter = GaugeStatisticMeter("elder.count", "Current number of system elders hosted in this member.")
    private val initialImageMessageInFlightMeter = GaugeStatisticMeter("initial.image.messages.inflight", "The number of messages with initial image data sent from this member that have not yet been acknowledged.")
    private val initialImageMessageInProgressMeter = GaugeStatisticMeter("initial.image.messages.inprogress", "The number of initial images this member is currently receiving.")

    // For GMSHealthMonitor
    private val heartBeatRequestSentMeter = CounterStatisticMeter("messages.sent", "Heartbeat request messages that this member has sent.", arrayOf("type", "heartbeatRequest", "transport", "all"))
    private val heartBeatRequestReceivedMeter = CounterStatisticMeter("messages.received", "Heartbeat request messages that this member has received.", arrayOf("type", "heartbeatRequest", "transport", "all"))
    private val heartBeatSendMeter = CounterStatisticMeter("messages.sent", "Heartbeat messages that this member has sent.", arrayOf("type", "heartbeat", "transport", "all"))
    private val heartBeatReceivedMeter = CounterStatisticMeter("messages.received", "Heartbeat messages that this member has received.", arrayOf("type", "heartbeat", "transport", "all"))
    private val suspectSentMeter = CounterStatisticMeter("messages.sent", "Suspect member messages that this member has sent.", arrayOf("type", "suspect", "transport", "all"))
    private val suspectReceivedMeter = CounterStatisticMeter("messages.received", "Suspect member messages that this member has received.", arrayOf("type", "suspect", "transport", "all"))
    private val finalCheckRequestSentMeter = CounterStatisticMeter("messages.sent", "final check requests that this member has sent.", arrayOf("type", "finalCheckRequest", "transport", "all"))
    private val finalCheckRequestReceivedMeter = CounterStatisticMeter("messages.received", "final check requests that this member has received.", arrayOf("type", "finalCheckRequest", "transport", "all"))
    private val finalCheckResponseSentMeter = CounterStatisticMeter("messages.sent", "final check responses that this member has sent.", arrayOf("type", "finalCheckResponse", "transport", "all"))
    private val finalCheckResponseReceivedMeter = CounterStatisticMeter("messages.received", "final check responses that this member has received.", arrayOf("type", "finalCheckResponse", "transport", "all"))
    private val tcpFinalCheckRequestSentMeter = CounterStatisticMeter("messages.sent", "TCP final check requests that this member has sent.", arrayOf("type", "finalCheckRequest", "transport", "tcp"))
    private val tcpFinalCheckRequestReceivedMeter = CounterStatisticMeter("messages.received", "TCP final check requests that this member has received.", arrayOf("type", "finalCheckRequest", "transport", "tcp"))
    private val tcpFinalCheckResponseSentMeter = CounterStatisticMeter("messages.sent", "TCP final check responses that this member has sent.", arrayOf("type", "finalCheckResponse", "transport", "tcp"))
    private val tcpFinalCheckResponseReceivedMeter = CounterStatisticMeter("messages.received", "TCP final check responses that this member has received.", arrayOf("type", "finalCheckResponse", "transport", "tcp"))
    private val udpFinalCheckRequestSentMeter = CounterStatisticMeter("messages.sent", "UDP final check requests that this member has sent.", arrayOf("type", "finalCheckRequest", "transport", "udp"))
    //    private val udpFinalCheckRequestReceivedMeter = CounterStatisticMeter("messages.received", "UDP final check requests that this member has received.", arrayOf("type", "finalCheckRequest", "transport", "udp"))
//    private val udpFinalCheckResponseSentMeter = CounterStatisticMeter("messages.sent", "UDP final check responses that this member has sent.", arrayOf("type", "finalCheckResponse", "transport", "udp"))
    private val udpFinalCheckResponseReceivedMeter = CounterStatisticMeter("messages.received", "UDP final check responses that this member has received.", arrayOf("type", "finalCheckResponse", "transport", "udp"))


    override fun incAsyncQueueTimeouts(inc: Int) {
        asyncQueueTimeOutExceededMeter.increment(inc)
    }

    override fun incAsyncQueueSizeExceeded(inc: Int) {
        asyncQueueSizeExceededMeter.increment(inc)
    }

    override fun incAsyncDistributionTimeoutExceeded() {
        asyncQueueDistributionTimeOutExceededMeter.increment()
    }

    override fun incTOSentMsg() {
        threadOwnedMessagesSentMeter.increment()
    }

    override fun incSentMessages(messages: Long) {
        distributionMessageSentMeter.increment(messages)
    }

    override fun incSentCommitMessages(messages: Long) {
        distributionMessageTransactionCommitMeter.increment(messages)
    }

    override fun incCommitWaits() {
        distributionMessageTransactionCommitWaitMeter.increment()
    }

    override fun incSentMessagesTime(nanos: Long) {
        distributionMessageTimer.increment(NOW_NANOS - nanos)
    }

    override fun incBroadcastMessages(messages: Long) {
        distributionBroadcastMessageMeter.increment(messages)
    }

    override fun incBroadcastMessagesTime(nanos: Long) {
        distributionBroadcastMessageTimer.increment(nanos)
    }

    override fun incReceivedMessages(messages: Long) {
        distributionMessageReceivedMeter.increment(messages)
    }

    override fun incReceivedBytes(bytes: Long) {
        distributionMessageReceivedBytesMeter.increment(bytes)
    }

    override fun incSentBytes(bytes: Long) {
        distributionMessageSentBytesMeter.increment(bytes)
    }

    override fun incProcessedMessages(messages: Long) {
        distributionMessageProcessedMeter.increment(messages)
    }

    override fun incProcessedMessagesTime(nanos: Long) {
        distributionMessageProcessedTimer.increment(NOW_NANOS - nanos)
    }

    override fun incMessageProcessingScheduleTime(nanos: Long) {
        distributionMessageDispatchTimer.increment(nanos)
    }

    override fun incOverflowQueueSize(messages: Int) {
        distributionQueueOverflowMeter.increment(messages)
    }

    override fun incWaitingQueueSize(messages: Int) {
        distributionQueueWaitingMeter.increment(messages)
    }

    override fun incOverflowQueueThrottleCount(delays: Int) {
        distributionQueueOverflowThrottleMeter.increment(delays)
    }

    override fun incOverflowQueueThrottleTime(nanos: Long) {
        distributionQueueOverflowThrottleTimer.recordValue(nanos)
    }

    override fun incHighPriorityQueueSize(messages: Int) {
        distributionQueueHighPriorityMeter.increment(messages)
    }

    override fun incHighPriorityQueueThrottleCount(delays: Int) {
        distributionQueueHighPriorityThrottleMeter.increment(delays)
    }

    override fun incHighPriorityQueueThrottleTime(nanos: Long) {
        distributionQueueHighPriorityThrottleTimer.recordValue(nanos)
    }

    override fun incPartitionedRegionQueueSize(messages: Int) {
        distributionQueuePartitionedMeter.increment(messages)
    }

    override fun incPartitionedRegionQueueThrottleCount(delays: Int) {
        distributionQueuePartitionedThrottleMeter.increment(delays)
    }

    override fun incPartitionedRegionQueueThrottleTime(nanos: Long) {
        distributionQueuePartitionedThrottleTimer.recordValue(nanos)
    }

    override fun incFunctionExecutionQueueSize(messages: Int) {
        distributionQueueFunctionMeter.increment(messages)
    }

    override fun incFunctionExecutionQueueThrottleCount(delays: Int) {
        distributionQueueFunctionThrottleMeter.increment(delays)
    }

    override fun incFunctionExecutionQueueThrottleTime(nanos: Long) {
        distributionQueueFunctionThrottleTimer.recordValue(nanos)
    }

    override fun incSerialQueueSize(messages: Int) {
        distributionQueueSerialMeter.increment(messages)
    }

    override fun incSerialQueueBytes(amount: Int) {
        distributionQueueSerialBytesMeter.increment(amount)
    }

    override fun incSerialPooledThread() {
        distributionQueueSerialPoolThreadsMeter.increment()
    }

    override fun incSerialQueueThrottleCount(delays: Int) {
        distributionQueueSerialThrottleMeter.increment(delays)
    }

    override fun incSerialQueueThrottleTime(nanos: Long) {
        distributionQueueSerialThrottleTimer.recordValue(nanos)
    }

    override fun incNumProcessingThreads(threads: Int) {
        distributionThreadNormalPriorityMeter.increment(threads)
    }

    override fun incNumSerialThreads(threads: Int) {
        distributionThreadSerialMeter.increment(threads)
    }

    override fun incWaitingThreads(threads: Int) {
        distributionThreadWaitingMeter.increment(threads)
    }

    override fun incHighPriorityThreads(threads: Int) {
        distributionThreadHighPriorityMeter.increment(threads)
    }

    override fun incPartitionedRegionThreads(threads: Int) {
        distributionThreadPartitionedMeter.increment(threads)
    }

    override fun incFunctionExecutionThreads(threads: Int) {
        distributionThreadFunctionMeter.increment(threads)
    }

    override fun incMessageChannelTime(delta: Long) {
        distributionChannelReceivedTimer.recordValue(delta)
    }

    override fun incUDPDispatchRequestTime(delta: Long) {
        distributionChannelDispatchingTimer.increment(delta)
    }

    override fun incReplyMessageTime(nanos: Long) {
        distributionChannelUDPTimer.increment(nanos)
    }

    override fun incDistributeMessageTime(nanos: Long) {
        distributionChannelDistributeTimer.increment(nanos)
    }

    override fun setNodes(nodeCount: Int) {
        distributionNodesMeter.setValue(nodeCount)
    }

    override fun incNodes(nodeCount: Int) {
        distributionNodesMeter.increment(nodeCount)
    }

    override fun startSocketWrite(sync: Boolean): Long {
        if (sync) {
            socketWritesInProgressMeter.increment()
        } else {
            socketAsyncWriteInProgressMeter.increment()
        }
        return NOW_NANOS
    }

    override fun endSocketWrite(sync: Boolean, start: Long, bytesWritten: Int, retries: Int) {
        if (sync) {
            socketWritesInProgressMeter.decrement()
            socketWritesCompletedMeter.increment()
            socketWritesBytesMeter.increment(bytesWritten)
            socketWritesTimer.recordValue(NOW_NANOS - start)
        } else {
            socketAsyncWriteInProgressMeter.decrement()
            socketAsyncWriteCompletedMeter.increment()
            socketAsyncWriteRetriesMeter.increment(retries)
            socketAsyncWriteBytesMeter.increment(bytesWritten)
            socketAsyncWriteTimer.increment(NOW_NANOS - start)
        }
    }

    override fun startSocketLock(): Long {
        socketLocksInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endSocketLock(start: Long) {
        socketLockTimer.recordValue(NOW_NANOS - start)
        socketLocksInProgressMeter.decrement()
        socketLocksMeter.increment()
    }

    override fun startBufferAcquire(): Long {
        bufferAcquiresInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endBufferAcquire(start: Long) {
        bufferAcquireTimer.recordValue(NOW_NANOS - start)
        bufferAcquiresInProgressMeter.decrement()
        bufferAcquiresMeter.increment()
    }

    override fun incUcastWriteBytes(bytesWritten: Int) {
        socketUniCastWriteMeter.increment()
        socketUniCastWriteBytesMeter.increment(bytesWritten)
    }

    override fun incMcastWriteBytes(bytesWritten: Int) {
        socketMultiCastWriteMeter.increment()
        socketMultiCastWriteBytesMeter.increment(bytesWritten)
    }

    override fun incMcastReadBytes(amount: Int) {
        socketMultiCastReadMeter.increment()
        socketMultiCastReadBytesMeter.increment(amount)
    }

    override fun incUcastReadBytes(amount: Int) {
        socketUniCastReadMeter.increment()
        socketUniCastReadBytesMeter.increment(amount)
    }

    override fun startSerialization(): Long = NOW_NANOS

    override fun endSerialization(start: Long, bytes: Int) {
        serializationTimer.recordValue(NOW_NANOS - start)
        serializationMeter.increment()
        serializationBytesMeter.increment(bytes)
    }

    override fun startPdxInstanceDeserialization(): Long = NOW_NANOS

    override fun endPdxInstanceDeserialization(start: Long) {
        deserializationPdxObjectTimer.recordValue(NOW_NANOS - start)
        deserializationPdxObjectMeter.increment()
    }

    override fun incPdxSerialization(bytes: Int) {
        serializationPdxMeter.increment()
        serializationPdxBytesMeter.increment(bytes)
    }

    override fun incPdxDeserialization(bytes: Int) {
        deserializationPdxMeter.increment()
        deserializationPdxBytesMeter.increment(bytes)
    }

    override fun incPdxInstanceCreations() {
        pdxObjectCreateMeter.increment()
    }

    override fun startDeserialization(): Long = NOW_NANOS

    override fun endDeserialization(start: Long, bytes: Int) {
        deserializationTimer.recordValue(NOW_NANOS - start)
        deserializationMeter.increment()
        deserializationBytesMeter.increment(bytes)
    }

    override fun startMsgSerialization(): Long = NOW_NANOS

    override fun endMsgSerialization(start: Long) {
        messageSerializationTimer.recordValue(NOW_NANOS - start)
    }

    override fun startUDPMsgEncryption(): Long = NOW_NANOS

    override fun endUDPMsgEncryption(start: Long) {
        messageUDPEncryptionTimer.increment(NOW_NANOS - start)
    }

    override fun startMsgDeserialization(): Long = NOW_NANOS

    override fun endMsgDeserialization(start: Long) {
        messageDeserializationTimer.recordValue(NOW_NANOS - start)
    }

    override fun startUDPMsgDecryption(): Long = NOW_NANOS

    override fun endUDPMsgDecryption(start: Long) {
        messageUDPDecryptionTimer.increment(NOW_NANOS - start)
    }

    override fun startReplyWait(): Long {
        distributionReplyThreadMeter.increment()
        return NOW_NANOS
    }

    override fun endReplyWait(startNanos: Long, initTime: Long) {
        distributionReplyThreadWaitingTimer.increment(NOW_NANOS - startNanos)
        distributionReplyThreadMeter.decrement()
        distributionReplyThreadCompletedMeter.increment()
    }

    override fun incReplyTimeouts() {
        distributionMessageTimeoutMeter.increment()
    }

    override fun incReceivers() {
        socketReceiverCountMeter.increment()
    }

    override fun decReceivers() {
        socketReceiverCountMeter.decrement()
    }

    override fun incFailedAccept() {
        socketReceiverFailedMeter.increment()
    }

    override fun incFailedConnect() {
        socketSenderFailedMeter.increment()
    }

    override fun incReconnectAttempts() {
        socketReconnectMeter.increment()
    }

    override fun incLostLease() {
        socketSenderExpiredMeter.increment()
    }

    override fun incSenders(shared: Boolean, preserveOrder: Boolean) {
        if (shared) {
            if (preserveOrder) {
                socketSenderSharedOrderedCountMeter.increment()
            } else {
                socketSenderSharedUnOrderedCountMeter.increment()
            }
        } else {
            if (preserveOrder) {
                socketSenderThreadOrderedCountMeter.increment()
            } else {
                socketSenderThreadUnOrderedCountMeter.increment()
            }
        }
    }

    override fun decSenders(shared: Boolean, preserveOrder: Boolean) {
        if (shared) {
            if (preserveOrder) {
                socketSenderSharedOrderedCountMeter.decrement()
            } else {
                socketSenderSharedUnOrderedCountMeter.decrement()
            }
        } else {
            if (preserveOrder) {
                socketSenderThreadOrderedCountMeter.decrement()
            } else {
                socketSenderThreadUnOrderedCountMeter.decrement()
            }
        }
    }

    override fun incAsyncQueueAddTime(inc: Long) {
        asyncQueueAddTimer.increment(inc)
    }

    override fun incAsyncQueueRemoveTime(inc: Long) {
        asyncQueueRemoveTimer.increment(inc)
    }

    override fun incAsyncQueues(inc: Int) {
        asyncQueueCountMeter.increment(inc)
    }

    override fun startAsyncQueueFlush(): Long {
        asyncQueueFlushesInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endAsyncQueueFlush(start: Long) {
        asyncQueueFlushesInProgressMeter.decrement()
        asyncQueueFlushedCompletedMeter.increment()
        asyncQueueFlushesTimer.increment(NOW_NANOS - start)
    }

    override fun incAsyncQueueSize(bytes: Long) {
        asyncQueueSizeBytesMeter.increment(bytes)
    }

    override fun incAsyncQueuedMsgs() {
        asyncQueueQueuedMessagesMeter.increment()
    }

    override fun incAsyncDequeuedMsgs() {
        asyncQueueDequeuedMessagesMeter.increment()
    }

    override fun incAsyncConflatedMsgs() {
        asyncQueueConflatedMessagesMeter.increment()
    }

    override fun incAsyncThreads(inc: Int) {
        asyncQueueThreadMeter.increment(inc)
    }

    override fun startAsyncThread(): Long {
        asyncQueueThreadInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endAsyncThread(start: Long) {
        asyncQueueThreadInProgressMeter.decrement()
        asyncQueueThreadCompletedMeter.increment()
        asyncQueueThreadTimer.increment(NOW_NANOS - start)
    }

    override fun incBatchSendTime(start: Long) {
        batchSendTimer.recordValue(NOW_NANOS - start)
    }

    override fun incBatchCopyTime(start: Long) {
        batchCopyTimer.recordValue(NOW_NANOS - start)
    }

    override fun incBatchWaitTime(start: Long) {
        batchWaitTimer.recordValue(NOW_NANOS - start)
    }

    override fun incBatchFlushTime(start: Long) {
        batchFlushTimer.recordValue(NOW_NANOS - start)
    }

    override fun incUcastRetransmits() {
        socketUniCastRetransmitMeter.increment()
    }

    override fun incMcastRetransmits() {
        socketMultiCastRetransmitMeter.increment()
    }

    override fun incMcastRetransmitRequests() {
        socketMultiCastRetransmitRequestsMeter.increment()
    }

    override fun incThreadOwnedReceivers(value: Long, dominoCount: Int) {
        if (dominoCount < 2) {
            receiverThreadsOwnedByNonReceiverMeter.increment(value)
        } else {
            receiverThreadsOwnedByReceiverMeter.increment(value)
        }
    }

    override fun incReceiverBufferSize(inc: Int, direct: Boolean) {
        if (direct) {
            receiverDirectBufferSizeMeter.increment(inc)
        } else {
            receiverHeapBufferSizeMeter.increment(inc)
        }
    }

    override fun incSenderBufferSize(inc: Int, direct: Boolean) {
        if (direct) {
            senderDirectBufferSizeMeter.increment(inc)
        } else {
            senderHeapBufferSizeMeter.increment(inc)
        }
    }

    override fun incMessagesBeingReceived(newMsg: Boolean, bytes: Int) {
        if (newMsg) {
            messageBeingReceivedMeter.increment()
        }
        messageBeingReceivedBytedMeter.increment(bytes)
    }

    override fun decMessagesBeingReceived(bytes: Int) {
        messageBeingReceivedMeter.decrement()
        messageBeingReceivedBytedMeter.decrement(bytes)
    }

    override fun incSerialThreadStarts() {
        serialThreadStartMeter.increment()
    }

    override fun incViewThreadStarts() {
        viewThreadStartMeter.increment()
    }

    override fun incProcessingThreadStarts() {
        processingThreadStartMeter.increment()
    }

    override fun incHighPriorityThreadStarts() {
        highPriorityThreadStartMeter.increment()
    }

    override fun incWaitingThreadStarts() {
        waitingThreadStartMeter.increment()
    }

    override fun incPartitionedRegionThreadStarts() {
        partitionedRegionThreadStartMeter.increment()
    }

    override fun incFunctionExecutionThreadStarts() {
        functionExecutionThreadStartMeter.increment()
    }

    override fun incSerialPooledThreadStarts() {
        serialPoolThreadStartMeter.increment()
    }

    override fun incReplyHandOffTime(start: Long) {
        replayHandOffTimer.recordValue(NOW_NANOS - start)
    }

    override fun incPartitionedRegionThreadJobs(i: Int) {
        partitionedRegionThreadJobsMeter.increment(i)
    }

    override fun incFunctionExecutionThreadJobs(i: Int) {
        functionThreadJobsMeter.increment(i)
    }

    override fun incNumViewThreads(threads: Int) {
        threadCountForViewMessageMeter.increment(threads)
    }

    override fun incNumSerialThreadJobs(jobs: Int) {
        threadJobsForSerialThreadsMeter.increment(jobs)
    }

    override fun incViewProcessorThreadJobs(jobs: Int) {
        threadJobsForViewThreadsMeter.increment(jobs)
    }

    override fun incSerialPooledProcessorThreadJobs(jobs: Int) {
        threadJobsForSerialPoolThreadsMeter.increment(jobs)
    }

    override fun incNormalPoolThreadJobs(jobs: Int) {
        threadJobsForProcessingThreadsMeter.increment(jobs)
    }

    override fun incHighPriorityThreadJobs(jobs: Int) {
        threadJobsForHighPriorityThreadsMeter.increment(jobs)
    }

    override fun incWaitingPoolThreadJobs(jobs: Int) {
        threadJobsForWaitingThreadsMeter.increment(jobs)
    }

    override fun incElders(elderCount: Int) {
        elderCountMeter.increment(elderCount)
    }

    override fun incInitialImageMessagesInFlight(`val`: Int) {
        initialImageMessageInFlightMeter.increment(`val`)
    }

    override fun incInitialImageRequestsInProgress(`val`: Int) {
        initialImageMessageInProgressMeter.increment(`val`)
    }

    override fun incHeartbeatRequestsSent() {
        heartBeatRequestSentMeter.increment()
    }

    override fun incHeartbeatRequestsReceived() {
        heartBeatRequestReceivedMeter.increment()
    }

    override fun incHeartbeatsSent() {
        heartBeatSendMeter.increment()
    }

    override fun incHeartbeatsReceived() {
        heartBeatReceivedMeter.increment()
    }

    override fun incSuspectsSent() {
        suspectSentMeter.increment()
    }

    override fun incSuspectsReceived() {
        suspectReceivedMeter.increment()
    }

    override fun incFinalCheckRequestsSent() {
        finalCheckRequestSentMeter.increment()
    }

    override fun incFinalCheckRequestsReceived() {
        finalCheckRequestReceivedMeter.increment()
    }

    override fun incFinalCheckResponsesSent() {
        finalCheckResponseSentMeter.increment()
    }

    override fun incFinalCheckResponsesReceived() {
        finalCheckResponseReceivedMeter.increment()
    }

    override fun incTcpFinalCheckRequestsSent() {
        tcpFinalCheckRequestSentMeter.increment()
    }

    override fun incTcpFinalCheckRequestsReceived() {
        tcpFinalCheckRequestReceivedMeter.increment()
    }

    override fun incTcpFinalCheckResponsesSent() {
        tcpFinalCheckResponseSentMeter.increment()
    }

    override fun incTcpFinalCheckResponsesReceived() {
        tcpFinalCheckResponseReceivedMeter.increment()
    }

    override fun incUdpFinalCheckRequestsSent() {
        udpFinalCheckRequestSentMeter.increment()
    }

    override fun incUdpFinalCheckResponsesReceived() {
        udpFinalCheckResponseReceivedMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getSerialQueueBytes(): Int = distributionQueueSerialBytesMeter.getValue().toInt()

    override fun getOverflowQueueHelper(): ThrottledQueueStatHelper =
            object : ThrottledQueueStatHelper {
                override fun incThrottleCount() {
                    incOverflowQueueThrottleCount(1)
                }

                override fun throttleTime(nanos: Long) {
                    incOverflowQueueThrottleTime(nanos)
                }

                override fun add() {
                    incOverflowQueueSize(1)
                }

                override fun remove() {
                    incOverflowQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incOverflowQueueSize(-count)
                }
            }

    override fun getWaitingQueueHelper(): QueueStatHelper =
            object : QueueStatHelper {
                override fun add() {
                    incWaitingQueueSize(1)
                }

                override fun remove() {
                    incWaitingQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incWaitingQueueSize(-count)
                }
            }

    override fun getHighPriorityQueueHelper(): ThrottledQueueStatHelper =
            object : ThrottledQueueStatHelper {
                override fun incThrottleCount() {
                    incHighPriorityQueueThrottleCount(1)
                }

                override fun throttleTime(nanos: Long) {
                    incHighPriorityQueueThrottleTime(nanos)
                }

                override fun add() {
                    incHighPriorityQueueSize(1)
                }

                override fun remove() {
                    incHighPriorityQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incHighPriorityQueueSize(-count)
                }
            }

    override fun getPartitionedRegionQueueHelper(): ThrottledQueueStatHelper =
            object : ThrottledQueueStatHelper {
                override fun incThrottleCount() {
                    incPartitionedRegionQueueThrottleCount(1)
                }

                override fun throttleTime(nanos: Long) {
                    incPartitionedRegionQueueThrottleTime(nanos)
                }

                override fun add() {
                    incPartitionedRegionQueueSize(1)
                }

                override fun remove() {
                    incPartitionedRegionQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incPartitionedRegionQueueSize(-count)
                }
            }

    override fun getPartitionedRegionPoolHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incPartitionedRegionThreadJobs(1)
                }

                override fun endJob() {
                    incPartitionedRegionThreadJobs(-1)
                }
            }

    override fun getFunctionExecutionQueueHelper(): ThrottledQueueStatHelper =
            object : ThrottledQueueStatHelper {
                override fun incThrottleCount() {
                    incFunctionExecutionQueueThrottleCount(1)
                }

                override fun throttleTime(nanos: Long) {
                    incFunctionExecutionQueueThrottleTime(nanos)
                }

                override fun add() {
                    incFunctionExecutionQueueSize(1)
                }

                override fun remove() {
                    incFunctionExecutionQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incFunctionExecutionQueueSize(-count)
                }
            }

    override fun getFunctionExecutionPoolHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incFunctionExecutionThreadJobs(1)
                }

                override fun endJob() {
                    incFunctionExecutionThreadJobs(-1)
                }
            }

    override fun getSerialQueueHelper(): ThrottledMemQueueStatHelper =
            object : ThrottledMemQueueStatHelper {
                override fun incThrottleCount() {
                    incSerialQueueThrottleCount(1)
                }

                override fun throttleTime(nanos: Long) {
                    incSerialQueueThrottleTime(nanos)
                }

                override fun add() {
                    incSerialQueueSize(1)
                }

                override fun remove() {
                    incSerialQueueSize(-1)
                }

                override fun remove(count: Int) {
                    incSerialQueueSize(-count)
                }

                override fun addMem(amount: Int) {
                    incSerialQueueBytes(amount)
                }

                override fun removeMem(amount: Int) {
                    incSerialQueueBytes(amount * -1)
                }
            }

    override fun getNormalPoolHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incNormalPoolThreadJobs(1)
                }

                override fun endJob() {
                    incNormalPoolThreadJobs(-1)
                }
            }

    override fun getWaitingPoolHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incWaitingPoolThreadJobs(1)
                }

                override fun endJob() {
                    incWaitingPoolThreadJobs(-1)
                }
            }

    override fun getHighPriorityPoolHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incHighPriorityThreadJobs(1)
                }

                override fun endJob() {
                    incHighPriorityThreadJobs(-1)
                }
            }

    override fun getSerialProcessorHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incNumSerialThreadJobs(1)
                }

                override fun endJob() {
                    incNumSerialThreadJobs(-1)
                }
            }

    override fun getViewProcessorHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incViewProcessorThreadJobs(1)
                }

                override fun endJob() {
                    incViewProcessorThreadJobs(-1)
                }
            }

    override fun getSerialPooledProcessorHelper(): PoolStatHelper =
            object : PoolStatHelper {
                override fun startJob() {
                    incSerialPooledProcessorThreadJobs(1)
                }

                override fun endJob() {
                    incSerialPooledProcessorThreadJobs(-1)
                }
            }


    override fun close() {
        //noop
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSentMessages(): Long = distributionMessageSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSentCommitMessages(): Long = distributionMessageTransactionCommitMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getCommitWaits(): Long = distributionMessageTransactionCommitWaitMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSentMessagesTime(): Long = distributionMessageTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSentMessagesMaxTime(): Long = -1

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBroadcastMessages(): Long = distributionBroadcastMessageMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getBroadcastMessagesTime(): Long = distributionBroadcastMessageTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReceivedMessages(): Long = distributionMessageReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReceivedBytes(): Long = distributionMessageReceivedBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getProcessedMessages(): Long = distributionMessageProcessedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getProcessedMessagesTime(): Long = distributionMessageProcessedTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMessageProcessingScheduleTime(): Long = distributionChannelDispatchingTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getOverflowQueueSize(): Int = distributionQueueOverflowMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumProcessingThreads(): Int = distributionThreadNormalPriorityMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumSerialThreads(): Int = distributionThreadSerialMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUDPDispatchRequestTime(): Long = distributionChannelDispatchingTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyMessageTime(): Long = distributionChannelUDPTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getDistributeMessageTime(): Long = distributionChannelDistributeTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNodes(): Int = distributionNodesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyWaitsInProgress(): Int = distributionReplyThreadMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyWaitsCompleted(): Int = distributionReplyThreadCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyWaitTime(): Long = distributionReplyThreadWaitingTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyWaitMaxTime(): Long = -1

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMcastWrites(): Int = socketMultiCastWriteMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMcastReads(): Int = socketMultiCastReadMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUDPMsgDecryptionTime(): Long = messageUDPDecryptionTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUDPMsgEncryptionTime(): Long = messageUDPEncryptionTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getReplyTimeouts(): Long = distributionMessageTimeoutMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSendersSU(): Int = socketSenderSharedUnOrderedCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncSocketWritesInProgress(): Int = socketAsyncWriteInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncSocketWrites(): Int = socketAsyncWriteCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncSocketWriteRetries(): Int = socketAsyncWriteRetriesMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncSocketWriteBytes(): Long = socketAsyncWriteBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncSocketWriteTime(): Long = socketAsyncWriteTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueAddTime(): Long = asyncQueueAddTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueRemoveTime(): Long = asyncQueueRemoveTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueues(): Int = asyncQueueCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueFlushesInProgress(): Int = asyncQueueFlushesInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueFlushesCompleted(): Int = asyncQueueFlushedCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueFlushTime(): Long = asyncQueueFlushesTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueTimeouts(): Int = asyncQueueTimeOutExceededMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueSizeExceeded(): Int = asyncQueueSizeExceededMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncDistributionTimeoutExceeded(): Int = asyncQueueDistributionTimeOutExceededMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueueSize(): Long = asyncQueueSizeBytesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncQueuedMsgs(): Long = asyncQueueQueuedMessagesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncDequeuedMsgs(): Long = asyncQueueDequeuedMessagesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncConflatedMsgs(): Long = asyncQueueConflatedMessagesMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncThreads(): Int = asyncQueueThreadMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncThreadInProgress(): Int = asyncQueueThreadInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncThreadCompleted(): Int = asyncQueueThreadCompletedMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getAsyncThreadTime(): Long = asyncQueueThreadTimer.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getNumViewThreads(): Int = threadCountForViewMessageMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getElders(): Int = elderCountMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getInitialImageMessagesInFlight(): Int = initialImageMessageInFlightMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getInitialImageRequestsInProgress(): Int = initialImageMessageInProgressMeter.getValue().toInt()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeartbeatRequestsSent(): Long = heartBeatRequestSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeartbeatRequestsReceived(): Long = heartBeatRequestReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeartbeatsSent(): Long = heartBeatSendMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getHeartbeatsReceived(): Long = heartBeatReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSuspectsSent(): Long = suspectReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getSuspectsReceived(): Long = suspectReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFinalCheckRequestsSent(): Long = finalCheckRequestSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFinalCheckRequestsReceived(): Long = finalCheckRequestReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFinalCheckResponsesSent(): Long = finalCheckResponseSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getFinalCheckResponsesReceived(): Long = finalCheckResponseReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTcpFinalCheckRequestsSent(): Long = tcpFinalCheckRequestSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTcpFinalCheckRequestsReceived(): Long = tcpFinalCheckRequestReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTcpFinalCheckResponsesSent(): Long = tcpFinalCheckResponseSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getTcpFinalCheckResponsesReceived(): Long = tcpFinalCheckResponseReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUdpFinalCheckRequestsSent(): Long = udpFinalCheckRequestSentMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getUdpFinalCheckResponsesReceived(): Long = udpFinalCheckResponseReceivedMeter.getValue()

    @Deprecated("The method is deprecated to be removed, but here until a better stats mechanism is found")
    override fun getMcastRetransmits(): Int = socketMultiCastRetransmitMeter.getValue().toInt()
}
