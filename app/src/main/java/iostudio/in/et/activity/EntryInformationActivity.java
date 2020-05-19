package iostudio.in.et.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;
import iostudio.in.et.R;
import iostudio.in.et.adapter.MeetingInformationRecyclerAdapter;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.AccountData;
import iostudio.in.et.retrofit.response.AccountInfo;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Note;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class EntryInformationActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewMeetingNotes;
    MeetingInformationRecyclerAdapter meetingInformationRecyclerAdapter;
    private ArrayList<Note> dataArrayList;
    AppCompatEditText et_add_meeting_note;
    MaterialButton btn_update;
    MaterialButton btn_delete;
    MaterialButton btn_add_note;
    AppCompatTextView tv_header_meeeting_note;
    AppCompatTextView tv_entry_type;
    AppCompatTextView tv_entry_name;
    AppCompatTextView tv_entry_cost;
    AppCompatImageView iv_attachment;

    private AccountData accountData;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_information);
        context = this;
        setToolbar(true, true, getString(R.string.account_entry_information), true);
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewMeetingNotes = findViewById(R.id.recyclerViewMeetingNotes);
        tv_header_meeeting_note = findViewById(R.id.tv_header_meeeting_note);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_add_note = findViewById(R.id.btn_add_note);
        tv_entry_type = findViewById(R.id.tv_entry_type);
        tv_entry_name = findViewById(R.id.tv_entry_name);
        tv_entry_cost = findViewById(R.id.tv_entry_cost);
        iv_attachment = findViewById(R.id.iv_attachment);
        et_add_meeting_note = findViewById(R.id.et_add_meeting_note);

    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.ACCOUNT_OBJECT)) {
                accountData = bundle.getParcelable(Constant.INTENT.ACCOUNT_OBJECT);
            }
            if (bundle.containsKey(Constant.INTENT.ID)) {
                ID = bundle.getString(Constant.INTENT.ID);
            }
        }
        dataArrayList = new ArrayList<>();
        meetingInformationRecyclerAdapter = new MeetingInformationRecyclerAdapter(context, dataArrayList);
        setRecyclerViewWithAdapter(recyclerViewMeetingNotes,
                new LinearLayoutManager(context),
                meetingInformationRecyclerAdapter,
                false);

        getData();
    }

    private void getData() {
        if (accountData != null &&
                !TextUtils.isEmpty(accountData.getAccount_id())) {
            getAccountData(accountData.getAccount_id());
        } else if (!TextUtils.isEmpty(ID)) {
            getAccountData(ID);
        } else {
            Log.e("accountData", " null");
        }
    }

    @Override
    protected void bindEvent() {
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_add_note.setOnClickListener(this);
        iv_attachment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;

            switch (v.getId()) {
                case R.id.btn_add_note:
                    if (et_add_meeting_note.getText().toString().trim().length() > 0) {
                        addNote();
                    } else {
                        showMessage(getString(R.string.error_pls_add_note));
                    }
                    break;
                case R.id.btn_delete:
                    try {
                        Alert.alert(context, "", getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                            @Override
                            public void run() {
                                deleteAccount(accountData.getAccount_id());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_update:
                    intent = new Intent();
                    intent.putExtra(Constant.INTENT.ACCOUNT_OBJECT, accountData);
                    intent.setClass(context, CreateNewExpenseActivity.class);
                    startActivityForResult(intent, Constant.INTENT_RESULT.CREATE_EXPENSE);
                    break;

                case R.id.iv_attachment:

                    String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                    String attachment_image = "https://climbstaff.com/uploads/company/"+companyid+"/"+Utility.getUserID(context)+"/accounts/"+accountData.getAccount_id()+"/image.jpg";
                    getMultipleimages(attachment_image);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void addNote() {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.addAccountNote(
                    accountData.getAccount_id(),
                    et_add_meeting_note.getText().toString().trim(),
                    UtilDate.getCurrentDate());

            Call<CommonResponse> call = apiClient.getApi().accountAddNote(request);

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
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, CommonResponse response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());

                            if (response.getCode().equalsIgnoreCase("1")) {
                                et_add_meeting_note.setText("");
                                getData();
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

    @SuppressWarnings("unchecked")
    private void deleteAccount(String client_id) {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);

            //"2017-02-14",
            Map<String, String> request = RetrofitRequest.deleteAccountRequest(Utility.getUserID(context), client_id);

            Call<CommonResponse> call = apiClient.getApi().accountDelete(request);

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
                                setResult(RESULT_OK);
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
                    showMessage(getString(R.string.something_went_wrong));
                }
            });

        } else {
            showAlertNoInternet();
        }
    }


    @SuppressWarnings("unchecked")
    private void getAccountData(String meeting_id) {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                //"2017-02-14",
                /*Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                        meetingData.getMeeting_id(),
                        meetingData.getStatus(),
                        et_add_meeting_note.getText().toString().trim());
*/

                Call<AccountInfo> call = apiClient.getApi().getAccountInformation(meeting_id);

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
                                    showMessage(e.getMessage());
                                    showMessage(getString(R.string.something_went_wrong));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                                        accountData = response.getData();



                                        if (accountData.getImage().equalsIgnoreCase("1"))
                                        {
                                            String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                                            String attachment_image = "https://climbstaff.com/uploads/company/"+companyid+"/"+Utility.getUserID(context)+"/accounts/"+accountData.getAccount_id()+"/image.jpg";
                                            Picasso.get().invalidate(attachment_image);
                                            Picasso.get().load(attachment_image)
                                                    .placeholder(R.drawable.ic_add_box)
                                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(iv_attachment);



                                        }
                                        else if (accountData.getImage().equalsIgnoreCase("0"))
                                        {
                                            iv_attachment.setVisibility(View.GONE);
                                            /*Picasso.get().load(R.drawable.ic_add_box)
                                                    .placeholder(R.drawable.ic_add_box)
                                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(iv_attachment);

                                            iv_attachment.setEnabled(false);*/



                                        }



                                        if (!TextUtils.isEmpty(accountData.getName())) {
                                            tv_entry_name.setText(accountData.getName());
                                            tv_entry_name.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_entry_name.setVisibility(View.GONE);
                                        }
                                        if (!TextUtils.isEmpty(accountData.getType_name())) {
                                            tv_entry_type.setText(accountData.getType_name());
                                            tv_entry_type.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_entry_type.setVisibility(View.GONE);
                                        }
                                        if (!TextUtils.isEmpty(accountData.getAmount())) {
                                            tv_entry_cost.setText(accountData.getAmount());
                                            tv_entry_cost.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_entry_cost.setVisibility(View.GONE);
                                        }

                                        if (accountData.getNotes() != null && accountData.getNotes().size() > 0) {

                                            dataArrayList.clear();
                                            dataArrayList.addAll(accountData.getNotes());
                                            meetingInformationRecyclerAdapter.notifyDataSetChanged();

                                            recyclerViewMeetingNotes.setVisibility(View.VISIBLE);
                                            tv_header_meeeting_note.setVisibility(View.VISIBLE);
                                        } else {
                                            recyclerViewMeetingNotes.setVisibility(View.GONE);
                                            tv_header_meeeting_note.setVisibility(View.GONE);
                                        }
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
            showMessage(getString(R.string.something_went_wrong));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constant.INTENT_RESULT.CREATE_EXPENSE){
                if (resultCode == RESULT_OK){
                    setResult(RESULT_OK);
                    getData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    int NUM_PAGES_RETURN_imagedisplay = 0;
    int currentPage_RETURN_imagedisplay = 0;
    public void getMultipleimages(String images)
    {

        final Dialog dialog1 = new Dialog(context,R.style.DialogCustomTheme);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setContentView(R.layout.dialog_display_images);

        int width = getWindowManager().getDefaultDisplay()
                .getWidth();
        dialog1.getWindow().setLayout(width-20, ActionBar.LayoutParams.MATCH_PARENT);


        AppCompatImageView iv_cross_provider = (AppCompatImageView) dialog1.findViewById(R.id.iv_cross_provider);
        AppCompatImageView iv_fullimage = (AppCompatImageView) dialog1.findViewById(R.id.iv_fullimage);

        Picasso.get().load(images)
                .placeholder(R.drawable.ic_add_box)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(iv_fullimage);





        iv_cross_provider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });




        dialog1.show();

    }
}
