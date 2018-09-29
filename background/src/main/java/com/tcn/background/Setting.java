package com.tcn.background;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.view.MySlipSwitch;

import java.util.Calendar;

import android_serialport_api.SerialPortFinder;


/**
 * Created by Administrator on 2016/7/21.
 */
public class Setting extends PreferenceActivity {
    private static final String TAG = "Setting";
    private SerialPortFinder mSerialPortFinder;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK_START = 2;
    private static final int SHOW_TIMEPICK_END = 3;
    private static final int TIME_DIALOG_START_ID = 4;
    private static final int TIME_DIALOG_END_ID = 5;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String mTimeStart;
    private String mTimeEnd;

    private EditText m_show_time_start = null;
    private EditText show_time_end = null;

    private Button pick_time_start = null;
    private Button pick_time_end = null;
    private ClickListener m_ClickListener = null;
    private MySlipSwitch m_TimeSwitch;
    private LinearLayout m_layout_time_start = null;
    private LinearLayout m_layout_time_end = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSerialPortFinder = TcnVendIF.getInstance().getSerialPortFinder();

        addPreferencesFromResource(R.xml.preferences);
        setContentView(R.layout.preference);

        // Devices
        final ListPreference devices = (ListPreference)findPreference("MAINDEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        devices.setEntries(entries);
        devices.setEntryValues(entryValues);
        devices.setSummary(devices.getValue());
        devices.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        // Baud rates
        final ListPreference baudrates = (ListPreference)findPreference("MAINBAUDRATE");
        baudrates.setSummary(baudrates.getValue());
        baudrates.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String)newValue);
                return true;
            }
        });

        m_layout_time_start = (LinearLayout) findViewById(R.id.layout_time_start);
        m_layout_time_end = (LinearLayout) findViewById(R.id.layout_time_end);

        m_TimeSwitch=(MySlipSwitch) findViewById(R.id.info_time_switch);
        m_TimeSwitch.setImageResource(R.mipmap.switch_bkg_switch,
                R.mipmap.switch_bkg_switch, R.mipmap.switch_btn_slip);
        m_TimeSwitch.setOnSwitchListener(new MySlipSwitch.OnSwitchListener() {

            @Override
            public void onSwitched(boolean isSwitchOn) {
                if (isSwitchOn) {
                    TcnShareUseData.getInstance().setPlayTimeLimitOpen(true);
                    m_layout_time_start.setVisibility(View.VISIBLE);
                    m_layout_time_end.setVisibility(View.VISIBLE);
                } else {
                    TcnShareUseData.getInstance().setPlayTimeLimitOpen(false);
                    m_layout_time_start.setVisibility(View.GONE);
                    m_layout_time_end.setVisibility(View.GONE);
                }
            }
        });
        m_TimeSwitch.setSwitchState(TcnShareUseData.getInstance().isPlayTimeLimitOpen());

        m_ClickListener =  new ClickListener();
        m_show_time_start = (EditText) findViewById(R.id.show_time_start);
        pick_time_start = (Button)findViewById(R.id.pick_time_start);
        pick_time_start.setOnClickListener(m_ClickListener);
        show_time_end = (EditText) findViewById(R.id.show_time_end);
        pick_time_end = (Button)findViewById(R.id.pick_time_end);
        pick_time_end.setOnClickListener(m_ClickListener);

        setDateTime();
        setTimeOfDay();
        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSerialPortFinder = null;
        mTimeStart = null;
        mTimeEnd = null;

        if(m_show_time_start != null) {
            m_show_time_start.setOnClickListener(null);
            m_show_time_start = null;
        }
        if(show_time_end != null) {
            show_time_end.setOnClickListener(null);
            show_time_end = null;
        }
        if(pick_time_start != null) {
            pick_time_start.setOnClickListener(null);
            pick_time_start = null;
        }
        if(pick_time_end != null) {
            pick_time_end.setOnClickListener(null);
            pick_time_end = null;
        }
        m_ClickListener = null;
        if(m_TimeSwitch != null) {
            m_TimeSwitch.setOnSwitchListener(null);
            m_TimeSwitch = null;
        }
        m_layout_time_start = null;
        m_layout_time_end = null;
        mTimeSetStartListener = null;
        mTimeSetEndListener = null;
        if (dateandtimeHandler != null) {
            dateandtimeHandler.removeCallbacksAndMessages(null);
            dateandtimeHandler = null;
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case TIME_DIALOG_START_ID:
                String mStrHourStart = TcnUtility.getHours(mTimeStart);
                String mStrMinuteStart = TcnUtility.getMinutes(mTimeStart);
                if (TcnVendIF.getInstance().isDigital(mStrHourStart) && TcnVendIF.getInstance().isDigital(mStrMinuteStart)) {
                    return new TimePickerDialog(this, mTimeSetStartListener, Integer.valueOf(mStrHourStart), Integer.valueOf(mStrMinuteStart), true);
                } else {
                    return new TimePickerDialog(this, mTimeSetStartListener, mHour, mMinute, true);
                }

            case TIME_DIALOG_END_ID:
                String mStrHourEnd = TcnUtility.getHours(mTimeEnd);
                String mStrMinuteEnd = TcnUtility.getMinutes(mTimeEnd);
                if (TcnVendIF.getInstance().isDigital(mStrHourEnd) && TcnVendIF.getInstance().isDigital(mStrMinuteEnd)) {
                    return new TimePickerDialog(this, mTimeSetEndListener, Integer.valueOf(mStrHourEnd), Integer.valueOf(mStrMinuteEnd), true);
                } else {
                    return new TimePickerDialog(this, mTimeSetEndListener, mHour, mMinute, true);
                }

        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case TIME_DIALOG_START_ID:
                String mStrHourStart = TcnUtility.getHours(mTimeStart);
                String mStrMinuteStart = TcnUtility.getMinutes(mTimeStart);
                if (TcnVendIF.getInstance().isDigital(mStrHourStart) && TcnVendIF.getInstance().isDigital(mStrMinuteStart)) {
                    ((TimePickerDialog) dialog).updateTime(Integer.valueOf(mStrHourStart), Integer.valueOf(mStrMinuteStart));
                } else {
                    ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                }

                break;
            case TIME_DIALOG_END_ID:
                String mStrHourEnd = TcnUtility.getHours(mTimeEnd);
                String mStrMinuteEnd = TcnUtility.getMinutes(mTimeEnd);
                if (TcnVendIF.getInstance().isDigital(mStrHourEnd) && TcnVendIF.getInstance().isDigital(mStrMinuteEnd)) {
                    ((TimePickerDialog) dialog).updateTime(Integer.valueOf(mStrHourEnd), Integer.valueOf(mStrMinuteEnd));
                } else {
                    ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                }
                break;
        }
    }

    private void initData(){
        if (TcnShareUseData.getInstance().isPlayTimeLimitOpen()) {
            m_layout_time_start.setVisibility(View.VISIBLE);
            m_layout_time_end.setVisibility(View.VISIBLE);
        } else {
            m_layout_time_start.setVisibility(View.GONE);
            m_layout_time_end.setVisibility(View.GONE);
        }
    }

    /**
     * 设置日期
     */
    private void setDateTime(){
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    /**
     * 更新日期显示
     */
    private void updateDateDisplay(){
//        showDate.setText(new StringBuilder().append(mYear).append("-")
//                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
//                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    /**
     * 日期控件的事件
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            updateDateDisplay();
        }
    };

    /**
     * 设置时间
     */
    private void setTimeOfDay(){
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        updateTimeDisplay();
    }

    /**
     * 更新时间显示
     */
    private void updateTimeDisplay(){

    }

    /**
     * 时间控件事件
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetStartListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            String strHour = String.valueOf(hourOfDay);
            if (1 == strHour.length()) {
                strHour = "0"+strHour;
            }
            String strMinute = String.valueOf(minute);
            if (1 == strMinute.length()) {
                strMinute = "0"+strMinute;
            }
            updateTimeDisplay();
        }
    };

    /**
     * 时间控件事件
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetEndListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            String strHour = String.valueOf(hourOfDay);
            if (1 == strHour.length()) {
                strHour = "0"+strHour;
            }
            String strMinute = String.valueOf(minute);
            if (1 == strMinute.length()) {
                strMinute = "0"+strMinute;
            }
            updateTimeDisplay();
        }
    };

    public void serial_back(View v){
        this.finish();
    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (R.id.pick_time_start == id) {
                dateandtimeHandler.sendEmptyMessage(SHOW_TIMEPICK_START);
            } else if (R.id.pick_time_end == id) {
                dateandtimeHandler.sendEmptyMessage(SHOW_TIMEPICK_END);
            } else {

            }
        }
    }
    /**
     * 处理日期和时间控件的Handler
     */
    Handler dateandtimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Setting.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case Setting.SHOW_TIMEPICK_START:
                    showDialog(TIME_DIALOG_START_ID);
                    break;
                case Setting.SHOW_TIMEPICK_END:
                    showDialog(TIME_DIALOG_END_ID);
                    break;
            }
        }

    };
}
