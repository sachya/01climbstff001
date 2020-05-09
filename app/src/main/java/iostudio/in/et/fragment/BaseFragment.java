package iostudio.in.et.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.retrofit.api.ApiClient;


public class BaseFragment extends Fragment {
    public ApiClient apiClient = ApiClient.getInstance();

    public Activity activity;
    public Context context;
    private ProgressDialog progressDialog;
    public View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();
        context = getActivity();
        return rootView;
    }




    public View setFragmentView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState, int viewID) {
        rootView = inflater.inflate(viewID, container, false);
        return rootView;
    }

    public void setParentView(final View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init();
    }

    public void showToast(Activity activity, String message) {
        if (activity != null)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

// --Commented out by Inspection START (21/02/19, 12:46 AM):
//    public void handleError(Activity activity, String message) {
//        //Log error
//        //Remove loader
//        showToast(activity, message);
//    }
// --Commented out by Inspection STOP (21/02/19, 12:46 AM)


    public void showProgressDialogSimple(Boolean isShow) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        if (isShow)
            progressDialog.show();
        else
            progressDialog.dismiss();

    }

    public void hideProgressDialogSimple() {
        progressDialog.dismiss();

    }


    public void showProgressDialogSimple(Boolean isShow, SwipeRefreshLayout swipe_refresh_layout) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        if (isShow) {
            swipe_refresh_layout.setRefreshing(false);
            progressDialog.show();
        } else {
            swipe_refresh_layout.setRefreshing(true);
            progressDialog.dismiss();
        }
    }

    public void hideProgressDialogSimple(SwipeRefreshLayout swipe_refresh_layout) {
        progressDialog.dismiss();
        swipe_refresh_layout.setRefreshing(false);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isFixedSize,
                                           boolean isAutoMeasureEnabled,
                                           boolean isNestedScrollingEnabled) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isNestedScrollingEnabled) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerViewWithAdapter(RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager,
                                           RecyclerView.Adapter<RecyclerView.ViewHolder> recyclerAdapter,
                                           boolean isFixedSize,
                                           boolean isAutoMeasureEnabled,
                                           boolean isNestedScrollingEnabled,
                                           RecyclerView.ItemDecoration dividerItemDecoration) {
        recyclerView.setHasFixedSize(isFixedSize);
        layoutManager.setAutoMeasureEnabled(isAutoMeasureEnabled);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(isNestedScrollingEnabled);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(recyclerAdapter);

    }

// --Commented out by Inspection START (21/02/19, 12:46 AM):
//    public String buildSharedPreferenceKey(Call<Object> api, Map<String, String> inputMap) {
//        String preferenceKeyGenerated = "";
//        try {
//            JSONObject jsonObject = new JSONObject(inputMap);
//            String jsonString = jsonObject.toString();
//            preferenceKeyGenerated = api.request().url().toString() + "-" + jsonString;
//            //Log.d("preferenceKeyGenerated", preferenceKeyGenerated);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return preferenceKeyGenerated;
//    }
// --Commented out by Inspection STOP (21/02/19, 12:46 AM)


    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
    public void showAlertNoInternet() {
        Alert.alert(getContext(), "Alert", getString(R.string.no_internet), "", "Ok", null, null);
    }

    public boolean isEmptyString(String value) {
        return TextUtils.isEmpty(value);
    }
}
