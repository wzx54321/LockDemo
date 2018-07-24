package com.lib.lock.gesture.customView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.lib.lock.gesture.bean.CellBean;
import com.lib.lock.gesture.config.Config;

/**
 */

public class DefaultLockerHitCellView implements IHitCellView {

    private RadialGradient mRadialGradient;
    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;
    private @ColorInt
    int fillColor;
    private @ColorInt
    int normalColor;

    private float lineWidth;

    private Paint paint;


    public DefaultLockerHitCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);


    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    public DefaultLockerHitCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getErrorColor() {
        return errorColor;
    }

    public DefaultLockerHitCellView setErrorColor(@ColorInt int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerHitCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public @ColorInt
    int getFillColor() {
        return fillColor;
    }

    public DefaultLockerHitCellView setFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerHitCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }


    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean, boolean isError) {
        final int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.getOuterColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        // draw fill circle
        Paint mPaint = new Paint();
        mRadialGradient = new RadialGradient(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth() * 4,
                getGradientColor(isError),Color.WHITE, Shader.TileMode.REPEAT );
        mPaint.setShader(mRadialGradient);
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth() * 4, mPaint);

        // draw inner circle
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 4f, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private @ColorInt
    int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getHitColor();
    }

    private @ColorInt
    int getOuterColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getNormalColor();
    }


    private @ColorInt
    int getGradientColor(boolean isError) {
        return isError ? this.getErrorColor() : Color.parseColor("#9AD7F7");
    }
}
