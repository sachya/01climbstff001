package iostudio.in.et.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import iostudio.in.et.R;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Type;
import iostudio.in.et.retrofit.response.TypeData;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class CreateNewLeaveActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    // String[] leave = {"Week Off", "Bereavement", "Sick", "Maternity/Paternity*", "Other"};


    MaterialButton btnSubmit;
    AppCompatSpinner spinner_leave;
    AppCompatEditText et_from;
    AppCompatEditText et_leave_reason;
    AppCompatEditText et_to;
    private Calendar calendarFrom;
    private Calendar calendarTo;
    boolean isFromDate;
    String fromDate;
    String toDate;


    private ArrayList<TypeData> typeDataArrayList;
    private ArrayList<String> typeTitleArrayList;
    private String selectedTypeID = "";
    private ArrayAdapter typeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_leave);
        setToolbar(true, true, getString(R.string.create_new), true);
        typeDataArrayList = new ArrayList<>();
        typeTitleArrayList = new ArrayList<>();
        context = this;
        initBase();

    }

    @Override
    protected void initView() {
        btnSubmit = findViewById(R.id.btnSubmit);
        spinner_leave = findViewById(R.id.spinner_leave);
        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);
        et_leave_reason = findViewById(R.id.et_leave_reason);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void initData() {

        //set current date
        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();


        //Creating the ArrayAdapter instance having the country list
        typeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, typeTitleArrayList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_leave.setAdapter(typeAdapter);


        getTypeData();

    }

    private void getTypeData() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                Map<String, String> request = RetrofitRequest.buildUserID(Utility.getUserID(context));
                getTypeRequest(request);

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void getTypeRequest(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<Type> call = apiClient.getApi().getLeaveTypeData(request);

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

                            if (response.getCode().equalsIgnoreCase("1") &&
                                    response.getData() != null &&
                                    response.getData().size() > 0) {
                                typeDataArrayList.clear();
                                typeTitleArrayList.clear();
                                typeDataArrayList.addAll(response.getData());

                                for (TypeData data : typeDataArrayList) {
                                    typeTitleArrayList.add(data.getName());
                                }

                               // typeAdapter.addAll(typeTitleArrayList);
                                typeAdapter.notifyDataSetChanged();

                            }else{
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



    @Override
    protected void bindEvent() {

        et_from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isFromDate = true;
                    showDatePicker(calendarFrom);
                    Log.e("calendarFrom", " onFocusChange :" + isFromDate);

                }
            }
        });
        et_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isFromDate = false;
                    showDatePicker(calendarTo);
                    Log.e("calendarTo", " onFocusChange :" + isFromDate);

                }
            }
        });
        et_from.setOnClickListener(this);
        et_to.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.et_from:
                    isFromDate = true;
                    showDatePicker(calendarFrom);
                    Log.e("calendarFrom", "clicked :" + isFromDate);

                    break;
                case R.id.et_to:
                    isFromDate = false;
                    showDatePicker(calendarTo);
                    Log.e("calendarTo", " clicked :" + isFromDate);

                    break;
                case R.id.btnSubmit:
                    if (isValidData()) {
                        applyForLeave();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidData() {
        String reason = et_leave_reason.getText().toString().trim();
        String from = et_from.getText().toString().trim();
        String to = et_to.getText().toString().trim();
        if (TextUtils.isEmpty(reason)) {
            showMessage(getString(R.string.error_reason));
            return false;
        }
        if (TextUtils.isEmpty(from)) {
            showMessage(getString(R.string.error_select_from_date));
            return false;
        }
        if (TextUtils.isEmpty(to)) {
            showMessage(getString(R.string.error_select_to_date));
            return false;
        }
        return true;
    }


    @SuppressWarnings("unchecked")
    private void applyForLeave() {
        try {
            String userID = Utility.getUserID(context);
            String reason = et_leave_reason.getText().toString().trim();

            if (NetworkUtil.checkNetworkStatus(context)) {

                Map<String, String> request = RetrofitRequest.applyLeaveRequest(userID, reason, fromDate, toDate);
                leaveRequest(request);

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @SuppressWarnings("unchecked")
    private void leaveRequest(Map<String, String> request) {
        showProgressDialogSimple(true);

        Call<CommonResponse> call = apiClient.getApi().requestLeave(request);

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
    }


    DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (isFromDate) {
                calendarFrom.set(Calendar.YEAR, year);
                calendarFrom.set(Calendar.MONTH, monthOfYear);
                calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(calendarFrom);
            } else {
                calendarTo.set(Calendar.YEAR, year);
                calendarTo.set(Calendar.MONTH, monthOfYear);
                calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditText(calendarTo);
            }

        }

    };

    private void updateEditText(Calendar calendar) {
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formatedDate = sdf.format(calendar.getTime());


        String attendaceFormat = "yyyy-MM-dd";
        SimpleDateFormat attendanceSdf = new SimpleDateFormat(attendaceFormat, Locale.US);
        String originalDate = attendanceSdf.format(calendar.getTime());

        if (isFromDate) {
            fromDate = originalDate;
            et_from.setText(formatedDate);
        } else {
            toDate = originalDate;
            et_to.setText(formatedDate);
        }

    }

    private void showDatePicker(Calendar calendar) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, datePicker, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        //max should be current date
        // datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        //30 days from current date
        Calendar lastmonthCal;

        if (isFromDate) {
            lastmonthCal = Calendar.getInstance();
            lastmonthCal.add(Calendar.DATE, -30);
        } else {
            lastmonthCal = calendarFrom;
        }

        Log.e("isFromDate", " :" + isFromDate);
        datePickerDialog.getDatePicker().setMinDate(lastmonthCal.getTimeInMillis());

        //show the dialog
        datePickerDialog.show();
    }

}
