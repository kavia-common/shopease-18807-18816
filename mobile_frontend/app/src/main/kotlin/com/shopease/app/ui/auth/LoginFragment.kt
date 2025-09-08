package com.shopease.app.ui.auth

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.common.VMProviders

/**
 * PUBLIC_INTERFACE
 * LoginFragment
 */
class LoginFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.AuthViewModel> {
        VMProviders.auth(ServiceLocator.authRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val email = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val login = view.findViewById<Button>(R.id.login)
        val toSignup = view.findViewById<Button>(R.id.to_signup)

        login.setOnClickListener {
            vm.login(email.text.toString(), password.text.toString()) { err ->
                if (err == null) (activity as? AuthActivity)?.proceedToApp()
                else Toast.makeText(requireContext(), err.message ?: "Failed", Toast.LENGTH_LONG).show()
            }
        }
        toSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_container, SignupFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        // PUBLIC_INTERFACE
        fun newInstance() = LoginFragment()
    }
}
