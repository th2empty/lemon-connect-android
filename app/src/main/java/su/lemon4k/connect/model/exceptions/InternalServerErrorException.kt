package su.lemon4k.connect.model.exceptions

class InternalServerErrorException(message: String? = "Internal server error", cause: Throwable? = null)
    : Exception(message, cause)