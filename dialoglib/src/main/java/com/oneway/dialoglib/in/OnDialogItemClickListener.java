package com.oneway.dialoglib.in;

import com.oneway.dialoglib.base.IDialog;

public interface OnDialogItemClickListener<T> {
    void onItemClick(IDialog dialog, T data, int position);
}
