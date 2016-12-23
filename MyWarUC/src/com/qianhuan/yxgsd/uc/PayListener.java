package com.qianhuan.yxgsd.uc;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;

import android.util.Log;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

/**
 * 涓嬪崟缁撴灉渚﹀惉鍣紝姝や睛鍚櫒寰楀埌鐨勭粨鏋滀粎涓轰笅鍗曠粨鏋滐紝涓嶄唬琛ㄦ渶缁堟敮浠樼粨鏋溿��
 *
 */
public class PayListener implements UCCallbackListener<OrderInfo> {
    private final static String TAG = "JNI_PayListener";
    
    private static PayListener _instance = null;
    
    public static PayListener getInstance() {
        if (_instance == null) {
            _instance = new PayListener();
        }
        return _instance;
    }


    @Override
    public void callback(int code, OrderInfo orderInfo) {
        Log.d(TAG, "receive pay callback, code=" + code);
        
        String text = null;

        String orderId = "";
        float orderAmount = 0;
        int payWayId = 0;
        String payWayName = "";

        switch (code) {
        case UCGameSdkStatusCode.SUCCESS:
            if (orderInfo != null) {
                orderId = orderInfo.getOrderId();
                orderAmount = orderInfo.getOrderAmount();
                payWayId = orderInfo.getPayWay();
                payWayName = orderInfo.getPayWayName();

                text = "Order Result: OrderId=" + orderId + ", Amount=" + orderAmount + ", PayWayId=" + payWayId + ", PayWayName=" + payWayName;
                Log.d(TAG, text);
                text = null;

                MessageHandle.resultCallBack(Util.User, UserWrapper.PaySuccess, orderId);
                //
            } else {
                Log.e(TAG, "Received empty order result");
            }

            break;
        case UCGameSdkStatusCode.NO_INIT:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
            text = "Paying failed: no init";
            Log.e(TAG, text);
            break;
        case UCGameSdkStatusCode.PAY_USER_EXIT:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, "");
            text = "User exit the paying page, return to game page.";
            Log.d(TAG, text);
            break;
        default:
        	MessageHandle.resultCallBack(Util.User, UserWrapper.PayFailed, String.valueOf(code));
            text = "Unknown paying result code: code=" + code;
            Log.e(TAG, text);
            break;

        }


        /*try {
            Unity3DCallback.doPayCallback(code, orderId, orderAmount, payWayId, payWayName);
        } catch (Throwable e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }*/

    }

}