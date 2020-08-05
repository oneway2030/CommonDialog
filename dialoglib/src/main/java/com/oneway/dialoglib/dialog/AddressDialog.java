package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.oneway.dialoglib.R;
import com.oneway.dialoglib.adapter.TestAdapter;
import com.oneway.dialoglib.base.BaseBuilder;
import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.base.MyBaseDialogFragment;

import java.util.ArrayList;

public class AddressDialog extends MyBaseDialogFragment<AddressDialog.Builder> implements TabLayout.OnTabSelectedListener {



    private TextView mTitleView;
    private ImageView mCloseView;
    private TabLayout mTabLayout;

    private ViewPager2 mViewPager;
    private ViewPager2.OnPageChangeCallback mCallback;
    private TestAdapter mAdapter;

    private String mProvince = null;
    private String mCity = null;
    private String mArea = null;

    @Override
    protected int getLayoutRes() {
        return R.layout.address_dialog;
    }

    @Override
    protected void initView(View rooView) {
        mViewPager = rooView.findViewById(R.id.vp_address_province);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("" + i);
        }
        mAdapter = new TestAdapter();
        mAdapter.setNewData(list);
        mViewPager.setAdapter(mAdapter);
        mTitleView = rooView.findViewById(R.id.tv_address_title);
        mCloseView = rooView.findViewById(R.id.iv_address_closer);
        mTabLayout = rooView.findViewById(R.id.tb_address_tab);
        setOnClickListener(mCloseView);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.address_hint)), true);
        //TODO
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        synchronized (this) {
            if (mViewPager.getCurrentItem() != tab.getPosition()) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            tab.setText(getString(R.string.address_hint));
            switch (tab.getPosition()) {
                case 0:
                    mProvince = mCity = mArea = null;
                    if (mTabLayout.getTabAt(2) != null) {
                        mTabLayout.removeTabAt(2);
                        mAdapter.remove(2);
                    }
                    if (mTabLayout.getTabAt(1) != null) {
                        mTabLayout.removeTabAt(1);
                        mAdapter.remove(1);
                    }
                    break;
                case 1:
                    mCity = mArea = null;
                    if (mTabLayout.getTabAt(2) != null) {
                        mTabLayout.removeTabAt(2);
                        mAdapter.remove(2);
                    }
                    break;
                case 2:
                    mArea = null;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public static class Builder extends BaseBuilder<AddressDialog.Builder> {

        public Builder(Context context) {
            super(context);
            setScreenHeightP(0.6f);
            setScreenWidthP(1f);
            setGravity(Gravity.BOTTOM);
        }

        @Override
        protected IDialog create() {
            return new AddressDialog();
        }
    }
}
