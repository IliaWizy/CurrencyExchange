package repository.mapper

import model.Currency
import model.ExchangeRate
import java.sql.ResultSet


fun ResultSet.toExchangeRate() = ExchangeRate(
    baseCurrency = Currency(
        id = getInt("base_currency_id"),
        name = getString("base_currency_name"),
        code = getString("base_currency_code"),
        sign = getString("base_currency_sign")
    ), targetCurrency = Currency(
        id = getInt("target_currency_id"),
        name = getString("target_currency_name"),
        code = getString("target_currency_code"),
        sign = getString("target_currency_sign")
    ), rate = getBigDecimal("exchange_rate"), id = getInt("exchange_id")
)
