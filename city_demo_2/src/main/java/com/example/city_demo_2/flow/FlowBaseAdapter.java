package com.example.city_demo_2.flow;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;


public abstract class FlowBaseAdapter<T> {
    private  DataSetObservable mDataSetObservable = new DataSetObservable();


    public void unregisterDataSetObserver(DataSetObserver mDataSetObserver) {
        mDataSetObservable.unregisterObserver(mDataSetObserver);
    }

    public void registerDataSetObserver(DataSetObserver mDataSetObserver) {
        mDataSetObservable.registerObserver(mDataSetObserver);
    }


    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }



    public abstract int getCount();

    public abstract T getItem(int pos);

    public abstract View getView(FlowingLayout flowingLayout, int i, T item);



}
