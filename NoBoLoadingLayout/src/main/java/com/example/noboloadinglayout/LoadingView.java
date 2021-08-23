package com.example.noboloadinglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 如果需要自定义loading组件，需要实现该接口
 */
public abstract class LoadingView extends LinearLayout implements View.OnClickListener {
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;


    public LoadingView(Context context) {
        super(context);
    }


   public interface IRetryClickListener {
        void retryClick();
    }

    protected abstract void setVisibleByStatus(int status);

    protected abstract void setIRetryClickListener(IRetryClickListener iRetryClickListener);


}

