package com.libmta;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;


/**
 * @author Gson
 * @date 2021/8/2 15:38
 */
public class MTAManager {

    private static MTAManager mtaManager;

    private static final String TAG = MTAManager.class.getSimpleName();

    List<String> mEventActions;

    private MTAManager() {
    }

    public static MTAManager getInstance() {
        if (mtaManager == null) {
            mtaManager = new MTAManager();
            UpLoad.upLoad.up();
        }
        return mtaManager;
    }

    public void init(Context context, @NonNull String appid, @NonNull String baseUrl) {
        if (appid == null || baseUrl == null) {
            throw new IllegalArgumentException("appid or baseUrl can't be null!");
        }

        setConfig(appid, baseUrl, context);

    }

    private void setConfig(String appid, String baseUrl, Context context) {
        Config.appid = appid;
        Config.baseUrl = baseUrl;
        Config.density = Utils.getDensity(context) + "";
        Config.androidId = Utils.getAndroidId(context);
        Config.sid = Utils.getSid();
    }

    public void init(Context context, @NonNull String appid, @NonNull String baseUrl, ComfirmAction comfirmAction) {
        if (appid == null || baseUrl == null) {
            throw new IllegalArgumentException("appid or baseUrl can't be null!");
        }
        if (comfirmAction == null) {
            throw new IllegalArgumentException("需定义上报事件类型列表");
        }
        mEventActions = comfirmAction.setActions();
        if (mEventActions == null || mEventActions.size() == 0) {
            throw new IllegalArgumentException("上报事件类型列表不能为空");
        }

        setConfig(appid, baseUrl, context);

    }

    private int mTnterval = 120;

    private int getInterval() {
        return mTnterval;
    }

    public void setInterval(int intervalSeconds) {
        mTnterval = intervalSeconds;
    }

    public interface ComfirmAction {
        List<String> setActions();
    }

    private boolean checkInit() {
        if (Config.appid == null || Config.baseUrl == null) {
            Log.e(TAG, "请先初始化！");
            return false;
        }
        return true;
    }

    private Handler handler = new Handler();
    private Runnable runnable = null;

    public void enterPage(final String path) {
        if (checkInit()) {
            MtaEvent mtaEvent = makeMtaEvent("PV", path);
            pageUp(mtaEvent);

            runnable = new Runnable() {
                @Override
                public void run() {
                    pageUp(makeMtaEvent("HB", path));
                    handler.postDelayed(this, getInterval() * 1000);
                }
            };
        }
    }

    private void pageUp(MtaEvent mtaEvent) {
        offer2Upload(mtaEvent);
    }


    public void leavepPage(String path) {
        if (checkInit()) {
            pageUp(makeMtaEvent("HB", path));
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    public void up(MtaEvent mtaEvent) {
        if (checkInit()) {
            if (mtaEvent.getAc() == null) {
                throw new NullPointerException("埋点事件类型ac不能为空");
            }
            mtaEvent.setAppid(Config.appid);
            mtaEvent.setBaseUrl(Config.baseUrl);
            mtaEvent.setApi(Config.api);
            mtaEvent.setDpr(Config.density);
            mtaEvent.setUuid(Config.androidId);
            mtaEvent.setSid(Config.sid);
            mtaEvent.setSign(Utils.getSign(Config.appid, mtaEvent.getAc(), mtaEvent.getTs() + ""));
            mtaEvent.setInterval(mTnterval);

            offer2Upload(mtaEvent);
        }
    }

    private void offer2Upload(MtaEvent mtaEvent) {
        if (mEventActions != null) {
            if (mEventActions.contains(mtaEvent.getAc())) {
                UpLoad.mtaEvents.offer(mtaEvent);
            } else {
                throw new IllegalArgumentException("事件类型ac定义有误，请检查！");
            }
        } else {
            UpLoad.mtaEvents.offer(mtaEvent);
        }
    }

    private MtaEvent makeMtaEvent(String ac, String path) {
        MtaEvent mtaEvent = new MtaEvent();
        mtaEvent.setAppid(Config.appid);
        mtaEvent.setBaseUrl(Config.baseUrl);
        mtaEvent.setApi(Config.api);
        mtaEvent.setDpr(Config.density);
        mtaEvent.setUuid(Config.androidId);
        mtaEvent.setSid(Config.sid);
        mtaEvent.setDac(mtaEvent.getInterval() + "");
        mtaEvent.setAc(ac);
        mtaEvent.setPath(path);
        mtaEvent.setSign(Utils.getSign(Config.appid, ac, mtaEvent.getTs() + ""));
        mtaEvent.setInterval(mTnterval);

        return mtaEvent;
    }


}
