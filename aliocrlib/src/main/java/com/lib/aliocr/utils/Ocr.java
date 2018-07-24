package com.lib.aliocr.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.lib.aliocr.callback.OcrCallback;
import com.lib.aliocr.view.OCRMainActivity;
import com.lib.aliocr.widget.crop.Crop;
import com.lib.aliocr.widget.popup.XinPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class Ocr {

    private static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 222;
    private static final int PICK_IMAGE_PERMISSIONS_REQUEST_CODE = 222222;
    private static final int IMAGE_CROP_CODE = 3333;


    /**
     * 发起验证
     *
     * @param activity
     * @param rootView
     */
    public static void doOcr(final Activity activity, final Fragment fragment, final View rootView, final boolean isFace) {
        List<XinPopWindow.MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new XinPopWindow.MenuItem("拍照识别", 0));
        menuItems.add(new XinPopWindow.MenuItem("相册识别", 1));
        final XinPopWindow myPopWindow = new XinPopWindow(activity);
        myPopWindow.setData(menuItems);
        myPopWindow.setOnItemClickListener(new XinPopWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, int type) {
                if (type == 0) {
                    OCRMainActivity.Launcher(activity, isFace);
                } else {

                    if (fragment != null) {
                        Crop.pickImage(activity, fragment);
                    } else {

                        Crop.pickImage(activity);
                    }
                }
                myPopWindow.dismiss();
            }
        });
        myPopWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }


    /**
     * activity回调
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param result
     * @param callback
     */
    public static void onCropImgResult(Activity activity, Fragment fragment, int requestCode, int resultCode, Intent result, OcrCallback callback) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            OcrUtils.beginCrop(result.getData(), activity, fragment);
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imgUri = Crop.getOutput(result);

                if (callback != null) {
                    callback.onPicResult(OcrUtils.getPath(activity, imgUri));
                }
            } else if (resultCode == Crop.RESULT_ERROR) {
                if (callback != null) {

                    callback.onPicError();
                }
            }
        }
    }


    public static Uri onPickImgResult(Activity activity, int requestCode, int resultCode, Intent data) {

        Uri cropImageUri = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                cropImageUri = data.getData();

                if (OcrUtils.isReadExternalStoragePermissionsRequired(activity, cropImageUri)) {// 需要权限处理
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    handleImgShot(activity, cropImageUri);
                }

            }

        }
        return cropImageUri;

    }


    public static void onBackResult(Activity activity, int requestCode, int resultCode, Intent data, boolean isFace) {
        //noinspection StatementWithEmptyBody
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CROP_CODE) {
            // TODO

            //    new OCRModel().AuthCard(isFace,)
        }
    }


    public static void onRequestPermissionsResult(Activity activity, int requestCode, String permissions[],
                                                  int[] grantResults, Uri cropImageUri) {

        if (requestCode == PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (cropImageUri != null
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                handleImgShot(activity, cropImageUri);
            }
            // TODO  用户拒绝授权

        }
    }


    /**
     * Intent方式截图处理
     */
    public static void handleImgShot(Activity activity, Uri uri) {
        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + ".jpg");
        String cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, IMAGE_CROP_CODE);

    }


}
