package com.example.city_demo_2.bean;

/**
 * Created by next on 2016/3/24.
 */
public class CityBean extends BaseIndexBean{

    private String cName;
    //Latitude and longitude
    private String latitude;
    private String longitude;

    //辅助排序使用

    public CityBean() {
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



    @Override
    public String toString() {
        return "City{" +
                "cName='" + cName + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    @Override
    public String getTarget() {
        return cName;
    }
}
