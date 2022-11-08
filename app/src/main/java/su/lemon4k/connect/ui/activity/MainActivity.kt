package su.lemon4k.connect.ui.activity

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import su.lemon4k.connect.R
import su.lemon4k.connect.account.Constants
import su.lemon4k.connect.presentation.presenter.MainPresenter
import su.lemon4k.connect.ui.views.MainView

class MainActivity : MvpActivity(), MainView {

    private val scope = MainScope()
    private lateinit var mAccountManager: AccountManager
    private lateinit var sharedPreferences: SharedPreferences

    @InjectPresenter
    lateinit var presenter: MainPresenter

    // Views
    private lateinit var settingsView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        mAccountManager = AccountManager.get(this)
        sharedPreferences =
            this.getSharedPreferences(Constants.KEY_ACCOUNT_PREFERENCE, Context.MODE_PRIVATE) ?: return

        scope.launch(Dispatchers.IO) {
            if (!presenter.isUserLoggedIn(sharedPreferences, mAccountManager)) {
                runBlocking(Dispatchers.Main) {
                    startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                }
            }
        }

        settingsView = findViewById(R.id.settings_ll)

        settingsView.setOnClickListener(settingsClick())
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