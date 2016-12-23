package com.qianhuan.yxgsd.hola.ju360;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import cn.jpush.android.api.JPushInterface;

import com.qianhuan.yxgsd.hola.ju360.net.ReqTask;
import com.qianhuan.yxgsd.hola.ju360.net.ReqTask.Delegate;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.jusdk.bean.JUPayInfos;
import com.jusdk.bean.JURolerInfo;
import com.jusdk.bean.JUUserInfo;
import com.jusdk.listener.JUExitListener;
import com.jusdk.listener.JUInitListener;
import com.jusdk.listener.JULoginListener;
import com.jusdk.listener.JULogoutListener;
import com.jusdk.listener.JUPayListener;
import com.jusdk.manager.Matrix;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * @author xiaowei
 * 2016-10-12 下午2:07:46
 * hola sdk
 */
public class MainActivity extends UnityPlayerActivity {
	private String uid;
	//private Activity activity;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//activity = MainActivity.this;
		//setContentView(ResUtils.getLayout("demo_hisdk_activity_main", MainActivity.this)); //加载布局的配置文件
		//initView();
		HolaSdkInit("", "", "", "");//android:theme="@style/MyDialogStyle"
	}
	
	//初始化操作
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
			Matrix.getInstance().init(MainActivity.this, new JUInitListener() {
				@Override
				public void onSuccess() {
					MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
				}
				@Override
				public void onFail() {
					MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, "");
				}
			});
	}
	
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Matrix.getInstance().login(MainActivity.this, new JULoginListener() {
					
					@Override
					public void onSuccess(JUUserInfo userInfo) {
						String loginType = userInfo.getLoginType();
						String token = userInfo.getToken();
						ReqTask reqTask = new ReqTask(new Delegate() {
							
							@Override
							public void execute(String result) {
								try {
									JSONObject mJson = new JSONObject(result);
									mJson = mJson.getJSONObject("data");
									uid = mJson.getString("guid");
								} catch (JSONException e) {
									e.printStackTrace();
								}
								MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, uid);
							}
						}, "&token=" + token, "http://123.207.146.159/holasdk/login.php");
						reqTask.execute();
					}
					
					@Override
					public void onLogoutSuccess() {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
					}
					
					@Override
					public void onLoginFailed(String reason) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
					}
					
					@Override
					public void onLoginCancel() {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
					}
				});
			}
		});
	}

	public void Pay(final String msg){
		 this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String a[] = msg.split("_");
			    JUPayInfos payInfo = new JUPayInfos();
				String orderid = System.currentTimeMillis() + "_" + a[2] + "_" + a[1] + "_" + a[5] + "_" + a[0];// 订单号
				payInfo.setProDes(a[6]);   // 商品描述
				payInfo.setOrderId(orderid);// [String] 订单号 请不要带特殊字符 （
										    // 数字，英文以及下划线组成）
				payInfo.setProAmount(1);    // [int] 商品数量
				payInfo.setProId(a[5]); // [String] 商品ID
				payInfo.setProName(a[6]);  // [String] 商品名称
				payInfo.setProPrice(Integer.parseInt(a[0])*100f);  // [float] 商品价格 分，值必须为100的倍数 以分为单位
				String sp = a[1];
				String fn = sp.substring(8);
				payInfo.setServerId(fn);   // 游戏服务器id 纯数字
				payInfo.setServerName(a[7]);//服务器名称
				payInfo.setExchangeRate(10);// [int] RMB与游戏货币兑换比率
				payInfo.setGameMoneyName("钻石");// 游戏货币名称
				payInfo.setNotifyUrl("http://123.207.146.159/holasdk/pay.php");// 支付回调地址
				payInfo.setUid(uid);        // SDK传给游戏的uid
				
				JURolerInfo roler = new JURolerInfo();
				roler.setRole_Id(a[2]);  // 游戏角色id
				roler.setRole_Name(a[3]);  // 游戏角色名称
				roler.setRole_Grade(a[4]);  // 游戏角色等级 纯数字
				roler.setRole_Balance("0");// 用户游戏内虚拟币余额，例如 余额100元宝则填写100
				roler.setRole_Vip("1");      // 角色vip等级 如果没有vip等级请写 0或者无
				roler.setRole_UserParty("无");// 角色所在公会 如果没有工会，请填写 无
				roler.setServer_Name(a[7]);   // 服务器名名称
				roler.setServer_Id(fn);// 服务器id 纯数字例如1服 则为 1
				payInfo.setGameRolerInfo(roler);
						Matrix.getInstance().pay(MainActivity.this, payInfo,
								new JUPayListener() {
									@Override
									public void onSuccess(Object object) {// 当支付成功时，object为null
										MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
									}
									@Override
									public void onFail(Object object) {
										MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
									}
									@Override
									public void onCancel() {
										MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
									}
								});
			}
		});
	}
	
	public void Pay1(final String msg){
		 this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String a[] = msg.split("_");
			  JUPayInfos payInfo = new JUPayInfos();
				String orderid = System.currentTimeMillis() + "_" + a[2] + "_" + a[1] + "_" + a[5] + "_" + a[0];// 订单号
				payInfo.setProDes(a[6]);   // 商品描述
				payInfo.setOrderId(orderid);// [String] 订单号 请不要带特殊字符 （
										    // 数字，英文以及下划线组成）
				payInfo.setProAmount(1);    // [int] 商品数量
				payInfo.setProId(System.currentTimeMillis()+""); // [String] 商品ID
				payInfo.setProName(a[6]);  // [String] 商品名称
				payInfo.setProPrice(Integer.parseInt(a[0])*100f);  // [float] 商品价格 分，值必须为100的倍数 以分为单位
				payInfo.setServerId(a[1]);   // 游戏服务器id 纯数字
				payInfo.setServerName(a[1]);//服务器名称
				payInfo.setExchangeRate(10);// [int] RMB与游戏货币兑换比率
				payInfo.setGameMoneyName("钻石");// 游戏货币名称
				payInfo.setNotifyUrl("http://123.207.146.159/holasdk/pay.php");// 支付回调地址
				payInfo.setUid(uid);        // SDK传给游戏的uid
				
				JURolerInfo roler = new JURolerInfo();
				roler.setRole_Id(a[2]);  // 游戏角色id
				roler.setRole_Name(a[3]);  // 游戏角色名称
				roler.setRole_Grade(a[4]);  // 游戏角色等级 纯数字
				roler.setRole_Balance("0");// 用户游戏内虚拟币余额，例如 余额100元宝则填写100
				roler.setRole_Vip("1");      // 角色vip等级 如果没有vip等级请写 0或者无
				roler.setRole_UserParty("召唤公会");// 角色所在公会 如果没有工会，请填写 无
				roler.setServer_Name(a[1]);   // 服务器名名称
				roler.setServer_Id(a[1]);// 服务器id 纯数字例如1服 则为 1
				payInfo.setGameRolerInfo(roler);
						Matrix.getInstance().pay(MainActivity.this, payInfo,
								new JUPayListener() {
									@Override
									public void onSuccess(Object object) {// 当支付成功时，object为null
										MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
									}
									@Override
									public void onFail(Object object) {
										MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
									}
									@Override
									public void onCancel() {
										MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
									}
								});
			}
		});
	}
	
	public void Logout(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Matrix.getInstance().logout(MainActivity.this, new JULogoutListener() {
					@Override
					public void onSuccess() {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
						UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","LOGOUT"); 
					}
					
					@Override
					public void onFail() {
						MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "");
					}
				});
			}
		});
	}
	
	public void showToolBar(){
		
	}
	
	//SetGameData() create levelup enter exit

	public void LoginGameRole(String type,String msg){
		JURolerInfo rolerInfo= new JURolerInfo();
		//HashMap<String, String> mHashMap = new HashMap<String, String>();
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",type);   
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",msg);
		String[] mstrs = msg.split("&");
		String mStri0[] = mstrs[0].split("=");
		String mStri1[] = mstrs[1].split("=");
		String mStri2[] = mstrs[2].split("=");
		String mStri3[] = mstrs[3].split("=");
		String mStri4[] = mstrs[4].split("=");
		String mStri5[] = mstrs[5].split("=");
		String mStri6[] = mstrs[6].split("=");
		String mStri7[] = mstrs[7].split("=");
		
		rolerInfo.setRole_Id(mStri0[1]);
		rolerInfo.setRole_Name(mStri1[1]);
		rolerInfo.setRole_Grade(mStri2[1]);
		rolerInfo.setRole_Vip(mStri7[1]);
		rolerInfo.setServer_Name(mStri4[1]);
		rolerInfo.setRole_Balance("0");
		rolerInfo.setRole_UserParty("无");
		rolerInfo.setServer_Id(mStri3[1]);
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","GAMEROLE2");
		if("create".equals(type)){
			Matrix.getInstance().submitExtraData(MainActivity.this,JURolerInfo.QH_CREATE_ROLE , rolerInfo);
		}
		if("levelup".equals(type)){
			Matrix.getInstance().submitExtraData(MainActivity.this, JURolerInfo.QH_LEVEL_UP, rolerInfo);
		}
		if("enter".equals(type)){
			Matrix.getInstance().submitExtraData(MainActivity.this, JURolerInfo.QH_LOGIN_GAME,rolerInfo);
		}
		if("exit".equals(type)){
			Matrix.getInstance().submitExtraData(MainActivity.this, JURolerInfo.QH_EXIT_GAME, rolerInfo);
		}
	}
	
	public void entryGameCenter(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				boolean isSipportUC = Matrix.getInstance().IsSupportUserCenter();
				if(isSipportUC){
					Matrix.getInstance().showAccountCenter(MainActivity.this);
					UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","entryGameCenter");
				}else{
					
				}
			}
		});
	}
	
	public void ExitSDK(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Matrix.getInstance().exit(MainActivity.this, new JUExitListener() {
					@Override
					public void onExit() {
						finish();
						System.exit(0);
						Process.killProcess(0);
						UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","ExitSDK");
					}
				});
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Matrix.getInstance().OnResume(this);
		JPushInterface.onResume(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Matrix.getInstance().OnStop(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Matrix.getInstance().OnStart(this);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Matrix.getInstance().OnReStart(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Matrix.getInstance().OnPause(this);
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Matrix.getInstance().OnDestory(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Matrix.getInstance().OnActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Matrix.getInstance().OnNewIntent(intent);
	}
}
