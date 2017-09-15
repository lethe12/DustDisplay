package com.grean.dustdisplay.presenter;

import com.grean.dustdisplay.protocol.ExportDataInfo;
import com.grean.dustdisplay.protocol.HistoryDataContent;

/**
 * Created by weifeng on 2017/9/15.
 */

public interface NotifyDataInfo {
    void onExportDataResult(ExportDataInfo info);
    void upDatePages(int now,int all);
    void noMorePages(String info);
    void updateHistoryData(HistoryDataContent content);
    void searchHistoryData();
}
