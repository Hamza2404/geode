package org.apache.geode.internal.cache;

public interface PoolStats {
  static long getStatTime() {
    return System.nanoTime();
  }

  void close();

  long startTime();

  void setInitialContacts(int ic);

  void setServerCount(int sc);

  void setSubscriptionCount(int qc);

  void setLocatorCount(int lc);

  long getLocatorRequests();

  void incLocatorRequests();

  void incLocatorResponses();

  void setLocatorRequests(long rl);

  void setLocatorResponses(long rl);

  // public void incConCount(int delta) {
  // this._stats.incInt(conCountId, delta);
  // }
  void incConnections(int delta);

  void incPoolConnections(int delta);

  int getPoolConnections();

  int getConnects();

  int getDisconnects();

  void incPrefillConnect();

  int getLoadConditioningCheck();

  void incLoadConditioningCheck();

  int getLoadConditioningExtensions();

  void incLoadConditioningExtensions();

  void incIdleCheck();

  int getLoadConditioningConnect();

  void incLoadConditioningConnect();

  int getLoadConditioningReplaceTimeouts();

  void incLoadConditioningReplaceTimeouts();

  int getLoadConditioningDisconnect();

  void incLoadConditioningDisconnect();

  int getIdleExpire();

  void incIdleExpire(int delta);

  long beginConnectionWait();

  void endConnectionWait(long start);

  void startClientOp();

  void endClientOpSend(long duration, boolean failed);

  void endClientOp(long duration, boolean timedOut, boolean failed);
}
