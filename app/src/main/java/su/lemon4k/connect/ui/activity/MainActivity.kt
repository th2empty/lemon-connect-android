package su.lemon4k.connect.ui.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import moxy.presenterScope
import su.lemon4k.connect.R
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.model.User
import su.lemon4k.connect.presentation.presenter.MainPresenter
import su.lemon4k.connect.ui.views.MainView
import su.lemon4k.connect.network.Constants as NetworkConstants

class MainActivity : MvpActivity(), MainView {

    private lateinit var mAccountManager: AccountManager
    private lateinit var sharedPreferences: SharedPreferences

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private var user: User? = null

    // Views
    private lateinit var settingsView: LinearLayout
    private lateinit var viewUsername: TextView
    private lateinit var viewPersonalId: TextView
    private lateinit var viewAvatar: CircleImageView
    private lateinit var viewError: LinearLayout
    private lateinit var viewRetry: Button
    private lateinit var loadingView: ProgressBar
    private lateinit var viewMain: RelativeLayout
    private lateinit var buttonLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewUsername = findViewById(R.id.view_username)
        viewPersonalId = findViewById(R.id.view_personal_id)
        viewAvatar = findViewById(R.id.view_avatar)
        viewMain = findViewById(R.id.view_main)
        viewError = findViewById(R.id.error_view)
        viewRetry = findViewById(R.id.btn_retry)
        loadingView = findViewById(R.id.progress_bar)
        buttonLogout = findViewById(R.id.button_logout)
        settingsView = findViewById(R.id.settings_ll)

        settingsView.setOnClickListener(settingsClick())
        viewRetry.setOnClickListener { init() }
        buttonLogout.setOnClickListener(buttonLogoutClick())

        init()
    }

    private fun init() {
        hideErrorView()
        showLoadingView()
        mAccountManager = AccountManager.get(this)
        sharedPreferences =
            this.getSharedPreferences(Constants.KEY_ACCOUNT_PREFERENCE, Context.MODE_PRIVATE) ?: return

        presenter.presenterScope.launch(Dispatchers.IO) {
            if (!presenter.isUserLoggedIn(sharedPreferences, mAccountManager)) {
                runBlocking(Dispatchers.Main) {
                    startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                    finish()
                }
            } else {
                user = presenter.getProfileInfo(mAccountManager, sharedPreferences)
                if (user != null) {
                    runBlocking(Dispatchers.Main) {
                        showProfileInfo()
                        hideLoadingView()
                    }
                }
                else {
                    runBlocking(Dispatchers.Main) {
                        hideLoadingView()
                        showErrorView()
                    }
                }
            }
        }
    }

    private fun showErrorView() {
        viewMain.visibility = View.GONE
        viewError.visibility = View.VISIBLE
    }

    private fun hideErrorView() {
        viewMain.visibility = View.VISIBLE
        viewError.visibility = View.GONE
    }

    private fun showLoadingView() {
        viewMain.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        viewMain.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
    }

    private fun showProfileInfo() {
        viewUsername.text = user?.username
        viewPersonalId.text = getString(R.string.user_id).format(user?.id)
        if (user?.avatar_url != null)
            Glide.with(this)
                .load(String.format("%s/%s", NetworkConstants().BASE_URL, user?.avatar_url))
                .into(viewAvatar)
    }

    private fun settingsClick(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    // TODO: fix when re-authorizing, the wrong token (?)
    //  is saved, as a result of which the server returns a 401 error
    private fun buttonLogoutClick() = View.OnClickListener {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.st_sign_out))
            .setMessage(getString(R.string.st_confirm_logout))
            .setPositiveButton(getString(R.string.st_sign_out)) { _, _ ->
                presenter.presenterScope.launch(Dispatchers.IO) {
                    val account = Account(
                        sharedPreferences.getString(Constants.KEY_ACCOUNT_NAME, Constants.KEY_UNAUTHORIZED),
                        Constants.ACCOUNT_TYPE)
                    val accessToken = presenter.getAccessToken(sharedPreferences, mAccountManager)
                    if (accessToken == null) {
                        runBlocking(Dispatchers.Main) {
                            mAccountManager.removeAccount(account, this@MainActivity, null, null)
                            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                            this@MainActivity.finish()
                        }
                        return@launch
                    }

                    val logoutResult = presenter.logout(accessToken)
                    if (logoutResult != false) {
                        runBlocking(Dispatchers.Main) {
                            mAccountManager.removeAccount(account, this@MainActivity, null, null)
                            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                            this@MainActivity.finish()
                        }
                    } else runBlocking(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.st_failed_to_logout), Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton(getString(R.string.st_cancel), null)
            .show()
    }

    override fun showLoading() {}

    override fun hideLoading() {}
}