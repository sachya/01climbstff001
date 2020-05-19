package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.adapter.ExpenseRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.response.AccountData;
import iostudio.in.et.retrofit.response.AccountIncome;
import iostudio.in.et.retrofit.response.AccountInfo;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    RecyclerView recyclerViewExpense;
    ExpenseRecyclerAdapter expenseRecyclerAdapter;
    AppCompatImageView iv_clock;
    AppCompatTextView tv_income;
    AppCompatTextView tv_expense_title;
    AppCompatTextView tv_remember;
    AppCompatTextView tv_income_title;
    AppCompatTextView tv_expense;
    AppCompatTextView tv_in_month;
    AppCompatTextView tv_expense_month;
    AppCompatTextView tv_today_expense;
    AppCompatTextView tv_expense_title_today;
    AppCompatTextView tv_today_income;
    AppCompatTextView tv_income_title_today;
    AppCompatTextView tv_entries_header;
    FloatingActionButton fabAdd;
    private ArrayList<AccountData> dataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        context = this;
        setToolbar(true, true, getString(R.string.account), true);
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewExpense = findViewById(R.id.recyclerViewExpense);
        fabAdd = findViewById(R.id.fabAdd);
        iv_clock = findViewById(R.id.iv_clock);
        tv_income = findViewById(R.id.tv_income);
        tv_income_title = findViewById(R.id.tv_income_title);
        tv_expense = findViewById(R.id.tv_expense);
        tv_in_month = findViewById(R.id.tv_in_month);
        tv_expense_month = findViewById(R.id.tv_expense_month);
        tv_expense_title = findViewById(R.id.tv_expense_title);
        tv_remember = findViewById(R.id.tv_remember);
        tv_today_expense = findViewById(R.id.tv_today_expense);
        tv_expense_title_today = findViewById(R.id.tv_expense_title_today);
        tv_today_income = findViewById(R.id.tv_today_income);
        tv_income_title_today = findViewById(R.id.tv_income_title_today);
        tv_entries_header = findViewById(R.id.tv_entries_header);

    }

    @Override
    protected void initData() {
        dataArrayList = new ArrayList<>();
        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(context, dataArrayList);
        setRecyclerViewWithAdapter(recyclerViewExpense,
                new LinearLayoutManager(context),
                expenseRecyclerAdapter,
                false);
        fabAnimation();
        getAccountData();
    }

    @Override
    protected void bindEvent() {
        iv_clock.setOnClickListener(this);
        fabAdd.setOnClickListener(this);
    }

    private void fabAnimation() {
        fabAdd.hide();
        fabAdd.postDelayed(new Runnable() {
            @Override
            public void run() {
                fabAdd.show();
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;
            switch (v.getId()) {
                case R.id.iv_clock:
                    intent = new Intent();
                    intent.setClass(context, AccountEntriesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.fabAdd:
                    intent = new Intent();
                    intent.setClass(context, CreateNewExpenseActivity.class);
                    startActivityForResult(intent, Constant.INTENT_RESULT.CREATE_EXPENSE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_entries) {
            Intent intent = new Intent();
            intent.setClass(context, AccountEntriesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    @SuppressWarnings("unchecked")
    private void getAccountData() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                //"2017-02-14",
                /*Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                        meetingData.getMeeting_id(),
                        meetingData.getStatus(),
                        et_add_meeting_note.getText().toString().trim());
*/

                Call<AccountInfo> call = apiClient.getApi().getAccountHome(Utility.getUserID(context));

                call.enqueue(new AppRetrofitCallback<AccountInfo>(context) {
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
                    protected void onResponseAppObject(Call call, AccountInfo response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        AccountData data = response.getData();
                                        setData(data);
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

            } else {
                showAlertNoInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(AccountData data) {
        try {
            if (!TextUtils.isEmpty(data.getQuote())) {
                tv_remember.setText(data.getQuote());
                tv_remember.setVisibility(View.VISIBLE);
            } else {
                tv_remember.setVisibility(View.GONE);
            }


            AccountIncome accountIncome = data.getMonthincome();
            if (accountIncome != null) {
                if (!TextUtils.isEmpty(accountIncome.getLabel())) {
                    tv_income_title.setText(accountIncome.getLabel());
                }
                if (!TextUtils.isEmpty(accountIncome.getAmount())) {
                    tv_income.setText(accountIncome.getAmount()+" ₹");
                }if (!TextUtils.isEmpty(accountIncome.getMonth())) {
                    tv_in_month.setText(accountIncome.getMonth());
                }
            }
            AccountIncome accountExpense = data.getMonthexpense();
            if (accountExpense != null) {
                if (!TextUtils.isEmpty(accountExpense.getLabel())) {
                    tv_expense_title.setText(accountExpense.getLabel());
                }
                if (!TextUtils.isEmpty(accountExpense.getAmount())) {
                    tv_expense.setText(accountExpense.getAmount()+" ₹");
                }  if (!TextUtils.isEmpty(accountExpense.getMonth())) {
                    tv_expense_month.setText(accountExpense.getMonth());
                }
            }

            AccountIncome accountDayInc = data.getDayincome();
            if (accountDayInc != null) {
                if (!TextUtils.isEmpty(accountDayInc.getLabel())) {
                    tv_income_title_today.setText(accountDayInc.getLabel());
                }
                if (!TextUtils.isEmpty(accountDayInc.getAmount())) {
                    tv_today_income.setText(accountDayInc.getAmount()+" ₹");
                }

            }

            AccountIncome accountDayExp = data.getDayexpense();
            if (accountDayExp != null) {
                if (!TextUtils.isEmpty(accountDayExp.getLabel())) {
                    tv_expense_title_today.setText(accountDayExp.getLabel());
                }
                if (!TextUtils.isEmpty(accountDayExp.getAmount())) {
                    tv_today_expense.setText(accountDayExp.getAmount() +" ₹");
                }

            }

            if (data.getExpenses() != null &&
                    data.getExpenses().size() > 0) {
                tv_entries_header.setVisibility(View.VISIBLE);
                dataArrayList.clear();
                dataArrayList.addAll(data.getExpenses());
                expenseRecyclerAdapter.notifyDataSetChanged();
            } else {
                tv_entries_header.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constant.INTENT_RESULT.CREATE_EXPENSE){
                if (resultCode == RESULT_OK){
                    getAccountData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
