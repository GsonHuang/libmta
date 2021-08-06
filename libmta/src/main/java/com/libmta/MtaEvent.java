package com.libmta;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Gson
 * @date 2021/8/2 16:02
 */
public class MtaEvent {

    private String ac;
    //PV进入页面 HB定时器或者离开页面，dac=interval，，，详情dac表示id
    private String dac;
    private String uid;
    //表示哪个页面
    private String path;
    private String paramsType = "1";

    private String appid;
    private String baseUrl;
    private int interval;


    //设备分辨率
    private String dpr;
    private String api = "/mta/v2/put";
    private String brand = Build.BRAND;
    private String plat = "android";
    private int platVersion = Build.VERSION.SDK_INT;
    private String sign;
    //随机字符串，启动到离开应用之间操作的表示id
    private String sid;
    //用户id
    private long ts = System.currentTimeMillis() / 1000;
    private String uuid;

    private JSONObject customInfo;

    public String getActionContent() {
        JSONObject contennt = new JSONObject();

        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String name = declaredField.getName();
            Class<?> type = declaredField.getType();
            try {
                if (type == String.class) {
                    String value = (String) declaredField.get(this);
                    contennt.put(name, value);
                } else if (type == int.class) {
                    int value = declaredField.getInt(this);
                    contennt.put(name, value);
                } else if (type == long.class) {
                    long value = declaredField.getLong(this);
                    contennt.put(name, value);
                }else if (type == JSONObject.class) {
                    if (customInfo != null) {
                        contennt.put("content", customInfo);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String contentStr = contennt.toString();
        return contentStr;
    }


    public void setCustomInfo(JSONObject customInfo) {
        this.customInfo = customInfo;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public void setDac(String dac) {
        this.dac = dac;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setParamsType(String paramsType) {
        this.paramsType = paramsType;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAc() {
        return ac;
    }

    public String getApi() {
        return api;
    }

    public String getAppid() {
        return appid;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBrand() {
        return brand;
    }

    public String getDac() {
        return dac;
    }

    public String getDpr() {
        return dpr;
    }

    public int getInterval() {
        return interval;
    }

    public String getParamsType() {
        return paramsType;
    }

    public String getPath() {
        return path;
    }

    public String getPlat() {
        return plat;
    }

    public int getPlatVersion() {
        return platVersion;
    }

    public String getSign() {
        return sign;
    }

    public String getSid() {
        return sid;
    }

    public long getTs() {
        return ts;
    }

    public String getUid() {
        return uid;
    }

    public String getUuid() {
        return uuid;
    }

    protected void setApi(String api) {
        this.api = api;
    }

    protected void setAppid(String appid) {
        this.appid = appid;
    }

    protected void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    protected void setBrand(String brand) {
        this.brand = brand;
    }

    protected void setDpr(String dpr) {
        this.dpr = dpr;
    }

    protected void setPlat(String plat) {
        this.plat = plat;
    }

    protected void setPlatVersion(int platVersion) {
        this.platVersion = platVersion;
    }

    protected void setSign(String sign) {
        this.sign = sign;
    }

    protected void setSid(String sid) {
        this.sid = sid;
    }

    protected void setTs(long ts) {
        this.ts = ts;
    }

    protected void setUid(String uid) {
        this.uid = uid;
    }

    protected void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
