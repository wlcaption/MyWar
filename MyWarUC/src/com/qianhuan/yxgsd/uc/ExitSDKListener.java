package com.qianhuan.yxgsd.uc;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class ExitSDKListener implements UCCallbackListener<String> {
 private final static String TAG = "JNI_ExitSDKListener";
    
    private static ExitSDKListener _instance = null;
    
    public static ExitSDKListener getInstance() {
        if (_instance == null) {
            _instance = new ExitSDKListener();
        }
        return _instance;
    }

    
    @Override
    public void callback(int code, String data) {
        Log.d(TAG, "Exit SDK notification: code=" + code + ", data=" + data);
        String msg = null;
        switch (code) {
        case UCGameSdkStatusCode.SDK_EXIT:
            msg = "SDK閫�鍑�";
            MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
            break;
        default:
            msg = data;
            break;
        }
        
        /*try{
            Unity3DCallback.doExitSDKCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, "", e);
        }*/
        
    }

}
