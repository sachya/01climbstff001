package iostudio.in.et.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.core.content.FileProvider;
import iostudio.in.et.BuildConfig;
import iostudio.in.et.R;
import iostudio.in.et.alert.Alert;
import iostudio.in.et.pref.IOPref;
import iostudio.in.et.retrofit.api.AppRetrofitCallback;
import iostudio.in.et.retrofit.request.RetrofitRequest;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Profile;
import iostudio.in.et.retrofit.response.ProfileData;
import iostudio.in.et.utility.CircleTransform;
import iostudio.in.et.utility.IImageCompressTaskListener;
import iostudio.in.et.utility.ImageCompressTask;
import iostudio.in.et.utility.ImageSelection;
import iostudio.in.et.utility.NetworkUtil;
import iostudio.in.et.utility.SocketConnectionListner;
import iostudio.in.et.utility.UtilDate;
import iostudio.in.et.utility.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    AppCompatImageView iv_logo;
    AppCompatTextView tv_name;
    AppCompatTextView tv_address;
    AppCompatTextView tv_city;
    AppCompatTextView tv_designation;
    AppCompatTextView tv_company;
    MaterialButton btn_delete;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 33;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    String imageFilePath;
    ImageCompressTask imageCompressTask;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        setToolbar(true, true, getString(R.string.profile), true);
        initBase();

    }

    @Override
    protected void initView() {
        tv_name = findViewById(R.id.tv_name);
        tv_designation = findViewById(R.id.tv_designation);
        tv_address = findViewById(R.id.tv_address);
        tv_city = findViewById(R.id.tv_city);
        tv_company = findViewById(R.id.tv_company);
        iv_logo = findViewById(R.id.iv_logo);
        btn_delete = findViewById(R.id.btn_delete);

    }

    @Override
    protected void initData() {

        getProfile();
    }

    @SuppressWarnings("unchecked")
    String imagestatus;
    private void getProfile() {
        try {
            if (NetworkUtil.checkNetworkStatus(context)) {

                showProgressDialogSimple(true);

                Call<Profile> call = apiClient.getApi().getProfileInformation(Utility.getUserID(context));

                call.enqueue(new AppRetrofitCallback<Profile>(context) {
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
                    protected void onResponseAppObject(Call call, Profile response) {
                        if (response != null) {
                            try {
                                if (!TextUtils.isEmpty(response.getMessage()))
                                    showMessage(response.getMessage());

                                if (response.getCode().equalsIgnoreCase("1")) {
                                    if (response.getData() != null) {
                                        ProfileData data = response.getData();

                                        String name = data.getName();


                                        if (!TextUtils.isEmpty(name)) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.name, name);
                                            tv_name.setText(name);
                                            tv_name.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_name.setVisibility(View.GONE);
                                        }

                                        String designation = data.getDesignation();
                                        if (!TextUtils.isEmpty(designation)) {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.designation, designation);

                                            tv_designation.setText(designation);
                                            tv_designation.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_designation.setVisibility(View.GONE);
                                        }

                                        String address = data.getAddress();
                                        if (!TextUtils.isEmpty(address)) {
                                            tv_address.setText(address);
                                            tv_address.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_address.setVisibility(View.GONE);
                                        }

                                        String city = data.getCity();
                                        if (!TextUtils.isEmpty(city)) {
                                            tv_city.setText(city);
                                            tv_city.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_city.setVisibility(View.GONE);
                                        }
                                        String company = data.getCompany();
                                        if (!TextUtils.isEmpty(company)) {
                                            tv_company.setText(company);
                                            tv_company.setVisibility(View.VISIBLE);
                                        } else {
                                            tv_company.setVisibility(View.GONE);
                                        }
                                        imagestatus = data.getImage();

                                        if (data.getImage()!=null&&!TextUtils.isEmpty(data.getImage())&&data.getImage().equalsIgnoreCase("1"))
                                        {

                                            String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");

                                            String profile_url  = "https://climbstaff.com/uploads/company/"+companyid+"/"+Utility.getUserID(context)+"/profile/image.jpg";
                                            Picasso.get().invalidate(profile_url);
                                            Picasso.get()
                                                    .load(profile_url)
                                                    .placeholder(R.drawable.defult_icon)
                                                    .transform(new CircleTransform())
                                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(iv_logo);

                                            btn_delete.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            Picasso.get()
                                                    .load(R.drawable.defult_icon)
                                                    .placeholder(R.drawable.defult_icon)
                                                    .transform(new CircleTransform())
                                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                    .into(iv_logo);
                                            btn_delete.setVisibility(View.GONE);
                                        }

                                      /*  if (TextUtils.isEmpty(data.getImage())) {
                                           // iv_logo.setBackground(ContextCompat.getDrawable(context, R.drawable.purple_circle));
                                         //   iv_logo.setImageResource(R.drawable.contact);
                                        } else {
                                            IOPref.getInstance().saveString(context,IOPref.PreferenceKey.profileImage, data.getImage());

                                            Picasso.get()
                                                    .load(data.getImage())
                                                    .transform(new CircleTransform())
                                                    .into(iv_logo);
                                        }*/

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
       iv_logo.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }


    File photoFile;
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
           // System.out.println("### photoFile length >>> "+photoFile.length());
            imageCompressTask = new ImageCompressTask(this, photoFile.getAbsolutePath(), iImageCompressTaskListener);

            mExecutorService.execute(imageCompressTask);
        }
        else if (requestCode==1)
        {
              handlegallerydata(context,data);
        }
    }
    private IImageCompressTaskListener iImageCompressTaskListener = new IImageCompressTaskListener() {
        @Override
        public void onComplete(List<File> compressed) {
            //photo compressed. Yay!

            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)

            File file = compressed.get(0);

            Log.d("ImageCompressor", "New photo size ==> " + file.length()); //log new file size.
            Picasso.get()
                    .load(file)
                    .transform(new CircleTransform())
                    .into(iv_logo);
            photoFile = file;
            System.out.println("### photoFile length 22 >>> "+photoFile.length());
            updateImage();
           // addImageofTool();
        }

        @Override
        public void onError(Throwable error) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error);
        }
    };

    public   void captureimage()
    {
        if (checkPermissions()) {
            openCameraIntent();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {

                case R.id.iv_logo:


                    showBottomSheetDialog(context,imagestatus);

                 /* new ImageSelection().showBottomSheetDialog(ProfileActivity.this, imagestatus, new SocketConnectionListner() {
                      @Override
                      public void onSocketCallback(Context mContext, String Socketstatus)
                      {


                          if (Socketstatus.equalsIgnoreCase("camera"))
                          {

                          }
                          else if (Socketstatus.equalsIgnoreCase("delete"))
                          {
                              try {
                                  if (NetworkUtil.checkNetworkStatus(context))
                                  {

                                      Alert.alert(context, "",getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                                          @Override
                                          public void run() {
                                              String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                                              Map<String, String> request = RetrofitRequest.deleteprofileImageRequest(Utility.getUserID(context),companyid);
                                              deleteImage(request);

                                              Picasso.get().load(R.drawable.defult_icon)
                                                      .placeholder(R.drawable.defult_icon)
                                                      .into(iv_logo);

                                              btn_delete.setVisibility(View.GONE);


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
                          }



                      }
                  });*/
                    break;

                case R.id.btn_delete:

                    try {
                        if (NetworkUtil.checkNetworkStatus(context))
                        {

                            Alert.alert(context, "",getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                                @Override
                                public void run() {
                                    String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                                    Map<String, String> request = RetrofitRequest.deleteprofileImageRequest(Utility.getUserID(context),companyid);
                                    deleteImage(request);

                                    Picasso.get().load(R.drawable.defult_icon)
                                            .placeholder(R.drawable.defult_icon)
                                            .into(iv_logo);

                                    btn_delete.setVisibility(View.GONE);


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


    private void updateImage() {

        String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("UploadForm[file]", photoFile.getName(), fileReqBody);

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), Utility.getUserID(context));
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), companyid);


        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient_image.getApi().requestAPROFILE_ADD(part, userId,company_id);

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
                            btn_delete.setVisibility(View.GONE);
                            if (response.getCode().equalsIgnoreCase("1")) {
                                setResult(RESULT_OK);
                                finish();
                            }
                            else if (response.getCode().equalsIgnoreCase("200"))
                            {
                              getProfile();
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


   /* public void addImageofTool( ) {

        String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
         String user_id = Utility.getUserID(context);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody company_id = RequestBody.create(MediaType.parse("text/plain"), companyid);


        Call<JsonObject> call = apiClient.getApi().requestAPROFILE_ADD(prepareFilePart("image" +0,"" ), userId,company_id);


        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response1) {


                try {
                    Log.e("response",response1.toString()+"");

                    String json = response1.body().toString();
                    JSONObject response = null;
                    response = new JSONObject(json);
                    if (response.getString("code").equalsIgnoreCase("200")) {

                    } else {

                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Log.v("onFailure","onFailure");
            }
        });

    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {


        // create RequestBody instance from file

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), photoFile);


        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData("UploadForm[file]", partName, requestFile);
    }*/


    String imageEncoded;
    private void handlegallerydata(Context context,Intent data)
    {

        try {
            // When an Image is picked
            if (null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                ArrayList<String> imagesEncodedList = new ArrayList<String>();

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();



                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        String file = getfilename(context,uri.toString());
                        photoFile= new File(file);
                        Picasso.get()
                                .load(photoFile)
                                .transform(new CircleTransform())
                                .into(iv_logo);
                        updateImage();


                        // Get the cursor
                        Cursor cursor =context.getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded = cursor.getString(columnIndex);
                        imagesEncodedList.add(imageEncoded);
                        cursor.close();
                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());

                }
                else
                {
                    if(data.getData()!=null)
                    {
                        Uri mImageUri=data.getData();
                        String file = getfilename(context,mImageUri.toString());
                        photoFile= new File(file);
                        Picasso.get()
                                .load(photoFile)
                                .transform(new CircleTransform())
                                .into(iv_logo);
                        updateImage();

                    }
                }
                //  }
            } else {
                Toast.makeText(context, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }



    }

    private  String getfilename(Context mContext,String  uri)
    {
        String imagecropFile = compressImage1(mContext,uri);

        Log.e("eerorr",imagecropFile);

        return imagecropFile;
    }
    private String getRealPathFromURI(Context mContext,String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public String compressImage1(Context mContext,String imageUri) {

        String filePath = getRealPathFromURI(mContext,imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 712.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;

        String filename = getFilename(mContext);
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename(Context mContext) {
        File file = new File(mContext.getFilesDir(), "Tooltribe/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    @SuppressWarnings("unchecked")
    private void deleteImage(Map<String, String> request) {
        try {
            showProgressDialogSimple(true);

            Call<CommonResponse> call = apiClient_image.getApi().deleteImage(request);

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
                               getProfile();
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
    protected void onResume() {
        super.onResume();
       Log.e("error","onResume called");
    }

    private BottomSheetDialog mBottomSheetDialog;
    public void showBottomSheetDialog(final Context mContext, final String imagestatus ) {




        mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog);
        View view = getLayoutInflater().inflate(R.layout.selectimage_options, null);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);

        TextView tv_galley = (TextView)view.findViewById(R.id.tv_galley);
        TextView tv_camera = (TextView)view.findViewById(R.id.tv_camera);
        final TextView tv_delete = (TextView)view.findViewById(R.id.tv_delete);

        if (imagestatus.equalsIgnoreCase("0"))
        {
            tv_delete.setVisibility(View.GONE);
        }
        else  if (imagestatus.equalsIgnoreCase("1"))
        {
            tv_delete.setVisibility(View.VISIBLE);
        }

        LinearLayout lv_cancel = (LinearLayout)view.findViewById(R.id.lv_cancel);

        tv_galley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                //  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                mBottomSheetDialog.dismiss();
            }
        });


        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (checkPermissions()) {
                    openCameraIntent();
                } else {
                    ActivityCompat.requestPermissions( ProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                }

                mBottomSheetDialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try {
                    if (NetworkUtil.checkNetworkStatus(context))
                    {

                        Alert.alert(context, "",getString(R.string.are_you_sure), getString(R.string.cancel), getString(R.string.yes), null, new Runnable() {
                            @Override
                            public void run() {
                                String companyid = IOPref.getInstance().getString(context, IOPref.PreferenceKey.companyImage, "");
                                Map<String, String> request = RetrofitRequest.deleteprofileImageRequest(Utility.getUserID(context),companyid);
                                deleteImage(request);

                                Picasso.get().load(R.drawable.defult_icon)
                                        .placeholder(R.drawable.defult_icon)
                                        .into(iv_logo);

                                btn_delete.setVisibility(View.GONE);


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
                mBottomSheetDialog.dismiss();
            }
        });





        lv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

            }
        });



        mBottomSheetDialog.show();
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

    }

}
