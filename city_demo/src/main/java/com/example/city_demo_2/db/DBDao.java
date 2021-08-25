package com.example.city_demo_2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.city_demo_2.citylist.bean.CityBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DBDao {

    private final MyOpenHelper myOpenHelper;
    private Context context;

    public DBDao(Context context) {
        myOpenHelper = new MyOpenHelper(context, "cities.db", null, 3);

        this.context = context;
    }

    public List<CityBean> getAllList() {

            List<CityBean> cityBeanList = new ArrayList<>();
        try {
            SQLiteDatabase writableDatabase = myOpenHelper.getWritableDatabase();


            Cursor cursor = writableDatabase.query("city", null, null, null, null, null, null);
            while (cursor.moveToNext()) {

                CityBean cityBean = new CityBean();

                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));

                cityBean.setcName(name);
                cityBean.setPinyin(pinyin);
                if (!TextUtils.isEmpty(pinyin)) {
                    String shou = pinyin.substring(0, 1).toUpperCase();
                    Log.d("TAG", "getAllList: " + shou);

                    cityBean.setFirstWord(shou);
                }

                cityBeanList.add(cityBean);

            }
            if (!cityBeanList.isEmpty()){

//                cityBeanList = cityBeanList.subList(1,4);

                return cityBeanList;
            }

            //解析 JSON 文件
            String path = Environment.getExternalStorageDirectory()+"/city.json";

            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);


            int len=0;
            byte[] bytes = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while ((len = fileInputStream.read(bytes))!=-1){

                stringBuffer.append(new String(bytes,0,len));
            }
            JSONObject jsonObject = new JSONObject(stringBuffer.toString());

            JSONArray columns = jsonObject.getJSONArray("rows");
            for (int i = 0; i < columns.length(); i++) {
                JSONObject jj = columns.getJSONObject(i);
                if (jj.optString("pinyin").equals(""))
                    continue;
                CityBean cityBean = new CityBean();
                String name = jj.optString("name");
                String pinyin = jj.optString("pinyin");

                cityBean.setcName(name);
                cityBean.setPinyin(pinyin);
                if (!TextUtils.isEmpty(pinyin)) {
                    String shou = pinyin.substring(0, 1).toUpperCase();
                    Log.d("TAG", "getAllList: " + shou);

                    cityBean.setFirstWord(shou);
                }

                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("pinyin",pinyin);
                writableDatabase.insert("city",null,values);

                cityBeanList.add(cityBean);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityBeanList;
    }
}
