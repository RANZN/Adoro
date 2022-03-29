package com.hm.mmmhmm.helper

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.hm.mmmhmm.R
import com.hm.mmmhmm.models.DefaultResponse
import java.io.Reader

class CommanFunction {

    companion object{
        fun handleApiError(errorBody: Reader?, context: Context) {
            var str = context.getString(R.string.please_ret_again)
            try {
                str = Gson().fromJson(errorBody, DefaultResponse::class.java).message
            } catch (e: Exception) {
                str = context.getString(R.string.please_ret_again)
            }
            if (str.isEmpty()) {
                str = context.getString(R.string.please_ret_again)
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

            //context.showToast(str)
        }

    }
}