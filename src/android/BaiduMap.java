package com.baidu.light.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class BaiduMap extends CordovaPlugin {
	private static final String LOG_TAG = "BaiduMap";
	private static final boolean DEBUG = true;

	private CallbackContext mCallbackContext = null;

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action            The action to execute.
	 * @param args              JSONArry of arguments for the plugin.
	 * @param callbackContext   The callback id used when calling back into JavaScript.
	 * @return                  True if the action was valid, false if not.
	 */
	@SuppressWarnings("unchecked")
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (DEBUG) {
			Log.d(LOG_TAG, String.format("execute({ action: %s, args: %s })",
					action, args));
		}
		if ("init".equals(action)) {
			mCallbackContext = callbackContext;
		}
		return true;
	}
}
