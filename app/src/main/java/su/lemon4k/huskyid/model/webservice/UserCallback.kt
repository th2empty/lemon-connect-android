package su.lemon4k.huskyid.model.webservice

interface UserCallback {
    fun onResponse(response: Any)
    fun onFailure(t: Throwable)
}