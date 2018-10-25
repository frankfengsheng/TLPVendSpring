package com.tlp.vendspring.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.dialog.LoadingDialog;
import com.tcn.uicommon.recycleview.PageRecyclerView;
import com.tcn.vendspring.R;
import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tcn.vendspring.pay.TlpDialogPay;
import com.tlp.vendspring.MSUIUtils;
import com.tlp.vendspring.bean.MSGoodsInfoBean;
import com.tlp.vendspring.bean.MsClearShelfInfoBean;
import com.tlp.vendspring.util.MSNetUtils;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.TLPApiServices;
import com.tlp.vendspring.util.ToastUtil;

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
    private TlpDialogPay m_DialogPay = null;
    public static String mOrderNumber;//订单号
    public static String mAisleNumber;
    private TextView tv_noData;
  //从弹出二维码支付页面开始90秒内一直刷新支付状态
    Handler handler=new Handler();
    MSGoodsInfoBean.DataBean selectgoodBean;
    LoadingDialog loadingDialog;


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
        loadingDialog=new LoadingDialog(getActivity(),"数据正在加载中","请稍后...");
        loadingDialog.show();
        mTvPageSize= (TextView) contentView.findViewById(R.id.select_page);
        tv_noData= (TextView) contentView.findViewById(R.id.goods_shimmer_tv_loading);
        m_PageAdapterCallBack = new PageAdapterCallBack();
        initPage();
        handler.postDelayed(runnable2,1000*60*30);

    }

    Runnable runnable2=new Runnable() {
        @Override
        public void run() {
            GetGoodsInfo(getActivity());
            handler.postDelayed(this,1000*60*30);
        }
    };
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
            MSUIUtils.getInstance().displayImage(goodsInfoBean.getData().get(position).getGoods_url(), ((PageHolder) holder).imageGoods, R.mipmap.default_ticket_poster_pic, getActivity());
           // Glide.with(getActivity()).load("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2621483064,3869409487&fm=27&gp=0.jpg").into(((PageHolder) holder).imageGoods);
            if(Integer.parseInt(goodsInfoBean.getData().get(position).getChannel_remain())>0) {
                ((PageHolder) holder).iv_onSale.setVisibility(View.GONE);
            }else {
                ((PageHolder) holder).iv_onSale.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public void onItemClickListener(View view, int position) {
            //TcnVendIF.getInstance().ship(position+1,"00 FF 01 FE AA 55","","00 FF 01 FE AA 55");
           //判断是否锁机
          /*  if (TcnVendIF.getInstance().isMachineLocked()) {
                TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_MACHINE_LOCKED, -1, -1, -1, getActivity().getString(com.tcn.funcommon.R.string.tip_machine_locked));
            }else {*/
                if (goodsInfoBean != null && goodsInfoBean.getData() != null) {
                    if (Integer.parseInt(goodsInfoBean.getData().get(position).getChannel_remain()) > 0) {
                        selectgoodBean = goodsInfoBean.getData().get(position);
                        new MSNetUtils(getActivity(), new MSNetUtils.PayStateCallBack() {
                            @Override
                            public void paySucess(String aisleNumber, String orderNumber) {
                                MSGoodsFragment.mOrderNumber=orderNumber;
                                MSGoodsFragment.mAisleNumber=aisleNumber;
                                paySucessedToShip(aisleNumber);
                            }

                            @Override
                            public void payFailed() {

                            }
                        }).getOrderNumber(getActivity(), selectgoodBean.getGoods_id(), selectgoodBean.getPrice_sales(), selectgoodBean.getGoods_name(),
                                selectgoodBean.getGoods_model(), selectgoodBean.getGoods_url());
                    } else {
                        ToastUtil.showToast(getActivity(), "该商品已售罄");
                    }
                }


        }

        @Override
        public void onItemLongClickListener(View view, int position) {

        }

        @Override
        public void onItemTouchListener(View view, int position, MotionEvent event) {
            if ((null == view) || (null == event)) {

                return;
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
        public ImageView iv_onSale;

        public PageHolder(View itemView) {
            super(itemView);
            imageGoods = (ImageView)itemView.findViewById(R.id.img);
            textname = (TextView) itemView.findViewById(R.id.textname);
            textcoilId = (TextView) itemView.findViewById(R.id.textcoilId);
            textprice = (TextView) itemView.findViewById(R.id.textprice);
            imgprice = (ImageView)itemView.findViewById(R.id.imgprice);
            iv_onSale= (ImageView) itemView.findViewById(R.id.iv_sellout);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        //网络获取
        GetGoodsInfo(getActivity().getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();

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
        map.put("machine_code", MSUserUtils.getInstance().getMachineCode(context));
        Call<MSGoodsInfoBean> call=loginInfoPost.getgoods(map);
        call.enqueue(new Callback<MSGoodsInfoBean>() {
            @Override
            public void onResponse(Call<MSGoodsInfoBean> call, Response<MSGoodsInfoBean> response) {
                if(loadingDialog!=null) loadingDialog.dismiss();
                goodsInfoBean=response.body();
                if(goodsInfoBean!=null&&goodsInfoBean.getStatus()==200&&goodsInfoBean.getData()!=null&&goodsInfoBean.getData().size()>0) {
                    tv_noData.setVisibility(View.GONE);
                    pageAdapter.setDataList(goodsInfoBean.getData().size());
                    pageRecyclerView.setVisibility(View.VISIBLE);
                }else if(goodsInfoBean!=null&&goodsInfoBean.getData()==null){
                    pageRecyclerView.setVisibility(View.GONE);
                    tv_noData.setVisibility(View.VISIBLE);
                    tv_noData.setText("此设备暂无数据，请在管理员后台页面添加");
                }else {
                    pageRecyclerView.setVisibility(View.GONE);
                    tv_noData.setVisibility(View.VISIBLE);
                    tv_noData.setText("此设备暂无数据，请在管理员后台页面添加");
                }
            }

            @Override
            public void onFailure(Call<MSGoodsInfoBean> call, Throwable t) {
                if(loadingDialog!=null) loadingDialog.dismiss();
                tv_noData.setText("数据加载失败，请重启售货机");
            }
        });
        return null;
    }

    /**
     * 出货成功通知服务器
     * @param context
     * @return
     */
    public void shipSuc(final Context context,String orderNumber,String channel_num){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",MSUserUtils.getInstance().getMachineCode(context));
        map.put("order_number",mOrderNumber);
        map.put("channel_num",mAisleNumber);
        Call<MsClearShelfInfoBean> call=loginInfoPost.shipSucessed(map);
        call.enqueue(new Callback<MsClearShelfInfoBean>() {
            @Override
            public void onResponse(Call<MsClearShelfInfoBean> call, Response<MsClearShelfInfoBean> response) {
               MsClearShelfInfoBean bean=response.body();
               if(bean!=null&&bean.getStatus()==200){
                   ToastUtil.showToast(context,"通知服务器成功");
               }
            }
            @Override
            public void onFailure(Call<MsClearShelfInfoBean> call, Throwable t) {

            }
        });
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
    private void paySucessedToShip(String aisleNumber){
        if(!TextUtils.isEmpty(aisleNumber)) {
            int aisleNO = Integer.parseInt(aisleNumber);
            TcnVendIF.getInstance().ship(aisleNO, "00 FF 01 FE AA 55", "", "00 FF 01 FE AA 55");
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(m_DialogPay!=null)m_DialogPay.dismiss();

    }

    //出货成功
    public void shipSucess(){
        shipSuc(getActivity(),mOrderNumber,mAisleNumber);
        GetGoodsInfo(getActivity());

    }

    //出货失败
    public void shipFailed(){

        GetGoodsInfo(getActivity());
    }


}
