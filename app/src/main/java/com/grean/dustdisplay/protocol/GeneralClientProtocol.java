package com.grean.dustdisplay.protocol;

import com.grean.dustdisplay.presenter.NotifyProcessDialogInfo;
import com.grean.dustdisplay.presenter.ShowOperateInfo;
import com.grean.dustdisplay.presenter.ShowRealTimeData;

/**
 * Created by weifeng on 2017/9/5.
 */

public interface GeneralClientProtocol {
    void handleReceiveData(String rec);
    void setShowRealTimeData(ShowRealTimeData showRealTimeData);
    void sendScanCommand();
    void sendLoadSetting(ShowOperateInfo info);
    void sendCalDust(ShowOperateInfo info,float target);
    void sendUploadSetting(SettingFormat format);
    void sendDustMeterCalStart(NotifyProcessDialogInfo dialogInfo);
    void sendDustMeterCalResult();
    void sendDustMeterCalProcess(DustMeterCalCtrl ctrl);
    void sendDustMeterInfo(ShowOperateInfo info);
}
