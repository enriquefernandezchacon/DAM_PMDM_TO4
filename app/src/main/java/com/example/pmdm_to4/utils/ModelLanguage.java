package com.example.pmdm_to4.utils;

public class ModelLanguage {

    private final String languageCode;
    private final String languageTitle;


    //En este objeto se guarda un idioma, tanto su código como su título
    public ModelLanguage(String languageCode, String languageTitle) {
        this.languageCode = languageCode;
        this.languageTitle = languageTitle;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }
}
