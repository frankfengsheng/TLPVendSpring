package com.tcn.funcommon.advert;

import android.os.Handler;
import android.os.Message;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnShareUseData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class RemoutAdvertControl {
    private static final String TAG = "RemoutAdvertControl";
    private static RemoutAdvertControl mInstance;
    public static final int CMD_ADVERT_NOT_DOWNLOAD = 0;
    public static final int CMD_ADVERT_ALREADY_DOWNLOAD = 1;

    public static final int CMD_ADVERT_ID_NOT_END = 0;
    public static final int CMD_ADVERT_ID_END = 1;

    public static final int ADVERT_TYPE_RESULT_FAIL         = 0;
    public static final int ADVERT_TYPE_RESULT_SUCCESS      = 1;
    public static final int ADVERT_TYPE_RESULT_NOT_MATCH    = 2;

    public static final int CMD_REMOUT_ADVERT = 120;
    private RemoutAdThread m_RemoutAdThread = null;
    private List<String> m_listAdId = null;
    private List<String> m_listAdFileName = null;
    private List<String> m_listAdStandbyFileName = null;

    public static synchronized RemoutAdvertControl getInstance() {
        if (null == mInstance) {
            mInstance = new RemoutAdvertControl();
        }
        return mInstance;
    }

    public void startRemoutAdvert() {
        stopRemoutAdvert();
        m_RemoutAdThread = new RemoutAdThread();
        m_RemoutAdThread.setName("RemoutAdThread");
        m_RemoutAdThread.setAdvertStart(true);
        m_RemoutAdThread.start();
    }

    public void stopRemoutAdvert() {
        if (null != m_RemoutAdThread) {
            m_RemoutAdThread.setAdvertStart(false);
            m_RemoutAdThread.isInterrupted();
            m_RemoutAdThread = null;
        }
    }

    public String getAdID(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdId = null;
        try {
            mAdId = json.get("AdID").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdId;
    }

    public String getAdMachingID(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdMachingID = null;
        try {
            mAdMachingID = json.get("AdMachingID").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdMachingID;
    }

    public String getAdImg(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdImg = null;
        try {
            mAdImg = json.get("AdImg").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdImg;
    }

    public String getAdrDownload(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdrDownload = null;
        try {
            mAdrDownload = json.get("AdrDownload").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdrDownload;
    }

    public String getAdrsTime(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdrsTime = null;
        try {
            mAdrsTime = json.get("AdrsTime").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdrsTime;
    }

    public String getAdUrl(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdUrl = null;
        try {
            mAdUrl = json.get("Url").toString();
            mAdUrl.replace("\\/","/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdUrl;
    }

    public String getAdHost(String url) {
        if ((null == url) || (url.length() <= 0)) {
            return null;
        }
        String mStrParam = null;
        try {
            if (url.contains("|")) {
                String[] strarr = url.split("\\|");
                if (strarr != null) {
                    if (3 == strarr.length) {
                        int indexX = strarr[0].indexOf("/",6);
                        mStrParam = strarr[0].substring(6,indexX);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrParam;
    }

    public String getAdRemotePath(String url) {
        if ((null == url) || (url.length() <= 0)) {
            return null;
        }
        String mStrParam = null;
        try {
            if (url.contains("|")) {
                String[] strarr = url.split("\\|");
                if (strarr != null) {
                    if (3 == strarr.length) {
                        int indexX = strarr[0].indexOf("/",6);
                        mStrParam = strarr[0].substring(indexX);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrParam;
    }

    public String getAdUser(String url) {
        if ((null == url) || (url.length() <= 0)) {
            return null;
        }
        String mStrParam = null;
        try {
            if (url.contains("|")) {
                String[] strarr = url.split("\\|");
                if (strarr != null) {
                    if (3 == strarr.length) {
                        mStrParam = strarr[1];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrParam;
    }

    public String getAdPassword(String url) {
        if ((null == url) || (url.length() <= 0)) {
            return null;
        }
        String mStrParam = null;
        try {
            if (url.contains("|")) {
                String[] strarr = url.split("\\|");
                if (strarr != null) {
                    if (3 == strarr.length) {
                        mStrParam = strarr[2];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrParam;
    }

    //0: 广告  1：待机图片  2：背景图片
    public String getAdPlayType(JSONObject json) {
        if ((null == json) || (json.length() <= 0)) {
            return null;
        }
        String mAdPlayType = null;
        try {
            mAdPlayType = json.get("AdPlayType").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mAdPlayType;
    }

    public List<String> getAdIdList() {
        return m_listAdId;
    }

    public List<String> getAdFileNameList() {
        return m_listAdFileName;
    }

    public List<String> getAdStandbyFileNameList() {
        return m_listAdStandbyFileName;
    }

    private void analyseData(String strJSONArray) {

        if ((null == strJSONArray) || (strJSONArray.length() <= 0)) {
            return;
        }
        try {
            if (null == m_listAdId) {
                m_listAdId = new ArrayList<String>();
            } else {
                m_listAdId.clear();
            }

            if (null == m_listAdFileName) {
                m_listAdFileName = new ArrayList<String>();
            } else {
                m_listAdFileName.clear();
            }

            if (null == m_listAdStandbyFileName) {
                m_listAdStandbyFileName = new ArrayList<String>();
            } else {
                m_listAdStandbyFileName.clear();
            }

            JSONArray arr = new JSONArray(strJSONArray);
            int iLength = arr.length();
            for (int i = 0; i < iLength; i++) {
                JSONObject json = (JSONObject) arr.get(i);
                TcnLog.getInstance().LoggerInfo(TAG, "analyseData json: " + json);
                if (m_Handler != null) {
                    Message message = m_Handler.obtainMessage();
                    message.what = CMD_REMOUT_ADVERT;
                    String download = getAdrDownload(json);
                    TcnLog.getInstance().LoggerInfo(TAG, "analyseData download: " + download);
                    if ("1".equals(download)) {
                        message.arg1 = CMD_ADVERT_ALREADY_DOWNLOAD;
                    } else {
                        message.arg1 = CMD_ADVERT_NOT_DOWNLOAD;
                    }

                    m_listAdId.add(getAdID(json));

                    String strType = getAdPlayType(json);
                    if ("0".equals(strType)) {
                        m_listAdFileName.add(getAdImg(json));
                    } else if ("1".equals(strType)) {
                        m_listAdStandbyFileName.add(getAdImg(json));
                    } else {

                    }

                    if ((iLength - 1) == i) {
                        message.arg2 = CMD_ADVERT_ID_END;
                    } else {
                        message.arg2 = CMD_ADVERT_ID_NOT_END;
                    }

                    message.obj = json;

                    m_Handler.sendMessage(message);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reqAdvert() {
        TcnLog.getInstance().LoggerInfo(TAG, "reqAdvert getMachineID: " + TcnShareUseData.getInstance().getMachineID()+" getAdvertIP: "+TcnShareUseData.getInstance().getAdvertIP());
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("MachineID", TcnShareUseData.getInstance().getMachineID()));
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter(mParams);
        HttpUtils httpUtils = new HttpUtils();
        StringBuffer sb = new StringBuffer();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://"+TcnShareUseData.getInstance().getAdvertIP()+"/ADDownload/GetAD", requestParams, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                TcnLog.getInstance().LoggerInfo(TAG, "reqAdvert onFailure arg0: " + arg0 + " arg1: " + arg1);
              //  sendMessage(mHandler, what, null);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                TcnLog.getInstance().LoggerInfo(TAG, "reqAdvert onSuccess arg0: " + arg0.result);
                analyseData(arg0.result);
               // sendMessage(mHandler,what,arg0.result);
            }
        });
    }

    public void reqDownloadFeedBack(String adId,int typeMatch) {
        TcnLog.getInstance().LoggerInfo(TAG, "reqDownloadFeedBack adId: " + adId+" getMachineID:"+TcnShareUseData.getInstance().getMachineID());
        if ((null == adId) || (adId.length() < 1)) {
            return;
        }
        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("AdID", adId));
        mParams.add(new BasicNameValuePair("MachineID", TcnShareUseData.getInstance().getMachineID()));
        mParams.add(new BasicNameValuePair("ADStrat", String.valueOf(typeMatch)));
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter(mParams);
        HttpUtils httpUtils = new HttpUtils();
        StringBuffer sb = new StringBuffer();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://"+TcnShareUseData.getInstance().getAdvertIP()+"/ADDownload/SetAD", requestParams, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                TcnLog.getInstance().LoggerInfo(TAG, "reqDownloadFeedBack onFailure arg0: " + arg0 + " arg1: " + arg1);
                //  sendMessage(mHandler, what, null);
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                TcnLog.getInstance().LoggerInfo(TAG, "reqDownloadFeedBack onSuccess arg0: " + arg0.result);
                // sendMessage(mHandler,what,arg0.result);
            }
        });
    }

    private class RemoutAdThread extends Thread {
        private boolean mbStart = false;
        public void setAdvertStart(boolean start) {
            mbStart = start;
        }

        @Override
        public void run() {
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!isInterrupted() && mbStart) {

                reqAdvert();

                try {
                    sleep(TcnShareUseData.getInstance().getAdvertPollTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            super.run();
        }
    }

    private Handler m_Handler = null;
    public void setHandler(Handler handler) {
        m_Handler = handler;
    }
}
