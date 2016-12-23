package com.qianhuan.yxgsd.downjoy;

import cn.jpush.android.api.JPushInterface;

import com.downjoy.CallbackListener;
import com.downjoy.CallbackStatus;
import com.downjoy.Downjoy;
import com.downjoy.LoginInfo;
import com.downjoy.LogoutListener;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.downjoy.ReqTask;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;

public class MainActivity extends UnityPlayerActivity {
	
	static MainActivity currentActivity;
    Downjoy downjoy; 		//实例化当乐游戏中心
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = this;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(downjoy != null){
			downjoy.destroy();
			downjoy = null;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(downjoy != null){
			downjoy.resume(MainActivity.this);
		}
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(downjoy != null){
			downjoy.pause();
		}
		JPushInterface.onPause(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			 downjoyExit();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private boolean downjoyExit(){
		if(downjoy == null){
			return false;
		}
		downjoy.openExitDialog(this, new CallbackListener<String>() {

			@SuppressLint("NewApi")
			@Override
			public void callback(int status, String data) {
				if(CallbackStatus.SUCCESS == status){
					finish();
				}else if(CallbackStatus.CANCEL == status){
					MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "");
				}
			}
		});
		return true;
	}
	
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				downjoy = Downjoy.getInstance();
				downjoy.showDownjoyIconAfterLogined(true);
				downjoy.setInitLocation(Downjoy.LOCATION_RIGHT_CENTER_VERTICAL);
				downjoy.setLogoutListener(mLogoutListener);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		};
			
	
	};
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
			downjoy = Downjoy.getInstance();
			downjoy.showDownjoyIconAfterLogined(true);
			downjoy.setLogoutListener(mLogoutListener);
			MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	/***
	 * 登出回调
	 * @param msg
	 */
	private LogoutListener mLogoutListener = new LogoutListener() {
		@Override
		public void onLogoutSuccess() {
			MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
		}
		
		@Override
		public void onLogoutError(String errorMsg) {
			MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "");
		}
	};
	
	
	public void Login(String msg){
		if(downjoy == null)
			return;
		downjoy.openLoginDialog(MainActivity.this, new CallbackListener<LoginInfo>() {
			@Override
			public void callback(int status, LoginInfo data) {
				if(status == CallbackStatus.SUCCESS && data != null){
					final String umid     = data.getUmid();
					String username = data.getUserName();
					String nickname = data.getNickName();
					String token    = data.getToken();
					String checkurl = data.getCheckTokenUrl();
					ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
			        {            
			            @Override
			            public void execute(String buoyPrivateKey)
			            {      
			            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, umid);
			            }
			        }, "&umid=" + umid + "&access_token=" + token, "http://123.207.146.159/downjoysdk/login.php");
			        getBuoyPrivate.execute();     
				}else if(status == CallbackStatus.FAIL && data != null){
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
				}else if(status == CallbackStatus.CANCEL && data !=null){
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
				}
			}
		});
	}
	
	public void Pay(String msg){
		if(downjoy == null)
			return;
		String a[] = msg.split("_");
		float amout         =  1; 				//购买钻石花费金额
		String productName = a[6];			    //商品名称
		String body        = a[6];           	//商品描述
		String transNo     = getRandString() + "|" + a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0];	//订单号(额外字段)
		String serverName  = "001";				//记录订单产生的服务器
		String playerName  = "001";				//记录订单产生的玩家名称
		downjoy.openPaymentDialog(this, amout, productName, body, transNo, serverName, playerName, new CallbackListener<String>() {
			@Override
			public void callback(int status, String data) {
				if(status == CallbackStatus.SUCCESS){
					MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
				}else if(status == CallbackStatus.FAIL){
					MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
				}else if(status == CallbackStatus.CANCEL){
					MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
				}
			}
		});
	}
	
	public void ExitSDK(){
		if(null == downjoy)
			return;
		downjoy.openExitDialog(this, new CallbackListener<String>() {
			@SuppressLint("NewApi")
			@Override
			public void callback(int status, String data) {
				if(CallbackStatus.SUCCESS == status){
					finish();
				}else if(CallbackStatus.CANCEL == status){
					//MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "");
				}
			}
		});
		
	}
	
	public boolean onKeyDown1(int keyCOde, KeyEvent event){
		if(keyCOde == KeyEvent.KEYCODE_BACK){
			  return downjoyExit();
		}
		return super.onKeyDown(keyCOde, event);
	}
	
	/**
     * 生成CP自定义信息和订单号
     *
     * @return
     */
    private static String getRandString() {
        int[] x = new int[10];
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            x[i] = (int) (Math.random() * 10);
            b.append(x[i]);
        }
        return b.toString();
    }
}
