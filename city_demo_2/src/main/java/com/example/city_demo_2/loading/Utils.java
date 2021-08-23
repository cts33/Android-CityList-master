package com.example.city_demo_2.loading;

import android.util.Log;

import java.util.Locale;

public class Utils {

    private static final String TAG = "Utils";
    public static String getRandomImage() {
        int id = (int) (Math.random() * 100000);
//        int id = -1;
        String url = String.format(Locale.CHINA, "https://www.thiswaifudoesnotexist.net/example-%d.jpg", id);
        Log.d(TAG, "getRandomImage: "+url);
        return url;
    }

}
