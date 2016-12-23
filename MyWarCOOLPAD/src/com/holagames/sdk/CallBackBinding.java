package com.holagames.sdk;

public class CallBackBinding {
	
	private String mGmaeObjectName;
	private String mFunctionName;
	
	public CallBackBinding(String gameObjectName, String functionName) {
		this.mGmaeObjectName = gameObjectName;
		this.mFunctionName = functionName;
	}
	
	public String getGameObjectName() {
		return this.mGmaeObjectName;
	}
	
	public String getFunctionName()
	{
		return this.mFunctionName;
	}

}
