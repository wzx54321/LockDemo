package com.lib.lock.fingerprint.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lib.lock.fingerprint.BuildConfig;
import com.lib.lock.fingerprint.R;
import com.lib.lock.fingerprint.core.FingerprintCore;
import com.lib.lock.fingerprint.core.MyListener;
import com.lib.lock.fingerprint.dialog.FingerDialog;


/**
 * <p>
 * create by xin
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class FingerprintUtil {

    private static final String ACTION_SETTING = "android.settings.SETTINGS";
    /**
     * 重试次数
     */
    private static int retryCount;

    /**
     * 打开设置页面根据需求调用
     *
     * @param context
     */
    public static void openFingerPrintSettingPage(Context context) {
        Intent intent = new Intent(ACTION_SETTING);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 系统支持和系统开启了指纹
     */
    public static boolean supportAndSysOpenedFingerPrint() {
        FingerprintCore mFingerprintCore = FingerprintCore.getInstance();

        return mFingerprintCore.isSupport() && mFingerprintCore.isHasEnrolledFingerprints();
    }


    /**
     * 开始识别
     */
    public static void startFingerprintRecognition(final Activity activity, final MyListener listener) {

        final FingerDialog dialog = new FingerDialog(activity);
        retryCount = 4;
        // 硬件支持且有指纹密码
        if (supportAndSysOpenedFingerPrint()) {

            final View[] rootContent = {LayoutInflater.from(FingerContext.getContext()).inflate(R.layout.finger_dialog_icon_info, null)};
            dialog.setCustom(rootContent[0]);
            final TextView textRetry = rootContent[0].findViewById(R.id.text_retry);
            final TextView textContent = rootContent[0].findViewById(R.id.text_title);
            final ImageView iconFinger = rootContent[0].findViewById(R.id.icon_finger);
            final TextView textOk = rootContent[0].findViewById(R.id.btn_cancle);

            textRetry.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            textRetry.setVisibility(View.GONE);
            rootContent[0].findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        FingerprintCore.getInstance().cancelAuthenticate();

                        retryCount = 4;
                    }
                }
            });

            FingerprintCore.getInstance().setFingerprintManager(new FingerprintCore.IFingerprintResultListener() {
                @Override
                public void onAuthenticateSuccess() {

                    if (listener != null) {
                        listener.onAuthenticateSuccess();
                    }
                    dialog.dismiss();
                    textContent.setVisibility(View.VISIBLE);
                    textContent.setText(R.string.auth_ok);
                    textRetry.setVisibility(View.GONE);
                    iconFinger.setVisibility(View.GONE);
                    textOk.setText(R.string.ok);
                    FingerprintCore.getInstance().cancelAuthenticate();

                    showLog("指纹识别成功");
                    dialog.show();
                }

                @Override
                public void onAuthenticateFailed(int helpId) {


                 /*   if (retryCount < 1) {
                        FingerprintCore.getInstance().cancelAuthenticate();

                        retryCount = 4;
                        dialog.dismiss();
                        return;
                    }
                    textRetry.setText(String.format(FingerContext.getContext().getString(R.string.error_retry_count_format), retryCount));
                    showLog(FingerContext.getContext().getString(R.string.log_info_failed));

                    textContent.setVisibility(View.GONE);
                    textRetry.setVisibility(View.VISIBLE);
                    dialog.show();

                    iconFinger.clearAnimation();
                    Animation anim = AnimationUtils.loadAnimation(FingerContext.getContext(), R.anim.anim_shake);
                    iconFinger.startAnimation(anim);

                    if (listener != null) {
                        listener.onAuthenticateFailed(helpId);
                    }
                    retryCount--;*/

                    textRetry.setVisibility(View.VISIBLE);
                    iconFinger.clearAnimation();
                    Animation anim = AnimationUtils.loadAnimation(FingerContext.getContext(), R.anim.anim_shake);
                    iconFinger.startAnimation(anim);
                }

                @Override
                public void onAuthenticateError(int errMsgId) {

                    if (errMsgId == 7) {
                        Toast.makeText(FingerContext.getContext(), R.string.toast_text_retry_to_much, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onStartAuthenticateResult(boolean isSuccess) {
                    if (listener != null) {
                        listener.onStartAuthenticateResult(isSuccess);
                    }
                }
            });
            dialog.show();
           /* if (FingerprintCore.getInstance().isAuthenticating()) {
                //   Toast.makeText(FingerContext.getContext(), "指纹识别已经开启，长按指纹解锁键", Toast.LENGTH_LONG).show();
            } else {*/
                FingerprintCore.getInstance().startDelay();
          //  }
            showLog(FingerContext.getContext().getString(R.string.log_info_start));

        } else if (!FingerprintCore.getInstance().isSupport()) {
            // 硬件不支持
            View rootContent = LayoutInflater.from(FingerContext.getContext()).inflate(R.layout.finger_dialog_error, null);
            TextView errInfo = rootContent.findViewById(R.id.text_info);
            rootContent.findViewById(R.id.btn_i_see).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        FingerprintCore.getInstance().cancelAuthenticate();
                        retryCount = 4;
                    }
                }
            });
            errInfo.setText(R.string.finger_info_error_un_support);
            dialog.setCustom(rootContent);
            dialog.show();
        } else if (!FingerprintCore.getInstance().isHasEnrolledFingerprints()) {
            // 没有指纹
            View rootContent = LayoutInflater.from(FingerContext.getContext()).inflate(R.layout.finger_dialog_error, null);
            TextView errInfo = rootContent.findViewById(R.id.text_info);
            rootContent.findViewById(R.id.btn_i_see).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        FingerprintCore.getInstance().cancelAuthenticate();
                    }
                }
            });
            errInfo.setText(R.string.finger_info_error_un_get_fingerprint);
            dialog.setCustom(rootContent);
            dialog.show();
        }


    }

    private static void showLog(String msg) {

        if (BuildConfig.DEBUG) {
            Log.d(FingerContext.getContext().getString(R.string.app_name), msg);
        }
    }
}
