package com.qianhuan.yxgsd.m4399;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;

import cn.jpush.android.api.JPushInterface;
import cn.m4399.operate.OperateCenter;
import cn.m4399.operate.OperateCenter.OnLoginFinishedListener;
import cn.m4399.operate.OperateCenter.OnQuitGameListener;
import cn.m4399.operate.OperateCenter.OnRechargeFinishedListener;
import cn.m4399.operate.OperateCenterConfig;
import cn.m4399.operate.User;
import cn.m4399.operate.OperateCenterConfig.PopLogoStyle;
import cn.m4399.operate.OperateCenterConfig.PopWinPosition;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhua.yxgsd.net.m4399.ReqTask;
import com.qianhua.yxgsd.net.m4399.ReqTask.Delegate;
import com.unity3d.player.UnityPlayerActivity;

public class MainActivity extends UnityPlayerActivity {
	private static final String GAME_KEY = "113083";
	
	//SDK界面支持的四个方面配置
	public static final Integer[] ORIENTION = new Integer[]{
		ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, 		    //横屏
		ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, 		   //竖屏
		ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, //横屏180度旋转
		ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT  //竖屏180度旋转
	};
	
	//悬浮框的四种风格
	public static final String[] POP_STYLE_NAMES = new String[]{"黄色","数字","豆娃","熊猫"};
	public static final PopLogoStyle [] POP_STYLES = new PopLogoStyle[]{
		PopLogoStyle.POPLOGOSTYLE_ONE,
		PopLogoStyle.POPLOGOSTYLE_TWO,
		PopLogoStyle.POPLOGOSTYLE_THREE,
		PopLogoStyle.POPLOGOSTYLE_FOUR
	};
	OperateCenter mOperCenter;
	SharedPreferences mSp;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mSp = getSharedPreferences("sdk_sp", MODE_PRIVATE);
		//HolaSdkInit("", "", "", "");
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
	
	/***
	 * 
	 * @param setDebugEnabled 设置日子开关，发布的时候需要设为false，默认为false
	 * @param setOrientation 设置横竖屏方向，默认为横屏
	 * @param setPopLogoStyle 设置悬浮框样式，总共有四种
	 * @param setPopWinPosition 设置悬浮框默认显示位置，现有四种
	 * @param setSupportExcess 设置服务器端是否超出金额部分,默认为false
	 * @param setGameKey 设置appkey
	 */
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		this.runOnUiThread(new Runnable() {
			@Override
		public void run() {
		mOperCenter = OperateCenter.getInstance();
		OperateCenterConfig opeConfig = new OperateCenterConfig.Builder(MainActivity.this)
		.setDebugEnabled(false)
		.setOrientation(getOrientation())
		.setPopLogoStyle(getPopStylePreference())
		.setPopWinPosition(PopWinPosition.POS_LEFT)
		.setSupportExcess(true)
		.setGameKey("113083")
		.build();
		mOperCenter.setConfig(opeConfig);
		
		//初始化SDK，在这个过程中会读取各种配置的检查当前账号是否在登录中
		//只用在Init之后，isLogin()返回的状态才可靠
		mOperCenter.init(MainActivity.this, new OperateCenter.OnInitGloabListener() {
			
			@Override
			public void onUserAccountLogout(boolean arg0, int arg1) {
				
			}
			
			@Override
			public void onSwitchUserAccountFinished(User arg0) {
				
			}
			
			@Override
			public void onInitFinished(boolean isLogin, User userInfo) {
				assert(isLogin == mOperCenter.isLogin());
				MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
			}
		});
			}
		});
	}
	
	private int getOrientation() {
		int pos = mSp.getInt("orientation", 0);
		if (pos <0 || pos > 3)
			pos = 0;		
		return ORIENTION[pos];
		
	}
	
	private PopLogoStyle getPopStylePreference() {
		int pos = mSp.getInt("pop_style", 0);
		return POP_STYLES[pos];
		
	}
	
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mOperCenter.login(MainActivity.this, new OnLoginFinishedListener() {
					@Override
					public void onLoginFinished(boolean arg0, int resultCode, User userInfo) {
						final String uid     = userInfo.getUid();
						String staste = userInfo.getState();
						ReqTask reqTask = new ReqTask(new Delegate() {
							@Override
							public void execute(String result) {
								MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, uid);
							}
						}, "&uid=" + uid + "&state=" + staste, "http://123.207.146.159/4399sdk/login.php");
						reqTask.execute();
					}
				});
			}
		});
	}
	private void destroySDK(){
		if(mOperCenter != null){
			mOperCenter.destroy();
			mOperCenter = null;
		}
	}
	
	//退出游戏函数
	public void ExitSDK(){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mOperCenter.shouldQuitGame(MainActivity.this, new OnQuitGameListener() {
					
					@Override
					public void onQuitGame(boolean arg0) {
						if(arg0){
							destroySDK();
							finish();
						}
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
				// 生成mark, 支持字母数字和字符‘-’，‘_’，‘|’
				String mark = System.currentTimeMillis() + "|" + a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0];
				if (mark.length() > 32)
					mark = mark.substring(0, 32);
				String productName = "钻石";
				//mOperCenter.recharge -- 1:充值金额(元) mark:自定义订单号 productName : 商品名称
				mOperCenter.recharge(MainActivity.this, 1, mark, productName, new OnRechargeFinishedListener() {
					
					@Override
					public void onRechargeFinished(boolean success, int resultCode, String msg) {
						if(success){
							//充值成功
							MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
						}else {
							//充值失败
							MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
						}
					}
				});
			}
		});
		

	}
}
