package com.example.city_demo_2.view;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.city_demo_2.R;
import com.example.city_demo_2.citylist.BaseCityAdapter;
import com.example.city_demo_2.citylist.Header_FooterWrapperAdapter;
import com.example.city_demo_2.citylist.ViewHolder;
import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.citylist.bean.SuspensionDecoration;
import com.example.city_demo_2.flow.CommonFlowAdapter;
import com.example.city_demo_2.flow.FlowingLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CityListLayout extends LinearLayout {

    private View root;
    private RecyclerView recyclerView;
    private LetterListView letterListView;
    private SuspensionDecoration mDecoration;
    private Header_FooterWrapperAdapter mHeaderAdapter;
    private BaseCityAdapter mAdapter;
    private boolean isScroll;
    private int mTitleHeight;
    private List<String> letterFirstWordList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CityListLayout(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CityListLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CityListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CityListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initConfig();

        initView();
    }

    private void initConfig() {
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());

        setOrientation(HORIZONTAL);

        root = LayoutInflater.from(getContext()).inflate(R.layout.city_list_layout, null, false);

        addView(root);


    }

    private LinkedHashMap<String, List> hashMap = new LinkedHashMap<>();
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    private void initView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        letterListView = (LetterListView) root.findViewById(R.id.letter_list);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.RIGHT;
        letterListView.setLayoutParams(layoutParams);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
                if (scrollState == RecyclerView.SCROLL_STATE_DRAGGING || scrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isScroll = true;
                }

                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == scrollState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recy, int dx, int dy) {
                super.onScrolled(recy, dx, dy);
                if (!isScroll)
                    return;


                // TODO 获取第一个可见的首字母，便于更新字母列表
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recy.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                String word = "";
                if (mHeaderAdapter != null) {
                    word = mHeaderAdapter.getFirstWordByPosition(firstVisibleItemPosition);

                } else {
                    word = mAdapter.getFirstWordByPos(firstVisibleItemPosition);
                }
                if (TextUtils.isEmpty(word))
                    return;

                //TODO 处理多次任务为一次任务
                letterListView.updateSelectIndex(word);

            }
        });
        letterListView.setOnTouchingLetterChangedListener(new LetterListView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String word) {


                int pos = mAdapter.getPosByFirstWord(word);
                //如果有头部，pos +1
                if (mHeaderAdapter != null && mHeaderAdapter.getHeaderViewCount() > 0) {
                    pos++;
                }

                smoothMoveToPosition(recyclerView, pos);

            }
        });
    }


    public static final String SPECIAL_TYPE = "0";

    public <T> void addCurrLocation(T currCityBean) {

        if (mAdapter == null) {
            //处理普通数据的适配器
            mAdapter = new BaseCityAdapter(getContext());
        }
        //处理头部数据的适配器，eg: 当前位置
        mHeaderAdapter = new Header_FooterWrapperAdapter<T>(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, T t) {

                if (cityAdapter != null) {
                    cityAdapter.bindHeaderViewData(holder, t);
                }

            }

            @Override
            protected String getFWBean(T bean) {
                if (cityAdapter != null) {
                    return cityAdapter.getHeaderViewFirstWord(bean);
                }
                return "";
            }

        };

        mHeaderAdapter.addHeaderViewData(R.layout.item_current_location, currCityBean);


    }



    private CityAdapter cityAdapter;


    public interface CityAdapter<T> {

        /**
         * 检查内部元素是否有首字母这个字段firstword，如果没有，把城市名称
         * 转成拼音，再转成首字母大写,最后记得按照字母排序
         **/
        void checkInputListAndSort(List<T> list);

        /**
         * 把原来list集合数据，根据首字母分类成各个字母的集合
         **/
        LinkedHashMap<String, List<T>> convertList2Map(List<T> list);


        /**
         * 获取每个字母组 对应的layout
         *
         * @return
         */
        int getGroupItemLayout();

        /**
         * 绑定字母组的数据 给每个组view,eg:A对应的集合数据给view,如何显示，自己定义
         *
         * @param holder
         * @param cityBeans
         */
        void bindGroupData2ItemView(ViewHolder holder, List<T> cityBeans);

        /**
         * 给头布局绑定 数据
         * @param holder
         * @param t
         */
        void bindHeaderViewData(ViewHolder holder, T t);

        /**
         * 获取头部布局对应的首字母
         * @param bean
         * @return
         */
        String getHeaderViewFirstWord(T bean);
    }


    /**
     * key  代表特殊类型，如：热门城市，历史
     * 0--热门
     *
     * @param specialBeanListList
     */
    public void addCitySpecialData(String key, List specialBeanListList) {
        if (!TextUtils.isEmpty(key) && key.equals("热门")) {
            //0小于字母的值，所以会排序到前面
            hashMap.put(SPECIAL_TYPE, specialBeanListList);
        }
    }

    /**
     * 添加普通数据，把集合数据按照字母分类存入到map
     * 注意Bean内部的FirstWord，代表首字母---添加必须是英文字母的数据
     * 如果要添加 热门，最热等城市数据，另有其他方法。
     * 城市bean，必须要有name字段
     *
     * @param cityBeanListList
     */
    public <T> void addCityList(List<T> cityBeanListList, CityAdapter cityAdapter) {
        this.cityAdapter = cityAdapter;
        this.cityAdapter.checkInputListAndSort(cityBeanListList);


        LinkedHashMap linkedHashMap = this.cityAdapter.convertList2Map(cityBeanListList);

        hashMap.putAll(linkedHashMap);

        letterFirstWordList = new ArrayList<>();
        Iterator<Map.Entry<String, List>> iterator = hashMap.entrySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().getKey();
            key = key.equals(SPECIAL_TYPE) ? "热门" : key;
            letterFirstWordList.add(key);
        }

        letterListView.setLetters(letterFirstWordList);

        setData2ViewLayout();
    }

    /**
     * 数据已经准备好，准备给view 赋值了
     */
    private void setData2ViewLayout() {

        if (recyclerView == null) {
            throw new NullPointerException("recyclerview is null!");
        }
        if (mAdapter == null) {
            mAdapter = new BaseCityAdapter(getContext());
        }
        mAdapter.setDataMap(hashMap);

        addItemDecoration();
        //如果没有头部适配器，说明没有添加头部类型
        if (mHeaderAdapter == null) {
            recyclerView.setAdapter(mAdapter);
        } else {
            recyclerView.setAdapter(mHeaderAdapter);
        }

        mAdapter.setIBindItemSubList(new BaseCityAdapter.IBindItemSubList<CityBean>() {
            @Override
            public void bindData2View(ViewHolder holder, List<CityBean> cityBeans) {
                if (cityAdapter != null) {
                    cityAdapter.bindGroupData2ItemView(holder, cityBeans);
                }
            }

            @Override
            public int getVHLayout() {
                if (cityAdapter != null) {
                    return cityAdapter.getGroupItemLayout();
                }
                return 0;
            }
        });

    }

    private static final String TAG = "CityListLayout";

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {

        if (position < 0)
            return;
        //第一个可见的view
        View firstVisibleView = mRecyclerView.getChildAt(0);
        //第一个可见的条目
        int firstItem = mRecyclerView.getChildLayoutPosition(firstVisibleView);

        //可见的总条目数量
        int visibleItemCount = mRecyclerView.getChildCount();
        //最后一个可见的条目
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(visibleItemCount - 1));
        Log.d(TAG, "smoothMoveToPosition: " + position);
        if (position < firstItem) {
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {

            int movePosition = position - firstItem;
            //计算选中的item,距离顶部的偏移量
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();

                mRecyclerView.smoothScrollBy(0, top - mTitleHeight);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    /**
     * 添加数据玩，调用
     */

    private void addItemDecoration() {
        //添加分割线
        mDecoration = new SuspensionDecoration(getContext(), letterFirstWordList);

        if (mHeaderAdapter != null) {

            mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());
        }

        recyclerView.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

    }


}
