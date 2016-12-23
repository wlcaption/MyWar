package com.qianhuan.yxgsd.uc;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class InitResultListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_InitResultListener";
    
    private static InitResultListener _instance = null;
    
    public static InitResultListener getInstance() {
        if (_instance == null) {
            _instance = new InitResultListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "UCGameSDK init result: code=" + code + ", msg=" + msg + "");
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, msg);
            // 鍒濆鍖栨垚鍔�
            break;
        case UCGameSdkStatusCode.INIT_FAIL:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, msg);
            break;
        case UCGameSdkStatusCode.LOGIN_EXIT:
            //addOutputResult("login-exit", String.valueOf(code));
            break;
        default:
            //addOutputResult("login-fail", String.valueOf(code));
            break;
        }

        /*try {
            //Log.d(TAG, "callback init notification to unity3d, code=" + code + ", msg=" + msg);
            Unity3DCallback.doInitResultCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }

}