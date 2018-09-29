package com.tcn.background;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tcn.funcommon.vend.controller.TcnVendEventID;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.funcommon.vend.controller.VendEventInfo;
import com.tcn.uicommon.view.TcnMainActivity;


public class exit extends TcnMainActivity {

	private Button exit_login = null;
	private Button exit_confirm = null;
	private Button exit_cancel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.exit);
		exit_login = (Button) findViewById(R.id.exit_login);
		exit_confirm = (Button) findViewById(R.id.exit_confirm);
		exit_cancel = (Button) findViewById(R.id.exit_back);
		exit_login.setOnClickListener(m_ButtonListener);
		exit_confirm.setOnClickListener(m_ButtonListener);
		exit_cancel.setOnClickListener(m_ButtonListener);
		TcnVendIF.getInstance().registerListener(m_vendListener);
		super.onCreate(savedInstanceState);
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
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		m_ButtonListener = null;
		if (exit_login != null) {
			exit_login.setOnClickListener(null);
			exit_login = null;
		}
		if (exit_confirm != null) {
			exit_confirm.setOnClickListener(null);
			exit_confirm = null;
		}
		if (exit_cancel != null) {
			exit_cancel.setOnClickListener(null);
			exit_cancel = null;
		}
		m_ButtonListener = null;
		super.onDestroy();
	}

	private ButtonOnClickListener m_ButtonListener = new ButtonOnClickListener();
	private class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			int mId = v.getId();
			if (R.id.exit_login == mId) {
				Intent in = new Intent(exit.this, LoginMenu.class);
				startActivity(in);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				exit.this.finish();
			} else if (R.id.exit_confirm == mId) {
				setResult(777);
				exit.this.finish();
			} else if (R.id.exit_back == mId) {
				setResult(888);
				exit.this.finish();
			} else {
				
			}
			
		}
		
	}
	
	private VendListener m_vendListener = new VendListener();
	private class VendListener implements TcnVendIF.VendEventListener {

		@Override
		public void VendEvent(VendEventInfo cEventInfo) {
			switch (cEventInfo.m_iEventID) {
			case TcnVendEventID.COMMAND_DOOR_SWITCH:
				if (cEventInfo.m_lParam1 == TcnVendIF.COMMAND_CLOSE_DOOR) {
					setResult(RESULT_OK);
					exit.this.finish();
				}
				break;
			default:
				break;
			}
			
		}

	}

}
