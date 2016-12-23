package com.holagames.sdk;

public class CallBackBinding {

		private String mGameObjectName;
		private String mFunctionName;

		CallBackBinding(String gameObjectName, String functionName)
		{
			this.mGameObjectName = gameObjectName;
			this.mFunctionName = functionName;
		}

		public String getGameObjectName() {
			return this.mGameObjectName;
		}

		public String getFunctionName()
		{
			return this.mFunctionName;
		}
}
