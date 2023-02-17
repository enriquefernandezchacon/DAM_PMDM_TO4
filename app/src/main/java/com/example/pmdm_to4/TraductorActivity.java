package com.example.pmdm_to4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmdm_to4.utils.ModelLanguage;
import com.example.pmdm_to4.utils.TTSManager;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TraductorActivity extends AppCompatActivity {
    // Permisos
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    // UI
    private EditText etTextoATraducir;
    private TextView tvTextoTraducido;
    private MaterialButton btIdiomaInicio;
    private MaterialButton btIdiomaTraducido;
    private Switch switchAutomatico;
    // Traduccion
    private Translator translator;
    private TTSManager ttsm;
    // Progress Dialog
    private ProgressDialog progressDialog;
    // Lenguajes
    private ArrayList<ModelLanguage> listaLenguajes;
    // Parametros auxiliares
    private String codeLenguaBase = "es";
    private String nombreLenguaBase = "Español";
    private String codeLenguaTraducida = "en";
    private String nombreLenguaTraducida = "English";
    private String textoCargadoATraducir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traductor);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.actionbar_traductor);
        // Establezco los recursos de la UI
        etTextoATraducir = findViewById(R.id.etTextoATraducir);
        tvTextoTraducido = findViewById(R.id.tvTextoTraducido);
        btIdiomaInicio = findViewById(R.id.btIdiomaInicio);
        btIdiomaTraducido = findViewById(R.id.btIdiomaTraducido);
        MaterialButton btTraducir = findViewById(R.id.btTraducir);
        MaterialButton btGrabar = findViewById(R.id.btGrabar);
        MaterialButton btReproducir = findViewById(R.id.btReproducir);
        switchAutomatico = findViewById(R.id.switchAutomatico);
        // Establezco los valores iniciales de los botones
        btIdiomaInicio.setText(nombreLenguaBase);
        btIdiomaTraducido.setText(nombreLenguaTraducida);
        // Establezco el ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);
        // Establezco el TTSManager
        ttsm = new TTSManager();
        ttsm.init(this);
        // Establezco la lista de lenguajes
        cargarLenguajes();
        // Establezco los listeners
        btIdiomaInicio.setOnClickListener(v -> elegirIdiomaBase());
        btIdiomaTraducido.setOnClickListener(v -> elegirIdiomaTraduccion());
        btTraducir.setOnClickListener(v -> traducir());
        btGrabar.setOnClickListener(v -> grabarVoz());
        btReproducir.setOnClickListener(v -> ttsm.reproducir(tvTextoTraducido.getText().toString()));
    }

    // Método para grabar la voz del usuario
    private void grabarVoz() {
        // Intent para grabar la voz
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Establezco el idioma basado en el idioma de salida seleccionado
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, codeLenguaBase);
        // Establezco el mensaje de ayuda
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable ahora");
        try {
            // Inicio la actividad para grabar la voz
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Compruebo si la actividad de grabar voz ha finalizado
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // Compruebo si la actividad ha finalizado correctamente
            if (resultCode == RESULT_OK && null != data) {
                // Obtengo el resultado de la actividad
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                // Establezco el texto obtenido en el EditText
                etTextoATraducir.setText(result.get(0));
                // Compruebo si el switch está activado
                if (switchAutomatico.isChecked()) {
                    // Traduzco el texto
                    traducir();
                }
            // Si la actividad no ha finalizado correctamente
            } else {
                // Muestro un mensaje de error
                Toast.makeText(this, "No se ha podido grabar la voz", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para traducir el texto
    private void traducir() {
        // Cargo el texto
        textoCargadoATraducir = etTextoATraducir.getText().toString().trim();
        // Compruebo si el texto está vacío
        if (textoCargadoATraducir.isEmpty()) {
            // Muestro un mensaje de error
            etTextoATraducir.setError("Enter text");
        // Si el texto no está vacío
        } else {
            // Empiezo la traduccion
            startTranslation();
        }
    }

    // Método para realizar el proceso de traducción
    private void startTranslation() {
        // Establezco el mensaje del ProgressDialog y lo muestro
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();
        // Establezco las opciones de traducción
        TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
                // Establezco el idioma seleciconado por el usuario
                .setSourceLanguage(codeLenguaBase)
                .setTargetLanguage(codeLenguaTraducida)
                .build();
        // Obtengo el cliente de traducción
        translator = Translation.getClient(translatorOptions);
        // Establezco las condiciones de descarga
        DownloadConditions conditions = new DownloadConditions.Builder().build();
        // Descargo el modelo de traducción si es necesario
        translator.downloadModelIfNeeded(conditions)
                // Compruebo si se ha descargado correctamente
                .addOnSuccessListener(unused -> {
                    // Establezco el mensaje del ProgressDialog y lo muestro
                    progressDialog.setMessage("Translating...");
                    // Traduzco el texto
                    translator.translate(textoCargadoATraducir)
                            // Compruebo si se ha traducido correctamente
                            .addOnSuccessListener(translatedText -> {
                                // Establezco el texto traducido en el TextView
                                tvTextoTraducido.setText(translatedText);
                                // Oculto el ProgressDialog
                                progressDialog.dismiss();
                                // Compruebo si el switch está activado
                                if (switchAutomatico.isChecked()) {
                                    // Reproduzco el texto traducido
                                    ttsm.reproducir(tvTextoTraducido.getText().toString());
                                }
                            })
                            // Si no se ha traducido correctamente
                            .addOnFailureListener(e -> {
                                // Oculto el ProgressDialog
                                progressDialog.dismiss();
                                // Muestro un mensaje de error
                                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                // Si no se ha descargado correctamente
                .addOnFailureListener(e -> {
                    // Oculto el ProgressDialog
                    progressDialog.dismiss();
                    // Muestro un mensaje de error
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para elegir el idioma inicial
    private void elegirIdiomaBase() {
        // Creo un PopupMenu
        PopupMenu popupMenu = new PopupMenu(this, btIdiomaInicio);
        // Recorro la lista de lenguajes
        for (int i = 0; i < listaLenguajes.size(); i++) {
            // Añado los lenguajes a la lista del PopupMenu
            popupMenu.getMenu().add(Menu.NONE, i, i, listaLenguajes.get(i).getLanguageTitle());
        }
        // Muestro el PopupMenu
        popupMenu.show();
        // Establezco el listener del PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            // Obtengo la posición del lenguaje seleccionado
            int position = item.getItemId();
            // Obtengo el código del lenguaje seleccionado
            codeLenguaBase = listaLenguajes.get(position).getLanguageCode();
            // Obtengo el nombre del lenguaje seleccionado
            nombreLenguaBase = listaLenguajes.get(position).getLanguageTitle();
            // Establezco el idioma del TextToSpeechManager
            btIdiomaInicio.setText(nombreLenguaBase);
            // Establezco el hint del EditText
            etTextoATraducir.setHint("Enter " + nombreLenguaBase + " text");
            // Paro la propagación del evento
            return false;
        });
    }

    // Método para elegir el idioma de traducción
    private void elegirIdiomaTraduccion() {
        PopupMenu popupMenu = new PopupMenu(this, btIdiomaTraducido);
        for (int i = 0; i < listaLenguajes.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, listaLenguajes.get(i).getLanguageTitle());
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            codeLenguaTraducida = listaLenguajes.get(position).getLanguageCode();
            nombreLenguaTraducida = listaLenguajes.get(position).getLanguageTitle();
            ttsm.language = codeLenguaTraducida;
            btIdiomaTraducido.setText(nombreLenguaTraducida);
            tvTextoTraducido.setHint("Translated " + nombreLenguaTraducida + " text");
            return false;
        });
    }

    // Método para cargar los lenguajes
    private void cargarLenguajes() {
        listaLenguajes = new ArrayList<>();
        // Obtengo todos los códigos de lenguaje
        List<String> languageCodes = TranslateLanguage.getAllLanguages();
        // Recorro la lista de códigos de lenguaje
        for (String languageCode : languageCodes) {
            // Obtengo el nombre del lenguaje y lo añado a la lista
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            listaLenguajes.add(new ModelLanguage(languageCode, languageTitle));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsm.shutDown();
    }
}