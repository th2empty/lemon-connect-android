package su.lemon4k.connect.model

import android.accounts.AccountManager
import android.content.SharedPreferences
import android.util.Log
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.exceptions.UnauthorizedException

data class User(
    var id: Long? = null,
    var avatar_url: String? = null,
    var email: String? = null,
    var username: String? = null) {

    fun getProfileInfo(accountManager: AccountManager, sharedPreferences: SharedPreferences): User {
        val account = android.accounts.Account(
            sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED),
            Constants.ACCOUNT_TYPE
        )
        val bundle = accountManager.getAuthToken(account, Constants.AUTH_TOKEN_TYPE,
            null, true, null, null).result
        val accessToken = bundle.getString(AccountManager.KEY_AUTHTOKEN) ?: throw UnauthorizedException()
        Log.i("User", accessToken)
        val lemonAccount = Account()
        val response =
            lemonAccount.getProfileInfo(accessToken) ?: throw NullPointerException("empty answer")

        this.id = response.id
        this.avatar_url = response.avatar_url
        this.email = response.email
        this.username = response.username

        if (this.avatar_url.equals("none"))
            this.avatar_url = null

        return this
    }
}
