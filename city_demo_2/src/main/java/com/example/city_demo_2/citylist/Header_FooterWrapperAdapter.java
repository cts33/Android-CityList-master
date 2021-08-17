package com.example.city_demo_2.citylist;


import android.view.ViewGroup;

import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.city_demo_2.ViewHolder;
import com.example.city_demo_2.citylist.bean.CityBean;

import java.util.List;


public abstract class Header_FooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 11;


    private SparseArrayCompat<SparseArrayCompat> mHeaderDatas = new SparseArrayCompat<SparseArrayCompat>();

    protected RecyclerView.Adapter mInnerAdapter;//内部的的普通Adapter
    //热门城市
    private List<CityBean> hotBeanList;

    public Header_FooterWrapperAdapter(RecyclerView.Adapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    public int getHeaderViewCount() {
        return mHeaderDatas.size();
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
    public boolean isHeaderViewPos(int position) {// 举例， 2 个头，pos 0 1，true， 2+ false
        return getHeaderViewCount() > position;
    }


    /**
     * 添加HeaderView
     *
     * @param layoutId headerView 的LayoutId
     * @param data     headerView 的data(可能多种不同类型的header 只能用Object了)
     */
    public void addHeaderView(int layoutId, Object data) {
        //mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, v);
        SparseArrayCompat headerContainer = new SparseArrayCompat();
        headerContainer.put(layoutId, data);
        mHeaderDatas.put(mHeaderDatas.size() + BASE_ITEM_TYPE_HEADER, headerContainer);
    }

    /**
     * 添加头部布局和数据
     *
     * @param layoutId    布局id
     * @param currLoction 当前位置对象
     * @param hotBeanList 热门对象的集合
     */
    public void setHeaderDataAndView(int layoutId, CityBean currLoction, List<CityBean> hotBeanList) {

        this.hotBeanList = hotBeanList;
        boolean isFinded = false;
        for (int i = 0; i < mHeaderDatas.size(); i++) {
            SparseArrayCompat sparse = mHeaderDatas.valueAt(i);
            if (layoutId == sparse.keyAt(0)) {
                sparse.setValueAt(0, currLoction);
                isFinded = true;
            }
        }
        if (!isFinded) {//没发现 说明是addHeaderView
            addHeaderView(layoutId, currLoction);
        }
    }

    /**
     * 设置(更新)某个layoutId的HeaderView的数据
     *
     * @param layoutId
     * @param data
     */
    public void setHeaderView(int layoutId, Object data) {
        boolean isFinded = false;
        for (int i = 0; i < mHeaderDatas.size(); i++) {
            SparseArrayCompat sparse = mHeaderDatas.valueAt(i);
            if (layoutId == sparse.keyAt(0)) {
                sparse.setValueAt(0, data);
                isFinded = true;
            }
        }
        if (!isFinded) {//没发现 说明是addHeaderView
            addHeaderView(layoutId, data);
        }
    }


    /**
     * 设置某个位置的HeaderView
     *
     * @param headerPos 从0开始，如果pos过大 就是addHeaderview
     * @param layoutId
     * @param data
     */
    public void setHeaderView(int headerPos, int layoutId, Object data) {
        if (mHeaderDatas.size() > headerPos) {
            SparseArrayCompat headerContainer = new SparseArrayCompat();
            headerContainer.put(layoutId, data);
            mHeaderDatas.setValueAt(headerPos, headerContainer);
        } else if (mHeaderDatas.size() == headerPos) {//调用addHeaderView
            addHeaderView(layoutId, data);
        } else {
            //
            addHeaderView(layoutId, data);
        }
    }


    /**
     * 清空HeaderView数据
     */
    public void clearHeaderView() {
        mHeaderDatas.clear();
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return mHeaderDatas.keyAt(position);
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderDatas.get(viewType) != null) {

            int layoutid = mHeaderDatas.get(viewType).keyAt(0);

            return ViewHolder.get(parent.getContext(), null, parent, layoutid, -1);
        } else {

            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    protected abstract void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, CityBean o);
    //多回传一个layoutId出去，用于判断是第几个headerview

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            int layoutId = mHeaderDatas.get(getItemViewType(position)).keyAt(0);
            CityBean cityBean = (CityBean) mHeaderDatas.get(getItemViewType(position)).get(layoutId);

            onBindHeaderHolder((ViewHolder) holder, position, layoutId,cityBean );

        } else {
            mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        }
    }


    @Override
    public int getItemCount() {
        return getInnerItemCount() + getHeaderViewCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        //为了兼容GridLayout
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderDatas.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderViewPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }
}
