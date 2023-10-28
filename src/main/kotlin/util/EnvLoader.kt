package util

import java.io.IOException
import java.util.*

object EnvLoader {
    private val PROPERTIES = Properties()

    init {
        loadProperties()
    }

    operator fun get(key: String?): String {
        return PROPERTIES.getProperty(key)
    }

    private fun loadProperties() {
        try {
            EnvLoader::class.java
                .getClassLoader()
                .getResourceAsStream("dev.properties")
                .use { resourceAsStream -> PROPERTIES.load(resourceAsStream) }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
