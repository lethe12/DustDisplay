package com.grean.dustdisplay;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grean.dustdisplay.model.LoadConfig;
import com.grean.dustdisplay.presenter.FragmentData;
import com.grean.dustdisplay.presenter.FragmentOperate;
import com.grean.dustdisplay.presenter.FragmentRealTime;
import com.grean.dustdisplay.protocol.ProtocolLibs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SocketClientCtrl{
    private static final String tag="MainActivity";
    private View layoutRealTime,layoutOperate,layoutData;
    private FragmentRealTime fragmentRealTime;
    private FragmentOperate fragmentOperate;
    private FragmentData fragmentData;
    private FragmentManager fragmentManager;
    private LoadConfig loadConfig;
    private String serverIp;
    private int serverPort;
    private TextView tvTab1,tvTab2,tvTab3;
    private ImageView ivTab1,ivTab2,ivTab3;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MainBroadcast");
        registerReceiver(broadcastReceiver,intentFilter);
        loadConfig = new LoadConfig(this);
        loadConfig.readConfig();
        serverIp = loadConfig.getServerIp();
        serverPort = loadConfig.getServerPort();
        SocketTask.getInstance().startSocketHeart(serverIp,serverPort,this,this, ProtocolLibs.getInstance().getClientProtocol());
    }

    private void initView(){
        layoutData = findViewById(R.id.layoutData);
        layoutOperate = findViewById(R.id.layoutSetting);
        layoutRealTime = findViewById(R.id.layoutRealTime);
        layoutData.setOnClickListener(this);
        layoutRealTime.setOnClickListener(this);
        layoutOperate.setOnClickListener(this);
        tvTab1 = (TextView) findViewById(R.id.tvMenuRealTime);
        tvTab2 = (TextView) findViewById(R.id.tvMenuSetting);
        tvTab3 = (TextView) findViewById(R.id.tvMenuData);
        ivTab1 = (ImageView) findViewById(R.id.ivMenuRealTime);
        ivTab2 = (ImageView) findViewById(R.id.ivMenuSetting);
        ivTab3 = (ImageView) findViewById(R.id.ivMenuData);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    /**
     * 清除选中状态
     */
    private void clearSelection(){
        /*layoutData.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_bright));
        layoutOperate.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_bright));
        layoutRealTime.setBackgroundColor(Resources.getSystem().getColor(android.R.color.holo_blue_bright));*/
        tvTab1.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
        tvTab2.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
        tvTab3.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
        ivTab1.setImageDrawable(getResources().getDrawable(R.drawable.real_time_unselected));
        ivTab2.setImageDrawable(getResources().getDrawable(R.drawable.setting_unselected));
        ivTab3.setImageDrawable(getResources().getDrawable(R.drawable.data_unselected));
    }

    private void selectOne(int index){
        switch (index){
            case 0:
                tvTab1.setTextColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));
                ivTab1.setImageDrawable(getResources().getDrawable(R.drawable.real_time_selected));
                break;
            case 1:
                tvTab2.setTextColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));
                ivTab2.setImageDrawable(getResources().getDrawable(R.drawable.setting_selected));
                break;
            case 2:
                tvTab3.setTextColor(Resources.getSystem().getColor(android.R.color.holo_blue_light));
                ivTab3.setImageDrawable(getResources().getDrawable(R.drawable.data_selected));
                break;
            default:

                break;
        }
    }

    private void  hideFragment(FragmentTransaction transaction){
        if(fragmentData!=null){
            transaction.hide(fragmentData);
        }
        if(fragmentOperate!=null){
            transaction.hide(fragmentOperate);
            //transaction.remove(fragmentOperate);
        }
        if(fragmentRealTime!=null){
            transaction.hide(fragmentRealTime);
        }
    }

    private void setTabSelection(int index){
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        selectOne(index);
        switch (index){
            case 0:
            default:
                if(fragmentRealTime == null){
                    fragmentRealTime = new FragmentRealTime();
                    transaction.add(R.id.content,fragmentRealTime).commit();
                }else {
                    if(fragmentRealTime.isAdded()){
                        transaction.show(fragmentRealTime).commit();
                    }else{
                        transaction.add(R.id.content,fragmentRealTime).commit();
                    }
                }
                break;
            case 1:
                layoutOperate.setBackgroundColor(Resources.getSystem().getColor(android.R.color.background_light));
                if(fragmentOperate == null){
                    fragmentOperate = new FragmentOperate();
                    transaction.add(R.id.content,fragmentOperate).commit();
                }else {
                    if(fragmentOperate.isAdded()){
                        transaction.show(fragmentOperate).commit();
                    }else{
                        transaction.add(R.id.content,fragmentOperate).commit();
                    }
                }
                break;
            case 2:
                layoutData.setBackgroundColor(Resources.getSystem().getColor(android.R.color.background_light));
                if(fragmentData == null){
                    fragmentData = new FragmentData();
                    transaction.add(R.id.content,fragmentData).commit();
                }else {
                    if(fragmentData.isAdded()){
                        transaction.show(fragmentData).commit();
                    }else{
                        transaction.add(R.id.content,fragmentData).commit();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutData:
                setTabSelection(2);
                break;
            case R.id.layoutRealTime:
                setTabSelection(0);
                break;
            case R.id.layoutSetting:
                setTabSelection(1);
                break;
            default:

                break;

        }
    }

    @Override
    public void endHeartThread() {
        Log.d(tag,"end socket");
    }
}
