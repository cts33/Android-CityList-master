package com.example.noboloadinglayout;

public interface ILoadingView {
    void loading();
    void success();
    void failed();
    void retry();
}
