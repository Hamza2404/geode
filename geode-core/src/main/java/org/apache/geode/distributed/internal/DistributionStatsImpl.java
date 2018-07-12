/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode.distributed.internal;

import org.apache.logging.log4j.Logger;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.internal.tcp.Buffers;
import org.apache.geode.internal.util.Breadcrumbs;

/**
 * This class maintains statistics in GemFire about the distribution manager and distribution in
 * general.
 *
 *
 */
public class DistributionStatsImpl implements DistributionStats, GFSStatsImplementor {
  private static final Logger logger = LogService.getLogger();
  public static boolean enableClockStats;

  ////////////////// Statistic "Id" Fields //////////////////

  private StatisticsType type;
  private int sentMessagesId;
  private int sentCommitMessagesId;
  private int commitWaitsId;
  private int sentMessagesTimeId;
  private int sentMessagesMaxTimeId;
  private int broadcastMessagesId;
  private int broadcastMessagesTimeId;
  private int receivedMessagesId;
  private int receivedBytesId;
  private int sentBytesId;
  private int processedMessagesId;
  private int processedMessagesTimeId;
  private int messageProcessingScheduleTimeId;
  private int messageChannelTimeId;
  private int udpDispatchRequestTimeId;
  private int replyMessageTimeId;
  private int distributeMessageTimeId;
  private int nodesId;
  private int overflowQueueSizeId;
  private int processingThreadsId;
  private int serialThreadsId;
  private int waitingThreadsId;
  private int highPriorityThreadsId;
  private int partitionedRegionThreadsId;
  private int functionExecutionThreadsId;
  private int partitionedRegionThreadJobsId;
  private int functionExecutionThreadJobsId;
  private int waitingQueueSizeId;
  private int overflowQueueThrottleTimeId;
  private int overflowQueueThrottleCountId;
  private int highPriorityQueueSizeId;
  private int highPriorityQueueThrottleTimeId;
  private int highPriorityQueueThrottleCountId;
  private int partitionedRegionQueueSizeId;
  private int partitionedRegionQueueThrottleTimeId;
  private int partitionedRegionQueueThrottleCountId;
  private int functionExecutionQueueSizeId;
  private int functionExecutionQueueThrottleCountId;
  private int functionExecutionQueueThrottleTimeId;
  private int serialQueueSizeId;
  private int serialQueueBytesId;
  private int serialPooledThreadId;
  private int serialQueueThrottleTimeId;
  private int serialQueueThrottleCountId;
  private int replyWaitsInProgressId;
  private int replyWaitsCompletedId;
  private int replyWaitTimeId;
  private int replyTimeoutsId;
  private int replyWaitMaxTimeId;
  private int receiverConnectionsId;
  private int failedAcceptsId;
  private int failedConnectsId;
  private int reconnectAttemptsId;
  private int lostConnectionLeaseId;
  private int sharedOrderedSenderConnectionsId;
  private int sharedUnorderedSenderConnectionsId;
  private int threadOrderedSenderConnectionsId;
  private int threadUnorderedSenderConnectionsId;

  private int syncSocketWritesInProgressId;
  private int syncSocketWriteTimeId;
  private int syncSocketWritesId;
  private int syncSocketWriteBytesId;

  private int ucastReadsId;
  private int ucastReadBytesId;
  private int ucastWritesId;
  private int ucastWriteBytesId;
  private int ucastRetransmitsId;

  private int mcastReadsId;
  private int mcastReadBytesId;
  private int mcastWritesId;
  private int mcastWriteBytesId;
  private int mcastRetransmitsId;
  private int mcastRetransmitRequestsId;

  private int serializationTimeId;
  private int serializationsId;
  private int serializedBytesId;

  private int pdxSerializationsId;
  private int pdxSerializedBytesId;

  private int deserializationTimeId;
  private int deserializationsId;
  private int deserializedBytesId;
  private int pdxDeserializationsId;
  private int pdxDeserializedBytesId;
  private int pdxInstanceDeserializationsId;
  private int pdxInstanceDeserializationTimeId;
  private int pdxInstanceCreationsId;

  private int msgSerializationTimeId;
  private int msgDeserializationTimeId;

  private int udpMsgEncryptionTimeId;
  private int udpMsgDecryptionTimeId;

  private int batchSendTimeId;
  private int batchCopyTimeId;
  private int batchWaitTimeId;
  private int batchFlushTimeId;

  private int threadOwnedReceiversId;
  private int threadOwnedReceiversId2;

  private int asyncSocketWritesInProgressId;
  private int asyncSocketWritesId;
  private int asyncSocketWriteRetriesId;
  private int asyncSocketWriteTimeId;
  private int asyncSocketWriteBytesId;

  private int socketLocksInProgressId;
  private int socketLocksId;
  private int socketLockTimeId;

  private int bufferAcquiresInProgressId;
  private int bufferAcquiresId;
  private int bufferAcquireTimeId;

  private int asyncQueueAddTimeId;
  private int asyncQueueRemoveTimeId;

  private int asyncQueuesId;
  private int asyncQueueFlushesInProgressId;
  private int asyncQueueFlushesCompletedId;
  private int asyncQueueFlushTimeId;
  private int asyncQueueTimeoutExceededId;
  private int asyncQueueSizeExceededId;
  private int asyncDistributionTimeoutExceededId;
  private int asyncQueueSizeId;
  private int asyncQueuedMsgsId;
  private int asyncDequeuedMsgsId;
  private int asyncConflatedMsgsId;

  private int asyncThreadsId;
  private int asyncThreadInProgressId;
  private int asyncThreadCompletedId;
  private int asyncThreadTimeId;

  private int receiverDirectBufferSizeId;
  private int receiverHeapBufferSizeId;
  private int senderDirectBufferSizeId;
  private int senderHeapBufferSizeId;

  private int messagesBeingReceivedId;
  private int messageBytesBeingReceivedId;

  private int serialThreadStartsId;
  private int viewThreadStartsId;
  private int processingThreadStartsId;
  private int highPriorityThreadStartsId;
  private int waitingThreadStartsId;
  private int partitionedRegionThreadStartsId;
  private int functionExecutionThreadStartsId;
  private int serialPooledThreadStartsId;
  private int TOSentMsgId;

  private int replyHandoffTimeId;

  private int viewThreadsId;
  private int serialThreadJobsId;
  private int viewProcessorThreadJobsId;
  private int serialPooledThreadJobsId;
  private int pooledMessageThreadJobsId;
  private int highPriorityThreadJobsId;
  private int waitingPoolThreadJobsId;

  private int eldersId;
  private int initialImageMessagesInFlightId;
  private int initialImageRequestsInProgressId;

  // For GMSHealthMonitor
  private int heartbeatRequestsSentId;
  private int heartbeatRequestsReceivedId;
  private int heartbeatsSentId;
  private int heartbeatsReceivedId;
  private int suspectsSentId;
  private int suspectsReceivedId;
  private int finalCheckRequestsSentId;
  private int finalCheckRequestsReceivedId;
  private int finalCheckResponsesSentId;
  private int finalCheckResponsesReceivedId;
  private int tcpFinalCheckRequestsSentId;
  private int tcpFinalCheckRequestsReceivedId;
  private int tcpFinalCheckResponsesSentId;
  private int tcpFinalCheckResponsesReceivedId;
  private int udpFinalCheckRequestsSentId;
  private int udpFinalCheckRequestsReceivedId;
  private int udpFinalCheckResponsesSentId;
  private int udpFinalCheckResponsesReceivedId;

  @Override
  public void initializeStats(StatisticsFactory factory) {
    String statName = "DistributionStats";
    String statDescription = "Statistics on the gemfire distribution layer.";

    final String sentMessagesDesc =
        "The number of distribution messages that this GemFire system has sent. This includes broadcastMessages.";
    final String sentCommitMessagesDesc =
        "The number of transaction commit messages that this GemFire system has created to be sent. Note that it is possible for a commit to only create one message even though it will end up being sent to multiple recipients.";
    final String commitWaitsDesc =
        "The number of transaction commits that had to wait for a response before they could complete.";
    final String sentMessagesTimeDesc =
        "The total amount of time this distribution manager has spent sending messages. This includes broadcastMessagesTime.";
    final String sentMessagesMaxTimeDesc =
        "The highest amount of time this distribution manager has spent distributing a single message to the network.";
    final String broadcastMessagesDesc =
        "The number of distribution messages that this GemFire system has broadcast. A broadcast message is one sent to every other manager in the group.";
    final String broadcastMessagesTimeDesc =
        "The total amount of time this distribution manager has spent broadcasting messages. A broadcast message is one sent to every other manager in the group.";
    final String receivedMessagesDesc =
        "The number of distribution messages that this GemFire system has received.";
    final String receivedBytesDesc =
        "The number of distribution message bytes that this GemFire system has received.";
    final String sentBytesDesc =
        "The number of distribution message bytes that this GemFire system has sent.";
    final String processedMessagesDesc =
        "The number of distribution messages that this GemFire system has processed.";
    final String processedMessagesTimeDesc =
        "The amount of time this distribution manager has spent in message.process().";
    final String messageProcessingScheduleTimeDesc =
        "The amount of time this distribution manager has spent dispatching message to processor threads.";
    final String overflowQueueSizeDesc =
        "The number of normal distribution messages currently waiting to be processed.";
    final String waitingQueueSizeDesc =
        "The number of distribution messages currently waiting for some other resource before they can be processed.";
    final String overflowQueueThrottleTimeDesc =
        "The total amount of time, in nanoseconds, spent delayed by the overflow queue throttle.";
    final String overflowQueueThrottleCountDesc =
        "The total number of times a thread was delayed in adding a normal message to the overflow queue.";
    final String highPriorityQueueSizeDesc =
        "The number of high priority distribution messages currently waiting to be processed.";
    final String highPriorityQueueThrottleTimeDesc =
        "The total amount of time, in nanoseconds, spent delayed by the high priority queue throttle.";
    final String highPriorityQueueThrottleCountDesc =
        "The total number of times a thread was delayed in adding a normal message to the high priority queue.";
    final String serialQueueSizeDesc =
        "The number of serial distribution messages currently waiting to be processed.";
    final String serialQueueBytesDesc =
        "The approximate number of bytes consumed by serial distribution messages currently waiting to be processed.";
    final String serialPooledThreadDesc =
        "The number of threads created in the SerialQueuedExecutorPool.";
    final String serialQueueThrottleTimeDesc =
        "The total amount of time, in nanoseconds, spent delayed by the serial queue throttle.";
    final String serialQueueThrottleCountDesc =
        "The total number of times a thread was delayed in adding a ordered message to the serial queue.";
    final String serialThreadsDesc =
        "The number of threads currently processing serial/ordered messages.";
    final String processingThreadsDesc =
        "The number of threads currently processing normal messages.";
    final String highPriorityThreadsDesc =
        "The number of threads currently processing high priority messages.";
    final String partitionedRegionThreadsDesc =
        "The number of threads currently processing partitioned region messages.";
    final String functionExecutionThreadsDesc =
        "The number of threads currently processing function execution messages.";
    final String waitingThreadsDesc =
        "The number of threads currently processing messages that had to wait for a resource.";
    final String messageChannelTimeDesc =
        "The total amount of time received messages spent in the distribution channel";
    final String udpDispatchRequestTimeDesc =
        "The total amount of time spent deserializing and dispatching UDP messages in the message-reader thread.";
    final String replyMessageTimeDesc =
        "The amount of time spent processing reply messages. This includes both processedMessagesTime and messageProcessingScheduleTime.";
    final String distributeMessageTimeDesc =
        "The amount of time it takes to prepare a message and send it on the network.  This includes sentMessagesTime.";
    final String nodesDesc = "The current number of nodes in this distributed system.";
    final String replyWaitsInProgressDesc = "Current number of threads waiting for a reply.";
    final String replyWaitsCompletedDesc =
        "Total number of times waits for a reply have completed.";
    final String replyWaitTimeDesc = "Total time spent waiting for a reply to a message.";
    final String replyWaitMaxTimeDesc =
        "Maximum time spent transmitting and then waiting for a reply to a message. See sentMessagesMaxTime for related information";
    final String replyTimeoutsDesc = "Total number of message replies that have timed out.";
    final String receiverConnectionsDesc =
        "Current number of sockets dedicated to receiving messages.";
    final String failedAcceptsDesc =
        "Total number of times an accept (receiver creation) of a connect from some other member has failed";
    final String failedConnectsDesc =
        "Total number of times a connect (sender creation) to some other member has failed.";
    final String reconnectAttemptsDesc =
        "Total number of times an established connection was lost and a reconnect was attempted.";
    final String lostConnectionLeaseDesc =
        "Total number of times an unshared sender socket has remained idle long enough that its lease expired.";
    final String sharedOrderedSenderConnectionsDesc =
        "Current number of shared sockets dedicated to sending ordered messages.";
    final String sharedUnorderedSenderConnectionsDesc =
        "Current number of shared sockets dedicated to sending unordered messages.";
    final String threadOrderedSenderConnectionsDesc =
        "Current number of thread sockets dedicated to sending ordered messages.";
    final String threadUnorderedSenderConnectionsDesc =
        "Current number of thread sockets dedicated to sending unordered messages.";

    final String asyncQueuesDesc = "The current number of queues for asynchronous messaging.";
    final String asyncQueueFlushesInProgressDesc =
        "Current number of asynchronous queues being flushed.";
    final String asyncQueueFlushesCompletedDesc =
        "Total number of asynchronous queue flushes completed.";
    final String asyncQueueFlushTimeDesc = "Total time spent flushing asynchronous queues.";
    final String asyncQueueTimeoutExceededDesc =
        "Total number of asynchronous queues that have timed out by being blocked for more than async-queue-timeout milliseconds.";
    final String asyncQueueSizeExceededDesc =
        "Total number of asynchronous queues that have exceeded max size.";
    final String asyncDistributionTimeoutExceededDesc =
        "Total number of times the async-distribution-timeout has been exceeded during a socket write.";
    final String asyncQueueSizeDesc = "The current size in bytes used for asynchronous queues.";
    final String asyncQueuedMsgsDesc =
        "The total number of queued messages used for asynchronous queues.";
    final String asyncDequeuedMsgsDesc =
        "The total number of queued messages that have been removed from the queue and successfully sent.";
    final String asyncConflatedMsgsDesc =
        "The total number of queued conflated messages used for asynchronous queues.";

    final String asyncThreadsDesc = "Total number of asynchronous message queue threads.";
    final String asyncThreadInProgressDesc =
        "Current iterations of work performed by asynchronous message queue threads.";
    final String asyncThreadCompletedDesc =
        "Total number of iterations of work performed by asynchronous message queue threads.";
    final String asyncThreadTimeDesc =
        "Total time spent by asynchronous message queue threads performing iterations.";
    final String receiverDirectBufferSizeDesc =
        "Current number of bytes allocated from direct memory as buffers for incoming messages.";
    final String receiverHeapBufferSizeDesc =
        "Current number of bytes allocated from Java heap memory as buffers for incoming messages.";
    final String senderDirectBufferSizeDesc =
        "Current number of bytes allocated from direct memory as buffers for outgoing messages.";
    final String senderHeapBufferSizeDesc =
        "Current number of bytes allocated from Java heap memory as buffers for outoing messages.";

    final String replyHandoffTimeDesc =
        "Total number of seconds to switch thread contexts from processing thread to application thread.";

    final String partitionedRegionThreadJobsDesc =
        "The number of messages currently being processed by partitioned region threads";
    final String functionExecutionThreadJobsDesc =
        "The number of messages currently being processed by function execution threads";
    final String viewThreadsDesc = "The number of threads currently processing view messages.";
    final String serialThreadJobsDesc =
        "The number of messages currently being processed by serial threads.";
    final String viewThreadJobsDesc =
        "The number of messages currently being processed by view threads.";
    final String serialPooledThreadJobsDesc =
        "The number of messages currently being processed by pooled serial processor threads.";
    final String processingThreadJobsDesc =
        "The number of messages currently being processed by pooled message processor threads.";
    final String highPriorityThreadJobsDesc =
        "The number of messages currently being processed by high priority processor threads.";
    final String waitingThreadJobsDesc =
        "The number of messages currently being processed by waiting pooly processor threads.";

    final String eldersDesc = "Current number of system elders hosted in this member.";
    final String initialImageMessagesInFlightDesc =
        "The number of messages with initial image data sent from this member that have not yet been acknowledged.";
    final String initialImageRequestsInProgressDesc =
        "The number of initial images this member is currently receiving.";

    // For GMSHealthMonitor
    final String heartbeatRequestsSentDesc =
        "Heartbeat request messages that this member has sent.";
    final String heartbeatRequestsReceivedDesc =
        "Heartbeat request messages that this member has received.";

    final String heartbeatsSentDesc = "Heartbeat messages that this member has sent.";
    final String heartbeatsReceivedDesc = "Heartbeat messages that this member has received.";

    final String suspectsSentDesc = "Suspect member messages that this member has sent.";
    final String suspectsReceivedDesc = "Suspect member messages that this member has received.";

    final String finalCheckRequestsSentDesc = "Final check requests that this member has sent.";
    final String finalCheckRequestsReceivedDesc =
        "Final check requests that this member has received.";

    final String finalCheckResponsesSentDesc = "Final check responses that this member has sent.";
    final String finalCheckResponsesReceivedDesc =
        "Final check responses that this member has received.";

    final String tcpFinalCheckRequestsSentDesc =
        "TCP final check requests that this member has sent.";
    final String tcpFinalCheckRequestsReceivedDesc =
        "TCP final check requests that this member has received.";

    final String tcpFinalCheckResponsesSentDesc =
        "TCP final check responses that this member has sent.";
    final String tcpFinalCheckResponsesReceivedDesc =
        "TCP final check responses that this member has received.";

    final String udpFinalCheckRequestsSentDesc =
        "UDP final check requests that this member has sent.";
    final String udpFinalCheckRequestsReceivedDesc =
        "UDP final check requests that this member has received.";

    final String udpFinalCheckResponsesSentDesc =
        "UDP final check responses that this member has sent.";
    final String udpFinalCheckResponsesReceivedDesc =
        "UDP final check responses that this member has received.";

    type = factory.createType(statName, statDescription, new StatisticDescriptor[] {
        factory.createLongCounter("sentMessages", sentMessagesDesc, "messages"),
        factory.createLongCounter("commitMessages", sentCommitMessagesDesc, "messages"),
        factory.createLongCounter("commitWaits", commitWaitsDesc, "messages"),
        factory.createLongCounter("sentMessagesTime", sentMessagesTimeDesc, "nanoseconds", false),
        factory.createLongGauge("sentMessagesMaxTime", sentMessagesMaxTimeDesc, "milliseconds", false),
        factory.createLongCounter("broadcastMessages", broadcastMessagesDesc, "messages"),
        factory.createLongCounter("broadcastMessagesTime", broadcastMessagesTimeDesc, "nanoseconds",
            false),
        factory.createLongCounter("receivedMessages", receivedMessagesDesc, "messages"),
        factory.createLongCounter("receivedBytes", receivedBytesDesc, "bytes"),
        factory.createLongCounter("sentBytes", sentBytesDesc, "bytes"),
        factory.createLongCounter("processedMessages", processedMessagesDesc, "messages"),
        factory.createLongCounter("processedMessagesTime", processedMessagesTimeDesc, "nanoseconds",
            false),
        factory.createLongCounter("messageProcessingScheduleTime", messageProcessingScheduleTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("overflowQueueSize", overflowQueueSizeDesc, "messages"),
        factory.createIntGauge("waitingQueueSize", waitingQueueSizeDesc, "messages"),
        factory.createIntGauge("overflowQueueThrottleCount", overflowQueueThrottleCountDesc, "delays"),
        factory.createLongCounter("overflowQueueThrottleTime", overflowQueueThrottleTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("highPriorityQueueSize", highPriorityQueueSizeDesc, "messages"),
        factory.createIntGauge("highPriorityQueueThrottleCount", highPriorityQueueThrottleCountDesc,
            "delays"),
        factory.createLongCounter("highPriorityQueueThrottleTime", highPriorityQueueThrottleTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("partitionedRegionQueueSize", highPriorityQueueSizeDesc, "messages"),
        factory.createIntGauge("partitionedRegionQueueThrottleCount", highPriorityQueueThrottleCountDesc,
            "delays"),
        factory.createLongCounter("partitionedRegionQueueThrottleTime", highPriorityQueueThrottleTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("functionExecutionQueueSize", highPriorityQueueSizeDesc, "messages"),
        factory.createIntGauge("functionExecutionQueueThrottleCount", highPriorityQueueThrottleCountDesc,
            "delays"),
        factory.createLongCounter("functionExecutionQueueThrottleTime", highPriorityQueueThrottleTimeDesc,
            "nanoseconds", false),
        factory.createIntGauge("serialQueueSize", serialQueueSizeDesc, "messages"),
        factory.createIntGauge("serialQueueBytes", serialQueueBytesDesc, "bytes"),
        factory.createIntCounter("serialPooledThread", serialPooledThreadDesc, "threads"),
        factory.createIntGauge("serialQueueThrottleCount", serialQueueThrottleCountDesc, "delays"),
        factory.createLongCounter("serialQueueThrottleTime", serialQueueThrottleTimeDesc, "nanoseconds",
            false),
        factory.createIntGauge("serialThreads", serialThreadsDesc, "threads"),
        factory.createIntGauge("processingThreads", processingThreadsDesc, "threads"),
        factory.createIntGauge("highPriorityThreads", highPriorityThreadsDesc, "threads"),
        factory.createIntGauge("partitionedRegionThreads", partitionedRegionThreadsDesc, "threads"),
        factory.createIntGauge("functionExecutionThreads", functionExecutionThreadsDesc, "threads"),
        factory.createIntGauge("waitingThreads", waitingThreadsDesc, "threads"),
        factory.createLongCounter("messageChannelTime", messageChannelTimeDesc, "nanoseconds", false),
        factory.createLongCounter("udpDispatchRequestTime", udpDispatchRequestTimeDesc, "nanoseconds",
            false),
        factory.createLongCounter("replyMessageTime", replyMessageTimeDesc, "nanoseconds", false),
        factory.createLongCounter("distributeMessageTime", distributeMessageTimeDesc, "nanoseconds",
            false),
        factory.createIntGauge("nodes", nodesDesc, "nodes"),
        factory.createIntGauge("replyWaitsInProgress", replyWaitsInProgressDesc, "operations"),
        factory.createIntCounter("replyWaitsCompleted", replyWaitsCompletedDesc, "operations"),
        factory.createLongCounter("replyWaitTime", replyWaitTimeDesc, "nanoseconds", false),
        factory.createLongGauge("replyWaitMaxTime", replyWaitMaxTimeDesc, "milliseconds", false),
        factory.createLongCounter("replyTimeouts", replyTimeoutsDesc, "timeouts", false),
        factory.createIntGauge("receivers", receiverConnectionsDesc, "sockets"),
        factory.createIntGauge("sendersSO", sharedOrderedSenderConnectionsDesc, "sockets"),
        factory.createIntGauge("sendersSU", sharedUnorderedSenderConnectionsDesc, "sockets"),
        factory.createIntGauge("sendersTO", threadOrderedSenderConnectionsDesc, "sockets"),
        factory.createIntGauge("sendersTU", threadUnorderedSenderConnectionsDesc, "sockets"),
        factory.createIntCounter("failedAccepts", failedAcceptsDesc, "accepts"),
        factory.createIntCounter("failedConnects", failedConnectsDesc, "connects"),
        factory.createIntCounter("reconnectAttempts", reconnectAttemptsDesc, "connects"),
        factory.createIntCounter("senderTimeouts", lostConnectionLeaseDesc, "expirations"),

        factory.createIntGauge("syncSocketWritesInProgress",
            "Current number of synchronous/blocking socket write calls in progress.", "writes"),
        factory.createLongCounter("syncSocketWriteTime",
            "Total amount of time, in nanoseconds, spent in synchronous/blocking socket write calls.",
            "nanoseconds"),
        factory.createIntCounter("syncSocketWrites",
            "Total number of completed synchronous/blocking socket write calls.", "writes"),
        factory.createLongCounter("syncSocketWriteBytes",
            "Total number of bytes sent out in synchronous/blocking mode on sockets.", "bytes"),

        factory.createIntCounter("ucastReads", "Total number of unicast datagrams received", "datagrams"),
        factory.createLongCounter("ucastReadBytes", "Total number of bytes received in unicast datagrams",
            "bytes"),
        factory.createIntCounter("ucastWrites", "Total number of unicast datagram socket write calls.",
            "writes"),
        factory.createLongCounter("ucastWriteBytes",
            "Total number of bytes sent out on unicast datagram sockets.", "bytes"),
        factory.createIntCounter("ucastRetransmits",
            "Total number of unicast datagram socket retransmissions", "writes"),

        factory.createIntCounter("mcastReads", "Total number of multicast datagrams received",
            "datagrams"),
        factory.createLongCounter("mcastReadBytes",
            "Total number of bytes received in multicast datagrams", "bytes"),
        factory.createIntCounter("mcastWrites", "Total number of multicast datagram socket write calls.",
            "writes"),
        factory.createLongCounter("mcastWriteBytes",
            "Total number of bytes sent out on multicast datagram sockets.", "bytes"),
        factory.createIntCounter("mcastRetransmits",
            "Total number of multicast datagram socket retransmissions", "writes"),
        factory.createIntCounter("mcastRetransmitRequests",
            "Total number of multicast datagram socket retransmission requests sent to other processes",
            "requests"),

        factory.createLongCounter("serializationTime",
            "Total amount of time, in nanoseconds, spent serializing objects. This includes pdx serializations.",
            "nanoseconds"),
        factory.createIntCounter("serializations",
            "Total number of object serialization calls. This includes pdx serializations.", "ops"),
        factory.createLongCounter("serializedBytes",
            "Total number of bytes produced by object serialization. This includes pdx serializations.",
            "bytes"),
        factory.createIntCounter("pdxSerializations", "Total number of pdx serializations.", "ops"),
        factory.createLongCounter("pdxSerializedBytes",
            "Total number of bytes produced by pdx serialization.", "bytes"),
        factory.createLongCounter("deserializationTime",
            "Total amount of time, in nanoseconds, spent deserializing objects. This includes deserialization that results in a PdxInstance.",
            "nanoseconds"),
        factory.createIntCounter("deserializations",
            "Total number of object deserialization calls. This includes deserialization that results in a PdxInstance.",
            "ops"),
        factory.createLongCounter("deserializedBytes",
            "Total number of bytes read by object deserialization. This includes deserialization that results in a PdxInstance.",
            "bytes"),
        factory.createIntCounter("pdxDeserializations", "Total number of pdx deserializations.", "ops"),
        factory.createLongCounter("pdxDeserializedBytes",
            "Total number of bytes read by pdx deserialization.", "bytes"),
        factory.createLongCounter("msgSerializationTime",
            "Total amount of time, in nanoseconds, spent serializing messages.", "nanoseconds"),
        factory.createLongCounter("msgDeserializationTime",
            "Total amount of time, in nanoseconds, spent deserializing messages.", "nanoseconds"),
        factory.createLongCounter("udpMsgEncryptionTime",
            "Total amount of time, in nanoseconds, spent encrypting udp messages.", "nanoseconds"),
        factory.createLongCounter("udpMsgDecryptionTime",
            "Total amount of time, in nanoseconds, spent decrypting udp messages.", "nanoseconds"),
        factory.createIntCounter("pdxInstanceDeserializations",
            "Total number of times getObject has been called on a PdxInstance.", "ops"),
        factory.createLongCounter("pdxInstanceDeserializationTime",
            "Total amount of time, in nanoseconds, spent deserializing PdxInstances by calling getObject.",
            "nanoseconds"),
        factory.createIntCounter("pdxInstanceCreations",
            "Total number of times a deserialization created a PdxInstance.", "ops"),

        factory.createLongCounter("batchSendTime",
            "Total amount of time, in nanoseconds, spent queueing and flushing message batches",
            "nanoseconds"),
        factory.createLongCounter("batchWaitTime", "Reserved for future use", "nanoseconds"),
        factory.createLongCounter("batchCopyTime",
            "Total amount of time, in nanoseconds, spent copying messages for batched transmission",
            "nanoseconds"),
        factory.createLongCounter("batchFlushTime",
            "Total amount of time, in nanoseconds, spent flushing batched messages to the network",
            "nanoseconds"),

        factory.createIntGauge("asyncSocketWritesInProgress",
            "Current number of non-blocking socket write calls in progress.", "writes"),
        factory.createIntCounter("asyncSocketWrites",
            "Total number of non-blocking socket write calls completed.", "writes"),
        factory.createIntCounter("asyncSocketWriteRetries",
            "Total number of retries needed to write a single block of data using non-blocking socket write calls.",
            "writes"),
        factory.createLongCounter("asyncSocketWriteTime",
            "Total amount of time, in nanoseconds, spent in non-blocking socket write calls.",
            "nanoseconds"),
        factory.createLongCounter("asyncSocketWriteBytes",
            "Total number of bytes sent out on non-blocking sockets.", "bytes"),

        factory.createLongCounter("asyncQueueAddTime",
            "Total amount of time, in nanoseconds, spent in adding messages to async queue.",
            "nanoseconds"),
        factory.createLongCounter("asyncQueueRemoveTime",
            "Total amount of time, in nanoseconds, spent in removing messages from async queue.",
            "nanoseconds"),

        factory.createIntGauge("asyncQueues", asyncQueuesDesc, "queues"),
        factory.createIntGauge("asyncQueueFlushesInProgress", asyncQueueFlushesInProgressDesc,
            "operations"),
        factory.createIntCounter("asyncQueueFlushesCompleted", asyncQueueFlushesCompletedDesc,
            "operations"),
        factory.createLongCounter("asyncQueueFlushTime", asyncQueueFlushTimeDesc, "nanoseconds", false),
        factory.createIntCounter("asyncQueueTimeoutExceeded", asyncQueueTimeoutExceededDesc, "timeouts"),
        factory.createIntCounter("asyncQueueSizeExceeded", asyncQueueSizeExceededDesc, "operations"),
        factory.createIntCounter("asyncDistributionTimeoutExceeded", asyncDistributionTimeoutExceededDesc,
            "operations"),
        factory.createLongGauge("asyncQueueSize", asyncQueueSizeDesc, "bytes"),
        factory.createLongCounter("asyncQueuedMsgs", asyncQueuedMsgsDesc, "msgs"),
        factory.createLongCounter("asyncDequeuedMsgs", asyncDequeuedMsgsDesc, "msgs"),
        factory.createLongCounter("asyncConflatedMsgs", asyncConflatedMsgsDesc, "msgs"),

        factory.createIntGauge("asyncThreads", asyncThreadsDesc, "threads"),
        factory.createIntGauge("asyncThreadInProgress", asyncThreadInProgressDesc, "operations"),
        factory.createIntCounter("asyncThreadCompleted", asyncThreadCompletedDesc, "operations"),
        factory.createLongCounter("asyncThreadTime", asyncThreadTimeDesc, "nanoseconds", false),

        factory.createLongGauge("receiversTO",
            "Number of receiver threads owned by non-receiver threads in other members.",
            "threads"),
        factory.createLongGauge("receiversTO2",
            "Number of receiver threads owned in turn by receiver threads in other members",
            "threads"),

        factory.createLongGauge("receiverDirectBufferSize", receiverDirectBufferSizeDesc, "bytes"),
        factory.createLongGauge("receiverHeapBufferSize", receiverHeapBufferSizeDesc, "bytes"),
        factory.createLongGauge("senderDirectBufferSize", senderDirectBufferSizeDesc, "bytes"),
        factory.createLongGauge("senderHeapBufferSize", senderHeapBufferSizeDesc, "bytes"),
        factory.createIntGauge("socketLocksInProgress",
            "Current number of threads waiting to lock a socket", "threads", false),
        factory.createIntCounter("socketLocks", "Total number of times a socket has been locked.",
            "locks"),
        factory.createLongCounter("socketLockTime",
            "Total amount of time, in nanoseconds, spent locking a socket", "nanoseconds", false),
        factory.createIntGauge("bufferAcquiresInProgress",
            "Current number of threads waiting to acquire a buffer", "threads", false),
        factory.createIntCounter("bufferAcquires", "Total number of times a buffer has been acquired.",
            "operations"),
        factory.createLongCounter("bufferAcquireTime",
            "Total amount of time, in nanoseconds, spent acquiring a socket", "nanoseconds", false),

        factory.createIntGauge("messagesBeingReceived",
            "Current number of message being received off the network or being processed after reception.",
            "messages"),
        factory.createLongGauge("messageBytesBeingReceived",
            "Current number of bytes consumed by messages being received or processed.", "bytes"),

        factory.createLongCounter("serialThreadStarts",
            "Total number of times a thread has been created for the serial message executor.",
            "starts", false),
        factory.createLongCounter("viewThreadStarts",
            "Total number of times a thread has been created for the view message executor.",
            "starts", false),
        factory.createLongCounter("processingThreadStarts",
            "Total number of times a thread has been created for the pool processing normal messages.",
            "starts", false),
        factory.createLongCounter("highPriorityThreadStarts",
            "Total number of times a thread has been created for the pool handling high priority messages.",
            "starts", false),
        factory.createLongCounter("waitingThreadStarts",
            "Total number of times a thread has been created for the waiting pool.", "starts",
            false),
        factory.createLongCounter("partitionedRegionThreadStarts",
            "Total number of times a thread has been created for the pool handling partitioned region messages.",
            "starts", false),
        factory.createLongCounter("functionExecutionThreadStarts",
            "Total number of times a thread has been created for the pool handling function execution messages.",
            "starts", false),
        factory.createLongCounter("serialPooledThreadStarts",
            "Total number of times a thread has been created for the serial pool(s).", "starts",
            false),
        factory.createLongCounter("TOSentMsgs", "Total number of messages sent on thread owned senders",
            "messages", false),
        factory.createLongCounter("replyHandoffTime", replyHandoffTimeDesc, "nanoseconds"),

        factory.createIntGauge("partitionedRegionThreadJobs", partitionedRegionThreadJobsDesc,
            "messages"),
        factory.createIntGauge("functionExecutionThreadJobs", functionExecutionThreadJobsDesc,
            "messages"),
        factory.createIntGauge("viewThreads", viewThreadsDesc, "threads"),
        factory.createIntGauge("serialThreadJobs", serialThreadJobsDesc, "messages"),
        factory.createIntGauge("viewThreadJobs", viewThreadJobsDesc, "messages"),
        factory.createIntGauge("serialPooledThreadJobs", serialPooledThreadJobsDesc, "messages"),
        factory.createIntGauge("processingThreadJobs", processingThreadJobsDesc, "messages"),
        factory.createIntGauge("highPriorityThreadJobs", highPriorityThreadJobsDesc, "messages"),
        factory.createIntGauge("waitingThreadJobs", waitingThreadJobsDesc, "messages"),

        factory.createIntGauge("elders", eldersDesc, "elders"),
        factory.createIntGauge("initialImageMessagesInFlight", initialImageMessagesInFlightDesc,
            "messages"),
        factory.createIntGauge("initialImageRequestsInProgress", initialImageRequestsInProgressDesc,
            "requests"),

        // For GMSHealthMonitor
        factory.createLongCounter("heartbeatRequestsSent", heartbeatRequestsSentDesc, "messages"),
        factory.createLongCounter("heartbeatRequestsReceived", heartbeatRequestsReceivedDesc, "messages"),
        factory.createLongCounter("heartbeatsSent", heartbeatsSentDesc, "messages"),
        factory.createLongCounter("heartbeatsReceived", heartbeatsReceivedDesc, "messages"),
        factory.createLongCounter("suspectsSent", suspectsSentDesc, "messages"),
        factory.createLongCounter("suspectsReceived", suspectsReceivedDesc, "messages"),
        factory.createLongCounter("finalCheckRequestsSent", finalCheckRequestsSentDesc, "messages"),
        factory.createLongCounter("finalCheckRequestsReceived", finalCheckRequestsReceivedDesc,
            "messages"),
        factory.createLongCounter("finalCheckResponsesSent", finalCheckResponsesSentDesc, "messages"),
        factory.createLongCounter("finalCheckResponsesReceived", finalCheckResponsesReceivedDesc,
            "messages"),
        factory.createLongCounter("tcpFinalCheckRequestsSent", tcpFinalCheckRequestsSentDesc, "messages"),
        factory.createLongCounter("tcpFinalCheckRequestsReceived", tcpFinalCheckRequestsReceivedDesc,
            "messages"),
        factory.createLongCounter("tcpFinalCheckResponsesSent", tcpFinalCheckResponsesSentDesc,
            "messages"),
        factory.createLongCounter("tcpFinalCheckResponsesReceived", tcpFinalCheckResponsesReceivedDesc,
            "messages"),
        factory.createLongCounter("udpFinalCheckRequestsSent", udpFinalCheckRequestsSentDesc, "messages"),
        factory.createLongCounter("udpFinalCheckRequestsReceived", udpFinalCheckRequestsReceivedDesc,
            "messages"),
        factory.createLongCounter("udpFinalCheckResponsesSent", udpFinalCheckResponsesSentDesc,
            "messages"),
        factory.createLongCounter("udpFinalCheckResponsesReceived", udpFinalCheckResponsesReceivedDesc,
            "messages"),});

    // Initialize id fields
    sentMessagesId = type.nameToId("sentMessages");
    sentCommitMessagesId = type.nameToId("commitMessages");
    commitWaitsId = type.nameToId("commitWaits");
    sentMessagesTimeId = type.nameToId("sentMessagesTime");
    sentMessagesMaxTimeId = type.nameToId("sentMessagesMaxTime");
    broadcastMessagesId = type.nameToId("broadcastMessages");
    broadcastMessagesTimeId = type.nameToId("broadcastMessagesTime");
    receivedMessagesId = type.nameToId("receivedMessages");
    receivedBytesId = type.nameToId("receivedBytes");
    sentBytesId = type.nameToId("sentBytes");
    processedMessagesId = type.nameToId("processedMessages");
    processedMessagesTimeId = type.nameToId("processedMessagesTime");
    messageProcessingScheduleTimeId = type.nameToId("messageProcessingScheduleTime");
    messageChannelTimeId = type.nameToId("messageChannelTime");
    udpDispatchRequestTimeId = type.nameToId("udpDispatchRequestTime");
    replyMessageTimeId = type.nameToId("replyMessageTime");
    distributeMessageTimeId = type.nameToId("distributeMessageTime");
    nodesId = type.nameToId("nodes");
    overflowQueueSizeId = type.nameToId("overflowQueueSize");
    waitingQueueSizeId = type.nameToId("waitingQueueSize");
    overflowQueueThrottleTimeId = type.nameToId("overflowQueueThrottleTime");
    overflowQueueThrottleCountId = type.nameToId("overflowQueueThrottleCount");
    highPriorityQueueSizeId = type.nameToId("highPriorityQueueSize");
    highPriorityQueueThrottleTimeId = type.nameToId("highPriorityQueueThrottleTime");
    highPriorityQueueThrottleCountId = type.nameToId("highPriorityQueueThrottleCount");
    partitionedRegionQueueSizeId = type.nameToId("partitionedRegionQueueSize");
    partitionedRegionQueueThrottleTimeId = type.nameToId("partitionedRegionQueueThrottleTime");
    partitionedRegionQueueThrottleCountId = type.nameToId("partitionedRegionQueueThrottleCount");
    functionExecutionQueueSizeId = type.nameToId("functionExecutionQueueSize");
    functionExecutionQueueThrottleTimeId = type.nameToId("functionExecutionQueueThrottleTime");
    functionExecutionQueueThrottleCountId = type.nameToId("functionExecutionQueueThrottleCount");
    serialQueueSizeId = type.nameToId("serialQueueSize");
    serialQueueBytesId = type.nameToId("serialQueueBytes");
    serialPooledThreadId = type.nameToId("serialPooledThread");
    serialQueueThrottleTimeId = type.nameToId("serialQueueThrottleTime");
    serialQueueThrottleCountId = type.nameToId("serialQueueThrottleCount");
    serialThreadsId = type.nameToId("serialThreads");
    processingThreadsId = type.nameToId("processingThreads");
    highPriorityThreadsId = type.nameToId("highPriorityThreads");
    partitionedRegionThreadsId = type.nameToId("partitionedRegionThreads");
    functionExecutionThreadsId = type.nameToId("functionExecutionThreads");
    waitingThreadsId = type.nameToId("waitingThreads");
    replyWaitsInProgressId = type.nameToId("replyWaitsInProgress");
    replyWaitsCompletedId = type.nameToId("replyWaitsCompleted");
    replyWaitTimeId = type.nameToId("replyWaitTime");
    replyTimeoutsId = type.nameToId("replyTimeouts");
    replyWaitMaxTimeId = type.nameToId("replyWaitMaxTime");
    receiverConnectionsId = type.nameToId("receivers");
    failedAcceptsId = type.nameToId("failedAccepts");
    failedConnectsId = type.nameToId("failedConnects");
    reconnectAttemptsId = type.nameToId("reconnectAttempts");
    lostConnectionLeaseId = type.nameToId("senderTimeouts");
    sharedOrderedSenderConnectionsId = type.nameToId("sendersSO");
    sharedUnorderedSenderConnectionsId = type.nameToId("sendersSU");
    threadOrderedSenderConnectionsId = type.nameToId("sendersTO");
    threadUnorderedSenderConnectionsId = type.nameToId("sendersTU");

    syncSocketWritesInProgressId = type.nameToId("syncSocketWritesInProgress");
    syncSocketWriteTimeId = type.nameToId("syncSocketWriteTime");
    syncSocketWritesId = type.nameToId("syncSocketWrites");
    syncSocketWriteBytesId = type.nameToId("syncSocketWriteBytes");

    ucastReadsId = type.nameToId("ucastReads");
    ucastReadBytesId = type.nameToId("ucastReadBytes");
    ucastWritesId = type.nameToId("ucastWrites");
    ucastWriteBytesId = type.nameToId("ucastWriteBytes");
    ucastRetransmitsId = type.nameToId("ucastRetransmits");

    mcastReadsId = type.nameToId("mcastReads");
    mcastReadBytesId = type.nameToId("mcastReadBytes");
    mcastWritesId = type.nameToId("mcastWrites");
    mcastWriteBytesId = type.nameToId("mcastWriteBytes");
    mcastRetransmitsId = type.nameToId("mcastRetransmits");
    mcastRetransmitRequestsId = type.nameToId("mcastRetransmitRequests");

    serializationTimeId = type.nameToId("serializationTime");
    serializationsId = type.nameToId("serializations");
    serializedBytesId = type.nameToId("serializedBytes");
    deserializationTimeId = type.nameToId("deserializationTime");
    deserializationsId = type.nameToId("deserializations");
    deserializedBytesId = type.nameToId("deserializedBytes");
    pdxSerializationsId = type.nameToId("pdxSerializations");
    pdxSerializedBytesId = type.nameToId("pdxSerializedBytes");
    pdxDeserializationsId = type.nameToId("pdxDeserializations");
    pdxDeserializedBytesId = type.nameToId("pdxDeserializedBytes");
    pdxInstanceDeserializationsId = type.nameToId("pdxInstanceDeserializations");
    pdxInstanceDeserializationTimeId = type.nameToId("pdxInstanceDeserializationTime");
    pdxInstanceCreationsId = type.nameToId("pdxInstanceCreations");

    msgSerializationTimeId = type.nameToId("msgSerializationTime");
    msgDeserializationTimeId = type.nameToId("msgDeserializationTime");

    udpMsgEncryptionTimeId = type.nameToId("udpMsgEncryptionTime");
    udpMsgDecryptionTimeId = type.nameToId("udpMsgDecryptionTime");

    batchSendTimeId = type.nameToId("batchSendTime");
    batchCopyTimeId = type.nameToId("batchCopyTime");
    batchWaitTimeId = type.nameToId("batchWaitTime");
    batchFlushTimeId = type.nameToId("batchFlushTime");

    asyncSocketWritesInProgressId = type.nameToId("asyncSocketWritesInProgress");
    asyncSocketWritesId = type.nameToId("asyncSocketWrites");
    asyncSocketWriteRetriesId = type.nameToId("asyncSocketWriteRetries");
    asyncSocketWriteTimeId = type.nameToId("asyncSocketWriteTime");
    asyncSocketWriteBytesId = type.nameToId("asyncSocketWriteBytes");

    asyncQueueAddTimeId = type.nameToId("asyncQueueAddTime");
    asyncQueueRemoveTimeId = type.nameToId("asyncQueueRemoveTime");

    asyncQueuesId = type.nameToId("asyncQueues");
    asyncQueueFlushesInProgressId = type.nameToId("asyncQueueFlushesInProgress");
    asyncQueueFlushesCompletedId = type.nameToId("asyncQueueFlushesCompleted");
    asyncQueueFlushTimeId = type.nameToId("asyncQueueFlushTime");
    asyncQueueTimeoutExceededId = type.nameToId("asyncQueueTimeoutExceeded");
    asyncQueueSizeExceededId = type.nameToId("asyncQueueSizeExceeded");
    asyncDistributionTimeoutExceededId = type.nameToId("asyncDistributionTimeoutExceeded");
    asyncQueueSizeId = type.nameToId("asyncQueueSize");
    asyncQueuedMsgsId = type.nameToId("asyncQueuedMsgs");
    asyncDequeuedMsgsId = type.nameToId("asyncDequeuedMsgs");
    asyncConflatedMsgsId = type.nameToId("asyncConflatedMsgs");

    asyncThreadsId = type.nameToId("asyncThreads");
    asyncThreadInProgressId = type.nameToId("asyncThreadInProgress");
    asyncThreadCompletedId = type.nameToId("asyncThreadCompleted");
    asyncThreadTimeId = type.nameToId("asyncThreadTime");

    threadOwnedReceiversId = type.nameToId("receiversTO");
    threadOwnedReceiversId2 = type.nameToId("receiversTO2");

    receiverDirectBufferSizeId = type.nameToId("receiverDirectBufferSize");
    receiverHeapBufferSizeId = type.nameToId("receiverHeapBufferSize");
    senderDirectBufferSizeId = type.nameToId("senderDirectBufferSize");
    senderHeapBufferSizeId = type.nameToId("senderHeapBufferSize");

    socketLocksInProgressId = type.nameToId("socketLocksInProgress");
    socketLocksId = type.nameToId("socketLocks");
    socketLockTimeId = type.nameToId("socketLockTime");

    bufferAcquiresInProgressId = type.nameToId("bufferAcquiresInProgress");
    bufferAcquiresId = type.nameToId("bufferAcquires");
    bufferAcquireTimeId = type.nameToId("bufferAcquireTime");
    messagesBeingReceivedId = type.nameToId("messagesBeingReceived");
    messageBytesBeingReceivedId = type.nameToId("messageBytesBeingReceived");

    serialThreadStartsId = type.nameToId("serialThreadStarts");
    viewThreadStartsId = type.nameToId("viewThreadStarts");
    processingThreadStartsId = type.nameToId("processingThreadStarts");
    highPriorityThreadStartsId = type.nameToId("highPriorityThreadStarts");
    waitingThreadStartsId = type.nameToId("waitingThreadStarts");
    partitionedRegionThreadStartsId = type.nameToId("partitionedRegionThreadStarts");
    functionExecutionThreadStartsId = type.nameToId("functionExecutionThreadStarts");
    serialPooledThreadStartsId = type.nameToId("serialPooledThreadStarts");
    TOSentMsgId = type.nameToId("TOSentMsgs");
    replyHandoffTimeId = type.nameToId("replyHandoffTime");
    partitionedRegionThreadJobsId = type.nameToId("partitionedRegionThreadJobs");
    functionExecutionThreadJobsId = type.nameToId("functionExecutionThreadJobs");
    viewThreadsId = type.nameToId("viewThreads");
    serialThreadJobsId = type.nameToId("serialThreadJobs");
    viewProcessorThreadJobsId = type.nameToId("viewThreadJobs");
    serialPooledThreadJobsId = type.nameToId("serialPooledThreadJobs");
    pooledMessageThreadJobsId = type.nameToId("processingThreadJobs");
    highPriorityThreadJobsId = type.nameToId("highPriorityThreadJobs");
    waitingPoolThreadJobsId = type.nameToId("waitingThreadJobs");

    eldersId = type.nameToId("elders");
    initialImageMessagesInFlightId = type.nameToId("initialImageMessagesInFlight");
    initialImageRequestsInProgressId = type.nameToId("initialImageRequestsInProgress");

    // For GMSHealthMonitor
    heartbeatRequestsSentId = type.nameToId("heartbeatRequestsSent");
    heartbeatRequestsReceivedId = type.nameToId("heartbeatRequestsReceived");
    heartbeatsSentId = type.nameToId("heartbeatsSent");
    heartbeatsReceivedId = type.nameToId("heartbeatsReceived");
    suspectsSentId = type.nameToId("suspectsSent");
    suspectsReceivedId = type.nameToId("suspectsReceived");
    finalCheckRequestsSentId = type.nameToId("finalCheckRequestsSent");
    finalCheckRequestsReceivedId = type.nameToId("finalCheckRequestsReceived");
    finalCheckResponsesSentId = type.nameToId("finalCheckResponsesSent");
    finalCheckResponsesReceivedId = type.nameToId("finalCheckResponsesReceived");
    tcpFinalCheckRequestsSentId = type.nameToId("tcpFinalCheckRequestsSent");
    tcpFinalCheckRequestsReceivedId = type.nameToId("tcpFinalCheckRequestsReceived");
    tcpFinalCheckResponsesSentId = type.nameToId("tcpFinalCheckResponsesSent");
    tcpFinalCheckResponsesReceivedId = type.nameToId("tcpFinalCheckResponsesReceived");
    udpFinalCheckRequestsSentId = type.nameToId("udpFinalCheckRequestsSent");
    udpFinalCheckRequestsReceivedId = type.nameToId("udpFinalCheckRequestsReceived");
    udpFinalCheckResponsesSentId = type.nameToId("udpFinalCheckResponsesSent");
    udpFinalCheckResponsesReceivedId = type.nameToId("udpFinalCheckResponsesReceived");
  }

  /** The Statistics object that we delegate most behavior to */
  private final Statistics stats;

  // private final HistogramStats replyHandoffHistogram;
  // private final HistogramStats replyWaitHistogram;

  //////////////////////// Constructors ////////////////////////

  /**
   * Creates a new <code>DistributionStats</code> and registers itself with the given statistics
   * factory.
   */
  public DistributionStatsImpl(StatisticsFactory factory, long statId) {
    initializeStats(factory);
    this.stats = factory.createAtomicStatistics(type, "distributionStats", statId);
    // this.replyHandoffHistogram = new HistogramStats("ReplyHandOff", "nanoseconds", factory,
    // new long[] {100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000},
    // false);
    // this.replyWaitHistogram = new HistogramStats("ReplyWait", "nanoseconds", factory,
    // new long[] {100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000},
    // false);
    Buffers.initBufferStats((DMStats) this);
  }

  ////////////////////// Instance Methods //////////////////////

  @Override
  public void close() {
    this.stats.close();
  }

  /**
   * Returns the total number of messages sent by the distribution manager
   */
  @Override
  public long getSentMessages() {
    return this.stats.getLong(sentMessagesId);
  }

  @Override
  public void incTOSentMsg() {
    this.stats.incLong(TOSentMsgId, 1);
  }

  @Override
  public long getSentCommitMessages() {
    return this.stats.getLong(sentCommitMessagesId);
  }

  @Override
  public long getCommitWaits() {
    return this.stats.getLong(commitWaitsId);
  }

  /**
   * Increments the total number of messages sent by the distribution manager
   */
  @Override
  public void incSentMessages(long messages) {
    this.stats.incLong(sentMessagesId, messages);
  }

  /**
   * Increments the total number of transactino commit messages sent by the distribution manager
   */
  @Override
  public void incSentCommitMessages(long messages) {
    this.stats.incLong(sentCommitMessagesId, messages);
  }

  @Override
  public void incCommitWaits() {
    this.stats.incLong(commitWaitsId, 1);
  }

  /**
   * Returns the total number of nanoseconds spent sending messages.
   */
  @Override
  public long getSentMessagesTime() {
    return this.stats.getLong(sentMessagesTimeId);
  }

  /**
   * Increments the total number of nanoseconds spend sending messages.
   * <p>
   * This also sets the sentMessagesMaxTime, if appropriate
   */
  @Override
  public void incSentMessagesTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(sentMessagesTimeId, nanos);
      long millis = nanos / 1000000;
      if (getSentMessagesMaxTime() < millis) {
        this.stats.setLong(sentMessagesMaxTimeId, millis);
      }
    }
  }

  /**
   * Returns the longest time required to distribute a message, in nanos
   */
  @Override
  public long getSentMessagesMaxTime() {
    return this.stats.getLong(sentMessagesMaxTimeId);
  }


  /**
   * Returns the total number of messages broadcast by the distribution manager
   */
  @Override
  public long getBroadcastMessages() {
    return this.stats.getLong(broadcastMessagesId);
  }

  /**
   * Increments the total number of messages broadcast by the distribution manager
   */
  @Override
  public void incBroadcastMessages(long messages) {
    this.stats.incLong(broadcastMessagesId, messages);
  }

  /**
   * Returns the total number of nanoseconds spent sending messages.
   */
  @Override
  public long getBroadcastMessagesTime() {
    return this.stats.getLong(broadcastMessagesTimeId);
  }

  /**
   * Increments the total number of nanoseconds spend sending messages.
   */
  @Override
  public void incBroadcastMessagesTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(broadcastMessagesTimeId, nanos);
    }
  }

  /**
   * Returns the total number of messages received by the distribution manager
   */
  @Override
  public long getReceivedMessages() {
    return this.stats.getLong(receivedMessagesId);
  }

  /**
   * Increments the total number of messages received by the distribution manager
   */
  @Override
  public void incReceivedMessages(long messages) {
    this.stats.incLong(receivedMessagesId, messages);
  }

  /**
   * Returns the total number of bytes received by the distribution manager
   */
  @Override
  public long getReceivedBytes() {
    return this.stats.getLong(receivedBytesId);
  }

  @Override
  public void incReceivedBytes(long bytes) {
    this.stats.incLong(receivedBytesId, bytes);
  }

  @Override
  public void incSentBytes(long bytes) {
    this.stats.incLong(sentBytesId, bytes);
  }

  /**
   * Returns the total number of messages processed by the distribution manager
   */
  @Override
  public long getProcessedMessages() {
    return this.stats.getLong(processedMessagesId);
  }

  /**
   * Increments the total number of messages processed by the distribution manager
   */
  @Override
  public void incProcessedMessages(long messages) {
    this.stats.incLong(processedMessagesId, messages);
  }

  /**
   * Returns the total number of nanoseconds spent processing messages.
   */
  @Override
  public long getProcessedMessagesTime() {
    return this.stats.getLong(processedMessagesTimeId);
  }

  /**
   * Increments the total number of nanoseconds spend processing messages.
   */
  @Override
  public void incProcessedMessagesTime(long start) {
    if (enableClockStats) {
      this.stats.incLong(processedMessagesTimeId, System.nanoTime() - start);
    }
  }

  /**
   * Returns the total number of nanoseconds spent scheduling messages to be processed.
   */
  @Override
  public long getMessageProcessingScheduleTime() {
    return this.stats.getLong(messageProcessingScheduleTimeId);
  }

  /**
   * Increments the total number of nanoseconds spent scheduling messages to be processed.
   */
  @Override
  public void incMessageProcessingScheduleTime(long elapsed) {
    if (enableClockStats) {
      this.stats.incLong(messageProcessingScheduleTimeId, elapsed);
    }
  }

  @Override
  public int getOverflowQueueSize() {
    return this.stats.getInt(overflowQueueSizeId);
  }

  @Override
  public void incOverflowQueueSize(int messages) {
    this.stats.incInt(overflowQueueSizeId, messages);
  }

  @Override
  public void incWaitingQueueSize(int messages) {
    this.stats.incInt(waitingQueueSizeId, messages);
  }

  @Override
  public void incOverflowQueueThrottleCount(int delays) {
    this.stats.incInt(overflowQueueThrottleCountId, delays);
  }

  @Override
  public void incOverflowQueueThrottleTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(overflowQueueThrottleTimeId, nanos);
    }
  }

  @Override
  public void incHighPriorityQueueSize(int messages) {
    this.stats.incInt(highPriorityQueueSizeId, messages);
  }

  @Override
  public void incHighPriorityQueueThrottleCount(int delays) {
    this.stats.incInt(highPriorityQueueThrottleCountId, delays);
  }

  @Override
  public void incHighPriorityQueueThrottleTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(highPriorityQueueThrottleTimeId, nanos);
    }
  }

  @Override
  public void incPartitionedRegionQueueSize(int messages) {
    this.stats.incInt(partitionedRegionQueueSizeId, messages);
  }

  @Override
  public void incPartitionedRegionQueueThrottleCount(int delays) {
    this.stats.incInt(partitionedRegionQueueThrottleCountId, delays);
  }

  @Override
  public void incPartitionedRegionQueueThrottleTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(partitionedRegionQueueThrottleTimeId, nanos);
    }
  }

  @Override
  public void incFunctionExecutionQueueSize(int messages) {
    this.stats.incInt(functionExecutionQueueSizeId, messages);
  }

  @Override
  public void incFunctionExecutionQueueThrottleCount(int delays) {
    this.stats.incInt(functionExecutionQueueThrottleCountId, delays);
  }

  @Override
  public void incFunctionExecutionQueueThrottleTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(functionExecutionQueueThrottleTimeId, nanos);
    }
  }

  @Override
  public void incSerialQueueSize(int messages) {
    this.stats.incInt(serialQueueSizeId, messages);
  }

  @Override
  public void incSerialQueueBytes(int amount) {
    this.stats.incInt(serialQueueBytesId, amount);
  }

  @Override
  public int getSerialQueueBytes() {
    return this.stats.getInt(serialQueueBytesId);
  }

  @Override
  public void incSerialPooledThread() {
    this.stats.incInt(serialPooledThreadId, 1);
  }

  @Override
  public void incSerialQueueThrottleCount(int delays) {
    this.stats.incInt(serialQueueThrottleCountId, delays);
  }

  @Override
  public void incSerialQueueThrottleTime(long nanos) {
    if (enableClockStats) {
      this.stats.incLong(serialQueueThrottleTimeId, nanos);
    }
  }

  @Override
  public int getNumProcessingThreads() {
    return this.stats.getInt(processingThreadsId);
  }

  @Override
  public void incNumProcessingThreads(int threads) {
    this.stats.incInt(processingThreadsId, threads);
  }

  @Override
  public int getNumSerialThreads() {
    return this.stats.getInt(serialThreadsId);
  }

  @Override
  public void incNumSerialThreads(int threads) {
    this.stats.incInt(serialThreadsId, threads);
  }

  @Override
  public void incWaitingThreads(int threads) {
    this.stats.incInt(waitingThreadsId, threads);
  }

  @Override
  public void incHighPriorityThreads(int threads) {
    this.stats.incInt(highPriorityThreadsId, threads);
  }

  @Override
  public void incPartitionedRegionThreads(int threads) {
    this.stats.incInt(partitionedRegionThreadsId, threads);
  }

  @Override
  public void incFunctionExecutionThreads(int threads) {
    this.stats.incInt(functionExecutionThreadsId, threads);
  }

  @Override
  public void incMessageChannelTime(long delta) {
    if (enableClockStats) {
      this.stats.incLong(messageChannelTimeId, delta);
    }
  }

  @Override
  public void incUDPDispatchRequestTime(long delta) {
    if (enableClockStats) {
      this.stats.incLong(udpDispatchRequestTimeId, delta);
    }
  }

  @Override
  public long getUDPDispatchRequestTime() {
    return this.stats.getLong(udpDispatchRequestTimeId);
  }

  @Override
  public long getReplyMessageTime() {
    return this.stats.getLong(replyMessageTimeId);
  }

  @Override
  public void incReplyMessageTime(long val) {
    if (enableClockStats) {
      this.stats.incLong(replyMessageTimeId, val);
    }
  }

  @Override
  public long getDistributeMessageTime() {
    return this.stats.getLong(distributeMessageTimeId);
  }

  @Override
  public void incDistributeMessageTime(long val) {
    if (enableClockStats) {
      this.stats.incLong(distributeMessageTimeId, val);
    }
  }

  @Override
  public int getNodes() {
    return this.stats.getInt(nodesId);
  }

  @Override
  public void setNodes(int val) {
    this.stats.setInt(nodesId, val);
  }

  @Override
  public void incNodes(int val) {
    this.stats.incInt(nodesId, val);
  }

  @Override
  public int getReplyWaitsInProgress() {
    return stats.getInt(replyWaitsInProgressId);
  }

  @Override
  public int getReplyWaitsCompleted() {
    return stats.getInt(replyWaitsCompletedId);
  }

  @Override
  public long getReplyWaitTime() {
    return stats.getLong(replyWaitTimeId);
  }

  @Override
  public long getReplyWaitMaxTime() {
    return stats.getLong(replyWaitMaxTimeId);
  }

  @Override
  public long startSocketWrite(boolean sync) {
    if (sync) {
      stats.incInt(syncSocketWritesInProgressId, 1);
    } else {
      stats.incInt(asyncSocketWritesInProgressId, 1);
    }
    return System.nanoTime();
  }

  @Override
  public void endSocketWrite(boolean sync, long start, int bytesWritten, int retries) {
    final long now = System.nanoTime();
    if (sync) {
      stats.incInt(syncSocketWritesInProgressId, -1);
      stats.incInt(syncSocketWritesId, 1);
      stats.incLong(syncSocketWriteBytesId, bytesWritten);
      if (enableClockStats) {
        stats.incLong(syncSocketWriteTimeId, now - start);
      }
    } else {
      stats.incInt(asyncSocketWritesInProgressId, -1);
      stats.incInt(asyncSocketWritesId, 1);
      if (retries != 0) {
        stats.incInt(asyncSocketWriteRetriesId, retries);
      }
      stats.incLong(asyncSocketWriteBytesId, bytesWritten);
      if (enableClockStats) {
        stats.incLong(asyncSocketWriteTimeId, now - start);
      }
    }
  }

  @Override
  public long startSocketLock() {
    stats.incInt(socketLocksInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endSocketLock(long start) {
    long ts = System.nanoTime();
    stats.incInt(socketLocksInProgressId, -1);
    stats.incInt(socketLocksId, 1);
    stats.incLong(socketLockTimeId, ts - start);
  }

  @Override
  public long startBufferAcquire() {
    stats.incInt(bufferAcquiresInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endBufferAcquire(long start) {
    long ts = System.nanoTime();
    stats.incInt(bufferAcquiresInProgressId, -1);
    stats.incInt(bufferAcquiresId, 1);
    stats.incLong(bufferAcquireTimeId, ts - start);
  }

  @Override
  public void incUcastWriteBytes(int bytesWritten) {
    stats.incInt(ucastWritesId, 1);
    stats.incLong(ucastWriteBytesId, bytesWritten);
  }

  @Override
  public void incMcastWriteBytes(int bytesWritten) {
    stats.incInt(mcastWritesId, 1);
    stats.incLong(mcastWriteBytesId, bytesWritten);
  }

  @Override
  public int getMcastWrites() {
    return stats.getInt(mcastWritesId);
  }

  @Override
  public int getMcastReads() {
    return stats.getInt(mcastReadsId);
  }

  @Override
  public long getUDPMsgDecryptionTime() {
    return stats.getLong(udpMsgDecryptionTimeId);
  }

  @Override
  public long getUDPMsgEncryptionTime() {
    return stats.getLong(udpMsgEncryptionTimeId);
  }

  @Override
  public void incMcastReadBytes(int amount) {
    stats.incInt(mcastReadsId, 1);
    stats.incLong(mcastReadBytesId, amount);
  }

  @Override
  public void incUcastReadBytes(int amount) {
    stats.incInt(ucastReadsId, 1);
    stats.incLong(ucastReadBytesId, amount);
  }

  @Override
  public long startSerialization() {
    return System.nanoTime();
  }

  @Override
  public void endSerialization(long start, int bytes) {
    if (enableClockStats) {
      stats.incLong(serializationTimeId, System.nanoTime() - start);
    }
    stats.incInt(serializationsId, 1);
    stats.incLong(serializedBytesId, bytes);
  }

  @Override
  public long startPdxInstanceDeserialization() {
    return System.nanoTime();
  }

  @Override
  public void endPdxInstanceDeserialization(long start) {
    if (enableClockStats) {
      stats.incLong(pdxInstanceDeserializationTimeId, System.nanoTime() - start);
    }
    stats.incInt(pdxInstanceDeserializationsId, 1);
  }

  @Override
  public void incPdxSerialization(int bytes) {
    stats.incInt(pdxSerializationsId, 1);
    stats.incLong(pdxSerializedBytesId, bytes);
  }

  @Override
  public void incPdxDeserialization(int bytes) {
    stats.incInt(pdxDeserializationsId, 1);
    stats.incLong(pdxDeserializedBytesId, bytes);
  }

  @Override
  public void incPdxInstanceCreations() {
    stats.incInt(pdxInstanceCreationsId, 1);
  }

  @Override
  public long startDeserialization() {
    return System.nanoTime();
  }

  @Override
  public void endDeserialization(long start, int bytes) {
    if (enableClockStats) {
      stats.incLong(deserializationTimeId, System.nanoTime() - start);
    }
    stats.incInt(deserializationsId, 1);
    stats.incLong(deserializedBytesId, bytes);
  }

  @Override
  public long startMsgSerialization() {
    return System.nanoTime();
  }

  @Override
  public void endMsgSerialization(long start) {
    if (enableClockStats) {
      stats.incLong(msgSerializationTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public long startUDPMsgEncryption() {
    return System.nanoTime();
  }

  @Override
  public void endUDPMsgEncryption(long start) {
    if (enableClockStats) {
      stats.incLong(udpMsgEncryptionTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public long startMsgDeserialization() {
    return System.nanoTime();
  }

  @Override
  public void endMsgDeserialization(long start) {
    if (enableClockStats) {
      stats.incLong(msgDeserializationTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public long startUDPMsgDecryption() {
    return System.nanoTime();
  }

  @Override
  public void endUDPMsgDecryption(long start) {
    if (enableClockStats) {
      stats.incLong(udpMsgDecryptionTimeId, System.nanoTime() - start);
    }
  }

  /**
   * @return the timestamp that marks the start of the operation
   */
  @Override
  public long startReplyWait() {
    stats.incInt(replyWaitsInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endReplyWait(long startNanos, long initTime) {
    if (enableClockStats) {
      stats.incLong(replyWaitTimeId, System.nanoTime() - startNanos);
      // this.replyWaitHistogram.endOp(delta);
    }
    if (initTime != 0) {
      long mswait = System.currentTimeMillis() - initTime;
      if (mswait > getReplyWaitMaxTime()) {
        stats.setLong(replyWaitMaxTimeId, mswait);
      }
    }
    stats.incInt(replyWaitsInProgressId, -1);
    stats.incInt(replyWaitsCompletedId, 1);

    Breadcrumbs.setSendSide(null); // clear any recipient breadcrumbs set by the message
    Breadcrumbs.setProblem(null); // clear out reply-wait errors
  }

  @Override
  public void incReplyTimeouts() {
    stats.incLong(replyTimeoutsId, 1L);
  }

  @Override
  public long getReplyTimeouts() {
    return stats.getLong(replyTimeoutsId);
  }

  @Override
  public void incReceivers() {
    stats.incInt(receiverConnectionsId, 1);
  }

  @Override
  public void decReceivers() {
    stats.incInt(receiverConnectionsId, -1);
  }

  @Override
  public void incFailedAccept() {
    stats.incInt(failedAcceptsId, 1);
  }

  @Override
  public void incFailedConnect() {
    stats.incInt(failedConnectsId, 1);
  }

  @Override
  public void incReconnectAttempts() {
    stats.incInt(reconnectAttemptsId, 1);
  }

  @Override
  public void incLostLease() {
    stats.incInt(lostConnectionLeaseId, 1);
  }

  @Override
  public void incSenders(boolean shared, boolean preserveOrder) {
    if (shared) {
      if (preserveOrder) {
        stats.incInt(sharedOrderedSenderConnectionsId, 1);
      } else {
        stats.incInt(sharedUnorderedSenderConnectionsId, 1);
      }
    } else {
      if (preserveOrder) {
        stats.incInt(threadOrderedSenderConnectionsId, 1);
      } else {
        stats.incInt(threadUnorderedSenderConnectionsId, 1);
      }
    }
  }

  @Override
  public int getSendersSU() {
    return stats.getInt(sharedUnorderedSenderConnectionsId);
  }

  @Override
  public void decSenders(boolean shared, boolean preserveOrder) {
    if (shared) {
      if (preserveOrder) {
        stats.incInt(sharedOrderedSenderConnectionsId, -1);
      } else {
        stats.incInt(sharedUnorderedSenderConnectionsId, -1);
      }
    } else {
      if (preserveOrder) {
        stats.incInt(threadOrderedSenderConnectionsId, -1);
      } else {
        stats.incInt(threadUnorderedSenderConnectionsId, -1);
      }
    }
  }

  @Override
  public int getAsyncSocketWritesInProgress() {
    return stats.getInt(asyncSocketWritesInProgressId);
  }

  @Override
  public int getAsyncSocketWrites() {
    return stats.getInt(asyncSocketWritesId);
  }

  @Override
  public int getAsyncSocketWriteRetries() {
    return stats.getInt(asyncSocketWriteRetriesId);
  }

  @Override
  public long getAsyncSocketWriteBytes() {
    return stats.getLong(asyncSocketWriteBytesId);
  }

  @Override
  public long getAsyncSocketWriteTime() {
    return stats.getLong(asyncSocketWriteTimeId);
  }

  @Override
  public long getAsyncQueueAddTime() {
    return stats.getLong(asyncQueueAddTimeId);
  }

  @Override
  public void incAsyncQueueAddTime(long inc) {
    if (enableClockStats) {
      stats.incLong(asyncQueueAddTimeId, inc);
    }
  }

  @Override
  public long getAsyncQueueRemoveTime() {
    return stats.getLong(asyncQueueRemoveTimeId);
  }

  @Override
  public void incAsyncQueueRemoveTime(long inc) {
    if (enableClockStats) {
      stats.incLong(asyncQueueRemoveTimeId, inc);
    }
  }

  @Override
  public int getAsyncQueues() {
    return stats.getInt(asyncQueuesId);
  }

  @Override
  public void incAsyncQueues(int inc) {
    stats.incInt(asyncQueuesId, inc);
  }

  @Override
  public int getAsyncQueueFlushesInProgress() {
    return stats.getInt(asyncQueueFlushesInProgressId);
  }

  @Override
  public int getAsyncQueueFlushesCompleted() {
    return stats.getInt(asyncQueueFlushesCompletedId);
  }

  @Override
  public long getAsyncQueueFlushTime() {
    return stats.getLong(asyncQueueFlushTimeId);
  }

  @Override
  public long startAsyncQueueFlush() {
    stats.incInt(asyncQueueFlushesInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endAsyncQueueFlush(long start) {
    stats.incInt(asyncQueueFlushesInProgressId, -1);
    stats.incInt(asyncQueueFlushesCompletedId, 1);
    if (enableClockStats) {
      stats.incLong(asyncQueueFlushTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public int getAsyncQueueTimeouts() {
    return stats.getInt(asyncQueueTimeoutExceededId);
  }

  @Override
  public void incAsyncQueueTimeouts(int inc) {
    stats.incInt(asyncQueueTimeoutExceededId, inc);
  }

  @Override
  public int getAsyncQueueSizeExceeded() {
    return stats.getInt(asyncQueueSizeExceededId);
  }

  @Override
  public void incAsyncQueueSizeExceeded(int inc) {
    stats.incInt(asyncQueueSizeExceededId, inc);
  }

  @Override
  public int getAsyncDistributionTimeoutExceeded() {
    return stats.getInt(asyncDistributionTimeoutExceededId);
  }

  @Override
  public void incAsyncDistributionTimeoutExceeded() {
    stats.incInt(asyncDistributionTimeoutExceededId, 1);
  }

  @Override
  public long getAsyncQueueSize() {
    return stats.getLong(asyncQueueSizeId);
  }

  @Override
  public void incAsyncQueueSize(long inc) {
    stats.incLong(asyncQueueSizeId, inc);
  }

  @Override
  public long getAsyncQueuedMsgs() {
    return stats.getLong(asyncQueuedMsgsId);
  }

  @Override
  public void incAsyncQueuedMsgs() {
    stats.incLong(asyncQueuedMsgsId, 1);
  }

  @Override
  public long getAsyncDequeuedMsgs() {
    return stats.getLong(asyncDequeuedMsgsId);
  }

  @Override
  public void incAsyncDequeuedMsgs() {
    stats.incLong(asyncDequeuedMsgsId, 1);
  }

  @Override
  public long getAsyncConflatedMsgs() {
    return stats.getLong(asyncConflatedMsgsId);
  }

  @Override
  public void incAsyncConflatedMsgs() {
    stats.incLong(asyncConflatedMsgsId, 1);
  }

  @Override
  public int getAsyncThreads() {
    return stats.getInt(asyncThreadsId);
  }

  @Override
  public void incAsyncThreads(int inc) {
    stats.incInt(asyncThreadsId, inc);
  }

  @Override
  public int getAsyncThreadInProgress() {
    return stats.getInt(asyncThreadInProgressId);
  }

  @Override
  public int getAsyncThreadCompleted() {
    return stats.getInt(asyncThreadCompletedId);
  }

  @Override
  public long getAsyncThreadTime() {
    return stats.getLong(asyncThreadTimeId);
  }

  @Override
  public long startAsyncThread() {
    stats.incInt(asyncThreadInProgressId, 1);
    return System.nanoTime();
  }

  @Override
  public void endAsyncThread(long start) {
    stats.incInt(asyncThreadInProgressId, -1);
    stats.incInt(asyncThreadCompletedId, 1);
    if (enableClockStats) {
      stats.incLong(asyncThreadTimeId, System.nanoTime() - start);
    }
  }

  /**
   * Returns a helper object so that the overflow queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public ThrottledQueueStatHelper getOverflowQueueHelper() {
    return new ThrottledQueueStatHelper() {
      public void incThrottleCount() {
        incOverflowQueueThrottleCount(1);
      }

      public void throttleTime(long nanos) {
        incOverflowQueueThrottleTime(nanos);
      }

      public void add() {
        incOverflowQueueSize(1);
      }

      public void remove() {
        incOverflowQueueSize(-1);
      }

      public void remove(int count) {
        incOverflowQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the waiting queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public QueueStatHelper getWaitingQueueHelper() {
    return new QueueStatHelper() {
      public void add() {
        incWaitingQueueSize(1);
      }

      public void remove() {
        incWaitingQueueSize(-1);
      }

      public void remove(int count) {
        incWaitingQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the high priority queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public ThrottledQueueStatHelper getHighPriorityQueueHelper() {
    return new ThrottledQueueStatHelper() {
      public void incThrottleCount() {
        incHighPriorityQueueThrottleCount(1);
      }

      public void throttleTime(long nanos) {
        incHighPriorityQueueThrottleTime(nanos);
      }

      public void add() {
        incHighPriorityQueueSize(1);
      }

      public void remove() {
        incHighPriorityQueueSize(-1);
      }

      public void remove(int count) {
        incHighPriorityQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the partitioned region queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 5.0
   */
  @Override
  public ThrottledQueueStatHelper getPartitionedRegionQueueHelper() {
    return new ThrottledQueueStatHelper() {
      public void incThrottleCount() {
        incPartitionedRegionQueueThrottleCount(1);
      }

      public void throttleTime(long nanos) {
        incPartitionedRegionQueueThrottleTime(nanos);
      }

      public void add() {
        incPartitionedRegionQueueSize(1);
      }

      public void remove() {
        incPartitionedRegionQueueSize(-1);
      }

      public void remove(int count) {
        incPartitionedRegionQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the partitioned region pool can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 5.0.2
   */
  @Override
  public PoolStatHelper getPartitionedRegionPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incPartitionedRegionThreadJobs(1);
      }

      public void endJob() {
        incPartitionedRegionThreadJobs(-1);
      }
    };
  }

  /**
   * Returns a helper object so that the function execution queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 6.0
   */
  @Override
  public ThrottledQueueStatHelper getFunctionExecutionQueueHelper() {
    return new ThrottledQueueStatHelper() {
      public void incThrottleCount() {
        incFunctionExecutionQueueThrottleCount(1);
      }

      public void throttleTime(long nanos) {
        incFunctionExecutionQueueThrottleTime(nanos);
      }

      public void add() {
        incFunctionExecutionQueueSize(1);
      }

      public void remove() {
        incFunctionExecutionQueueSize(-1);
      }

      public void remove(int count) {
        incFunctionExecutionQueueSize(-count);
      }
    };
  }

  /**
   * Returns a helper object so that the function execution pool can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 6.0
   */
  @Override
  public PoolStatHelper getFunctionExecutionPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incFunctionExecutionThreadJobs(1);
      }

      public void endJob() {
        incFunctionExecutionThreadJobs(-1);
      }
    };
  }

  /**
   * Returns a helper object so that the serial queue can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public ThrottledMemQueueStatHelper getSerialQueueHelper() {
    return new ThrottledMemQueueStatHelper() {
      public void incThrottleCount() {
        incSerialQueueThrottleCount(1);
      }

      public void throttleTime(long nanos) {
        incSerialQueueThrottleTime(nanos);
      }

      public void add() {
        incSerialQueueSize(1);
      }

      public void remove() {
        incSerialQueueSize(-1);
      }

      public void remove(int count) {
        incSerialQueueSize(-count);
      }

      public void addMem(int amount) {
        incSerialQueueBytes(amount);
      }

      public void removeMem(int amount) {
        incSerialQueueBytes(amount * (-1));
      }
    };
  }

  /**
   * Returns a helper object so that the normal pool can record its stats to the proper distribution
   * stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public PoolStatHelper getNormalPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incNormalPoolThreadJobs(1);
      }

      public void endJob() {
        incNormalPoolThreadJobs(-1);
      }
    };
  }

  /**
   * Returns a helper object so that the waiting pool can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public PoolStatHelper getWaitingPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incWaitingPoolThreadJobs(1);
      }

      public void endJob() {
        incWaitingPoolThreadJobs(-1);
      }
    };
  }

  /**
   * Returns a helper object so that the highPriority pool can record its stats to the proper
   * distribution stats.
   *
   * @since GemFire 3.5
   */
  @Override
  public PoolStatHelper getHighPriorityPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incHighPriorityThreadJobs(1);
      }

      public void endJob() {
        incHighPriorityThreadJobs(-1);
      }
    };
  }

  @Override
  public void incBatchSendTime(long start) {
    if (enableClockStats) {
      stats.incLong(batchSendTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public void incBatchCopyTime(long start) {
    if (enableClockStats) {
      stats.incLong(batchCopyTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public void incBatchWaitTime(long start) {
    if (enableClockStats) {
      stats.incLong(batchWaitTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public void incBatchFlushTime(long start) {
    if (enableClockStats) {
      stats.incLong(batchFlushTimeId, System.nanoTime() - start);
    }
  }

  @Override
  public void incUcastRetransmits() {
    stats.incInt(ucastRetransmitsId, 1);
  }

  @Override
  public void incMcastRetransmits() {
    stats.incInt(mcastRetransmitsId, 1);
  }

  @Override
  public void incMcastRetransmitRequests() {
    stats.incInt(mcastRetransmitRequestsId, 1);
  }

  @Override
  public int getMcastRetransmits() {
    return stats.getInt(mcastRetransmitsId);
  }

  @Override
  public void incThreadOwnedReceivers(long value, int dominoCount) {
    if (dominoCount < 2) {
      stats.incLong(threadOwnedReceiversId, value);
    } else {
      stats.incLong(threadOwnedReceiversId2, value);
    }
  }

  /**
   * @since GemFire 5.0.2.4
   */
  @Override
  public void incReceiverBufferSize(int inc, boolean direct) {
    if (direct) {
      stats.incLong(receiverDirectBufferSizeId, inc);
    } else {
      stats.incLong(receiverHeapBufferSizeId, inc);
    }
  }

  /**
   * @since GemFire 5.0.2.4
   */
  @Override
  public void incSenderBufferSize(int inc, boolean direct) {
    if (direct) {
      stats.incLong(senderDirectBufferSizeId, inc);
    } else {
      stats.incLong(senderHeapBufferSizeId, inc);
    }
  }

  @Override
  public void incMessagesBeingReceived(boolean newMsg, int bytes) {
    if (newMsg) {
      stats.incInt(messagesBeingReceivedId, 1);
    }
    stats.incLong(messageBytesBeingReceivedId, bytes);
  }

  @Override
  public void decMessagesBeingReceived(int bytes) {
    stats.incInt(messagesBeingReceivedId, -1);
    stats.incLong(messageBytesBeingReceivedId, -bytes);
  }

  @Override
  public void incSerialThreadStarts() {
    stats.incLong(serialThreadStartsId, 1);
  }

  @Override
  public void incViewThreadStarts() {
    stats.incLong(viewThreadStartsId, 1);
  }

  @Override
  public void incProcessingThreadStarts() {
    stats.incLong(processingThreadStartsId, 1);
  }

  @Override
  public void incHighPriorityThreadStarts() {
    stats.incLong(highPriorityThreadStartsId, 1);
  }

  @Override
  public void incWaitingThreadStarts() {
    stats.incLong(waitingThreadStartsId, 1);
  }

  @Override
  public void incPartitionedRegionThreadStarts() {
    stats.incLong(partitionedRegionThreadStartsId, 1);
  }

  @Override
  public void incFunctionExecutionThreadStarts() {
    stats.incLong(functionExecutionThreadStartsId, 1);
  }

  @Override
  public void incSerialPooledThreadStarts() {
    stats.incLong(serialPooledThreadStartsId, 1);
  }

  @Override
  public void incReplyHandOffTime(long start) {
    if (enableClockStats) {
      long delta = System.nanoTime() - start;
      stats.incLong(replyHandoffTimeId, delta);
      // this.replyHandoffHistogram.endOp(delta);
    }
  }

  @Override
  public void incPartitionedRegionThreadJobs(int i) {
    this.stats.incInt(partitionedRegionThreadJobsId, i);
  }

  @Override
  public void incFunctionExecutionThreadJobs(int i) {
    this.stats.incInt(functionExecutionThreadJobsId, i);
  }

  @Override
  public void incNumViewThreads(int threads) {
    this.stats.incInt(viewThreadsId, threads);
  }

  @Override
  public PoolStatHelper getSerialProcessorHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incNumSerialThreadJobs(1);
        if (logger.isTraceEnabled()) {
          logger.trace("[DM.SerialQueuedExecutor.execute] numSerialThreads={}",
              getNumSerialThreads());
        }
      }

      public void endJob() {
        incNumSerialThreadJobs(-1);
      }
    };
  }

  @Override
  public void incNumSerialThreadJobs(int jobs) {
    this.stats.incInt(serialThreadJobsId, jobs);
  }

  @Override
  public PoolStatHelper getViewProcessorHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incViewProcessorThreadJobs(1);
        if (logger.isTraceEnabled()) {
          logger.trace("[DM.SerialQueuedExecutor.execute] numViewThreads={}", getNumViewThreads());
        }
      }

      public void endJob() {
        incViewProcessorThreadJobs(-1);
      }
    };
  }

  @Override
  public int getNumViewThreads() {
    return this.stats.getInt(viewThreadsId);
  }

  @Override
  public void incViewProcessorThreadJobs(int jobs) {
    this.stats.incInt(viewProcessorThreadJobsId, jobs);
  }

  @Override
  public PoolStatHelper getSerialPooledProcessorHelper() {
    return new PoolStatHelper() {
      public void startJob() {
        incSerialPooledProcessorThreadJobs(1);
      }

      public void endJob() {
        incSerialPooledProcessorThreadJobs(-1);
      }
    };
  }

  @Override
  public void incSerialPooledProcessorThreadJobs(int jobs) {
    this.stats.incInt(serialPooledThreadJobsId, jobs);
  }

  @Override
  public void incNormalPoolThreadJobs(int jobs) {
    this.stats.incInt(pooledMessageThreadJobsId, jobs);
  }

  @Override
  public void incHighPriorityThreadJobs(int jobs) {
    this.stats.incInt(highPriorityThreadJobsId, jobs);
  }

  @Override
  public void incWaitingPoolThreadJobs(int jobs) {
    this.stats.incInt(waitingPoolThreadJobsId, jobs);
  }

  @Override
  public int getElders() {
    return this.stats.getInt(eldersId);
  }

  @Override
  public void incElders(int val) {
    this.stats.incInt(eldersId, val);
  }

  @Override
  public int getInitialImageMessagesInFlight() {
    return this.stats.getInt(initialImageMessagesInFlightId);
  }

  @Override
  public void incInitialImageMessagesInFlight(int val) {
    this.stats.incInt(initialImageMessagesInFlightId, val);
  }

  @Override
  public int getInitialImageRequestsInProgress() {
    return this.stats.getInt(initialImageRequestsInProgressId);
  }

  @Override
  public void incInitialImageRequestsInProgress(int val) {
    this.stats.incInt(initialImageRequestsInProgressId, val);
  }

  @Override
  public Statistics getStats() {
    return stats;
  }

  // For GMSHealthMonitor
  @Override
  public long getHeartbeatRequestsSent() {
    return this.stats.getLong(heartbeatRequestsSentId);
  }

  @Override
  public void incHeartbeatRequestsSent() {
    this.stats.incLong(heartbeatRequestsSentId, 1L);
  }

  @Override
  public long getHeartbeatRequestsReceived() {
    return this.stats.getLong(heartbeatRequestsReceivedId);
  }

  @Override
  public void incHeartbeatRequestsReceived() {
    this.stats.incLong(heartbeatRequestsReceivedId, 1L);
  }

  @Override
  public long getHeartbeatsSent() {
    return this.stats.getLong(heartbeatsSentId);
  }

  @Override
  public void incHeartbeatsSent() {
    this.stats.incLong(heartbeatsSentId, 1L);
  }

  @Override
  public long getHeartbeatsReceived() {
    return this.stats.getLong(heartbeatsReceivedId);
  }

  @Override
  public void incHeartbeatsReceived() {
    this.stats.incLong(heartbeatsReceivedId, 1L);
  }

  @Override
  public long getSuspectsSent() {
    return this.stats.getLong(suspectsSentId);
  }

  @Override
  public void incSuspectsSent() {
    this.stats.incLong(suspectsSentId, 1L);
  }

  @Override
  public long getSuspectsReceived() {
    return this.stats.getLong(suspectsReceivedId);
  }

  @Override
  public void incSuspectsReceived() {
    this.stats.incLong(suspectsReceivedId, 1L);
  }

  @Override
  public long getFinalCheckRequestsSent() {
    return this.stats.getLong(finalCheckRequestsSentId);
  }

  @Override
  public void incFinalCheckRequestsSent() {
    this.stats.incLong(finalCheckRequestsSentId, 1L);
  }

  @Override
  public long getFinalCheckRequestsReceived() {
    return this.stats.getLong(finalCheckRequestsReceivedId);
  }

  @Override
  public void incFinalCheckRequestsReceived() {
    this.stats.incLong(finalCheckRequestsReceivedId, 1L);
  }

  @Override
  public long getFinalCheckResponsesSent() {
    return this.stats.getLong(finalCheckResponsesSentId);
  }

  @Override
  public void incFinalCheckResponsesSent() {
    this.stats.incLong(finalCheckResponsesSentId, 1L);
  }

  @Override
  public long getFinalCheckResponsesReceived() {
    return this.stats.getLong(finalCheckResponsesReceivedId);
  }

  @Override
  public void incFinalCheckResponsesReceived() {
    this.stats.incLong(finalCheckResponsesReceivedId, 1L);
  }

  ///
  @Override
  public long getTcpFinalCheckRequestsSent() {
    return this.stats.getLong(tcpFinalCheckRequestsSentId);
  }

  @Override
  public void incTcpFinalCheckRequestsSent() {
    this.stats.incLong(tcpFinalCheckRequestsSentId, 1L);
  }

  @Override
  public long getTcpFinalCheckRequestsReceived() {
    return this.stats.getLong(tcpFinalCheckRequestsReceivedId);
  }

  @Override
  public void incTcpFinalCheckRequestsReceived() {
    this.stats.incLong(tcpFinalCheckRequestsReceivedId, 1L);
  }

  @Override
  public long getTcpFinalCheckResponsesSent() {
    return this.stats.getLong(tcpFinalCheckResponsesSentId);
  }

  @Override
  public void incTcpFinalCheckResponsesSent() {
    this.stats.incLong(tcpFinalCheckResponsesSentId, 1L);
  }

  @Override
  public long getTcpFinalCheckResponsesReceived() {
    return this.stats.getLong(tcpFinalCheckResponsesReceivedId);
  }

  @Override
  public void incTcpFinalCheckResponsesReceived() {
    this.stats.incLong(tcpFinalCheckResponsesReceivedId, 1L);
  }

  ///
  @Override
  public long getUdpFinalCheckRequestsSent() {
    return this.stats.getLong(udpFinalCheckRequestsSentId);
  }

  @Override
  public void incUdpFinalCheckRequestsSent() {
    this.stats.incLong(udpFinalCheckRequestsSentId, 1L);
  }

  // UDP final check is implemented using HeartbeatRequestMessage and HeartbeatMessage
  // So the following code is commented out
  // public long getUdpFinalCheckRequestsReceived() {
  // return this.stats.getLong(udpFinalCheckRequestsReceivedId);
  // }
  //
  // public void incUdpFinalCheckRequestsReceived() {
  // this.stats.incLong(udpFinalCheckRequestsReceivedId, 1L);
  // }
  //
  // public long getUdpFinalCheckResponsesSent() {
  // return this.stats.getLong(udpFinalCheckResponsesSentId);
  // }
  //
  // public void incUdpFinalCheckResponsesSent() {
  // this.stats.incLong(udpFinalCheckResponsesSentId, 1L);
  // }

  @Override
  public long getUdpFinalCheckResponsesReceived() {
    return this.stats.getLong(udpFinalCheckResponsesReceivedId);
  }

  @Override
  public void incUdpFinalCheckResponsesReceived() {
    this.stats.incLong(udpFinalCheckResponsesReceivedId, 1L);
  }
}
