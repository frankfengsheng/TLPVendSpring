package com.tcn.funcommon.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TextSpeaker {
    private Context m_context;
    private TextToSpeech m_tts;
     
    private boolean m_isTTSAvailable = false;
    
    public TextSpeaker(final Context context) {
        // TODO Auto-generated constructor stub
        this.m_context = context;
        m_tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

			@Override
			public void onInit(int status) {
				if (null == m_tts) {
					Log.e("", "TextSpeaker m_tts is null");
					return;
				}
				if (TextToSpeech.SUCCESS == status) {
                    int result = m_tts.setLanguage(Locale.getDefault());
                    if ((TextToSpeech.LANG_MISSING_DATA == result) && (TextToSpeech.LANG_NOT_SUPPORTED == result)) {
                    	m_isTTSAvailable = false;
                    } else {
                    	m_isTTSAvailable = true;
					}
                }
			}
            
        });
    }

    public void speak(String text) {
    	if (m_tts != null) {
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "UniqueID");
    		m_tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
		}	
    }
  
     
    public boolean isTTSAvailable() {
    	return m_isTTSAvailable;
    }
    
    public boolean isSpeaking() {
    	boolean bRet = false;
    	if (m_tts != null) {
    		bRet = m_tts.isSpeaking();
    	}
    	return bRet;
    }
    
    public void stopSpeak() {
    	if (m_tts != null) {
    		m_tts.stop();
    	}
    }
    
    public void shutdown() {
    	if (m_tts != null) {
    		m_tts.shutdown();
    	}
    }
    
    public void setOnTTSProgressListener(UtteranceProgressListener mTTSCompletedListener) {
		if (m_tts != null) {
			m_tts.setOnUtteranceProgressListener(mTTSCompletedListener);
		}
	}

}