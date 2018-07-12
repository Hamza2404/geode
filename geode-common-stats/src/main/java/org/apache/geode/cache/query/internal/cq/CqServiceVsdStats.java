package org.apache.geode.cache.query.internal.cq;

public interface CqServiceVsdStats {
  void close();

  long getNumCqsCreated();

  void incCqsCreated();

  long getNumCqsActive();

  void incCqsActive();

  void decCqsActive();

  long getNumCqsStopped();

  void incCqsStopped();

  void decCqsStopped();

  long getNumCqsClosed();

  void incCqsClosed();

  long getNumCqsOnClient();

  void incCqsOnClient();

  void decCqsOnClient();

  long getNumClientsWithCqs();

  void incClientsWithCqs();

  void decClientsWithCqs();

  long startCqQueryExecution();

  void endCqQueryExecution(long start);

  long getCqQueryExecutionTime();

  void incUniqueCqQuery();

  void decUniqueCqQuery();
}
