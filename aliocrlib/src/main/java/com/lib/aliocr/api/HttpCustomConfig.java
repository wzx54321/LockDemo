package com.lib.aliocr.api;

import com.lib.aliocr.http.config.HttpConfig;

import okhttp3.OkHttpClient;

/**
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * 邮箱：ittfxin@126.com
 * <P>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class HttpCustomConfig extends HttpConfig {
    @Override
    public OkHttpClient.Builder getCustomBuilder() {
        // 根据个人需要配置
        return null;
    }
}
