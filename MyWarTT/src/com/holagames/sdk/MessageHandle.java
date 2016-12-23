package com.holagames.sdk;

import com.unity3d.player.UnityPlayer;
import java.util.HashMap;

public class MessageHandle {
	 private static HashMap<String, CallBackBinding> mGameObjects = new HashMap<String, CallBackBinding>();

	  public static void registerActionResultCallback(int type, String gameObjectName, String functionName)
	  {
	    CallBackBinding obj = new CallBackBinding(gameObjectName, functionName);
	    mGameObjects.put(String.valueOf(type), obj);
	    //nativeSetListener(type);
	  }

	  public static void unRegisterActionResultCallback(String type, String gameObjectName)
	  {
	    if (mGameObjects.containsKey(type))
	      mGameObjects.remove(type);
	  }

	  public static void resultCallBack(int type, int result, String msg)
	  {
		    String key = String.valueOf(type);
		    if (mGameObjects.containsKey(key)) 
		    {
		      CallBackBinding gameObject = (CallBackBinding)mGameObjects.get(key);
		      String param = new String();
		      param = String.format("%s=%s&%s=%s", new Object[] { "code", String.valueOf(result), "msg", msg });
		      UnityPlayer.UnitySendMessage(gameObject.getGameObjectName(), gameObject.getFunctionName(), param);
		    }
	  }

	  //public static native void nativeSetListener(int paramInt);
	  
	  
}
