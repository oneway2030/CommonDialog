package com.oneway.dialoglib.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.oneway.dialoglib.util.ScreenUtil;

public abstract class BaseBuilder<T extends BaseBuilder> {
    public static final String FTag = "dialogTag";
    public FragmentManager fragmentManager;
    public int layoutRes;
    public int dialogWidth;
    public int dialogHeight;
    public float dimAmount = 0.4f;
    public int gravity = Gravity.CENTER;
    public boolean isCancelableOutside = true;
    public boolean cancelable = true;
    public View dialogView;
    public Context mContext;
    public int animRes = -1;//Dialog动画style
    public IDialog.OnBuildListener buildListener;
    public IDialog.OnDismissListener dismissListener;
    private IDialog dialog;

    public BaseBuilder(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("Context must be Activity");
        }
        mContext = context;
        fragmentManager = ((Activity) context).getFragmentManager();
    }

    /**
     * 设置DialogView
     *
     * @param layoutRes 布局文件
     * @return BaseBuilder
     */
    public T setDialogView(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        return (T) this;
    }

    /**
     * 设置DialogView
     *
     * @param dialogView View
     * @return BaseBuilder
     */
    public T setDialogView(View dialogView) {
        this.dialogView = dialogView;
        return (T) this;
    }

    /**
     * 设置屏幕宽度百分比
     *
     * @param percentage 0.0f~1.0f
     * @return BaseBuilder
     */
    public T setScreenWidthP(float percentage) {
        this.dialogWidth = (int) (ScreenUtil.getScreenWidth(mContext) * percentage);
        return (T) this;
    }

    /**
     * 设置屏幕高度百分比
     *
     * @param percentage 0.0f~1.0f
     * @return BaseBuilder
     */
    public T setScreenHeightP(float percentage) {
        this.dialogHeight = (int) (ScreenUtil.getScreenHeight(mContext) * percentage);
        return (T) this;
    }

    /**
     * 设置Dialog的宽度
     *
     * @param width 宽度
     * @return BaseBuilder
     */
    public T setWidth(int width) {
        this.dialogWidth = width;
        return (T) this;
    }

    /**
     * 设置Dialog的高度
     *
     * @param height 高度
     * @return BaseBuilder
     */
    public T setHeight(int height) {
        this.dialogHeight = height;
        return (T) this;
    }

    /**
     * 设置背景色色值
     *
     * @param percentage 0.0f~1.0f 1.0f为完全不透明
     * @return BaseBuilder
     */
    public T setWindowBackgroundP(float percentage) {
        this.dimAmount = percentage;
        return (T) this;
    }

    /**
     * 设置Gravity
     *
     * @param gravity Gravity
     * @return BaseBuilder
     */
    public T setGravity(int gravity) {
        this.gravity = gravity;
        return (T) this;
    }

    /**
     * 设置dialog外点击是否可以让dialog消失
     *
     * @param cancelableOutSide true 则在dialog屏幕外点击可以使dialog消失
     * @return BaseBuilder
     */
    public T setCancelableOutSide(boolean cancelableOutSide) {
        this.isCancelableOutside = cancelableOutSide;
        return (T) this;
    }

    /**
     * 设置是否屏蔽物理返回键
     *
     * @param cancelable true 点击物理返回键可以让dialog消失；反之不消失
     * @return BaseBuilder
     */
    public T setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return (T) this;
    }

    /**
     * 构建子View的listener
     *
     * @param listener IDialog.OnBuildListener
     * @return BaseBuilder
     */
    public T setBuildChildListener(IDialog.OnBuildListener listener) {
        this.buildListener = listener;
        return (T) this;
    }

    /**
     * 监听dialog的dismiss
     *
     * @param dismissListener IDialog.OnDismissListener
     * @return T
     */
    public T setOnDismissListener(IDialog.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
        return (T) this;
    }

    /**
     * 设置dialog的动画效果
     *
     * @param animStyle 动画资源文件
     * @return T
     */
    public T setAnimStyle(int animStyle) {
        this.animRes = animStyle;
        return (T) this;
    }

    /**
     * 移除之前的dialog
     */
    protected void removePreDialog() {
        FragmentTransaction ft = this.fragmentManager.beginTransaction();
        Fragment prev = this.fragmentManager.findFragmentByTag(FTag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.commitAllowingStateLoss();
    }


    /**
     * 展示Dialog
     *
     * @return SYDialog
     */
    public IDialog show() {
        if (this.layoutRes <= 0 && this.dialogView == null) {
            //如果没有设置布局 提供默认设置
//            setDefaultOption();
        }
        //创建dialog
        dialog = create();
        dialog.setBuilder(this);
        //判空
        if (this.mContext == null) return dialog;
        if (this.mContext instanceof Activity) {
            Activity activity = (Activity) this.mContext;
            //如果Activity正在关闭或者已经销毁 直接返回
            boolean isRefuse = Build.VERSION.SDK_INT >= 17
                    ? activity.isFinishing() || activity.isDestroyed()
                    : activity.isFinishing();

            if (isRefuse) return dialog;
        }
        removePreDialog();
        dialog.showAllowingLoss(this.fragmentManager, FTag);
        return dialog;
    }

    /**
     * 创建dialog
     *
     * @return
     */
    protected abstract IDialog create();


}
