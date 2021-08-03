package com.libmta;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Map;

/**
 * @author Gson
 * @date 2021/8/2 16:02
 */
public class Action {
    private ActionType mActionType;

    private Map mParms;

    private String mPageName;

    public Action(ActionType actionType,String pageName){
        mActionType = actionType;
        mParms = null;
        mPageName = pageName;
    }

    public Action(Map<String,String> params){
        mActionType = ActionType.UP;
        mParms = params;
    }

    public String getActionContent(){
        String contennt = null;
        if (mParms!=null){
            JSONObject jsonObject = new JSONObject(mParms);
            contennt = jsonObject.toString();
        } else {
            if (mActionType == ActionType.ENTER){
                //进入页面
            }else {
                //出页面
            }
        }
        if (contennt == null){
            throw new NullPointerException("mta上报信息转化失败！");
        }
        return contennt.toString();
    }


}
