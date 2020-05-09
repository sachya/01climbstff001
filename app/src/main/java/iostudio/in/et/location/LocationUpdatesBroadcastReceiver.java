/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iostudio.in.et.location;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fonfon.geohash.GeoHash;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.sac.BasicListener;
import io.github.sac.Socket;
import iostudio.in.et.app.IOApp;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.utility.MyPhoneStateListener;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;

import static iostudio.in.et.location.LocationUpdatesService.EXTRA_LOCATION;

/**
 * Receiver for handling location updates.
 * <p>
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 * <p>
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 * foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";

    public static final String ACTION_PROCESS_UPDATES = "iostudio.in.et.location.action" + ".PROCESS_UPDATES";

    private String data = "";
    private Context mContext;

    private BasicListener basicListener = new BasicListener() {

        public void onConnected(Socket socket, Map<String, List<String>> headers) {
            System.out.println("Connected to endpoint");
        }

        public void onDisconnected(Socket socket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            System.out.println("Disconnected from end-point");
        }

        public void onConnectError(Socket socket, WebSocketException exception) {
            System.out.println("Got connect error " + exception);

        }

        public void onSetAuthToken(String token, Socket socket) {
            System.out.println("Token is " + token);
        }

        public void onAuthentication(Socket socket, Boolean status) {
            if (status) {
                System.out.println("socket is authenticated");
            } else {
                System.out.println("Authentication is required (optional)");
            }
            sendOfflineDataAndCurrentLocation(mContext, IOApp.getInstance().getSocketInstance(), data);
        }

    };

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();


                if (ACTION_PROCESS_UPDATES.equals(action)) {
                    Log.e(TAG, " onReceive " + "ACTION_PROCESS_UPDATES:" + ACTION_PROCESS_UPDATES +
                            " ==  action:" + action + " " + intent.getExtras().toString());
                    LocationResult result = LocationResult.extractResult(intent);
                    Location location = intent.getParcelableExtra(EXTRA_LOCATION);

                    if (location != null) {
                       /* List<Location> locations = result.getLocations();
                        LocationResultHelper locationResultHelper = new LocationResultHelper(
                                context, locations);
                        // Save the location data to SharedPreferences.
                        locationResultHelper.saveResults();
                        // Show notification with the location data.
                        //locationResultHelper.showNotification();
                        Log.e(TAG, *//*LocationResultHelper.getSavedLocationResult(context)*//*  " " + locations.get(0).getAccuracy());*/

                        if (Utility.getOnDuty(context)) {
                            mContext = context;
                            getNetowrk(context);
//                            updateLocationRemote(context, locations);
                            sendLocation(context, location);
                        } else {
                            try {
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.LAT, location.getLatitude() + "");
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.LNG, location.getLongitude() + "");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }


                    } else {
                        Log.e(TAG, "LocationResult null");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getNetowrk(final Context context) {
        try {
            final TelephonyManager[] mTelephonyManager = {(TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE)};
            MyPhoneStateListener mPhoneStatelistener = new MyPhoneStateListener(mTelephonyManager, new MyPhoneStateListener.SignalStrengthListener() {
                @Override
                public void onSignalStrength(String mSignalStrength) {
                    IOPref.getInstance().saveString(context, IOPref.PreferenceKey.NETWORK_STRENGTH, String.valueOf(mSignalStrength));
                    mTelephonyManager[0] = null;
                }
            });

            mTelephonyManager[0].listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HardwareIds")
    private void updateLocationRemote(Context context, List<Location> locationList) {
        try {
            if (locationList != null && locationList.size() > 0) {
                Log.e(TAG, " locationList:" + locationList.size());

                for (int i = 0; i < locationList.size(); i++) {
                    sendLocation(context, locationList.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLocation(Context context, Location location) {
        String deviceID = "", network = "", battery = "", imei = "", lat = "", lng = "", oldlat = "", oldlng = "";

        deviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        network = Utility.getNetworkType(context);

        battery = String.valueOf(Utility.getBatteryPercentage(context));

        imei = Utility.getDeviceImei(context);
        oldlat = Utility.getLatitude(context);
        oldlng = Utility.getLongitude(context);

        lat = String.valueOf(location.getLatitude());
        lng = String.valueOf(location.getLongitude());

        IOPref.getInstance().saveString(context, IOPref.PreferenceKey.LAT, lat + "");
        IOPref.getInstance().saveString(context, IOPref.PreferenceKey.LNG, lng + "");

        GeoHash hash = GeoHash.fromLocation(location, 12);
        String geoHash = hash.toString(); //"v12n8trdj";

        data = Utility.getUserID(context) + "," + battery + "," + lat + "," + lng + "," + geoHash + ","+
        Utility.getNetworkStrength(context) + "," + UtilDate.getCurrentDate() + "," +
                location.getAccuracy();

        Log.e(TAG, " onLocationChanged lat:" + lat + " lng:" + lng + " " + Utility.getOnDuty(context));

        if (Utility.getOnDuty(context)) {
            if (NetworkUtil.checkNetworkStatus(context)) {

                Log.e(TAG, "diff onLocationChanged lat:" + lat + " lng:" + lng + " " + Utility.getOnDuty(context));
                Socket socket = IOApp.getInstance().getSocketInstance();

                try {
                    if (!socket.isconnected()) {
                        socket.setListener(basicListener);
                        socket.connectAsync();
                    } else {
                        sendOfflineDataAndCurrentLocation(context, socket, data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "offline locationList else save :" + data);

                ArrayList<String> locationArrayList = new ArrayList<>();
                try {
                    String list = IOPref.getInstance().getString(context, IOPref.PreferenceKey.offlineLocation, "");
                    if (!TextUtils.isEmpty(list) && list.length() > 3) {
                        List<String> logs = new Gson().fromJson(list, new TypeToken<List<String>>() {
                        }.getType());
                        locationArrayList.addAll(logs);
                        locationArrayList.add(data);
                    } else {
                        locationArrayList.add(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    locationArrayList.add(data);
                }
                String result = new Gson().toJson(locationArrayList);
                Log.e(TAG, " result offline : " + result);
                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.offlineLocation, result);
            }
        } else {
            Log.e(TAG, " onLocationChanged offline lat:" + lat + " lng:" + lng + " " + Utility.getOnDuty(context));
            // Toast.makeText(context, "OFFLINE - " + data, Toast.LENGTH_SHORT).show();

        }
    }

    private void sendOfflineDataAndCurrentLocation(Context context, Socket socket, String data) {
        if (context!=null) {
            ArrayList<String> locationArrayList = new ArrayList<>();
            String list = IOPref.getInstance().getString(context, IOPref.PreferenceKey.offlineLocation, "");
            if (!TextUtils.isEmpty(list) && list.length() > 3) {
                List<String> logs = new Gson().fromJson(list, new TypeToken<List<String>>() {
                }.getType());
                locationArrayList.addAll(logs);
                for (String offlineData : locationArrayList) {
                    Log.e(TAG, "before offlineData:" + offlineData);
                    socket.emit("data", offlineData);
                    Log.e(TAG, "after offlineData:" + offlineData);
                }
                locationArrayList.clear();
                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.offlineLocation, new Gson().toJson(locationArrayList));

            }
            Log.e(TAG, "before data:" + data);
            socket.emit("data", data);
            Log.e(TAG, "after data:" + data);
        }
    }

//https://blog.codecentric.de/en/2014/05/android-gps-positioning-location-strategies/
    //https://stackoverflow.com/questions/46906017/getting-location-offline
    //https://github.com/googlesamples/android-play-location
    //https://github.com/googlesamples/android-play-location/blob/master/LocationUpdatesForegroundService/app/src/main/java/com/google/android/gms/location/sample/locationupdatesforegroundservice/MainActivity.java
    //https://hackernoon.com/android-location-tracking-with-a-service-80940218f561


    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}
