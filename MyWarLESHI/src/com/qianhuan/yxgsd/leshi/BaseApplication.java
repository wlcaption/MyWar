package com.qianhuan.yxgsd.leshi;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.le.legamesdk.LeGameSDK;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeGameSDK.init(this);
        MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

