package com.lib.aliocr.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lib.aliocr.R;
import com.lib.aliocr.callback.OcrCallback;
import com.lib.aliocr.contact.OcrContact;
import com.lib.aliocr.presenter.OCRPresenter;
import com.lib.aliocr.utils.Ocr;

import cameraview.CameraView;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
@SuppressWarnings("StatementWithEmptyBody")
public class OCRMainActivity extends AppCompatActivity implements OcrContact.V {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String TAG = OCRMainActivity.class.getSimpleName();
    private CameraView mCameraView;
    private Handler mBackgroundHandler;
    private OCRPresenter presenter;
    private boolean mIsFace;

    public static void Launcher(Context context, boolean isFace) {

        Intent intent = new Intent(context, OCRMainActivity.class);


        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("mIsFace", isFace);


        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFace = getIntent().getBooleanExtra("mIsFace", true);

        setContentView(R.layout.activity_ocr_main);
        presenter = new OCRPresenter(this);
        mCameraView = findViewById(R.id.my_camera);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
        FloatingActionButton fab = findViewById(R.id.take_picture_);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCameraView != null) {
                        mCameraView.takePicture();
                    }
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA))
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } /*else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.      .CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        }*/ else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        }
    }


    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);

            presenter.onPictureTaken(data );

            //   mCameraView.stop();

        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Ocr.onCropImgResult(this, null, requestCode, resultCode, data, new OcrCallback() {
            @Override
            public void onPicResult(String picPath) {
                presenter.request(mIsFace, picPath);
            }

            @Override
            public void onPicError() {

            }
        });
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
