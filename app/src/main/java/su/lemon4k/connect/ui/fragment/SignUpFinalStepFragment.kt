package su.lemon4k.connect.ui.fragment

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.ktx.moxyPresenter
import moxy.presenterScope
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.network.input.SignUpInput
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.activity.MainActivity
import su.lemon4k.connect.ui.views.SignUpView


class SignUpFinalStepFragment : MvpFragment(), SignUpView {

    private val scope = MainScope()
    private val presenter by moxyPresenter { SignUpPresenter() }
    private lateinit var mAccountManager: AccountManager
    private lateinit var sharedPreferences: SharedPreferences

    // Views
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var errorMessageView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var email: String
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_final_step, container, false)

        initAccountManager()

        email = arguments?.getString("email", "")!!
        username = arguments?.getString("username", "")!!

        inputPassword = v.findViewById(R.id.input_password)
        inputConfirmPassword = v.findViewById(R.id.input_confirm_password)
        errorMessageView = v.findViewById(R.id.error_message_tv)
        btnSignUp = v.findViewById(R.id.sign_up_btn)
        progressBar = v.findViewById(R.id.progress_bar)

        inputPassword.addTextChangedListener { hideErrorMessage() }
        inputConfirmPassword.addTextChangedListener { hideErrorMessage() }
        btnSignUp.setOnClickListener(signUpButtonClick())

        return v
    }

    private fun initAccountManager() {
        mAccountManager = AccountManager.get(this.context)
        sharedPreferences =
            this.context?.getSharedPreferences(Constants.KEY_ACCOUNT_PREFERENCE, Context.MODE_PRIVATE) ?: return
    }

    private fun signUpButtonClick() = View.OnClickListener {
        when (true) {
            (inputPassword.text.isEmpty() || inputConfirmPassword.text.isEmpty()) -> {
                showEmptyFieldError()
                return@OnClickListener
            }
            (inputPassword.text.length < 6) -> {
                showWeakPasswordError()
                return@OnClickListener
            }
            (inputPassword.text.toString() != inputConfirmPassword.text.toString()) -> {
                showPasswordsMismatchError()
                return@OnClickListener
            }
            else -> {
                showLoading()
                presenter.presenterScope.launch(Dispatchers.IO) {
                    val input = SignUpInput(1, email, username, inputPassword.text.toString())
                    presenter.signUp(mAccountManager, sharedPreferences, input) {
                        runBlocking(Dispatchers.Main) {
                            hideLoading()
                            val intent = Intent(this@SignUpFinalStepFragment.context,
                                MainActivity::class.java)
                            startActivity(intent)
                            this@SignUpFinalStepFragment.activity?.finish()
                        }
                    }
                }
            }
        }
    }

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        btnSignUp.visibility = View.GONE
    }

    fun hideLoading() {
        progressBar.visibility = View.GONE
        btnSignUp.visibility = View.VISIBLE
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_field_cannot_be_empty)
        errorMessageView.visibility = View.VISIBLE
        btnSignUp.isEnabled = false
    }

    private fun hideErrorMessage() {
        errorMessageView.visibility = View.GONE
        btnSignUp.isEnabled = true
    }

    private fun showWeakPasswordError() {
        errorMessageView.text = getString(R.string.st_password_lenght)
        errorMessageView.visibility = View.VISIBLE
        btnSignUp.isEnabled = false
    }

    private fun showPasswordsMismatchError() {
        errorMessageView.text = getString(R.string.st_password_mismatch_error)
        errorMessageView.visibility = View.VISIBLE
        btnSignUp.isEnabled = false
    }
}