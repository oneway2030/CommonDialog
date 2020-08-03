package com.oneway.commondialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.dialog.BaseCommonDialog;
import com.oneway.dialoglib.dialog.BottomSheetDialog;
import com.oneway.dialoglib.dialog.InputDialog;
import com.oneway.dialoglib.dialog.MeesageDialog;
import com.oneway.dialoglib.in.DialogTextFormatter;
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

    public void showMyDialog(View view) {
        new BaseCommonDialog.Builder(this)
                .setTitle("提示")
                .setNegativeButton("", null)
//                .setCancelableOutSide(true)
//                .setCancelable(true)
                .show();
    }

    public void MessageDialog(View view) {
        new MeesageDialog.Builder(this)
                .setTitle("提示")
                .setMessage("这里是内容")
                .show();
    }

    public void InputDialog(View view) {
        new InputDialog.Builder(this)
                .setTitle("我是标题")
                .show();
    }

    private int defPosition=0;

    public void BottomSheetDialog(View view) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User("名字=>" + i, i));
        }
        new BottomSheetDialog.Builder(this)
                .setTitle("请选择路段")
                .setPositiveButton(new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        showToast("点击确定了");
                        dialog.dismiss();
                    }
                })
                .setItemSelectPosition(defPosition)
                .setItemSelectIcon(R.mipmap.ic_launcher)
                .itemTextColor(R.color.black5)
                .setList(list)
                .setItemTextFormatter((DialogTextFormatter<User>) item -> item.getName())
                .setOnItemClick(new OnItemClickListener<User>() {
                    @Override
                    public void onItemClick(IDialog dialog, User data, int position) {
                        defPosition=position;
                        showToast("点击的是 =>" + data.getAge() + "岁");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void BottomSheetDialogCustomTitle(View view) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new User("名字=>" + i, i));
        }
        new BottomSheetDialog.Builder(this)
//                .setTitle("请选择路段")
                .setCustomTitleView(R.layout.custom_title_view_layout)
                .setTitleViewCallback(new TitleViewCallback() {
                    @Override
                    public void onCallback(IDialog dialog, View titleView) {
                        titleView.findViewById(R.id.tv_ui_left).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setList(list)
                .setItemGravity(Gravity.LEFT)//默认中间
                .setItemTextFormatter((DialogTextFormatter<User>) item -> item.getName())
                .setOnItemClick(new OnItemClickListener<User>() {
                    @Override
                    public void onItemClick(IDialog dialog, User data, int position) {
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
