package data

import model.Currency
import util.DBLoader
import java.sql.Statement
import javax.sql.DataSource

class CurrencyRepository : Repository<Currency> {

    private val dataSource: DataSource by lazy { DBLoader.getDataSource() }

    override fun findById(id: Int): Currency? {
        return dataSource.connection.use { connection ->
            connection.prepareStatement("SELECT id, code, full_name, sing FROM currency WHERE id = ?")
                .use { statement ->
                    statement.setInt(1, id)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        Currency(
                            id = resultSet.getInt("id"),
                            code = resultSet.getString("code"),
                            name = resultSet.getString("full_name"),
                            sign = resultSet.getString("sing")
                        )
                    } else {
                        null
                    }
                }
        }
    }

    override fun findAll(): List<Currency> {
        return dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery("SELECT id, code, full_name, sing FROM currency")
                val currencies = mutableListOf<Currency>()
                while (resultSet.next()) {
                    val currency = Currency(
                        id = resultSet.getInt("id"),
                        code = resultSet.getString("code"),
                        name = resultSet.getString("full_name"),
                        sign = resultSet.getString("sing")
                    )
                    currencies.add(currency)
                }
                currencies
            }
        }
    }


    override fun delete(id: Int): Boolean {
        return dataSource.connection.use { connection ->
            connection.prepareStatement("DELETE FROM currency WHERE id = ?").use { statement ->
                statement.setInt(1, id)
                val deletedRows = statement.executeUpdate()
                deletedRows > 0
            }
        }
    }


    override fun update(entity: Currency): Boolean {
        return dataSource.connection.use { connection ->
            connection.prepareStatement("UPDATE currency SET code = ?, full_name = ?, sing = ? WHERE id = ?")
                .use { statement ->
                    statement.setString(1, entity.code)
                    statement.setString(2, entity.name)
                    statement.setString(3, entity.sign)
                    statement.setInt(4, entity.id ?: -1)

                    val updatedRows = statement.executeUpdate()
                    updatedRows > 0
                }
        }
    }


    override fun create(entity: Currency): Currency {
        return dataSource.connection.use { connection ->
            connection.prepareStatement(
                "INSERT INTO currency (code, full_name, sing) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            ).use { statement ->
                statement.setString(1, entity.code)
                statement.setString(2, entity.name)
                statement.setString(3, entity.sign)

                statement.executeUpdate()

                statement.generatedKeys.use { generatedKeys ->
                    if (generatedKeys.next()) {
                        entity.copy(id = generatedKeys.getInt(1))
                    } else {
                        entity
                    }
                }
            }
        }
    }


}
