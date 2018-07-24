package com.lib.lock.gesture.customView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.lib.lock.gesture.bean.CellBean;
import com.lib.lock.gesture.config.Config;

/**
 */

public class DefaultLockerNormalCustomCellView implements INormalCellView {

    private @ColorInt
    int normalColor;
    private @ColorInt
    int fillColor;
    private @ColorInt
    int hitColor;

    private float lineWidth;

    private Paint paint;


    public DefaultLockerNormalCustomCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerNormalCustomCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public DefaultLockerNormalCustomCellView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerNormalCustomCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }


    public DefaultLockerNormalCustomCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean) {
        final int saveCount = canvas.save();

        // draw inner circle
        this.paint.setColor(getNormalColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 4f, this.paint);

        canvas.restoreToCount(saveCount);
    }



}
