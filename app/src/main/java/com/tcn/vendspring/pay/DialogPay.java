package com.tcn.vendspring.pay;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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

import controller.UICommon;


/**
 * Created by Administrator on 2016/10/15.
 */
public class DialogPay extends Dialog {
    private static final String TAG = "DialogPay";

    private int m_iPageSmallFont = 20;
    private SpannableStringBuilder m_stringBuilder = null;
    private AbsoluteSizeSpan m_textSizeSpan = null;

    private Button m_btn_back = null;

    private RelativeLayout m_pay_qrcode_load_layout_wx = null;
    private RelativeLayout m_pay_qrcode_layout_wx = null;
    private RelativeLayout m_pay_qrcode_load_layout_ali = null;
    private RelativeLayout m_pay_qrcode_layout_ali = null;
    private TextView m_pay_goods_name = null;
    private TextView m_pay_goods_slotno = null;
    private TextView m_pay_goods_price = null;
    private TextView m_pay_goods_introduction = null;
    private ImageView m_pay_goods_picture = null;
    private ImageView m_pay_logo_wx = null;
    private ImageView m_pay_logo_ali = null;
    private ImageView m_pay_loading_ali = null;
    private ImageView m_pay_qrcode_wx = null;
    private ImageView m_pay_qrcode_ali = null;
    private ImageView m_pay_loading_wx = null;

    private TextView m_pay_tips = null;
    private TextView m_pay_qrcode_tips_wx = null;
    private TextView m_pay_qrcode_tips_ali = null;
    private TextView m_pay_time = null;
    private TextView m_pay_loading_tips_wx = null;
    private TextView m_pay_loading_tips_ali = null;
    private RotateAnimation m_AnimLoad;
    private Animation m_AnimGoods = null;
    private Context m_context = null;
    private Animation anim_pay;

    public DialogPay(Context context) {
        super(context, R.style.Dialog_bocop);
        init(context);
    }

    @Override
    public void show() {
        super.show();
        TcnVendIF.getInstance().LoggerDebug(TAG, "show()");
        TcnVendIF.getInstance().registerListener(m_vendListener);

        initShow();

        UICommon.getInstance().setPayShow(true);
        UICommon.getInstance().setCanRefund(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        TcnVendIF.getInstance().LoggerDebug(TAG, "dismiss()");
        if (!UICommon.getInstance().isPayShowing()) {
            return;
        }
        UICommon.getInstance().setPayShow(false);
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
        initDismiss();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    private void init(Context context) {
        View contentView = View.inflate(context, UICommon.getInstance().getPayLayout(), null);
        setContentView(contentView);
        m_context = context;
        m_pay_goods_picture = (ImageView) findViewById(R.id.pay_goods_picture);
        m_pay_goods_name = (TextView) findViewById(R.id.pay_goods_name);
        m_pay_goods_slotno = (TextView) findViewById(R.id.pay_goods_slotno);
        m_pay_goods_price = (TextView) findViewById(R.id.pay_goods_price);
        m_pay_goods_introduction = (TextView) findViewById(R.id.pay_goods_introduction);

        m_pay_logo_wx = (ImageView) findViewById(R.id.pay_logo_wx);
        m_pay_loading_wx = (ImageView) findViewById(R.id.pay_loading_wx);
        m_pay_qrcode_load_layout_wx = (RelativeLayout) findViewById(R.id.pay_qrcode_load_layout_wx);
        m_pay_qrcode_layout_wx = (RelativeLayout) findViewById(R.id.pay_qrcode_layout_wx);
        m_pay_qrcode_wx = (ImageView) findViewById(R.id.pay_qrcode_wx);
        m_pay_qrcode_tips_wx = (TextView) findViewById(R.id.pay_qrcode_tips_wx);

        m_pay_tips = (TextView) findViewById(R.id.pay_tips);

        m_pay_time = (TextView) findViewById(R.id.pay_time);
        m_pay_loading_tips_wx = (TextView) findViewById(R.id.pay_loading_tips_wx);

        m_pay_logo_ali = (ImageView) findViewById(R.id.pay_logo_ali);
        m_pay_loading_ali = (ImageView) findViewById(R.id.pay_loading_ali);
        m_pay_qrcode_load_layout_ali = (RelativeLayout) findViewById(R.id.pay_qrcode_load_layout_ali);
        m_pay_qrcode_layout_ali = (RelativeLayout) findViewById(R.id.pay_qrcode_layout_ali);
        m_pay_qrcode_ali = (ImageView) findViewById(R.id.pay_qrcode_ali);
        m_pay_qrcode_tips_ali = (TextView) findViewById(R.id.pay_qrcode_tips_ali);
        m_pay_loading_tips_ali = (TextView) findViewById(R.id.pay_loading_tips_ali);
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


        /*m_pay_select_layout = (RelativeLayout) findViewById(R.id.pay_select_layout);

        m_pay_wechat = (ImageView) findViewById(R.id.pay_wechat);
        m_pay_wechat.setOnClickListener(m_BtnClickListener);
        m_pay_ali = (ImageView) findViewById(R.id.pay_ali);
        m_pay_ali.setOnClickListener(m_BtnClickListener);
        m_pay_cash = (ImageView) findViewById(R.id.pay_cash);
        m_pay_cash.setOnClickListener(m_BtnClickListener);*/
        m_btn_back = (Button) findViewById(R.id.pay_back);
        m_btn_back.setOnClickListener(m_BtnClickListener);

        initAnim(context);

        Window win = getWindow();
        win.setWindowAnimations(Resources.getAnimResourceID(com.tcn.uicommon.R.anim.alpha_in));
        if (TcnShareUseData.getInstance().isAdvertOnScreenBottom()) {
            win.setGravity(Gravity.TOP);
        } else {
            win.setGravity(Gravity.BOTTOM);
        }
        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        if (ImageController.SCREEN_TYPE_S1080X1920 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1820;
            } else {
                lp.height = 1212;
            }
            lp.y = 50;
        } else if (ImageController.SCREEN_TYPE_S1920X1080 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 980;
            lp.y = 50;
        } else if (ImageController.SCREEN_TYPE_S768X1360 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1290;
            } else {
                lp.height = 858;
            }
            lp.y = 35;
            m_iPageSmallFont = 16;
        } else if (ImageController.SCREEN_TYPE_S1360X768 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 698;
            lp.y = 35;
        } else if (ImageController.SCREEN_TYPE_S768X1366 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1296;
            } else {
                lp.height = 864;
            }
            lp.y = 35;
            m_iPageSmallFont = 16;
        } else if (ImageController.SCREEN_TYPE_S1366X768 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 698;
            lp.y = 35;
        }
        else if (ImageController.SCREEN_TYPE_S480X800 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 490;
        }
        else if (ImageController.SCREEN_TYPE_S800X1280 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 780;
        } else if (ImageController.SCREEN_TYPE_S600X1024 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 628;
        } else if (ImageController.SCREEN_TYPE_S1024X600 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 604;
            lp.x = 420;
            lp.height = 560;
            win.setGravity(Gravity.BOTTOM);

        } else if (ImageController.SCREEN_TYPE_S1280X800 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 750;
            lp.x = 430;
            lp.height = 750;
            win.setGravity(Gravity.BOTTOM);
        } else if (ImageController.SCREEN_TYPE_S1680X1050 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 952;
            lp.y = 49;
        } else if (ImageController.SCREEN_TYPE_S1050X1680 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1600;
            } else {
                lp.height = 1010;
            }
            lp.y = 40;
        } else if (ImageController.SCREEN_TYPE_S1280X720 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 660;
            lp.y = 30;
        } else if (ImageController.SCREEN_TYPE_S720X1280 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1220;
            } else {
                lp.height = 815;
            }
            lp.y = 30;
            m_iPageSmallFont = 14;
        }
        else {
        }
        //  lp.dimAmount = 0.2f;
        lp.dimAmount = 0.0f;
        win.setAttributes(lp);
    }

    public void deInit() {
        if (m_btn_back != null) {
            m_btn_back.setOnClickListener(null);
            m_btn_back = null;
        }
        stopAnimLoad();
        stopAnimGoods();
        if (anim_pay != null) {
            anim_pay.setAnimationListener(null);
            anim_pay = null;
        }
        m_pay_qrcode_load_layout_wx = null;
        m_pay_qrcode_layout_wx = null;
        m_pay_qrcode_load_layout_ali = null;
        m_pay_qrcode_layout_ali = null;
        m_pay_goods_name = null;
        m_pay_goods_slotno = null;
        m_pay_goods_price = null;
        m_pay_goods_picture = null;
        m_pay_goods_introduction = null;
        m_pay_logo_wx = null;
        m_pay_logo_ali = null;
        m_pay_loading_ali = null;

        if (m_pay_qrcode_wx != null) {
            m_pay_qrcode_wx.setImageBitmap(null);
            m_pay_qrcode_wx = null;
        }
        if (m_pay_qrcode_ali != null) {
            m_pay_qrcode_ali.setImageBitmap(null);
            m_pay_qrcode_ali = null;
        }
        m_pay_tips = null;
        m_pay_qrcode_tips_wx = null;
        m_pay_qrcode_tips_ali = null;
        m_pay_time = null;
        m_pay_loading_tips_wx = null;
        m_pay_loading_tips_ali = null;
        m_AnimLoad = null;
        m_AnimGoods = null;
        m_BtnClickListener = null;
        m_vendListener = null;
    }

    /**
     * 初始化动画
     */
    private void initAnim(Context context) {
        m_AnimLoad = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
        m_AnimLoad.setDuration(1500);
        m_AnimLoad.setRepeatCount(Animation.INFINITE);
        m_AnimLoad.setRepeatMode(Animation.RESTART);
        m_AnimLoad.setStartTime(Animation.START_ON_FIRST_FRAME);
        LinearInterpolator lir = new LinearInterpolator();
        m_AnimLoad.setInterpolator(lir);
        if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch()) || null == anim_pay) {

            m_AnimGoods = AnimationUtils.loadAnimation(context, R.anim.myim);
            anim_pay = AnimationUtils.loadAnimation(context, R.anim.anim_pay);
        } else {
            m_AnimGoods = AnimationUtils.loadAnimation(context, R.anim.in_translate_top);
        }
    }

    private void initShow() {
        m_pay_qrcode_layout_wx.setVisibility(View.GONE);
        m_pay_qrcode_layout_ali.setVisibility(View.GONE);
        m_pay_tips.setText(TcnShareUseData.getInstance().getPayTips());
        if (TcnShareUseData.getInstance().isShowSingleQRCode()) {
            if (((TcnShareUseData.getInstance().getPaySystemType()).equals(TcnConstant.PAY_SYSTEM_TYPE[0]))) {
                if (TcnShareUseData.getInstance().isAliPayOpen()) {
                    m_pay_qrcode_load_layout_wx.setVisibility(View.GONE);
                    m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                    //m_pay_logo_wx.setVisibility(View.GONE);
                  //  m_pay_qrcode_tips_wx.setText("");
                } else if (TcnShareUseData.getInstance().isWeixinOpen()) {
                    m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
                    m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                } else {
                    m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
                    m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                    m_pay_logo_wx.setVisibility(View.GONE);
                    m_pay_qrcode_tips_wx.setText("");
                }
            } else {
                m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
                m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                m_pay_logo_wx.setVisibility(View.GONE);
                m_pay_qrcode_tips_wx.setText("");
            }
        } else {

            m_pay_qrcode_tips_wx.setText(TcnShareUseData.getInstance().getPayFirstQrcodeTips());
            m_pay_qrcode_tips_ali.setText(TcnShareUseData.getInstance().getPaySecondQrcodeTips());

            if (!TcnShareUseData.getInstance().isAliPayOpen()) {
                if (!TcnShareUseData.getInstance().isWeixinOpen()) {
                    if (!TcnShareUseData.getInstance().isJidongOpen()) {
                        if (m_pay_qrcode_load_layout_wx != null) {
                            m_pay_qrcode_load_layout_wx.setVisibility(View.GONE);
                        }
                        if (m_pay_qrcode_load_layout_ali != null) {
                            m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        initData();
        startAnimLoad();
        startAnimGoods();
    }

    private void initDismiss() {
        initDataEmpty();
        stopAnimLoad();
        closeTrade();
        if (TcnShareUseData.getInstance().isShowSingleQRCode()) {
            if (((TcnShareUseData.getInstance().getPaySystemType()).equals(TcnConstant.PAY_SYSTEM_TYPE[0]))) {
                if (TcnShareUseData.getInstance().isAliPayOpen()) {
                    m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                    m_pay_qrcode_load_layout_ali.setVisibility(View.VISIBLE);
                    m_pay_loading_ali.setVisibility(View.VISIBLE);
                    m_pay_qrcode_ali.setImageBitmap(null);
                } else {
                    m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                    m_pay_qrcode_load_layout_wx.setVisibility(View.VISIBLE);
                    m_pay_loading_wx.setVisibility(View.VISIBLE);
                    m_pay_qrcode_wx.setImageBitmap(null);
                }
            } else {
                m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                m_pay_qrcode_load_layout_wx.setVisibility(View.VISIBLE);
                m_pay_loading_wx.setVisibility(View.VISIBLE);
                m_pay_qrcode_wx.setImageBitmap(null);
            }

        } else {
            if (TcnShareUseData.getInstance().isWeixinOpen()) {
                m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                m_pay_qrcode_load_layout_wx.setVisibility(View.VISIBLE);
                m_pay_loading_wx.setVisibility(View.VISIBLE);
                m_pay_qrcode_wx.setImageBitmap(null);
            }
            if (TcnShareUseData.getInstance().isAliPayOpen()) {
                m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                m_pay_qrcode_load_layout_ali.setVisibility(View.VISIBLE);
                m_pay_loading_ali.setVisibility(View.VISIBLE);
                m_pay_qrcode_ali.setImageBitmap(null);
            }
        }
        m_pay_loading_wx.setBackgroundResource(R.mipmap.fm_play_page_playing_loading);
        m_pay_goods_picture.setImageResource(R.mipmap.default_ticket_poster_pic);
    }
    private void initDataEmpty() {
        m_pay_goods_name.setText(m_context.getString(R.string.ui_pay_product_name));
        m_pay_goods_slotno.setText(m_context.getString(R.string.ui_aisle));
        m_pay_goods_price.setText(m_context.getString(R.string.ui_price));
        m_pay_goods_introduction.setText(m_context.getString(R.string.ui_slot_introduction));
        m_pay_time.setText("");
        setGoodsImage();
    }

    private void initData() {
        if (m_pay_qrcode_wx != null) {
            m_pay_qrcode_wx.setImageBitmap(null);
        }

        if (m_pay_qrcode_ali != null) {
            m_pay_qrcode_ali.setImageBitmap(null);
        }

        Coil_info info = VendDBControl.getInstance().getSelectCoilInfo();
        if (null == info) {
            return;
        }
        m_pay_goods_name.setText(m_context.getString(R.string.ui_pay_product_name)+info.getPar_name());
        if (info.getKeyNum() > 0) {
            m_pay_goods_slotno.setText(m_context.getString(R.string.ui_keynum)+info.getKeyNum());
        } else {
            m_pay_goods_slotno.setText(m_context.getString(R.string.ui_aisle)+info.getCoil_id());
        }

        m_pay_goods_price.setText(m_context.getString(R.string.ui_price)+info.getPar_price());
        m_pay_goods_introduction.setText(m_context.getString(R.string.ui_slot_introduction)+info.getContent());
        setGoodsImage();
        if (TcnShareUseData.getInstance().isShowSingleQRCode()) {

        } else {
            if (TcnShareUseData.getInstance().isWeixinOpen()) {

            }

            if (TcnShareUseData.getInstance().isAliPayOpen()) {

            }

        }
    }

    /**
     * 没有设置商品图片的，没有的用默认图片
     */
    private void setGoodsImage() {
        Coil_info info = VendDBControl.getInstance().getSelectCoilInfo();
        if (null == info) {
            return;
        }
        String mImgUrl = info.getImg_url();
        if((null == mImgUrl) || ("".equals(mImgUrl))) {
            m_pay_goods_picture.setImageResource(R.mipmap.default_ticket_poster_pic);
            return;
        }
        TcnVendIF.getInstance().displayImage(mImgUrl,m_pay_goods_picture,R.mipmap.default_ticket_poster_pic);
    }

    /**
     *  商品图片显示动画
     */
    private void startAnimGoods() {
        if ((m_pay_goods_picture != null) && (m_AnimGoods != null)) {
            m_pay_goods_picture.setVisibility(View.VISIBLE);
            m_pay_goods_picture.startAnimation(m_AnimGoods);
        }
    }

    private void stopAnimGoods() {
        if (m_AnimGoods != null) {
            m_AnimGoods.cancel();
        }
        if (m_pay_goods_picture != null) {
            m_pay_goods_picture.setOnClickListener(null);
            m_pay_goods_picture.clearAnimation();
        }
    }

    private void startAnimLoad() {
        if (TcnShareUseData.getInstance().isShowSingleQRCode()) {
            if (TcnVendIF.getInstance().isHasQRCodePay()) {
                if (((TcnShareUseData.getInstance().getPaySystemType()).equals(TcnConstant.PAY_SYSTEM_TYPE[0]))) {
                    if (TcnShareUseData.getInstance().isAliPayOpen()) {
                        if (m_pay_loading_ali != null) {
                            m_pay_loading_ali.startAnimation(m_AnimLoad);
                        }
                    } else {
                        if (m_pay_loading_wx != null) {
                            m_pay_loading_wx.startAnimation(m_AnimLoad);
                        }
                    }
                } else {
                    if (m_pay_loading_wx != null) {
                        m_pay_loading_wx.startAnimation(m_AnimLoad);
                    }
                }
            }
        } else {
            if (TcnVendIF.getInstance().isHasQRCodePay()) {
                if (m_pay_loading_wx != null) {
                    m_pay_loading_wx.startAnimation(m_AnimLoad);
                }
                if (m_pay_loading_ali != null) {
                    m_pay_loading_ali.startAnimation(m_AnimLoad);
                }
            }
        }

    }

    private void stopAnimLoad() {
        if (m_AnimLoad != null) {
            m_AnimLoad.cancel();
        }
        if (m_pay_loading_wx != null) {
            m_pay_loading_wx.clearAnimation();
        }
        if (m_pay_loading_ali != null) {
            m_pay_loading_ali.clearAnimation();
        }
    }

    private void setQrCodeUnion(int status, String errMsg, Bitmap bitmap) {
        if (TcnVendEventResultID.QR_CODE_GENERATE_FAILED == status) {
            TcnUtility.getToast(m_context, errMsg);
        }
        setQrCodeWeixin(status,bitmap);
    }

    private void setQrCodeUnionApp(int status, String errMsg, Bitmap bitmap) {
        if (TcnVendEventResultID.QR_CODE_GENERATE_FAILED == status) {
            TcnUtility.getToast(m_context, errMsg);
        }
        setQrCodeAli(status,bitmap);
    }

    private void setQrCodeEc(boolean isSingleQRCode, int status,int slotNo) {
        if (isSingleQRCode) {
            if (m_pay_loading_wx != null) {
                m_pay_loading_wx.clearAnimation();
            }
            if (TcnVendEventResultID.QR_CODE_GENERATE_QRURL_SUCCESS == status) {
                m_pay_qrcode_load_layout_wx.setVisibility(View.GONE);
                m_pay_qrcode_layout_wx.setVisibility(View.VISIBLE);
                m_pay_loading_wx.setVisibility(View.GONE);
                Coil_info sInfo = TcnVendIF.getInstance().getCoilInfo(slotNo);
                if (sInfo != null) {
                    TcnVendIF.getInstance().displayImage(sInfo.getQrPayUrl(),m_pay_qrcode_wx,R.mipmap.juy);
                } else {
                    m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                    m_pay_loading_wx.setVisibility(View.VISIBLE);
                    m_pay_loading_wx.setBackgroundResource(R.mipmap.juy);
                    m_pay_loading_tips_wx.setText(m_context.getString(R.string.notify_load_fail));
                }
            } else {
                m_pay_qrcode_layout_wx.setVisibility(View.GONE);
                m_pay_loading_wx.setVisibility(View.VISIBLE);
                m_pay_loading_wx.setBackgroundResource(R.mipmap.juy);
                m_pay_loading_tips_wx.setText(m_context.getString(R.string.notify_load_fail));
            }
        } else {
            if (m_pay_loading_ali != null) {
                m_pay_loading_ali.clearAnimation();
            }
            if (TcnVendEventResultID.QR_CODE_GENERATE_QRURL_SUCCESS == status) {
                m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
                m_pay_qrcode_layout_ali.setVisibility(View.VISIBLE);
                m_pay_loading_ali.setVisibility(View.GONE);
                Coil_info sInfo = TcnVendIF.getInstance().getCoilInfo(slotNo);
                if (sInfo != null) {
                    TcnVendIF.getInstance().displayImage(sInfo.getQrPayUrl(),m_pay_qrcode_ali,R.mipmap.juy);
                } else {
                    m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                    m_pay_loading_ali.setVisibility(View.VISIBLE);
                    m_pay_loading_ali.setBackgroundResource(R.mipmap.juy);
                    m_pay_loading_tips_ali.setText(m_context.getString(R.string.notify_load_fail));
                }
            } else {
                m_pay_qrcode_layout_ali.setVisibility(View.GONE);
                m_pay_loading_ali.setVisibility(View.VISIBLE);
                m_pay_loading_ali.setBackgroundResource(R.mipmap.juy);
                m_pay_loading_tips_ali.setText(m_context.getString(R.string.notify_load_fail));
            }
        }

    }

    private void setQrCodeWeixin(int status, Bitmap bitmap) {
        if (m_pay_loading_wx != null) {
            m_pay_loading_wx.clearAnimation();
        }
        if (TcnVendEventResultID.QR_CODE_GENERATE_SUCCESS == status) {
            m_pay_qrcode_load_layout_wx.setVisibility(View.GONE);
            m_pay_qrcode_layout_wx.setVisibility(View.VISIBLE);
            m_pay_loading_wx.setVisibility(View.GONE);
            if ((null != bitmap) && (!bitmap.isRecycled())) {
                m_pay_qrcode_wx.setImageBitmap(bitmap);
            }
        } else {
            m_pay_qrcode_layout_wx.setVisibility(View.GONE);
            m_pay_loading_wx.setVisibility(View.VISIBLE);
            m_pay_loading_wx.setBackgroundResource(R.mipmap.juy);
            m_pay_loading_tips_wx.setText(m_context.getString(R.string.notify_load_fail));
        }
    }

    private void setQrCodeAli(int status, Bitmap bitmap) {
        if (m_pay_loading_ali != null) {
            m_pay_loading_ali.clearAnimation();
        }
       // m_pay_logo_ali.setVisibility(View.GONE);
        if (TcnVendEventResultID.QR_CODE_GENERATE_SUCCESS == status) {
            m_pay_qrcode_load_layout_ali.setVisibility(View.GONE);
            m_pay_qrcode_layout_ali.setVisibility(View.VISIBLE);
            m_pay_loading_ali.setVisibility(View.GONE);
            if ((null != bitmap) && (!bitmap.isRecycled())) {
                m_pay_qrcode_ali.setImageBitmap(bitmap);
            }
        } else {
            m_pay_qrcode_layout_ali.setVisibility(View.GONE);
            m_pay_loading_ali.setVisibility(View.VISIBLE);
            m_pay_loading_ali.setBackgroundResource(R.mipmap.juy);
            m_pay_loading_tips_ali.setText(m_context.getString(R.string.notify_load_fail));
        }
    }

    private void setPayTime(int time) {
        if ((null == m_pay_time) || (null == m_context)) {
            return;
        }
        if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
            m_btn_back.setText(m_context.getString(R.string.back)+"("+time+"s)");
        } else {
            m_pay_time.setText(getSpanPayTimeString(time));
        }

    }

    private SpannableStringBuilder getSpanPayTimeString(int time) {
        if (null == m_stringBuilder) {
            m_stringBuilder = new SpannableStringBuilder();
        }

        if (null == m_textSizeSpan) {
            m_textSizeSpan = new AbsoluteSizeSpan(m_iPageSmallFont);
        }
        m_stringBuilder.clear();
        m_stringBuilder.clearSpans();
        m_stringBuilder.append(m_context.getString(R.string.ui_pay_time_start_tip));
        m_stringBuilder.append(" ");
        m_stringBuilder.append(String.valueOf(time));
        m_stringBuilder.append(" ");
        m_stringBuilder.append(m_context.getString(R.string.ui_pay_time_end_tip));
        int start = m_stringBuilder.length();
        m_stringBuilder.append(m_context.getString(R.string.ui_pay_not_back_tip));
        int end = m_stringBuilder.length();
        m_stringBuilder.setSpan(m_textSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return m_stringBuilder;
    }

    private void closeTrade() {
        TcnVendIF.getInstance().closeTrade(UICommon.getInstance().isCanRefund());
    }

    private ButtonClick m_BtnClickListener = new ButtonClick();
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
            if (id == R.id.pay_back) {
                TcnVendIF.getInstance().reqEndEffectiveTime();
                dismiss();
            }
        }
    }

	public void anim(){
 	if (!(TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())||null==anim_pay) {
     	return;
	 }
   	 anim_pay.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            DialogPay.this.dismiss();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    });
    m_pay_goods_picture.startAnimation(anim_pay);

	}
    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {

            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.WX_QR_CODE_GENERATED:
                    setQrCodeWeixin(cEventInfo.m_lParam1,null);
                    break;
                case TcnVendEventID.ALI_QR_CODE_GENERATED:
                    setQrCodeAli(cEventInfo.m_lParam1,null);
                    break;
                case TcnVendEventID.COMMAND_INPUT_MONEY:
                    break;
                case TcnVendEventID.COMMAND_SELECT_GOODS:
                    initData();
                    break;
                case TcnVendEventID.UPDATE_PAY_TIME:
                    if (cEventInfo.m_lParam1 <= 0) {
                        dismiss();
                    } else {
                        setPayTime(cEventInfo.m_lParam1);
                    }
                    break;
                case TcnVendEventID.COMMAND_SOLD_OUT:
                case TcnVendEventID.BACK_TO_SHOPPING:
                case TcnVendEventID.COMMAND_INVALID_SLOTNO:
                case TcnVendEventID.COMMAND_SHIPMENT_FAILURE:
                case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:
                    dismiss();
                    break;
                case TcnVendEventID.NETWORK_NOT_GOOOD:
                    setQrCodeWeixin(cEventInfo.m_lParam1,null);
                    setQrCodeAli(cEventInfo.m_lParam1,null);
                    break;
                case TcnVendEventID.PAY_FAIL:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
