package su.lemon4k.connect.network.response

data class GetProfileInfoResponse(
    val id: Long,
    val avatar_url: String,
    val email: String,
    val username: String
)
