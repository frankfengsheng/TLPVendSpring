package com.tcn.background;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import com.tcn.background.controller.UIComBack;
import com.tcn.funcommon.TcnCommon;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.button.ButtonEditSelectD;
import com.tcn.uicommon.button.ButtonSwitch;
import com.tcn.uicommon.dialog.OutDialog;
import com.tcn.uicommon.titlebar.Titlebar;
import com.tcn.uicommon.view.TcnMainActivity;


/**
 * Created by Administrator on 2017/11/4.
 */
public class MenuSettingsSpringActivity extends TcnMainActivity {
    private static final String TAG = "MenuSettingsSpringActivity";
    private static final int CMD_SET_SLOTNO_SPRING     = 32;
    private static final int CMD_SET_SLOTNO_BELTS     = 33;
    private static final int CMD_SET_SLOTNO_ALL_SPRING     = 34;
    private static final int CMD_SET_SLOTNO_ALL_BELT     = 35;
    private static final int CMD_SET_SLOTNO_SINGLE     = 36;
    private static final int CMD_SET_SLOTNO_DOUBLE     = 37;
    private static final int CMD_SET_SLOTNO_ALL_SINGLE     = 38;
    private static final int CMD_SET_TEST_MODE    = 39;

    private int singleitem=0;

    private Titlebar m_Titlebar = null;

    private ButtonEditSelectD menu_spr_query_slot = null;
    private ButtonEditSelectD menu_spr_clear_fault = null;
    private ButtonEditSelectD menu_spr_query_fault = null;
    private ButtonEditSelectD menu_spr_reset = null;
    private ButtonEditSelectD menu_spr_set_heat_cool = null;
    private ButtonEditSelectD menu_spr_set_temp = null;
    private ButtonEditSelectD menu_spr_glass_heat_enable = null;
    private ButtonEditSelectD menu_spr_glass_heat_disable = null;
    private ButtonEditSelectD menu_spr_open_led = null;
    private ButtonEditSelectD menu_spr_close_led = null;
    private ButtonEditSelectD menu_spr_buzzer_open = null;
    private ButtonEditSelectD menu_spr_buzzer_close = null;
    private ButtonEditSelectD menu_spr_self_check = null;
    private ButtonEditSelectD menu_spr_set_slot_spring = null;
    private ButtonEditSelectD menu_spr_set_slot_belts = null;
    private ButtonEditSelectD menu_spr_set_slot_spring_all = null;
    private ButtonEditSelectD menu_spr_set_slot_belts_all = null;
    private ButtonEditSelectD menu_spr_set_single_slot = null;
    private ButtonEditSelectD menu_spr_set_double_slot = null;
    private ButtonEditSelectD menu_spr_set_single_slot_all = null;
    private ButtonEditSelectD menu_spr_test_mode = null;

    private ButtonSwitch menu_spr_light_check = null;

    private OutDialog m_OutDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_settings_layout_spr);
        TcnVendIF.getInstance().LoggerDebug(TAG, "MenuSettingsSpringActivity onCreate()");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcnVendIF.getInstance().registerListener(m_vendListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TcnVendIF.getInstance().unregisterListener(m_vendListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_Titlebar != null) {
            m_Titlebar.removeButtonListener();
            m_Titlebar = null;
        }

        if (menu_spr_light_check != null) {
            menu_spr_light_check.removeButtonListener();
            menu_spr_light_check = null;
        }

        if (menu_spr_query_slot != null) {
            menu_spr_query_slot.removeButtonListener();
            menu_spr_query_slot = null;
        }

        if (menu_spr_clear_fault != null) {
            menu_spr_clear_fault.removeButtonListener();
            menu_spr_clear_fault = null;
        }

        if (menu_spr_query_fault != null) {
            menu_spr_query_fault.removeButtonListener();
            menu_spr_query_fault = null;
        }

        if (menu_spr_reset != null) {
            menu_spr_reset.removeButtonListener();
            menu_spr_reset = null;
        }

        if (menu_spr_set_heat_cool != null) {
            menu_spr_set_heat_cool.removeButtonListener();
            menu_spr_set_heat_cool = null;
        }

        if (menu_spr_set_temp != null) {
            menu_spr_set_temp.removeButtonListener();
            menu_spr_set_temp = null;
        }

        if (menu_spr_glass_heat_enable != null) {
            menu_spr_glass_heat_enable.removeButtonListener();
            menu_spr_glass_heat_enable = null;
        }

        if (menu_spr_glass_heat_disable != null) {
            menu_spr_glass_heat_disable.removeButtonListener();
            menu_spr_glass_heat_disable = null;
        }

        if (menu_spr_open_led != null) {
            menu_spr_open_led.removeButtonListener();
            menu_spr_open_led = null;
        }

        if (menu_spr_close_led != null) {
            menu_spr_close_led.removeButtonListener();
            menu_spr_close_led = null;
        }

        if (menu_spr_buzzer_open != null) {
            menu_spr_buzzer_open.removeButtonListener();
            menu_spr_buzzer_open = null;
        }

        if (menu_spr_buzzer_close != null) {
            menu_spr_buzzer_close.removeButtonListener();
            menu_spr_buzzer_close = null;
        }

        if (menu_spr_self_check != null) {
            menu_spr_self_check.removeButtonListener();
            menu_spr_self_check = null;
        }

        if (menu_spr_set_slot_spring != null) {
            menu_spr_set_slot_spring.removeButtonListener();
            menu_spr_set_slot_spring = null;
        }


        if (menu_spr_set_slot_belts != null) {
            menu_spr_set_slot_belts.removeButtonListener();
            menu_spr_set_slot_belts = null;
        }


        if (menu_spr_set_slot_spring_all != null) {
            menu_spr_set_slot_spring_all.removeButtonListener();
            menu_spr_set_slot_spring_all = null;
        }

        if (menu_spr_set_slot_belts_all != null) {
            menu_spr_set_slot_belts_all.removeButtonListener();
            menu_spr_set_slot_belts_all = null;
        }

        if (menu_spr_set_single_slot != null) {
            menu_spr_set_single_slot.removeButtonListener();
            menu_spr_set_single_slot = null;
        }

        if (menu_spr_set_double_slot != null) {
            menu_spr_set_double_slot.removeButtonListener();
            menu_spr_set_double_slot = null;
        }

        if (menu_spr_set_single_slot_all != null) {
            menu_spr_set_single_slot_all.removeButtonListener();
            menu_spr_set_single_slot_all = null;
        }

        if (menu_spr_test_mode != null) {
            menu_spr_test_mode.removeButtonListener();
            menu_spr_test_mode = null;
        }
        m_OutDialog = null;
        m_TitleBarListener = null;
        m_vendListener = null;
        m_SwitchButtonListener = null;
        m_ButtonEditClickListener = null;
    }

    private void initView() {

        m_Titlebar = (Titlebar) findViewById(R.id.menu_setttings_titlebar);
        if (m_Titlebar != null) {
            m_Titlebar.setButtonType(Titlebar.BUTTON_TYPE_BACK);
            m_Titlebar.setButtonName(R.string.menu_settings);
            m_Titlebar.setTitleBarListener(m_TitleBarListener);
        }

        menu_spr_light_check = (ButtonSwitch) findViewById(R.id.menu_spr_light_check);
        if (menu_spr_light_check != null) {
            menu_spr_light_check.setButtonName(R.string.menu_drop_sensor_whole);
            menu_spr_light_check.setButtonListener(m_SwitchButtonListener);
            menu_spr_light_check.setTextSize(TcnVendIF.getInstance().getFitScreenSize(22));
            menu_spr_light_check.setSwitchState(TcnShareUseData.getInstance().isDropSensorCheck());
        }

        menu_spr_query_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_query_slot);
        if (menu_spr_query_slot != null) {
            menu_spr_query_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
            menu_spr_query_slot.setButtonName(getString(R.string.drive_query_slot));
            menu_spr_query_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_query_slot.setButtonQueryText(getString(R.string.drive_query));
            menu_spr_query_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            menu_spr_query_slot.setButtonQueryTextColor("#ffffff");
            menu_spr_query_slot.setButtonDisplayTextColor("#4e5d72");
            menu_spr_query_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
            menu_spr_query_slot.setButtonListener(m_ButtonEditClickListener);

        }

        menu_spr_query_fault = (ButtonEditSelectD) findViewById(R.id.menu_spr_query_fault);
        if (menu_spr_query_fault != null) {
            menu_spr_query_fault.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            menu_spr_query_fault.setButtonName(getString(R.string.drive_query_fault));
            menu_spr_query_fault.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_query_fault.setButtonQueryText(getString(R.string.drive_query));
            menu_spr_query_fault.setButtonQueryTextColor("#ffffff");
            menu_spr_query_fault.setButtonDisplayTextColor("#4e5d72");
            menu_spr_query_fault.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_clear_fault = (ButtonEditSelectD) findViewById(R.id.menu_spr_clear_fault);
        if (menu_spr_clear_fault != null) {
            menu_spr_clear_fault.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            menu_spr_clear_fault.setButtonName(R.string.drive_clean_fault);
            menu_spr_clear_fault.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_clear_fault.setButtonQueryText(getString(R.string.drive_clean));
            menu_spr_clear_fault.setButtonQueryTextColor("#ffffff");
            menu_spr_clear_fault.setButtonDisplayTextColor("#4e5d72");
            menu_spr_clear_fault.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_self_check = (ButtonEditSelectD) findViewById(R.id.menu_spr_self_check);
        if (menu_spr_self_check != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_self_check.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_self_check.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_self_check.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_self_check.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }

            menu_spr_self_check.setButtonQueryText(getString(R.string.spring_self_check));
            menu_spr_self_check.setButtonQueryTextColor("#ffffff");
            menu_spr_self_check.setButtonDisplayTextColor("#4e5d72");
            menu_spr_self_check.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_reset = (ButtonEditSelectD) findViewById(R.id.menu_spr_reset);
        if (menu_spr_reset != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_reset.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_reset.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_reset.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_reset.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_reset.setButtonQueryText(getString(R.string.spring_reset));
            menu_spr_reset.setButtonQueryTextColor("#ffffff");
            menu_spr_reset.setButtonDisplayTextColor("#4e5d72");
            menu_spr_reset.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_heat_cool = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_heat_cool);
        if (menu_spr_set_heat_cool != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_heat_cool.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECTTWO_QUERY);
                menu_spr_set_heat_cool.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_heat_cool.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_heat_cool.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_SECOND_QUERY);
            }
            menu_spr_set_heat_cool.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_heat_cool.setButtonName(R.string.spring_set_heat_cool);
            menu_spr_set_heat_cool.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_heat_cool.setButtonQueryTextColor("#ffffff");
            menu_spr_set_heat_cool.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_heat_cool.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_temp = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_temp);
        if (menu_spr_set_temp != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_temp.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECTTWO_QUERY);
                menu_spr_set_temp.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_temp.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_temp.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_SECOND_QUERY);
            }
            menu_spr_set_temp.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_temp.setButtonName(R.string.spring_set_temp);
            menu_spr_set_temp.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_temp.setButtonQueryTextColor("#ffffff");
            menu_spr_set_temp.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_temp.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_glass_heat_enable = (ButtonEditSelectD) findViewById(R.id.menu_spr_glass_heat_enable);
        if (menu_spr_glass_heat_enable != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_glass_heat_enable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_glass_heat_enable.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_glass_heat_enable.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_glass_heat_enable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_glass_heat_enable.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_glass_heat_enable.setButtonName(R.string.spring_glass_heat_open);
            menu_spr_glass_heat_enable.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_glass_heat_enable.setButtonQueryTextColor("#ffffff");
            menu_spr_glass_heat_enable.setButtonDisplayTextColor("#4e5d72");
            menu_spr_glass_heat_enable.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_glass_heat_disable = (ButtonEditSelectD) findViewById(R.id.menu_spr_glass_heat_disable);
        if (menu_spr_glass_heat_disable != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_glass_heat_disable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_glass_heat_disable.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_glass_heat_disable.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_glass_heat_disable.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_glass_heat_disable.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_glass_heat_disable.setButtonName(R.string.spring_glass_heat_close);
            menu_spr_glass_heat_disable.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_glass_heat_disable.setButtonQueryTextColor("#ffffff");
            menu_spr_glass_heat_disable.setButtonDisplayTextColor("#4e5d72");
            menu_spr_glass_heat_disable.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_open_led = (ButtonEditSelectD) findViewById(R.id.menu_spr_open_led);
        if (menu_spr_open_led != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_open_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_open_led.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_open_led.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_open_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_open_led.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            menu_spr_open_led.setButtonName(R.string.spring_led_open);
            menu_spr_open_led.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_open_led.setButtonQueryTextColor("#ffffff");
            menu_spr_open_led.setButtonDisplayTextColor("#4e5d72");
            menu_spr_open_led.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_close_led = (ButtonEditSelectD) findViewById(R.id.menu_spr_close_led);
        if (menu_spr_close_led != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_close_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_close_led.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_close_led.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_close_led.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_close_led.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_close_led.setButtonName(R.string.spring_led_close);
            menu_spr_close_led.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_close_led.setButtonQueryTextColor("#ffffff");
            menu_spr_close_led.setButtonDisplayTextColor("#4e5d72");
            menu_spr_close_led.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_buzzer_open = (ButtonEditSelectD) findViewById(R.id.menu_spr_buzzer_open);
        if (menu_spr_buzzer_open != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_buzzer_open.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_buzzer_open.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_buzzer_open.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_buzzer_open.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_buzzer_open.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_buzzer_open.setButtonName(R.string.spring_buzzer_open);
            menu_spr_buzzer_open.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_buzzer_open.setButtonQueryTextColor("#ffffff");
            menu_spr_buzzer_open.setButtonDisplayTextColor("#4e5d72");
            menu_spr_buzzer_open.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_buzzer_close = (ButtonEditSelectD) findViewById(R.id.menu_spr_buzzer_close);
        if (menu_spr_buzzer_close != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_buzzer_close.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_buzzer_close.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_buzzer_close.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_buzzer_close.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }
            menu_spr_buzzer_close.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_buzzer_close.setButtonName(R.string.spring_buzzer_close);
            menu_spr_buzzer_close.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_buzzer_close.setButtonQueryTextColor("#ffffff");
            menu_spr_buzzer_close.setButtonDisplayTextColor("#4e5d72");
            menu_spr_buzzer_close.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_slot_spring = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_spring);
        if (menu_spr_set_slot_spring != null) {
            menu_spr_set_slot_spring.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
            menu_spr_set_slot_spring.setButtonName(getString(R.string.spring_set_slot_spring));
            menu_spr_set_slot_spring.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_slot_spring.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_slot_spring.setButtonQueryTextColor("#ffffff");
            menu_spr_set_slot_spring.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_slot_spring.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_slot_spring.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
            menu_spr_set_slot_spring.setButtonListener(m_ButtonEditClickListener);
        }


        menu_spr_set_slot_belts = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_belts);
        if (menu_spr_set_slot_belts != null) {
            menu_spr_set_slot_belts.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
            menu_spr_set_slot_belts.setButtonName(getString(R.string.spring_set_slot_belts));
            menu_spr_set_slot_belts.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_slot_belts.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_slot_belts.setButtonQueryTextColor("#ffffff");
            menu_spr_set_slot_belts.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_slot_belts.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            menu_spr_set_slot_belts.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
            menu_spr_set_slot_belts.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_slot_spring_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_spring_all);
        if (menu_spr_set_slot_spring_all != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_slot_spring_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_set_slot_spring_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_slot_spring_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_slot_spring_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }

            menu_spr_set_slot_spring_all.setButtonName(getString(R.string.spring_set_slot_spring_all));
            menu_spr_set_slot_spring_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_slot_spring_all.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_slot_spring_all.setButtonQueryTextColor("#ffffff");
            menu_spr_set_slot_spring_all.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_slot_spring_all.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_slot_belts_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_slot_belts_all);
        if (menu_spr_set_slot_belts_all != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_slot_belts_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_set_slot_belts_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_slot_belts_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_slot_belts_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }

            menu_spr_set_slot_belts_all.setButtonName(getString(R.string.spring_set_slot_belts_all));
            menu_spr_set_slot_belts_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_slot_belts_all.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_slot_belts_all.setButtonQueryTextColor("#ffffff");
            menu_spr_set_slot_belts_all.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_slot_belts_all.setButtonListener(m_ButtonEditClickListener);
        }


        menu_spr_set_single_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_single_slot);
        if (menu_spr_set_single_slot != null) {
            menu_spr_set_single_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
            menu_spr_set_single_slot.setButtonName(getString(R.string.spring_set_single_slot));
            menu_spr_set_single_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_single_slot.setButtonQueryText(getString(R.string.spring_set_single));
            menu_spr_set_single_slot.setButtonQueryTextColor("#ffffff");
            menu_spr_set_single_slot.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_single_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            menu_spr_set_single_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
            menu_spr_set_single_slot.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_double_slot = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_double_slot);
        if (menu_spr_set_double_slot != null) {
            menu_spr_set_double_slot.setButtonType(ButtonEditSelectD.BUTTON_TYPE_EDIT_QUERY);
            menu_spr_set_double_slot.setButtonName(getString(R.string.spring_set_double_slot));
            menu_spr_set_double_slot.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_double_slot.setButtonQueryText(getString(R.string.spring_set_double));
            menu_spr_set_double_slot.setButtonQueryTextColor("#ffffff");
            menu_spr_set_double_slot.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_double_slot.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            menu_spr_set_double_slot.setInputTypeInput(InputType.TYPE_CLASS_NUMBER);
            menu_spr_set_double_slot.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_set_single_slot_all = (ButtonEditSelectD) findViewById(R.id.menu_spr_set_single_slot_all);
        if (menu_spr_set_single_slot_all != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_set_single_slot_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_set_single_slot_all.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_set_single_slot_all.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_set_single_slot_all.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }

            menu_spr_set_single_slot_all.setButtonName(getString(R.string.spring_set_single_slot_all));
            menu_spr_set_single_slot_all.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_set_single_slot_all.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_set_single_slot_all.setButtonQueryTextColor("#ffffff");
            menu_spr_set_single_slot_all.setButtonDisplayTextColor("#4e5d72");
            menu_spr_set_single_slot_all.setButtonListener(m_ButtonEditClickListener);
        }

        menu_spr_test_mode = (ButtonEditSelectD) findViewById(R.id.menu_spr_test_mode);
        if (menu_spr_test_mode != null) {
            if (UIComBack.getInstance().isMutiGrpSpring()) {
                menu_spr_test_mode.setButtonType(ButtonEditSelectD.BUTTON_TYPE_SELECT_QUERY);
                menu_spr_test_mode.setButtonQueryTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
                menu_spr_test_mode.setButtonDisplayTextSize(TcnVendIF.getInstance().getFitScreenSize(16));
            } else {
                menu_spr_test_mode.setButtonType(ButtonEditSelectD.BUTTON_TYPE_QUERY);
            }

            menu_spr_test_mode.setButtonName(getString(R.string.spring_test_mode));
            menu_spr_test_mode.setButtonNameTextSize(TcnVendIF.getInstance().getFitScreenSize(20));
            menu_spr_test_mode.setButtonQueryText(getString(R.string.drive_set));
            menu_spr_test_mode.setButtonQueryTextColor("#ffffff");
            menu_spr_test_mode.setButtonDisplayTextColor("#4e5d72");
            menu_spr_test_mode.setButtonListener(m_ButtonEditClickListener);
        }
    }

    private void showSelectDialog(final int type, String title, final EditText v, String selectData, final String[] str) {
        if (null == str) {
            return;
        }
        int checkedItem = -1;
        if ((selectData != null) && (selectData.length() > 0)) {
            for (int i = 0; i < str.length; i++) {
                if (str[i].equals(selectData)) {
                    checkedItem = i;
                    break;
                }
            }
        }

        singleitem=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setSingleChoiceItems(str, checkedItem, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                singleitem=which;
            }
        });
        builder.setPositiveButton(getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                v.setText(str[singleitem]);
            }
        });
        builder.setNegativeButton(getString(R.string.backgroound_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    private MenuSetTitleBarListener m_TitleBarListener = new MenuSetTitleBarListener();
    private class MenuSetTitleBarListener implements Titlebar.TitleBarListener {

        @Override
        public void onClick(View v, int buttonId) {
            if (Titlebar.BUTTON_ID_BACK == buttonId) {
                MenuSettingsSpringActivity.this.finish();
            }
        }
    }

    private void showSetConfirm(final int cmdType,final String grp,final String data1,final String data2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
            builder.setTitle(getString(R.string.spring_double_ask));
        } else {
            builder.setTitle(getString(R.string.drive_modify_ask));
        }
        builder.setPositiveButton(getString(R.string.backgroound_ensure), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int showTimeOut = 5;
                if (CMD_SET_SLOTNO_SPRING == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(data1)) {
                        TcnVendIF.getInstance().reqSetSpringSlot(Integer.valueOf(data1));
                    }

                } else if (CMD_SET_SLOTNO_BELTS == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(data1)) {
                        TcnVendIF.getInstance().reqSetBeltsSlot(Integer.valueOf(data1));
                    }

                } else if (CMD_SET_SLOTNO_ALL_SPRING == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(grp)) {
                        TcnVendIF.getInstance().reqSpringAllSlot(Integer.valueOf(grp));
                    } else {
                        TcnVendIF.getInstance().reqSpringAllSlot(-1);
                    }

                } else if (CMD_SET_SLOTNO_ALL_BELT == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(grp)) {
                        TcnVendIF.getInstance().reqBeltsAllSlot(Integer.valueOf(grp));
                    } else {
                        TcnVendIF.getInstance().reqBeltsAllSlot(-1);
                    }

                } else if (CMD_SET_SLOTNO_SINGLE == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(data1)) {
                        TcnVendIF.getInstance().reqSingleSlot(Integer.valueOf(data1));
                    }
                } else if (CMD_SET_SLOTNO_DOUBLE == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(data1)) {
                        TcnVendIF.getInstance().reqDoubleSlot(Integer.valueOf(data1));
                    }
                } else if (CMD_SET_SLOTNO_ALL_SINGLE == cmdType) {
                    if (TcnVendIF.getInstance().isDigital(grp)) {
                        TcnVendIF.getInstance().reqSingleAllSlot(Integer.valueOf(grp));
                    } else {
                        TcnVendIF.getInstance().reqSingleAllSlot(-1);
                    }

                } else if (CMD_SET_TEST_MODE == cmdType) {
                    showTimeOut = 60*60;
                    if (TcnVendIF.getInstance().isDigital(grp)) {
                        TcnVendIF.getInstance().reqTestMode(Integer.valueOf(grp));
                    } else {
                        TcnVendIF.getInstance().reqTestMode(-1);
                    }

                }
                else {

                }

                if (m_OutDialog == null) {
                    m_OutDialog = new OutDialog(MenuSettingsSpringActivity.this, "", getString(R.string.drive_setting));
                    m_OutDialog.setShowTime(10000);
                }

                if (m_OutDialog != null) {
                    m_OutDialog.setShowTime(showTimeOut);
                    m_OutDialog.show();
                }

            }
        });
        builder.setNegativeButton(getString(R.string.backgroound_cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    private SwitchButtonListener m_SwitchButtonListener = new SwitchButtonListener();
    private class SwitchButtonListener implements ButtonSwitch.ButtonListener {

        @Override
        public void onSwitched(View v, boolean isSwitchOn) {
            int iId = v.getId();
            if (R.id.menu_spr_light_check == iId) {
                TcnShareUseData.getInstance().setDropSensorCheck(isSwitchOn);
            } else {

            }
        }
    }

    private ButtonEditClickListener m_ButtonEditClickListener= new ButtonEditClickListener();
    private class ButtonEditClickListener implements ButtonEditSelectD.ButtonListener {
        @Override
        public void onClick(View v, int buttonId) {
            if (null == v) {
                return;
            }
            int id = v.getId();
            if (R.id.menu_spr_query_slot == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    String strParam = menu_spr_query_slot.getButtonEditInputText();
                    if ((null == strParam) || (strParam.length() < 1)) {
                        TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_input_slotno));
                    } else {
                        TcnVendIF.getInstance().reqQuerySlotStatus(Integer.valueOf(strParam));
                    }
                }
            } else if (R.id.menu_spr_query_fault == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    TcnVendIF.getInstance().reqQueryFaults();
                }
            } else if (R.id.menu_spr_clear_fault == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    TcnVendIF.getInstance().reqClearFaults();
                }
            } else if (R.id.menu_spr_self_check == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_self_check.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_self_check.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSelfCheck(UIComBack.getInstance().getGroupSpringId(strParam));
                        }
                    } else {
                        TcnVendIF.getInstance().reqSelfCheck(-1);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_self_check.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_reset == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_reset.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_reset.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqReset(UIComBack.getInstance().getGroupSpringId(strParam));
                            if (m_OutDialog == null) {
                                m_OutDialog = new OutDialog(MenuSettingsSpringActivity.this, "", getString(R.string.drive_setting));
                                m_OutDialog.setShowTime(10000);
                            }
                            if (m_OutDialog != null) {
                                m_OutDialog.setShowTime(60*10);
                                m_OutDialog.show();
                            }
                        }
                    } else {
                        TcnVendIF.getInstance().reqReset(-1);
                        if (m_OutDialog == null) {
                            m_OutDialog = new OutDialog(MenuSettingsSpringActivity.this, "", getString(R.string.drive_setting));
                            m_OutDialog.setShowTime(10000);
                        }
                        if (m_OutDialog != null) {
                            m_OutDialog.setShowTime(60*10);
                            m_OutDialog.show();
                        }
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_reset.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_set_heat_cool == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_heat_cool.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_heat_cool.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            String strParamSecond = menu_spr_set_heat_cool.getButtonEditTextSecond();
                            if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                                TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.lift_tips_select_control_action));
                            } else {
                                TcnVendIF.getInstance().reqTemperControlSpring(UIComBack.getInstance().getGroupSpringId(strParam),strParamSecond);
                            }

                        }
                    } else {
                        String strParamSecond = menu_spr_set_heat_cool.getButtonEditTextSecond();
                        if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.lift_tips_select_control_action));
                        } else {
                            TcnVendIF.getInstance().reqTemperControlSpring(-1,strParamSecond);
                        }
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_set_heat_cool.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT_SECOND == buttonId) {
                    showSelectDialog(-1,getString(R.string.lift_tips_select_control_action),menu_spr_set_heat_cool.getButtonEditSecond(), "", TcnCommon.HEAT_COOL_OFF_SWITCH_SELECT);
                }
                else {

                }

            } else if (R.id.menu_spr_set_temp == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_temp.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_temp.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            String strParamSecond = menu_spr_set_temp.getButtonEditTextSecond();
                            if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                                TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.lift_tips_select_control_action));
                            } else {
                                TcnVendIF.getInstance().reqSetTemp(UIComBack.getInstance().getGroupSpringId(strParam),Integer.valueOf(strParamSecond));
                            }

                        }
                    } else {
                        String strParamSecond = menu_spr_set_temp.getButtonEditTextSecond();
                        if ((null == strParamSecond) || (strParamSecond.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.lift_tips_select_control_action));
                        } else {
                            TcnVendIF.getInstance().reqSetTemp(-1,Integer.valueOf(strParamSecond));
                        }
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_set_temp.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT_SECOND == buttonId) {
                    showSelectDialog(-1,getString(R.string.lift_tips_select_control_action),menu_spr_set_temp.getButtonEditSecond(), "", TcnCommon.TEMPERATURE_SELECT);
                }
                else {

                }
            } else if (R.id.menu_spr_glass_heat_enable == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_glass_heat_enable.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_glass_heat_enable.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetGlassHeatEnable(UIComBack.getInstance().getGroupSpringId(strParam),true);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetGlassHeatEnable(-1,true);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_glass_heat_enable.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_glass_heat_disable == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_glass_heat_disable.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_glass_heat_disable.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetGlassHeatEnable(UIComBack.getInstance().getGroupSpringId(strParam),false);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetGlassHeatEnable(-1,false);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_glass_heat_disable.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_open_led == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_open_led.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_open_led.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetLedOpen(UIComBack.getInstance().getGroupSpringId(strParam),true);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetLedOpen(-1,true);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_open_led.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_close_led == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_close_led.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_close_led.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetLedOpen(UIComBack.getInstance().getGroupSpringId(strParam),false);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetLedOpen(-1,false);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_close_led.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_buzzer_open == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_buzzer_open.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_buzzer_open.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetBuzzerOpen(UIComBack.getInstance().getGroupSpringId(strParam),true);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetBuzzerOpen(-1,true);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_buzzer_open.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_buzzer_close == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_buzzer_close.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_buzzer_close.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            TcnVendIF.getInstance().reqSetBuzzerOpen(UIComBack.getInstance().getGroupSpringId(strParam),false);
                        }
                    } else {
                        TcnVendIF.getInstance().reqSetBuzzerOpen(-1,false);
                    }
                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_buzzer_close.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            }
            else if (R.id.menu_spr_set_slot_spring == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_slot_spring.setButtonDisplayText("");
                    String strParam = menu_spr_set_slot_spring.getButtonEditInputText();
                    if ((null == strParam) || (strParam.length() < 1)) {
                        TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_input_slotno));
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_SPRING,"",strParam,"");
                    }
                }
            } else if (R.id.menu_spr_set_slot_belts == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_slot_belts.setButtonDisplayText("");
                    String strParam = menu_spr_set_slot_belts.getButtonEditInputText();
                    if ((null == strParam) || (strParam.length() < 1)) {
                        TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_input_slotno));
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_BELTS,"",strParam,"");
                    }
                }
            } else if (R.id.menu_spr_set_slot_spring_all == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_slot_spring_all.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_slot_spring_all.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            showSetConfirm(CMD_SET_SLOTNO_ALL_SPRING,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
                        }
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_ALL_SPRING,"","","");
                    }

                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_set_slot_spring_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_set_slot_belts_all == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_slot_belts_all.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_slot_belts_all.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            showSetConfirm(CMD_SET_SLOTNO_ALL_BELT,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
                        }
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_ALL_BELT,"","","");
                    }

                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_set_slot_belts_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_set_single_slot == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_single_slot.setButtonDisplayText("");
                    String strParam = menu_spr_set_single_slot.getButtonEditInputText();
                    if ((null == strParam) || (strParam.length() < 1)) {
                        TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_input_slotno));
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_SINGLE,"",strParam,"");
                    }

                }
            } else if (R.id.menu_spr_set_double_slot == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_double_slot.setButtonDisplayText("");
                    String strParam = menu_spr_set_double_slot.getButtonEditInputText();
                    if ((null == strParam) || (strParam.length() < 1)) {
                        TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_input_slotno));
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_DOUBLE,"",strParam,"");
                    }
                }
            } else if (R.id.menu_spr_set_single_slot_all == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_set_single_slot_all.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_set_single_slot_all.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            showSetConfirm(CMD_SET_SLOTNO_ALL_SINGLE,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
                        }
                    } else {
                        showSetConfirm(CMD_SET_SLOTNO_ALL_SINGLE,"","","");
                    }

                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_set_single_slot_all.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            } else if (R.id.menu_spr_test_mode == id) {
                if (ButtonEditSelectD.BUTTON_ID_QUERY == buttonId) {
                    menu_spr_test_mode.setButtonDisplayText("");
                    if (UIComBack.getInstance().isMutiGrpSpring()) {
                        String strParam = menu_spr_test_mode.getButtonEditText();
                        if ((null == strParam) || (strParam.length() < 1)) {
                            TcnUtility.getToast(MenuSettingsSpringActivity.this, getString(R.string.drive_tips_select_cabinetno));
                        } else {
                            showSetConfirm(CMD_SET_TEST_MODE,String.valueOf(UIComBack.getInstance().getGroupSpringId(strParam)),"","");
                        }
                    } else {
                        showSetConfirm(CMD_SET_TEST_MODE,"","","");
                    }

                } else if (ButtonEditSelectD.BUTTON_ID_SELECT == buttonId) {
                    showSelectDialog(-1,getString(R.string.drive_tips_select_cabinetno),menu_spr_test_mode.getButtonEdit(), "",UIComBack.getInstance().getGroupListSpringShow());
                } else {

                }
            }
            else {

            }
        }
    }

    private VendListener m_vendListener = new VendListener();
    private class VendListener implements TcnVendIF.VendEventListener {
        @Override
        public void VendEvent(VendEventInfo cEventInfo) {
            if (null == cEventInfo) {
                TcnVendIF.getInstance().LoggerError(TAG, "VendListener cEventInfo is null");
                return;
            }
            switch (cEventInfo.m_iEventID) {
                case TcnVendEventID.CMD_QUERY_SLOT_FAULTS:
                    if (cEventInfo.m_lParam1 > 0) {
                        menu_spr_query_fault.setButtonDisplayText(getString(R.string.drive_tips_have_fault));
                    } else {
                        menu_spr_query_fault.setButtonDisplayText(getString(R.string.drive_tips_no_fault));
                    }
                    break;
                case TcnVendEventID.CMD_CLEAR_SLOT_FAULTS:
                    menu_spr_clear_fault.setButtonDisplayText(getString(R.string.drive_tips_clean_fault_success));
                    break;
                case TcnVendEventID.CMD_QUERY_SLOT_STATUS:
                    menu_spr_query_slot.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SELF_CHECK:
                    menu_spr_self_check.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_RESET:
                    menu_spr_reset.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_SPRING:
                    menu_spr_set_slot_spring.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_BELTS:
                    menu_spr_set_slot_belts.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_ALL_SPRING:
                    menu_spr_set_slot_spring_all.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_ALL_BELT:
                    menu_spr_set_slot_belts_all.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_SINGLE:
                    menu_spr_set_single_slot.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_DOUBLE:
                    menu_spr_set_double_slot.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.SET_SLOTNO_ALL_SINGLE:
                    menu_spr_set_single_slot_all.setButtonDisplayText(cEventInfo.m_lParam4);
                    if (m_OutDialog != null) {
                        m_OutDialog.dismiss();
                    }
                    break;
                case TcnVendEventID.CMD_SET_COOL:
                    menu_spr_set_heat_cool.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_HEAT:
                    menu_spr_set_heat_cool.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_TEMP:
                    menu_spr_set_temp.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_GLASS_HEAT_OPEN:
                    menu_spr_glass_heat_enable.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_GLASS_HEAT_CLOSE:
                    menu_spr_glass_heat_disable.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_LIGHT_OPEN:
                    menu_spr_open_led.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_LIGHT_CLOSE:
                    menu_spr_close_led.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_BUZZER_OPEN:
                    menu_spr_buzzer_open.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                case TcnVendEventID.CMD_SET_BUZZER_CLOSE:
                    menu_spr_buzzer_close.setButtonDisplayText(cEventInfo.m_lParam4);
                    break;
                default:
                    break;
            }
        }
    }
}
