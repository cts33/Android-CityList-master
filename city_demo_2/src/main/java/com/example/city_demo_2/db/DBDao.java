package com.example.city_demo_2.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.city_demo_2.bean.CityBean;

import java.util.ArrayList;
import java.util.List;

public class DBDao {

    private final MyOpenHelper myOpenHelper;

    public DBDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "cities.db", null, 3);
    }

    public   List<CityBean> getAllList(){

        List<CityBean> cityBeanList = new ArrayList<>();
        SQLiteDatabase writableDatabase = myOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("city", null, null, null, null, null, null);
        while (cursor.moveToNext()){

            CityBean cityBean = new CityBean();

            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            String name  = cursor.getString(cursor.getColumnIndex("name"));
            String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));

            cityBean.setcName(name);
            cityBean.setPinyin(pinyin);
           String shou=  pinyin.toUpperCase().toCharArray()[0]+"";
            Log.d("TAG", "getAllList: "+shou);
            cityBean.setFirstWord(shou);
            cityBeanList.add(cityBean);

        }

        return cityBeanList;
    }
}
