package com.oneway.dialoglib.adapter;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.dialog.AddressDialog;

import java.util.List;

public class TestAdapter extends BaseQuickAdapter<List<AddressDialog.AddressBean>, BaseViewHolder> implements OnItemClickListener {

    private ViewPager2 mViewPager;
    private AddressListAdapter mAdapter;
    private TabLayout mTabLayout;

    private String mProvince = null;
    private String mCity = null;
    private String mArea = null;

    public TestAdapter(ViewPager2 mViewPager, TabLayout mTabLayout) {
        super(R.layout.lib_vp_item_address, null);
        this.mViewPager = mViewPager;
        this.mTabLayout = mTabLayout;
    }

    @Override
    protected void convert(BaseViewHolder helper, List<AddressDialog.AddressBean> item) {
        int parentPosition = helper.getAdapterPosition();
        RecyclerView recy = helper.getView(R.id.recy);
        recy.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AddressListAdapter();
        recy.setAdapter(mAdapter);
        mAdapter.setNewData(item);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (parentPosition) {
                    case 0:
                        // 记录当前选择的省份
                        AddressDialog.AddressBean item = (AddressDialog.AddressBean) adapter.getData().get(position);
                        addData(AddressDialog.AddressManager.getCityList(item.getNext()));
                        mProvince = getItem(parentPosition).get(position).getName();
                        mTabLayout.getTabAt(mTabLayout.getSelectedTabPosition()).setText(mProvince);
                        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.address_hint), true);
//                mAdapter.addItem(AddressDialog.AddressManager.getCityList(mAdapter.getItem(recyclerViewPosition).get(clickItemPosition).getNext()));
                        mViewPager.setCurrentItem(1);
                        // 如果当前选择的是直辖市，就直接跳过选择城市，直接选择区域
                        if (getItem(1).size() == 1) {
//                    onSelected(recyclerViewPosition + 1, 0);
                        }
                        break;
                    case 1:
                        break;
                    default:
                        break;
                }
            }
        });
//        helper.setText(R.id.tv_menu_text, "测试=========");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 0:

                break;
            case 1:
                break;
            default:
                break;
        }
    }
}
