package su.lemon4k.connect.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import moxy.ktx.moxyPresenter
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.activity.SignInActivity
import su.lemon4k.connect.ui.views.SignUpView

class SignUpStepOneFragment : MvpFragment(), SignUpView {

    private val presenter by moxyPresenter { SignUpPresenter() }

    // Views
    private lateinit var inputEmail: EditText
    private lateinit var btnContinue: Button
    private lateinit var errorMessageView: TextView
    private lateinit var btnSignIn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_step_one, container, false)

        inputEmail = v.findViewById(R.id.input_email)
        btnContinue = v.findViewById(R.id.continue_button)
        errorMessageView = v.findViewById(R.id.error_message_tv)
        btnSignIn = v.findViewById(R.id.sign_in_btn)

        inputEmail.addTextChangedListener { hideError() }
        btnContinue.setOnClickListener(continueButtonClick())
        btnSignIn.setOnClickListener(signInButtonClick())

        return v
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_field_cannot_be_empty)
        errorMessageView.visibility = View.VISIBLE
        btnContinue.isEnabled = false
    }

    private fun hideError() {
        errorMessageView.visibility = View.GONE
        btnContinue.isEnabled = true
    }

    private fun continueButtonClick() = View.OnClickListener {
        when (true) {
            inputEmail.text.isEmpty() -> {
                showEmptyFieldError()
                return@OnClickListener
            }
            !Patterns.EMAIL_ADDRESS.matcher(inputEmail.text).matches() -> {
                showIncorrectEmailError()
                return@OnClickListener
            }
            !presenter.emailIsFree() -> {
                showAlreadyRegisteredError()
                return@OnClickListener
            }
            else -> findNavController().navigate(R.id.signUpStepTwoFragment)
        }
    }

    private fun signInButtonClick() = View.OnClickListener {
        startActivity(Intent(this.context, SignInActivity::class.java))
        activity?.finish()
    }

    private fun showAlreadyRegisteredError() {
        errorMessageView.text = getString(R.string.st_email_already_registered)
        errorMessageView.visibility = View.VISIBLE
        btnContinue.isEnabled = false
    }

    private fun showIncorrectEmailError() {
        errorMessageView.text = getString(R.string.st_incorrect_email)
        errorMessageView.visibility = View.VISIBLE
        btnContinue.isEnabled = false
    }
}