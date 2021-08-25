package com.example.city_demo_2.citylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseCityAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;

    protected HashMap<String, List<T>> hashMap = new LinkedHashMap<>();
    protected LayoutInflater mInflater;

    public BaseCityAdapter(Context mContext) {
        this.mContext = mContext;

        this.mInflater = LayoutInflater.from(mContext);
    }

    public BaseCityAdapter setDataMap(HashMap<String, List<T>> hashMap) {
        this.hashMap.clear();
        this.hashMap.putAll(hashMap);
        notifyDataSetChanged();
        return this;
    }

    public String getFirstWordByPos(int pos) {
        Iterator<Map.Entry<String, List<T>>> iterator = this.hashMap.entrySet().iterator();
        int index=0;
        while (iterator.hasNext()) {

            Map.Entry<String, List<T>> next = iterator.next();
            if (index == pos) {
                //返回 “A”
                return next.getKey();
            }
            index++;
        }
        return "";
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutRes = iBindItemSubList.getVHLayout();

        View inflate = mInflater.inflate(layoutRes, parent, false);

        return new ViewHolder(mContext,inflate);

    }

    private static final String TAG = "BaseCityAdapter";

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = 0;

        Iterator<Map.Entry<String, List<T>>> iterator = this.hashMap.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, List<T>> next = iterator.next();
            if (index == position) {
                String key = next.getKey();
                List<T> value = next.getValue();
                Log.d(TAG, "onBindViewHolder: " + key + "---" + value.size());

                if (iBindItemSubList!=null){
                    iBindItemSubList.bindData2View(holder,value);
                }
            }
            index++;
        }

    }


    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public int getPosByFirstWord(String word) {
        Iterator<Map.Entry<String, List<T>>> iterator = this.hashMap.entrySet().iterator();
        int index=0;
        while (iterator.hasNext()) {

            Map.Entry<String, List<T>> next = iterator.next();
            if (next.getKey() .equals( word)) {
                //返回 “A”
                return index;
            }
            index++;
        }

        return -1;
    }

    IBindItemSubList iBindItemSubList;

    public void setIBindItemSubList(IBindItemSubList iBindItemSubList) {
        this.iBindItemSubList = iBindItemSubList;
    }

    public interface IBindItemSubList<T>{
        /**
         *
          把数据绑定到viewholder的view 上
         */
        void bindData2View(ViewHolder holder,List<T>  cityBeans);

        //条目的布局文件
        int getVHLayout();
    }
}
