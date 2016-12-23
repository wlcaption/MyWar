package com.qianhuan.yxgsd.pyw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.pengyouwan.sdk.api.ISDKEventCode;
import com.pengyouwan.sdk.api.ISDKEventExtraKey;
import com.pengyouwan.sdk.api.OnSDKEventListener;
import com.pengyouwan.sdk.api.User;
import com.pengyouwan.sdk.utils.FloatViewTool;
import com.qianhua.yxgsd.net.pyw.ReqTask;
import com.qianhua.yxgsd.net.pyw.ReqTask.Delegate;


/**
 * @author xiaowei
 * 2016-10-11 下午6:19:06
 *
 */
public class SDKEventListener implements OnSDKEventListener {
	private Context mContext;
	
	public SDKEventListener(Context context){
		mContext = context;
	}
	@Override
	public void onEvent(int eventCode, Bundle data) {
		switch (eventCode) {
		case ISDKEventCode.CODE_LOGIN_SUCCESS:
			if(data != null){
				User user = (User) data.getSerializable(ISDKEventExtraKey.EXTRA_USER);
				if(user != null){
					final String userId = user.getUserId();
					String token  = user.getToken();
					ReqTask reqTask = new ReqTask(new Delegate() {
						@Override
						public void execute(String result) {
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, userId);
						}
					}, "&userid=" + userId + "&token=" + token, "http://123.207.146.159/pengyouwansdk/login.ph");
					reqTask.execute();
				}
			}
			break;
		case ISDKEventCode.CODE_LOGIN_FAILD:
			String errorMsg = data.getString(ISDKEventExtraKey.EXTRA_ERRO);
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
			break;
		case ISDKEventCode.CODE_CHARGE_SUCCESS:
			String sucessMsg = data.getString(ISDKEventExtraKey.EXTRA_ORDERID);
			MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
			break;
		case ISDKEventCode.CODE_CHARGE_CANCEL:
			String cancelPayMsg = data.getString(ISDKEventExtraKey.EXTRA_ORDERID);
			MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
			break;
		case ISDKEventCode.CODE_CHARGE_FAIL:
			String erroePayMsg = data.getString(ISDKEventExtraKey.EXTRA_ORDERID);
			MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
			break;
		case ISDKEventCode.CODE_LOGOUT:
			break;
		case ISDKEventCode.CODE_EXIT:
			break;
		}
	}
}
