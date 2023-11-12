package repository

import config.DataSourceConfig
import model.Currency
import repository.mapper.toCurrency
import java.sql.Statement
import javax.sql.DataSource

class JdbcCurrencyRepository : CurrencyRepository {
    private val ds: DataSource = DataSourceConfig.getDataSource()
    override fun findByCode(code: String): Currency? {
        val connect = ds.connection
        val query = "SELECT * FROM currency WHERE code = ?"

        connect.use { conn ->
            conn.prepareStatement(query).use { ps ->
                ps.setString(1, code)
                val resultSet = ps.executeQuery()

                return resultSet.takeIf { it.next() }?.toCurrency()
            }
        }


    }


    override fun findById(id: Int?): Currency? {
        val connect = ds.connection
        val query = "SELECT * FROM currency WHERE id = ?"

        connect.use { conn ->
            conn.prepareStatement(query).use { ps ->
                ps.setInt(1, id!!)
                val resultSet = ps.executeQuery()

                return resultSet.takeIf { it.next() }?.toCurrency()
            }
        }
    }

    override fun findAll(): List<Currency> {
        val connect = ds.connection
        val query = "SELECT * FROM currency"

        connect.use { conn ->
            conn.prepareStatement(query).use { ps ->
                val resultSet = ps.executeQuery()

                return generateSequence {
                    resultSet.takeIf { it.next() }?.toCurrency()
                }.toList()
            }
        }
    }

    override fun create(entity: Currency): Currency {
        val connect = ds.connection
        val command = "INSERT INTO currency (code, name, sign) VALUES (?, ?, ?)"

        /*        connect.use { conn ->
                    conn.prepareStatement(command).use { ps ->
                        ps.setString(1, entity.code)
                        ps.setString(2, entity.name)
                        ps.setString(3, entity.sign)

                        ps.executeUpdate()

                        ps.generatedKeys
                            .takeIf { it.next() }
                            ?.getInt(1)
                            .let { entity.copy(id = it!!) }
                    }
                }*/
        val ps = connect.prepareStatement(command, Statement.RETURN_GENERATED_KEYS)


        ps.setString(1, entity.code)
        ps.setString(2, entity.name)
        ps.setString(3, entity.sign)

        ps.executeUpdate()


        return entity.copy(id = ps.generatedKeys.takeIf { it.next() }?.getInt(1)!!).also {
            ps.close()
            connect.close()
        }
    }

    override fun update(entity: Currency) {
        val connect = ds.connection
        val command = "UPDATE currency SET code = ?, name = ?, sign = ? WHERE id = ?"

        connect.use { conn ->
            conn.prepareStatement(command).use { ps ->
                ps.setString(1, entity.code)
                ps.setString(2, entity.name)
                ps.setString(3, entity.sign)
                ps.setInt(4, entity.id)

                ps.executeUpdate()
            }
        }
    }

    override fun delete(id: Int) {
        val connect = ds.connection
        val command = "DELETE FROM currency WHERE id = ?"

        connect.use { conn ->
            conn.prepareStatement(command).use { ps ->
                ps.setInt(1, id)

                ps.executeUpdate()
            }
        }
    }

}
