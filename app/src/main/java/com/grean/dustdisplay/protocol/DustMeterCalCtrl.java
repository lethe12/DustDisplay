package com.grean.dustdisplay.protocol;

/**
 * Created by weifeng on 2017/9/13.
 */

public interface DustMeterCalCtrl {
    /**
     * 结束校准
     */
    void onFinish();

    /**
     * 显示校准结果
     * @param info
     */
    void onResult(String info);
}
