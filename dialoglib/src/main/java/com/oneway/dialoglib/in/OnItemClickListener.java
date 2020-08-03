package com.oneway.dialoglib.in;

import com.oneway.dialoglib.base.IDialog;

public interface OnItemClickListener<T> {
    void onItemClick(IDialog dialog, T data, int position);
}
