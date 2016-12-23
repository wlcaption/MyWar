package com.qianhuan.yxgsd.uc.uc;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.uc.gamesdk.UCGameSdk;

import com.qianhuan.yxgsd.uc.UCGameSDK;
import com.unity3d.player.UnityPlayer;

import org.json.JSONObject;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class UnityPlayerActivity extends Activity
{
	protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		setContentView(mUnityPlayer);
		mUnityPlayer.requestFocus();
	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
	}

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
		JPushInterface.onPause(this);
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
		JPushInterface.onResume(this);
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer)
	{
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAAAAAAAAAA");   
		UCGameSDK.initSDK(false, 2, 725637, 0, 1, true, true);
	}
	
	public void Login(String LoginString)
	{
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","BBBBBBBBBBBBBBBBBBBB");
		UCGameSDK.login(false, "");
	}
	
	public void Logout()
	{
		UCGameSDK.logout();
	}
	
	public void Pay(String PayString)
	{
		String a[] = PayString.split("_");  //diamond, zoneid, guid, name, level, paytype
		
    	UCGameSDK.pay(Float.parseFloat(a[0]), 0, a[2], a[3], a[4], a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0], "http://123.207.146.159/ucsdk/pay.php", a[5]);
	}
	
	public void ExitSDK()
	{
		UCGameSDK.exitSDK();
	}
	
	public String getUserID()
	{
		return String.valueOf(UCGameSDK.getSid());
	}
	
	public void hideToolBar()
	{
		UCGameSDK.hideFloatButton();		
	}
		
	//显示工具按钮
	public void showToolBar() 
	{
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","BBBBBBBBBBBBBBBBBBBB");
		UCGameSDK.showFloatButton(0, 0);
	}
	
	public void createToolBar() 
	{
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","BBBBBBBBBBBBBBBBBBBB");
		UCGameSDK.createFloatButton();
	}
	
	public void EnterGameCenter() 
	{
		UCGameSDK.enterUserCenter();
	}	
	
	public void LoginGameRole(String type,String msg) {
		
		HashMap<String,String> mHash = new HashMap<String,String>();
		String[] mStr =  msg.split("&");		
		
		for(int i = 0;i<mStr.length;i++)
		{
			String[] mStri = mStr[i].split("=");
			mHash.put(mStri[0], mStri[1]);
		}
		
		JSONObject jsonObj = new JSONObject(mHash);
		UCGameSdk.defaultSdk().submitExtendData(type, jsonObj);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
