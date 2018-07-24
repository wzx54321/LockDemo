package com.lib.aliocr.api;


import com.lib.aliocr.bean.RepOutput;
import com.lib.aliocr.common.Api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.lib.aliocr.common.Api.path;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public interface MyApiManager {


    @POST(path)
    @Headers({"Authorization: APPCODE " + Api.APPCODE})
    Observable<RepOutput> authCard(@Body RequestBody body);
}
