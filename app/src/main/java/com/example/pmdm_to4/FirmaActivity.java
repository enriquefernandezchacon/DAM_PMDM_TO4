package com.example.pmdm_to4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class FirmaActivity extends AppCompatActivity {

    Pizzara pizarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        getSupportActionBar().setTitle(R.string.actionbar_firma);

        pizarra = new Pizzara(this);
        pizarra.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayout = findViewById(R.id.layoutFirma);
        linearLayout.addView(pizarra);
        pizarra.requestFocus();

        ocultarConfiguracion();

        gestionarEventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_firma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_firma_configuracion:
                mostrarConfiguracion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gestionarEventos() {
        findViewById(R.id.btBorrarFirma).setOnClickListener(v -> {
            pizarra.limpiar();
        });
        findViewById(R.id.btGuardarFirma).setOnClickListener(v -> {
            createFile(pizarra.guardarFirma());
        });
    }

    private void ocultarConfiguracion() {
        findViewById(R.id.layoutConfAlarma).setVisibility(ConstraintLayout.GONE);
        findViewById(R.id.tvConfTitulo).setVisibility(View.GONE);
        findViewById(R.id.tvConfSubTitulo).setVisibility(View.GONE);
        findViewById(R.id.confSeekBar).setVisibility(View.GONE);
    }

    private void mostrarConfiguracion() {
        findViewById(R.id.layoutConfAlarma).setVisibility(ConstraintLayout.VISIBLE);
        findViewById(R.id.tvConfTitulo).setVisibility(View.VISIBLE);
        findViewById(R.id.tvConfSubTitulo).setVisibility(View.VISIBLE);
        findViewById(R.id.confSeekBar).setVisibility(View.VISIBLE);
    }

    private void createFile(String nombre) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, nombre);

        startActivity(intent);
    }
}