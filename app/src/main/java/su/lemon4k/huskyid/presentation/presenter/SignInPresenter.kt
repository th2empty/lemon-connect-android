package su.lemon4k.huskyid.presentation.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.content.SharedPreferences
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import su.lemon4k.huskyid.account.Constants
import su.lemon4k.huskyid.model.User
import su.lemon4k.huskyid.model.exceptions.InternalServerError
import su.lemon4k.huskyid.model.exceptions.UnauthorizedException
import su.lemon4k.huskyid.model.network.SignInInput
import su.lemon4k.huskyid.ui.views.SignInView

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