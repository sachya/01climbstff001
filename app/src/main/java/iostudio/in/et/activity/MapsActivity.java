package iostudio.in.et.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Client;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    Context context;
    AppCompatTextView tv_address;
    AppCompatEditText et_location;

    private ClientData clientData;
    String address = "";
    MaterialButton btn_update;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setToolbar(true, true, getString(R.string.update_client_location), true);

        context = this;
        initBase();
        getLastLocation();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void getLastLocation() {
        try {
            FusedLocationProviderClient mFusedLocationClient;
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

    @Override
    protected void initView() {
        tv_address = findViewById(R.id.tv_address);
        et_location = findViewById(R.id.et_location);
        btn_update = findViewById(R.id.btn_update);

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey(Constant.INTENT.CLIENT_OBJECT)) {
                clientData = bundle.getParcelable(Constant.INTENT.CLIENT_OBJECT);
            }
        }


        if (clientData != null) {
            getAddress(clientData.getClient_id());
        }
        setData(clientData);

    }

    private void setData(ClientData clientData) {
        String address = getString(R.string.last_location) + ": " + clientData.getAddress();
        tv_address.setText(address);
        setupMap();
    }

    public String getAddressFromLatLng(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            String address = addresses.get(0).getAddressLine(0);
            et_location.setText(address);

            return address;
        } catch (Exception e) {
            e.printStackTrace();
            et_location.setText("");
            return "";
        }
    }

    @SuppressWarnings("unchecked")
    private void getAddress(String id) {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                //"2017-02-14",
                /*Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                        meetingData.getMeeting_id(),
                        meetingData.getStatus(),
                        et_add_meeting_note.getText().toString().trim());
*/

                Call<Client> call = apiClient.getApi().getClientLocation(id);

                call.enqueue(new AppRetrofitCallback<Client>(context) {
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
                                   // showMessage(e.getMessage());
                                    showMessage(getString(R.string.something_went_wrong));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onResponseAppObject(Call call, Client response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        if (response.getData().size() > 0) {
                                            setData(response.getData().get(0));
                                        }
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
                        showMessage(getString(R.string.something_went_wrong));
                    }
                });

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void updateAddress(String id) {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                Map<String, String> request = RetrofitRequest.clientLocationRequest(
                        id,
                        //et_location.getText().toString().trim(),
                        Utility.getLatitude(context),
                        Utility.getLongitude(context),
                        UtilDate.getCurrentDate());

                Call<CommonResponse> call = apiClient.getApi().requestClientLocationUpdate(request);

                call.enqueue(new AppRetrofitCallback<CommonResponse>(context) {

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
                                  //  showMessage(e.getMessage());
                                    showMessage(getString(R.string.something_went_wrong));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(getString(R.string.something_went_wrong));
                        }
                    }

                    @Override
                    protected void onResponseAppObject(Call call, CommonResponse response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    onBackPressed();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                showMessage(getString(R.string.something_went_wrong));
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
                        showMessage(getString(R.string.something_went_wrong));
                    }
                });

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(getString(R.string.something_went_wrong));
        }
    }

    @Override
    protected void bindEvent() {
        btn_update.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            setupMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupMap() {
        Log.e("setupMap ", " getLatitude:" + Utility.getLatitude(context) + " getLongitude:" + Utility.getLongitude(context));
        // Add a marker in Sydney and move the camera
        if (!TextUtils.isEmpty(Utility.getLatitude(context)) && !TextUtils.isEmpty(Utility.getLongitude(context))) {
            LatLng sydney = new LatLng(Double.parseDouble(Utility.getLatitude(context)),
                    Double.parseDouble(Utility.getLongitude(context)));
            if (mMap != null) {
                mMap.setMinZoomPreference(10f);
                this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                }
                this.mMap.setMyLocationEnabled(true);
                mMap.addMarker(new MarkerOptions().position(sydney));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                getAddressFromLatLng(context, sydney);
            }
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_update:
                    if (clientData != null)
                        updateAddress(clientData.getClient_id());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
