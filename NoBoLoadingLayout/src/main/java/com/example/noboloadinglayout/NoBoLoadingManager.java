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

import java.util.HashMap;

public class NoBoLoadingManager {


    private static final String TAG = "NoBoLoadingManager";
    private Context context;
    private static ViewGroup wrapper;

    private LoadingView innerView;
    private SparseArray<View> statusView = new SparseArray<>(4);


    public NoBoLoadingManager(FragmentActivity activity) {
        this.context = activity;
        wrapper = activity.findViewById(android.R.id.content);
    }
    public NoBoLoadingManager(View rootView) {
        this.context = rootView.getContext();
        wrapView(rootView);
    }


    public void wrapView(View rootView) {
        if (rootView == null)
            throw new NullPointerException("rootview is null");

        wrapper = new FrameLayout(context);
        ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();

        if (layoutParams != null) {
            wrapper.setLayoutParams(layoutParams);
        }

        if (rootView.getParent() != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            int index = parent.indexOfChild(rootView);
            parent.removeView(rootView);
            parent.addView(wrapper, index);
        }
        FrameLayout.LayoutParams newLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        wrapper.addView(rootView, newLp);


    }


    public void showLoading() {
        showLoading(null);
    }


    /**
     * 执行loading
     */
    public void showLoading(LoadingView inputView) {


            // 复用view
            innerView = (LoadingView) statusView.get(LoadingView.STATUS_LOADING);

            innerView= innerView == null ? (LoadingView) statusView.get(LoadingView.STATUS_LOAD_FAILED) : innerView;

            //缓存里没有，取外部
            if (innerView==null){

                if (inputView!=null){

                    innerView = inputView;

                    ViewGroup parent;
                    if ((parent = (ViewGroup) innerView.getParent()) != null) {
                        parent.removeView(innerView);
                    }
                    statusView.put(LoadingView.STATUS_LOADING, innerView);

                    wrapper.addView(innerView);
                }else{
                    innerView = new NormalLoadingView(context);
                    statusView.put(LoadingView.STATUS_LOADING, innerView);

                    wrapper.addView(innerView);

                }

            } else{

                ViewGroup parent;
                if ((parent = (ViewGroup) innerView.getParent()) != null) {
                    parent.removeView(innerView);
                }

                wrapper.addView(innerView);
            }

        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOADING);
    }

    public void showLoadFailed() {

        showLoadFailed(null);

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
        innerView.setVisibleByStatus(NormalLoadingView.STATUS_LOAD_SUCCESS);
    }


    private void checkNotNull() {
        if (innerView == null)
            throw new NullPointerException("innerview must be not null");
    }

    /**
     * 回收资源
     */
    public void clear(){
        if (this.context!=null)
            this.context =null;

        statusView.clear();
    }

}
