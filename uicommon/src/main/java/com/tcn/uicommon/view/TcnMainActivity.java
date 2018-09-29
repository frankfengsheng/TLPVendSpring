package com.tcn.uicommon.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/10/22.
 */
public class TcnMainActivity extends Activity {

    private InputMethodManager m_managerInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideInputKeyBoard();
    }

    @Override
    protected void onPause() {
        hideInputKeyBoard();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_managerInput = null;
    }

    //点击空白处输入法消失1
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    private void hideInputKeyBoard() {
        m_managerInput = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        if((m_managerInput != null) && (getCurrentFocus()!=null) && (getCurrentFocus().getWindowToken()!=null)){
            m_managerInput.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //点击空白处输入法消失2

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
