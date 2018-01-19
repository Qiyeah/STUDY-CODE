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

    public int[] date2IntArray(String date, String regex) {
        String[] arr = date.split(regex);
        int[] temp = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = Integer.valueOf(temp[i]);
        }
        return temp;
    }

    public static String formatDate(int... data){
        String date = "";
        for (int i = 0; i < data.length-1; i++) {
            date += (data[i]<10?0+data[i]:data[i])+"-";
        }
        date += data[data.length-1];
        return date;
    }
}
