package com.lib.lock.fingerprint.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lib.lock.fingerprint.R;


/**
 * 使用方法：
 * final FingerDialog selfDialog = new FingerDialog(MainActivity.this);
 * <p>
 * selfDialog.setCustom(你的布局);
 * <p>
 * FingerDialog.show();
 * <p>
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */


public class FingerDialog extends Dialog {


    private View mContentView;


    private RelativeLayout contentRoot;


    public FingerDialog(Context context) {
        super(context, R.style.dialog_finger);

    }


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_dialog_custom_view_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

       setCancelable(false);
    }


    private void initView() {

        contentRoot = findViewById(R.id.content_view_root);

    }

    /**
     * 初始化界面控件的显示数据
     */


    private void initData() {


        if (mContentView != null) {
            RelativeLayout.LayoutParams params = new RelativeLayout.
                    LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            contentRoot.addView(mContentView, params);

        }

    }

    /**
     * 初始化界面控件
     */

    private void initEvent() {


    }


    public View getContentView() {
        return mContentView;
    }

    public void setCustom(View mContentView) {
        this.mContentView = mContentView;


    }
}
