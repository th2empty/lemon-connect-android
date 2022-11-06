package su.lemon4k.huskyid.presentation.presenter

import android.util.Log
import android.widget.Toast
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import su.lemon4k.huskyid.model.UserModel
import su.lemon4k.huskyid.model.webservice.ErrorResponse
import su.lemon4k.huskyid.model.webservice.SignInInput
import su.lemon4k.huskyid.model.webservice.SignInResponse
import su.lemon4k.huskyid.model.webservice.UserCallback
import su.lemon4k.huskyid.ui.views.SignInView

@InjectViewState
class SignInPresenter : MvpPresenter<SignInView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signIn(input: SignInInput): ErrorResponse? {
        val user = UserModel()
        try {
            user.signIn(input)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}