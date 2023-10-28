package exception

class ApiException(val status: Int, message: String) : Exception(message)
