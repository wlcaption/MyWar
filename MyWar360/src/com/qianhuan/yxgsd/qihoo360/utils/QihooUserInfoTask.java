
package com.qianhuan.yxgsd.qihoo360.utils;

import android.content.Context;
import android.util.Log;

/***
 * 姝ょ被浣跨敤Access Token锛岃姹傛偍鐨勫簲鐢ㄦ湇鍔″櫒锛岃幏鍙朡ihooUserInfo銆�
 * 锛堟敞锛氬簲鐢ㄦ湇鍔″櫒鐢�360SDK浣跨敤鏂硅嚜琛屾惌寤猴紝鐢ㄤ簬鍜�360鏈嶅姟鍣ㄨ繘琛屽畨鍏ㄤ氦浜掞紝鍏蜂綋鍗忚璇锋煡鐪嬫枃妗ｄ腑锛屾湇鍔″櫒绔帴鍙ｏ級銆�
 */
public class QihooUserInfoTask {

    private static final String TAG = "QihooUserInfoTask";

    /**
     * 搴旂敤鏈嶅姟鍣ㄤ负搴旂敤瀹㈡埛绔彁渚涚殑鎺ュ彛Url锛岀敤浜庨�氳繃AccessToken鑾峰彇QihooUserInfo
     * (杩欐槸DEMO涓撶敤鐨刄RL锛岃浣跨敤鏂硅嚜宸辨惌寤鸿嚜宸辩殑搴旂敤鏈嶅姟鍣紝姝ゆ湇鍔″櫒鍙DEMO鐨凙ppKey銆�)
     */
    private static final String DEMO_APP_SERVER_URL_GET_USER = "https://openapi.360.cn/user/me.json?access_token=";


    private SdkHttpTask sSdkHttpTask;

    public static QihooUserInfoTask newInstance(){
        return new QihooUserInfoTask();
     }

    public void doRequest(Context context, String accessToken, String appKey,
            final QihooUserInfoListener listener) {

        // DEMO浣跨敤鐨勫簲鐢ㄦ湇鍔″櫒url浠呴檺DEMO绀鸿寖浣跨敤锛岀姝㈡寮忎笂绾挎父鎴忔妸DEMO搴旂敤鏈嶅姟鍣ㄥ綋鍋氭寮忓簲鐢ㄦ湇鍔″櫒浣跨敤锛岃浣跨敤鏂硅嚜宸辨惌寤鸿嚜宸辩殑搴旂敤鏈嶅姟鍣ㄣ��
        String url = DEMO_APP_SERVER_URL_GET_USER + accessToken + "&appkey=" + appKey + "&fields=id,name,avatar,set,area";

        // 濡傛灉瀛樺湪锛屽彇娑堜笂涓�娆¤姹�
        if (sSdkHttpTask != null) {
            sSdkHttpTask.cancel(true);
        }

        // 鏂拌姹�
        sSdkHttpTask = new SdkHttpTask(context);
        sSdkHttpTask.doGet(new SdkHttpListener() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "onResponse=" + response);

                // QihooUserInfo.parseJson杩欎釜瑙ｆ瀽鐨勬槸demo娴嬭瘯鏈嶅姟鍣ㄨ繑鍥炵殑鏁版嵁鏍煎紡銆�
                // 娓告垙鏇存崲url鍚庨渶瑕佹寜鏂扮殑鏍煎紡瑙ｆ瀽response瀛楃涓层�備笉鑳界洿鎺ョ敤杩欎釜銆�
                QihooUserInfo userInfo = QihooUserInfo.parseJson(response);
                listener.onGotUserInfo(userInfo);
                sSdkHttpTask = null;
            }

            @Override
            public void onCancelled() {
                listener.onGotUserInfo(null);
                sSdkHttpTask = null;
            }

        }, url);

//        Log.d(TAG, "url=" + url);
    }

    public boolean doCancel() {
        return (sSdkHttpTask != null) ? sSdkHttpTask.cancel(true) : false;
    }

}
