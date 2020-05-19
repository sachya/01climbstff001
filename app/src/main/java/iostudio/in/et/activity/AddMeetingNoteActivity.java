package iostudio.in.et.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.adapter.MeetingInformationRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.retrofit.response.MeetingInfo;
import iostudio.in.et.retrofit.response.Note;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class AddMeetingNoteActivity extends BaseActivity implements View.OnClickListener {

    private Context context;

    RecyclerView recyclerViewMeetingNotes;
    MeetingInformationRecyclerAdapter meetingInformationRecyclerAdapter;
    private ArrayList<Note> dataArrayList;
    private MeetingData meetingData;
    MaterialButton btn_add_note;
    AppCompatEditText et_add_meeting_note;
    AppCompatTextView tv_meeting_time;
    AppCompatTextView tv_meeting_status;
    AppCompatTextView tv_meeting_with;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting_note);
        context = this;
        setToolbar(true, true, getString(R.string.add_note), true);
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewMeetingNotes = findViewById(R.id.recyclerViewMeetingNotes);
        btn_add_note = findViewById(R.id.btn_add_note);
        et_add_meeting_note = findViewById(R.id.et_add_meeting_note);
        tv_meeting_time = findViewById(R.id.tv_meeting_time);
        tv_meeting_status = findViewById(R.id.tv_meeting_status);
        tv_meeting_with = findViewById(R.id.tv_meeting_with);

    }

    @Override
    protected void initData() {

        try {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                if (bundle.containsKey(Constant.INTENT.MEETING_OBJECT)) {
                    meetingData = bundle.getParcelable(Constant.INTENT.MEETING_OBJECT);

                  //  setData(meetingData);
                    getMeetingData(meetingData.getMeeting_id());
                }
            }
            dataArrayList = new ArrayList<>();
            meetingInformationRecyclerAdapter = new MeetingInformationRecyclerAdapter(context, dataArrayList);
            setRecyclerViewWithAdapter(recyclerViewMeetingNotes,
                    new LinearLayoutManager(context),
                    meetingInformationRecyclerAdapter,
                    false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(MeetingData meetingData) {
        try {
            if (meetingData != null) {
                Log.e("getName",": "+meetingData.getName());
                if (!isEmptyString(meetingData.getName())) {
                    tv_meeting_with.setText(meetingData.getName());
                    tv_meeting_with.setVisibility(View.VISIBLE);
                } else {
                    tv_meeting_with.setVisibility(View.GONE);
                }
                if (!isEmptyString(meetingData.getStatus_name())) {
                    tv_meeting_status.setText(meetingData.getStatus_name());
                    tv_meeting_status.setVisibility(View.VISIBLE);
                } else {
                    tv_meeting_status.setVisibility(View.GONE);
                }
                if (!isEmptyString(meetingData.getMeeting_date())) {
                    tv_meeting_time.setText(meetingData.getMeeting_date());
                    tv_meeting_time.setVisibility(View.VISIBLE);
                } else {
                    tv_meeting_time.setVisibility(View.GONE);
                }
                if (!isEmptyString(meetingData.getStart_time())) {
                    tv_meeting_time.setText(meetingData.getStart_time());
                    tv_meeting_time.setVisibility(View.VISIBLE);
                } else {
                    tv_meeting_time.setVisibility(View.GONE);
                }

                if (meetingData.getNotes() != null &&
                        meetingData.getNotes().size() > 0) {
                    dataArrayList.clear();
                    dataArrayList.addAll(meetingData.getNotes());
                    meetingInformationRecyclerAdapter.notifyDataSetChanged();
                    recyclerViewMeetingNotes.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewMeetingNotes.setVisibility(View.GONE);
                }
            }
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
                                    showMessage(getString(R.string.something_went_wrong));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(getString(R.string.something_went_wrong));
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
            e.printStackTrace();showMessage(getString(R.string.something_went_wrong));

        }
    }




    @Override
    protected void bindEvent() {
        btn_add_note.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btn_add_note:
                    if (et_add_meeting_note.getText().toString().trim().length() > 0) {
                        addNote();
                    } else {
                        showMessage(getString(R.string.error_pls_add_note));
                    }
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
            Map<String, String> request = RetrofitRequest.addMeetingNote(Utility.getUserID(context),
                    meetingData.getMeeting_id(),
                    et_add_meeting_note.getText().toString().trim());

            Call<CommonResponse> call = apiClient.getApi().addNote(request);

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
