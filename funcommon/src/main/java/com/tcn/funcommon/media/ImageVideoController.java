package com.tcn.funcommon.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.tcn.funcommon.TcnConstant;
import com.tcn.funcommon.TcnLog;
import com.tcn.funcommon.TcnShareUseData;
import com.tcn.funcommon.TcnUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by songjiancheng on 2016/4/12.
 */
public class ImageVideoController {
    private static final String TAG = "ImageVideoController";

    private static final int SURFACE_VIDEO_CREATE		= 1;
    private static final int SURFACE_VIDEO_CHANGED		= 2;
    private static final int SURFACE_VIDEO_DESTROY		= 3;
    private static final int SURFACE_IMAGE_CREATE		= 4;
    private static final int SURFACE_IMAGE_CHANGED		= 5;
    private static final int SURFACE_IMAGE_DESTROY		= 6;
    private static final int TRK_NEXT		= 60;
    public static final int TRK_PLAY		= 61;
    public static final int TRK_PAUSE		= 62;
    public static final int TRK_STOP		= 63;
    public static final int PLAY_GIVEN_FOLDER_COMPLETION	= 64;

    public static final int TRK_PLAY_COMPLETION		= 65;

    private static final int EVENT_MEDIA_COMPLETION		= 11;
    private static final int EVENT_MEDIA_ERR = 12;

    private static final int EVENT_PLAY_IMAGE_NEXT	   	= 15;

    public static final int CMD_PLAY_IMAGE		= 16;
    public static final int CMD_PLAY_VIDEO		= 17;

    private static ImageVideoController s_m = null;

    private boolean m_bPlayGivenFolder = false;
    private boolean m_bCanUseGivenFolder = false;
    private boolean[] m_bCanPlay = null;
    private boolean m_bIsPlaying = false;
    private boolean m_bLoopPlayback = false;
    private boolean m_bIsOriginalShow = false;

    private int m_VideoImages_Count = 0;
    private int m_VideoImages_Count_Given = 0;
    private int m_iPlayPos = 0;
    private int m_iPlayPos_Given = 0;
    private String m_strAdvertPath = TcnConstant.FOLDER_VIDEO_IMAGE_AD;
    private volatile String m_strCutPlayFileUrl = "";
    private List<String> m_VideoImages_pathList_given = null;
    private List<String> m_VideoImages_pathList = null;
    private MultiPlayer m_MultiPlayer = null;
    private EventHandler m_cEventHandler		= null;



    public static synchronized ImageVideoController instance() {
        if (s_m == null)
        {
            s_m = new ImageVideoController();
        }
        return s_m;
    }

    public ImageVideoController() {

    }

    public void setPlaybackLoop(boolean loop) {
        m_bLoopPlayback = loop;
    }

    public String getCurrentPlayFileUrl() {
        return m_strCutPlayFileUrl;
    }

    public void setCanUseGivenFolder(boolean canUseGivenFolder) {
        m_bCanUseGivenFolder = canUseGivenFolder;
    }

    public boolean isPlayGivenFolder() {
        return m_bPlayGivenFolder;
    }


    public void queryAdvertPathList() {
        if ((m_VideoImages_pathList != null) && (!m_VideoImages_pathList.isEmpty())) {
            m_VideoImages_pathList.clear();
        }

        String mExternalPath = Utils.getExternalStorageDirectory();
        if (null == mExternalPath) {
            return;
        }
        String folderPath = mExternalPath + m_strAdvertPath;
        File file = new File(folderPath);
        if (!file.exists() || (!file.isDirectory())) {
            return;
        }
        m_VideoImages_pathList = Utils.getVideoAndImagePathFromUDisk(folderPath);
        if (m_VideoImages_pathList != null) {
            m_VideoImages_Count = m_VideoImages_pathList.size();
        }
    }

    public void play() {//
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "play() m_MultiPlayer is null.");
            return;
        }

        m_MultiPlayer.play();
    }

    public void play(boolean loop, String uri) {
        if (null == m_MultiPlayer) {
            return;
        }
        if ((null == m_VideoImages_pathList) || (m_VideoImages_pathList.isEmpty()) || (m_VideoImages_Count < 1)) {
            m_VideoImages_pathList = Utils.getVideoAndImagePathFromUDisk(m_strAdvertPath);
            if ((m_VideoImages_pathList != null) && (m_VideoImages_pathList.size() > 0)) {
                m_VideoImages_Count = m_VideoImages_pathList.size();
                m_bCanPlay = new boolean[m_VideoImages_Count];
            } else {
                return;
            }
        }
        int iPlayPos = getContainsPostion(uri);
        if (iPlayPos >= 0) {
            m_bLoopPlayback = true;
            m_strCutPlayFileUrl = uri;
            m_iPlayPos = iPlayPos;
            m_MultiPlayer.setSeekPosition(0);
            m_MultiPlayer.setUri(uri);
        }
    }

    public void pause() {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "pausePlay() m_MultiPlayer is null.");
            return;
        }

        m_MultiPlayer.pause();
    }

    public void playGivenFolder() {

        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "setPlayGivenFolder() m_MultiPlayer is null.");
            return;
        }

        if (null == m_VideoImages_pathList_given) {
            TcnLog.getInstance().LoggerError(TAG, "setPlayGivenFolder() m_VideoImages_pathList_given is null.");
            return;
        }

        if (m_VideoImages_pathList_given.size() < 1) {
            TcnLog.getInstance().LoggerError(TAG, "setPlayGivenFolder() size is 0");
            return;
        }

        m_bPlayGivenFolder = true;
        m_iPlayPos_Given = 0;
        m_MultiPlayer.setSeekPosition(0);
        m_MultiPlayer.setUri(m_VideoImages_pathList_given.get(m_iPlayPos_Given));
    }

    public void setForbiddenPlay(boolean forbiddenPlay) {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "setForbiddenPlay() m_MultiPlayer is null.");
            return;
        }
        m_MultiPlayer.setForbiddenPlay(forbiddenPlay);
    }

    public boolean isForbiddenPlay() {
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "isForbiddenPlay() m_MultiPlayer is null.");
            return false;
        }
        return m_MultiPlayer.isForbiddenPlay();
    }

    private int getContainsPostion(String uri) {
        int iRet = -1;
        if ((null == uri) || (null == m_VideoImages_pathList) || (m_VideoImages_pathList.isEmpty())) {
            return iRet;
        }
        int indexF = uri.indexOf("TcnFolder");
        if (indexF < 0) {
            return iRet;
        }
        String strUri = uri.substring(indexF);
        for (int i = 0; i < m_VideoImages_pathList.size(); i++) {
            if ((m_VideoImages_pathList.get(i)).contains(strUri)) {
                iRet = i;
                break;
            }
        }
        return iRet;
    }

    public void init() {
        setVideoAndImagePathList();
        if (null == m_MultiPlayer) {
            m_MultiPlayer = new MultiPlayer();
        }
        if (null == m_cEventHandler) {
            m_cEventHandler = new EventHandler();
        }

    }

    public void deInit() {
        if (m_MultiPlayer != null) {
            m_MultiPlayer.deInit();
            m_MultiPlayer = null;
        }
        if (m_VideoImages_pathList_given != null) {
            m_VideoImages_pathList_given.clear();
            m_VideoImages_pathList_given = null;
        }
        if (m_VideoImages_pathList != null) {
            m_VideoImages_pathList.clear();
            m_VideoImages_pathList = null;
        }
        if (m_cEventHandler != null) {
            m_cEventHandler.removeCallbacksAndMessages(null);
            m_cEventHandler = null;
        }
        m_bIsPlaying = false;
        m_VideoImages_Count = 0;
        m_VideoImages_Count_Given = 0;
        m_iPlayPos = 0;
        m_iPlayPos_Given = 0;
        m_MultiPlayer = null;
        m_strCutPlayFileUrl = null;
    }

    public String getAdvertPath() {
        return Utils.getExternalStorageDirectory() + m_strAdvertPath;
    }

    public List<String> getAdvertPathList() {
        return m_VideoImages_pathList;
    }

    public int getAdvertCount() {
        return m_VideoImages_Count;
    }

    private class EventHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_MEDIA_COMPLETION:
                    if (m_bPlayGivenFolder) {
                        if (m_iPlayPos_Given >= m_VideoImages_Count_Given-1) {
                            if (m_handler != null) {
                                m_handler.sendEmptyMessage(PLAY_GIVEN_FOLDER_COMPLETION);
                            }
                        } else {
                            onPlayNext();
                        }
                    } else {
                        onPlayNext();
                    }
                    TcnUtility.sendMsg(m_handler,TRK_PLAY_COMPLETION , -1, -1, null);
                    break;
                case EVENT_PLAY_IMAGE_NEXT:
                    if (m_bPlayGivenFolder) {
                        if (m_iPlayPos_Given >= m_VideoImages_Count_Given-1) {
                            if (m_handler != null) {
                                m_handler.sendEmptyMessage(PLAY_GIVEN_FOLDER_COMPLETION);
                            }
                        } else {
                            onPlayNext();
                        }
                    } else {
                        onPlayNext();
                    }
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

    private void getVideoAndImagePathGivenList() {
        String mPathGiven = Utils.getExternalStorageDirectory() + TcnConstant.FOLDER_VIDEO_IMAGE_AD_GIVEN;
        TcnLog.getInstance().LoggerDebug(TAG, "setVideoAndImagePathGivenList mPathGiven: " + mPathGiven);
        File file = new File(mPathGiven);
        if (!file.exists() || (!file.isDirectory())) {
            TcnLog.getInstance().LoggerError(TAG, "setVideoAndImagePathGivenList file not exist");
            return;
        }
        m_iPlayPos_Given = 0;
        //m_VideoImages_pathList = Utils.getVideoPathFromUDisk(mPath);
        m_VideoImages_pathList_given = Utils.getVideoAndImagePathFromUDisk(mPathGiven);
        if (m_VideoImages_pathList_given != null) {
            m_VideoImages_Count_Given = m_VideoImages_pathList_given.size();
            m_bCanPlay = new boolean[m_VideoImages_Count_Given];
        }
        if (m_bPlayGivenFolder) {
            if (m_MultiPlayer != null) {
                m_MultiPlayer.setSeekPosition(0);
            }
        }

        TcnLog.getInstance().LoggerDebug(TAG, "setVideoAndImagePathGivenList m_VideoImages_Count_Given: " + m_VideoImages_Count_Given);
    }

    public void getVideoAndImagePathList(String path) {
        m_strAdvertPath = path;
        String mPath = Utils.getExternalStorageDirectory() + path;
        TcnLog.getInstance().LoggerDebug(TAG, "getVideoAndImagePathList mPath: " + mPath);
        File file = new File(mPath);
        if (!file.exists() || (!file.isDirectory())) {
            TcnLog.getInstance().LoggerError(TAG, "getVideoAndImagePathList file not exist");
            return;
        }
        m_iPlayPos = 0;
        //m_VideoImages_pathList = Utils.getVideoPathFromUDisk(mPath);
        m_VideoImages_pathList = Utils.getVideoAndImagePathFromUDisk(mPath);
        if (m_VideoImages_pathList != null) {
            m_VideoImages_Count = m_VideoImages_pathList.size();
            m_bCanPlay = new boolean[m_VideoImages_Count];
        }
        if (!m_bPlayGivenFolder) {
            if (m_MultiPlayer != null) {
                m_MultiPlayer.setSeekPosition(0);
            }
        }

        if (m_VideoImages_pathList != null) {
            if (m_VideoImages_pathList.size() > 0) {
                String mUri = m_VideoImages_pathList.get(m_iPlayPos);
                m_bIsPlaying = false;
                if (Utils.isTcnVideo(mUri)) {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_VIDEO, -1, -1, null);
                } else if (Utils.isTcnImage(mUri)) {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_IMAGE, -1, -1, null);
                } else {

                }
            }
        }
        TcnLog.getInstance().LoggerDebug(TAG, "getVideoAndImagePathList m_VideoImages_Count: " + m_VideoImages_Count);
    }

    private void setVideoAndImagePathList() {

        if (m_bCanUseGivenFolder) {
            if ((null == m_VideoImages_pathList_given) || (m_VideoImages_pathList_given.isEmpty()) || (m_VideoImages_Count_Given < 1)) {
                getVideoAndImagePathGivenList();
            }
        }

        if ((null == m_VideoImages_pathList) || (m_VideoImages_pathList.isEmpty()) || (m_VideoImages_Count < 1)) {
            getVideoAndImagePathList(m_strAdvertPath);
        }
    }

    public void removePlayMessage() {
        TcnUtility.removeMessages(m_handler, CMD_PLAY_IMAGE);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_PLAY_IMAGE_NEXT);
        TcnUtility.removeMessages(m_handler, CMD_PLAY_VIDEO);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_MEDIA_COMPLETION);
        TcnUtility.removeMessages(m_cEventHandler,EVENT_MEDIA_ERR);
    }

    public void stopPlayAdvert() {
        removePlayMessage();
        if (m_MultiPlayer != null) {
            m_MultiPlayer.setSeekPosition(m_MultiPlayer.getCurrentPosition());
            m_MultiPlayer.stop();
        }
    }

    public void onVideoSurfaceCreated(SurfaceHolder holder, int w, int h) {
        Log.i(TAG, "-------onVideoSurfaceCreated() ======.............");
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

    public void onVideoSurfaceChanged(int w, int h) {
        TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceChanged() w: " + w + " h: " + h);
        if (null == m_MultiPlayer) {
            TcnLog.getInstance().LoggerError(TAG, "onVideoSurfaceChanged()");
            return;
        }
        m_MultiPlayer.onVideoSurfaceChanged(w, h);
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

    public void onPlayNext() {
        synchronized (this) {
            if (null == m_MultiPlayer) {
                TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_MultiPlayer is null, return.");
                return;
            }
            String strUri = null;
            if (m_bPlayGivenFolder) {
                if (null == m_VideoImages_pathList_given) {
                    TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_VideoImages_pathList_given is null.");
                    return;
                }
                if (m_iPlayPos_Given >= m_VideoImages_pathList_given.size()) {
                    TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_iPlayPos_Given: "+m_iPlayPos_Given+" m_VideoImages_pathList.size(): "+m_VideoImages_pathList.size());
                    return;
                }
                if (m_iPlayPos_Given < m_VideoImages_Count_Given-1) {
                    m_iPlayPos_Given++;
                    strUri = m_VideoImages_pathList_given.get(m_iPlayPos_Given);
                } else {
                    //播放完成之后，播放另一个目录
                    m_bPlayGivenFolder = false;
                    m_iPlayPos = 0;
                    if (m_VideoImages_pathList != null) {
                        strUri = m_VideoImages_pathList.get(m_iPlayPos);
                    }
                }
                TcnLog.getInstance().LoggerDebug(TAG, "onPlayNext() m_iPlayPos_Given: " + m_iPlayPos_Given+" m_VideoImages_Count_Given: "+m_VideoImages_Count_Given);
            } else {
                if (null == m_VideoImages_pathList) {
                    TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_VideoImages_pathList is null.");
                    return;
                }
                if (m_iPlayPos >= m_VideoImages_pathList.size()) {
                    TcnLog.getInstance().LoggerError(TAG, "onPlayNext() m_iPlayPos: "+m_iPlayPos+" m_VideoImages_pathList.size(): "+m_VideoImages_pathList.size());
                    return;
                }
                if (!m_bLoopPlayback) {
                    if (m_iPlayPos < m_VideoImages_Count-1) {
                        m_iPlayPos++;
                    } else {
                        m_iPlayPos = 0;
                    }
                }
                strUri = m_VideoImages_pathList.get(m_iPlayPos);
                TcnLog.getInstance().LoggerDebug(TAG, "onPlayNext() m_iPlayPos: " + m_iPlayPos);
            }
            m_strCutPlayFileUrl = strUri;
            m_MultiPlayer.setSeekPosition(0);
            m_MultiPlayer.setUri(strUri);
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
        return Bitmap.createBitmap(bitmap, 0,0,width,hight,matrix,true);
    }

    private class DrawImageThread extends Thread {
        private SurfaceHolder mHolder;
        public String mPath = null;

        public  DrawImageThread(SurfaceHolder holder, String path) {
            mHolder = holder;
            mPath = path;
        }

        @Override
        public void run() {

            super.run();
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
            if (m_bPlayGivenFolder) {
                if (m_VideoImages_pathList_given != null) {
                    mUri = m_VideoImages_pathList_given.get(m_iPlayPos_Given);
                }

            } else {
                if (m_VideoImages_pathList != null) {
                    mUri = m_VideoImages_pathList.get(m_iPlayPos);
                }
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

    public String getUri() {
        String mUri = null;
        setVideoAndImagePathList();
        if (m_bPlayGivenFolder) {
            if ((null == m_VideoImages_pathList_given) || (m_VideoImages_pathList_given.size() <= m_iPlayPos_Given)) {
                TcnLog.getInstance().LoggerError(TAG, "getUri() m_VideoImages_pathList_given: " + m_VideoImages_pathList_given + " m_iPlayPos_Given: " + m_iPlayPos_Given);
                return mUri;
            }
            mUri = m_VideoImages_pathList_given.get(m_iPlayPos_Given);
        } else {
            if ((null == m_VideoImages_pathList) || (m_VideoImages_pathList.size() <= m_iPlayPos)) {
                TcnLog.getInstance().LoggerError(TAG, "getUri() m_VideoImages_pathList: " + m_VideoImages_pathList + " m_iPlayPos: " + m_iPlayPos);
                return mUri;
            }
            mUri = m_VideoImages_pathList.get(m_iPlayPos);
        }

        return mUri;
    }

    private void setCanPlay(boolean canPlay) {
        if (m_bPlayGivenFolder) {
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
        }
    }

    private boolean isAllPlayFail() {
        boolean bRet = true;
        if (null == m_bCanPlay) {
            return bRet;
        }

        for (int i = 0; i < m_bCanPlay.length; i++) {
            if (m_bCanPlay[i]) {
                bRet = false;
                break;
            }
        }

        return bRet;
    }

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
        private boolean m_bForbiddenPlay = false;


        public void onVideoSurfaceCreated(SurfaceHolder holder, String uri, int w, int h) {
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceCreated() uri: "+uri);
            if (null == uri) {
                return;
            }
            if (Utils.isTcnImage(uri)) {
                TcnUtility.sendMsg(m_handler, CMD_PLAY_IMAGE, -1, -1, null);
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

        public void onVideoSurfaceChanged(int w, int h) {
            m_iWidthVideo = w;
            m_iHeightVideo = h;
        }

        public void onVideoSurfaceDestroyed(SurfaceHolder holder) {
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceDestroyed()");

            if ((m_MediaPlayer != null) && m_MediaPlayer.getCurrentPosition() > 0) {
                m_SeekWhenPrepared = m_MediaPlayer.getCurrentPosition();
            }
            TcnLog.getInstance().LoggerDebug(TAG, "onVideoSurfaceDestroyed() m_SeekWhenPrepared: " + m_SeekWhenPrepared);
            TcnUtility.removeMessages(m_handler, CMD_PLAY_VIDEO);
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
                TcnUtility.sendMsg(m_handler, CMD_PLAY_VIDEO, -1, -1, null);
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
            TcnUtility.removeMessages(m_handler, CMD_PLAY_IMAGE);
            TcnUtility.removeMessages(m_cEventHandler,EVENT_PLAY_IMAGE_NEXT);
            m_ImageSurfaceHolder = null;
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
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_VIDEO, -1, -1, null);
                }
            } else {
                m_SeekWhenPrepared = 0;
                if (m_isPrePlayVideo) {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_IMAGE, -1, -1, null);
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
            TcnLog.getInstance().LoggerDebug(TAG, "showImage() uri: " + uri);
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

            if (m_bForbiddenPlay) {
                m_bIsPlaying = false;
                TcnLog.getInstance().LoggerDebug(TAG, "showImage() ForbiddenPlay");
                return;
            }
            m_strCutPlayFileUrl = uri;
            setCanPlay(true);
           // m_ImageSurfaceHolderInMediaPlayer = m_ImageSurfaceHolder;
            drawImage(m_iWidthImage, m_iHeightImage,m_ImageSurfaceHolder, uri);
            if (m_cEventHandler != null) {
                if (m_VideoImages_Count > 1) {
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

        public void setForbiddenPlay(boolean forbiddenPlay) {
            m_bForbiddenPlay = forbiddenPlay;
            if (forbiddenPlay) {
                if (Utils.isTcnVideo(getUri())) {
                    m_bIsPlaying = false;
                } else {
                    if (isPlaying()) {
                        pause();
                    }
                }
            } else {
                if (Utils.isTcnVideo(getUri())) {
                    TcnUtility.sendMsg(m_handler, CMD_PLAY_VIDEO, -1, -1, null);
                } else {
                    if (Utils.isTcnImage(getUri())) {
                        TcnUtility.sendMsg(m_handler, CMD_PLAY_IMAGE, -1, -1, null);
                    }
                }
            }
        }

        public boolean isForbiddenPlay() {
            return m_bForbiddenPlay;
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
            if (m_bForbiddenPlay) {
                TcnLog.getInstance().LoggerDebug(TAG, "play() ForbiddenPlay");
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

        public boolean isOriginalShow() {
            return m_bIsOriginalShow;
        }

        public void setOriginalShow(boolean originalShow) {
            m_bIsOriginalShow = originalShow;
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

    private Handler m_handler = null;
    public void setHandler(Handler handler) {
        m_handler = handler;
    }
}
