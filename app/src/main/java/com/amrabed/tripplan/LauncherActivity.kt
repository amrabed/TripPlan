package com.amrabed.tripplan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.amrabed.tripplan.auth.Authenticator
import com.firebase.ui.auth.IdpResponse

class LauncherActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        if (Authenticator.user == null) {
            startActivityForResult(Authenticator.createSignInIntent(), SIGN_IN_REQUEST)
        } else {
            startMainActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST) {
            when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, R.string.signedIn, Toast.LENGTH_LONG).show()
                }
                RESULT_CANCELED -> {
                    val response = IdpResponse.fromResultIntent(data)
                    if (response == null) {
                        Toast.makeText(this, R.string.notSignedIn, Toast.LENGTH_LONG).show()
                    } else {
                        val message = response.error?.message as String
                        Log.e(NAME, message)
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                    }
                }
            }
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        const val SIGN_IN_REQUEST = 101
        val NAME: String = LauncherActivity::class.java.simpleName
    }
}