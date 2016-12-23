package com.qianhuan.yxgsd.uc;

import android.util.Log;
import cn.uc.gamesdk.callback.GameUserLoginResult;
import cn.uc.gamesdk.callback.IGameUserLogin;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;

/**
 * 澶勭悊娓告垙鑰佽处鍙风櫥褰曢獙璇併��
 * 瀹為檯瀹炵幇涓紝娓告垙搴旀牴鎹柟娉曚腑鐨勭敤鎴峰悕鍜屽瘑鐮佸弬鏁帮紝鍚戞父鎴忔湇鍔″櫒鍙戣捣楠岃瘉璇锋眰锛屾妸浠庢父鎴忔湇鍔″櫒杩斿洖鐨勯獙璇佺粨鏋滃拰sid璁剧疆鍒� process 鏂规硶鐨勮繑鍥炲�间腑銆�
 *
 */
public class GameUserLoginOperation implements IGameUserLogin {
    private final static String TAG = "JNI_GameUserLoginOperation";
    
    private static GameUserLoginOperation _instance = null;
    
    public static GameUserLoginOperation getInstance() {
        if (_instance == null) {
            _instance = new GameUserLoginOperation();
        }
        return _instance;
    }


    @Override
    public GameUserLoginResult process(String username, String password) {
        Log.d(TAG, "Received game user login operation callback: username=" + username + ", password=" + password);
        
        GameUserLoginResult gulr = new GameUserLoginResult();
        gulr.setLoginResult(UCGameSdkStatusCode.LOGIN_GAME_USER_OTHER_FAIL);

        try {
            int loginResultCode = UCGameSdkStatusCode.LOGIN_GAME_USER_AUTH_FAIL;
            String sid = "";
            
            //TODO: 璋冪敤娓告垙鏈嶅姟鍣ㄧ殑鎺ュ彛杩涜鐢ㄦ埛楠岃瘉锛屽苟浠庢父鎴忔湇鍔″櫒鑾峰彇鍦ㄦ湇鍔″櫒绔粦瀹氫簡UC璐﹀彿鍚庣殑 sid 銆�
            boolean authed = false;     //TODO: 搴斾娇鐢ㄦ父鎴忕殑瀹為檯閫昏緫
            
            
            if (authed) {
            //if (username.equals(password)) {
                //gulr.setLoginResult(UCGameSdkStatusCode.SUCCESS);
                //gulr.setSid("50f5ffac-b40d-4256-ab9d-8a7951bca526164201");
                gulr.setLoginResult(loginResultCode);
                gulr.setSid(sid);
            } else {
                // 鏍规嵁楠岃瘉澶辫触鐨勬儏鍐碉紝璁剧疆杩斿洖鍊笺��
                gulr.setLoginResult(UCGameSdkStatusCode.LOGIN_GAME_USER_AUTH_FAIL);
                //gulr.setLoginResult(UCGameSdkStatusCode.LOGIN_GAME_USER_NETWORK_FAIL);
                //gulr.setLoginResult(UCGameSdkStatusCode.LOGIN_GAME_USER_OTHER_FAIL);
            }
        
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
            gulr.setLoginResult(UCGameSdkStatusCode.LOGIN_GAME_USER_OTHER_FAIL);
            gulr.setSid("");
        }
        
        Log.d(TAG, "return game user login result: loginResultCode=" + gulr.getLoginResult() + ", sid=" + gulr.getSid());
        return gulr;
    }
    
}