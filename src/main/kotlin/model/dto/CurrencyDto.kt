package model.dto

import model.Currency


data class CurrencyDto(val name: String, val code: String, val sign: String) {

    fun toCurrency(): Currency = Currency(-1, name, code, sign)
}
