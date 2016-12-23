package com.qianhuan.yxgsd;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.unity3d.player.UnityPlayerActivity;
public class MainActivity extends UnityPlayerActivity {
public static Handler hd = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SFOnlineHelper.onCreate(this);
		
		SFOnlineHelper.onCreate(this,new SFOnlineInitListener() {@Override
		public void onResponse(String tag, String value) {
			if(tag.equalsIgnoreCase("success")){
					Log.e("unity","success");
				UnityPlayer.UnitySendMessage("Main Camera", "InitResult", "success");
				MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
			}else if(tag.equalsIgnoreCase("fail")){
				Log.e("unity","fail");
				MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, "");
			}
			
		}});
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SFOnlineHelper.onDestroy(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		hd.postDelayed(new Runnable() {
			public void run() {
				SFOnlineHelper.onResume(MainActivity.this);
			}
		}, 1000);
	}
	
	@Override
	protected void onStop() { 
		super.onStop();
		SFOnlineHelper.onStop(this);
	}
	
	@Override
	protected void onPause() { 
		super.onPause();
		SFOnlineHelper.onPause(this);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		SFOnlineHelper.onRestart(this);
	}

}
