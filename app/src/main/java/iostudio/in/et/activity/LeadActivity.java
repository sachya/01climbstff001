package iostudio.in.et.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import iostudio.in.et.R;
import iostudio.in.et.adapter.ContactRecyclerAdapter;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.response.Client;
import iostudio.in.et.retrofit.response.ClientData;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.Utility;
import retrofit2.Call;
import retrofit2.Response;

public class LeadActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    RecyclerView recyclerViewLead;
    FloatingActionButton fabAdd;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private ArrayList<ClientData> dataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        setToolbar(true, true, getString(R.string.contact_lead), true);
        context = this;
        dataArrayList = new ArrayList<>();
        initBase();
    }

    @Override
    protected void initView() {
        recyclerViewLead = findViewById(R.id.recyclerViewLead);
        fabAdd = findViewById(R.id.fabAdd);

        fabAnimation();

    }

    @Override
    protected void initData() {

        contactRecyclerAdapter = new ContactRecyclerAdapter(context, dataArrayList,false);
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

            call.enqueue(new AppRetrofitCallback<Client>(this) {
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
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());

                            if (response.getData() != null &&
                                    response.getData().size() > 0) {
                                dataArrayList.clear();
                                dataArrayList.addAll(response.getData());
                                contactRecyclerAdapter.notifyDataSetChanged();
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
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Intent intent;

            switch (v.getId()) {
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
