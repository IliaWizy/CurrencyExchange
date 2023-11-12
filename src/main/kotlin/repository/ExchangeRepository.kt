package repository

import model.ExchangeRate
import java.sql.SQLException

interface ExchangeRepository : BaseRepository<ExchangeRate> {

    @Throws(SQLException::class)
    fun findByCodes(baseCurrencyCode: String, targetCurrencyCode: String): ExchangeRate?
}
