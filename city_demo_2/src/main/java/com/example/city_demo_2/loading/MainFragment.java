package com.example.city_demo_2.loading;

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
import com.example.city_demo_2.R;
import com.example.noboloadinglayout.LoadingView;
import com.example.noboloadinglayout.NoBoLoadingManager;

import java.util.Locale;

import static com.example.city_demo_2.loading.Utils.getRandomImage;

public class MainFragment extends Fragment {

    private ImageView mImage;
    private View root;
    private NoBoLoadingManager noBoLoadingManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, null);

        Log.d(TAG, "onCreateView: " + root.getId());

        noBoLoadingManager = new NoBoLoadingManager(root);

        mImage = root.findViewById(R.id.image);

        initViews();

        return (View) root.getParent();
    }

    private static final String TAG = "MainFragment";

    private void initViews() {

        String randomImage = getRandomImage();

        noBoLoadingManager.showLoading();
        Log.d(TAG, "initViews:  开始执行任务" + randomImage);
        Glide.with(this)
                .load(randomImage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        noBoLoadingManager.showLoadFailed(new LoadingView.IRetryClickListener() {
                            @Override
                            public void retryClick() {
                                initViews();
                            }
                        });

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
