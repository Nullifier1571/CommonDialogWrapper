package com.nullifier.dialog.bean;


import com.nullifier.dialog.inter.ICommonDialogEventBinderListener;

public class CustomDialogBinderBean {
    public CustomDialogBinderBean(int resId, String textString) {
        this.resId = resId;
        this.textString = textString;
    }

    public CustomDialogBinderBean(int resId, String textString, ICommonDialogEventBinderListener listener) {
        this.resId = resId;
        this.textString = textString;
        this.listener = listener;
    }

    public int resId;
    public String textString;
    public ICommonDialogEventBinderListener listener;
}
