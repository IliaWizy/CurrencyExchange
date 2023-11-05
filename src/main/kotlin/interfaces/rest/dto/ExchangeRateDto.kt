package interfaces.rest.dto

import java.math.BigDecimal

data class ExchangeRateDto(
    val baseCurrency: CurrencyDto,
    val targetCurrency: CurrencyDto,
    val rate: BigDecimal,
    val amount: BigDecimal,
    val convertedAmount: BigDecimal
)
