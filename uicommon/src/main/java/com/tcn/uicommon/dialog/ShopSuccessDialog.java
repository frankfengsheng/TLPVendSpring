package com.tcn.uicommon.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tcn.uicommon.R;

import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Side;

import static android.graphics.Color.BLACK;

/**
 * Created by hua on 2017/2/13.
 */

public class ShopSuccessDialog extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;

    private int timeCount = 0;
    private int m_iMaxTime = 60000;

    private Context context;
    private int mScrnWidth;
    private LinearLayout mScrnLayout;
    private RelativeLayout mDialogLayout;
    private ImageView door;
    private String text;
    public ShopSuccessDialog(Context context,String text) {
        super(context);
        this.context = context;
        this.text=text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScrnWidth = metrics.widthPixels;
        mScrnLayout = new LinearLayout(context);
        mScrnLayout.setGravity(Gravity.CENTER);
        mScrnLayout.setBackgroundColor(Color.WHITE);
        mDialogLayout = new RelativeLayout(context);
        int width = (int) (mScrnWidth * 0.8);
        mDialogLayout.setLayoutParams(new RelativeLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
        View view = getView();
        mDialogLayout.addView(view);
        mScrnLayout.addView(mDialogLayout);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mScrnLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onAttachedToWindow() {
        showText();
        btn();
    }

    @Override
    public void show() {
        super.show();
        timeCount = 0;
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void cancel() {
        super.cancel();
        handler.removeCallbacksAndMessages(null);
    }

    public void deInit() {
        setShowTime(0);
        setOnShowListener(null);
        setOnDismissListener(null);
        setOnCancelListener(null);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        context = null;
        mScrnLayout = null;
        mDialogLayout = null;
        door = null;
        text = null;
    }

    public void btn() {
        Glide.with(context).load(R.drawable.ggd).asGif().into(door);

    }

    public View getView() {
        View view = View.inflate(context, R.layout.shopsuccess, null);
        door = (ImageView) view.findViewById(R.id.door);
        textSurface = (TextSurface) view.findViewById(R.id.textSurface);
        return view;
    }

    TextSurface textSurface;

    public void showText() {
        if (textSurface == null) {
            return;
        }
        textSurface.setLayoutParams(new LinearLayout.LayoutParams(mScrnWidth - mScrnWidth / 5, mScrnWidth / 10));
        textSurface.reset();

        Text textDaai = TextBuilder
                .create(text)
                .setSize(35)
                .setAlpha(0)
                .setColor(BLACK)
                .setPosition(Align.SURFACE_CENTER).build();


        textSurface.play(
                new Sequential(
                        ShapeReveal.create(textDaai, 750, SideCut.show(Side.LEFT), false),
                        new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textDaai, 600, SideCut.show(Side.LEFT), false))),
                        Delay.duration(200)
                )
        );

    }

    public void setShowTime(int second) {
        timeCount = 0;
        m_iMaxTime = second * 1000;
    }

    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {

                if (timeCount++ > (m_iMaxTime/CHNAGE_TITLE_DELAYMILLIS)) {
                    handler.removeMessages(CHANGE_TITLE_WHAT);
                    timeCount = 0;
                    dismiss();
                    return;
                }

                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    timeCount = 0;
                }
            }
        };
    };
}
