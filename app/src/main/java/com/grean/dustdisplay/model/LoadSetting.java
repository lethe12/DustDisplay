package com.grean.dustdisplay.model;

import com.grean.dustdisplay.presenter.NotifyProcessDialogInfo;
import com.grean.dustdisplay.presenter.ShowOperateInfo;
import com.grean.dustdisplay.protocol.DustMeterCalCtrl;
import com.grean.dustdisplay.protocol.DustMeterCalProcessFormat;
import com.grean.dustdisplay.protocol.GeneralClientProtocol;
import com.grean.dustdisplay.protocol.ProtocolLibs;
import com.grean.dustdisplay.protocol.SettingFormat;
import com.tools;

/**
 * Created by weifeng on 2017/9/13.
 */

public class LoadSetting implements DustMeterCalCtrl{
    private  ShowOperateInfo info;
    private SettingFormat format;
    private boolean dustMeterCalRun = false;

    public LoadSetting(ShowOperateInfo info){
        this.info = info;
    }

    public void loadSetting(){
        ProtocolLibs.getInstance().getClientProtocol().sendLoadSetting(info);
    }

    public void calDust(float target){
        ProtocolLibs.getInstance().getClientProtocol().sendCalDust(info,target);

    }

    public void saveAutoCal(boolean enable,String date,String interval){
        format.setAutoCalEnable(enable);
        format.setAutoCalDate(tools.string2timestamp(date));
        format.setAutoCalInterval(tools.string2timestamp(interval));
        ProtocolLibs.getInstance().getClientProtocol().sendUploadSetting(format);
    }

    public void saveServer(String ip,String port){
        format.setServerIp(ip);
        format.setServerPort(Integer.valueOf(port));
        ProtocolLibs.getInstance().getClientProtocol().sendUploadSetting(format);
    }

    public void readSetting(SettingFormat format){
        this.format = new SettingFormat();
        this.format.setAutoCalEnable(format.isAutoCalEnable());
        this.format.setAutoCalDate(format.getAutoCalDate());
        this.format.setAutoCalInterval(format.getAutoCalInterval());
        this.format.setServerIp(format.getServerIp());
        this.format.setServerPort(format.getServerPort());
    }

    public String calcNextDate(String string,String intervalString){
        long plan = tools.string2timestamp(string);
        long now = tools.nowtime2timestamp();
        long interval = Long.valueOf(intervalString);
        long next;
        if (interval!=0){
            next = tools.calcNextTime(now,plan,interval);
        }else {
            next = now + 24*3600l;
        }
        return tools.timestamp2string(next);
    }

    @Override
    public void onFinish() {
        dustMeterCalRun = false;
    }

    @Override
    public void onResult(String info) {
        this.info.showToast(info);
    }


    private class DustMeterCalThread extends Thread{
        private NotifyProcessDialogInfo dialogInfo;

        public DustMeterCalThread(NotifyProcessDialogInfo dialogInfo){
            this.dialogInfo = dialogInfo;
            dustMeterCalRun = true;
        }
        @Override
        public void run() {
            GeneralClientProtocol clientProtocol = ProtocolLibs.getInstance().getClientProtocol();
            clientProtocol.sendDustMeterCalStart(dialogInfo);
            dialogInfo.showInfo("开始校准...0%");
            while (dustMeterCalRun&&(!interrupted())) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientProtocol.sendDustMeterCalProcess(LoadSetting.this);
            }

            clientProtocol.sendDustMeterCalResult();
        }
    }
}
