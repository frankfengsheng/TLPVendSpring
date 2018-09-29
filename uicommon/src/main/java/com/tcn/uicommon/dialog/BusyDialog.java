package com.tcn.uicommon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcn.uicommon.R;
import com.tcn.uicommon.resources.Resources;


/**
 * Created by Administrator on 2016/7/11.
 */
public class BusyDialog extends Dialog {
    private static final int CANCEL_SHOW = 1;
    private int m_iMaxData = -1;
    private int m_iMaxTime = 60000;
    private RotateAnimation mAnim;
    private ImageView load_img;
    private TextView load_msg;

    public BusyDialog(Context context,String msg) {
        super(context, R.style.Dialog_bocop);
        init(context,msg);
    }

    @Override
    public void show() {
        super.show();
        load_img.startAnimation(mAnim);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mAnim.cancel();
        load_img.clearAnimation();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void cancel() {
        super.cancel();
        mAnim.cancel();
        load_img.clearAnimation();
        handler.removeCallbacksAndMessages(null);
    }

    public void setMaxData(int maxData) {
        m_iMaxData = maxData;
    }

    public int getMaxData() {
        return m_iMaxData;
    }

    public void setShowMaxTime(int second) {
        m_iMaxTime = second * 1000;
        handler.removeMessages(CANCEL_SHOW);
        handler.sendEmptyMessageDelayed(CANCEL_SHOW,m_iMaxTime);
    }

    public void setShowMessage(String msg) {
        if (load_msg != null) {
            load_msg.setText(msg);
        }
    }

    private void init(Context context,String msg) {
        View contentView = View.inflate(context, R.layout.lodaing, null);
        setContentView(contentView);

        load_img = (ImageView) contentView.findViewById(R.id.load_img);
        load_msg = (TextView) (TextView) contentView.findViewById(R.id.load_msg);
        load_msg.setText(msg);
        initAnim();
       getWindow().setWindowAnimations(Resources.getAnimResourceID(R.anim.alpha_in));
    }


    private void initAnim() {
        mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
        mAnim.setDuration(1500);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        LinearInterpolator lir = new LinearInterpolator();
        mAnim.setInterpolator(lir);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CANCEL_SHOW:
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}
