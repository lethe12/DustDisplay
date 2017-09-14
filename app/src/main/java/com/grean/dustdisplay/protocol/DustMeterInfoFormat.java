package com.grean.dustdisplay.protocol;

/**
 * Created by weifeng on 2017/9/14.
 */

public class DustMeterInfoFormat {
    private int pumpTime,laserTime;

    public int getPumpTime() {
        return pumpTime;
    }

    public void setPumpTime(int pumpTime) {
        this.pumpTime = pumpTime;
    }

    public int getLaserTime() {
        return laserTime;
    }

    public void setLaserTime(int laserTime) {
        this.laserTime = laserTime;
    }
}
