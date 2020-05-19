package iostudio.in.et.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.adapter.MeetingRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Meeting;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class PastMeetingActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    RecyclerView recyclerViewMeeting;
    MeetingRecyclerAdapter meetingRecyclerAdapter;

    String fromDate;
    String toDate;
    AppCompatTextView tv_no_data;
    AppCompatTextView tv_to_date;
    AppCompatTextView tv_from_date;
    MaterialButton btnShow;
    private ArrayList<MeetingData> dataArrayList;
    private Calendar calendarFrom;
    private Calendar calendarTo;
    private long fromDateMilliSec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_meeting);
        context = this;
        setToolbar(true, true, getString(R.string.past_meetings), true);
        initBase();

    }

    @Override
    protected void initView() {
        recyclerViewMeeting = findViewById(R.id.recyclerViewMeeting);
        tv_no_data = findViewById(R.id.tv_no_data);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        btnShow = findViewById(R.id.btnShow);

    }

    @Override
    protected void initData() {
        calendarFrom = Calendar.getInstance();
        dataArrayList = new ArrayList<>();
        //Calendar lastmonthCal = Calendar.getInstance();
        calendarFrom.add(Calendar.DATE, -7);
        updateEditText(0);
        calendarTo = Calendar.getInstance();
        updateEditText(1);
        getMeetingData();

        meetingRecyclerAdapter = new MeetingRecyclerAdapter(context, dataArrayList);
        setRecyclerViewWithAdapter(recyclerViewMeeting,
                new LinearLayoutManager(context),
                meetingRecyclerAdapter,
                false);
    }

    @Override
    protected void bindEvent() {
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);
        btnShow.setOnClickListener(this);
    }


    private void getMeetingData() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {
                getDataRequest();
            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getDataRequest() {
        try {
            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.meetingRequest(Utility.getUserID(context),
                    fromDate,
                    toDate);

            Call<Meeting> call = apiClient.getApi().getMeetingData(request);

            call.enqueue(new AppRetrofitCallback<Meeting>(context) {
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
                protected void onResponseAppObject(Call call, Meeting response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());


                            if (response.getData() != null && response.getData().size() > 0) {
                                dataArrayList.clear();
                                dataArrayList.addAll(response.getData());
                                meetingRecyclerAdapter.notifyDataSetChanged();
                                recyclerViewMeeting.setVisibility(View.VISIBLE);
                                tv_no_data.setVisibility(View.GONE);
                            } else {
                                recyclerViewMeeting.setVisibility(View.GONE);
                                tv_no_data.setVisibility(View.VISIBLE);
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


    private void showDatePicker(final int type) {

        DatePickerDialog datePickerDialog;

        if (type==0) {

            datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            calendarFrom.set(Calendar.YEAR, year);
                            calendarFrom.set(Calendar.MONTH, monthOfYear);
                            calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateEditText(type);

                        }

                    },
                    calendarFrom.get(Calendar.YEAR),
                    calendarFrom.get(Calendar.MONTH),
                    calendarFrom.get(Calendar.DAY_OF_MONTH));

        }else{
            datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            calendarTo.set(Calendar.YEAR, year);
                            calendarTo.set(Calendar.MONTH, monthOfYear);
                            calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateEditText(type);

                        }

                    },
                    calendarTo.get(Calendar.YEAR),
                    calendarTo.get(Calendar.MONTH),
                    calendarTo.get(Calendar.DAY_OF_MONTH));
        }
        //max should be current date
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        //30 days from current date
        //  Calendar lastmonthCal = Calendar.getInstance();
        //lastmonthCal.add(Calendar.DATE, -30);
        if (type == 1)
            datePickerDialog.getDatePicker().setMinDate(fromDateMilliSec);

        //show the dialog
        datePickerDialog.show();
    }

    private void updateEditText(int type) {

        String myFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formatedDate ="";


        String attendaceFormat = "yyyy-MM-dd";
        SimpleDateFormat attendanceSdf = new SimpleDateFormat(attendaceFormat, Locale.US);
        String originalDate = "";

        if (type == 0) {
            originalDate = attendanceSdf.format(calendarFrom.getTime());
            formatedDate = sdf.format(calendarFrom.getTime());
            fromDateMilliSec = calendarFrom.getTimeInMillis();
            tv_from_date.setText(formatedDate);
            fromDate = originalDate;
        } else {
            formatedDate  = sdf.format(calendarTo.getTime());
            originalDate = attendanceSdf.format(calendarTo.getTime());
            toDate = originalDate;
            tv_to_date.setText(formatedDate);
        }


    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.tv_from_date:
                    showDatePicker(0);
                    break;
                case R.id.tv_to_date:
                    showDatePicker(1);
                    break;
                case R.id.btnShow:
                    getMeetingData();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
