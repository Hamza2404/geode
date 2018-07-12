package org.apache.geode.cache.client.internal;

import org.apache.geode.internal.cache.PoolStats;
import org.apache.geode.internal.cache.tier.sockets.MessageStats;
import org.apache.geode.statistics.StatisticsFactory;

public interface ConnectionStats extends MessageStats {

  long startGet();

  void endGetSend(long startTime, boolean failed);

  void endGet(long startTime, boolean timedOut, boolean failed);

  int getGets();

  long getGetDuration();

  long startPut();

  void endPutSend(long startTime, boolean failed);

  void endPut(long startTime, boolean timedOut, boolean failed);

  int getPuts();

  long getPutDuration();

  long startDestroy();

  void endDestroySend(long startTime, boolean failed);

  void endDestroy(long startTime, boolean timedOut, boolean failed);

  long startDestroyRegion();

  void endDestroyRegionSend(long startTime, boolean failed);

  void endDestroyRegion(long startTime, boolean timedOut, boolean failed);

  long startClear();

  void endClearSend(long startTime, boolean failed);

  void endClear(long startTime, boolean timedOut, boolean failed);

  long startContainsKey();

  void endContainsKeySend(long startTime, boolean failed);

  void endContainsKey(long startTime, boolean timedOut, boolean failed);

  long startKeySet();

  void endKeySetSend(long startTime, boolean failed);

  void endKeySet(long startTime, boolean timedOut, boolean failed);

  long startRegisterInterest();

  void endRegisterInterestSend(long startTime, boolean failed);

  void endRegisterInterest(long startTime, boolean timedOut, boolean failed);

  long startUnregisterInterest();

  void endUnregisterInterestSend(long startTime, boolean failed);

  void endUnregisterInterest(long startTime, boolean timedOut, boolean failed);

  long startQuery();

  void endQuerySend(long startTime, boolean failed);

  void endQuery(long startTime, boolean timedOut, boolean failed);

  long startCreateCQ();

  void endCreateCQSend(long startTime, boolean failed);

  void endCreateCQ(long startTime, boolean timedOut, boolean failed);

  long startStopCQ();

  void endStopCQSend(long startTime, boolean failed);

  void endStopCQ(long startTime, boolean timedOut, boolean failed);

  long startCloseCQ();

  void endCloseCQSend(long startTime, boolean failed);

  void endCloseCQ(long startTime, boolean timedOut, boolean failed);

  long startGetDurableCQs();

  void endGetDurableCQsSend(long startTime, boolean failed);

  void endGetDurableCQs(long startTime, boolean timedOut, boolean failed);

  long startGatewayBatch();

  void endGatewayBatchSend(long startTime, boolean failed);

  void endGatewayBatch(long startTime, boolean timedOut, boolean failed);

  long startReadyForEvents();

  void endReadyForEventsSend(long startTime, boolean failed);

  void endReadyForEvents(long startTime, boolean timedOut, boolean failed);

  long startMakePrimary();

  void endMakePrimarySend(long startTime, boolean failed);

  void endMakePrimary(long startTime, boolean timedOut, boolean failed);

  long startCloseCon();

  void endCloseConSend(long startTime, boolean failed);

  void endCloseCon(long startTime, boolean timedOut, boolean failed);

  long startPrimaryAck();

  void endPrimaryAckSend(long startTime, boolean failed);

  void endPrimaryAck(long startTime, boolean timedOut, boolean failed);

  long startPing();

  void endPingSend(long startTime, boolean failed);

  void endPing(long startTime, boolean timedOut, boolean failed);

  long startRegisterInstantiators();

  long startRegisterDataSerializers();

  void endRegisterInstantiatorsSend(long startTime, boolean failed);

  void endRegisterDataSerializersSend(long startTime, boolean failed);

  void endRegisterInstantiators(long startTime, boolean timedOut, boolean failed);

  void endRegisterDataSerializers(long startTime, boolean timedOut, boolean failed);

  long startPutAll();

  void endPutAllSend(long startTime, boolean failed);

  void endPutAll(long startTime, boolean timedOut, boolean failed);

  long startRemoveAll();

  void endRemoveAllSend(long startTime, boolean failed);

  void endRemoveAll(long startTime, boolean timedOut, boolean failed);

  long startGetAll();

  void endGetAllSend(long startTime, boolean failed);

  void endGetAll(long startTime, boolean timedOut, boolean failed);

  int getConnections();

  int getOps();

  void incConnections(int delta);

  void startClientOp();

  void endClientOpSend(long duration, boolean failed);

  void endClientOp(long duration, boolean timedOut, boolean failed);

  void close();

  void incReceivedBytes(long v);

  void incSentBytes(long v);

  void incMessagesBeingReceived(int bytes);

  void decMessagesBeingReceived(int bytes);

  long startExecuteFunction();

  void endExecuteFunctionSend(long startTime, boolean failed);

  void endExecuteFunction(long startTime, boolean timedOut, boolean failed);

  int getExecuteFunctions();

  long getExecuteFunctionDuration();

  int getGetDurableCqs();

  long startGetClientPRMetadata();

  void endGetClientPRMetadataSend(long startTime, boolean failed);

  void endGetClientPRMetadata(long startTime, boolean timedOut, boolean failed);

  long startGetClientPartitionAttributes();

  void endGetClientPartitionAttributesSend(long startTime, boolean failed);

  void endGetClientPartitionAttributes(long startTime, boolean timedOut, boolean failed);

  long startGetPDXTypeById();

  long startGetPDXIdForType();

  void endGetPDXTypeByIdSend(long startTime, boolean failed);

  void endGetPDXIdForTypeSend(long startTime, boolean failed);

  void endGetPDXTypeById(long startTime, boolean timedOut, boolean failed);

  void endGetPDXIdForType(long startTime, boolean timedOut, boolean failed);

  long startAddPdxType();

  void endAddPdxTypeSend(long startTime, boolean failed);

  void endAddPdxType(long startTime, boolean timedOut, boolean failed);

  long startSize();

  void endSizeSend(long startTime, boolean failed);

  void endSize(long startTime, boolean timedOut, boolean failed);

  long startInvalidate();

  void endInvalidateSend(long startTime, boolean failed);

  void endInvalidate(long startTime, boolean timedOut, boolean failed);

  long startCommit();

  void endCommitSend(long startTime, boolean failed);

  void endCommit(long startTime, boolean timedOut, boolean failed);

  long startGetEntry();

  void endGetEntrySend(long startTime, boolean failed);

  void endGetEntry(long startTime, boolean timedOut, boolean failed);

  long startRollback();

  void endRollbackSend(long startTime, boolean failed);

  void endRollback(long startTime, boolean timedOut, boolean failed);

  long startTxFailover();

  void endTxFailoverSend(long startTime, boolean failed);

  void endTxFailover(long startTime, boolean timedOut, boolean failed);

  long startTxSynchronization();

  void endTxSynchronizationSend(long startTime, boolean failed);

  void endTxSynchronization(long startTime, boolean timedOut, boolean failed);
}
