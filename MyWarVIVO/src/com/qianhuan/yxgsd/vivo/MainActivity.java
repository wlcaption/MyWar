package com.qianhuan.yxgsd.vivo;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

import com.bbk.payment.payment.OnVivoPayResultListener;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qinahuan.yxgsd.vivo.net.ReqTask;
import com.qinahuan.yxgsd.vivo.net.ReqTask.Delegate;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import com.vivo.sdkplugin.aidl.VivoUnionManager;
import com.vivo.sdkplugin.accounts.OnVivoAccountChangedListener;


/**
 * @author xiaowei
 * 2016-10-17 下午2:08:53
 * wlcaption@qq.com
 */
public class MainActivity extends UnityPlayerActivity {
	private Context mContext;
	private VivoUnionManager mVivoUnionManager; 
	private static String APPID = "9885e4eaa65e696ed43360ac994a05df";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mContext = this;
		mVivoUnionManager = new VivoUnionManager(mContext);
		mVivoUnionManager.registVivoAccountChangeListener(accountChangedListener);
		mVivoUnionManager.bindUnionService();
	}
	
	OnVivoAccountChangedListener accountChangedListener = new OnVivoAccountChangedListener() {
		
		@Override
		public void onAccountRemove(boolean isRemoved) {
			
		}
		
		@Override
		public void onAccountLoginCancled() {
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
		}
		
		@Override
		public void onAccountLogin(final String name, final String openid, String authtoken) {
			ReqTask reqTask = new ReqTask(new Delegate() {
				@Override
				public void execute(String result) {
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, openid);
				}
			}, "&authtoken=" + authtoken, "http://123.207.146.159/vivosdk/login.php");
			reqTask.execute();
		}
	};
	
	OnVivoPayResultListener payResultListener = new OnVivoPayResultListener() {
		
		@Override
		public void rechargeResult(String openid, boolean pay_result, String result_code,String pay_msg) {
			UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",result_code);
		}
		
		@Override
		public void payResult(String transNo, boolean pay_result, String result_code, String pay_msg) {
			//MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, transNo + "-" + result_code +"--" + pay_msg);
		}
	};
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mVivoUnionManager.startLogin(APPID);
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "");
			}
		});
	}
	
//	public void CreateRole(String type, String msg){
//		this.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				VivoUnionManager.vivoAccountreportRoleInfo("123", "1", "1", "wwc", mContext, "1区");
//				MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "CREATEROLE");
//			}
//		});
//	}
	
	public void showToolBar(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mVivoUnionManager.showVivoAssitView(mContext);
			}
		});
	}
	
	@SuppressLint("SimpleDateFormat")
	public void Pay(String msg){
		
		String a[] = msg.split("_");
		String transNo1 = System.currentTimeMillis() + "";
		String cpId = "20160201102033928864";
		final String productName = a[6];
		final String productDes  = a[6];
		final long price = Integer.parseInt(a[0])*100;
		final String appId = APPID;
		
		final String blance = "100";
		final String vip = "vip2";
		final int level = Integer.parseInt(a[4]);
		final String party = "同盟";
		final String roleId = a[2];
		final String roleName = a[3];
		final String serverName = "1区";
		String orderTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		final String extInfo = a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0];
		final boolean logOnOff = false;
		
		ReqTask reqTask = new ReqTask(new Delegate() {
			@Override
			public void execute(String result) {
				try {
				JSONObject mJson = new JSONObject(result);
				String	transNo  = mJson.getString("orderNumber");
				String accessKey = mJson.getString("accessKey");
				VivoUnionManager manager = new VivoUnionManager(MainActivity.this);
				manager.initVivoPaymentAndRecharge(mContext, payResultListener);
				Bundle bundle = new Bundle();
				bundle.putString("transNo", transNo);        //交易流水号
				bundle.putString("accessKey", accessKey);	 //签名方法
				bundle.putString("appId", appId);			 //APP ID
				bundle.putString("productName", productName);//商品名称
				bundle.putString("productDes", productDes);  //商品描述
				bundle.putLong("price", price);              //商品价格
				
				bundle.putString("blance", blance);          //游戏中元宝数量
				bundle.putString("vip", vip);				 //游戏中vip等级
				bundle.putInt("level",level);				 //游戏中游戏等级
				bundle.putString("party", party);			 //公会
				bundle.putString("roleId", roleId);          //角色ID
				bundle.putString("roleName" ,roleName);      //角色名
				bundle.putString("serverName", serverName);  //服务器名称
				bundle.putString("extInfo", extInfo);        //额外字段
				bundle.putBoolean("logOnOff", logOnOff);     //日志开关
				manager.payment(mContext, bundle);  //支付接口
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "&transNo="+ transNo1 + "&cpId=" + cpId + "&appId=" + appId + "&orderTime=" + orderTime
		   +"&price=" + price + "&productName=" + productName + "&productDes=" + productDes + "&extInfo="
		   + extInfo, "http://123.207.146.159/vivosdk/order.php");
		reqTask.execute();
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
	
	@Override
	protected void onResume() {
		super.onResume();
		mVivoUnionManager.showVivoAssitView(MainActivity.this);
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
		mVivoUnionManager.hideVivoAssitView(MainActivity.this);
		mVivoUnionManager.cancelVivoPaymentAndRecharge(payResultListener);
	}

}
