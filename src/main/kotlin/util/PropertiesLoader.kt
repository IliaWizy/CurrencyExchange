package util

import java.io.FileNotFoundException
import java.util.*

object PropertiesLoader {
    private const val DEFAULT_PROPERTIES_FILE = "dev.properties"
    private val properties: Properties by lazy { loadProperties() }

    private fun loadProperties(): Properties {
        val propertiesFileName = System.getenv("CONFIG_FILE") ?: DEFAULT_PROPERTIES_FILE
        val properties = Properties()

        runCatching {
            Thread.currentThread().contextClassLoader.getResourceAsStream(propertiesFileName)?.use {
                properties.load(it)
            } ?: throw FileNotFoundException("Property file '$propertiesFileName' not found in classpath.")
        }.onFailure {
            throw IllegalStateException("Could not load properties from file $propertiesFileName", it)
        }

        return properties
    }

    operator fun get(key: String): String? = properties.getProperty(key)
}
