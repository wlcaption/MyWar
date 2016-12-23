package com.qianhuan.yxgsd.mz;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;

import android.app.Application;

public class GameApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		// TODO 游戏初始化时，初始化参数
		MzGameCenterPlatform.init(this, GameConstants.GAME_ID, GameConstants.GAME_KEY);
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "SUCCESS");
	}

}
