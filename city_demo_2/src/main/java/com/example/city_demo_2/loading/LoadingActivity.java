package com.example.city_demo_2.loading;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.city_demo_2.R;
import com.example.noboloadinglayout.NoBoLoadingManager;

import java.util.Locale;

import static com.example.city_demo_2.loading.Utils.getRandomImage;

public class LoadingActivity extends AppCompatActivity {

    private ImageView mImage;
    private NoBoLoadingManager noBoLoadingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        noBoLoadingManager = NoBoLoadingManager.wrapActivity(this);

        initViews();


    }

    private static final String TAG = "LoadingActivity";

    private void initViews() {
        mImage = findViewById(R.id.image);

        noBoLoadingManager.showLoading();

        Glide.with(this)
                .load(getRandomImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        noBoLoadingManager.showLoadFailed();

                        Log.d(TAG, "onLoadFailed: ");

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");

                        noBoLoadingManager.showLoadSuccess();

                        return false;
                    }
                })
                .into(mImage);

    }
}