package su.lemon4k.huskyid.presentation.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.MainScope
import su.lemon4k.huskyid.account.Constants
import su.lemon4k.huskyid.ui.views.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun isUserLoggedIn(sharedPreferences: SharedPreferences, mAccountManager: AccountManager):
            Boolean {
        if (sharedPreferences
                .getString(
                    Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED
                ) == Constants.KEY_UNAUTHORIZED) {
            return false
        }

        val account = Account(
            sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED),
            Constants.ACCOUNT_TYPE)
        val bundle = mAccountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE,
            null, true, null, null).result

        val accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN)
        if (accessToken != null) { // TODO: token validity check
            Log.i(TAG, accessToken)
            return true
        }

        return false
    }
}