package su.lemon4k.huskyid.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import su.lemon4k.huskyid.model.User
import su.lemon4k.huskyid.model.network.SignInInput
import su.lemon4k.huskyid.ui.activity.SignInActivity

class Authenticator(context: Context) : AbstractAccountAuthenticator(context) {

    private val mContext = context

    override fun editProperties(
        response: AccountAuthenticatorResponse?,
        accountType: String?
    ): Bundle? = null

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val intent = Intent(mContext, SignInActivity::class.java)
        intent.putExtra(Constants.ACCOUNT_TYPE, accountType)
        intent.putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType)
        intent.putExtra(Constants.KEY_IS_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle? = null

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        val mAccountManager = AccountManager.get(mContext)
        var accessToken: String = mAccountManager.peekAuthToken(account, Constants.AUTH_TOKEN_TYPE)
        val password: String = mAccountManager.getPassword(account)

        // if access token is empty but the password is not empty try to auth
        if (accessToken.isEmpty() && password.isNotEmpty()) {
            // TODO: if RefreshToken saved update AccessToken without new signing in
            val user = User()
            val authResponse = user.signIn(SignInInput(account?.name!!, password))

            if (authResponse.isSuccessful) {
                accessToken = authResponse.body()?.access_token ?: ""
            }
        }

        if (accessToken.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            bundle.putString(AccountManager.KEY_AUTHTOKEN, accessToken)
            return bundle
        }

        val intent = Intent(mContext, SignInActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(Constants.ACCOUNT_TYPE, account?.type)
        intent.putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType)

        if (account != null) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name)
        }

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun getAuthTokenLabel(authTokenType: String?): String? = null

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? = null

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle? = null

}