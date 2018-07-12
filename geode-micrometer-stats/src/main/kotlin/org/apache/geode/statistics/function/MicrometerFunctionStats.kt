package org.apache.geode.statistics.function

import org.apache.geode.internal.cache.execute.FunctionStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS
import java.util.concurrent.ConcurrentHashMap

class MicrometerFunctionStats(statisticsFactory: StatisticsFactory?, private val functionName: String) :
        MicrometerMeterGroup(statisticsFactory, "FunctionStats-$functionName"), FunctionStats {

    override fun getGroupTags(): Array<String> = arrayOf("functionId", functionName)

    override fun initializeStaticMeters() {
        registerMeter(functionExecutionsCompletedMeter)
        registerMeter(functionExecutionTimer)
        registerMeter(functionExecutionsInProgressMeter)
        registerMeter(functionExecutionResultsSentToCollectorMeter)
        registerMeter(functionExecutionResultsSendReceiveByCollectorMeter)
        registerMeter(functionExecutionsMeter)
        registerMeter(functionExecutionWithResultTimer)
        registerMeter(functionExecutionsInProgressWithResultMeter)
        registerMeter(functionExecutionsExceptionMeter)
    }

    //This is a necessary evil for now. Until we can work out how the stats stuff really fits into
    //the new modular world.
    companion object {
        private val functionExecutionStatsMap = ConcurrentHashMap<String, MicrometerFunctionStats>()

        @JvmStatic
        fun getFunctionStats(textId: String): MicrometerFunctionStats =
                functionExecutionStatsMap[textId] ?: run {
                    val functionStats = MicrometerFunctionStats(statisticsFactory = null,functionName = textId)
                    functionExecutionStatsMap[textId] = functionStats
                    functionStats
                }

    }

    private val functionExecutionsCompletedMeter = CounterStatisticMeter("function.execution",
            "Total number of completed function.execute() calls for given function", arrayOf("status", "completed"))
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val functionExecutionTimer = CounterStatisticMeter("function.execution.timer",
            "Total time consumed for all completed invocations of the given function", arrayOf("status", "completed"), "nanoseconds")
    private val functionExecutionsInProgressMeter = GaugeStatisticMeter("function.execution.inprogress",
            "number of currently running invocations of the given function")
    private val functionExecutionResultsSentToCollectorMeter = CounterStatisticMeter("function,execution.results",
            "Total number of results sent to the ResultCollector", arrayOf("results", "sent"))
    private val functionExecutionResultsSendReceiveByCollectorMeter = CounterStatisticMeter("function,execution.results",
            "Total number of results received and passed to the ResultCollector", arrayOf("results", "sent-receive"))
    private val functionExecutionsMeter = CounterStatisticMeter("function.execution",
            "Total number of FunctionService.execute() calls for given function")
    @Deprecated("For Micrometer one should really be using Timers rather than current implemented Counters")
    private val functionExecutionWithResultTimer = CounterStatisticMeter("function.execution.timer",
            "Total time consumed for all completed given function.execute() calls where hasResult() returns true.",
            arrayOf("withResult", "true", "status", "completed"), "nanoseconds")
    private val functionExecutionsInProgressWithResultMeter = GaugeStatisticMeter("function.execution.inprogress",
            "A gauge indicating the number of currently active execute() calls for functions where hasResult() returns true.",
            arrayOf("withResult", "true"))
    private val functionExecutionsExceptionMeter = CounterStatisticMeter("function.execution",
            "Total number of Exceptions Occurred while executing function", arrayOf("status", "exception"))

    override fun close() {
        //noop for Micrometer impl
    }

    override fun getFunctionExecutionsCompleted(): Int = functionExecutionsCompletedMeter.getValue().toInt()

        override fun getFunctionExecutionCompleteProcessingTime(): Long = functionExecutionTimer.getValue()

    override fun getFunctionExecutionsRunning(): Int = functionExecutionsInProgressMeter.getValue().toInt()

    override fun getResultsSentToResultCollector(): Int = functionExecutionResultsSentToCollectorMeter.getValue().toInt()

    override fun getResultsReceived(): Int = functionExecutionResultsSendReceiveByCollectorMeter.getValue().toInt()

    override fun getFunctionExecutionCalls(): Int = functionExecutionsMeter.getValue().toInt()

    override fun getFunctionExecutionHasResultCompleteProcessingTime(): Int = functionExecutionWithResultTimer.getValue().toInt()

    override fun getFunctionExecutionHasResultRunning(): Int = functionExecutionsInProgressWithResultMeter.getValue().toInt()

    override fun getFunctionExecutionExceptions(): Int = functionExecutionsExceptionMeter.getValue().toInt()

    override fun startTime(): Long = NOW_NANOS

    override fun incFunctionExecutionsCompleted() {
        functionExecutionsCompletedMeter.increment()
    }

    override fun incFunctionExecutionsRunning() {
        functionExecutionsInProgressMeter.increment()
    }

    override fun incResultsReturned() {
        functionExecutionResultsSentToCollectorMeter.increment()
    }

    override fun incResultsReceived() {
        functionExecutionResultsSendReceiveByCollectorMeter.increment()
    }

    override fun incFunctionExecutionCalls() {
        functionExecutionsMeter.increment()
    }

    override fun incFunctionExecutionHasResultRunning() {
        functionExecutionsInProgressWithResultMeter.increment()
    }

    override fun incFunctionExecutionExceptions() {
        functionExecutionsExceptionMeter.increment()
    }

    override fun startFunctionExecution(haveResult: Boolean) {
        incFunctionExecutionCalls()
        if (haveResult) {
            incFunctionExecutionHasResultRunning()
        } else {
            incFunctionExecutionsRunning()
        }
    }


    override fun endFunctionExecution(start: Long, haveResult: Boolean) {
        val elapsed = System.nanoTime() - start
        incFunctionExecutionsCompleted()

        if (haveResult) {
            functionExecutionsInProgressWithResultMeter.decrement()
            functionExecutionWithResultTimer.increment(elapsed)
        } else {
            functionExecutionsInProgressMeter.decrement()
            functionExecutionTimer.increment(elapsed)
        }
    }

    override fun endFunctionExecutionWithException(haveResult: Boolean) {
        functionExecutionsExceptionMeter.increment()

        if (haveResult) {
            functionExecutionsInProgressWithResultMeter.decrement()
        } else {
            functionExecutionsInProgressMeter.decrement()
        }
    }
}