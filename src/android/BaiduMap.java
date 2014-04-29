package com.baidu.light.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.AbsoluteLayout.LayoutParams;

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
			if (args == null) {
				return false;
			}

			JSONObject params = args.optJSONObject(0);
			JSONArray center = params.optJSONArray("center");

			int left = params.optInt("left");
			int top = params.optInt("top");
			int width = params.optInt("width");
			int height = params.optInt("height");
			String guid = params.optString("id");
			int zoom = params.optInt("zoom");
			createMap(guid, left, top, width, height,
					(float) center.optDouble(0), (float) center.optDouble(1),
					zoom);
			mCallbackContext = callbackContext;

		}
		return true;
	}

	private static Handler mHandler = new Handler(Looper.getMainLooper());
	private static Hashtable<String, MapView> mMaps = new Hashtable<String, MapView>();

	/**
	 * @param cordova The context of the main Activity.
	 * @param webView The associated CordovaWebView.
	 */
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		if (DEBUG) {
			Log.d(LOG_TAG, String.format("initialize()"));
		}

		BMapManager baiduMapManager = new BMapManager(webView.getContext()
				.getApplicationContext());
		baiduMapManager.init(new MKGeneralListener() {
			@Override
			public void onGetNetworkState(int state) {
			}

			@Override
			public void onGetPermissionState(int state) {
			}
		});
	}

	public void createMap(String guid, int left, int top, int width,
			int height, float lng, float lat, int zoom) {
		if (DEBUG) {
			Log.d(LOG_TAG,
					String.format(
							"createMap(guid: %s, left: %s, top: %s, width: %s, height: %s, lng: %s, lat: %s, zoom: %s)",
							guid, left, top, width, height, lng, lat, zoom));
		}

		mHandler.post(new Runnable() {
			private String mGuid;
			private int mLeft;
			private int mTop;
			private int mWidth;
			private int mHeight;
			private float mLng;
			private float mLat;
			private int mZoom;

			public Runnable config(String guid, int left, int top, int width,
					int height, float lng, float lat, int zoom) {
				mGuid = guid;
				mLeft = left;
				mTop = top;
				mHeight = height;
				mWidth = width;
				mLng = lng;
				mLat = lat;
				mZoom = zoom;
				return this;
			}

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				MapView mapView = new MapView(BaiduMap.this.webView
						.getContext());
				MapController mapController = mapView.getController();
				GeoPoint point = new GeoPoint((int) (mLat * 1E6),
						(int) (mLng * 1E6));
				mapController.setCenter(point);
				mapController.setZoom(mZoom);

				float scale = BaiduMap.this.webView.getScale();

				LayoutParams params = new LayoutParams((int) (mWidth * scale),
						(int) (mHeight * scale), (int) (mLeft * scale),
						(int) (mTop * scale));
				mapView.setLayoutParams(params);
				BaiduMap.this.webView.addView(mapView);

				mMaps.put(mGuid, mapView);
			}

		}.config(guid, left, top, width, height, lng, lat, zoom));
	}
}
