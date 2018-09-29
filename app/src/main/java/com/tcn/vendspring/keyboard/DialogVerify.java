package com.tcn.vendspring.keyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.tcn.background.LoginMenu;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.dialog.KeyBoardDialog;
import com.tcn.uicommon.resources.Resources;
import com.tcn.vendspring.R;

import controller.UICommon;

/**
 * Created by Administrator on 2016/11/29.
 */
public class DialogVerify extends KeyBoardDialog {
    private static final int CMD_GO_BACK		      = 1;
    private static final int TIME_GO_BACK		      = 60000;
    private Context m_context = null;

    public DialogVerify(Context context) {
        super(context);
        setInputListener(m_Input);
        m_context = context;

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
        }
        else if (ImageController.SCREEN_TYPE_S768X1366 == TcnVendIF.getInstance().getScreenType()) {
            if (TcnShareUseData.getInstance().isFullScreen()) {
                lp.height = 1296;
            } else {
                lp.height = 864;
            }
            lp.y = 35;
        } else if (ImageController.SCREEN_TYPE_S1366X768 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 698;
            lp.y = 35;
        }
        else if (ImageController.SCREEN_TYPE_S800X1280 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 780;
        } else if (ImageController.SCREEN_TYPE_S600X1024 == TcnVendIF.getInstance().getScreenType()) {
            lp.height = 646;
        } else if (ImageController.SCREEN_TYPE_S1024X600 == TcnVendIF.getInstance().getScreenType()) {
            lp.width = 604;
            lp.x = 420;
            lp.height = 560;
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
    }

    @Override
    public int getLayout() {
        return UICommon.getInstance().getKeyboardLayout();
    }

    @Override
    public void show() {
        super.show();
        if (TcnShareUseData.getInstance().isQuickSetupGuideOpen()) {
            setTime(Color.RED,UICommon.getInstance().getTextSize20(),m_context.getString(R.string.qkstp_input_and_login));
        } else {
            setTime(UICommon.getInstance().getTextSize20(),"");
        }
        if (!TcnVendIF.getInstance().isUserMainBoard()) {
            if (TcnShareUseData.getInstance().isCashPayOpen()) {
                setCancelText(m_context.getString(R.string.key_cancel_return_coin));
            }
        }
        setPassWord(TcnShareUseData.getInstance().getQuickEntrPassword());
        setHintText(R.string.ui_input_slotno);

        TcnVendIF.getInstance().startGoBackShopTimer();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        TcnVendIF.getInstance().stopGoBackShopTimer();
        clearAll();
        m_handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_DOWN) {
            TcnVendIF.getInstance().startGoBackShopTimer();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void deInit() {
        if (m_handler != null) {
            m_handler.removeCallbacksAndMessages(null);
            m_handler = null;
        }
        destroy();
        m_Input = null;
        m_context = null;
    }

    private TcnInputHanppens m_Input = new TcnInputHanppens();
    private class TcnInputHanppens implements InputHanppens {

        @Override
        public void onSelectGoods(String data) {
            TcnVendIF.getInstance().reqTextSpeak(data);
        }

        @Override
        public void onTakeGoods(String data) {
            TcnVendIF.getInstance().reqTextSpeak(data);
        }

        @Override
        public void onInputNumber(String str, int id, long time) {
            m_handler.removeMessages(CMD_GO_BACK);
            m_handler.sendEmptyMessageDelayed(CMD_GO_BACK,TIME_GO_BACK);
        }

        @Override
        public void onClear(String data) {
            m_handler.removeMessages(CMD_GO_BACK);
            m_handler.sendEmptyMessageDelayed(CMD_GO_BACK,TIME_GO_BACK);


            if ((null == data) || (data.length() < 1)) {
                return;
            }

            TcnVendIF.getInstance().reqTextSpeak(m_context.getString(R.string.backgroound_cancel));
        }

        @Override
        public void onEnter(String data) {
            m_handler.removeMessages(CMD_GO_BACK);
            m_handler.sendEmptyMessageDelayed(CMD_GO_BACK,TIME_GO_BACK);
            if (null == data) {
                return;
            }
            int iLength = data.length();
            if (iLength > 3) {
                com.tcn.uicommon.TcnUtility.getToast(m_context,m_context.getString(R.string.notify_invalid_slot));
                TcnVendIF.getInstance().reqTextSpeak(m_context.getString(R.string.notify_invalid_slot));
            } else {
                if (TcnVendIF.getInstance().isDigital(data)) {
                    TcnVendIF.getInstance().reqSelectSlotNo(Integer.valueOf(data));
                    TcnVendIF.getInstance().reqTextSpeak(m_context.getString(R.string.backgroound_ensure));
                    dismiss();
                } else {
                    com.tcn.uicommon.TcnUtility.getToast(m_context,m_context.getString(R.string.notify_invalid_slot));
                    TcnVendIF.getInstance().reqTextSpeak(m_context.getString(R.string.notify_invalid_slot));
                }
            }
        }

        @Override
        public void onClearAll() {

        }

        @Override
        public void onPassWordVerified() {
            Intent in = new Intent(m_context, LoginMenu.class);
            m_context.startActivity(in);
            dismiss();
        }

        @Override
        public void onBack() {

        }
    }

    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CMD_GO_BACK:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}
