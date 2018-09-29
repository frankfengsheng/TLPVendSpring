package com.tcn.funcommon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tcn.funcommon.systeminfo.SystemInfo;
import com.tcn.funcommon.vend.controller.TcnVendIF;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/31 15:40
 * 邮箱：m68013@qq.com
 */
public class TcnMediaReceiver extends BroadcastReceiver {
    private static final String TAG = "TcnMediaReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Uri uri = intent.getData();
        String path = null;
        if ((null == action) || (null == uri)) {
            TcnVendIF.getInstance().LoggerError(TAG, "onReceive Error: action or uri is null");
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "onReceive uri: " + uri + "  uri.getScheme(): " + uri.getScheme());
        if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "TcnMediaReceiver intent.getAction(): " + intent.getAction());
        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
            String strPath = uri.toString();
            if (strPath.startsWith("file:///")) {
                strPath = strPath.substring(7);
                TcnVendIF.getInstance().addMountedDevicePath(strPath);
            }
            if (TcnVendIF.getInstance().isUserMainBoard()) {
                if (!TcnShareUseData.getInstance().isUSBConfigCannotReboot()) {
                    TcnShareUseData.getInstance().setUSBConfigCannotReboot(true);
                    if (TcnVendIF.getInstance().isExistSlotConfigFile()) {
                        SystemInfo.rebootDevice();
                    }
                }
            } else {

            }
            TcnVendIF.getInstance().queryImagePathList();
        } else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
            TcnShareUseData.getInstance().setUSBConfigCannotReboot(false);
            TcnVendIF.getInstance().queryImagePathList();
        } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
            TcnVendIF.getInstance().queryImagePathList();
        } else if (action.equals(intent.ACTION_MEDIA_REMOVED) ||
                action.equals(intent.ACTION_MEDIA_BAD_REMOVAL) || action.equals(intent.ACTION_MEDIA_EJECT)) {
            TcnShareUseData.getInstance().setUSBConfigCannotReboot(false);
        } else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {

        } else {
            TcnVendIF.getInstance().LoggerError(TAG, "unknown action:(" + action + ")");
        }
    }

}
