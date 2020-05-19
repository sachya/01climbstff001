package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Type;
import iostudio.in.et.retrofit.response.TypeData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class CreateNewContactActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    MaterialButton btnSubmit;
    AppCompatSpinner spinner_type;
    AppCompatEditText et_remark;
    AppCompatEditText et_address;
    // AppCompatEditText et_owner_email_address;
    AppCompatEditText et_owner_contact_no;
    AppCompatEditText et_owner_name;
    AppCompatEditText et_company_name;
    LinearLayout ll_extra;

    private ArrayList<TypeData> typeDataArrayList;
    private ArrayList<String> typeTitleArrayList;
    private String selectedTypeID = "";
    private ArrayAdapter typeAdapter;
    private ClientData clientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);
        setToolbar(true, true, getString(R.string.add_new_contact), true);
        context = this;
        typeTitleArrayList = new ArrayList<>();
        typeDataArrayList = new ArrayList<>();
        initBase();

    }

    @Override
    protected void initView() {
        btnSubmit = findViewById(R.id.btnSubmit);
        spinner_type = findViewById(R.id.spinner_type);
        et_remark = findViewById(R.id.et_remark);
        et_address = findViewById(R.id.et_address);
        //  et_owner_email_address = findViewById(R.id.et_owner_email_address);
        et_owner_contact_no = findViewById(R.id.et_owner_contact_no);
        et_owner_name = findViewById(R.id.et_owner_name);
        et_company_name = findViewById(R.id.et_company_name);
        ll_extra = findViewById(R.id.ll_extra);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.CLIENT_OBJECT)) {
                clientData = bundle.getParcelable(Constant.INTENT.CLIENT_OBJECT);
                if (clientData != null) {
                    setToolbar(true, true, getString(R.string.update_contact), true);
                    Log.e(" clientData :", " " + new Gson().toJson(clientData));
                }
            }
        }
        getTypeData();


        //Creating the ArrayAdapter instance having the country list
        typeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, typeTitleArrayList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_type.setAdapter(typeAdapter);

    }


    @Override
    protected void bindEvent() {
        btnSubmit.setOnClickListener(this);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeID = typeDataArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnSubmit:
                    if (hasValidData()) {
                        createLead();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void createLead() {
        try {
            String userID = Utility.getUserID(context);
            String clientType = selectedTypeID;
            String companyName = et_company_name.getText().toString().trim();
            String companyAddress = et_address.getText().toString().trim();
            String ownerName = et_owner_name.getText().toString().trim();
            String ownerMobile = et_owner_contact_no.getText().toString().trim();
            String remark = et_remark.getText().toString().trim();
            //  String email = et_owner_email_address.getText().toString().trim();
            String createdAt = UtilDate.getCurrentDate();
            String lat = Utility.getLatitude(context);
            String lng = Utility.getLongitude(context);

            if (NetworkUtil.checkNetworkStatus(context)) {

                if (clientData == null) {
                    Map<String, String> request = RetrofitRequest.createLeadRequest(userID,
                            clientType,
                            companyName,
                            companyAddress,
                            ownerName,
                            ownerMobile,
                            createdAt,
                            lat,
                            lng,
                            remark);
                    leadRequest(request);
                } else {

                    Map<String, String> request = RetrofitRequest.updateClientRequest(userID,
                            clientType,
                            companyName,
                            companyAddress,
                            ownerName,
                            ownerMobile,
                            clientData.getClient_id());
                    updateRequest(request);

                }
            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @SuppressWarnings("unchecked")
    private void leadRequest(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient.getApi().requestClientAdd(request);

            call.enqueue(new AppRetrofitCallback<CommonResponse>(this) {
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
                protected void onResponseAppObject(Call call, CommonResponse response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());
                            if (response.getCode().equalsIgnoreCase("1")) {
                                finish();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void updateRequest(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient.getApi().requestClientUpdate(request);

            call.enqueue(new AppRetrofitCallback<CommonResponse>(this) {
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
                protected void onResponseAppObject(Call call, CommonResponse response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());
                            if (response.getCode().equalsIgnoreCase("1")) {
                                setResult(RESULT_OK);
                                finish();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasValidData() {
        String companyName = et_company_name.getText().toString().trim();
        String companyAddress = et_address.getText().toString().trim();
        String ownerName = et_owner_name.getText().toString().trim();
        String ownerMobile = et_owner_contact_no.getText().toString().trim();
        String remark = et_remark.getText().toString().trim();
        //String email = et_owner_email_address.getText().toString().trim();

        if (TextUtils.isEmpty(companyName)) {
            showMessage(getString(R.string.error_company_name));
            return false;
        }
        if (TextUtils.isEmpty(selectedTypeID)) {
            showMessage(getString(R.string.error_selected_type_id));
            return false;
        }
        if (TextUtils.isEmpty(companyAddress)) {
            showMessage(getString(R.string.error_company_address));
            return false;
        }
        if (TextUtils.isEmpty(ownerName)) {
            showMessage(getString(R.string.error_owner_name));
            return false;
        }
        if (TextUtils.isEmpty(ownerMobile)) {
            showMessage(getString(R.string.error_owner_mobile));
            return false;
        }
        return true;
    }


    private void getTypeData() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                getTypeRequest();

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getTypeRequest() {
        try {
            showProgressDialogSimple(true);

            Call<Type> call = apiClient.getApi().getClientTypeData(Utility.getUserID(context));

            call.enqueue(new AppRetrofitCallback<Type>(this) {
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
                protected void onResponseAppObject(Call call, Type response) {
                    if (response != null) {
                        try {

                            if (response.getData() != null &&
                                    response.getData().size() > 0) {
                                typeDataArrayList.clear();
                                typeTitleArrayList.clear();
                                typeDataArrayList.addAll(response.getData());

                                for (TypeData data : typeDataArrayList) {
                                    typeTitleArrayList.add(data.getName());
                                }

                                //typeAdapter.addAll(typeTitleArrayList);
                                typeAdapter.notifyDataSetChanged();

                                setdata(clientData);

                            } else {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setdata(ClientData clientData) {
        try {
            if (clientData != null) {
                ll_extra.setVisibility(View.GONE);
                btnSubmit.setText(getString(R.string.update));

                et_owner_name.setText(clientData.getOwner_name());
                et_company_name.setText(clientData.getCompany_name());
                et_owner_contact_no.setText(clientData.getOwner_mobile());
                et_address.setText(clientData.getCompany_address());


                if (!TextUtils.isEmpty(clientData.getClient_type_id())) {
                    for (int i = 0; i < typeDataArrayList.size(); i++) {
                        TypeData data = typeDataArrayList.get(i);
                        if (data != null) {
                            if (data.getId().equalsIgnoreCase(clientData.getClient_type_id())) {
                                Log.e("getClient_type_id ", " setSelection " + data.getId());
                                spinner_type.setSelection(i);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
