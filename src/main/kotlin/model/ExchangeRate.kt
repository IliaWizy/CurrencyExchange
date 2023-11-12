package model

import java.math.BigDecimal

data class ExchangeRate(
    val id: Int,
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    val rate: BigDecimal
)
