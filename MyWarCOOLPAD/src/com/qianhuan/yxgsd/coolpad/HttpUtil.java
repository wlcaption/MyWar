package com.qianhuan.yxgsd.coolpad;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 获取酷派账号token
 * 
 * */
public class HttpUtil {

	private static final String TAG = HttpUtil.class.getSimpleName();
	
    
	public static String doPost(String url , String param) {
		try {
			HttpPost httpRequest = new HttpPost(url);  
			// 绑定到请求 Entry  
			httpRequest.setEntity(new StringEntity( param.toString(), HTTP.UTF_8));
			httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
			Log.d(TAG, "请求参数:" + param.toString() );

            HttpClient client = new DefaultHttpClient();
            // 请求超时
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
            // 读取超时
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20 * 1000);
            
            // 发送请求  
            HttpResponse httpResponse = client.execute(httpRequest);
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
			String retSrc = EntityUtils.toString(httpResponse.getEntity());  
			Log.d(TAG, "请求结果:" + retSrc.toString() );
			
			return retSrc.toString(); 
			
		} catch (Exception e) {
			Log.d(TAG , e.toString());
			return "" ;
		}
	}
}
