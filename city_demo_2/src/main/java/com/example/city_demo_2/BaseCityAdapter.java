package com.example.city_demo_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.city_demo_2.bean.CityBean;

import java.util.List;

public class BaseCityAdapter extends RecyclerView.Adapter<BaseCityAdapter.ViewHolder> {

    protected Context mContext;
    protected List<CityBean> mDatas;
    protected LayoutInflater mInflater;

    public BaseCityAdapter(Context mContext, List<CityBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public BaseCityAdapter setDatas(List<CityBean> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public BaseCityAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull  BaseCityAdapter.ViewHolder holder, int position) {
        final CityBean cityBean = mDatas.get(position);
        holder.tvCity.setText(cityBean.getcName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT).show();
            }
        });
//        holder.avatar.setImageResource(R.drawable.);
    }


    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;


        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.city_name);

        }
    }
}
