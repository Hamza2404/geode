package org.apache.geode.distributed.internal.locks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.ExpirationAction;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.Scope;
import org.apache.geode.internal.cache.DistributedRegion;
import org.apache.geode.test.junit.categories.UnitTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

@Category(UnitTest.class)
public class DLockServiceJUnitTest {

  private Cache cache;
  private ExecutorService executorService;
  private DistributedRegion testRegion;

  @Test
  public void basicDLockUsage() throws InterruptedException, ExecutionException, TimeoutException {
    Lock lock = testRegion.getDistributedLock("testLockName");
    lock.lockInterruptibly();

    Future<Boolean>
        future =
        executorService.submit(() -> lock.tryLock());
    assertFalse("should not be able to get lock from another thread",
        future.get(5, TimeUnit.SECONDS));

    assertTrue("Lock is reentrant", lock.tryLock());
    // now locked twice.

    future =
        executorService.submit(() -> lock.tryLock());
    assertFalse("should not be able to get lock from another thread",
        future.get(5, TimeUnit.SECONDS));

    lock.unlock();

    future = executorService.submit(() -> lock.tryLock());
    assertFalse("should not be able to get lock from another thread", future.get());

    lock.unlock();

    future = executorService.submit(() -> {
      boolean locked = lock.tryLock();
      if (!locked) {
        return false;
      }
      lock.unlock();
      return true;
    });
    assertTrue("Another thread can now take out the lock", future.get(5, TimeUnit.SECONDS));

    DLockService lockService = (DLockService) testRegion.getLockService();
    Collection<DLockToken> tokens = lockService.getTokens();

    assertEquals(1, tokens.size());

    for (DLockToken token : tokens) {
      assertEquals(0, token.getUsageCount());
    }
  }

  @Before
  public void setUp() {
    cache = new CacheFactory().create();
    testRegion = (DistributedRegion) cache.createRegionFactory(RegionShortcut.REPLICATE)
        .setScope(Scope.GLOBAL)
        .setEntryTimeToLive(new ExpirationAttributes(1, ExpirationAction.DESTROY))
        .create("testRegion");
    testRegion.becomeLockGrantor();

    executorService = Executors.newFixedThreadPool(5);
  }

  @After
  public void tearDown() {
    cache.close();
    executorService.shutdownNow();
  }

  @Test
  public void multipleThreadsWithCache() {
    LinkedList<Future> futures = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      futures.add(executorService.submit(this::putTestKey));
    }

    futures.stream().forEach(future -> {
      try {
        future.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    });

    DLockService lockService = (DLockService) testRegion.getLockService();
    Collection<DLockToken> tokens = lockService.getTokens();

    assertEquals(1, tokens.size());

    for (DLockToken token : tokens) {
      assertEquals(0, token.getUsageCount());
    }
  }

  private void putTestKey() {
    for (int i = 0; i < 1000; i++) {
      testRegion.put("testKey", "testValue");
    }
  }
}