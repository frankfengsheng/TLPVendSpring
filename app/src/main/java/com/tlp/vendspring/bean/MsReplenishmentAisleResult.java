package com.tlp.vendspring.bean;

import java.util.List;

public class MsReplenishmentAisleResult {


    /**
     * data : [{"channel_num":"1","channel_capacity":"5","channel_remain":"5"},{"channel_num":"2","channel_capacity":"6","channel_remain":"6"},{"channel_num":"3","channel_capacity":"6","channel_remain":"6"},{"channel_num":"4","channel_capacity":"6","channel_remain":"6"},{"channel_num":"5","channel_capacity":"6","channel_remain":"6"},{"channel_num":"6","channel_capacity":"6","channel_remain":"6"},{"channel_num":"7","channel_capacity":"6","channel_remain":"6"},{"channel_num":"8","channel_capacity":"6","channel_remain":"6"},{"channel_num":"9","channel_capacity":"6","channel_remain":"6"},{"channel_num":"10","channel_capacity":"6","channel_remain":"6"}]
     * status : 200
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * channel_num : 1
         * channel_capacity : 5
         * channel_remain : 5
         */

        private String channel_num;
        private String channel_capacity;
        private String channel_remain;
        private int channel_remains;


        public String getChannel_num() {
            return channel_num;
        }

        public void setChannel_num(String channel_num) {
            this.channel_num = channel_num;
        }

        public String getChannel_capacity() {
            return channel_capacity;
        }

        public void setChannel_capacity(String channel_capacity) {
            this.channel_capacity = channel_capacity;
        }

        public String getChannel_remain() {
            return channel_remain;
        }

        public void setChannel_remain(String channel_remain) {
            this.channel_remain = channel_remain;
        }

        public int getChannel_remains() {
            return channel_remains;
        }

        public void setChannel_remains(int channel_remains) {
            this.channel_remains = channel_remains;
        }
    }
}
