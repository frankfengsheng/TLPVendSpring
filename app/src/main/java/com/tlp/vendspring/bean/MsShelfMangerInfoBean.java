package com.tlp.vendspring.bean;

import java.util.List;

public class MsShelfMangerInfoBean {


    /**
     * data : [{"goods_name":"可口可乐","goods_model":"500ml","price_sales":"3.20","goods_id":"3","channel_capacity":10,"channel_remain":10,"channel_start":"1","goods_url":"url123123","channel_end":"2"},{"goods_name":"百威啤酒经典纯正","goods_model":"500ml","price_sales":"3.33","goods_id":"2","channel_capacity":"3","channel_remain":"0","channel_start":"3","goods_url":"url123123","channel_end":"3"},{"goods_name":"百威啤酒经典纯正","goods_model":"500ml","price_sales":"3.33","goods_id":"2","channel_capacity":"3","channel_remain":"0","channel_start":"5","goods_url":"url123123","channel_end":"5"},{"goods_name":"可口可乐","goods_model":"500ml","price_sales":"3.33","goods_id":"3","channel_capacity":10,"channel_remain":0,"channel_start":"6","goods_url":"url123123","channel_end":"7"}]
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
         * goods_name : 可口可乐
         * goods_model : 500ml
         * price_sales : 3.20
         * goods_id : 3
         * channel_capacity : 10
         * channel_remain : 10
         * channel_start : 1
         * goods_url : url123123
         * channel_end : 2
         */

        private String goods_name;
        private String goods_model;
        private String price_sales;
        private String goods_id;
        private int channel_capacity;
        private int channel_remain;
        private String channel_start;
        private String goods_url;
        private String channel_end;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_model() {
            return goods_model;
        }

        public void setGoods_model(String goods_model) {
            this.goods_model = goods_model;
        }

        public String getPrice_sales() {
            return price_sales;
        }

        public void setPrice_sales(String price_sales) {
            this.price_sales = price_sales;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public int getChannel_capacity() {
            return channel_capacity;
        }

        public void setChannel_capacity(int channel_capacity) {
            this.channel_capacity = channel_capacity;
        }

        public int getChannel_remain() {
            return channel_remain;
        }

        public void setChannel_remain(int channel_remain) {
            this.channel_remain = channel_remain;
        }

        public String getChannel_start() {
            return channel_start;
        }

        public void setChannel_start(String channel_start) {
            this.channel_start = channel_start;
        }

        public String getGoods_url() {
            return goods_url;
        }

        public void setGoods_url(String goods_url) {
            this.goods_url = goods_url;
        }

        public String getChannel_end() {
            return channel_end;
        }

        public void setChannel_end(String channel_end) {
            this.channel_end = channel_end;
        }
    }
}
