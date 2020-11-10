package com.amrabed.tripplan.auth

import android.content.Intent
import com.amrabed.tripplan.R
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


object Authenticator {
    private val auth get() = FirebaseAuth.getInstance()
    val user get() = auth.currentUser

    fun createSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        return AuthUI.getInstance().createSignInIntentBuilder()
            .enableAnonymousUsersAutoUpgrade()
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.AppTheme_FullScreen)
            .setLogo(R.drawable.app_icon)
            .setAvailableProviders(providers)
            .build()
    }

    fun createUser(email: String, password: String, listener: OnCompleteListener<AuthResult>) =
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener)

    fun setCurrentUser(user: FirebaseUser) = auth.updateCurrentUser(user)

    fun signOut() {
        auth.signOut()
    }
}