package com.grean.dustdisplay.protocol;

import com.grean.dustdisplay.presenter.ShowRealTimeData;

/**
 * Created by weifeng on 2017/9/5.
 */

public interface GeneralClientProtocol {
    void handleReceiveData(String rec);
    void setShowRealTimeData(ShowRealTimeData showRealTimeData);
    void sendScanCommand();
}
