package com.example.pmdm_to4.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class TTSManager {

    private TextToSpeech tts = null;
    private boolean isLoaded = false;
    private Context context;
    public String language = "es";

    //Al inicializar la clase, asignamos un contexto y creamos un objeto TextToSpeech
    public void init(Context context) {
        this.context = context;
        try {
            tts = new TextToSpeech(context, onInitListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Al inicializar el objeto TextToSpeech, se ejecuta este método
    private final TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            //Se asigna el idioma que se ha seleccionado como base
            Locale locale = new Locale(language);
            //Si se ha podido cargar el servicio de reproducción de voz
            if (status == TextToSpeech.SUCCESS) {
                //Se asigna el idioma
                int result = tts.setLanguage(locale);
                //Se establece el flag a true
                isLoaded = true;
                //Si no se ha podido cargar el idioma
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    //Se establece el flag a false
                    isLoaded = false;
                    //Se muestra un mensaje de error
                    Toast.makeText(context, "No se ha podido cargar los datos necesarios para el servicio de reproducción de voz", Toast.LENGTH_SHORT).show();
                }
            //Si no se ha podido cargar el servicio de reproducción de voz
            } else {
                //Se muestra un mensaje de error
                Toast.makeText(context, "No se ha podido cargar el servicio de reproducción de voz", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //Metodo para cerrar el servicio de reproducción de voz
    public void shutDown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    //Metodo para reproducir un texto
    public void reproducir(String text) {
        //Si el servicio de reproducción de voz está disponible
        if (isLoaded) {
            //Se reproduce el texto
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        //Si no está disponible
        } else {
            //Se muestra un mensaje de error
            Toast.makeText(context, "El servicio de reproducción de voz no está disponible", Toast.LENGTH_SHORT).show();
        }
    }
}
