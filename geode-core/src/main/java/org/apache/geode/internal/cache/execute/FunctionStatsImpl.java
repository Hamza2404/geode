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
package org.apache.geode.internal.cache.execute;

import org.apache.geode.statistics.GFSStatsImplementor;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.distributed.internal.DistributionConfig;
import org.apache.geode.distributed.internal.DistributionStats;
import org.apache.geode.distributed.internal.InternalDistributedSystem;

public class FunctionStatsImpl implements FunctionStats, GFSStatsImplementor {

  public static String statName = "FunctionStatistics";
  /**
   * The <code>StatisticsType</code> of the statistics
   */
  private StatisticsType _type;

  private FunctionServiceStats aggregateStats;
  /**
   * Total number of completed function.execute() calls (aka invocations of a individual
   * function)Name of the function executions cimpleted statistic
   */
  private static final String FUNCTION_EXECUTIONS_COMPLETED = "functionExecutionsCompleted";

  /**
   * Total time consumed for all completed invocations of a individual function. Name of the
   * function executions completed processing time statistic
   */
  private static final String FUNCTION_EXECUTIONS_COMPLETED_PROCESSING_TIME =
      "functionExecutionsCompletedProcessingTime";

  /**
   * A guage indicating the number of currently running invocations Name of the function executions
   * running statistic
   */
  private static final String FUNCTION_EXECUTIONS_RUNNING = "functionExecutionsRunning";

  /**
   * Total number of results sent to the ResultCollector Name of the results returned statistic
   */
  private static final String RESULTS_SENT_TO_RESULTCOLLECTOR = "resultsSentToResultCollector";

  /**
   * Total number of FunctionService...execute() calls Name of the total function executions call
   * statistic
   */
  private static final String FUNCTION_EXECUTION_CALLS = "functionExecutionCalls";

  /**
   * Total time consumed for all completed execute() calls where hasResult() returns true. Name of
   * the function executions calls having hasResult=true time statistic
   */
  private static final String FUNCTION_EXECUTIONS_HASRESULT_COMPLETED_PROCESSING_TIME =
      "functionExecutionsHasResultCompletedProcessingTime";

  /**
   * A gauge indicating the number of currently active execute() calls for functions where
   * hasResult() returns true. Name of the function execution time statistic
   */
  private static final String FUNCTION_EXECUTIONS_HASRESULT_RUNNING =
      "functionExecutionsHasResultRunning";


  /**
   * Total number of results sent to the ResultCollector Name of the results returned statistic
   */
  private static final String RESULTS_RECEIVED = "resultsReceived";

  /**
   * Total number of Exceptions Occurred while executing function Name of the functionExecution
   * exceptions statistic
   */
  private static final String FUNCTION_EXECUTION_EXCEPTIONS = "functionExecutionsExceptions";

  // /**
  // * Total number of bytes received before invoking the function
  // * Name of the functionExecution bytes received statistic
  // */
  // private static final String BYTES_RECEIVED = "bytesReceived";
  //
  // /**
  // * Total number of bytes serialized for the result of the function
  // * Name of the bytes serialized statistic
  // */
  // private static final String BYTES_SERIALIZED = "bytesSerialized";

  /**
   * Id of the FUNCTION_EXECUTIONS_COMPLETED statistic
   */
  private int _functionExecutionsCompletedId;

  /**
   * Id of the FUNCTION_EXECUTIONS_COMPLETED_PROCESSING_TIME statistic
   */
  private int _functionExecutionsCompletedProcessingTimeId;

  /**
   * Id of the FUNCTION_EXECUTIONS_RUNNING statistic
   */
  private int _functionExecutionsRunningId;

  /**
   * Id of the RESULTS_SENT_TO_RESULTCOLLECTOR statistic
   */
  private int _resultsSentToResultCollectorId;

  /**
   * Id of the FUNCTION_EXECUTIONS_CALL statistic
   */
  private int _functionExecutionCallsId;

  /**
   * Id of the FUNCTION_EXECUTION_HASRESULT_COMPLETED_TIME statistic
   */
  private int _functionExecutionsHasResultCompletedProcessingTimeId;

  /**
   * Id of the FUNCTION_EXECUTIONS_HASRESULT_RUNNING statistic
   */
  private int _functionExecutionsHasResultRunningId;

  /**
   * Id of the RESULTS_RECEIVED statistic
   */
  private int _resultsReceived;

  /**
   * Id of the FUNCTION_EXECUTIONS_EXCEPTIONS statistic
   */
  private int _functionExecutionExceptions;

  /**
   * Returns the Function Stats for the given function
   * @param functionID represents the function for which we are returning the function Stats
   * @param distributedSystem represents the Distributed System
   * @return object of the FunctionStats
   */
  public static FunctionStats getFunctionStats(String functionID,
                                                   InternalDistributedSystem distributedSystem) {
    return distributedSystem.getInternalDistributedSystemStats().getFunctionStats(functionID);
  }

  public static FunctionStats getFunctionStats(String functionID) {
    InternalDistributedSystem distributedSystem = InternalDistributedSystem.getAnyInstance();
    return distributedSystem.getInternalDistributedSystemStats().getFunctionStats(functionID);
  }

  public static boolean isDisabled() {
    return Boolean.getBoolean(DistributionConfig.GEMFIRE_PREFIX + "statsDisabled");
  }

  // /** Id of the RESULTS_RECEIVED statistic */
  // private int _bytesReceived;
  //
  // /** Id of the FUNCTION_EXECUTIONS_EXCEPTIONS statistic */
  // private int _bytesSerialized;


  /**
   * Static initializer to create and initialize the <code>StatisticsType</code>
   */
  public void initializeStats(StatisticsFactory factory) {

    String statDescription = "This is the stats for the individual Function's Execution";

    _type = factory.createType(statName, statDescription,
        new StatisticDescriptor[]{factory.createIntCounter(FUNCTION_EXECUTIONS_COMPLETED,
            "Total number of completed function.execute() calls for given function", "operations"),

            factory.createLongCounter(FUNCTION_EXECUTIONS_COMPLETED_PROCESSING_TIME,
                "Total time consumed for all completed invocations of the given function",
                "nanoseconds"),

            factory.createIntGauge(FUNCTION_EXECUTIONS_RUNNING,
                "number of currently running invocations of the given function", "operations"),

            factory.createIntCounter(RESULTS_SENT_TO_RESULTCOLLECTOR,
                "Total number of results sent to the ResultCollector", "operations"),

            factory.createIntCounter(RESULTS_RECEIVED,
                "Total number of results received and passed to the ResultCollector", "operations"),

            factory.createIntCounter(FUNCTION_EXECUTION_CALLS,
                "Total number of FunctionService.execute() calls for given function", "operations"),

            factory.createLongCounter(FUNCTION_EXECUTIONS_HASRESULT_COMPLETED_PROCESSING_TIME,
                "Total time consumed for all completed given function.execute() calls where hasResult() returns true.",
                "nanoseconds"),

            factory.createIntGauge(FUNCTION_EXECUTIONS_HASRESULT_RUNNING,
                "A gauge indicating the number of currently active execute() calls for functions where hasResult() returns true.",
                "operations"),

            factory.createIntCounter(FUNCTION_EXECUTION_EXCEPTIONS,
                "Total number of Exceptions Occurred while executing function", "operations"),


        });
    // Initialize id fields
    _functionExecutionsCompletedId = _type.nameToId(FUNCTION_EXECUTIONS_COMPLETED);
    _functionExecutionsCompletedProcessingTimeId =
        _type.nameToId(FUNCTION_EXECUTIONS_COMPLETED_PROCESSING_TIME);
    _functionExecutionsRunningId = _type.nameToId(FUNCTION_EXECUTIONS_RUNNING);
    _resultsSentToResultCollectorId = _type.nameToId(RESULTS_SENT_TO_RESULTCOLLECTOR);
    _functionExecutionCallsId = _type.nameToId(FUNCTION_EXECUTION_CALLS);
    _functionExecutionsHasResultCompletedProcessingTimeId =
        _type.nameToId(FUNCTION_EXECUTIONS_HASRESULT_COMPLETED_PROCESSING_TIME);
    _functionExecutionsHasResultRunningId = _type.nameToId(FUNCTION_EXECUTIONS_HASRESULT_RUNNING);
    _functionExecutionExceptions = _type.nameToId(FUNCTION_EXECUTION_EXCEPTIONS);
    _resultsReceived = _type.nameToId(RESULTS_RECEIVED);
    // _bytesReceived = _type.nameToId(BYTES_RECEIVED);
    // _bytesSerialized = _type.nameToId(BYTES_SERIALIZED);
  }

  // //////////////////// Instance Fields //////////////////////

  /**
   * The <code>Statistics</code> instance to which most behavior is delegated
   */
  private final Statistics _stats;

  /**
   * Constructor.
   * @param factory The <code>StatisticsFactory</code> which creates the <code>Statistics</code>
   * instance
   * @param name The name of the <code>Statistics</code>
   */
  public FunctionStatsImpl(StatisticsFactory factory, String name) {
    initializeStats(factory);
    this._stats = factory.createAtomicStatistics(_type, name);
    aggregateStats =
        ((InternalDistributedSystem) factory).getInternalDistributedSystemStats()
            .getFunctionServiceStats();
  }

  /**
   * Closes the <code>FunctionServiceStats</code>.
   */
  @Override
  public void close() {
    this._stats.close();
  }

  /**
   * Returns the current value of the "Total number of completed function.execute() calls" stat.
   * @return the current value of the "function Executions completed" stat
   */
  @Override
  public int getFunctionExecutionsCompleted() {
    return this._stats.getInt(_functionExecutionsCompletedId);
  }

  /**
   * Increments the "FunctionExecutionsCompleted" stat.
   */
  @Override
  public void incFunctionExecutionsCompleted() {
    this._stats.incInt(_functionExecutionsCompletedId, 1);
    aggregateStats.incFunctionExecutionsCompleted();
  }

  /**
   * Returns the current value of the "Total time consumed for all completed invocations" stat.
   * @return the current value of the "functionExecutionCompleteProcessingTime" stat
   */
  @Override
  public long getFunctionExecutionCompleteProcessingTime() {
    return this._stats.getLong(_functionExecutionsCompletedProcessingTimeId);
  }

  /**
   * Returns the current value of the "number of currently running invocations" stat.
   * @return the current value of the "functionExecutionsRunning" stat
   */
  @Override
  public int getFunctionExecutionsRunning() {
    return this._stats.getInt(_functionExecutionsRunningId);
  }

  /**
   * Increments the "FunctionExecutionsRunning" stat.
   */
  @Override
  public void incFunctionExecutionsRunning() {
    this._stats.incInt(_functionExecutionsRunningId, 1);
    aggregateStats.incFunctionExecutionsRunning();
  }

  /**
   * Returns the current value of the "Total number of results sent to the ResultCollector" stat.
   * @return the current value of the "resultsReturned" stat
   */
  @Override
  public int getResultsSentToResultCollector() {
    return this._stats.getInt(_resultsSentToResultCollectorId);
  }

  /**
   * Increments the "ResultsReturnedToResultCollector" stat.
   */
  @Override
  public void incResultsReturned() {
    this._stats.incInt(_resultsSentToResultCollectorId, 1);
    aggregateStats.incResultsReturned();
  }

  /**
   * Returns the current value of the "Total number of results received and passed to
   * ResultCollector" stat.
   * @return the current value of the "resultsReturned" stat
   */
  @Override
  public int getResultsReceived() {
    return this._stats.getInt(_resultsReceived);
  }

  /**
   * Increments the "ResultsReturnedToResultCollector" stat.
   */
  @Override
  public void incResultsReceived() {
    this._stats.incInt(_resultsReceived, 1);
    aggregateStats.incResultsReceived();
  }

  /**
   * Returns the current value of the "Total number of FunctionService...execute() calls" stat.
   * @return the current value of the "functionExecutionsCall" stat
   */
  @Override
  public int getFunctionExecutionCalls() {
    return this._stats.getInt(_functionExecutionCallsId);
  }

  /**
   * Increments the "FunctionExecutionsCall" stat.
   */
  @Override
  public void incFunctionExecutionCalls() {
    this._stats.incInt(_functionExecutionCallsId, 1);
    aggregateStats.incFunctionExecutionCalls();
  }

  /**
   * Returns the current value of the "Total time consumed for all completed execute() calls where
   * hasResult() returns true" stat.
   * @return the current value of the "functionExecutionHasResultCompleteProcessingTime" stat
   */
  @Override
  public int getFunctionExecutionHasResultCompleteProcessingTime() {
    return this._stats.getInt(_functionExecutionsHasResultCompletedProcessingTimeId);
  }

  /**
   * Returns the current value of the "A gauge indicating the number of currently active execute()
   * calls for functions where hasResult() returns true" stat.
   * @return the current value of the "functionExecutionHasResultRunning" stat
   */
  @Override
  public int getFunctionExecutionHasResultRunning() {
    return this._stats.getInt(_functionExecutionsHasResultRunningId);
  }

  /**
   * Increments the "FunctionExecutionsCall" stat.
   */
  @Override
  public void incFunctionExecutionHasResultRunning() {
    this._stats.incInt(_functionExecutionsHasResultRunningId, 1);
    aggregateStats.incFunctionExecutionHasResultRunning();
  }

  /**
   * Returns the current value of the "Total number of Exceptions Occurred while executing function"
   * stat.
   * @return the current value of the "functionExecutionHasResultRunning" stat
   */
  @Override
  public int getFunctionExecutionExceptions() {
    return this._stats.getInt(_functionExecutionExceptions);
  }

  /**
   * Increments the "FunctionExecutionsCall" stat.
   */
  @Override
  public void incFunctionExecutionExceptions() {
    this._stats.incInt(_functionExecutionExceptions, 1);
    aggregateStats.incFunctionExecutionExceptions();
  }

  /**
   * Returns the current time (ns).
   * @return the current time (ns)
   */
  @Override
  public long startTime() {
    return System.nanoTime();
  }

  /**
   * Increments the "_functionExecutionCallsId" and "_functionExecutionsRunningId" stats and
   * "_functionExecutionHasResultRunningId" in case of function.hasResult = true..
   */
  @Override
  public void startFunctionExecution(boolean haveResult) {
    // Increment number of function execution calls
    this._stats.incInt(_functionExecutionCallsId, 1);

    // Increment number of functions running
    this._stats.incInt(_functionExecutionsRunningId, 1);

    if (haveResult) {
      // Increment number of function excution with haveResult = true call
      this._stats.incInt(_functionExecutionsHasResultRunningId, 1);
    }
    aggregateStats.startFunctionExecution(haveResult);
  }

  /**
   * Increments the "functionExecutionsCompleted" and "functionExecutionCompleteProcessingTime"
   * stats.
   * @param start The start of the functionExecution (which is decremented from the current time to
   * determine the function Execution processing time).
   * @param haveResult haveResult=true then update the _functionExecutionHasResultRunningId and
   * _functionExecutionHasResultCompleteProcessingTimeId
   */
  @Override
  public void endFunctionExecution(long start, boolean haveResult) {
    long ts = System.nanoTime();

    // Increment number of function executions completed
    this._stats.incInt(_functionExecutionsCompletedId, 1);

    // Decrement function Executions running.
    this._stats.incInt(_functionExecutionsRunningId, -1);

    // Increment function execution complete processing time
    long elapsed = ts - start;
    this._stats.incLong(_functionExecutionsCompletedProcessingTimeId, elapsed);

    if (haveResult) {
      // Decrement function Executions with haveResult = true running.
      this._stats.incInt(_functionExecutionsHasResultRunningId, -1);

      // Increment function execution with haveResult = true complete processing time
      this._stats.incLong(_functionExecutionsHasResultCompletedProcessingTimeId, elapsed);
    }
    aggregateStats.endFunctionExecution(start, haveResult);
  }

  /**
   * Increments the "_functionExecutionException" and decrements "_functionExecutionsRunningId" and
   * decrement "_functionExecutionHasResultRunningId"
   */
  @Override
  public void endFunctionExecutionWithException(boolean haveResult) {
    // Decrement function Executions running.
    this._stats.incInt(_functionExecutionsRunningId, -1);

    // Increment number of function excution exceptions
    this._stats.incInt(_functionExecutionExceptions, 1);

    if (haveResult) {
      // Decrement function Executions with haveResult = true running.
      this._stats.incInt(_functionExecutionsHasResultRunningId, -1);
    }
    aggregateStats.endFunctionExecutionWithException(haveResult);
  }

}
