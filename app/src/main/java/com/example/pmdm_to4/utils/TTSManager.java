package com.example.pmdm_to4.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class TTSManager {

    private TextToSpeech mTts = null;
    private boolean isLoaded = false;
    private Context context;

    public void init(Context context) {
        this.context = context;
        try {
            mTts = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            Locale spanish = new Locale("es", "ES");
            if (status == TextToSpeech.SUCCESS) {
                int result = mTts.setLanguage(spanish);
                isLoaded = true;

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    isLoaded = false;
                    Toast.makeText(context, "No se ha podido cargar los datos necesarios para el servicio de reproducción de voz", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "No se ha podido cargar el servicio de reproducción de voz", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void shutDown() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
    }

    public void addQueue(String text) {
        if (isLoaded) {
            mTts.speak(text, TextToSpeech.QUEUE_ADD, null);
        } else {
            Toast.makeText(context, "El servicio de reproducción de voz no está disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public void initQueue(String text) {
        if (isLoaded) {
            mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            Toast.makeText(context, "El servicio de reproducción de voz no está disponible", Toast.LENGTH_SHORT).show();
        }
    }

}
