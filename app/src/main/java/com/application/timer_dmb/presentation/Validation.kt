package com.application.timer_dmb.presentation

object Validation {

    private const val ALPHABET = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890_"

    fun validatePassword(password: String) : Boolean{
        return password.length in 6..127
    }

    fun validateNickname(nickname: String) : Boolean {
        if (nickname.length in 4..30){
            if (nickname.all { it in ALPHABET }) return true
        }
        return false
    }
}