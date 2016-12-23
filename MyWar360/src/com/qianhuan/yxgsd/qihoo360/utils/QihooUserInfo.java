
package com.qianhuan.yxgsd.qihoo360.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * QihooUserInfo锛屾槸搴旂敤鏈嶅姟鍣ㄨ姹�360鏈嶅姟鍣ㄥ緱鍒扮殑360鐢ㄦ埛淇℃伅鏁版嵁銆�
 */
public class QihooUserInfo {

    private String id; // 360鐢ㄦ埛ID锛岀己鐪佽繑鍥炪��

    private String name; // 360鐢ㄦ埛鍚嶏紝缂虹渷杩斿洖銆�

    private String avatar; // 360鐢ㄦ埛澶村儚url锛岀己鐪佽繑鍥炪��

    private String sex; // 360鐢ㄦ埛鎬у埆锛屼粎鍦╢ields涓寘鍚椂鍊欐墠杩斿洖锛岃繑鍥炲�间负锛氱敺锛屽コ鎴栬�呮湭鐭ャ��

    private String area; // 360鐢ㄦ埛鍦板尯锛屼粎鍦╢ields涓寘鍚椂鍊欐墠杩斿洖銆�

    private String nick; // 360鐢ㄦ埛鏄电О锛屾棤鍊兼椂鍊欒繑鍥炵┖銆�

    public static QihooUserInfo parseJson(String jsonString) {
        QihooUserInfo userInfo = null;
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                String status = jsonObj.optString("status", null);
                JSONObject dataJsonObj = jsonObj.optJSONObject("data");
                if (TextUtils.isEmpty(status) || null == dataJsonObj) {
                    // 灏濊瘯鎸塽ser/me.json鎺ュ彛鐩存帴杩斿洖鐨勬暟鎹В鏋�
                    status = "ok";
                    dataJsonObj = jsonObj;
                }
                if (status != null && status.equals("ok")) {
                    // 蹇呰繑鍥為」
                    String id = dataJsonObj.getString("id");
                    String name = dataJsonObj.getString("name");
                    String avatar = dataJsonObj.getString("avatar");

                    userInfo = new QihooUserInfo();
                    userInfo.setId(id);
                    userInfo.setName(name);
                    userInfo.setAvatar(avatar);

                    // 闈炲繀杩斿洖椤�
                    if (dataJsonObj.has("sex")) {
                        String sex = dataJsonObj.getString("sex");
                        userInfo.setSex(sex);
                    }

                    if (dataJsonObj.has("area")) {
                        String area = dataJsonObj.getString("area");

                        userInfo.setArea(area);
                    }

                    if (dataJsonObj.has("nick")) {
                        String nick = dataJsonObj.getString("nick");
                        userInfo.setNick(nick);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return userInfo;
    }

    public static QihooUserInfo parseUserInfo(JSONObject joInfo) {
        QihooUserInfo userInfo = null;
        if (joInfo != null) {
            try {
                // 蹇呴』杩斿洖
                String name = joInfo.getString("name");
                String avatar = joInfo.getString("avatar");

                userInfo = new QihooUserInfo();
                userInfo.setName(name);
                userInfo.setAvatar(avatar);

                // 闈炲繀杩斿洖椤�
                if (joInfo.has("sex")) {
                    String sex = joInfo.getString("sex");
                    userInfo.setSex(sex);
                }

                if (joInfo.has("area")) {
                    String area = joInfo.getString("area");

                    userInfo.setArea(area);
                }

                if (joInfo.has("nick")) {
                    String nick = joInfo.getString("nick");
                    userInfo.setNick(nick);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userInfo;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

}
