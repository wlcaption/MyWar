package com.qianhuan.yxgsd.pyw;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.pengyouwan.sdk.api.PYWPlatform;
import com.pengyouwan.sdk.api.PayConstant;
import com.pengyouwan.sdk.api.User;
import com.pengyouwan.sdk.utils.FloatViewTool;
import com.unity3d.player.UnityPlayerActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;


/**
 * @author xiaowei
 * 2016-10-11 下午5:29:29
 *
 */
public class MainActivity extends UnityPlayerActivity {
	 public static final String ACTION_LOGIN_SDK_SUCCESS = "com.qianhuan.yxgsd.pyw.ACTION_LOGIN_SDK_SUCCESS";

	    public static final String ACTION_TO_START_LOGIN = "com.qianhuan.yxgsd.pyw.ACTION_TO_START_LOGIN";

	    public static final String ACTION_TO_EXIT_GAME = "com.qianhuan.yxgsd.pyw.ACTION_TO_EXIT";

	    private SDKResultReceiver mReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InitSDKCallbackReceiver();
	}
	
	//初始化操作
	public void HolaSdkInit(String appid, String appkey, String privateKey,String oauthLoginServer) {
		MessageHandle.resultCallBack(Util.User, UserWrapper.InitSuccess, "");
	}
	
	
	
	//登錄操作
	public void Login(String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				PYWPlatform.openLogin(MainActivity.this);
			}
		});
	}
	
	//支付操作
	public void Pay(String msg){
		String a[] = msg.split("_");
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put(PayConstant.PAY_PRODUCE_NAME, "钻石"); //商品名称
		orderMap.put(PayConstant.PAY_MONEY, 1);            //充值金额(元)
		orderMap.put(PayConstant.PAY_PRODUCT_ID, 1);       //产品id
		orderMap.put(PayConstant.PAY_ORDER_ID, System.currentTimeMillis() + "|" + a[2] + "-" + a[1] + "-" + a[5] + "-" + a[0]); //订单号
		orderMap.put(PayConstant.PAY_EXTRA, getOrderExtraParams());
		
		PYWPlatform.openChargeCenter(MainActivity.this, orderMap, true);
	}
	
	private String getOrderExtraParams() {
		JSONObject mJson = new JSONObject();
		try {
			mJson.put("roles_nick", "www");
			mJson.put("area_name", "1区");
			mJson.put("area_num", "1");
			mJson.put("channel", "渠道号");
			mJson.put("product_desc", "充值钻石");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mJson.toString();
	}

	//退出操作
	public void ExitSDK(){
//		finish();
//		Process.killProcess(Process.myPid());
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
	
	
	/***
	 * 注册SDK回调广播接收器
	 */
	public void InitSDKCallbackReceiver(){
		mReceiver = new SDKResultReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_LOGIN_SDK_SUCCESS);
		filter.addAction(ACTION_TO_START_LOGIN);
		filter.addAction(ACTION_TO_EXIT_GAME);
		registerReceiver(mReceiver, filter);
	}
	
	
	
	/**
     * 描述:SDK回调广播接收者
     */
    class SDKResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (MainActivity.ACTION_LOGIN_SDK_SUCCESS.equals(action)) {
            	showToolBar(); //显示悬浮框
            } else if (MainActivity.ACTION_TO_START_LOGIN.equals(action)) {
                hideFloatView(); //隐藏悬浮框
               // PYWPlatform.openLogin(MainActivity.this);
            } else if (MainActivity.ACTION_TO_EXIT_GAME.equals(action)) {
                ExitSDK(); //退出游戏
            }
        }
    }
	
    @Override
    protected void onResume() {
    	super.onResume();
    	JPushInterface.onResume(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	JPushInterface.onPause(this);
    }
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);
		}
		FloatViewTool.instance(MainActivity.this).destroyFloatView();
	}

	public void hideFloatView() {
	       FloatViewTool.instance(this).hideFloatView();
	}

	public void showToolBar() {
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (null != PYWPlatform.getCurrentUser()) { // 表示当前登录状态
		            FloatViewTool.instance(MainActivity.this).showFloatView();
		        }
			}
		});
	}
	
	public void LoginGameRole(String type,String msg){
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showToolBar();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			PYWPlatform.exit(MainActivity.this);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
