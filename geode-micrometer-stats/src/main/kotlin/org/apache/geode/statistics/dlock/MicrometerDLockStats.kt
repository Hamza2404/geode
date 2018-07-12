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

package org.apache.geode.statistics.dlock

import org.apache.geode.distributed.internal.PoolStatHelper
import org.apache.geode.distributed.internal.QueueStatHelper
import org.apache.geode.distributed.internal.locks.DistributedLockStats
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerMeterGroup
import org.apache.geode.statistics.internal.micrometer.impl.TimerStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor
import org.apache.geode.statistics.util.NOW_NANOS

class MicrometerDLockStats(statisticsFactory: StatisticsFactory?,private val statMeter: String) :
        MicrometerMeterGroup(statisticsFactory,"DLockStats-$statMeter"), DistributedLockStats {

    override fun getGroupTags(): Array<String> = arrayOf("statsMeter", statMeter)

    override fun initializeStaticMeters() {
        registerMeter(dlockGrantorsMeter)
        registerMeter(dlockServicesMeter)
        registerMeter(dlockTokensMeter)
        registerMeter(dlockGrantTokenMeter)
        registerMeter(dlockRequestQueuesMeter)
        registerMeter(dlockSerialQueueSizeMeter)
        registerMeter(dlockSerialThreadsMeter)
        registerMeter(dlockWaitingQueueSizeMeter)
        registerMeter(dlockWaitingThreadsMeter)
        registerMeter(dlockLockWaitsInProgressMeter)
        registerMeter(dlockLockWaitsCompletedMeter)
        registerMeter(dlockLockWaitTimer)
        registerMeter(dlockLockWaitsFailedMeter)
        registerMeter(dlockLockWaitsFailedTimer)
        registerMeter(dlockGrantWaitsInProgressMeter)
        registerMeter(dlockGrantWaitsCompletedMeter)
        registerMeter(dlockGrantWaitsTimer)
        registerMeter(dlockGrantWaitsNotGrantorMeter)
        registerMeter(dlockGrantWaitsNotGrantorTimer)
        registerMeter(dlockGrantWaitsTimeoutMeter)
        registerMeter(dlockGrantWaitsTimeoutTimer)
        registerMeter(dlockGrantWaitsNotHolderMeter)
        registerMeter(dlockGrantWaitsNotHolderTimer)
        registerMeter(dlockGrantWaitsFailedMeter)
        registerMeter(dlockGrantWaitsFailedTimer)
        registerMeter(dlockGrantWaitsSuspendedMeter)
        registerMeter(dlockGrantWaitsSuspendedTimer)
        registerMeter(dlockGrantWaitsDestroyedMeter)
        registerMeter(dlockGrantWaitsDestroyedTimer)
        registerMeter(dlockCreateGrantorInProgressMeter)
        registerMeter(dlockCreateGrantorCompletedMeter)
        registerMeter(dlockCreateGrantorTimer)
        registerMeter(dlockServiceCreatesInProgressMeter)
        registerMeter(dlockServiceCreatesCompletedMeter)
        registerMeter(dlockServiceCreatesTimer)
        registerMeter(dlockServiceInitLatchTimer)
        registerMeter(dlockGrantorWaitsInProgressMeter)
        registerMeter(dlockGrantorWaitsCompletedMeter)
        registerMeter(dlockGrantorWaitsTimer)
        registerMeter(dlockGrantorWaitsFailedMeter)
        registerMeter(dlockGrantorWaitsFailedTimer)
        registerMeter(dlockGrantorThreadsInProgressMeter)
        registerMeter(dlockGrantorThreadsCompletedMeter)
        registerMeter(dlockGrantorThreadExpireAndGrantLocksTimer)
        registerMeter(dlockGrantorThreadHandleRequestTimeoutsTimer)
        registerMeter(dlockGrantorThreadRemoveUnusedTokensTimer)
        registerMeter(dlockGrantorThreadTimer)
        registerMeter(dlockPendingRequestsMeter)
        registerMeter(dlockDestroyReadWaitsInProgressMeter)
        registerMeter(dlockDestroyReadWaitsCompletedMeter)
        registerMeter(dlockDestroyReadWaitsTimer)
        registerMeter(dlockDestroyReadWaitsFailedMeter)
        registerMeter(dlockDestroyReadWaitsFailedTimer)
        registerMeter(dlockDestroyWriteWaitsInProgressMeter)
        registerMeter(dlockDestroyWriteWaitsCompletedMeter)
        registerMeter(dlockDestroyWriteWaitsTimer)
        registerMeter(dlockDestroyWriteWaitsFailedMeter)
        registerMeter(dlockDestroyWriteWaitsFailerTimer)
        registerMeter(dlockDestroyReadsMeter)
        registerMeter(dlockDestroyWritesMeter)
        registerMeter(dlockLockReleasesInProgressMeter)
        registerMeter(dlockLockReleasesCompletedMeter)
        registerMeter(dlockLockReleasesTimer)
        registerMeter(dlockBecomeGrantorRequestsMeter)
        registerMeter(dlockFreeResourcesCompleteMeter)
        registerMeter(dlockFreeResourcesFailedMeter)
    }

    private val dlockGrantorsMeter = GaugeStatisticMeter("dlock.grantors", "The current number of lock grantors hosted by this system member.")
    private val dlockServicesMeter = GaugeStatisticMeter("dlock.services", "The current number of lock services used by this system member.")
    private val dlockTokensMeter = GaugeStatisticMeter("dlock.tokens", "The current number of lock tokens used by this system member.")
    private val dlockGrantTokenMeter = GaugeStatisticMeter("dlock.grantTokens", "The current number of grant tokens used by local grantors.")
    private val dlockRequestQueuesMeter = GaugeStatisticMeter("dlock.requestQueues", "The current number of lock request queues used by this system member.")
    private val dlockSerialQueueSizeMeter = GaugeStatisticMeter("dlock.serialQueueSize", "The number of serial distribution messages currently waiting to be processed.")
    private val dlockSerialThreadsMeter = GaugeStatisticMeter("dlock.serialThreads", "The number of threads currently processing serial/ordered messages.")
    private val dlockWaitingQueueSizeMeter = GaugeStatisticMeter("dlock.waitingQueueSize", "The number of distribution messages currently waiting for some other resource before they can be processed.")
    private val dlockWaitingThreadsMeter = GaugeStatisticMeter("dlock.waitingThreads", "The number of threads currently processing messages that had to wait for a resource.")
    private val dlockLockWaitsInProgressMeter = GaugeStatisticMeter("dlock.lockWaitsInProgress", "Current number of threads waiting for a distributed lock.")
    private val dlockLockWaitsCompletedMeter = CounterStatisticMeter("dlock.lockWaitsCompleted", "Total number of times distributed lock wait has completed by successfully obtained the lock.")
    private val dlockLockWaitTimer = TimerStatisticMeter("dlock.lockWaitTime", "Total time spent waiting for a distributed lock that was obtained.", meterUnit = "nanoseconds")
    private val dlockLockWaitsFailedMeter = CounterStatisticMeter("dlock.lockWaitsFailed", "Total number of times distributed lock wait has completed by failing to obtain the lock.")
    private val dlockLockWaitsFailedTimer = TimerStatisticMeter("dlock.lockWaitFailedTime", "Total time spent waiting for a distributed lock that we failed to obtain.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsInProgressMeter = GaugeStatisticMeter("dlock.grantWaitsInProgress", "Current number of distributed lock requests being granted.")
    private val dlockGrantWaitsCompletedMeter = CounterStatisticMeter("dlock.grantWaitsCompleted", "Total number of times granting of a lock request has completed by successfully granting the lock.")
    private val dlockGrantWaitsTimer = TimerStatisticMeter("dlock.grantWaitTime", "Total time spent attempting to grant a distributed lock.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsNotGrantorMeter = CounterStatisticMeter("dlock.grantWaitsNotGrantor", "Total number of times granting of lock request failed because not grantor.")
    private val dlockGrantWaitsNotGrantorTimer = TimerStatisticMeter("dlock.grantWaitNotGrantorTime", "Total time spent granting of lock requests that failed because not grantor.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsTimeoutMeter = CounterStatisticMeter("dlock.grantWaitsTimeout", "Total number of times granting of lock request failed because timeout.")
    private val dlockGrantWaitsTimeoutTimer = TimerStatisticMeter("grantWaitTimeoutTime", "Total time spent granting of lock requests that failed because timeout.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsNotHolderMeter = CounterStatisticMeter("dlock.grantWaitsNotHolder", "Total number of times granting of lock request failed because reentrant was not holder.")
    private val dlockGrantWaitsNotHolderTimer = TimerStatisticMeter("dlock.grantWaitNotHolderTime", "Total time spent granting of lock requests that failed because reentrant was not holder.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsFailedMeter = CounterStatisticMeter("dlock.grantWaitsFailed", "Total number of times granting of lock request failed because try locks failed.")
    private val dlockGrantWaitsFailedTimer = TimerStatisticMeter("dlock.grantWaitFailedTime", "Total time spent granting of lock requests that failed because try locks failed.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsSuspendedMeter = CounterStatisticMeter("dlock.grantWaitsSuspended", "Total number of times granting of lock request failed because lock service was suspended.")
    private val dlockGrantWaitsSuspendedTimer = TimerStatisticMeter("dlock.grantWaitSuspendedTime", "Total time spent granting of lock requests that failed because lock service was suspended.", meterUnit = "nanoseconds")
    private val dlockGrantWaitsDestroyedMeter = CounterStatisticMeter("dlock.grantWaitsDestroyed", "Total number of times granting of lock request failed because lock service was destroyed.")
    private val dlockGrantWaitsDestroyedTimer = TimerStatisticMeter("dlock.grantWaitDestroyedTime", "Total time spent granting of lock requests that failed because lock service was destroyed.", meterUnit = "nanoseconds")
    private val dlockCreateGrantorInProgressMeter = GaugeStatisticMeter("dlock.createGrantorsInProgress", "Current number of initial grantors being created in this process.")
    private val dlockCreateGrantorCompletedMeter = CounterStatisticMeter("dlock.createGrantorsCompleted", "Total number of initial grantors created in this process.")
    private val dlockCreateGrantorTimer = TimerStatisticMeter("dlock.createGrantorTime", "Total time spent waiting create the intial grantor for lock services.", meterUnit = "nanoseconds")
    private val dlockServiceCreatesInProgressMeter = GaugeStatisticMeter("dlock.serviceCreatesInProgress", "Current number of lock services being created in this process.")
    private val dlockServiceCreatesCompletedMeter = CounterStatisticMeter("dlock.serviceCreatesCompleted", "Total number of lock services created in this process.")
    private val dlockServiceCreatesTimer = TimerStatisticMeter("dlock.serviceCreateLatchTime", "Total time spent creating lock services before releasing create latches.", meterUnit = "nanoseconds")
    private val dlockServiceInitLatchTimer = TimerStatisticMeter("dlock.serviceInitLatchTime", "Total time spent creating lock services before releasing init latches.", meterUnit = "nanoseconds")
    private val dlockGrantorWaitsInProgressMeter = GaugeStatisticMeter("dlock.grantorWaitsInProgress", "Current number of threads waiting for grantor latch to open.")
    private val dlockGrantorWaitsCompletedMeter = CounterStatisticMeter("dlock.grantorWaitsCompleted", "Total number of times waiting threads completed waiting for the grantor latch to open.")
    private val dlockGrantorWaitsTimer = TimerStatisticMeter("dlock.grantorWaitTime", "Total time spent waiting for the grantor latch which resulted in success.", meterUnit = "nanoseconds")
    private val dlockGrantorWaitsFailedMeter = CounterStatisticMeter("dlock.grantorWaitsFailed", "Total number of times waiting threads failed to finish waiting for the grantor latch to open.")
    private val dlockGrantorWaitsFailedTimer = TimerStatisticMeter("dlock.grantorWaitFailedTime", "Total time spent waiting for the grantor latch which resulted in failure.", meterUnit = "nanoseconds")
    private val dlockGrantorThreadsInProgressMeter = GaugeStatisticMeter("dlock.grantorThreadsInProgress", "Current iterations of work performed by grantor thread(s).")
    private val dlockGrantorThreadsCompletedMeter = CounterStatisticMeter("dlock.grantorThreadsCompleted", "Total number of iterations of work performed by grantor thread(s).")
    private val dlockGrantorThreadExpireAndGrantLocksTimer = TimerStatisticMeter("dlock.grantorThreadExpireAndGrantLocksTime", "Total time spent by grantor thread(s) performing expireAndGrantLocks tasks.", meterUnit = "nanoseconds")
    private val dlockGrantorThreadHandleRequestTimeoutsTimer = TimerStatisticMeter("dlock.grantorThreadHandleRequestTimeoutsTime", "Total time spent by grantor thread(s) performing handleRequestTimeouts tasks.", meterUnit = "nanoseconds")
    private val dlockGrantorThreadRemoveUnusedTokensTimer = TimerStatisticMeter("dlock.grantorThreadRemoveUnusedTokensTime", "Total time spent by grantor thread(s) performing removeUnusedTokens tasks.", meterUnit = "nanoseconds")
    private val dlockGrantorThreadTimer = TimerStatisticMeter("dlock.grantorThreadTime", "Total time spent by grantor thread(s) performing all grantor tasks.", meterUnit = "nanoseconds")
    private val dlockPendingRequestsMeter = GaugeStatisticMeter("dlock.pendingRequests", "The current number of pending lock requests queued by grantors in this process.")
    private val dlockDestroyReadWaitsInProgressMeter = GaugeStatisticMeter("dlock.destroyReadWaitsInProgress", "Current number of threads waiting for a DLockService destroy read lock.")
    private val dlockDestroyReadWaitsCompletedMeter = CounterStatisticMeter("dlock.destroyReadWaitsCompleted", "Total number of times a DLockService destroy read lock wait has completed successfully.")
    private val dlockDestroyReadWaitsTimer = TimerStatisticMeter("dlock.destroyReadWaitTime", "Total time spent waiting for a DLockService destroy read lock that was obtained.", meterUnit = "nanoseconds")
    private val dlockDestroyReadWaitsFailedMeter = CounterStatisticMeter("dlock.destroyReadWaitsFailed", "Total number of times a DLockService destroy read lock wait has completed unsuccessfully.")
    private val dlockDestroyReadWaitsFailedTimer = TimerStatisticMeter("dlock.destroyReadWaitFailedTime", "Total time spent waiting for a DLockService destroy read lock that was not obtained.", meterUnit = "nanoseconds")
    private val dlockDestroyWriteWaitsInProgressMeter = GaugeStatisticMeter("dlock.destroyWriteWaitsInProgress", "Current number of thwrites waiting for a DLockService destroy write lock.")
    private val dlockDestroyWriteWaitsCompletedMeter = CounterStatisticMeter("dlock.destroyWriteWaitsCompleted", "Total number of times a DLockService destroy write lock wait has completed successfully.")
    private val dlockDestroyWriteWaitsTimer = TimerStatisticMeter("dlock.destroyWriteWaitTime", "Total time spent waiting for a DLockService destroy write lock that was obtained.", meterUnit = "nanoseconds")
    private val dlockDestroyWriteWaitsFailedMeter = CounterStatisticMeter("dlock.destroyWriteWaitsFailed", "Total number of times a DLockService destroy write lock wait has completed unsuccessfully.")
    private val dlockDestroyWriteWaitsFailerTimer = TimerStatisticMeter("dlock.destroyWriteWaitFailedTime", "Total time spent waiting for a DLockService destroy write lock that was not obtained.", meterUnit = "nanoseconds")
    private val dlockDestroyReadsMeter = GaugeStatisticMeter("dlock.destroyReads", "The current number of DLockService destroy read locks held by this process.")
    private val dlockDestroyWritesMeter = GaugeStatisticMeter("dlock.destroyWrites", "The current number of DLockService destroy write locks held by this process.")
    private val dlockLockReleasesInProgressMeter = GaugeStatisticMeter("dlock.lockReleasesInProgress", "Current number of threads releasing a distributed lock.")
    private val dlockLockReleasesCompletedMeter = CounterStatisticMeter("dlock.lockReleasesCompleted", "Total number of times distributed lock release has completed.")
    private val dlockLockReleasesTimer = TimerStatisticMeter("dlock.lockReleaseTime", "Total time spent releasing a distributed lock.", meterUnit = "nanoseconds")
    private val dlockBecomeGrantorRequestsMeter = CounterStatisticMeter("dlock.becomeGrantorRequests", "Total number of times this member has explicitly requested to become lock grantor.")
    private val dlockFreeResourcesCompleteMeter = CounterStatisticMeter("dlock.freeResourcesCompleted", "Total number of times this member has freed resources for a distributed lock.")
    private val dlockFreeResourcesFailedMeter = CounterStatisticMeter("dlock.freeResourcesFailed", "Total number of times this member has attempted to free resources for a distributed lock which remained in use.")

    override fun getLockWaitsInProgress(): Int = dlockLockWaitsInProgressMeter.getValue().toInt()
    override fun getLockWaitsCompleted(): Int = dlockLockWaitsCompletedMeter.getValue().toInt()
    override fun getLockWaitsFailed(): Int = dlockLockWaitsFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLockWaitTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLockWaitFailedTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startLockWait(): Long {
        dlockLockWaitsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endLockWait(start: Long, success: Boolean) {
        val ts = NOW_NANOS
        dlockLockWaitsInProgressMeter.decrement()
        if (success) {
            dlockLockWaitsCompletedMeter.increment()
            dlockLockWaitTimer.recordValue(ts - start)
        } else {
            dlockLockWaitsFailedMeter.increment()
            dlockLockWaitsFailedTimer.recordValue(ts - start)
        }
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getWaitingQueueSize(): Int = dlockWaitingQueueSizeMeter.getValue().toInt()

    override fun incWaitingQueueSize(messages: Int) {
        dlockWaitingQueueSizeMeter.increment(messages)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getSerialQueueSize(): Int = dlockSerialQueueSizeMeter.getValue().toInt()

    override fun incSerialQueueSize(messages: Int) {
        dlockSerialQueueSizeMeter.increment(messages)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getNumSerialThreads(): Int = dlockSerialThreadsMeter.getValue().toInt()

    override fun incNumSerialThreads(threads: Int) {
        dlockSerialThreadsMeter.increment(threads)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getWaitingThreads(): Int = dlockWaitingThreadsMeter.getValue().toInt()

    override fun incWaitingThreads(threads: Int) {
        dlockWaitingThreadsMeter.increment(threads)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getServices(): Int = dlockServicesMeter.getValue().toInt()

    override fun incServices(value: Int) {
        dlockServicesMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantors(): Int = dlockGrantorsMeter.getValue().toInt()

    override fun incGrantors(value: Int) {
        dlockGrantorsMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getTokens(): Int = dlockTokensMeter.getValue().toInt()

    override fun incTokens(value: Int) {
        dlockTokensMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantTokens(): Int = dlockGrantTokenMeter.getValue().toInt()

    override fun incGrantTokens(value: Int) {
        dlockGrantTokenMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getRequestQueues(): Int = dlockRequestQueuesMeter.getValue().toInt()

    override fun incRequestQueues(value: Int) {
        dlockRequestQueuesMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantWaitsInProgress(): Int = dlockGrantWaitsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantWaitsCompleted(): Int = dlockGrantWaitsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantWaitsFailed(): Int = dlockGrantWaitsFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantWaitTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantWaitFailedTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startGrantWait(): Long {
        dlockGrantWaitsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endGrantWait(start: Long) {
        dlockGrantWaitsTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsCompletedMeter.increment()
    }

    override fun endGrantWaitNotGrantor(start: Long) {
        dlockGrantWaitsNotGrantorTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsNotGrantorMeter.increment()
    }

    override fun endGrantWaitTimeout(start: Long) {
        dlockGrantWaitsTimeoutTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsTimeoutMeter.increment()
    }

    override fun endGrantWaitNotHolder(start: Long) {
        dlockGrantWaitsNotHolderTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsNotHolderMeter.increment()
    }

    override fun endGrantWaitFailed(start: Long) {
        dlockGrantWaitsFailedTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsFailedMeter.increment()
    }

    override fun endGrantWaitSuspended(start: Long) {
        dlockGrantWaitsSuspendedTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsSuspendedMeter.increment()
    }

    override fun endGrantWaitDestroyed(start: Long) {
        dlockGrantWaitsDestroyedTimer.recordValue(NOW_NANOS - start)
        dlockGrantWaitsInProgressMeter.decrement()
        dlockGrantWaitsDestroyedMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getCreateGrantorsInProgress(): Int = dlockCreateGrantorInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getCreateGrantorsCompleted(): Int = dlockCreateGrantorCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getCreateGrantorTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startCreateGrantor(): Long {
        dlockCreateGrantorInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endCreateGrantor(start: Long) {
        dlockCreateGrantorTimer.recordValue(NOW_NANOS - start)
        dlockCreateGrantorInProgressMeter.decrement()
        dlockCreateGrantorCompletedMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getServiceCreatesInProgress(): Int = dlockServiceCreatesInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getServiceCreatesCompleted(): Int = dlockServiceCreatesCompletedMeter.getValue().toInt()

    override fun startServiceCreate(): Long {
        dlockServiceCreatesInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun serviceCreateLatchReleased(start: Long) {
        dlockServiceInitLatchTimer.recordValue(NOW_NANOS - start)
    }

    override fun serviceInitLatchReleased(start: Long) {
        dlockServiceInitLatchTimer.recordValue(NOW_NANOS - start)
        dlockServiceCreatesInProgressMeter.decrement()
        dlockServiceCreatesCompletedMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getServiceCreateLatchTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getServiceInitLatchTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorWaitsInProgress(): Int = dlockGrantorWaitsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorWaitsCompleted(): Int = dlockGrantorWaitsCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorWaitsFailed(): Int = dlockGrantorWaitsFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorWaitTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorWaitFailedTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startGrantorWait(): Long {
        dlockGrantorWaitsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endGrantorWait(start: Long, success: Boolean) {
        dlockGrantorWaitsInProgressMeter.decrement()
        if (success) {
            dlockGrantorWaitsCompletedMeter.increment()
            dlockGrantorWaitsTimer.recordValue(NOW_NANOS - start)
        } else {
            dlockGrantorWaitsFailedMeter.increment()
            dlockGrantorWaitsFailedTimer.recordValue(NOW_NANOS - start)
        }
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadsInProgress(): Int = dlockGrantorThreadsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadsCompleted(): Int = dlockGrantorThreadsCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadExpireAndGrantLocksTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadHandleRequestTimeoutsTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getGrantorThreadRemoveUnusedTokensTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startGrantorThread(): Long {
        dlockGrantorThreadsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endGrantorThreadExpireAndGrantLocks(start: Long): Long {
        dlockGrantorThreadExpireAndGrantLocksTimer.recordValue(NOW_NANOS - start)
        return NOW_NANOS
    }

    override fun endGrantorThreadHandleRequestTimeouts(timing: Long): Long {
        dlockGrantorThreadHandleRequestTimeoutsTimer.recordValue(NOW_NANOS - timing)
        return NOW_NANOS
    }

    override fun endGrantorThreadRemoveUnusedTokens(timing: Long) {
        dlockGrantorThreadRemoveUnusedTokensTimer.recordValue(NOW_NANOS - timing)
    }

    override fun endGrantorThread(start: Long) {
        dlockGrantorThreadsInProgressMeter.decrement()
        dlockGrantorThreadsCompletedMeter.increment()
        dlockGrantorThreadTimer.recordValue(NOW_NANOS - start)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getPendingRequests(): Int = dlockPendingRequestsMeter.getValue().toInt()

    override fun incPendingRequests(value: Int) {
        dlockPendingRequestsMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReadWaitsInProgress(): Int = dlockDestroyReadWaitsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReadWaitsCompleted(): Int = dlockDestroyReadWaitsCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReadWaitsFailed(): Int = dlockDestroyReadWaitsFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReadWaitTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReadWaitFailedTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startDestroyReadWait(): Long {
        dlockDestroyReadWaitsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endDestroyReadWait(start: Long, success: Boolean) {
        dlockDestroyReadWaitsInProgressMeter.decrement()
        if (success) {
            dlockDestroyReadWaitsCompletedMeter.increment()
            dlockDestroyReadWaitsTimer.recordValue(NOW_NANOS - start)
        } else {
            dlockDestroyReadWaitsFailedMeter.increment()
            dlockDestroyReadWaitsFailedTimer.recordValue(NOW_NANOS - start)
        }
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWriteWaitsInProgress(): Int = dlockDestroyWriteWaitsInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWriteWaitsCompleted(): Int = dlockDestroyWriteWaitsCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWriteWaitsFailed(): Int = dlockDestroyWriteWaitsFailedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWriteWaitTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWriteWaitFailedTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startDestroyWriteWait(): Long {
        dlockDestroyWriteWaitsInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endDestroyWriteWait(start: Long, success: Boolean) {
        dlockDestroyWriteWaitsInProgressMeter.decrement()
        if (success) {
            dlockDestroyWriteWaitsCompletedMeter.increment()
            dlockDestroyWriteWaitsTimer.recordValue(NOW_NANOS - start)
        } else {
            dlockDestroyWriteWaitsFailedMeter.increment()
            dlockDestroyWriteWaitsFailerTimer.recordValue(NOW_NANOS - start)
        }
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyReads(): Int = dlockDestroyReadsMeter.getValue().toInt()

    override fun incDestroyReads(value: Int) {
        dlockDestroyReadsMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getDestroyWrites(): Int = dlockDestroyWritesMeter.getValue().toInt()

    override fun incDestroyWrites(value: Int) {
        dlockDestroyWritesMeter.increment(value)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLockReleasesInProgress(): Int = dlockLockReleasesInProgressMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLockReleasesCompleted(): Int = dlockLockReleasesCompletedMeter.getValue().toInt()

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getLockReleaseTime(): Long {
        TODO("We should not expose the timer stats publically")
    }

    override fun startLockRelease(): Long {
        dlockLockReleasesInProgressMeter.increment()
        return NOW_NANOS
    }

    override fun endLockRelease(start: Long) {
        dlockLockReleasesInProgressMeter.decrement()
        dlockLockReleasesCompletedMeter.increment()
        dlockLockReleasesTimer.recordValue(NOW_NANOS - start)
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getBecomeGrantorRequests(): Int = dlockBecomeGrantorRequestsMeter.getValue().toInt()

    override fun incBecomeGrantorRequests() {
        dlockBecomeGrantorRequestsMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getFreeResourcesCompleted(): Int = dlockFreeResourcesCompleteMeter.getValue().toInt()

    override fun incFreeResourcesCompleted() {
        dlockFreeResourcesCompleteMeter.increment()
    }

    @Deprecated(message = "This was just done as an interim solution until GEODE does not depend on stats for internal state")
    override fun getFreeResourcesFailed(): Int = dlockFreeResourcesFailedMeter.getValue().toInt()

    override fun incFreeResourcesFailed() {
        dlockFreeResourcesFailedMeter.increment()
    }

    override fun getSerialQueueHelper(): QueueStatHelper {
        return object : QueueStatHelper {
            override fun add() {
                incSerialQueueSize(1)
            }

            override fun remove() {
                incSerialQueueSize(-1)
            }

            override fun remove(count: Int) {
                incSerialQueueSize(-count)
            }
        }
    }

    override fun getWaitingPoolHelper(): PoolStatHelper {
        return object : PoolStatHelper {
            override fun startJob() {
                incWaitingThreads(1)
            }

            override fun endJob() {
                incWaitingThreads(-1)
            }
        }
    }

    override fun getWaitingQueueHelper(): QueueStatHelper {
        return object : QueueStatHelper {
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
    }

    override fun close() {
        //noop
    }

    override fun getStats(): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
