package application.queryservices

import domain.Currency
import infrastructure.repositories.CurrencyQueryRepository


class CurrencyQueryHandler {

    private val queryRepository = CurrencyQueryRepository()

    fun findAll(): List<Currency> = queryRepository.findAll()

    fun findByCode(code: String): Currency = queryRepository.findByCode(code)
        ?: throw Exception("Валюта с кодом $code не найдена")
}
