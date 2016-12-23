package com.qianhuan.yxgsd.wdj;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.wdj.ReqTask;
import com.qianhua.yxgsd.net.wdj.ReqTask.Delegate;
import com.unity3d.player.UnityPlayerActivity;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi.ExitCallback;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLoginFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnPayFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.LoginFinishType;
import com.wandoujia.mariosdk.plugin.api.model.model.PayResult;
import com.wandoujia.mariosdk.plugin.api.model.model.UnverifiedPlayer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


/**
 * @author xiaowei
 * 2016-10-11 下午12:25:37
 *
 */
public class MainActivity extends UnityPlayerActivity {
	
	private WandouGamesApi wandouGamesApi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
		wandouGamesApi.init(this);
		wandouGamesApi.onCreate(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		wandouGamesApi.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		wandouGamesApi.onPause(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		wandouGamesApi.onStop(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		wandouGamesApi.onStop(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		wandouGamesApi.onNewIntent(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		wandouGamesApi.onActivityResult(this, requestCode, resultCode, data);
	}
	
	//初始化操作
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	//登录操作
	@SuppressLint("NewApi")
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				wandouGamesApi.login(new OnLoginFinishedListener() {
					@Override
					public void onLoginFinished(LoginFinishType loginType, UnverifiedPlayer userInfo) {
						final String uid   = userInfo.getId();
						String token = userInfo.getToken();
						ReqTask reqTask = new ReqTask(new Delegate() {
							@Override
							public void execute(String result) {
								MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, uid);
							}
						}, "&uid=" + uid + "&token=" + token, "http://123.207.146.159/peapodssdk/login.php");
						reqTask.execute();
					}
				});
			}
		});
	}
	
	//支付操作
	@SuppressLint("NewApi")
	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				String a[] = msg.split("_");
				String productDesc  = "钻石"; //商品名称
				long moneyInFen     = 100;   //商品价格(分)
				long timeCount      = 1;     //商品数量 
				String OuttradeNo   = System.currentTimeMillis() + "|" + a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0] ; //自定义订单号
				wandouGamesApi.pay(MainActivity.this, productDesc, moneyInFen, timeCount, OuttradeNo, new OnPayFinishedListener() {
					
					@Override
					public void onPaySuccess(PayResult payResult) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
					}
					
					@Override
					public void onPayFail(PayResult payResult) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed,  "");
					}
				});
			}
		});
	}
	
	//退出游戏
	@SuppressLint("NewApi")
	public void ExitSDK(){
//		wandouGamesApi.exit(MainActivity.this, new ExitCallback() {
//			
//			@Override
//			public void onGameExit() {
//				
//			}
//			
//			@Override
//			public void onChannelExit() {
//				
//			}
//		}, true);
//	}
			this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        builder.setTitle("退出");
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
