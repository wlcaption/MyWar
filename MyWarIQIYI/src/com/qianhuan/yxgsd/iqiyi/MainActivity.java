package com.qianhuan.yxgsd.iqiyi;

import java.util.HashMap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.iqiyi.sdk.listener.LoginListener;
import com.iqiyi.sdk.listener.PayListener;
import com.iqiyi.sdk.platform.GamePlatform;
import com.iqiyi.sdk.platform.GamePlatformInitListener;
import com.iqiyi.sdk.platform.GameSDKResultCode;
import com.iqiyi.sdk.platform.GameUser;
import com.qianhua.yxgsd.net.iqiyi.ReqTask;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {
	private static  GamePlatform platform;
	private static String gameId = "5023"; 
    private  String uid;
    private String sign;
    private int time;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		HolaSdkInit("", "", "", "");
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		platform = GamePlatform.getInstance();
		platform.initPlatform(this, gameId, new GamePlatformInitListener() {
 			@Override
 			public void onSuccess() {
 				MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
 				
 				platform.addLoginListener(new LoginListener() {
 					@Override
 					public void logout() {
 					}
 					@Override
 					public void loginResult(int resultCode, GameUser user) {
 						if(resultCode == GameSDKResultCode.SUCCESSLOGIN && user != null){
 							platform.initSliderBar(MainActivity.this);
 							
 							 uid = user.uid;
 							 sign = user.sign;
 							 time = user.timestamp;
 							 ConnectHttp("", "");
 						}
 					}
 					@SuppressLint("NewApi")
					@Override
 					public void exitGame() {
 						finish();
 					}
 				});
 				platform.addPaymentListener(new PayListener() {
 					
 					@Override
 					public void paymentResult(int resultCode) {
 						if(resultCode == GameSDKResultCode.SUCCESSPAYMENT){
 						}
 					}
 					@Override
 					public void leavePlatform() {
 					}
 				});
 			}
 			@Override
 			public void onFail(String arg0) {
 			}
 		});
	}
	
	public void ConnectHttp(String userip, String account_type)
    {
    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey){
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess,uid);
            }
        }, "&uid=" + uid + "&sign=" + sign + "&time" + time , "http://123.207.146.159/ppssdk/login.php");
        getBuoyPrivate.execute();     
    }
	
	
	public void LoginGameRole(String type,String msg) {
		HashMap<String,String> mHash = new HashMap<String,String>();
		String[] mStr =  msg.split("&");		
		
//		for(int i = 0;i<mStr.length;i++)
//		{
//			String[] mStri = mStr[i].split("=");
//			mHash.put(mStri[0], mStri[1]);
//		}
		String[] mStri = mStr[3].split("=");
		mHash.put(mStri[3], mStri[1]);
		
		JSONObject jsonObj = new JSONObject(mHash);
		platform.enterGame(MainActivity.this, jsonObj.toString());
		
//		  mDic.Add("roleId", CharacterRecorder.instance.userId.ToString());
//        mDic.Add("roleName", CharacterRecorder.instance.characterName);
//        mDic.Add("roleLevel", CharacterRecorder.instance.level.ToString());
//        mDic.Add("zoneId", PlayerPrefs.GetString("ServerID"));
//        mDic.Add("zoneName", PlayerPrefs.GetString("ServerName"));
//        mDic.Add("roleCTime", CharacterRecorder.CTime);
//        mDic.Add("roleLevelMTime", Utils.GetNowTimeUTC().ToString());

	}
	
//	public void CreateRole(String type, String msg){
//		platform.createRole(MainActivity.this, "ppsmobile_s1");
//	}
	
	public void Login(String msg){
		
		platform.iqiyiUserLogin(this);
	}
	
	@SuppressLint("NewApi")
	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String a[] = msg.split("_");
				platform.iqiyiPayment(MainActivity.this, Integer.parseInt(a[0]),uid, "ppsmobile_s1",  a[2] + "-" + a[1] +"-" + a[5] + "-" + a[0]);//当前acitivity,固定充值金额，角色id,服务器所在id，扩展字段
			}
		});
	}
	
	public void ExitSDK(){
		platform.iqiyiUserLogout(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
}
