package com.grean.dustdisplay.protocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weifeng on 2017/9/13.
 */

public class JSON {
    public static byte[] readRealTimeData() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","realTimeData");
        return object.toString().getBytes();
    }

    public static byte[] readSetting() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","downloadSetting");
        return object.toString().getBytes();
    }

    public static SettingFormat getSetting(JSONObject jsonObject) throws JSONException {
        SettingFormat format = new SettingFormat();
        format.setAutoCalEnable(jsonObject.getBoolean("autoCalEnable"));
        format.setAutoCalDate(jsonObject.getLong("autoCalTime"));
        format.setAutoCalInterval(jsonObject.getLong("autoCalInterval"));
        format.setServerIp(jsonObject.getString("serverIp"));
        format.setServerPort(jsonObject.getInt("serverPort"));
        format.setParaK((float) jsonObject.getDouble("dustParaK"));
        return format;
    }

    public static float getOperateDustCal(JSONObject jsonObject) throws JSONException {

        return (float) jsonObject.getDouble("ParaK");
    }

    public static byte[] operateDustCal(float target) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustCal",true);
        object.put("target",target);
        return object.toString().getBytes();
    }

    public static byte[] operateDustMeterCal() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCal",true);
        return object.toString().getBytes();
    }

    public static byte[] operateDustMeterCalResult() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCalResult",true);
        return object.toString().getBytes();
    }

    public static byte[] operateDustMeterCalProcess() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("protocolType","operate");
        object.put("DustMeterCalProcess",true);
        return object.toString().getBytes();
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
        return object.toString().getBytes();
    }


    public static RealTimeDataFormat getRealTimeData(JSONObject jsonObject) throws JSONException {
        RealTimeDataFormat format = new RealTimeDataFormat();
        format.setState(jsonObject.getString("state"));
        JSONArray array = jsonObject.getJSONArray("realTimeData");
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
