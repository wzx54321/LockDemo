package com.lib.lock.gesture.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.lock.gesture.R;
import com.lib.lock.gesture.customView.OnPatternChangeListener;
import com.lib.lock.gesture.customView.PatternLockerView;
import com.lib.lock.gesture.utils.PatternHelper;

import java.util.List;

/**
 * 作者：xin on 2018/6/20 0020 11:01
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <P>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class GestureVerifyActivity extends AppCompatActivity {

    boolean isSettings;

    private PatternLockerView patternLockerView;
    private TextView textMsg;
    private PatternHelper patternHelper;
    private boolean isError;


    public static void launch(Activity activity, boolean isSettings) {
        Intent intent = new Intent(activity, GestureVerifyActivity.class);
        intent.putExtra("isSettings", isSettings);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);
        isSettings = getIntent().getBooleanExtra("isSettings", false);


        this.patternLockerView = findViewById(R.id.pattern_lock_view);
        this.textMsg = findViewById(R.id.text_msg);

        this.patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                isError = !isPatternOk(hitList);
                view.updateStatus(isError);
                //    patternIndicatorView.updateState(hitList, isError);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        this.textMsg.setText("绘制解锁图案");
        this.patternHelper = new PatternHelper();
    }

    private boolean isPatternOk(List<Integer> hitList) {
        this.patternHelper.validateForChecking(hitList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.textMsg.setText(this.patternHelper.getMessage());

    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()) {

            if (isSettings && !isError) {
                GestureSettingsActivity.launch(this);
            } else if (!isError) {
                Toast.makeText(this, "验证成功！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "验证失败！", Toast.LENGTH_LONG).show();
            }
            finish();


        }
    }
}
