package com.tlp.vendspring.bean;

public class PaySuccessulGetAisleNumberInfoBean {


    /**
     * data : {"channel_num":"1"}
     * status : 200
     * msg : 成功
     */

    private DataBean data;
    private int status;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * channel_num : 1
         */

        private String channel_num;

        public String getChannel_num() {
            return channel_num;
        }

        public void setChannel_num(String channel_num) {
            this.channel_num = channel_num;
        }
    }
}
