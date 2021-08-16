package com.example.city_demo_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.city_demo_2.bean.CityBean;
import com.example.city_demo_2.bean.SuspensionDecoration;
import com.example.city_demo_2.db.DBDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                holder.setText(R.id.location,   o.getcName());
//                holder.setImageResource(R.id.ivAvatar,R.drawable.friend);
            }
        };

        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");
        List<CityBean> hotCitys = new ArrayList<>();
        hotCitys.add(cityBean);
        mHeaderAdapter.setHeaderDataAndView(R.layout.item_city,cityBean,hotCitys);
//        mHeaderAdapter.setHeaderView(R.layout.item_city, cityBean);

        recyclerView.setAdapter(mHeaderAdapter);

        mDecoration = new SuspensionDecoration(this, mDatas);

        mDecoration.setHeaderViewCount(mHeaderAdapter.getHeaderViewCount());

        recyclerView.addItemDecoration(mDecoration );
        //如果add两个，那么按照先后顺序，依次渲染。
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        initDatas( );
    }


    private void initDatas( ) {

        List<CityBean> allList = dbDao.getAllList();

        Collections.sort(allList, new Comparator<CityBean>() {
            @Override
            public int compare(CityBean o1, CityBean o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });

        mAdapter.setDatas(allList);

        mHeaderAdapter.notifyDataSetChanged();
        mDecoration.setmDatas(allList);
    }
}