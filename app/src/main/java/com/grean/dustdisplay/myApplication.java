package com.grean.dustdisplay;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by weifeng on 2017/9/12.
 */

public class myApplication extends Application{
    private static final String tag="myApplication";
    private static myApplication instance;
    private HashMap config = new HashMap<String,Objects>();

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
    }

    public myApplication(){
        Log.d(tag,"开机");
    }

    public static myApplication getInstance() {
        return instance;
    }

    public HashMap getConfig() {
        return config;
    }

    public void setConfig(HashMap config) {
        this.config = config;
    }
}
