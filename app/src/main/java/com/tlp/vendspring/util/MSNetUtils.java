package com.tlp.vendspring.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.tcn.vendspring.netUtil.RetrofitClient;
import com.tcn.vendspring.pay.TlpDialogPay;
import com.tlp.vendspring.bean.GetPayOrderNumberResultInfoBean;
import com.tlp.vendspring.bean.PaySuccessulGetAisleNumberInfoBean;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MSNetUtils {
      private  Context context;
      private  android.os.Handler handler;
      private  String orderNumber;
      private  PayStateCallBack payStateCallBack;
      private   TlpDialogPay  m_DialogPay;
    public MSNetUtils(Context context,PayStateCallBack payStateCallBack){
        handler=new android.os.Handler();
        this.context=context;
        this.payStateCallBack=payStateCallBack;
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            CheckPayState(context,orderNumber);
            handler.postDelayed(this,3000);
        }
    };

    /**
     * 生成支付订单
     * @param context
     * @return
     */
    public void getOrderNumber(final Context context, String good_id, final String price_sales, final String goods_name, final String goodModle, final String goodUrl){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",MSUserUtils.getInstance().getMachineCode(context));
        map.put("goods_id",good_id);
        map.put("price_sales",price_sales);
        map.put("goods_name",goods_name);
        Call<GetPayOrderNumberResultInfoBean> call=loginInfoPost.getPayOrderNumber(map);
        call.enqueue(new Callback<GetPayOrderNumberResultInfoBean>() {
            @Override
            public void onResponse(Call<GetPayOrderNumberResultInfoBean> call, Response<GetPayOrderNumberResultInfoBean> response) {
                GetPayOrderNumberResultInfoBean bean=response.body();
                if(bean!=null&&bean.getStatus()==200)
                {
                    orderNumber=bean.getData().getOrder_number();
                    String url="http://wx.51mengshou.com/home/showg/index/orderno/"+orderNumber;
                    if(!TextUtils.isEmpty(url)) {
                       m_DialogPay = new TlpDialogPay(context, url,0, null,goods_name,
                                price_sales,goodModle,goodUrl);
                        m_DialogPay.show();
                        starPayTimer();//开启倒计时
                        handler.postDelayed(runnable, 0);
                    }
                }else if(bean!=null&&bean.getStatus()==198){
                    ToastUtil.showToast(context,"设备已停用,暂时不能购买");
                }
            }

            @Override
            public void onFailure(Call<GetPayOrderNumberResultInfoBean> call, Throwable t) {

            }
        });

    }

    /**
     * 开始 倒计时120s 倒计时结束之后关闭支付状态查询
     */
    private void starPayTimer(){
        CountDownTimer paytimer =new CountDownTimer(120*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if(context!=null) handler.removeCallbacks(runnable);
                cancel();
            }
        }.start();
    }

    /**
     * 查询支付状态
     * @param context
     * @return
     */
    public void CheckPayState(final Context context, final String orderNumber){
        Retrofit retrofit =new RetrofitClient().getRetrofit(context);
        TLPApiServices loginInfoPost=retrofit.create(TLPApiServices.class);
        Map map=new HashMap();
        map.put("machine_code",MSUserUtils.getInstance().getMachineCode(context));
        map.put("order_number",orderNumber);
        Call<PaySuccessulGetAisleNumberInfoBean> call=loginInfoPost.paySuccessGetAisleNumber(map);
        call.enqueue(new Callback<PaySuccessulGetAisleNumberInfoBean>() {
            @Override
            public void onResponse(Call<PaySuccessulGetAisleNumberInfoBean> call, Response<PaySuccessulGetAisleNumberInfoBean> response) {
                PaySuccessulGetAisleNumberInfoBean bean=response.body();
                if(bean!=null&&bean.getData()!=null&&bean.getStatus()==200){
                   String  aisleNumber=bean.getData().getChannel_num();
                    handler.removeCallbacks(runnable);
                    payStateCallBack.paySucess(aisleNumber,orderNumber);
                    if(m_DialogPay!=null)m_DialogPay.dismiss();

                }else {
                     handler.removeCallbacks(runnable);
                    payStateCallBack.payFailed();
                    if(m_DialogPay!=null)m_DialogPay.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PaySuccessulGetAisleNumberInfoBean> call, Throwable t) {
                payStateCallBack.payFailed();
                handler.removeCallbacks(runnable);
                if(m_DialogPay!=null)m_DialogPay.dismiss();
            }
        });

    }

   public static interface PayStateCallBack{
        void paySucess(String aisleNumber,String orderNumber);
        void payFailed();
    }
}
