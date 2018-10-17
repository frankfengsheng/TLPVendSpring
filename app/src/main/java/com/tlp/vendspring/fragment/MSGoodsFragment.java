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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.recycleview.PageRecyclerView;
import com.tcn.vendspring.R;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tlp.vendspring.MSUIUtils;
import com.tlp.vendspring.netutil.TLPApiServices;

import java.util.HashMap;
import java.util.Map;

import controller.TlpUICommon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MSGoodsFragment extends Fragment implements View.OnClickListener{
    private View contentView;
    private PageRecyclerView pageRecyclerView;
    private PageRecyclerView.PageAdapter pageAdapter;
    private TextView mTvPageSize;
    private LinearLayout select_pager_layout;
    private MSGoodsInfoBean goodsInfoBean;
    private PageAdapterCallBack m_PageAdapterCallBack ;

    private Goods_info gs_info;
    private Button btn_last;
    private Button btn_next;
    public MSGoodsFragment() {
        // Required empty public constructor
    }
    public static MSGoodsFragment newInstance(String param1, String param2) {
        MSGoodsFragment fragment = new MSGoodsFragment();
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
        //网络获取
        GetGoodsInfo(getActivity().getApplicationContext());

    }



    private class PageAdapterCallBack implements PageRecyclerView.CallBack {

        int res = 0;

        public void setItemLayout(int layout) {
            res = layout;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tlp_layout_commodity_item_goods, parent, false);

            return new PageHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((PageHolder)holder).textname.setText(goodsInfoBean.getData().get(position).getGoods_name());
            ((PageHolder)holder).textprice.setText(goodsInfoBean.getData().get(position).getPrice_sales());
           // Glide.with(getActivity()).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2621483064,3869409487&fm=27&gp=0.jpg").into(((PageHolder) holder).imageGoods);
            MSUIUtils.getInstance().displayImage(goodsInfoBean.getData().get(position).getGoods_url(), ((PageHolder) holder).imageGoods, R.mipmap.default_ticket_poster_pic,getActivity());

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
        pageRecyclerView.setCanScroll(false);
        pageRecyclerView.setCustomized(true);
        //上一页下一页布局
        select_pager_layout= (LinearLayout) contentView.findViewById(R.id.select_page_layout);
        btn_last= (Button) contentView.findViewById(R.id.select_pre);
        btn_next= (Button) contentView.findViewById(R.id.select_next);
        //设置行列数
        pageRecyclerView.setPageSize(3,4);
        pageRecyclerView.setHasFixedSize(true);

        btn_last.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        m_PageAdapterCallBack.setItemLayout(TlpUICommon.getInstance().getSelectionItemLayout());
        pageAdapter = pageRecyclerView.new PageAdapter(0,m_PageAdapterCallBack);
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

    /**
     * 获取设备端展示商品
     * @param context
     * @return
     */
    public MSGoodsInfoBean GetGoodsInfo(final Context context){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code","10020030011");
        Call<MSGoodsInfoBean> call=loginInfoPost.getgoods(map);
        call.enqueue(new Callback<MSGoodsInfoBean>() {
            @Override
            public void onResponse(Call<MSGoodsInfoBean> call, Response<MSGoodsInfoBean> response) {
                goodsInfoBean=response.body();
                pageAdapter.setDataList(goodsInfoBean.getData().size());
               /* Log.i("TLPJSON",response.body().getMsg());
                Toast.makeText(context,response.body().getStatus()+response.body().getData().get(0).getGoods_name()+"",Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onFailure(Call<MSGoodsInfoBean> call, Throwable t) {

            }
        });
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.select_pre:
                if (pageRecyclerView != null) {
                    pageRecyclerView.pageDown();
                }
                break;
            case R.id.select_next:
                if (pageRecyclerView != null) {
                    pageRecyclerView.pageUp();
                }
                break;
        }
    }
}
