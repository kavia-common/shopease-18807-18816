package com.shopease.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shopease.app.R
import com.shopease.app.ui.MainActivity

/**
 * PUBLIC_INTERFACE
 * AuthActivity
 *
 * Hosts login and signup fragments.
 */
class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            val containerId = R.id.auth_container
            supportFragmentManager.beginTransaction()
                .replace(containerId, LoginFragment.newInstance())
                .commit()
        }
    }

    // PUBLIC_INTERFACE
    fun proceedToApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
