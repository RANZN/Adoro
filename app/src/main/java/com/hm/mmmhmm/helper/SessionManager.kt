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
    private const val lastFirstVisiblePosition = "lastFirstVisiblePosition"
    private const val ADORO_COINS = "adoro_coins"
    private const val USER_DATA = "user_data"
    private const val USER_EMAIL = "user_email"
    private const val referCode = "refer_code"
    private const val adoroCoins = "adoro_coins"
    private const val adoroShield = "adoro_shield"
    private const val totalFollowers = "total_followers"
    private const val USER_PHONE = "user_phone"
    private const val USER_IMAGE = "user_image"
    private const val LANGUAGE = "language"
    private const val ACCOUNT_NUMBER = "account_number"
    private const val ACCOUNT_HOLDER = "account_holder"
    private const val IFSC_CODE = "ifsc_code"
    private const val BANK_NAME = "bank_name"
    private const val PAN_CARD = "pan_card"
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

    fun setFeedLastPosition(value: Int) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putInt(lastFirstVisiblePosition, value)
            commit()
        }
    }


    fun getFeedLastPosition(): Int {
        return prefs.getInt(
            lastFirstVisiblePosition,
            0
        )
    }

    fun setAccountNumber(value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(ACCOUNT_NUMBER, value)
            commit()
        }
    }

    fun getAccountNumber(): String? {
        return prefs.getString(
            ACCOUNT_NUMBER,
            ""
        )
    }

    fun setAccountHolder(value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(ACCOUNT_HOLDER, value)
            commit()
        }
    }

    fun getAccountHolder(): String? {
        return prefs.getString(
            ACCOUNT_HOLDER,
            ""
        )
    }

    fun setIFSC(value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(IFSC_CODE, value)
            commit()
        }
    }

    fun getIFSC(): String? {
        return prefs.getString(
            IFSC_CODE,
            ""
        )
    }


    fun setBank(value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(BANK_NAME, value)
            commit()
        }
    }

    fun getBank(): String? {
        return prefs.getString(
            BANK_NAME,
            ""
        )
    }

    fun setPanNumber(value: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(PAN_CARD, value)
            commit()
        }
    }

    fun getPanNumber(): String? {
        return prefs.getString(
            PAN_CARD,
            ""
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

    fun setFirebaseID(id: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString(FIREBASE_USER_ID, id)
            commit()
        }
    }

    fun getFirebaseID(): String? {
        return prefs.getString(
            FIREBASE_USER_ID,
            FIREBASE_USER_ID
        )
    }


    fun setChat(v: Boolean) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putBoolean("isChat", v)
            commit()
        }
    }


    fun setFriend(lat: String) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString("friendID", lat)
            commit()
        }
    }

    fun getFriend(): String? {
        return prefs.getString(
            "friendID",
            "friendID"
        )
    }

    fun isChat(): Boolean {
        return prefs.getBoolean(
            "isChat",
            false
        )
    }

    fun getUserNameId(): String? {
        return prefs.getString(
            "usernameid",
            "usernameid"
        )
    }

    fun setUserNameId(fName: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString("usernameid", fName)
            commit()
        }
    }

    fun setselectedUserNameId(fName: String?) {
        val prefsEditor: SharedPreferences.Editor = prefs.edit()
        with(prefsEditor) {
            putString("selected_id", fName)
            commit()
        }
    }

    fun getselectedUserNameId(): String? {
        return prefs.getString(
            "selected_id",
            ""
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

    fun setUserEmail(email: String) {
        prefs.edit().putString(USER_EMAIL, email).commit()
    }

    fun getUserEmail(): String {
        return prefs.getString(USER_EMAIL, "")!!
    }

    fun setRefrerCode(value: String) {
        prefs.edit().putString(referCode, value).commit()
    }

    fun getReferCode(): String {
        return prefs.getString(referCode, "")!!
    }

    fun setTotalFollowers(value: Int) {
        prefs.edit().putInt(totalFollowers, value).commit()
    }

    fun getTotalFollowers(): Int {
        return prefs.getInt(totalFollowers, 0)?:0
    }


    fun setAdoroCoins(value: Int) {
        prefs.edit().putInt(adoroCoins, value).commit()
    }

    fun getAdoroCoins(): Int {
        return prefs.getInt(adoroCoins, 0)
    }

    fun setAdoroShield(value: Int) {
        prefs.edit().putInt(adoroShield, value).commit()
    }

    fun getAdoroShield(): Int {
        return prefs.getInt(adoroShield, 0)
    }

    fun setUserPhone(value: String) {
        prefs.edit().putString(USER_PHONE, value).commit()
    }

    fun getUserPhone(): String {
        return prefs.getString(USER_PHONE, "")?:""
    }
}