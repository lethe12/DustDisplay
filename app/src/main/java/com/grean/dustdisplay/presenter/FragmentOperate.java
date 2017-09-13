package com.grean.dustdisplay.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.grean.dustdisplay.R;
import com.grean.dustdisplay.model.LoadSetting;
import com.grean.dustdisplay.protocol.SettingFormat;
import com.tools;

/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentOperate extends Fragment implements View.OnClickListener ,ShowOperateInfo{
    private TextView tvDustMeterTitle,tvDustMeterParaK,tvAutoCalDate,tvSystemTitle;
    private EditText etDustTarget,etAutoCalInterval,etServerIp,etServerPort,etSoftwareUpdateUrl;
    private Button btnDustCal,btnAutoSave,btnDustMeterCal,btnSaveServer,btnSoftwareUpdate,btnVideoPreview,btnVideoSetting;
    private Switch swAutoCalEnable;
    private LoadSetting setting;

    private String paraKString,autoCalDateString,autoCalIntervalString,serverIpString,serverPortString;
    private boolean autoCalEnable;


    private static final int msgShowSetting = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msgShowSetting:
                    tvDustMeterParaK.setText(paraKString);
                    etServerIp.setText(serverIpString);
                    etServerPort.setText(serverPortString);
                    if(autoCalEnable){
                        swAutoCalEnable.setChecked(true);
                        tvAutoCalDate.setVisibility(View.VISIBLE);
                        etAutoCalInterval.setVisibility(View.INVISIBLE);
                    }else{
                        swAutoCalEnable.setChecked(false);
                        tvAutoCalDate.setVisibility(View.INVISIBLE);
                        etAutoCalInterval.setVisibility(View.INVISIBLE);
                    }
                    tvAutoCalDate.setText(autoCalDateString);
                    etAutoCalInterval.setText(autoCalIntervalString);
                    break;
                default:

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_operate,container,false);
        initView(view);
        setting = new LoadSetting(this);
        setting.loadSetting();
        return view;
    }

    void initView(View v){
        tvDustMeterTitle = v.findViewById(R.id.tvOperateDustMeterTitle);
        tvDustMeterParaK = v.findViewById(R.id.tvOperateParaK);
        tvAutoCalDate = v.findViewById(R.id.tvOperateAutoCalDate);
        tvSystemTitle = v.findViewById(R.id.tvOperateSystemTitle);
        etDustTarget = v.findViewById(R.id.etOperateDustTarget);
        etAutoCalInterval = v.findViewById(R.id.etOperateAutoCalInterval);
        etServerIp = v.findViewById(R.id.etOperateServerIp);
        etServerPort = v.findViewById(R.id.etOperateServerPort);
        etSoftwareUpdateUrl = v.findViewById(R.id.etOperateUpdateSoftwareUrl);
        btnDustCal = v.findViewById(R.id.btnOperateDustCal);
        btnAutoSave = v.findViewById(R.id.btnOperateSaveAutoCal);
        btnDustMeterCal = v.findViewById(R.id.btnOperateCalMan);
        btnSaveServer = v.findViewById(R.id.btnOperateSaveServer);
        btnSoftwareUpdate = v.findViewById(R.id.btnOperateUpdateSoftware);
        btnVideoPreview = v.findViewById(R.id.btnOperateVideoPreview);
        btnVideoSetting = v.findViewById(R.id.btnOperateVideoSetting);
        swAutoCalEnable = v.findViewById(R.id.swOperateAutoCal);
        tvDustMeterTitle.setOnClickListener(this);
        tvAutoCalDate.setOnClickListener(this);
        tvSystemTitle.setOnClickListener(this);
        btnDustCal.setOnClickListener(this);
        btnAutoSave.setOnClickListener(this);
        btnDustMeterCal.setOnClickListener(this);
        btnSaveServer.setOnClickListener(this);
        btnSoftwareUpdate.setOnClickListener(this);
        btnVideoPreview.setOnClickListener(this);
        btnVideoSetting.setOnClickListener(this);
        swAutoCalEnable.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvOperateDustMeterTitle:

                break;
            case R.id.tvOperateAutoCalDate:

                break;
            case R.id.tvOperateSystemTitle:

                break;
            case R.id.btnOperateDustCal:

                break;
            case R.id.btnOperateSaveServer:

                break;
            case R.id.btnOperateUpdateSoftware:

                break;
            case R.id.btnOperateVideoPreview:

                break;
            case R.id.btnOperateVideoSetting:

                break;
            case R.id.swOperateAutoCal:

                break;
            default:

                break;
        }

    }

    @Override
    public void show(SettingFormat format) {
        paraKString = tools.float2String3(format.getParaK());
        this.autoCalEnable = format.isAutoCalEnable();
        autoCalDateString = tools.timestamp2string(format.getAutoCalDate());
        autoCalIntervalString = String.valueOf(format.getAutoCalInterval() / 3600000l);
        this.serverIpString = format.getServerIp();
        serverPortString = String.valueOf(format.getServerPort());
        handler.sendEmptyMessage(msgShowSetting);
    }
}
