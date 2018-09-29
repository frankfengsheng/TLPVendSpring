package com.tcn.uicommon.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 描述：
 * 作者：hua on 2017/2/5 15:20
 */

public class UsbProgressDialog {
    Context context;
    ProgressDialog pd;
    public UsbProgressDialog(Context context) {
        this.context=context;
    }

    public void show(String title,String msg) {
        if (pd == null) {
            pd = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            pd.setTitle(title);
            pd.setMessage(msg);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
           isCilck(false);
            // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
            pd.setIndeterminate(false);
            pd.setCancelable(true); // 设置ProgressDialog 是否可以按退回键取消
        } else {
            pd.setTitle(title);
            pd.setMessage(msg);
        }
            pd.show(); // 让ProgressDialog显示
    }

    public void deInit() {
        context = null;
        if (pd != null) {
            pd.setOnCancelListener(null);
            pd.setOnDismissListener(null);
            pd.setOnShowListener(null);
        }
        pd = null;
    }

    public void isCilck(boolean falg) {
        pd.setCanceledOnTouchOutside(falg) ;
    }
    public void setMsg(String msg) {
        if (pd != null) {
            pd.setMessage(msg);
        }
    }
    public void dismiss() {
        if (pd != null) {
            pd.dismiss();
            pd=null;
        }
    }
}
