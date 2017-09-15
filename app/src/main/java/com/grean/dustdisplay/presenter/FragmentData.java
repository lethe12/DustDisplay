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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ScrollablePanel.ElementInfo;
import com.ScrollablePanel.HistoryDataPanelAdapter;
import com.ScrollablePanel.ScrollablePanel;
import com.grean.dustdisplay.R;
import com.grean.dustdisplay.model.SearchData;
import com.grean.dustdisplay.protocol.ExportDataInfo;
import com.grean.dustdisplay.protocol.HistoryDataContent;
import com.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentData extends Fragment implements View.OnClickListener ,NotifyDataInfo{
    private static final String tag = "FragmentData";
    private Button btnExportData,btnPageUp,btnPageDown;
    private TextView tvDateStart,tvDateEnd,tvPages;
    private HistoryDataPanelAdapter historyDataPanelAdapter;
    private ScrollablePanel scrollablePanel;
    private String pagesString,toastString;
    private ProcessDialogFragment dialogFragment;
    private StartDialogTimeSelected startDialogTimeSelected;
    private EndDialogTimeSelected endDialogTimeSelected;
    private SearchData searchData;
    private static final String[] elementNames = {"TSP","温度","湿度","气压","风速","风向","噪声"};
    private static final String[] getElementUnit = {"mg/m³","℃","%","hPa","m/s","°","dB"};

    private static final int msgUpdatePages = 1;
    private static final int msgUpdateTable = 2;
    private static final int msgUpExportDataInfo = 3;
    private static final int msgCancelDialogWithToast = 4;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msgUpdatePages:
                    tvPages.setText(pagesString);
                    break;
                case msgUpdateTable:
                    scrollablePanel.setPanelAdapter(historyDataPanelAdapter);
                    dialogFragment.dismiss();
                    break;
                case msgUpExportDataInfo:
                    Toast.makeText(getActivity(),toastString,Toast.LENGTH_SHORT).show();
                    dialogFragment.dismiss();
                    break;
                case msgCancelDialogWithToast:
                    Toast.makeText(getActivity(),toastString,Toast.LENGTH_SHORT).show();
                    dialogFragment.dismiss();
                    break;
                default:
                    break;

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data,container,false);
        scrollablePanel = (ScrollablePanel) view.findViewById(R.id.scrollable_panel);
        initView(view);
        historyDataPanelAdapter = new HistoryDataPanelAdapter();
        //generateTestData(historyDataPanelAdapter);
        setElement(historyDataPanelAdapter);
        searchData = new SearchData(this);
        dialogFragment = new ProcessDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(),"DataInfo");
        long now = tools.nowtime2timestamp();
        tvDateEnd.setText(tools.timestamp2string(now));
        tvDateStart.setText(tools.timestamp2string(now-3600000l));
        searchData.readHistoryData(tools.timestamp2string(now-3600000l),tools.timestamp2string(now));
        startDialogTimeSelected = new StartDialogTimeSelected(this);
        endDialogTimeSelected = new EndDialogTimeSelected(this);
        return view;
    }
    void initView(View v){
        btnExportData = v.findViewById(R.id.btnDataExportData);
        btnPageDown = v.findViewById(R.id.btnDataPageDown);
        btnPageUp = v.findViewById(R.id.btnDataPageUp);
        tvDateStart = v.findViewById(R.id.tvDataStartDate);
        tvDateEnd = v.findViewById(R.id.tvDataEndData);
        tvPages = v.findViewById(R.id.tvDataPages);
        btnPageDown.setOnClickListener(this);
        btnPageUp.setOnClickListener(this);
        btnExportData.setOnClickListener(this);
        tvDateEnd.setOnClickListener(this);
        tvDateStart.setOnClickListener(this);
    }

    private void setElement(HistoryDataPanelAdapter historyDataPanelAdapter){
        List<ElementInfo> element = new ArrayList<>();
        for (int i=0;i<7;i++) {
            ElementInfo info = new ElementInfo();
            info.setName(elementNames[i]);
            info.setUnit(getElementUnit[i]);
            element.add(info);
        }
        historyDataPanelAdapter.setElement(element);
    }


    private void generateTestData(HistoryDataPanelAdapter historyDataPanelAdapter){
        List<String>date = new ArrayList<>();
        for(int i=0;i<60;i++){
            String dateString = String.valueOf(i+100);
            date.add(dateString);
        }
        historyDataPanelAdapter.setDate(date);

        List<ElementInfo> element = new ArrayList<>();
        for(int i=0;i<7;i++){
            ElementInfo info = new ElementInfo();
            info.setName(String.valueOf(200+i));
            info.setUnit("m/s");
            element.add(info);
        }
        historyDataPanelAdapter.setElement(element);

        List<List<String>> data = new ArrayList<>();
        for(int i =0;i<60;i++){
            List<String> item = new ArrayList<>();
            for(int j=0;j<7;j++){
                String string = String.valueOf(i)+" "+String.valueOf(j);
                item.add(string);
            }
            data.add(item);
        }
        historyDataPanelAdapter.setData(data);
    }


    @Override
    public void onClick(View view) {
        Calendar calendar;
        DialogTimeChoose choose;
        switch (view.getId()){
            case R.id.tvDataStartDate:
                calendar = Calendar.getInstance();
                choose = new DialogTimeChoose(getActivity(),"设置起始时间");
                choose.showDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,startDialogTimeSelected);
                break;
            case R.id.tvDataEndData:
                calendar = Calendar.getInstance();
                choose = new DialogTimeChoose(getActivity(),"设置结束时间");
                choose.showDialog(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,endDialogTimeSelected);
                break;
            case R.id.btnDataExportData:
                dialogFragment = new ProcessDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(),"DataInfo");
                searchData.exportData(tvDateStart.getText().toString(),tvDateEnd.getText().toString());
                break;
            case R.id.btnDataPageDown:
                dialogFragment = new ProcessDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(),"DataInfo");
                searchData.pageDown();
                break;
            case R.id.btnDataPageUp:
                dialogFragment = new ProcessDialogFragment();
                dialogFragment.setCancelable(false);
                dialogFragment.show(getFragmentManager(),"DataInfo");
                searchData.pageUp();
                break;
            default:

                break;
        }

    }

    @Override
    public void onExportDataResult(ExportDataInfo info) {
        if(info.isSuccess()){
            toastString = "导出成功！";
        }else{
            toastString = "导出失败！";
        }
        handler.sendEmptyMessage(msgUpExportDataInfo);
    }

    @Override
    public void upDatePages(int now, int all) {
        pagesString = String.valueOf(now)+"/"+String.valueOf(all);
        handler.sendEmptyMessage(msgUpdatePages);
    }

    @Override
    public void noMorePages(String info) {
        toastString = info;
        handler.sendEmptyMessage(msgCancelDialogWithToast);
    }

    @Override
    public void updateHistoryData(HistoryDataContent content) {
        if(content.getSize()>0) {
            historyDataPanelAdapter.setDate(content.getDate());
            historyDataPanelAdapter.setData(content.getData());
        }else{
            historyDataPanelAdapter.setDate(emptyDate());
            historyDataPanelAdapter.setData(emptyData());
        }
        handler.sendEmptyMessage(msgUpdateTable);
    }

    private List<String> emptyDate(){
        List<String> list = new ArrayList<>();
        list.add(searchData.getNowString());
        return list;
    }

    private List<List<String>> emptyData(){
        List<List<String>> list = new ArrayList<>();
        List<String> item= new ArrayList<>();
        for(int i=0;i<7;i++){
            String string = "-";
            item.add(string);
        }
        list.add(item);
        return list;

    }

    @Override
    public void searchHistoryData() {
        dialogFragment = new ProcessDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(),"SearchData");
        searchData.readHistoryData(tvDateStart.getText().toString(),tvDateEnd.getText().toString());
    }

    private class StartDialogTimeSelected implements DialogTimeSelected{
        private NotifyDataInfo dataInfo;

        public StartDialogTimeSelected(NotifyDataInfo dataInfo){
            this.dataInfo = dataInfo;
        }

        @Override
        public void onComplete(String string) {
            Log.d(tag,string);
            long time = tools.string2timestamp(string);
            long now = tools.nowtime2timestamp();
            if(time >= now){
                time = now - 3600000l;
                tvDateStart.setText(tools.timestamp2string(time));
            }else{
                tvDateStart.setText(string);
            }
            dataInfo.searchHistoryData();
        }
    }

    private class EndDialogTimeSelected implements DialogTimeSelected{
        private NotifyDataInfo dataInfo;

        public EndDialogTimeSelected(NotifyDataInfo dataInfo){
            this.dataInfo = dataInfo;
        }

        @Override
        public void onComplete(String string) {
            long time = tools.string2timestamp(string);
            long now = tools.nowtime2timestamp();
            if(time >= now){
                time = now;
                tvDateStart.setText(tools.timestamp2string(time));
            }else{
                tvDateStart.setText(string);
            }
            dataInfo.searchHistoryData();
        }
    }
}
