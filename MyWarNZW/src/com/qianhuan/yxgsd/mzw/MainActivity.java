package com.qianhuan.yxgsd.mzw;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.muzhiwan.sdk.core.MzwSdkController;
import com.muzhiwan.sdk.core.callback.MzwInitCallback;
import com.muzhiwan.sdk.core.callback.MzwLoignCallback;
import com.muzhiwan.sdk.core.callback.MzwPayCallback;
import com.muzhiwan.sdk.service.MzwOrder;
import com.qianhua.yxgsd.net.mzw.ReqTask;
import com.qianhua.yxgsd.net.mzw.ReqTask.Delegate;
import com.unity3d.player.UnityPlayerActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends UnityPlayerActivity {

	private Handler mHandler,pHandler;
	private static final int MSG_INIT  = 0x01;
	private static final int MSG_LOGIN = 0x02;
	private static final int MSG_PAY   = 0x03;
	private MzwOrder order;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_INIT:
					//初始化失败
					if(msg.arg1 == 0){
					MessageHandle.resultCallBack(Util.User, UserWrapper.InitFailed, "");
					}else{
						MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
					}
					break;
				case MSG_LOGIN:
					if(msg.arg1 == 1){
					}else if(msg.arg1 == 6){
						Login("");
					}else if(msg.arg1 ==4){
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
					}else{
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
					}
					break;
				}
			}
		};
	}
	
	@Override
	public void onBackPressed() {
		MzwSdkController.getInstance().destory();
		super.onBackPressed();
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		MzwSdkController.getInstance().init(MainActivity.this,MzwSdkController.ORIENTATION_HORIZONTAL , new MzwInitCallback() {
			
			@Override
			public void onResult(int code, String msg) {
				Message message = new Message();
				message.what = MSG_INIT;
				message.arg1 = code;
				mHandler.handleMessage(message);
			}
		});
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
	
	public void Login(String msg){
		MzwSdkController.getInstance().doLogin(new MzwLoignCallback() {
			@Override
			public void onResult(int code, String msg) {
				Message message = new Message();
				message.what = MSG_LOGIN;
				message.arg1 = code;
				mHandler.handleMessage(message);
				ReqTask reqTask = new ReqTask(new Delegate() {
					@Override
					public void execute(String result) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "");
					}
				}, "&access_token=" + msg, "http://123.207.146.159/mzwsdk/login.php");
				reqTask.execute();
			}
		});
	}
	
	
	@SuppressLint("NewApi")
	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String a[] = msg.split("_");
				double priceValue = 1;
				order = new MzwOrder();
				order.setMoney(priceValue);
				order.setProductname("充值钻石");
				order.setProductdesc("充值钻石");
				order.setExtern(a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0]);
				MzwSdkController.getInstance().doPay(order, new MzwPayCallback() {
					@Override
					public void onResult(int code, MzwOrder order) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
					}
				});
			}
		});
		
	}

	@SuppressLint("NewApi")
	public void ExitSDK(){
//		MzwSdkController.getInstance().doLogout(); //登出
			this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        builder.setTitle("退出");
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            @SuppressLint("NewApi")
					public void onClick(DialogInterface dialog, int whichButton) {
		                //退出apk
		                dialog.dismiss();
		                MainActivity.this.finish();
		            }
		        });
		        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                // dialog隐藏
		                dialog.dismiss();
		            }
		        });
		        builder.create().show();
			}
		});
	}
}
