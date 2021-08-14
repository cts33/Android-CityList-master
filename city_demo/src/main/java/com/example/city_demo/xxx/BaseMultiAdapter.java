package com.example.city_demo.xxx;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseMultiAdapter extends RecyclerView.Adapter<BaseMultiAdapter.BaseViewHolder> {

    private static final Integer DEFAULT_TYPE = 0;
    //各种type 对应的  list
    protected HashMap<Integer, List<Object>> dataLists = new LinkedHashMap<>();

    //       type  xmlLayout
    HashMap<Integer, Integer> typeAndXmls = new LinkedHashMap<>();

    //自动累加 类型值
    int typeValue = -1;

    private Context context;

    public BaseMultiAdapter(Context context) {
        this.context = context;

    }

    /**
     * 按照顺序 添加类型和数据
     * 1--》list-1
     * 2--> list-2
     *
     * @param subList
     */
    public void addList(List<Object> subList, @LayoutRes int xmlLayout) {
        typeValue++;
        //代表类型为1，sublist为对应的数据
        dataLists.put(typeValue, subList);

        typeAndXmls.put(typeValue, xmlLayout);
        notifyDataSetChanged();
    }

    /**
     * 根据index 找出对应类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        int currIndex = 0;
        Set<Map.Entry<Integer, List<Object>>> entries = dataLists.entrySet();
        Iterator<Map.Entry<Integer, List<Object>>> iterator = entries.iterator();
        while (iterator.hasNext()) {

            Map.Entry<Integer, List<Object>> next = iterator.next();
            List<Object> value = next.getValue();
            Integer key = next.getKey();
            for (int i = 0; i < value.size(); i++) {

                if (currIndex == position) {

                    return key;
                }
                currIndex++;
            }

        }
        return -1;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO 查找布局 by type


        return createVH(viewType, parent);
    }

    protected BaseViewHolder createVH(int viewType, ViewGroup parent) {

        Integer layoutId = typeAndXmls.get(viewType);

        View root = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new BaseViewHolder(root);

    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        // Type  Bean(position)
        int offset;
        List<Object> value;
        Integer type;
        Set<Map.Entry<Integer, List<Object>>> entries = dataLists.entrySet();
        Iterator<Map.Entry<Integer, List<Object>>> iterator = entries.iterator();
        int index=0;
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Object>> next = iterator.next();
            type = next.getKey();
            value = next.getValue();
            offset = getOffset(type);

            for (int i = 0; i < value.size(); i++) {
                if (index==position){
                    // TODO  偏移量
                    attchDataByViewHolder(holder, type, value, position, offset);
                    return;
                }
                index++;
            }


        }


    }

    /**
     * 计算列表的偏移量
     *
     * @param type
     * @return
     */
    private int getOffset(Integer type) {
        if (type ==0)
            return 0;
        int offset = 0;
        //记录上一个类型的偏移量，所以 -1
        for (int i = 0; i <= type-1; i++) {
            List<Object> objects = dataLists.get(type-1);
            offset += objects.size();
        }
        return offset;
    }

    protected abstract void attchDataByViewHolder(BaseViewHolder holder, Integer type, List<Object> value, int position, int offset);


    @Override
    public int getItemCount() {
        int size = 0;
        Set<Map.Entry<Integer, List<Object>>> entries = dataLists.entrySet();
        Iterator<Map.Entry<Integer, List<Object>>> iterator = entries.iterator();
        while (iterator.hasNext()) {

            List<Object> value = iterator.next().getValue();
            size += value.size();

        }
        return size;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
