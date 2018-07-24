package com.lib.aliocr.contact;

import android.app.Activity;

import com.lib.aliocr.bean.RepOutput;

import io.reactivex.Observable;

/**
 * 作者：xin on 2018/7/12 0012 16:55
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public interface OcrContact {

    interface V {

        Activity getActivity();
    }

    interface P {
        void onPictureTaken(final byte[] data );

        void request(boolean isFace, String imgPath);
    }


    interface M {

        Observable<RepOutput> AuthCard(boolean isFace, String imgPath);
    }
}
