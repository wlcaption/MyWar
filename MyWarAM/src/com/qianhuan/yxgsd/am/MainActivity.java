package com.qianhuan.yxgsd.am;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.gionee.gamesdk.AccountInfo;
import com.gionee.gamesdk.GamePayer;
import com.gionee.gamesdk.OrderInfo;
import com.gionee.gamesdk.GamePayer.GamePayCallback;
import com.gionee.gamesdk.GamePlatform;
import com.gionee.gamesdk.GamePlatform.LoginListener;
import com.gionee.gamesdk.demo.Constants;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.am.ReqTask;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends UnityPlayerActivity {
	private GamePayer mGamePayer;				//支付对象
	private GamePayCallback mGamePayCallback; 	//支付结果回调
	private String playerId;
	private String amigoToken;
	private String userId;
	private String outOrderNo;
	private String submitTime;
	private String deal_price;
	private String ext;
	private String subject;
	private String deliver_type;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGamePayer = new GamePayer(this);
		mGamePayCallback = mGamePayer.new GamePayCallback(){
			public void onPaySuccess() {
				MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
			}
			
		
			public void onPayCancel() {
				MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
			}
			
		
			public void onPayFail(String stateCode) {
				MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
			}
		};
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGamePayer.onDestroy();
	}
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				loginAccount();
			}
		});
	}
	
	private void loginAccount(){
		GamePlatform.loginAccount(MainActivity.this, true, new LoginListener() {
			
			public void onSuccess(AccountInfo accountInfo) {
				 playerId = accountInfo.mPlayerId;
				 amigoToken = accountInfo.mToken;
				 userId = accountInfo.mUserId;
				ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
		        {            
		            @Override
		            public void execute(String buoyPrivateKey)
		            {      
		            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, playerId);
		            }
		        }, "&playerId=" + playerId + "&amigoToken=" + amigoToken + "&userId=" + userId , "http://123.207.146.159/gioneesdk/login.php");
		        getBuoyPrivate.execute();     
			}
			
			public void onError(Exception arg0) {
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
			}
			
			public void onCancel() {
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
			}
		});
	}
	
	@SuppressLint("SimpleDateFormat")
	public void Pay(String msg){
		String[] a = msg.split("_");
	    outOrderNo = UUID.randomUUID().toString().replaceAll("-", "");
		Date CurrentTime = new Date();
		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		 submitTime = format.format(CurrentTime);
		 deal_price = "1";
		 ext = a[2] + "-" + a[1] + "-" +a[5] + "-" + a[0];
		 subject = "钻石";
		 deliver_type = "1";
		 user_id = userId;
		 //MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "------" +userId);
		
		 ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
	        {   
	            @Override
	            public void execute(String buoyPrivateKey)
	            {    
	            	JSONObject mJson;
					try {
						mJson = new JSONObject(buoyPrivateKey);
						String order_id = mJson.getString("out_order_no");
						OrderInfo orderInfo = new OrderInfo();
		        		//appkey
		        		orderInfo.setApiKey(Constants.API_KEY);
		        		//商户订单号，与创建支付订单中的out_order_no的值相同
		        		orderInfo.setOutOrderNo(order_id);
		        		//支付订单提交时间，与创建支付订单中的submit_time值相同
		        		orderInfo.setSubmitTime(submitTime);
		        		mGamePayer.pay(orderInfo,  mGamePayCallback);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
	            	//创建订单信息
	            }
	        }, "&deal_price=" + deal_price + "&subject=" +subject  + "&player_id=" + playerId +"&out_order_no=" + 
	            outOrderNo+"|"+ ext + "&submit_time=" + submitTime +"&deliver_type=" + deliver_type + "&user_id=" + userId , "http://123.207.146.159/gioneesdk/order.php");
	        getBuoyPrivate.execute();  
	}
	
	public void ExitSDK(){
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
