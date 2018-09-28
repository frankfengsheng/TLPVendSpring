package com.tcn.funcommon.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tcn.funcommon.R;
import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2016/4/10.
 */
public class ImageController {
    private static final String TAG = "ImageController";
    private static ImageController s_m = null;

    public static final int SCREEN_TYPE_S768X1360        = 1;
    public static final int SCREEN_TYPE_S1360X768	        = 2;
    public static final int SCREEN_TYPE_S768X1366        = 3;
    public static final int SCREEN_TYPE_S1366X768	        = 4;
    public static final int SCREEN_TYPE_S1080X1920		= 5;
    public static final int SCREEN_TYPE_S1920X1080		= 6;
    public static final int SCREEN_TYPE_S600X1024      	= 7;
    public static final int SCREEN_TYPE_S1024X600      	= 8;
    public static final int SCREEN_TYPE_S800X1280      	= 9;
    public static final int SCREEN_TYPE_S1280X800      	= 10;
    public static final int SCREEN_TYPE_S480X800      	= 11;
    public static final int SCREEN_TYPE_S800X480      	= 12;
    public static final int SCREEN_TYPE_S1680X1050      	= 13;
    public static final int SCREEN_TYPE_S1050X1680      	= 14;
    public static final int SCREEN_TYPE_S1280X720      	= 15;
    public static final int SCREEN_TYPE_S720X1280      	= 16;


    public static final int CMD_PLAY_SCREEN_IMAGE		= 20;
    public static final int CMD_PLAY_SCREEN_VIDEO		= 21;

    public static final int TRK_PLAY_COMPLETION_SCREEN		= 65;

    private static final int EVENT_MEDIA_COMPLETION		= 11;
    private static final int EVENT_MEDIA_ERR = 12;

    private static final int EVENT_PLAY_IMAGE_NEXT	   	= 15;


    private volatile int m_iScreenWidth = -1;
    private volatile int m_iScreenHeight = -1;
    private volatile int m_iScreenType = SCREEN_TYPE_S1080X1920;
    private volatile int m_iPlayPos = 0;
    private volatile int m_Screen_Count = 0;
    private volatile int m_iWidthScreen = 0;
    private volatile int m_iHeightScreen = 0;
    private float m_fDensity = 0;

    private boolean m_bIsPlaying = false;
    private boolean m_bIsImageLoadInited = false;
    private boolean m_bIsOriginalShow = false;

            ;
    public volatile String m_ExternalPath = "";
    private volatile String m_strCutPlayFileUrl = "";
    private volatile List<String> m_ListImageHelpPath = null;

    private Context m_context = null;

    private Bitmap m_ScreenBitmap = null;
    private Bitmap m_BackgroundBitmap = null;

    private int m_images_goods_Count = 0;
    private List<String> m_images_goods_pathList = null;
    private List<String> m_ImageShowList = null;
    private List<String> m_Screen_pathList = null;
    private DisplayImageOptions m_options;


    public static synchronized ImageController instance() {
        if (s_m == null)
        {
            s_m = new ImageController();
        }
        return s_m;
    }

    public ImageController() {
        m_ExternalPath = Utils.getExternalStorageDirectory();
    }

    public void init(Context ctx)
    {
        m_context = ctx;
        initDeviceData(ctx);
        initImageLoader();
        if (null == m_MultiPlayer) {
            m_MultiPlayer = new MultiPlayer();
        }
        if (null == m_cEventHandler) {
            m_cEventHandler = new EventHandler();
        }
    }

    public void deInit() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        if (m_cEventHandler != null) {
            m_cEventHandler.removeCallbacksAndMessages(null);
            m_cEventHandler = null;
        }
        if (m_MultiPlayer != null) {
            m_MultiPlayer.deInit();
            m_MultiPlayer = null;
        }
        if ((m_BackgroundBitmap != null) && (!m_BackgroundBitmap.isRecycled())) {
            m_BackgroundBitmap.recycle();
        }
        if ((m_ScreenBitmap != null) && (!m_ScreenBitmap.isRecycled())) {
            m_ScreenBitmap.recycle();
        }
        m_ExternalPath = null;
        m_strCutPlayFileUrl = null;
        m_ListImageHelpPath = null;
        m_context = null;

        m_ScreenBitmap = null;

        m_BackgroundBitmap = null;
        m_images_goods_pathList = null;
        m_ImageShowList = null;
        m_Screen_pathList = null;
    }

    public String getCurrentPlayFileUrl() {
        return m_strCutPlayFileUrl;
    }

    private void initDeviceData(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        m_iScreenWidth = dm.widthPixels;
        m_iScreenHeight = dm.heightPixels;
        m_fDensity = dm.density;
        TcnLog.getInstance().LoggerDebug(TAG, "initDeviceData m_iScreenWidth: "+m_iScreenWidth+" m_iScreenHeight: "+m_iScreenHeight
                +" density: "+dm.density+" densityDpi: "+dm.densityDpi+" dm.xdpi: "+dm.xdpi+" ydpi: "+dm.ydpi);


        if ((768 == m_iScreenWidth) && (1360 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S768X1360;

        } else if ((1360 == m_iScreenWidth) && (768 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S1360X768;

        }  else if ((768 == m_iScreenWidth) && ((1366 >= m_iScreenHeight) && (1300 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S768X1366;
        }  else if ((1366 == m_iScreenWidth) && ((768 >= m_iScreenHeight) && (710 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S1366X768;
        } else if ((1080 == m_iScreenWidth) && ((1920 >= m_iScreenHeight) && (1800 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S1080X1920;

        } else if ((1920 == m_iScreenWidth) && ((1080 >= m_iScreenHeight) && (1000 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S1920X1080;

        } else if ((600 == m_iScreenWidth) && ((1024 >= m_iScreenHeight) && (960 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S600X1024;
        } else if ((1024 == m_iScreenWidth) && ((600 >= m_iScreenHeight) && (540 <= m_iScreenHeight))) {
            m_iScreenType = SCREEN_TYPE_S1024X600;
        } else if ((800 == m_iScreenWidth) && (1280 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S800X1280;
        } else if ((1280 == m_iScreenWidth) && (800 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S1280X800;
        } else if ((480 == m_iScreenWidth) && (800 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S480X800;
        } else if ((800 == m_iScreenWidth) && (480 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S800X480;
        } else if ((1680 == m_iScreenWidth) && (1050 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S1680X1050;
        } else if ((1050 == m_iScreenWidth) && (1680 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S1050X1680;
        } else if ((Math.abs(1280 - m_iScreenWidth) < 100) && (720 == m_iScreenHeight)) {
            m_iScreenType = SCREEN_TYPE_S1280X720;
        } else if ((720 == m_iScreenWidth) && (Math.abs(1280 - m_iScreenHeight) < 100)) {
            m_iScreenType = SCREEN_TYPE_S720X1280;
        }
        else {

        }
    }

    public ImageLoader getImageLoader() {
        if (!m_bIsImageLoadInited) {
            initImageLoader();
        }
       return ImageLoader.getInstance();
    }

    public int  getScreenType() {
        return m_iScreenType;
    }

    public int getScreenWidth() {
        return m_iScreenWidth;
    }

    public int  getScreenHeight() {
        return m_iScreenHeight;
    }

    public int getFitScreenSize(int defaultSize) {

        if (m_fDensity < 1.0f) {
            return defaultSize;
        } else {
            return (int)(defaultSize * (1.0f / m_fDensity));
        }
    }

    public boolean isOriginalShow() {
        return m_bIsOriginalShow;
    }

    public void setOriginalShow(boolean originalShow) {
        m_bIsOriginalShow = originalShow;
    }

    public int getVideoWidth() {
        if (null == m_MultiPlayer) {
            return -1;
        }
        return  m_MultiPlayer.getVideoWidth();
    }

    public int getVideoHeight() {
        if (null == m_MultiPlayer) {
            return -1;
        }
        return  m_MultiPlayer.getVideoHeight();
    }


    public Bitmap getBitmapFromFilePath(String filePath, int width, int height) {
        Bitmap mBmp = null;
        if (null == filePath) {
            return mBmp;
        }
        // 获取根目录
        File f = new File(filePath);
        if (!f.exists() || (!f.isFile())) {
            return mBmp;
        }

        if (width > 0 && height > 0) {
            BitmapFactory.Options opts = new BitmapFactory.Options();  //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
            opts.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(filePath,opts);
            opts.inSampleSize = Utils.calculateInSampleSize(opts, width, height);

            opts.inJustDecodeBounds = false;

            FileInputStream mFileInputStream = null;
            try {
                mFileInputStream = new FileInputStream(filePath);
                if (mFileInputStream != null) {
                    mBmp = BitmapFactory.decodeStream(mFileInputStream, null, opts);
                }

            } catch (IOException e) {
                e.printStackTrace();
                TcnLog.getInstance().LoggerError(TAG, "getBitmapFromFilePath IOException e: "+e);
            } finally {
                if (mFileInputStream != null) {
                    try {
                        mFileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        TcnLog.getInstance().LoggerError(TAG, "getBitmapFromFilePath mFileInputStream e: "+e);
                    }
                }
            }
        }
        return mBmp;
    }

    public void decodeScreenBitmap() {
        if ((SCREEN_TYPE_S768X1366 != m_iScreenType) && (SCREEN_TYPE_S1080X1920 != m_iScreenType)) {
            return;
        }
        if (null == m_ExternalPath) {
            return;
        }
        if (m_ExternalPath.length() < 1) {
            m_ExternalPath = Utils.getExternalStorageDirectory();
        }
        try {
            String filePath = Utils.getFirstImagePathFromUDisk(m_ExternalPath + TcnConstant.FOLDER_IMAGE_SCREEN);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFile(filePath, options);

            if (SCREEN_TYPE_S768X1366 == m_iScreenType) {
                if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 1366);
                } else {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 790);
                }

            } else if (SCREEN_TYPE_S1366X768 == m_iScreenType) {
                options.inSampleSize = 1;
            } else if (SCREEN_TYPE_S1080X1920 == m_iScreenType) {
                if (TcnShareUseData.getInstance().isStandbyImageFullScreen()) {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 1920);
                } else {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 1110);
                }

            } else if (SCREEN_TYPE_S1920X1080 == m_iScreenType) {
                //options.inSampleSize = Utils.calculateInSampleSize(options, 1920, 1020);
                options.inSampleSize = 1;
            } else {
                // options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 440);
                options.inSampleSize = 1;
            }

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            if (m_ScreenBitmap != null) {
                if (!m_ScreenBitmap.isRecycled()) {
                    m_ScreenBitmap.recycle();
                    m_ScreenBitmap = null;
                }
            }
            m_ScreenBitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void decodeBackgroundBitmap() {
        if (null == m_ExternalPath) {
            return;
        }
        if (m_ExternalPath.length() < 1) {
            m_ExternalPath = Utils.getExternalStorageDirectory();
        }
        try {
            String filePath = Utils.getFirstImagePathFromUDisk(m_ExternalPath + TcnConstant.FOLDER_IMAGE_BACKGROUND);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            BitmapFactory.decodeFile(filePath, options);

            if (SCREEN_TYPE_S768X1360 == m_iScreenType) {
                if (TcnShareUseData.getInstance().isFullScreen()) {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 1290);
                } else {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 858);
                }
            } else if (SCREEN_TYPE_S1360X768 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 1360, 698);
            } else if (SCREEN_TYPE_S768X1366 == m_iScreenType) {
                if (TcnShareUseData.getInstance().isFullScreen()) {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 1296);
                } else {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 768, 864);
                }

            } else if (SCREEN_TYPE_S1366X768 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 1366, 698);
            } else if (SCREEN_TYPE_S1080X1920 == m_iScreenType) {
                if (TcnShareUseData.getInstance().isFullScreen()) {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 1820);
                } else {
                    options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 1212);
                }

            } else if (SCREEN_TYPE_S1920X1080 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 1920, 980);
            } else if (SCREEN_TYPE_S600X1024 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 600, 638);
            } else if (SCREEN_TYPE_S1024X600 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 604, 560);
            } else if (SCREEN_TYPE_S800X1280 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 800, 785);
            } else if (SCREEN_TYPE_S1280X800 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 1235, 800);
            } else if (SCREEN_TYPE_S480X800 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 480, 495);
            } else if (SCREEN_TYPE_S800X480 == m_iScreenType) {
                options.inSampleSize = Utils.calculateInSampleSize(options, 800, 445);
            } else {
                options.inSampleSize = 1;
            }

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            if (m_BackgroundBitmap != null) {
                if (!m_BackgroundBitmap.isRecycled()) {
                    m_BackgroundBitmap.recycle();
                    m_BackgroundBitmap = null;
                }
            }
            m_BackgroundBitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decodeScreenBitmapByScale() {
        if ((SCREEN_TYPE_S768X1366 != m_iScreenType) && (SCREEN_TYPE_S1080X1920 != m_iScreenType)) {
            return;
        }
        if (null == m_ExternalPath) {
            return;
        }
        float scale = 970.0f / 1920;
        if (m_ExternalPath.length() < 1) {
            m_ExternalPath = Utils.getExternalStorageDirectory();
        }
        String filePath = Utils.getFirstImagePathFromUDisk(m_ExternalPath + TcnConstant.FOLDER_IMAGE_SCREEN);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        if (SCREEN_TYPE_S768X1366 == m_iScreenType) {
            options.inSampleSize = Utils.calculateInSampleSize(options, 768, (int)(scale*1366));
        } else if (SCREEN_TYPE_S1366X768 == m_iScreenType) {
            options.inSampleSize = 1;
        } else if (SCREEN_TYPE_S1080X1920 == m_iScreenType) {
            options.inSampleSize = Utils.calculateInSampleSize(options, 1080, (int)(scale*1920));
        } else if (SCREEN_TYPE_S1920X1080 == m_iScreenType) {
            //options.inSampleSize = Utils.calculateInSampleSize(options, 1920, 1020);
            options.inSampleSize = 1;
        } else {
            // options.inSampleSize = Utils.calculateInSampleSize(options, 1080, 440);
            options.inSampleSize = 1;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        if (m_ScreenBitmap != null) {
            if (!m_ScreenBitmap.isRecycled()) {
                m_ScreenBitmap.recycle();
                m_ScreenBitmap = null;
            }
        }
        m_ScreenBitmap = BitmapFactory.decodeFile(filePath, options);
    }

    public List<String> decodeHelpImagePath() {
        m_ListImageHelpPath = Utils.getImageWithGifPathFromUDisk(m_ExternalPath + TcnConstant.FOLDER_IMAGE_HELP);
        return m_ListImageHelpPath;
    }

    public List<String> getImageListHelp() {
        return m_ListImageHelpPath;
    }

    public Bitmap getScreenBitmap() {
        return m_ScreenBitmap;
    }

    public Bitmap getBackgroundBitmap() {
        return m_BackgroundBitmap;
    }

    public void queryImagePathList() {
        if ((m_images_goods_pathList != null) && (!m_images_goods_pathList.isEmpty())) {
            m_images_goods_pathList.clear();
        }
        if (null == m_ExternalPath) {
            return;
        }
        if (m_ExternalPath.length() < 1) {
            m_ExternalPath = Utils.getExternalStorageDirectory();
        }
        String filePath = m_ExternalPath + TcnConstant.FOLDER_IMAGE_GOODS;
        File file = new File(filePath);
        if (!file.exists() || (!file.isDirectory())) {
            return;
        }
        m_images_goods_pathList = Utils.getImagePathFromUDisk(filePath);
        if (m_images_goods_pathList != null) {
            m_images_goods_Count = m_images_goods_pathList.size();
        }
    }

    public List<String> getImageGoodsPathList() {
        return m_images_goods_pathList;
    }

    public int getImageGoodsCount() {
        return m_images_goods_Count;
    }

    public List<String> queryImageShowList() {
        if ((m_ImageShowList != null) && (!m_ImageShowList.isEmpty())) {
            m_ImageShowList.clear();
        }
        if (null == m_ExternalPath) {
            return m_ImageShowList;
        }
        if (m_ExternalPath.length() < 1) {
            m_ExternalPath = Utils.getExternalStorageDirectory();
        }
        String filePath = m_ExternalPath + TcnConstant.FOLDER_SHOW;
        File file = new File(filePath);
        if (!file.exists() || (!file.isDirectory())) {
            return null;
        }
        m_ImageShowList = Utils.getImagePathFromUDisk(filePath);
        return m_ImageShowList;
    }
    public List<String> getImageShowList() {
        return m_ImageShowList;
    }

    public DisplayImageOptions getImageOptions(int resId) {

        if (null == m_options) {
            //初始化imageloader
            m_options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(resId)            //加载图片时的图片
                    .showImageForEmptyUri(resId)         //没有图片资源时的默认图片
                    .showImageOnFail(resId)              //加载失败时的图片
                    .cacheInMemory(true)                               //启用内存缓存
                    .cacheOnDisk(true)                                 //启用外存缓存
                    .considerExifParams(true)                          //启用EXIF和JPEG图像格式
                     .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型 add by song
                    .displayer(new RoundedBitmapDisplayer(20))         //设置显示风格这里是圆角矩形
                            //.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间 add by song

                    .build();
        }

        return m_options;
    }

    public void setImageOptions(DisplayImageOptions options) {
        m_options = options;
    }

    public String getImageGoodsPath() {
        return m_ExternalPath+TcnConstant.FOLDER_IMAGE_GOODS;
    }

    private void initImageLoader() {
        m_bIsImageLoadInited = true;
        File cacheDir = StorageUtils.getOwnCacheDirectory(m_context, "imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(m_context)
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                        //.memoryCache(new UsingFreqLimitedMemoryCache(2* 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCache(new WeakMemoryCache()) // add by song
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
//		           .discCacheFileNameGenerator(newMd5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(m_context,5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    public Bitmap getBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (null == myFileUrl) {
            return null;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void removePlayMessage() {
        TcnUtility.removeMessages(m_handler, CMD_PLAY_SCREEN_IMAGE);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_PLAY_IMAGE_NEXT);
        TcnUtility.removeMessages(m_handler, CMD_PLAY_SCREEN_VIDEO);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_MEDIA_COMPLETION);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_MEDIA_ERR);
    }

    public void stopPlayStandbyAdvert() {
        removePlayMessage();
        if (m_MultiPlayer != null) {
            m_MultiPlayer.setSeekPosition(m_MultiPlayer.getCurrentPosition());
            m_MultiPlayer.stop();
        }
    }


    public void onVideoSurfaceCreated(SurfaceHolder holder, int w, int h) {
        TcnLog.getInstance().LoggerDebug(TAG, "-------onVideoSurfaceCreated() ======.............");
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceCreated()");
            return;
        }

        if (holder == m_MultiPlayer.m_VideoSurfaceHolder) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceCreated() is same");
            return;
        }

        TcnLog.getInstance().LoggerDebug(TAG, "-------onVideoSurfaceCreated() ======");
        m_MultiPlayer.onVideoSurfaceCreated(holder, getUri(), w, h);
    }

    public void onVideoSurfaceChanged(SurfaceHolder holder, int w, int h) {
        TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceChanged() w: " + w + " h: " + h);
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceChanged()");
            return;
        }
        if (holder != m_MultiPlayer.m_VideoSurfaceHolder) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceChanged() is not same");
            return;
        }
        m_MultiPlayer.onVideoSurfaceChanged(holder, w, h);
    }

    public void onVideoSurfaceDestroyed(SurfaceHolder holder) {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceDestroyed()");
            return;
        }
        TcnLog.getInstance().LoggerDebug(TAG, "-------onVideoSurfaceDestroyed() ======");
        m_MultiPlayer.onVideoSurfaceDestroyed(holder);
    }

    public void onImageSurfaceCreated(SurfaceHolder holder, int w, int h) {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onImageSurfaceCreated()");
            return;
        }

        if (holder == m_MultiPlayer.m_ImageSurfaceHolder) {
            TcnLog.getInstance().LoggerError(TAG, "onImageSurfaceCreated() is same");
            return;
        }
        TcnLog.getInstance().LoggerDebug(TAG, "-------onImageSurfaceCreated() ======");
        m_MultiPlayer.onImageSurfaceCreated(holder, getUri(), w, h);
    }

    public void onImageSurfaceChanged(int w, int h) {
        TcnLog.getInstance().LoggerError(TAG, "onImageSurfaceChanged() w: " + w + " h: " + h);
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onImageSurfaceChanged()");
            return;
        }
        m_MultiPlayer.onImageSurfaceChanged(w, h);
    }

    public void onImageSurfaceDestroyed(SurfaceHolder holder) {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onImageSurfaceChanged()");
            return;
        }
        TcnLog.getInstance().LoggerDebug(TAG, "-------onImageSurfaceChanged() ======");
        m_MultiPlayer.onImageSurfaceDestroyed(holder);
    }

   /* public void onScreenSurfaceCreated(SurfaceHolder holder, int w, int h) {
        TcnLog.getInstance().LoggerDebug(TAG, "onScreenSurfaceCreated w: " + w+" h: "+h);
        m_ImageSurfaceHolder = holder;
        m_iWidthScreen = w;
        m_iHeightScreen = h;
        if (null == m_DrawThread) {
            m_DrawThread = new DrawThread();
            m_DrawThread.setRun(true);
            m_DrawThread.start();
        }
    }

    public void onScreenSurfaceDestroyed(SurfaceHolder holder) {
        TcnLog.getInstance().LoggerDebug(TAG, "onScreenSurfaceDestroyed");
        m_SurfaceHolderScreen = null;
        if (m_DrawThread != null) {
            m_DrawThread.setRun(false);
            m_DrawThread.isInterrupted();
            m_DrawThread = null;
        }
    }*/

    public void getScreenPathList() {
        String mPath = Utils.getExternalStorageDirectory() + TcnConstant.FOLDER_IMAGE_SCREEN;;
        TcnLog.getInstance().LoggerDebug(TAG, "getScreenPathList mPath: " + mPath);
        File file = new File(mPath);
        if (!file.exists() || (!file.isDirectory())) {
            TcnLog.getInstance().LoggerError(TAG, "getScreenPathList file not exist");
            return;
        }
        m_iPlayPos = 0;
        m_Screen_pathList = Utils.getVideoAndImagePathFromUDisk(mPath);
        if (m_Screen_pathList != null) {
            m_Screen_Count = m_Screen_pathList.size();
        }
    }


    //缩放图片
    private Bitmap getReduceBitmap(Bitmap bitmap ,int w,int h) {
        int width = bitmap.getWidth();
        int hight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float wScake =  ((float)w/width);
        float hScake = ((float)h/hight);
        matrix.postScale(wScake, hScake);
        bitmap = Bitmap.createBitmap(bitmap, 0,0,width,hight,matrix,true);
        return bitmap;
    }

    //画图方法
    private void drawImage(int width, int height, SurfaceHolder holder, String path) {

        if ((holder == null) || (width < 1) || (height < 1) || (null == path) || (path.length() < 1)) {
            TcnLog.getInstance().LoggerError(TAG, "drawImage() holder: "+holder+" path: "+path);
            return;
        }
        Canvas canvas = null;
        Matrix matrix = new Matrix();
        Bitmap bitmap  = null;
        try {
            canvas = holder.lockCanvas();
            bitmap  = getBitmapFromFilePath(path,width,height);

            if(bitmap != null) {
                //生成合适的图像
                bitmap = getReduceBitmap(bitmap,width,height);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                //清屏
                paint.setColor(Color.BLACK);
              //  canvas = holder.lockCanvas();
                canvas.drawRect(new Rect(0, 0, width,height), paint);
                //画图
                canvas.drawBitmap(bitmap, matrix, paint);

            }
            Paint paintText = new Paint();

            //锯齿
            paintText.setAntiAlias(true);
            //字体
            paintText.setTypeface(Typeface.MONOSPACE);
            paintText.setColor(Color.RED);
            //字体颜色
            // paint.setColor(Color.parseColor(fontColor));
            //字体大小
            paintText.setTextSize(40f);
            //画文字
            canvas.drawText(m_context.getString(R.string.ui_click_and_buy),width/2 - 140, height - 15, paintText);

        } catch (Exception ex) {

            TcnLog.getInstance().LoggerError(TAG,"drawImage ex: "+ex.getMessage());
            //资源回收
            if(bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            return;
        } finally {

            if (holder != null) {
                try {
                    //Thread.sleep(1000);//睡眠时间为1秒
                    //解锁显示
                    holder.unlockCanvasAndPost(canvas);
                } catch (Exception e) {
                    TcnLog.getInstance().LoggerError(TAG, "unlockCanvasAndPost e: "+e);
                }

            }

            //资源回收
            if(bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        }
    }

    public String getUri() {
        if ((null == m_Screen_pathList) || (m_Screen_pathList.isEmpty()) || (m_Screen_Count < 1)) {
            getScreenPathList();
        }

        String mUri = null;
        if ((null == m_Screen_pathList) || (m_Screen_pathList.size() <= m_iPlayPos)) {
            TcnLog.getInstance().LoggerError(TAG, "getUri() m_iPlayPos: " + m_iPlayPos);
            return mUri;
        }
        mUri = m_Screen_pathList.get(m_iPlayPos);
        return mUri;
    }



    private void onPlayNext() {
        if (null == m_Screen_pathList) {
            TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_Screen_pathList is null.");
            return;
        }
        if (m_Screen_pathList.size() < 1) {
            TcnLog.getInstance().LoggerError(TAG, "onPlayNext() size() "+m_Screen_pathList.size());
            return;
        }
        if (m_iPlayPos >= m_Screen_pathList.size()) {
            TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_iPlayPos: "+m_iPlayPos+" size(): "+m_Screen_pathList.size());
            m_iPlayPos = 0;
        }
        if (m_iPlayPos < m_Screen_Count-1) {
            m_iPlayPos++;
        } else {
            m_iPlayPos = 0;
        }
        String strUri = m_Screen_pathList.get(m_iPlayPos);
        TcnLog.getInstance().LoggerDebug(TAG, "onPlayNext() m_iPlayPos: " + m_iPlayPos+" strUri: "+strUri);
        m_strCutPlayFileUrl = strUri;
        m_MultiPlayer.setSeekPosition(0);
        m_MultiPlayer.setUri(strUri);

    }

    private Handler m_handler = null;
    public void setHandler(Handler handler) {
        m_handler = handler;
    }

    private DrawThread m_DrawThread = null;
    private class DrawThread extends Thread {

        private boolean mRun = false;

        public void setRun(boolean run) {
            mRun = run;
        }

        @Override
        public void run() {
            super.run();

            //playImage(getUri());

            while (mRun && (!isInterrupted())) {
                try {
                    if (m_Screen_Count <= 1) {
                        break;
                    }
                    try {
                        sleep(TcnShareUseData.getInstance().getImagePlayIntervalTime()*1000);
                    } catch (InterruptedException e) {
                        TcnLog.getInstance().LoggerError(TAG, "DrawThread InterruptedException e: " + e);
                        e.printStackTrace();
                        break;
                    }
                    onPlayNext();
                } catch (Exception e) {
                    TcnLog.getInstance().LoggerError(TAG, "DrawThread Exception e: " + e);
                    e.printStackTrace();
                    break;
                }

            }

        }
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (null != m_MultiPlayer) {
            m_MultiPlayer.setVolume(leftVolume, rightVolume);
        }
    }

    public boolean isPlaying() {
        boolean bRetValue = false;
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "....isPlaying() m_MultiPlayer is null");
            return bRetValue;
        }
        try {
            String mUri = null;
            if (m_Screen_pathList != null) {
                mUri = m_Screen_pathList.get(m_iPlayPos);
            }
            if (Utils.isTcnVideo(mUri)) {
                bRetValue = m_MultiPlayer.isPlaying();
            } else if (Utils.isTcnImage(mUri)) {
                bRetValue = m_bIsPlaying;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bRetValue;
    }

    private void setCanPlay(boolean canPlay) {
       /* if (m_bPlayGivenFolder) {
            if (m_bCanPlay != null) {
                if (m_bCanPlay.length > m_iPlayPos_Given) {
                    m_bCanPlay[m_iPlayPos_Given] = canPlay;
                }
            }

        } else {
            if (m_bCanPlay != null) {
                if (m_bCanPlay.length > m_iPlayPos) {
                    m_bCanPlay[m_iPlayPos] = canPlay;
                }
            }
        }*/
    }

    private boolean isAllPlayFail() {
        boolean bRet = true;
       /* if (null == m_bCanPlay) {
            return bRet;
        }

        for (int i = 0; i < m_bCanPlay.length; i++) {
            if (m_bCanPlay[i]) {
                bRet = false;
                break;
            }
        }*/

        return bRet;
    }

    public void getVideoAndImagePathList(String path) {
        String mPath = Utils.getExternalStorageDirectory() + path;
        TcnLog.getInstance().LoggerDebug(TAG, "getVideoAndImagePathList mPath: " + mPath);
        File file = new File(mPath);
        if (!file.exists() || (!file.isDirectory())) {
            TcnLog.getInstance().LoggerError(TAG, "getVideoAndImagePathList file not exist");
            return;
        }
        m_iPlayPos = 0;
        //m_VideoImages_pathList = Utils.getVideoPathFromUDisk(mPath);
        m_Screen_pathList = Utils.getVideoAndImagePathFromUDisk(mPath);
        if (m_Screen_pathList != null) {
            m_Screen_Count = m_Screen_pathList.size();
            if (m_Screen_Count > 0) {
                String mUri = m_Screen_pathList.get(m_iPlayPos);
                if (m_bIsPlaying) {
                    if (Utils.isTcnVideo(mUri)) {
                        TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_VIDEO, -1, -1, null);
                    } else if (Utils.isTcnImage(mUri)) {
                        TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_IMAGE, -1, -1, null);
                    } else {

                    }
                }
            }
        }
        TcnLog.getInstance().LoggerDebug(TAG, "getVideoAndImagePathList m_Screen_Count: " + m_Screen_Count);
    }

    private MultiPlayer m_MultiPlayer = null;
    private class MultiPlayer {

        private int m_iWidthVideo = 0;
        private int m_iHeightVideo = 0;

        private int m_iWidthImage = 0;
        private int m_iHeightImage = 0;
        // All the stuff we need for playing and showing a video
        private SurfaceHolder m_VideoSurfaceHolder				= null;
        private SurfaceHolder m_ImageSurfaceHolder				= null;
        //        private SurfaceHolder m_VideoSurfaceHolderInMediaPlayer	= null;
//        private SurfaceHolder m_ImageSurfaceHolderInMediaPlayer	= null;
        private MediaPlayer m_MediaPlayer					= null;
        private int         m_SeekWhenPrepared				= 0;  // recording the seek position while preparing

        private boolean m_isPrePlayVideo = false;


        public void onVideoSurfaceCreated(SurfaceHolder holder, String uri, int w, int h) {
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceCreated() uri: "+uri);
            if (null == uri) {
                return;
            }
            if (Utils.isTcnImage(uri)) {
                TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_IMAGE, -1, -1, null);
                return;
            }

            if (!Utils.isTcnVideo(uri)) {
                m_bIsPlaying = false;
                return;
            }

            m_VideoSurfaceHolder = holder;
            m_iWidthVideo = w;
            m_iHeightVideo = h;
            m_isPrePlayVideo = true;
            setUri(uri);
        }

        public void onVideoSurfaceChanged(SurfaceHolder holder,int w, int h) {
           // m_iWidthVideo = w;
          //  m_iHeightVideo = h;
            if (m_MediaPlayer != null) {
            //    m_iWidthVideo = m_MediaPlayer.getVideoWidth();
            //    m_iHeightVideo = m_MediaPlayer.getVideoHeight();
                TcnLog.getInstance().LoggerDebug(TAG, "3 onVideoSurfaceChanged() m_iWidthVideo: "+m_iWidthVideo+" m_iHeightVideo: "+m_iHeightVideo);
              //  holder.setFixedSize(m_iWidthVideo, m_iHeightVideo);
                m_MediaPlayer.setDisplay(holder);
            }

        }

        public void onVideoSurfaceDestroyed(SurfaceHolder holder) {
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceDestroyed()");

            if ((m_MediaPlayer != null) && m_MediaPlayer.getCurrentPosition() > 0) {
                m_SeekWhenPrepared = m_MediaPlayer.getCurrentPosition();
            }
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceDestroyed() m_SeekWhenPrepared: " + m_SeekWhenPrepared);
            TcnUtility.removeMessages(m_handler, CMD_PLAY_SCREEN_VIDEO);
            TcnUtility.removeMessages(m_cEventHandler,EVENT_MEDIA_COMPLETION);
            // after we return from this we can't use the surface any more
            release();
            m_VideoSurfaceHolder = null;
        }

        public void onImageSurfaceCreated(SurfaceHolder holder, String uri, int w, int h) {
            TcnLog.getInstance().LoggerDebug(TAG, "onImageSurfaceCreated() uri: "+uri);
            if (null == uri) {
                return;
            }
            if (Utils.isTcnVideo(uri)) {
                TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_VIDEO, -1, -1, null);
                return;
            }
            if (!Utils.isTcnImage(uri)) {
                m_bIsPlaying = false;
                return;
            }
            m_ImageSurfaceHolder = holder;
            m_iWidthImage = w;
            m_iHeightImage = h;
            m_isPrePlayVideo = false;
            setUri(uri);
        }

        public void onImageSurfaceChanged(int w, int h) {
            m_iWidthImage = w;
            m_iHeightImage = h;
        }

        public void onImageSurfaceDestroyed(SurfaceHolder holder) {
            TcnLog.getInstance().LoggerDebug(TAG, "onImageSurfaceDestroyed()");
            TcnUtility.removeMessages(m_handler, CMD_PLAY_SCREEN_IMAGE);
            TcnUtility.removeMessages(m_cEventHandler,EVENT_PLAY_IMAGE_NEXT);
            m_ImageSurfaceHolder = null;
        }

        public int getVideoWidth() {
            if (m_MediaPlayer != null) {
                m_iWidthVideo = m_MediaPlayer.getVideoWidth();
            }
            return m_iWidthVideo;
        }

        public int getVideoHeight() {
            if (m_MediaPlayer != null) {
                m_iHeightVideo = m_MediaPlayer.getVideoWidth();
            }
            return m_iHeightVideo;
        }


        public void setVideoSurfaceHolder(SurfaceHolder sh) {
            if (sh == m_VideoSurfaceHolder) {
                return;
            }

            m_VideoSurfaceHolder = sh;
        }

        public void setUri(String uri) {
            if (null == uri) {
                TcnLog.getInstance().LoggerError(TAG, "setUri() uri is null");
                return;
            }

            TcnLog.getInstance().LoggerDebug(TAG, "setURI() uri: " + uri+" m_isPrePlayVideo: "+m_isPrePlayVideo);

            if (Utils.isTcnVideo(uri)) {
                if (m_isPrePlayVideo) {
                    openVideo(uri);
                } else {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_VIDEO, -1, -1, null);
                }
            } else {
                m_SeekWhenPrepared = 0;
                if (m_isPrePlayVideo) {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_SCREEN_IMAGE, -1, -1, null);
                } else {
                    showImage(uri);
                }

            }
        }

        private void openVideo(String uri) {
            TcnLog.getInstance().LoggerDebug(TAG, "openVideo() uri: " + uri);
            if (null == uri) {
                return;
            }
            if (!Utils.isTcnVideo(uri)) {
                TcnLog.getInstance().LoggerDebug(TAG, "openVideo() is not video, return.");
                return;
            }

            if (null == m_VideoSurfaceHolder) {
                TcnLog.getInstance().LoggerError(TAG, "openVideo(), m_VideoSurfaceHolder is null, return.");
                return;
            }
            m_bIsPlaying = true;
            release();
            m_strCutPlayFileUrl = uri;
            try {
                m_MediaPlayer = new MediaPlayer();
                m_MediaPlayer.setOnPreparedListener(m_PreparedListener);
                m_MediaPlayer.setOnCompletionListener(m_CompletionListener);
                m_MediaPlayer.setOnErrorListener(m_ErrorListener);
                m_MediaPlayer.setOnSeekCompleteListener(m_SeekCompleteListener);
                //m_MediaPlayer.setOnVideoSizeChangedListener(m_SizeChangedListener);
                //m_MediaPlayer.setOnBufferingUpdateListener(m_BufferingUpdateListener);
                // FileInputStream fio = new FileInputStream(new File(m_Uri));//修改后
                //  m_MediaPlayer.setDataSource(fio.getFD());
                // File file = new File(m_Uri);
                // FileInputStream fis = new FileInputStream(file);
                //m_MediaPlayer.setDataSource(fis.getFD());
                m_MediaPlayer.setDataSource(uri);
                //m_MediaPlayer.setDataSource(m_Context, m_Uri);
                m_MediaPlayer.setDisplay(m_VideoSurfaceHolder);
//                m_VideoSurfaceHolderInMediaPlayer = m_VideoSurfaceHolder;
                m_MediaPlayer.setScreenOnWhilePlaying(true);
                //m_MediaPlayer.prepareAsync();
                // we don't set the target state here either, but preserve the
                // target state that was there before.
                //m_CurrentState = STATE_PREPARING;
                m_MediaPlayer.prepare();

                setCanPlay(true);

            } catch (IOException ex) {
                TcnLog.getInstance().LoggerError(TAG, "openVideo() IOException ex: " + ex);
                setCanPlay(false);
                m_bIsPlaying = false;
                m_strCutPlayFileUrl = "";
                m_ErrorListener.onError(m_MediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
                return;
            } catch (IllegalArgumentException ex) {
                TcnLog.getInstance().LoggerError(TAG, "openVideo() IllegalArgumentException ex: " + ex);
                setCanPlay(false);
                m_bIsPlaying = false;
                m_strCutPlayFileUrl = "";
                m_ErrorListener.onError(m_MediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
                return;
            }
        }

        public void showImage(String uri) {
            TcnLog.getInstance().LoggerDebug(TAG, "showImage() uri: " + uri+" m_Screen_Count: "+m_Screen_Count);
            if (null == uri) {
                return;
            }
            if (!Utils.isTcnImage(uri)) {
                TcnLog.getInstance().LoggerDebug(TAG, "showImage() is not image, return.");
                return;
            }

            if (null == m_ImageSurfaceHolder) {
                TcnLog.getInstance().LoggerError(TAG, "showImage(), m_ImageSurfaceHolder is null, return.");
                return;
            }
            m_strCutPlayFileUrl = uri;
            setCanPlay(true);
            drawImage(m_iWidthImage, m_iHeightImage,m_ImageSurfaceHolder, uri);
            if (m_cEventHandler != null) {
                if (m_Screen_Count > 1) {
                    TcnUtility.sendMsgDelayed(m_cEventHandler, EVENT_PLAY_IMAGE_NEXT, -1, TcnShareUseData.getInstance().getImagePlayIntervalTime()*1000, null);
                }
            }
        }

        public void deInit() {
            if (m_MediaPlayer != null) {
                m_MediaPlayer.setOnPreparedListener(null);
                m_MediaPlayer.setOnCompletionListener(null);
                m_MediaPlayer.setOnErrorListener(null);
                m_MediaPlayer.setOnSeekCompleteListener(null);
                m_MediaPlayer.setDisplay(null);
            }
            m_PreparedListener = null;
            m_CompletionListener = null;
            m_ErrorListener = null;
            m_SeekCompleteListener = null;
            m_VideoSurfaceHolder = null;
            stop();
        }

        /*
         * release the media player in any state
         */
        private void release() {
            if (m_MediaPlayer != null) {
                m_MediaPlayer.stop();
                m_MediaPlayer.reset();
                m_MediaPlayer.release();
                m_MediaPlayer = null;
//                m_VideoSurfaceHolderInMediaPlayer = null;
            }
        }

        public int getCurrentPosition() {
            int iRetValue = 0;
            if (null == m_MediaPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "getCurrentPosition()");
                return iRetValue;
            }
            iRetValue = m_MediaPlayer.getCurrentPosition();
            return iRetValue;
        }

        public void setVolume(float leftVolume, float rightVolume) {
            if (null != m_MediaPlayer) {
                m_MediaPlayer.setVolume(leftVolume, rightVolume);
            }
        }

        public boolean isPlaying() {
            boolean bRetValue = false;
            if (null == m_MediaPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "isPlaying() m_MediaPlayer is null");
                return bRetValue;
            }
            bRetValue = m_MediaPlayer.isPlaying();
            return bRetValue;
        }

        public void play() {
            if (null == m_MediaPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "play() m_MediaPlayer is null");
                return;
            }
            TcnLog.getInstance().LoggerDebug(TAG, "play()");
            m_MediaPlayer.start();
        }

        public void pause() {
            m_bIsPlaying = false;
            if (null == m_MediaPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "pause() m_MediaPlayer is null");
                return;
            }
            TcnLog.getInstance().LoggerDebug(TAG, "pause()");
            if (m_MediaPlayer.isPlaying()) {
                m_MediaPlayer.pause();
            }
        }

        public void seekTo(int iMsec) {
            if (null == m_MediaPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "seekTo()");
                return;
            }
            m_MediaPlayer.seekTo(iMsec);
            m_SeekWhenPrepared = 0;
        }

        public void setSeekPosition(int position) {
            m_SeekWhenPrepared = position;
        }

        public void stop() {
            if (m_MediaPlayer != null) {
                m_MediaPlayer.stop();
                m_MediaPlayer.release();
                m_MediaPlayer = null;
                m_VideoSurfaceHolder = null;
//                m_VideoSurfaceHolderInMediaPlayer = null;
            }
        }

        private Bitmap getBitmapFromFilePath(String filePath, int width, int height) {
            Bitmap mBmp = null;
            if (null == filePath) {
                return mBmp;
            }
            // 获取根目录
            File f = new File(filePath);
            if (!f.exists() || (!f.isFile())) {
                return mBmp;
            }

            if (width > 0 && height > 0) {
                BitmapFactory.Options opts = new BitmapFactory.Options();  //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inPreferredConfig = Bitmap.Config.RGB_565;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
                opts.inJustDecodeBounds = true;

                BitmapFactory.decodeFile(filePath,opts);
                opts.inSampleSize = Utils.calculateInSampleSizeLow(opts, width, height);

                opts.inJustDecodeBounds = false;

                FileInputStream mFileInputStream = null;
                try {
                    mFileInputStream = new FileInputStream(filePath);
                    if (mFileInputStream != null) {
                        long available = mFileInputStream.available();
                        if((available < 1*1048576)) {   //1M
                            mBmp = BitmapFactory.decodeStream(mFileInputStream, null, opts);
                        } else {
                            TcnLog.getInstance().LoggerDebug(TAG, "getBitmapFromFilePath available: "+available);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    TcnLog.getInstance().LoggerError(TAG, "getBitmapFromFilePath IOException e: "+e);
                } finally {
                    if (mFileInputStream != null) {
                        try {
                            mFileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            TcnLog.getInstance().LoggerError(TAG, "getBitmapFromFilePath mFileInputStream e: "+e);
                        }
                    }
                }
            }
            return mBmp;
        }

        private void playImage(String uri) {
            TcnLog.getInstance().LoggerDebug(TAG, "playImage() uri: " + uri);
            if (null == uri) {
                return;
            }

            if (!Utils.isTcnImage(uri)) {
                TcnLog.getInstance().LoggerDebug(TAG, "playImage() is not image, return.");
                return;
            }

            if (null == m_ImageSurfaceHolder) {
                TcnLog.getInstance().LoggerError(TAG, "playImage() m_SurfaceHolderScreen is null, return.");
                return;
            }

            drawImage(m_iWidthScreen, m_iHeightScreen,m_ImageSurfaceHolder, uri);

//        if (m_cEventHandler != null) {
//            if (m_Screen_Count > 1) {
//                TcnUtility.sendMsgDelayed(m_cEventHandler, EVENT_PLAY_NEXT, -1, TcnShareUseData.getInstance().getImagePlayIntervalTime()*1000, null);
//            }
//        }
        }

        //画图方法
        private void drawImage(int width, int height, SurfaceHolder holder, String path) {
            if ((holder == null) || (width < 1) || (height < 1) || (null == path) || (path.length() < 1)) {
                TcnLog.getInstance().LoggerError(TAG, "drawImage() holder: "+holder+" path: "+path);
                return;
            }
            Canvas canvas = null;
            Matrix matrix = new Matrix();
            Bitmap bitmap  = null;
            try {
                m_bIsPlaying = true;
                bitmap  = getBitmapFromFilePath(path,width,height);
                if(bitmap != null) {

                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL);
                    //清屏
                    paint.setColor(Color.BLACK);
                    canvas = holder.lockCanvas();

                    if (m_bIsOriginalShow) {
                        int xPoint = (width - bitmap.getWidth()) / 2;
                        int yPoint = (height - bitmap.getHeight()) / 2;

                        int xEnd = (bitmap.getWidth());
                        int yEnd = (bitmap.getHeight());

                        canvas.translate(xPoint, yPoint);

                        canvas.drawRect(new RectF(xPoint, yPoint, xEnd,yEnd), paint);

                    } else {
                        if ((bitmap.getWidth() != width) || (bitmap.getHeight() != height)) {
                            //生成合适的图像
                            bitmap = getReduceBitmap(bitmap,width,height);
                        }
                        canvas.drawRect(new RectF(0, 0, width,height), paint);
                    }

                    //画图
                    canvas.drawBitmap(bitmap, matrix, paint);
                }
            } catch (OutOfMemoryError ex) {
                TcnLog.getInstance().LoggerError(TAG,"OutOfMemoryError ex: "+ex.getMessage());
                if(bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                m_bIsPlaying = false;
            } catch (Exception ex) {
                TcnLog.getInstance().LoggerError(TAG,"drawImage ex: "+ex.getMessage());
                //资源回收
                if(bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                m_bIsPlaying = false;
            } finally {
                if (holder != null) {
                    try {
                        //Thread.sleep(1000);//睡眠时间为1秒
                        //解锁显示
                        holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        TcnLog.getInstance().LoggerError(TAG, "unlockCanvasAndPost e: "+e);
                    }

                }

                //资源回收
                if(bitmap != null) {
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            }
        }

        private MediaPlayer.OnPreparedListener m_PreparedListener =
                new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        TcnLog.getInstance().LoggerDebug(TAG, "OnPreparedListener() m_SeekWhenPrepared: " + m_SeekWhenPrepared);
                        // m_SeekWhenPrepared may be changed after seekTo() call
                        if (m_SeekWhenPrepared != 0) {
                            seekTo(m_SeekWhenPrepared);
                        }
                        play();
                        //TcnUtility.sendEmptyMsg(m_cEventHandler, EVENT_MEDIA_PREPARED);
                    }
                };

        private MediaPlayer.OnCompletionListener m_CompletionListener =
                new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        TcnLog.getInstance().LoggerDebug(TAG, "OnCompletionListener() m_iPlayPos: "+m_iPlayPos);
                        TcnUtility.sendEmptyMsg(m_cEventHandler, EVENT_MEDIA_COMPLETION);
                    }
                };

        private MediaPlayer.OnErrorListener m_ErrorListener =
                new MediaPlayer.OnErrorListener() {

                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        TcnLog.getInstance().LoggerDebug(TAG, "OnErrorListener()");
                        TcnUtility.sendEmptyMsg(m_cEventHandler, EVENT_MEDIA_ERR);
                        return true;
                    }
                };

        private MediaPlayer.OnSeekCompleteListener m_SeekCompleteListener =
                new MediaPlayer.OnSeekCompleteListener() {

                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        TcnLog.getInstance().LoggerDebug(TAG, "OnSeekCompleteListener()");
                        //TcnUtility.sendEmptyMsg(m_cEventHandler, EVENT_MEDIA_SEEK_COMPLETE);
                    }
                };
    }

    private EventHandler m_cEventHandler		= null;
    private class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_MEDIA_COMPLETION:
                    onPlayNext();
                   // TcnUtility.sendMsg(m_handler,TRK_PLAY_COMPLETION_SCREEN , -1, -1, null);
                    break;
                case EVENT_PLAY_IMAGE_NEXT:
                    onPlayNext();
                    break;
                case EVENT_MEDIA_ERR:
                    if (!isAllPlayFail()) {
                        onPlayNext();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
