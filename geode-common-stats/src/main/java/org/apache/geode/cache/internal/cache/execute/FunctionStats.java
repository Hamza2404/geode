package org.apache.geode.internal.cache.execute;

public interface FunctionStats {

  void close();

  int getFunctionExecutionsCompleted();

  void incFunctionExecutionsCompleted();

  long getFunctionExecutionCompleteProcessingTime();

  int getFunctionExecutionsRunning();

  void incFunctionExecutionsRunning();

  int getResultsSentToResultCollector();

  void incResultsReturned();

  int getResultsReceived();

  void incResultsReceived();

  int getFunctionExecutionCalls();

  void incFunctionExecutionCalls();

  int getFunctionExecutionHasResultCompleteProcessingTime();

  int getFunctionExecutionHasResultRunning();

  void incFunctionExecutionHasResultRunning();

  int getFunctionExecutionExceptions();

  void incFunctionExecutionExceptions();

  long startTime();

  void startFunctionExecution(boolean haveResult);

  void endFunctionExecution(long start, boolean haveResult);

  void endFunctionExecutionWithException(boolean haveResult);
}
