package com.lib.lock.gesture.customView;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lib.lock.gesture.bean.CellBean;

import java.util.List;

/**
 */

public interface ILockerLinkedLineView {
    /**
     * 绘制图案密码连接线
     *
     * @param canvas
     * @param hitList
     * @param cellBeanList
     * @param endX
     * @param endY
     * @param isError
     */
    void draw(@NonNull Canvas canvas,
              @Nullable List<Integer> hitList,
              @NonNull List<CellBean> cellBeanList,
              float endX,
              float endY,
              boolean isError);
}