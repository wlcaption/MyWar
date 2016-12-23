package com.tencent.tmgp.yxgsd;

import android.app.Activity;
import android.util.Log;

import com.holagames.sdk.MessageHandle;
import com.holagames.sdk.UserWrapper;
import com.holagames.sdk.Util;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** 
 * TODO GAME ��Ϸ��Ҫ����Լ����߼�ʵ���Լ���YSDKCallback���� 
 * YSDKͨ��UserListener�������еķ�������Ȩ���ѯ���ص�����Ϸ��
 * ��Ϸ��ݻص�������UI�ȡ�ֻ�����ûص�����Ϸ�����յ�YSDK����Ӧ��
 * ������Java��ص�(������Java��ص������ȵ���Java��ص�, ���Ҫʹ��C++��ص���������Java��ص�)
 */
public class YSDKCallback implements UserListener, BuglyListener,PayListener {
    public static MainActivity mainActivity;

    
    public YSDKCallback(Activity activity) {
    	mainActivity = (MainActivity) activity;
    }
    
    public void OnLoginNotify(UserLoginRet ret) {
        String result = "";
        switch (ret.flag) {
            case eFlag.Succ:
                mainActivity.letUserLogin();
                break;
            // ��Ϸ�߼����Ե�¼ʧ������ֱ���д���
            case eFlag.QQ_UserCancel:       
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, ret.toString());
                mainActivity.sendResult("QQ_UserCancel");
                mainActivity.letUserLogout();
                break;
            case eFlag.QQ_LoginFail:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("QQ_LoginFail");
                mainActivity.letUserLogout();
                break;
            case eFlag.QQ_NetworkErr:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("QQ_NetworkErr");
                mainActivity.letUserLogout();
                break;
            case eFlag.QQ_NotInstall:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("QQ_NotInstall");
                mainActivity.letUserLogout();
                break;
            case eFlag.QQ_NotSupportApi:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("QQ_NotSupportApi");
                mainActivity.letUserLogout();
                break;
            case eFlag.WX_NotInstall:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("WX_NotInstall");
                mainActivity.letUserLogout();
                break;
            case eFlag.WX_NotSupportApi:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("WX_NotSupportApi");
                mainActivity.letUserLogout();
                break;
            case eFlag.WX_UserCancel:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginCancel, ret.toString());
            	mainActivity.sendResult("WX_UserCancel");
                mainActivity.letUserLogout();
                break;
            case eFlag.WX_UserDeny:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("WX_UserDeny");
                mainActivity.letUserLogout();
                break;
            case eFlag.WX_LoginFail:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("WX_LoginFail");
                mainActivity.letUserLogout();
                break;
            case eFlag.Login_TokenInvalid:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("Login_TokenInvalid");
                mainActivity.letUserLogout();
                break;
            case eFlag.Login_NotRegisterRealName:
                // ��ʾ��¼����
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("Login_NotRegisterRealName");
                mainActivity.letUserLogout();
                break;
            default:
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginFailed, ret.toString());
            	mainActivity.sendResult("default");
                // ��ʾ��¼����
                mainActivity.letUserLogout();
                break;
        }
    }

    public void OnWakeupNotify(WakeupRet ret) {
        /*Log.d(MainActivity.LOG_TAG,"called");
        Log.d(MainActivity.LOG_TAG,"flag:" + ret.flag);
        Log.d(MainActivity.LOG_TAG,"msg:" + ret.msg);
        Log.d(MainActivity.LOG_TAG,"platform:" + ret.platform);
        MainActivity.platform = ret.platform;
        // TODO GAME ��Ϸ��Ҫ���������Ӵ������˺ŵ��߼�
        if (eFlag.Wakeup_YSDKLogining == ret.flag) {
            // ��������˺ŵ�¼����¼�����OnLoginNotify()�лص�
        } else if (ret.flag == eFlag.Wakeup_NeedUserSelectAccount) {
            // ���˺�ʱ����Ϸ��Ҫ������ʾ�����û�ѡ����Ҫ��¼���˺�
            Log.d(MainActivity.LOG_TAG,"diff account");
            mainActivity.showDiffLogin();
        } else if (ret.flag == eFlag.Wakeup_NeedUserLogin) {
            // û����Ч��Ʊ�ݣ��ǳ���Ϸ���û����µ�¼
            Log.d(MainActivity.LOG_TAG,"need login");
            mainActivity.letUserLogout();
        } else {
            Log.d(MainActivity.LOG_TAG,"logout");
            mainActivity.letUserLogout();
        }*/
    }

    @Override
    public void OnRelationNotify(UserRelationRet relationRet) {
    	String result = "";
        result = result +"flag:" + relationRet.flag + "\n";
        result = result +"msg:" + relationRet.msg + "\n";
        result = result +"platform:" + relationRet.platform + "\n";
        if (relationRet.persons != null && relationRet.persons.size()>0) {
            PersonInfo personInfo = (PersonInfo)relationRet.persons.firstElement();
            StringBuilder builder = new StringBuilder();
            builder.append("UserInfoResponse json: \n");
            builder.append("nick_name: " + personInfo.nickName + "\n");
            builder.append("open_id: " + personInfo.openId + "\n");
            builder.append("userId: " + personInfo.userId + "\n");
            builder.append("gender: " + personInfo.gender + "\n");
            builder.append("picture_small: " + personInfo.pictureSmall + "\n");
            builder.append("picture_middle: " + personInfo.pictureMiddle + "\n");
            builder.append("picture_large: " + personInfo.pictureLarge + "\n");
            builder.append("provice: " + personInfo.province + "\n");
            builder.append("city: " + personInfo.city + "\n");
            builder.append("country: " + personInfo.country + "\n");
            builder.append("is_yellow_vip: " + personInfo.is_yellow_vip + "\n");
            builder.append("is_yellow_year_vip: " + personInfo.is_yellow_year_vip + "\n");
            builder.append("yellow_vip_level: " + personInfo.yellow_vip_level + "\n");
            builder.append("is_yellow_high_vip: " + personInfo.is_yellow_high_vip + "\n");
            result = result + builder.toString();
            
            if(mainActivity.getPlatform()== ePlatform.QQ)
            {
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "_" + personInfo.openId);
            }
            else if(mainActivity.getPlatform()== ePlatform.WX)
            {
            	MessageHandle.resultCallBack(Util.User, UserWrapper.LoginSuccess, "WX_" + personInfo.openId);
            }
            	
        } else {
            result = result + "relationRet.persons is bad";
        }        

        // ���ͽ����չʾ����
       //mainActivity.sendResult(result);
    }

    @Override
    public String OnCrashExtMessageNotify() {
        /*// �˴���Ϸ����crashʱ�ϱ��Ķ�����Ϣ
        Log.d(MainActivity.LOG_TAG,String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");*/
        return "new Upload extra crashing message for bugly on ";
    }

	@Override
	public byte[] OnCrashExtDataNotify() {
		return null;
	}

    @Override
    public void OnPayNotify(PayRet ret) {
        /*Log.d(MainActivity.LOG_TAG,ret.toString());
        if(PayRet.RET_SUCC == ret.ret){
            //֧�����̳ɹ�
            switch (ret.payState){
                //֧���ɹ�
                case PayRet.PAYSTATE_PAYSUCC:
                    mainActivity.sendResult(
                            "�û�֧���ɹ���֧�����"+ret.realSaveNum+";" +
                            "ʹ��������"+ret.payChannel+";" +
                            "����״̬��"+ret.provideState+";" +
                            "ҵ�����ͣ�"+ret.extendInfo+";�����ѯ��"+ret.toString());
                    break;
                //ȡ��֧��
                case PayRet.PAYSTATE_PAYCANCEL:
                    mainActivity.sendResult("�û�ȡ��֧����"+ret.toString());
                    break;
                //֧�����δ֪
                case PayRet.PAYSTATE_PAYUNKOWN:
                    mainActivity.sendResult("�û�֧�����δ֪�������ѯ��"+ret.toString());
                    break;
                //֧��ʧ��
                case PayRet.PAYSTATE_PAYERROR:
                    mainActivity.sendResult("֧���쳣"+ret.toString());
                    break;
            }
        }else{
            switch (ret.flag){
                case eFlag.Login_TokenInvalid:
                    //�û�ȡ��֧��
                    mainActivity.sendResult("��¼̬���ڣ������µ�¼��"+ret.toString());
                    mainActivity.letUserLogout();
                    break;
                case eFlag.Pay_User_Cancle:
                    //�û�ȡ��֧��
                    mainActivity.sendResult("�û�ȡ��֧����"+ret.toString());
                    break;
                case eFlag.Pay_Param_Error:
                    mainActivity.sendResult("֧��ʧ�ܣ��������"+ret.toString());
                    break;
                case eFlag.Error:
                default:
                    mainActivity.sendResult("֧���쳣"+ret.toString());
                    break;
            }
        }*/
    }
}

