package com.tlp.vendspring.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.recycleview.AutoGridLayoutManager;
import com.tcn.uicommon.recycleview.PageRecyclerView;
import com.tcn.vendspring.R;
import com.tcn.vendspring.bean.TLPGoodsInfoBean;
import com.tcn.vendspring.shopping.FragmentSelection;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import controller.TlpUICommon;
import controller.UICommon;
import controller.UIGoodsInfo;


public class TlpGoodsFragment extends Fragment {
    private View contentView;
    private PageRecyclerView pageRecyclerView;
    private PageRecyclerView.PageAdapter pageAdapter;
    private TextView mTvPageSize;
    private LinearLayout select_pager_layout;
    private TLPGoodsInfoBean goodsInfoBean;
    private PageAdapterCallBack m_PageAdapterCallBack ;
    public TlpGoodsFragment() {
        // Required empty public constructor
    }
    public static TlpGoodsFragment newInstance(String param1, String param2) {
        TlpGoodsFragment fragment = new TlpGoodsFragment();
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
        contentView=inflater.inflate(R.layout.fragment_tlp_goods, container, false);
        init_view();
        return contentView;
    }
    private void init_view(){
        mTvPageSize= (TextView) contentView.findViewById(R.id.select_page);
        m_PageAdapterCallBack = new PageAdapterCallBack();
        initPage();
    }


    private class PageAdapterCallBack implements PageRecyclerView.CallBack {

        private UIGoodsInfo mInfo = null;

        int res = 0;

        public void setItemLayout(int layout) {
            res = layout;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(0, parent, false);
            view.setOnTouchListener(null);
            return new PageHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((PageHolder)holder).textname.setText("绿茶");
            TcnVendIF.getInstance().displayImage("http://00.minipic.eastday.com/20170823/20170823144739_d41d8cd98f00b204e9800998ecf8427e_2.jpeg", ((PageHolder) holder).imageGoods, R.mipmap.default_ticket_poster_pic);
        }

        @Override
        public void onItemClickListener(View view, int position) {
            //itemClick(position);
        }

        @Override
        public void onItemLongClickListener(View view, int position) {

        }

        @Override
        public void onItemTouchListener(View view, int position, MotionEvent event) {
            if ((null == view) || (null == event)) {

                return;
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                itemClick(position);
                ImageView mImageView = (ImageView)view.findViewById(R.id.img);
                if (mImageView != null) {
                    mImageView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.in_translate_top));
                }
            }
        }

        @Override
        public void onPageChange(int currentPage) {
            if ((mTvPageSize != null) && (pageRecyclerView != null)) {
                mTvPageSize.setText(currentPage+"/"+pageRecyclerView.getTotalPage());
            }
        }
    }

    private void initPage(){
        pageRecyclerView= (PageRecyclerView) contentView.findViewById(R.id.tlp_rcy_goods);
        mTvPageSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"xxxxxx",Toast.LENGTH_SHORT).show();
            }
        });
        //上一页下一页布局
        select_pager_layout= (LinearLayout) contentView.findViewById(R.id.select_page_layout);
        //设置行列数
        pageRecyclerView.setPageSize(3,3);
        pageRecyclerView.setHasFixedSize(true);

        m_PageAdapterCallBack.setItemLayout(TlpUICommon.getInstance().getSelectionItemLayout());
        pageAdapter = pageRecyclerView.new PageAdapter(TlpUICommon.getInstance().isPageDisplay(),30, TlpUICommon.getInstance().getRecyclerViewWidth(),
                TlpUICommon.getInstance().getRecyclerViewHeight(),m_PageAdapterCallBack);
        pageRecyclerView.setAdapter(pageAdapter);
        pageAdapter.setDataList(30);

    }

    private class PageHolder extends RecyclerView.ViewHolder {

        public ImageView imageGoods = null;
        public TextView textname = null;
        public TextView textcoilId = null;
        public TextView textprice = null;
        public ImageView imgprice = null;

        public PageHolder(View itemView) {
            super(itemView);
            imageGoods = (ImageView)itemView.findViewById(R.id.img);
            textname = (TextView) itemView.findViewById(R.id.textname);
            textcoilId = (TextView) itemView.findViewById(R.id.textcoilId);
            textprice = (TextView) itemView.findViewById(R.id.textprice);
            imgprice = (ImageView)itemView.findViewById(R.id.imgprice);
        }

    }

    private void itemClick(int position) {
		/*AnimationSet set =new AnimationSet(true);
      //  ScaleAnimation scaleAnim = new ScaleAnimation (1.0f,0.0f,1.0f,0.0f,0.5f,0.5f);
        VendIF.getInstance().LoggerDebug(TAG, "----onItemClick left: "+left+" top: "+top);
        TranslateAnimation tranAnim = new TranslateAnimation(
                Animation.ABSOLUTE,
                768,Animation.ABSOLUTE,view.getLeft(),
                Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0);
        set.addAnimation(tranAnim);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(800);
        stopPageTimer();
        view.startAnimation(set); */
      /*  TcnVendIF.getInstance().reqTouchSoundPlay();
        TcnVendIF.getInstance().reqSelectGoods(position);*/
        TcnVendIF.getInstance().ship(position+1,"00 FF 01 FE AA 55","","00 FF 01 FE AA 55");
    }

    @Override
    public void onPause() {
        super.onPause();
        pageAdapter.removeCallBack();
    }




}
