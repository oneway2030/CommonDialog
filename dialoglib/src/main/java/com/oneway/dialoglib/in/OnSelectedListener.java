package com.oneway.dialoglib.in;

import com.oneway.dialoglib.base.IDialog;

import java.util.HashMap;

public interface OnSelectedListener<T> {
    /**
     * 选择回调
     *
     * @param data 选择的位置和数据
     */
    void onSelected(IDialog dialog, HashMap<Integer, T> data);
}
