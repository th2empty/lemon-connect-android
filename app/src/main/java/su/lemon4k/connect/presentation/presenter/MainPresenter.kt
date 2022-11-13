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
import su.lemon4k.connect.model.exceptions.InternalServerErrorException
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.ui.views.MainView
import su.lemon4k.connect.model.Account as LemonAccount

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun getAccessToken(sharedPreferences: SharedPreferences, mAccountManager: AccountManager): String? {
        val accountName = sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED)
        Log.i(TAG, accountName.toString())
        if (accountName == Constants.KEY_UNAUTHORIZED) {
            return null
        }

        val account = Account(
            sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED),
            Constants.ACCOUNT_TYPE)
        val bundle = mAccountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE,
            null, true, null, null).result

        return bundle.getString(AccountManager.KEY_AUTHTOKEN)
    }

    fun isUserLoggedIn(sharedPreferences: SharedPreferences, mAccountManager: AccountManager):
            Boolean {
        return try { // TODO: add token validity check
            val accessToken = getAccessToken(sharedPreferences, mAccountManager)
            Log.i(TAG, (accessToken != null).toString())
            accessToken != null
        } catch (ex: AuthenticatorException) {
            ex.printStackTrace()
            false
        }
    }

    fun getProfileInfo(accountManager: AccountManager, sharedPreferences: SharedPreferences): User? {
        return try {
            val user = User()
            user.getProfileInfo(accountManager, sharedPreferences)
        } catch (ex: InternalServerErrorException) {
            Log.e(TAG, ex.printStackTrace().toString())
            null
        } catch (ex: UnauthorizedException) {
            Log.e(TAG, ex.printStackTrace().toString())
            // TODO: add re-enter password
            null
        } catch (ex: Exception) {
            Log.e(TAG, ex.printStackTrace().toString())
            null
        }
    }

    fun logout(accessToken: String): Boolean? {
        return try {
            val account = LemonAccount()
            val result = account.logout(accessToken) ?: return null
            result
        } catch (ex: Exception) {
            Log.i(TAG, ex.printStackTrace().toString())
            null
        }
    }
}