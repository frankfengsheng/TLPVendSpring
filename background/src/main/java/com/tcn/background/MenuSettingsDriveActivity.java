package com.tcn.background;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;


/**
 * Created by Administrator on 2016/8/26.
 */
public class MenuSettingsDriveActivity extends TcnMainActivity {
    private static final String TAG = "MenuSettingsDriveActivity";

    private Titlebar m_Titlebar = null;

    private Button menu_drive_spring = null;
    private Button menu_drive_lift = null;
    private Button menu_drive_lattice = null;
    private Button menu_drive_coff = null;
    private Button menu_drive_snake = null;
    private Button menu_drive_light = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_settings_layout_drive);
        TcnVendIF.getInstance().LoggerDebug(TAG, "MenuSettingsDriveActivity onCreate()");

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }
        if (menu_drive_spring != null) {
            menu_drive_spring.setOnClickListener(null);
            menu_drive_spring = null;
        }
        if (menu_drive_lift != null) {
            menu_drive_lift.setOnClickListener(null);
            menu_drive_lift = null;
        }
        if (menu_drive_lattice != null) {
            menu_drive_lattice.setOnClickListener(null);
            menu_drive_lattice = null;
        }
        if (menu_drive_coff != null) {
            menu_drive_coff.setOnClickListener(null);
            menu_drive_coff = null;
        }
        if (menu_drive_snake != null) {
            menu_drive_snake.setOnClickListener(null);
            menu_drive_snake = null;
        }
        if (menu_drive_light != null) {
            menu_drive_light.setOnClickListener(null);
            menu_drive_light = null;
        }
        m_TitleBarListener = null;
        m_ButtonClickLister = null;
    }

    private void initView() {

        m_Titlebar = (Titlebar) findViewById(R.id.menu_setttings_titlebar);
        if (m_Titlebar != null) {
            m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
            m_Titlebar.setButtonName(R.string.menu_settings);
            m_Titlebar.setTitleBarListener(m_TitleBarListener);
        }

        menu_drive_spring = (Button) findViewById(R.id.menu_drive_spring);
        if (menu_drive_spring != null) {
            menu_drive_spring.setText(getString(R.string.drive_spring_set));
            menu_drive_spring.setOnClickListener(m_ButtonClickLister);
            menu_drive_spring.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_spring.setTextColor(Color.parseColor("#ffffff"));
        }

        menu_drive_lift = (Button) findViewById(R.id.menu_drive_lift);
        if (menu_drive_lift != null) {
            menu_drive_lift.setText(getString(R.string.drive_lift_set));
            menu_drive_lift.setOnClickListener(m_ButtonClickLister);
            menu_drive_lift.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_lift.setTextColor(Color.parseColor("#ffffff"));
        }

        menu_drive_lattice = (Button) findViewById(R.id.menu_drive_lattice);
        if (menu_drive_lattice != null) {
            menu_drive_lattice.setText(getString(R.string.drive_lattice_set));
            menu_drive_lattice.setOnClickListener(m_ButtonClickLister);
            menu_drive_lattice.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_lattice.setTextColor(Color.parseColor("#ffffff"));
        }
        menu_drive_coff = (Button) findViewById(R.id.menu_drive_coff);
        if (menu_drive_coff != null) {
            menu_drive_coff.setText(getString(R.string.drive_coff_set));
            menu_drive_lattice.setOnClickListener(m_ButtonClickLister);
            menu_drive_lattice.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_lattice.setTextColor(Color.parseColor("#ffffff"));
        }
        menu_drive_snake = (Button) findViewById(R.id.menu_drive_snake);
        if (menu_drive_snake != null) {
            menu_drive_snake.setText(getString(R.string.drive_snake_set));
            menu_drive_snake.setOnClickListener(m_ButtonClickLister);
            menu_drive_snake.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_snake.setTextColor(Color.parseColor("#ffffff"));
        }
        menu_drive_light = (Button) findViewById(R.id.menu_drive_light);
        if (menu_drive_light != null) {
            menu_drive_light.setText(getString(R.string.drive_light_set));
            menu_drive_light.setOnClickListener(m_ButtonClickLister);
            menu_drive_light.setBackgroundResource(R.drawable.btn_selector);
            menu_drive_light.setTextColor(Color.parseColor("#ffffff"));
        }
        String dataType = TcnShareUseData.getInstance().getTcnDataType();
        if ((dataType.equals(TcnConstant.DATA_TYPE[5]))) {
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[6]))) {
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[7]))) {
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[8]))) {
            menu_drive_coff.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[10]))) {
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[11]))) {  //GZ&CF
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[12]))) {  //ShJ&CF
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[13]))) {  //TH&GZ&CF
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_snake.setVisibility(View.GONE);
            menu_drive_light.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[16]))) {
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
        }
        else if ((dataType.equals(TcnConstant.DATA_TYPE[17]))) {  //Sk&TH
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[18]))) {  //Sk&GZ
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[19]))) {  //Sk&SHJ
            menu_drive_lattice.setVisibility(View.GONE);
            menu_drive_spring.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
        } else if ((dataType.equals(TcnConstant.DATA_TYPE[20]))) {
            menu_drive_lift.setVisibility(View.GONE);
            menu_drive_coff.setVisibility(View.GONE);
        }
        else {

        }

    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                MenuSettingsDriveActivity.this.finish();
            }
        }
    }

    private ButtonClickLister m_ButtonClickLister = new ButtonClickLister();
    private class ButtonClickLister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.menu_drive_spring == id) {
                Intent in = new Intent(MenuSettingsDriveActivity.this, MenuSettingsSpringActivity.class);
                startActivityForResult(in,300);
            }
            else {

            }
        }
    }
}
