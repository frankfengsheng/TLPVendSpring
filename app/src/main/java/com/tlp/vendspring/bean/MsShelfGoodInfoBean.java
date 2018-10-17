package com.tlp.vendspring.bean;

import java.util.List;

public class MsShelfGoodInfoBean {

    /**
     * data : [{"goods_id":"2","goods_type_id":"1","goods_type_name":"","goods_code":"JQH51651","goods_name":"百威啤酒经典纯正","goods_model":"500ml","goods_shelf_life":"15天","price_sales":"5.00"},{"goods_id":"3","goods_type_id":"1","goods_type_name":"","goods_code":"JQH51652","goods_name":"可口可乐","goods_model":"500ml","goods_shelf_life":"15天","price_sales":"5.00"}]
     * msg : 成功
     * status : 200
     * capacity : 5
     */

    private String msg;
    private int status;
    private String capacity;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * goods_id : 2
         * goods_type_id : 1
         * goods_type_name :
         * goods_code : JQH51651
         * goods_name : 百威啤酒经典纯正
         * goods_model : 500ml
         * goods_shelf_life : 15天
         * price_sales : 5.00
         */

        private String goods_id;
        private String goods_type_id;
        private String goods_type_name;
        private String goods_code;
        private String goods_name;
        private String goods_model;
        private String goods_shelf_life;
        private String price_sales;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_type_id() {
            return goods_type_id;
        }

        public void setGoods_type_id(String goods_type_id) {
            this.goods_type_id = goods_type_id;
        }

        public String getGoods_type_name() {
            return goods_type_name;
        }

        public void setGoods_type_name(String goods_type_name) {
            this.goods_type_name = goods_type_name;
        }

        public String getGoods_code() {
            return goods_code;
        }

        public void setGoods_code(String goods_code) {
            this.goods_code = goods_code;
        }

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

        public String getGoods_shelf_life() {
            return goods_shelf_life;
        }

        public void setGoods_shelf_life(String goods_shelf_life) {
            this.goods_shelf_life = goods_shelf_life;
        }

        public String getPrice_sales() {
            return price_sales;
        }

        public void setPrice_sales(String price_sales) {
            this.price_sales = price_sales;
        }
    }
}
