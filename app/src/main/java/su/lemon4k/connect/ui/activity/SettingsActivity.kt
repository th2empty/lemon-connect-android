package su.lemon4k.connect.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.appbar.MaterialToolbar
import moxy.MvpActivity
import moxy.presenter.InjectPresenter
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SettingsPresenter
import su.lemon4k.connect.ui.views.SettingsView

class SettingsActivity : MvpActivity(), SettingsView {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    // Views
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var checkUpdatesBtn: Button
    private lateinit var aboutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
    }

    private fun initViews() {
        topAppBar = findViewById(R.id.top_app_bar)
        checkUpdatesBtn = findViewById(R.id.check_for_updates_btn)
        aboutBtn = findViewById(R.id.about_btn)

        topAppBar.setNavigationOnClickListener { finish() }
        checkUpdatesBtn.setOnClickListener(checkUpdatesClick())
        aboutBtn.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun checkUpdatesClick(): View.OnClickListener {
        return View.OnClickListener {
            Toast.makeText(this, "In progress...", Toast.LENGTH_LONG).show()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}