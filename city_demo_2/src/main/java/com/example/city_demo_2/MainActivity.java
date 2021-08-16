package com.example.city_demo_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.example.city_demo_2.bean.CityBean;
import com.example.city_demo_2.bean.SuspensionDecoration;
import com.example.city_demo_2.db.DBDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BaseCityAdapter mAdapter;
    private Header_FooterWrapperAdapter mHeaderAdapter;
    private List<CityBean> mDatas;
    private SuspensionDecoration mDecoration;
    private DBDao dbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dbDao = new DBDao(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new BaseCityAdapter(this);

        mHeaderAdapter = new Header_FooterWrapperAdapter(mAdapter) {
            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, CityBean o) {
                holder.setText(R.id.location, o.getcName());
//                holder.setImageResource(R.id.ivAvatar,R.drawable.friend);
            }
        };

        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");
        List<CityBean> hotCitys = new ArrayList<>();
        hotCitys.add(cityBean);
        mHeaderAdapter.setHeaderDataAndView(R.layout.item_city, cityBean, hotCitys);
//        mHeaderAdapter.setHeaderView(R.layout.item_city, cityBean);

        recyclerView.setAdapter(mHeaderAdapter);

        mDecoration = new SuspensionDecoration(this, mDatas);

        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());

        recyclerView.addItemDecoration(mDecoration);
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        initDatas();
    }


    private void initDatas() {

        long l = System.currentTimeMillis();
        List<CityBean> allList = dbDao.getAllList();

        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");
        cityBean.setPinyin("");
        cityBean.setFirstWord("热门");
        allList.add(cityBean);

        Collections.sort(allList);

        HashMap<String,List<CityBean>> hashMap = new LinkedHashMap<>();
        List<CityBean>  subArray =null ;
        String currLetter="";
        for (int i = 0; i < allList.size(); i++) {

            CityBean cc = allList.get(i);
            String pp = cc.getFirstWord();


            //上次字母和本次不一样，证明新数据
            if(!currLetter.equals(pp)){
                currLetter = pp;
                subArray = new ArrayList<>();
                subArray.add(cc);
                hashMap.put(pp,subArray);
            }else{
                subArray.add(cc);
            }
        }

//        mAdapter.setDatas(allList);
        mAdapter.setDataMap(hashMap);

        mHeaderAdapter.notifyDataSetChanged();
        mDecoration.setmDatas(allList);
        Log.d(TAG, "initDatas: " + ((System.currentTimeMillis() - l)));
    }

    private static final String TAG = "MainActivity";
}