package com.lib.aliocr.http.helper;


import com.lib.aliocr.api.HttpCustomConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class HttpHelper {


    private OkHttpClient mOkHttpClient;
    private RxJava2CallAdapterFactory mAdapterFactory;
    private GsonConverterFactory mGsonConverterFactory;

    private HttpHelper() {

        mGsonConverterFactory = GsonConverterFactory.create();
        mAdapterFactory = RxJava2CallAdapterFactory.create();
        //设置超时时间
        mOkHttpClient = new HttpCustomConfig().build();
    }

    public static HttpHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public  Retrofit getRetrofit(String url) {

        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(mAdapterFactory)
                .addConverterFactory(mGsonConverterFactory)
                .client(mOkHttpClient)
                .build();
    }

    private static final class LazyHolder {
        static final HttpHelper INSTANCE = new HttpHelper();
    }
}
