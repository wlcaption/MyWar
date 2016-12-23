package com.qianhuan.yxgsd.uc;

import org.json.JSONArray;
import org.json.JSONObject;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.exception.UCCallbackListenerNullException;
import cn.uc.gamesdk.exception.UCMissActivityException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.PaymentInfo;
import cn.uc.gamesdk.open.UCLogLevel;
import cn.uc.gamesdk.open.UCOrientation;

/**
 * UC娓告垙SDK闈㈠悜 Unity3D 鐨勬帴鍙� 搴斾粠Java浠ｇ爜涓皟鐢� setCurrentActivity 鏂规硶璁剧疆娓告垙鐨勫綋鍓�
 * Activity锛屽叾瀹冩柟娉曚竴鑸粠 Unity3D 涓皟鐢ㄣ��
 * 
 */
public class UCGameSDK {
    private final static String TAG = "JNI_UCGameSdk";

    // 閿欒淇℃伅绾у埆锛岃褰曢敊璇棩蹇�
    public final static int LOGLEVEL_ERROR = 0;
    // 璀﹀憡淇℃伅绾у埆锛岃褰曢敊璇拰璀﹀憡鏃ュ織
    public final static int LOGLEVEL_WARN = 1;
    // 璋冭瘯淇℃伅绾у埆锛岃褰曢敊璇�佽鍛婂拰璋冭瘯淇℃伅锛屼负鏈�璇﹀敖鐨勬棩蹇楃骇鍒�
    public final static int LOGLEVEL_DEBUG = 2;
    // 绔栧睆
    public final static int ORIENTATION_PORTRAIT = 0;
    // 妯睆
    public final static int ORIENTATION_LANDSCAPE = 1;

    private static GameParamInfo genGameParams(int gameId, int serverId, int orientation, boolean enablePayHistory, boolean enableUserChange) {
        Log.d(TAG, "gameid=" + gameId);
        GameParamInfo gpi = new GameParamInfo();
        gpi.setGameId(gameId);
        gpi.setServerId(serverId);

        gpi.setOrientation(orientation == ORIENTATION_PORTRAIT ? UCOrientation.PORTRAIT : UCOrientation.LANDSCAPE);
        gpi.setEnablePayHistory(enablePayHistory);
        gpi.setEnableUserChange(enableUserChange);
        return gpi;
    }

    /**
     * 鍒濆鍖� 鍒濆鍖朣DK
     * 
     * @param debugMode
     *            鏄惁鑱旇皟妯″紡锛� false=杩炴帴SDK鐨勬寮忕敓浜х幆澧冿紝true=杩炴帴SDK鐨勬祴璇曡仈璋冪幆澧�
     * @param logLevel
     *            鏃ュ織绾у埆锛� 0=閿欒淇℃伅绾у埆锛岃褰曢敊璇棩蹇楋紝 1=璀﹀憡淇℃伅绾у埆锛岃褰曢敊璇拰璀﹀憡鏃ュ織锛�
     *            2=璋冭瘯淇℃伅绾у埆锛岃褰曢敊璇�佽鍛婂拰璋冭瘯淇℃伅锛屼负鏈�璇﹀敖鐨勬棩蹇楃骇鍒�
     * @param cpId
     *            娓告垙鍚堜綔鍟咺D锛岃ID鐢盪C娓告垙涓績鍒嗛厤锛屽敮涓�鏍囪瘑涓�涓父鎴忓悎浣滃晢
     * @param gameId
     *            娓告垙ID锛岃ID鐢盪C娓告垙涓績鍒嗛厤锛屽敮涓�鏍囪瘑涓�娆炬父鎴�
     * @param serverId
     *            娓告垙鏈嶅姟鍣紙娓告垙鍒嗗尯锛夋爣璇嗭紝鐢盪C娓告垙涓績鍒嗛厤
     * @param serverName
     *            娓告垙鏈嶅姟鍣紙娓告垙鍒嗗尯锛夊悕绉�
     * @param orientation
     *            灞忓箷鏂瑰悜锛�0=绔栧睆锛�1=妯睆锛�
     * @param enablePayHistory
     *            鏄惁鍚敤鏀粯鏌ヨ鍔熻兘
     * @param enableUserChange
     *            鏄惁鍚敤鐢ㄦ埛鍒囨崲鍔熻兘
     */
    public static void initSDK(final boolean debugMode, int logLevel, int gameId, int serverId, int orientation, boolean enablePayHistory, boolean enableUserChange) {
        Log.d(TAG, "initSDK calling...");
        try {
            Log.d(TAG, "initSDK parameters: debugMode=" + debugMode + ", loglevel=" + logLevel + ", gameId=" + gameId 
                    + ", orientation=" + orientation + ", enablePayHistory=" + enablePayHistory + ", enableUserChange=" + enableUserChange);

            final GameParamInfo gp = genGameParams(gameId, serverId, orientation, enablePayHistory, enableUserChange);
            Log.d(TAG, "after gp values set");

            UCLogLevel ucloglevel = UCLogLevel.DEBUG;
            if (logLevel == LOGLEVEL_ERROR)
                ucloglevel = UCLogLevel.ERROR;
            else if (logLevel == LOGLEVEL_WARN)
                ucloglevel = UCLogLevel.WARN;

            Log.d(TAG, "will init SDK...");

            final UCLogLevel loglevel = ucloglevel;
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        // SDK鍒濆鍖栧繀椤诲湪涓荤嚎绋嬫墽琛�
                        UCGameSdk.defaultSdk().initSdk(UnityPlayer.currentActivity, loglevel, debugMode, gp, InitResultListener.getInstance());
                    } catch (UCCallbackListenerNullException | UCMissActivityException e) {
                        e.printStackTrace();
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            });

            Log.d(TAG, "after init SDK invoked");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * 璋冪敤SDK鐨勭敤鎴风櫥褰�
     * 
     * @param enableGameAccount
     *            鏄惁鍏佽浣跨敤娓告垙鑰佽处鍙凤紙娓告垙鑷韩璐﹀彿锛夌櫥褰�
     * @param gameAccountTitle
     *            娓告垙鑰佽处鍙凤紙娓告垙鑷韩璐﹀彿锛夌殑璐﹀彿鍚嶇О锛屽鈥滀笁鍥藉彿鈥濄�佲�滈浜戝彿鈥濈瓑銆� 濡傛灉 enableGameAccount
     *            涓篺alse锛屾鍙傛暟鐨勫�艰涓虹┖瀛楃涓插嵆鍙��
     * @param gameUserLoginOperation
     *            娓告垙鑰佽处鍙风櫥褰曟搷浣滃璞★紝濡傛灉 enableGameAccount 涓篺alse锛屾鍙傛暟璁句负绌哄嵆鍙紝濡傛灉
     *            enableGameAccount 涓簍rue锛屾瀵硅薄涓嶅彲涓虹┖銆�
     */
    public static void login(final boolean enableGameAccount, final String gameAccountTitle) {
        Log.d(TAG, "login calling...");
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (enableGameAccount) {
                        // 璋冪敤鐧诲綍鏂规硶锛屼紶鍏ndroid绋嬪簭褰撳墠鐣岄潰activity
                        UCGameSdk.defaultSdk().login(LoginResultListener.getInstance(), GameUserLoginOperation.getInstance(), gameAccountTitle);
                    }
                    else {
                        UCGameSdk.defaultSdk().login(LoginResultListener.getInstance());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });

    }

    /**
     * 杩斿洖鐢ㄦ埛鐧诲綍鍚庣殑浼氳瘽鏍囪瘑锛屾鏍囪瘑浼氬湪澶辨晥鏃跺埛鏂帮紝娓告垙鍦ㄦ瘡娆￠渶瑕佷娇鐢ㄨ鏍囪瘑鏃跺簲浠嶴DK鑾峰彇
     * 
     * @return 鐢ㄦ埛鐧诲綍浼氳瘽鏍囪瘑
     */
    public static String getSid() {
        Log.d(TAG, "getSid calling...");
        return UCGameSdk.defaultSdk().getSid();
    }


    /**
     * 鎵ц鍏呭�间笅鍗曟搷浣滐紝姝ゆ搷浣滀細璋冨嚭鍏呭�肩晫闈€��
     * 
     * @param amount
     *            鍏呭�奸噾棰濄�傞粯璁や负0锛屽鏋滀笉璁炬垨璁句负0锛屽厖鍊兼椂鐢ㄦ埛浠庡厖鍊肩晫闈腑閫夋嫨鎴栬緭鍏ラ噾棰濓紱濡傛灉璁句负澶т簬0鐨勫�硷紝琛ㄧず鍥哄畾鍏呭�奸噾棰濓紝
     *            涓嶅厑璁哥敤鎴烽�夋嫨鎴栬緭鍏ュ叾瀹冮噾棰濄��
     * @param serverId
     * 			       姝ゅ弬鏁板凡搴熷純锛岄粯璁や负0锛氬綋鍓嶅厖鍊肩殑娓告垙鏈嶅姟鍣紙鍒嗗尯锛夋爣璇嗭紝姝ゆ爣璇嗗嵆UC鍒嗛厤鐨勬父鎴忔湇鍔″櫒ID
     * @param roleId
     *            褰撳墠鍏呭�肩敤鎴峰湪娓告垙涓殑瑙掕壊鏍囪瘑
     * @param roleName
     *            褰撳墠鍏呭�肩敤鎴峰湪娓告垙涓殑瑙掕壊鍚嶇О
     * @param grade
     *            褰撳墠鍏呭�肩敤鎴峰湪娓告垙涓殑瑙掕壊绛夌骇
     * @param customInfo
     *            鍏呭�艰嚜瀹氫箟淇℃伅锛屾淇℃伅浣滀负鍏呭�艰鍗曠殑闄勫姞淇℃伅锛屽厖鍊艰繃绋嬩腑涓嶄綔浠讳綍澶勭悊锛屼粎鐢ㄤ簬娓告垙璁剧疆鑷姪淇℃伅锛屾瘮濡傛父鎴忚嚜韬骇鐢熺殑璁㈠崟鍙枫��
     *            鐜╁瑙掕壊銆佹父鎴忔ā寮忕瓑銆�
     *            濡傛灉璁剧疆浜嗚嚜瀹氫箟淇℃伅锛孶C鍦ㄥ畬鎴愬厖鍊煎悗锛岃皟鐢ㄥ厖鍊肩粨鏋滃洖璋冩帴鍙ｅ悜娓告垙鏈嶅姟鍣ㄥ彂閫佸厖鍊肩粨鏋滄椂灏嗕細闄勫甫姝や俊鎭紝
     *            娓告垙鏈嶅姟鍣ㄩ渶鑷瑙ｆ瀽鑷畾涔変俊鎭�� 濡傛灉涓嶉渶璁剧疆鑷畾涔変俊鎭紝灏嗘鍙傛暟缃负绌哄瓧绗︿覆鍗冲彲銆�
     * @param notifyUrl
     *            鏀粯鍥炶皟閫氱煡URL
     * @param transactionNumCP
     *            鑷湁浜ゆ槗鍙�
     */
    public static void pay(float amount, int serverId, String roleId, String roleName, String grade, String customInfo, String notifyUrl, String transactionNumCP) {
        Log.d(TAG, "pay calling...");
        try {
            PaymentInfo payInfo = new PaymentInfo();
//            payInfo.setAllowContinuousPay(allowContinuousPay);
            payInfo.setAmount(amount);
            payInfo.setServerId(serverId);
            payInfo.setRoleId(roleId);
            payInfo.setRoleName(roleName);
            payInfo.setGrade(grade);
            payInfo.setCustomInfo(customInfo);
            payInfo.setNotifyUrl(notifyUrl);
            payInfo.setTransactionNumCP(transactionNumCP);
            try {
                UCGameSdk.defaultSdk().pay(payInfo, PayListener.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage(), e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * 鎻愪氦娓告垙鎵╁睍鏁版嵁锛屽湪鐧诲綍鎴愬姛浠ュ悗鍙互璋冪敤銆傚叿浣撶殑鏁版嵁绉嶇被鍜屾暟鎹唴瀹瑰畾涔夛紝璇峰弬鑰冣�滃紑鍙戝弬鑰冭鏄庝功鈥濄��
     * 
     * @param category
     *            鏁版嵁绉嶇被
     * @param jsonObjStr
     *            鏁版嵁鍐呭锛屾槸涓�涓� JSON 瀛楃涓层��
     * 
     */
    public static void submitExtendData(String category, String jsonObjStr) {
        Log.d(TAG, "submitExtendData calling...");
        try {
            JSONObject data = new JSONObject(jsonObjStr);

            UCGameSdk.defaultSdk().submitExtendData(category, data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * 
     * 鎻愪氦娓告垙鎵╁睍鏁版嵁锛屽湪鐧诲綍鎴愬姛浠ュ悗鍙互璋冪敤銆傚叿浣撶殑鏁版嵁绉嶇被鍜屾暟鎹唴瀹瑰畾涔夛紝璇峰弬鑰冣�滃紑鍙戝弬鑰冭鏄庝功鈥濄��
     * 涓�娆℃�ф鎻愪氦澶氱鏁版嵁绫诲瀷
     * 
     * @param jsonArrayStr
     */
    public static void submitExtendData(String jsonArrayStr) {
        Log.d(TAG, "submitExtendData calling...");
        try {
            JSONArray data = new JSONArray(jsonArrayStr);

            UCGameSdk.defaultSdk().submitExtendData(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }

    }

    /**
     * 鍦ㄥ綋鍓� Activity 涓婂垱寤轰節娓哥殑鎮诞鎸夐挳
     */
    public static void createFloatButton() {
        Log.d(TAG, "createFloatButton calling...");
        UCGameSdk.defaultSdk().createFloatButton(UnityPlayer.currentActivity);
    }

    /**
     * 鏄剧ず涔濇父鐨勬偓娴寜閽�
     * 
     * @param x
     *            鎮诞鎸夐挳鏄剧ず浣嶇疆鐨勬í鍧愭爣锛屽崟浣嶏細%锛屾敮鎸佸皬鏁般�傝鍙傛暟鍙敮鎸� 0 鍜� 100锛屽垎鍒〃绀哄湪灞忓箷鏈�宸﹁竟鎴栨渶鍙宠竟鏄剧ず鎮诞鎸夐挳銆�
     * @param y
     *            鎮诞鎸夐挳鏄剧ず浣嶇疆鐨勭旱鍧愭爣锛屽崟浣嶏細%锛屾敮鎸佸皬鏁般�備緥濡傦細80锛岃〃绀烘偓娴寜閽樉绀虹殑浣嶇疆璺濆睆骞曢《閮ㄧ殑璺濈涓哄睆骞曢珮搴︾殑 80% 銆�
     */
    public static void showFloatButton(final float x, final float y) {
        Log.d(TAG, "showFloatButton calling...");
        UCGameSdk.defaultSdk().showFloatButton(UnityPlayer.currentActivity, x, y);
    }

    /**
     * 闅愯棌涔濇父鐨勬偓娴寜閽�
     */
    public static void hideFloatButton() {
        Log.d(TAG, "hideFloatButton calling...");
        UCGameSdk.defaultSdk().hideFloatButton(UnityPlayer.currentActivity);
    }

    /**
     * 閿�姣佸綋鍓� Activity 鐨勪節娓告偓娴寜閽�
     */
    public static void destroyFloatButton() {
        Log.d(TAG, "destroyFloatButton calling...");
        UCGameSdk.defaultSdk().destoryFloatButton(UnityPlayer.currentActivity);
    }

    /**
     * 杩涘叆涔濇父绀惧尯锛堢敤鎴蜂腑蹇冿級
     */
    public static void enterUserCenter() {
        Log.d(TAG, "enterUserCenter calling...");
        try {
            UCGameSdk.defaultSdk().enterUserCenter(UserCenterListener.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 杩涘叆SDK鐨勬煇涓�鎸囧畾鐣岄潰
     * 
     * @param business
     *            涓氬姟鏍囪瘑
     */
    public static void enterUI(String business) {
        Log.d(TAG, "enterUserCenter calling...");
        try {
            UCGameSdk.defaultSdk().enterUI(business, EnterUIListener.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 鎻愪氦鐜╁閫夋嫨鐨勬父鎴忓垎鍖哄強瑙掕壊淇℃伅
     * 
     * @param zoneName
     *            鐜╁瀹為檯鐧诲綍鐨勫垎鍖哄悕绉�
     * @param roleId
     *            瑙掕壊缂栧彿
     * @param roleName
     *            瑙掕壊鍚嶇О
     */
//    public static void notifyZone(final String zoneName, final String roleId, final String roleName) {
//        Log.d(TAG, "notifyZone calling...");
//        UCGameSdk.defaultSdk().notifyZone(zoneName, roleId, roleName);
//    }

    /**
     * 璁剧疆閫�鍑鸿处鍙蜂睛鍚櫒
     */
    public static void setLogoutNotifyListener() {
        Log.d(TAG, "setLogoutNotifyListener calling...");
        try {
            UCGameSdk.defaultSdk().setLogoutNotifyListener(LogoutNotifyListener.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 閫�鍑哄綋鍓嶇櫥褰曠殑璐﹀彿
     */
    public static void logout() {
        Log.d(TAG, "logout calling...");
        try {
            UCGameSdk.defaultSdk().logout();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 閫�鍑篠DK锛屾父鎴忛��鍑哄墠蹇呴』璋冪敤姝ゆ柟娉曪紝浠ユ竻鐞哠DK鍗犵敤鐨勭郴缁熻祫婧愩�傚鏋滄父鎴忛��鍑烘椂涓嶈皟鐢ㄨ鏂规硶锛屽彲鑳戒細寮曡捣绋嬪簭閿欒銆�
     */
    public static void exitSDK() {
        Log.d(TAG, "exitSDK calling...");
        Activity currentActivity = UnityPlayer.currentActivity;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    UCGameSdk.defaultSdk().exitSDK(UnityPlayer.currentActivity, ExitSDKListener.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }
}
