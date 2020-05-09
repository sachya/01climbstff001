package iostudio.in.et.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import iostudio.in.et.R;
import iostudio.in.et.activity.CreateNewMeetingActivity1;
import iostudio.in.et.activity.DashboardActivity;
import iostudio.in.et.activity.PastMeetingActivity;
import iostudio.in.et.adapter.MeetingRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.Meeting;
import iostudio.in.et.retrofit.response.MeetingData;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingFragment extends BaseFragment implements View.OnClickListener {

    private MeetingRecyclerAdapter meetingRecyclerAdapter;
    AppCompatImageView iv_clock;
    AppCompatImageView iv_logo;
    AppCompatTextView tv_no_data;
    RecyclerView recyclerViewMeeting;
    FloatingActionButton fabAdd;
    private ArrayList<MeetingData> dataArrayList;


    public MeetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting, container, false);

        recyclerViewMeeting = view.findViewById(R.id.recyclerViewMeeting);
        fabAdd = view.findViewById(R.id.fabAdd);
        iv_clock = view.findViewById(R.id.iv_clock);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        iv_logo = view.findViewById(R.id.iv_logo);
        fabAnimation();
        dataArrayList = new ArrayList<>();

        meetingRecyclerAdapter = new MeetingRecyclerAdapter(getContext(), dataArrayList);
        setRecyclerViewWithAdapter(recyclerViewMeeting,
                new LinearLayoutManager(context),
                meetingRecyclerAdapter,
                false);

        iv_logo.setOnClickListener(this);
        iv_clock.setOnClickListener(this);
        fabAdd.setOnClickListener(this);


        getMeetingData();
        return view;
    }

    private void getMeetingData() {
        try {
            if (NetworkUtil.checkNetworkStatus(getContext())) {
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

            Map<String, String> request = RetrofitRequest.meetingRequest(Utility.getUserID(getContext()),
                    //"2017-02-14",
                    UtilDate.getCurrentDateOnly(),
                    //"2019-12-14");
                    "");

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
                            if (!TextUtils.isEmpty(response.getMessage()) && ((DashboardActivity) getActivity()).getSelectedPosition() == 2)
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
                                if (!TextUtils.isEmpty(response.getMessage())) {
                                    tv_no_data.setText(response.getMessage());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.iv_logo:
                    ((DashboardActivity) getActivity()).openDrawer();
                    break;
                case R.id.fabAdd:
                    intent.setClass(getContext(), CreateNewMeetingActivity1.class);
                    startActivity(intent);
                    break;
                case R.id.iv_clock:
                    intent.setClass(getContext(), PastMeetingActivity.class);
                    startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
