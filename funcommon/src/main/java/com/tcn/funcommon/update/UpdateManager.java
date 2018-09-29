package com.tcn.funcommon.update;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {
    private static final String TAG = "UpdateManager";

    private String curVersionName;
    private String newVersionName;
    private int curVersionCode;
    private int newVersionCode;
    private UpdateCallback callback;
    private Context ctx;

    private int progress;
    private Boolean hasNewVersion;
    private Boolean canceled;

    private static final int UPDATE_CHECK_ERR = 0;
    private static final int UPDATE_CHECKCOMPLETED = 1;
    private static final int UPDATE_DOWNLOADING = 2;
    private static final int UPDATE_DOWNLOAD_ERROR = 3;
    private static final int UPDATE_DOWNLOAD_COMPLETED = 4;
    private static final int UPDATE_DOWNLOAD_CANCELED = 5;

    private String m_strApkName = null;
    private String m_strApkUrl = null;

    private UpdateInfo m_UpdateInfo;

    public UpdateManager(Context context, String apkName, String apkUrl, UpdateCallback updateCallback) {
        ctx = context;
        callback = updateCallback;
        canceled = false;
        m_strApkName = apkName;
        m_strApkUrl = apkUrl;
        getCurVersion();
    }

    public void deInitialize() {
        cancelDownload();
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
            updateHandler = null;
        }
        callback = null;
        m_strApkName = null;
        m_strApkUrl = null;
        m_UpdateInfo = null;
        ctx = null;
    }

    public String[] getCurVerInfo()
    {
        String[] str = {String.valueOf(curVersionCode), curVersionName};
        return str;
    }

    public UpdateInfo getUpdataInfo() {
        return m_UpdateInfo;
    }

    private void getCurVersion() {
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            curVersionName = pInfo.versionName;
            curVersionCode = pInfo.versionCode;
            m_UpdateInfo = new UpdateInfo();
            m_UpdateInfo.setVersionName(curVersionName);
            m_UpdateInfo.setVersionCode(String.valueOf(curVersionCode));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void checkUpdate() {
        hasNewVersion = false;
        new Thread() {
            /**
             * @by song add
             *
             */
            @Override
            public void run() {
                boolean bSuccess = false;
                try {
                    //从资源文件获取服务器 地址
                    String path = m_strApkUrl;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpParams params=httpclient.getParams(); //计算网络超时用. String str = "";
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    HttpGet httpget = new HttpGet(path);
                    //HttpPost httppost = new HttpPost(path);
                    HttpResponse response = httpclient.execute(httpget);
                    TcnVendIF.getInstance().LoggerDebug(TAG,"getStatusCode: "+response.getStatusLine().getStatusCode());
                    if (response.getStatusLine().getStatusCode() == 200) {
                        bSuccess = true;
                        HttpEntity entity = response.getEntity();
                        InputStream inputStream = entity.getContent();
                        m_UpdateInfo = getUpdataInfo(inputStream);
                        newVersionName = m_UpdateInfo.getVersionName();
                        if(!newVersionName.equals(curVersionName)) {
                            hasNewVersion = true;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    TcnVendIF.getInstance().LoggerError(TAG,"checkUpdate m_strApkUrl: "+m_strApkUrl+" e: "+e);
                }
                if (bSuccess) {
                    updateHandler.sendEmptyMessage(UPDATE_CHECKCOMPLETED);
                } else {
                    updateHandler.sendEmptyMessage(UPDATE_CHECK_ERR);
                }

            }
        }.start();

    }


    /*
    * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
    */
    public static UpdateInfo getUpdataInfo(InputStream is) throws Exception{
        XmlPullParser  parser = Xml.newPullParser();
        parser.setInput(is, "gb2312");//设置解析的数据源
        int type = parser.getEventType();
        UpdateInfo info = new UpdateInfo();//实体
        while(type != XmlPullParser.END_DOCUMENT ){
            switch (type) {
                case XmlPullParser.START_TAG:
                    if("version".equals(parser.getName())){
                        info.setVersionCode(parser.nextText()); //获取版本号
                    }else if ("url".equals(parser.getName())){
                        info.setUrl(parser.nextText()); //获取要升级的APK文件
                    } else if ("description".equals(parser.getName())){
                        info.setVersionName(parser.nextText()); //获取版本名称
                    } else if ("content".equals(parser.getName())){
                        info.setContent(parser.nextText()); //获取该文件的信息
                    } else {

                    }
                    break;
                default:
                    break;
            }
            type = parser.next();
        }
        return info;
    }

    public void update() {

        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), m_strApkName)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void downloadPackage()
    {	new Thread() {

        @Override
        public void run() {

            try {
                URL url = new URL(m_UpdateInfo.getUrl());
                HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.connect();
                int length = conn.getContentLength();
                InputStream inputStream = conn.getInputStream();

                File ApkFile = new File(Environment.getExternalStorageDirectory(), m_strApkName);
                if(ApkFile.exists())
                {

                    ApkFile.delete();
                }

                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[512];

                do {
                    int numread = inputStream.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING));
                    if(numread <= 0) {
                        updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
                        break;
                    }
                    fos.write(buf,0,numread);
                } while(!canceled);

                fos.close();
                inputStream.close();

                if(canceled)
                {
                    updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
                }
                updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
            } catch (Exception e) {
                e.printStackTrace();
                updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR,e.getMessage()));
            }

        }

    }.start();

    }

    public void cancelDownload()
    {
        canceled = true;
        if (updateHandler != null) {
            updateHandler.removeCallbacksAndMessages(null);
            updateHandler = null;
        }
    }

    Handler updateHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_CHECK_ERR:
                    callback.checkUpdateCompleted(false, null);
                    break;
                case UPDATE_CHECKCOMPLETED:

                    callback.checkUpdateCompleted(hasNewVersion, newVersionName);
                    break;
                case UPDATE_DOWNLOADING:

                    callback.downloadProgressChanged(progress);
                    break;
                case UPDATE_DOWNLOAD_ERROR:

                    callback.downloadCompleted(false, msg.obj.toString());
                    break;
                case UPDATE_DOWNLOAD_COMPLETED:

                    callback.downloadCompleted(true, "");
                    break;
                case UPDATE_DOWNLOAD_CANCELED:

                    callback.downloadCanceled();
                default:
                    break;
            }
        }
    };

    public interface UpdateCallback {
        public void checkUpdateCompleted(Boolean hasUpdate,
                                         CharSequence updateInfo);

        public void downloadProgressChanged(int progress);
        public void downloadCanceled();
        public void downloadCompleted(Boolean sucess, CharSequence errorMsg);
    }
}
