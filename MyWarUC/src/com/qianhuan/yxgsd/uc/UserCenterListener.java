package com.qianhuan.yxgsd.uc;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class UserCenterListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_UserCenterListener";
    
    private static UserCenterListener _instance = null;
    
    public static UserCenterListener getInstance() {
        if (_instance == null) {
            _instance = new UserCenterListener();
        }
        return _instance;
    }

    
    @Override
    public void callback(int code, String data) {
        Log.d(TAG, "Received user center notification: code=" + code + ", data=" + data);
        String msg = null;
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            msg = "涔濇父绀惧尯姝ｅ父閫�鍑�";
            break;

        case UCGameSdkStatusCode.NO_INIT:
            msg = "璋冪敤涔濇父绀惧尯澶辫触锛屾病鏈夊垵濮嬪寲";
            break;
        case UCGameSdkStatusCode.NO_LOGIN:
            msg = "璋冪敤涔濇父绀惧尯澶辫触锛屾病鏈夌櫥褰�";
            break;
        default:
            msg = data;
            break;
        }
        
        /*try{
            //Log.d(TAG, "callback user center notification to unity3d, code=" + code + ", msg=" + msg);
            Unity3DCallback.doUserCenterCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }
    
}