package com.xin.lockdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.lib.lock.gesture.activities.GestureSettingsActivity;
import com.lib.lock.gesture.activities.GestureVerifyActivity;
import com.lib.lock.gesture.content.SPManager;


/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class GesturePswMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnPswSettings;
    private Button mBtnVerifyPsw;


    public static void Launcher(Context context) {

        Intent intent = new Intent(context, GesturePswMainActivity.class);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.xin.lockdemo.R.layout.activity_gesture_psw_main);

        mBtnPswSettings = findViewById(com.xin.lockdemo.R.id.btn_settings_gesture_password);
        mBtnVerifyPsw = findViewById(com.xin.lockdemo.R.id.btn_verify_gesture_password);

        mBtnPswSettings.setOnClickListener(this);
        mBtnVerifyPsw.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (hasPsw()) {
            mBtnVerifyPsw.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_settings_gesture_password:

                if (hasPsw()) {
                    GestureVerifyActivity.launch(this,true);
                } else {
                    GestureSettingsActivity.launch(this);
                }

                break;

            case  R.id.btn_verify_gesture_password:
                GestureVerifyActivity.launch(this,false);
                break;

        }
    }


    private boolean hasPsw() {
        return !TextUtils.isEmpty(SPManager.getInstance().getPatternPSW());
    }
}
