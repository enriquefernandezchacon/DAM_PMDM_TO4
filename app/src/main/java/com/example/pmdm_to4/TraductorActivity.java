package com.example.pmdm_to4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TraductorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traductor);

        getSupportActionBar().setTitle(R.string.actionbar_traductor);


    }
}