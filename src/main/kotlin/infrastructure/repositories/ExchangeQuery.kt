package infrastructure.repositories

interface ExchangeQuery<R> {
    fun findExchangeRate(baseCurrencyCode: String, targetCurrencyCode: String, amount: Double): R
}
