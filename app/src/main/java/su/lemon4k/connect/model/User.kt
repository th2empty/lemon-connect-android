package su.lemon4k.connect.model

import retrofit2.Response
import su.lemon4k.connect.network.input.SignInInput
import su.lemon4k.connect.network.response.SignInResponse

data class User(
    val id: Long,
    val avatar_url: String,
    val email: String,
    val username: String) {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }
}
