package com.tlp.vendspring.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcn.vendspring.R;

public class DialogUtil {


    public void showDialog(Context context,String message, final OnDialogSureClick click){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setMessage(message);
        View contentView=LayoutInflater.from(context).inflate(R.layout.layout_dialog,null);
        Button btn_sure= (Button) contentView.findViewById(R.id.btn_save);
        Button btn_cancle= (Button) contentView.findViewById(R.id.btn_cancle);
        TextView tv_title= (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText(message);
        final Dialog dialog=normalDialog.create();
        dialog.show();
        dialog.getWindow().setContentView(contentView);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.sureClick();
                dialog.dismiss();
            }
        });

    }
  public static   interface OnDialogSureClick{
        void sureClick();
    }
}
