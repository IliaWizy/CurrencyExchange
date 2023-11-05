package interfaces.rest

import application.queryservices.ExchangeQueryHandler
import com.google.gson.GsonBuilder
import interfaces.rest.exception.ApiException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/exchange")
class ExchangeController : HttpServlet() {
    private val exchangeQueryHandler = ExchangeQueryHandler()

    /**
     * Получение курса для обмена может пройти по одному из трёх сценариев. Допустим, совершаем перевод из валюты A в валюту B:
     *
     * В таблице ExchangeRates существует валютная пара AB - берём её курс
     * В таблице ExchangeRates существует валютная пара BA - берем её курс, и считаем обратный, чтобы получить AB
     * В таблице ExchangeRates существуют валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
     * Остальные возможные сценарии, для упрощения, опустим.
     *
     * Пример ответа:
     *
     * {
     *     "baseCurrency": {
     *         "id": 0,
     *         "name": "United States dollar",
     *         "code": "USD",
     *         "sign": "$"
     *     },
     *     "targetCurrency": {
     *         "id": 1,
     *         "name": "Australian dollar",
     *         "code": "AUD",
     *         "sign": "A€"
     *     },
     *     "rate": 1.45,
     *     "amount": 10.00
     *     "convertedAmount": 14.50
     * }
     */
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val baseCurrencyCode = req.getParameter("from")
        val targetCurrencyCode = req.getParameter("to")
        val amount = req.getParameter("amount")?.toDoubleOrNull()

        if (baseCurrencyCode.isNullOrEmpty() || targetCurrencyCode.isNullOrEmpty() || amount == null) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            return
        }

        try {
            val exchangeRate = exchangeQueryHandler.getExchangeRate(baseCurrencyCode, targetCurrencyCode, amount)
            resp.status = HttpServletResponse.SC_OK
            GsonBuilder().create().toJson(exchangeRate, resp.writer)
        } catch (e: ApiException) {
            resp.status = e.status
            e.message?.let { resp.writer.write(it) }
        }
    }
}
