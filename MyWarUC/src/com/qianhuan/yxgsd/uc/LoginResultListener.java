package com.qianhuan.yxgsd.uc;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhuan.yxgsd.uc.net.ReqTask;
import com.unity3d.player.UnityPlayer;

import android.util.Log;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

public class LoginResultListener implements UCCallbackListener<String> {
    private final static String TAG = "JNI_LoginResultListener";
    
    private static LoginResultListener _instance = null;
    
    public static LoginResultListener getInstance() {
        if (_instance == null) {
            _instance = new LoginResultListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, String msg) {
        Log.d(TAG, "UCGameSDK login result: code=" + code + ", msg=" + msg);

        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            String sid = UCGameSDK.getSid();
            ConnectHttp(sid);
//        	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "sid:"+ sid);
            break;
        case UCGameSdkStatusCode.NO_INIT:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
            break;
        case UCGameSdkStatusCode.LOGIN_EXIT:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
            break;
        default:
            break;
        }

        /*try {
            //Log.d(TAG, "callback login result to unity3d, code=" + code + ", msg=" + msg);
            Unity3DCallback.doLoginResultCallback(code, msg);
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }*/
    }
    
    
    public void ConnectHttp(String sid)
    {
    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      	
            	buoyPrivateKey = buoyPrivateKey.trim();
            	if(buoyPrivateKey.length()>0){
            		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","CCCCCCCCCCCCC" + buoyPrivateKey);
            		MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess,buoyPrivateKey);
            	}else{
            		MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
            	}
            }
            //http://139.196.14.52/ucsdk/login.php
            //http://123.207.146.159/ucsdk/login.php
        }, "sid=" + sid, "http://123.207.146.159/ucsdk/login.php");
        getBuoyPrivate.execute();     
    }
}