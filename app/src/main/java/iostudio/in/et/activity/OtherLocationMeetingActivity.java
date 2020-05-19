package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Meeting;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.retrofit.response.MeetingType;
import iostudio.in.et.retrofit.response.MeetingTypeData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class OtherLocationMeetingActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    AppCompatTextView tv_meeting_with;
    AppCompatEditText et_add_meeting_note;
    MaterialButton btn_confirm_status;
    MeetingData meetingData;

    private ArrayList<MeetingTypeData> typeDataArrayList=new ArrayList<>();
    private ArrayList<String> typeTitleArrayList=new ArrayList<>();
    private String selectedTypeID = "";
    private ArrayAdapter typeAdapter;
    AppCompatSpinner spinner_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_location_meeting);
        context = this;
        setToolbar(true, true, getString(R.string.other_location_meeting), true);
        initBase();
    }

    @Override
    protected void initView() {
        tv_meeting_with = findViewById(R.id.tv_meeting_with);
        et_add_meeting_note = findViewById(R.id.et_add_meeting_note);
        btn_confirm_status = findViewById(R.id.btn_confirm_status);
        spinner_type = findViewById(R.id.spinner_type);

    }

    @Override
    protected void initData() {
        try {
            getTypeData();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constant.INTENT.MEETING_OBJECT)) {
                    meetingData = bundle.getParcelable(Constant.INTENT.MEETING_OBJECT);
                }
            }

            if (meetingData != null) {
                tv_meeting_with.setText(meetingData.getName());
            }


            //Creating the ArrayAdapter instance having the country list
            typeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, typeTitleArrayList);
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_type.setAdapter(typeAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void bindEvent() {
        btn_confirm_status.setOnClickListener(this);

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
                case R.id.btn_confirm_status:
                    updateMeetingLocation();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            Call<MeetingType> call = apiClient.getApi().getMeetingTypeData();

            call.enqueue(new AppRetrofitCallback<MeetingType>(this) {
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
                            //    showMessage(e.getMessage());
                                showMessage(getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage(getString(R.string.something_went_wrong));
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, MeetingType response) {
                    if (response != null) {
                        try {

                            if (response.getData() != null &&
                                    response.getData().size() > 0) {
                                typeDataArrayList.clear();
                                typeTitleArrayList.clear();
                                typeDataArrayList.addAll(response.getData());

                                for (MeetingTypeData data : typeDataArrayList) {
                                    typeTitleArrayList.add(data.getStatus_name());
                                }

                                //typeAdapter.addAll(typeTitleArrayList);
                                typeAdapter.notifyDataSetChanged();

                                for (int i = 0; i < typeDataArrayList.size(); i++) {
                                    if (meetingData.getStatus().equalsIgnoreCase(typeDataArrayList.get(i).getId())) {
                                        selectedTypeID = typeDataArrayList.get(i).getId();
                                        spinner_type.setSelection(i);
                                        break;
                                    }
                                }

                            } else {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                  //  showMessage(response.getMessage());
                                showMessage(getString(R.string.something_went_wrong));

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
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(getString(R.string.something_went_wrong));
        }
    }


    @SuppressWarnings("unchecked")
    private void updateMeetingLocation() {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                    meetingData.getMeeting_id(),
                    UtilDate.getCurrentDate(),
                    selectedTypeID,
                    et_add_meeting_note.getText().toString().trim());

            Call<CommonResponse> call = apiClient.getApi().meetingOtherLocationData(request);

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
                                //showMessage(e.getMessage());
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
                                setResult(RESULT_OK);
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
    }
}
