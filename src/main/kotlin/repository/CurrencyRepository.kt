package repository

import model.Currency
import java.sql.SQLException

interface CurrencyRepository : BaseRepository<Currency> {

    @Throws(SQLException::class)
    fun findByCode(code: String): Currency?
}
