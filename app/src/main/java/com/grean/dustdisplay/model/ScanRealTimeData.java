package com.grean.dustdisplay.model;

import android.util.Log;

import com.grean.dustdisplay.presenter.ShowRealTimeData;
import com.grean.dustdisplay.protocol.GeneralClientProtocol;
import com.grean.dustdisplay.protocol.ProtocolLibs;

/**
 * Created by weifeng on 2017/9/13.
 */

public class ScanRealTimeData {
    private static final String tag = "ScanRealTimeData";
    private ShowRealTimeData show;
    private boolean run = false;
    private ScanDataThread thread;
    private GeneralClientProtocol clientProtocol;

    public ScanRealTimeData(ShowRealTimeData showRealTimeData){
        this.show = showRealTimeData;
        clientProtocol = ProtocolLibs.getInstance().getClientProtocol();
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

    private class ScanDataThread extends Thread{
        @Override
        public void run() {
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
