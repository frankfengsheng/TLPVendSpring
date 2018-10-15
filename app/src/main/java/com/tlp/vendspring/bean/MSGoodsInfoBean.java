package com.tlp.vendspring.bean;

import java.util.List;

public class MSGoodsInfoBean {


    /**
     * data : [{"goods_name":"苹果","goods_url":"urlurlimg·········","goods_id":"2","channel_remain":"2","goods_model":"123123","price_sales":"3.00","goods_shelf_life":"180天"}]
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
         * goods_name : 苹果
         * goods_url : urlurlimg·········
         * goods_id : 2
         * channel_remain : 2
         * goods_model : 123123
         * price_sales : 3.00
         * goods_shelf_life : 180天
         */

        private String goods_name;
        private String goods_url;
        private String goods_id;
        private String channel_remain;
        private String goods_model;
        private String price_sales;
        private String goods_shelf_life;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_url() {
            return goods_url;
        }

        public void setGoods_url(String goods_url) {
            this.goods_url = goods_url;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getChannel_remain() {
            return channel_remain;
        }

        public void setChannel_remain(String channel_remain) {
            this.channel_remain = channel_remain;
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

        public String getGoods_shelf_life() {
            return goods_shelf_life;
        }

        public void setGoods_shelf_life(String goods_shelf_life) {
            this.goods_shelf_life = goods_shelf_life;
        }
    }
}
