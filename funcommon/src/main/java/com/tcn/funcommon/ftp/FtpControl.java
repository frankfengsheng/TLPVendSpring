package com.tcn.funcommon.ftp;

import android.os.Handler;
import android.os.Message;

import com.tcn.funcommon.fileoperation.FileOperation;
import com.tcn.funcommon.media.Utils;
import com.tcn.funcommon.vend.controller.TcnVendIF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class FtpControl {
    private static final String TAG = "FtpControl";
    private static FtpControl mInstance;
    public static final int CMD_DOWNLOAD_SUCCESS = 630;
    public static final int CMD_DOWNLOAD_FAIL = 631;
    public static final int CMD_DOWNLOAD_ALL_NOT_END = 635;
    public static final int CMD_DOWNLOAD_ALL_END = 636;

    public static final int SUCCESS = 1;
    public static final int FAIL = 0;

    private boolean m_bIsDownLoading = false;
    private boolean m_bIsUpLoading = false;
    private String m_host = null;
    private String m_user = null;
    private String m_password = null;
    private FTP m_ftp;
    private FTP m_uploadFtp = null;


    public static synchronized FtpControl getInstance() {
        if (null == mInstance) {
            mInstance = new FtpControl();
        }
        return mInstance;
    }

    public void init(String host, String user, String pass) {
        m_host = host;
        m_user = user;
        m_password = pass;
    }

    public void AnalyseAdvertUrl(String url) {

    }

    private String getLocalPath(String localPath) {
        if ((localPath == null) || (localPath.length() < 1)) {
            return localPath;
        }
        String retLocalPath = "";
        String mStrRootPath = Utils.getExternalStorageDirectory();
        if (localPath.startsWith(mStrRootPath)) {
            retLocalPath = localPath;
        } else {
            retLocalPath = mStrRootPath+"/"+localPath;
        }
        return retLocalPath;
    }

    public boolean isDownLoading() {
        return m_bIsDownLoading;
    }

    public void openConnect(String host, String user, String pass) {
        try {
            closeConnect();
            m_ftp = new FTP(m_host, m_user, m_password);
            // 打开FTP服务
            m_ftp.openConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnect() {
        try {
            if (m_ftp != null) {
                m_ftp.closeConnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeUpload() {
        try {
            if (m_uploadFtp != null) {
                m_uploadFtp.closeConnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(final int type,final Handler handler, final String host, final String user, final String password,
                         final String fileName,final String remotePath, final String localPath) {
        if ((null == handler) || (null == host) || (host.length() <= 0) || (null == user) || (user.length() <= 0) || (null == password) || (password.length() <= 0)
                || (null == fileName)|| (fileName.length() <= 0) || (null == localPath) || (localPath.length() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"download map return");
            return;
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    if (!FileOperation.instance().createFolder(localPath)) {
                        TcnVendIF.getInstance().LoggerError(TAG,"download createFolder fail localPath: "+localPath);
                        m_bIsDownLoading = false;
                        return;
                    }
                    ArrayList<String> fileList = new ArrayList<String>();
                    if (fileName.contains("|")) {
                        String[] strarr = fileName.split("\\|");
                        for (String tmpFileName:strarr) {
                            if ((tmpFileName != null) && (tmpFileName.length() > 0)) {
                                fileList.add(tmpFileName);
                            }
                        }
                    } else {
                        fileList.add(fileName);
                    }
                    m_bIsDownLoading = true;
                    for (String fileName:fileList) {
                        Result result = mftp.download(remotePath, fileName, getLocalPath(localPath));
                        Message msg = handler.obtainMessage();
                        if (null == result) {
                            TcnVendIF.getInstance().LoggerDebug(TAG,"download result is null");
                            msg.what = CMD_DOWNLOAD_FAIL;
                            msg.arg1 = type;
                            msg.obj = fileName;
                            handler.sendMessage(msg);
                        } else {
                            TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed: "+result.isSucceed());
                            if (result.isSucceed()) {
                                msg.what = CMD_DOWNLOAD_SUCCESS;
                            } else {
                                msg.what = CMD_DOWNLOAD_FAIL;
                            }
                            msg.arg1 = type;
                            msg.obj = fileName;
                            handler.sendMessage(msg);
                        }
                    }
                    m_bIsDownLoading = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    m_bIsDownLoading = false;
                    TcnVendIF.getInstance().LoggerError(TAG,"download IOException e: "+e);
                } finally {
                    if (mftp != null) {
                        try {
                            mftp.closeConnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mThread.start();
    }

    public void download(final int type,final Handler handler, final String host, final String user, final String password,
                         final List<String> fileList,final String remotePath, final String localPath) {
        if ((null == handler) || (null == host) || (host.length() <= 0) || (null == user) || (user.length() <= 0) || (null == password) || (password.length() <= 0)
                || (null == fileList)|| (fileList.size() <= 0) || (null == localPath) || (localPath.length() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"download map return");
            return;
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    if (!FileOperation.instance().createFolder(localPath)) {
                        TcnVendIF.getInstance().LoggerError(TAG,"download createFolder fail localPath: "+localPath);
                        m_bIsDownLoading = false;
                        return;
                    }
                    m_bIsDownLoading = true;
                    for (String fileName:fileList) {
                        Result result = mftp.download(remotePath, fileName, getLocalPath(localPath));
                        Message msg = handler.obtainMessage();
                        if (null == result) {
                            TcnVendIF.getInstance().LoggerDebug(TAG,"download result is null");
                            msg.what = CMD_DOWNLOAD_FAIL;
                            msg.arg1 = type;
                            msg.obj = fileName;
                            handler.sendMessage(msg);
                        } else {
                            TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed: "+result.isSucceed());
                            if (result.isSucceed()) {
                                msg.what = CMD_DOWNLOAD_SUCCESS;
                            } else {
                                msg.what = CMD_DOWNLOAD_FAIL;
                            }
                            msg.arg1 = type;
                            msg.obj = fileName;
                            handler.sendMessage(msg);
                        }
                    }
                    m_bIsDownLoading = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    m_bIsDownLoading = false;
                    TcnVendIF.getInstance().LoggerError(TAG,"download IOException e: "+e);
                } finally {
                    if (mftp != null) {
                        try {
                            mftp.closeConnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mThread.start();
    }

    public void download(final HashMap<String, HashMap<String,String>> mapHashMapap, final List<String> localPathList) {
        if ((null == mapHashMapap) || (mapHashMapap.size() <= 0) || (null == localPathList) || (localPathList.size() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"download map return");
            return;
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    if (m_ftp != null) {
//                        m_ftp.closeConnect();
//                    }
                    m_ftp = new FTP(m_host, m_user, m_password);
                    // 打开FTP服务
                    m_ftp.openConnect();
                    TcnVendIF.getInstance().LoggerDebug(TAG,"download start");
                    m_bIsDownLoading = true;
                    int count = 0;
                    HashMap<String,String> map = null;
                    for (String key : mapHashMapap.keySet()) {
                        count++;
                        map = mapHashMapap.get(key);
                        for (String mapkey : map.keySet()) {
                            String[] pathArray = (map.get(mapkey)).split( "\\|" );
                            String localPath = null;
                            if ("0".equals(pathArray[1])) {         //广告
                                localPath = localPathList.get(0);
                            } else if ("1".equals(pathArray[1])) {    //待机图片
                                localPath = localPathList.get(1);
                            } else if ("2".equals(pathArray[1])) {      //背景图片
                                localPath = localPathList.get(2);
                            } else {

                            }
                            if (!FileOperation.instance().createFolder(localPath) || (null == localPath)) {
                                TcnVendIF.getInstance().LoggerError(TAG,"download createFolder fail localPath: "+localPath);
                                continue;
                            }
                            Result result = m_ftp.download(pathArray[0], mapkey, getLocalPath(localPath));
                            TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed: "+result.isSucceed());
                            if (result.isSucceed()) {
                                if (m_Handler != null) {
                                    Message msg = m_Handler.obtainMessage();
                                    msg.what = CMD_DOWNLOAD_SUCCESS;
                                    if (count >= mapHashMapap.size()) {
                                        msg.arg1 = CMD_DOWNLOAD_ALL_END;
                                        if (m_ftp != null) {
                                            m_ftp.closeConnect();
                                        }
                                    } else {
                                        msg.arg1 = CMD_DOWNLOAD_ALL_NOT_END;
                                    }
                                    msg.obj = key;
                                    m_Handler.sendMessage(msg);
                                }
                                TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed");
                            } else {
                                if (m_Handler != null) {
                                    Message msg = m_Handler.obtainMessage();
                                    msg.what = CMD_DOWNLOAD_FAIL;
                                    if (count >= mapHashMapap.size()) {
                                        msg.arg1 = CMD_DOWNLOAD_ALL_END;
                                        if (m_ftp != null) {
                                            m_ftp.closeConnect();
                                        }
                                    } else {
                                        msg.arg1 = CMD_DOWNLOAD_ALL_NOT_END;
                                    }
                                    msg.obj = key;
                                    m_Handler.sendMessage(msg);
                                }
                                TcnVendIF.getInstance().LoggerDebug(TAG,"download fail");
                            }
                        }
                    }
                    TcnVendIF.getInstance().LoggerDebug(TAG,"download end");
                    m_bIsDownLoading = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    m_bIsDownLoading = false;
                    TcnVendIF.getInstance().LoggerError(TAG,"download IOException e: "+e);
                }
            }
        });

        mThread.start();

    }

    public void download(String remotePath, String fileName, String localPath) {
        if ((null == remotePath) || (remotePath.length() <= 0) || (null == fileName) || (fileName.length() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"download return");
            return;
        }
        try {

            TcnVendIF.getInstance().LoggerDebug(TAG,"download start");
            m_bIsDownLoading = true;
            Result result = m_ftp.download(remotePath, fileName, getLocalPath(localPath));
            m_bIsDownLoading = false;
            TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed: "+result.isSucceed());
            if (result.isSucceed()) {
                TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            m_bIsDownLoading = false;
            TcnVendIF.getInstance().LoggerError(TAG,"download IOException e: "+e);
        }
    }

    public void downloadByThread(final String host, final String user, final String password,final String remotePath,final String endfileName,final String localPath,
                                 final Handler handler, final int what) {
        if ((null == remotePath) || (remotePath.length() <= 0) || (null == endfileName) || (endfileName.length() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"downloadByThread return");
            return;
        }
        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    Result result = mftp.downloadContain(remotePath, endfileName, getLocalPath(localPath));
                    TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed "+result.isSucceed());
                    if (result.isSucceed()) {
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = what;
                            message.arg1 = SUCCESS;
                            message.obj = localPath;
                            handler.sendMessage(message);
                        } else {
                            Message message = handler.obtainMessage();
                            message.what = what;
                            message.arg1 = FAIL;
                            message.obj = localPath;
                            handler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG,"download e "+e);
                }
            }
        });
        mThread.start();
    }

    public void download(String remotePath, List<String> fileNameList, String localPath) {
        if ((null == remotePath) || (remotePath.length() <= 0) || (null == fileNameList) || (fileNameList.size() <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG,"download list return");
            return;
        }
        try {
            TcnVendIF.getInstance().LoggerDebug(TAG,"download start");
            m_bIsDownLoading = true;
            for (String fileName:fileNameList) {
                Result result = m_ftp.download(remotePath, fileName, getLocalPath(localPath));
                TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed: "+result.isSucceed());
                if (result.isSucceed()) {
                    TcnVendIF.getInstance().LoggerDebug(TAG,"download isSucceed");
                }
            }
            m_bIsDownLoading = false;

        } catch (IOException e) {
            e.printStackTrace();
            m_bIsDownLoading = false;
            TcnVendIF.getInstance().LoggerError(TAG,"download IOException e: "+e);
        }
    }

    public void uploading(final String host, final String user, final String password, final String filePath, final String remotePath) {


        final File file = new File(filePath.trim());
        if ((!file.exists()) || (!file.isFile())) {
            return;
        }

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    Result result = mftp.uploading(file,remotePath);
                    TcnVendIF.getInstance().LoggerDebug(TAG,"uploading isSucceed "+result.isSucceed());
                    if (result.isSucceed()) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG,"uploading e "+e);
                }
            }
        });
        mThread.start();
    }

    public void uploading(final String host, final String user, final String password, final File file, final String remotePath) {

        if ((null == file) || (!file.exists()) || (!file.isFile())) {
            return;
        }

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    Result result = mftp.uploading(file,remotePath);
                    TcnVendIF.getInstance().LoggerDebug(TAG,"uploading isSucceed "+result.isSucceed());
                    if (result.isSucceed()) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG,"uploading e "+e);
                }
            }
        });
        mThread.start();
    }

    public void uploading(final String host, final String user, final String password, final File file, final String remotePath,
                          final Handler handler, final int what, final int type) {

        if ((null == file) || (!file.exists()) || (!file.isFile())) {
            return;
        }

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTP mftp = null;
                try {
                    mftp = new FTP(host, user, password);
                    // 打开FTP服务
                    mftp.openConnect();
                    Result result = mftp.uploading(file,remotePath);
                    TcnVendIF.getInstance().LoggerDebug(TAG,"uploading isSucceed "+result.isSucceed());
                    if (result.isSucceed()) {
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = what;
                            message.arg1 = SUCCESS;
                            message.arg2 = type;
                            message.obj = file.getAbsolutePath();
                            handler.sendMessage(message);
                        }
                    } else {
                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = what;
                            message.arg1 = FAIL;
                            message.arg2 = type;
                            message.obj = file.getAbsolutePath();
                            handler.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG,"uploading e "+e);
                }
            }
        });
        mThread.start();
    }

    public void uploading(final String host, final String user, final String password, final HashMap<File,String> fileNameAndPathMap) {
        if ((null == host) || (host.length() < 1) || (null == user) || (user.length() < 1) || (null == password) || (password.length() < 1)
                || (null == fileNameAndPathMap) || (fileNameAndPathMap.size() < 1)) {
            TcnVendIF.getInstance().LoggerError(TAG,"uploading host: "+host+" user: "+user+" password: "+password);
            return;
        }
        Thread mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    m_uploadFtp = new FTP(host, user, password);
                    // 打开FTP服务
                    m_uploadFtp.openConnect();
                    TcnVendIF.getInstance().LoggerDebug(TAG,"uploading start");
                    m_bIsUpLoading = true;
                    for (File key : fileNameAndPathMap.keySet()) {
                        Result result = m_uploadFtp.uploading(key, fileNameAndPathMap.get(key));
                        TcnVendIF.getInstance().LoggerDebug(TAG,"uploading isSucceed: "+result.isSucceed()+" getName: "+key.getName());
                        if (!result.isSucceed()) {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            result = m_uploadFtp.uploading(key, fileNameAndPathMap.get(key));
                            TcnVendIF.getInstance().LoggerDebug(TAG,"uploading isSucceed: "+result.isSucceed());
                        }
                    }

                    m_bIsUpLoading = false;
                    if (m_uploadFtp != null) {
                        m_uploadFtp.closeConnect();
                    }
                } catch (IOException e) {
                    m_bIsUpLoading = false;
                    TcnVendIF.getInstance().LoggerError(TAG,"uploading IOException e: "+e);
                }
            }
        });
        mThread.start();
    }

    private Handler m_Handler = null;
    public void setHandler(Handler handler) {
        m_Handler = handler;
    }
}
