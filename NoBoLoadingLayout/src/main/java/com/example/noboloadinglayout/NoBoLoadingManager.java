package com.example.noboloadinglayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class NoBoLoadingManager {


    private static final String TAG = "NoBoLoadingManager";
    private static volatile NoBoLoadingManager noBoLoadingManager;
    private Context context;
    private static ViewGroup wrapper;

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

    private LoadingView innerView;

    private void initConfig(Context context) {

        // TODO 创建默认view 对象  各种状态

    }


    public static NoBoLoadingManager wrapActivity(FragmentActivity activity) {
        Log.d(TAG, "wrapActivity: " + activity.hashCode());
        getInstance(activity);

        wrapper = activity.findViewById(android.R.id.content);

        return noBoLoadingManager;
    }

    private static boolean isLoaded = false;

    public static NoBoLoadingManager wrapFragment(Fragment fragment, View fragmentView) {
        getInstance(fragment.getContext());

        //如果已经加载过loading，不用再次加载
        if (isLoaded) {
            return noBoLoadingManager;
        }
        isLoaded = true;

        wrapper = new FrameLayout(fragment.getContext());
        ViewGroup.LayoutParams layoutParams = fragmentView.getLayoutParams();

        if (layoutParams != null) {

            FrameLayout.LayoutParams lay = new FrameLayout.LayoutParams(layoutParams.width,layoutParams.height);
            wrapper.setLayoutParams(lay);
        }

        ViewGroup parent;
        if ((parent = (ViewGroup) fragmentView.getParent()) != null) {
            parent.removeView(fragmentView);
        }
        wrapper.addView(fragmentView);
        return noBoLoadingManager;

    }

    public static NoBoLoadingManager wrapView( View rootView) {
        if (rootView==null)
            throw new NullPointerException("rootview is null");
        getInstance(rootView.getContext());

        //如果已经加载过loading，不用再次加载
        if (isLoaded) {
            return noBoLoadingManager;
        }
        isLoaded = true;

        wrapper = new FrameLayout(rootView.getContext());
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();

        if (layoutParams != null) {

            FrameLayout.LayoutParams lay = new FrameLayout.LayoutParams(layoutParams.width,layoutParams.height);
            wrapper.setLayoutParams(lay);
        }

        ViewGroup parent;
        if ((parent = (ViewGroup) rootView.getParent()) != null) {
            parent.removeView(rootView);
        }
        wrapper.addView(rootView);
        return noBoLoadingManager;

    }

    public static ViewGroup getWrapper() {

        ViewGroup parent;
        if ((parent = (ViewGroup) wrapper.getParent()) != null) {
            parent.removeView(wrapper);
        }
        return wrapper;
    }

    private void initView() {
        // 复用view
        LoadingView currView = (LoadingView) statusView.get(LoadingView.STATUS_LOADING);
        if (currView == null) {
            currView = (LoadingView) statusView.get(LoadingView.STATUS_LOAD_FAILED);
        }
        if (currView == null) {
            innerView = new NormalLoadingView(context);
            statusView.put(LoadingView.STATUS_LOADING, innerView);
//            preStatus = LoadingView.STATUS_LOADING;
            wrapper.addView(innerView);
        } else {
            ViewGroup parent;
            if ((parent = (ViewGroup) currView.getParent()) != null) {
                parent.removeView(currView);
            }
            innerView = currView;
            wrapper.addView(innerView);
            innerView.bringToFront();
        }
    }

    public void wrapView() {

    }

    public void showLoading() {
        showLoading(null);
    }

    private SparseArray<View> statusView = new SparseArray<>(4);

    /**
     * 执行loading
     */
    public void showLoading(LoadingView inputView) {

        if (inputView == null) {

            initView();
        } else {
            innerView = inputView;
        }

        checkNotNull();


        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOADING);
    }

    public NoBoLoadingManager showLoadFailed() {

        showLoadFailed(null);
        return this;
    }

    public void showRetry(LoadingView.IRetryClickListener iRetryClickListener) {
        checkNotNull();
        statusView.put(LoadingView.STATUS_LOAD_FAILED, innerView);
//        preStatus = LoadingView.STATUS_LOAD_FAILED;
        innerView.setIRetryClickListener(iRetryClickListener);

    }

    public void showLoadFailed(LoadingView.IRetryClickListener iRetryClickListener) {
        checkNotNull();
        statusView.put(LoadingView.STATUS_LOAD_FAILED, innerView);
        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOAD_FAILED);
        innerView.setIRetryClickListener(iRetryClickListener);
    }

    public void showLoadSuccess() {

        showLoadSuccess(null);
    }

    public void showLoadSuccess(String msg) {
        checkNotNull();
        statusView.put(LoadingView.STATUS_LOAD_SUCCESS, innerView);
//        preStatus = LoadingView.STATUS_LOAD_SUCCESS;
        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOAD_SUCCESS);
        reset();
    }

    private void reset() {
        statusView.clear();
        isLoaded = false;
        innerView = null;
    }

    private void checkNotNull() {
        if (innerView == null)
            throw new NullPointerException("innerview must be not null");
    }

}
