package com.oneway.dialoglib.in;


import com.chad.library.adapter.base.viewholder.BaseViewHolder;

public interface OnConvertItemListener<T> {
    void conver(BaseViewHolder helper, T data, int defSelectposition);
}
