package com.hm.mmmhmm.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.hm.mmmhmm.models.UserData

object SessionManager {


    private lateinit var prefs: SharedPreferences

    private const val PREFS_NAME = "com.hm.mmmhmm.session.secure"

    private const val ACCESS_TOKEN = "access_token"
    private const val FCM_TOKEN = "fcm_token"
    private const val LOGIN_STATUS = "false"
    private const val USER_ID = "user_id"
    private const val OTP = "otp"
    private const val USER_NAME = "user_name"
    private const val USERNAME = "username"
    private const val USER_DATA = "user_data"
    private const val USER_EMAIL = "user_email"
    private const val USER_PHONE = "user_phone"
    private const val USER_IMAGE = "user_image"
    private const val LANGUAGE = "language"
    private const val FIREBASE_USER_ID = "firebase_user_id"


    //    ----/ Instantiating /--------------------------------------------------
    fun init(context: Context) {
        prefs = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
    }

    //    -------------/ACCESS  TOKEN /--------------------------------
    fun setAccessToken(token: String) {

        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(ACCESS_TOKEN, token)
            commit()
        }
    }

    fun getAccessToken(): String? {
        return prefs.getString(
            ACCESS_TOKEN,
            ""
        )
    }

    //=========================FCM Token=======================//
    fun setFcmToken(token: String) {

        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(FCM_TOKEN, token)
            commit()
        }
    }

    fun getFCMToken(): String? {
        return prefs.getString(
            FCM_TOKEN,
            ""
        )
    }

    //    -------------/ USER ID /----------------------
    fun setUserId(id: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_ID, id)
            commit()
        }
    }
    fun getUserId(): String? {
        return prefs.getString(
            USER_ID,
            "0"
        )
    }


    fun setOTP(id: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(OTP, id)
            commit()
        }
    }

    fun getOTP(): String? {
        return prefs.getString(OTP, OTP)
    }



    fun setUserData(userData: UserData?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_DATA, Gson().toJson(userData))
            commit()
        }
    }

    fun getUserData(): UserData? {
        val data = prefs.getString(
            USER_DATA,
            USER_NAME
        )
        return Gson().fromJson(data, UserData::class.java)
    }

    //    ----------------LOGIN STATUS--------------
    fun setLoginStatus(status: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(LOGIN_STATUS, status)
            commit()
        }
    }

    fun getLoginStatus(): String? {
        return prefs.getString(
            LOGIN_STATUS,
            LOGIN_STATUS
        )
    }
    fun setUserPic(image: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_IMAGE, image)
            commit()
        }
    }

    fun getUserPic(): String? {
        return prefs.getString(
            USER_IMAGE,
            USER_IMAGE
        )
    }

    fun setUserName(username: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USER_NAME, username)
            commit()
        }
    }

    fun getUserName(): String? {
        return prefs.getString(
            USER_NAME,
            USER_NAME
        )
    }

    fun setUsername(username: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(USERNAME, username)
            commit()
        }
    }

    fun getUsername(): String? {
        return prefs.getString(
            USERNAME,
            USERNAME
        )
    }

    //    -------------LANGUAGE-------------------------
    fun setLanguage(language: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(LANGUAGE, language)
            commit()
        }
    }

    fun getLanguage(): String? {
        return prefs.getString(
            LANGUAGE,
            LANGUAGE
        )
    }

    fun setFirbaseID(id: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(FIREBASE_USER_ID, id)
            commit()
        }
    }

    fun getFirbaseID(): String? {
        return prefs.getString(
            FIREBASE_USER_ID,
            FIREBASE_USER_ID
        )
    }


    //    -----/LOG OUT /-------------
    fun logout() {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            prefsEditor.clear()
            commit()
        }
    }


}