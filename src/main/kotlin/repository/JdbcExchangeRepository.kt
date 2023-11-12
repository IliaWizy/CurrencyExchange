package repository

import config.DataSourceConfig
import model.ExchangeRate
import repository.mapper.toExchangeRate
import javax.sql.DataSource

class JdbcExchangeRepository : ExchangeRepository {
    private val ds: DataSource = DataSourceConfig.getDataSource()

    override fun findByCodes(
        baseCurrencyCode: String, targetCurrencyCode: String
    ): ExchangeRate? {
        val connection = ds.connection
        val query = """
            SELECT 
                ex.id as exchange_id, ex.rate as exchange_rate,
                bc.id as base_currency_id, bc.code as base_currency_code, bc.name as base_currency_name, bc.sign as base_currency_sign,
                tc.id as target_currency_id, tc.code as target_currency_code, tc.name as target_currency_name, tc.sign as target_currency_sign
            FROM exchanger ex
            JOIN currency bc ON ex.base_currency_id = bc.id
            JOIN currency tc ON ex.target_currency_id = tc.id
            WHERE bc.code = ? AND tc.code = ?
        """

        connection.use { conn ->
            conn.prepareStatement(query).use { statement ->

                statement.setString(1, baseCurrencyCode)
                statement.setString(2, targetCurrencyCode)
                val resultSet = statement.executeQuery()

                return resultSet.takeIf { it.next() }?.toExchangeRate()
            }
        }

    }

    override fun create(entity: ExchangeRate): ExchangeRate {
        val connection = ds.connection
        val command = """
            INSERT INTO exchanger (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)
        """

        connection.use { conn ->
            conn.prepareStatement(command).use { statement ->
                statement.setInt(1, entity.baseCurrency.id)
                statement.setInt(2, entity.targetCurrency.id)
                statement.setBigDecimal(3, entity.rate)

                statement.executeUpdate()

                return statement.generatedKeys
                    .takeIf { it.next() }?.getInt("id")
                    .let { id ->
                        entity.copy(id = id!!)
                    }
            }
        }
    }

    override fun findById(id: Int?): ExchangeRate? {
        val connection = ds.connection
        val query = """
            SELECT 
                ex.id as exchange_id, ex.rate as exchange_rate,
                bc.id as base_currency_id, bc.code as base_currency_code, bc.name as base_currency_name, bc.sign as base_currency_sign,
                tc.id as target_currency_id, tc.code as target_currency_code, tc.name as target_currency_name, tc.sign as target_currency_sign
            FROM exchanger ex
            JOIN currency bc ON ex.base_currency_id = bc.id
            JOIN currency tc ON ex.target_currency_id = tc.id
            WHERE ex.id = ?
        """

        connection.use { conn ->
            conn.prepareStatement(query).use { statement ->
                statement.setInt(1, id!!)
                val resultSet = statement.executeQuery()

                return resultSet.takeIf { it.next() }?.toExchangeRate()
            }
        }
    }

    override fun findAll(): List<ExchangeRate> {
        val connection = ds.connection
        val query = """
            SELECT 
                ex.id as exchange_id, ex.rate as exchange_rate,
                bc.id as base_currency_id, bc.code as base_currency_code, bc.name as base_currency_name, bc.sign as base_currency_sign,
                tc.id as target_currency_id, tc.code as target_currency_code, tc.name as target_currency_name, tc.sign as target_currency_sign
            FROM exchanger ex
            JOIN currency bc ON ex.base_currency_id = bc.id
            JOIN currency tc ON ex.target_currency_id = tc.id
        """

        connection.use { conn ->
            conn.prepareStatement(query).use { statement ->
                val resultSet = statement.executeQuery()

                return generateSequence {
                    resultSet.takeIf { it.next() }?.toExchangeRate()
                }.toList()
            }
        }
    }

    override fun update(entity: ExchangeRate) {
        val connection = ds.connection
        val command = """
            UPDATE exchanger SET rate = ? WHERE id = ?
        """
        connection.use { conn ->
            conn.prepareStatement(command).use { statement ->
                statement.setBigDecimal(1, entity.rate)
                statement.setInt(2, entity.id)

                statement.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        val connection = ds.connection
        val command = """
            DELETE FROM exchanger WHERE id = ?
        """

        connection.use { conn ->
            conn.prepareStatement(command).use { statement ->
                statement.setInt(1, id)

                statement.executeUpdate()
            }
        }
    }
}
