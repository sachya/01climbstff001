package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Contractor;
import iostudio.in.et.retrofit.response.Login;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import retrofit2.Call;
import retrofit2.Response;

public class PhoneNumberActivity extends BaseActivity implements View.OnClickListener {
    private MaterialButton btn_login;
    private Context context;
    TextInputLayout til_number;
    TextInputEditText tie_phone;
    AppCompatTextView tv_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        initBase();
        context = this;

    }




    @Override
    protected void initView() {
        btn_login = findViewById(R.id.btn_login);
        tie_phone = findViewById(R.id.tie_phone);
        til_number = findViewById(R.id.til_number);
        tv_terms = findViewById(R.id.tv_terms);

    }

    @Override
    protected void initData() {

        String s = getString(R.string.terms_n_conditions_policy);
        tv_terms.setText(getSpannedText(s));
    }

    @Override
    protected void bindEvent() {
        btn_login.setOnClickListener(this);
        tv_terms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.tv_terms:

                    Intent intent  =new Intent();
                    intent.setClass(context,TermsActivity.class);
                    startActivity(intent);

                    break;
                case R.id.btn_login:
                    if (tie_phone.getText().toString().trim().length() == 10) {
                        login(tie_phone.getText().toString().trim());
                    } else {
                        showMessage(getString(R.string.error_invalid_phone));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void login(String phone) {
        if (NetworkUtil.checkNetworkStatus(context)) {

            Map<String, String> request = RetrofitRequest.loginRequest(phone);
            loginRequest(request);

        } else {
            showAlertNoInternet();
        }
    }

    @SuppressWarnings("unchecked")
    private void loginRequest(Map<String, String> request) {
        showProgressDialogSimple(true);

        Call<Login> call = apiClient.getApi().getLoginUser(request);

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
                            showMessage(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onResponseAppObject(Call call, Login response) {
                if (response != null) {
                    try {
                        if (response.getCode().equalsIgnoreCase("1")) {
                            switchToOTPActivity();
                        }
                        if (!TextUtils.isEmpty(response.getMessage()))
                            showMessage(response.getMessage());
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
    }

    private void switchToOTPActivity() {

        Intent intent = new Intent();
        intent.putExtra(Constant.INTENT.PHONE_NUMBER,tie_phone.getText().toString().trim());
        intent.setClass(context,OtpActivity.class);
        startActivity(intent);

    }
}
