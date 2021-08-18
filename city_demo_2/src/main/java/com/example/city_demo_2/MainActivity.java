package com.example.city_demo_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.city_demo_2.citylist.Header_FooterWrapperAdapter;
import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.citylist.bean.SuspensionDecoration;
import com.example.city_demo_2.db.DBDao;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.city_demo_2.BaseCityAdapter mAdapter;
    private  Header_FooterWrapperAdapter mHeaderAdapter;
    private List<CityBean> mDatas;
    private SuspensionDecoration mDecoration;
    private DBDao dbDao;
    private HashMap<String, List<CityBean>> hashMap;
    private LetterListView letterListView;
    private boolean isScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        dbDao = new DBDao(this);


        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        letterListView = (LetterListView) findViewById(R.id.letter_list);


        mAdapter = new com.example.city_demo_2.BaseCityAdapter(this);

        mHeaderAdapter = new  Header_FooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(com.example.city_demo_2.ViewHolder holder, int headerPos, int layoutId, CityBean o) {
                holder.setText(R.id.location, o.getcName());
//                holder.setImageResource(R.id.ivAvatar,R.drawable.friend);
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
            public void onScrollStateChanged( RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
                if (scrollState == RecyclerView.SCROLL_STATE_DRAGGING||scrollState==RecyclerView.SCROLL_STATE_SETTLING ) {
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled( RecyclerView recy, int dx, int dy) {
                super.onScrolled(recy, dx, dy);
                if (!isScroll)
                    return;


                // TODO 获取第一个可见的首字母，便于更新字母列表
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recy.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                String word = mHeaderAdapter.getFirstWordByPosition(firstVisibleItemPosition);
                Log.d(TAG, "onScrolled: first="+firstVisibleItemPosition+" word="+word);

                //TODO 处理多次任务为一次任务
                letterListView.updateSelectIndex(word);

            }
        });
        letterListView.setOnTouchingLetterChangedListener(new LetterListView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {

                int base=65;
                char index = s.toCharArray()[0];

                View childAt = recyclerView.getFocusedChild();
//                int top = childAt.getTop();
//                Log.d(TAG, "onTouchingLetterChanged: "+top);
//                recyclerView.scrollBy(0,-top);
//                if (index>='A'||index<='Z')
//                    recyclerView.smoothScrollToPosition(index-base+1);

//
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                layoutManager.

            }
        });

        initDatas();
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
            letterList.add( cityBean1.getFirstWord());
        }
        ArrayList<String> strings = new ArrayList<>(letterList);
        strings.add(0,"热门");
        letterListView.setLetters(strings);



    }

    private static final String TAG = "MainActivity";
}