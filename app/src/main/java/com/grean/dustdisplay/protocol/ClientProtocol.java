package com.grean.dustdisplay.protocol;

import android.util.Log;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.presenter.NotifyProcessDialogInfo;
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
    private DustMeterCalCtrl ctrl;
    private NotifyProcessDialogInfo dialogInfo;

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
            }else if(type.equals("operate")){
                Log.d(tag,rec);
                if(jsonObject.has("DustCal")){
                    if(info!=null){
                        info.showParaK(JSON.getOperateDustCal(jsonObject));
                    }
                }else if(jsonObject.has("DustMeterCalProcess")){
                    DustMeterCalProcessFormat format = JSON.getDustMeterCalProcess(jsonObject);
                    if(dialogInfo!=null){
                        dialogInfo.showInfo(format.getString());
                        }
                    if(format.getProcess() == 100) {
                        if (ctrl != null) {
                            ctrl.onFinish();
                        }
                    }
                }else if(jsonObject.has("DustMeterCalResult")){
                    String string =  JSON.getDustMeterCalResult(jsonObject);
                    if(ctrl!=null){
                        ctrl.onResult(string);
                    }
                }else if(jsonObject.has("DustMeterInfo")){
                    DustMeterInfoFormat format = JSON.getDustMeterInfo(jsonObject);
                    if(info!=null){
                        info.showDustMeterInfo(format);
                    }
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

    @Override
    public void sendCalDust(ShowOperateInfo info, float target) {
        this.info = info;
        try {
            SocketTask.getInstance().send(JSON.operateDustCal(target));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUploadSetting(SettingFormat format) {
        try {
            SocketTask.getInstance().send(JSON.uploadSetting(format));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDustMeterCalStart(NotifyProcessDialogInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
        try {
            SocketTask.getInstance().send(JSON.operateDustMeterCal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDustMeterCalResult() {
        try {
            SocketTask.getInstance().send(JSON.operateDustMeterCalResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDustMeterCalProcess(DustMeterCalCtrl ctrl) {
        this.ctrl = ctrl;
        try {
            SocketTask.getInstance().send(JSON.operateDustMeterCalProcess());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDustMeterInfo(ShowOperateInfo info) {
        this.info = info;
        try {
            SocketTask.getInstance().send(JSON.readDustMeterInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
