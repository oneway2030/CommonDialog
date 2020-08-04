package com.oneway.dialoglib.adapter;

import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.dialog.SelectDialog;
import com.oneway.dialoglib.util.ResourcesUtils;

import java.util.HashMap;

public class SelectAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private SelectDialog.Builder mBuilder;


    public SelectAdapter() {
        super(R.layout.lib_item_select, null);
    }

    public void setBuilder(SelectDialog.Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    /**
     * 获取选中的数据
     *
     * @return
     */
    public HashMap<Integer, T> getSelectData() {
        HashMap<Integer, T> map = new HashMap<>();
        for (Integer position : mBuilder.mSelectList) {
            map.put(position, getItem(position));
        }
        return map;
    }

    /**
     * 校验是否选中
     *
     * @param position
     */
    public void updateCheck(int position) {
        if (mBuilder.isSingle) {//单选
            if (!mBuilder.mSelectList.contains(position)) {//不包含这条数据
                mBuilder.mSelectList.clear();
                mBuilder.mSelectList.add(position);
            }
            notifyDataSetChanged();
        } else {//多选
            if (mBuilder.mSelectList.contains(position)) {//包含这条数据,取消选中
                mBuilder.mSelectList.remove(Integer.valueOf(position));
                notifyItemChanged(position);
            } else {//不包含这条数据,选中
                if (mBuilder.mSelectList.size() < mBuilder.mMaxSelect) {
                    mBuilder.mSelectList.add(position);
                    notifyItemChanged(position);
                } else {//超过最大选中数量
                    Toast.makeText(mContext, String.format(ResourcesUtils.getString(mContext, R.string.select_max_hint), mBuilder.mMaxSelect), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (mBuilder.dialogTextFormatter != null && item != null) {
            helper.setText(R.id.tv_select_text, mBuilder.dialogTextFormatter.format(item));
        }
        helper.setImageResource(R.id.iv_select_checkbox, getIcon(helper.getLayoutPosition()));

    }

    public int getIcon(int position) {
        if (mBuilder.mSelectList.contains(position)) {
            if (mBuilder.selectRes == 0) {
                return R.drawable.icon_checkbox_checked;
            } else {
                return mBuilder.selectRes;
            }
        } else {
            if (mBuilder.unSelectRes == 0) {
                return R.drawable.icon_checkbox_normal;
            } else {
                return mBuilder.unSelectRes;
            }
        }
    }


}