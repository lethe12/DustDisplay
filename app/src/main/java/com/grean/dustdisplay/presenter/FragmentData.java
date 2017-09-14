package com.grean.dustdisplay.presenter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ScrollablePanel.DateInfo;
import com.ScrollablePanel.ElementInfo;
import com.ScrollablePanel.HistoryDataPanelAdapter;
import com.ScrollablePanel.OrderInfo;
import com.ScrollablePanel.RoomInfo;
import com.ScrollablePanel.ScrollablePanel;
import com.ScrollablePanel.ScrollablePanelAdapter;
import com.grean.dustdisplay.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by weifeng on 2017/9/12.
 */

public class FragmentData extends Fragment{
    /*public static final SimpleDateFormat DAY_UI_MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");
    public static final SimpleDateFormat WEEK_FORMAT = new SimpleDateFormat("EEE", Locale.US);*/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data,container,false);
        final ScrollablePanel scrollablePanel = (ScrollablePanel) view.findViewById(R.id.scrollable_panel);
        final HistoryDataPanelAdapter historyDataPanelAdapter = new HistoryDataPanelAdapter();
        generateTestData(historyDataPanelAdapter);
        scrollablePanel.setPanelAdapter(historyDataPanelAdapter);
        /*final ScrollablePanelAdapter scrollablePanelAdapter = new ScrollablePanelAdapter();
        generateTestData(scrollablePanelAdapter);
        scrollablePanel.setPanelAdapter(scrollablePanelAdapter);*/
        return view;
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

    /*private void generateTestData(ScrollablePanelAdapter scrollablePanelAdapter) {
        List<RoomInfo> roomInfoList = new ArrayList<>();
        //插入行信息
        for (int i = 0; i < 6; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomType("SUPK");
            roomInfo.setRoomId(i);
            roomInfo.setRoomName("20" + i);
            roomInfoList.add(roomInfo);
        }
        for (int i = 6; i < 30; i++) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomType("Standard");
            roomInfo.setRoomId(i);
            roomInfo.setRoomName("30" + i);
            roomInfoList.add(roomInfo);
        }
        scrollablePanelAdapter.setRoomInfoList(roomInfoList);

        //第一行标题信息
        List<DateInfo> dateInfoList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 14; i++) {
            DateInfo dateInfo = new DateInfo();
            String date = DAY_UI_MONTH_DAY_FORMAT.format(calendar.getTime());
            String week = WEEK_FORMAT.format(calendar.getTime());
            dateInfo.setDate(date);
            dateInfo.setWeek(week);
            dateInfoList.add(dateInfo);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        scrollablePanelAdapter.setDateInfoList(dateInfoList);

        List<List<OrderInfo>> ordersList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            List<OrderInfo> orderInfoList = new ArrayList<>();
            for (int j = 0; j < 14; j++) {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setGuestName("NO." + i + j);
                orderInfo.setBegin(true);
                orderInfo.setStatus(OrderInfo.Status.randomStatus());
                if (orderInfoList.size() > 0) {
                    OrderInfo lastOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
                    if (orderInfo.getStatus().ordinal() == lastOrderInfo.getStatus().ordinal()) {
                        orderInfo.setId(lastOrderInfo.getId());
                        orderInfo.setBegin(false);
                        orderInfo.setGuestName("");
                    } else {
                        if (new Random().nextBoolean()) {
                            orderInfo.setStatus(OrderInfo.Status.BLANK);
                        }
                    }
                }
                orderInfoList.add(orderInfo);
            }
            ordersList.add(orderInfoList);
        }
        scrollablePanelAdapter.setOrdersList(ordersList);
    }*/
}
