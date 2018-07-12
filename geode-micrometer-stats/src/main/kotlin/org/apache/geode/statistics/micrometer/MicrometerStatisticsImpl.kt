package org.apache.geode.statistics.micrometer

import org.apache.geode.statistics.StatisticDescriptor
import org.apache.geode.statistics.Statistics
import java.util.function.DoubleSupplier
import java.util.function.IntSupplier
import java.util.function.LongSupplier

class MicrometerStatisticsImpl(private val uniqueId: Long,
                               private val type: MicrometerStatisticsType,
                               private val textId: String,
                               private val numericId: Long) : Statistics {

    override fun getUniqueId() = uniqueId
    override fun getType() = type
    override fun getTextId() = textId
    override fun getNumericId() = numericId
    override fun isAtomic() = true
    override fun isClosed() = false

    override fun close() {
    }

    override fun nameToId(name: String)= type.nameToId(name)

    override fun nameToDescriptor(name: String) = type.nameToDescriptor(name)

    override fun setInt(id: Int, value: Int) {
        type.getStatsForId(id).setValue(value)
    }

    override fun setInt(name: String, value: Int) {
        type.getStatsForId(nameToId(name)).setValue(value)
    }

    override fun setInt(descriptor: StatisticDescriptor, value: Int) {
        type.getStatsForId(descriptor.id).setValue(value)
    }

    override fun setLong(id: Int, value: Long) {
        type.getStatsForId(id).setValue(value)
    }

    override fun setLong(descriptor: StatisticDescriptor, value: Long) {
        type.getStatsForId(descriptor.id).setValue(value)
    }

    override fun setLong(name: String, value: Long) {
        type.getStatsForId(nameToId(name)).setValue(value)
    }

    override fun setDouble(id: Int, value: Double) {
        type.getStatsForId(id).setValue(value)
    }

    override fun setDouble(descriptor: StatisticDescriptor, value: Double) {
        type.getStatsForId(descriptor.id).setValue(value)
    }

    override fun setDouble(name: String, value: Double) {
        type.getStatsForId(nameToId(name)).setValue(value)
    }

    override fun getInt(id: Int): Int = type.getStatsForId(id).getValue().toInt()

    override fun getInt(descriptor: StatisticDescriptor): Int = type.getStatsForId(descriptor.id).getValue().toInt()

    override fun getInt(name: String): Int = type.getStatsForId(nameToId(name)).getValue().toInt()

    override fun getLong(id: Int): Long = type.getStatsForId(id).getValue()

    override fun getLong(descriptor: StatisticDescriptor): Long = type.getStatsForId(descriptor.id).getValue()

    override fun getLong(name: String): Long = type.getStatsForId(nameToId(name)).getValue()

    override fun getDouble(id: Int): Double = type.getStatsForId(id).getValue().toDouble()

    override fun getDouble(descriptor: StatisticDescriptor): Double = type.getStatsForId(descriptor.id).getValue().toDouble()

    override fun getDouble(name: String): Double = type.getStatsForId(nameToId(name)).getValue().toDouble()

    override fun get(descriptor: StatisticDescriptor): Number = type.getStatsForId(descriptor.id).getValue()

    override fun get(name: String): Number = type.getStatsForId(nameToId(name)).getValue()

    override fun getRawBits(descriptor: StatisticDescriptor): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRawBits(name: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun incInt(id: Int, delta: Int) {
        incLong(id, delta.toLong())
    }

    override fun incInt(descriptor: StatisticDescriptor, delta: Int) {
        incLong(descriptor, delta.toLong())
    }

    override fun incInt(name: String, delta: Int) {
        type.getStatsForId(nameToId(name)).increment(delta)
    }

    override fun incLong(id: Int, delta: Long) {
        type.getStatsForId(id).increment(delta)
    }

    override fun incLong(descriptor: StatisticDescriptor, delta: Long) {
        type.getStatsForId(descriptor.id).increment(delta)
    }

    override fun incLong(name: String, delta: Long) {
        type.getStatsForId(nameToId(name)).increment(delta)
    }

    override fun incDouble(id: Int, delta: Double) {
        incLong(id, delta.toLong())
    }

    override fun incDouble(descriptor: StatisticDescriptor, delta: Double) {
        incLong(descriptor.id, delta.toLong())
    }

    override fun incDouble(name: String, delta: Double) {
        incLong(nameToId(name), delta.toLong())
    }

    override fun setIntSupplier(id: Int, supplier: IntSupplier): IntSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setIntSupplier(name: String, supplier: IntSupplier): IntSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setIntSupplier(descriptor: StatisticDescriptor, supplier: IntSupplier): IntSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLongSupplier(id: Int, supplier: LongSupplier): LongSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLongSupplier(name: String, supplier: LongSupplier): LongSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLongSupplier(descriptor: StatisticDescriptor, supplier: LongSupplier): LongSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDoubleSupplier(id: Int, supplier: DoubleSupplier): DoubleSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDoubleSupplier(name: String, supplier: DoubleSupplier): DoubleSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDoubleSupplier(descriptor: StatisticDescriptor, supplier: DoubleSupplier): DoubleSupplier {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}