package com.example.city_demo_2.citylist.bean;


/**
 * 介绍：索引类的标志位的实体基类

 */

public abstract class BaseIndexBean implements ISuspensionInterface {
    //所属的分类（城市的汉语拼音首字母）
    protected String firstWord="";
    protected String pinyin="";

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    //是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
    //微信的头部 不需要显示索引
    //美团的头部 索引自定义
    //默认应该是需要的
    public boolean isNeedToPinyin() {
        return true;
    }

    //需要转化成拼音的目标字段
    public abstract String getTarget();



    public String getFirstWord() {
        return firstWord;
    }

    public void setFirstWord(String firstWord) {
        this.firstWord = firstWord;
    }

    @Override
    public String getSuspensionTag() {
        return firstWord;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
