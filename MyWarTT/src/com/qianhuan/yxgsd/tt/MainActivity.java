package com.qianhuan.yxgsd.tt;

import java.util.Date;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.tt.net.ReqTask;
import com.qianhua.yxgsd.tt.net.ReqTask.Delegate;
import com.unity3d.player.UnityPlayerActivity;
import com.wett.cooperation.container.SdkCallback;
import com.wett.cooperation.container.TTSDKV2;
import com.wett.cooperation.container.bean.GameInfo;
import com.wett.cooperation.container.bean.PayInfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Process;


/**
 * @author xiaowei
 * 2016-10-17 下午5:26:31
 * wlcaption@qq.com
 * TT SDK接入
 */
	public class MainActivity extends UnityPlayerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameInfo gameInfo = new GameInfo();
		TTSDKV2.getInstance().init(this, gameInfo, false, Configuration.ORIENTATION_LANDSCAPE, new SdkCallback<String>() {
			@Override
			protected boolean onResult(int code, String result) {
				if(code == 0){
					
				}else{
					MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, "");
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		TTSDKV2.getInstance().onResume(this);
		if(TTSDKV2.getInstance().isLogin()){
			TTSDKV2.getInstance().showFloatView(this);
		}
		JPushInterface.onResume(this);
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		TTSDKV2.getInstance().hideFloatView(this);
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		TTSDKV2.getInstance().onRestart(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		TTSDKV2.getInstance().onDestroy(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		TTSDKV2.getInstance().onNewIntent(intent);
	}
	
	@Override
	public void onActivityReenter(int resultCode, Intent data) {
		super.onActivityReenter(resultCode, data);
		TTSDKV2.getInstance().onActivityResult(this, resultCode, resultCode	, data);
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	public void Login(String msg){
		TTSDKV2.getInstance().login(MainActivity.this, new SdkCallback<String>() {
			@Override
			protected boolean onResult(int code, String result) {
				if(code == 0){
					String gameId = TTSDKV2.getInstance().getGameId();
					final String uid    = TTSDKV2.getInstance().getUid();
					String session= TTSDKV2.getInstance().getSession();
					ReqTask reqTask = new ReqTask(new Delegate() {
						
						@Override
						public void execute(String result) {
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, uid);
						}
					}, "&uid=" + uid + "&session=" + session, "http://123.207.146.159/ttsdk/login.php");
					reqTask.execute();
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "");
				}else{
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
				}
				return false;
			}
		});
	}

	public void Pay(String msg){
		String a[] = msg.split("_");
 		PayInfo payInfo = new PayInfo();
		payInfo.setRoleId("123");
		payInfo.setRoleName("wwc");
		payInfo.setBody("钻石");
		payInfo.setCpFee(1);
		payInfo.setCpTradeNo(System.currentTimeMillis() + "");
		payInfo.setExInfo(a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0]);
		payInfo.setSubject("钻石");
		payInfo.setPayMethod(payInfo.PAY_METHOD_ALL);
		payInfo.setCpCallbackUrl("http://123.207.146.159/ttsdk/pay.php");
		payInfo.setChargeDate(new Date().getTime());
		
		TTSDKV2.getInstance().pay(MainActivity.this, payInfo, new SdkCallback<String>() {
			@Override
			protected boolean onResult(int code, String result) {
				if(code == 0){
					MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
				}else{
					MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
				}
				return true;
			}
		});
	}

	public void ExitSDK(){
		TTSDKV2.getInstance().uninit(this, new SdkCallback<String>() {
			
			@SuppressLint("NewApi")
			@Override
			protected boolean onResult(int code, String result) {
				if(code ==0 ){
					finish();
					System.exit(0);
				}else{
					MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "");
				}
				return false;
			}
		});
	}

}
