package su.lemon4k.connect.ui.activity

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import su.lemon4k.connect.R
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.exceptions.InternalServerError
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.network.SignInInput
import su.lemon4k.connect.presentation.presenter.SignInPresenter
import su.lemon4k.connect.ui.views.SignInView
import java.net.ConnectException

class SignInActivity : MvpActivity(), SignInView {

    private val scope = MainScope()

    private lateinit var mAccountManager: AccountManager
    private lateinit var sharedPreferences: SharedPreferences

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

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_empty_field)
        errorMessageView.visibility = View.VISIBLE
        loginInput.addTextChangedListener { errorMessageView.visibility = View.GONE }
        passwordInput.addTextChangedListener { errorMessageView.visibility = View.GONE }
        signInBtn.isEnabled = false
    }

    override fun showAuthError(message: String, t: Throwable?) {
        runBlocking(Dispatchers.Main) {
            t?.printStackTrace()
            hideLoading()
            errorMessageView.text = message
            errorMessageView.visibility = View.VISIBLE
            signInBtn.isEnabled = false
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

    override fun init() {
        mAccountManager = AccountManager.get(this)
        sharedPreferences =
            this.getSharedPreferences(Constants.KEY_ACCOUNT_PREFERENCE, Context.MODE_PRIVATE) ?: return

        loginInput = findViewById(R.id.login_input)
        passwordInput = findViewById(R.id.password_input)
        signInBtn = findViewById(R.id.sign_in_btn)
        signUpBtn = findViewById(R.id.sign_up_btn)
        errorMessageView = findViewById(R.id.error_message_tv)
        loadingProgressBar = findViewById(R.id.loading_pb)

        loginInput.addTextChangedListener { hideErrorMessage() }
        passwordInput.addTextChangedListener { hideErrorMessage() }
        signInBtn.setOnClickListener(signInButtonClick())
        signUpBtn.setOnClickListener(signUpButtonClick())
    }

    private fun hideErrorMessage() {
        errorMessageView.visibility = View.GONE
        signInBtn.isEnabled = true
    }

    private fun signInButtonClick(): View.OnClickListener {
        return View.OnClickListener {
            if (loginInput.text.isEmpty() || passwordInput.text.isEmpty()) {
                showEmptyFieldError()
                return@OnClickListener
            }

            showLoading()
            scope.launch(Dispatchers.IO) {
                try {
                    presenter.signIn(mAccountManager, sharedPreferences,
                        SignInInput(loginInput.text.toString(), passwordInput.text.toString())
                    ) {
                        runBlocking(Dispatchers.Main) {
                            hideLoading()
                            val intent = Intent(this@SignInActivity,
                                MainActivity::class.java)
                            startActivity(intent)
                            this@SignInActivity.finish()
                        }
                    }
                } catch (ex: UnauthorizedException) {
                    showAuthError(getString(R.string.st_invalid_username_or_password), ex)
                } catch (ex: InternalServerError) {
                    showErrorMessage(ex, getString(R.string.st_internal_server_error))
                } catch (ex: ConnectException) {
                    showErrorMessage(ex, getString(R.string.st_server_unavailable))
                }
            }
        }
    }

    private fun showErrorMessage(t: Throwable, message: String) {
        runBlocking(Dispatchers.Main) {
            t.printStackTrace()
            Toast.makeText(this@SignInActivity, message, Toast.LENGTH_LONG).show()
            hideLoading()
        }
    }

    private fun signUpButtonClick(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}