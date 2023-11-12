package controller

import com.google.gson.GsonBuilder
import controller.exception.ApiException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import model.dto.CurrencyDto
import org.postgresql.util.PSQLException
import service.CurrencyService


@WebServlet("/currencies")
class CurrenciesController : HttpServlet() {
    private val currencyService = CurrencyService()
    private val gson = GsonBuilder().create()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {

        runCatching { currencyService.findAll() }
            .onSuccess {
                resp.status = HttpServletResponse.SC_OK
                resp.writer.use { writer -> gson.toJson(it, writer) }
            }
            .onFailure { e ->
                throw ApiException.GeneralException(e.message ?: "An error occurred.")
            }

    }


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val name = req.getParameter("name")
        val code = req.getParameter("code")
        val sign = req.getParameter("sign")

        if (name.isNullOrEmpty() || code.isNullOrEmpty() || sign.isNullOrEmpty()) {
            throw ApiException.MissingOrIncorrectParameters("Missing or incorrect parameters")
        }

        currencyService.add(CurrencyDto(name = name, code = code, sign = sign))
            .onSuccess { addedCurrency ->
                resp.status = HttpServletResponse.SC_OK
                gson.toJson(addedCurrency, resp.writer)
            }
            .onFailure { e ->
                if (e is PSQLException && e.message!!.contains("duplicate key value violates unique constraint")) {
                    throw ApiException.CurrencyAlreadyExists("Currency with code $code already exists.")
                } else {
                    throw ApiException.GeneralException(e.message ?: "An error occurred.")
                }
            }
    }
}
