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

import java.util.Objects;

public class FirmaActivity extends AppCompatActivity {
    // Permisos
    private static final int WRITE_EXTERNAL_STORAGE = 1;
    private static final int READ_EXTERNAL_STORAGE = 2;
    // Componentes
    Pizzara pizarra;
    private LinearLayout layoutConfAlarma;
    private TextView tvConfTitulo;
    private TextView tvConfSubTitulo;
    private SeekBar confSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.actionbar_firma);
        // Asigno los componentes
        LinearLayout linearLayout = findViewById(R.id.layoutFirma);
        layoutConfAlarma = findViewById(R.id.layoutConfAlarma);
        tvConfTitulo = findViewById(R.id.tvConfTitulo);
        tvConfSubTitulo = findViewById(R.id.tvConfSubTitulo);
        confSeekBar = findViewById(R.id.seekBarConfiguracion);
        // Creo la view para dibujar
        pizarra = new Pizzara(this, layoutConfAlarma, tvConfTitulo, tvConfSubTitulo, confSeekBar);
        pizarra.setBackgroundColor(Color.WHITE);
        // Añado la view al layout
        linearLayout.addView(pizarra);
        // Pongo el foco en la view
        pizarra.requestFocus();
        // Oculto la configuración
        ocultarConfiguracion();
        // Creo los eventos
        crearEventos();
    }

    // Asigno el menú al ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_firma, menu);
        return true;
    }

    // Eventos del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Evento del botón de configuración
        if (item.getItemId() == R.id.menu_firma_configuracion) {
            mostrarConfiguracion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Este método gestiona los eventos de cada botón
    private void crearEventos() {
        // Evento para limpiar el lienzo
        findViewById(R.id.btBorrarFirma).setOnClickListener(v -> pizarra.limpiar());
        // Evento para guardar la firma en la memoria interna
        findViewById(R.id.btGuardarFirmaInterna).setOnClickListener(v -> pizarra.guardarFirma(true));
        // Evento para guardar la firma en la memoria externa
        findViewById(R.id.btGuardarFirmaExterna).setOnClickListener(v -> {
            // Compruebo si tengo permisos
            if (getPermisos()) {
                // Guardo la firma
                pizarra.guardarFirma(false);
            // Si no tengo permisos
            } else {
                // Muestro un mensaje de error
                Toast.makeText(this, "No tiene permisos para acceder al almacenamiento externo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Este método oculta la configuración
    private void ocultarConfiguracion() {
        layoutConfAlarma.setVisibility(ConstraintLayout.GONE);
        tvConfTitulo.setVisibility(View.GONE);
        tvConfSubTitulo.setVisibility(View.GONE);
        confSeekBar.setVisibility(View.GONE);
    }

    // Este método muestra la configuración
    private void mostrarConfiguracion() {
        layoutConfAlarma.setVisibility(ConstraintLayout.VISIBLE);
        tvConfTitulo.setVisibility(View.VISIBLE);
        tvConfSubTitulo.setVisibility(View.VISIBLE);
        confSeekBar.setVisibility(View.VISIBLE);
    }

    // Este método comprueba si tengo permisos para acceder al almacenamiento externo y si no los tengo los solicita
    public boolean getPermisos() {
        // Compruebo si tengo permiso de escritura en la memoria externa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no lo tengo lo solicito
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
        // Compruebo si tengo permiso de lectura en la memoria externa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no lo tengo lo solicito
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_EXTERNAL_STORAGE);
        }
        // Devuelvo si tengo permisos
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
}