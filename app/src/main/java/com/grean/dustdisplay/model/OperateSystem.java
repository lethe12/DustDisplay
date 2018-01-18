package com.grean.dustdisplay.model;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.myApplication;
import com.grean.dustdisplay.presenter.NotifyOperateInfo;
import com.grean.dustdisplay.presenter.NotifyProcessDialogInfo;
import com.grean.dustdisplay.presenter.ShowOperateInfo;

/**
 * Created by weifeng on 2017/9/13.
 */

public class OperateSystem {
    private static final String tag = "OperateSystem";

    public OperateSystem(){

    }

    public void startDownLoadSoftware(Context context, String url, NotifyProcessDialogInfo processDialogInfo, ShowOperateInfo operateInfo){
        new Thread(new DownloadRunnable(context,url,processDialogInfo,operateInfo)).start();
    }

    public void connectServer(boolean connect){
        if(connect) {
            SocketTask.getInstance().restartSocketHeart();

        }else{
            SocketTask.getInstance().stopSocketHeart();
        }
    }

    private class DownloadRunnable implements Runnable{

        private String url;
        private NotifyProcessDialogInfo info;
        private Context context;
        private ShowOperateInfo operateInfo;

        public DownloadRunnable(Context context, String url, NotifyProcessDialogInfo info, ShowOperateInfo operateInfo){
            this.context = context;
            this.url = url;
            this.info = info;
            this.operateInfo = operateInfo;
        }

        private void queryDownloadProcess(long requestId,DownloadManager downloadManager){
            DownloadManager.Query query= new DownloadManager.Query();
            query.setFilterById(requestId);
            try{
                boolean isGoing = true;
                int times = 0;
                while (isGoing){
                    Cursor cursor = downloadManager.query(query);
                    if(cursor!=null && cursor.moveToFirst()){
                        int state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (state){
                            case DownloadManager.STATUS_SUCCESSFUL:
                                isGoing = false;
                                operateInfo.cancelDialogWithToast("下载成功!");
                                break;
                            case DownloadManager.STATUS_FAILED:
                                isGoing = false;
                                operateInfo.cancelDialogWithToast("下载失败!");
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                isGoing = false;
                                operateInfo.cancelDialogWithToast("下载失败!");
                                break;
                            case DownloadManager.STATUS_PENDING:
                                info.showInfo("准备下载");
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                Log.d(tag,"下载中");
                                break;
                            default:
                                break;

                        }
                        Thread.sleep(200);
                        if(cursor!=null){
                            cursor.close();
                        }
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d(tag,"下载完成");
        }

        private long startDownload(){
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long requestId = downloadManager.enqueue(CreateRequest(url));
            myApplication.getInstance().getConfig().put("ID",requestId);
            queryDownloadProcess(requestId,downloadManager);
            return requestId;
        }

        private DownloadManager.Request CreateRequest(String url){

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
           // Log.d(tag,url);
            // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir() ;
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,"123.apk");
            request.setDescription("杭州绿洁扬尘在线监测系统");
            return request;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            startDownload();
        }
    }
}
