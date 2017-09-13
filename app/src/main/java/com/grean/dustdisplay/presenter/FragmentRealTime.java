package com.grean.dustdisplay.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grean.dustdisplay.R;
import com.grean.dustdisplay.model.ScanRealTimeData;
import com.grean.dustdisplay.protocol.RealTimeDataFormat;
import com.tools;



/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentRealTime extends Fragment implements ShowRealTimeData{
    private static final String tag = "FragmentRealTime";
    private ScanRealTimeData realTimeData;
    private TextView tvDust,tvTemperature,tvHumidity,tvPressure,tvWindForce,tvWindDirection,tvNoise,tvState;
    private String dustString,temperatureString,humidityString,pressureString,windForceString,windDirectionString
            ,noiseString,stateString;
    private static final int msgShowRealTImeData = 1;
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
        realTimeData.startScan();
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
        temperatureString = tools.float2String3(format.getTemperature());
        humidityString = tools.float2String3(format.getHumidity());
        pressureString = tools.float2String0(format.getPressure());
        windForceString = tools.float2String3(format.getWindForce());
        windDirectionString = tools.float2String0(format.getWindDirection());
        noiseString = tools.float2String3(format.getNoise());
        stateString = format.getState();
        handler.sendEmptyMessage(msgShowRealTImeData);
    }
}
