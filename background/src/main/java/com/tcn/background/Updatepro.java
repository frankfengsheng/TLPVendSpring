package com.tcn.background;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tcn.funcommon.TcnCustomer;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.update.DialogHelper;
import com.tcn.funcommon.update.UpdateInfo;
import com.tcn.funcommon.update.UpdateManager;
import com.tcn.funcommon.vend.controller.TcnVendIF;
import com.tcn.uicommon.TcnUtility;
import com.tcn.uicommon.view.TcnMainActivity;




/**
 * 程序更新
 * @author Administrator
 */
public class Updatepro extends TcnMainActivity {

	private static final String TAG = "updatepro";
	private boolean m_bInit = false;
	private TextView m_curVer;
	private TextView m_newVer;
	private TextView update_content;
	private String m_curVerCode = "";
	private String m_curVerName = "";
	private UpdateManager updateMan;
	private ProgressDialog updateProgressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		if (null == m_curVer) {
			m_curVer = (TextView) findViewById(R.id.update_curVer);
			if (null == m_curVer) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() m_curVer is null");
				return;
			}
		}

		if (null == m_newVer) {
			m_newVer = (TextView) findViewById(R.id.update_newVer);
			if (null == m_newVer) {
				TcnVendIF.getInstance().LoggerError(TAG, "onCreate() m_newVer is null");
				return;
			}
		}

		update_content = (TextView) findViewById(R.id.update_content);

		if (null == updateMan) {
			updateMan = new UpdateManager(Updatepro.this, TcnShareUseData.getInstance().getApkName(), TcnShareUseData.getInstance().getApkUrl(), appUpdateCb);
		}

		m_bInit = true;
		updateMan.checkUpdate();
	}


	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (updateProgressDialog != null) {
			updateProgressDialog = null;
			updateProgressDialog = null;
		}
		if(updateMan != null) {
			updateMan.cancelDownload();
			updateMan = null;
		}
		m_curVer = null;
		m_newVer = null;
		m_curVerCode = null;
		m_curVerName = null;
		updateMan = null;
	}


	public void update_pro(View v) {
		if (!TcnVendIF.getInstance().isNetConnected()) {
			TcnUtility.getToast(Updatepro.this,getString(R.string.tip_check_network));
			return;
		}
		updateMan.checkUpdate();
	}

	public void update_back(View v) {
		this.finish();
	}

	private void initData() {
		String[] str = updateMan.getCurVerInfo();
		m_curVerCode = str[0];
		m_curVerName = str[1];
		m_curVer.setText(m_curVerName);
		m_newVer.setText(TcnVendIF.getInstance().getNewVerionName());
	}


	private UpdateManager.UpdateCallback appUpdateCb = new UpdateManager.UpdateCallback() {

		@Override
		public void checkUpdateCompleted(Boolean hasUpdate,
										 CharSequence updateInfo) {
			TcnVendIF.getInstance().LoggerDebug(TAG, "downloadapk checkUpdateCompleted hasUpdate: "+hasUpdate);

			UpdateInfo mUpdateInfo = updateMan.getUpdataInfo();
			if (null == mUpdateInfo) {
				return;
			}
			if (m_bInit) {
				m_newVer.setText(mUpdateInfo.getVersionName());
				m_bInit = false;
				if (!m_curVerName.equals(mUpdateInfo.getVersionName())) {
					update_content.setText(mUpdateInfo.getContent());
				}
				return;
			}

			if((!hasUpdate) && (null == updateInfo)) {
				TcnUtility.getToast(Updatepro.this, getString(R.string.tip_cannot_connect_server));
				return;
			}

			if (m_curVerName.equals(mUpdateInfo.getVersionName())) {
				TcnUtility.getToast(Updatepro.this, getString(R.string.tip_latest_version));
				return;
			}
			if (hasUpdate) {
				DialogHelper.Confirm(Updatepro.this,
						getText(R.string.dialog_update_title),
						getText(R.string.dialog_update_msg).toString()
								+ updateInfo +
								getText(R.string.dialog_update_msg2).toString(),
						getText(R.string.dialog_update_btnupdate),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								TcnVendIF.getInstance().LoggerDebug(TAG, "downloadapk checkUpdateCompleted which: " + which);
								updateProgressDialog = new ProgressDialog(Updatepro.this);
								updateProgressDialog.setMessage(getText(R.string.dialog_downloading_msg).toString());
								updateProgressDialog.setIndeterminate(false);
								updateProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								updateProgressDialog.setMax(100);
								updateProgressDialog.setProgress(0);
								updateProgressDialog.show();

								updateMan.downloadPackage();
							}

						}, getText(R.string.dialog_update_btnnext), null);
			}
		}

		@Override
		public void downloadProgressChanged(int progress) {
			if (updateProgressDialog != null && updateProgressDialog.isShowing()) {
				updateProgressDialog.setProgress(progress);
			}
		}

		@Override
		public void downloadCanceled() {
			// TODO Auto-generated method stub

		}

		@Override
		public void downloadCompleted(Boolean sucess, CharSequence errorMsg) {
			TcnVendIF.getInstance().LoggerDebug(TAG,"downloadapk downloadCompleted sucess: "+sucess+" errorMsg: "+errorMsg);
			if (updateProgressDialog != null && updateProgressDialog.isShowing()) {
				updateProgressDialog.dismiss();
			}
			if (sucess) {
				updateMan.update();
			} else {
				DialogHelper.Confirm(Updatepro.this,
						R.string.dialog_error_title,
						R.string.dialog_downfailed_msg,
						R.string.dialog_downfailed_btnnext,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								TcnVendIF.getInstance().LoggerDebug(TAG,"downloadapk downloadCompleted to downloadPackage which: "+which);
								updateMan.downloadPackage();
							}


						}, R.string.dialog_downfailed_btnnext, null);
			}
		}

	};
}
