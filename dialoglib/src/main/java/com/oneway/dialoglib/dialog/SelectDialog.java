package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.adapter.SelectAdapter;
import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.in.DialogTextFormatter;
import com.oneway.dialoglib.in.OnSelectedListener;
import com.oneway.dialoglib.in.SimpleDialogTextFormatter;
import com.oneway.dialoglib.util.ResourcesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectDialog extends BaseCommonDialog<SelectDialog.Builder> implements BaseQuickAdapter.OnItemClickListener {

    private SelectAdapter mAdapter;

    @Override
    public int getExtraViewRes() {
        return R.layout.lib_select_dialog;
    }

    @Override
    protected void initView(View rooView) {
        super.initView(rooView);
        RecyclerView recyclerView = rooView.findViewById(R.id.rv_select_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(null);
        mAdapter = new SelectAdapter();
        mAdapter.setBuilder(mBuilder);
        mAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mBuilder.data);
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
                if (mBuilder.mOnSelectedListener != null) {
                    HashMap selectData = mAdapter.getSelectData();
                    if (selectData != null && !selectData.isEmpty()) {
                        mBuilder.mOnSelectedListener.onSelected(this, selectData);
                    } else {
                        Toast.makeText(mContext, String.format(ResourcesUtils.getString(mContext, R.string.select_min_hint), mBuilder.mMinSelect), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.updateCheck(position);
    }


    public static class Builder extends BaseCommonDialog.BaseCommonBuilder<SelectDialog.Builder> {
        public boolean isSingle = true;//是否是单选
        public DialogTextFormatter dialogTextFormatter = new SimpleDialogTextFormatter();//文本转换
        public List data;//列表数据
        public List<Integer> mSelectList = new ArrayList<>();//选中位置
        public int mMinSelect = 1;
        public int mMaxSelect = Integer.MAX_VALUE;//Integer.MAX_VALUE
        public OnSelectedListener mOnSelectedListener;//选中回调

        public Builder(Context context) {
            super(context);
            setScreenWidthP(0.8f);
//            setWidth(ScreenUtil.dp2px(context, 260));
        }

        @Override
        protected IDialog create() {
            return new SelectDialog();
        }

        /**
         * 是否单选
         */
        public Builder setSingle(boolean isSingle) {
            this.isSingle = isSingle;
            return this;
        }

        /**
         * 设置默认选中的位置
         */
        public Builder setSelect(int... positions) {
            mSelectList.clear();
            for (int position : positions) {
                mSelectList.add(position);
            }
            return this;
        }

        /**
         * 设置最大选择数量
         */
        public Builder setMaxSelect(int maxCount) {
            this.mMaxSelect = maxCount;
            return this;
        }

        /**
         * 设置最小选择数量
         */
        public Builder setMinSelect(int minCount) {
            this.mMinSelect = minCount;
            return this;
        }

        public <T> Builder setOnSelectedListener(OnSelectedListener<T> selectedListener) {
            this.mOnSelectedListener = selectedListener;
            return this;
        }

        /**
         * 设置数据
         */
        public <T> Builder setList(@NonNull List<T> list) {
            this.data = list;
            return this;
        }

        /**
         * 设置条目 格式化文字
         */
        public <T> Builder setItemTextFormatter(DialogTextFormatter<T> spinnerTextFormatter) {
            this.dialogTextFormatter = spinnerTextFormatter;
            return this;
        }
    }
}
