package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.ClientInfo;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class ContactDetailsActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    MaterialButton btn_update;
    MaterialButton btn_delete;
    AppCompatTextView tv_meeting_count;
    AppCompatTextView tv_contact_remark;
    AppCompatTextView tv_contact_location;
    AppCompatTextView tv_contact_address;
    AppCompatTextView tv_contact_type;
    AppCompatTextView tv_contact_company;
    AppCompatTextView tv_name;
    private ClientData clientData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        context = this;
        setToolbar(true, true, getString(R.string.contact_information), true);
        initBase();
    }

    @Override
    protected void initView() {
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        tv_meeting_count = findViewById(R.id.tv_meeting_count);
        tv_contact_remark = findViewById(R.id.tv_contact_remark);
        tv_contact_location = findViewById(R.id.tv_contact_location);
        tv_contact_address = findViewById(R.id.tv_contact_address);
        tv_contact_type = findViewById(R.id.tv_contact_type);
        tv_contact_company = findViewById(R.id.tv_contact_company);
        tv_name = findViewById(R.id.tv_name);
    }

    @Override
    protected void initData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constant.INTENT.CLIENT_OBJECT)) {
                    clientData = bundle.getParcelable(Constant.INTENT.CLIENT_OBJECT);
                    setData(clientData);
                    getClientDetails(clientData.getClient_id());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getClientDetails(String meeting_id) {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                //"2017-02-14",
                /*Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                        meetingData.getMeeting_id(),
                        meetingData.getStatus(),
                        et_add_meeting_note.getText().toString().trim());
*/

                Call<ClientInfo> call = apiClient.getApi().getClientInformation(meeting_id);

                call.enqueue(new AppRetrofitCallback<ClientInfo>(context) {
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
                    protected void onResponseAppObject(Call call, ClientInfo response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        ClientData data = response.getData();
                                        setData(data);
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

    private void setData(ClientData clientData) {
        try {
            if (clientData != null) {
                if (!isEmptyString(clientData.getOwner_name())) {
                    tv_name.setText(clientData.getOwner_name());
                    tv_name.setVisibility(View.VISIBLE);
                } else {
                    tv_name.setVisibility(View.GONE);
                }

                if (!isEmptyString(clientData.getCompany_name())) {
                    tv_contact_company.setText(clientData.getCompany_name());
                    tv_contact_company.setVisibility(View.VISIBLE);
                } else {
                    tv_contact_company.setVisibility(View.GONE);
                }
                if (!isEmptyString(clientData.getClient_type())) {
                    tv_contact_type.setText(clientData.getClient_type());
                    tv_contact_type.setVisibility(View.VISIBLE);
                } else {
                    tv_contact_type.setVisibility(View.GONE);
                }
                if (!isEmptyString(clientData.getCompany_address())) {
                    tv_contact_address.setText(clientData.getCompany_address());
                    tv_contact_address.setVisibility(View.VISIBLE);
                } else {
                    tv_contact_address.setVisibility(View.GONE);
                }
                if (!isEmptyString(clientData.getAddress())) {
                    tv_contact_location.setText(clientData.getAddress());
                    tv_contact_location.setVisibility(View.VISIBLE);
                } else {
                    tv_contact_location.setVisibility(View.GONE);
                }
                if (!isEmptyString(clientData.getRemark())) {
                    tv_contact_remark.setText(clientData.getRemark());
                    tv_contact_remark.setVisibility(View.VISIBLE);
                } else {
                    tv_contact_remark.setVisibility(View.GONE);
                }
                if (!isEmptyString(clientData.getMeetings_completed())) {
                    tv_meeting_count.setText(clientData.getMeetings_completed());
                    tv_meeting_count.setVisibility(View.VISIBLE);
                } else {
                    tv_meeting_count.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void bindEvent() {
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_update:
                    intent = new Intent();
                    intent.putExtra(Constant.INTENT.CLIENT_OBJECT, clientData);
                    intent.setClass(context, CreateNewContactActivity.class);
                    startActivityForResult(intent, Constant.INTENT_RESULT.CONTACT_UPDATE);
                    break;
                case R.id.btn_delete:
                    try {
                        Alert.alert(context, "", getString(R.string.are_you_sure_contact_delete), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                            @Override
                            public void run() {
                                deleteContact(clientData.getClient_id());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void deleteContact(String client_id) {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.deleteClientRequest(Utility.getUserID(context),
                    client_id,
                    UtilDate.getCurrentDate());

            Call<CommonResponse> call = apiClient.getApi().clientDelete(request);

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
                                showMessage(e.getMessage());
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
                                showMessage(response.getMessage());

                            if (response.getCode().equalsIgnoreCase("1")) {
                                onBackPressed();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constant.INTENT_RESULT.CONTACT_UPDATE) {
                if (resultCode == RESULT_OK) {
                    if (clientData != null)
                        getClientDetails(clientData.getClient_id());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
