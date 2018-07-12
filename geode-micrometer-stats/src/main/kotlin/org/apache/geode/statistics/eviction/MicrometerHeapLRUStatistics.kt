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
package org.apache.geode.statistics.eviction

import org.apache.geode.internal.cache.eviction.EvictionStats
import org.apache.geode.internal.cache.eviction.HeapLRUEvictionStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter

class MicrometerHeapLRUStatistics(statisticsFactory: StatisticsFactory?, private val name: String) :
        MicrometerEvictionStatsImpl(statisticsFactory, name, "HeapLRUStatistics-$name"), HeapLRUEvictionStats {

    private val heapEvictionEntryBytesMeter = GaugeStatisticMeter("eviction.heap.lru.entries.bytes", "The amount of memory currently used by regions configured for eviction.", meterUnit = "bytes")

    override fun initializeStaticMeters() {
        super.initializeStaticMeters()
        registerMeter(heapEvictionEntryBytesMeter)
    }

    override fun updateCounter(delta: Long) {
        heapEvictionEntryBytesMeter.increment(delta)
    }

    override fun setCounter(newValue: Long) {
        heapEvictionEntryBytesMeter.setValue(newValue)
    }

    override fun setLimit(newValue: Long) {
        //noop
    }
}
