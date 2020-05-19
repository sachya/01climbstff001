package iostudio.in.et.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import iostudio.in.et.BuildConfig;
import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.AccountData;
import iostudio.in.et.retrofit.response.AccountEntryData;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.EntryData;
import iostudio.in.et.retrofit.response.Type;
import iostudio.in.et.retrofit.response.TypeData;
import iostudio.in.et.utility.Constant;
import iostudio.in.et.utility.IImageCompressTaskListener;
import iostudio.in.et.utility.ImageCompressTask;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class CreateNewExpenseActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    MaterialButton btnSubmit;
    AppCompatTextView tv_title_note;
    AppCompatEditText et_remark;
    AppCompatEditText et_expense_cost;
    AppCompatEditText et_expense_name;
    AppCompatSpinner spinner_leave;
    AppCompatImageView iv_attachment;
    AppCompatImageView iv_edit;
    MaterialButton iv_delete;
    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private ArrayList<TypeData> typeDataArrayList;
    private ArrayList<String> typeTitleArrayList;
    private String selectedTypeID = "";
    private ArrayAdapter typeAdapter;

    private AccountData accountData;
    LinearLayout ll_create_expense;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 33;
    String imageFilePath;
    File photoFile;
    ImageCompressTask imageCompressTask;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_expense);
        setToolbar(true, true, getString(R.string.create_new_entry), true);
        context = this;
        initBase();
    }

    @Override
    protected void initView() {
        btnSubmit = findViewById(R.id.btnSubmit);
        et_remark = findViewById(R.id.et_remark);
        et_expense_cost = findViewById(R.id.et_expense_cost);
        et_expense_name = findViewById(R.id.et_expense);
        spinner_leave = findViewById(R.id.spinner_leave);
        ll_create_expense = findViewById(R.id.ll_create_expense);
        tv_title_note = findViewById(R.id.tv_title_note);
        iv_attachment = findViewById(R.id.iv_attachment);
        iv_delete = findViewById(R.id.iv_delete);
        iv_edit = findViewById(R.id.iv_edit);

    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(Constant.INTENT.ACCOUNT_OBJECT)) {
                accountData = bundle.getParcelable(Constant.INTENT.ACCOUNT_OBJECT);
                btnSubmit.setText(getString(R.string.update));
                ll_create_expense.setVisibility(View.GONE);
                tv_title_note.setVisibility(View.GONE);
                et_remark.setVisibility(View.GONE);

                if (accountData!=null){
                    setToolbar(true, true, getString(R.string.update_entry), true);

                    if (!TextUtils.isEmpty(accountData.getName())){
                        et_expense_name.setText(accountData.getName());
                    }  if (!TextUtils.isEmpty(accountData.getAmount())){
                        et_expense_cost.setText(accountData.getAmount());
                    }
                   // https://climbstaff.com/uploads/company/1/12/accounts/56/image.jpg
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

                        iv_attachment.setEnabled(false);
                        iv_edit.setVisibility(View.VISIBLE);
                        iv_delete.setVisibility(View.VISIBLE);
                    }
                    else if (accountData.getImage().equalsIgnoreCase("0"))
                    {
                        Picasso.get().load(R.drawable.ic_add_box)
                                .placeholder(R.drawable.ic_add_box)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .into(iv_attachment);

                        iv_attachment.setEnabled(false);
                        iv_edit.setVisibility(View.VISIBLE);
                        iv_delete.setVisibility(View.GONE);

                    }

                }


            }
        }

        getTypeRequest();
        //Creating the ArrayAdapter instance having the country list
        typeTitleArrayList = new ArrayList<>();
        typeDataArrayList = new ArrayList<>();
        typeAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, typeTitleArrayList);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_leave.setAdapter(typeAdapter);

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
        btnSubmit.setOnClickListener(this);
        iv_attachment.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        iv_delete.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnSubmit:
                    if (hasValidData()) {

                        if (accountData == null) {
                           /* if (photoFile != null) {
                                createNewAccountEntryRequest();
                            } else {*/
                                Map<String, String> request = RetrofitRequest.createAccountRequest(
                                        Utility.getUserID(context), selectedTypeID,
                                        et_expense_cost.getText().toString().trim(),
                                        et_expense_name.getText().toString().trim(),
                                        UtilDate.getCurrentDate(),
                                        et_remark.getText().toString().trim());
                                createAccountEntry(request);
                           // }
                        } else {
                            Map<String, String> request = RetrofitRequest.updateAccountRequest(
                                    Utility.getUserID(context),
                                    accountData.getAccount_id(),
                                    et_expense_cost.getText().toString().trim(),
                                    et_expense_name.getText().toString().trim(),
                                    UtilDate.getCurrentDate());
                            updateAccountEntry(request);
                        }
                    }
                    break;
                case R.id.iv_attachment:
                    if (checkPermissions()) {
                        openCameraIntent();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    }
                    break;

                case R.id.iv_edit:
                    if (checkPermissions()) {
                        openCameraIntent();
                    } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                }

                    break;
                case R.id.iv_delete:


                    try {
                        if (NetworkUtil.checkNetworkStatus(context))
                        {

                            Alert.alert(context, "",getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                                @Override
                                public void run() {
                                    String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                                    Map<String, String> request = RetrofitRequest.deleteaccountprofileImageRequest(Utility.getUserID(context),companyid,accountData.getAccount_id());
                                    deleteImage(request);

                                    Picasso.get().load(R.drawable.ic_add_box)
                                            .placeholder(R.drawable.ic_add_box)
                                            .into(iv_attachment);
                                    iv_delete.setVisibility(View.GONE);
                                }
                            });


                        }
                        else
                        {
                            showAlertNoInternet();
                        }

                    }
                    catch (Exception e)
                    {
                        Log.e("error",e.toString());
                    }



                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewAccountEntryRequest(String _id) {
        String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("UploadForm[file]", photoFile.getName(), fileReqBody);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), Utility.getUserID(context));
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), companyid);
        RequestBody entry_id = RequestBody.create(MediaType.parse("text/plain"), _id);
      //  RequestBody typeId = RequestBody.create(MediaType.parse("text/plain"), selectedTypeID);
      //  RequestBody cost = RequestBody.create(MediaType.parse("text/plain"), et_expense_cost.getText().toString().trim());
     //   RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_expense_name.getText().toString().trim());
     //   RequestBody date = RequestBody.create(MediaType.parse("text/plain"), UtilDate.getCurrentDate());
     //   RequestBody remark = RequestBody.create(MediaType.parse("text/plain"), et_remark.getText().toString().trim());

        try {
            showProgressDialogSimple(true);

           // Call<CommonResponse> call = apiClient.getApi().requestAccountEntryAddWithAttachment(part, userId, typeId, cost, name, date, remark);
            Call<CommonResponse> call = apiClient_image.getApi().requestAcoountImage(part, userId, company_id, entry_id);

            call.enqueue(new AppRetrofitCallback<CommonResponse>(this) {
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
                              //  showMessage(e.getMessage());
                                showMessage(getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                       // e.printStackTrace();
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
                                setResult(RESULT_OK);
                                finish();
                        }else if (response.getCode().equalsIgnoreCase("200"))
                            {
                                setResult(RESULT_OK);
                                finish();
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

    public boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permission2State = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return ((permissionState == PackageManager.PERMISSION_GRANTED) && (permission2State == PackageManager.PERMISSION_GRANTED));
    }



    @SuppressWarnings("unchecked")
    private void createAccountEntry(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient.getApi().requestAccountEntryAdd(request);

            call.enqueue(new AppRetrofitCallback<AccountEntryData>(this) {
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
                              //  showMessage(e.getMessage());
                                showMessage(getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                       // e.printStackTrace();
                        showMessage(getString(R.string.something_went_wrong));
                    }
                }

                @Override
                protected void onResponseAppObject(Call call, AccountEntryData response)
                {
                    if (response != null) {
                        try {
                            if (!TextUtils.isEmpty(response.getMessage()))
                                showMessage(response.getMessage());
                            if (response.getCode().equalsIgnoreCase("1")) {
                                if (photoFile != null) {
                                    EntryData entryData = response.getData();
                                    createNewAccountEntryRequest(entryData.getId());
                                }
                                else
                                {
                                    setResult(RESULT_OK);
                                    finish();
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
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(getString(R.string.something_went_wrong));
        }
    }

    @SuppressWarnings("unchecked")
    private void updateAccountEntry(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient.getApi().requestAccountEntryUpdate(request);

            call.enqueue(new AppRetrofitCallback<CommonResponse>(this) {
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
                             //   showMessage(e.getMessage());
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
                                if (photoFile!=null)
                                {
                                   createNewAccountEntryRequest(accountData.getAccount_id());
                                }
                                else
                                {
                                    setResult(RESULT_OK);
                                    finish();

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
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(getString(R.string.something_went_wrong));
        }
    }


    private boolean hasValidData() {
        String name = et_expense_name.getText().toString().trim();
        String cost = et_expense_cost.getText().toString().trim();
        String note = et_remark.getText().toString().trim();

        if (accountData==null && TextUtils.isEmpty(selectedTypeID)) {
            showMessage(getString(R.string.error_select_entry_type));
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            showMessage(getString(R.string.error_entry_name));
            return false;
        }
        if (TextUtils.isEmpty(cost)) {
            showMessage(getString(R.string.error_entry_cost));
            return false;
        }
        if (accountData==null && TextUtils.isEmpty(note)) {
            showMessage(getString(R.string.error_entry_note));
            return false;
        }
        return true;
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCameraIntent();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            //Picasso.get().load(photoFile).into(iv_attachment);
            System.out.println("### photoFile length >>> "+photoFile.length());
            imageCompressTask = new ImageCompressTask(this, photoFile.getAbsolutePath(), iImageCompressTaskListener);

            mExecutorService.execute(imageCompressTask);
        }
    }
    private IImageCompressTaskListener iImageCompressTaskListener = new IImageCompressTaskListener() {
        @Override
        public void onComplete(List<File> compressed) {
            //photo compressed. Yay!

            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)

            File file = compressed.get(0);

            Log.d("ImageCompressor", "New photo size ==> " + file.length()); //log new file size.
            Picasso.get().load(file).into(iv_attachment);
            photoFile = file;
            System.out.println("### photoFile length 22 >>> "+photoFile.length());
        }

        @Override
        public void onError(Throwable error) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdown();
    }


    @SuppressWarnings("unchecked")
    private void deleteImage(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient_image.getApi().deleteAccountEntryImage(request);

            call.enqueue(new AppRetrofitCallback<CommonResponse>(this) {
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
                                //  showMessage(e.getMessage());
                                showMessage(getString(R.string.something_went_wrong));
                            }
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
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
                                setResult(RESULT_OK);
                                finish();
                            }
                            else if (response.getCode().equalsIgnoreCase("200"))
                            {
                                setResult(RESULT_OK);
                                finish();
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
}
