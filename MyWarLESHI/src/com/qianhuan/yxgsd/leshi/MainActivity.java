
package com.qianhuan.yxgsd.leshi;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.le.accountoauth.utils.LeUserInfo;
import com.le.legamesdk.LeGameSDK;
import com.le.legamesdk.LeGameSDK.ExitCallback;
import com.le.legamesdk.LeGameSDK.LoginCallback;
import com.le.legamesdk.LeGameSDK.PayCallback;
import com.le.legamesdk.utils.RandomUtil;
import com.letv.lepaysdk.smart.LePayInfo;
import com.qianhua.yxgsd.net.leshi.ReqTask;
import com.unity3d.player.UnityPlayerActivity;
import android.os.Bundle;

public class MainActivity extends UnityPlayerActivity {
	private LeGameSDK leGameSDK;
	private String userId  = "";
	private String accessToken = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		leGameSDK = LeGameSDK.getInstance();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		leGameSDK.onResume(this);
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		leGameSDK.onPause(this);
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		leGameSDK.onDestory(this);
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	public void Login(String msg){
		leGameSDK.doLogin(MainActivity.this, loginCallback, false);//最后一个参数是否切换账号
	}
	
	
	private LoginCallback loginCallback = new LoginCallback() {
		
		@Override
		public void onLoginSuccess(LeUserInfo userInfo) {
			if(userInfo != null){
				accessToken = userInfo.getAccessToken();
				userId      = userInfo.getUserId();
				ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
		        {            
		            @Override
		            public void execute(String buoyPrivateKey)
		            {      
		            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, userId);
		            }
		        }, "&uid=" + userId + "&access_token=" + accessToken , "http://123.207.146.159/legamesdk/login.php");
		        getBuoyPrivate.execute();   
			}else{
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
			}
		}
		
		@Override
		public void onLoginFailure(int errorCode, String errorMsg) {
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
		}
		
		@Override
		public void onLoginCancel() {
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
		}
	};
	

	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String a[]  = msg.split("_");
				LePayInfo lePayInfo = new LePayInfo();
				lePayInfo.setLetv_user_access_token(accessToken); //用户授权token,账号登录成功并授权后，返回授权token
				lePayInfo.setLetv_user_id(userId);				  //乐视集团用户id
				lePayInfo.setNotify_url("http://123.207.146.159/legamesdk/pay.php"); //支付成功之后，支付平台服务端通过此URL,异步通知CP支付结果
				lePayInfo.setCooperator_order_no(RandomUtil.getRandom());  //cp 自定义订单号
				lePayInfo.setPrice("1");                                  //交易金额
				lePayInfo.setProduct_name("钻石");		//商品名称
				lePayInfo.setProduct_desc("60钻石");		//商品描述
				
				lePayInfo.setPay_expire("21600");  //交易自动关闭时间
				lePayInfo.setProduct_id("2222");   //产品id由游戏方自己定义
				lePayInfo.setCurrency("RMB");      //货币单位
				lePayInfo.setProduct_urls(""); //商品图片URL
				lePayInfo.setExtro_info(a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0]);
				leGameSDK.doPay(MainActivity.this, lePayInfo, payCallback);
			}
		});
		
	}
	
	private PayCallback payCallback = new PayCallback() {
		@Override
		public void onPayResult(String status, String errorMessage) {
			if(status.equals("SUCCESS")){
				MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
				
			}else if(status.equals("FAILT")){
				MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
				
			}else if(status.equals("PAYED")){
				
			}else if(status.equals("WAITTING")){
				
			}else if(status.equals("CANCEL")){
				MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, "");
				
			}
		}
	};
	
	
	
	public void ExitSDK(){
		leGameSDK.onExit(MainActivity.this, exitCallback);
	}
	
	private ExitCallback exitCallback = new ExitCallback() {
		
		@Override
		public void onSdkExitConfirmed() {
			finish();
		}
		
		@Override
		public void onSdkExitCanceled() {
			
		}
	};
	
}
