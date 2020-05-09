package iostudio.in.et.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import iostudio.in.et.R;
import iostudio.in.et.adapter.DashboardPagerAdapter;
import iostudio.in.et.location.LocationUpdatesService;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.response.Profile;
import iostudio.in.et.retrofit.response.ProfileData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import iostudio.in.et.utility.Utils;
import retrofit2.Call;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    private Context context;
    ViewPager viewPagerDashboard;
    BottomNavigationView bottomNavigationView;
    DashboardPagerAdapter dashboardPagerAdapter;
    MenuItem prevMenuItem;
    private GoogleApiClient mGoogleApiClient;

    int selectedPosition = 1;

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;
    private String TAG = DashboardActivity.class.getSimpleName();

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    NavigationView navigationView;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    DrawerLayout drawer;
    ImageView iv_logo;
    AppCompatTextView tv_title, tvTermsAndPolicy;
    AppCompatTextView tv_desc, tv_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard3);
        context = this;
        initBase();
        Log.e(TAG, " onCreate");
        getProfile();
       setprofileData();

    }

    @SuppressWarnings("unchecked")
    private void getProfile() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(false);

                Call<Profile> call = apiClient.getApi().getProfileInformation(Utility.getUserID(context));

                call.enqueue(new AppRetrofitCallback<Profile>(context) {
                    @Override
                    protected void onResponseMazkara(Call call, Response response) {
                        try {
                            String code = String.valueOf(response.code());

                            if (code.substring(0, 2).contains("50")) {

                                showMessage(getString(R.string.something_went_wrong));
                                hideProgressDialogSimple();
                            } else if (!code.equalsIgnoreCase("200")) {
                                try {

                                    assert response.errorBody() != null;
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    showMessage(jObjError.getString("message"));
                                    hideProgressDialogSimple();
                                } catch (Exception e) {
                                    showMessage(e.getMessage());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onResponseAppObject(Call call, Profile response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        ProfileData data = response.getData();

                                        String name = data.getName();
                                        if (!TextUtils.isEmpty(name)) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.name, name);
                                        }

                                        String designation = data.getDesignation();
                                        if (!TextUtils.isEmpty(designation)) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.designation, designation);
                                        }

                                        if (!TextUtils.isEmpty(data.getImage())) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.profileImage, data.getImage());
                                        }

                                        if (!TextUtils.isEmpty(data.getCompany())) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.company, data.getCompany());
                                        }

                                        setprofileData();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    protected void common() {
                        hideProgressDialogSimple();
                    }


                    @Override
                    public void onFailure(Call call, Throwable t) {
                        super.onFailure(call, t);
                        Log.e("onFailure: ", " :" + t.getMessage());
                    }
                });

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void setprofileData() {
        String profileUrl = IOPref.getInstance().getString(context, IOPref.PreferenceKey.profileImage, "");
        if (!TextUtils.isEmpty(profileUrl)) {
            iv_logo.setBackground(null);
            Picasso.get().load(profileUrl).into(iv_logo);
        }
        String name = IOPref.getInstance().getString(context, IOPref.PreferenceKey.name, "");
        if (!TextUtils.isEmpty(name)){
            tv_title.setText(name);
            tv_title.setVisibility(View.VISIBLE);
        }else {
            tv_title.setVisibility(View.GONE);
        }

        String designation = IOPref.getInstance().getString(context, IOPref.PreferenceKey.designation, "");
        if (!TextUtils.isEmpty(designation)) {
            tv_desc.setText(designation);
            tv_desc.setVisibility(View.VISIBLE);
        } else {
            tv_desc.setVisibility(View.GONE);
        }

        if (IOPref.getInstance().getString(context, IOPref.PreferenceKey.company, "").length() > 0) {
            tv_company.setVisibility(View.VISIBLE);
            tv_company.setText(IOPref.getInstance().getString(context, IOPref.PreferenceKey.company, ""));
        } else {
            tv_company.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void initView() {
        viewPagerDashboard = findViewById(R.id.viewPagerDashboard);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        LinearLayout ll_profile = navigationView.findViewById(R.id.ll_profile);
        ll_profile.setOnClickListener(this);
        LinearLayout ll_accounts = navigationView.findViewById(R.id.ll_accounts);
        ll_accounts.setOnClickListener(this);
        //LinearLayout ll_report = navigationView.findViewById(R.id.ll_report);
        iv_logo = navigationView.findViewById(R.id.iv_logo);
        tv_title = navigationView.findViewById(R.id.tv_title);
        tv_desc = navigationView.findViewById(R.id.tv_desc);
        tv_company = navigationView.findViewById(R.id.tv_company);
        //ll_report.setOnClickListener(this);
        navigationView.findViewById(R.id.ll_setting).setOnClickListener(this);

        tvTermsAndPolicy = navigationView.findViewById(R.id.tv_terms_n_policy);
        SpannableString ss = new SpannableString("Terms & Conditions | Privacy Policy");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(DashboardActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, true);
                startActivity(i);
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(DashboardActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, false);
                startActivity(i);
            }
        };

        ss.setSpan(span1, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 21, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTermsAndPolicy.setText(ss);
        tvTermsAndPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void initData() {

        Log.e(TAG, " initData");
        dashboardPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager());
        viewPagerDashboard.setAdapter(dashboardPagerAdapter);
        //viewPagerDashboard.setOffscreenPageLimit(3);
        try {
            bottomNavigationView.getMenu().getItem(0).getActionView();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void bindEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_contacts:
                                selectedPosition = 0;
                                viewPagerDashboard.setCurrentItem(0);
                                break;
                            case R.id.action_home:
                                selectedPosition = 1;
                                viewPagerDashboard.setCurrentItem(1);
                                break;
                            case R.id.action_meeting:
                                selectedPosition = 2;
                                viewPagerDashboard.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });


        viewPagerDashboard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
*/
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.e(TAG, " onServiceConnected name:" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
            Log.e(TAG, "onServiceDisconnected  name:" + name);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, " onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

        viewPagerDashboard.setCurrentItem(selectedPosition);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
        Log.e(TAG, " onPause");

    }

    public void requestLocationUpdate() {
        mService.requestLocationUpdates();
    }

    public void removeLocationUpdate() {
        mService.removeLocationUpdates();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Update the buttons state depending on whether location updates are being requested.
        if (key.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
        }
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.ll_profile:
                    intent = new Intent();
                    intent.setClass(context, ProfileActivity.class);
                    startActivity(intent);
                    drawer.closeDrawers();
                    break;
                case R.id.ll_accounts:
                    intent = new Intent();
                    intent.setClass(context, AccountActivity.class);
                    startActivity(intent);
                    drawer.closeDrawers();
                    break;
                /*case R.id.ll_report:
                    intent = new Intent();
                    intent.setClass(context, ReportActivity.class);
                    startActivity(intent);
                    drawer.closeDrawers();
                    break;*/
                case R.id.ll_setting:
                    intent = new Intent(context, EnterPinActivity.class);
                    intent.putExtra("IS_UPDATE_PIN", true);
                    startActivity(intent);
                    drawer.closeDrawers();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDrawer() {
        Log.e(TAG, "openDrawer");
        drawer.openDrawer(GravityCompat.START);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive MyReceiver===========" + intent.getAction() + " " + intent.getExtras().toString());
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                Toast.makeText(getApplicationContext(), Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buildGoogleApiClient() {
        try {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {

                            }

                            @Override
                            public void onConnectionSuspended(int i) {

                            }
                        })
                        .enableAutoManage(this, 34992, new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Log.e(TAG, " connectionResult:" + connectionResult.getErrorMessage() + " " + connectionResult.getErrorCode());
                            }
                        })
                        .addApi(LocationServices.API)
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                            }
                        })
                        .build();
            }

            askTurnGpsOn();
            //createLocationRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void askTurnGpsOn() {
        if (mGoogleApiClient != null) {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mService.getLocationRequest());
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi
                            .checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(DashboardActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.CANCELED:
                            Log.e("CANCELED", ":called");
                            System.exit(0);
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            Log.e("CANCELED", "SETTINGS_CHANGE_UNAVAILABLE:called");
                            System.exit(0);
                            break;
                    }
                }
            });
        }
    }

    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.e(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(DashboardActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.e(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        initBase();


        // Restore the state of the buttons when the activity (re)launches.
        //setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                BIND_AUTO_CREATE | BIND_ADJUST_WITH_ACTIVITY);
    }


    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
        Log.e(TAG, " initData onStop");

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, " initData onDestroy");
    }
}
