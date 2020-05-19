package iostudio.in.et.fragment;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

import io.github.sac.Socket;
import iostudio.in.et.R;
import iostudio.in.et.activity.CreateNewExpenseActivity;
import iostudio.in.et.activity.CreateNewMeetingActivity1;
import iostudio.in.et.activity.DashboardActivity;
import iostudio.in.et.app.IOApp;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.ApiClient;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Home;
import iostudio.in.et.retrofit.response.HomeData;
import iostudio.in.et.retrofit.response.IncomeExpense;
import iostudio.in.et.utility.GPSConnectionListner;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.SocketConnectionListner;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import iostudio.in.et.utility.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    // Tracks the bound state of the service.
    private boolean mBound = false;
    private boolean isOnDuty;
    int status = 2;

    public HomeFragment() {
        // Required empty public constructor
    }

    ConstraintLayout cl_root_dashboard;
    RecyclerView recyclerViewDashboard;
    Context context;
    private GoogleApiClient mGoogleApiClient;

    AppCompatTextView tv_duty_status;
    AppCompatTextView  tv_quote;
    AppCompatTextView tv_name;
    AppCompatTextView tv_ta_da_amt;
    AppCompatTextView tv_expense_amt;
    AppCompatTextView tv_meeting_count;
    AppCompatTextView tv_expense_month;
    AppCompatTextView tv_in_month;
    AppCompatImageView iv_location;
    AppCompatImageView iv_logo;
    AppCompatTextView tv_km;
    AppCompatTextView tv_location;
    AppCompatTextView tv_exp_label;
    AppCompatTextView tv_ta_label;
    LinearLayout ll_status;
    LinearLayout ll_add_entry;
    LinearLayout ll_add_meeting;
    LinearLayout ll_meeting;

    public static SocketConnectionListner socketConnectionListner;
    public static GPSConnectionListner gpsConnectionListner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        iv_logo = view.findViewById(R.id.iv_logo);
        cl_root_dashboard = view.findViewById(R.id.cl_root_dashboard);
        recyclerViewDashboard = view.findViewById(R.id.recyclerViewDashboard);
        tv_duty_status = view.findViewById(R.id.tv_duty_status);
        tv_quote = view.findViewById(R.id.tv_quote);
        tv_meeting_count = view.findViewById(R.id.tv_meeting_count);
        tv_expense_month = view.findViewById(R.id.tv_expense_month);
        tv_in_month = view.findViewById(R.id.tv_in_month);
        tv_name = view.findViewById(R.id.tv_name);
        ll_status = view.findViewById(R.id.ll_status);
        ll_add_entry = view.findViewById(R.id.ll_add_entry);
        ll_add_meeting = view.findViewById(R.id.ll_add_meeting);
        ll_meeting = view.findViewById(R.id.ll_meeting);
        iv_location = view.findViewById(R.id.iv_location);
        tv_ta_da_amt = view.findViewById(R.id.tv_ta_da_amt);
        tv_expense_amt = view.findViewById(R.id.tv_expense_amt);
        tv_km = view.findViewById(R.id.tv_km);
        tv_exp_label = view.findViewById(R.id.tv_exp_label);
        tv_location = view.findViewById(R.id.tv_location);
        tv_ta_label = view.findViewById(R.id.tv_ta_label);
        context = getActivity();
        status = 2;


        // Check that the user hasn't revoked permissions by going to Settings.
        /*if (Utils.requestingLocationUpdates(getActivity())) {
            if (!checkPermissionsLocation()) {
                ((DashboardActivity) getActivity()).requestPermissions();
            }
        }else {
            if (!checkPermissionsLocation()) {
                ((DashboardActivity) getActivity()).requestPermissions();
            }
        }*/
       /* if (!checkPermissionsLocation()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }*/
        boolean isOnDuty = Utility.getOnDuty(context);
        updateDutyUI(isOnDuty);

        ll_status.setOnClickListener(this);
        ll_add_meeting.setOnClickListener(this);
        ll_add_entry.setOnClickListener(this);
        iv_logo.setOnClickListener(this);

        //getData();
        sendStatusRequest(status);

        gpsConnectionListner = new GPSConnectionListner() {
            @Override
            public void onGPSCallback(Context mContext, String Socketstatus)
            {
                if (Socketstatus.equalsIgnoreCase("connect"))
                {
                   ontapClick();
                }
                else if (Socketstatus.equalsIgnoreCase("disconnect"))
                {
                    Utility.showMessage(context,"Something went wrong,Please try again");
                    ll_status.setEnabled(true);
                }

            }
        };
        return view;
    }

    private void updateDutyUI(boolean isOnDuty) {
        try {
            Log.e(TAG, " updateDutyUI:" + isOnDuty);
            if (isOnDuty) {
                tv_duty_status.setText(getString(R.string.finish_day));
                tv_duty_status.setTextColor(ContextCompat.getColor(context, R.color.text_red));
                iv_location.setColorFilter(ContextCompat.getColor(context, R.color.textColorGreen));
            } else {
                tv_duty_status.setText(getString(R.string.tap_to_start));
                tv_duty_status.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondaryDark));
                iv_location.setColorFilter(ContextCompat.getColor(context, R.color.textColorSecondaryMid));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setButtonsState(boolean isChecked) {
        Log.e("isChecked", ":" + isChecked);
        try {
            Socket socket = IOApp.getInstance().getSocketInstance();
            if (isChecked) {

                if (!socket.isconnected()) {
                    socket.connectAsync();
                }
                else
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendStatusRequest(status);
                        }
                    });
                }


                socketConnectionListner = new SocketConnectionListner() {
                    @Override
                    public void onSocketCallback(Context mContext, String Socketstatus)
                    {
                        if (Socketstatus.equalsIgnoreCase("connect"))
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendStatusRequest(status);
                                }
                            });

                        }
                        else if (Socketstatus.equalsIgnoreCase("Disconnect"))
                        {
                            Utility.showMessage(context,"Something went wrong,Please try again");
                          ll_status.setEnabled(true);
                        }

                    }
                };

           /*     if (!checkPermissions()) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                    ((DashboardActivity) getActivity()).requestPermissions();
                    IOPref.getInstance().saveBoolean(context, IOPref.PreferenceKey.onDuty, false);
                    updateDutyUI(false);
                } else {
                    if (mGoogleApiClient == null) {
                        ((DashboardActivity) getActivity()).buildGoogleApiClient();
                    } else if (!isGPSEnabled(context)) {
                        ((DashboardActivity) getActivity()).askTurnGpsOn();
                    }
                    ((DashboardActivity) getActivity()).requestLocationUpdate();
                }*/
            } else {
                if (socket.isconnected()) {
                    socket.disconnect();
                }
                ((DashboardActivity) getActivity()).removeLocationUpdate();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Return the current state of the permissions needed.
     */
    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissionsLocation() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
    }


    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.iv_logo:
                    ((DashboardActivity) getActivity()).openDrawer();
                    break;
                case R.id.ll_add_entry:
                    intent = new Intent();
                    intent.setClass(getContext(), CreateNewExpenseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_add_meeting:
                    intent = new Intent();
                    intent.setClass(getContext(), CreateNewMeetingActivity1.class);
                    startActivity(intent);
                    break;
                case R.id.ll_status:
                    if (NetworkUtil.checkNetworkStatus(context)) {
                        Log.e(TAG, " ll_status: clicked");

                        if (!checkPermissions()) {
                            status = 0;
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                        } else {
                            if (mGoogleApiClient == null) {
                                ((DashboardActivity) getActivity()).buildGoogleApiClient();
                                ontapClick();
                            } else
                            {
                               ontapClick();
                            }

                        }
                    } else {
                        showAlertNoInternet();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if (Utility.getOnDuty(context))
        {
            sendStatusRequest(0);
        }*/

     // sendStatusRequest(2);

    }

    @SuppressWarnings("unchecked")
    private void sendStatusRequest(final int status) {
        try {
            showProgressDialogSimple(true);
            Map<String, String> request = RetrofitRequest.statusRequest(Utility.getUserID(getContext()),
                    UtilDate.getCurrentDate(),
                    String.valueOf(Utility.getBatteryPercentage(context)),
                    Utility.getLatitude(context),
                    Utility.getLongitude(context),
                    String.valueOf(status));


            Call<Home> call = apiClient.getApi().getHomeStatus(request);

            call.enqueue(new AppRetrofitCallback<Home>(context) {
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

                            } catch (Exception e) {
                               // showMessage(e.getMessage());
                                showMessage(getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage(getString(R.string.something_went_wrong));
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, Home response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()) && ((DashboardActivity)getActivity()).getSelectedPosition() == 1)
                                showMessage(response.getMessage());

                            if (status != 2) {
                                IOPref.getInstance().saveBoolean(context, IOPref.PreferenceKey.onDuty, isOnDuty);
                                if (!Utility.getOnDuty(context))
                                {
                                    NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.cancelAll();

                                }
                                else
                                {
                                    ((DashboardActivity) getActivity()).displayDuty();
                                }

                                updateDutyUI(isOnDuty);
                              //  setButtonsState(isOnDuty);
                            }

                            HomeData data = response.getData();
                            if (!Utility.getOnDuty(context))
                            {
                                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancelAll();

                            }
                            else
                            {
                                ((DashboardActivity) getActivity()).displayDuty();
                            }

                            setHomeData(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(getString(R.string.something_went_wrong));
                        }
                    }
                }

                @Override
                protected void common() {
                    hideProgressDialogSimple();
                    ll_status.setEnabled(true);
                }


                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    Log.e("onFailure: ", " :" + t.getMessage());
                    showMessage(getString(R.string.something_went_wrong));
                }
            });
        } catch (Exception e) {
            ll_status.setEnabled(true);
            e.printStackTrace();
            showMessage(getString(R.string.something_went_wrong));
        }
    }

    private void setHomeData(HomeData data) {
        try {
            if (data != null) {
                if (!isEmptyString(data.getQuote())) {
                    tv_quote.setText(data.getQuote());
                }
                if (!isEmptyString(data.getUser()))
                    tv_name.setText("Hi, " + data.getUser());

                if (!isEmptyString(data.getMeetingCount()))
                    tv_meeting_count.setText(data.getMeetingCount());

                IncomeExpense income = data.getIncome();
                if (income != null) {
                    tv_ta_da_amt.setText(income.getAmount()+" ₹");
                    tv_ta_label.setText(income.getLabel());
                    if (!TextUtils.isEmpty(income.getMonth())) {
                        tv_in_month.setText(income.getMonth());
                    }
                }


                IncomeExpense expense = data.getExpense();
                if (expense != null) {
                    tv_expense_amt.setText(expense.getAmount() +" ₹");
                    tv_exp_label.setText(expense.getLabel());

                    if (expense.getMonth() != null) {
                        tv_expense_month.setText(expense.getMonth());
                    }
                }


                if (data.getGeo() != null) {
                    tv_km.setText(data.getGeo().getKm() + " KM");
                    tv_location.setText(data.getGeo().getLocation());
                }

                 // String token =  IOPref.getInstance().getString(getActivity(), IOPref.PreferenceKey.FIREBASE_TOKEN, "");
                     //   updateDeviceIdToServer(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            FusedLocationProviderClient mFusedLocationClient;
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location mLocation = task.getResult();
                                Log.e(TAG, "mLocation :" + new Gson().toJson(mLocation));
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }
    int request_status;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (status != 2) {
                    if (mGoogleApiClient == null) {
                        ((DashboardActivity) getActivity()).buildGoogleApiClient();
                    } else if (!isGPSEnabled(context)) {
                        ((DashboardActivity) getActivity()).askTurnGpsOn();
                    }
                    ((DashboardActivity) getActivity()).requestLocationUpdate();
                    isOnDuty = Utility.getOnDuty(context);
                     request_status = 0;
                    if (isOnDuty) {
                        isOnDuty = false;
                        status = 0;
                    } else {
                        isOnDuty = true;
                        status = 1;
                        setButtonsState(isOnDuty);
                    }
                    ll_status.setEnabled(false);

                   /* final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendStatusRequest(request_status);

                        }
                    }, 3000);*/

                   // sendStatusRequest(status);
                }
            } else {
               /* Snackbar.make(
                        getActivity().findViewById(R.id.activity_main),
                        R.string.permission_rationale,
                        Snackbar.LENGTH_LONG)
                        .show();*/
               showMessage(getString(R.string.permission_rationale));
            }
        }
    }


    private void ontapClick()
    {
        if (isGPSEnabled(context))
        {
           // ((DashboardActivity) getActivity()).requestLocationUpdate();
            isOnDuty = Utility.getOnDuty(context);
            status = 0;
            if (isOnDuty) {
                Socket socket = IOApp.getInstance().getSocketInstance();
                if (socket.isconnected()) {
                    socket.disconnect();
                }
                ((DashboardActivity) getActivity()).removeLocationUpdate();
                isOnDuty = false;
                status = 0;
                sendStatusRequest(status);
            } else {

                ((DashboardActivity) getActivity()).requestLocationUpdate();
                isOnDuty = true;
                status = 1;
                setButtonsState(isOnDuty);
            }
            ll_status.setEnabled(false);
            //  sendStatusRequest(status);
        }
        else
        {
            ((DashboardActivity) getActivity()).askTurnGpsOn();
        }

    }


    private void updateDeviceIdToServer(String device_id) {
        try {
            Call<CommonResponse> call = ApiClient.getInstance().getApi().requestToUpdateFirebaseDeviceId(RetrofitRequest.getUpdateFirebaseDeviceIdRequest(device_id, Utility.getUserID(getActivity())));

            call.enqueue(new AppRetrofitCallback<CommonResponse>(getActivity()) {
                @Override
                protected void onResponseMazkara(Call call, Response response) {
                    try {
                        String code = String.valueOf(response.code());

                        if (code.substring(0, 2).contains("50")) {

                            Log.i("MyFirebaseMsgService", getString(R.string.something_went_wrong));
                            Utility.showMessage(getActivity(), getString(R.string.something_went_wrong));

                        } else if (!code.equalsIgnoreCase("200")) {
                            try {

                                assert response.errorBody() != null;
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Utility.showMessage(getActivity(), jObjError.getString("message"));

                            } catch (Exception e) {
                                // Utility.showMessage(getApplicationContext(), e.getMessage());
                                Utility.showMessage(getActivity(), getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, CommonResponse response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                Utility.showMessage(getActivity(), response.getMessage());
                            if (response.getCode().equalsIgnoreCase("1")) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                protected void common() {
                }


                @Override
                public void onFailure(Call call, Throwable t) {
                    super.onFailure(call, t);
                    Log.e("onFailure: ", " :" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
