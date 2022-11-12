package su.lemon4k.connect.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import moxy.ktx.moxyPresenter
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.views.SignUpView


class SignUpFinalStepFragment : MvpFragment(), SignUpView {

    private val presenter by moxyPresenter { SignUpPresenter() }

    // Views
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var errorMessageView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_final_step, container, false)

        inputPassword = v.findViewById(R.id.input_password)
        inputConfirmPassword = v.findViewById(R.id.input_confirm_password)
        errorMessageView = v.findViewById(R.id.error_message_tv)
        btnSignUp = v.findViewById(R.id.sign_up_btn)

        btnSignUp.setOnClickListener(signUpButtonClick())

        return v
    }

    private fun signUpButtonClick() = View.OnClickListener {
        if (inputPassword.text.isEmpty() || inputConfirmPassword.text.isEmpty()) {
            showEmptyFieldError()
            return@OnClickListener
        }

        Toast.makeText(this.context, "In progress...", Toast.LENGTH_LONG).show()
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_field_cannot_be_empty)
        errorMessageView.visibility = View.VISIBLE
    }

    private fun weakPasswordError() {
        errorMessageView.text = getString(R.string.st_password_lenght)
        errorMessageView.visibility = View.VISIBLE
    }
}