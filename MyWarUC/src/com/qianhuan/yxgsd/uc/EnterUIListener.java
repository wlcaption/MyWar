package com.qianhuan.yxgsd.uc;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class EnterUIListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_EnterUIListener";
    
    private static EnterUIListener _instance = null;
    
    public static EnterUIListener getInstance() {
        if (_instance == null) {
            _instance = new EnterUIListener();
        }
        return _instance;
    }

    
    @Override
    public void callback(int code, String data) {
        Log.d(TAG, "Received sdk-ui notification: code=" + code + ", data=" + data);
        String msg = null;
        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            msg = "SDK鐣岄潰姝ｅ父閫�鍑�";
            break;

        case UCGameSdkStatusCode.NO_INIT:
            msg = "鎵撳紑SDK鐣岄潰澶辫触锛屾病鏈夊垵濮嬪寲";
            break;
        case UCGameSdkStatusCode.NO_LOGIN:
            msg = "鎵撳紑SDK鐣岄潰澶辫触锛屾病鏈夌櫥褰�";
            break;
        default:
            msg = data;
            break;
        }
        
        /*try{
            //Log.d(TAG, "callback enterUI notification to unity3d, code=" + code + ", msg=" + msg);
            Unity3DCallback.doEnterUICallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, "", e);
        }*/
    }
    
}