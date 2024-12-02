package com.application.militarychatproject.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class UserData @Inject constructor (
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    inner class Token(
        val accessToken: String = "",
        val refreshToken: String = "",
        val accessTokenExpiresAt: Long = -1,
        val refreshTokenExpiresAt: Long = -1
    )

    private val _token = MutableStateFlow(Token())
    val token = _token.asStateFlow()

    fun setToken(accessToken: String, refreshToken: String, accessTokenExpiresAt: Long, refreshTokenExpiresAt: Long) {
        _token.value = Token(accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt)

        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.putLong("accessTokenExpiresAt", accessTokenExpiresAt)
        editor.putLong("refreshTokenExpiresAt", refreshTokenExpiresAt)
        editor.apply()
    }

    fun loadToken(){
        val accessToken = sharedPrefs.getString("accessToken", "")
        val refreshToken = sharedPrefs.getString("refreshToken", "")
        val accessTokenExpiresAt = sharedPrefs.getLong("accessTokenExpiresAt", -1)
        val refreshTokenExpiresAt = sharedPrefs.getLong("refreshTokenExpiresAt", -1)
        _token.value = Token(accessToken ?: "", refreshToken ?: "", accessTokenExpiresAt, refreshTokenExpiresAt)
    }

    fun checkAuthorized(): Boolean{
        loadToken()
        return token.value.accessToken.isNotBlank() && token.value.refreshToken.isNotBlank() && token.value.accessTokenExpiresAt.toInt() != -1 && token.value.refreshTokenExpiresAt.toInt() != -1
    }

}