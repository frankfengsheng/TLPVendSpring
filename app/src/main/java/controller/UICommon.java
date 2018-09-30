package controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.db.Coil_info;
import com.tcn.funcommon.db.Goods_info;
import com.tcn.funcommon.db.Key_info;
import com.tcn.funcommon.db.UtilsDB;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.vendspring.MainAct;
import com.tcn.vendspring.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class UICommon {
    private static final String TAG = "UICommon";
    private static final int ADVERT_TYPE_ADVERT_VIDEO = 1;
    private static final int ADVERT_TYPE_ADVERT_IMAGE = 2;
    private static final int ADVERT_TYPE_STANDBY_VIDEO = 3;
    private static final int ADVERT_TYPE_STANDBY_IMAGE = 4;

    private static UICommon m_Instance = null;
    private VendApplication m_application;
    private Context m_Context = null;
    private RotateAnimation m_AnimLoad;
    private boolean m_isScreenShowing = false;
    private boolean m_isPayShowing = false;
    private boolean m_isCanRefund = false;
    private boolean m_bIsShipSuccessed = false;


    public static synchronized UICommon getInstance() {
        if (null == m_Instance) {
            m_Instance = new UICommon();
        }
        return m_Instance;
    }

    public boolean isShipSuccessed() {
        return m_bIsShipSuccessed;
    }

    public void setShipSuccessed(boolean shipSuccessed) {
        m_bIsShipSuccessed = shipSuccessed;
    }

    public void setApplication(VendApplication application) {
        m_application = application;
    }

    public VendApplication getApplication() {
        return m_application;
    }

    /**
     * 添加新打开的Activity
     * @return
     */
    public void addActivity(Activity activity) {
        if(m_application != null) {
            m_application.addActivity(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if(m_application != null) {
            m_application.removeActivity(activity);
        }
    }

    /**
     *关闭所有容器里的的Activity
     * @return
     */
    public void closeActivity(){
        if(m_application != null) {
            m_application.closeActivity();
        }
    }

    public void setMainActivity(Activity main){
        if(m_application != null) {
            m_application.setMainActivity(main);
        }
    }

    /**
     *关闭除了exception的所有容器里的的Activity
     * @return
     */
    public void closeActivityExceptMain() {
        if(m_application != null) {
            m_application.closeActivityExceptMain();
        }
    }

    public int getClickBuyTextSize() {
        int textSize = 50;
        String m_ScreenInch = TcnVendIF.getInstance().getScreenInch();
        if (m_ScreenInch.equals(TcnCommon.SCREEN_INCH[1])) {
            textSize = 35;
        } else {
            if ((ImageController.SCREEN_TYPE_S768X1360 == ImageController.instance().getScreenType())
                    || (ImageController.SCREEN_TYPE_S1360X768 == ImageController.instance().getScreenType())
                    || (ImageController.SCREEN_TYPE_S768X1366 == ImageController.instance().getScreenType())
                    || (ImageController.SCREEN_TYPE_S1366X768 == ImageController.instance().getScreenType())) {

                textSize = 38;
            } else if ((ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                    || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {
                textSize = 22;
            } else {

            }
        }

        return textSize;
    }

    public int getTextSize16() {
        int textSize = 16;
        if ((ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {
            textSize = 8;
        }
        return textSize;
    }

    public int getTextSize20() {
        int textSize = 20;
        if ((ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {
            textSize = 10;
        }
        return textSize;
    }

    public int getTextSize22() {
        int textSize = 22;
        if ((ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {
            textSize = 10;
        }
        return textSize;
    }

    public int getTextSize26() {
        int textSize = 26;
        if ((ImageController.SCREEN_TYPE_S1280X720 == ImageController.instance().getScreenType())
                || (ImageController.SCREEN_TYPE_S720X1280 == ImageController.instance().getScreenType())) {
            textSize = 14;
        }
        return textSize;
    }

    public void setContentViewMainActivity (Activity activity) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "setContentViewMainActivity isScreenFitted: "+TcnVendIF.getInstance().isScreenFitted()
                +" isAdvertOnScreenBottom:"+TcnShareUseData.getInstance().isAdvertOnScreenBottom()+" isStandbyImageFullScreen: "+TcnShareUseData.getInstance().isStandbyImageFullScreen()
                +" isFullScreen:"+TcnShareUseData.getInstance().isFullScreen());
       String m_ScreenInch = TcnVendIF.getInstance().getScreenInch();
        if (m_ScreenInch.equals(TcnCommon.SCREEN_INCH[1])) {    //小屏
            if (TcnVendIF.getInstance().isScreenFitted()) {
                activity.setContentView(R.layout.mainact_small);
            } else {
                if (TcnVendIF.getInstance().getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                    activity.setContentView(R.layout.mainact_small_landscape_default);
                } else {
                    activity.setContentView(R.layout.mainact_small_portrait_default);
                }
            }

        } else {                //大屏
            if (TcnVendIF.getInstance().isScreenFitted()) {
                if (TcnShareUseData.getInstance().isFullScreen()) {
                    activity.setContentView(R.layout.mainact_large_full);   //竖屏的全屏
                } else {
                    if (TcnShareUseData.getInstance().isAdvertOnScreenBottom()) {  //竖屏广告在底下播放
                        if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {     //待机广告全屏播放
                            activity.setContentView(R.layout.mainact_large_ad_bottom_and_stby_ad_full);
                        } else {
                            activity.setContentView(R.layout.mainact_large_ad_bottom_and_stby_ad_hf);
                        }

                    } else {
                        if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {     //竖屏广告在顶上播放
                            activity.setContentView(R.layout.mainact_large_ad_top_and_stby_ad_full);        //待机广告全屏播放
                        } else {
                            activity.setContentView(R.layout.mainact_large_ad_top_and_stby_ad_hf);
                        }

                    }
                }

            } else {
                if (TcnVendIF.getInstance().getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                    activity.setContentView(R.layout.mainact_large_landscape_default);
                } else {
                    if (TcnShareUseData.getInstance().isFullScreen()) {
                        activity.setContentView(R.layout.mainact_large_portrait_full_default);   //竖屏的全屏
                    } else {
                        if (TcnShareUseData.getInstance().isAdvertOnScreenBottom()) {       //竖屏广告在底下播放
                            if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {      //待机广告全屏播放
                                activity.setContentView(R.layout.mainact_large_portrit_ad_bot_and_stby_ad_full_dft);
                            } else {
                                activity.setContentView(R.layout.mainact_large_portrit_ad_bot_and_stby_ad_hf_dft);
                            }
                        } else {
                            if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
                                activity.setContentView(R.layout.mainact_large_portrit_ad_top_and_stby_ad_full_dft);
                            } else {
                                activity.setContentView(R.layout.mainact_large_portrit_ad_top_and_stby_ad_hf_dft);
                            }
                        }

                    }
                }
            }
        }
    }



    public void setSurfaceViewAdvertVideo(SurfaceView surfaceView) {
        if (null == surfaceView) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewAdvertVideo() surfaceView is null");
            return;
        }
        m_callback_ad_video = new SurfaceHolderCallback(ADVERT_TYPE_ADVERT_VIDEO,surfaceView);
        SurfaceHolder mHolder = surfaceView.getHolder(); //SurfaceHolder是SurfaceView的控制接口
        if (null == mHolder) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewAdvertVideo() mHolder is null");
            return;
        }
        mHolder.addCallback(m_callback_ad_video); //因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //Surface类型
    }

    public void setSurfaceViewAdvertImage(SurfaceView surfaceView) {
        if (null == surfaceView) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewAdvertImage() surfaceView is null");
            return;
        }
        m_callback_ad_image = new SurfaceHolderCallback(ADVERT_TYPE_ADVERT_IMAGE,surfaceView);
        SurfaceHolder mHolder = surfaceView.getHolder(); //SurfaceHolder是SurfaceView的控制接口
        if (null == mHolder) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewAdvertImage() mHolder is null");
            return;
        }
        mHolder.addCallback(m_callback_ad_image); //因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //Surface类型
    }

    public void setSurfaceViewStandbyVideo(SurfaceView surfaceView) {
        if (null == surfaceView) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewStandbyVideo() surfaceView is null");
            return;
        }
        m_callback_stdby_video = new SurfaceHolderCallback(ADVERT_TYPE_STANDBY_VIDEO,surfaceView);
        SurfaceHolder mHolder = surfaceView.getHolder(); //SurfaceHolder是SurfaceView的控制接口
        if (null == mHolder) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewStandbyVideo() mHolder is null");
            return;
        }
        mHolder.addCallback(m_callback_stdby_video); //因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //Surface类型
    }

    public void setSurfaceViewStandbyImage(SurfaceView surfaceView) {
        if (null == surfaceView) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewStandbyImage() surfaceView is null");
            return;
        }
        m_callback_stdby_image = new SurfaceHolderCallback(ADVERT_TYPE_STANDBY_IMAGE,surfaceView);
        SurfaceHolder mHolder = surfaceView.getHolder(); //SurfaceHolder是SurfaceView的控制接口
        if (null == mHolder) {
            TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceViewStandbyImage() mHolder is null");
            return;
        }
        mHolder.addCallback(m_callback_stdby_image); //因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //Surface类型
    }

    public void removeSurfaceCallbackAdvertVideo(SurfaceView surfaceView) {
        if (null == surfaceView) {
            return;
        }
        SurfaceHolder mHolder = surfaceView.getHolder();
        if (null == mHolder) {
            return;
        }
        mHolder.removeCallback(m_callback_ad_video);
        m_callback_ad_video = null;
    }

    public void removeSurfaceCallbackAdvertImage(SurfaceView surfaceView) {
        if (null == surfaceView) {
            return;
        }
        SurfaceHolder mHolder = surfaceView.getHolder();
        if (null == mHolder) {
            return;
        }
        mHolder.removeCallback(m_callback_ad_image);
        m_callback_ad_image = null;
    }

    public void removeSurfaceCallbackStandbyVideo(SurfaceView surfaceView) {
        if (null == surfaceView) {
            return;
        }
        SurfaceHolder mHolder = surfaceView.getHolder();
        if (null == mHolder) {
            return;
        }
        mHolder.removeCallback(m_callback_stdby_video);
        m_callback_stdby_video = null;
    }

    public void removeSurfaceCallbackStandbyImage(SurfaceView surfaceView) {
        if (null == surfaceView) {
            return;
        }
        SurfaceHolder mHolder = surfaceView.getHolder();
        if (null == mHolder) {
            return;
        }
        mHolder.removeCallback(m_callback_stdby_image);
        m_callback_stdby_image = null;
    }

    public boolean isPayShowing() {
        return m_isPayShowing;
    }

    public void setPayShow(boolean show) {
        m_isPayShowing = show;
    }

    public void setCanRefund(boolean canRefund) {
        m_isCanRefund = canRefund;
    }

    public boolean isCanRefund() {
        return m_isCanRefund;
    }

    public boolean isScreenAdvertShowing() {
        return m_isScreenShowing;
    }

    public void setScreenAdvertShow(boolean show) {
        m_isScreenShowing = show;
    }

    public RotateAnimation getRotateAnimation() {
        if (null == m_AnimLoad) {
            m_AnimLoad = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
            m_AnimLoad.setDuration(1500);
            m_AnimLoad.setRepeatCount(Animation.INFINITE);
            m_AnimLoad.setRepeatMode(Animation.RESTART);
            m_AnimLoad.setStartTime(Animation.START_ON_FIRST_FRAME);
            LinearInterpolator lir = new LinearInterpolator();
            m_AnimLoad.setInterpolator(lir);
        }
        return m_AnimLoad;
    }

    public void stopRotateAnimation() {
        if (m_AnimLoad != null) {
            m_AnimLoad.cancel();
        }
    }

    public boolean isRecyclerViewMeasured() {
        boolean bRet = false;
        if ((Configuration.ORIENTATION_PORTRAIT == TcnVendIF.getInstance().getScreenOrientation())
                && (TcnShareUseData.getInstance().isFullScreen())) {
            bRet = true;
        }
        return bRet;
    }

    public boolean isPageDisplay() {
        boolean bRet = false;
        int iCountEveryPage = TcnShareUseData.getInstance().getItemCountEveryPage();
        if (iCountEveryPage > 20) {
            TcnShareUseData.getInstance().setPageDisplay(true);
            bRet = true;
        }
        return bRet;
    }

    public int getRecyclerViewWidth() {
        int iWidth = 1040;
        int screenType = TcnVendIF.getInstance().getScreenType();
        if (ImageController.SCREEN_TYPE_S1080X1920 == screenType) {
            iWidth = 1040;
        } else if (ImageController.SCREEN_TYPE_S768X1366 == screenType) {
            iWidth = 770;
        } else if (ImageController.SCREEN_TYPE_S768X1360 == screenType) {
            iWidth = 770;
        } else if (ImageController.SCREEN_TYPE_S1050X1680 == screenType) {
            iWidth = 1050;
        } else if (ImageController.SCREEN_TYPE_S1680X1050 == screenType) {
            iWidth = 1680;
        } else if (ImageController.SCREEN_TYPE_S1920X1080 == screenType) {
            iWidth = 1920;
        }
        else {
            iWidth = 700;
        }
        return iWidth;
    }

    public int getRecyclerViewHeight() {
        int iHeight = 1560;
        int screenType = TcnVendIF.getInstance().getScreenType();
        if (ImageController.SCREEN_TYPE_S1080X1920 == screenType) {
            iHeight = 1560;
        } else if (ImageController.SCREEN_TYPE_S768X1366 == screenType) {
            iHeight = 1050;
        } else if (ImageController.SCREEN_TYPE_S768X1360 == screenType) {
            iHeight = 1050;
        } else if (ImageController.SCREEN_TYPE_S1050X1680 == screenType) {
            iHeight = 1200;
        } else if (ImageController.SCREEN_TYPE_S1680X1050 == screenType) {
            iHeight = 950;
        } else if (ImageController.SCREEN_TYPE_S1920X1080 == screenType) {
            iHeight = 980;
        }
        else {
            iHeight = 500;
        }
        return iHeight;
    }

    public int getSetDeviceLayout() {
        int res = 0;
        int type = TcnVendIF.getInstance().getScreenType();
        if ((ImageController.SCREEN_TYPE_S720X1280 == type) || (ImageController.SCREEN_TYPE_S1280X720 == type)) {
            res = R.layout.set_device_info_ldp;
        } else {
            res = R.layout.set_device_info;
        }
        return res;
    }

    public int getSelectionLayout() {
        int res = 0;
        if (Configuration.ORIENTATION_PORTRAIT == TcnVendIF.getInstance().getScreenOrientation()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                res = R.layout.fragment_commodity_selection_full;
            } else {
                res = R.layout.fragment_commodity_selection;
            }
        } else {
            res = R.layout.fragment_commodity_selection;
        }
        return res;
    }

    public int getSelectionItemLayout() {
        int res = 0;
        int CountEveryPage = TcnShareUseData.getInstance().getItemCountEveryPage();
        if (Configuration.ORIENTATION_PORTRAIT == TcnVendIF.getInstance().getScreenOrientation()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                if (TcnShareUseData.getInstance().isPageDisplay()) {
                    if (CountEveryPage == 30) {
                        res = R.layout.layout_commodity_item_30_goods;
                    } else if (CountEveryPage == 80) {
                        res = R.layout.layout_commodity_item_80_goods;
                    } else if (CountEveryPage == 60) {
                        res = R.layout.layout_commodity_item_60_goods;
                    } else if (CountEveryPage == 16) {
                        res = R.layout.layout_commodity_item_goods;
                    } else if (CountEveryPage == 20) {
                        res = R.layout.layout_commodity_item_20_goods;
                    } else {
                        res = R.layout.layout_commodity_item_goods;
                    }

                } else {
                    if (CountEveryPage == 30) {
                        res = R.layout.layout_commodity_item_30_goods;
                    } else if (CountEveryPage == 80) {
                        res = R.layout.layout_commodity_item_80_goods;
                    } else if (CountEveryPage == 60) {
                        res = R.layout.layout_commodity_item_60_goods;
                    } else if (CountEveryPage == 20) {
                        res = R.layout.layout_commodity_item_20_goods;
                    } else {
                        res = R.layout.layout_commodity_item_goods;
                    }
                }
            } else {
                if (CountEveryPage == 10) {
                    res = R.layout.layout_commodity_item_10_goods;
                } else {
                    res = R.layout.layout_commodity_item_goods;
                }
            }
        } else {
            if (CountEveryPage == 8) {
                res = R.layout.layout_commodity_item_goods;
            } else if (CountEveryPage == 10) {
                res = R.layout.layout_commodity_item_10_goods;
            } else if (CountEveryPage == 20) {
                res = R.layout.layout_commodity_item_20_goods;
            } else if (CountEveryPage == 30) {
                res = R.layout.layout_commodity_item_30_goods;
            } else {

            }
        }

        return res;
    }

    public int getPayLayout() {
        int res = 0;
        String m_ScreenInch = TcnVendIF.getInstance().getScreenInch();
        if (m_ScreenInch.equals(TcnCommon.SCREEN_INCH[1])) {
            res = R.layout.dialog_pay_small;
        } else {
            res = R.layout.dialog_pay;
        }
        return res;
    }

    public int getKeyboardLayout () {
        int res = 0;
        String m_ScreenInch = TcnVendIF.getInstance().getScreenInch();
        if (m_ScreenInch.equals(TcnCommon.SCREEN_INCH[1])) {
            res = R.layout.tcn_input_keyboard_small;
        } else {
            res = R.layout.tcn_input_keyboard;
        }
        return res;
    }

    public int getGoodsCount(String type) {
        TcnVendIF.getInstance().setGoodsType(type);
        String srtKeyType = TcnShareUseData.getInstance().getKeyAndSlotDisplayType();
        if (TcnShareUseData.getInstance().isShowByGoodsCode() && (srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[0]))) {
            return TcnVendIF.getInstance().getAliveGoodsCount();
        } else if (!(TcnConstant.DATA_TYPE[0]).equals(TcnShareUseData.getInstance().getTcnDataType()) && !(TcnConstant.DATA_TYPE[1]).equals(TcnShareUseData.getInstance().getTcnDataType())
                && (srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[1]) || srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[2]))) {
            List<Integer>  keyOrSlotNoList = TcnVendIF.getInstance().getAliveKeyCoil();
            int iCount = 0;
            if (keyOrSlotNoList != null) {
                iCount = keyOrSlotNoList.size();
            }
            return iCount;
        }
        else {
            int iCount = 0;
            List<Coil_info> infoList = null;
            if ((TcnVendIF.GOODS_TYPE_ALL).equals(type)) {
                infoList = TcnVendIF.getInstance().getAliveCoil();
            } else if ((TcnVendIF.GOODS_TYPE_OTHER).equals(type)) {
                infoList = TcnVendIF.getInstance().getAliveCoilExcept();
            } else {
                infoList = TcnVendIF.getInstance().getAliveCoilFromType(type);
            }

            if (infoList != null) {
                iCount = infoList.size();
            }
            return iCount;
        }
    }

    public List<String> getAliveType() {
        List<String> mList = TcnVendIF.getInstance().getAliveType();
        List<String> mTypeList = new ArrayList<String>() ;
        mTypeList.add(TcnVendIF.GOODS_TYPE_ALL);
        if (mList == null) {
            return mTypeList;
        }
        for (int i = 1; i < (mList.size() + 1); i++) {
            mTypeList.add(mList.get(i-1));
        }
        return mTypeList;
    }

    public String getGoodsType() {
        if (!TcnShareUseData.getInstance().isShowType()) {
            TcnVendIF.getInstance().setGoodsType(TcnVendIF.GOODS_TYPE_ALL);
        }
        return TcnVendIF.getInstance().getGoodsType();
    }

    public List<String> getGoodsLuckyNameList() {
        List<String> mNameList = new ArrayList<>();
        String sDataType = TcnShareUseData.getInstance().getTcnDataType();
        if (sDataType.equals(TcnConstant.DATA_TYPE[1])) {
            List<Coil_info> sInfoList = TcnVendIF.getInstance().getAliveCoil();
            if ((null == sInfoList) || (sInfoList.size() < 1)) {
                return mNameList;
            }
            mNameList.add(m_application.getString(R.string.tip_lucky_try_again));
            for (Coil_info info:sInfoList) {
                if ((String.valueOf(UIComBack.TYPE_CJ_FLAG)).equals(info.getType())) {
                    if (!mNameList.contains(info.getPar_name())) {
                        mNameList.add(info.getPar_name());
                    }
                }
            }
        } else {
            List<Coil_info> sInfoList = TcnVendIF.getInstance().getAliveCoil();
            if ((null == sInfoList) || (sInfoList.size() < 1)) {
                return mNameList;
            }
            mNameList.add(m_application.getString(R.string.tip_lucky_try_again));
            for (Coil_info info:sInfoList) {
                if ((String.valueOf(UIComBack.TYPE_CJ_FLAG)).equals(info.getType())) {
                    if (!mNameList.contains(info.getPar_name())) {
                        mNameList.add(info.getPar_name());
                    }
                }
            }
        }

        return mNameList;
    }

    public UIGoodsInfo getGoodsInfo(int position) {
        UIGoodsInfo mGoodsInfo = new UIGoodsInfo();
        String type = TcnVendIF.getInstance().getGoodsType();
        int iCount = getGoodsCount(type);
        if ((iCount > 0) && (position < iCount) && (position >= 0)) {
            String srtKeyType = TcnShareUseData.getInstance().getKeyAndSlotDisplayType();
            if (TcnShareUseData.getInstance().isShowByGoodsCode() && (srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[0]))) {
                List<Goods_info> listInfo = TcnVendIF.getInstance().getAliveGoods();
                if (position < listInfo.size()) {
                    Goods_info info = listInfo.get(position);
                    if (info != null) {
                        mGoodsInfo.setGoods_name(info.getGoods_name());
                        mGoodsInfo.setGoods_id(info.getGoods_id());
                        mGoodsInfo.setGoods_price(info.getGoods_price());
                        mGoodsInfo.setGoods_type(info.getGoods_type());
                        mGoodsInfo.setGoods_introduce(info.getGoods_introduce());
                        mGoodsInfo.setGoods_status(info.getGoods_status());
                        mGoodsInfo.setGoods_details_url(info.getGoods_details_url());
                        mGoodsInfo.setGoods_stock(info.getGoods_stock());
                        mGoodsInfo.setGoods_url(info.getGoods_url());
                    }
                }

            }  else if (!(TcnConstant.DATA_TYPE[0]).equals(TcnShareUseData.getInstance().getTcnDataType()) && !(TcnConstant.DATA_TYPE[1]).equals(TcnShareUseData.getInstance().getTcnDataType())
                    && (srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[1]) || srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[2]))) {

                List<Integer> infoList = TcnVendIF.getInstance().getAliveKeyCoil();
                if ((infoList != null) && (infoList.size() > 0)) {
                    if (position >= infoList.size()) {
                        return mGoodsInfo;
                    }
                    int keyOrSlotNo = infoList.get(position);
                    if (keyOrSlotNo < 100) {
                        Key_info info = TcnVendIF.getInstance().getKeyInfo(keyOrSlotNo);
                        mGoodsInfo.setGoods_name(info.getPar_name());
                        mGoodsInfo.setGoods_id(info.getGoodsCode());
                        mGoodsInfo.setGoods_price(info.getPar_price());
                        mGoodsInfo.setGoods_type(info.getType());
                        mGoodsInfo.setGoods_introduce(info.getContent());
                        mGoodsInfo.setGoods_status(info.getWork_status());
                        mGoodsInfo.setGoods_details_url(info.getGoods_details_url());
                        mGoodsInfo.setGoods_url(info.getImg_url());
                        mGoodsInfo.setKeyNum(info.getKeyNum());
                        if (info.getWork_status() == UtilsDB.KEY_STATE_NO_GOODS) {
                            mGoodsInfo.setGoods_stock(0);
                        }

                    } else {
                        Coil_info info = TcnVendIF.getInstance().getCoilInfo(keyOrSlotNo);
                        mGoodsInfo.setGoods_name(info.getPar_name());
                        mGoodsInfo.setCoil_id(info.getCoil_id());
                        mGoodsInfo.setGoods_id(info.getGoodsCode());
                        mGoodsInfo.setGoods_price(info.getPar_price());
                        mGoodsInfo.setGoods_type(info.getType());
                        mGoodsInfo.setGoods_introduce(info.getContent());
                        mGoodsInfo.setGoods_status(info.getWork_status());
                        mGoodsInfo.setGoods_details_url(info.getGoods_details_url());
                        mGoodsInfo.setGoods_stock(info.getExtant_quantity());
                        mGoodsInfo.setGoods_url(info.getImg_url());
                        mGoodsInfo.setKeyNum(info.getKeyNum());
                    }
                }
            }
            else {
                List<Coil_info> listInfo = null;
                if ((TcnVendIF.GOODS_TYPE_ALL).equals(type)) {
                    listInfo = TcnVendIF.getInstance().getAliveCoil();
                } else if ((TcnVendIF.GOODS_TYPE_OTHER).equals(type)) {
                    listInfo = TcnVendIF.getInstance().getAliveCoilExcept();
                } else {
                    listInfo = TcnVendIF.getInstance().getAliveCoilFromType(type);
                }
                Coil_info info = listInfo.get(position);
                mGoodsInfo.setGoods_name(info.getPar_name());
                mGoodsInfo.setCoil_id(info.getCoil_id());
                mGoodsInfo.setGoods_id(info.getGoodsCode());
                mGoodsInfo.setGoods_price(info.getPar_price());
                mGoodsInfo.setGoods_type(info.getType());
                mGoodsInfo.setGoods_introduce(info.getContent());
                mGoodsInfo.setGoods_status(info.getWork_status());
                mGoodsInfo.setGoods_details_url(info.getGoods_details_url());
                mGoodsInfo.setGoods_stock(info.getExtant_quantity());
                mGoodsInfo.setGoods_url(info.getImg_url());
                mGoodsInfo.setKeyNum(info.getKeyNum());
            }

        }
        return mGoodsInfo;
    }

    public int getSlotPosition(int slotNo) {
        int iPosition = 0;
        String srtKeyType = TcnShareUseData.getInstance().getKeyAndSlotDisplayType();
        if (TcnShareUseData.getInstance().isShowByGoodsCode() && (srtKeyType.equals( TcnConstant.KEY_SLOT_DISPLAY_TYPE[0]))) {
            //
        } else {
            List<Coil_info> listInfo = TcnVendIF.getInstance().getAliveCoil();
            if (listInfo != null) {
                for (Coil_info info:listInfo) {
                    if (info.getCoil_id() == slotNo) {
                        iPosition = listInfo.indexOf(info);
                    }
                }
            }
        }
        return iPosition;
    }

    private SurfaceHolderCallback m_callback_ad_video = null;
    private SurfaceHolderCallback m_callback_ad_image = null;
    private SurfaceHolderCallback m_callback_stdby_video = null;
    private SurfaceHolderCallback m_callback_stdby_image = null;

    private class SurfaceHolderCallback implements SurfaceHolder.Callback {
        private int iAdType = -1;
        private SurfaceView mSurfaceView = null;

        public SurfaceHolderCallback(int adType, SurfaceView surfaceView) {
            iAdType = adType;
            mSurfaceView = surfaceView;
        }


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            setSurfaceCreated(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            setSurfaceDestroyed(holder);
        }

        private void setSurfaceCreated(SurfaceHolder holder) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "setSurfaceCreated() iAdType: "+iAdType);
            if (null == holder) {
                TcnVendIF.getInstance().LoggerError(TAG, "setSurfaceCreated() holder is null");
                return;
            }
            if (ADVERT_TYPE_ADVERT_VIDEO == iAdType) {
                TcnVendIF.getInstance().setVideoSurfaceCreated(holder, mSurfaceView.getWidth(), mSurfaceView.getHeight());
            } else if (ADVERT_TYPE_ADVERT_IMAGE == iAdType) {
                TcnVendIF.getInstance().setImageSurfaceCreated(holder, mSurfaceView.getWidth(), mSurfaceView.getHeight());
            } else if (ADVERT_TYPE_STANDBY_VIDEO == iAdType) {
                TcnVendIF.getInstance().setScreenSurfaceVideoCreated(holder, mSurfaceView.getWidth(), mSurfaceView.getHeight());
            } else if (ADVERT_TYPE_STANDBY_IMAGE == iAdType) {
                TcnVendIF.getInstance().setScreenSurfaceCreated(holder, mSurfaceView.getWidth(), mSurfaceView.getHeight());
            } else {

            }
        }

        private void setSurfaceDestroyed(SurfaceHolder holder) {
            TcnVendIF.getInstance().LoggerDebug(TAG, "setSurfaceDestroyed() iAdType: "+iAdType);
            if (ADVERT_TYPE_ADVERT_VIDEO == iAdType) {
                TcnVendIF.getInstance().setVideoSurfaceDestroyed(holder);
            } else if (ADVERT_TYPE_ADVERT_IMAGE == iAdType) {
                TcnVendIF.getInstance().setImageSurfaceDestroyed(holder);
            } else if (ADVERT_TYPE_STANDBY_VIDEO == iAdType) {
                TcnVendIF.getInstance().setScreenSurfaceVideoDestroyed(holder);
            } else if (ADVERT_TYPE_STANDBY_IMAGE == iAdType) {
                TcnVendIF.getInstance().setScreenSurfaceDestroyed(holder);
            } else {

            }
        }
    }

    public void registerListener () {
        TcnVendIF.getInstance().registerListener(m_vendListener);
    }

    public void unregisterListener() {
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            switch (cEventInfo.m_iEventID) {
                case VendEventID.BACKGROUND_AISLE_MANAGE:
                    break;
                case VendEventID.RESTART_MAIN_ACTIVITY:
                    if (m_application != null) {
                        Intent mIntent = new Intent(m_application,MainAct.class);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        m_application.startActivity(mIntent);
                    }
                    break;
                case VendEventID.SERVER_HEART_CONNECTED:
                case VendEventID.SERVER_HEART_RECONNECTED:
                    if (TcnShareUseData.getInstance().isAppForegroundCheck()) {
                        if (!TcnVendIF.getInstance().isAppForeground()) {
                            if (m_application != null) {
                                Intent mIntentApp = new Intent(m_application,MainAct.class);
                                mIntentApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                m_application.startActivity(mIntentApp);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
