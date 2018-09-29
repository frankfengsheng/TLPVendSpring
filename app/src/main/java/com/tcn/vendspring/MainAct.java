package com.tcn.vendspring;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcn.background.LoginMenu;
import com.tcn.background.SerialPortSetting;
import com.tcn.background.TLPAisleDisplayActivity;
import com.tcn.background.controller.UIComBack;
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
import com.tcn.uicommon.shimmer.Shimmer;
import com.tcn.uicommon.shimmer.ShimmerTextView;
import com.tcn.uicommon.view.TcnMainActivity;
import com.tcn.uicommon.view.TextSurfaceView;
import com.tcn.vendspring.help.DialogHelp;
import com.tcn.vendspring.keyboard.DialogVerify;
import com.tcn.vendspring.keyboard.FragmentVerify;
import com.tcn.vendspring.pay.DialogPay;
import com.tcn.vendspring.pay.TlpDialogPay;
import com.tcn.vendspring.shopping.FragmentSelection;

import controller.TlpUICommon;
import controller.UICommon;
import controller.VendApplication;
import controller.VendService;


@SuppressLint("NewApi")
public class MainAct extends TcnMainActivity {

	private static final String TAG = "MainAct";
	private static final int ALIVE_COIL_GOODS	 = 1;
	private static final int GOTO_BACKGROUND      = 2;
	private static final int SHOWN_FULLSCREEN      = 3;
	private static final int SHOW_SHIPSUCCESS_DIALOG = 5;
	private static int m_listData_count = 0;
	private boolean m_bHasData = false;
	private TextView m_main_balance = null;
	private TextView m_main_temperature = null;
	private TextView m_main_time = null;
	private ShimmerTextView m_ShimmerTextView = null;
	private Shimmer m_Shimmer = null;
	private LinearLayout m_GoodsLayout = null;
	private LinearLayout m_function_bar_layout = null;
	private TextView m_main_machine_id = null;
	private ImageView main_signal = null;

	private SurfaceView m_surface_advert_video;
	private SurfaceView m_surface_advert_image;
	private SurfaceView m_surface_standby_video = null;
	private SurfaceView m_surface_standby_image = null;

	private TextSurfaceView m_TextSurfaceView = null;

	private FragmentManager m_fragmentManager;
	private FragmentSelection m_fragmentSelection;
	private FragmentVerify m_fragmentVerify;

	private Button m_ButtonShopp = null;
	private Button m_ButtonKeyboard = null;
	private Button m_ButtonHelp = null;
	private Button m_ButtonLogin=null;//自修改增加一个暂时登录按钮。

	private RelativeLayout m_click_buy_layout = null;
	private TextView m_image_click_buy = null;

	private OutDialog m_OutDialog = null;
	private LoadingDialog m_LoadingDialog = null;
	private TlpDialogPay m_DialogPay = null;
	private DialogVerify m_DialogVerify = null;
	private DialogHelp m_DialogHelp = null;
	private ShopSuccessDialog m_shopSuccessDialog;
	private UsbProgressDialog m_upProgress;


	@SuppressLint({ "NewApi", "CommitTransaction" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate() start");

		initCreatView();

		TcnVendIF.getInstance().registerListener(m_vendListener);
		if (TcnVendIF.getInstance().isServiceRunning()){
			TcnVendIF.getInstance().LoggerDebug(TAG, "onCreate() isServiceRunning");
		} else {
			startService(new Intent(getApplication(), VendService.class));
		}
	}

	@Override
	protected void onResume() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "-------onResume()");
		super.onResume();
		TcnVendIF.getInstance().registerListener(m_vendListener);
		m_bHasData = false;
		initView();

		TcnVendIF.getInstance().queryImagePathList();
		initTitleBar();
		initShowBitmap();
		initTextAd();
		m_listData_count = 0;

		m_listData_count = TcnVendIF.getInstance().getAliveCoilCount();
		if((TcnVendIF.getInstance().getAliveCoil() != null) && (m_listData_count > 0) && (!TcnVendIF.getInstance().getAliveCoil().isEmpty())) {
			m_handler.removeMessages(ALIVE_COIL_GOODS);
			m_handler.sendEmptyMessage(ALIVE_COIL_GOODS);
		} else {
			m_Shimmer.start(m_ShimmerTextView);
		}
		TcnVendIF.getInstance().LoggerDebug(TAG, "-------onResume end");
	}

	@Override
	protected void onPause() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "-------onPause()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		removeFragment();
    	if (m_Shimmer != null) {
			m_Shimmer.cancel();
			m_Shimmer.removeListener();
        }
		if (m_TextSurfaceView != null) {
			m_TextSurfaceView.setVisibility(View.GONE);
			m_TextSurfaceView.setLoop(false);
		}

		if (m_handler != null) {
			m_handler.removeCallbacksAndMessages(null);
		}

		if (m_fragmentSelection != null) {
			m_fragmentSelection.destroyView();
			m_fragmentSelection = null;
		}

		if (m_fragmentVerify != null) {
			m_fragmentVerify.onDestroyView();
			m_fragmentVerify = null;
		}

		if (m_fragmentManager != null) {
			m_fragmentManager = null;
		}

		super.onPause();
	}


	@Override
	protected void onDestroy() {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onDestroy()");
		TcnVendIF.getInstance().unregisterListener(m_vendListener);
		TcnShareUseData.getInstance().setMainActivityCreated(false);
		TcnVendIF.getInstance().showSystembar();
		if (m_DialogPay != null) {
			m_DialogPay.setOnDismissListener(null);
			m_DialogPay.setOnShowListener(null);
			if (m_DialogPay.isShowing()) {
				m_DialogPay.dismiss();
			}
			//m_DialogPay.deInit();
			m_DialogPay = null;
		}
		if (m_DialogVerify != null) {
			m_DialogVerify.setOnDismissListener(null);
			m_DialogVerify.setOnShowListener(null);
			if (m_DialogVerify.isShowing()) {
				m_DialogVerify.dismiss();
			}
			m_DialogVerify.deInit();
			m_DialogVerify = null;
		}

		if (m_DialogHelp != null) {
			m_DialogHelp.setOnDismissListener(null);
			m_DialogHelp.setOnShowListener(null);
			if (m_DialogHelp.isShowing()) {
				m_DialogHelp.dismiss();
			}
			m_DialogHelp.deInit();
			m_DialogHelp = null;
		}

		UICommon.getInstance().removeSurfaceCallbackAdvertVideo(m_surface_advert_video);
		m_surface_advert_video = null;

		UICommon.getInstance().removeSurfaceCallbackAdvertImage(m_surface_advert_image);
		m_surface_advert_image = null;

		UICommon.getInstance().removeSurfaceCallbackStandbyVideo(m_surface_standby_video);
		m_surface_standby_video = null;

		UICommon.getInstance().removeSurfaceCallbackStandbyImage(m_surface_standby_image);
		m_surface_standby_image = null;
		if (m_Shimmer != null) {
			m_Shimmer.cancel();
			m_Shimmer = null;
		}
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
		if (m_TextSurfaceView != null) {
			m_TextSurfaceView.setLoop(false);
			m_TextSurfaceView = null;
		}
		if (m_ButtonShopp != null) {
			m_ButtonShopp.setOnClickListener(null);
			m_ButtonShopp = null;
		}
		if (m_ButtonKeyboard != null) {
			m_ButtonKeyboard.setOnClickListener(null);
			m_ButtonKeyboard = null;
		}
		if (m_ButtonHelp != null) {
			m_ButtonHelp.setOnClickListener(null);
			m_ButtonHelp = null;
		}

		//自修改增加点击登录按钮监听
		if (m_ButtonLogin != null) {
			m_ButtonLogin.setOnClickListener(null);
			m_ButtonLogin = null;
		}
		if (m_handler != null) {
			m_handler.removeCallbacksAndMessages(null);
			m_handler = null;
		}
		m_ShimmerTextView = null;
		m_fragmentSelection = null;
		m_fragmentVerify = null;
		m_LoadingDialog = null;
		m_ShowListener = null;
		m_vendListener = null;
		m_DismissListener = null;
		m_ClickListener = null;
		m_GoodsLayout = null;
		m_main_balance = null;
		m_main_temperature = null;
		m_main_time = null;
		m_Shimmer = null;
		m_function_bar_layout = null;
		m_main_machine_id = null;
		main_signal = null;
		m_fragmentManager = null;
		m_click_buy_layout = null;
		m_image_click_buy = null;
		m_OutDialog = null;
		m_LoadingDialog = null;

		super.onDestroy();
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TcnVendIF.getInstance().LoggerDebug(TAG, "onActivityResult requestCode : "+requestCode+" resultCode: "+resultCode);

		if (777 == resultCode) {
			if (666 == requestCode) {
				finish();
			}
		} else {

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (TcnShareUseData.getInstance().isShowScreenProtect()) {
			int action = MotionEventCompat.getActionMasked(ev);
			if (action == MotionEvent.ACTION_DOWN) {
				if (UICommon.getInstance().isScreenAdvertShowing()) {
					TcnVendIF.getInstance().reqTextSpeak(TcnShareUseData.getInstance().getWeclcome());
					hideMediaAd();
				} else {
					if (!UICommon.getInstance().isPayShowing()) {
						TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return super.dispatchKeyEvent(event);
	}

	private void initCreatView() {
		UICommon.getInstance().setContentViewMainActivity(MainAct.this);
		UIComBack.getInstance().setContext(getApplication());
		UICommon.getInstance().setApplication((VendApplication) getApplication());

		TcnVendIF.getInstance().hideSystemBar();
		TcnShareUseData.getInstance().setMainActivityCreated(true);

		/***********************************  Title bar start ***********************************/

		m_main_machine_id = (TextView) findViewById(R.id.title_bar_machine_id);
		m_main_balance = (TextView) findViewById(R.id.title_bar_balance);
		m_main_temperature = (TextView) findViewById(R.id.title_bar_temperature);
		m_main_time = (TextView) findViewById(R.id.title_bar_time);
		main_signal = (ImageView) findViewById(R.id.title_bar_signal);

		/***********************************  Title bar end ***********************************/



		/***********************************  advert start ***********************************/

		m_surface_advert_video = (SurfaceView) findViewById(R.id.advert_video);
		UICommon.getInstance().setSurfaceViewAdvertVideo(m_surface_advert_video);

		m_surface_advert_image = (SurfaceView) findViewById(R.id.advert_image);
		UICommon.getInstance().setSurfaceViewAdvertImage(m_surface_advert_image);

		m_surface_standby_video = (SurfaceView) findViewById(R.id.advert_video_standby);
		UICommon.getInstance().setSurfaceViewStandbyVideo(m_surface_standby_video);

		m_surface_standby_image = (SurfaceView) findViewById(R.id.advert_image_standby);
		UICommon.getInstance().setSurfaceViewStandbyImage(m_surface_standby_image);

		/***********************************  advert end ***********************************/



		m_GoodsLayout = (LinearLayout) findViewById(R.id.main_goods_layout);//这个是下半块区域的总布局

		m_function_bar_layout  = (LinearLayout) findViewById(R.id.main_function_bar_layout);


		/***********************************  function start ***********************************/

		m_ButtonShopp = (Button) findViewById(R.id.function_bar_btn_shopp);
		if (m_ButtonShopp != null) {
			m_ButtonShopp.setOnClickListener(m_ClickListener);
		}

		m_ButtonKeyboard = (Button) findViewById(R.id.function_bar_btn_keyboard);
		m_ButtonKeyboard.setOnClickListener(m_ClickListener);
		
		m_ButtonHelp = (Button) findViewById(R.id.function_bar_btn_help);
		m_ButtonHelp.setOnClickListener(m_ClickListener);

		m_ButtonLogin = (Button) findViewById(R.id.function_bar_btn_login);
		m_ButtonLogin.setOnClickListener(m_ClickListener);

		/***********************************  function end ***********************************/


		m_click_buy_layout = (RelativeLayout) findViewById(R.id.main_click_buy_layout);
		//m_click_buy_layout.setVisibility(View.GONE);
		m_image_click_buy = (TextView) findViewById(R.id.click_buy_image);
		m_image_click_buy.setTextColor(Color.RED);
		m_image_click_buy.setTextSize(UICommon.getInstance().getClickBuyTextSize());


		/***********************************  TextSurfaceView start ***********************************/

		m_TextSurfaceView = (TextSurfaceView) findViewById(R.id.main_text_sfv);
		//为最下方添加点击事件
        m_TextSurfaceView.setOnClickListener(m_ClickListener);
		if (m_TextSurfaceView != null) {
			m_TextSurfaceView.setMove(true);
			m_TextSurfaceView.setSpeed(100);
			m_TextSurfaceView.setFontSize(20);
		}

		/***********************************  TextSurfaceView end ***********************************/




		/***********************************  shimmer start ***********************************/

		m_ShimmerTextView = (ShimmerTextView) findViewById(R.id.goods_shimmer_tv_loading);

		m_Shimmer = new Shimmer();
		m_Shimmer.setDuration(3000);

		/***********************************  shimmer end ***********************************/

		if (null == m_DialogPay) {
			m_DialogPay = new TlpDialogPay(MainAct.this);
			m_DialogPay.setOnDismissListener(m_DismissListener);
			m_DialogPay.setOnShowListener(m_ShowListener);
		}
	}

	/**
	 * 初始化TitleBar
	 */
	private void initTitleBar() {
		m_main_machine_id.setText(getString(R.string.machine_id_tip) + TcnShareUseData.getInstance().getMachineID());
	}

	private void initView() {
		m_ButtonKeyboard.setText(TcnShareUseData.getInstance().getKeyBoardText());
		if (m_TextSurfaceView != null) {
			m_TextSurfaceView.setVisibility(View.VISIBLE);
		}
		if (!TcnShareUseData.getInstance().isShowShopping()) {
			m_function_bar_layout.setVisibility(View.GONE);
		}
		hideMediaAd();
	}

	private void initTextAd() {
		if (m_TextSurfaceView != null) {
			m_TextSurfaceView.setContent(TcnShareUseData.getInstance().getAdvertText());
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

	private void initShowBitmap() {
		Bitmap bitmap = TcnVendIF.getInstance().getBackgroundBitmap();
		if (bitmap != null) {
			m_GoodsLayout.setBackground(new BitmapDrawable(bitmap));
		}
	}

	private void selectButtonShop() {
		if (m_ButtonShopp == null) {
			return;
		}
		m_ButtonShopp.setSelected(true);
		m_ButtonShopp.setTextColor(getResources().getColor(R.color.layout1));
	}

	private void selectKeyboard() {
		if (m_ButtonKeyboard == null) {
			return;
		}
		m_ButtonKeyboard.setSelected(true);
		m_ButtonKeyboard.setTextColor(getResources().getColor(R.color.layout1));
	}

	private void removeFragment() {

		if (m_fragmentManager != null) {
			FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();
			if (null != fragmentTransaction) {
				if ((m_fragmentSelection != null)) {
					fragmentTransaction.remove(m_fragmentSelection);
				}
				if ((m_fragmentVerify != null)) {
					fragmentTransaction.remove(m_fragmentVerify);
				}
				fragmentTransaction.commit();
			}
		}

	}

	private FragmentTransaction getFragmentTransaction() {
		m_fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();
		if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())
				|| (!TcnShareUseData.getInstance().isShowShopping())) {
			if (null == m_fragmentVerify) {
				m_fragmentVerify = new FragmentVerify();
				fragmentTransaction.add(R.id.goods_frame, m_fragmentVerify);
			} else {
				if (!m_fragmentVerify.isAdded()) {
					fragmentTransaction.remove(m_fragmentVerify);
					m_fragmentVerify = new FragmentVerify();
					fragmentTransaction.add(R.id.goods_frame, m_fragmentVerify);
				}
			}

//			if (!m_fragmentVerify.isAdded()) {
//				fragmentTransaction.remove(m_fragmentVerify);
//				fragmentTransaction.add(R.id.goods_frame, m_fragmentVerify);
//			}
		} else {
			if (null == m_fragmentSelection) {
				m_fragmentSelection = new FragmentSelection();
				fragmentTransaction.add(R.id.goods_frame, m_fragmentSelection);
			} else {
				if (!m_fragmentSelection.isAdded()) {
					fragmentTransaction.remove(m_fragmentSelection);
					m_fragmentSelection = new FragmentSelection();
					fragmentTransaction.add(R.id.goods_frame, m_fragmentSelection);
				}
			}
		}

		return fragmentTransaction;
	}
	private void DisplayError(int resourceId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainAct.this);
		builder.setTitle("提示");
		builder.setMessage(resourceId);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent in = new Intent(MainAct.this, SerialPortSetting.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainAct.this.startActivity(in);
			}
		});
		Dialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
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

	private void showDialogPay() {
		if (!TcnShareUseData.getInstance().isShowShopping()) {
			return;
		}
		if (isFinishing()) {
			return;
		}
		if (m_DialogHelp != null) {
			m_DialogHelp.dismiss();
		}
		if (m_DialogVerify != null) {
			m_DialogVerify.dismiss();
		}
		if (m_DialogPay != null) {
			if (!m_DialogPay.isShowing()) {
				m_DialogPay.show();
			}
		}
	}

	private void setStandbyAdvert(final boolean bShow) {
		if (TcnShareUseData.getInstance().isFullScreen()) {
			m_GoodsLayout.post(new Runnable() {
				@Override
				public void run() {
					if (bShow) {
						m_GoodsLayout.setVisibility(View.GONE);
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.VISIBLE);
						}
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.VISIBLE);
						}
						startRotateAnimation();
					} else {
						stopRotateAnimation();
						TcnVendIF.getInstance().stopPlayStandbyAdvert();
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.GONE);
						}
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.GONE);
						}
						m_GoodsLayout.setVisibility(View.VISIBLE);
					}
				}
			});
			return;
		}
		m_GoodsLayout.post(new Runnable() {
			@Override
			public void run() {
				if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
					if (bShow) {
						TcnVendIF.getInstance().stopPlayAdvert();
						if (m_surface_advert_video != null) {
							m_surface_advert_video.setVisibility(View.GONE);
						}
						if (m_surface_advert_image != null) {
							m_surface_advert_image.setVisibility(View.GONE);
						}
						m_GoodsLayout.setVisibility(View.GONE);
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.VISIBLE);
						}
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.VISIBLE);
						}
						startRotateAnimation();
					} else {
						stopRotateAnimation();
						TcnVendIF.getInstance().stopPlayStandbyAdvert();
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.GONE);
						}
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.GONE);
						}
						if (m_surface_advert_video != null) {
							m_surface_advert_video.setVisibility(View.VISIBLE);
						}
						if (m_surface_advert_image != null) {
							m_surface_advert_image.setVisibility(View.VISIBLE);
						}
						m_GoodsLayout.setVisibility(View.VISIBLE);
					}
				} else {
					if (bShow) {
						m_GoodsLayout.setVisibility(View.GONE);
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.VISIBLE);
						}
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.VISIBLE);
						}
						startRotateAnimation();
					} else {
						stopRotateAnimation();
						TcnVendIF.getInstance().stopPlayStandbyAdvert();
						if (m_surface_standby_image != null) {
							m_surface_standby_image.setVisibility(View.GONE);
						}
						if (m_surface_standby_video != null) {
							m_surface_standby_video.setVisibility(View.GONE);
						}
						m_GoodsLayout.setVisibility(View.VISIBLE);
					}
				}

			}
		});

	}

	private void setPlayImage() {
		boolean bPlayImage = true;
		if (!TcnShareUseData.getInstance().isFullScreen()) {
			if (TcnShareUseData.getInstance().isShowScreenProtect()) {
				if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
					if (UICommon.getInstance().isScreenAdvertShowing()) {
						bPlayImage = false;
					}
				}
			}
		}

		if(null == m_surface_advert_video) {
			return;
		}
		if(null == m_surface_advert_image) {
			return;
		}

		if (bPlayImage) {
			if (!TcnVendIF.getInstance().isPlaying()) {
				m_surface_advert_image.setVisibility(View.GONE);
			}
			m_surface_advert_video.setVisibility(View.GONE);
			m_surface_advert_image.setVisibility(View.VISIBLE);
		}
	}

	private void setPlayVideo() {
		boolean bPlayVideo = true;
		if (!TcnShareUseData.getInstance().isFullScreen()) {
			if (TcnShareUseData.getInstance().isShowScreenProtect()) {
				if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
					if (UICommon.getInstance().isScreenAdvertShowing()) {
						bPlayVideo = false;
					}
				}
			}
		}

		if(null == m_surface_advert_video) {
			return;
		}
		if(null == m_surface_advert_image) {
			return;
		}

		if (bPlayVideo) {
			if (!TcnVendIF.getInstance().isPlaying()) {
				m_surface_advert_video.setVisibility(View.GONE);
			}
			m_surface_advert_image.setVisibility(View.GONE);
			m_surface_advert_video.setVisibility(View.VISIBLE);
		}
	}

	private void setPlayStandbyImage() {
		if (!TcnShareUseData.getInstance().isShowScreenProtect()) {
			return;
		}
		if(null == m_surface_standby_video) {
			return;
		}
		if(null == m_surface_standby_image) {
			return;
		}
		m_surface_standby_image.post(new Runnable() {
			@Override
			public void run() {
				if (!TcnVendIF.getInstance().isPlayingScreen()) {
					m_surface_standby_image.setVisibility(View.GONE);
				}
				m_surface_standby_video.setVisibility(View.GONE);
				m_surface_standby_image.setVisibility(View.VISIBLE);
				startRotateAnimation();
			}
		});
	}

	private void setPlayStandbyVideo() {
		if (!TcnShareUseData.getInstance().isShowScreenProtect()) {
			return;
		}
		if(null == m_surface_standby_video) {
			return;
		}
		if(null == m_surface_standby_image) {
			return;
		}
		m_surface_standby_video.post(new Runnable() {
			@Override
			public void run() {
				if (!TcnVendIF.getInstance().isPlayingScreen()) {
					m_surface_standby_video.setVisibility(View.GONE);
				}
				m_surface_standby_image.setVisibility(View.GONE);
				m_surface_standby_video.setVisibility(View.VISIBLE);
				startRotateAnimation();
			}
		});
	}

	private void startRotateAnimation() {
		m_click_buy_layout.postDelayed(new Runnable() {
			@Override
			public void run() {
				stopRotateAnimation();
				//m_click_buy_layout.setVisibility(View.VISIBLE);
				//m_image_click_buy.setVisibility(View.VISIBLE);
				//m_image_click_buy.startAnimation(UICommon.getInstance().getRotateAnimation());
				if (TcnShareUseData.getInstance().isShowTapScreenText()) {
					m_image_click_buy.setText(R.string.ui_click_and_buy);
				}
			}
		},300);

	}

	private void stopRotateAnimation() {
		/*m_image_click_buy.clearAnimation();
		m_image_click_buy.setAnimation(null);
		m_click_buy_layout.clearAnimation();
		m_click_buy_layout.setAnimation(null);
		m_image_click_buy.setVisibility(View.GONE);
		m_click_buy_layout.setVisibility(View.GONE);
		UICommon.getInstance().stopRotateAnimation();*/
		m_image_click_buy.setText("");
	}

	//显示屏保
	private void showMediaAd() {
		if (!TcnShareUseData.getInstance().isShowScreenProtect()) {
			return;
		}
		if (UICommon.getInstance().isScreenAdvertShowing()) {
			return;
		}
		setStandbyAdvert(true);
		if (m_fragmentSelection != null) {
			if (!m_fragmentSelection.isHidden() && m_fragmentSelection.isVisible()) {
				m_fragmentSelection.cancelScrollTimerTask();
			}
		}
		UICommon.getInstance().setScreenAdvertShow(true);
	}

	//隐藏屏保
	private void hideMediaAd() {
		if (!TcnShareUseData.getInstance().isShowScreenProtect()) {
			if (m_fragmentSelection != null) {
				if (!m_fragmentSelection.isHidden() && m_fragmentSelection.isVisible()) {
					m_fragmentSelection.startScrollTimer(10000);
				}
			}
			return;
		}

		hideMediaAdNotRestart();

		if (m_fragmentSelection != null) {
			if (!m_fragmentSelection.isHidden() && m_fragmentSelection.isVisible()) {
				m_fragmentSelection.startScrollTimer(10000);
			}
		}
		TcnVendIF.getInstance().reqShowOrHideAdMedia(TcnVendIF.SHOW_AD_MEDIA);
	}

	private void hideMediaAdNotRestart() {
		if (m_fragmentSelection != null) {
			if (!m_fragmentSelection.isHidden() && m_fragmentSelection.isVisible()) {
				m_fragmentSelection.cancelScrollTimerTask();
			}
		}

		if (!TcnShareUseData.getInstance().isShowScreenProtect()) {
			return;
		}

		setStandbyAdvert(false);
		TcnVendIF.getInstance().reqStopShowOrHideAdMedia();
		UICommon.getInstance().setScreenAdvertShow(false);
	}

	private void shipSuccessTips() {
		if (null != m_OutDialog) {
			m_OutDialog.cancel();
		}

		if (m_LoadingDialog == null) {
			m_LoadingDialog = new LoadingDialog(MainAct.this, getString(R.string.notify_shipment_success), getString(R.string.notify_receive_goods));
		} else {
			m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_success));
			m_LoadingDialog.setTitle(getString(R.string.notify_receive_goods));
		}

		m_LoadingDialog.setShowTime(3);

		m_LoadingDialog.show();
	}


	private Handler m_handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
				case ALIVE_COIL_GOODS:
					TcnVendIF.getInstance().LoggerDebug(TAG, "handleMessage ......有货道信息........ ");
					hideMediaAd();
					if (m_Shimmer != null && m_Shimmer.isAnimating()) {
						m_Shimmer.cancel();
					}
					m_ShimmerTextView.setVisibility(View.GONE);
					selectButtonShop();
					FragmentTransaction fragmentTransaction = getFragmentTransaction();
					m_bHasData = true;
					if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())
							|| (!TcnShareUseData.getInstance().isShowShopping())) {
						if (!getResources().getConfiguration().locale.getCountry().equals("CN")) {
							m_function_bar_layout.setVisibility(View.GONE);
						}
						selectKeyboard();
						if (m_fragmentVerify != null) {
							fragmentTransaction.show(m_fragmentVerify).commit();
						}
					} else {
						if (m_fragmentSelection != null) {
							fragmentTransaction.show(m_fragmentSelection).commit();
						}
					}
					break;
				case GOTO_BACKGROUND:
					Intent in = new Intent(MainAct.this, LoginMenu.class);
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

	private ButtonClickListener m_ClickListener = new ButtonClickListener();
	private class ButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			FragmentTransaction fragmentTransaction = getFragmentTransaction();
			if (R.id.function_bar_btn_shopp == id) {
				if (m_fragmentSelection != null) {
					fragmentTransaction.show(m_fragmentSelection).commit();
				}
				selectButtonShop();
			} else if (R.id.function_bar_btn_keyboard == id) {
				if ((TcnCommon.SCREEN_INCH[1]).equals(TcnShareUseData.getInstance().getScreenInch())
						|| (!TcnShareUseData.getInstance().isShowShopping())) {
					fragmentTransaction.show(m_fragmentVerify).commit();
					selectKeyboard();
				} else {
					if (null == m_DialogVerify) {
						m_DialogVerify = new DialogVerify(MainAct.this);
						m_DialogVerify.setOnDismissListener(m_DismissListener);
						m_DialogVerify.setOnShowListener(m_ShowListener);
					}
					m_DialogVerify.show();
				}
			} else if (R.id.function_bar_btn_help == id) {
				if (null == m_DialogHelp) {
					m_DialogHelp = new DialogHelp(MainAct.this);
					m_DialogHelp.setOnDismissListener(m_DismissListener);
					m_DialogHelp.setOnShowListener(m_ShowListener);
				}
				m_DialogHelp.show();
			} else if (R.id.function_bar_btn_login == id) {
				/*Intent intent=new Intent(getApplicationContext(),LoginMenu.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
				/*Intent intent=new Intent(MainAct.this, TLPAisleDisplayActivity.class);
				startActivity(intent);*/

			}else  if(R.id.main_text_sfv==id){
				TlpUICommon.getInstance().GetGoodsInfo(getApplicationContext());
                Intent intent=new Intent(getApplicationContext(),LoginMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
			else {

			}

		}
	}

	private PayDismissListener m_DismissListener = new PayDismissListener();
    private class PayDismissListener implements DialogInterface.OnDismissListener {
		@Override
		public void onDismiss(DialogInterface dialog) {
			hideMediaAd();
		}
	}

	private PayShowListener m_ShowListener = new PayShowListener();
	private class PayShowListener implements DialogInterface.OnShowListener {
		@Override
		public void onShow(DialogInterface dialog) {
			hideMediaAdNotRestart();
		}
	}

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
					TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4, Toast.LENGTH_LONG).show();
					break;
				case TcnVendEventID.ALL_FILES_PLAY_FAILED:
					TcnUtility.getToast(MainAct.this, getString(R.string.notify_no_video), Toast.LENGTH_LONG).show();
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
					TcnUtility.getToast(MainAct.this, getString(R.string.notify_atcip_mode));
					break;
				case TcnVendEventID.IMAGE_SCREEN:
					/*if (m_surface_standby_image != null) {
						m_surface_standby_image.setImageBitmap(TcnVendIF.getInstance().getScreenBitmap());
					}*/
					break;
				case TcnVendEventID.TEXT_AD:
					initTextAd();
					break;
				case TcnVendEventID.SHOW_OR_HIDE_AD_MEDIA:
					if (cEventInfo.m_lParam1 == TcnVendIF.SHOW_AD_MEDIA) {
						showMediaAd();
					} else {
						hideMediaAd();
					}

					break;

				case TcnVendEventID.COMMAND_DOOR_SWITCH:
					if (cEventInfo.m_lParam1 != TcnVendIF.COMMAND_CLOSE_DOOR) {
						Intent in = new Intent(MainAct.this, LoginMenu.class);
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
					showDialogPay();
					break;
				case TcnVendEventID.COMMAND_INVALID_SLOTNO:
					TcnUtility.getToast(MainAct.this, getString(R.string.notify_invalid_slot));
					dismissAllDialog();
					break;
				case TcnVendEventID.COMMAND_SOLD_OUT:
					if (cEventInfo.m_lParam1 > 0) {
						TcnUtility.getToast(MainAct.this, getString(R.string.aisle_name)+cEventInfo.m_lParam1+getString(R.string.notify_sold_out));
					} else {
						TcnUtility.getToast(MainAct.this, getString(R.string.notify_sold_out));
					}
					dismissAllDialog();
					break;
				case TcnVendEventID.COMMAND_FAULT_SLOTNO:
					TcnUtility.getToast(MainAct.this,cEventInfo.m_lParam4);
					dismissAllDialog();
					break;
				case TcnVendEventID.COMMAND_SHIPPING:
					if (m_DialogPay != null) {
						UICommon.getInstance().setCanRefund(false);
						m_DialogPay.dismiss();
					}

					if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
						if (m_OutDialog == null) {
							m_OutDialog = new OutDialog(MainAct.this, String.valueOf(cEventInfo.m_lParam1), cEventInfo.m_lParam4);
						} else {
							m_OutDialog.setText(cEventInfo.m_lParam4);
						}
						m_OutDialog.cleanData();
					} else {
						if (m_OutDialog == null) {
							m_OutDialog = new OutDialog(MainAct.this, String.valueOf(cEventInfo.m_lParam1), getString(R.string.notify_shipping));
						} else {
							m_OutDialog.setText(getString(R.string.notify_shipping));
						}
					}
					m_OutDialog.setNumber(String.valueOf(cEventInfo.m_lParam1));
					m_OutDialog.show();
					break;

				case TcnVendEventID.COMMAND_SHIPMENT_SUCCESS:
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
					if(null != m_OutDialog) {
						m_OutDialog.cancel();
					}
					if(null == m_LoadingDialog) {
						m_LoadingDialog = new LoadingDialog(MainAct.this,getString(R.string.notify_shipment_fail),getString(R.string.notify_contact_merchant));
					}
					m_LoadingDialog.setLoadText(getString(R.string.notify_shipment_fail));
					m_LoadingDialog.setTitle(getString(R.string.notify_contact_merchant));
					m_LoadingDialog.setShowTime(3);
					m_LoadingDialog.show();
					break;
				case TcnVendEventID.WX_TRADE_REFUND:
					TcnUtility.getToast(MainAct.this,cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.ALIPAY_SCAN_COLSE:
				case TcnVendEventID.ALIPAY_WAVE_COLSE:
					TcnUtility.getToast(MainAct.this,cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_PLAY_IMAGE:
					setPlayImage();
					break;
				case TcnVendEventID.CMD_PLAY_VIDEO:
					setPlayVideo();
					break;
				case TcnVendEventID.IMAGE_BACKGROUND:
					Bitmap bitmap = TcnVendIF.getInstance().getBackgroundBitmap();
					if (bitmap != null) {
						m_GoodsLayout.setBackground(new BitmapDrawable(bitmap));
					}
					break;
				case TcnVendEventID.IMAGE_HELP:
					if (m_DialogHelp != null) {
						m_DialogHelp.refsh();
					}
					break;
				case TcnVendEventID.NETWORK_CHANGE:
					if (ConnectivityManager.TYPE_WIFI == cEventInfo.m_lParam1) {
						if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_POOR) {
							main_signal.setImageResource(R.mipmap.ic_qs_wifi_full_1);

						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_MODERATE) {
							main_signal.setImageResource(R.mipmap.ic_qs_wifi_full_2);
						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_GOOD) {
							main_signal.setImageResource(R.mipmap.ic_qs_wifi_full_3);

						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_GREAT) {
							main_signal.setImageResource(R.mipmap.ic_qs_wifi_full_4);
						} else {
							main_signal.setImageResource(R.mipmap.ic_qs_wifi_0);

						}
					} else if (ConnectivityManager.TYPE_ETHERNET == cEventInfo.m_lParam1) {
						main_signal.setImageResource(R.mipmap.ic_settings_ethernet);

					} else if (ConnectivityManager.TYPE_MOBILE == cEventInfo.m_lParam1) {
						if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_POOR) {
							main_signal.setImageResource(R.mipmap.ic_qs_signal_full_1);

						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_MODERATE) {
							main_signal.setImageResource(R.mipmap.ic_qs_signal_full_2);

						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_GOOD) {
							main_signal.setImageResource(R.mipmap.ic_qs_signal_full_3);

						} else if (cEventInfo.m_lParam2 == NetWorkUtil.SIGNAL_STRENGTH_GREAT) {
							main_signal.setImageResource(R.mipmap.ic_qs_signal_full_4);

						} else {
							main_signal.setImageResource(R.mipmap.ic_qs_signal_full_0);

						}
					} else {

					}
					break;
				case TcnVendEventID.UPDATE_TIME:
					m_main_time.setText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.BACK_TO_SHOPPING:
					dismissAllDialog();
					break;
				case TcnVendEventID.CMD_PLAY_SCREEN_IMAGE:
					setPlayStandbyImage();
					break;
				case TcnVendEventID.CMD_PLAY_SCREEN_VIDEO:
					setPlayStandbyVideo();
					break;

				case TcnVendEventID.COMMAND_DOOR_IS_OPEND:
					TcnUtility.getToast(MainAct.this, getString(R.string.tip_close_door));
					break;
				case TcnVendEventID.PROMPT_INFO:
					TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.NETWORK_NOT_GOOOD:
					if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
						TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4);
					}
					break;
				case TcnVendEventID.PAY_FAIL:
					if ((cEventInfo.m_lParam4 != null) && ((cEventInfo.m_lParam4).length() > 0)) {
						TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4);
					}
					break;
				case TcnVendEventID.TEMPERATURE_INFO:
					m_main_temperature.setText(cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.COMMAND_CANCEL_PAY:
					backMainMenu();
					break;
				case TcnVendEventID.REMOUT_ADVERT_TEXT:
					initTextAd();
					break;
				case TcnVendEventID.PLESE_CLOSE_FOOR:
					TcnUtility.getToast(MainAct.this, getString(R.string.tip_close_door));
					break;
				case TcnVendEventID.COMMAND_SYSTEM_BUSY:
					TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				case TcnVendEventID.CMD_MACHINE_LOCKED:
					TcnUtility.getToast(MainAct.this, cEventInfo.m_lParam4);
					break;
				default:
					break;
			}
		}

	}
}
