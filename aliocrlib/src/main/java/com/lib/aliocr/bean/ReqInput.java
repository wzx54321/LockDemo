package com.lib.aliocr.bean;

import java.io.Serializable;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <P>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public class ReqInput implements Serializable {
    private static final long serialVersionUID = 7540344538333996814L;

    private String image;// 图片的 base64
    private String configure;//  "{\"side\":\"face\"}"   身份证正反面类型:face/back


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConfigure() {
        return configure;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }
}
