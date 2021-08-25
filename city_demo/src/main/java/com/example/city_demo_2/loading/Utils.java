package com.example.city_demo_2.loading;

import android.util.Log;

import java.util.Locale;
import java.util.Random;

public class Utils {

    private static final String TAG = "Utils";
    public static String getRandomImage() {
        Random random = new Random();
        int id = random.nextInt(1000);
        id = id%2==0 ? -1:0;

        String url = String.format(Locale.CHINA, "https://www.thiswaifudoesnotexist.net/example-%d.jpg", id);
        Log.d(TAG, "getRandomImage: "+url);
        return url;
    }

}
