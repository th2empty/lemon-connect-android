package su.lemon4k.connect.model.exceptions

class UnauthorizedException(message: String? = "Unauthorized", cause: Throwable? = null)
    : Exception(message, cause)