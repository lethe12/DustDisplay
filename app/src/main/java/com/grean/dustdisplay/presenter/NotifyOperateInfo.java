package com.grean.dustdisplay.presenter;

/**
 * 操作界面下信息更新
 * Created by Administrator on 2017/8/29.
 */

public interface NotifyOperateInfo {

    void showDustMeterInfo(String info);
    void cancelDialog();
    void cancelDialogWithToast(String string);
}
