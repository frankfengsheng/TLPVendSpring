package com.tcn.vendspring.pay;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendEventResultID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendDBControl;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.resources.Resources;
import com.tcn.vendspring.R;
import com.tlp.vendspring.util.ZXingUtils;

import org.w3c.dom.Text;

import controller.TlpUICommon;
import controller.UICommon;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;


/**
 * Created by Administrator on 2016/10/15.
 */
public class TlpDialogPay extends Dialog {
    private Context m_context;
    private ImageView iv_pay_goods_picture;
    private ImageView iv_pay_erweima_picture;
    private TextView tv_goods_name;
    private TextView tv_goods_guige;
    private TextView tv_colse_timer;
    private TextView tv_pay_money;
    private Button m_btn_back;
    CountDownTimer timer;
    private String url;
    private int drawableResource;
    TimerStopInterface timerStopInterface;
    private String goodName,goodPrice,goodModle,goodUrl;
    public TlpDialogPay(@NonNull Context context,String url,int drawableResource,TimerStopInterface timerStopInterface,
                        String goodName,String goodPrice,String googModle,String goodUrl) {
        super(context,R.style.Dialog_bocop);
        this.url=url;
        this.drawableResource=drawableResource;
        this.timerStopInterface=timerStopInterface;
        this.goodName=goodName;
        this.goodPrice=goodPrice;
        this.goodModle=googModle;
        this.goodUrl=goodUrl;
        init(context);
    }

    private void init(Context context) {
        View contentView = View.inflate(context, TlpUICommon.getInstance().getPayLayout(), null);
        setContentView(contentView);
        m_context = context;
        iv_pay_goods_picture= (ImageView) findViewById(R.id.tlp_iv_pay_dialog_goods);
        iv_pay_erweima_picture= (ImageView) findViewById(R.id.tlp_iv_pay_dialog_small_erweima);
        tv_goods_guige= (TextView) findViewById(R.id.tlp_tv_pay_dialog_shop_guige);
        tv_goods_name= (TextView) findViewById(R.id.tlp_tv_pay_dialog_shopname);
        tv_colse_timer= (TextView) findViewById(R.id.tlp_tv_pay_dialog_close_time1);
        tv_pay_money= (TextView) findViewById(R.id.tlp_tv_pay_dialog_paymoney);

        if(!TextUtils.isEmpty(goodName))tv_goods_name.setText(goodName);
        if(!TextUtils.isEmpty(goodModle))tv_goods_guige.setText(goodModle);
        if(!TextUtils.isEmpty(goodPrice))tv_pay_money.setText(goodPrice);

        m_btn_back= (Button) findViewById(R.id.tlp_btn_pay_dialog_back);
        TlpDialogPay.ButtonClick m_BtnClickListener = new TlpDialogPay.ButtonClick();
        m_btn_back.setOnClickListener(m_BtnClickListener);

        Bitmap bitmap = ZXingUtils.createQRImage(url, 600, 600, BitmapFactory.decodeResource(context.getResources(),drawableResource));
        iv_pay_erweima_picture.setImageBitmap(bitmap);

        if(!TextUtils.isEmpty(goodUrl))Glide.with(context).load(goodUrl).into(iv_pay_goods_picture);
       // Glide.with(context).load("http://www.tonglepai.cn/Public/Home/images/member_wx.jpg").into(iv_pay_erweima_picture);
        if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
            try {
                TcnVendIF.getInstance().LoggerDebug(TAG, "pay logo start");
                if (TcnShareUseData.getInstance().isJidongOpen()) {
                    findViewById(R.id.jdlogo).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.jdlogo).setVisibility(View.GONE);

                }
                if (TcnShareUseData.getInstance().isAliPayOpen()) {
                    findViewById(R.id.alilogo).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.alilogo).setVisibility(View.GONE);

                }
                if (TcnShareUseData.getInstance().isAliPayOpen()) {
                    findViewById(R.id.wxlogo).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.wxlogo).setVisibility(View.GONE);
                }
            } catch (Exception e) {
                TcnVendIF.getInstance().LoggerDebug(TAG, "pay logo null");
            }
        }
       /* m_btn_back = (Button) findViewById(R.id.pay_back);
        m_btn_back.setOnClickListener(m_BtnClickListener);*/

       // initAnim(context);
        Window win = getWindow();
        win.setWindowAnimations(Resources.getAnimResourceID(com.tcn.uicommon.R.anim.alpha_in));
        if (TcnShareUseData.getInstance().isAdvertOnScreenBottom()) {
            win.setGravity(Gravity.CENTER);
        } else {
            win.setGravity(Gravity.CENTER);
        }
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1820;
            } else {
                lp.height = 912;
            }
            lp.y = 50;
        } else if (ImageController.SCREEN_TYPE_S1920X1080 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 980;
            lp.y = 50;
        } else if (ImageController.SCREEN_TYPE_S768X1360 == TcnVendIF.getInstance().getScreenType()) {
           /* if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1290;
            } else {
                lp.height = 858;
            }*/
            lp.y = 35;
          //  m_iPageSmallFont = 16;
        } else if (ImageController.SCREEN_TYPE_S1360X768 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 698;
            lp.y = 35;
        } else if (ImageController.SCREEN_TYPE_S768X1366 == TcnVendIF.getInstance().getScreenType()) {
           /* if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1296;
            } else {
                lp.height = 864;
            }*/
            lp.y = 35;
           // m_iPageSmallFont = 16;
        } else if (ImageController.SCREEN_TYPE_S1366X768 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 698;
            lp.y = 35;
        }
        else if (ImageController.SCREEN_TYPE_S480X800 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 490;
        }
        else if (ImageController.SCREEN_TYPE_S800X1280 == TcnVendIF.getInstance().getScreenType()) {
          //  lp.height = 780;
        } else if (ImageController.SCREEN_TYPE_S600X1024 == TcnVendIF.getInstance().getScreenType()) {
          //  lp.height = 628;
        } else if (ImageController.SCREEN_TYPE_S1024X600 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 704;
            lp.x = 420;
           // lp.height = 560;
            win.setGravity(Gravity.BOTTOM);

        } else if (ImageController.SCREEN_TYPE_S1280X800 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 750;
            lp.x = 430;
            //lp.height = 750;
            win.setGravity(Gravity.BOTTOM);
        } else if (ImageController.SCREEN_TYPE_S1680X1050 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 952;
            lp.y = 49;
        } else if (ImageController.SCREEN_TYPE_S1050X1680 == TcnVendIF.getInstance().getScreenType()) {
           /* if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1600;
            } else {
                lp.height = 1010;
            }*/
            lp.y = 40;
        } else if (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType()) {
           // lp.height = 660;
            lp.y = 30;
        } else if (ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) {
           /* if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1220;
            } else {
                lp.height = 815;
            }*/
            lp.y = 30;
           // m_iPageSmallFont = 14;
        }
        else {
        }
        //  lp.dimAmount = 0.2f;
        lp.dimAmount = 0.0f;
        win.setAttributes(lp);

    }

    /**
     * 开始 倒计时90s 倒计时结束之后关闭dialog
     */
    private void starTimer(){
          timer =new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_colse_timer.setText(millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                TcnVendIF.getInstance().reqEndEffectiveTime();
                dismiss();
            }
        }.start();
    }


    private class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id =v.getId();
            /*if (id == R.id.pay_wechat) {
                TcnVendIF.getInstance().reqWeixinCodeSelected();
            }  else if (id == R.id.pay_ali) {
                TcnVendIF.getInstance().reqAlipayCodeSelected();
            } else if (id == R.id.pay_cash) {

            }
            else */
            if (id == R.id.tlp_btn_pay_dialog_back) {
                dismiss();
            }
        }
    }

    @Override
    public void show() {
        super.show();
        //开始倒计时60s
        starTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
    //通知倒计时结束
  public   interface TimerStopInterface{
        void timerStop();
    }
}
