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
package org.apache.geode.internal.admin.remote;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.geode.DataSerializer;
import org.apache.geode.internal.statistics.InternalDistributedSystemStats;
import org.apache.geode.statistics.StatisticDescriptor;
import org.apache.geode.statistics.Statistics;
import org.apache.geode.statistics.StatisticsType;
import org.apache.geode.distributed.internal.DistributionManager;
import org.apache.geode.distributed.internal.InternalDistributedSystem;
import org.apache.geode.distributed.internal.membership.InternalDistributedMember;

public class FetchResourceAttributesResponse extends AdminResponse {

  private RemoteStat[] stats;

  public static FetchResourceAttributesResponse create(DistributionManager distributionManager,
      InternalDistributedMember recipient, long rsrcUniqueId) {
    FetchResourceAttributesResponse attributesResponse = new FetchResourceAttributesResponse();
    attributesResponse.setRecipient(recipient);
    Statistics statistics = null;
    InternalDistributedSystemStats internalDistributedSystemStats =
        distributionManager.getSystem().getInternalDistributedSystemStats();
    statistics = internalDistributedSystemStats.findStatisticsByUniqueId(rsrcUniqueId);
    if (statistics != null) {
      StatisticsType type = statistics.getType();
      StatisticDescriptor[] statisticDescriptors = type.getStatistics();
      attributesResponse.stats = new RemoteStat[statisticDescriptors.length];
      for (int i = 0; i < statisticDescriptors.length; i++) {
        attributesResponse.stats[i] = new RemoteStat(statistics, statisticDescriptors[i]);
      }
    }
    if (attributesResponse.stats == null) {
      attributesResponse.stats = new RemoteStat[0];
    }
    return attributesResponse;
  }

  public RemoteStat[] getStats() {
    return stats;
  }

  /**
   * Constructor required by {@code DataSerializable}
   */
  public FetchResourceAttributesResponse() {
    // nothing
  }

  public int getDSFID() {
    return FETCH_RESOURCE_ATTRIBUTES_RESPONSE;
  }

  @Override
  public void toData(DataOutput out) throws IOException {
    super.toData(out);
    DataSerializer.writeObject(stats, out);
  }

  @Override
  public void fromData(DataInput in) throws IOException, ClassNotFoundException {
    super.fromData(in);
    stats = (RemoteStat[]) DataSerializer.readObject(in);
  }

  @Override
  public String toString() {
    return "FetchResourceAttributesResponse from " + getRecipient();
  }
}
