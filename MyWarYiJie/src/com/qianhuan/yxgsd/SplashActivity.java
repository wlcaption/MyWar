package com.qianhuan.yxgsd;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.snowfish.cn.ganga.helper.SFOnlineSplashActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.util.Log;

public class SplashActivity extends  SFOnlineSplashActivity{

	public int getBackgroundColor() {
		return Color.WHITE;
	}
	@Override
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);       

	}
	//"zy_class_name"
	public static final byte [] STRING_ACTIVITY_CLASS_NAME =   {122, 121, 95, 99, 108, 97, 115, 115, 95, 110, 97, 109, 101};
	
	@Override
	public void onSplashStop() {



		int id = SplashActivity.this.getResources().getIdentifier(new String(STRING_ACTIVITY_CLASS_NAME), "string",
				SplashActivity.this.getPackageName());
		String str = SplashActivity.this.getString(id);
		Log.e("SplashActivity", "SplashActivity str :"+str);
		Intent localIntent = new Intent();
		localIntent.setClassName(SplashActivity.this.getPackageName(),
				str);
		SplashActivity.this.startActivity(localIntent);

		SplashActivity.this.finish();
	}

	public void getSingInfo() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					SplashActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			Log.v("aaa","signName:" + cert.getSigAlgName());
			Log.v("aaa","pubKey:" + pubKey);
			Log.v("aaa","signNumber:" + signNumber);
			Log.v("aaa","subjectDN:" + cert.getSubjectDN().toString());
			
			
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}
}
