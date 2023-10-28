package filter

import exception.ApiException
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletResponse
import util.ExceptionHandler.sendErrorAsJson

@WebFilter("/*")
class ExceptionHandlingFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        try {
            chain?.doFilter(request, response)
        } catch (e: ApiException) {
            sendErrorAsJson(
                response as HttpServletResponse, e.message ?: "Unknown error", e.status
            )
        }/* catch (e: Throwable) {
            sendErrorAsJson(
                response as HttpServletResponse, "Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            )
        }*/
    }
}
