package com.grean.dustdisplay.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by weifeng on 2017/9/12.
 */

public class ConfigOperate {
    private static ConfigOperate instance = new ConfigOperate();
    private static Context context = null;
    private ConfigOperate(){

    }

    public static ConfigOperate getInstance(Context c) {
        context = c;
        return instance;
    }

    public void saveConfig(String key,float data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key,data);
        editor.commit();
    }

    public void saveConfig(String key,long data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key,data);
        editor.commit();
    }

    public void saveConfig(String key,int data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,data);
        editor.commit();
    }

    public void saveConfig(String key,String data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,data);
        editor.commit();
    }

    public void saveConfig(String key,boolean data){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,data);
        editor.commit();
    }

    public boolean getConfigBoolean(String key){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    public float getConfigFloat(String key){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getFloat(key,0f);
    }

    public int getConfigInt(String key){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getInt(key,10);
    }

    public long getConfigLong(String key){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getLong(key,10);
    }

    public String getConfigString(String key){
        SharedPreferences sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(key," ");
    }
}
