package com.xin.lockdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xin.lockdemo.R;


/**
 * 使用方法：
 * final CustomDialog dialog = new CustomDialog(MainActivity.this);
 * <p>
 * dialog.setCustom(你的布局);
 * dialog.show();
 *
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class CustomDialog extends Dialog {


    private View mContentView;


    private RelativeLayout contentRoot;



    public CustomDialog(Context context) {
        super(context, R.style.dialog_normal);

    }


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog_custom_view_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();


    }


    private void initView() {

        contentRoot = findViewById(R.id.content_view_root);

    }

    /**
     * * 初始化界面控件的显示数据
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
     * * 初始化界面控件
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
