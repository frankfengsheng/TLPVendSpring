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
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.bean.GetAdvertismentInfoBean;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MSBannerFragment extends Fragment  implements OnBannerListener {
    private Banner banner;
    private View contentView;
    private ArrayList<String> list_path=new ArrayList<>();
    private ArrayList<String> list_title=new ArrayList<>();


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

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播间隔时间
        banner.setDelayTime(10000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);


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

    /**
     * 获取广告
     * @param context
     * @return
     */
    public void getAdvertisment(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code", MSUserUtils.getInstance().getMachineCode(context));
        Call<GetAdvertismentInfoBean> call=loginInfoPost.getAdvertising(map);
        call.enqueue(new Callback<GetAdvertismentInfoBean>() {
            @Override
            public void onResponse(Call<GetAdvertismentInfoBean> call, Response<GetAdvertismentInfoBean> response) {
                GetAdvertismentInfoBean infoBean=response.body();
                if(infoBean!=null&&infoBean.getStatus()==200&&infoBean.getData()!=null&&infoBean.getData().size()>0){
                    refreshUI(infoBean);
                }
            }

            @Override
            public void onFailure(Call<GetAdvertismentInfoBean> call, Throwable t) {

            }
        });

    }

    private void refreshUI(GetAdvertismentInfoBean infoBean){
        list_path.clear();
        for(GetAdvertismentInfoBean.DataBean bean:infoBean.getData()){
            list_path.add(bean.getUrl());
            list_title.add("");
        }
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAdvertisment(getActivity());
    }
}
