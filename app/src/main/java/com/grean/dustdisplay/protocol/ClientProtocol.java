package com.grean.dustdisplay.protocol;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.presenter.ShowOperateInfo;
import com.grean.dustdisplay.presenter.ShowRealTimeData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weifeng on 2017/9/12.
 */

public class ClientProtocol implements GeneralClientProtocol{
    private static final String tag = "ClientProtocol";
    private ShowRealTimeData show;
    private RealTimeDataFormat dataFormat;
    private ShowOperateInfo info;

    @Override
    public void handleReceiveData(String rec) {
        try {
            JSONObject jsonObject = new JSONObject(rec);
            String type = JSON.getProtocolType(jsonObject);
            if(type.equals("realTimeData")){
                dataFormat = JSON.getRealTimeData(jsonObject);
                if(show!=null) {
                    show.show(dataFormat);
                }
            }else if(type.equals("downloadSetting")){
                SettingFormat format = JSON.getSetting(jsonObject);
                if(info!=null) {
                    info.show(format);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setShowRealTimeData(ShowRealTimeData showRealTimeData) {
        this.show = showRealTimeData;
    }

    @Override
    public void sendScanCommand() {

        try {
            SocketTask.getInstance().send(JSON.readRealTimeData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLoadSetting(ShowOperateInfo info) {
        this.info = info;
        try {
            SocketTask.getInstance().send(JSON.readSetting());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
