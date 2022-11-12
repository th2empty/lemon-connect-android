package su.lemon4k.connect.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import moxy.ktx.moxyPresenter
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.views.SignUpView

class SignUpSecondFragment : MvpFragment(), SignUpView {

    private val presenter by moxyPresenter { SignUpPresenter() }

    // Views
    private lateinit var inputUsername: EditText
    private lateinit var btnContinue: Button
    private lateinit var errorMessageView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_step_two, container, false)

        inputUsername = v.findViewById(R.id.input_username)
        btnContinue = v.findViewById(R.id.continue_button)
        errorMessageView = v.findViewById(R.id.error_message_tv)

        btnContinue.setOnClickListener(continueButtonClick())

        return v
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_field_cannot_be_empty)
        errorMessageView.visibility = View.VISIBLE
    }

    private fun showAlreadyRegisteredError() {
        errorMessageView.text = getString(R.string.st_email_already_registered)
        errorMessageView.visibility = View.VISIBLE
    }

    private fun continueButtonClick() = View.OnClickListener {
        when (true) {
            inputUsername.text.isEmpty() -> {
                showEmptyFieldError()
                return@OnClickListener
            }
            !presenter.usernameIsFree() -> {
                showAlreadyRegisteredError()
                return@OnClickListener
            }
            else -> findNavController().navigate(R.id.signUpFinalStepFragment)
        }
    }
}