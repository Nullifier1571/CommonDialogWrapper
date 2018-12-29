package com.nullifier.dialog.inter;

import android.os.Bundle;
import android.view.View;

import com.nullifier.dialog.CommonDialogWrapper;


public interface ICommonDialogEventBinderListener {
    /**
     * @param yourView            响应点击事件的View
     * @param commonDialogWrapper 该组件所在的dialog
     * @param bundle              dialog所绑定的数据源
     */
    void OnClickListener(View yourView, CommonDialogWrapper commonDialogWrapper, Bundle bundle);
}
