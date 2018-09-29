package com.tcn.vendspring.help;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.resources.Resources;
import com.tcn.uicommon.viewpager.AutoScrollViewPager;
import com.tcn.vendspring.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class DialogHelp extends Dialog {

    private Button m_btn_back = null;

    private List<ImageView> views =  null ;

    private RadioGroup mRadioGroup =  null ;

    private AutoScrollViewPager mViewPager =  null ;

    private Context m_context = null;


    public DialogHelp(Context context) {
        super(context, R.style.Dialog_bocop);
        init(context);
    }

    @Override
    public void show() {
        super.show();
        TcnVendIF.getInstance().startGoBackShopTimer();
        startAutoScroll();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        TcnVendIF.getInstance().stopGoBackShopTimer();
        stopAutoScroll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_DOWN) {
            TcnVendIF.getInstance().startGoBackShopTimer();
        }
        return super.dispatchTouchEvent(ev);
    }

    private void init(Context context) {
        m_context = context;
        View contentView = View.inflate(context, R.layout.fragment_help, null);
        setContentView(contentView);

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
        } else if (ImageController.SCREEN_TYPE_S1360X768 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 698;
            lp.y = 35;
        }
        else if (ImageController.SCREEN_TYPE_S480X800 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 490;
        }
        else if (ImageController.SCREEN_TYPE_S800X1280 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 750;
        } else if (ImageController.SCREEN_TYPE_S600X1024 == TcnVendIF.getInstance().getScreenType()) {
            lp.height =638;
        } else if (ImageController.SCREEN_TYPE_S1024X600 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 604;
            lp.x = 420;
            lp.height = 560;
            win.setGravity(Gravity.BOTTOM);
        }else if (ImageController.SCREEN_TYPE_S1280X800 == TcnVendIF.getInstance().getScreenType()) {
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
        }
        else {

        }
        lp.dimAmount = 0.0f;
        win.setAttributes(lp);

        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewpager_demo);

        mRadioGroup = (RadioGroup) findViewById(R.id.m_radiogroup);
        RadioGroup.LayoutParams mLayoutParams = new RadioGroup.LayoutParams(10,10);

        mLayoutParams.setMargins(8,8,8,8);
        List<String> mList = TcnVendIF.getInstance().getImageListHelp();

        if ((null == mList) || (mList.isEmpty())) {
            if (null == mList) {
                mList = new ArrayList<String>();
            }
            if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_wx));
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_zfb));
                if (TcnShareUseData.getInstance().isJidongOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_jd));
                }
                if (TcnShareUseData.getInstance().isCashPayOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_cash));
                }
            } else {
   				mList.add(TcnVendIF.getInstance().getResPath(R.drawable.weixin_on_delivery));
           		mList.add(TcnVendIF.getInstance().getResPath(R.drawable.alipay_tips));
                if (TcnShareUseData.getInstance().isCashPayOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.cash_on_delivery));
                }
            }
        }
        if ((null == mList) || (mList.isEmpty())) {
            return;
        }
        if (mList.size() > 1) {
            mRadioGroup.setVisibility(View.VISIBLE);
        } else {
            mRadioGroup.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < mList.size() ; i++) {
            RadioButton radioButton = new RadioButton(m_context);
            radioButton.setBackgroundResource(R.mipmap.page_indicator_unfocused);
            radioButton.setCompoundDrawables(null,null,null,null);
            mRadioGroup.addView(radioButton,i,mLayoutParams);
        }
        mRadioGroup.setEnabled(false);
        mViewPager.configMode(mList,mRadioGroup ,12,AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);

        m_btn_back = (Button) findViewById(R.id.help_back);
        m_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void deInit() {
        setOnDismissListener(null);
        setOnShowListener(null);
        if (m_btn_back != null) {
            m_btn_back.setOnClickListener(null);
            m_btn_back = null;
        }
        stopAutoScroll();
        views =  null ;

        mRadioGroup =  null ;
        m_context = null;
    }

    public void refsh() {
        List<String> mList = TcnVendIF.getInstance().getImageListHelp();

        if ((null == mList) || (mList.isEmpty())) {
            if (null == mList) {
                mList = new ArrayList<String>();
            }
            if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_wx));
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_zfb));
                if (TcnShareUseData.getInstance().isJidongOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_jd));
                }
                if (TcnShareUseData.getInstance().isCashPayOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.help_pay_cash));
                }
            } else {
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.weixin_on_delivery));
                mList.add(TcnVendIF.getInstance().getResPath(R.drawable.alipay_tips));
                if (TcnShareUseData.getInstance().isCashPayOpen()) {
                    mList.add(TcnVendIF.getInstance().getResPath(R.drawable.cash_on_delivery));
                }
            }
        }
        if ((null == mList) || (mList.isEmpty())) {
            return;
        }
        if (mList.size() > 1) {
            mRadioGroup.setVisibility(View.VISIBLE);
        } else {
            mRadioGroup.setVisibility(View.INVISIBLE);
        }
        mRadioGroup.removeAllViews();
        RadioGroup.LayoutParams mLayoutParams = new RadioGroup.LayoutParams(10,10);

        mLayoutParams.setMargins(8,8,8,8);
        for (int i = 0; i < mList.size() ; i++) {
            RadioButton radioButton = new RadioButton(m_context);
            radioButton.setBackgroundResource(R.mipmap.page_indicator_unfocused);
            radioButton.setCompoundDrawables(null,null,null,null);
            mRadioGroup.addView(radioButton,i,mLayoutParams);
        }
        mRadioGroup.setEnabled(false);
        mViewPager.configMode(mList,mRadioGroup ,12,AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
    }

    private void startAutoScroll() {
        if (mViewPager != null) {
            mViewPager.setDepthTransFormer();
            mViewPager.startAutoScroll();
        }
    }

    private void stopAutoScroll() {
        if (mViewPager != null){
            mViewPager.setCurrentItem(0,false);
            mViewPager.stopAutoScroll();
        }
    }
}
