package com.qianhuan.yxgsd.uc;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;

public class LogoutNotifyListener implements UCCallbackListener<String>{

 private final static String TAG = "JNI_LogoutNotifyListener";
    
    private static LogoutNotifyListener _instance = null;
    
    public static LogoutNotifyListener getInstance() {
        if (_instance == null) {
            _instance = new LogoutNotifyListener();
        }
        return _instance;
    }

    
    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "received logout notification: msg=" + msg);
        /*try {
            Unity3DCallback.doLogoutCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }

}
