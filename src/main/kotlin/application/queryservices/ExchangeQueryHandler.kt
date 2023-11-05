package application.queryservices

import infrastructure.repositories.ExchangeQueryRepository
import interfaces.rest.dto.ExchangeRateDto


class ExchangeQueryHandler {

    private val exchangeQuery = ExchangeQueryRepository()

    fun getExchangeRate(baseCurrencyCode: String, targetCurrencyCode: String, amount: Double): ExchangeRateDto =
        exchangeQuery.findExchangeRate(baseCurrencyCode, targetCurrencyCode, amount)
}
