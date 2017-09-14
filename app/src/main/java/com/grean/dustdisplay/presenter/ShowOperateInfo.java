package com.grean.dustdisplay.presenter;

import com.grean.dustdisplay.protocol.DustMeterInfoFormat;
import com.grean.dustdisplay.protocol.SettingFormat;

/**
 * Created by weifeng on 2017/9/13.
 */

public interface ShowOperateInfo {
    void show(SettingFormat format);
    void showParaK(float para);
    void showToast(String string);
    void cancelDialog();
    void cancelDialogWithToast(String string);
    void showDustMeterInfo(DustMeterInfoFormat format);
}
