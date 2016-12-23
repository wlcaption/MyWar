package com.qianhuan.yxgsd.tt;

import com.wett.cooperation.container.TTSDKV2;
import android.app.Application;


/**
 * @author xiaowei
 * 2016-10-17 下午5:47:02
 * wlcaption@qq.com
 */
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		TTSDKV2.getInstance().prepare(MyApplication.this);
	}
}
