package su.lemon4k.connect.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import su.lemon4k.connect.R

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFirstFragment : Fragment() {

    // Views
    private lateinit var inputEmail: EditText
    private lateinit var btnContinue: Button
    private lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_sign_up_first, container, false)

        inputEmail = v.findViewById(R.id.input_email)
        btnContinue = v.findViewById(R.id.continue_button)
        btnSignIn = v.findViewById(R.id.sign_in_btn)

        btnContinue.setOnClickListener(continueButtonClick())

        return v
    }

    private fun continueButtonClick() = View.OnClickListener {
        findNavController().navigate(R.id.signUpSecondFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFirstFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFirstFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}