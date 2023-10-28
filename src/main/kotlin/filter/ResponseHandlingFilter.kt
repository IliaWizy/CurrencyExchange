package filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.annotation.WebFilter

@WebFilter("/*")
class ResponseHandlingFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        response?.characterEncoding = "UTF-8"
        response?.contentType = "application/json"
        chain?.doFilter(request, response)
    }
}
