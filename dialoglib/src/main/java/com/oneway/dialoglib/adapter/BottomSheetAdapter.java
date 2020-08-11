package com.oneway.dialoglib.adapter;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.in.DialogTextFormatter;
import com.oneway.dialoglib.in.OnConvertItemListener;
import com.oneway.dialoglib.util.ResourcesUtils;

public class BottomSheetAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    private DialogTextFormatter dialogTextFormatter;
    private int itemTextGravity;
    private int defSelectPosition = -99;
    private int itemTextColor = -99;
    private int itemIconRes = -99;
    private OnConvertItemListener mListener;

    public BottomSheetAdapter(@LayoutRes int layoutResId, OnConvertItemListener listener) {
        super(layoutResId, null);
        this.mListener = listener;
    }

    public BottomSheetAdapter() {
        super(R.layout.lib_item_bottom_sheet, null);
    }

    public void setTextGravity(int itemTextGravity) {
        this.itemTextGravity = itemTextGravity;
    }

    public void setTextFormatter(DialogTextFormatter dialogTextFormatter) {
        this.dialogTextFormatter = dialogTextFormatter;
    }

    public void setSelectPosition(int defSelectPosition) {
        this.defSelectPosition = defSelectPosition;
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    public void setItemSelectIcon(@DrawableRes int itemIconRes) {
        this.itemIconRes = itemIconRes;
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        if (mListener == null) {//默认布局 lib_item_bottom_sheet
            helper.setText(R.id.tv_menu_text, dialogTextFormatter.format(item));
            AppCompatTextView tv = helper.getView(R.id.tv_menu_text);
            tv.setGravity(itemTextGravity);
            helper.setGone(R.id.iv_icon, defSelectPosition == helper.getLayoutPosition());
            if (itemTextColor > 0) {
                helper.setTextColor(R.id.tv_menu_text, ResourcesUtils.getColor(getContext(), itemTextColor));
            }
            if (itemIconRes > 0) {
                helper.setImageResource(R.id.iv_icon, itemIconRes);
            }
        } else {
            mListener.conver(helper, item, defSelectPosition);
        }

    }


}
