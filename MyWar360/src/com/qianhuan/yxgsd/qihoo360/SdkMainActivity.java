package com.qianhuan.yxgsd.qihoo360;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.qianhuan.yxgsd.qihoo360.payment.QihooPayInfo;
import com.qianhuan.yxgsd.qihoo360.utils.QihooUserInfo;
import com.qianhuan.yxgsd.qihoo360.utils.QihooUserInfoListener;
import com.qianhuan.yxgsd.qihoo360.utils.QihooUserInfoTask;
import com.qihoo.gamecenter.sdk.activity.ContainerActivity;
import com.qihoo.gamecenter.sdk.common.IDispatcherCallback;
import com.qihoo.gamecenter.sdk.matrix.Matrix;
import com.qihoo.gamecenter.sdk.protocols.CPCallBackMgr.MatrixCallBack;
import com.qihoo.gamecenter.sdk.protocols.ProtocolConfigs;
import com.qihoo.gamecenter.sdk.protocols.ProtocolKeys;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;

import com.unity3d.player.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;
/**
 * 绗竴涓猘ctivity锛屾彁渚涙í灞忓拰绔栧睆娓告垙鐨勫叆鍙�
 */
public class SdkMainActivity extends UnityPlayerActivity {

	protected String mAccessToken = null;
	
	private QihooUserInfo mUserInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);	        
    }
    
    protected void onDestroy() {	
    	Matrix.destroy(this);         
		super.onDestroy();
	}

	protected void onPause() {		
		super.onPause();
		JPushInterface.onPause(this);
		Matrix.onPause(this);
	}
	
	protected void onRestart() {		
		super.onRestart();
		Matrix.onRestart(this);
	}
	
	
	protected void onStart() {		
		super.onStart();
		Matrix.onStart(this);
	}
	
	
	protected void onStop() {		
		super.onStop();
		Matrix.onStop(this);
	}	

	protected void onResume() {		
		super.onResume();
		JPushInterface.onResume(this);
		Matrix.onResume(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Matrix.onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Matrix.onNewIntent(this, intent);
	}
	
	@SuppressLint("NewApi")
	public boolean getLandscape(Context context) {
    	if (context == null) {
    		return false;
    	}
    	boolean landscape = (this.getResources().getConfiguration().orientation
    			== Configuration.ORIENTATION_LANDSCAPE);
    	return landscape;
    }

	
	protected MatrixCallBack mSDKCallback = new MatrixCallBack() {
		@Override
		public void execute(Context context, int functionCode, String functionParams) {
			if (functionCode == ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT) {
				UnityPlayer.UnitySendMessage("MainCamera","AndroidReceiveChangeUser",""); 
				SwitchLogin(getLandscape(context));
			}else if (functionCode == ProtocolConfigs.FUNC_CODE_INITSUCCESS) {
				//这里返回成功之后才能调用SDK 其它接口
				MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
			}
		}

    };
	
	// sdk鍒濆鍖�
	@SuppressLint("NewApi")
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer)
	{
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Matrix.init(SdkMainActivity.this, mSDKCallback);
			}
		});
	}

	// 鐧诲綍
	public void Login(String LoginString)
	{			
		Intent intent = getLoginIntent(true);			
		IDispatcherCallback callback = mLoginCallback;			
		Matrix.execute(SdkMainActivity.this, intent, callback);
	}		

    
	//退出sdk
	public void ExitSDK()
	{
		doSdkQuit(true);
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	@SuppressLint("NewApi")
	public void LoginGameRole(final String type, final String msg){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",type);
				UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",msg);
				String[] mstrs = msg.split("&");
				String mStr0[] = mstrs[0].split("=");
				String mStr1[] = mstrs[1].split("=");
				String mStr2[] = mstrs[2].split("=");
				String mStr3[] = mstrs[3].split("=");
				String mStr4[] = mstrs[4].split("=");
				String mStr5[] = mstrs[5].split("=");
				String mStr6[] = mstrs[6].split("=");
				String mStr7[] = mstrs[7].split("=");
				if("create".equals(type)){
					HashMap eventParams = new HashMap();
					eventParams.put("zoneid",   mStr3[1]);
					eventParams.put("zonename", mStr4[1]);
					eventParams.put("roleid",   mStr0[1]);
					eventParams.put("rolename", mStr1[1]);
					eventParams.put("rolelevel",mStr2[1]);
					eventParams.put("vip",      mStr7[1]);
					eventParams.put("professionid", 0);
					eventParams.put("profession", "无");
					eventParams.put("gender", "无");
					eventParams.put("professionroleid", "无");
					eventParams.put("professionrolename", "无");
					eventParams.put("power", "无帮派");
					eventParams.put("balance", "无");
					eventParams.put("partyid", "0");
					eventParams.put("partyname", "无");
					eventParams.put("partyroleid", "0");
					eventParams.put("partyrolename", "无");
					eventParams.put("friendlist", "无");
					eventParams.put("ranking", "无");
					eventParams.put("type", "createRole");
					Matrix.statEventInfo(SdkMainActivity.this, eventParams);
				}
				if("enter".equals(type)){
					HashMap eventParams = new HashMap();
					eventParams.put("zoneid",    mStr3[1]);
					eventParams.put("zonename",  mStr4[1]);
					eventParams.put("roleid",    mStr0[1]);
					eventParams.put("rolename",  mStr1[1]);
					eventParams.put("rolelevel", mStr2[1]);
					eventParams.put("vip",       mStr7[1]);
					eventParams.put("professionid", 0);
					eventParams.put("profession", "无");
					eventParams.put("gender", "无");
					eventParams.put("professionroleid", "无");
					eventParams.put("professionrolename", "无");
					eventParams.put("power", "无帮派");
					eventParams.put("balance", "无");
					eventParams.put("partyid", "0");
					eventParams.put("partyname", "无");
					eventParams.put("partyroleid", "0");
					eventParams.put("partyrolename", "无");
					eventParams.put("friendlist", "无");
					eventParams.put("ranking", "无");
					eventParams.put("type", "enterServer");
					Matrix.statEventInfo(SdkMainActivity.this, eventParams);
				}
				
				if("levelup".equals(type)){
					HashMap eventParams = new HashMap();
					eventParams.put("zoneid",    mStr3[1]);
					eventParams.put("zonename",  mStr4[1]);
					eventParams.put("roleid",    mStr0[1]);
					eventParams.put("rolename",  mStr1[1]);
					eventParams.put("rolelevel", mStr2[1]);
					eventParams.put("vip",       mStr7[1]);
					eventParams.put("professionid", 0);
					eventParams.put("profession", "无");
					eventParams.put("gender", "无");
					eventParams.put("professionroleid", "无");
					eventParams.put("professionrolename", "无");
					eventParams.put("power", "无帮派");
					eventParams.put("balance", "无");
					eventParams.put("partyid", "0");
					eventParams.put("partyname", "无");
					eventParams.put("partyroleid", "0");
					eventParams.put("partyrolename", "无");
					eventParams.put("friendlist", "无");
					eventParams.put("ranking", "无");
					eventParams.put("type", "levelUp");
					Matrix.statEventInfo(SdkMainActivity.this, eventParams);
				}
				if("exit".equals(type)){
					HashMap eventParams = new HashMap();
					eventParams.put("zoneid",    mStr3[1]);
					eventParams.put("zonename",  mStr4[1]);
					eventParams.put("roleid",    mStr0[1]);
					eventParams.put("rolename",  mStr1[1]);
					eventParams.put("rolelevel", mStr2[1]);
					eventParams.put("vip",       mStr7[1]);
					eventParams.put("professionid", 0);
					eventParams.put("profession", "无");
					eventParams.put("gender", "无");
					eventParams.put("professionroleid", "无");
					eventParams.put("professionrolename", "无");
					eventParams.put("power", "无帮派");
					eventParams.put("balance", "无");
					eventParams.put("partyid", "0");
					eventParams.put("partyname", "无");
					eventParams.put("partyroleid", "0");
					eventParams.put("partyrolename", "无");
					eventParams.put("friendlist", "无");
					eventParams.put("ranking", "无");
					eventParams.put("type", "exitServer");
					Matrix.statEventInfo(SdkMainActivity.this, eventParams);
				}
			}
		});
	}
	
	public void doSdkQuit(boolean isLandScape){
		Bundle bundle = new Bundle();
		bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);
		bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_QUIT);
		
		Intent intent = new Intent(SdkMainActivity.this, ContainerActivity.class);
		intent.putExtras(bundle);
		
		Matrix.invokeActivity(SdkMainActivity.this, intent, mQuitCallback);
	}
	
	private IDispatcherCallback mQuitCallback = new IDispatcherCallback() {
		
		@SuppressLint("NewApi")
		@Override
		public void onFinished(String data) {
			JSONObject json;
			try {
				json = new JSONObject(data);
				int which = json.optInt("which",-1);
				String label = json.optString("label");
				switch (which) {
				case 0:
					return;

				default:
					finish();
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","ExitSDK");
		}
	};
	
	// 鐧诲嚭
	public void Logout()
	{
		
	}
	
	// 鏄惁宸茬粡鐧诲綍
	public boolean IsLogin() 
	{
		return false;		
	}
	
	//鑾峰彇鐢ㄦ埛id
	public String getUserID()
	{
		return String.valueOf(0);
	}
	
	//闅愯棌宸ュ叿鎸夐挳
	public void hideToolBar()
	{

	}
	
	//鏄剧ず宸ュ叿鎸夐挳
	public void showToolBar() 
	{
		
	}
	
	//杩涘叆鐢ㄦ埛涓績
	public void entryGameCenter() 
	{
		
	}
	
	// 切换账号的回调
    private IDispatcherCallback mAccountSwitchCallback = new IDispatcherCallback() {
		
    	 @Override
         public void onFinished(String data) {
             // press back
             if (isCancelLogin(data)) {
             	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, data);
                 return;
             }
             
             mAccessToken = parseAccessTokenFromLoginResult(data);
             
             UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","mAccessToken" + mAccessToken);
             

             if (!TextUtils.isEmpty(mAccessToken)) {
                 // 闇�瑕佸幓搴旂敤鐨勬湇鍔″櫒鑾峰彇鐢╝ccess_token鑾峰彇涓�涓嬪甫qid鐨勭敤鎴蜂俊鎭�
                 getUserInfo();
             } else {
             	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, data);
                 //Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
             }            
         }
	};
	
	//public void SwitchLogin(boolean isLandScape){
	
	public void ChangeUser(){
		SwitchLogin(true);
		//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceiveChangeUser",""); 
	}
	
	public void SwitchLogin(boolean isLandScape){
		Intent intent = getSwitchAccountIntent(isLandScape);
		Matrix.invokeActivity(SdkMainActivity.this, intent, mAccountSwitchCallback);
	}
	

	//支付
	public void Pay(String PayString)
	{
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAA");
		String a[] = PayString.split("_");  //Product_Price Product_Name Product_Id Role_Name Role_Id OrderId EXT
		UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAABBBBBB");
		// 鍒涘缓QihooPay
        QihooPayInfo qihooPay = new QihooPayInfo();	        
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAACCCCCC");
        qihooPay.setAccessToken(mAccessToken);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAACCCCCCEEEEEe");
        qihooPay.setQihooUserId(mUserInfo.getId());
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAADDDDDDd");
        qihooPay.setMoneyAmount(a[0]);
        qihooPay.setExchangeRate("1");
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAEEEEEEEEe");
        qihooPay.setProductName(a[1]);
        qihooPay.setProductId(a[2]);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAFFFFFFFFFF");
        qihooPay.setNotifyUri("http://123.207.146.159/360sdk/pay.php");
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAGGGGGGG");
        qihooPay.setAppName("一血敢死队");
        qihooPay.setAppUserName(a[3]);
        qihooPay.setAppUserId(a[4]);
        qihooPay.setAppOrderId(a[5]);
        qihooPay.setAppExt1(a[6]);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAGHHHHHHH");
		
        Intent intent = getPayIntent(true, qihooPay);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAAOOOOO"); 
        // 蹇呴渶鍙傛暟锛屼娇鐢�360SDK鐨勬敮浠樻ā鍧椼��
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAATTTTTTTTT");
        // 鍚姩鎺ュ彛
        Matrix.invokeActivity(this, intent, mPayCallback);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","AAAAAAAAAArrrrrr");
	}
	
	/***
     * 生成调用360SDK切换账号接口的Intent
     *
     * @param isLandScape 是否横屏
     * @return Intent
     */
    private Intent getSwitchAccountIntent(boolean isLandScape) {

        Intent intent = new Intent(this, ContainerActivity.class);

        // 必须参数，360SDK界面是否以横屏显示。
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 必需参数，使用360SDK的切换账号模块。
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_SWITCH_ACCOUNT);

        //是否显示关闭按钮
        intent.putExtra(ProtocolKeys.IS_LOGIN_SHOW_CLOSE_ICON, false);

        // 可选参数，是否支持离线模式，默认值为false
        intent.putExtra(ProtocolKeys.IS_SUPPORT_OFFLINE, false);

        // 可选参数，是否隐藏欢迎界面
        intent.putExtra(ProtocolKeys.IS_HIDE_WELLCOME, false);
        return intent;
    }

	
	
	
	/**
     * 鐢熸垚璋冪敤360SDK鐧诲綍鎺ュ彛鐨処ntent
     * @param isLandScape 鏄惁妯睆
     * @return intent
     */
    private Intent getLoginIntent(boolean isLandScape) {

        Intent intent = new Intent(this, ContainerActivity.class);

        // 鐣岄潰鐩稿叧鍙傛暟锛�360SDK鐣岄潰鏄惁浠ユí灞忔樉绀恒��
        intent.putExtra(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // 蹇呴渶鍙傛暟锛屼娇鐢�360SDK鐨勭櫥褰曟ā鍧椼��
        intent.putExtra(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_LOGIN);

        //鏄惁鏄剧ず鍏抽棴鎸夐挳
        intent.putExtra(ProtocolKeys.IS_LOGIN_SHOW_CLOSE_ICON, false);

        // 鍙�夊弬鏁帮紝鏄惁鏀寔绂荤嚎妯″紡锛岄粯璁ゅ�间负false
        intent.putExtra(ProtocolKeys.IS_SUPPORT_OFFLINE, false);

        // 鍙�夊弬鏁帮紝鏄惁鍦ㄨ嚜鍔ㄧ櫥褰曠殑杩囩▼涓樉绀哄垏鎹㈣处鍙锋寜閽�
        intent.putExtra(ProtocolKeys.IS_SHOW_AUTOLOGIN_SWITCH, false);

        // 鍙�夊弬鏁帮紝鏄惁闅愯棌娆㈣繋鐣岄潰
        intent.putExtra(ProtocolKeys.IS_HIDE_WELLCOME, true);
        
        return intent;
    }
    
 // 鐧诲綍銆佹敞鍐岀殑鍥炶皟
    private IDispatcherCallback mLoginCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
            // press back
            if (isCancelLogin(data)) {
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, data);
                return;
            }
            
            mAccessToken = parseAccessTokenFromLoginResult(data);
            
            UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","mAccessToken" + mAccessToken);
            

            if (!TextUtils.isEmpty(mAccessToken)) {
                // 闇�瑕佸幓搴旂敤鐨勬湇鍔″櫒鑾峰彇鐢╝ccess_token鑾峰彇涓�涓嬪甫qid鐨勭敤鎴蜂俊鎭�
                getUserInfo();
            } else {
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, data);
                //Toast.makeText(SdkUserBaseActivity.this, "get access_token failed!", Toast.LENGTH_LONG).show();
            }            
        }
    };
    
    private void getUserInfo() {
        
        final QihooUserInfoTask mUserInfoTask = QihooUserInfoTask.newInstance();
       
        // 璇锋眰搴旂敤鏈嶅姟鍣紝鐢ˋccessToken鎹㈠彇UserInfo
        mUserInfoTask.doRequest(this, mAccessToken, "e19f7fb2767272df364708f705eaa7bb", new QihooUserInfoListener() {
            @Override
            public void onGotUserInfo(QihooUserInfo userInfo) {	                
                if (null == userInfo) {
                    MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "get userinfo err");
                	//Toast.makeText(SdkUserBaseActivity.this, "浠庡簲鐢ㄦ湇鍔″櫒鑾峰彇鐢ㄦ埛淇℃伅澶辫触", Toast.LENGTH_LONG).show();
                }
                else if( !userInfo.isValid())
                {
                	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "userInfo id not  valid");
                }
                else {
                	mUserInfo = userInfo;
                	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, userInfo.getId());
                    //SdkUserBaseActivity.this.onGotUserInfo(userInfo);
                }
            }
        });
    }	    
    
    private boolean isCancelLogin(String data) {
        try {
            JSONObject joData = new JSONObject(data);
            int errno = joData.optInt("errno", -1);
            if (-1 == errno) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
    
    private String parseAccessTokenFromLoginResult(String loginRes) {
        try {

            JSONObject joRes = new JSONObject(loginRes);
            JSONObject joData = joRes.getJSONObject("data");
            return joData.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    
    
    /***
     * 鐢熸垚璋冪敤360SDK鏀粯鎺ュ彛鐨処ntent
     *
     * @param isLandScape
     * @param pay
     * @return Intent
     */
    protected Intent getPayIntent(boolean isLandScape, QihooPayInfo pay) {
        Bundle bundle = new Bundle();

        // 鐣岄潰鐩稿叧鍙傛暟锛�360SDK鐣岄潰鏄惁浠ユí灞忔樉绀恒��
        bundle.putBoolean(ProtocolKeys.IS_SCREEN_ORIENTATION_LANDSCAPE, isLandScape);

        // *** 浠ヤ笅闈炵晫闈㈢浉鍏冲弬鏁� ***

        // 璁剧疆QihooPay涓殑鍙傛暟銆�

        // 蹇呴渶鍙傛暟锛�360璐﹀彿id锛屾暣鏁般��
        bundle.putString(ProtocolKeys.QIHOO_USER_ID, pay.getQihooUserId());

        // 蹇呴渶鍙傛暟锛屾墍璐拱鍟嗗搧閲戦, 浠ュ垎涓哄崟浣嶃�傞噾棰濆ぇ浜庣瓑浜�100鍒嗭紝360SDK杩愯瀹氶鏀粯娴佺▼锛� 閲戦鏁颁负0锛�360SDK杩愯涓嶅畾棰濇敮浠樻祦绋嬨��
        bundle.putString(ProtocolKeys.AMOUNT, pay.getMoneyAmount());

        // 蹇呴渶鍙傛暟锛屾墍璐拱鍟嗗搧鍚嶇О锛屽簲鐢ㄦ寚瀹氾紝寤鸿涓枃锛屾渶澶�10涓腑鏂囧瓧銆�
        bundle.putString(ProtocolKeys.PRODUCT_NAME, pay.getProductName());

        // 蹇呴渶鍙傛暟锛岃喘涔板晢鍝佺殑鍟嗗搧id锛屽簲鐢ㄦ寚瀹氾紝鏈�澶�16瀛楃銆�
        bundle.putString(ProtocolKeys.PRODUCT_ID, pay.getProductId());

        // 蹇呴渶鍙傛暟锛屽簲鐢ㄦ柟鎻愪緵鐨勬敮浠樼粨鏋滈�氱煡uri锛屾渶澶�255瀛楃銆�360鏈嶅姟鍣ㄥ皢鎶婃敮浠樻帴鍙ｅ洖璋冪粰璇ri锛屽叿浣撳崗璁鏌ョ湅鏂囨。涓紝鏀粯缁撴灉閫氱煡鎺ュ彛鈥撳簲鐢ㄦ湇鍔″櫒鎻愪緵鎺ュ彛銆�
        bundle.putString(ProtocolKeys.NOTIFY_URI, pay.getNotifyUri());

        // 蹇呴渶鍙傛暟锛屾父鎴忔垨搴旂敤鍚嶇О锛屾渶澶�16涓枃瀛椼��
        bundle.putString(ProtocolKeys.APP_NAME, pay.getAppName());

        // 蹇呴渶鍙傛暟锛屽簲鐢ㄥ唴鐨勭敤鎴峰悕锛屽娓告垙瑙掕壊鍚嶃�� 鑻ュ簲鐢ㄥ唴缁戝畾360璐﹀彿鍜屽簲鐢ㄨ处鍙凤紝鍒欏彲鐢�360鐢ㄦ埛鍚嶏紝鏈�澶�16涓枃瀛椼�傦紙鍏呭�间笉鍒嗗尯鏈嶏紝
        // 鍏呭埌缁熶竴鐨勭敤鎴疯处鎴凤紝鍚勫尯鏈嶈鑹插潎鍙娇鐢級銆�
        bundle.putString(ProtocolKeys.APP_USER_NAME, pay.getAppUserName());

        // 蹇呴渶鍙傛暟锛屽簲鐢ㄥ唴鐨勭敤鎴穒d銆�
        // 鑻ュ簲鐢ㄥ唴缁戝畾360璐﹀彿鍜屽簲鐢ㄨ处鍙凤紝鍏呭�间笉鍒嗗尯鏈嶏紝鍏呭埌缁熶竴鐨勭敤鎴疯处鎴凤紝鍚勫尯鏈嶈鑹插潎鍙娇鐢紝鍒欏彲鐢�360鐢ㄦ埛ID鏈�澶�32瀛楃銆�
        bundle.putString(ProtocolKeys.APP_USER_ID, pay.getAppUserId());

        // 鍙�夊弬鏁帮紝搴旂敤鎵╁睍淇℃伅1锛屽師鏍疯繑鍥烇紝鏈�澶�255瀛楃銆�
        bundle.putString(ProtocolKeys.APP_EXT_1, pay.getAppExt1());

        // 鍙�夊弬鏁帮紝搴旂敤鎵╁睍淇℃伅2锛屽師鏍疯繑鍥烇紝鏈�澶�255瀛楃銆�
        bundle.putString(ProtocolKeys.APP_EXT_2, pay.getAppExt2());

        // 鍙�夊弬鏁帮紝搴旂敤璁㈠崟鍙凤紝搴旂敤鍐呭繀椤诲敮涓�锛屾渶澶�32瀛楃銆�
        bundle.putString(ProtocolKeys.APP_ORDER_ID, pay.getAppOrderId());

        // 蹇呴渶鍙傛暟锛屼娇鐢�360SDK鐨勬敮浠樻ā鍧椼��
        bundle.putInt(ProtocolKeys.FUNCTION_CODE, ProtocolConfigs.FUNC_CODE_PAY);

        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtras(bundle);

        return intent;
    }
    
 // 鏀粯鐨勫洖璋�
    protected IDispatcherCallback mPayCallback = new IDispatcherCallback() {

        @Override
        public void onFinished(String data) {
//            Log.d(TAG, "mPayCallback, data is " + data);
            if(TextUtils.isEmpty(data)) {
                return;
            }
            
            JSONObject jsonRes;
            try {
                jsonRes = new JSONObject(data);
                // error_code 鐘舵�佺爜锛� 0 鏀粯鎴愬姛锛� -1 鏀粯鍙栨秷锛� 1 鏀粯澶辫触锛� -2 鏀粯杩涜涓�, 4010201鍜�4009911 鐧诲綍鐘舵�佸凡澶辨晥锛屽紩瀵肩敤鎴烽噸鏂扮櫥褰�
                // error_msg 鐘舵�佹弿杩�
                int errorCode = jsonRes.optInt("error_code");	                
                switch (errorCode) {
                    case 0:
                    	MessageHandle.resultCallBack(Util.IAP, UserWrapper.PaySuccess,"");  
                    	break;
                    case 1:
                    	MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed,"pay failed");  
                    	break;
                    case -1:
                    	MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayCancel,""); 
                    	break;
                    case -2:                      
                        String errorMsg = jsonRes.optString("paying");
                        MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, errorMsg);
                        break;	                    	                        
                    case 4010201:
                        //acess_token澶辨晥	                        
                    	MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, "acess_token err");
                        break;
                    case 4009911:
                        //QT澶辨晥
                    	MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, "QT err");
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }	            
        }
    };

    // payment end
}

