package com.tcn.funcommon.socket;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 *   判断网络连接状态
 * @author wjh
 *
 */
public class NetManager
{
	static NetManager s_m = null;

	private Context context;

	private NetManager()
	{

	}

	public void init(Context ctx)
	{
		context = ctx;
	}

	public static synchronized NetManager instance()
	{
		if (s_m == null)
		{
			s_m = new NetManager();
		}
		return s_m;
	}

	/**
	 * 判断是否有网络连接
	 * @return
	 */
	public boolean isNetworkConnected()
	{
		if (context == null)
		{
			return false;
		}
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null)
		{
			return false;
		} else
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (NetworkInfo ni : info) {
					if (ni.isConnected() && ni.isAvailable()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	/**
	 * 判断WIFI网络是否可用
	 * @return
	 */
	public boolean isWifiConnected()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null)
			{
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * 判断MOBILE网络是否可用
	 * @return
	 */
	public boolean isMobileConnected()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null)
			{
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public int getConnectedType()
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable())
			{
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	public String getNetWorkType() {
		String type = "";
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				int iNetType = mNetworkInfo.getType();
				String strSubTypeName = mNetworkInfo.getSubtypeName();
				Log.i("test", "getNetWorkType iNetType: "+iNetType+" strSubTypeName: "+strSubTypeName+" getTypeName: "+mNetworkInfo.getTypeName());
				if (ConnectivityManager.TYPE_WIFI == iNetType) {
					type = "WLAN";
				} else if (ConnectivityManager.TYPE_MOBILE == iNetType) {
					int subType = mNetworkInfo.getSubtype();
					if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
							|| subType == TelephonyManager.NETWORK_TYPE_EDGE || subType == TelephonyManager.NETWORK_TYPE_1xRTT
							|| subType == TelephonyManager.NETWORK_TYPE_IDEN) {

						type = "2G";
					} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
							|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
							|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B || subType == TelephonyManager.NETWORK_TYPE_HSUPA
							|| subType == TelephonyManager.NETWORK_TYPE_HSPA || subType == TelephonyManager.NETWORK_TYPE_EHRPD
							|| subType == TelephonyManager.NETWORK_TYPE_HSPAP) {

						type = "3G";
					} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
						type = "4G";
					} else {
						//TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							type = "3G";
						} else {
							type = strSubTypeName;
						}
					}
				} else if (ConnectivityManager.TYPE_ETHERNET == iNetType) {
					type = "ETHERNET";
				} else {

				}
			}
		}
		return type;
	}

	public String getWifiStrength() {
		String strStrength = "";
		String strNetType = getNetWorkType();
		if (strNetType != null) {
			if (strNetType.equals("WLAN")) {
				// Wifi的连接速度及信号强度：
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				WifiInfo info = wifiManager.getConnectionInfo();
				if (info.getBSSID() != null) {
					strStrength = String.valueOf(info.getRssi())+"dB";
				}
			}
		}
		return strStrength;
	}

	/**
	 * 设置手机的移动数据 
	 */
	public void setMobileData(boolean pBoolean) {

		try {

			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			Class<? extends ConnectivityManager> ownerClass = mConnectivityManager.getClass();

			Class<?>[] argsClass = new Class[1];
			argsClass[0] = boolean.class;

			Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);

			method.invoke(mConnectivityManager, pBoolean);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("移动数据设置错误: " + e.toString());
		}
	}

	/**
	 * 返回手机移动数据的状态 
	 *
	 * @param arg
	 *            默认填null 
	 * @return true 连接 false 未连接 
	 */
	public boolean getMobileDataState(Object[] arg) {

		try {

			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			Class<? extends ConnectivityManager> ownerClass = mConnectivityManager.getClass();

			Class<?>[] argsClass = null;
			if (arg != null) {
				argsClass = new Class[1];
				argsClass[0] = arg.getClass();
			}

			Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);

			Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);

			return isOpen;

		} catch (Exception e) {
			// TODO: handle exception

			System.out.println("得到移动数据状态出错");
			return false;
		}
	}

	/**
	 * WIFI网络开关
	 *
	 * @param enabled
	 * @return 设置是否success
	 */
	public boolean setWiFi(boolean enabled) {
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wm.setWifiEnabled(enabled);

	}

	/**
	 *
	 * @return 是否处于飞行模式
	 */
	public boolean isAirplaneModeOn() {
		// 返回值是1时表示处于飞行模式
		int modeIdx = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
		boolean isEnabled = (modeIdx == 1);
		return isEnabled;
	}
	/**
	 * 飞行模式开关
	 * @param setAirPlane
	 */
	public void setAirplaneMode(boolean setAirPlane) {
		Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);
		// 广播飞行模式信号的改变，让相应的程序可以处理。
		// 不发送广播时，在非飞行模式下，Android 2.2.1上测试关闭了Wifi,不关闭正常的通话网络(如GMS/GPRS等)。
		// 不发送广播时，在飞行模式下，Android 2.2.1上测试无法关闭飞行模式。
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		// intent.putExtra("Sponsor", "Sodino");
		// 2.3及以后，需设置此状态，否则会一直处于与运营商断连的情况
		intent.putExtra("state", setAirPlane);
		context.sendBroadcast(intent);
	}

	public int rebootNetWork() {
		int iNetWorkType = NetManager.instance().getConnectedType();
		if (iNetWorkType == ConnectivityManager.TYPE_MOBILE) {
			setMobileData(false);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setMobileData(true);
				}
			}, 3000);
		} else if (iNetWorkType == ConnectivityManager.TYPE_WIFI) {
			setWiFi(false);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					setWiFi(true);
				}
			}, 3000);
		} else {

		}
		return 0;

	}


}
