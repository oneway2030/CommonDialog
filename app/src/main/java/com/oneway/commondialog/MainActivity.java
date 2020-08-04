package com.oneway.commondialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.dialog.BaseCommonDialog;
import com.oneway.dialoglib.dialog.BottomSheetDialog;
import com.oneway.dialoglib.dialog.InputDialog;
import com.oneway.dialoglib.dialog.MeesageDialog;
import com.oneway.dialoglib.in.DialogTextFormatter;
import com.oneway.dialoglib.in.OnConvertItemListener;
import com.oneway.dialoglib.in.OnItemClickListener;
import com.oneway.dialoglib.in.TitleViewCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * ios风格的 基础dialog
     */
    public void showCommonDialog(View view) {
        new BaseCommonDialog.Builder(this)
                .setTitle("提示")
                .setNegativeButton("", null)
//                .setCancelableOutSide(true)
//                .setCancelable(true)
                .show();
    }

    /**
     * 消息对话框(基于 BaseCommonDialog)
     */
    public void MessageDialog(View view) {
        new MeesageDialog.Builder(this)
                .setTitle("提示")
                .setMessage("这里是内容")
                .show();
    }

    /**
     * 有一个编辑框的dialog (基于 BaseCommonDialog)
     */
    public void InputDialog(View view) {
        new InputDialog.Builder(this)
                .setTitle("我是标题")
                .show();
    }

    private int defPosition=0;

    /**
     * 默认底部列表弹窗
     */
    public void BottomSheetDialog(View view) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User("名字=>" + i, i));
        }
        new BottomSheetDialog.Builder(this)
                .setTitle("请选择路段")//设置标题
                .setPositiveButton(dialog -> {//监听确定点击
                    showToast("点击确定了");
                    dialog.dismiss();
                })
                .setItemSelectPosition(defPosition)
                .setItemSelectIcon(R.mipmap.ic_launcher)
                .itemTextColor(R.color.black95)
                .setList(list)
                .setItemTextFormatter((DialogTextFormatter<User>) item -> item.getName())
                .setOnItemClick((OnItemClickListener<User>) (dialog, data, position) -> {
                    defPosition = position;
                    showToast("点击的是 =>" + data.getAge() + "岁");
                    dialog.dismiss();
                })
                .show();
    }

    /**
     * 自定义标题和 adapter
     */
    public void BottomSheetDialogCustom(View view) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User("名字=>" + i, i));
        }
        new BottomSheetDialog.Builder(this)
                //TODO 自定义标题
                .setCustomTitleView(R.layout.lib_title_layout)
                .setTitleViewCallback(new TitleViewCallback() {
                    @Override
                    public void onCallback(IDialog dialog, View titleView) {
                        titleView.findViewById(R.id.tv_ui_cancel).setOnClickListener(v -> dialog.dismiss());
                    }
                })
                .setItemSelectPosition(defPosition)
                //TODO 自定义adapter样例,如果不自定义会使用默认设置
                .customAdapterlayoutResId(R.layout.lib_item_bottom_sheet)
                .setConvertItemListener((OnConvertItemListener<User>) (helper, data, defSelectposition) -> {
                    helper.setText(R.id.tv_menu_text, data.getName());
                    AppCompatTextView tv = helper.getView(com.oneway.dialoglib.R.id.tv_menu_text);
                    tv.setGravity(Gravity.CENTER);
                    helper.setGone(com.oneway.dialoglib.R.id.iv_icon, defPosition == helper.getLayoutPosition());
                })
                //设置数据
                .setList(list)
                .setOnItemClick(new OnItemClickListener<User>() {
                    @Override
                    public void onItemClick(IDialog dialog, User data, int position) {
                        defPosition = position;
                        showToast("点击的是 =>" + data.getAge() + "岁");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
