package com.example.noboloadinglayout;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NormalLoadingView extends  LoadingView{

    private LayoutInflater inflater;
    private Context context;
    private ImageView mImage;
    private IRetryClickListener iRetryClickListener;



    private TextView mDes;

    public NormalLoadingView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        context = getContext();
        inflater = LayoutInflater.from(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.view_global_loading_status, this, true);
        mImage = findViewById(R.id.image);
        mDes = findViewById(R.id.text);
        setBackgroundColor(getResources().getColor(R.color.main_bg_1));

    }

    public void setIRetryClickListener(IRetryClickListener iRetryClickListener) {
        this.iRetryClickListener = iRetryClickListener;
    }



    @Override
    public void onClick(View view) {
        if (iRetryClickListener!=null){
            iRetryClickListener.retryClick();
            iRetryClickListener =null;
        }
    }

    private static final String TAG = "NormalLoadingView";
    @Override
    public void setVisibleByStatus(int status) {
        Log.d(TAG, "setVisibleByStatus: "+status);
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
}
