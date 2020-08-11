package com.oneway.dialoglib.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.dialog.AddressDialog;

public class AddressListAdapter extends BaseQuickAdapter<AddressDialog.AddressBean, BaseViewHolder> {

    public AddressListAdapter() {
        super(R.layout.lib_vp_item_list_address, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressDialog.AddressBean item) {
            helper.setText(R.id.tv,item.getName());
    }
}
