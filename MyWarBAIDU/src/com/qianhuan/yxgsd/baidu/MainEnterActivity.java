package com.qianhuan.yxgsd.baidu;

import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.bdgamesdk.demo.utils.Utils;
import com.baidu.gamesdk.BDGameSDK;
import com.baidu.gamesdk.BDGameSDKSetting;
import com.baidu.gamesdk.IResponse;
import com.baidu.gamesdk.OnGameExitListener;
import com.baidu.gamesdk.ResultCode;
import com.baidu.gamesdk.BDGameSDKSetting.Domain;
import com.baidu.home.datamodel.HomeCfgResponseVip.User;
import com.baidu.platformsdk.PayOrderInfo;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.baidu.*;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;




public class MainEnterActivity extends BaseActivity {
	 private EditText amountText;
	 private AlertDialog alertDialog;
	 private String guid;
	 private String authtoken;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		HolaSdkInit("", "", "", "");
		
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
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) { //init SDK
		BDGameSDKSetting mBDGameSDKSetting = new BDGameSDKSetting();
	    mBDGameSDKSetting.setAppID(8276613);
		mBDGameSDKSetting.setAppKey("Elj4kQVZBeynzHk4phMYgNhZ");
		mBDGameSDKSetting.setDomain(Domain.RELEASE);
		mBDGameSDKSetting.setOrientation(Utils.getOrientation(this));
		
		BDGameSDK.init(this, mBDGameSDKSetting, new IResponse<Void>() {//初始化SDK的函数调用
			@Override
            public void onResponse(int resultCode, String resultDesc, Void extraData) {
                switch (resultCode) {
                    case ResultCode.INIT_SUCCESS:
                        // 初始化成功
                    	MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
                    	BDGameSDK.getAnnouncementInfo(MainEnterActivity.this);
                        break;

                    case ResultCode.INIT_FAIL:
                   	MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, "");
                }

            }

        });
	}
	
	public void setSuspendWindowChangeAccountListener() { // 设置切换账号事件监听（个人中心界面切换账号）
	        BDGameSDK.setSuspendWindowChangeAccountListener(new IResponse<Void>() {

	            @Override
	            public void onResponse(int resultCode, String resultDesc, Void extraData) {
	                switch (resultCode) {
	                    case ResultCode.LOGIN_SUCCESS:
	                    	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceiveChangeUser","");
	                        // TODO 登录成功，不管之前是什么登录状态，游戏内部都要切换成新的用户
	                       
	                        break;
	                    case ResultCode.LOGIN_FAIL:
	                        // TODO 登录失败，游戏内部之前如果是已经登录的，要清除自己记录的登录状态，设置成未登录。如果之前未登录，不用处理。
	                        
	                        break;
	                    case ResultCode.LOGIN_CANCEL:
	                        // TODO 取消，操作前后的登录状态没变化
	                        break;
	                    default:
	                        // TODO 此时当登录失败处理，参照ResultCode.LOGIN_FAIL（正常情况下不会到这个步骤，除非SDK内部异常）
	                       
	                        break;

	                }
	            }

	        });
	    }
	    
	public void Login(String msg) {
		BDGameSDK.login(new IResponse<Void>() {
			@Override
			public void onResponse(int resultCode, String arg1, Void arg2) {
				String hint = "";
				switch(resultCode){
				case ResultCode.LOGIN_SUCCESS: //登录成功提示
				hint = "登录成功";
				ConnectHttp("","");
				break;
				case ResultCode.LOGIN_CANCEL: //取消登录提示
				hint = "取消登录";
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, hint);
				break;
				case ResultCode.LOGIN_FAIL: //登录失败
				default: //其他值为登录失败
				hint = "登录失败";
				MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, hint);
				}
			}
		});
	}
	
	/**
     * 构建订单信息
     */
    
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {
        	 switch (msg.what) {   
             case 1: 
            	 BDGameSDK.gameExit(MainEnterActivity.this, new OnGameExitListener() {
         			@SuppressLint("NewApi")
         			@Override
         			public void onGameExit() {
         				Looper.myLooper().quit();
         				finish();
         			}
         		});	
            	 break;   
             }   
             super.handleMessage(msg);   
        }   
   };  
	
	public void ExitSDK()
	{
		Message message = new Message();   
        message.what = 1;   
        MainEnterActivity.this.myHandler.sendMessage(message);
	}
	
	public void Pay(String msg) {
		String[] a = msg.split("_");//diamond, zoneid, guid, name, level, paytype, ProductName
		String cpOrderId = UUID.randomUUID().toString(); // CP订单号
        //String goodsName = "金币";
        //String totalAmount = amountText.getText().toString(); // 支付总金额 （以分为单位）
        int ratio = 1; // 该参数为非定额支付时生效 (支付金额为0时为非定额支付,具体参见使用手册)
        String extInfo = "第X号服务器，Y游戏分区充值"; // 扩展字段，该信息在支付成功后原样返回给CP
        int totalAmount = Integer.parseInt(a[0])*100;

        PayOrderInfo payOrderInfo = new PayOrderInfo();
        payOrderInfo.setCooperatorOrderSerial(cpOrderId);//订单号
        payOrderInfo.setProductName(a[6]);
        payOrderInfo.setTotalPriceCent(totalAmount); // 以分为单位,这样就以元为单位来进行支付了
        payOrderInfo.setRatio(ratio);  //guid-serverid-paytype-diamond
        payOrderInfo.setExtInfo(a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0]); // 该字段将会在支付成功后原样返回给CP(不超过500个字符
        //"", a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0] , amout)
        BDGameSDK.pay(payOrderInfo, "http://123.207.146.159/baidusdk/pay.php", new IResponse<PayOrderInfo>() {
			
			
			@Override
			public void onResponse(int resultCode, String resultDesc, PayOrderInfo extraData) {
                String resultStr = "";
                switch (resultCode) {
                    case ResultCode.PAY_SUCCESS: // 支付成功
                    	Toast.makeText(MainEnterActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","SUCCESS");
                        resultDesc = "支付成功:" + resultDesc; 
                        break;
                    case ResultCode.PAY_CANCEL: // 订单支付取消
                        Toast.makeText(MainEnterActivity.this, "取消支付", Toast.LENGTH_SHORT).show();
                        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","CANCAL");
                        resultDesc = "取消支付:" + resultDesc;
                        break;
                    case ResultCode.PAY_FAIL: // 订单支付失败
                        Toast.makeText(MainEnterActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","FAILED");
                        resultDesc = "支付失败:" + resultDesc;
                        break;
                    case ResultCode.PAY_SUBMIT_ORDER: // 订单已经提交，支付结果未知（比如：已经请求了，但是查询超时）
                        resultStr = "订单已经提交，支付结果未知";
                        break;
                    default:
                        resultStr = "订单已经提交，支付结果未知";
                        break;
                }
                Toast.makeText(MainEnterActivity.this, resultStr, Toast.LENGTH_LONG).show();
            }
		});
	}
	
	 
	
	public void ConnectHttp(String userip, String account_type)
    {
    	
		guid = BDGameSDK.getLoginUid();
		authtoken = BDGameSDK.getLoginAccessToken();
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",guid);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",authtoken);
        
    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      
            	if(buoyPrivateKey.indexOf("success",0) != -1){
            	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive", "SUCCESS");
            	BDGameSDK.showFloatView(MainEnterActivity.this);
            	setSessionInvalidListener();
        		setSuspendWindowChangeAccountListener();
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, guid);
            }
            else{
            	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive", "FAILED");
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, guid);
            }
            	
            }
        }, "&guid=" + guid + "&authtoken=" + authtoken , "http://123.207.146.159/baidusdk/login.php");
        getBuoyPrivate.execute();     
    }
	
	private void setSessionInvalidListener() {
		
		BDGameSDK.setSessionInvalidListener(new IResponse<Void>() {
			
			@Override
			public void onResponse(int resultCode, String resultDesc, Void extraData) {
				if(resultCode == ResultCode.SESSION_INVALID){
					String msg = null;
					Login(msg);	
			}
			}
		});
	}
}
