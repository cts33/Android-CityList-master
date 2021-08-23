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
        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOADING);

    }
    public void showLoadFailed( ) {
        showLoadFailed(null);
    }


    public void showLoadFailed(ILoadingView.IRetryClickListener iRetryClickListener) {
        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOAD_FAILED);
        innerView.setIRetryClickListener(iRetryClickListener);
    }

    public void showLoadSuccess() {

        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOAD_SUCCESS);
    }


}
