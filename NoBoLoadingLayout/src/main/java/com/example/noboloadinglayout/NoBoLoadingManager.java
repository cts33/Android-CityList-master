package com.example.noboloadinglayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

public class NoBoLoadingManager {


    private static final String TAG = "NoBoLoadingManager";
    private static NoBoLoadingManager noBoLoadingManager;
    private Context context;

    private NoBoLoadingManager(Context context) {
        this.context = context;
        initConfig(context);
    }


    private static NoBoLoadingManager getInstance(Context context) {

        if (noBoLoadingManager == null) {
            synchronized (NoBoLoadingManager.class) {

                noBoLoadingManager = new NoBoLoadingManager(context);
            }
        }
        return noBoLoadingManager;
    }


    private static NormalLoadingView innerView;

    private void initConfig(Context context) {

        // TODO 创建默认view 对象  各种状态
        if (innerView == null) {
            innerView = new NormalLoadingView(context);
        }
    }


    public static NoBoLoadingManager wrapActivity(FragmentActivity activity) {
        Log.d(TAG, "wrapActivity: "+activity.hashCode());
        getInstance(activity);

        ViewGroup wrapper = activity.findViewById(android.R.id.content);


        if (innerView != null) {
            wrapper.removeView(innerView);
            wrapper.addView(innerView);
        }


        return noBoLoadingManager;
    }

    public void wrapFragment() {

    }

    public void wrapView() {

    }

    public void loading()  {


        innerView.setStatus(NormalLoadingView.STATUS_LOADING);
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//
//        try {
//            int index = 0;
//            for (StackTraceElement e : stackTrace) {
//    //            if (e.getClassName().equals(Activity.class.getName())) {
//                Class<?> aClass = Class.forName(e.getClassName());
//
//                Log.d(TAG, index + "-----" + aClass+ "   \t" + e.getMethodName());
//                index++;
//    //            }
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
    public void showLoadFailed( ) {
        showLoadFailed(null);
    }


    public void showLoadFailed(IRetryClickListener iRetryClickListener) {
        innerView.setStatus(NormalLoadingView.STATUS_LOAD_FAILED);
        innerView.setiRetryClickListener(iRetryClickListener);
    }

    public void showLoadSuccess() {

        innerView.setStatus(NormalLoadingView.STATUS_LOAD_SUCCESS);
    }

    public static class Builder {


    }


}
