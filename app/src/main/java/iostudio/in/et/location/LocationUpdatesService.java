/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package iostudio.in.et.location;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import iostudio.in.et.R;
import iostudio.in.et.activity.SplashActivity2;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.utility.Utility;
import iostudio.in.et.utility.Utils;

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 * <p>
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 * <p>
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification assocaited with that service is removed.
 */
public class LocationUpdatesService extends Service {

    private static final String PACKAGE_NAME = "iostudio.in.et";

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = (40 * 1000);

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS/2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL_IN_MILLISECONDS * 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;

    public LocationUpdatesService() {
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate ");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Log.e(TAG, "onLocationResult ");
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        checkAndStartServiceWithNotification();
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    private void checkAndStartServiceWithNotification() {
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this) || Utility.getOnDuty(getApplicationContext())) {
            Log.e(TAG, "Starting foreground service");

            startForeground(NOTIFICATION_ID, getNotification());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"ET onDestroy");
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.e(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
            //mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(getApplicationContext(), LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);

        PendingIntent pendingIntent =
                // PendingIntent.getActivity(this, 0, notificationIntent, 0);
                PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            //mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient.removeLocationUpdates(getPendingIntent());
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    public Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService.class);

        CharSequence text = Utils.getLocationText(mLocation);
        Log.e(TAG, " location update" + " text " + text);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashActivity2.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.on_duty, getString(R.string.launch_activity),
                        activityPendingIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setColorized(true)
                /*.addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)*/
                //Utils.getLocationTitle(this)
                .setContentText(getString(R.string.on_duty))
                // .setContentText(text)
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.login)
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Playback Notification",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                                Log.e(TAG, "mLocation :" + new Gson().toJson(mLocation));
                                try {
                                    IOPref.getInstance().saveString(getApplicationContext(), IOPref.PreferenceKey.LAT, mLocation.getLatitude() + "");
                                    IOPref.getInstance().saveString(getApplicationContext(), IOPref.PreferenceKey.LNG, mLocation.getLongitude() + "");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.e(TAG, "New location: " + location);
        mLocation = location;
        try {
            IOPref.getInstance().saveString(getApplicationContext(), IOPref.PreferenceKey.LAT, mLocation.getLatitude() + "");
            IOPref.getInstance().saveString(getApplicationContext(), IOPref.PreferenceKey.LNG, mLocation.getLongitude() + "");
        }catch (Exception e){
            e.printStackTrace();
        }
        // Notify anyone listening for broadcasts about the new location.
        //Intent intent = new Intent(ACTION_BROADCAST);
        // intent.putExtra(EXTRA_LOCATION, location);
        // LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        intent.putExtra(EXTRA_LOCATION, location);
        sendBroadcast(intent);

        // Update notification content if running as a foreground service.

        if (Utility.getOnDuty(getApplicationContext()))
        {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());

        }
        else
        {
            Log.e(TAG, " serviceIsRunningInForeground not called");
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }
        /*if (serviceIsRunningInForeground(this)) {
            Log.e(TAG, " serviceIsRunningInForeground called");
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        } else {
            Log.e(TAG, " serviceIsRunningInForeground not called");
        }*/


    }

    /**
     * Sets the location request parameters.
     * https://stackoverflow.com/questions/44948245/fusedlocationproviderclient-not-sending-location-updates
     * https://guides.codepath.com/android/Retrieving-Location-with-LocationServices-API
     * https://stackoverflow.com/a/30617800/5059725
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(5);
        //mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);

    }

    public LocationRequest getLocationRequest() {
        if (mLocationRequest == null) {
            createLocationRequest();
        }
        return mLocationRequest;
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onLowMemory() {
        Log.e(TAG, " ET onLowMemory");
        //Send broadcast to the Activity to kill this service and restart it.
        if (Utils.requestingLocationUpdates(this) || Utility.getOnDuty(getApplicationContext())) {
            requestLocationUpdates();
        }
        //checkAndStartServiceWithNotification();
        super.onLowMemory();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, " ET onTaskRemoved");
        if (Utils.requestingLocationUpdates(this) || Utility.getOnDuty(getApplicationContext())) {
            requestLocationUpdates();
        }
        //Send broadcast to the Activity to restart the service
        super.onDestroy();
    }


    //https://stackoverflow.com/questions/22066531/hide-notification-of-foreground-service-while-activity-is-visible?noredirect=1&lq=1
    //https://stackoverflow.com/questions/35181785/android-sometimes-force-kills-application
    //https://stackoverflow.com/questions/27471159/foreground-service-gets-killed-every-time
    //https://stackoverflow.com/questions/26288717/android-foreground-service-being-killed-under-certain-conditions
    //https://stackoverflow.com/questions/23787601/foreground-service-being-killed-on-notification-click
    //https://stackoverflow.com/questions/49637967/minimal-android-foreground-service-killed-on-high-end-phone?noredirect=1&lq=1
}

