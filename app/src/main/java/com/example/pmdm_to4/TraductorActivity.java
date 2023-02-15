package com.example.pmdm_to4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TraductorActivity extends AppCompatActivity {

    private EditText sourceLanguageEt;
    private TextView targetLanguageTv;
    private MaterialButton sourceLanguageChooseBtn;
    private MaterialButton destinationLanguageChooseBtn;
    private MaterialButton translateBtn;

    private TranslatorOptions translatorOptions;
    private Translator translator;

    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;

    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "ur";
    private String destinationLanguageTitle = "Urdu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traductor);

        getSupportActionBar().setTitle(R.string.actionbar_traductor);

        sourceLanguageEt = findViewById(R.id.sourceLanguageET);
        targetLanguageTv = findViewById(R.id.destinationLanguageTV);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseButton);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseButton);
        translateBtn = findViewById(R.id.translateButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);

        loadAvailableLanguages();

        sourceLanguageChooseBtn.setOnClickListener(v -> {
            sourceLanguageChoose();
        });

        destinationLanguageChooseBtn.setOnClickListener(v -> {
            destinationLanguageChoose();
        });

        translateBtn.setOnClickListener(v -> {
            valideData();
        });
    }

    private String sourceLanguageText = "";
    private void valideData() {
        sourceLanguageText = sourceLanguageEt.getText().toString().trim();

        if (sourceLanguageText.isEmpty()) {
            sourceLanguageEt.setError("Enter text");
        } else {
            startTranslation();
        }
    }

    private void startTranslation() {
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();

        translator = Translation.getClient(translatorOptions);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(unused -> {
                    progressDialog.setMessage("Translating...");
                    translator.translate(sourceLanguageText)
                            .addOnSuccessListener(translatedText -> {
                                targetLanguageTv.setText(translatedText);
                                progressDialog.dismiss();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            sourceLanguageCode = languageArrayList.get(position).getLanguageCode();
            sourceLanguageTitle = languageArrayList.get(position).getLanguageTitle();

            sourceLanguageChooseBtn.setText(sourceLanguageTitle);
            sourceLanguageEt.setHint("Enter " + sourceLanguageTitle + " text");

            return false;
        });
    }

    private void destinationLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            destinationLanguageCode = languageArrayList.get(position).getLanguageCode();
            destinationLanguageTitle = languageArrayList.get(position).getLanguageTitle();

            destinationLanguageChooseBtn.setText(destinationLanguageTitle);
            targetLanguageTv.setHint("Translated " + destinationLanguageTitle + " text");

            return false;
        });
    }

    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();
        List<String> languageCodes = TranslateLanguage.getAllLanguages();
        for (String languageCode : languageCodes) {
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            languageArrayList.add(new ModelLanguage(languageCode, languageTitle));
        }


    }
}