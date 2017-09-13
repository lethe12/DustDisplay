package com.grean.dustdisplay.protocol;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.presenter.ShowRealTimeData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weifeng on 2017/9/12.
 */

public class ClientProtocol implements GeneralClientProtocol{
    private ShowRealTimeData show;
    private RealTimeDataFormat dataFormat;

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
}
