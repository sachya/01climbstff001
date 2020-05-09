package iostudio.in.et.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
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
import iostudio.in.et.adapter.ExpenseRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Account;
import iostudio.in.et.retrofit.response.AccountData;
import iostudio.in.et.retrofit.response.Type;
import iostudio.in.et.retrofit.response.TypeData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class AccountEntriesActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewExpense;
    ExpenseRecyclerAdapter expenseRecyclerAdapter;

    AppCompatSpinner spinner_leave;

    String fromDate;
    String toDate;
    AppCompatTextView tv_no_data;
    AppCompatTextView tv_to_date;
    AppCompatTextView tv_from_date;
    MaterialButton btnShow;


    private ArrayList<TypeData> typeDataArrayList;
    private ArrayList<String> typeTitleArrayList;
    private String selectedTypeID = "";
    private ArrayAdapter typeAdapter;
    private Calendar calendar;
    private long fromDateMilliSec = 0;
    private ArrayList<AccountData> dataArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_entries);
        context = this;
        setToolbar(true, true, getString(R.string.account_entries), true);
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewExpense = findViewById(R.id.recyclerViewExpense);
        spinner_leave = findViewById(R.id.spinner_leave);

        tv_no_data = findViewById(R.id.tv_no_data);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        btnShow = findViewById(R.id.btnShow);

    }

    @Override
    protected void initData() {

        calendar = Calendar.getInstance();
        dataArrayList = new ArrayList<>();

        updateEditText(0);
        updateEditText(1);


        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(context, dataArrayList);
        setRecyclerViewWithAdapter(recyclerViewExpense,
                new LinearLayoutManager(context),
                expenseRecyclerAdapter,
                false);

        //Creating the ArrayAdapter instance having the country list
        typeTitleArrayList = new ArrayList<>();
        typeDataArrayList = new ArrayList<>();
        typeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, typeTitleArrayList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_leave.setAdapter(typeAdapter);

        getTypeRequest();
    }

    @Override
    protected void bindEvent() {
        btnShow.setOnClickListener(this);
        tv_from_date.setOnClickListener(this);
        tv_to_date.setOnClickListener(this);
        spinner_leave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeID = typeDataArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getTypeRequest() {
        try {
            showProgressDialogSimple(true);

            Call<Type> call = apiClient.getApi().getaccountTypeData(Utility.getUserID(context));

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

                                //setdata(clientData);

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

    private void showDatePicker(final int type) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateEditText(type);

                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


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
            Map<String, String> request = RetrofitRequest.accountRequest(Utility.getUserID(context),
                    selectedTypeID,
                    fromDate,
                    toDate);

            Call<Account> call = apiClient.getApi().getAccountData(request);

            call.enqueue(new AppRetrofitCallback<Account>(context) {
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
                protected void onResponseAppObject(Call call, Account response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());


                            if (response.getData() != null && response.getData().size() > 0) {
                                dataArrayList.clear();
                                dataArrayList.addAll(response.getData());
                                expenseRecyclerAdapter.notifyDataSetChanged();
                                recyclerViewExpense.setVisibility(View.VISIBLE);
                                // tv_no_data.setVisibility(View.GONE);
                            } else {
                                recyclerViewExpense.setVisibility(View.GONE);
                                //   tv_no_data.setVisibility(View.VISIBLE);
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


    private void updateEditText(int type) {

        String myFormat = "dd MMM";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String formatedDate = sdf.format(calendar.getTime());


        String attendaceFormat = "yyyy-MM-dd";
        SimpleDateFormat attendanceSdf = new SimpleDateFormat(attendaceFormat, Locale.US);
        String originalDate = attendanceSdf.format(calendar.getTime());

        if (type == 0) {
            fromDateMilliSec = calendar.getTimeInMillis();
            tv_from_date.setText(formatedDate);
            fromDate = originalDate;
        } else {
            toDate = originalDate;
            tv_to_date.setText(formatedDate);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constant.INTENT_RESULT.ACCOUNT_INFO){
                if (resultCode == RESULT_OK){
                    getMeetingData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
