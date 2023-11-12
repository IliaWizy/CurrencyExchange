package controller.filter

import com.google.gson.Gson
import controller.exception.ApiException
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletResponse

@WebFilter("/*")
class ExceptionFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            chain?.doFilter(request, response)
        } catch (e: ApiException) {
            sendErrorAsJson(
                response as HttpServletResponse, e.message ?: "Unknown error", e.status
            )
        }
    }

    private fun sendErrorAsJson(response: HttpServletResponse, message: String, status: Int) {
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.status = status
        response.writer.use { writer ->
            val errorResponse = mapOf("error" to message, "status" to status)
            writer.print(Gson().toJson(errorResponse))
        }
    }
}
