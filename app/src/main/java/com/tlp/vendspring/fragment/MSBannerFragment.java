package com.tlp.vendspring.fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tcn.vendspring.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;


public class MSBannerFragment extends Fragment  implements OnBannerListener {
    private Banner banner;
    private View contentView;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    public MSBannerFragment() {
        // Required empty public constructor
    }
    public static MSBannerFragment newInstance(String param1, String param2) {
        MSBannerFragment fragment = new MSBannerFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView=inflater.inflate(R.layout.fragment_tlp_banner, container, false);
        init_view();
        return contentView;
    }
    private void init_view(){
        banner = (Banner) contentView.findViewById(R.id.banner);
        banner.setViewPagerIsScroll(false);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_path.add("http://00.minipic.eastday.com/20170823/20170823144739_d41d8cd98f00b204e9800998ecf8427e_2.jpeg");
        list_path.add("https://b-ssl.duitang.com/uploads/item/201706/29/20170629212300_NemC2.jpeg");
        list_path.add("http://a.hiphotos.baidu.com/zhidao/pic/item/03087bf40ad162d97296c91716dfa9ec8a13cd45.jpg");
        list_path.add("http://b.hiphotos.baidu.com/zhidao/pic/item/e850352ac65c103882ee7361ba119313b17e8985.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    @Override
    public void OnBannerClick(int position) {

    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

}
