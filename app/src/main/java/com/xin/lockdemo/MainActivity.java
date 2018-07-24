package com.xin.lockdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lib.aliocr.view.OcrLaunchFragment;
import com.lib.lock.fingerprint.activities.FingerprintPswMainActivity;
import com.lib.lock.fingerprint.core.FingerprintCore;
import com.lib.lock.fingerprint.utils.FingerprintUtil;
import com.lib.lock.gesture.content.SPManager;
import com.xin.lockdemo.dialog.CustomDialog;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnFingprint;
    private Uri imgUri;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.xin.lockdemo.R.layout.activity_main);

        findViewById(com.xin.lockdemo.R.id.gesture_password).setOnClickListener(this);
        findViewById(com.xin.lockdemo.R.id.ocr_auth).setOnClickListener(this);
        mBtnFingprint = findViewById(com.xin.lockdemo.R.id.fingerprint_password);
        mBtnFingprint.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case com.xin.lockdemo.R.id.ocr_auth:

                FragmentTransaction transition = fragmentManager.beginTransaction();

                transition.replace(com.xin.lockdemo.R.id.fragment_content_root, new OcrLaunchFragment(), OcrLaunchFragment.class.getSimpleName());
                if (transition.isAddToBackStackAllowed())
                    transition.addToBackStack(OcrLaunchFragment.class.getSimpleName());
                transition.commit();
                findViewById(com.xin.lockdemo.R.id.layout_).setVisibility(View.GONE);
                break;
            case com.xin.lockdemo.R.id.gesture_password:
                GesturePswMainActivity.Launcher(this);
                break;
            case com.xin.lockdemo.R.id.fingerprint_password:

                FingerprintUtil.startFingerprintRecognition(this, null);
                //    authActivity();


                break;
        }
    }


    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            findViewById(com.xin.lockdemo.R.id.layout_).setVisibility(View.VISIBLE);
        } else {

            super.onBackPressed();
        }

    }

    /**
     * 跳转验证
     */
    private void authActivity() {

        if ("验证指纹密码".contentEquals(mBtnFingprint.getText())) {

            FingerprintPswMainActivity.Launcher(this);
            return;
        }
        if (!FingerprintCore.getInstance().isSupport()) {
            Toast.makeText(this, "系统不支持指纹识别", Toast.LENGTH_LONG).show();
            return;
        }
        if (!FingerprintCore.getInstance().isHasEnrolledFingerprints()) {
            Toast.makeText(this, "请检查是否录入指纹", Toast.LENGTH_LONG).show();
            final CustomDialog selfDialog = new CustomDialog(this);

            View view = LayoutInflater.from(this).inflate(com.xin.lockdemo.R.layout.login_finger_change_dialog, null, false);

            view.findViewById(com.xin.lockdemo.R.id.open_fingerprint).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FingerprintUtil.openFingerPrintSettingPage(getApplicationContext());
                }
            });
            selfDialog.setCustom(view);

            selfDialog.show();

            return;
        } else {
            SPManager.getInstance().setHasFingerPrint(true);
            mBtnFingprint.setText("验证指纹密码");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (FingerprintUtil.supportAndSysOpenedFingerPrint() &&
                SPManager.getInstance().getHasFingerPrint()) {
            mBtnFingprint.setText("验证指纹密码");
        } else {
            mBtnFingprint.setText("开启指纹密码");
        }
    }



   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Ocr.onRequestPermissionsResult(this, requestCode, permissions, grantResults, imgUri);
    }*/
}
