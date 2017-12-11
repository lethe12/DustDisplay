package com.grean.dustdisplay.presenter;

import com.grean.dustdisplay.protocol.RealTimeDataFormat;

/**
 * Created by weifeng on 2017/9/13.
 */

public interface ShowRealTimeData {
    void show(RealTimeDataFormat format);
    void showAlarm(boolean isAlarm);
    void showInitProcess(int restTime);
    void showFinishInit();
    void showServer(boolean connected);
    void showLocal(boolean connected);
    void showDustName(String name);
}
