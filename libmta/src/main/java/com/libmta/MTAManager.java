package com.libmta;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import java.util.Map;


/**
 * @author Gson
 * @date 2021/8/2 15:38
 */
public class MTAManager {

    private static MTAManager mtaManager;

    private static final String TAG = MTAManager.class.getSimpleName();

    private MTAManager() {
    }

    public static MTAManager getInstance() {
        if (mtaManager == null) {
            mtaManager = new MTAManager();
            UpLoad.upLoad.up();
        }
        return mtaManager;
    }

    public void init(@NonNull String appid, @NonNull String baseUrl) {
        if (appid == null || baseUrl == null) {
            throw new IllegalArgumentException("appid or baseUrl can't be null!");
        }
        Config.appid = appid;
        Config.baseUrl = baseUrl;
    }

    public boolean checkInit(){
        if (Config.appid  == null || Config.baseUrl == null) {
            Log.e(TAG,"请先初始化！");
            return false;
        }
        return true;
    }

    public void enterPage(Context context) {
        if (checkInit()){
            String aName = context.getClass().getName();
            Action action = new Action(ActionType.ENTER,aName);
            offer2Upload(action);
        }
    }

    public void leavepPage(Context context) {
        if (checkInit()){
            String aName = context.getClass().getName();
            Action action = new Action(ActionType.LEAVE,aName);
            offer2Upload(action);
        }
    }

    public void up(Map<String,String> params) {
        if (checkInit()){
            offer2Upload(new Action(params));
        }
    }

    public void offer2Upload(Action action){
        UpLoad.actions.offer(action);
    }


}
