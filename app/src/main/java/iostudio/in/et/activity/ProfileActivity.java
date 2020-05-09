package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import iostudio.in.et.R;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.response.Profile;
import iostudio.in.et.retrofit.response.ProfileData;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {
    Context context;
    AppCompatImageView iv_logo;
    AppCompatTextView tv_name;
    AppCompatTextView tv_address;
    AppCompatTextView tv_city;
    AppCompatTextView tv_designation;
    AppCompatTextView tv_company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        setToolbar(true, true, getString(R.string.profile), true);
        initBase();

    }

    @Override
    protected void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_designation = findViewById(R.id.tv_designation);
        tv_address = findViewById(R.id.tv_address);
        tv_city = findViewById(R.id.tv_city);
        tv_company = findViewById(R.id.tv_company);
        iv_logo = findViewById(R.id.iv_logo);

    }

    @Override
    protected void initData() {

        getProfile();
    }

    @SuppressWarnings("unchecked")
    private void getProfile() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

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
                                            tv_name.setText(name);
                                            tv_name.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_name.setVisibility(View.GONE);
                                        }

                                        String designation = data.getDesignation();
                                        if (!TextUtils.isEmpty(designation)) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.designation, designation);

                                            tv_designation.setText(designation);
                                            tv_designation.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_designation.setVisibility(View.GONE);
                                        }

                                        String address = data.getAddress();
                                        if (!TextUtils.isEmpty(address)) {
                                            tv_address.setText(address);
                                            tv_address.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_address.setVisibility(View.GONE);
                                        }

                                        String city = data.getCity();
                                        if (!TextUtils.isEmpty(city)) {
                                            tv_city.setText(city);
                                            tv_city.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_city.setVisibility(View.GONE);
                                        }
                                        String company = data.getCompany();
                                        if (!TextUtils.isEmpty(company)) {
                                            tv_company.setText(company);
                                            tv_company.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_company.setVisibility(View.GONE);
                                        }

                                        if (TextUtils.isEmpty(data.getImage())) {
                                            iv_logo.setBackground(ContextCompat.getDrawable(context, R.drawable.purple_circle));
                                            iv_logo.setImageResource(R.drawable.contact);
                                        } else {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.profileImage, data.getImage());
                                            Picasso.get()
                                                    .load(data.getImage())
                                                    .fit().centerInside()
                                                    .into(iv_logo);
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
                    }
                });

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void bindEvent() {

    }
}
