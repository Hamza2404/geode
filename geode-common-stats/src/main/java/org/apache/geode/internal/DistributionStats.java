package org.apache.geode.distributed.internal;

import org.apache.geode.statistics.Statistics;

public interface DistributionStats extends DMStats {
  default long getStatTime() {
    return System.nanoTime();
  }

  void close();

  long getSentMessages();

  void incTOSentMsg();

  long getSentCommitMessages();

  long getCommitWaits();

  void incSentMessages(long messages);

  void incSentCommitMessages(long messages);

  void incCommitWaits();

  long getSentMessagesTime();

  void incSentMessagesTime(long nanos);

  long getSentMessagesMaxTime();

  long getBroadcastMessages();

  void incBroadcastMessages(long messages);

  long getBroadcastMessagesTime();

  void incBroadcastMessagesTime(long nanos);

  long getReceivedMessages();

  void incReceivedMessages(long messages);

  long getReceivedBytes();

  void incReceivedBytes(long bytes);

  void incSentBytes(long bytes);

  long getProcessedMessages();

  void incProcessedMessages(long messages);

  long getProcessedMessagesTime();

  void incProcessedMessagesTime(long start);

  long getMessageProcessingScheduleTime();

  void incMessageProcessingScheduleTime(long elapsed);

  int getOverflowQueueSize();

  void incOverflowQueueSize(int messages);

  void incWaitingQueueSize(int messages);

  void incOverflowQueueThrottleCount(int delays);

  void incOverflowQueueThrottleTime(long nanos);

  void incHighPriorityQueueSize(int messages);

  void incHighPriorityQueueThrottleCount(int delays);

  void incHighPriorityQueueThrottleTime(long nanos);

  void incPartitionedRegionQueueSize(int messages);

  void incPartitionedRegionQueueThrottleCount(int delays);

  void incPartitionedRegionQueueThrottleTime(long nanos);

  void incFunctionExecutionQueueSize(int messages);

  void incFunctionExecutionQueueThrottleCount(int delays);

  void incFunctionExecutionQueueThrottleTime(long nanos);

  void incSerialQueueSize(int messages);

  void incSerialQueueBytes(int amount);

  int getSerialQueueBytes();

  void incSerialPooledThread();

  void incSerialQueueThrottleCount(int delays);

  void incSerialQueueThrottleTime(long nanos);

  int getNumProcessingThreads();

  void incNumProcessingThreads(int threads);

  int getNumSerialThreads();

  void incNumSerialThreads(int threads);

  void incWaitingThreads(int threads);

  void incHighPriorityThreads(int threads);

  void incPartitionedRegionThreads(int threads);

  void incFunctionExecutionThreads(int threads);

  void incMessageChannelTime(long delta);

  void incUDPDispatchRequestTime(long delta);

  long getUDPDispatchRequestTime();

  long getReplyMessageTime();

  void incReplyMessageTime(long val);

  long getDistributeMessageTime();

  void incDistributeMessageTime(long val);

  int getNodes();

  void setNodes(int val);

  void incNodes(int val);

  int getReplyWaitsInProgress();

  int getReplyWaitsCompleted();

  long getReplyWaitTime();

  long getReplyWaitMaxTime();

  long startSocketWrite(boolean sync);

  void endSocketWrite(boolean sync, long start, int bytesWritten, int retries);

  long startSocketLock();

  void endSocketLock(long start);

  long startBufferAcquire();

  void endBufferAcquire(long start);

  void incUcastWriteBytes(int bytesWritten);

  void incMcastWriteBytes(int bytesWritten);

  int getMcastWrites();

  int getMcastReads();

  long getUDPMsgDecryptionTime();

  long getUDPMsgEncryptionTime();

  void incMcastReadBytes(int amount);

  void incUcastReadBytes(int amount);

  long startSerialization();

  void endSerialization(long start, int bytes);

  long startPdxInstanceDeserialization();

  void endPdxInstanceDeserialization(long start);

  void incPdxSerialization(int bytes);

  void incPdxDeserialization(int bytes);

  void incPdxInstanceCreations();

  long startDeserialization();

  void endDeserialization(long start, int bytes);

  long startMsgSerialization();

  void endMsgSerialization(long start);

  long startUDPMsgEncryption();

  void endUDPMsgEncryption(long start);

  long startMsgDeserialization();

  void endMsgDeserialization(long start);

  long startUDPMsgDecryption();

  void endUDPMsgDecryption(long start);

  long startReplyWait();

  void endReplyWait(long startNanos, long initTime);

  void incReplyTimeouts();

  long getReplyTimeouts();

  void incReceivers();

  void decReceivers();

  void incFailedAccept();

  void incFailedConnect();

  void incReconnectAttempts();

  void incLostLease();

  void incSenders(boolean shared, boolean preserveOrder);

  int getSendersSU();

  void decSenders(boolean shared, boolean preserveOrder);

  int getAsyncSocketWritesInProgress();

  int getAsyncSocketWrites();

  int getAsyncSocketWriteRetries();

  long getAsyncSocketWriteBytes();

  long getAsyncSocketWriteTime();

  long getAsyncQueueAddTime();

  void incAsyncQueueAddTime(long inc);

  long getAsyncQueueRemoveTime();

  void incAsyncQueueRemoveTime(long inc);

  int getAsyncQueues();

  void incAsyncQueues(int inc);

  int getAsyncQueueFlushesInProgress();

  int getAsyncQueueFlushesCompleted();

  long getAsyncQueueFlushTime();

  long startAsyncQueueFlush();

  void endAsyncQueueFlush(long start);

  int getAsyncQueueTimeouts();

  void incAsyncQueueTimeouts(int inc);

  int getAsyncQueueSizeExceeded();

  void incAsyncQueueSizeExceeded(int inc);

  int getAsyncDistributionTimeoutExceeded();

  void incAsyncDistributionTimeoutExceeded();

  long getAsyncQueueSize();

  void incAsyncQueueSize(long inc);

  long getAsyncQueuedMsgs();

  void incAsyncQueuedMsgs();

  long getAsyncDequeuedMsgs();

  void incAsyncDequeuedMsgs();

  long getAsyncConflatedMsgs();

  void incAsyncConflatedMsgs();

  int getAsyncThreads();

  void incAsyncThreads(int inc);

  int getAsyncThreadInProgress();

  int getAsyncThreadCompleted();

  long getAsyncThreadTime();

  long startAsyncThread();

  void endAsyncThread(long start);

  ThrottledQueueStatHelper getOverflowQueueHelper();

  QueueStatHelper getWaitingQueueHelper();

  ThrottledQueueStatHelper getHighPriorityQueueHelper();

  ThrottledQueueStatHelper getPartitionedRegionQueueHelper();

  PoolStatHelper getPartitionedRegionPoolHelper();

  ThrottledQueueStatHelper getFunctionExecutionQueueHelper();

  PoolStatHelper getFunctionExecutionPoolHelper();

  ThrottledMemQueueStatHelper getSerialQueueHelper();

  PoolStatHelper getNormalPoolHelper();

  PoolStatHelper getWaitingPoolHelper();

  PoolStatHelper getHighPriorityPoolHelper();

  void incBatchSendTime(long start);

  void incBatchCopyTime(long start);

  void incBatchWaitTime(long start);

  void incBatchFlushTime(long start);

  void incUcastRetransmits();

  void incMcastRetransmits();

  void incMcastRetransmitRequests();

  int getMcastRetransmits();

  void incThreadOwnedReceivers(long value, int dominoCount);

  void incReceiverBufferSize(int inc, boolean direct);

  abstract void incSenderBufferSize(int inc, boolean direct);

  void incMessagesBeingReceived(boolean newMsg, int bytes);

  void decMessagesBeingReceived(int bytes);

  void incSerialThreadStarts();

  void incViewThreadStarts();

  void incProcessingThreadStarts();

  void incHighPriorityThreadStarts();

  void incWaitingThreadStarts();

  void incPartitionedRegionThreadStarts();

  void incFunctionExecutionThreadStarts();

  void incSerialPooledThreadStarts();

  void incReplyHandOffTime(long start);

  void incPartitionedRegionThreadJobs(int i);

  void incFunctionExecutionThreadJobs(int i);

  void incNumViewThreads(int threads);

  PoolStatHelper getSerialProcessorHelper();

  void incNumSerialThreadJobs(int jobs);

  PoolStatHelper getViewProcessorHelper();

  int getNumViewThreads();

  void incViewProcessorThreadJobs(int jobs);

  PoolStatHelper getSerialPooledProcessorHelper();

  void incSerialPooledProcessorThreadJobs(int jobs);

  void incNormalPoolThreadJobs(int jobs);

  void incHighPriorityThreadJobs(int jobs);

  void incWaitingPoolThreadJobs(int jobs);

  int getElders();

  void incElders(int val);

  int getInitialImageMessagesInFlight();

  void incInitialImageMessagesInFlight(int val);

  int getInitialImageRequestsInProgress();

  void incInitialImageRequestsInProgress(int val);

  Statistics getStats();

  // For GMSHealthMonitor
  long getHeartbeatRequestsSent();

  void incHeartbeatRequestsSent();

  long getHeartbeatRequestsReceived();

  void incHeartbeatRequestsReceived();

  long getHeartbeatsSent();

  void incHeartbeatsSent();

  long getHeartbeatsReceived();

  void incHeartbeatsReceived();

  long getSuspectsSent();

  void incSuspectsSent();

  long getSuspectsReceived();

  void incSuspectsReceived();

  long getFinalCheckRequestsSent();

  void incFinalCheckRequestsSent();

  long getFinalCheckRequestsReceived();

  void incFinalCheckRequestsReceived();

  long getFinalCheckResponsesSent();

  void incFinalCheckResponsesSent();

  long getFinalCheckResponsesReceived();

  void incFinalCheckResponsesReceived();

  ///
  long getTcpFinalCheckRequestsSent();

  void incTcpFinalCheckRequestsSent();

  long getTcpFinalCheckRequestsReceived();

  void incTcpFinalCheckRequestsReceived();

  long getTcpFinalCheckResponsesSent();

  void incTcpFinalCheckResponsesSent();

  long getTcpFinalCheckResponsesReceived();

  void incTcpFinalCheckResponsesReceived();

  ///
  long getUdpFinalCheckRequestsSent();

  void incUdpFinalCheckRequestsSent();

  long getUdpFinalCheckResponsesReceived();

  void incUdpFinalCheckResponsesReceived();
}
