
package com.qianhuan.yxgsd.qihoo360.payment;

public interface Constants {
    /**
     * MONEY AMOUNT浠ュ垎涓哄崟浣嶃��100鍒嗭紝鍗�1鍏冦�侻ONEY AMOUNT澶т簬绛変簬100鏃讹紝鍗冲畾棰濇敮浠樸��
     */
    public static final String DEMO_FIXED_PAY_MONEY_AMOUNT = "100";

    /**
     * MONEY AMOUNT涓�0鍒嗘椂锛屽嵆涓嶅畾棰濇敮浠樸��
     */
    public static final String DEMO_NOT_FIXED_PAY_MONEY_AMOUNT = "0";

    /**
     * 浜烘皯甯佷笌娓告垙鍏呭�煎竵鐨勬瘮渚嬶紝渚嬪2锛屼唬琛�1鍏冧汉姘戝竵鍙互鍏戞崲2涓父鎴忓竵锛屾暣鏁般��
     */
    public static final String DEMO_PAY_EXCHANGE_RATE = "1";

    /**
     * 璐拱鍟嗗搧鐨勫晢鍝乮d锛屽簲鐢ㄦ寚瀹氾紝鏈�澶�16瀛楃銆�
     */
    public static final String DEMO_PAY_PRODUCT_ID = "100";

    /**
     * 搴旂敤鍐呯殑鐢ㄦ埛id銆� 鏈�澶�32瀛楃銆�
     */
    public static final String DEMO_PAY_APP_USER_ID = "1888";

    /**
     * 搴旂敤鏈嶅姟鍣ㄤ负360鏈嶅姟鍣ㄦ彁渚涚殑鏀粯缁撴灉閫氱煡鎺ュ彛锛岀敱360鏈嶅姟鍣ㄦ妸鏀粯缁撴灉閫氱煡鍒拌繖涓猆RI銆俇RI鏈�澶�255瀛楃銆傚叿浣撳崗璁鏌ョ湅鏂囨。涓紝
     * 鏀粯缁撴灉閫氱煡鎺ュ彛銆�
     */
    public static final String DEMO_APP_SERVER_NOTIFY_URI = "http://mgame.360.cn/gamecp/status";

    /**
     * 搴旂敤鏈嶅姟鍣ㄤ负搴旂敤瀹㈡埛绔彁渚涚殑鎺ュ彛Url锛岀敤浜庨�氳繃AuthorizationCode鑾峰彇TokenInfo
     * (DEMO浣跨敤鐨勫簲鐢ㄦ湇鍔″櫒url浠呴檺DEMO绀鸿寖浣跨敤
     * 锛岀姝㈡寮忎笂绾挎父鎴忔妸DEMO搴旂敤鏈嶅姟鍣ㄥ綋鍋氭寮忓簲鐢ㄦ湇鍔″櫒浣跨敤锛岃浣跨敤鏂硅嚜宸辨惌寤鸿嚜宸辩殑搴旂敤鏈嶅姟鍣ㄣ��)
     */
    public static final String DEMO_APP_SERVER_URL_GET_TOKEN_BY_CODE = "http://sdbxapp.msdk.mobilem.360.cn/mobileSDK/api.php?type=get_token_by_code&debug=1&code=";
    /**
     * 搴旂敤鏈嶅姟鍣ㄤ负搴旂敤瀹㈡埛绔彁渚涚殑鎺ュ彛Url锛岀敤浜庨�氳繃AccessToken鑾峰彇QihooUserInfo
     * (DEMO浣跨敤鐨勫簲鐢ㄦ湇鍔″櫒url浠呴檺DEMO绀鸿寖浣跨敤
     * 锛岀姝㈡寮忎笂绾挎父鎴忔妸DEMO搴旂敤鏈嶅姟鍣ㄥ綋鍋氭寮忓簲鐢ㄦ湇鍔″櫒浣跨敤锛岃浣跨敤鏂硅嚜宸辨惌寤鸿嚜宸辩殑搴旂敤鏈嶅姟鍣ㄣ��)
     */
    public static final  String DEMO_APP_SERVER_URL_GET_USER_BY_TOKEN = "http://sdbxapp.msdk.mobilem.360.cn/mobileSDK/api.php?type=get_userinfo_by_token&debug=1&token=";
    public static final String IS_LANDSCAPE = "is_landscape";
}
