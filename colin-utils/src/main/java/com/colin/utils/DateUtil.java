package com.colin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018-1-2.
 */
public class DateUtil {
    public static DateUtil instance = null;

    private SimpleDateFormat sdf = null;
    private String pattern = "";

    private DateUtil(){

    }

    public static synchronized DateUtil getInstance() {
        if (null == instance){
            synchronized (DateUtil.class){
                instance = new DateUtil();
            }
        }
        return instance;
    }
    public DateUtil setPattern(String pattern){
        this.pattern = pattern;
        return this;
    }

    public Date str2Date(String strDate,String pattern){
        sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public double diffDays(String date2, String date1){
        return (int) ((str2Date(date2,pattern).getTime() - str2Date(date1,pattern).getTime()) / (1000*3600*24));
    }
}
