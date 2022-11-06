package su.lemon4k.huskyid.model

import android.util.Log
import su.lemon4k.huskyid.model.webservice.UserCallback
import su.lemon4k.huskyid.model.webservice.SignInController
import su.lemon4k.huskyid.model.webservice.SignInInput

data class UserModel(
    val id: Long? = null,
    val avatar_url: String? = null,
    val email: String? = null,
    val username: String? = null) {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signIn(input: SignInInput) {
        val controller = SignInController(input)
        controller.auth()
    }
}
