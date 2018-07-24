package com.lib.aliocr.utils.io;

import java.nio.charset.Charset;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class StringCodingUtils {

    public static byte[] getBytes(String src, Charset charSet) {
        return src.getBytes(charSet);
    }

}
