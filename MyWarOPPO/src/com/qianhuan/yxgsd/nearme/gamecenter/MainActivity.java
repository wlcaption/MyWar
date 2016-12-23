package com.qianhuan.yxgsd.nearme.gamecenter;


import java.util.Random;

import org.json.JSONObject;
import android.os.Bundle;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.platform.opensdk.pay.PayResponse;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity{
	
	private String mssoid;
	private boolean mHasLogin = false;
	private String token;
	private String  ssoid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		String appSecret = "ab03f4746b8C0A1d68624598177f64AD";
		GameCenterSDK.init(appSecret, this);
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {		
		GameCenterSDK.getInstance().onPause();
		super.onPause();
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onRestart() {		
		super.onRestart();		
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
	}
	
	@Override
	protected void onStop() {		
		super.onStop();
	}	

	@Override
	protected void onResume() {		
		super.onResume();
		GameCenterSDK.getInstance().onResume(this);
		JPushInterface.onResume(this);
	}
	
	//init Sdk
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer)
	{
		GameCenterSDK.init(appkey, this);	
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	
	// Login 
	public void Login(String msg)
	{
		
		GameCenterSDK.getInstance().doLogin(this, new ApiCallback() {
			
			@Override
			public void onSuccess(String resultMsg) {
				
				GetToken();
			}
			
			@Override
			public void onFailure(String resultMsg, int resultCode) {
				
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
			}
		});
	}
	
	// Token
	public void GetToken() {
		GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {
			
			@Override
			public void onSuccess(String resultMsg) {
				
				try {
					JSONObject json = new JSONObject(resultMsg);
					 token = json.getString("token");
					 ssoid = json.getString("ssoid");
					ConnectHttp("","");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(String resultMsg, int resultCode) {
			}
		});
		
	}
	
	public boolean IsLogin()
	{
		return mHasLogin;
	}
	
	public String getUserID()
	{
		return mssoid;
	}
	
	
	// doPay
	public void Pay(String msg)
	{
		
		String[] a = msg.split("_"); 
		int amout = 1; //支付的金额，单位是分
		PayInfo payinfo = new PayInfo(System.currentTimeMillis() +
				new Random().nextInt(1000) + "", a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0] , amout);
		payinfo.setProductDesc(a[6]);
		payinfo.setProductName(a[6]);
		payinfo.setAmount(Integer.parseInt(a[0])* 100);
		payinfo.setCallbackUrl("http://123.207.146.159/opposdk/pay.php");
		
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAADDDDDDd");
		GameCenterSDK.getInstance().doPay(this, payinfo, new ApiCallback() {
			
			@Override
			public void onSuccess(String resultMsg) { //支付成功
				
				MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
			}
			
			@Override
			public void onFailure(String resultMsg, int resultCode) { // 支付失败
				
				if(PayResponse.CODE_CANCEL != resultCode)
				MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
			}
		});
	}
	
	//Exit Sdk
		public void ExitSDK()
		{
			GameCenterSDK.getInstance().onExit(this,
					new GameExitCallback() {
						@Override
						public void exitGame() {
							MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
						}
					});
		}
		
		public void sendResult(String result) {   
	    	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",result);
	    }
	    
		
		 public void ConnectHttp(String userip, String account_type)
		    {
			 sendResult("&ssoid=" + ssoid + "&token" + token);
		    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
		        {            
		            @Override
		            public void execute(String buoyPrivateKey)
		            {      	
		            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, ssoid);
		            }
		        },  "&ssoid=" + ssoid + "&token=" + token ,"http://123.207.146.159/opposdk/login.php");
		        getBuoyPrivate.execute();     
		    }

}
