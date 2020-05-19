package iostudio.in.et.utility;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import iostudio.in.et.BuildConfig;
import iostudio.in.et.R;

public class ImageSelection
{
    private BottomSheetDialog mBottomSheetDialog;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 33;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public void showBottomSheetDialog(final Activity mContext, final String imagestatus , final SocketConnectionListner socketConnectionListner) {




        mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialog);
        View view = mContext.getLayoutInflater().inflate(R.layout.selectimage_options, null);

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
                mContext.startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                mBottomSheetDialog.dismiss();
            }
        });


        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                socketConnectionListner.onSocketCallback(mContext,"camera");
                /*if (checkPermissions(mContext)) {
                    openCameraIntent(mContext,mContext);
                } else {
                    ActivityCompat.requestPermissions(mContext,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS_REQUEST_CODE);
                }*/
                mBottomSheetDialog.dismiss();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                socketConnectionListner.onSocketCallback(mContext,"delete");
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


    /**
     * Return the current state of the permissions needed.
     */
    public boolean checkPermissions(Context mContext) {
        int permissionState = ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static File photoFile;
    private void openCameraIntent(Context mContext,Activity activity) {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(mContext.getPackageManager()) != null){
            //Create a file to store the image
            photoFile = null;
            try {
                photoFile = createImageFile(mContext);
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                activity.startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }
    String imageFilePath;
    private File createImageFile(Context mContext) throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


}
