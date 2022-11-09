package su.lemon4k.connect.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import su.lemon4k.connect.R
import su.lemon4k.connect.ui.activity.SignInActivity


// TODO: create SignUpStepOneView & SignUpStepOnePresenter
class SignUpStepOneFragment : Fragment(R.layout.sign_up_step_one_fragment) {

    private lateinit var inputEmailView: EditText
    private lateinit var btnSignIn: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputEmailView = view.findViewById(R.id.input_email)
        btnSignIn = view.findViewById(R.id.sign_in_btn)

        btnSignIn.setOnClickListener(signInButtonClick())
    }

    private fun signInButtonClick(): View.OnClickListener {
        return View.OnClickListener {
            startActivity(Intent(view?.context, SignInActivity::class.java))
            activity?.finish()
        }
    }
}