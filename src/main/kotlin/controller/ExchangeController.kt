package controller

import exception.ApiException
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/exchange")
class ExchangeController : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return

        val attributeFrom = req.getParameter("from").toString()
        val attributeTo = req.getParameter("to").toString()
        val attributeAmount: Double = req.getParameter("amount")?.toString()?.toDoubleOrNull()
            ?: throw ApiException(HttpServletResponse.SC_NOT_FOUND, "Проверьте заполнение данных в атрибутах")

        resp.status = HttpServletResponse.SC_OK
        resp.writer.write("Обменяли")
    }
}
