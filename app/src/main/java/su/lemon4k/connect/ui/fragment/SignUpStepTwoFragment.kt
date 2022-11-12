package su.lemon4k.connect.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.ktx.moxyPresenter
import su.lemon4k.connect.MvpFragment
import su.lemon4k.connect.R
import su.lemon4k.connect.presentation.presenter.SignUpPresenter
import su.lemon4k.connect.ui.views.SignUpView

class SignUpSecondFragment : MvpFragment(), SignUpView {

    private val presenter by moxyPresenter { SignUpPresenter() }
    private val scope = MainScope()

    // Views
    private lateinit var inputUsername: EditText
    private lateinit var btnContinue: Button
    private lateinit var errorMessageView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, arguments.toString())
        email = arguments?.getString("email", "")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_step_two, container, false)

        inputUsername = v.findViewById(R.id.input_username)
        btnContinue = v.findViewById(R.id.continue_button)
        errorMessageView = v.findViewById(R.id.error_message_tv)
        progressBar = v.findViewById(R.id.progress_bar)

        inputUsername.addTextChangedListener { hideErrorMessage() }
        btnContinue.setOnClickListener(continueButtonClick())

        return v
    }

    override fun showEmptyFieldError() {
        errorMessageView.text = getString(R.string.st_field_cannot_be_empty)
        errorMessageView.visibility = View.VISIBLE
        btnContinue.isEnabled = false
    }

    fun showLoading() {
        progressBar.visibility = View.VISIBLE
        btnContinue.visibility = View.GONE
    }

    fun hideLoading() {
        progressBar.visibility = View.GONE
        btnContinue.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        errorMessageView.visibility = View.GONE
        btnContinue.isEnabled = true
    }

    private fun showAlreadyRegisteredError() {
        errorMessageView.text = getString(R.string.st_username_already_registered)
        errorMessageView.visibility = View.VISIBLE
        btnContinue.isEnabled = false
    }

    private fun continueButtonClick() = View.OnClickListener {
        scope.launch(Dispatchers.IO) {
            when (true) {
                inputUsername.text.isEmpty() ->
                    runBlocking(Dispatchers.Main) { showEmptyFieldError() }
                !presenter.usernameIsFree(inputUsername.text.toString()) ->
                    runBlocking(Dispatchers.Main) { showAlreadyRegisteredError() }
                else -> runBlocking(Dispatchers.Main) {
                    val args = Bundle()
                    args.putString("email", email)
                    args.putString("username", inputUsername.text.toString())
                    findNavController().navigate(R.id.signUpFinalStepFragment, args)
                }
            }
        }
    }

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }
}