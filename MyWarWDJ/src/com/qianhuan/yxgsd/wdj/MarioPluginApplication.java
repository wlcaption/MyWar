package com.qianhuan.yxgsd.wdj;

import android.app.Application;
import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
/**
 * @author xiaowei
 *2016-10-11 上午11:00:10
 *
 */
public class MarioPluginApplication extends Application {
	private static final long  APP_KEY       = 100042954;
	private static final String SECURITY_KEY = "a0655cd13b85fb6121b22d3aaef5bb84";
	
	private static WandouGamesApi wandouGamesApi;
	
	public static WandouGamesApi getWandouGamesApi(){
		return wandouGamesApi;
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		wandouGamesApi.initPlugin(base, APP_KEY, SECURITY_KEY);
		super.attachBaseContext(base);
	}
	
	@Override
	public void onCreate() {
		wandouGamesApi = new WandouGamesApi.Builder(this, APP_KEY, SECURITY_KEY).create();
		wandouGamesApi.setLogEnabled(true);
		super.onCreate();
	}
	
}
