package com.example.noboloadinglayout;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NormalLoadingView extends LinearLayout  implements View.OnClickListener{

    private LayoutInflater inflater;
    private Context context;
    private ImageView mImage;
    private IRetryClickListener iRetryClickListener;

    public NormalLoadingView(Context context ) {
        super(context);

        initView();
    }

    private TextView mDes;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;

    private void initView() {
        context = getContext();
        inflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_global_loading_status, this, true);
        mImage = findViewById(R.id.image);
        mDes = findViewById(R.id.text);
        setBackgroundColor(getResources().getColor(R.color.main_bg_1));
//        this.mRetryTask = retryTask;

    }

    public void setiRetryClickListener(IRetryClickListener iRetryClickListener) {
        this.iRetryClickListener = iRetryClickListener;
    }

    public void setStatus(int status) {
        boolean show = true;
        View.OnClickListener onClickListener = null;
        int image = R.drawable.loading;
        int str = R.string.str_none;
        switch (status) {
            case STATUS_LOAD_SUCCESS: show = false; break;
            case STATUS_LOADING: str = R.string.loading; break;
            case STATUS_LOAD_FAILED:
                str = R.string.load_failed;
                image = R.drawable.icon_failed;

                onClickListener = this;
                break;
            case STATUS_EMPTY_DATA:
                str = R.string.empty;
                image = R.drawable.icon_empty;
                break;
            default: break;
        }
        mImage.setImageResource(image);
        setOnClickListener(onClickListener);
        mDes.setText(str);
        setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (iRetryClickListener!=null){
            iRetryClickListener.retryClick();
            iRetryClickListener =null;
        }
    }


//    @Override
//    public void loading() {
//
//    }
//
//    @Override
//    public void success() {
//
//    }
//
//    @Override
//    public void failed() {
//
//    }
//
//    @Override
//    public void retry() {
//
//    }
}
