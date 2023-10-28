package util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DBLoader {

    fun getDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = EnvLoader["DB_URL"]
        config.username = EnvLoader["DB_USER"]
        config.password = EnvLoader["DB_PASSWORD"]
        config.driverClassName = "org.postgresql.Driver"

        config.maximumPoolSize = 10
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        return HikariDataSource(config)
    }

}
