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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

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
        mTitleView = rooView.findViewById(R.id.tv_address_title);
        mCloseView = rooView.findViewById(R.id.iv_address_closer);
        mTabLayout = rooView.findViewById(R.id.tb_address_tab);
//        ArrayList<ArrayList<String>> list = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            ArrayList<String> list2 = new ArrayList<>();
//            for (int y = 0; y < 50; y++) {
//                list2.add("" + y);
//            }
//            list.add(list2);
//        }
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private int mPreviousScrollState, mScrollState = SCROLL_STATE_IDLE;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final boolean updateText = mScrollState != SCROLL_STATE_SETTLING || mPreviousScrollState == SCROLL_STATE_DRAGGING;
                final boolean updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE);
                mTabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                mPreviousScrollState = mScrollState;
                mScrollState = state;
                if (state == ViewPager2.SCROLL_STATE_IDLE && mTabLayout.getSelectedTabPosition() != mViewPager.getCurrentItem()) {
                    final boolean updateIndicator = mScrollState == SCROLL_STATE_IDLE || (mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE);
                    mTabLayout.selectTab(mTabLayout.getTabAt(mViewPager.getCurrentItem()), updateIndicator);
                }
            }
        });
        mAdapter = new TestAdapter(mViewPager,mTabLayout);
        mAdapter.addData(AddressManager.getProvinceList(mContext));
        mViewPager.setAdapter(mAdapter);
        setOnClickListener(mCloseView);
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.address_hint)), true);
        mTabLayout.addOnTabSelectedListener(this);
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

    public static final class AddressBean {

        /**
         * （省\市\区）的名称
         */
        private final String name;
        /**
         * 下一级的 Json
         */
        private final JSONObject next;

        private AddressBean(String name, JSONObject next) {
            this.name = name;
            this.next = next;
        }

        public String getName() {
            return name;
        }

        public JSONObject getNext() {
            return next;
        }
    }

    /**
     * 省市区数据管理类
     */
    public static final class AddressManager {

        /**
         * 获取省列表
         */
        private static List<AddressBean> getProvinceList(Context context) {
            try {
                // 省市区Json数据文件来源：https://github.com/getActivity/ProvinceJson
                JSONArray jsonArray = getProvinceJson(context);

                if (jsonArray != null) {

                    int length = jsonArray.length();
                    ArrayList<AddressBean> list = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(new AddressBean(jsonObject.getString("name"), jsonObject));
                    }

                    return list;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 获取城市列表
         *
         * @param jsonObject 城市Json
         */
        public static List<AddressBean> getCityList(JSONObject jsonObject) {
            try {
                JSONArray listCity = jsonObject.getJSONArray("city");
                int length = listCity.length();

                ArrayList<AddressBean> list = new ArrayList<>(length);

                for (int i = 0; i < length; i++) {
                    list.add(new AddressBean(listCity.getJSONObject(i).getString("name"), listCity.getJSONObject(i)));
                }

                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 获取区域列表
         *
         * @param jsonObject 区域 Json
         */
        private static List<AddressBean> getAreaList(JSONObject jsonObject) {
            try {
                JSONArray listArea = jsonObject.getJSONArray("area");
                int length = listArea.length();

                ArrayList<AddressBean> list = new ArrayList<>(length);

                for (int i = 0; i < length; i++) {
                    String string = listArea.getString(i);
                    list.add(new AddressBean(string, null));
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 获取资产目录下面文件的字符串
         */
        private static JSONArray getProvinceJson(Context context) {
            try {
                InputStream inputStream = context.getResources().openRawResource(R.raw.province);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, length);
                }
                outStream.close();
                inputStream.close();
                return new JSONArray(outStream.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
