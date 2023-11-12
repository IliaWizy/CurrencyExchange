package config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import util.PropertiesLoader

object DataSourceConfig {

    fun getDataSource(): HikariDataSource = HikariConfig().apply {
        jdbcUrl = PropertiesLoader["DB_URL"]
        username = PropertiesLoader["DB_USER"]
        password = PropertiesLoader["DB_PASSWORD"]
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 10
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }.let { HikariDataSource(it) }

}
