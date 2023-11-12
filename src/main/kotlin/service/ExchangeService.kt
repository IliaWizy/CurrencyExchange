package service

import controller.exception.ApiException
import model.ExchangeRate
import repository.JdbcCurrencyRepository
import repository.JdbcExchangeRepository
import java.math.BigDecimal
import java.math.MathContext


class ExchangeService {
    private val exchangeRepository = JdbcExchangeRepository()
    private val currencyRepository = JdbcCurrencyRepository()

    fun getExchange(baseCurrencyCode: String, targetCurrencyCode: String, amount: BigDecimal): Result<ExchangeRate> {
        /**
         * В таблице ExchangeRates существует валютная пара AB - берём её курс
         */
        exchangeRepository.findByCodes(baseCurrencyCode, targetCurrencyCode)?.let {
            return Result.success(it.applyRate(amount))
        }

        /**
         * В таблице ExchangeRates существует валютная пара BA - берем её курс, и считаем обратный, чтобы получить AB
         */
        exchangeRepository.findByCodes(targetCurrencyCode, baseCurrencyCode)?.let {
            return Result.success(it.invertRate().applyRate(amount))
        }

        /**
         * В таблице ExchangeRates существуют валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
         */
        val rateUsdToBase = exchangeRepository.findByCodes("USD", baseCurrencyCode)?.rate
        val rateUSDToTarget = exchangeRepository.findByCodes("USD", targetCurrencyCode)?.rate

        if (rateUsdToBase != null && rateUSDToTarget != null) {
            val rateBaseToTarget = rateUsdToBase.divide(rateUSDToTarget, MathContext.DECIMAL128).multiply(amount)
            val baseCurrency = currencyRepository.findByCode(baseCurrencyCode)!!
            val targetCurrency = currencyRepository.findByCode(targetCurrencyCode)!!
            return Result.success(ExchangeRate(0, baseCurrency, targetCurrency, rateBaseToTarget))
        }

        return Result.failure(ApiException.ExchangeRateNotFound("No exchange rate found for $baseCurrencyCode to $targetCurrencyCode"))
    }

    private fun ExchangeRate.applyRate(amount: BigDecimal): ExchangeRate {
        val newRate = this.rate.multiply(amount)
        return ExchangeRate(this.id, this.baseCurrency, this.targetCurrency, newRate)
    }

    private fun ExchangeRate.invertRate(): ExchangeRate {
        val invertedRate = BigDecimal.ONE.divide(this.rate, MathContext.DECIMAL128)
        return ExchangeRate(this.id, this.targetCurrency, this.baseCurrency, invertedRate)
    }
}
