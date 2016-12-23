package com.qianhuan.yxgsd.uc;

import org.json.JSONObject;

import com.unity3d.player.UnityPlayer;

import android.util.Log;


/**
 * 瀹氫箟 Java 鍚� Unity3D 鍙戦�佹墽琛岀粨鏋滄柟娉曘��
 *
 */
public class Unity3DCallback {
    private static final String TAG = "Unity3DCallback";

    //瀹氫箟鍥炶皟浜嬩欢绫诲瀷
    public static final String CALLBACKTYPE_InitSDK              = "InitSDK";
    public static final String CALLBACKTYPE_Login                = "Login";
    public static final String CALLBACKTYPE_Logout               = "Logout";
    public static final String CALLBACKTYPE_UserCenter           = "UserCenter";
    public static final String CALLBACKTYPE_EnterUI              = "EnterUI";
    public static final String CALLBACKTYPE_Pay                  = "Pay";
    public static final String CALLBACKTYPE_ExitSDK              = "ExitSDK";

    
    public static final String CALLBACKTYPE_GameUserAuthentication = "GameUserAuthentication";
    
    public static void unity3dSendMessage(String json) {
        Log.d(TAG, "send message to Unity3D, message data=" + json.toString() );
        UnityPlayer.UnitySendMessage("Main Camera", "OnUCGameSdkCallback", json);
    }
    
    public static void callback(String callbackType, int code, Object data) {
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("callbackType", callbackType);
            jobj.put("code", code);
            jobj.put("data", data);
            
            unity3dSendMessage(jobj.toString());
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }
    
    public static void doInitResultCallback(int code, String msg) {
        callback(CALLBACKTYPE_InitSDK, code, msg);
    }
    
    public static void doLoginResultCallback(int code, String msg) {
        callback(CALLBACKTYPE_Login, code, msg);
    }
    
    public static void doLogoutCallback(int code, String msg) {
        callback(CALLBACKTYPE_Logout, code, msg);
    }
    
    public static void doPayCallback(int code, String orderId, float orderAmount, int payWayId, String payWayName) {
        try {
            JSONObject jdata = new JSONObject();
            jdata.put("orderId", orderId);
            jdata.put("orderAmount", orderAmount);
            jdata.put("payWayId", payWayId);
            jdata.put("payWayName", payWayName);
            
            callback(CALLBACKTYPE_Pay, code, jdata);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }
    
    public static void doUserCenterCallback(int code, String msg) {
        callback(CALLBACKTYPE_UserCenter, code, msg);
    }
    
    public static void doEnterUICallback(int code, String msg) {
        callback(CALLBACKTYPE_EnterUI, code, msg);
    }
    
    public static void doExitSDKCallback(int code, String msg) {
        callback(CALLBACKTYPE_ExitSDK, code, msg);
    }

    
}
