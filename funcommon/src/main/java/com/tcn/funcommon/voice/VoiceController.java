package com.tcn.funcommon.voice;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import com.tcn.funcommon.R;
import com.tcn.funcommon.media.ImageVideoController;


/**
 * Created by Administrator on 2016/4/13.
 */
public class VoiceController {
    private static final String TAG = "VoiceController";
    private static VoiceController s_m = null;
    private TextSpeaker m_TextSpeaker = null;
    private SoundPool m_SoundPool = null;
    private volatile int m_SourceId = -1;

    public static synchronized VoiceController instance() {
        if (s_m == null)
        {
            s_m = new VoiceController();
        }
        return s_m;
    }

    public void init(Context context) {
        m_TextSpeaker = new TextSpeaker(context);
        m_TextSpeaker.setOnTTSProgressListener(m_TTSProgress);
    }

    public void deInit() {
        if (m_TextSpeaker != null) {
            m_TextSpeaker.setOnTTSProgressListener(null);
            m_TextSpeaker = null;
        }
    }

    public void loadSound(Context context) {
        m_SoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        //载入音频流，返回在池中的id
        m_SourceId = m_SoundPool.load(context, R.raw.shuidi, 0);
        //final AudioManager am = (AudioManager) m_Context.getSystemService(Context.AUDIO_SERVICE);
        //final float audioMaxVolum = am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM); // 获取设备最大音量
        //final int audioCurrentVolum = am.getStreamVolume(AudioManager.STREAM_SYSTEM); // 获取设备当前音量
        //VendIF.getInstance().LoggerDebug(TAG, "播放语音wwwwwww audioCurrentVolum: "+audioCurrentVolum+" audioMaxVolum: "+audioMaxVolum);
        //am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
    }

    public void playSound() {
        if (null != m_SoundPool) {
            m_SoundPool.play(m_SourceId, 0.6f, 0.6f, 0, 0, 1);
        }
    }

    public void releaseSound() {
        if (m_SoundPool != null) {
            m_SoundPool.unload(m_SourceId);
            m_SoundPool.release();
            m_SoundPool = null;
        }
    }

    public void speak(String strSpeak) {
        if (null == m_TextSpeaker) {
            Log.e(TAG,"onTextSpeak()  m_TextSpeaker is null");
            return;
        }

        if (!m_TextSpeaker.isTTSAvailable()) {
            Log.e(TAG, "onTextSpeak()  m_TextSpeaker isTTSAvailable not");
            return;
        }

        if (m_TextSpeaker.isSpeaking()) {
            m_TextSpeaker.stopSpeak();
        }
        m_TextSpeaker.speak(strSpeak);
    }

    public boolean isTextSpeak() {
        boolean bRet = false;
        if (null != m_TextSpeaker) {
            bRet = m_TextSpeaker.isSpeaking();
        }
        return bRet;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private TTSProgressListener m_TTSProgress = new TTSProgressListener();
    public class TTSProgressListener extends UtteranceProgressListener {

        private boolean mVolumeChange = false;
        private float mVol = 1.0f;

        @Override
        public void onStart(String utteranceId) {
            mVolumeChange = false;
            mVol = 1.0f;
            if (ImageVideoController.instance().isPlaying()) {
                for (int i = 0; i < 8; i++) {
                    mVol -= 0.1f;
                    ImageVideoController.instance().setVolume(mVol, mVol);
                    sleep(20);
                }
                mVolumeChange = true;
            }
        }

        @Override
        public void onDone(String utteranceId) {
            if (mVolumeChange) {
                while (mVol < 1.0f) {
                    mVol += 0.05f;
                    ImageVideoController.instance().setVolume(mVol, mVol);
                    sleep(100);
                }
            }
        }

        @Override
        public void onError(String utteranceId) {
            Log.e(TAG, "TTSProgressListener onError: utteranceId: " + utteranceId);
        }
    }
}
