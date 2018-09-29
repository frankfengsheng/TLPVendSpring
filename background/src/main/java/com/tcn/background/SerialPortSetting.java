package com.tcn.background;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.View;

import com.tcn.funcommon.vend.controller.TcnVendIF;

import android_serialport_api.SerialPortFinder;


/**
 * 设置更换串口界面
 * @author Administrator
 *
 */
public class SerialPortSetting extends PreferenceActivity {

	private SerialPortFinder mSerialPortFinder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSerialPortFinder = TcnVendIF.getInstance().getSerialPortFinder();

		addPreferencesFromResource(R.xml.serial_port_preferences);
		setContentView(R.layout.preference_set);

		// Devices
		final ListPreference devices = (ListPreference)findPreference("MAINDEVICE");
		String[] entries = mSerialPortFinder.getAllDevices();
		String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("MAINBAUDRATE");
		baudrates.setSummary(baudrates.getValue());
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Server Devices
		final ListPreference serverPreference = (ListPreference)findPreference("SERVERDEVICE");
		String[] entriesServer = mSerialPortFinder.getAllDevices();
		String[] entryValuesServer = mSerialPortFinder.getAllDevicesPath();
		serverPreference.setEntries(entriesServer);
		serverPreference.setEntryValues(entryValuesServer);
		serverPreference.setSummary(serverPreference.getValue());
		serverPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Server Devices
		final ListPreference thirdPreference = (ListPreference)findPreference("DEVICETHIRD");
		String[] entriesThird = mSerialPortFinder.getAllDevices();
		String[] entryValuesThird = mSerialPortFinder.getAllDevicesPath();
		thirdPreference.setEntries(entriesThird);
		thirdPreference.setEntryValues(entryValuesThird);
		thirdPreference.setSummary(thirdPreference.getValue());
		thirdPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		final ListPreference keyPreference = (ListPreference)findPreference("DEVICEKey");
		String[] entriesKey = mSerialPortFinder.getAllDevices();
		String[] entryValuesKey = mSerialPortFinder.getAllDevicesPath();
		keyPreference.setEntries(entriesKey);
		keyPreference.setEntryValues(entryValuesKey);
		keyPreference.setSummary(keyPreference.getValue());
		keyPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference posPreference = (ListPreference)findPreference("POSDEVICE");
		String[] entriesPos = mSerialPortFinder.getAllDevices();
		String[] entryValuesPos = mSerialPortFinder.getAllDevicesPath();
		posPreference.setEntries(entriesPos);
		posPreference.setEntryValues(entryValuesPos);
		posPreference.setSummary(posPreference.getValue());
		posPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference ICCardPreference = (ListPreference)findPreference("ICCARDDEVICE");
		String[] entriesICCard = mSerialPortFinder.getAllDevices();
		String[] entryValuesICCard = mSerialPortFinder.getAllDevicesPath();
		ICCardPreference.setEntries(entriesICCard);
		ICCardPreference.setEntryValues(entryValuesICCard);
		ICCardPreference.setSummary(ICCardPreference.getValue());
		ICCardPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// IC card Baud rates
		final ListPreference ICCardBaudrates = (ListPreference)findPreference("ICCARDBAUDRATE");
		ICCardBaudrates.setSummary(ICCardBaudrates.getValue());
		ICCardBaudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference pasivScanPayPreference = (ListPreference)findPreference("PasivScanPayComPath");
		String[] entriesPsvScanPay = mSerialPortFinder.getAllDevices();
		String[] entryValuesPsvScanPay = mSerialPortFinder.getAllDevicesPath();
		pasivScanPayPreference.setEntries(entriesPsvScanPay);
		pasivScanPayPreference.setEntryValues(entryValuesPsvScanPay);
		pasivScanPayPreference.setSummary(pasivScanPayPreference.getValue());
		pasivScanPayPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// IC card Baud rates
		final ListPreference pasivScanPayBaudrates = (ListPreference)findPreference("PasivScanPayBaudrate");
		pasivScanPayBaudrates.setSummary(pasivScanPayBaudrates.getValue());
		pasivScanPayBaudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference dspPreference = (ListPreference)findPreference("DEVICEDGTDSP");
		String[] entriesDsp = mSerialPortFinder.getAllDevices();
		String[] entryValuesDsp = mSerialPortFinder.getAllDevicesPath();
		dspPreference.setEntries(entriesDsp);
		dspPreference.setEntryValues(entryValuesDsp);
		dspPreference.setSummary(dspPreference.getValue());
		dspPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// IC card Baud rates
		final ListPreference dspBaudrates = (ListPreference)findPreference("DgtDspBAUDRATE");
		dspBaudrates.setSummary(dspBaudrates.getValue());
		dspBaudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference tempPreference = (ListPreference)findPreference("DEVICETEMP");
		String[] entriesTemp = mSerialPortFinder.getAllDevices();
		String[] entryValuesTemp = mSerialPortFinder.getAllDevicesPath();
		tempPreference.setEntries(entriesTemp);
		tempPreference.setEntryValues(entryValuesTemp);
		tempPreference.setSummary(tempPreference.getValue());
		tempPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// IC card Baud rates
		final ListPreference tempBaudrates = (ListPreference)findPreference("TEMPBAUDRATE");
		tempBaudrates.setSummary(tempBaudrates.getValue());
		tempBaudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});


		// Devices
		final ListPreference mdbPreference = (ListPreference)findPreference("DEVICEMDB");
		String[] entriesMdb = mSerialPortFinder.getAllDevices();
		String[] entryValuesMdb = mSerialPortFinder.getAllDevicesPath();
		mdbPreference.setEntries(entriesMdb);
		mdbPreference.setEntryValues(entryValuesMdb);
		mdbPreference.setSummary(mdbPreference.getValue());
		mdbPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Devices
		final ListPreference OtherPreference = (ListPreference)findPreference("OTHER");
		String[] entriesOther = mSerialPortFinder.getAllDevices();
		String[] entryValuesOther = mSerialPortFinder.getAllDevicesPath();
		OtherPreference.setEntries(entriesOther);
		OtherPreference.setEntryValues(entryValuesOther);
		OtherPreference.setSummary(OtherPreference.getValue());
		OtherPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// IC card Baud rates
		final ListPreference OtherBaudrates = (ListPreference)findPreference("OTHERBAUDRATE");
		OtherBaudrates.setSummary(OtherBaudrates.getValue());
		OtherBaudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSerialPortFinder = null;
	}

	public void serial_back(View v){
		TcnVendIF.getInstance().reqSlotNoInfoOpenSerialPort();
		this.finish();
	}

}
