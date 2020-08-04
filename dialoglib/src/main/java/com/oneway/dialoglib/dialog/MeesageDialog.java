package com.oneway.dialoglib.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.oneway.dialoglib.R;
import com.oneway.dialoglib.util.ScreenUtil;

import static com.oneway.dialoglib.dialog.MeesageDialog.Builder;

public class MeesageDialog extends BaseCommonDialog<Builder> {
    private TextView mMessageView;

    @Override
    public int getExtraViewRes() {
        return R.layout.message_dialog;
    }

    @Override
    protected void initView(View rooView) {
        super.initView(rooView);
        mMessageView = rooView.findViewById(R.id.tv_message_message);
        mMessageView.setText(builder.contentStr);
    }

    public static class Builder extends BaseCommonDialog.BaseCommonBuilder<Builder> {
        public String contentStr;//默认内容

        public Builder(Context context) {
            super(context);
            setWidth(ScreenUtil.dp2px(context, 260));
        }

        @Override
        protected MeesageDialog create() {
            return new MeesageDialog();
        }

        /**
         * 设置默认dialog的内容
         */
        public Builder setMessage(String messageStr) {
            this.contentStr = messageStr;
            return this;
        }
    }
}
