package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONObject;

import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Login;
import iostudio.in.et.retrofit.response.LoginData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class OtpActivity extends BaseActivity {

    private Context context;
    OtpView otp_view;
    String phoneNumber;
    MaterialButton btn_verify;
    AppCompatTextView tv_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initBase();
        context = this;


    }

    @Override
    protected void initView() {
        otp_view = findViewById(R.id.otp_view);
        btn_verify = findViewById(R.id.btn_verify);
        tv_terms = findViewById(R.id.tv_terms);

        SpannableString ss = new SpannableString("Terms & Conditions | Privacy Policy");
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(OtpActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, true);
                startActivity(i);
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(OtpActivity.this, TermsNCondActivity.class);
                i.putExtra(Constant.INTENT.DO_LOAD_TERMS, false);
                startActivity(i);
            }
        };

        ss.setSpan(span1, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 21, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_terms.setText(ss);
        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.PHONE_NUMBER)) {
                phoneNumber = bundle.getString(Constant.INTENT.PHONE_NUMBER);
            }
        }


     /*   String s = getString(R.string.terms_n_conditions_policy);
        tv_terms.setText(getSpannedText(s));*/


    }

    @Override
    protected void bindEvent() {

        otp_view.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                hideKeyboard(context);

            }
        });
tv_terms.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent  =new Intent();
        intent.setClass(context,TermsActivity.class);
        startActivity(intent);
    }
});
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn_verify", ": clicked");
                login(phoneNumber, otp_view.getText().toString().trim());
            }
        });

    }



    @SuppressWarnings("unchecked")
    private void login(String phone, String otp) {
        if (NetworkUtil.checkNetworkStatus(context)) {
            String imei_no = Utility.getDeviceImei(context);
            String model_no = Utility.getModelNo();
            String token =  IOPref.getInstance().getString(context, IOPref.PreferenceKey.FIREBASE_TOKEN, "");
            Map<String, String> request = RetrofitRequest.otpRequest(phone, otp, imei_no, model_no,token);
            loginRequest(request);

        } else {
            showAlertNoInternet();
        }
    }

    @SuppressWarnings("unchecked")
    private void loginRequest(Map<String, String> request) {
        showProgressDialogSimple(true);

        Call<Login> call = apiClient.getApi().getLoginVerifyUser(request);

        call.enqueue(new AppRetrofitCallback<Login>(this) {
            @Override
            protected void onResponseMazkara(Call call, Response response) {
                try {
                    String code = String.valueOf(response.code());

                    if (code.substring(0, 2).contains("50")) {

                        showMessage(getString(R.string.something_went_wrong));

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
            protected void onResponseAppObject(Call call, Login response) {
                if (response != null) {
                    try {
                        if (!TextUtils.isEmpty(response.getMessage()))
                            showMessage(response.getMessage());
                        if (response.getCode().equalsIgnoreCase("1")) {

                            LoginData data = response.getData();

                            if (data != null) {

                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.mobile, data.getMobile());
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.first_name, data.getFirst_name());
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.last_name, data.getLast_name());
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.email, data.getEmail());
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.city, data.getCity());
                                IOPref.getInstance().saveString(context, IOPref.PreferenceKey.userID, data.getId());

                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setClass(context, SetPinActivity.class);
                                startActivity(intent);
                                finish();
                            }
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, PhoneNumberActivity.class));
        finish();
    }
}
