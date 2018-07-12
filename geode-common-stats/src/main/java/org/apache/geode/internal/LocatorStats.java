package org.apache.geode.distributed.internal;

import org.apache.geode.statistics.StatisticsFactory;

public interface LocatorStats {

  void hookupStats(StatisticsFactory f, String name);

  void setServerCount(int sc);

  void setLocatorCount(int lc);

  void endLocatorRequest(long startTime);

  void endLocatorResponse(long startTime);

  void setLocatorRequests(long rl);

  void setLocatorResponses(long rl);

  void setServerLoadUpdates(long v);

  void incServerLoadUpdates();

  void incRequestInProgress(int threads);

  void close();
}
