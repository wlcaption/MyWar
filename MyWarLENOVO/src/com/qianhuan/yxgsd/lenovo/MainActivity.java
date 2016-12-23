package com.qianhuan.yxgsd.lenovo;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.lenovo.lsf.gamesdk.GamePayRequest;
import com.lenovo.lsf.gamesdk.IAuthResult;
import com.lenovo.lsf.gamesdk.IPayResult;
import com.lenovo.lsf.gamesdk.LenovoGameApi;
import com.qianhua.yxgsd.net.lenovo.ReqTask;
import com.qianhua.yxgsd.net.lenovo.ReqTask.Delegate;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author xiaowei
 * 2016-10-11 下午2:12:12
 *
 */
public class MainActivity extends UnityPlayerActivity {
	private static final int MSG_LOGIN_AUTO_ONEKEY = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LenovoGameApi.doInit(MainActivity.this, Config.appid);
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
	
	//初始化操作
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	//登录操作
	public void Login(String msg){
		LenovoGameApi.doAutoLogin(MainActivity.this, new IAuthResult() {
			@Override
			public void onFinished(boolean ret, String data) {
				Message message = new Message();
				message.what = MSG_LOGIN_AUTO_ONEKEY;
				message.obj = data;
				if(ret){
					myHandler.sendMessage(message);
					ReqTask reqTask = new ReqTask(new Delegate() {
						@Override
						public void execute(String result) {
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "");
						}
					}, "&token=" + data, "http://123.207.146.159/lenovosdk/login.php");
					reqTask.execute();
				}else{
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
				}
			}
		});
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_LOGIN_AUTO_ONEKEY:
				break;
			default:
				break;
			}
		};
	};
	
	@SuppressLint("NewApi")
	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				String a[] = msg.split("_");
				GamePayRequest payRequest = new GamePayRequest();
				int waresid = 95805;
				int price   = 1;
				payRequest.addParam("notifyurl", "");
				payRequest.addParam("appid", Config.appid);
				payRequest.addParam("waresid", waresid);//waresid
				payRequest.addParam("exorderno", System.currentTimeMillis() + "");
				payRequest.addParam("price", price);
				payRequest.addParam("cpprivateinfo", a[2] + "-" + a[1] + "-" + a[5] + "-" +a[0]);
				 LenovoGameApi.doPay(MainActivity.this, Config.appkey, payRequest, new IPayResult() {
					@Override
					public void onPayResult(int resultCode, String signValue, String resultInfo) {
						
						if(LenovoGameApi.PAY_SUCCESS == resultCode){
							MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
						}else if(LenovoGameApi.PAY_CANCEL == resultCode){
							MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
						}else if(LenovoGameApi.PAY_FAIL == resultCode){
							MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
						}
					}
				});
			}
		});
	}
	
	@SuppressLint("NewApi")
	public void ExitSDK(){
//			this.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		        builder.setTitle("退出");
//		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//		            @SuppressLint("NewApi")
//					public void onClick(DialogInterface dialog, int whichButton) {
//		                //退出apk
//		                dialog.dismiss();
//		                MainActivity.this.finish();
//		            }
//		        });
//		        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//		            public void onClick(DialogInterface dialog, int whichButton) {
//		                // dialog隐藏
//		                dialog.dismiss();
//		            }
//		        });
//		        builder.create().show();
//			}
//		});
		LenovoGameApi.doQuit(MainActivity.this, new IAuthResult() {
			
			@Override
			public void onFinished(boolean arg0, String result) {
				if(arg0){
					finish();
					System.exit(0);
				}
				MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
			}
		});
	}
}
