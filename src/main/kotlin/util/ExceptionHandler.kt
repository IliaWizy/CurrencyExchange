package util

import jakarta.servlet.http.HttpServletResponse

object ExceptionHandler {

    fun sendErrorAsJson(resp: HttpServletResponse, message: String, statusCode: Int) {
        resp.status = statusCode

        val responseMessage = mapOf("message" to message)
        val json = JsonBuilder.gson.toJson(responseMessage)

        return resp.writer.write(json)
    }
}
