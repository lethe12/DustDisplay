package com.grean.dustdisplay.protocol;

import android.util.Log;

import com.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weifeng on 2017/9/13.
 */
/*
* 测试git数据
* */
public class JSON {
    private static final String tag="JSON";
    public static boolean isFrameRight(String content){
        if(content.length() <12){
            return false;
        }

        if(!content.substring(0,2).equals("##")){
            return false;
        }

        if(!content.substring(content.length()-2,content.length()).equals("\r\n")){
            return false;
        }

        String string = content.substring(2,content.indexOf("$$"));
        try{
            int len = Integer.valueOf(string);
            if(len!=(content.length()-12)){
                return false;
            }
        }catch (NumberFormatException e){
            return false;
        }

        return true;
    }

    private static byte[] insertFrame(String content){
        String lenString = String.format("%06d",content.length());
        return ("##"+lenString+"$$"+content+"\r\n").getBytes();
    }


    public static byte[] readRealTimeData() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","realTimeData");
        return insertFrame(object.toString());
    }

    public static byte[] readSetting() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","downloadSetting");
        return insertFrame(object.toString());
    }

    public static byte[] readDustMeterInfo() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterInfo",true);
        return insertFrame(object.toString());
    }

    public static byte[] readHistoryData(long date) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","historyData");
        object.put("Date",date);
        return insertFrame(object.toString());
    }

    public static byte[] exportData(long start,long end) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("ExportData",true);
        object.put("start",start);
        object.put("end",end);
        return insertFrame(object.toString());
    }

    public static byte[] readExportDataProcess() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("ExportDataProcess",true);
        return insertFrame(object.toString());
    }

    public static byte[] readOperateInit() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operateInit");
        return insertFrame(object.toString());
    }

    public static ExportDataInfo getExportDataProcess(JSONObject jsonObject) throws JSONException {
        ExportDataInfo info = new ExportDataInfo();
        info.setProcess(jsonObject.getInt("process"));
        info.setSuccess(jsonObject.getBoolean("result"));
        return info;
    }

    public static DustMeterInfoFormat getDustMeterInfo(JSONObject jsonObject) throws JSONException {
        DustMeterInfoFormat format = new DustMeterInfoFormat();
        format.setPumpTime(jsonObject.getInt("DustMeterPumpTime"));
        format.setLaserTime(jsonObject.getInt("DustMeterLaserTime"));
        return format;
    }

    public static SettingFormat getSetting(JSONObject jsonObject) throws JSONException {
        SettingFormat format = new SettingFormat();
        format.setAutoCalEnable(jsonObject.getBoolean("autoCalEnable"));
        format.setAutoCalDate(jsonObject.getLong("autoCalTime"));
        format.setAutoCalInterval(jsonObject.getLong("autoCalInterval"));
        format.setServerIp(jsonObject.getString("serverIp"));
        format.setServerPort(jsonObject.getInt("serverPort"));
        format.setMnCode(jsonObject.getString("mnCode"));
        format.setParaK((float) jsonObject.getDouble("dustParaK"));
        if(jsonObject.has("dustParaB")){
            format.setParaB((float) jsonObject.getDouble("dustParaB"));
        }
        format.setAlarmDust((float) jsonObject.getDouble("alarmDust"));
        format.setProtocolName(jsonObject.getInt("clientProtocolName"));
        JSONArray array = jsonObject.getJSONArray("clientProtocolNames");
        int size = array.length();
        if(size!=0) {
            String[] names = new String[size];
            for (int i = 0; i < size; i++) {
                names[i] = array.getString(i);
            }
            format.setProtocolNames(names);
        }
        return format;
    }

    public static String getDustName(JSONObject jsonObject) throws JSONException {
        JSONArray array = jsonObject.getJSONArray("dustNames");
        int size = array.length();
        if(size!=0){
            String[] names = new String[size];
            for(int i=0;i<size;i++){
                names[i] = array.getString(i);
            }
            int name = jsonObject.getInt("dustName");
            return names[name];
        }
        return "TSP";
    }

    public static HistoryDataContent getHistoryData(JSONObject jsonObject) throws JSONException {
        HistoryDataContent content = new HistoryDataContent();
        int arraySize = jsonObject.getInt("DateSize");
        if(arraySize>0) {
            JSONArray array = jsonObject.getJSONArray("ArrayData");
            Log.d(tag, "json size=" + String.valueOf(arraySize) + ";array size = " + String.valueOf(array.length()));
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String string = tools.timestamp2string(item.getLong("date"));
                content.addDate(string);
                content.setDust((float) item.getDouble("dust"));
                content.setTemperate((float) item.getDouble("temperature"));
                content.setHumidity((float) item.getDouble("humidity"));
                content.setPressure((float) item.getDouble("pressure"));
                content.setWindForce((float) item.getDouble("windForce"));
                content.setWindDirection((float) item.getDouble("windDirection"));
                content.setNoise((float) item.getDouble("noise"));
                content.addItem(content.putItem());
            }
        }
        return content;
    }

    public static float getOperateDustCal(JSONObject jsonObject) throws JSONException {

        return (float) jsonObject.getDouble("ParaK");
    }

    public static byte[] operateDustCal(float target) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustCal",true);
        object.put("target",target);
        return insertFrame(object.toString());
    }

    public static byte[] operateDustSetParaK(float k,float b) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterSetParaK",true);
        object.put("DustMeterParaK",k);
        object.put("DustMeterParaB",b);
        return insertFrame(object.toString());
    }

    public static byte[] operateDustMeterCal() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCal",true);
        return insertFrame(object.toString());
    }

    public static byte[] operateDustMeterCalZero() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCalZero",true);
        return insertFrame(object.toString());
    }

    public static byte[] operateDustMeterCalResult() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCalResult",true);
        return insertFrame(object.toString());
    }

    public static byte[] operateDustMeterCalProcess() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCalProcess",true);
        return insertFrame(object.toString());
    }

    public static String getDustMeterCalResult(JSONObject jsonObject) throws JSONException {
        boolean bg = jsonObject.getBoolean("DustMeterCalBg");
        boolean span = jsonObject.getBoolean("DustMeterCalSpan");
        String string;
        if(bg){
            string = "校零成功，";
        }else{
            string = "校零失败，";
        }
        if(span){
            string += "校跨成功。";
        }else{
            string += "校跨失败。";
        }
        return string;
    }

    public static DustMeterCalProcessFormat getDustMeterCalProcess(JSONObject jsonObject) throws JSONException {
        DustMeterCalProcessFormat format = new DustMeterCalProcessFormat();
        String string = jsonObject.getString("DustMeterCalInfo");
        int process = jsonObject.getInt("DustMeterCalProcessInt");
        string += "..."+String.valueOf(process)+"%";
        format.setProcess(process);
        format.setString(string);
        return format;
    }

    public static byte[] uploadSetting(SettingFormat format) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","uploadSetting");
        object.put("autoCalEnable",format.isAutoCalEnable());
        object.put("autoCalTime",format.getAutoCalDate());
        object.put("autoCalInterval",format.getAutoCalInterval());
        object.put("serverIp",format.getServerIp());
        object.put("serverPort",format.getServerPort());
        object.put("mnCode",format.getMnCode());
        object.put("alarmDust",format.getAlarmDust());
        object.put("clientProtocolName",format.getProtocolName());
        return insertFrame(object.toString());
    }


    public static RealTimeDataFormat getRealTimeData(JSONObject jsonObject) throws JSONException {
        RealTimeDataFormat format = new RealTimeDataFormat();
        format.setState(jsonObject.getString("state"));
        JSONArray array = jsonObject.getJSONArray("realTimeData");
        if(jsonObject.has("alarm")) {
            format.setAlarm(jsonObject.getBoolean("alarm"));
        }
        if(jsonObject.has("serverConnected")){
            format.setServerConnected(jsonObject.getBoolean("serverConnected"));
        }
        for(int i=0; i <array.length();i++){
            JSONObject item = array.getJSONObject(i);
            if(item.getString("name").equals("dust")){
                format.setDust((float) item.getDouble("value"));
            }else if(item.getString("name").equals("temperature")){
                format.setTemperature((float) item.getDouble("value"));
            }else if(item.getString("name").equals("humidity")){
                format.setHumidity((float) item.getDouble("value"));
            }else if(item.getString("name").equals("pressure")){
                format.setPressure((float) item.getDouble("value"));
            }else if(item.getString("name").equals("windForce")){
                format.setWindForce((float) item.getDouble("value"));
            }else if(item.getString("name").equals("windDirection")){
                format.setWindDirection((float) item.getDouble("value"));
            }else if(item.getString("name").equals("noise")){
                format.setNoise((float) item.getDouble("value"));
            }else if(item.getString("name").equals("value")){
                format.setValue((float) item.getDouble("value"));
            }
        }
        return format;
    }

    public static String getProtocolType(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has("protocolType")){
            return jsonObject.getString("protocolType");
        }else{
            return "";
        }
    }

}
