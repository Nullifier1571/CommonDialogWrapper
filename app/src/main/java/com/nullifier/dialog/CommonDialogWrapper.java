package com.nullifier.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.nullifier.commondialogwrapper.R;
import com.nullifier.dialog.bean.CustomDialogBinderBean;
import com.nullifier.dialog.inter.ICommonDialogEventBinderListener;

import java.util.List;

public class CommonDialogWrapper implements DialogInterface.OnDismissListener {
    private Dialog mDialog = null;
    private boolean mIsNeedShowImmediately;
    private int mShowTimes;
    private Bundle mBundle;

    public CommonDialogWrapper(Dialog dialog) {
        if (dialog == null) {
            throw new RuntimeException("dialog==null");
        }
        this.mDialog = dialog;
        mDialog.setOnDismissListener(this);
    }

    public CommonDialogWrapper(Context context) {
        if (context == null) {
            throw new RuntimeException("context==null");
        }

        this.mDialog = new Dialog(context, R.style.QuitDialog);
        mDialog.setOnDismissListener(this);
    }

    public boolean isNeedShowImmediately() {
        return mIsNeedShowImmediately;
    }

    public void showDialog() {
        mDialog.show();
    }

    public void disMissDialog() {
        mDialog.dismiss();
    }

    public boolean isDialogShowing() {
        return mDialog.isShowing();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogManager.getInstance().startNextIf();
    }


    /*------------------------------------------公共方法start-----------------------------------------------------------*/

    /***
     * 设置dialog弹出的的次数,默认0不限制弹出次数
     * @param showTimes
     * @return
     */
    public CommonDialogWrapper setShowTimes(int showTimes) {
        this.mShowTimes = showTimes < 0 ? 0 : showTimes;
        return this;
    }

    public int getShowTimes() {
        return this.mShowTimes;
    }

    public String getClassName() {
        return this.mDialog.getClass().getName();
    }

    public CommonDialogWrapper isNeedShowImmediately(boolean isNeedShowImmediately) {
        this.mIsNeedShowImmediately = isNeedShowImmediately;
        return this;
    }

    public CommonDialogWrapper setCanDismissOutside(boolean isCanceledOnTouchOutside) {
        mDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        return this;
    }

    public CommonDialogWrapper bindData(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    public CommonDialogWrapper fullScreen() {
        Window win = mDialog.getWindow();
        win.setBackgroundDrawableResource(android.R.color.transparent);//去除dialog黑边
        win.getDecorView().setPadding(0, 0, 0, 0);//dialog默认有padding，需设置为0，否则无法全屏
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        transparencyBar();
        return this;
    }

    @TargetApi(19)
    private void transparencyBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = mDialog.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /*------------------------------------------自定义样式start-----------------------------------------------------------*/

    public CommonDialogWrapper builderCustomDialog(int layoutResID, List<CustomDialogBinderBean> eventBinderList) {
        if (layoutResID == 0) {
            return this;
        }
        mDialog.setContentView(layoutResID);
        if (eventBinderList != null && !eventBinderList.isEmpty()) {
            for (CustomDialogBinderBean customDialogBinderBean : eventBinderList) {
                final ICommonDialogEventBinderListener listener = customDialogBinderBean.listener;
                final int resId = customDialogBinderBean.resId;
                String textString = customDialogBinderBean.textString;
                if (resId != 0) {
                    final View view = mDialog.findViewById(resId);
                    //如果有Listener就添加点击回调
                    if (listener != null && view != null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.OnClickListener(v, CommonDialogWrapper.this, mBundle);
                            }
                        });
                    }
                    //如果是textView并且有传来的字符串则添加
                    if (view instanceof TextView && !TextUtils.isEmpty(textString)) {
                        ((TextView) view).setText(textString);
                    }

                    //如果是textView并且有传来的字符串则添加
                    if (view instanceof SimpleDraweeView && !TextUtils.isEmpty(textString)) {
                        if (TextUtils.isDigitsOnly(textString)) {
                            ((SimpleDraweeView) view).setImageResource(Integer.parseInt(textString));
                        }
                        if (textString.startsWith("https") || textString.startsWith("http")) {
                            ((SimpleDraweeView) view).setImageURI(textString);
                        }
                    }
                }
            }
        }
        return this;
    }

}
