package su.lemon4k.connect.ui.activity

import android.os.Bundle
import androidx.fragment.app.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.views.SignUpView

class SignUpActivity : FragmentActivity(), SignUpView {

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
    }

    override fun showEmptyFieldError() {
    }

    override fun showAlreadyRegisteredError() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }
}