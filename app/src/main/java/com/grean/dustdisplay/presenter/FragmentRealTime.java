package com.grean.dustdisplay.presenter;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grean.dustdisplay.R;
import com.grean.dustdisplay.SocketTask;
import com.grean.dustdisplay.model.ScanRealTimeData;
import com.grean.dustdisplay.protocol.RealTimeDataFormat;
import com.tools;



/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentRealTime extends Fragment implements ShowRealTimeData{
    private static final String tag = "FragmentRealTime";
    private ScanRealTimeData realTimeData;
    private View layoutTsp;
    private TextView tvDust,tvTemperature,tvHumidity,tvPressure,tvWindForce,tvWindDirection,tvNoise,tvState,tvAlarm,tvDustName;
    private String dustString,temperatureString,humidityString,pressureString,windForceString,windDirectionString
            ,noiseString,stateString,initProcessString,dustNameString;
    private boolean isAlarm,isLocal,isServer = false;
    private ProcessDialogFragment dialogFragment;
    private ImageView ivLocal,ivServer;
    private static final int msgShowRealTImeData = 1,
            msgShowAlarmContent = 2,
    msgShowInitProcess = 3,
    msgFinishInit = 4,
    msgShowLocal = 5,
    msgShowServer = 6,
    msgShowDustName = 7;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msgShowRealTImeData:
                    tvDust.setText(dustString);
                    tvTemperature.setText(temperatureString);
                    tvHumidity.setText(humidityString);
                    tvPressure.setText(pressureString);
                    tvWindForce.setText(windForceString);
                    tvWindDirection.setText(windDirectionString);
                    tvState.setText(stateString);
                    tvNoise.setText(noiseString);
                    break;
                case msgShowAlarmContent:
                    if(isAlarm) {
                        tvAlarm.setText("警告:扬尘浓度高");
                        layoutTsp.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_red_light));
                    }else{
                        tvAlarm.setText("警告:无");
                        layoutTsp.setBackgroundColor(Resources.getSystem().getColor(android.R.color.background_light));
                    }

                    break;
                case msgShowInitProcess:
                    dialogFragment.showInfo(initProcessString);
                    break;
                case msgFinishInit:
                    if(dialogFragment!=null &&  dialogFragment.getDialog()!=null
                            && dialogFragment.getDialog().isShowing()) {
                        dialogFragment.dismiss();
                    }
                    break;
                case msgShowServer:
                    if(isServer){
                       ivServer.setImageDrawable(getResources().getDrawable(R.drawable.server_online));
                    }else {
                        ivServer.setImageDrawable(getResources().getDrawable(R.drawable.server_offline));
                    }
                    break;
                case msgShowLocal:
                    if(isLocal){
                        ivLocal.setImageDrawable(getResources().getDrawable(R.drawable.local_online));
                    }else{
                        ivLocal.setImageDrawable(getResources().getDrawable(R.drawable.local_offline));
                    }
                    break;
                case msgShowDustName:
                    tvDustName.setText(dustNameString);
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime,container,false);
        initView(view);
        realTimeData = new ScanRealTimeData(this);
        dialogFragment = new ProcessDialogFragment();
        dialogFragment.setCancelable(true);
        dialogFragment.show(getFragmentManager(),"Init");
        dialogFragment.onCancel(new Dialog(getActivity()){
            @Override
            public void setOnCancelListener(@Nullable OnCancelListener listener) {
                Log.d(tag,"cancel Init Dialog");
                super.setOnCancelListener(listener);
                realTimeData.stopInit();
            }
        });
        realTimeData.startScan();
        isLocal = realTimeData.getLocalConnected();
        if(isLocal){
            ivLocal.setImageDrawable(getResources().getDrawable(R.drawable.local_online));
        }else{
            ivLocal.setImageDrawable(getResources().getDrawable(R.drawable.local_offline));
        }
        SocketTask.getInstance().setLocalListener(this);
        return view;
    }

    private void initView (View v){
        tvDust = v.findViewById(R.id.tvRealTimeDust);
        tvTemperature = v.findViewById(R.id.tvRealTimeTemperature);
        tvHumidity = v.findViewById(R.id.tvRealTimeHumidity);
        tvPressure = v.findViewById(R.id.tvRealTimePressure);
        tvWindForce = v.findViewById(R.id.tvRealTimeWindForce);
        tvWindDirection = v.findViewById(R.id.tvRealTimeWindDirection);
        tvNoise = v.findViewById(R.id.tvRealTimeNoise);
        tvState = v.findViewById(R.id.tvRealTimeState);
        tvAlarm = v.findViewById(R.id.tvMainAlarmContent);
        layoutTsp = v.findViewById(R.id.layoutMainTsp);
        ivLocal = v.findViewById(R.id.ivMainLocal);
        ivServer = v.findViewById(R.id.ivMainServer);
        tvDustName = v.findViewById(R.id.tvRealTimeDustName);
    }

    @Override
    public void onDestroy() {
        realTimeData.stopScan();
        super.onDestroy();
        Log.d(tag,"onDestroy");

    }

    @Override
    public void show(RealTimeDataFormat format) {
        dustString = tools.float2String3(format.getDust());
        temperatureString = tools.float2String1(format.getTemperature());
        humidityString = tools.float2String1(format.getHumidity());
        pressureString = tools.float2String0(format.getPressure());
        windForceString = tools.float2String1(format.getWindForce());
        windDirectionString = tools.float2String0(format.getWindDirection());
        noiseString = tools.float2String1(format.getNoise());
        stateString = format.getState();
        handler.sendEmptyMessage(msgShowRealTImeData);
    }

    @Override
    public void showAlarm(boolean isAlarm) {
        if(this.isAlarm!=isAlarm){
            this.isAlarm = isAlarm;
            handler.sendEmptyMessage(msgShowAlarmContent);
        }
    }

    @Override
    public void showInitProcess(int restTime) {
        initProcessString = "初始化剩余时间:"+String.valueOf(restTime)+"S";
        handler.sendEmptyMessage(msgShowInitProcess);
    }

    @Override
    public void showFinishInit() {
        handler.sendEmptyMessage(msgFinishInit);
    }

    @Override
    public void showServer(boolean connected) {
        if(isServer!=connected){
            isServer = connected;
            handler.sendEmptyMessage(msgShowServer);
        }
    }

    @Override
    public void showLocal(boolean connected) {
        if(isLocal!=connected){
            isLocal = connected;
            handler.sendEmptyMessage(msgShowLocal);
        }

    }

    @Override
    public void showDustName(String name) {
        dustNameString = name;
        handler.sendEmptyMessage(msgShowDustName);
    }
}
