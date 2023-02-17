package com.example.pmdm_to4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Botones
        Button btFirma =  findViewById(R.id.mainButtonFirma);
        Button btTraductor =  findViewById(R.id.mainButtonTraductor);
        // Eventos
        btFirma.setOnClickListener(v -> startActivity(new Intent(this, FirmaActivity.class)));
        btTraductor.setOnClickListener(v -> startActivity(new Intent(this, TraductorActivity.class)));
    }
}