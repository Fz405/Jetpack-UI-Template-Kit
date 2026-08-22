package com.template.composeuikit.utils

import android.util.Patterns

object AuthValidator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.isNotBlank() && password.length >= 6
    }

    // Add this new function for the SignUp screen!
    fun isNameValid(name: String): Boolean {
        return name.isNotBlank() && name.length >= 2
    }
}