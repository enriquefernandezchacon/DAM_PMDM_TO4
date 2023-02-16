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
import android.widget.SeekBar;
import android.widget.TextView;

public class FirmaActivity extends AppCompatActivity {

    Pizzara pizarra;

    private LinearLayout layoutConfAlarma;
    private TextView tvConfTitulo;
    private TextView tvConfSubTitulo;
    private SeekBar confSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        getSupportActionBar().setTitle(R.string.actionbar_firma);

        layoutConfAlarma = findViewById(R.id.layoutConfAlarma);
        tvConfTitulo = findViewById(R.id.tvConfTitulo);
        tvConfSubTitulo = findViewById(R.id.tvConfSubTitulo);
        confSeekBar = findViewById(R.id.confSeekBar);

        pizarra = new Pizzara(this, layoutConfAlarma, tvConfTitulo, tvConfSubTitulo, confSeekBar);
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
        layoutConfAlarma.setVisibility(ConstraintLayout.GONE);
        tvConfTitulo.setVisibility(View.GONE);
        tvConfSubTitulo.setVisibility(View.GONE);
        confSeekBar.setVisibility(View.GONE);
    }

    private void mostrarConfiguracion() {
        layoutConfAlarma.setVisibility(ConstraintLayout.VISIBLE);
        tvConfTitulo.setVisibility(View.VISIBLE);
        tvConfSubTitulo.setVisibility(View.VISIBLE);
        confSeekBar.setVisibility(View.VISIBLE);
    }

    private void createFile(String nombre) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, nombre);

        startActivity(intent);
    }
}