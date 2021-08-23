package com.example.noboloadinglayout;

import android.view.View;

public interface ILoadingView  extends View.OnClickListener{
    interface IRetryClickListener {
        void retryClick();
    }

    void setVisibleByStatus(int status);

    void setIRetryClickListener(IRetryClickListener iRetryClickListener);



}

