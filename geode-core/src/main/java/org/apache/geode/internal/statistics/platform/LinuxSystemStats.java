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
 * This class provides the interface for statistics about the Linux machine a GemFire system is
 * running on.
 */
public class LinuxSystemStats {

  // shared fields
  static final int allocatedSwapINT = 0;
  static final int bufferMemoryINT = 1;
  static final int sharedMemoryINT = 2;
  static final int cpuActiveINT = 3;
  static final int cpuIdleINT = 4;
  static final int cpuNiceINT = 5;
  static final int cpuSystemINT = 6;
  static final int cpuUserINT = 7;
  static final int iowaitINT = 8;
  static final int irqINT = 9;
  static final int softirqINT = 10;
  static final int cpusINT = 11;
  static final int freeMemoryINT = 12;
  static final int physicalMemoryINT = 13;
  static final int processesINT = 14;
  static final int unallocatedSwapINT = 15;
  static final int cachedMemoryINT = 16;
  static final int dirtyMemoryINT = 17;
  static final int cpuNonUserINT = 18;
  static final int cpuStealINT = 19;

  static final int loopbackPacketsLONG = 0;
  static final int loopbackBytesLONG = 1;
  static final int recvPacketsLONG = 2;
  static final int recvBytesLONG = 3;
  static final int recvErrorsLONG = 4;
  static final int recvDropsLONG = 5;
  static final int xmitPacketsLONG = 6;
  static final int xmitBytesLONG = 7;
  static final int xmitErrorsLONG = 8;
  static final int xmitDropsLONG = 9;
  static final int xmitCollisionsLONG = 10;
  static final int contextSwitchesLONG = 11;
  static final int processCreatesLONG = 12;
  static final int pagesPagedInLONG = 13;
  static final int pagesPagedOutLONG = 14;
  static final int pagesSwappedInLONG = 15;
  static final int pagesSwappedOutLONG = 16;
  static final int readsCompletedLONG = 17;
  static final int readsMergedLONG = 18;
  static final int bytesReadLONG = 19;
  static final int timeReadingLONG = 20;
  static final int writesCompletedLONG = 21;
  static final int writesMergedLONG = 22;
  static final int bytesWrittenLONG = 23;
  static final int timeWritingLONG = 24;
  static final int iosInProgressLONG = 25;
  static final int timeIosInProgressLONG = 26;
  static final int ioTimeLONG = 27;

  static final int loadAverage1DOUBLE = 0;
  static final int loadAverage15DOUBLE = 1;
  static final int loadAverage5DOUBLE = 2;

  private StatisticsType myType;

  private void checkOffset(String name, int offset) {
    int id = myType.nameToId(name);
    Assert.assertTrue(offset == id,
        "Expected the offset for " + name + " to be " + offset + " but it was " + id);
  }

  private void initializeStats(StatisticsFactory factory) {
    myType = factory.createType("LinuxSystemStats", "Statistics on a Linux machine.",
        new StatisticDescriptor[] {
            factory.createIntGauge("allocatedSwap",
                "The number of megabytes of swap space have actually been written to. Swap space must be reserved before it can be allocated.",
                "megabytes"),
            factory.createIntGauge("bufferMemory",
                "The number of megabytes of memory allocated to buffers.", "megabytes"),
            factory.createIntGauge("sharedMemory",
                "The number of megabytes of shared memory on the machine.", "megabytes", true),
            factory.createIntGauge("cpuActive",
                "The percentage of the total available time that has been used in a non-idle state.",
                "%"),
            factory.createIntGauge("cpuIdle",
                "The percentage of the total available time that has been spent sleeping.", "%",
                true),
            factory.createIntGauge("cpuNice",
                "The percentage of the total available time that has been used to execute user code in processes with low priority.",
                "%"),
            factory.createIntGauge("cpuSystem",
                "The percentage of the total available time that has been used to execute system (i.e. kernel) code.",
                "%"),
            factory.createIntGauge("cpuUser",
                "The percentage of the total available time that has been used to execute user code.",
                "%"),
            factory.createIntGauge("iowait",
                "The percentage of the total available time that has been used to wait for I/O to complete.",
                "%"),
            factory.createIntGauge("irq",
                "The percentage of the total available time that has been used servicing  interrupts.",
                "%"),
            factory.createIntGauge("softirq",
                "The percentage of the total available time that has been used servicing softirqs.",
                "%"),
            factory.createIntGauge("cpus", "The number of online cpus on the local machine.", "items"),
            factory.createIntGauge("freeMemory",
                "The number of megabytes of unused memory on the machine.", "megabytes", true),
            factory.createIntGauge("physicalMemory",
                "The actual amount of total physical memory on the machine.", "megabytes", true),
            factory.createIntGauge("processes",
                "The number of processes in the computer at the time of data collection.  Notice that this is an instantaneous count, not an average over the time interval.  Each process represents the running of a program.",
                "processes"),
            factory.createIntGauge("unallocatedSwap",
                "The number of megabytes of swap space that have not been allocated.", "megabytes",
                true),
            factory.createIntGauge("cachedMemory",
                "The number of megabytes of memory used for the file system cache.", "megabytes",
                true),
            factory.createIntGauge("dirtyMemory",
                "The number of megabytes of memory in the file system cache that need to be written.",
                "megabytes", true),
            factory.createIntGauge("cpuNonUser",
                "The percentage of total available time that has been used to execute non-user code.(includes system, iowait, irq, softirq etc.)",
                "%"),
            factory.createIntGauge("cpuSteal",
                "Steal time is the amount of time the operating system wanted to execute, but was not allowed to by the hypervisor.",
                "%"),

            factory.createLongCounter("loopbackPackets",
                "The number of network packets sent (or received) on the loopback interface",
                "packets", false),
            factory.createLongCounter("loopbackBytes",
                "The number of network bytes sent (or received) on the loopback interface", "bytes",
                false),
            factory.createLongCounter("recvPackets",
                "The total number of network packets received (excluding loopback)", "packets",
                false),
            factory.createLongCounter("recvBytes",
                "The total number of network bytes received (excluding loopback)", "bytes", false),
            factory.createLongCounter("recvErrors", "The total number of network receive errors",
                "errors", false),
            factory.createLongCounter("recvDrops", "The total number network receives dropped", "packets",
                false),
            factory.createLongCounter("xmitPackets",
                "The total number of network packets transmitted (excluding loopback)", "packets",
                false),
            factory.createLongCounter("xmitBytes",
                "The total number of network bytes transmitted (excluding loopback)", "bytes",
                false),
            factory.createLongCounter("xmitErrors", "The total number of network transmit errors",
                "errors", false),
            factory.createLongCounter("xmitDrops", "The total number of network transmits dropped",
                "packets", false),
            factory.createLongCounter("xmitCollisions", "The total number of network transmit collisions",
                "collisions", false),
            factory.createLongCounter("contextSwitches",
                "The total number of context switches from one thread to another on the computer.  Thread switches can occur either inside of a single process or across processes.  A thread switch may be caused either by one thread asking another for information, or by a thread being preempted by another, higher priority thread becoming ready to run.",
                "operations", false),
            factory.createLongCounter("processCreates",
                "The total number of times a process has been created.", "operations", false),
            factory.createLongCounter("pagesPagedIn",
                "The total number of pages that have been brought into memory from disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("pagesPagedOut",
                "The total number of pages that have been flushed from memory to disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("pagesSwappedIn",
                "The total number of swap pages that have been read in from disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("pagesSwappedOut",
                "The total number of swap pages that have been written out to disk by the operating system's memory manager.",
                "pages", false),
            factory.createLongCounter("diskReadsCompleted",
                "The total number disk read operations completed successfully", "ops"),
            factory.createLongCounter("diskReadsMerged",
                "The total number disk read operations that were able to be merge with adjacent reads for efficiency",
                "ops"),
            factory.createLongCounter("diskBytesRead",
                "The total number bytes read from disk successfully", "bytes"),
            factory.createLongCounter("diskTimeReading",
                "The total number of milliseconds spent reading from disk", "milliseconds"),
            factory.createLongCounter("diskWritesCompleted",
                "The total number disk write operations completed successfully", "ops"),
            factory.createLongCounter("diskWritesMerged",
                "The total number disk write operations that were able to be merge with adjacent reads for efficiency",
                "ops"),
            factory.createLongCounter("diskBytesWritten",
                "The total number bytes written to disk successfully", "bytes"),
            factory.createLongCounter("diskTimeWriting",
                "The total number of milliseconds spent writing to disk", "milliseconds"),
            factory.createLongGauge("diskOpsInProgress",
                "The current number of disk operations in progress", "ops"),
            factory.createLongCounter("diskTimeInProgress",
                "The total number of milliseconds spent with disk ops in progress", "milliseconds"),
            factory.createLongCounter("diskTime",
                "The total number of milliseconds that measures both completed disk operations and any accumulating backlog of in progress ops.",
                "milliseconds"),


            factory.createDoubleGauge("loadAverage1",
                "The average number of threads in the run queue or waiting for disk I/O over the last minute.",
                "threads"),
            factory.createDoubleGauge("loadAverage15",
                "The average number of threads in the run queue or waiting for disk I/O over the last fifteen minutes.",
                "threads"),
            factory.createDoubleGauge("loadAverage5",
                "The average number of threads in the run queue or waiting for disk I/O over the last five minutes.",
                "threads"),});

    checkOffset("allocatedSwap", allocatedSwapINT);
    checkOffset("bufferMemory", bufferMemoryINT);
    checkOffset("sharedMemory", sharedMemoryINT);
    checkOffset("cpuActive", cpuActiveINT);
    checkOffset("cpuIdle", cpuIdleINT);
    checkOffset("cpuNice", cpuNiceINT);
    checkOffset("cpuSystem", cpuSystemINT);
    checkOffset("cpuUser", cpuUserINT);
    checkOffset("iowait", iowaitINT);
    checkOffset("irq", irqINT);
    checkOffset("softirq", softirqINT);
    checkOffset("cpus", cpusINT);
    checkOffset("freeMemory", freeMemoryINT);
    checkOffset("physicalMemory", physicalMemoryINT);
    checkOffset("processes", processesINT);
    checkOffset("unallocatedSwap", unallocatedSwapINT);
    checkOffset("cachedMemory", cachedMemoryINT);
    checkOffset("dirtyMemory", dirtyMemoryINT);
    checkOffset("cpuNonUser", cpuNonUserINT);
    checkOffset("cpuSteal", cpuStealINT);

    checkOffset("loopbackPackets", loopbackPacketsLONG);
    checkOffset("loopbackBytes", loopbackBytesLONG);
    checkOffset("recvPackets", recvPacketsLONG);
    checkOffset("recvBytes", recvBytesLONG);
    checkOffset("recvErrors", recvErrorsLONG);
    checkOffset("recvDrops", recvDropsLONG);
    checkOffset("xmitPackets", xmitPacketsLONG);
    checkOffset("xmitBytes", xmitBytesLONG);
    checkOffset("xmitErrors", xmitErrorsLONG);
    checkOffset("xmitDrops", xmitDropsLONG);
    checkOffset("xmitCollisions", xmitCollisionsLONG);
    checkOffset("contextSwitches", contextSwitchesLONG);
    checkOffset("processCreates", processCreatesLONG);
    checkOffset("pagesPagedIn", pagesPagedInLONG);
    checkOffset("pagesPagedOut", pagesPagedOutLONG);
    checkOffset("pagesSwappedIn", pagesSwappedInLONG);
    checkOffset("pagesSwappedOut", pagesSwappedOutLONG);
    checkOffset("diskReadsCompleted", readsCompletedLONG);
    checkOffset("diskReadsMerged", readsMergedLONG);
    checkOffset("diskBytesRead", bytesReadLONG);
    checkOffset("diskTimeReading", timeReadingLONG);
    checkOffset("diskWritesCompleted", writesCompletedLONG);
    checkOffset("diskWritesMerged", writesMergedLONG);
    checkOffset("diskBytesWritten", bytesWrittenLONG);
    checkOffset("diskTimeWriting", timeWritingLONG);
    checkOffset("diskOpsInProgress", iosInProgressLONG);
    checkOffset("diskTimeInProgress", timeIosInProgressLONG);
    checkOffset("diskTime", ioTimeLONG);

    checkOffset("loadAverage1", loadAverage1DOUBLE);
    checkOffset("loadAverage15", loadAverage15DOUBLE);
    checkOffset("loadAverage5", loadAverage5DOUBLE);
  }

  public LinuxSystemStats(StatisticsFactory factory) {
    initializeStats(factory);
  }

  public StatisticsType getType(){
    return myType;
  }
}
