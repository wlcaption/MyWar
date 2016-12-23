package com.tencent.tmgp.yxgsd;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.tencent.tmgp.yxgsd.net.ReqTask;
import com.tencent.tmgp.yxgsd.net.ReqTask.Delegate;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.BaseRet;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.UserLoginRet;

import com.unity3d.player.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;


public class MainActivity extends UnityPlayerActivity {

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (YSDKApi.isDifferentActivity(this)) {
            this.finish();
            return;
        }
        
        YSDKApi.onCreate(this);
        
        YSDKApi.setUserListener(new YSDKCallback(this));
        YSDKApi.setBuglyListener(new YSDKCallback(this));
        
        YSDKApi.handleIntent(this.getIntent());
    }    
    
 // TODO GAME 游戏需要集成此方法并调用YSDKApi.onRestart()
    @Override
    protected void onRestart() {
        super.onRestart();
        YSDKApi.onRestart(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onResume()
    @Override
    protected void onResume() {
        super.onResume();
        YSDKApi.onResume(this);
        JPushInterface.onResume(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onPause()
    @Override
    protected void onPause() {
        super.onPause();
        YSDKApi.onPause(this);
        JPushInterface.onPause(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onStop()
    @Override
    protected void onStop() {
        super.onStop();
        YSDKApi.onStop(this);
    }

    // TODO GAME 游戏需要集成此方法并调用YSDKApi.onDestory()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        YSDKApi.onDestroy(this);        
    }

    // TODO GAME 在onActivityResult中需要调用YSDKApi.onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        YSDKApi.onActivityResult(requestCode, resultCode, data);        
    }

    // TODO GAME 在onNewIntent中需要调用handleCallback将平台带来的数据交给YSDK处理
    @Override
    protected void onNewIntent(Intent intent) {        
        super.onNewIntent(intent);
        // TODO GAME 处理游戏被拉起的情况        
        YSDKApi.handleIntent(intent);
    }
    
    public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer)
	{
    	if (getPlatform() == ePlatform.None) {
    		MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
    	}
    	else if (getPlatform() == ePlatform.QQ) {    		
    		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "QQ");
    	}
    	else if (getPlatform() == ePlatform.WX) {    		
    		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "WX");
    	}
	}
    
    public void HuaWeiSDKShow()
    {
    	
    }
    
    
    public void Logout()
    {
    	letUserLogout();
    }
    
    public void CheckBill(String userip)
    {
    	ConnectHttp(userip, "balance_m");
    }
    
    public void ConnectHttp(String userip, String account_type)
    {
    	//balance_m查  pay_m支付成功
    	UserLoginRet retUser = new UserLoginRet();
        int platform = YSDKApi.getLoginRecord(retUser);
        String accessToken = retUser.getAccessToken();
        String pay_token = retUser.getPayToken();
        String openid = retUser.open_id;    	                    
        int flag = retUser.flag;
        String msg = retUser.msg;
        String pf = retUser.pf;
        String pf_key = retUser.pf_key;
        
        sendResult("account_type=" + account_type + "&openid=" + openid + "&openkey=" + accessToken + "&userip=" + userip + "&pay_token=" + pay_token + "&pf=" + pf + "&=" + pf + "&pfkey=" + pf_key + "&platform=" + platform);
        
    	ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate()
        {            
            @Override
            public void execute(String buoyPrivateKey)
            {      	
            	//buoyPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALeFRVP7fYkdpGpC03EywRbW3yNPUVeWXq9wQjnSumWu7uTbIY3LtK6eBFp2U7NmIwzizF4QfoJigaMQA0Zf++Na/EmARwbqE5tWc3EXzyCThpA7wu+ZJVtyhExo81Y6yzs9viSzzI57eahefZHAytz9DVBi70Xqv9G/HCHqjuWJAgMBAAECgYBPAxnuJoDN3+1BsrPGMMvhQXGgii3DQAJUFkjtDEUkMkS4U4AGxIVuCKpnKDqxKnA8xAipFwDgsfiOc/509i5D4B3Ymi5HCYgAUJwza8aIxOqzzuR3sDDmpr92pFPprfOExMviC/vR/rQqjZ1oC0PnoxiN2OuCUl/31qvoyWY0YQJBAPkTyJzhY5NOd7QxgitXNuQGyfwi5S2dyGeeFRGFQqccVN1ZJlxLEEmF9fgv4OrmdSN53Oo+RMIaaqjzArD49TcCQQC8nwsDapAIm2Mjagj7L1x6/XtBgoMn4H+9G4QG/COsYwasT3JnM/4izH2dnK6m5S2AlmscbRwtx1n2Hhgi4Fs/AkEAygucTnYeuh2KyKboepPSuQIw0tDTqz80k6kOWhoJSVmYFb39ehyB58I9Fshv4Nx05QQziehdCj83ijkmU7x/3QJAaCQHmKNFUvs4CV2KB+VmUiGR4+GDOIki/e4rPxAHQi6KiKh0qbZzIhN1Z4gSheMS9GQYT2GOpVfju5dyDA+DUQJBAKL6n1+vcW1FQkv0ScYmacQkyLxOSFzNTzgNpF7yrag4l0daopjNSFr7lodxEdSGPvYmkfw1Er6xzZzw6InpRK4=";            	
            	buoyPrivateKey = buoyPrivateKey.trim();
                
            	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",buoyPrivateKey);
            	if(buoyPrivateKey.indexOf("0_pay_m") > -1)
            	{
            		MessageHandle.resultCallBack(Util.User, UserWrapper.PayCheck, buoyPrivateKey);	
            	}
            	else if(buoyPrivateKey.indexOf("1_pay_m") > -1)
            	{
            		MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, buoyPrivateKey);
            	}
            	else if(buoyPrivateKey.indexOf("0_balance_m") > -1)
            	{
            		MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, buoyPrivateKey);
            	}
            	else
            	{
            		
            	}	
            }
        }, "account_type=" + account_type + "&openid=" + openid + "&openkey=" + accessToken + "&userip=" + userip + "&pay_token=" + pay_token + "&pf=" + pf + "&=" + pf + "&pfkey=" + pf_key + "&platform=" + platform, "http://123.207.146.159/ysdk/yybsdk.php");
        getBuoyPrivate.execute();     
    }
    
    
  
    public void Login(String p)
	{		
    	if("QQ".equals(p))
    	{
    		if(YSDKApi.isPlatformInstalled(ePlatform.QQ))
            {
    			if (getPlatform() == ePlatform.QQ) {
		            // 如已登录直接进入相应模块视图
		    		//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","QQ HasLogin");
		    		YSDKApi.queryUserInfo(ePlatform.QQ);
		        } else if (getPlatform() == ePlatform.None) {
		        	//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","QQ NoLogin");
		        	
		              YSDKApi.login(ePlatform.QQ);
		            
		        } else {
		
		        }
            }
            else
            {
            	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","QQ_NotInstall");
	    	}
    	}
    	else if("WX".equals(p))
    	{
    		if (getPlatform() == ePlatform.WX) {
                // 如已登录直接进入相应模块视图
    			//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","WX HasLogin");
    			YSDKApi.queryUserInfo(ePlatform.WX);
            } else if (getPlatform() == ePlatform.None) {
            	//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","WX NoLogin");
                YSDKApi.login(ePlatform.WX);
            } else {

            }
    	}
    	else
    	{
    		if (getPlatform() == ePlatform.QQ) {
	            // 如已登录直接进入相应模块视图
	    		//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","QQ HasLogin");
	    		YSDKApi.queryUserInfo(ePlatform.QQ);
	        }
    		else if (getPlatform() == ePlatform.WX) {
                // 如已登录直接进入相应模块视图
    			//UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","WX HasLogin");
    			YSDKApi.queryUserInfo(ePlatform.WX);
            } 
    		else
    		{
    			letUserLogout();
    		}
    	}
	}

    /*// TODO GAME 在异账号时，模拟游戏弹框提示用户选择登陆账号
    public void showDiffLogin() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("异账号提示");
                builder.setMessage("你当前拉起的账号与你本地的账号不一致，请选择使用哪个账号登陆：");
                builder.setPositiveButton("本地账号",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showToastTips("选择使用本地账号");
                                if (LANG.equals("java")) {
                                    if (!YSDKApi.switchUser(false)) {
                                        letUserLogout();
                                    }
                                } else {
                                    if(!PlatformTest.switchUser(false)){
                                        letUserLogout();
                                    }
                                }
                            }
                        });
                builder.setNeutralButton("拉起账号",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                showToastTips("选择使用拉起账号");
                                if (LANG.equals("java")) {
                                    if (!YSDKApi.switchUser(true)) {
                                        letUserLogout();
                                    }
                                } else {
                                    if(!PlatformTest.switchUser(true)){
                                        letUserLogout();
                                    }
                                }
                            }
                        });
                builder.show();
            }
        });

    }*/

    // 平台授权成功,让用户进入游戏. 由游戏自己实现登录的逻辑
    public void letUserLogin() {
        UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",ret.flag + " " + ret.platform);
        if (ret.ret != BaseRet.RET_SUCC) {            
            UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","UserLogin error!!!");
            return;
        }
        if (ret.platform == ePlatform.PLATFORM_ID_QQ) {            
            UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","UserLogin QQ!!!");
            YSDKApi.queryUserInfo(ePlatform.QQ);
        } else if (ret.platform == ePlatform.PLATFORM_ID_WX) {
        	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","UserLogin WX!!!");
        	YSDKApi.queryUserInfo(ePlatform.WX);
        }
    }

    // 登出后, 更新view. 由游戏自己实现登出的逻辑
    public void letUserLogout() {
    	UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",ret.flag + " " + ret.platform);
        if (ret.ret != BaseRet.RET_SUCC) {            
            UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive","UserLogin error!!!");            
        }
        else
        {
        	YSDKApi.logout();	
        }
    	
    	
    	
    	//MessageHandle.resultCallBack(Util.User, UserWrapper.LogoutSuccess, "");
    }


    /*public void startWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG,"startWaiting");
                mAutoLoginWaitingDlg = new ProgressDialog(MainActivity.this);
                stopWaiting();
                mAutoLoginWaitingDlg.setTitle("YSDK DEMO登录中...");
                mAutoLoginWaitingDlg.show();
            }
        });

    }

    public void stopWaiting() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(LOG_TAG,"stopWaiting");
                if (mAutoLoginWaitingDlg != null && mAutoLoginWaitingDlg.isShowing()) {
                    mAutoLoginWaitingDlg.dismiss();
                }
            }
        });

    }

    public void showToastTips(String tips) {
        Toast.makeText(this,tips,Toast.LENGTH_LONG).show();
    }*/

    // 获取当前登录平台
    public ePlatform getPlatform() {
        UserLoginRet ret = new UserLoginRet();
        YSDKApi.getLoginRecord(ret);
        if (ret.flag == eFlag.Succ) {
            return ePlatform.getEnum(ret.platform);
        }
        return ePlatform.None;
    }

    // ***********************界面布局相关*************************
    // 初始化界面
    /*private void initView() {
        Util.init(getApplicationContext()); //初始化Demo需要的工具类
        ModuleManager.LANG = LANG;
        nameList = ModuleManager.getInstance().modules;

        lbm = LocalBroadcastManager.getInstance(getApplicationContext());

        mModuleListView = (GridView) findViewById(R.id.gridview);
        mModuleView = (LinearLayout) findViewById(R.id.module);
        mResultView = (LinearLayout) findViewById(R.id.result);

        //设置actionbar
        //隐藏后退按钮，并设置为不可点击
        LinearLayout llayout = (LinearLayout) findViewById(R.id.actionBarReturn);
        llayout.setFocusable(false);
        llayout.setClickable(false);
        llayout.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.TactionBarTitle);
        LayoutParams textParams = (LayoutParams) title.getLayoutParams();
        textParams.leftMargin = Util.dp(9);
        title.setLayoutParams(textParams);
        title.setText("YSDKDemo 未登录");

        // 设置局部广播，处理回调信息
        lbm = LocalBroadcastManager.getInstance(this.getApplicationContext());
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getExtras().getString("Result");
                Log.d(LOG_TAG,result);
                displayResult(result);
            }

        };
        lbm.registerReceiver(mReceiver, new IntentFilter(LOCAL_ACTION));

        // 初始化下载进度条
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle("更新中");
        mProgressDialog.setMessage("下载进度");
        resetMainView();

        mModuleListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                seletedModule = nameList.get(position);
                if ("QQ登录".equals(seletedModule.name)) {
                    if (getPlatform() == ePlatform.QQ) {
                        // 如已登录直接进入相应模块视图
                        startModule();
                    } else if (getPlatform() == ePlatform.None) {
                        if ("cpp".equals(ModuleManager.LANG)) { // 使用C++调用YSDK, 游戏只需要用一种方式即可
                            PlatformTest.login(ePlatform.QQ.val());
                        } else if ("java".equals(ModuleManager.LANG)) { // 使用Java调用YSDK
                            YSDKApi.login(ePlatform.QQ);
                        }
                        startWaiting();
                    } else {

                    }
                } else if ("微信登录".equals(seletedModule.name)) {
                    if (getPlatform() == ePlatform.WX) {
                        // 如已登录直接进入相应模块视图
                        startModule();
                    } else if (getPlatform() == ePlatform.None) {
                        if ("cpp".equals(ModuleManager.LANG)) { // 使用C++调用YSDK, 游戏只需要用一种方式即可
                            PlatformTest.login(ePlatform.WX.val());
                        } else if ("java".equals(ModuleManager.LANG)) { // 使用Java调用YSDK
                            YSDKApi.login(ePlatform.WX);
                        }
                        startWaiting();
                    } else {

                    }
                } else {
                    // 进行其它功能模块
                    startModule();
                }

            }
        });
    }

    @SuppressLint("NewApi")
    public void resetMainView() {
        mModuleListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mModuleListView.setAdapter(new ArrayAdapter<BaseModule>(
                MainActivity.this, R.layout.gridview_item, nameList) {

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater inflater = (LayoutInflater) MainActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.gridview_item, null);
                }
                TextView itemText = (TextView) view.findViewById(R.id.item_txt);
                String item = getItem(position).name;
                if (item != null) {
                    if (item.equals("微信登录") && getPlatform() == ePlatform.QQ) {
                        view.getBackground().setAlpha(60);
                        itemText.setTextColor(0x60000000);
                    } else if (item.equals("QQ登录") && getPlatform() == ePlatform.WX) {
                        view.getBackground().setAlpha(60);
                        itemText.setTextColor(0x60000000);
                    } else {
                        view.getBackground().setAlpha(255);
                        itemText.setTextColor(0xff000000);
                    }
                    TextView itemView = (TextView) view
                            .findViewById(R.id.item_txt);
                    if (itemView != null) {
                        itemView.setText(item);
                    }
                }
                return view;
            }
        });
    }


    // 展示相应的功能模块
    public void startModule() {
        mModuleListView.setVisibility(View.GONE);
        mResultView.setVisibility(View.GONE);
        mModuleView.removeAllViews();
        seletedModule.init(mModuleView, this);
        mModuleView.setVisibility(View.VISIBLE);

        //设置actionbar、模块通用布局
        LinearLayout llayout = (LinearLayout) findViewById(R.id.actionBarReturn);
        llayout.setVisibility(View.VISIBLE);
        TextView title = (TextView) findViewById(R.id.TactionBarTitle);
        title.setTextColor(Color.WHITE);
        title.setText(seletedModule.name);
        llayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                endModule();
            }

        });
    }

    // 退出功能模块
    public void endModule() {
        mModuleView.removeAllViews();
        mModuleView.setVisibility(View.GONE);
        mResultView.setVisibility(View.GONE);
        mModuleListView.setVisibility(View.VISIBLE);
        resetMainView();
        //设置actionbar
        //隐藏后退按钮，并设置为不可点击
        LinearLayout llayout = (LinearLayout) findViewById(R.id.actionBarReturn);
        llayout.setFocusable(false);
        llayout.setClickable(false);
        llayout.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.TactionBarTitle);
        ePlatform platform = getPlatform();
        title.setTextColor(Color.RED);
        if (platform == ePlatform.QQ) {
            title.setText("YSDKDemo QQ登录中");
        } else if (platform == ePlatform.WX) {
            title.setText("YSDKDemo 微信登录中");
        } else {
            title.setText("YSDKDemo 未登录");
            title.setTextColor(Color.WHITE);
        }
    }

    public void displayResult(String result) {
        mModuleView.setVisibility(View.GONE);
        mModuleListView.setVisibility(View.GONE);
        mResultView.removeAllViews();

        ResultView block = new ResultView(this, mResultView);
        block.addView("CallAPI", callAPI);
        block.addView("Desripton", desripton);
        block.addView("Result", result);

        //设置actionbar、模块通用布局
        LinearLayout llayout = (LinearLayout) findViewById(R.id.actionBarReturn);
        llayout.setVisibility(View.VISIBLE);
        TextView title = (TextView) findViewById(R.id.TactionBarTitle);
        title.setText(this.title);
        llayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                endResult();
            }

        });
        mResultView.setVisibility(View.VISIBLE);
    }

    public void endResult() {
        mModuleView.setVisibility(View.VISIBLE);
        mResultView.removeAllViews();
        mResultView.setVisibility(View.GONE);
        mModuleListView.setVisibility(View.GONE);

        //设置actionbar、模块通用布局
        LinearLayout llayout = (LinearLayout) findViewById(R.id.actionBarReturn);
        llayout.setVisibility(View.VISIBLE);
        TextView title = (TextView) findViewById(R.id.TactionBarTitle);
        title.setText(seletedModule.name);
        llayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                endModule();
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopWaiting();
            if (mModuleListView.getVisibility() == View.VISIBLE) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("退出YSDKDemo");
                        builder.setMessage("你确定退出YSDK Demo么？");
                        builder.setPositiveButton("确定退出",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        finish();
                                        System.exit(0);
                                    }
                                });
                        builder.setNeutralButton("暂不退出",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                    }
                                });
                        builder.show();
                    }
                });
            } else if (mModuleView.getVisibility() == View.VISIBLE) {
                endModule();
            } else if (mResultView.getVisibility() == View.VISIBLE) {
                endResult();
            }
        }
        return false;
    }*/

    public void sendResult(String result) {   
    	UnityPlayer.UnitySendMessage("MainCamera","AndroidReceive",result);
    }
    
    public void Pay(String PayString)
    {
    	String a[] = PayString.split("_");  //zoneid, diamond, guid, serverid, paytype
    	PayMoney(a[0], a[1], a[2] + "-" + a[3] + "-" + a[4] + "-" + a[1]);
    }
    
    @SuppressLint("NewApi")
	public void PayMoney(String zoneId, String diamond, String userip)
    {
    	//YSDKApi.recharge(arg0, arg1, arg2, arg3, arg4, arg5);
    	
    	
    	//String zoneId = "1";
    	String saveValue = diamond;
    	boolean isCanChange = false;
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.demo_icon);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();    	
    	bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
    	byte[] appResData = baos.toByteArray();
    	String ysdkExt = userip;
    	YSDKApi.recharge(zoneId, saveValue,isCanChange,appResData,ysdkExt,new PayListener() {
    	    @Override
    	    public void OnPayNotify(PayRet ret) {
    	        if(PayRet.RET_SUCC == ret.ret){
    	            //支付流程成功
    	            switch (ret.payState){
    	                //支付成功
    	                case PayRet.PAYSTATE_PAYSUCC:
    	                    sendResult(
    	                            "用户支付成功，支付金额"+ret.realSaveNum+";" +
    	                            "使用渠道："+ret.payChannel+";" +
    	                            "发货状态："+ret.provideState+";" +
    	                            "业务类型："+ret.extendInfo+";建议查询余额："+ret.toString());
    	                    
    	                    //MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, ret.toString());
    	                    ConnectHttp(ret.ysdkExtInfo, "pay_m");
    	                    break;
    	                //取消支付
    	                case PayRet.PAYSTATE_PAYCANCEL:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, ret.toString());
    	                    sendResult("用户取消支付："+ret.toString());
    	                    break;
    	                //支付结果未知
    	                case PayRet.PAYSTATE_PAYUNKOWN:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, ret.toString());
    	                    sendResult("用户支付结果未知，建议查询余额："+ret.toString());
    	                    break;
    	                //支付失败
    	                case PayRet.PAYSTATE_PAYERROR:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, ret.toString());
    	                    sendResult("支付异常"+ret.toString());
    	                    break;
    	            }
    	        }else{
    	            switch (ret.flag){
    	                case eFlag.Login_TokenInvalid:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, ret.toString());
    	                    //用户取消支付
    	                    sendResult("登陆态过期，请重新登陆："+ret.toString());
    	                    break;
    	                case eFlag.Pay_User_Cancle:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayCancel, ret.toString());
    	                    //用户取消支付
    	                    sendResult("用户取消支付："+ret.toString());
    	                    break;
    	                case eFlag.Pay_Param_Error:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, ret.toString());
    	                    sendResult("支付失败，参数错误"+ret.toString());
    	                    break;
    	                case eFlag.Error:
    	                default:
    	                	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, ret.toString());
    	                    sendResult("支付异常"+ret.toString());
    	                    break;
    	            }
    	        }
    	    }
    	});
    }
}
