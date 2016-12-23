package com.qianhuan.yxgsd.huawei;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import com.android.huawei.pay.plugin.PayParameters;
import com.android.huawei.pay.util.HuaweiPayUtil;
import com.android.huawei.pay.util.Rsa;
import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.api.GameEventHandler;
import com.huawei.gameservice.sdk.api.PayResult;
import com.huawei.gameservice.sdk.api.Result;
import com.huawei.gameservice.sdk.api.UserResult;
import com.huawei.gameservice.sdk.model.RoleInfo;
import com.qianhua.yxgsd.net.huawei.ReqTask;
import com.qianhua.yxgsd.net.huawei.ReqTask.Delegate;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author xiaowei
 * 2016-10-31 上午10:39:10
 * wlcaption@qq.com
 */
public class MainActivity extends UnityPlayerActivity {

	private String buoyPrivateKey = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onResume() {
		super.onResume();
		GameServiceSDK.showFloatWindow(MainActivity.this);
		JPushInterface.onResume(MainActivity.this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		GameServiceSDK.hideFloatWindow(MainActivity.this);
		JPushInterface.onPause(MainActivity.this);
	}
	
	protected void onRestart() {
		super.onRestart();
	}
	
	
	protected void onStart() {
		super.onStart();
	}
	
	
	protected void onStop() {
		super.onStop();
	}	
	
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isTaskRoot()){
			GameServiceSDK.destroy(MainActivity.this);
		}
	}
	
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		ReqTask reqTask = new ReqTask(new Delegate() {
			@Override
			public void execute(String result) {
				buoyPrivateKey = result;
				Init();
			}
		}, null, GlobalParam.GET_BUOY_PRIVATE_KEY);
		reqTask.execute();
	}
	
	@SuppressLint("NewApi")
	public void Init() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				GameServiceSDK.init(MainActivity.this, GlobalParam.APP_ID, GlobalParam.PAY_ID, new GameEventHandler() {
					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							MessageHandle.resultCallBack(Util.User, UserWrapper.InitFalied, "");
							return;
						}else{
							MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
							checkUpdate();
						}
						//Login("");
					}
					
					@Override
					public String getGameSign(String appId, String cpId, String ts) {
						return createGameSign(appId+cpId+ts);
					}
				});
			}
		});
	}
	
	 /**
		 * 检测游戏更新
		 * check the update for game
		 */
	    private void checkUpdate()
	    {
			GameServiceSDK.checkUpdate(MainActivity.this, new GameEventHandler(){
	        	
				@Override
				public void onResult(Result result) {
					if(result.rtnCode != Result.RESULT_OK){
					}
				}
				
				@Override
				public String getGameSign(String appId, String cpId, String ts){
					return createGameSign(appId+cpId+ts);
				}
			});
		}
	
	
	private String createGameSign(String data){
		String str = data;
		try {
			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), buoyPrivateKey);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected boolean checkSign(String data, String gameAuthSign)
    {
        try
        {
            return RSAUtil.verify(data.getBytes("UTF-8"), GlobalParam.PAY_RSA_PUBLIC, gameAuthSign);
        }
        catch (Exception e)
        {
            return false;
        }
    }
	
	@SuppressLint("NewApi")
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GameServiceSDK.login(MainActivity.this, new GameEventHandler() {
					@Override
					public String getGameSign(String appId, String cpId, String ts) {
						return createGameSign(appId+cpId+ts);
					}

					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
						}else{
							UserResult userResult = (UserResult) result;
							if(userResult.isAuth != null && userResult.isAuth == 1)
		                    {
		                        boolean checkResult = checkSign(GlobalParam.APP_ID + userResult.ts + userResult.playerId, userResult.gameAuthSign);
		                        if (checkResult)
		                        {
		                            MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "");
		                        }
		                        else
		                        {
		                        	//需要做验证签名
									String appId = GlobalParam.APP_ID;
									final String userR = userResult.playerId;
									String ts    = userResult.ts;
									String gameSign = userResult.gameAuthSign;
									ReqTask reqTask = new ReqTask(new Delegate() {
										@Override
										public void execute(String result) {
											if(result.equals("true")){
												MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, userR);
												getPlayerLevel();
												UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AUTHSUCCESS");
											}else{
												MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "LoginFialed");
											}
										}
									}, "&appId="+ appId + "&userR=" + userR + "&ts=" + ts + "&gameSign=" + gameSign, GlobalParam.VALID_TOKEN_ADDR);
									reqTask.execute();
		                        }
		                    }else if(userResult.isChange != null && userResult.isChange == 1){
		                    UnityPlayer.UnitySendMessage("MainCamera","AndroidReceiveChangeUser",""); 
		                    }
		                    else
		                    {
		                    	getPlayerLevel();
		                        //MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "直接登录："+userResult.playerId);
		                    }
						}
					}
				}, 1);
			}
		});
	}
	
	private GameEventHandler payHandler =  new GameEventHandler() {
		
		@Override
		public void onResult(Result result) {
			Map<String, String> payResp = ((PayResult)result).getResultMap();
            // 支付成功，进行验签
            // payment successful, then check the response value
            if ("0".equals(payResp.get(PayParameters.returnCode)))
            {
                if ("success".equals(payResp.get(PayParameters.errMsg)))
                {
                    // 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
                	// If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
                	if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode")))
                    {
                        payResp.remove("isCheckReturnCode");
                        
                    }
                	// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
                	// If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
                	else
                    {
                        payResp.remove("isCheckReturnCode");
                        payResp.remove(PayParameters.returnCode);
                    }
                    // 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
                	// remove the param "sign" from response
                    String sign = payResp.remove(PayParameters.sign);
                    String noSigna = HuaweiPayUtil.getSignData(payResp);
                    // 使用公钥进行验签
                    // check the sign using RSA public key
                    boolean s = Rsa.doCheck(noSigna, sign, GlobalParam.PAY_RSA_PUBLIC);
                    
                    if (s)
                    {
                        MessageHandle.resultCallBack(Util.User,UserWrapper.PaySuccess ,"");
                    }
                    else
                    {
                    	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
                    }
                }
               
            }
            else if ("30002".equals(payResp.get(PayParameters.returnCode)))
            {
                //超时了
            }
		}
		
		@Override
		public String getGameSign(String arg0, String arg1, String arg2) {
			return null;
		}
	};
	
	@SuppressLint("NewApi")
	public void Pay(final String msg){
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAAAAAAAAAA"); 
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				 //TODO Auto-generated method stub
				UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","BBBBBBBBBBBBB"); 
				String a[] = msg.split("_");
				String price = a[0] + ".00";
				String oreder = System.currentTimeMillis()+"";
				String requestId =  oreder.substring(6, 12);
				GameBoxUtil.startPay(MainActivity.this, price, a[6], a[6], requestId+"_" + 
				a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0] , payHandler);
			}
		});
	}
	
	@SuppressLint("NewApi")
	public void showToolBar(){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				GameServiceSDK.showFloatWindow(MainActivity.this);
			}
		});
	}
	
	public void getPlayerLevel(){
		
		GameServiceSDK.getPlayerLevel(MainActivity.this,
				new GameEventHandler(){
					@Override
					public void onResult(Result result) {
						if(result.rtnCode != Result.RESULT_OK){
							//handleError("get player level failed:" + result.rtnCode);
							UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",result.rtnCode+""); 
							return;
						}
						UserResult userResult = (UserResult)result;
						UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",userResult.playerLevel+""); 
					}

					@Override
					public String getGameSign(String appId, String cpId,
							String ts) {
						return null;
					}
			
		});
	}
	
	public void LoginGameRole(String type,String msg){
		
		HashMap<String, String> playerInfo = new HashMap<String, String>();
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",type);
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","SUbmitData");
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",msg);
		String[] mstrs = msg.split("&");
		String mStri0[] = mstrs[0].split("=");
		String mStri1[] = mstrs[1].split("=");
		String mStri2[] = mstrs[2].split("=");
		String mStri3[] = mstrs[3].split("=");
		String mStri4[] = mstrs[4].split("=");
		String mStri5[] = mstrs[5].split("=");
		String mStri6[] = mstrs[6].split("=");
		
		playerInfo.put(RoleInfo.GAME_RANK, mStri2[1]);
		playerInfo.put(RoleInfo.GAME_ROLE, mStri1[1]);
		playerInfo.put(RoleInfo.GAME_AREA, mStri4[1]);
		playerInfo.put(RoleInfo.GAME_SOCIATY, "");
		GameServiceSDK.addPlayerInfo(MainActivity.this, playerInfo, new GameEventHandler() {
			
			@Override
			public void onResult(Result result) {
				if(result.rtnCode != Result.RESULT_OK){
					UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","ERROR");
				}
			}
			
			@Override
			public String getGameSign(String appId, String cpId, String ts) {
				return null;
			}
		});
		
//		rolerInfo.setRole_Id(mStri0[1]);
//		rolerInfo.setRole_Name(mStri1[1]);
//		rolerInfo.setRole_Balance("0");
//		rolerInfo.setRole_Vip(mStri6[1]);
//		rolerInfo.setRole_UserParty("无");
//		rolerInfo.setServer_Name(mStri4[1]);
//		rolerInfo.setServer_Id(mStri3[1]);
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
