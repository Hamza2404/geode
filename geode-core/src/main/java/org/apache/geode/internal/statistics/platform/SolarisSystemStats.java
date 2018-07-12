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
 * This class provides the interface for statistics about the Solaris machine a GemFire system is
 * running on.
 */
public class SolarisSystemStats {
  private static final int allocatedSwapINT = 0;
  private static final int cpuActiveINT = 1;
  private static final int cpuIdleINT = 2;
  private static final int cpuIoWaitINT = 3;
  private static final int cpuSwapWaitINT = 4;
  private static final int cpuSystemINT = 5;
  private static final int cpuUserINT = 6;
  private static final int cpuWaitingINT = 7;
  private static final int cpusINT = 8;
  private static final int freeMemoryINT = 9;
  private static final int physicalMemoryINT = 10;
  private static final int processesINT = 11;
  private static final int reservedSwapINT = 12;
  private static final int schedulerRunCountINT = 13;
  private static final int schedulerSwapCountINT = 14;
  private static final int schedulerWaitCountINT = 15;
  private static final int unreservedSwapINT = 16;
  private static final int unallocatedSwapINT = 17;

  private static final int anonymousPagesFreedLONG = 0;
  private static final int anonymousPagesPagedInLONG = 1;
  private static final int anonymousPagesPagedOutLONG = 2;
  private static final int contextSwitchesLONG = 3;
  private static final int execPagesFreedLONG = 4;
  private static final int execPagesPagedInLONG = 5;
  private static final int execPagesPagedOutLONG = 6;
  private static final int failedMutexEntersLONG = 7;
  private static final int failedReaderLocksLONG = 8;
  private static final int failedWriterLocksLONG = 9;
  private static final int fileSystemPagesFreedLONG = 10;
  private static final int fileSystemPagesPagedInLONG = 11;
  private static final int fileSystemPagesPagedOutLONG = 12;
  private static final int hatMinorFaultsLONG = 13;
  private static final int interruptsLONG = 14;
  private static final int involContextSwitchesLONG = 15;
  private static final int majorPageFaultsLONG = 16;
  private static final int messageCountLONG = 17;
  private static final int pageDaemonCyclesLONG = 18;
  private static final int pageInsLONG = 19;
  private static final int pageOutsLONG = 20;
  private static final int pagerRunsLONG = 21;
  private static final int pagesPagedInLONG = 22;
  private static final int pagesPagedOutLONG = 23;
  private static final int pagesScannedLONG = 24;
  private static final int procsInIoWaitLONG = 25;
  private static final int protectionFaultsLONG = 26;
  private static final int semphoreOpsLONG = 27;
  private static final int softwareLockFaultsLONG = 28;
  private static final int systemCallsLONG = 29;
  private static final int systemMinorFaultsLONG = 30;
  private static final int threadCreatesLONG = 31;
  private static final int trapsLONG = 32;
  private static final int userMinorFaultsLONG = 33;
  private static final int loopbackInputPacketsLONG = 34;
  private static final int loopbackOutputPacketsLONG = 35;
  private static final int inputPacketsLONG = 36;
  private static final int inputErrorsLONG = 37;
  private static final int outputPacketsLONG = 38;
  private static final int outputErrorsLONG = 39;
  private static final int collisionsLONG = 40;
  private static final int inputBytesLONG = 41;
  private static final int outputBytesLONG = 42;
  private static final int multicastInputPacketsLONG = 43;
  private static final int multicastOutputPacketsLONG = 44;
  private static final int broadcastInputPacketsLONG = 45;
  private static final int broadcastOutputPacketsLONG = 46;
  private static final int inputPacketsDiscardedLONG = 47;
  private static final int outputPacketsDiscardedLONG = 48;

  private static final int loadAverage1DOUBLE = 0;
  private static final int loadAverage15DOUBLE = 1;
  private static final int loadAverage5DOUBLE = 2;

  private StatisticsType myType;

  private void checkOffset(String name, int offset) {
    int id = myType.nameToId(name);
    Assert.assertTrue(offset == id,
        "Expected the offset for " + name + " to be " + offset + " but it was " + id);
  }

  private void initializeStats(StatisticsFactory factory) {
    myType = factory.createType("SolarisSystemStats", "Statistics on a Solaris machine.",
        new StatisticDescriptor[] {
            factory.createIntGauge("allocatedSwap",
                "The number of megabytes of swap space have actually been written to. Swap space must be reserved before it can be allocated.",
                "megabytes"),
            factory.createIntGauge("cpuActive",
                "The percentage of the total available time that has been used to execute user or system code.",
                "%"),
            factory.createIntGauge("cpuIdle",
                "The percentage of the total available time that has been spent sleeping.", "%",
                true),
            factory.createIntGauge("cpuIoWait",
                "The percentage of the total available time that has been spent waiting for disk io to complete.",
                "%"),
            factory.createIntGauge("cpuSwapWait",
                "The percentage of the total available time that has been spent waiting for paging and swapping to complete.",
                "%"),
            factory.createIntGauge("cpuSystem",
                "The percentage of the total available time that has been used to execute system (i.e. kernel) code.",
                "%"),
            factory.createIntGauge("cpuUser",
                "The percentage of the total available time that has been used to execute user code.",
                "%"),
            factory.createIntGauge("cpuWaiting",
                "The percentage of the total available time that has been spent waiting for io, paging, or swapping.",
                "%"),
            factory.createIntGauge("cpus", "The number of online cpus on the local machine.", "items"),
            factory.createIntGauge("freeMemory",
                "The number of megabytes of unused memory on the machine.", "megabytes", true),
            factory.createIntGauge("physicalMemory",
                "The actual amount of total physical memory on the machine.", "megabytes", true),
            factory.createIntGauge("processes",
                "The number of processes in the computer at the time of data collection.  Notice that this is an instantaneous count, not an average over the time interval.  Each process represents the running of a program.",
                "processes"),
            factory.createIntGauge("reservedSwap",
                "The number of megabytes of swap space reserved for allocation by a particular process.",
                "megabytes"),
            factory.createIntCounter("schedulerRunCount",
                "The total number of times the system scheduler has put a thread in its run queue.",
                "operations", false),
            factory.createIntCounter("schedulerSwapCount",
                "The total number of times the system scheduler has swapped out an idle process.",
                "operations", false),
            factory.createIntCounter("schedulerWaitCount",
                "The total number of times the system scheduler has removed a thread from the run queue because it was waiting for a resource.",
                "operations", false),
            factory.createIntGauge("unreservedSwap",
                "The number of megabytes of swap space that are free. If this value goes to zero new processes can no longer be created.",
                "megabytes", true),
            factory.createIntGauge("unallocatedSwap",
                "The number of megabytes of swap space that have not been allocated.", "megabytes",
                true),


            factory.createLongCounter("anonymousPagesFreed",
                "The total number pages that contain heap, stack, or other changeable data that have been removed from memory and added to the free list.",
                "pages"),
            factory.createLongCounter("anonymousPagesPagedIn",
                "The total number pages that contain heap, stack, or other changeable data that have been allocated in memory and possibly copied from disk.",
                "pages", false),
            factory.createLongCounter("anonymousPagesPagedOut",
                "The total number pages that contain heap, stack, or other changeable data that have been removed from memory and copied to disk.",
                "pages", false),
            factory.createLongCounter("contextSwitches",
                "The total number of context switches from one thread to another on the computer.  Thread switches can occur either inside of a single process or across processes.  A thread switch may be caused either by one thread asking another for information, or by a thread being preempted by another, higher priority thread becoming ready to run.",
                "operations", false),
            factory.createLongCounter("execPagesFreed",
                "The total number readonly pages that contain code or data that have been removed from memory and returned to the free list.",
                "pages"),
            factory.createLongCounter("execPagesPagedIn",
                "The total number readonly pages that contain code or data that have been copied from disk to memory.",
                "pages", false),
            factory.createLongCounter("execPagesPagedOut",
                "The total number readonly pages that contain code or data that have been removed from memory and will need to be paged in when used again.",
                "pages", false),
            factory.createLongCounter("failedMutexEnters",
                "The total number of times a thread entering a mutex had to wait for the mutex to be unlocked.",
                "operations", false),
            factory.createLongCounter("failedReaderLocks",
                "The total number of times readers failed to obtain a readers/writer locks on their first try. When this happens the reader has to wait for the current writer to release the lock.",
                "operations", false),
            factory.createLongCounter("failedWriterLocks",
                "The total number of times writers failed to obtain a readers/writer locks on their first try. When this happens the writer has to wait for all the current readers or the single writer to release the lock.",
                "operations", false),
            factory.createLongCounter("fileSystemPagesFreed",
                "The total number of pages, that contained the contents of a file due to the file being read from a file system, that   have been removed from memory and put on the free list.",
                "pages"),
            factory.createLongCounter("fileSystemPagesPagedIn",
                "The total number of pages that contain the contents of a file due to the file being read from a file system.",
                "pages", false),
            factory.createLongCounter("fileSystemPagesPagedOut",
                "The total number of pages, that contained the contents of a file due to the file being read from a file system, that   have been removed from memory and copied to disk.",
                "pages", false),
            factory.createLongCounter("hatMinorFaults",
                "The total number of hat faults. You only get these on systems with software memory management units.",
                "operations", false),
            factory.createLongCounter("interrupts",
                "The total number of interrupts that have occurred on the computer.", "operations",
                false),
            factory.createLongCounter("involContextSwitches",
                "The total number of times a thread was forced to give up the cpu even though it was still ready to run.",
                "operations", false),
            factory.createLongCounter("majorPageFaults",
                "The total number of times a page fault required disk io to get the page",
                "operations", false),
            factory.createLongCounter("messageCount",
                "The total number of msgrcv() and msgsnd() system calls.", "messages"),
            factory.createLongCounter("pageDaemonCycles",
                "The total number of revolutions of the page daemon's scan \"clock hand\".",
                "operations", false),
            factory.createLongCounter("pageIns",
                "The total number of times pages have been brought into memory from disk by the operating system's memory manager.",
                "operations", false),
            factory.createLongCounter("pageOuts",
                "The total number of times pages have been flushed from memory to disk by the operating system's memory manager.",
                "operations", false),
            factory.createLongCounter("pagerRuns",
                "The total number of times the pager daemon has been scheduled to run.",
                "operations", false),
            factory.createLongCounter("pagesPagedIn",
                "The total number of pages that have been brought into memory from disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("pagesPagedOut",
                "The total number of pages that have been flushed from memory to disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("pagesScanned",
                "The total number pages examined by the pageout daemon. When the amount of free memory gets below a certain size, the daemon start to look for inactive memory pages to steal from processes. So I high scan rate is a good indication of needing more memory.",
                "pages", false),
            factory.createLongGauge("procsInIoWait",
                "The number of processes waiting for block I/O at this instant in time.",
                "processes"),
            factory.createLongCounter("protectionFaults",
                "The total number of times memory has been accessed in a way that was not allowed. This results in a segementation violation and in most cases a core dump.",
                "operations", false),
            factory.createLongCounter("semphoreOps", "The total number of semaphore operations.",
                "operations"),
            factory.createLongCounter("softwareLockFaults",
                "The total number of faults caused by software locks held on memory pages.",
                "operations", false),
            factory.createLongCounter("systemCalls", "The total number system calls.", "operations"),
            factory.createLongCounter("systemMinorFaults",
                "The total number of minor page faults in kernel code. Minor page faults do not require disk access.",
                "operations", false),
            factory.createLongCounter("threadCreates",
                "The total number of times a thread has been created.", "operations", false),
            factory.createLongCounter("traps",
                "The total number of traps that have occurred on the computer.", "operations",
                false),
            factory.createLongCounter("userMinorFaults",
                "The total number of minor page faults in non-kernel code. Minor page faults do not require disk access.",
                "operatations", false),
            factory.createLongCounter("loopbackInputPackets",
                "The total number of input packets received over the loopback network adaptor.",
                "packets"),
            factory.createLongCounter("loopbackOutputPackets",
                "The total number of output packets sent over the loopback network adaptor.",
                "packets"),
            factory.createLongCounter("inputPackets", "packets received (Solaris kstat 'ipackets')",
                "packets"),
            factory.createLongCounter("inputErrors", "input errors (Solaris kstat 'ierrors')", "errors",
                false),
            factory.createLongCounter("outputPackets", "Solaris kstat 'opackets'", "packets"),
            factory.createLongCounter("outputErrors", "output errors (Solaris kstat 'oerrors')", "errors",
                false),
            factory.createLongCounter("collisions", "Solaris kstat 'collisions'", "collisions", false),
            factory.createLongCounter("inputBytes", "octets received (Solaris kstat 'rbytes')", "bytes"),
            factory.createLongCounter("outputBytes", "octats transmitted (Solaris kstat 'obytes')",
                "bytes"),
            factory.createLongCounter("multicastInputPackets",
                "multicast received (Solaris kstat 'multircv')", "packets"),
            factory.createLongCounter("multicastOutputPackets",
                "multicast requested to be sent (Solaris kstat 'multixmt')", "packets"),
            factory.createLongCounter("broadcastInputPackets",
                "broadcast received (Solaris kstat 'brdcstrcv')", "packets"),
            factory.createLongCounter("broadcastOutputPackets",
                "broadcast requested to be sent (Solaris kstat 'brdcstxmt')", "packets"),
            factory.createLongCounter("inputPacketsDiscarded",
                "number receive packets discarded (Solaris kstat 'norcvbuf')", "packets"),
            factory.createLongCounter("outputPacketsDiscarded",
                "packets that could not be sent up because the queue was flow controlled (Solaris kstat 'noxmtbuf')",
                "packets"),


            factory.createDoubleGauge("loadAverage1",
                "The average number of threads ready to run over the last minute.", "threads"),
            factory.createDoubleGauge("loadAverage15",
                "The average number of threads ready to run over the last fifteen minutes.",
                "threads"),
            factory.createDoubleGauge("loadAverage5",
                "The average number of threads ready to run over the last five minutes.",
                "threads")});
    checkOffset("allocatedSwap", allocatedSwapINT);
    checkOffset("cpuActive", cpuActiveINT);
    checkOffset("cpuIdle", cpuIdleINT);
    checkOffset("cpuIoWait", cpuIoWaitINT);
    checkOffset("cpuSwapWait", cpuSwapWaitINT);
    checkOffset("cpuSystem", cpuSystemINT);
    checkOffset("cpuUser", cpuUserINT);
    checkOffset("cpuWaiting", cpuWaitingINT);
    checkOffset("cpus", cpusINT);
    checkOffset("freeMemory", freeMemoryINT);
    checkOffset("physicalMemory", physicalMemoryINT);
    checkOffset("processes", processesINT);
    checkOffset("reservedSwap", reservedSwapINT);
    checkOffset("schedulerRunCount", schedulerRunCountINT);
    checkOffset("schedulerSwapCount", schedulerSwapCountINT);
    checkOffset("schedulerWaitCount", schedulerWaitCountINT);
    checkOffset("unreservedSwap", unreservedSwapINT);
    checkOffset("unallocatedSwap", unallocatedSwapINT);

    checkOffset("anonymousPagesFreed", anonymousPagesFreedLONG);
    checkOffset("anonymousPagesPagedIn", anonymousPagesPagedInLONG);
    checkOffset("anonymousPagesPagedOut", anonymousPagesPagedOutLONG);
    checkOffset("contextSwitches", contextSwitchesLONG);
    checkOffset("execPagesFreed", execPagesFreedLONG);
    checkOffset("execPagesPagedIn", execPagesPagedInLONG);
    checkOffset("execPagesPagedOut", execPagesPagedOutLONG);
    checkOffset("failedMutexEnters", failedMutexEntersLONG);
    checkOffset("failedReaderLocks", failedReaderLocksLONG);
    checkOffset("failedWriterLocks", failedWriterLocksLONG);
    checkOffset("fileSystemPagesFreed", fileSystemPagesFreedLONG);
    checkOffset("fileSystemPagesPagedIn", fileSystemPagesPagedInLONG);
    checkOffset("fileSystemPagesPagedOut", fileSystemPagesPagedOutLONG);
    checkOffset("hatMinorFaults", hatMinorFaultsLONG);
    checkOffset("interrupts", interruptsLONG);
    checkOffset("involContextSwitches", involContextSwitchesLONG);
    checkOffset("majorPageFaults", majorPageFaultsLONG);
    checkOffset("messageCount", messageCountLONG);
    checkOffset("pageDaemonCycles", pageDaemonCyclesLONG);
    checkOffset("pageIns", pageInsLONG);
    checkOffset("pageOuts", pageOutsLONG);
    checkOffset("pagerRuns", pagerRunsLONG);
    checkOffset("pagesPagedIn", pagesPagedInLONG);
    checkOffset("pagesPagedOut", pagesPagedOutLONG);
    checkOffset("pagesScanned", pagesScannedLONG);
    checkOffset("procsInIoWait", procsInIoWaitLONG);
    checkOffset("protectionFaults", protectionFaultsLONG);
    checkOffset("semphoreOps", semphoreOpsLONG);
    checkOffset("softwareLockFaults", softwareLockFaultsLONG);
    checkOffset("systemCalls", systemCallsLONG);
    checkOffset("systemMinorFaults", systemMinorFaultsLONG);
    checkOffset("threadCreates", threadCreatesLONG);
    checkOffset("traps", trapsLONG);
    checkOffset("userMinorFaults", userMinorFaultsLONG);
    checkOffset("loopbackInputPackets", loopbackInputPacketsLONG);
    checkOffset("loopbackOutputPackets", loopbackOutputPacketsLONG);
    checkOffset("inputPackets", inputPacketsLONG);
    checkOffset("inputErrors", inputErrorsLONG);
    checkOffset("outputPackets", outputPacketsLONG);
    checkOffset("outputErrors", outputErrorsLONG);
    checkOffset("collisions", collisionsLONG);
    checkOffset("inputBytes", inputBytesLONG);
    checkOffset("outputBytes", outputBytesLONG);
    checkOffset("multicastInputPackets", multicastInputPacketsLONG);
    checkOffset("multicastOutputPackets", multicastOutputPacketsLONG);
    checkOffset("broadcastInputPackets", broadcastInputPacketsLONG);
    checkOffset("broadcastOutputPackets", broadcastOutputPacketsLONG);
    checkOffset("inputPacketsDiscarded", inputPacketsDiscardedLONG);
    checkOffset("outputPacketsDiscarded", outputPacketsDiscardedLONG);

    checkOffset("loadAverage1", loadAverage1DOUBLE);
    checkOffset("loadAverage15", loadAverage15DOUBLE);
    checkOffset("loadAverage5", loadAverage5DOUBLE);
  }

  public SolarisSystemStats(StatisticsFactory factory) {
    initializeStats(factory);
  }

  public StatisticsType getType() {
    return myType;
  }
}
