package su.lemon4k.connect.model

import android.accounts.Account
import android.accounts.AccountManager
import android.content.SharedPreferences
import retrofit2.Response
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.exceptions.InternalServerErrorException
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.network.LemonConnectApi
import su.lemon4k.connect.network.input.SignInInput
import su.lemon4k.connect.network.input.SignUpInput
import su.lemon4k.connect.network.response.RegisteredResponse
import su.lemon4k.connect.network.response.SignInResponse
import su.lemon4k.connect.network.response.SignUpResponse

open class Auth {
    private val constants = Constants()
    private val api = LemonConnectApi().getApi()

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signUp(accountManager: AccountManager?, sharedPreferences:
            SharedPreferences?, input: SignUpInput): SignUpResponse? {
        val response = api.signUp(input).execute()
        if (response.isSuccessful) {
            signIn(accountManager, sharedPreferences, SignInInput(input.username, input.password))
            return response.body()
        } else {
            when (response.code()) {
                400 -> throw Exception(response.message())
                500 -> throw InternalServerErrorException(response.message())
            }
        }

        return null
    }

    fun signIn(accountManager: AccountManager?, sharedPreferences:
            SharedPreferences?, input: SignInInput): SignInResponse? {
        val response = api.signIn(input).execute()
        if (response.isSuccessful) {
            saveAccount(accountManager!!, sharedPreferences!!, input, response.body())
            return response.body()
        } else {
            when (response.code()) {
                400 -> throw Exception(response.message())
                401 -> throw UnauthorizedException(response.message())
                500 -> throw InternalServerErrorException(response.message())
            }
        }

        return null
    }

    fun findEmail(email: String): Response<RegisteredResponse> {
        return api.findEmail(email).execute()
    }

    fun findUsername(username: String): Response<RegisteredResponse> {
        return api.findUsername(username).execute()
    }

    private fun saveAccount(accountManager: AccountManager, sharedPreferences:
    SharedPreferences, input: SignInInput, responseBody: SignInResponse?) {
        Account(input.username, Constants.ACCOUNT_TYPE).also { account ->
            accountManager.addAccountExplicitly(account, input.password, null)
            accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, responseBody?.access_token)
            accountManager.setUserData(account, Constants.KEY_REFRESH_TOKEN, responseBody?.refresh_token)
        }

        with (sharedPreferences.edit()) {
            putString(Constants.KEY_ACCOUNT_NAME, input.username)
            apply()
        }
    }
}