package com.tlp.vendspring.bean;

import java.util.List;

public class MsShlefGoodInfoSubmitBean {


    /**
     * data : []
     * status : 200
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<?> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
