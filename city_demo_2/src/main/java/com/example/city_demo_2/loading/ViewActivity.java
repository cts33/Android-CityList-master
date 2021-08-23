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

import static com.example.city_demo_2.loading.Utils.getRandomImage;


public class ViewActivity extends AppCompatActivity {




    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_activity);


        image = findViewById(R.id.image);


        initView();
    }

    private static final String TAG = "FragAndViewActivity";
    private void initView() {


        NoBoLoadingManager.wrapView(image).showLoading();
        Glide.with(this)
                .load( getRandomImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        NoBoLoadingManager.wrapView(image).showLoadFailed();

                        Log.d(TAG, "onLoadFailed: ");

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "onResourceReady: ");

                        NoBoLoadingManager.wrapView(image).showLoadSuccess();

                        return false;
                    }
                })
                .into(image);
    }
}