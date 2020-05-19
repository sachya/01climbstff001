package iostudio.in.et.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Meeting;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class RescheduleCancelMeetingActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    AppCompatTextView tv_meeting_with;
    AppCompatTextView tv_date;
    AppCompatEditText et_add_meeting_note;
    MaterialButton btn_cancel;
    MaterialButton btn_reschedule;
    private MeetingData meetingData;
    private String selectedDate = "";
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_cancel_meeting);
        context = this;
        setToolbar(true, true, getString(R.string.reschedule_cancel_meeting), true);
        initBase();
    }

    @Override
    protected void initView() {
        tv_meeting_with = findViewById(R.id.tv_meeting_with);
        tv_date = findViewById(R.id.tv_date);
        et_add_meeting_note = findViewById(R.id.et_add_meeting_note);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_reschedule = findViewById(R.id.btn_reschedule);
    }

    @Override
    protected void initData() {
        try {
            calendar = Calendar.getInstance();
            updateEditText();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constant.INTENT.MEETING_OBJECT)) {
                    meetingData = bundle.getParcelable(Constant.INTENT.MEETING_OBJECT);
                }
            }

            if (meetingData != null) {
                tv_meeting_with.setText(meetingData.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateEditText();

                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        //max should be current date
        // datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        //30 days from current date
        //  Calendar lastmonthCal = Calendar.getInstance();
        //lastmonthCal.add(Calendar.DATE, -30);
        datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());

        //show the dialog
        datePickerDialog.show();
    }

    private void updateEditText() {
        try {
            String myFormat = "dd MMM yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String formatedDate = sdf.format(calendar.getTime());


            String attendaceFormat = "yyyy-MM-dd";
            SimpleDateFormat attendanceSdf = new SimpleDateFormat(attendaceFormat, Locale.US);
            selectedDate = attendanceSdf.format(calendar.getTime());

            tv_date.setText(formatedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void bindEvent() {
        btn_reschedule.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_date.setOnClickListener(this);
    }


    @SuppressWarnings("unchecked")
    private void updateMeeting() {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.meetingReschdeuleRequest(Utility.getUserID(context),
                    meetingData.getMeeting_id(),
                    selectedDate,
                    et_add_meeting_note.getText().toString().trim());

            Call<CommonResponse> call = apiClient.getApi().meetingReschdeule(request);

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

    @SuppressWarnings("unchecked")
    private void cancelMeeting() {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Call<CommonResponse> call = apiClient.getApi().meetingCancelInformation(meetingData.getMeeting_id());

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
    public void onClick(View v) {
        try {

            switch (v.getId()) {
                case R.id.btn_cancel:
                    Alert.alert(context, "", getString(R.string.cancel_meeting_desc), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                        @Override
                        public void run() {
                            cancelMeeting();
                        }
                    });
                    break;
                case R.id.btn_reschedule:
                    Alert.alert(context, "", getString(R.string.reschedule_meeting_desc), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                        @Override
                        public void run() {
                            updateMeeting();
                        }
                    });
                    break;
                case R.id.tv_date:
                    showDatePicker();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
