package com.lib.lock.gesture.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 作者：xin on 2018/6/7 0007 14:17
 * <p>
 * 邮箱：ittfxin@126.com
 *
 * <P>
 * https://github.com/wzx54321/XinFrameworkLib
 */



public class ContextUtils {



    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private ContextUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull Context context) {
        ContextUtils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}
