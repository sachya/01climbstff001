package iostudio.in.et.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import iostudio.in.et.R;
import iostudio.in.et.activity.CreateNewContactActivity;
import iostudio.in.et.activity.DashboardActivity;
import iostudio.in.et.adapter.ContactRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.retrofit.response.Client;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewLead;
    FloatingActionButton fabAdd;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private ArrayList<ClientData> dataArrayList;
    AppCompatEditText et_search;
    AppCompatImageView iv_logo;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        context = getContext();
        dataArrayList = new ArrayList<>();

        et_search = view.findViewById(R.id.et_search);
        recyclerViewLead = view.findViewById(R.id.recyclerViewLead);
        fabAdd = view.findViewById(R.id.fabAdd);
        iv_logo = view.findViewById(R.id.iv_logo);


       // IOPref.getInstance().saveString(getContext(), IOPref.PreferenceKey.userID, "1");

        contactRecyclerAdapter = new ContactRecyclerAdapter(context, dataArrayList,false);
        setRecyclerViewWithAdapter(recyclerViewLead,
                new LinearLayoutManager(context),
                contactRecyclerAdapter,
                false);

        getLead();


        fabAnimation();
        fabAdd.setOnClickListener(this);
        iv_logo.setOnClickListener(this);


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


        return view;
    }

    private void getLead() {
        try {
            if (NetworkUtil.checkNetworkStatus(getContext())) {

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
                                showMessage(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, Client response) {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()) && ((DashboardActivity)getActivity()).getSelectedPosition() == 0)
                                showMessage(response.getMessage());

                            if (response.getData() != null &&
                                    response.getData().size() > 0) {
                                dataArrayList.clear();
                                dataArrayList.addAll(response.getData());
                                contactRecyclerAdapter.notifyDataSetChanged();
                            }else {
                               /* if (!TextUtils.isEmpty(response.getMessage())) {
                                    tv_no_data.setText(response.getMessage());
                                }*/
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
            Intent intent;

            switch (v.getId()) {
                case R.id.iv_logo:
                    Log.e("iv_logo ", " clicked");
                    ((DashboardActivity) getActivity()).openDrawer();
                    break;
                case R.id.fabAdd:
                    intent = new Intent();
                    intent.setClass(context, CreateNewContactActivity.class);
                    startActivity(intent);
                    break;
            }
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

}
