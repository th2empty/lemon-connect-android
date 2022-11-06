package su.lemon4k.huskyid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import su.lemon4k.huskyid.R
import su.lemon4k.huskyid.model.webservice.SignInInput
import su.lemon4k.huskyid.presentation.presenter.SignInPresenter
import su.lemon4k.huskyid.ui.views.SignInView

class SignInActivity : MvpActivity(), SignInView {

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    private lateinit var loginInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var errorMessageView: TextView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        init()
    }

    private fun init() {
        loginInput = findViewById(R.id.login_input)
        passwordInput = findViewById(R.id.password_input)
        signInBtn = findViewById(R.id.sign_in_btn)
        signUpBtn = findViewById(R.id.sign_up_btn)
        errorMessageView = findViewById(R.id.error_message_tv)
        loadingProgressBar = findViewById(R.id.loading_pb)

        signInBtn.setOnClickListener(signInButtonClick())
        signUpBtn.setOnClickListener(signUpButtonClick())
    }

    private fun signInButtonClick(): View.OnClickListener {
        return View.OnClickListener {
            if (loginInput.text.isEmpty() || passwordInput.text.isEmpty()) {
                showEmptyFieldError()
            } else {
                showLoading()

                val response = presenter.signIn(
                    SignInInput(loginInput.text.toString(), passwordInput.text.toString()))

                if (response != null) {
                    showAuthError(response.message)
                    hideLoading()
                } else {
                    hideLoading()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun signUpButtonClick(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "In progress...", Toast.LENGTH_LONG).show()
        }
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_empty_field)
        errorMessageView.visibility = View.VISIBLE
        loginInput.addTextChangedListener {
            errorMessageView.visibility = View.GONE
        }
    }

    override fun showAuthError(message: String) {
        errorMessageView.text = message
        errorMessageView.visibility = View.VISIBLE
        loginInput.addTextChangedListener {
            errorMessageView.visibility = View.GONE
        }
    }

    override fun showLoading() {
        loginInput.isEnabled = false
        signInBtn.visibility = View.GONE
        signUpBtn.isEnabled = false
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loginInput.isEnabled = true
        signInBtn.visibility = View.VISIBLE
        signUpBtn.isEnabled = true
        loadingProgressBar.visibility = View.GONE
    }
}