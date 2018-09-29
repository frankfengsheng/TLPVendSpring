package com.tcn.vendspring.keyboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tcn.background.LoginMenu;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.view.KeyBoardView;
import com.tcn.vendspring.R;

import controller.UICommon;


/**
 * Created by Administrator on 2016/4/29.
 */
public class FragmentVerify extends KeyBoardView {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setInputListener(m_Input);
        View mView = super.onCreateView(inflater, container, savedInstanceState);
        Button back = (Button) mView.findViewById(R.id.key_board_back);
        if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())) {
            //
        } else {
            if (!TcnShareUseData.getInstance().isShowShopping()) {
                if (back != null) {
                    back.setVisibility(View.INVISIBLE);
                }
            }
        }
        return mView;
    }

    @Override
    public int getLayout() {
        return UICommon.getInstance().getKeyboardLayout();
    }

    @Override
    public void setBack() {
        super.setBack();
        TcnVendIF.getInstance().reqShowOrHideAdMediaImmediately(TcnVendIF.SHOW_AD_MEDIA);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TcnShareUseData.getInstance().isQuickSetupGuideOpen()) {
            setTime(Color.RED,UICommon.getInstance().getTextSize16(),getString(R.string.qkstp_input_and_login));
        } else {
            setTime(UICommon.getInstance().getTextSize16(),"");
        }
        setPassWord(TcnShareUseData.getInstance().getQuickEntrPassword());
        setHintText(R.string.ui_input_slotno);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            clearAll();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroy();
        m_Input = null;
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
            TcnVendIF.getInstance().reqTextSpeak(str);
        }

        @Override
        public void onClear(String data) {
            if ((null == data) || (data.length() < 1)) {
                return;
            }
            TcnVendIF.getInstance().reqTextSpeak(getString(R.string.backgroound_cancel));
        }

        @Override
        public void onEnter(String data) {
            if ((null == data) || (!isAdded())) {
                return;
            }
            int iLength = data.length();
            if (iLength > 3) {
                com.tcn.uicommon.TcnUtility.getsToastSign(getActivity(),getString(R.string.notify_invalid_slot));
                TcnVendIF.getInstance().reqTextSpeak(getString(R.string.notify_invalid_slot));
            } else {
                if (TcnVendIF.getInstance().isDigital(data)) {
                    TcnVendIF.getInstance().reqSelectSlotNo(Integer.valueOf(data));
                    TcnVendIF.getInstance().reqTextSpeak(getString(R.string.backgroound_ensure));
                } else {
                    com.tcn.uicommon.TcnUtility.getsToastSign(getActivity(),getString(R.string.notify_invalid_slot));
                    TcnVendIF.getInstance().reqTextSpeak(getString(R.string.notify_invalid_slot));
                }
            }
        }

        @Override
        public void onClearAll() {

        }

        @Override
        public void onPassWordVerified() {
            Intent in = new Intent(getActivity(), LoginMenu.class);
            startActivity(in);
        }
    }
}
