
package com.qianhuan.yxgsd.qihoo360.payment;

/**
 * QihooPayInfo锛岃姹�360SDK鏀粯鎺ュ彛鏃剁殑鍙傛暟淇℃伅绫汇��
 */
public class QihooPayInfo {
 // 蹇呴渶鍙傛暟锛岀敤鎴穉ccess token锛岃浣跨敤娉ㄦ剰杩囨湡鍜屽埛鏂伴棶棰橈紝鏈�澶�64瀛楃銆�
    private String accessToken;
    // 蹇呴渶鍙傛暟锛�360璐﹀彿id锛屾暣鏁般��
    private String qihooUserId;

    // 蹇呴渶鍙傛暟锛屽簲鐢╝pp key銆�
    private String appKey;

    // 蹇呴渶鍙傛暟锛屽�间负md5(app_secret +鈥�#鈥�+
    // app_key)鍏ㄥ皬鍐欙紝鐢ㄤ簬绛惧悕鐨勫瘑閽ヤ笉鑳芥妸app_secret鍐欏埌搴旂敤瀹㈡埛绔▼搴忛噷鍥犳浣跨敤杩欐牱涓�涓壒娈婄殑KEY锛屽簲绠楀嚭鍊肩洿鎺ュ啓鍦╝pp涓紝鑰屼笉鏄啓md5鐨勮绠楄繃绋嬨��
    private String privateKey;

    // 蹇呴渶鍙傛暟锛屾墍璐拱鍟嗗搧閲戦锛屼互鍒嗕负鍗曚綅銆傞噾棰濆ぇ浜庣瓑浜�100鍒嗭紝360SDK杩愯瀹氶鏀粯娴佺▼锛� 閲戦鏁颁负0锛�360SDK杩愯涓嶅畾棰濇敮浠樻祦绋嬨��
    private String moneyAmount;

    // 蹇呴渶鍙傛暟锛屼汉姘戝竵涓庢父鎴忓厖鍊煎竵鐨勯粯璁ゆ瘮渚嬶紝渚嬪2锛屼唬琛�1鍏冧汉姘戝竵鍙互鍏戞崲2涓父鎴忓竵锛屾暣鏁般��
    private String exchangeRate;

    // 蹇呴渶鍙傛暟锛屾墍璐拱鍟嗗搧鍚嶇О锛屽簲鐢ㄦ寚瀹氾紝寤鸿涓枃锛屾渶澶�10涓腑鏂囧瓧銆�
    private String productName;

    // 蹇呴渶鍙傛暟锛岃喘涔板晢鍝佺殑鍟嗗搧id锛屽簲鐢ㄦ寚瀹氾紝鏈�澶�16瀛楃銆�
    private String productId;

    // 蹇呴渶鍙傛暟锛屽簲鐢ㄦ柟鎻愪緵鐨勬敮浠樼粨鏋滈�氱煡uri锛屾渶澶�255瀛楃銆�360鏈嶅姟鍣ㄥ皢鎶婃敮浠樻帴鍙ｅ洖璋冪粰璇ri锛屽叿浣撳崗璁鏌ョ湅鏂囨。涓紝鏀粯缁撴灉閫氱煡鎺ュ彛鈥撳簲鐢ㄦ湇鍔″櫒鎻愪緵鎺ュ彛銆�
    private String notifyUri;

    // 蹇呴渶鍙傛暟锛屾父鎴忔垨搴旂敤鍚嶇О锛屾渶澶�16涓枃瀛椼��
    private String appName;

    // 蹇呴渶鍙傛暟锛屽簲鐢ㄥ唴鐨勭敤鎴峰悕锛屽娓告垙瑙掕壊鍚嶃�� 鑻ュ簲鐢ㄥ唴缁戝畾360璐﹀彿鍜屽簲鐢ㄨ处鍙凤紝鍒欏彲鐢�360鐢ㄦ埛鍚嶏紝鏈�澶�16涓枃瀛椼�傦紙鍏呭�间笉鍒嗗尯鏈嶏紝
    // 鍏呭埌缁熶竴鐨勭敤鎴疯处鎴凤紝鍚勫尯鏈嶈鑹插潎鍙娇鐢級銆�
    private String appUserName;

    // 蹇呴渶鍙傛暟锛屽簲鐢ㄥ唴鐨勭敤鎴穒d銆� 鑻ュ簲鐢ㄥ唴缁戝畾360璐﹀彿鍜屽簲鐢ㄨ处鍙�, 鍏呭�间笉鍒嗗尯鏈�, 鍏呭埌缁熶竴鐨勭敤鎴疯处鎴�, 鍚勫尯鏈嶈鑹插潎鍙娇鐢�,
    // 鍒欏彲鐢�360鐢ㄦ埛ID銆傛渶澶�32瀛楃銆�
    private String appUserId;

    // 鍙�夊弬鏁帮紝搴旂敤鎵╁睍淇℃伅1锛屽師鏍疯繑鍥烇紝鏈�澶�255瀛楃銆�
    private String appExt1;

    // 鍙�夊弬鏁帮紝搴旂敤鎵╁睍淇℃伅2锛屽師鏍疯繑鍥烇紝鏈�澶�255瀛楃銆�
    private String appExt2;

    // 鍙�夊弬鏁帮紝搴旂敤璁㈠崟鍙凤紝搴旂敤鍐呭繀椤诲敮涓�锛屾渶澶�32瀛楃銆�
    private String appOrderId;

    // 鍙�夊弬鏁帮紝鏀粯绫诲瀷瀹氬埗
    private String[] payTypes;

    public String[] getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(String[] payTypes) {
        this.payTypes = payTypes;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getQihooUserId() {
        return qihooUserId;
    }

    public void setQihooUserId(String qihooUserId) {
        this.qihooUserId = qihooUserId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(String moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUserName() {
        return appUserName;
    }

    public void setAppUserName(String appUserName) {
        this.appUserName = appUserName;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNotifyUri() {
        return notifyUri;
    }

    public void setNotifyUri(String notifyUri) {
        this.notifyUri = notifyUri;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getAppExt1() {
        return appExt1;
    }

    public void setAppExt1(String appExt1) {
        this.appExt1 = appExt1;
    }

    public String getAppExt2() {
        return appExt2;
    }

    public void setAppExt2(String appExt2) {
        this.appExt2 = appExt2;
    }

    public String getAppOrderId() {
        return appOrderId;
    }

    public void setAppOrderId(String appOrderId) {
        this.appOrderId = appOrderId;
    }

}
