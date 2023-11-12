package controller

import com.google.gson.GsonBuilder
import controller.exception.ApiException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import repository.JdbcCurrencyRepository


@WebServlet("/currency/*")
class CurrencyController : HttpServlet() {
    private val currencyRepository = JdbcCurrencyRepository()
    private val gson = GsonBuilder().create()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val code = req.pathInfo?.substring(1)

        if (code.isNullOrEmpty()) {
            throw ApiException.MissingOrIncorrectParameters("Currency code not specified")
        }

        runCatching { currencyRepository.findByCode(code) }.onSuccess { currency ->
            currency ?: throw ApiException.CurrencyNotFound("Currency not found")

            resp.status = HttpServletResponse.SC_OK
            resp.writer.use { writer -> gson.toJson(currency, writer) }
        }.onFailure { e ->
            throw ApiException.GeneralException(e.message ?: "An error occurred.")
        }
    }
}
