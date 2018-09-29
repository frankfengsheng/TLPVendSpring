package com.tcn.funcommon.vend.controller;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.SurfaceHolder;

import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;
import com.tcn.funcommon.fileoperation.FileOperation;
import com.tcn.funcommon.media.ImageController;
import com.tcn.funcommon.media.ImageVideoController;
import com.tcn.funcommon.voice.VoiceController;

/**
 * 描述：
 * 作者：Jiancheng,Song on 2016/5/29 02:13
 * 邮箱：m68013@qq.com
 */
public class HandlerThreadCommon extends HandlerThread {
    private static final String TAG = "HandlerThreadCommon";

    private static HandlerCommon m_commonHandler = null;

    private int m_CreatedVideoWidth = 0;
    private int m_CreatedVideoHeight = 0;
    private int m_CreatedImageWidth = 0;
    private int m_CreatedImageHeight = 0;
    private Context m_context = null;
    private SurfaceHolder m_CreatedVideoHolder = null;
    private SurfaceHolder m_CreatedImageHolder = null;
    private WatchThread m_WatchThread = null;


    public HandlerThreadCommon(Context context, String name) {
        super(name);
        m_context = context;
        TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadVend name: "+name);
    }

    public HandlerThreadCommon(Context context, String name, int priority) {
        super(name, priority);
        m_context = context;
        TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadCommon name: "+name+" priority: "+priority);
    }

    @Override
    protected void onLooperPrepared() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadCommon onLooperPrepared()");
        initialize();
        super.onLooperPrepared();
    }

    @Override
    public void run() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadCommon run()");
        super.run();
    }

    @Override
    public boolean quit() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadCommon quit()");
        deInitialize();
        return super.quit();
    }

    private void initialize() {
        m_commonHandler = new HandlerCommon();
        initVideoImageInHandThread();
        OnDecodeScreenBitmap();
        OnDecodeBackgroundBitmap();
        OnDecodeHelpImagePath();
        VoiceController.instance().init(m_context);
        OnTouchSoundLoad();
        startWatchThread();
        ImageController.instance().setHandler(m_commonHandler);
    }

    private void deInitialize() {
        synchronized (this) {
            m_CreatedImageHeight = 0;
            m_CreatedImageWidth = 0;
            m_CreatedImageHolder = null;

            m_CreatedVideoHeight = 0;
            m_CreatedVideoWidth = 0;
            m_CreatedVideoHolder = null;
            OnTouchSoundRelease();
            ImageController.instance().deInit();
            ImageVideoController.instance().deInit();
            VoiceController.instance().deInit();
            stopWatchThread();
            if (m_commonHandler != null) {
                m_commonHandler.removeCallbacksAndMessages(null);
                m_commonHandler = null;
            }
            TcnVendIF.getInstance().LoggerDebug(TAG, "HandlerThreadCommon deInitialize()");
        }
    }

    private void startWatchThread() {
        if ((null == m_WatchThread) || (m_WatchThread.isInterrupted()) || (!m_WatchThread.isAlive())) {
            m_WatchThread = new WatchThread();
            m_WatchThread.setName("WatchThread");
            m_WatchThread.setPriority(Thread.NORM_PRIORITY - 3);
            m_WatchThread.start();
        }

    }

    private void stopWatchThread() {
        if (m_WatchThread != null) {
            m_WatchThread.interrupt();
            m_WatchThread = null;
        }

    }

    private void initVideoImageInHandThread() {
        TcnVendIF.getInstance().LoggerDebug(TAG, "initVideoImageInHandThread ");
        ImageVideoController.instance().init();
        ImageVideoController.instance().setHandler(m_commonHandler);
        setVideoSurfaceCreated(m_CreatedVideoHolder, m_CreatedVideoWidth, m_CreatedVideoHeight);
        setImageSurfaceCreated(m_CreatedImageHolder, m_CreatedImageWidth, m_CreatedImageHeight);
    }

    public void setScreenSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if ((null == m_commonHandler) || (width <= 0) || (height <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "setScreenSurfaceCreated width: " + width + " height: " + height);
            return;
        }
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_IMAGE_SCREEN);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_IMAGE_SCREEN, width, height, holder);
    }

    public void setScreenSurfaceDestroyed(SurfaceHolder holder) {
        if (null == m_commonHandler) {
            TcnVendIF.getInstance().LoggerError(TAG, "setScreenSurfaceDestroyed");
            return;
        }
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_IMAGE_SCREEN);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_IMAGE_SCREEN, -1, -1, holder);
    }

    public void setScreenSurfaceVideoCreated(SurfaceHolder holder, int width,int height) {
        if ((null == m_commonHandler) || (width <= 0) || (height <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "setScreenSurfaceVideoCreated width: " + width + " height: " + height);
            return;
        }
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_VIDEO_SCREEN);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_VIDEO_SCREEN, width, height, holder);
    }

    public void setScreenSurfaceVideoChange(SurfaceHolder holder, int width,int height) {
        if ((null == m_commonHandler) || (width <= 0) || (height <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "setScreenSurfaceVideoChange width: " + width + " height: " + height);
            return;
        }
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_CHANGE_VIDEO_SCREEN);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_CHANGE_VIDEO_SCREEN, width, height, holder);
    }

    public void setScreenSurfaceVideoDestroyed(SurfaceHolder holder) {
        if (null == m_commonHandler) {
            TcnVendIF.getInstance().LoggerError(TAG, "setScreenSurfaceVideoDestroyed");
            return;
        }
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_VIDEO_SCREEN);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_VIDEO_SCREEN, -1, -1, holder);
    }

    public void setImageSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if ((null == m_commonHandler) || (width <= 0) || (height <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "setImageSurfaceCreated m_otherHandler: width: " + width + " height: " + height);
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "setImageSurfaceCreated width: " + width + " height: " + height);
        m_CreatedImageHolder = holder;
        m_CreatedImageWidth = width;
        m_CreatedImageHeight = height;
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_IMAGE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE_IMAGE, width, height, holder);
    }


    public void setVideoSurfaceCreated(SurfaceHolder holder, int width,int height) {
        if ((null == m_commonHandler) || (width <= 0) || (height <= 0)) {
            TcnVendIF.getInstance().LoggerError(TAG, "setVideoSurfaceCreated m_otherHandler: width: " + width + " height: " + height);
            return;
        }
        TcnVendIF.getInstance().LoggerDebug(TAG, "setVideoSurfaceCreated width: " + width + " height: " + height);
        m_CreatedVideoHolder = holder;
        m_CreatedVideoWidth = width;
        m_CreatedVideoHeight = height;
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_CREATE, width, height, holder);
    }

    public void setVideoSurfaceDestroyed(SurfaceHolder holder) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY, -1, -1, holder);
    }

    public void setImageSurfaceDestroyed(SurfaceHolder holder) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_IMAGE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.SURFACE_DESTROY_IMAGE, -1, -1, holder);
    }

    /************************** request start *******************************************/

    public void reqReadMeText() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_README_TEXT);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_README_TEXT, -1, -1, null);
    }

    public void queryImagePathList() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_PATHLIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_PATHLIST, -1, -1, null);
    }

    public void queryImagePathList(int iSearch) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_PATHLIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_PATHLIST, iSearch, -1, null);
    }

    public void queryAdvertPathList() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_ADVERT_PATHLIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_ADVERT_PATHLIST, -1, -1, null);
    }

    public void queryAdvertPathList(int iSearch) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_ADVERT_PATHLIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_ADVERT_PATHLIST, iSearch, -1, null);
    }

    public void reqImageShowPath(int pathType, String fileName) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_SHOW_PATH);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.QUERY_IMAGE_SHOW_PATH, pathType, -1, fileName);
    }

    public void reqImageScreen() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.IMAGE_SCREEN_DECODE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.IMAGE_SCREEN_DECODE, -1, -1, null);
    }

    public void reqImageBackground() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.IMAGE_BACKGROUND_DECODE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.IMAGE_BACKGROUND_DECODE, -1, -1, null);
    }

    public void reqHelpImage() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.IMAGE_HELP_DECODE);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.IMAGE_HELP_DECODE, -1, -1, null);
    }


    public void reqTouchSoundLoad() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TOUCH_SOUND_LOAD);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TOUCH_SOUND_LOAD, -1, -1, null);
    }

    public void reqTouchSoundPlay() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TOUCH_SOUND_PLAY);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TOUCH_SOUND_PLAY, -1, -1, null);
    }

    public void reqPlayGivenFolder() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER, -1, -1, null);
    }

    public void reqPlay() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PLAY, -1, -1, null);
    }

    public void reqPlay(boolean loop,String uri) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        if (loop) {
            TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PLAY, 1, -1, uri);
        } else {
            TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PLAY, -1, -1, uri);
        }
    }

    public void reqNext() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_NEXT);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_NEXT, -1, -1, null);
    }

    public void reqPause() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PAUSE, -1, -1, null);
    }

    public void setForbiddenPlay(boolean forbiddenPlay) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_STOP);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_PLAY_FORBIDDEN, -1, -1, forbiddenPlay);
    }

    public void stopPlayAdvert() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY);
        ImageVideoController.instance().removePlayMessage();
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_STOP, -1, -1, null);
    }

    public void stopPlayStandbyAdvert() {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PLAY_STANDBY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_NEXT_STANDBY);
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.TRK_PAUSE_STANDBY);
        ImageController.instance().removePlayMessage();
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.TRK_STOP_STANDBY, -1, -1, null);
    }

    public void reqVideoAndImageAdvertList(String path) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.CMD_REQ_ADVERT_LIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.CMD_REQ_ADVERT_LIST, -1, -1, path);
    }

    public void reqVideoAndImageStandBytList(String path) {
        TcnUtility.removeMessages(m_commonHandler, TcnVendCMDDef.CMD_REQ_ADVERT_STANDBY_LIST);
        TcnUtility.sendMsg(m_commonHandler, TcnVendCMDDef.CMD_REQ_ADVERT_STANDBY_LIST, -1, -1, path);
    }

    public boolean isPlaying() {
        return ImageVideoController.instance().isPlaying();
    }

    /************************** request end *******************************************/

    private void onSetScreenSurfaceCreated(SurfaceHolder holder, int width,int height) {
        ImageController.instance().onImageSurfaceCreated(holder, width, height);
        //ImageController.instance().onScreenSurfaceCreated(holder, width, height);
    }

    private void onScreenSurfaceDestroyed(SurfaceHolder holder) {
        ImageController.instance().onImageSurfaceDestroyed(holder);
     //   ImageController.instance().onScreenSurfaceDestroyed(holder);
    }

    private void onSetScreenSurfaceVideoCreated(SurfaceHolder holder, int width,int height) {
        ImageController.instance().onVideoSurfaceCreated(holder, width, height);
    }

    private void onScreenVideoSurfaceChanged(SurfaceHolder holder, int width,int height) {
        ImageController.instance().onVideoSurfaceChanged(holder, width, height);
    }

    private void onScreenSurfaceVideoDestroyed(SurfaceHolder holder) {
        ImageController.instance().onVideoSurfaceDestroyed(holder);
    }

    private void onSetVideoSurfaceCreated(SurfaceHolder holder, int width,int height) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "onSetVideoSurfaceCreated width: " + width + " height: " + height);
        ImageVideoController.instance().onVideoSurfaceCreated(holder, width, height);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY, -1, -1, -1, null);
    }

    private void onSetImageSurfaceCreated(SurfaceHolder holder, int width,int height) {
        ImageVideoController.instance().onImageSurfaceCreated(holder, width, height);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY, -1, -1, -1, null);
    }

    private void onVideoSurfaceDestroyed() {
        ImageVideoController.instance().onVideoSurfaceDestroyed(m_CreatedVideoHolder);
    }

    private void onImageSurfaceDestroyed() {
        ImageVideoController.instance().onImageSurfaceDestroyed(m_CreatedImageHolder);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    private void OnDecodeScreenBitmap() {
        if (TcnShareUseData.getInstance().isShowScreenProtect()) {
            ImageController.instance().decodeScreenBitmap();
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.IMAGE_SCREEN, -1, -1, -1, null);
        }
    }

    private void OnDecodeBackgroundBitmap() {
        ImageController.instance().decodeBackgroundBitmap();
        if (TcnShareUseData.getInstance().isFullScreen()) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.IMAGE_BACKGROUND, TcnVendIF.SCREEN_FULL, -1, -1, null);
        } else {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.IMAGE_BACKGROUND, TcnVendIF.SCREEN_HALF, -1, -1, null);
        }

    }

    private void OnDecodeHelpImagePath() {
        ImageController.instance().decodeHelpImagePath();
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.IMAGE_HELP, -1, -1, -1, null);
    }

    private void OnQueryImagePathList(int iSearch) {

        ImageController.instance().queryImagePathList();
        int count = ImageController.instance().getImageGoodsCount();
        if (0 == iSearch) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_IMAGE_PATHLIST, count, iSearch, -1, null);
        } else if (1 == iSearch) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_IMAGE_PATHLIST, count, iSearch, -1, null);
        } else {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_IMAGE_PATHLIST, count, -1, -1, null);
        }
    }

    private void OnQueryAdvertPathList(int iSearch) {

        ImageVideoController.instance().queryAdvertPathList();
        int count = ImageVideoController.instance().getAdvertCount();
        if (0 == iSearch) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ADVERT_PATHLIST, count, iSearch, -1, null);
        } else if (1 == iSearch) {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ADVERT_PATHLIST, count, iSearch, -1, null);
        } else {
            TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_ADVERT_PATHLIST, count, -1, -1, null);
        }
    }

    private void OnQueryImageShowPath(int pathType, String fileName) {
        String mPath = FileOperation.instance().queryFilePathIgnoreCase(fileName, ImageController.instance().queryImageShowList());
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.QUERY_IMAGE_SHOW_PATH, pathType, -1, -1, mPath);
    }

    private void onReadAdText(String textAd) {
        TcnVendIF.getInstance().LoggerDebug(TAG, "onShowAdMedia textAd: " + textAd);
        if ((null == textAd) || (textAd.length() < 1)) {
            return;
        }
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TEXT_AD, -1, -1, -1, textAd);
    }

    private void onReadMeText() {
        String textReadMe = FileOperation.instance().readFile(true,TcnConstant.FOLDER_TEXT,TcnConstant.FILENAME_README);
        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.README_TEXT, -1, -1, -1, textReadMe);
    }

    private void OnTouchSoundLoad() {
        if (TcnShareUseData.getInstance().isVoicePrompt()) {
            //VoiceController.instance().loadSound(m_context);
        }
    }
    private void OnTouchSoundPlay() {
        //VoiceController.instance().playSound();
    }

    private void OnTouchSoundRelease() {
        VoiceController.instance().releaseSound();
    }

    private class HandlerCommon extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TcnVendCMDDef.QUERY_IMAGE_PATHLIST:
                    OnQueryImagePathList(msg.arg1);
                    break;
                case TcnVendCMDDef.QUERY_ADVERT_PATHLIST:
                    OnQueryAdvertPathList(msg.arg1);
                    break;
                case TcnVendCMDDef.SURFACE_CREATE:
                    onSetVideoSurfaceCreated((SurfaceHolder) msg.obj, msg.arg1, msg.arg2);
                    break;

                case TcnVendCMDDef.SURFACE_CHANGED:
                    //OnMySurfaceChanged(msg.arg1, msg.arg2);
                    break;
                case TcnVendCMDDef.SURFACE_DESTROY:
                    onVideoSurfaceDestroyed();
                    break;
                case TcnVendCMDDef.TOUCH_SOUND_LOAD:
                    OnTouchSoundLoad();
                    break;
                case TcnVendCMDDef.TOUCH_SOUND_PLAY:
                    OnTouchSoundPlay();
                    break;
                case TcnVendCMDDef.SURFACE_CREATE_IMAGE_SCREEN:
                    onSetScreenSurfaceCreated((SurfaceHolder) msg.obj, msg.arg1, msg.arg2);
                    break;
                case TcnVendCMDDef.SURFACE_DESTROY_IMAGE_SCREEN:
                    onScreenSurfaceDestroyed((SurfaceHolder) msg.obj);
                    break;
                case TcnVendCMDDef.SURFACE_CREATE_VIDEO_SCREEN:
                    onSetScreenSurfaceVideoCreated((SurfaceHolder) msg.obj, msg.arg1, msg.arg2);
                    break;
                case TcnVendCMDDef.SURFACE_CHANGE_VIDEO_SCREEN:
                    onScreenVideoSurfaceChanged((SurfaceHolder) msg.obj, msg.arg1, msg.arg2);
                    break;
                case TcnVendCMDDef.SURFACE_DESTROY_VIDEO_SCREEN:
                    onScreenSurfaceVideoDestroyed((SurfaceHolder) msg.obj);
                    break;
                case TcnVendCMDDef.SURFACE_CREATE_IMAGE:
                    onSetImageSurfaceCreated((SurfaceHolder) msg.obj, msg.arg1, msg.arg2);
                    break;
                case TcnVendCMDDef.SURFACE_DESTROY_IMAGE:
                    onImageSurfaceDestroyed();
                    break;
                case TcnVendCMDDef.IMAGE_AD_DECODE:
                    break;
                case TcnVendCMDDef.IMAGE_SCREEN_DECODE:
                    OnDecodeScreenBitmap();
                    break;
                case TcnVendCMDDef.IMAGE_BACKGROUND_DECODE:
                    OnDecodeBackgroundBitmap();
                    break;
                case TcnVendCMDDef.IMAGE_HELP_DECODE:
                    OnDecodeHelpImagePath();
                    break;
                case TcnVendCMDDef.TEXT_AD:
                    onReadAdText((String)msg.obj);
                    break;
                case TcnVendCMDDef.QUERY_IMAGE_SHOW_PATH:
                    OnQueryImageShowPath(msg.arg1, (String) msg.obj);
                    break;
                case ImageController.CMD_PLAY_SCREEN_IMAGE:
                    if (TcnShareUseData.getInstance().isFullScreen()) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_SCREEN_IMAGE, TcnVendIF.SCREEN_FULL, -1, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_SCREEN_IMAGE, TcnVendIF.SCREEN_HALF, -1, -1, null);
                    }
                    break;
                case ImageController.CMD_PLAY_SCREEN_VIDEO:
                    if (TcnShareUseData.getInstance().isFullScreen()) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_SCREEN_VIDEO, TcnVendIF.SCREEN_FULL, -1, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_SCREEN_VIDEO, TcnVendIF.SCREEN_HALF, -1, -1, null);
                    }
                    break;
                case ImageVideoController.CMD_PLAY_VIDEO:
                    if (TcnShareUseData.getInstance().isFullScreen()) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_VIDEO, TcnVendIF.SCREEN_FULL, -1, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_VIDEO, TcnVendIF.SCREEN_HALF, -1, -1, null);
                    }
                    break;
                case ImageVideoController.CMD_PLAY_IMAGE:
                    if (TcnShareUseData.getInstance().isFullScreen()) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_IMAGE, TcnVendIF.SCREEN_FULL, -1, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.CMD_PLAY_IMAGE, TcnVendIF.SCREEN_HALF, -1, -1, null);
                    }
                    break;
                case ImageVideoController.PLAY_GIVEN_FOLDER_COMPLETION:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.PLAY_GIVEN_FOLDER_COMPLETION , -1, -1, -1, null);
                    break;
                case ImageVideoController.TRK_PLAY_COMPLETION:
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY_COMPLETION , -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.TRK_PLAY:
                    String playUri = (String)msg.obj;
                    if ((playUri != null) && (playUri.length() > 3)) {
                        if (1 == msg.arg1) {
                            ImageVideoController.instance().play(true,playUri);
                        } else {
                            ImageVideoController.instance().play(false,playUri);
                        }
                    } else {
                        ImageVideoController.instance().play();
                    }
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY, -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.TRK_NEXT:
                    ImageVideoController.instance().onPlayNext();
                    break;
                case TcnVendCMDDef.TRK_PAUSE:
                    ImageVideoController.instance().pause();
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PAUSE, -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.TRK_STOP:
                    ImageVideoController.instance().stopPlayAdvert();
                   // TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_STOP, -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.TRK_PLAY_GIVEN_FOLDER:
                    ImageVideoController.instance().playGivenFolder();
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY, -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.TRK_PLAY_FORBIDDEN:
                    ImageVideoController.instance().setForbiddenPlay((Boolean)msg.obj);
                    if ((Boolean)msg.obj) {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PAUSE, -1, -1, -1, null);
                    } else {
                        TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_PLAY, -1, -1, -1, null);
                    }
                    break;
                case TcnVendCMDDef.CMD_REQ_ADVERT_LIST:
                    ImageVideoController.instance().getVideoAndImagePathList((String)msg.obj);
                    break;
                case TcnVendCMDDef.CMD_REQ_ADVERT_STANDBY_LIST:
                    ImageController.instance().getVideoAndImagePathList((String)msg.obj);
                    break;
                case TcnVendCMDDef.TRK_PLAY_STANDBY:
                    break;
                case TcnVendCMDDef.TRK_NEXT_STANDBY:
                    break;
                case TcnVendCMDDef.TRK_PAUSE_STANDBY:
                    break;
                case TcnVendCMDDef.TRK_STOP_STANDBY:
                    ImageController.instance().stopPlayStandbyAdvert();
                    //TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.TRK_STOP_STANDBY, -1, -1, -1, null);
                    break;
                case TcnVendCMDDef.QUERY_README_TEXT:
                    onReadMeText();
                    break;
                default:
                    break;
            }
        }
    }


    private class WatchThread extends Thread {

        @Override
        public void run() {

            try {
                Thread.sleep(5*60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TcnLog.getInstance().LoggerDebug(TAG, "VendService 进入重启监视线程");
            TcnLog.getInstance().logFileCheck();
            while (!isInterrupted()) {
                String mTime = TcnUtility.getTime(TcnConstant.DATE_FORMAT_HMS);
                int iRebootTime = TcnShareUseData.getInstance().getRebootTime();
                if((iRebootTime > -1) && (Math.abs(Integer.parseInt(mTime) - (iRebootTime * 10000)) <= 100)) {
                    TcnLog.getInstance().setLogChecked(false);
                     TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.PROMPT_INFO, -1, -1, -1, "设备需要重启，30秒之后重启！");
                    try {
                        Thread.sleep(30*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TcnVendIF.getInstance().sendMsgToUI(TcnVendEventID.PROMPT_INFO, -1, -1, -1, "马上重启！");
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    TcnVendIF.getInstance().rebootDevice();
                } else if((Integer.parseInt(mTime) >= 50000) && (Integer.parseInt(mTime) < 50100)) {
                    TcnLog.getInstance().logFileCheck();
                } else {

                }

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            super.run();
        }
    }
}
