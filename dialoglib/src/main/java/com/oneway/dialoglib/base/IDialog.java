package com.oneway.dialoglib.base;

import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

/**
 * Created by mq on 2018/9/2 下午6:01
 * mqcoder90@gmail.com
 */

public interface IDialog<T extends BaseBuilder> {

    /**
     * 弹窗消失
     */
    void dismiss();

    Context getContext();

    void setBuilder(T builder);

    default void showAllowingLoss(FragmentManager manager, String tag) {
    }

    /***
     * 点击事件
     */
    interface OnClickListener {
        void onClick(IDialog dialog);
    }

    /**
     * Dialog消失回调
     */
    interface OnDismissListener {
        /**
         * This method will be invoked when the dialog is dismissed.
         *
         * @param dialog the dialog that was dismissed will be passed into the
         *               method
         */
        void onDismiss(IDialog dialog);
    }

    /**
     * 构造dialog里的View
     */
    interface OnBuildListener {
        /**
         * @param dialog    IDialog
         * @param view      Dialog整体View
         * @param layoutRes Dialog的布局 如果没有传入 默认是0
         */
        void onBuildChildView(IDialog dialog, View view, int layoutRes);
    }


}
