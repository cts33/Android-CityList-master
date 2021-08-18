package com.example.city_demo_2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.db.DBDao;
import com.example.city_demo_2.view.CityListLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private CityListLayout mCitylistlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initViews();

        initData();
    }

    private void initData() {
        DBDao dbDao = new DBDao(this);

        List<CityBean> allList = dbDao.getAllList();


        List<CityBean> sss = new ArrayList<>();
        sss.addAll(allList.subList(10, 20));


        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");


        mCitylistlayout.addCurrLocation(cityBean,R.layout.item_city);
        mCitylistlayout.addCitySpecialData("热门", sss);
        mCitylistlayout.addCityList(allList);
    }

    private void initViews() {
        mCitylistlayout = findViewById(R.id.cityListLayout);

    }
}