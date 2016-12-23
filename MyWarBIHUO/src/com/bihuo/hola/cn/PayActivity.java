package com.bihuo.hola.cn;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PayActivity extends Activity {
	private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		
		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
				Button payBtn = (Button) findViewById(R.id.appay_btn);
				payBtn.setEnabled(false);
				Toast.makeText(PayActivity.this, "获取订单中....", Toast.LENGTH_SHORT).show();
				try {
				byte[] buf = Util.httpGet(url);
				if(buf != null && buf.length > 0){
					String content = new String(buf);
					JSONObject mJson;
						mJson = new JSONObject(content);
						if(mJson != null && !mJson.has("retcode")){
							PayReq req = new PayReq();
							req.appId         = mJson.getString("appid");
							req.partnerId     = mJson.getString("partnerid");
							req.prepayId      = mJson.getString("prepayid");
							req.nonceStr      = mJson.getString("noncestr");
							req.timeStamp     = mJson.getString("timestamp");
							req.packageValue  = mJson.getString("package");
							req.sign          = mJson.getString("sign");
							req.extData       = mJson.getString("app data");
							Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
						
						api.sendReq(req);
						}else{
							Log.d("PAY_GET", "返回错误" + mJson.getString("retmsg"));
							Toast.makeText(PayActivity.this, "返回错误"+ mJson.getString("retmsg"), Toast.LENGTH_SHORT).show();
						}
				}else{
					Log.d("PAY_GET", "服务器请求错误");
					Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
				}
				} catch (JSONException e) {
					Log.e("PAY_GET", "异常:" + e.getMessage());
					Toast.makeText(PayActivity.this, "异常:" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				payBtn.setEnabled(true);
			}
		});
	}

}
