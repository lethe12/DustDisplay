package com.grean.dustdisplay.model;

import com.grean.dustdisplay.presenter.ShowOperateInfo;
import com.grean.dustdisplay.protocol.ProtocolLibs;

/**
 * Created by weifeng on 2017/9/13.
 */

public class LoadSetting {
    private  ShowOperateInfo info;

    public LoadSetting(ShowOperateInfo info){
        this.info = info;
    }

    public void loadSetting(){
        ProtocolLibs.getInstance().getClientProtocol().sendLoadSetting(info);

    }
}
