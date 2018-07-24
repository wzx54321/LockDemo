package com.lib.aliocr.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.lib.aliocr.bean.RepOutput;
import com.lib.aliocr.contact.OcrContact;
import com.lib.aliocr.modle.OCRModel;
import com.lib.aliocr.utils.OcrUtils;
import com.lib.aliocr.view.OCRMainActivity;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：xin on 2018/7/9 0009 15:03
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class OCRPresenter implements OcrContact.P {
    private OcrContact.M model;
    private OcrContact.V view;
    private static final String TAG = OCRMainActivity.class.getSimpleName();

    public OCRPresenter(OcrContact.V view) {
        this.view = view;
        model = new OCRModel();
    }

    public void onPictureTaken(final byte[] data) {



        Disposable disposable=  Observable .create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) {

                String fileName = System.currentTimeMillis() + "picture.jpg";
                File file = new File(view.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        fileName);
                Bitmap bitmap = OcrUtils.byte2bitmap(data);

                OcrUtils.saveBitmap(bitmap, file.getAbsolutePath());

                if (/*OcrUtils.compressSmallImage(bitmap, file, 4 * 1023)*/file.exists()) {
                    emitter.onNext(file);
                } else {
                    emitter.onError(new Exception("图片转换失败"));
                }
            }
        }).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                if (file != null && file.exists()) {
                    OcrUtils.beginCrop(Uri.fromFile(file), view.getActivity(), null);

                }
            }
        });
    }

    public void request(boolean isFace, final String imgPath) {
        model.AuthCard(isFace, imgPath).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<RepOutput>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RepOutput repOutput) {
                        if (repOutput != null)
                            Log.i("HTTPLOG", repOutput.toString());

                        Toast.makeText(view.getActivity(), "" + repOutput.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("HTTPLOG", e.toString());
                        Toast.makeText(view.getActivity(), "" + e.toString(), Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
