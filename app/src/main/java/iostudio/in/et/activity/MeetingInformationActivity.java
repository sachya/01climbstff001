package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.adapter.MeetingInformationRecyclerAdapter;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.retrofit.response.MeetingInfo;
import iostudio.in.et.retrofit.response.Note;
import iostudio.in.et.retrofit.response.Profile;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class MeetingInformationActivity extends BaseActivity implements View.OnClickListener {
    private Context context;
    RecyclerView recyclerViewMeetingNotes;
    MeetingInformationRecyclerAdapter meetingInformationRecyclerAdapter;
    private ArrayList<Note> dataArrayList;
    AppCompatTextView tv_name;
    AppCompatTextView tv_meeting_status;
    AppCompatTextView tv_meeting_time;
    AppCompatTextView tv_header_meeeting_note;
    AppCompatTextView tv_meeting_address;
    MaterialButton btn_delete;
    MaterialButton btn_update;
    MeetingData meetingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_information);
        context = this;
        setToolbar(true, true, getString(R.string.meeting_information), true);
        initBase();
    }

    @Override
    protected void initView() {
        tv_header_meeeting_note = findViewById(R.id.tv_header_meeeting_note);
        recyclerViewMeetingNotes = findViewById(R.id.recyclerViewMeetingNotes);
        tv_name = findViewById(R.id.tv_name);
        tv_meeting_status = findViewById(R.id.tv_meeting_status);
        tv_meeting_time = findViewById(R.id.tv_meeting_time);
        tv_meeting_address = findViewById(R.id.tv_meeting_address);
        btn_delete = findViewById(R.id.btn_delete);
        btn_update = findViewById(R.id.btn_update);
        btn_update.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        try {
            dataArrayList = new ArrayList<iostudio.in.et.retrofit.response.Note>();


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.containsKey(Constant.INTENT.MEETING_OBJECT)) {
                    meetingData = bundle.getParcelable(Constant.INTENT.MEETING_OBJECT);

                    getMeetingData(meetingData.getMeeting_id());
                }
            }

            meetingInformationRecyclerAdapter = new MeetingInformationRecyclerAdapter(context, dataArrayList);
            setRecyclerViewWithAdapter(recyclerViewMeetingNotes,
                    new LinearLayoutManager(context),
                    meetingInformationRecyclerAdapter,
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void getMeetingData(String meeting_id) {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                //"2017-02-14",
                /*Map<String, String> request = RetrofitRequest.meetingLocationRequest(Utility.getUserID(context),
                        meetingData.getMeeting_id(),
                        meetingData.getStatus(),
                        et_add_meeting_note.getText().toString().trim());
*/

                Call<MeetingInfo> call = apiClient.getApi().getMeetingInformation(meeting_id);

                call.enqueue(new AppRetrofitCallback<MeetingInfo>(context) {
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
                    protected void onResponseAppObject(Call call, MeetingInfo response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        MeetingData data = response.getData();
                                        if (!TextUtils.isEmpty(data.getName())) {
                                            tv_name.setText(data.getName());
                                            tv_name.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_name.setVisibility(View.GONE);
                                        }
                                        if (!TextUtils.isEmpty(data.getStatus_name())) {
                                            tv_meeting_status.setText(data.getStatus_name());
                                            tv_meeting_status.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_meeting_status.setVisibility(View.GONE);
                                        }
                                        if (!TextUtils.isEmpty(data.getStart_time())) {
                                            tv_meeting_time.setText(data.getStart_time());
                                            tv_meeting_time.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_meeting_time.setVisibility(View.GONE);
                                        }

                                        if (!TextUtils.isEmpty(data.getAddress())) {
                                            tv_meeting_address.setText(data.getAddress());
                                            tv_meeting_address.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_meeting_address.setVisibility(View.GONE);
                                        }

                                        if (data.getNotes() != null && data.getNotes().size() > 0) {

                                            dataArrayList.clear();
                                            dataArrayList.addAll(data.getNotes());
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

    @Override
    protected void bindEvent() {
        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_update:

                    break;
                case R.id.btn_delete:
                    try {
                        Alert.alert(context, "",getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                            @Override
                            public void run() {
                                deleteAccount(meetingData.getMeeting_id());
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
    private void deleteAccount(String id) {
        if (NetworkUtil.checkNetworkStatus(context)) {

            showProgressDialogSimple(true);


            Call<CommonResponse> call = apiClient.getApi().deleteMeeting(id);

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
                              //  showMessage(e.getMessage());
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
