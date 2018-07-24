package com.lib.aliocr.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lib.aliocr.R;
import com.lib.aliocr.callback.OcrCallback;
import com.lib.aliocr.contact.OcrContact;
import com.lib.aliocr.presenter.OCRPresenter;
import com.lib.aliocr.utils.Ocr;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class OcrLaunchFragment extends Fragment implements View.OnClickListener, OcrContact.V {

    boolean mIsFace;
    private View rootView;

    private OcrContact.P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new OCRPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ocr_launch, null);
        rootView.findViewById(R.id.side_face).setOnClickListener(this);
        rootView.findViewById(R.id.side_back).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.side_face) {
            mIsFace = true;
        } else if (i == R.id.side_back) {
            mIsFace = false;
        }
        Ocr.doOcr(getActivity(),this, rootView, mIsFace);
    }


    //  ----------------------------------ocr使用-------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Ocr.onCropImgResult(getActivity(),this, requestCode, resultCode, data, new OcrCallback() {
            @Override
            public void onPicResult(String picPath) {
                mPresenter.request(mIsFace, picPath);
            }

            @Override
            public void onPicError() {

            }
        });
    }

}
