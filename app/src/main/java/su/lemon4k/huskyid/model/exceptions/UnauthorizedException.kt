package su.lemon4k.huskyid.model.exceptions

class UnauthorizedException(message: String? = "Unauthorized", cause: Throwable? = null)
    : Exception(message, cause)