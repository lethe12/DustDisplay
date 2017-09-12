package com.grean.dustdisplay.model;

import android.content.Context;

/**
 * Created by weifeng on 2017/9/12.
 */

public class LoadConfig {
    private Context context;
    private String serverIp;
    private int serverPort;

    public LoadConfig(Context context){
        this.context= context;
    }

    public void readConfig(){
        ConfigOperate config = ConfigOperate.getInstance(context);
        if(!config.getConfigBoolean("FactorySetting")){
            config.saveConfig("FactorySetting",true);
            serverIp = "192.168.192.184";
            config.saveConfig("SeverIp",serverIp);
            serverPort = 8888;
            config.saveConfig("ServerPort",serverPort);
        }else{
            serverIp = config.getConfigString("ServerIp");
            serverPort = config.getConfigInt("ServerPort");
        }

    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }
}
