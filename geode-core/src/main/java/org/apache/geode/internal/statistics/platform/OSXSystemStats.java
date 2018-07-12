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

package org.apache.geode.internal.statistics.platform;

import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.StatisticsFactory;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.statistics.StatisticsTypeFactory;
import org.apache.geode.internal.Assert;
import org.apache.geode.internal.statistics.StatisticsTypeFactoryImpl;

/**
 * <P>
 * This class provides the interface for statistics about the OS X machine a GemFire system is
 * running on.
 */
public class OSXSystemStats {
  private StatisticsType myType;

  private void checkOffset(String name, int offset) {
    int id = myType.nameToId(name);
    Assert.assertTrue(offset == id,
        "Expected the offset for " + name + " to be " + offset + " but it was " + id);
  }

  private void initializeStats(StatisticsFactory factory) {
    myType = factory.createType("OSXSystemStats", "Statistics on an OS X machine.",
        new StatisticDescriptor[]{factory.createIntGauge("dummyStat",
            "Place holder statistic until Stats are implimented for the Mac OS X Platform.",
            "megabytes")});
  }

  public OSXSystemStats(StatisticsFactory factory) {
    initializeStats(factory);
  }

  public StatisticsType getType() {
    return myType;
  }
}
