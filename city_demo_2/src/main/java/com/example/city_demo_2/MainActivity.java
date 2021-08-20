package com.example.city_demo_2;

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
import com.example.noboloadinglayout.NoBoLoadingManager;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();


    }

    private static final String TAG = "MainActivity";

    private void initViews() {
        mImage = findViewById(R.id.image);


        NoBoLoadingManager.wrapActivity(this).loading();

        Glide.with(this)
                .load(getRandomImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        NoBoLoadingManager.wrapActivity(MainActivity.this).showLoadFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        NoBoLoadingManager.wrapActivity(MainActivity.this).showLoadSuccess();
                        return false;
                    }
                })
                .into(mImage);
    }

    public static String getRandomImage() {
        int id = (int) (Math.random() * 100000);
        return String.format(Locale.CHINA, "https://www.thiswaifudoesnotexist.net/example-%d.jpg", id);
    }
}