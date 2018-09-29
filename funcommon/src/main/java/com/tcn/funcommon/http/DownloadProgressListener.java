package com.tcn.funcommon.http;

/**
 * Created by Administrator on 2017/7/12.
 */
public interface DownloadProgressListener {
    public void onDownloadSize(int size ,int fileSize, String fileName);
}
