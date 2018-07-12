import org.apache.geode.distributed.ConfigurationProperties
import org.apache.geode.distributed.LocatorLauncher

fun main(args: Array<String>) {
    val build = LocatorLauncher.Builder().apply {
        port = 44550
        set("statistic-sampling-enabled", "true")
        set(ConfigurationProperties.JMX_MANAGER, "false")
        set(ConfigurationProperties.JMX_MANAGER_START, "false")
        set(ConfigurationProperties.ENABLE_CLUSTER_CONFIGURATION, "false")
    }.build()

    build.start()

    while(true)
    {
        Thread.sleep(5000)
    }
}