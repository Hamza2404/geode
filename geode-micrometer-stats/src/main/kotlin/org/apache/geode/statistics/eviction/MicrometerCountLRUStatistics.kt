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

import org.apache.geode.internal.cache.eviction.CountLRUEvictionStats
import org.apache.geode.internal.cache.eviction.EvictionStats
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.micrometer.MicrometerStatsImplementor

class MicrometerCountLRUStatistics(statisticsFactory: StatisticsFactory?, private val name: String) :
        MicrometerEvictionStatsImpl(statisticsFactory, name, "CountLRUStats-$name"), CountLRUEvictionStats {

    private val countEvictionEntriesAllowedCount = GaugeStatisticMeter("eviction.count.lru.entries.allowed.count", "Number of entries allowed in this region.")
    private val countEvictionEntryCount = GaugeStatisticMeter("eviction.count.lru.entries.count", "Number of entries in this region.")


    override fun initializeStaticMeters() {
        super.initializeStaticMeters()
        registerMeter(countEvictionEntriesAllowedCount)
        registerMeter(countEvictionEntryCount)
    }

    override fun updateCounter(delta: Long) {
        countEvictionEntryCount.increment(delta)
    }

    override fun setLimit(newValue: Long) {
        countEvictionEntriesAllowedCount.setValue(newValue)
    }

    override fun setCounter(newValue: Long) {
        countEvictionEntryCount.setValue(newValue)
    }
}
