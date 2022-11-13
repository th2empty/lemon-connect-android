package su.lemon4k.connect.presentation.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AuthenticatorException
import android.content.SharedPreferences
import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.User
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.ui.views.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun isUserLoggedIn(sharedPreferences: SharedPreferences, mAccountManager: AccountManager):
            Boolean {
        if (sharedPreferences.getString(
                    Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED
                ) == Constants.KEY_UNAUTHORIZED) {
            return false
        }

        val account = Account(
            sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED),
            Constants.ACCOUNT_TYPE)
        try {
            val bundle = mAccountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE,
                null, true, null, null).result

            val accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)
            if (accessToken != null) { // TODO: token validity check
                Log.i(TAG, accessToken)
                return true
            }
        } catch (ex: AuthenticatorException) {
            ex.printStackTrace()
            return false
        }

        return false
    }

    fun getProfileInfo(accountManager: AccountManager, sharedPreferences: SharedPreferences): User? {
        return try {
            val user = User()
            user.getProfileInfo(accountManager, sharedPreferences)
        } catch (ex: UnauthorizedException) {
            Log.e(TAG, ex.printStackTrace().toString())
            null
        }
    }
}