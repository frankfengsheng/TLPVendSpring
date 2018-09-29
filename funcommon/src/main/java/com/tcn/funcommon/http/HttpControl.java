package com.tcn.funcommon.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
public class HttpControl {
    private static final String TAG = "HttpControl";
    public static final int DOWNLOAD_FAIL                = 640;
    public static final int DOWNLOAD_SUCCESS             = 641;
    public static final int DOWNLOAD_PROGRESS            = 642;
    public static final int DOWNLOAD_PROGRESS_HALF      = 643;


    private static HttpControl mInstance;
    private volatile DownloadTask m_task;
    private volatile UploadTask m_task_upload;
    private volatile Handler m_ReceiveHandler = null;
    private volatile Context m_context = null;

    public static synchronized HttpControl getInstance() {
        if (null == mInstance) {
            mInstance = new HttpControl();
        }
        return mInstance;
    }

    public void setHandler(Handler handler) {
        m_ReceiveHandler = handler;
    }

    public void setContext(Context context) {
        m_context = context;
    }

    public void exitDownload() {
        if (m_task != null) {
            m_task.exit();
        }
    }

    public void uploadFile(String picPath, String requestURL) {
        String[] picPaths = new String[1];
        picPaths[0] = picPath;
        m_task_upload = new UploadTask(picPaths,requestURL);
        new Thread(m_task_upload).start();
    }

    public void download(String path, String saveDirPath) {
        File savDir = new File(saveDirPath);
        List<String> pathList = new ArrayList<String>();
        if (path.contains("|")) {
            String[] strarr = path.split("\\|");
            for (String tmpFileName:strarr) {
                if ((tmpFileName != null) && (tmpFileName.length() > 0)) {
                    pathList.add(tmpFileName);
                }
            }
        } else {
            pathList.add(path);
        }
        m_task = new DownloadTask(-1,pathList, savDir);
        new Thread(m_task).start();
    }

    public void download(List<String> pathList, String saveDirPath) {
        File savDir = new File(saveDirPath);
        m_task = new DownloadTask(-1,pathList, savDir);
        new Thread(m_task).start();
    }

    public void download(int type,String path, String saveDirPath) {
        File savDir = new File(saveDirPath);
        List<String> pathList = new ArrayList<String>();
        if (path.contains("|")) {
            String[] strarr = path.split("\\|");
            for (String tmpFileName:strarr) {
                if ((tmpFileName != null) && (tmpFileName.length() > 0)) {
                    pathList.add(tmpFileName);
                }
            }
        } else {
            pathList.add(path);
        }
        m_task = new DownloadTask(type,pathList, savDir);
        new Thread(m_task).start();
    }

    public void download(int type,List<String> pathList, String saveDirPath) {
        File savDir = new File(saveDirPath);
        m_task = new DownloadTask(type,pathList, savDir);
        new Thread(m_task).start();
    }

    private class DownloadTask implements Runnable {
        private int type;
        private List<String> pathList;
        private File saveDir;
        private FileDownloader loader;

        public DownloadTask(int type, List<String> pathList,File saveDir) {
            this.type = type;
            this.pathList = pathList;
            this.saveDir = saveDir;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null) {
                loader.exit();
            }
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {


            @Override
            public void onDownloadSize(int size,int fileSize, String fileName) {
                if (m_ReceiveHandler != null) {
                    Message msg = m_ReceiveHandler.obtainMessage();
                    if (size >= fileSize) {
                        msg.what = DOWNLOAD_SUCCESS;
                    } else if ((size >= (fileSize/2)) && (size <= (fileSize/2 + 100000))) {
                        msg.what = DOWNLOAD_PROGRESS_HALF;
                    }
                    else {
                        msg.what = DOWNLOAD_PROGRESS;
                    }
                    msg.arg1 = size;
                    msg.arg2 = type;
                    msg.obj = fileName;
                    m_ReceiveHandler.sendMessage(msg);
                }
            }
        };

        public void run() {
            if ((null == pathList) || (pathList.size() < 1)) {
                return;
            }
            for (String path:pathList) {
                try {
                    // 实例化一个文件下载器
                    loader = new FileDownloader(m_context, path,saveDir, 3);
                    // 设置进度条最大值
                    //progressBar.setMax(loader.getFileSize());
                    int iFileSize = loader.getFileSize();
                    TcnVendIF.getInstance().LoggerError(TAG, "DownloadTask iFileSize: "+iFileSize);
                    if (iFileSize > 0) {
                        loader.download(downloadProgressListener);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG, "DownloadTask e: "+e);
                    if (m_ReceiveHandler != null) {
                        Message msg = m_ReceiveHandler.obtainMessage();
                        msg.what = DOWNLOAD_FAIL;
                        msg.arg2 = type;
                        msg.obj = path.substring(path.lastIndexOf("/")+1);
                        m_ReceiveHandler.sendMessage(msg);
                    }
                }
            }
        }
    }

    private class UploadTask implements Runnable {

        private String[] pathList;
        private String requestURL;
        private FileUploader loader;

        public UploadTask(String[] picPaths,String rUrl) {
            this.pathList = picPaths;
            this.requestURL = rUrl;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null) {
            //    loader.exit();
            }
        }

        @Override
        public void run() {
            if ((null == pathList) || (pathList.length < 1)) {
                return;
            }
            String[] picPaths = null;
            // 实例化一个文件下载器
            loader = new FileUploader();
            loader.uploadFile(pathList,requestURL);
        }
    }
}
