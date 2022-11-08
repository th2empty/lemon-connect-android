package su.lemon4k.huskyid.model.exceptions

class InternalServerError(message: String? = "Internal server error", cause: Throwable? = null)
    : Exception(message, cause)