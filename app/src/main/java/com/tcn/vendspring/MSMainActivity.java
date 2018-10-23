package com.tcn.vendspring;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.tcn.background.LoginMenu;
import com.tcn.background.SerialPortSetting;
import com.tcn.funcommon.NetWorkUtil;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.dialog.LoadingDialog;
import com.tcn.uicommon.dialog.OutDialog;
import com.tcn.uicommon.dialog.ShopSuccessDialog;
import com.tcn.uicommon.dialog.UsbProgressDialog;
import com.tcn.vendspring.R;
import com.tcn.vendspring.help.DialogHelp;
import com.tcn.vendspring.keyboard.DialogVerify;
import com.tcn.vendspring.pay.TlpDialogPay;
import com.tlp.vendspring.MSInputMachineCodeActivity;
import com.tlp.vendspring.activity.MSLoginMenu;
import com.tlp.vendspring.fragment.MSBannerFragment;
import com.tlp.vendspring.fragment.MSGoodsFragment;
import com.tlp.vendspring.util.MSUserUtils;
import com.tlp.vendspring.util.ToastUtil;

import controller.UICommon;
import controller.VendService;


public class MSMainActivity extends FragmentActivity implements View.OnClickListener{


    private static final String TAG = "MainAct";
    private static final int ALIVE_COIL_GOODS	 = 1;
    private static final int GOTO_BACKGROUND      = 2;
    private static final int SHOWN_FULLSCREEN      = 3;
    private static final int SHOW_SHIPSUCCESS_DIALOG = 5;
    private static int m_listData_count = 0;
    private boolean m_bHasData = false;
    private RelativeLayout rl_title;
    FragmentTransaction transaction;
    FragmentManager fragmentManager;

    private OutDialog m_OutDialog = null;
    private LoadingDialog m_LoadingDialog = null;
    private TlpDialogPay m_DialogPay = null;
    private DialogVerify m_DialogVerify = null;
    private DialogHelp m_DialogHelp = null;
    private ShopSuccessDialog m_shopSuccessDialog;
    private UsbProgressDialog m_upProgress;
    MSGoodsFragment goodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlpmain);
        init_view();
        TcnVendIF.getInstance().registerListener(m_vendListener);
        if (TcnVendIF.getInstance().isServiceRunning()){
            TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate() isServiceRunning");
        } else {
            startService(new Intent(getApplication(), VendService.class));
        }
    }
    private void init_view(){

        rl_title= (RelativeLayout) findViewById(R.id.tlp_rl_main_titlebar);


        rl_title.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tlp_rl_main_titlebar:
                Intent intent=new Intent(getApplicationContext(), MSLoginMenu.class);
                startActivity(intent);
                break;
        }
    }

    private Handler m_handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case ALIVE_COIL_GOODS:

                    break;
                case GOTO_BACKGROUND:
                    Intent in = new Intent(MSMainActivity.this, MSLoginMenu.class);
                    startActivity(in);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    break;
                case SHOWN_FULLSCREEN:
                    break;
                case SHOW_SHIPSUCCESS_DIALOG:
                    UICommon.getInstance().setShipSuccessed(false);
                    shipSuccessTips();
                    break;
                default:
                    break;
            }
        }
    };

    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {

        @Override
        public void VendEvent(VendEventInfo cEventInfo) {

            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.QUERY_ALIVE_COIL:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "VendListener QUERY_ALIVE_COIL 货道数cEventInfo.m_lParam1: " + cEventInfo.m_lParam1);
                    m_listData_count = cEventInfo.m_lParam1;
                    if (!m_bHasData) {
                        m_handler.removeMessages(ALIVE_COIL_GOODS);
                        m_handler.sendEmptyMessage(ALIVE_COIL_GOODS);
                    }
                    break;
                case TcnVendEventID.SHOW_TOAST:
                    TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4, Toast.LENGTH_LONG).show();
                    break;
                case TcnVendEventID.ALL_FILES_PLAY_FAILED:
                    TcnUtility.getToast(MSMainActivity.this, getString(R.string.notify_no_video), Toast.LENGTH_LONG).show();
                    break;
                case TcnVendEventID.COMMAND_CONNECT_SERVER_SUCCESS:
                    //连上后台
                    //m_serDisconnect.setVisibility(View.GONE);
                    break;
                case TcnVendEventID.COMMAND_CONNECT_SERVER_FAILED:
                    //没连上后台
					/*if(TcnContentUtils.isNetConnected(MainAct.this)){
						m_serDisconnect.setVisibility(View.VISIBLE);
					} else {
						m_iSignal = NetWorkUtil.SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
						setSignalIcon(m_iSignal);
					}*/
                    break;
                case TcnVendEventID.COMMAND_NOTSET_CIPMODE:
                    TcnUtility.getToast(MSMainActivity.this, getString(R.string.notify_atcip_mode));
                    break;
                case TcnVendEventID.IMAGE_SCREEN:
					/*if (m_surface_standby_image != null) {
						m_surface_standby_image.setImageBitmap(TcnVendIF.getInstance().getScreenBitmap());
					}*/
                    break;
                case TcnVendEventID.TEXT_AD:
                    //initTextAd();
                    break;
                case TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA:
                    if (cEventInfo.m_lParam1 == TcnVendIF.SHOW_AD_MEDIA) {
                        //showMediaAd();
                    } else {
                        //hideMediaAd();
                    }

                    break;

                case TcnVendEventID.COMMAND_DOOR_SWITCH:
                    if (cEventInfo.m_lParam1 != TcnVendIF.COMMAND_CLOSE_DOOR) {
                        Intent in = new Intent(MSMainActivity.this, MSLoginMenu.class);
                        startActivity(in);
                    }
                    break;
                case TcnVendEventID.COMMAND_TEST:

                    break;
                case TcnVendEventID.SERIAL_PORT_CONFIG_ERROR:
                    TcnVendIF.getInstance().LoggerDebug(TAG, "SERIAL_PORT_CONFIG_ERROR");
                    //TcnUtility.getToast(MainAct.this, getString(R.string.error_seriport));
                    DisplayError(R.string.error_configuration);
                    break;
                case TcnVendEventID.SERIAL_PORT_SECURITY_ERROR:
                    DisplayError(R.string.error_security);
                    break;
                case TcnVendEventID.SERIAL_PORT_UNKNOWN_ERROR:
                    DisplayError(R.string.error_unknown);
                    break;
                case TcnVendEventID.COMMAND_SELECT_GOODS:
                   //showDialogPay();
                    break;
                case TcnVendEventID.COMMAND_INVALID_SLOTNO:
                    TcnUtility.getToast(MSMainActivity.this, getString(R.string.notify_invalid_slot));
                    dismissAllDialog();
                    break;
                case TcnVendEventID.COMMAND_SOLD_OUT:
                    if (cEventInfo.m_lParam1 > 0) {
                        TcnUtility.getToast(MSMainActivity.this, getString(R.string.aisle_name)+cEventInfo.m_lParam1+getString(R.string.notify_sold_out));
                    } else {
                        TcnUtility.getToast(MSMainActivity.this, getString(R.string.notify_sold_out));
                    }
                    dismissAllDialog();
                    break;
                case TcnVendEventID.COMMAND_FAULT_SLOTNO:
                    TcnUtility.getToast(MSMainActivity.this,cEventInfo.m_lParam4);
                    dismissAllDialog();
                    break;
                case TcnVendEventID.COMMAND_SHIPPING:
                    if (m_DialogPay != null) {
                        UICommon.getInstance().setCanRefund(false);
                        m_DialogPay.dismiss();
                    }

                    if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                        if (m_OutDialog == null) {
                            m_OutDialog = new OutDialog(MSMainActivity.this, String.valueOf(cEventInfo.m_lParam1), cEventInfo.m_lParam4);
                        } else {
                            m_OutDialog.setText(cEventInfo.m_lParam4);
                        }
                        m_OutDialog.cleanData();
                    } else {
                        if (m_OutDialog == null) {
                            m_OutDialog = new OutDialog(MSMainActivity.this, String.valueOf(cEventInfo.m_lParam1), getString(R.string.notify_shipping));
                        } else {
                            m_OutDialog.setText(getString(R.string.notify_shipping));
                        }
                    }
                       if(MSMainActivity.this!=null&&m_OutDialog!=null) {
                           m_OutDialog.setNumber(String.valueOf(cEventInfo.m_lParam1));
                           m_OutDialog.show();
                       }

                    break;

                case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:
                    //通知fragment出货成功
                    goodsFragment.shipSucess();
                    if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
                        if (m_DialogPay.isShowing()) {
                            UICommon.getInstance().setShipSuccessed(true);
                            //m_DialogPay.anim();
                            m_handler.sendEmptyMessageDelayed(SHOW_SHIPSUCCESS_DIALOG,2000);
                        } else {
                            shipSuccessTips();
                        }
                    } else {
                        shipSuccessTips();
                    }
                    break;
                case TcnVendEventID.COMMAND_SHIPMENT_FAILURE:
                    //通知fragment出货失败
                    goodsFragment.shipFailed();
                    if(null != m_OutDialog) {
                        m_OutDialog.cancel();
                    }
                    if(null == m_LoadingDialog) {
                        m_LoadingDialog = new LoadingDialog(MSMainActivity.this,getString(R.string.notify_shipment_fail),getString(R.string.notify_contact_merchant));
                    }
                    m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_fail));
                    m_LoadingDialog.setTitle(getString(R.string.notify_contact_merchant));
                    m_LoadingDialog.setShowTime(3);
                    m_LoadingDialog.show();
                    break;
                case TcnVendEventID.WX_TRADE_REFUND:
                    TcnUtility.getToast(MSMainActivity.this,cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.ALIPAY_SCAN_COLSE:
                case TcnVendEventID.ALIPAY_WAVE_COLSE:
                    TcnUtility.getToast(MSMainActivity.this,cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_PLAY_IMAGE:

                    break;
                case TcnVendEventID.CMD_PLAY_VIDEO:

                    break;
                case TcnVendEventID.IMAGE_BACKGROUND:

                    break;
                case TcnVendEventID.IMAGE_HELP:
                    if (m_DialogHelp != null) {
                        m_DialogHelp.refsh();
                    }
                    break;
                case TcnVendEventID.NETWORK_CHANGE:

                    break;
                case TcnVendEventID.UPDATE_TIME:

                    break;
                case TcnVendEventID.BACK_TO_SHOPPING:
                    dismissAllDialog();
                    break;
                case TcnVendEventID.CMD_PLAY_SCREEN_IMAGE:
                    //setPlayStandbyImage();
                    break;
                case TcnVendEventID.CMD_PLAY_SCREEN_VIDEO:
                    //setPlayStandbyVideo();
                    break;
                case TcnVendEventID.COMMAND_DOOR_IS_OPEND:
                    ToastUtil.showToast(MSMainActivity.this, getString(R.string.tip_close_door));
                    break;
                case TcnVendEventID.PROMPT_INFO:
                    TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.NETWORK_NOT_GOOOD:
                    if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                        TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4);
                    }
                    break;
                case TcnVendEventID.PAY_FAIL:
                    if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
                        TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4);
                    }
                    break;
                case TcnVendEventID.TEMPERATURE_INFO:

                    break;
                case TcnVendEventID.COMMAND_CANCEL_PAY:
                    backMainMenu();
                    break;
                case TcnVendEventID.REMOUT_ADVERT_TEXT:

                    break;
                case TcnVendEventID.PLESE_CLOSE_FOOR:
                    TcnUtility.getToast(MSMainActivity.this, getString(R.string.tip_close_door));
                    break;
                case TcnVendEventID.COMMAND_SYSTEM_BUSY:
                    TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_MACHINE_LOCKED:
                    TcnUtility.getToast(MSMainActivity.this, cEventInfo.m_lParam4);
                    break;
                default:
                    break;
            }
        }

    }
    private void dismissAllDialog() {
        if (isFinishing()) {
            return;
        }
        UICommon.getInstance().setShipSuccessed(false);
        if (m_DialogHelp != null) {
            m_DialogHelp.dismiss();
        }
        if (m_DialogVerify != null) {
            m_DialogVerify.dismiss();
        }
        if (m_DialogPay != null) {
            m_DialogPay.dismiss();
        }
    }


    private void backMainMenu() {
        if (m_DialogPay != null) {
            if (!UICommon.getInstance().isShipSuccessed()) {
                m_DialogPay.dismiss();
            }
        }
        if (null != m_OutDialog) {
            m_OutDialog.dismiss();
        }
        if (m_LoadingDialog != null) {
            m_LoadingDialog.dismiss();
        }
        if (m_shopSuccessDialog != null) {
            m_shopSuccessDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (m_upProgress != null) {
            m_upProgress.dismiss();
            m_upProgress.deInit();
            m_upProgress = null;
        }
        if (m_OutDialog != null) {
            m_OutDialog.deInit();
            m_OutDialog = null;
        }
        if (m_LoadingDialog != null) {
            m_LoadingDialog.deInit();
            m_LoadingDialog = null;
        }
        if (m_shopSuccessDialog != null) {
            m_shopSuccessDialog.deInit();
            m_shopSuccessDialog = null;
        }
    }

    private void shipSuccessTips() {
        if (null != m_OutDialog) {
            m_OutDialog.cancel();
        }

        if (m_LoadingDialog == null) {
            m_LoadingDialog = new LoadingDialog(MSMainActivity.this, getString(R.string.notify_shipment_success), getString(R.string.notify_receive_goods));
        } else {
            m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_success));
            m_LoadingDialog.setTitle(getString(R.string.notify_receive_goods));
        }

        m_LoadingDialog.setShowTime(3);

        m_LoadingDialog.show();
    }
    private void DisplayError(int resourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(resourceId);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in = new Intent(MSMainActivity.this, SerialPortSetting.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MSMainActivity.this.startActivity(in);
            }
        });
        Dialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //判断有没有绑定设备ID
        String machineCode=MSUserUtils.getInstance().getMachineCode(getApplicationContext());
        if(TextUtils.isEmpty(machineCode)){
            Intent intent=new Intent(this, MSInputMachineCodeActivity.class);
            startActivity(intent);
        }
        fragmentManager=this.getFragmentManager();
        transaction=fragmentManager.beginTransaction();
        MSBannerFragment bannerfragment= MSBannerFragment.newInstance(null,null);
        goodsFragment= MSGoodsFragment.newInstance(null,null);
        transaction.add(R.id.tlp_ly_advertisement_fragment,bannerfragment);
        transaction.add(R.id.tlp_ly_goods,goodsFragment);
        transaction.commit();
    }
}
