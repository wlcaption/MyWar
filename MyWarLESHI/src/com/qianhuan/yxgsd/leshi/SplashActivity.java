package com.qianhuan.yxgsd.leshi;

import com.unity3d.player.UnityPlayerActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author xiaowei
 * 2016-10-14 下午3:15:53
 * 闪屏界面
 */
public class SplashActivity extends UnityPlayerActivity {
	private static int END  = 1;
	private static int TIME = 3000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		endSplashActivity();
	}
	private void endSplashActivity() {
		myHandler.sendEmptyMessageAtTime(END, TIME);
	}
	
	Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			startActivity(new Intent(SplashActivity.this,MainActivity.class));
		};
	};

}
