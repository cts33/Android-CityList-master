package com.example.city_demo_2.bean;

/**
 * Created by next on 2016/3/24.
 */
public class CityBean extends BaseIndexPinyinBean{

    private String cName;
    //Latitude and longitude
    private String latitude;
    private String longitude;
    private String firstLetter;
    //辅助排序使用
    private String pinyin;

    public CityBean() {
    }

    public CityBean(String cName ) {
        this.cName = cName;

    }
    public CityBean(String cName, String pinyin) {
        this.cName = cName;
        this.pinyin = pinyin;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "City{" +
                "cName='" + cName + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", firstLetter='" + firstLetter + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }

    @Override
    public String getTarget() {
        return cName;
    }
}
