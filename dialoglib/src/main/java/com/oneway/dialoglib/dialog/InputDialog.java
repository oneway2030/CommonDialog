package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.oneway.dialoglib.R;
import com.oneway.dialoglib.util.ScreenUtil;

public class InputDialog extends BaseCommonDialog<InputDialog.Builder> {

    private EditText mInputView;

    @Override
    public int getExtraViewRes() {
        return R.layout.lib_input_dialog;
    }

    @Override
    protected void initView(View rooView) {
        super.initView(rooView);
        mInputView = rooView.findViewById(R.id.tv_input_message);
    }

    @Override
    public void onResume() {
        super.onResume();
        //弹出然键盘
        mInputView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ContextCompat.getSystemService(getContext(), InputMethodManager.class).showSoftInput(mInputView, 0);
            }
        }, 300);
    }

    public static class Builder extends BaseCommonDialog.BaseCommonBuilder<Builder, InputDialog> {
        public String contentStr;//默认内容

        public Builder(Context context) {
            super(context);
            setWidth(ScreenUtil.dp2px(context, 260));
        }

        @Override
        protected InputDialog create() {
            return new InputDialog();
        }

        /**
         * 设置默认dialog的内容
         */
        public InputDialog.Builder setMessage(String messageStr) {
            this.contentStr = messageStr;
            return this;
        }
    }
}
