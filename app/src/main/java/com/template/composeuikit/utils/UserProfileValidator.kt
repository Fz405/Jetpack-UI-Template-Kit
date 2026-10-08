package com.template.composeuikit.utils

object UserProfileValidator {
    fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank() && username.length >= 3
    }

    fun isBioValid(bio: String): Boolean {
        return bio.length <= 250
    }
}
