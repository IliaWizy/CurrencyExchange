package infrastructure.repositories

import domain.Currency
import util.DBLoader
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class CurrencyQueryRepository : CurrencyQuery<String, Currency> {

    private val ds: DataSource = DBLoader.getDataSource()

    override fun findByCode(code: String): Currency? {
        lateinit var connect: Connection
        lateinit var ps: PreparedStatement

        try {
            connect = ds.connection
            ps = connect.prepareStatement("SELECT * FROM currency WHERE code = ?")
            ps.setString(1, code)

            val resultSet = ps.executeQuery()

            if (resultSet.next()) {
                return resultToCurrency(resultSet)
            }

        } finally {
            ps.close()
            connect.close()
        }

        return null

    }

    override fun findAll(): List<Currency> {
        lateinit var connect: Connection
        lateinit var ps: PreparedStatement

        val currencies = mutableListOf<Currency>()

        try {
            connect = ds.connection
            ps = connect.prepareStatement("SELECT * FROM currency")

            val resultSet = ps.executeQuery()

            while (resultSet.next()) {
                val currency = resultToCurrency(resultSet)
                currencies.add(currency)
            }

        } finally {
            ps.close()
            connect.close()
        }

        return currencies
    }

    private fun resultToCurrency(resultSet: ResultSet): Currency {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("full_name")
        val code = resultSet.getString("code")
        val sign = resultSet.getString("sign")

        val currency = Currency(id, code, name, sign)
        return currency
    }
}
