package su.lemon4k.connect.ui.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.appbar.MaterialToolbar
import su.lemon4k.connect.BuildConfig
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.AboutPresenter
import su.lemon4k.connect.ui.views.AboutView

class AboutActivity : MvpActivity(), AboutView {

    @InjectPresenter
    lateinit var presenter: AboutPresenter

    // Views
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var versionView: TextView
    private lateinit var openSourceLicensesView: TextView

    private val versionName: String = BuildConfig.VERSION_NAME
    private val versionCode: Int = BuildConfig.VERSION_CODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        initViews()
    }

    private fun initViews() {
        topAppBar = findViewById(R.id.top_app_bar)
        versionView = findViewById(R.id.version_tv)
        openSourceLicensesView = findViewById(R.id.open_source_licenses)

        topAppBar.setNavigationOnClickListener { finish() }
        versionView.text = getString(R.string.st_version).format(versionName, versionCode)
        openSourceLicensesView.setOnClickListener {
            Toast.makeText(this, "In progress...", Toast.LENGTH_LONG).show()
        }
    }
}