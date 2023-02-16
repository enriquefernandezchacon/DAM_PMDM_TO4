package com.example.pmdm_to4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmdm_to4.utils.Pizzara;

public class FirmaActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private static final int READ_EXTERNAL_STORAGE = 2;

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
        findViewById(R.id.btGuardarFirmaInterna).setOnClickListener(v -> {
            pizarra.guardarFirma(true);
        });
        findViewById(R.id.btGuardarFirmaExterna).setOnClickListener(v -> {
            if (getPermisos()) {
                pizarra.guardarFirma(false);
            } else {
                Toast.makeText(this, "No tiene permisos para acceder al almacenamiento externo", Toast.LENGTH_SHORT).show();
            }
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

    public boolean getPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_EXTERNAL_STORAGE);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}