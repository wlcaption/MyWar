package com.qianhuan.yxgsd.pyw;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.pengyouwan.sdk.api.OnSDKEventListener;
import com.pengyouwan.sdk.api.PYWPlatform;
import com.pengyouwan.sdk.api.SDKConfig;

import android.app.Application;
import android.os.Bundle;


/**
 * @author xiaowei
 * 2016-10-11 下午5:39:32
 *
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setGameKey("262c85bd17");
		PYWPlatform.initSDK(this, sdkConfig, new SDKEventListener(this));
			
	}

}
