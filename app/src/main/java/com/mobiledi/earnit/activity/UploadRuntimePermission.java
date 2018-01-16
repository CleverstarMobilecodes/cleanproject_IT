package com.mobiledi.earnit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.SparseIntArray;
import android.view.Window;
import android.widget.ImageView;

import com.mobiledi.earnit.R;
import com.mobiledi.earnit.interfaces.ImageSelection;
import com.mobiledi.earnit.utils.AppConstant;
import com.mobiledi.earnit.utils.ScalingUtilities;
import com.mobiledi.earnit.utils.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by mobile-di on 12/8/17.
 */

public class UploadRuntimePermission extends Activity implements ImageSelection{
    

    SparseIntArray sparseIntArray;
    public final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    public final int SELECTED_PICTURE = 2;
    public boolean cameraStatus = false;
    public boolean rLibraryStatus = false;
    public boolean wLibraryStatus = false;
    public  String gFileName = null;
    public  Intent in = null;
    Bundle savedInstanceState;
    public final String TAG = "UploadRuntimePermission";
    Uri uri;
    Intent CropIntent;
    public final int CROP_IMAGE = 3;
    UploadRuntimePermission runtimePermission;
    public ImageView uploadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sparseIntArray = new SparseIntArray();
        runtimePermission = this;
    }

    public void requestRequiredApplicationPermission(final String[] requestedPermissions, final int stringId, final int requestCode) {
        sparseIntArray.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean showRequestPermission = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            showRequestPermission = showRequestPermission || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (!showRequestPermission) {
                ActivityCompat.requestPermissions(UploadRuntimePermission.this, requestedPermissions, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && PackageManager.PERMISSION_GRANTED == permissionCheck) {
        } else {
        }
    }

    public void showToast(String message){
        Utils.showToast(UploadRuntimePermission.this, message);
    }


    public void lockScreen(){
        Utils.lockScreen(getWindow());
    }


    public void unLockScreen(){
        Utils.unLockScreen(getWindow());
    }

    public void josnError(JSONObject message){
        Utils.JsonError(message, runtimePermission );
    }



    public void vRuntimePermission(ImageView view) {
         uploadView = view;
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            rLibraryStatus = true;
        } else {
            rLibraryStatus = false;
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            wLibraryStatus = true;
        } else {
            wLibraryStatus = false;
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraStatus = true;
        } else {
            cameraStatus = false;
        }
    }

    public void selectImage() {
        final CharSequence[] items = {AppConstant.GALLERY, AppConstant.LIBRARY, AppConstant.EXIT};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        SpannableStringBuilder dTitle = new SpannableStringBuilder();
        dTitle.append(AppConstant.VIA);
        dTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(runtimePermission, R.color.pink)), 0, 9, 0);
        builder.setTitle(dTitle);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(AppConstant.GALLERY)) {
                    if (cameraStatus && rLibraryStatus && wLibraryStatus)
                        fromCamera();
                    else {
                        showToast("Camera permission required");
                    }

                } else if (items[item].equals(AppConstant.LIBRARY)) {
                    if (rLibraryStatus && wLibraryStatus)
                        fromGallery();
                    else {
                        showToast("Storage permission required");
                    }

                } else if (items[item].equals(AppConstant.EXIT)) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
    }

    @Override
    public void fromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        cameraIntent.putExtra("return-data", true);

        startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                        ImageCropFunction();

                    }
                }
                break;

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        ImageCropFunction();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CROP_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            gFileName = decodeFile(data.getData().getPath(), 500, 500);
                            Bundle bundle = data.getExtras();
                            Bitmap bitmap = bundle.getParcelable("data");
                            uploadView.setImageBitmap(bitmap);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 180);
            CropIntent.putExtra("outputY", 180);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);
            File file = new File(Environment.getExternalStorageDirectory(),
                    "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
            uri = Uri.fromFile(file);

            CropIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

            CropIntent.putExtra("return-data", true);
            startActivityForResult(CropIntent, CROP_IMAGE);

        } catch (ActivityNotFoundException e) {

        }
    }
    //Image Crop Code End Here

    public String decodeFile(String path, int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }
}
