package controller

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/exchangeRate/*")
class ExchangeRateController : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return

        val pathInfo = req.pathInfo

    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return


    }

    override fun doPut(req: HttpServletRequest?, resp: HttpServletResponse?) {
        req ?: return
        resp ?: return


    }
}
