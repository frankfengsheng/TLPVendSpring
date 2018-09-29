package com.tcn.funcommon.update;

public class UpdateInfo {
    private String versionCode;
    private String url;
    private String versionName;
    private String content;
    public String getVersionCode() {
        return versionCode;
    }
    public void setVersionCode(String version) {
        this.versionCode = version;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String version) {
        this.versionName = version;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
