package com.qianhuan.wpgsd.mi;

import java.util.HashMap;
import java.util.Random;

import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.unity3d.player.UnityPlayerActivity;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

public class MainActivity extends UnityPlayerActivity implements OnLoginProcessListener, OnPayProcessListener
{
	
	public static MiAppInfo mAppInfo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}
	
	protected void onDestroy() {		
		super.onDestroy();
	}
	
	protected void onPause() {		
		super.onPause();
		JPushInterface.onPause(this);
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

	protected void onResume() {		
		super.onResume();
		JPushInterface.onResume(this);
	}	
	
	
	/*
	 * 鐧婚檰鍥炶皟
	 * **/
	@Override
	public void finishLoginProcess(int arg0, MiAccountInfo arg1)
	{
		if (MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS == arg0)
		{
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, String.valueOf(arg1.getUid()));
			//SendMessage("OnLoginSuccess",	arg1.getUid() + ":"  + arg1.getNikename());
		}
		else if (MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_SUCCESS == arg0)
		{
			MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, String.valueOf(arg1.getUid()));
			//SendMessage("OnLoginFailed", "MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_SUCCESS");
		}
		else if (MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_FAIL == arg0)
		{
			MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutFailed, "MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_FAIL");
			//SendMessage("OnLoginFailed", "MI_XIAOMI_GAMECENTER_ERROR_LOGINOUT_FAIL");
		}
		else if (MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED == arg0)
		{
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED");
			//SendMessage("OnLoginFailed", "MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED");
		}
		else
		{
			MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, "else...");
			//SendMessage("OnLoginFailed", "else...");
		}
	}
	

	/*
	 * (non-Javadoc)鏀粯鍥炶皟
	 * @see com.xiaomi.gamecenter.sdk.OnPayProcessListener#finishPayProcess(int)
	 */
	@Override
	public void finishPayProcess(int arg0)
	{
		if (arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS)// 閹存劕濮�
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PaySuccess, String.valueOf(0));
			//SendMessage("OnPayResultSuccess", "");
		}
		else if (arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_CANCEL
				|| arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL)// 閸欐牗绉�
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayCancel, String.valueOf(arg0));
			//SendMessage("OnPayResultFailed", "" + arg0);
		}
		else if (arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE)// 婢惰精瑙�
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, String.valueOf(arg0));
			//SendMessage("OnPayResultFailed", "" + arg0);
		}
		else if (arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_REPEAT)
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, String.valueOf(arg0));
			//SendMessage("OnPayResultFailed", "" + arg0);
		}
		else if (MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED == arg0)
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, String.valueOf(arg0));
			//SendMessage("OnPayResultFailed", "" + arg0);
		}
		else if (arg0 == MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL)
		{
			MessageHandle.resultCallBack(Util.IAP, UserWrapper.PayFailed, String.valueOf(arg0));
			//SendMessage("OnPayResultFailed", "" + arg0);
		}
	}
	

	// sdk鍒濆鍖�
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer)
	{
		/** SDK閸掓繂顫愰崠锟� */
		mAppInfo = new MiAppInfo();
		mAppInfo.setAppId( appid );
		mAppInfo.setAppKey( appkey );
		MiCommplatform.Init( this, mAppInfo );		
	
	}

	// 鐧诲綍
	public void Login(String LoginString)
	{
		MiCommplatform.getInstance().miLogin(MainActivity.this,MainActivity.this);
	}

	// 鐧诲嚭
	public void Logout()
	{
		// 璨屼技娌℃壘鍒板皬绫崇殑鐧诲嚭鎺ュ彛	
	}
	
	// 鏄惁宸茬粡鐧诲綍
	public boolean IsLogin() 
	{
		return MiCommplatform.getInstance().isMiAccountLogin();		
	}
	
	//鑾峰彇鐢ㄦ埛id
	public String getUserID()
	{
		return String.valueOf(MiCommplatform.getInstance().getMiAccountInfo().getUid());
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
	
	//鏀粯
	public void Pay(String PayString)
	{
		
		String a[] = PayString.split("_");  //diamond, zoneid, guid, name, level, Product_Name, paytype, vip0, orderid, ext
				
		// !< -- TODO.绾喛顓婚崗鏈电铂娣団剝浼呴弰顖氭儊韫囧懘銆忕拋鍓х枂? --
		MiBuyInfo miBuyInfo= new MiBuyInfo();
		miBuyInfo.setCpOrderId(System.currentTimeMillis() + new Random().nextInt(1000) + a[2]);//鐠併垹宕熼崣宄版暜娑擄拷閿涘牅绗夋稉铏光敄閿涳拷
		miBuyInfo.setCpUserInfo(a[2] + "-" + a[1] + "-" + a[6] + "-" + a[0]); //濮濄倕寮弫鏉挎躬閻€劍鍩涢弨顖欑帛閹存劕濮涢崥搴濈窗闁繋绱剁紒姗烶閻ㄥ嫭婀囬崝鈥虫珤
		miBuyInfo.setAmount(Integer.parseInt(a[0])); //韫囧懘銆忛弰顖氥亣娴滐拷1閻ㄥ嫭鏆ｉ弫甯礉10娴狅綀銆�10缁啿绔甸敍灞藉祮10閸忓啩姹夊鎴濈閿涘牅绗夋稉铏光敄閿涳拷
		//閻€劍鍩涙穱鈩冧紖閿涘瞼缍夊〒绋跨箑妞ゆ槒顔曠純顔猴拷浣稿礋閺堢儤鐖堕幋蹇斿灗鎼存梻鏁ら崣顖烇拷锟�**
		Bundle mBundle = new Bundle();
		mBundle.putString( GameInfoField.GAME_USER_BALANCE, "1000" );   //閻€劍鍩涙担娆擃杺
		mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, a[7]);  //vip缁涘楠�
		mBundle.putString( GameInfoField.GAME_USER_LV, a[4]);           //鐟欐帟澹婄粵澶岄獓
		mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, a[5] );  //瀹搞儰绱伴敍灞藉簻濞诧拷
		mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, a[3] ); //鐟欐帟澹婇崥宥囆�
		mBundle.putString( GameInfoField.GAME_USER_ROLEID, a[2] );    //鐟欐帟澹奿d
		mBundle.putString(GameInfoField.GAME_USER_SERVER_NAME, a[1]);  //閹碉拷閸︺劍婀囬崝鈥虫珤
		miBuyInfo.setExtraInfo(mBundle); //鐠佸墽鐤嗛悽銊﹀煕娣団剝浼�
		MiCommplatform.getInstance().miUniPay(MainActivity.this, miBuyInfo,MainActivity.this);
	}
}
