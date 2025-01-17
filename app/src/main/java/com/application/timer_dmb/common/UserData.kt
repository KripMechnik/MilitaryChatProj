package com.application.timer_dmb.common

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


class UserData @Inject constructor (
    @ApplicationContext private val context: Context
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

    fun setToken(accessToken: String, refreshToken: String, accessTokenExpiresAt: Long, refreshTokenExpiresAt: Long): Boolean {
        _token.value = Token(accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt)
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.putLong("accessTokenExpiresAt", accessTokenExpiresAt)
        editor.putLong("refreshTokenExpiresAt", refreshTokenExpiresAt)
        Log.i("set_token", accessToken)
        return editor.commit()

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
        if(token.value.accessToken.isNotBlank() && token.value.refreshToken.isNotBlank() && token.value.accessTokenExpiresAt.toInt() != -1 && token.value.refreshTokenExpiresAt.toInt() != -1){
            val localDateTime = LocalDateTime.now()

            val zoneId = ZoneId.systemDefault()

            val instant = localDateTime.atZone(zoneId).toInstant()

            val milliseconds = instant.toEpochMilli()
            if (token.value.accessTokenExpiresAt <= milliseconds){
                deleteToken()
                return false
            } else {
                return true
            }
        } else {
            return false
        }
    }

    fun deleteToken(){
        editor.putString("accessToken", "")
        editor.putString("refreshToken", "")
        editor.putLong("accessTokenExpiresAt", -1)
        editor.putLong("refreshTokenExpiresAt", -1)
        editor.apply()
    }

}