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
