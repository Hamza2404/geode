package org.apache.geode.statistics.micrometer

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.apache.geode.statistics.StatisticDescriptor
import org.apache.geode.statistics.Statistics
import org.apache.geode.statistics.StatisticsFactory
import org.apache.geode.statistics.StatisticsType
import org.apache.geode.statistics.internal.micrometer.impl.CounterStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.GaugeStatisticMeter
import org.apache.geode.statistics.internal.micrometer.impl.MicrometerStatisticsManager
import java.io.Reader

class MicrometerStatisticsFactoryImpl(vararg meterRegistries: MeterRegistry = arrayOf(SimpleMeterRegistry())) : StatisticsFactory {

    private val micrometerStatisticsManager = MicrometerStatisticsManager.createWithRegistries(meterRegistries)
    private val meterGroupMap = hashMapOf<String, StatisticsType>()

    override fun createOsStatistics(type: StatisticsType?, textId: String, numericId: Long, osStatFlags: Int): Statistics =
            MicrometerStatisticsImpl(0, type as MicrometerStatisticsType, textId, numericId)

    override fun createStatistics(type: StatisticsType): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStatistics(type: StatisticsType, textId: String): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createStatistics(type: StatisticsType, textId: String, numericId: Long): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createAtomicStatistics(type: StatisticsType): Statistics {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createAtomicStatistics(type: StatisticsType, textId: String): Statistics =
            createAtomicStatistics(type, textId, 0)

    override fun createAtomicStatistics(type: StatisticsType, textId: String, numericId: Long): Statistics =
            MicrometerStatisticsImpl(0, type as MicrometerStatisticsType, textId, numericId)

    override fun findStatisticsByType(type: StatisticsType): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findStatisticsByTextId(textId: String): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findStatisticsByNumericId(numericId: Long): Array<Statistics> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createIntCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createLongCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createDoubleCounter(name: String, description: String, units: String) = CounterStatisticMeter(name, description, meterUnit = units)

    override fun createIntGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createLongGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createDoubleGauge(name: String, description: String, units: String) = GaugeStatisticMeter(name, description, meterUnit = units)

    override fun createIntCounter(name: String, description: String, units: String, largerBetter: Boolean) = createIntCounter(name, description, units)

    override fun createLongCounter(name: String, description: String, units: String, largerBetter: Boolean) = createLongCounter(name, description, units)

    override fun createDoubleCounter(name: String, description: String, units: String, largerBetter: Boolean) = createDoubleCounter(name, description, units)

    override fun createIntGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createLongGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createDoubleGauge(name: String, description: String, units: String, largerBetter: Boolean) = createIntGauge(name, description, units)

    override fun createType(name: String, description: String, stats: Array<StatisticDescriptor>): StatisticsType {
        val micrometerStatisticsType = MicrometerStatisticsType(name, description, stats)
        micrometerStatisticsManager.registerMeterGroup(name, micrometerStatisticsType)
        meterGroupMap[name] = micrometerStatisticsType
        return micrometerStatisticsType
    }

    override fun findType(name: String): StatisticsType = meterGroupMap[name]
            ?: throw IllegalArgumentException("No stats group for $name exists")

    override fun createTypesFromXml(reader: Reader): Array<StatisticsType> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}