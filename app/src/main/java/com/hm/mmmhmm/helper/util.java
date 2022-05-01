package com.hm.mmmhmm.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;

import com.firebase.client.Firebase;
import com.hm.mmmhmm.R;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.statusbar_gradient);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    public static boolean isValidEmail(String email) {
        String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
        Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static void setOnlineStatus(Context context) {
        Firebase.setAndroidContext(context);
//        Toast.makeText(context, "https://lunetin-59aa3-default-rtdb.firebaseio.com/status/"+ Utility.getLoginId() + "_" + Utility.getUserName(), Toast.LENGTH_SHORT).show();
        Firebase  referenc4 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/statusNew/"+SessionManager.INSTANCE.getUserId());
        referenc4.child("status").setValue("1");
        referenc4.child("timestamp").setValue(String.valueOf(new Date().getTime()));
    }
    public static void setOfflineStatus(Context context) {
        Firebase.setAndroidContext(context);
//        Toast.makeText(context, "https://lunetin-59aa3-default-rtdb.firebaseio.com/status/"+ Utility.getLoginId() + "_" + Utility.getUserName(), Toast.LENGTH_SHORT).show();
        Firebase  referenc4 = new Firebase("https://lunetin-59aa3-default-rtdb.firebaseio.com/statusNew/"+SessionManager.INSTANCE.getUserId() );
        referenc4.child("status").setValue("0");
        referenc4.child("timestamp").setValue(String.valueOf(new Date().getTime()));
    }



}
