package com.colin.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2017-12-29.
 */

public class SharedPreferencesUtil {
    private Context mContext;
    private SharedPreferences sp;
    private SharedPreferences.Editor mEditor;
    public SharedPreferencesUtil(Context context, String xmlName) {
        mContext = context;
        sp = mContext.getSharedPreferences(xmlName, Context.MODE_PRIVATE);
    }
    public  void saveObject(String key, String obj){
        mEditor = sp.edit();
        mEditor.putString(key, obj);
        mEditor.commit();
        mEditor.clear();
    }
    public String getObject(String key) {
        return sp.getString(key, "");
    }
}
