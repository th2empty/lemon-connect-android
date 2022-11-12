package su.lemon4k.connect.network.input

data class SignUpInput(
    val avatar_id: Long,
    val email: String,
    val username: String,
    val password: String)