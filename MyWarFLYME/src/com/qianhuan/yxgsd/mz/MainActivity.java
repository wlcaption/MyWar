package com.qianhuan.yxgsd.mz;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.mz.ReqTask;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;
import com.meizu.gamesdk.online.model.model.MzBuyInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends UnityPlayerActivity{
	private String mUid;
	private String mSession;
	
	private String orderId        = "";			//自定语cp订单
	private String sign           = "";			//参数签名
	private String signType       = "";		    //签名算法
	private int buyCount          = 1;			//道具购买数量
	private String cpUserInfo     = "";			//自定义字段
	private String totalPrice     = "";			//购买总额
	private String productId      = "0";		//CP游戏道具ID
	private String productSubject = "";			//订单标题 (”购买“ + 数量 + 单位 + 产品名称)
	private String productBody    = "";			//游戏道具说明
	private String productUnit    = "";			//游戏道具单位
	private String appid          = "";			//游戏ID不能为空
	private String uid            = "";			//游戏玩家ID不能为空
	private String productperpric = "";			//游戏道具单价
	private long createTime       = 0;          //创建时间戳
	int payType                   = 0; 			//定额支付
	
	
	MzGameBarPlatform mzGameBarPlatform; //悬浮框对象
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	    mzGameBarPlatform = new MzGameBarPlatform(this,MzGameBarPlatform.GRAVITY_RIGHT_BOTTOM);
	    mzGameBarPlatform.onActivityCreate();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mzGameBarPlatform.onActivityResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		mzGameBarPlatform.onActivityPause();
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MzGameCenterPlatform.logout(MainActivity.this);
		mzGameBarPlatform.onActivityDestroy();
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	
		
	@SuppressLint("NewApi")
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				MzGameCenterPlatform.login(MainActivity.this, new MzLoginListener() {
					@Override
					public void onLoginResult(int code, MzAccountInfo accountInfo, String errorMsg) {
						switch (code) {
						case LoginResultCode.LOGIN_SUCCESS:
							mUid = accountInfo.getUid();
							mSession = accountInfo.getSession();
							mzGameBarPlatform.showGameBar();
							conecthttp();
							break;
						case LoginResultCode.LOGIN_ERROR_CANCEL:
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
							break;
						default:
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "" );
							break;
						}
					}
				});
			}
		});
	}
	
	
	public void conecthttp()
	{
		ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, mUid);
            }
        }, "&mUid=" + mUid + "&mSession=" + mSession , "http://123.207.146.159/meizusdk/login.php");
        getBuoyPrivate.execute();     
	}
	
	public  void Pay(String msg)
	{
		String a[] = msg.split("_");
		appid 			= GameConstants.GAME_ID;
		buyCount 		= 1;
		orderId 		= UUID.randomUUID().toString().replaceAll("-", "");
		createTime  	= 1398687820;
		payType  		= 0;
		productBody 	= a[6];
		productId   	="2";
		productSubject 	=a[6];
		productUnit		="颗";
		cpUserInfo		=a[2] + "-" +a[1] + "-" + a[5] + "-" + a[0];
		totalPrice		= Integer.parseInt(a[0])+ "";
		productperpric  = Integer.parseInt(a[0])+ "";
		uid             = mUid;
		signType        = "";
		ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      
					try {
						JSONObject mJson = new JSONObject(buoyPrivateKey);
						mJson = mJson.getJSONObject("value");
					    sign = mJson.getString("sign");
					    signType = mJson.getString("sign_type");
					    Bundle buyInfo = 
				 new MzBuyInfo().setBuyCount(buyCount).setCpUserInfo(cpUserInfo)
								.setOrderAmount(totalPrice).setOrderId(orderId).setPerPrice(productperpric)
								.setProductBody(productBody).setProductId(productId).setProductSubject(productSubject)
								.setProductUnit(productUnit).setSign(sign).setSignType(signType).setCreateTime(createTime)
								.setAppid(appid).setUserUid(uid).setPayType(payType).toBundle();
								
								//调用支付接口吊起支付窗口
								MzGameCenterPlatform.payOnline(MainActivity.this,buyInfo, new MzPayListener() {
									@Override
									public void onPayResult(int code,Bundle info, String errorMsg) {
										switch (code) {
										case PayResultCode.PAY_SUCCESS:
											MzBuyInfo buyInfo = MzBuyInfo.fromBundle(info);
											String order = buyInfo.getOrderId();//可以去服务器查询订单
											MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
											break;
										case PayResultCode.PAY_ERROR_CANCEL:
											MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
											break;
										default:
											MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
											break;
										}
									}
								});
					    
					} catch (JSONException e) {
						e.printStackTrace();
					}
            }
        }, "&appid=" + appid + "&buyCount=" + buyCount + "&orderId=" +orderId + 
           "&createTime=" + createTime + "&payType=" + payType + "&productBody=" + productBody + 
           "&productId=" + productId + "&productSubject=" + productSubject + "&productUnit=" + productUnit + 
           "&cpUserInfo=" + cpUserInfo + "&totalPrice=" + totalPrice  + "&productperpric=" + productperpric + "&uid=" + uid,  "http://139.196.14.52/meizusdk/order.php");
        getBuoyPrivate.execute();  
	}
	
	@SuppressLint("NewApi")
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
