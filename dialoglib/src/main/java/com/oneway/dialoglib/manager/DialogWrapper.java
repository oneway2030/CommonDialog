package com.oneway.dialoglib.manager;


import com.oneway.dialoglib.base.BaseBuilder;

/**
 * 管理多个dialog 按照dialog的优先级依次弹出
 * Created by mq on 2018/9/16 下午9:44
 * mqcoder90@gmail.com
 */

public class DialogWrapper {

    private BaseBuilder dialog;//统一管理dialog的弹出顺序

    public DialogWrapper(BaseBuilder dialog) {
        this.dialog = dialog;
    }

    public BaseBuilder getDialog() {
        return dialog;
    }

    public void setDialog(BaseBuilder dialog) {
        this.dialog = dialog;
    }

}
