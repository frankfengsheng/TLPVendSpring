package com.tcn.uicommon.view;

/**
 * Created by Administrator on 2016/10/28.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TextSurfaceView extends SurfaceView implements Callback, Runnable {

    /**
     *是否滚动
     */
    private boolean         isMove = false;
    /**
     * 移动方向
     */
    private int             orientation = 0;
    /**
     * 向左移动
     */
    public final static int MOVE_LEFT = 0;
    /**
     * 向右移动
     */
    public final static int MOVE_RIGHT = 1;
    /**
     * 移动速度　1.5s　移动一次
     */
    private long             speed = 1500;
    /**
     *字幕内容
     */
    private String             content;

    /**
     * 字幕背景色
     * */
    private String             bgColor = "#E7E7E7";

    /**
     * 字幕透明度　默认：60
     */
    private int             bgalpha = 60;

    /**
     * 字体颜色 　默认：白色 (#FFFFFF)
     */
    private String             fontColor = "#FFFFFF";

    /**
     * 字体透明度　默认：不透明(255)
     */
    private int             fontAlpha = 255;

    /**
     * 字体大小 　默认：20
     */
    private float             fontSize = 20f;
    /**
     * 容器
     */
    private SurfaceHolder     mSurfaceHolder;
    /**
     * 线程控制
     */
    private boolean         loop = true;
    /**
     * 内容滚动位置起始坐标
     */
    private float             x=0;

    private Thread mThread = null;

    /**
     * @param context
     * <see>默认滚动</see>
     */
    public TextSurfaceView(Context context) {
        super(context);
        defaultInit(context);
    }

    public TextSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultInit(context);
    }

    public TextSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultInit(context);
    }


    /**
     * @param context
     * @param move
     * <see>是否滚动</see>
     */
    public TextSurfaceView(Context context,boolean move) {
        this(context);
        this.isMove = move;
        setLoop(isMove());
    }

    private void defaultInit(Context context) {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        //设置画布背景不为黑色　继承Sureface时这样处理才能透明
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        //背景色
       // setBackgroundColor(Color.parseColor(bgColor));
        //设置透明
//        getBackground().setAlpha(bgalpha);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("surfaceCreated","surfaceCreated holder: "+holder);
        loop = true;
        if(isMove){//滚动效果
            if(orientation == MOVE_LEFT){
                x = getWidth();
            }else{
                if (content != null) {
                    x = -(content.length()*10);
                }
            }
            if ((content != null) && (content.length() > 0)) {
                mThread = new Thread(this);
                mThread.start();
            }
        }else{//不滚动只画一次
            draw();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("surfaceCreated","surfaceDestroyed ");
        loop = false;
        setLoop(false);
    }
    /**
     * 画图
     */
    private void draw(){
        if(mSurfaceHolder == null ||  null == content){
            Log.i("surfaceCreated","draw error ");
            return;
        }

        //锁定画布
        Canvas canvas = mSurfaceHolder.lockCanvas();
        if( canvas == null){
            Log.i("surfaceCreated","draw canvas error ");
            return;
        }

        Paint paint = new Paint();
        //清屏
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //锯齿
        paint.setAntiAlias(true);
        //字体
        paint.setTypeface(Typeface.SANS_SERIF);
        //字体大小
        paint.setTextSize(fontSize);
        //字体颜色
        paint.setColor(Color.parseColor(fontColor));
        //字体透明度
        paint.setAlpha(fontAlpha);
        //画文字
        canvas.drawText(content,x,(getHeight()/2+5), paint);
        //解锁显示
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        //滚动效果
        if(isMove){
            //内容所占像素
            float conlen = paint.measureText(content);
            //组件宽度
            int w = getWidth();
            //方向
            if(orientation == MOVE_LEFT){//向左
                if(x< -conlen){
                    x = w;
                }else{
                 //   x -= 2;
                    x -= 3;
                }
            }else if(orientation == MOVE_RIGHT){//向右
                if(x >= w){
                    x = -conlen;
                }else{
                  //  x+=2;
                    x+=3;
                }
            }
        }
    }
    public void run(){
        while(loop){
            synchronized (mSurfaceHolder) {
                draw();
            }
            try{
                Thread.sleep(speed);
            }catch(InterruptedException ex){
                ex.printStackTrace();
                break;
            }
        }
     //   content = null;
    }
    /******************************set get method***********************************/

    private int getOrientation() {
        return orientation;
    }

    /**
     * @param orientation
     *  <li>可以选择类静态变量</li>
     *  <li>1.MOVE_RIGHT 向右 (默认)</li>
     *  <li>2.MOVE_LEFT  向左</li>
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    private long getSpeed() {
        return speed;
    }

    /**
     * @param speed
     * <li>速度以毫秒计算两次移动之间的时间间隔</li>
     * <li>默认为 1500 毫秒</li>
     */
    public void setSpeed(long speed) {
        this.speed = speed;
    }
    public boolean isMove() {
        return isMove;
    }
    /**
     * @param isMove
     * <see>默认滚动</see>
     */
    public void setMove(boolean isMove) {
        this.isMove = isMove;
    }
    public void setLoop(boolean loop) {
        this.loop = loop;
        if (!loop) {
            if (mThread != null) {
                mThread.interrupt();
                mThread = null;
            }
        }
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
    public void setBgalpha(int bgalpha) {
        this.bgalpha = bgalpha;
    }
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
    public void setFontAlpha(int fontAlpha) {
        this.fontAlpha = fontAlpha;
    }
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }
}
