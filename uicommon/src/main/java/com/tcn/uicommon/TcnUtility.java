package com.tcn.uicommon;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 作者：Jiancheng,Song on 2016/5/27 23:48
 * 邮箱：m68013@qq.com
 */
public class TcnUtility {
    private static Toast toast;

    public static void getsToastSign(Context context,String text) {
        View view= LayoutInflater.from(context).inflate(R.layout.toast, null);
        if (toast == null) {
            toast=new Toast(context);
            TextView msg=(TextView) view.findViewById(R.id.msg);
            msg.setText(text);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            TextView msg=(TextView) view.findViewById(R.id.msg);
            msg.setText(text);
        }
        view.setBackgroundResource(R.drawable.toaststyle);
        toast.setView(view);
        toast.show();
    }
    /**
     * 自定义toast的样式
     * @param context
     * @param text
     */
    public static void getToast(Context context,String text){
        Toast toast=new Toast(context);
        View view= LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView msg=(TextView) view.findViewById(R.id.msg);
        msg.setText(text);
        view.setBackgroundResource(R.drawable.toaststyle);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(500);
        toast.show();
    }

    public static Toast getToast(Context context,String text, int duration){
        Toast toast=new Toast(context);
        View view= LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView msg=(TextView) view.findViewById(R.id.msg);
        msg.setText(text);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        return toast;
    }

    private static long lastClickTime;
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
