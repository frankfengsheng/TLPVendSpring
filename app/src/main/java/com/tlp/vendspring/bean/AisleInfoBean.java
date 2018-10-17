package com.tlp.vendspring.bean;

import java.io.Serializable;
import java.util.List;

public class AisleInfoBean implements Serializable{


    /**
     * data : [{"machine_code":"10020030011","channel_num":"1","goods_id":"25","goods_name":"小丸子的姐","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"5","channel_remain":"2","status":"0","price_sales":"6.00","goods_shelf_life":"两周","goods_model":"500ml","goods_type_id":"7","goods_type_name":"酒水"},{"machine_code":"10020030011","channel_num":"2","goods_id":"29","goods_name":"绿茶","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"5","channel_remain":"0","status":"0","price_sales":"8.00","goods_shelf_life":"1年","goods_model":"GTX1050","goods_type_id":"14","goods_type_name":"嘟嘟嘟"},{"machine_code":"10020030011","channel_num":"3","goods_id":"32","goods_name":"芙蓉王","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"5","channel_remain":"0","status":"1","price_sales":"23.00","goods_shelf_life":"9999","goods_model":"20支","goods_type_id":"15","goods_type_name":"烟草"},{"machine_code":"10020030011","channel_num":"4","goods_id":"25","goods_name":"小丸子的姐","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"5","channel_remain":"0","status":"1","price_sales":"6.00","goods_shelf_life":"两周","goods_model":"500ml","goods_type_id":"7","goods_type_name":"酒水"},{"machine_code":"10020030011","channel_num":"5","goods_id":"25","goods_name":"小丸子的姐","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"5","channel_remain":"0","status":"1","price_sales":"6.00","goods_shelf_life":"两周","goods_model":"500ml","goods_type_id":"7","goods_type_name":"酒水"},{"machine_code":"10020030011","channel_num":"21","goods_id":"32","goods_name":"芙蓉王","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"8","channel_remain":"2","status":"1","price_sales":"5.00","goods_shelf_life":"180","goods_model":"20支","goods_type_id":"15","goods_type_name":"烟草"},{"machine_code":"10020030011","channel_num":"22","goods_id":"32","goods_name":"芙蓉王","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"8","channel_remain":"0","status":"1","price_sales":"5.00","goods_shelf_life":"180","goods_model":"20支","goods_type_id":"15","goods_type_name":"烟草"},{"machine_code":"10020030011","channel_num":"23","goods_id":"32","goods_name":"芙蓉王","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.     jpg","channel_capacity":"8","channel_remain":"0","status":"1","price_sales":"5.00","goods_shelf_life":"180","goods_model":"20支","goods_type_id":"15","goods_type_name":"烟草"},{"machine_code":"10020030011","channel_num":"30","goods_id":"24","goods_name":"小丸子哈哈789","goods_url":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg","channel_capacity":"6","channel_remain":"0","status":"1","price_sales":"5.00","goods_shelf_life":"两周","goods_model":"500ml","goods_type_id":"14","goods_type_name":"嘟嘟嘟"}]
     * type : 6
     * status : 200
     * msg : 成功
     */

    private String type;
    private int status;
    private String msg;
    private List<DataBean> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * machine_code : 10020030011
         * channel_num : 1
         * goods_id : 25
         * goods_name : 小丸子的姐
         * goods_url : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539670448596&di=2027b5b85b95d2097cc6c039c7e00685&imgtype=0&src=http%3A%2F%2F6225141.s21i-6.faiusr.com%2F2%2FABUIABACGAAg682PrAUohs79jQMw6Ac46Ac.jpg
         * channel_capacity : 5
         * channel_remain : 2
         * status : 0
         * price_sales : 6.00
         * goods_shelf_life : 两周
         * goods_model : 500ml
         * goods_type_id : 7
         * goods_type_name : 酒水
         */

        private String machine_code;
        private String channel_num;
        private String goods_id;
        private String goods_name;
        private String goods_url;
        private String channel_capacity;
        private String channel_remain;
        private String status;
        private String price_sales;
        private String goods_shelf_life;
        private String goods_model;
        private String goods_type_id;
        private String goods_type_name;

        public String getMachine_code() {
            return machine_code;
        }

        public void setMachine_code(String machine_code) {
            this.machine_code = machine_code;
        }

        public String getChannel_num() {
            return channel_num;
        }

        public void setChannel_num(String channel_num) {
            this.channel_num = channel_num;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getGoods_model() {
            return goods_model;
        }

        public void setGoods_model(String goods_model) {
            this.goods_model = goods_model;
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
    }
}
