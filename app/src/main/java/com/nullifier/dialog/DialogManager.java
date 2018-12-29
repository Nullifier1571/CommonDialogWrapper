package com.nullifier.dialog;

import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;


/***
 * create by nullifier on on 18/11/21
 */
public class DialogManager {

    private static final String TAG = "DialogManager";
    private LinkedList<CommonDialogWrapper> mDialogQueue = new LinkedList<CommonDialogWrapper>();//dialog的队列
    private HashMap<String, Integer> mDialogTimesQueue = new HashMap<String, Integer>();
    private static DialogManager sInstance;
    private CommonDialogWrapper mCurrentShowDialog;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (sInstance == null) {
            synchronized (DialogManager.class) {
                if (sInstance == null) {
                    sInstance = new DialogManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * 每次弹窗调用PushQueue方法
     *
     * @param dialogBase
     */
    public void pushToQueue(CommonDialogWrapper dialogBase) {
        Log.d(TAG, "dialog进入");
        //添加到队列中
        if (dialogBase != null) {
            Log.e(TAG, "add..");
            if (dialogBase.isNeedShowImmediately()) {
                Log.d(TAG, "高优先级dialog进入");
                mDialogQueue.addFirst(dialogBase);
            } else if (dialogBase.getShowTimes() != 0) {
                int times = mDialogTimesQueue.containsKey(dialogBase.getClassName()) ?
                        mDialogTimesQueue.get(dialogBase.getClassName()) : 0;
                if (times < dialogBase.getShowTimes()) {
                    mDialogTimesQueue.put(dialogBase.getClassName(), ++times);
                    mDialogQueue.add(dialogBase);
                }
            } else {
                Log.d(TAG, "普通优先级dialog进入");
                mDialogQueue.add(dialogBase);
                //只有当前队列数量为1时才能进行下一步操作
            }

            if (mCurrentShowDialog == null || !mCurrentShowDialog.isDialogShowing()) {
                Log.d(TAG, "dialog显示进入");
                startNextIf();
            }
        }
    }


    /**
     * 显示下一个弹窗任务
     */
    public void startNextIf() {
        if (mDialogQueue != null && !mDialogQueue.isEmpty()) {
            mCurrentShowDialog = mDialogQueue.removeFirst();
            if (mCurrentShowDialog != null) {
                mCurrentShowDialog.showDialog();
            } else {
                Log.e(TAG, "任务队列为空...");
            }
        }
    }


    /**
     * 清除对象
     */
    public void clear() {
        mDialogQueue.clear();
        if (mCurrentShowDialog != null) {
            mCurrentShowDialog.disMissDialog();
            mCurrentShowDialog = null;
        }
    }

}
