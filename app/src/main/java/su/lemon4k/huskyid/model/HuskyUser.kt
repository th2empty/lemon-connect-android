package su.lemon4k.huskyid.model

import android.util.Log
import su.lemon4k.huskyid.model.webservice.UserCallback
import su.lemon4k.huskyid.model.webservice.SignInController
import su.lemon4k.huskyid.model.webservice.SignInInput

data class HuskyUser(
    val id: Long?,
    val avatar_url: String?,
    val email: String?,
    val username: String?) {

    fun signIn(input: SignInInput, userCallback: UserCallback) {
        val controller = SignInController(input, object : UserCallback {

            override fun onResponse(response: Any) {
                Log.i(this::class.java.name.split('.').last(), response.toString())
                userCallback.onResponse(response)
            }

            override fun onFailure(t: Throwable) {
                userCallback.onFailure(t)
            }

        })
        controller.auth()
    }
}
