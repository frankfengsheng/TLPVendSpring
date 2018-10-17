package com.tlp.vendspring.netutil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {


    public void showDialog(Context context,String message, final OnDialogSureClick click){
        AlertDialog.Builder
                normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface
                                                dialog, int which)
                    {
                        click.sureClick();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface
                                                dialog, int which)
                    {

                    }
                });

        normalDialog.show();
    }
  public static   interface OnDialogSureClick{
        void sureClick();
    }
}
