package com.grean.dustdisplay.protocol;

/**
 * Created by weifeng on 2017/9/12.
 */

public class ProtocolLibs {
    private GeneralClientProtocol clientProtocol;
    private static ProtocolLibs instance = new ProtocolLibs();

    private ProtocolLibs(){

    }

    public static ProtocolLibs getInstance() {
        return instance;
    }

    public GeneralClientProtocol getClientProtocol(){
        if(clientProtocol==null){
            clientProtocol = new ClientProtocol();
        }
        return clientProtocol;
    }
}
