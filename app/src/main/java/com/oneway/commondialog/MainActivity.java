package com.oneway.commondialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oneway.dialoglib.base.IDialog;
import com.oneway.dialoglib.dialog.BaseCommonDialog;
import com.oneway.dialoglib.dialog.InputDialog;
import com.oneway.dialoglib.dialog.MeesageDialog;

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
                //                .setTitle("提示")
                .setMessage("这里是内容")
//                .setOnDismissListener(new IDialog.OnDismissListener() {
//                    @Override
//                    public void onDismiss(IDialog dialog) {
//                        Toast.makeText(MainActivity.this,"setOnDismissListener",Toast.LENGTH_LONG).show();
//                    }
//                })
                .setNegativeButton(new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        Toast.makeText(MainActivity.this, "setNegativeButton", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton(new IDialog.OnClickListener() {
                    @Override
                    public void onClick(IDialog dialog) {
                        Toast.makeText(MainActivity.this, "setPositiveButton", Toast.LENGTH_LONG).show();
                    }
                })
                .setAutoDismiss(false)
                .show();
    }

    public void InputDialog(View view) {
        new InputDialog.Builder(this)
                .setTitle("我是标题")
                .show();
    }
}
