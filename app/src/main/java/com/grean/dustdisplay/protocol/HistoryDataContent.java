package com.grean.dustdisplay.protocol;

import com.tools;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weifeng on 2017/9/15.
 */

public class HistoryDataContent {
    private List<String> date = new ArrayList<String>();
    private List<List<String>> data = new ArrayList<>();
    private float dust,temperate,humidity,pressure,windForce,windDirection,noise;

    public int getSize(){
        return date.size();
    }

    public List<String> getDate() {
        return date;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setDust(float dust) {
        this.dust = dust;
    }

    public void setTemperate(float temperate) {
        this.temperate = temperate;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public void setWindForce(float windForce) {
        this.windForce = windForce;
    }

    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }

    public List<String> putItem(){
        List<String> list = new ArrayList<>();
        list.add(tools.float2String3(dust));
        list.add(tools.float2String3(temperate));
        list.add(tools.float2String3(humidity));
        list.add(tools.float2String3(pressure));
        list.add(tools.float2String3(windForce));
        list.add(tools.float2String0(windDirection));
        list.add(tools.float2String3(noise));
        return list;
    }

    public void addItem(List<String> list){
        data.add(list);
    }

    public void addDate(String string){
        date.add(string);
    }

    public void clearAll(){
        data.clear();
        date.clear();
    }

}
