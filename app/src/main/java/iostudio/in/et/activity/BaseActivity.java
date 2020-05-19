package iostudio.in.et.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.ApiClient;
import iostudio.in.et.retrofit.api.ApiClient_image;
import iostudio.in.et.utility.Utility;
import iostudio.in.et.utility.Utils;
import okhttp3.RequestBody;
import retrofit2.Call;


/**
 * Created by acer 4745 on 20-11-2016.
 */

abstract public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public ProgressDialog progressDialog;
    public ApiClient apiClient;
    public ApiClient_image apiClient_image;
    Toolbar toolbar;
    String className;
    TextView textView_toolbar_title;
    DecimalFormat decimalFormat;
    //user/firebasedevice  device_id/user_id
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static boolean isPinValidated = false, isAnyActivityRunning = false, isHandlerRunninig = false;
    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i("BA >>", "*** THREAD *** isAnyActivityRunning >"+isAnyActivityRunning);
            if (!isAnyActivityRunning)
                isPinValidated = false;
            isHandlerRunninig = false;
        }
    };

    public Spanned getSpannedText(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        className = this.getClass().getSimpleName();
        decimalFormat = new DecimalFormat("#.##");
        apiClient = ApiClient.getInstance();
        apiClient_image = ApiClient_image.getInstance();
    }

    public void initBase() {
        initView();
        initData();
        bindEvent();
    }

    protected void addFragment(final int container, Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().add(container, fragment)
                    .addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(container, fragment).commit();
        }
    }

    protected void replaceFragment(final int container, Fragment fragment, boolean addToBackStack) {
        if (addToBackStack) {
            getSupportFragmentManager().beginTransaction().replace(container, fragment)
                    .addToBackStack(null).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(container, fragment).commit();
        }
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void bindEvent();

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.getOnDuty(this)) {
            if (Utils.requestingLocationUpdates(this)) {
                if (!checkPermissionsLocation()) {
                    requestPermissions();
                }
            }
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("BA >>", "*** onRestart *** isPinValidated >"+isPinValidated);

        /*if (!isAppOpenInForeBackGround(getApplicationContext())) {
            if (!isPinValidated && IOPref.getInstance().getString(this, IOPref.PreferenceKey.PIN, "").length() > 2) {
                startActivity(new Intent(this, EnterPinActivity.class));
            }
        }
        else
        {

        }*/


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("BA >>", "*** onStart *** isPinValidated >"+isPinValidated);
        if (!isAnyActivityRunning)
            isAnyActivityRunning = true;
        else
            isAnyActivityRunning = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("BA >>", "*** onStop *** isPinValidated >"+isPinValidated+" < isHandlerRunninig >"+isHandlerRunninig);
        if  (!isHandlerRunninig && isPinValidated) {
            startPinValidityHandler();
        }
        if (isAnyActivityRunning)
            isAnyActivityRunning = false;
        else
            isAnyActivityRunning = true;

        if (!isAppOpenInForeBackGround(getApplicationContext())) {
          //  if (!isPinValidated && IOPref.getInstance().getString(this, IOPref.PreferenceKey.PIN, "").length() > 2) {
                startActivity(new Intent(this, EnterPinActivity.class));
         //   }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: // default back
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startPinValidityHandler() {
        new Handler().postDelayed(runnable, 5000);
    }


    public void setToolbar(boolean isShowTitle, int logo_with_icon, boolean isHomeUpEnabled,
                           String toolbar_title_text) {
        toolbar = findViewById(R.id.toolbar_main);
        if (toolbar != null) {
            textView_toolbar_title = toolbar.findViewById(R.id.textView_toolbar_title);

        }
        //        toolbar.setContentInsetsAbsolute(0, 0);

        setSupportActionBar(toolbar);
        try {
            if (isHomeUpEnabled) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                // getSupportActionBar().setTitle(toolbar_title_text);
                textView_toolbar_title.setVisibility(View.VISIBLE);
                textView_toolbar_title.setText(toolbar_title_text);
            } else {
                if (isShowTitle) {
                    //getSupportActionBar().setTitle(toolbar_title_text);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    textView_toolbar_title.setText(toolbar_title_text);
                    textView_toolbar_title.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setLogo(logo_with_icon);
                    textView_toolbar_title.setVisibility(View.GONE);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setToolbar(boolean isShowTitle, int logo_with_icon, int secondary_icon,
                           boolean isHomeUpEnabled,
                           String toolbar_title_text) {
        toolbar = findViewById(R.id.toolbar_main);
        if (toolbar != null) {
            textView_toolbar_title = toolbar.findViewById(R.id.textView_toolbar_title);
        }
        //        toolbar.setContentInsetsAbsolute(0, 0);

        setSupportActionBar(toolbar);
        try {
            if (isHomeUpEnabled) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                // getSupportActionBar().setTitle(toolbar_title_text);
                textView_toolbar_title.setVisibility(View.VISIBLE);
                textView_toolbar_title.setText(toolbar_title_text);
            } else {
                if (isShowTitle) {
                    //getSupportActionBar().setTitle(toolbar_title_text);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    textView_toolbar_title.setText(toolbar_title_text);
                    textView_toolbar_title.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setLogo(logo_with_icon);
                    textView_toolbar_title.setVisibility(View.GONE);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setToolbar(boolean isShowTitle, boolean isHomeUpEnabled,
                           String toolbar_title_text, boolean isShowShadow) {
        toolbar = findViewById(R.id.toolbar_main);
        //AppBarLayout appbar_layout = findViewById(R.id.appbar_layout);
        if (toolbar != null) {
            textView_toolbar_title = toolbar.findViewById(R.id.textView_toolbar_title);
        }
        setSupportActionBar(toolbar);
        try {
            if (isShowShadow &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //  appbar_layout.setElevation(2.0f);
            }

            if (isHomeUpEnabled) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                textView_toolbar_title.setVisibility(View.VISIBLE);
                textView_toolbar_title.setText(toolbar_title_text);
            } else {
                if (isShowTitle) {
                    //getSupportActionBar().setTitle(toolbar_title_text);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    textView_toolbar_title.setText(toolbar_title_text);
                    textView_toolbar_title.setVisibility(View.VISIBLE);
                } else {
                    if (textView_toolbar_title != null)
                        textView_toolbar_title.setVisibility(View.GONE);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAlertNoInternet() {
        Alert.alert(this, "Alert", getString(R.string.no_internet), "", "Ok", null, null);
    }


    public void showMessage(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isFixedSize,
                                           boolean isAutoMeasureEnabled,
                                           boolean isNestedScrollingEnabled) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isNestedScrollingEnabled) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isFixedSize,
                                           boolean isAutoMeasureEnabled,
                                           boolean isNestedScrollingEnabled,
                                           RecyclerView.ItemDecoration dividerItemDecoration) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void showNoInternetConnection(String isBack) {
        if (isBack.equalsIgnoreCase("1")) {
            Alert.alert(this, "Alert", getString(R.string.no_internet), "", "Ok", null,
                    new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    });
        } else {
            Alert.alert(this, "Alert", getString(R.string.no_internet), "", "Ok", null, null);
        }
    }

    public void showProgressDialogSimple(Boolean isShow) {
        try {
            if (progressDialog != null &&
                    progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        if (isShow)
            progressDialog.show();
        else
            progressDialog.dismiss();

    }

    public void hideProgressDialogSimple() {
        progressDialog.dismiss();
    }


    public String buildSharedPreferenceKey(Call<Object> api, Map<String, String> inputMap) {
        String preferenceKeyGenerated = "";
        try {
            try {
                inputMap.values().removeAll(Collections.singleton(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Log.e("jsonString ", "" + jsonString);
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }

    public String buildSharedPreferenceKey(Call<Object> api, String inputMap) {
        String preferenceKeyGenerated = "";
        try {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }

    public String buildSharedPreferenceKey(Call<Object> api, RequestBody inputMap) {
        String preferenceKeyGenerated = "";
        try {
            String jsonString = inputMap.toString();
            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferenceKeyGenerated;
    }

    public boolean isValidString(String value) {
        return !TextUtils.isEmpty(value);
    }

    public boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    /**
     * Return the current state of the permissions needed.
     */
    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }




    public void hideKeyboard(Context context) {
        try {
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmptyString(String value) {
        return TextUtils.isEmpty(value);
    }

    private boolean checkPermissionsLocation() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public void requestPermissions() {
        Log.e(TAG, "*** INSIDE BASE ACTIVITY *********");
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.e(TAG, "Displaying permission rationale to provide additional context.");
            showMessage(getString(R.string.permission_rationale));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            Log.e(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    /**
     * method to check app open or closed
     */
    public boolean isAppOpenInForeBackGround(Context context) {
        boolean isAppOpen = false;
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(am
                .getRunningAppProcesses().size());
        for (ActivityManager.RunningTaskInfo runningTaskInfo : tasks) {
            if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName()))
                isAppOpen = true;
            break;
        }
        return isAppOpen;
    }


}
