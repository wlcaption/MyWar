package com.gionee.gamesdk.demo;

import android.app.Application;

import com.gionee.gamesdk.GamePlatform;
import com.qianhuan.yxgsd.am.MainActivity;

public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GamePlatform.init(this, Constants.API_KEY);
    }


}
