package com.nullifier.commondialogwrapper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nullifier.dialog.CommonDialogWrapper;
import com.nullifier.dialog.DialogManager;
import com.nullifier.dialog.bean.CustomDialogBinderBean;
import com.nullifier.dialog.inter.ICommonDialogEventBinderListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICommonDialogEventBinderListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        List<CustomDialogBinderBean> eventBinderList = new ArrayList<CustomDialogBinderBean>();
        eventBinderList.add(new CustomDialogBinderBean(R.id.tv_red, "代码设置"));
        //设置图片类型的组件，并设置数据,支持网络数据
        eventBinderList.add(new CustomDialogBinderBean(R.id.iv_red, "" + R.mipmap.ic_launcher, this));

        CommonDialogWrapper commonDialogWrapper = new CommonDialogWrapper(this)
                .builderCustomDialog(R.layout.dialog_test, eventBinderList)//根据布局文件自己构造
                .bindData(new Bundle())//绑定的数据，会在事件点击处理时候得到
                .fullScreen();


        //根据系统自己写的dialog
        AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage("AAAA").setTitle("title").setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();


        CommonDialogWrapper needShowImmediately = new CommonDialogWrapper(alertDialog)
                .setShowTimes(3)//APP启动后总共可以显示多少次
                .isNeedShowImmediately(true);//高优先级

        DialogManager.getInstance().pushToQueue(commonDialogWrapper);
        DialogManager.getInstance().pushToQueue(needShowImmediately);
        DialogManager.getInstance().pushToQueue(needShowImmediately);
        DialogManager.getInstance().pushToQueue(needShowImmediately);
        DialogManager.getInstance().pushToQueue(needShowImmediately);
        DialogManager.getInstance().pushToQueue(needShowImmediately);
    }

    @Override
    public void OnClickListener(View yourView, CommonDialogWrapper commonDialogWrapper, Bundle bundle) {
        commonDialogWrapper.disMissDialog();
    }
}
