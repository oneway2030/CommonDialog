package com.oneway.dialoglib.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.oneway.dialoglib.config.AnimConst;
import com.oneway.dialoglib.manager.MyDialogsManager;

import java.lang.reflect.Field;


/**
 * Created by ww on 2020/7/30
 * DialogFragment 基类
 */
public abstract class MyBaseDialogFragment<T extends BaseBuilder> extends DialogFragment implements IDialog<T>, View.OnClickListener {
    private View mRooView = null;
    protected Context mContext;
    protected T builder;

    public void setBuilder(T builder) {
        this.builder = builder;
    }

    /**
     * 兼容6.0以下的某些版本 如（vivo x7、5.1.1系统）不走{@link #onAttach(Context)}方法，
     * 这里的Fragment用的不是v4包里面的
     *
     * @param activity Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutRes() > 0) {
            //调用方通过xml获取view
            mRooView = inflater.inflate(getLayoutRes(), container, false);
        } else if (getDialogView() != null) {
            //调用方直接传入view
            mRooView = getDialogView();
        }
        return mRooView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //如果isCancelable()是false 则会屏蔽物理返回键
            dialog.setCancelable(isCancelable());
            //如果isCancelableOutside()为false 点击屏幕外Dialog不会消失；反之会消失
            dialog.setCanceledOnTouchOutside(isCancelableOutside());
            //如果isCancelable()设置的是false 会屏蔽物理返回键
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && !isCancelable();
                }
            });
        }
        //设置默认Dialog的View布局
        builder.dialogView = view;
        initView(view);
//        dealDefaultDialog(mPositiveButtonListener, mNegativeButtonListener, titleStr,
//                contentStr, showBtnLeft, negativeStr, showBtnRight, positiveStr);
        //回调给调用者，用来设置子View及点击事件等
        if (builder.buildListener != null && mRooView != null) {
            builder.buildListener.onBuildChildView(this, mRooView, getLayoutRes());
        }
    }

    protected abstract void initView(View rooView);

    @Override
    public void onStart() {
        super.onStart();
        initDailog();
    }


    private void initDailog() {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        if (window == null) return;
        //设置背景色透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = window.getAttributes();
        //设置Dialog的Width
        if (getDialogWidth() > 0) {
            params.width = getDialogWidth();
        } else {
            //布局大小
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        //设置Dialog的Height
        if (getDialogHeight() > 0) {
            params.height = getDialogHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        //设置屏幕透明度 0.0f~1.0f(完全透明~完全不透明)
        params.dimAmount = getDimAmount();
        params.gravity = builder.gravity;
        // 如果当前没有设置动画效果，就设置一个默认的动画效果
        if (builder.animRes <= 0) {
            switch (builder.gravity) {
                case Gravity.TOP:
                    builder.animRes = AnimConst.ANIM_TOP;
                    break;
                case Gravity.BOTTOM:
                    builder.animRes = AnimConst.ANIM_BOTTOM;
                    break;
                case Gravity.LEFT:
                    builder.animRes = AnimConst.ANIM_LEFT;
                    break;
                case Gravity.RIGHT:
                    builder.animRes = AnimConst.ANIM_RIGHT;
                    break;
                default:
                    builder.animRes = AnimConst.ANIM_DEFAULT;
                    break;
            }
        }
        //设置Dialog动画效果
        if (getAnimRes() > 0) {
            window.setWindowAnimations(getAnimRes());
        }
        window.setAttributes(params);
    }

    protected int getLayoutRes() {
        return builder.layoutRes;
    }

    protected View getDialogView() {
        return builder.dialogView;
    }

    protected boolean isCancelableOutside() {
        return builder.isCancelableOutside;
    }

    @Override
    public boolean isCancelable() {
        return builder.cancelable;
    }

    protected int getDialogWidth() {
        return builder.dialogWidth;
    }

    protected int getDialogHeight() {
        return builder.dialogHeight;
    }

    public float getDimAmount() {
        return builder.dimAmount;
    }

    protected int getGravity() {
        return builder.gravity;
    }

    protected int getAnimRes() {
        return builder.animRes;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void dismiss() {
        //防止横竖屏切换时 getFragmentManager置空引起的问题：
        //Attempt to invoke virtual method 'android.app.FragmentTransaction
        //android.app.FragmentManager.beginTransaction()' on a null object reference
        if (getFragmentManager() == null) return;
        super.dismissAllowingStateLoss();
    }


    @Override
    public void onDestroy() {
        //监听dialog的dismiss
        if (builder.dismissListener != null) {
            builder.dismissListener.onDismiss(this);
        }
        super.onDestroy();
        MyDialogsManager.getInstance().over();
    }


    /**
     * 解决 Can not perform this action after onSaveInstanceState问题
     *
     * @param manager FragmentManager
     * @param tag     tag
     */
    public void showAllowingLoss(FragmentManager manager, String tag) {
        try {
            Class cls = DialogFragment.class;
            Field mDismissed = cls.getDeclaredField("mDismissed");
            mDismissed.setAccessible(true);
            mDismissed.set(this, false);
            Field mShownByMe = cls.getDeclaredField("mShownByMe");
            mShownByMe.setAccessible(true);
            mShownByMe.set(this, true);
        } catch (Exception e) {
            //调系统的show()方法
            show(manager, tag);
            return;
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        // 默认不实现，让子类实现

    }

    public void setOnClickListener(@IdRes int... ids) {
        if (mRooView != null) {
            for (int id : ids) {
                mRooView.findViewById(id).setOnClickListener(this);
            }
        }
    }

    public void setOnClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }


}
