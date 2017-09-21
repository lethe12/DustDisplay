package com.grean.dustdisplay.presenter;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.grean.dustdisplay.R;
import com.grean.dustdisplay.model.LoadSetting;
import com.grean.dustdisplay.model.OperateSystem;
import com.grean.dustdisplay.protocol.DustMeterInfoFormat;
import com.grean.dustdisplay.protocol.SettingFormat;
import com.tools;

import java.util.Calendar;

/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentOperate extends Fragment implements View.OnClickListener ,ShowOperateInfo,DialogTimeSelected{
    private TextView tvDustMeterTitle,tvDustMeterParaK,tvAutoCalDate,tvSystemTitle,tvDustMeterInfo;
    private EditText etDustTarget,etAutoCalInterval,etServerIp,etServerPort,etSoftwareUpdateUrl,etMnCode;
    private Button btnDustCal,btnAutoSave,btnDustMeterCal,btnSaveServer,btnSoftwareUpdate,btnVideoPreview,btnVideoSetting;
    private Switch swAutoCalEnable;
    private LoadSetting setting;
    private View layoutDustMeter,layoutSystem;
    private ProcessDialogFragment dialogFragment;
    private OperateSystem system;

    private String paraKString,autoCalDateString,autoCalIntervalString,serverIpString,serverPortString,toastString,dustMeterInfoString,mnCodeString;
    private boolean autoCalEnable;

    private int dustMeterClickTimes=0,systemClickTimes=0;

    private static final int msgShowSetting = 1;
    private static final int msgShowParaK = 2;
    private static final int msgShowToast = 3;
    private static final int msgCancelDialog = 4;
    private static final int msgCancelDialogWithToast = 5;
    private static final int msgShowDustMeterInfo = 6;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msgShowSetting:
                    tvDustMeterParaK.setText(paraKString);
                    etAutoCalInterval.setText(autoCalIntervalString);
                    etServerIp.setText(serverIpString);
                    etServerPort.setText(serverPortString);
                    etMnCode.setText(mnCodeString);
                    if(autoCalEnable){
                        swAutoCalEnable.setChecked(true);
                        tvAutoCalDate.setVisibility(View.VISIBLE);
                        etAutoCalInterval.setVisibility(View.VISIBLE);
                        btnAutoSave.setVisibility(View.VISIBLE);
                    }else{
                        swAutoCalEnable.setChecked(false);
                        tvAutoCalDate.setVisibility(View.INVISIBLE);
                        etAutoCalInterval.setVisibility(View.INVISIBLE);
                        btnAutoSave.setVisibility(View.INVISIBLE);
                    }
                    tvAutoCalDate.setText(autoCalDateString);
                    etAutoCalInterval.setText(autoCalIntervalString);
                    if(dialogFragment!=null){
                        dialogFragment.dismiss();
                    }
                    break;
                case msgShowParaK:
                    tvDustMeterParaK.setText(paraKString);
                    break;
                case msgShowToast:
                    Toast.makeText(getActivity(),toastString,Toast.LENGTH_SHORT).show();
                    break;
                case msgCancelDialog:
                    dialogFragment.dismiss();
                    break;
                case msgCancelDialogWithToast:
                    dialogFragment.dismiss();
                    Toast.makeText(getActivity(),toastString,Toast.LENGTH_SHORT).show();
                    break;
                case msgShowDustMeterInfo:
                    tvDustMeterInfo.setText(dustMeterInfoString);
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
        system = new OperateSystem();
        dialogFragment = new ProcessDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(),"OperateInfo");
        return view;
    }

    void initView(View v){
        tvDustMeterTitle = v.findViewById(R.id.tvOperateDustMeterTitle);
        tvDustMeterParaK = v.findViewById(R.id.tvOperateParaK);
        tvAutoCalDate = v.findViewById(R.id.tvOperateAutoCalDate);
        tvSystemTitle = v.findViewById(R.id.tvOperateSystemTitle);
        tvDustMeterInfo = v.findViewById(R.id.tvOperateDustMeterInfo);
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
        layoutDustMeter = v.findViewById(R.id.layoutOperateDustMeter);
        layoutSystem = v.findViewById(R.id.layoutOperateUpdateSoftware);
        etMnCode = v.findViewById(R.id.etOperateMnCode);
        tvDustMeterTitle.setOnClickListener(this);
        tvAutoCalDate.setOnClickListener(this);
        tvSystemTitle.setOnClickListener(this);
        tvDustMeterInfo.setOnClickListener(this);
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
                dustMeterClickTimes++;
                if(dustMeterClickTimes == 3){
                    layoutDustMeter.setVisibility(View.VISIBLE);
                }else if(dustMeterClickTimes ==4){
                    dustMeterClickTimes = 0;
                    layoutDustMeter.setVisibility(View.GONE);
                }
                break;
            case R.id.tvOperateAutoCalDate:
                Calendar calendar = Calendar.getInstance();
                DialogTimeChoose choose = new DialogTimeChoose(getActivity(),"设置下次自动校准时间");
                choose.showDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,this);
                break;
            case R.id.tvOperateSystemTitle:
                systemClickTimes++;
                if(systemClickTimes == 3){
                    layoutSystem.setVisibility(View.VISIBLE);
                }else if(systemClickTimes == 4){
                    systemClickTimes = 0;
                    layoutSystem.setVisibility(View.GONE);
                }
                break;
            case R.id.tvOperateDustMeterInfo:
                setting.getDustMeterInfo(this);
                break;
            case R.id.btnOperateSaveAutoCal:
                setting.saveAutoCal(swAutoCalEnable.isChecked(),tvAutoCalDate.getText().toString(),etAutoCalInterval.getText().toString());
                break;
            case R.id.btnOperateDustCal:
                setting.calDust(Float.valueOf(etDustTarget.getText().toString()));
                break;
            case R.id.btnOperateSaveServer:
                setting.saveServer(etServerIp.getText().toString(),etServerPort.getText().toString(),etMnCode.getText().toString());
                break;
            case R.id.btnOperateUpdateSoftware:
                dialogFragment = new ProcessDialogFragment();
                dialogFragment.setCancelable(true);
                dialogFragment.show(getFragmentManager(),"DownLoadSoftware");
                system.startDownLoadSoftware(getActivity(),etSoftwareUpdateUrl.getText().toString(),dialogFragment,this);
                break;
            case R.id.btnOperateVideoPreview:
                getActivity().startActivity(getActivity().getPackageManager().getLaunchIntentForPackage("com.mcu.iVMSHD"));
                break;
            case R.id.btnOperateVideoSetting:
                Uri uri = Uri.parse("http://192.168.1.64");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
            case R.id.swOperateAutoCal:
                autoCalEnable = swAutoCalEnable.isChecked();
                if(autoCalEnable){
                    swAutoCalEnable.setChecked(true);
                    tvAutoCalDate.setVisibility(View.VISIBLE);
                    etAutoCalInterval.setVisibility(View.VISIBLE);
                    btnAutoSave.setVisibility(View.VISIBLE);
                }else{
                    swAutoCalEnable.setChecked(false);
                    tvAutoCalDate.setVisibility(View.INVISIBLE);
                    etAutoCalInterval.setVisibility(View.INVISIBLE);
                    btnAutoSave.setVisibility(View.INVISIBLE);
                }
                setting.saveAutoCal(autoCalEnable,tvAutoCalDate.getText().toString(),etAutoCalInterval.getText().toString());
                break;
            case R.id.btnOperateCalMan:
                dialogFragment = new ProcessDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(),"Calibration");
                setting.startDustMeterCal(dialogFragment);
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
        mnCodeString = format.getMnCode();
        handler.sendEmptyMessage(msgShowSetting);
        setting.readSetting(format);
    }

    @Override
    public void showParaK(float para) {
        paraKString = tools.float2String3(para);
        handler.sendEmptyMessage(msgShowParaK);
    }

    @Override
    public void showToast(String string) {
        this.toastString = string;
        handler.sendEmptyMessage(msgShowToast);
    }

    @Override
    public void cancelDialog() {
        handler.sendEmptyMessage(msgCancelDialog);
    }

    @Override
    public void cancelDialogWithToast(String string) {
        toastString = string;
        handler.sendEmptyMessage(msgCancelDialogWithToast);
    }

    @Override
    public void showDustMeterInfo(DustMeterInfoFormat format) {
        dustMeterInfoString = "仪器信息:气泵累计运行时间"+ String.valueOf(format.getPumpTime())+"小时,激光器累计运行时间"+String.valueOf(format.getLaserTime())+"小时.";
        handler.sendEmptyMessage(msgShowDustMeterInfo);
    }

    @Override
    public void onComplete(String string) {
        tvAutoCalDate.setText(setting.calcNextDate(string,etAutoCalInterval.getText().toString()));
    }
}
