package com.grean.dustdisplay.model;

import android.util.Log;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.presenter.ShowRealTimeData;
import com.grean.dustdisplay.protocol.GeneralClientProtocol;
import com.grean.dustdisplay.protocol.ProtocolLibs;
import com.tools;

import java.net.Socket;

/**
 * Created by weifeng on 2017/9/13.
 */

public class ScanRealTimeData {
    private static final String tag = "ScanRealTimeData";
    private ShowRealTimeData show;
    private boolean run = false,initRun = false;
    private ScanDataThread thread;
    private GeneralClientProtocol clientProtocol;

    public ScanRealTimeData(ShowRealTimeData showRealTimeData){
        this.show = showRealTimeData;
        clientProtocol = ProtocolLibs.getInstance().getClientProtocol();
    }

    public boolean getLocalConnected(){
        return SocketTask.getInstance().isConnected();
    }

    public void stopScan(){
        run = false;
    }

    public void startScan(){
        clientProtocol.setShowRealTimeData(show);
        if(!run){
            thread = new ScanDataThread();
            thread.start();
        }
    }

    /**
     * 跳过初始化
     */
    public void stopInit(){
        initRun = false;
    }

    private class ScanDataThread extends Thread{
        @Override
        public void run() {
            //初始化
            long now = tools.nowtime2timestamp();
           // long end = now + 300000l;
            long end = now + 30000l;
            initRun = true;
            int i=0,restTime=300,j=0;
            show.showInitProcess(restTime);
            while ((now < end)&&initRun){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                j++;
                if(j>=5){
                    j=0;
                    restTime--;
                    if(restTime <= 0){
                        restTime = 0;
                    }
                    show.showInitProcess(restTime);
                }

                i++;
                if(i>=10){
                    i=0;
                    clientProtocol.sendScanCommand();
                }
                now = tools.nowtime2timestamp();
            }
            Log.d(tag,"stop init");
            show.showFinishInit();
            //查询信息
            Log.d(tag,"查询信息");
            i=0;
            while (!clientProtocol.sendGetOperateInit()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                if(i>200){
                    break;
                }
            }
            Log.d(tag,"查询数据");
            //开始查询数据
            run = true;
            while (run&&!interrupted()){
                clientProtocol.sendScanCommand();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            Log.d(tag,"end scan");
        }
    }
}
