package com.lib.aliocr.callback;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */


public interface OcrCallback {


    void onPicResult(String picPath);

    void onPicError();
}
