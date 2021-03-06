package com.grean.dustdisplay.protocol;

import android.util.Log;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.model.InquireExportDataProcess;
import com.grean.dustdisplay.presenter.NotifyDataInfo;
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
    private NotifyDataInfo dataInfo;
    private InquireExportDataProcess exportDataProcess;

    @Override
    public void handleReceiveData(String rec) {
        try {
            JSONObject jsonObject = new JSONObject(rec);
            String type = JSON.getProtocolType(jsonObject);
            if(type.equals("realTimeData")){
                //Log.d(tag,rec);
                dataFormat = JSON.getRealTimeData(jsonObject);
                if(show!=null) {
                    show.show(dataFormat);
                    show.showAlarm(dataFormat.isAlarm());
                    show.showServer(dataFormat.isServerConnected());
                }
            }else if(type.equals("downloadSetting")){
                //Log.d(tag,rec);
                SettingFormat format = JSON.getSetting(jsonObject);
                if(info!=null) {
                    info.show(format);
                }
            }else if(type.equals("operateInit")){
                String name = JSON.getDustName(jsonObject);
                if(show!=null){
                  show.showDustName(name);
                }
            }else if(type.equals("operate")){
                //Log.d(tag,rec);
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
                }else if(jsonObject.has("ExportDataProcess")){
                    ExportDataInfo exportDataInfo = JSON.getExportDataProcess(jsonObject);
                    if(dataInfo!=null){
                        if(exportDataInfo.getProcess() == 100) {
                            if(exportDataProcess!=null){
                                exportDataProcess.end();
                            }
                            dataInfo.onExportDataResult(exportDataInfo);
                        }
                    }
                }
            }else if(type.equals("historyData")){
                Log.d(tag,rec);
                HistoryDataContent content = JSON.getHistoryData(jsonObject);
                if(dataInfo!=null){
                    dataInfo.updateHistoryData(content);
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
    public boolean sendGetOperateInit() {
        try {
            return SocketTask.getInstance().send(JSON.readOperateInit());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
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
    public void sendSetDustMeterParaK(float k,float b) {
        try {
            SocketTask.getInstance().send(JSON.operateDustSetParaK(k,b));
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
    public void sendDustMeterCalZeroStart(NotifyProcessDialogInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
        try {
            SocketTask.getInstance().send(JSON.operateDustMeterCalZero());
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

    @Override
    public void sendExportData(NotifyDataInfo info,long start,long end) {
        this.dataInfo = info;
        try {
            SocketTask.getInstance().send(JSON.exportData(start,end));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendExportDataInfo(NotifyDataInfo info,InquireExportDataProcess dataProcess) {
        this.dataInfo = info;
        this.exportDataProcess = dataProcess;
        try {
            SocketTask.getInstance().send(JSON.readExportDataProcess());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendHistoryData(NotifyDataInfo info, long date) {
        this.dataInfo = info;
        try {
            SocketTask.getInstance().send(JSON.readHistoryData(date));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
