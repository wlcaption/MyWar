package com.bihuo.hola.cn;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(com.tencent.mm.sdk.modelbase.BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(com.tencent.mm.sdk.modelbase.BaseResp arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
