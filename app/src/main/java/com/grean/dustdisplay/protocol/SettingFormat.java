package com.grean.dustdisplay.protocol;

/**
 * Created by weifeng on 2017/9/13.
 */

public class SettingFormat {
    private boolean autoCalEnable;
    private long autoCalDate,autoCalInterval;
    private int serverPort,protocolName;
    private String serverIp,mnCode;
    private float paraK,alarmDust;
    private String[] protocolNames;

    public int getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(int protocolName) {
        this.protocolName = protocolName;
    }

    public float getAlarmDust() {
        return alarmDust;
    }

    public void setAlarmDust(float alarmDust) {
        this.alarmDust = alarmDust;
    }

    public String[] getProtocolNames() {
        return protocolNames;
    }

    public void setProtocolNames(String[] protocolNames) {
        this.protocolNames = protocolNames;
    }

    public String getMnCode() {
        return mnCode;
    }

    public void setMnCode(String mnCode) {
        this.mnCode = mnCode;
    }

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
