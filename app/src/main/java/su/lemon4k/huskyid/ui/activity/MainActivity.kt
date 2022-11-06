package su.lemon4k.huskyid.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.arellomobile.mvp.MvpActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import su.lemon4k.huskyid.R
import su.lemon4k.huskyid.model.UserModel
import su.lemon4k.huskyid.model.webservice.UserCallback
import su.lemon4k.huskyid.model.webservice.SignInInput
import su.lemon4k.huskyid.presentation.presenter.MainPresenter
import su.lemon4k.huskyid.ui.views.MainView

class MainActivity : MvpActivity(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        if (!presenter.isUserLoggedIn()) {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }
}