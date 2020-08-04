package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.oneway.dialoglib.R;
import com.oneway.dialoglib.base.BaseBuilder;
import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.base.MyBaseDialogFragment;
import com.oneway.dialoglib.util.ResourcesUtils;
import com.oneway.dialoglib.widget.SmartTextView;

/**
 * 只有标题 和 确认 取消的对话框
 *
 * @param <T>
 */
public class BaseCommonDialog<T extends BaseCommonDialog.BaseCommonBuilder> extends MyBaseDialogFragment<T> {
    private static final String FTag = "dialogTag";
    private SmartTextView btnCancel;
    private AppCompatTextView btnConfirm;
    private LinearLayout mContainerLayout;

    protected int getLayoutRes() {
        return R.layout.lib_dialog_common_layout;
    }

    @Override
    protected void initView(View rooView) {
        SmartTextView tv = rooView.findViewById(R.id.tv_ui_title);
        tv.setText(mBuilder.titleStr);
        mContainerLayout = rooView.findViewById(R.id.ll_ui_container);
        if (getExtraViewRes() > 0) {
            View inflate = LayoutInflater.from(mContext).inflate(getExtraViewRes(), mContainerLayout, false);
            mContainerLayout.addView(inflate, 1);
        }
        //如果 negativeStr为null或空字符,btnCancel将不显示
        btnCancel = rooView.findViewById(R.id.tv_ui_cancel);
        btnCancel.setText(mBuilder.negativeStr);
        btnConfirm = rooView.findViewById(R.id.tv_ui_confirm);
        btnConfirm.setText(mBuilder.positiveStr);
        setOnClickListener(R.id.tv_ui_cancel, R.id.tv_ui_confirm);

    }

    /**
     * 如果需要添加额外的布局到 mContainerLayout中 重写该方法
     *
     * @return
     */
    public int getExtraViewRes() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_ui_cancel) {
            if (mBuilder.negativeBtnListener != null) {
                mBuilder.negativeBtnListener.onClick(this);
            } else {
                dismiss();
            }
        } else if (id == R.id.tv_ui_confirm) {
            if (mBuilder.positiveBtnListener != null) {
                mBuilder.positiveBtnListener.onClick(this);
            } else {
                dismiss();
            }
        }
    }

    /**
     * ****************************************************************************************************
     */
    /**
     * 当前dialog的builder
     */
    public static class Builder extends BaseCommonBuilder<Builder> {

        public Builder(Context context) {
            super(context);
        }

        @Override
        protected BaseCommonDialog create() {
            return new BaseCommonDialog();
        }
    }

    /**
     * 通用dialog的builder
     *
     * @param <T>
     */
    public abstract static class BaseCommonBuilder<T extends BaseBuilder> extends BaseBuilder<T> {
        public IDialog.OnClickListener positiveBtnListener;
        public IDialog.OnClickListener negativeBtnListener;
        public String titleStr;//默认标题
        public String positiveStr = ResourcesUtils.getString(mContext, R.string.common_confirm);//右边按钮文字
        public String negativeStr = ResourcesUtils.getString(mContext, R.string.common_cancel);//左边按钮文字


        public BaseCommonBuilder(Context context) {
            super(context);
        }

        /**
         * 设置右边按钮文字
         *
         * @param btnStr
         * @return
         */
        public T setPositiveButton(String btnStr) {
            this.positiveStr = btnStr;
            return (T) this;
        }
        /**
         * 设置默认右侧点击按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public T setPositiveButton(OnClickListener onclickListener) {
            return (T) setPositiveButton(ResourcesUtils.getString(mContext, R.string.common_confirm), onclickListener);
        }

        /**
         * 设置默认右侧点击按钮及文字
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public T setPositiveButton(String btnStr, OnClickListener onclickListener) {
            this.positiveBtnListener = onclickListener;
            this.positiveStr = btnStr;
            return (T) this;
        }

        /**
         * 设置左边按钮文字
         *
         * @param btnStr
         * @return
         */
        public T setNegativeButton(String btnStr) {
            this.negativeStr = btnStr;
            return (T) this;
        }
        /**
         * 设置左侧按钮点击该监听
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public T setNegativeButton(OnClickListener onclickListener) {
            return (T) setNegativeButton(ResourcesUtils.getString(mContext, R.string.common_cancel), onclickListener);
        }

        /**
         * 设置左侧文字及按钮监听
         *
         * @param btnStr          文字
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public T setNegativeButton(String btnStr, OnClickListener onclickListener) {
            this.negativeBtnListener = onclickListener;
            this.negativeStr = btnStr;
            return (T) this;
        }

        /**
         * 设置默认dialog的title
         *
         * @param title 标题
         * @return T
         */
        public T setTitle(String title) {
            this.titleStr = title;
            return (T) this;
        }


    }


}
