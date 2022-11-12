package su.lemon4k.connect.presentation.presenter

import android.accounts.Account
import android.accounts.AccountManager
import android.content.SharedPreferences
import android.util.Log
import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.Auth
import su.lemon4k.connect.model.exceptions.InternalServerError
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.network.input.SignInInput
import su.lemon4k.connect.ui.views.SignInView

@InjectViewState
class SignInPresenter : MvpPresenter<SignInView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signIn(accountManager: AccountManager, sharedPreferences: SharedPreferences,
               input: SignInInput, callback: () -> Unit) {
        val auth = Auth()
        auth.signIn(accountManager, sharedPreferences, input)
        callback()
    }
}