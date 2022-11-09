package su.lemon4k.connect.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.*
import com.arellomobile.mvp.presenter.InjectPresenter
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.fragment.SignUpStepOneFragment
import su.lemon4k.connect.ui.views.SignUpView

class SignUpActivity : FragmentActivity(), SignUpView {

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    private val mFragmentManager: FragmentManager = supportFragmentManager
    private val fragmentTransaction = mFragmentManager.beginTransaction()

    // Views
    private lateinit var fragmentView: FragmentContainerView
    private lateinit var inputEmailView: EditText
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    override fun init() {
        fragmentView = findViewById(R.id.fragment_container_view)
        fragmentTransaction.attach(SignUpStepOneFragment()).commit()
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