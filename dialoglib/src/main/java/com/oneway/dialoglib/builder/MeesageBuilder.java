//package com.oneway.dialoglib.builder;
//
//import android.content.Context;
//
//import com.oneway.dialoglib.dialog.MeesageDialog;
//import com.oneway.dialoglib.dialog.MyDialog;
//
//public class MeesageBuilder extends MyDialog.MyBuilder<MeesageBuilder, MeesageDialog> {
//    public String contentStr;//默认内容
//
//    public MeesageBuilder(Context context) {
//        super(context);
//
//    }
//
//    @Override
//    protected MeesageDialog create() {
//        return new MeesageDialog();
//    }
//
//    /**
//     * TODO
//     * 设置默认dialog的内容
//     *
//     * @param messageStr 内容
//     * @return T
//     */
//    public MeesageBuilder setMessage(String messageStr) {
//        this.contentStr = messageStr;
//        return this;
//    }
//}
