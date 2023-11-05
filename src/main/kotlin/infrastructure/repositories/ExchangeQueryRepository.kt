package infrastructure.repositories

import interfaces.rest.dto.CurrencyDto
import interfaces.rest.dto.ExchangeRateDto
import util.DBLoader
import java.math.BigDecimal
import javax.sql.DataSource

class ExchangeQueryRepository : ExchangeQuery<ExchangeRateDto> {
    private val ds: DataSource = DBLoader.getDataSource()

    override fun findExchangeRate(
        baseCurrencyCode: String,
        targetCurrencyCode: String,
        amount: Double
    ): ExchangeRateDto {
        val connection = ds.connection
        val query = """
            SELECT 
                bc.id as base_currency_id, bc.code as base_currency_code, bc.full_name as base_currency_name, bc.sign as base_currency_sign,
                tc.id as target_currency_id, tc.code as target_currency_code, tc.full_name as target_currency_name, tc.sign as target_currency_sign,
                ex.rate as exchange_rate
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

                if (resultSet.next()) {
                    val baseCurrencyDto = CurrencyDto(
                        id = resultSet.getInt("base_currency_id"),
                        name = resultSet.getString("base_currency_name"),
                        code = resultSet.getString("base_currency_code"),
                        sign = resultSet.getString("base_currency_sign")
                    )

                    val targetCurrencyDto = CurrencyDto(
                        id = resultSet.getInt("target_currency_id"),
                        name = resultSet.getString("target_currency_name"),
                        code = resultSet.getString("target_currency_code"),
                        sign = resultSet.getString("target_currency_sign")
                    )

                    val rate = resultSet.getBigDecimal("exchange_rate")
                    val convertedAmount = rate.multiply(BigDecimal.valueOf(amount))

                    return ExchangeRateDto(
                        baseCurrency = baseCurrencyDto,
                        targetCurrency = targetCurrencyDto,
                        rate = rate,
                        amount = BigDecimal.valueOf(amount),
                        convertedAmount = convertedAmount
                    )
                } else {
                    throw IllegalArgumentException("Курс обмена $baseCurrencyCode на $targetCurrencyCode не найден.")
                }
            }
        }
    }
}
