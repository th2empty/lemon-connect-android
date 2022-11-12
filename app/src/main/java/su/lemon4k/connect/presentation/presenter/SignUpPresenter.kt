package su.lemon4k.connect.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.ui.views.SignUpView

@InjectViewState
class SignUpPresenter : MvpPresenter<SignUpView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun emailIsFree(): Boolean {
        return true
    }

    fun usernameIsFree(): Boolean {
        return true
    }
}