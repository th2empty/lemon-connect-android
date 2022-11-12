package su.lemon4k.connect.presentation.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.content.SharedPreferences
import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.User
import su.lemon4k.connect.model.exceptions.InternalServerError
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.model.network.SignInInput
import su.lemon4k.connect.ui.views.SignInView

@InjectViewState
class SignInPresenter : MvpPresenter<SignInView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signIn(accountManager: AccountManager, sharedPreferences: SharedPreferences,
               input: SignInInput, callback: () -> Unit) {
        val user = User()
        val response = user.signIn(input)
        if (response.isSuccessful) {
            val body = response.body()
            Log.i(TAG, body.toString())
            callback()
            Account(input.username, Constants.ACCOUNT_TYPE).also { account ->  
                accountManager.addAccountExplicitly(account, input.password, null)
                accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, body?.access_token)
                accountManager.setUserData(account, Constants.KEY_REFRESH_TOKEN, body?.refresh_token)
            }

            with (sharedPreferences.edit()) {
                putString(Constants.KEY_ACCOUNT_NAME, input.username)
                apply()
            }
        } else {
            Log.e(TAG, response.code().toString())
            when (response.code()) {
                400 -> throw Exception(response.message())
                401 -> throw UnauthorizedException(response.message())
                500 -> throw InternalServerError(response.message())
            }
        }
    }
}