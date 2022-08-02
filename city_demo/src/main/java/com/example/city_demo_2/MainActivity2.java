package com.example.city_demo_2;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.city_demo_2.citylist.ViewHolder;
import com.example.city_demo_2.citylist.bean.CityBean;
import com.example.city_demo_2.db.DBDao;
import com.example.city_demo_2.flow.CommonFlowAdapter;
import com.example.city_demo_2.flow.FlowingLayout;
import com.example.city_demo_2.view.CityListLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.city_demo_2.view.CityListLayout.SPECIAL_TYPE;

public class MainActivity2 extends AppCompatActivity {

    private CityListLayout mCitylistlayout;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initViews();

    }

    private void initViews() {
        DBDao dbDao = new DBDao(this);

        List<CityBean> allList = dbDao.getAllList();

        if (allList.isEmpty()){
            Log.d(TAG, "initViews: 请把数据库文件cities.db复制数据库目录里");
            return;
        }

        List<CityBean> sss = new ArrayList<>();
        sss.addAll(allList.subList(10, 20));


        CityBean cityBean = new CityBean();
        cityBean.setcName("北京");


        mCitylistlayout = findViewById(R.id.cityListLayout);

        mCitylistlayout.addCurrLocation(cityBean);
        mCitylistlayout.addCitySpecialData("热门", sss);
        mCitylistlayout.addCityList(allList,cityAdapter);


    }

    CityListLayout.CityAdapter cityAdapter =new CityListLayout.CityAdapter<CityBean>() {

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
        public int getGroupItemLayout() {
            return R.layout.recycler_item;
        }

        @Override
        public void bindGroupData2ItemView(ViewHolder holder, List<CityBean> cityBeans) {
                FlowingLayout view = holder.getView(R.id.flowingLayout);

                view.setAdapter(new CommonFlowAdapter<CityBean>(holder.getContext(), cityBeans) {
                    @Override
                    public void convert(FlowHolder holder, CityBean item, int position) {

                        holder.setText(item.getcName());
                    }

                    @Override
                    public CityBean getItem(int pos) {
                        return cityBeans.get(pos);
                    }
                });
        }

        @Override
        public void bindHeaderViewData(ViewHolder holder, CityBean cityBean) {
            holder.setText(R.id.location,cityBean.getcName());
            holder.getView(R.id.location).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public String getHeaderViewFirstWord(CityBean bean) {
            return bean.getFirstWord();
        }
    };
}