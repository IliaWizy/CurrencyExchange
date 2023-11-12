package service

import model.Currency
import model.dto.CurrencyDto
import repository.JdbcCurrencyRepository


class CurrencyService {

    private val currencyRepository = JdbcCurrencyRepository()
    
    fun add(dto: CurrencyDto): Result<Currency> =
        runCatching { currencyRepository.create(dto.toCurrency()) }

    fun findAll(): List<Currency> = currencyRepository.findAll()

}
