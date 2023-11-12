package controller

import com.google.gson.GsonBuilder
import controller.exception.ApiException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import service.ExchangeService
import java.math.BigDecimal

@WebServlet("/exchange")
class ExchangeController : HttpServlet() {
    private val exchangeService = ExchangeService()
    private val gson = GsonBuilder().create()


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val baseCurrencyCode = req.getParameter("from").orEmpty()
        val targetCurrencyCode = req.getParameter("to").orEmpty()
        val amount = req.getParameter("amount")?.toDoubleOrNull()

        if (baseCurrencyCode.isBlank() || targetCurrencyCode.isBlank() || amount == null) {
            throw ApiException.MissingOrIncorrectParameters("Missing or incorrect parameters")
        }

        exchangeService.getExchange(baseCurrencyCode, targetCurrencyCode, BigDecimal(amount))
            .onSuccess { exchangeRate ->
                resp.status = HttpServletResponse.SC_OK
                resp.writer.use { writer -> gson.toJson(exchangeRate, writer) }
            }.onFailure { e ->
                throw ApiException.GeneralException(e.message ?: "An error occurred.")
            }
    }

}
