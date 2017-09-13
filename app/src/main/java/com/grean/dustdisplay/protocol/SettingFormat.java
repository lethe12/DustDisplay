package com.grean.dustdisplay.protocol;

/**
 * Created by weifeng on 2017/9/13.
 */

public class SettingFormat {
    boolean autoCalEnable;
    long autoCalDate,autoCalInterval;
    int serverPort;
    String serverIp;
    float paraK;

    public float getParaK() {
        return paraK;
    }

    public void setParaK(float paraK) {
        this.paraK = paraK;
    }

    public boolean isAutoCalEnable() {
        return autoCalEnable;
    }

    public void setAutoCalEnable(boolean autoCalEnable) {
        this.autoCalEnable = autoCalEnable;
    }

    public long getAutoCalDate() {
        return autoCalDate;
    }

    public void setAutoCalDate(long autoCalDate) {
        this.autoCalDate = autoCalDate;
    }

    public long getAutoCalInterval() {
        return autoCalInterval;
    }

    public void setAutoCalInterval(long autoCalInterval) {
        this.autoCalInterval = autoCalInterval;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
