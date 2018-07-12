import org.apache.geode.cache.CacheFactory
import org.apache.geode.cache.RegionShortcut
import org.apache.geode.cache.client.ClientCacheFactory
import org.apache.geode.cache.client.ClientRegionShortcut
import org.apache.geode.distributed.ConfigurationProperties
import java.io.File
import java.util.*
import java.util.stream.IntStream

fun main(args: Array<String>) {
    val properties = Properties().apply {
        setProperty("mcast-port", "0")
        setProperty("statistic-sampling-enabled", "true")
    }
    val cache = ClientCacheFactory(properties).addPoolLocator("localhost", 44550).create()

    val regionFactory = cache.createClientRegionFactory<String, String>(ClientRegionShortcut.PROXY)
    regionFactory.setStatisticsEnabled(true)
    val region1 = regionFactory.create("Region1")

    val regionFactory2 = cache.createClientRegionFactory<String, String>(ClientRegionShortcut.PROXY)
    regionFactory2.setStatisticsEnabled(true)
    val region2 = regionFactory2.create("Region3")

    val random = Random()
    IntStream.range(0, Int.MAX_VALUE).parallel().forEach { count ->

        region1[count.toString()] = region1.size.toString()

        for (value in 0..random.nextInt(25)) {
            region2[(value*count).toString()] = value.toString()
        }

        if (random.nextBoolean()) {
            for (value in 0..random.nextInt(35)) {
                region1[count.toString()]
                region2.destroy((value*count).toString())
            }
        } else {
            for (value in 0..random.nextInt(10)) {
                region2[count.toString()]
                region1.destroy((value*count).toString())
            }
        }
//        Thread.sleep(100)

        println("Processed value $count")

    }
}