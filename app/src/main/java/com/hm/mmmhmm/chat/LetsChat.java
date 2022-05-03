package com.hm.mmmhmm.chat;

import android.app.Application;
import android.content.Context;


/**
 * Created by HP on 3/21/2018.
 */

public class LetsChat extends Application {
    private static Context mContext;


    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CameraApplication.init(this,true);
        mContext = getApplicationContext();
    }

}
