package com.shopease.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shopease.app.R
import com.shopease.app.data.repository.ServiceLocator
import com.shopease.app.ui.auth.AuthActivity
import com.shopease.app.ui.common.VMProviders

/**
 * PUBLIC_INTERFACE
 * ProfileFragment shows user info and logout.
 */
class ProfileFragment : Fragment() {

    private val vm by viewModels<com.shopease.app.ui.common.AuthViewModel> {
        VMProviders.auth(ServiceLocator.authRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val info = view.findViewById<TextView>(R.id.info)
        val logout = view.findViewById<Button>(R.id.logout)
        vm.token.observe(viewLifecycleOwner) { token ->
            info.text = if (token != null) "Logged in. Token: $token" else "Not logged in."
        }
        logout.setOnClickListener {
            vm.logout {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                requireActivity().finish()
            }
        }
    }
}
