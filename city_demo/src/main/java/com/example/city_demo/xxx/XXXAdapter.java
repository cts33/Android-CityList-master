package com.example.city_demo.xxx;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.city_demo.City;
import com.example.city_demo.R;

import java.util.List;

public class XXXAdapter extends BaseMultiAdapter {

    public XXXAdapter(Context context) {
        super(context);
    }

    @Override
    protected void attchDataByViewHolder(BaseViewHolder holder, Integer type, List<Object> value, int position,int offset) {

        TextView viewById = (TextView) holder.itemView.findViewById(R.id.city);
        City city = (City) value.get(position-offset);
        viewById.setText(city.getName());
    }
}
