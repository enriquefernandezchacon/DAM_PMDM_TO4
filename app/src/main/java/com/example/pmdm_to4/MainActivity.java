package com.example.pmdm_to4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btFirma =  findViewById(R.id.mainButtonFirma);
        Button btTraductor =  findViewById(R.id.mainButtonTraductor);

        btFirma.setOnClickListener(v -> {
            startActivity(new Intent(this, FirmaActivity.class));
        });

        btTraductor.setOnClickListener(v -> {
            startActivity(new Intent(this, TraductorActivity.class));
        });
    }
}