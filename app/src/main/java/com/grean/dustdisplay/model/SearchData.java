package com.grean.dustdisplay.model;

import android.util.Log;

import com.grean.dustdisplay.presenter.NotifyDataInfo;
import com.grean.dustdisplay.protocol.ProtocolLibs;
import com.tools;

/**
 * Created by weifeng on 2017/9/15.
 */

public class SearchData {
    private static final String tag="SearchData";
    private NotifyDataInfo dataInfo;
    private int nowPage,allPages;
    long start,end,now;

    public SearchData(NotifyDataInfo info){
        this.dataInfo = info;
    }

    public void readHistoryData(String startString,String endString){
        start = tools.string2timestamp(startString);
        end = tools.string2timestamp(endString);
        Log.d(tag,startString+":"+endString);
        nowPage = 1;
        now = start;
        long time;
        if(start < end){
            time = end - start;
        }else {
            time = start - end;
        }
        if((time%3600000l)!=0){
            allPages = (int) (time/3600000l)+1;
        }else{
            allPages = (int) (time / 3600000l);
        }

        ProtocolLibs.getInstance().getClientProtocol().sendHistoryData(dataInfo,now);
        dataInfo.upDatePages(nowPage,allPages);
    }

    public void pageUp(){
        if(nowPage >1){
            nowPage--;
            now -= 3600000l;
            ProtocolLibs.getInstance().getClientProtocol().sendHistoryData(dataInfo,now);
            dataInfo.upDatePages(nowPage,allPages);
        }else{
            dataInfo.noMorePages("第一页啦！");
        }
    }

    public String getNowString(){
        return tools.timestamp2string(now);
    }

    public void pageDown(){
        if(nowPage < allPages){
            nowPage++;
            now += 3600000l;
            ProtocolLibs.getInstance().getClientProtocol().sendHistoryData(dataInfo,now);
            dataInfo.upDatePages(nowPage,allPages);
        }else {
            dataInfo.noMorePages("最后一页啦！");

        }
    }

    public void exportData(String startString,String endString){
        long start = tools.string2timestamp(startString);
        long end = tools.string2timestamp(endString);
        ProtocolLibs.getInstance().getClientProtocol().sendExportData(dataInfo,start,end);
        new ReadExportDataProcess(dataInfo).start();
    }

    private class ReadExportDataProcess extends Thread implements InquireExportDataProcess{
        private NotifyDataInfo info;
        private boolean run;
        public ReadExportDataProcess(NotifyDataInfo info){
            this.info = info;
        }

        @Override
        public void run() {
            run = true;
            while (run&&!interrupted()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ProtocolLibs.getInstance().getClientProtocol().sendExportDataInfo(dataInfo,this);
            }
            Log.d(tag,"end Export");
        }

        @Override
        public void end() {
            run = false;
        }
    }
}
