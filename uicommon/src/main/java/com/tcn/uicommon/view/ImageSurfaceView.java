package com.tcn.uicommon.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/4/12.
 */
public class ImageSurfaceView extends SurfaceView {

    //获取画布
    private SurfaceHolder mSurfaceHolder = null;
    //图片索引
    private int mCount = 0;

    private static Matrix matrix = new Matrix();

    public ImageSurfaceView(Context context) {
        super(context);
        init();
    }
    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ImageSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
    }

    //缩放图片
    private Bitmap getReduceBitmap(Bitmap bitmap ,int w,int h) {
        int width = bitmap.getWidth();
        int hight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float wScake =  ((float)w/width);
        float hScake = ((float)h/hight);
        matrix.postScale(wScake, hScake);
        return Bitmap.createBitmap(bitmap, 0,0,width,hight,matrix,true);
    }

    //画图方法
    public void drawImage(int width, int height, String path) {

        Canvas canvas = mSurfaceHolder.lockCanvas();
        if(canvas == null || mSurfaceHolder == null) {
            return;
        }
        Matrix matrix = new Matrix();
        Bitmap bitmap  = null;
        FileInputStream mFileInputStream = null;
        try {
            mFileInputStream = new FileInputStream(path);
            bitmap  = BitmapFactory.decodeStream(mFileInputStream);
            if(bitmap != null) {
                //生成合适的图像
                bitmap = getReduceBitmap(bitmap,width,height);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                //清屏
                paint.setColor(Color.BLACK);
                canvas.drawRect(new Rect(0, 0, width,height), paint);
                //画图
                canvas.drawBitmap(bitmap, matrix, paint);
            }
            //解锁显示
            mSurfaceHolder.unlockCanvasAndPost(canvas);
        } catch (Exception ex) {
            Log.e("ImageSurfaceView", ex.getMessage());
            return;
        } finally {
            if (mFileInputStream != null) {
                try {
                    mFileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //资源回收
            if(bitmap!=null) {
                bitmap.recycle();
            }
        }
    }

    private class drawThread extends Thread {

        @Override
        public void run() {
            super.run();
        }
    }
}
