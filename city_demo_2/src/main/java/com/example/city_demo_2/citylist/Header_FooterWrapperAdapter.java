package com.example.city_demo_2.citylist;


import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.city_demo_2.citylist.bean.CityBean;

public abstract class Header_FooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_TYPE = 11;

    protected RecyclerView.Adapter mInnerAdapter;//内部的的普通Adapter
    private int layoutId;

    private CityBean data;

    public Header_FooterWrapperAdapter(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    private int getInnerItemCount() {
        return mInnerAdapter != null ? mInnerAdapter.getItemCount() : 0;
    }

    /**
     * 传入position 判断是否是headerview
     *
     * @param position
     * @return
     */
    public boolean isHeaderView(int position) {
        return data != null && layoutId != 0 && position == 0;
    }

    /**
     * 添加HeaderView
     *
     * @param layoutId headerView 的LayoutId
     * @param data     headerView 的data
     */
    public void addHeaderView(int layoutId, CityBean data) {
        this.layoutId = layoutId;
        this.data = data;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return HEADER_TYPE;
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEADER_TYPE) {
            return ViewHolder.get(parent.getContext(), null, parent, layoutId, -1);
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    protected abstract void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, CityBean o);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {


            onBindHeaderHolder((ViewHolder) holder, position, layoutId, data);

        } else {
            mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        }
    }

    public int getHeaderViewCount() {

        return data != null && layoutId != 0 ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return getInnerItemCount() + getHeaderViewCount();
    }

    public String getFirstWordByPosition(int firstVisibleItemPosition) {
        String result = "";
        if (firstVisibleItemPosition == 0) {
            result = data.getFirstWord();
        }
        if (mInnerAdapter != null) {
           result = ((BaseCityAdapter) mInnerAdapter).getFirstWordByPos(firstVisibleItemPosition - getHeaderViewCount());
        }
        return result;
    }
}
