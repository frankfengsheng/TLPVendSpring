package com.tlp.vendspring.bean;

public class GetPayOrderNumberResultInfoBean {


    /**
     * data : {"order_number":"H905428882824531"}
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
         * order_number : H905428882824531
         */

        private String order_number;
        private String status;

        public String getOrder_number() {
            return order_number;
        }

        public void setOrder_number(String order_number) {
            this.order_number = order_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
