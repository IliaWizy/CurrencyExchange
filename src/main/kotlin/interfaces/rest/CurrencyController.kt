package interfaces.rest

import application.commandservices.CurrencyCommandHandler
import application.queryservices.CurrencyQueryHandler
import com.google.gson.GsonBuilder
import interfaces.rest.dto.CurrencyDto
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.postgresql.util.PSQLException


@WebServlet("/currency/*")
class CurrencyController : HttpServlet() {
    private val currencyQueryHandler = CurrencyQueryHandler()


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val code = req.pathInfo?.substring(1)

        if (code.isNullOrEmpty()) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            return
        }

        try {
            val currency = currencyQueryHandler.findByCode(code)
            resp.status = HttpServletResponse.SC_OK
            GsonBuilder().create().toJson(currency, resp.writer)
        } catch (e: Exception) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
        }
    }

}

@WebServlet("/currencies")
class CurrenciesController : HttpServlet() {
    private val currencyQueryHandler = CurrencyQueryHandler()
    private val currencyCommandHandler = CurrencyCommandHandler()


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        return currencyQueryHandler.findAll().let {
            resp.status = HttpServletResponse.SC_OK
            GsonBuilder().create().toJson(it, resp.writer)
        }

    }


    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val name = req.getParameter("name")
        val code = req.getParameter("code")
        val sign = req.getParameter("sign")

        if (name.isNullOrEmpty() || code.isNullOrEmpty() || sign.isNullOrEmpty()) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            return
        }

        try {
            val addedCurrency = currencyCommandHandler.add(CurrencyDto(name = name, code = code, sign = sign))

            resp.status = HttpServletResponse.SC_OK
            GsonBuilder().create().toJson(addedCurrency, resp.writer)

        } catch (e: PSQLException) {
            if (e.message!!.contains("duplicate key value violates unique constraint")) {
                resp.status = HttpServletResponse.SC_CONFLICT
                return
            } else {
                resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            }
        }

    }
}
