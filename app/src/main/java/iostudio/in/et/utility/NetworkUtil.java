package iostudio.in.et.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Call;
import retrofit2.Callback;

public class NetworkUtil {

	public static final int TYPE_WIFI = 1;
	public static final int TYPE_MOBILE = 2;
	public static final int TYPE_NOT_CONNECTED = 0;
	public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
	public static final int NETWORK_STATUS_WIFI = 1;
	public static final int NETWORK_STATUS_MOBILE = 2;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static int getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		int status = 0;
		if (conn == NetworkUtil.TYPE_WIFI) {
			status = NETWORK_STATUS_WIFI;
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			status = NETWORK_STATUS_MOBILE;
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			status = NETWORK_STATUS_NOT_CONNECTED;
		}
		return status;
	}


	private NetworkUtil() {
	}

	/**
	 * Check if network is available or not.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkStatus(Context context) {
		boolean status = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
		for (NetworkInfo tempNetworkInfo : networkInfos) {

			if (tempNetworkInfo.isConnected()) {
				status = true;
				break;
			}
		}
		return status;
	}

	public static <T extends Object> void callApi(Call<T> call, Callback<T> callback) {
		call.enqueue(callback);
	}

}
