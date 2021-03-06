package com.tlp.vendspring.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tlp.vendspring.MSInputMachineCodeActivity;

public class MSUserUtils {

    private static class SingletonHolder {
        private static MSUserUtils instance = new MSUserUtils();
        private SingletonHolder() {
            //do nothing
        }

    }

    public static MSUserUtils getInstance() {
        return MSUserUtils.SingletonHolder.instance;
    }

   public void setUserId (Context context,String userid){
       SharedPreferences sharedPreferences=context.getSharedPreferences("userid",Context.MODE_PRIVATE);
       SharedPreferences.Editor editor=sharedPreferences.edit();
       editor.putString("userid",userid);
       editor.commit();
   }

   public String getUserId(Context context){
         SharedPreferences sharedPreferences=context.getSharedPreferences("userid",Context.MODE_PRIVATE);
        String userid=sharedPreferences.getString("userid",null);
        return userid;
    }

    public void setMachineCode (Context context,String machineCode){
        SharedPreferences sharedPreferences=context.getSharedPreferences("machinecode",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("machinecode",machineCode);
        editor.commit();
    }
    public String getMachineCode(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("machinecode",Context.MODE_PRIVATE);
        String machinecode=sharedPreferences.getString("machinecode",null);
        return machinecode;
    }
}
