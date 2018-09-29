package com.tcn.background.Entity;

import java.util.List;

public class WeatherEntity {


    /**
     * error : false
     * results : [{"_id":"5b977a759d212206c1b383d3","createdAt":"2018-09-11T08:19:01.268Z","desc":"手把手教你实现抖音视频特效","publishedAt":"2018-09-19T00:00:00.0Z","source":"web","type":"Android","url":"https://www.jianshu.com/p/5bb7f2a0da90","used":true,"who":"xue5455"},{"_id":"5b98b9f59d212206c73cd727","createdAt":"2018-09-12T07:02:13.931Z","desc":" LogCollector：一个收集 app 输出日志的工具","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchbaadj30u01hcta5","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchjeypj30u01hc0y3","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchpj7bj30tv1h9q5y","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchw320j30u01hcgow"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/ljuns/LogCollector","used":true,"who":"ljuns"},{"_id":"5b98be089d212206cae44993","createdAt":"2018-09-12T07:19:36.23Z","desc":"一个简单仿微信朋友圈的图片查看器","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcixf0yg30go0tnb2b","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcjxstyg30go0tn4qs"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/wanglu1209/PhotoViewer","used":true,"who":"wanglu1209"},{"_id":"5b9cc8fe9d212206c4385c34","createdAt":"2018-09-15T08:55:26.301Z","desc":"不用重新打包，应用内一键切换正式/测试环境","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcosopxg30f00qo48e"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/CodeXiaoMai/EnvironmentSwitcher","used":true,"who":"小迈"},{"_id":"5ba18d009d2122031ebe97fc","createdAt":"2018-09-18T23:40:48.207Z","desc":"显示等待加载状态的View。","publishedAt":"2018-09-19T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/ImKarl/WaitView","used":true,"who":"lijinshanmx"},{"_id":"5ba18dae9d21220316f09f5c","createdAt":"2018-09-18T23:43:42.411Z","desc":"重写LinearLayout，仿淘宝商品详情页，上拉查看更多详情。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcts33ag308x0fxb2h"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/LineChen/TwoPageLayout","used":true,"who":"lijinshanmx"},{"_id":"5ba18e1a9d2122031c8cde68","createdAt":"2018-09-18T23:45:30.53Z","desc":"快速实现圆角，边框，State各个状态的UI样式。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcupieqg30a60i6e4n","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcuubrfj30a70g40wh","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcuz05hg30a80g440v","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcv4koej30aa0g6gmf","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcv8jznj30a50g2n11"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/RuffianZhong/RWidgetHelper","used":true,"who":"lijinshanmx"},{"_id":"5ba18e759d21220316f09f5e","createdAt":"2018-09-18T23:47:01.968Z","desc":"RecyclerView 点击&长按事件、分割线、拖曳排序、滑动删除。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcw0vhrg308c0eogpf"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/OCNYang/RecyclerViewEvent","used":true,"who":"lijinshanmx"},{"_id":"5ba205fa9d212261127b79be","createdAt":"2018-09-19T08:16:58.239Z","desc":"通过标签直接生成shape，无需再写shape.xml。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexcwcthhg309n0k54fb"],"publishedAt":"2018-09-19T00:00:00.0Z","source":"chrome","type":"Android","url":"https://github.com/JavaNoober/BackgroundLibrary","used":true,"who":"lijinshanmx"},{"_id":"5b8ba0249d21224702457636","createdAt":"2018-09-11T07:34:17.647Z","desc":"轻量级安卓水印框架，支持隐形数字水印。","images":["https://ww1.sinaimg.cn/large/0073sXn7ly1fv5nqgugg9j31gs0l8b29","https://ww1.sinaimg.cn/large/0073sXn7ly1fv5nqi3xhvj31f80l2e81"],"publishedAt":"2018-09-11T00:00:00.0Z","source":"web","type":"Android","url":"https://github.com/huangyz0918/AndroidWM","used":true,"who":"Yizheng Huang "}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 5b977a759d212206c1b383d3
         * createdAt : 2018-09-11T08:19:01.268Z
         * desc : 手把手教你实现抖音视频特效
         * publishedAt : 2018-09-19T00:00:00.0Z
         * source : web
         * type : Android
         * url : https://www.jianshu.com/p/5bb7f2a0da90
         * used : true
         * who : xue5455
         * images : ["https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchbaadj30u01hcta5","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchjeypj30u01hc0y3","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchpj7bj30tv1h9q5y","https://ww1.sinaimg.cn/large/0073sXn7ly1fvexchw320j30u01hcgow"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
