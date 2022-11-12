package su.lemon4k.connect.presentation.presenter

import android.accounts.AccountManager
import android.content.SharedPreferences
import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.model.Auth
import su.lemon4k.connect.model.exceptions.InternalServerError
import su.lemon4k.connect.network.input.SignInInput
import su.lemon4k.connect.network.input.SignUpInput
import su.lemon4k.connect.ui.views.SignUpView

@InjectViewState
class SignUpPresenter : MvpPresenter<SignUpView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun emailIsFree(email: String): Boolean {
        val auth = Auth()
        val response = auth.findEmail(email)
        if (response.isSuccessful) {
            val body = response.body()
            return body?.is_registered == false
        } else {
            Log.e(TAG, response.code().toString())
            when (response.code()) {
                500 -> throw InternalServerError(response.message())
            }
        }

        return false
    }

    fun usernameIsFree(username: String): Boolean {
        val auth = Auth()
        val response = auth.findUsername(username)
        if (response.isSuccessful) {
            val body = response.body()
            return body?.is_registered == false
        } else {
            Log.e(TAG, response.code().toString())
            when (response.code()) {
                500 -> throw InternalServerError(response.message())
            }
        }

        return false
    }

    fun signUp(accountManager: AccountManager, sharedPreferences: SharedPreferences,
               input: SignUpInput, callback: () -> Unit) {
        val auth = Auth()
        auth.signUp(accountManager, sharedPreferences, input)
        callback()
    }
}