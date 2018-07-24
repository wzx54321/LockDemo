package com.lib.aliocr.utils;

import sun.misc.BASE64Encoder;
/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class Base64 {


    public static String getAuthor(String secretKey,String author) {
        byte[] data = null;
        String s = secretKey + author;
        try {
            data = s.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data != null ? data : new byte[0]);
    }
}
