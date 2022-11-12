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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.ktx.moxyPresenter
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.activity.SignInActivity
import su.lemon4k.connect.ui.views.SignUpView

class SignUpStepOneFragment : MvpFragment(), SignUpView {

    private val presenter by moxyPresenter { SignUpPresenter() }
    private val scope = MainScope()

    // Views
    private lateinit var inputEmail: EditText
    private lateinit var btnContinue: Button
    private lateinit var errorMessageView: TextView
    private lateinit var btnSignIn: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_step_one, container, false)

        inputEmail = v.findViewById(R.id.input_email)
        btnContinue = v.findViewById(R.id.continue_button)
        errorMessageView = v.findViewById(R.id.error_message_tv)
        btnSignIn = v.findViewById(R.id.sign_in_btn)
        progressBar = v.findViewById(R.id.progress_bar)

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

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        btnContinue.visibility = View.GONE
    }

    fun hideLoading() {
        progressBar.visibility = View.GONE
        btnContinue.visibility = View.VISIBLE
    }

    private fun hideError() {
        errorMessageView.visibility = View.GONE
        btnContinue.isEnabled = true
    }

    private fun continueButtonClick() = View.OnClickListener {
        showLoading()
        scope.launch(Dispatchers.IO) {
            when (true) {
                inputEmail.text.isEmpty() ->
                    runBlocking(Dispatchers.Main) {
                        showEmptyFieldError()
                        hideLoading()
                    }
                !Patterns.EMAIL_ADDRESS.matcher(inputEmail.text).matches() ->
                    runBlocking(Dispatchers.Main) {
                        showIncorrectEmailError()
                        hideLoading()
                    }
                !presenter.emailIsFree(inputEmail.text.toString()) ->
                    runBlocking(Dispatchers.Main) {
                        showAlreadyRegisteredError()
                        hideLoading()
                    }
                else -> runBlocking(Dispatchers.Main) {
                    hideLoading()
                    val args = Bundle()
                    args.putString("email", inputEmail.text.toString())
                    findNavController().navigate(R.id.signUpStepTwoFragment, args)
                }
            }
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