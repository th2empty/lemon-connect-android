package su.lemon4k.connect.ui.activity

import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
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

    //private val scope = MainScope()
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

        init()
    }

    private fun init() {
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

        settingsView = findViewById(R.id.settings_ll)

        settingsView.setOnClickListener(settingsClick())

        viewRetry.setOnClickListener {
            init()
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

    override fun showLoading() {

    }

    override fun hideLoading() {
    }
}