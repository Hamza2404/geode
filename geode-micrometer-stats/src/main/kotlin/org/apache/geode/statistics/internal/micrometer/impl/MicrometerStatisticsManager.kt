package org.apache.geode.statistics.internal.micrometer.impl

import com.sun.net.httpserver.HttpServer
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.core.instrument.composite.CompositeMeterRegistry
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.config.MeterFilterReply
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.apache.geode.statistics.internal.micrometer.StatisticsManager
import org.apache.geode.statistics.internal.micrometer.StatisticsMeterGroup
import java.io.IOException
import java.lang.management.ManagementFactory
import java.net.InetSocketAddress

object MicrometerStatisticsManager : StatisticsManager {

    //    @JvmOverloads constructor(private val enableStats: Boolean = true,
//                                                            private val serverName: String = "cacheServer" + InetAddress.getLocalHost().hostAddress,
//                                                            vararg meterRegistries: MeterRegistry,
//                                                            private val meterRegistry: CompositeMeterRegistry =
//                                                                    CompositeMeterRegistry(Clock.SYSTEM)) : StatisticsManager {
    private val registeredMeterGroups = mutableMapOf<String, MicrometerMeterGroup>()
    private val meterRegistry: CompositeMeterRegistry = createCompositeRegistry()
    private var serverName: String = "cacheServer_" + ManagementFactory.getRuntimeMXBean().name
    private var enableStats: Boolean = true

    fun registerMeterRegistries(vararg meterRegistries: MeterRegistry) {
        meterRegistries.forEach { meterRegistry.add(it) }
    }

    fun disableStatsCollection() {
        enableStats = false
    }


    init {
//        meterRegistries.forEach { meterRegistry.add(it) }
        meterRegistry.config().commonTags("serverName", serverName)
        JvmGcMetrics().bindTo(meterRegistry)
        JvmThreadMetrics().bindTo(meterRegistry)
        JvmMemoryMetrics().bindTo(meterRegistry)
        ClassLoaderMetrics().bindTo(meterRegistry)
        FileDescriptorMetrics().bindTo(meterRegistry)
        ProcessorMetrics().bindTo(meterRegistry)
        UptimeMetrics().bindTo(meterRegistry)
//        val procOSReaderFactory = ProcOSReaderFactory()
//        LoadAvgMetrics(procOSLoadAvg = ProcOSLoadAvg(procOSReaderFactory.getInstance(LoadAvgMetrics.LOAD_AVG)))
//        MemInfoMetrics(procOSMemInfo = ProcOSMemInfo(procOSReaderFactory.getInstance(MemInfoMetrics.MEM_INFO)))
//        StatMetrics(procOSStat = ProcOSStat(procOSReaderFactory.getInstance(StatMetrics.STAT)))
    }

    override fun registerMeterRegistry(meterRegistry: MeterRegistry) {
        this.meterRegistry.add(meterRegistry)
    }

    override fun registerMeterGroup(groupName: String, meterGroup: StatisticsMeterGroup) {
        if (meterGroup is MicrometerMeterGroup) {
            registeredMeterGroups.putIfAbsent(groupName, meterGroup)
                    ?.run { println("MeterGroup: $groupName was already registered") }
            if (!enableStats) {
                meterRegistry.config().meterFilter(object : MeterFilter {
                    override fun accept(id: Meter.Id): MeterFilterReply {
                        return MeterFilterReply.DENY
                    }
                })
            }
            meterGroup.bindTo(meterRegistry)
        } else {
            TODO("Register Non-MircometerMeterGrouops, this feature is not yet supported. Most likely never will be")
        }
    }

    fun createWithRegistries(meterRegistries: Array<out MeterRegistry>): MicrometerStatisticsManager {
        registerMeterRegistries(*meterRegistries)
        return this
    }

    private fun createCompositeRegistry(): CompositeMeterRegistry {
        val compositeMeterRegistry = CompositeMeterRegistry(Clock.SYSTEM)
//        compositeMeterRegistry.add(createInfluxDB())
        compositeMeterRegistry.add(createPrometheus())
//        compositeMeterRegistry.add(createJMX())
        return compositeMeterRegistry
    }

//    private fun createJMX(): JmxMeterRegistry {
//        return JmxMeterRegistry(JmxConfig { null }, Clock.SYSTEM)
//    }

//    private fun createInfluxDB(): InfluxMeterRegistry {
//        val config = object : InfluxConfig {
//            override fun step(): Duration = Duration.ofSeconds(1)
//            override fun db(): String = "mydb2"
//            override fun get(k: String): String? = null
//        }
//        return InfluxMeterRegistry(config, Clock.SYSTEM)
//    }

    private fun createPrometheus(): PrometheusMeterRegistry {
        val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        try {
            val port = System.getProperty("statsPort")?.toInt() ?: 10080
            val server = HttpServer.create(InetSocketAddress(port), 0)
            server.createContext("/geode") {
                val response = prometheusRegistry.scrape()
                it.sendResponseHeaders(200, response.toByteArray().size.toLong())
                it.responseBody?.run { this.write(response.toByteArray()) }
            }
            Thread(server::start).start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return prometheusRegistry
    }
}