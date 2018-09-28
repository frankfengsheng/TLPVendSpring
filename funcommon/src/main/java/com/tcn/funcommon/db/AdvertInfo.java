package com.tcn.funcommon.db;

/**
 * Created by Administrator on 2016/8/18.
 */
public class AdvertInfo {
    private int m_iId;
    private String m_strAdId;
    private String m_strMachineID;
    private String m_strAdImg;
    private String m_strAdDownload;
    private String m_strAdRsTime;
    private String m_strAdUrl;
    private String m_strAdPlayType;
    private String m_strAd_local_url;
    private String m_strAd_act_id;
    private String m_strAd_content;
    private String m_strAd_details_url;


    public int getId() {
        return m_iId;
    }
    public void setId(int id) {
        m_iId = id;
    }

    public String getAdId() {
        return m_strAdId;
    }
    public void setAdId(String adid) {
        m_strAdId = adid;
    }

    public String getAdMachineID() {
        return m_strMachineID;
    }
    public void setAdMachineID(String machineID) {
        m_strMachineID = machineID;
    }

    public String getAdName() {
        return m_strAdImg;
    }
    public void setAdImg(String adimg) {
        m_strAdImg = adimg;
    }

    public String getAdDownload() {
        return m_strAdDownload;
    }
    public void setAdDownload(String download) {
        m_strAdDownload = download;
    }

    public String getAdRsTime() {
        return m_strAdRsTime;
    }
    public void setAdRsTime(String rsTime) {
        m_strAdRsTime = rsTime;
    }

    public String getAdUrl() {
        return m_strAdUrl;
    }
    public void setAdUrl(String url) {
        m_strAdUrl = url;
    }

    public String getAdPlayType() {
        return m_strAdPlayType;
    }
    public void setAdPlayType(String type) {
        m_strAdPlayType = type;
    }

    public String getAdLocalUrl() {
        return m_strAd_local_url;
    }
    public void setAdLocalUrl(String url) {
        m_strAd_local_url = url;
    }

    public String getAdActId() {
        return m_strAd_act_id;
    }
    public void setAdActId(String actId) {
        m_strAd_act_id = actId;
    }

    public String getAdContent() {
        return m_strAd_content;
    }
    public void setAdContent(String content) {
        m_strAd_content = content;
    }

    public String getAdDetailsUrl() {
        return m_strAd_details_url;
    }
    public void setAdDetailsUrl(String url) {
        m_strAd_details_url = url;
    }
}
