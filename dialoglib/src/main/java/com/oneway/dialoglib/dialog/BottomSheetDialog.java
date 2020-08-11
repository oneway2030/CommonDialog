package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.adapter.BottomSheetAdapter;
import com.oneway.dialoglib.base.BaseBuilder;
import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.base.MyBaseDialogFragment;
import com.oneway.dialoglib.in.DialogTextFormatter;
import com.oneway.dialoglib.in.OnConvertItemListener;
import com.oneway.dialoglib.in.OnDialogItemClickListener;
import com.oneway.dialoglib.in.SimpleDialogTextFormatter;
import com.oneway.dialoglib.in.TitleViewCallback;
import com.oneway.dialoglib.util.ResourcesUtils;

import java.util.List;

public class BottomSheetDialog extends MyBaseDialogFragment<BottomSheetDialog.Builder> implements OnItemClickListener {

    private RecyclerView mRecy;
    private AppCompatTextView btnConfirm;
    private AppCompatTextView btnCancel;
    private BottomSheetAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.lib_bottom_sheet_dialog;
    }

    @Override
    protected void initView(View rooView) {
        LinearLayout llTitleLayout = rooView.findViewById(R.id.ll_title_layout);
        if (mBuilder.customTitleView > 0) {//自定义标题
            View titleView = LayoutInflater.from(mContext).inflate(mBuilder.customTitleView, llTitleLayout, false);
            llTitleLayout.addView(titleView);
            if (mBuilder.mTitleViewCallback != null) {
                mBuilder.mTitleViewCallback.onCallback(this, titleView);
            }
        } else {
            View titleView = LayoutInflater.from(mContext).inflate(R.layout.lib_title_layout, llTitleLayout, false);
            llTitleLayout.addView(titleView);
            AppCompatTextView tvTitle = rooView.findViewById(R.id.tv_ui_title);
            btnCancel = rooView.findViewById(R.id.tv_ui_cancel);
            btnConfirm = rooView.findViewById(R.id.tv_ui_confirm);
            tvTitle.setText(mBuilder.titleStr);
            btnCancel.setText(mBuilder.negativeStr);
            btnConfirm.setText(mBuilder.positiveStr);
            setOnClickListener(R.id.tv_ui_cancel, R.id.tv_ui_confirm);
        }
        mRecy = rooView.findViewById(R.id.recy);
        //
        mRecy.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBuilder.adapterlayoutResId > 0) {
            mAdapter = new BottomSheetAdapter(mBuilder.adapterlayoutResId, mBuilder.mOnConvertItemListener);
        } else {
            mAdapter = new BottomSheetAdapter();
        }
        //设置文本位置
        mAdapter.setTextGravity(mBuilder.itemTextGravity);
        //设置文本格式化
        if (mBuilder.dialogTextFormatter != null) {
            mAdapter.setTextFormatter(mBuilder.dialogTextFormatter);
        }
        if (mBuilder.defSelectPosition >= 0) {//默认位置
            mAdapter.setSelectPosition(mBuilder.defSelectPosition);
        }
        if (mBuilder.itemTextColor >= 0) {//文本颜色
            mAdapter.setItemTextColor(mBuilder.itemTextColor);
        }
        if (mBuilder.itemIconRes >= 0) {//默认选中图标
            mAdapter.setItemSelectIcon(mBuilder.itemIconRes);
        }
        mRecy.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setNewData(mBuilder.data);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mBuilder.onItemClickListener != null) {
            mBuilder.onItemClickListener.onItemClick(this, adapter.getData().get(position), position);
        }
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
     * *******************************************************************************************
     */
    public static class Builder extends BaseBuilder<BottomSheetDialog.Builder> {
        public IDialog.OnClickListener positiveBtnListener;
        public IDialog.OnClickListener negativeBtnListener;
        public String titleStr;//默认标题
        public String positiveStr = ResourcesUtils.getString(mContext, R.string.common_confirm);//右边按钮文字
        public String negativeStr = ResourcesUtils.getString(mContext, R.string.common_cancel);//左边按钮文字
        private DialogTextFormatter dialogTextFormatter = new SimpleDialogTextFormatter();//文本转换
        public int customTitleView; // 是自定义标题view
        private TitleViewCallback mTitleViewCallback;// 是自定义标题 设置回调
        private int defSelectPosition = -99;//默认位置
        private int itemTextColor = -1;//设置item 文本颜色
        private int itemIconRes = -1;//设置item icon
        public int itemTextGravity = Gravity.CENTER;//设置item 文本位置
        private List data;//列表数据
        public int adapterlayoutResId;//自定义adapter 布局id
        private OnConvertItemListener mOnConvertItemListener;//自定义adapter 布局设置监听
        private OnDialogItemClickListener onItemClickListener;//item点击监听

        public Builder(Context context) {
            super(context);
            setScreenWidthP(1.0f);
            setGravity(Gravity.BOTTOM);
        }

        @Override
        protected BottomSheetDialog create() {
            return new BottomSheetDialog();
        }

        /**
         * 设置右边按钮文字
         *
         * @param btnStr
         * @return
         */
        public Builder setPositiveButton(String btnStr) {
            this.positiveStr = btnStr;
            return this;
        }
        /**
         * 设置默认右侧点击按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public Builder setPositiveButton(OnClickListener onclickListener) {
            return setPositiveButton(ResourcesUtils.getString(mContext, R.string.common_confirm), onclickListener);
        }

        /**
         * 设置默认右侧点击按钮及文字
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public Builder setPositiveButton(String btnStr, OnClickListener onclickListener) {
            this.positiveBtnListener = onclickListener;
            this.positiveStr = btnStr;
            return this;
        }

        /**
         * 设置左边按钮文字
         *
         * @param btnStr
         * @return
         */
        public Builder setNegativeButton(String btnStr) {
            this.negativeStr = btnStr;
            return this;
        }
        /**
         * 设置左侧按钮
         *
         * @param onclickListener IDialog.OnClickListener
         * @return T
         */
        public Builder setNegativeButton(OnClickListener onclickListener) {
            return setNegativeButton(ResourcesUtils.getString(mContext, R.string.common_cancel), onclickListener);
        }


        /**
         * 设置左侧文字及按钮
         *
         * @param btnStr          文字
         * @param onclickListener IDialog.OnClickListener
         * @return Builder
         */
        public Builder setNegativeButton(String btnStr, OnClickListener onclickListener) {
            this.negativeBtnListener = onclickListener;
            this.negativeStr = btnStr;
            return this;
        }

        /**
         * 设置默认dialog的title
         *
         * @param title 标题
         * @return Builder
         */
        public Builder setTitle(String title) {
            this.titleStr = title;
            return this;
        }

        public <T> Builder setList(@NonNull List<T> list) {
            this.data = list;
            return this;
        }


        /**
         * 监听条目点击回调
         *
         * @param onItemClickListener
         * @return
         */
        public <T> Builder setOnItemClick(OnDialogItemClickListener<T> onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }


        /**
         * 自定义标题
         */
        public Builder setCustomTitleView(int customTitleView) {
            this.customTitleView = customTitleView;
            return this;
        }

        /**
         * 自定标题布局的回调
         *
         * @param titleViewCallback
         * @return
         */
        public Builder setTitleViewCallback(TitleViewCallback titleViewCallback) {
            this.mTitleViewCallback = titleViewCallback;
            return this;
        }

        /**
         * 设置条目 格式化文字
         */
        public <T> Builder setItemTextFormatter(DialogTextFormatter<T> spinnerTextFormatter) {
            this.dialogTextFormatter = spinnerTextFormatter;
            return this;
        }

        /**
         * 设置条目 文本的中心Gravity
         *
         * @param gravity Gravity
         */
        public Builder setItemGravity(int gravity) {
            this.itemTextGravity = gravity;
            return this;
        }

        /**
         * 设置条目 默认选中位置
         */
        public Builder setItemSelectPosition(int defSelectPosition) {
            this.defSelectPosition = defSelectPosition;
            return this;
        }

        /**
         * 设置条目 文本颜色
         */
        public Builder itemTextColor(@ColorRes int itemTextColor) {
            this.itemTextColor = itemTextColor;
            return this;
        }

        /**
         * 设置条目 自定义选中图标
         */
        public Builder setItemSelectIcon(int itemIconRes) {
            this.itemIconRes = itemIconRes;
            return this;
        }

        /**
         * 设置条目 自定义 adapter 布局
         *
         * @param adapterlayoutResId
         * @return
         */
        public Builder customAdapterlayoutResId(int adapterlayoutResId) {
            this.adapterlayoutResId = adapterlayoutResId;
            return this;
        }

        /**
         * 设置条目  自定义回调设置
         *
         * @param mOnConvertItemListener
         * @return
         */
        public <T> Builder setConvertItemListener(OnConvertItemListener<T> mOnConvertItemListener) {
            this.mOnConvertItemListener = mOnConvertItemListener;
            return this;
        }

    }


}
