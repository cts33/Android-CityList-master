package com.example.city_demo_2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.noboloadinglayout.LoadingView;
import com.example.noboloadinglayout.NoBoLoadingManager;

import java.util.Locale;

public class MainFragment extends Fragment {

    private ImageView mImage;
    private View root;
    private NoBoLoadingManager noBoLoadingManager;

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, null);
        noBoLoadingManager = NoBoLoadingManager.wrapFragment(this, root);
        initViews();
        return noBoLoadingManager.getWrapper();
    }


    private void initViews() {
        mImage = root.findViewById(R.id.image);


        noBoLoadingManager.showLoading();


        Glide.with(this)
                .load(getRandomImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        noBoLoadingManager   .showLoadFailed()
                                .showRetry(new LoadingView.IRetryClickListener() {
                                    @Override
                                    public void retryClick() {
                                        initViews();
                                    }
                                });
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        noBoLoadingManager.showLoadSuccess();


                        return false;
                    }
                })
                .into(mImage);
    }

    public static String getRandomImage() {
        int id = (int) (Math.random() * 100000);

//        int id = -1;
        return String.format(Locale.CHINA, "https://www.thiswaifudoesnotexist.net/example-%d.jpg", id);
    }
}
