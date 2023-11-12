package controller.exception

import jakarta.servlet.http.HttpServletResponse

sealed class ApiException(message: String, open val status: Int) : RuntimeException(message) {
    
    data class GeneralException(
        override val message: String, override val status: Int = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
    ) : ApiException(message, status)

    data class MissingOrIncorrectParameters(
        override val message: String, override val status: Int = HttpServletResponse.SC_BAD_REQUEST
    ) : ApiException(message, status)

    data class CurrencyAlreadyExists(
        override val message: String, override val status: Int = HttpServletResponse.SC_CONFLICT
    ) : ApiException(message, status)

    data class CurrencyNotFound(
        override val message: String, override val status: Int = HttpServletResponse.SC_NOT_FOUND
    ) : ApiException(message, status)

    data class ExchangeRateNotFound(
        override val message: String, override val status: Int = HttpServletResponse.SC_NOT_FOUND
    ) : ApiException(message, status)
}
