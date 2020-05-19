package iostudio.in.et.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatEditText;
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
import iostudio.in.et.adapter.ContactRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Client;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class CreateNewMeetingActivity1 extends BaseActivity implements View.OnClickListener {

    private Context context;
    MaterialButton btn_add_to_meeting;

    private ContactRecyclerAdapter contactRecyclerAdapter;
    private ArrayList<ClientData> dataArrayList;
    AppCompatEditText et_search;
    AppCompatTextView tv_date;
    RecyclerView recyclerViewLead;

    private String selectedDate = "";
    Calendar calendar;
    private ClientData clientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_meeting1);
        setToolbar(true, true, getString(R.string.add_contact_to_meeting), true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        btn_add_to_meeting = findViewById(R.id.btn_add_to_meeting);
        tv_date = findViewById(R.id.tv_date);


        dataArrayList = new ArrayList<>();

        et_search = findViewById(R.id.et_search);
        recyclerViewLead = findViewById(R.id.recyclerViewLead);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.CLIENT_OBJECT)) {
                clientData = bundle.getParcelable(Constant.INTENT.CLIENT_OBJECT);
            }
        }
        calendar = Calendar.getInstance();
        updateEditText();

        contactRecyclerAdapter = new ContactRecyclerAdapter(context, dataArrayList, true);
        setRecyclerViewWithAdapter(recyclerViewLead,
                new LinearLayoutManager(context),
                contactRecyclerAdapter,
                false);

        getLead();


    }

    private void getLead() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                getLeadRequest();

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getLeadRequest() {
        try {
            showProgressDialogSimple(true);

            Call<Client> call = apiClient.getApi().getLeadList(Utility.getUserID(context));

            call.enqueue(new AppRetrofitCallback<Client>(context) {
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
                protected void onResponseAppObject(Call call, Client response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());

                            if (response.getData() != null &&
                                    response.getData().size() > 0) {
                                dataArrayList.clear();
                                dataArrayList.addAll(response.getData());

                                try {
                                    for (ClientData data : dataArrayList) {
                                        if (data.getClient_id().equalsIgnoreCase(clientData.getClient_id())) {
                                            data.setChecked(true);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showMessage(getString(R.string.something_went_wrong));
                                }
                                contactRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    protected void bindEvent() {
        tv_date.setOnClickListener(this);
        btn_add_to_meeting.setOnClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    contactRecyclerAdapter.getFilter().filter(s.toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        //datePickerDialog.getDatePicker().setMinDate(fromDateMilliSec);

        //show the dialog
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateEditText() {
        try {
            String myFormat = "dd MMM";
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
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_add_to_meeting:
                    addMeetingRequest();
                    break;
                case R.id.tv_date:
                    showDatePicker();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void addMeetingRequest() {
        if (NetworkUtil.checkNetworkStatus(context)) {

            ArrayList<String> ids = new ArrayList<>();
            for (ClientData data : dataArrayList) {
                if (data.isChecked()) {
                    ids.add(data.getClient_id());
                }
            }
            String clientIDCSV = TextUtils.join(",", ids);

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.meetingAddRequest(Utility.getUserID(context),
                    clientIDCSV,
                    selectedDate,
                    UtilDate.getCurrentDate());

            Call<CommonResponse> call = apiClient.getApi().meetingAdd(request);

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
