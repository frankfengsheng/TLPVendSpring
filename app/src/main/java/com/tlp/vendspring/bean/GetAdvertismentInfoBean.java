package com.tlp.vendspring.bean;

import java.util.List;

public class GetAdvertismentInfoBean {


    /**
     * data : [{"name":"国庆","url":"http://app.51mengshou.com.","type":"2","machine_list":"0","time_start":"1538323200","time_end":"1538841600","cust":"王1","cust_mobile":"13120866696"},{"name":"十一活动营销广告","url":"http://app.51mengshou.com.","type":"1","machine_list":"0","time_start":"1537977600","time_end":"1538236800","cust":"","cust_mobile":""}]
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
         * name : 国庆
         * url : http://app.51mengshou.com.
         * type : 2
         * machine_list : 0
         * time_start : 1538323200
         * time_end : 1538841600
         * cust : 王1
         * cust_mobile : 13120866696
         */

        private String name;
        private String url;
        private String type;
        private String machine_list;
        private String time_start;
        private String time_end;
        private String cust;
        private String cust_mobile;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMachine_list() {
            return machine_list;
        }

        public void setMachine_list(String machine_list) {
            this.machine_list = machine_list;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public String getCust() {
            return cust;
        }

        public void setCust(String cust) {
            this.cust = cust;
        }

        public String getCust_mobile() {
            return cust_mobile;
        }

        public void setCust_mobile(String cust_mobile) {
            this.cust_mobile = cust_mobile;
        }
    }
}
