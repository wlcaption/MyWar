package com.qianhuan.yxgsd.anzhi;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.anzhi.sdk.middle.manage.AnzhiSDK;
import com.anzhi.sdk.middle.manage.GameCallBack;
import com.anzhi.sdk.middle.util.MD5;
import com.anzhi.sdk.util.Des3Util;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.anzhi.ReqTask;
import com.unity3d.player.UnityPlayerActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class MainActivity extends UnityPlayerActivity {
	private String Appkey    = "1471944031th0j09QPtN8eYViImA41";
	private String AppSecret = "1WUE4D6fHEnk52Ncvyyd39Lg";
	
	private AnzhiSDK mAnzhiManage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAnzhiManage = AnzhiSDK.getInstance();
		mAnzhiManage.init(this, Appkey, AppSecret, callBack);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		AnzhiSDK.getInstance().onNewIntentInvoked(intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AnzhiSDK.getInstance().onResumeInvoked();
		JPushInterface.onResume(this);
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		AnzhiSDK.getInstance().onStartInvoked();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AnzhiSDK.getInstance().onPauseInvoked();
		JPushInterface.onPause(this);
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        AnzhiSDK.getInstance().onStopInvoked();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AnzhiSDK.getInstance().onDestoryInvoked();
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	AnzhiSDK.getInstance().onActivityResultInvoked(requestCode, requestCode, data);
    };
	
	
	
	
	//SDK回调接口
	
	GameCallBack callBack = new GameCallBack() {
		@Override
		public void callBack(int type, String data) {
			
			JSONObject mJson = null;
			
			switch (type) {
			//SDK初始化回调
			case GameCallBack.SDK_TYPE_INIT:
				break;
			//登录状态回调
			case GameCallBack.SDK_TYPE_LOGIN:
				try {
					mJson = new JSONObject(data);
					if(mJson.optInt("code") == 200){ 				   //登录成功
						final String uid 		= mJson.optString("uid"); 	   //用户UID
						String sid 		= mJson.optString("sid");      //登录session
						String nickName = mJson.optString("nickName"); //用户昵称
						String encode = com.anzhi.sdk.middle.util.Base64.encodeToString(Appkey+ "1" + AppSecret);
						mAnzhiManage.subGameInfo(getGameInfo());	   //上传用户游戏角色信息
						mAnzhiManage.addPop(MainActivity.this); 	   //登录成功之后添加游戏悬浮气泡
						ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
				        {            
				            @Override
				            public void execute(String buoyPrivateKey)
				            {      
				            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, uid);
				            }
				        }, "&access_token=" + sid, "http://123.207.146.159/anzhisdk/login.php");
				        getBuoyPrivate.execute();  
					}else{
						String desc = mJson.optString("codeDesc");
						MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
					}
					break;
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			//支付回调
			case GameCallBack.SDK_TYPE_PAY:
				try {
					mJson = new JSONObject(data);
					int status = mJson.optInt("payStatus");
					if(status == 1){  //支付成功
						String anzhiOrderId = mJson.optString("orderId");
						String cpOrderId    = mJson.optString("cpOrderId");
						String cpCustomInfo = mJson.optString("cpCustomInfo");
 						MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
					}else if(status == 2){
						
					}else{
						MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				break;
			//取消支付回调
			case GameCallBack.SDK_TYPE_CANCEL_LOGIN:
				break;
			//退出游戏回调
			case GameCallBack.SDK_TYPE_EXIT_GAME:
				finish();
				if(data != null){
					try {
						mJson = new JSONObject(data);
						boolean killSelf = mJson.optBoolean("killSelf");
						if(killSelf){
							mAnzhiManage.closeSDKActivity();
							System.exit(0);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			//取消退出游戏回调
			case GameCallBack.SDK_TYPE_CANCEL_EXIT_GAME:
				break;
			//注销登录回调
			case GameCallBack.SDK_TYPE_LOGOUT:
				break;
			}
		}
	};
	
	/**
	 * 初始化操作
	 * @param appid
	 * @param appkey
	 * @param privateKey
	 * @param oauthLoginServer
	 */
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	private String getGameInfo() {
		JSONObject gameInfoJson = new JSONObject();
		try {
			gameInfoJson.put(AnzhiSDK.GAME_AREA, "服务器1"); //服务器区号
			gameInfoJson.put("gameAreaId", "0001");		   //角色所在服务器编码
			gameInfoJson.put(AnzhiSDK.GAME_LEVEL, "1级");  //游戏等级
			gameInfoJson.put(AnzhiSDK.ROLE_ID, "112233"); //角色ID
			gameInfoJson.put(AnzhiSDK.USER_ROLE, "wwc");  //用户角色名称
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gameInfoJson.toString();
	}

	/***
	 * 登录操作
	 * @param msg
	 */
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAnzhiManage.login(MainActivity.this);
			}
		});
	}
	
	/***
	 * 支付操作
	 * @param msg
	 */
	public void Pay(String msg){
		String data = createPayData(msg);
		AnzhiSDK.getInstance().pay(Des3Util.encrypt(data, AppSecret), MD5.encodeToString(AppSecret));
	}
	
	private String createPayData(String msg) {
		JSONObject mJson = new JSONObject();
		try {
			String a[] = msg.split("_");
			mJson.put("cpOrderId", "CP_" + System.currentTimeMillis()); 		  //厂商订单号
			mJson.put("cpOrderTime", System.currentTimeMillis());       		  //订单生成时间
			mJson.put("amount", 100);                                             //支付金额
			mJson.put("cpCustomInfo", a[2] + "-" + a[1] + "-" + a[5] + "-" +a[0]);//游戏方自定义参数
			mJson.put("productName", "钻石");							          //产品名称
			mJson.put("productCode", "0001");							          //游戏方商品代码
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJson.toString();
	}

	/***
	 * SDK PAY
	 */
	
	
	/***
	 * 退出操作
	 * @param msg
	 */
	public void ExitSDK(){
		mAnzhiManage.exitGame(this);
	}
	
	 @Override
	    public boolean onKeyUp(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            mAnzhiManage.exitGame(this); // 退出游戏必须调用SDK的方法
	            return true;
	        }
	        return super.onKeyUp(keyCode, event);
	    }
}
