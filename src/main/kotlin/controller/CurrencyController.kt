package controller

import com.fasterxml.jackson.databind.ObjectMapper
import dto.CurrencyDto
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mapper.CurrencyMapper
import service.CurrencyService
import util.JsonBuilder

@WebServlet("/currency/*")
class CurrencyController : HttpServlet() {
    private val currencyService: CurrencyService by lazy { CurrencyService() }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return

        val pathInfo = req.pathInfo

        if (pathInfo == "/" || pathInfo == null) {
            val currencies = currencyService.findAll()
            val result = JsonBuilder.gson.toJson(currencies)
            resp.writer.write(result)

        } else {
            val currencyCode = pathInfo.substring(1)

            val currency = currencyService.findByCode(currencyCode)

            if (currency != null) {
                val result = JsonBuilder.gson.toJson(currency)
                resp.writer.write(result)
            } else {
                resp.status = HttpServletResponse.SC_NOT_FOUND
                resp.writer.write(JsonBuilder.gson.toJson(mapOf("message" to "Валюта с кодом $currencyCode не найдена")))
            }
        }
    }


    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return

        val name = req.getParameter("name")
        val code = req.getParameter("code")
        val sign = req.getParameter("sign")

        if (name != null && code != null && sign != null) {
            val currencyDto = CurrencyDto(null, name, code, sign)

            val currency = CurrencyMapper.toEntity(currencyDto)

            val savedCurrency = currencyService.create(currency)

            resp.writer.write(ObjectMapper().writeValueAsString(CurrencyMapper.toDto(savedCurrency)))
            resp.status = HttpServletResponse.SC_CREATED
        } else {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            resp.writer.write("Отсутствующие или неверные параметры")
        }
    }
}
