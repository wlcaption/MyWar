package com.qianhuan.yxgsd.coolpad;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import com.coolcloud.uac.android.api.Coolcloud;
import com.coolcloud.uac.android.api.ErrInfo;
import com.coolcloud.uac.android.api.OnResultListener;
import com.coolcloud.uac.android.api.ResultFuture;
import com.coolcloud.uac.android.common.Constants;
import com.coolcloud.uac.android.common.Params;
import com.coolcloud.uac.android.gameassistplug.GameAssistApi;
import com.coolcloud.uac.android.gameassistplug.GameAssistApi.SwitchingAccount;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.iapppay.interfaces.authentactor.AccountBean;
import com.iapppay.interfaces.callback.IPayResultCallback;
import com.iapppay.sdk.main.CoolPadPay;
import com.iapppay.utils.RSAHelper;
import com.qianhua.yxgsd.net.coolpad.ReqTask;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends UnityPlayerActivity {
 int screenType = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
 private Coolcloud mCoolcloud;
 private GameAssistApi mGameAssistApi;
 
 private String accessToken;
 private String refreshToken;
 private String openid;
 private String mAuthcode;
 
 private String appuserid = "";
 
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCoolcloud = Coolcloud.get(this, PayConfig.appid);
		if(mGameAssistApi == null){
			mGameAssistApi = (GameAssistApi) mCoolcloud.getGameAssistApi(this);
			mGameAssistApi.addOnSwitchingAccountListen(new SwitchingAccount() {
				
				@Override
				public void onSwitchingAccounts() {
					doSwitchAccount();
				}
			});
		}
		//CoolPadPay.init(MainActivity.this, screenType, PayConfig.appid);
	}
	
	@Override
		protected void onResume() {
			super.onResume();
			if(mGameAssistApi != null){
				mGameAssistApi.onResume();
			}
		}
	
	@Override
		protected void onPause() {
			super.onPause();
			if(mGameAssistApi != null){
				mGameAssistApi.onPause();
			}
		}
	
	/***
	 * 切换账号
	 */
	
	private void doSwitchAccount(){
		Bundle mInput = new Bundle();
		mInput.putInt(Constants.KEY_SCREEN_ORIENTATION, screenType); //界面显示
		mInput.putString(Constants.KEY_SCOPE, "get_basic_userinfo"); //获取基本信息
		mInput.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE);//获取authcode
		
		mAuthcode = null;
		mCoolcloud.loginNew(MainActivity.this, mInput, new Handler(), new OnResultListener() {
			
			@Override
			public void onResult(Bundle arg0) {
				mAuthcode = arg0.getString(Constants.RESPONSE_TYPE_CODE);
			}
			
			@Override
			public void onError(ErrInfo arg0) {
				
			}
			
			@Override
			public void onCancel() {
				
			}
		});
		
	}
	
	/***
	 * 登录操作
	 * @param msg
	 */
	
	public void Login2(String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Bundle mInput = new Bundle();
				mInput.putInt(Constants.KEY_SCREEN_ORIENTATION, screenType);
				mInput.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
				mInput.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE);
				mAuthcode = null;
				mCoolcloud.login(MainActivity.this, mInput, new Handler(), new OnResultListener() {
					
					@Override
					public void onResult(Bundle arg0) {
						mAuthcode = arg0.getString(Constants.RESPONSE_TYPE_CODE);
						int code = Build.VERSION.SDK_INT;
					}
					
					@Override
					public void onError(ErrInfo arg0) {
						
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
		});
	}
	
	/***
	 * 初始化操作
	 * @param appid
	 * @param appkey
	 * @param privateKey
	 * @param oauthLoginServer
	 */
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer){
		CoolPadPay.init(MainActivity.this,screenType , PayConfig.appid);
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	private void callLogin(final String appid, final String appKey){
		mCoolcloud = Coolcloud.get(MainActivity.this, appid);
		Bundle input = new Bundle();
		input.putString(Constants.KEY_SCOPE, "get_basic_userinfo");
		input.putString(Constants.KEY_RESPONSE_TYPE, Constants.RESPONSE_TYPE_CODE);
		ResultFuture<Bundle> future = mCoolcloud.login(MainActivity.this, input, new Handler(), new OnResultListener() {
			@Override
			public void onResult(Bundle arg0) {
				//getTokenFromServer(appid, appKey, arg0.getString(Constants.RESPONSE_TYPE_CODE));
				ConnectHttp(appid, appKey, arg0.getString(Constants.RESPONSE_TYPE_CODE));
				String code =  arg0.getString(Params.KEY_AUTHCODE); //返回成功，获取授权码
			}
			
			@Override
			public void onError(ErrInfo arg0) {
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
			}
			
			@Override
			public void onCancel() {
				MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, "");
			}
		});
	}
	
	public void ConnectHttp(final String appid,final String appkey,final String code)
    {
		
    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      
            	try {
					JSONObject mJson = new JSONObject(buoyPrivateKey);
					accessToken = mJson.getString("access_token");
					refreshToken = mJson.getString("refresh_token");
					openid = mJson.getString("openid");
					MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, openid);
            	} catch (JSONException e) {
					e.printStackTrace();
				}
            }
        }, "grant_type=authorization_code&code=" + code + "&client_id="+ appid + "&client_secret=" +appkey +"&redirect_uri="+appkey , "http://123.207.146.159/coolpadsdk/login.php");
        getBuoyPrivate.execute();     
    }
	
	private void getTokenFromServer(final String appid,final String appkey,final String code) {
		   final String url = "https://openapi.coolyun.com/oauth2/token"; //发送请求
		   Thread thread = new Thread(new Runnable() {
				
				@SuppressLint("NewApi")
				@Override
				public void run() {
					String param = "grant_type=authorization_code&code=" + code + "&client_id="+ appid + "&client_secret=" +appkey +"&redirect_uri="+appkey;
					String result = HttpUtil.doPost(url, param);
					try {
						JSONObject jsObject = new JSONObject(result);
						/**登录令牌过期，要实现授权自动续期**/
						if (jsObject.has("error")) {
							String error = jsObject.getString("error");
							if (!TextUtils.isEmpty(error) && error.equals("2014")) {
								String prm = "grant_type=refresh_token&client_id=" + appid + "&client_secret=" +appkey + "&refresh_token" + refreshToken; 
								String reString = HttpUtil.doPost(url, prm);
								try {
									JSONObject jsonObject = new JSONObject(reString);
									accessToken = jsonObject.getString("access_token");
									refreshToken = jsonObject.getString("refresh_token");
									runOnUiThread(new Runnable() {
										
										@Override
										public void run() {
											Toast.makeText(MainActivity.this, "酷派账号登录成功", Toast.LENGTH_SHORT).show();								
										}
									});
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}else{
							/**没有过期，直接获取token**/
							openid = jsObject.getString("openid");
							accessToken = jsObject.getString("access_token");
							refreshToken = jsObject.getString("refresh_token");
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(MainActivity.this, "酷派账号登录成功", Toast.LENGTH_SHORT).show();								
								}
							});
							
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();
	   }
	
	/****
	 * 登录接口
	 * @param msg
	 */
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				callLogin(PayConfig.appid, PayConfig.appKey);
			}
		});
	}

	/***
	 * 支付接口
	 * @param msg
	 */
	public void Pay(final String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				/***
				 * 1、客户端下单模式
				 */
				String a[] = msg.split("_");
				String cpprivateinfo = a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0];
				String cporderid	 = System.currentTimeMillis() + "";
				int waresid          = 9;
				double price		 = 1;
				String appuserid     = System.currentTimeMillis() + "";
				String genUrl        = genUrl(PayConfig.appid, appuserid, cpprivateinfo, PayConfig.privateKey,waresid,price,cporderid);
				
				/**
				 * 2发起支付
				 */
				String coolPadAuthToken = accessToken;
				String coolPadAppid		= PayConfig.appid;
				String coolPadOpnID		= openid;
				AccountBean account 	= CoolPadPay.buildAccount(MainActivity.this, coolPadAuthToken, coolPadAppid, coolPadOpnID);
				CoolPadPay.startPay(MainActivity.this, genUrl,account, new IPayResultCallback() {
					@Override
					public void onPayResult(int resultCode, String signvalue, String resultInfo) {
						switch (resultCode) {
						case CoolPadPay.PAY_SUCCESS:
							dealPaySuccess(signvalue);
							MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, "");
							break;

						default:
							dealPayError(resultCode,resultInfo);
							MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
							break;
						}
					}
					private void dealPaySuccess(String signValue) {
						if (TextUtils.isEmpty(signValue)) {
							/**
							 *  没有签名值
							 */
							return;
						}
						boolean isvalid = false;
						try {
							isvalid = signCpPaySuccessInfo(signValue);
						} catch (Exception e) {
						
							isvalid = false;
						}
						if (isvalid) {
						} else {
						}
					}
					
					private boolean signCpPaySuccessInfo(String signValue) throws Exception {
						int transdataLast = signValue.indexOf("&sign=");
						String transdata = URLDecoder.decode(signValue.substring("transdata=".length(), transdataLast));
						
						int signLast = signValue.indexOf("&signtype=");
						String sign = URLDecoder.decode(signValue.substring(transdataLast+"&sign=".length(),signLast));
						
						String signtype = signValue.substring(signLast+"&signtype=".length());
						
						boolean isSign = RSAHelper.verify(transdata, PayConfig.publicKey, sign);
						if (signtype.equals("RSA") && isSign) {
						
							return true;
						}else{
						}
						return false;
					}
					
					private void dealPayError(int resultCode, String resultInfo) {
						MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
						
					}
				
				});
			} // Start Pay end 
		}); // run end
		
		
	} // Pay function end 
	
	
	private String genUrl( String appid, String appuserid, String cpprivateinfo, String appPrivateKey, int waresid, double price, String cporderid) {
		String json = "";

		JSONObject obj = new JSONObject();
		try {
			obj.put("appid", appid);
			obj.put("waresid", waresid);
			obj.put("cporderid", cporderid);
			obj.put("price", price);
			obj.put("appuserid", appuserid);

			
			/*CP私有信息，选填*/
			String cpprivateinfo0 = cpprivateinfo;
			if(!TextUtils.isEmpty(cpprivateinfo0)){
				obj.put("cpprivateinfo", cpprivateinfo0);
			}	
			
			/*支付成功的通知地址。选填。如果客户端不设置本参数，则使用服务端配置的地址。*/
//			String notifyurl0 = PayConfig.notifurl;
//			if(!TextUtils.isEmpty(notifyurl0)){
//				obj.put("notifyurl", notifyurl0);
//			}			
			json = obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = "";
		try {
			sign = RSAHelper.signForPKCS1(json, appPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "transdata=" + URLEncoder.encode(json) + "&sign=" + URLEncoder.encode(sign) + "&signtype=" + "RSA";
	}



	/**
	 * 本地退出接口
	 */
	public void ExitSDK(){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		        builder.setTitle("退出");
		        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                //退出apk
		                dialog.dismiss();
		                MainActivity.this.finish();
		            }
		        });
		        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                // dialog隐藏
		                dialog.dismiss();
		            }
		        });
		        builder.create().show();
			}
		});
		
	}
}
