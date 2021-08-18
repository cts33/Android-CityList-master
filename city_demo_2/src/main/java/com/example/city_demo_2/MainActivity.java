package com.example.city_demo_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.city_demo_2.citylist.BaseCityAdapter;
import com.example.city_demo_2.citylist.Header_FooterWrapperAdapter;
import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.citylist.bean.SuspensionDecoration;
import com.example.city_demo_2.db.DBDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseCityAdapter mAdapter;
    private Header_FooterWrapperAdapter mHeaderAdapter;
    private List<CityBean> mDatas;
    private SuspensionDecoration mDecoration;
    private DBDao dbDao;
    private HashMap<String, List<CityBean>> hashMap;
    private LetterListView letterListView;
    private boolean isScroll;
    private int mTitleHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());


        dbDao = new DBDao(this);


        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        letterListView = (LetterListView) findViewById(R.id.letter_list);


        mAdapter = new BaseCityAdapter(this);

        mHeaderAdapter = new Header_FooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(com.example.city_demo_2.ViewHolder holder, int headerPos, int layoutId, CityBean o) {
                holder.setText(R.id.location, o.getcName());
            }
        };

        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");

        mHeaderAdapter.addHeaderView(R.layout.item_city, cityBean);

        recyclerView.setAdapter(mHeaderAdapter);

        mDecoration = new SuspensionDecoration(this, hashMap);

        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());

        recyclerView.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));


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

                String word = mHeaderAdapter.getFirstWordByPosition(firstVisibleItemPosition);
                Log.d(TAG, "onScrolled: first=" + firstVisibleItemPosition + " word=" + word);

                //TODO 处理多次任务为一次任务
                letterListView.updateSelectIndex(word);

            }
        });
        letterListView.setOnTouchingLetterChangedListener(new LetterListView.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String word) {


                int pos = mAdapter.getPosByFirstWord(word);
                //如果有头部，
                if (mHeaderAdapter.getHeaderViewCount() > 0) {
                    pos++;
                }


                Log.d(TAG, "onTouchingLetterChanged: " + pos);
                smoothMoveToPosition(recyclerView, pos);


            }
        });

        initDatas();
    }

    private boolean isLeterClick;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {

        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));

        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
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


    private void initDatas() {


        List<CityBean> allList = dbDao.getAllList();

        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");
        cityBean.setPinyin("");
        cityBean.setFirstWord("热门");
        allList.add(cityBean);

        Collections.sort(allList);

        hashMap = new LinkedHashMap<>();
        List<CityBean> subArray = null;
        String currLetter = "";
        for (int i = 0; i < allList.size(); i++) {

            CityBean cc = allList.get(i);
            String pp = cc.getFirstWord();


            //上次字母和本次不一样，证明新数据
            if (!currLetter.equals(pp)) {
                currLetter = pp;
                subArray = new ArrayList<>();
                subArray.add(cc);
                hashMap.put(pp, subArray);
            } else {
                subArray.add(cc);
            }
        }


        mAdapter.setDataMap(hashMap);
        mHeaderAdapter.notifyDataSetChanged();
        mDecoration.setmDatas(hashMap);

        TreeSet<String> letterList = new TreeSet<>();
        for (int i = 0; i < allList.size(); i++) {
            CityBean cityBean1 = allList.get(i);
            if (cityBean1.getFirstWord().contains("热门")) {
                continue;
            }
            letterList.add(cityBean1.getFirstWord());
        }
        ArrayList<String> strings = new ArrayList<>(letterList);
        strings.add(0, "热门");
        letterListView.setLetters(strings);


    }

    private static final String TAG = "MainActivity";
}