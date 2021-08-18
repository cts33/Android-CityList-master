package com.example.city_demo_2.citylist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.city_demo_2.R;
import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.flow.CommonFlowAdapter;
import com.example.city_demo_2.flow.FlowingLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseCityAdapter extends RecyclerView.Adapter<BaseCityAdapter.ViewHolder> {

    protected Context mContext;
    //    protected List<CityBean> mDatas = new ArrayList<>();
    protected HashMap<String, List<CityBean>> hashMap = new LinkedHashMap<>();
    protected LayoutInflater mInflater;

    public BaseCityAdapter(Context mContext) {
        this.mContext = mContext;

        this.mInflater = LayoutInflater.from(mContext);
    }

    public BaseCityAdapter setDataMap(HashMap<String, List<CityBean>> hashMap) {
        this.hashMap.clear();
        this.hashMap.putAll(hashMap);
        notifyDataSetChanged();
        return this;
    }

    public String getFirstWordByPos(int pos) {
        Iterator<Map.Entry<String, List<CityBean>>> iterator = this.hashMap.entrySet().iterator();
        int index=0;
        while (iterator.hasNext()) {

            Map.Entry<String, List<CityBean>> next = iterator.next();
            if (index == pos) {
                //返回 “A”
                return next.getKey();
            }
            index++;
        }
        return "";
    }

//    public BaseCityAdapter setDatas(List<CityBean> datas) {
//        this.mDatas.clear();
//        mDatas.addAll(datas);
//        return this;
//    }

    @Override
    public BaseCityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(inflate);
    }

    private static final String TAG = "BaseCityAdapter";

    @Override
    public void onBindViewHolder(@NonNull BaseCityAdapter.ViewHolder holder, int position) {
        int index = 0;

        Iterator<Map.Entry<String, List<CityBean>>> iterator = this.hashMap.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, List<CityBean>> next = iterator.next();
            if (index == position) {
                String key = next.getKey();
                List<CityBean> value = next.getValue();
                Log.d(TAG, "onBindViewHolder: " + key + "---" + value.size());
//
                holder.flowingLayout.setAdapter(new CommonFlowAdapter<CityBean>(mContext, value) {
                    @Override
                    public void convert(FlowHolder holder, CityBean item, int position) {
                        holder.setText(item.getcName());
                    }

                    @Override
                    public CityBean getItem(int pos) {
                        return value.get(pos);
                    }
                });

                holder.flowingLayout.setOnItemClick(new FlowingLayout.OnItemTagListener<CityBean>() {
                    @Override
                    public void selectItem(CityBean o) {
                        Toast.makeText(mContext, "" + o.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
            index++;
        }
//        holder.avatar.setImageResource(R.drawable.);
    }


    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public int getPosByFirstWord(String word) {
        Iterator<Map.Entry<String, List<CityBean>>> iterator = this.hashMap.entrySet().iterator();
        int index=0;
        while (iterator.hasNext()) {

            Map.Entry<String, List<CityBean>> next = iterator.next();
            if (next.getKey() .equals( word)) {
                //返回 “A”
                return index;
            }
            index++;
        }

        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FlowingLayout flowingLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            flowingLayout = (FlowingLayout) itemView.findViewById(R.id.flowingLayout);

        }
    }
}
