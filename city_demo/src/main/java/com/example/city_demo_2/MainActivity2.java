package com.example.city_demo_2;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.db.DBDao;
import com.example.city_demo_2.view.CityListLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private CityListLayout mCitylistlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();

    }

    private void initViews() {
        DBDao dbDao = new DBDao(this);

        List<CityBean> allList = dbDao.getAllList();


        List<CityBean> sss = new ArrayList<>();
        sss.addAll(allList.subList(10, 20));


        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");


        mCitylistlayout = findViewById(R.id.cityListLayout);

        mCitylistlayout.setICongfig(iCongfig);

        mCitylistlayout.addCurrLocation(cityBean);
        mCitylistlayout.addCitySpecialData("热门", sss);
        mCitylistlayout.addCityList(allList);

//        mCitylistlayout.setItemClickListener(new CityListLayout.ItemClickListener() {
//            @Override
//            public void headerViewClick(CityBean cityBean) {
//                Toast.makeText(MainActivity2.this, "" + cityBean.toString(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void flowItemClick(CityBean cityBean) {
//
//                Toast.makeText(MainActivity2.this, "" + cityBean.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    CityListLayout.ICongfig iCongfig =new CityListLayout.ICongfig<CityBean>() {
        @Override
        public CityBean getBeanByPos(int pos) {
            return null;
        }

        @Override
        public void checkInputListAndSort(List<CityBean> cityBeanListList) {

            if (cityBeanListList == null)
                throw new NullPointerException("传入数据不能为 null");

            for (int i = 0; i < cityBeanListList.size(); i++) {
                CityBean cityBean = cityBeanListList.get(i);
                if (TextUtils.isEmpty(cityBean.getcName())) {
                    throw new NullPointerException("name 数据不能为 null");
                }
                //name  转化为拼音
                if (TextUtils.isEmpty(cityBean.getPinyin())) {

                    String pinyin = PingYinUtil.getPingYin(cityBean.getcName());
                    cityBean.setPinyin(pinyin);

                    if (!TextUtils.isEmpty(pinyin)) {
                        String shou = cityBean.getPinyin().substring(0, 1).toUpperCase();

                        cityBean.setFirstWord(shou);
                    }
                }


            }

            Collections.sort(cityBeanListList);



        }

        @Override
        public LinkedHashMap<String, List> convertList2Map(List cityBeanListList) {

            LinkedHashMap<String, List> hashMap = new LinkedHashMap<>();

            List<CityBean> subArray = null;
            String currLetter = "";
            for (int i = 0; i < cityBeanListList.size(); i++) {

                CityBean cc = (CityBean) cityBeanListList.get(i);
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

            return hashMap;
        }

        @Override
        public List<String> filterSpecialLetter(LinkedHashMap<String, List> hashMap) {


            List<String> subArray = new ArrayList<>();
            Iterator<Map.Entry<String, List>> iterator = hashMap.entrySet().iterator();

            while (iterator.hasNext()) {
                String key = iterator.next().getKey();
                key = key.equals("0") ? "热门" : key;
                subArray.add(key);
            }
            return subArray;
        }
    };
}