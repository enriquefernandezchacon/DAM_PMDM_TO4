package com.example.pmdm_to4.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pizzara extends View {

    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final Context context;

    private final LinearLayout layoutConfAlarma;
    private final TextView tvConfTitulo;
    private final TextView tvConfSubTitulo;
    private final SeekBar seekBar;

    public Pizzara(Context context, LinearLayout layoutConfAlarma, TextView tvConfTitulo, TextView tvConfSubTitulo, SeekBar confSeekBar) {
        super(context);
        this.context = context;
        this.layoutConfAlarma = layoutConfAlarma;
        this.tvConfTitulo = tvConfTitulo;
        this.tvConfSubTitulo = tvConfSubTitulo;
        this.seekBar = confSeekBar;
        //Configura el lienzo
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        //Configuro el comportamiento del SeekBar
        confSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //Cuando se mueve el SeekBar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Se cambia el grosor de la linea
                paint.setStrokeWidth(progress);
                //Se recarga el lienzo
                invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //Metodo para guardar la imagen
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    //Metodo para detectar el evento de pulsación
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Obtengo las coordenadas del evento
        float x = event.getX();
        float y = event.getY();
        //Si la configuración de la alarma está visible
        if(layoutConfAlarma.getVisibility() == View.VISIBLE) {
            //Si se ha pulsado fuera de la configuración, se oculta
            if (y > 250) {
                ocultarConfiguracion();
            //Si se ha pulsado dentro de la configuración, se ignora el evento
            } else {
                return false;
            }
        }
        //Obtengo el tipo de accion
        int action = event.getAction();
        //Si se ha pulsado
        if (action == MotionEvent.ACTION_DOWN) {
            path.moveTo(x, y);
            return true;
        //Si se ha arrastrado
        } else if (action == MotionEvent.ACTION_MOVE) {
            path.lineTo(x, y);
        } else {
            return false;
        }
        //Se recarga el lienzo
        invalidate();
        return true;
    }

    //Metodo para ocultar la configuración de la alarma
    private void ocultarConfiguracion() {
        layoutConfAlarma.setVisibility(ConstraintLayout.GONE);
        tvConfTitulo.setVisibility(TextView.GONE);
        tvConfSubTitulo.setVisibility(TextView.GONE);
        seekBar.setVisibility(SeekBar.GONE);
    }

    //Método para limpiar el lienzo
    public void limpiar() {
        path.reset();
        invalidate();
    }

    //Método para exportar la firma
    public void guardarFirma(boolean interna) {
        File file = null;
        //Si se ha seleccionado la memoria externa
        if (!interna) {
            //Si la memoria externa está montada
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                //Se crea el archivo
                file = new File(Environment.getExternalStorageDirectory(), getNombreArchivo());
            //Si no está montada
            } else {
                //Se muestra un mensaje de error
                Toast.makeText(context, "No se encuentra una memoria externa", Toast.LENGTH_SHORT).show();
            }
        //Si se ha seleccionado la memoria interna
        } else {
            //Se crea el archivo
            file = new File(getContext().getExternalFilesDir(null), getNombreArchivo());
        }

        //Si se ha creado el archivo
        if (file != null) {
            try {
                //Si no existe el archivo, crea el archivo
                if(!file.exists()){
                    file.createNewFile();
                } else {
                    //Si existe, lo borra
                    file.delete();
                    //Y lo vuelve a crear
                    file.createNewFile();
                }
                //Se escribe el archivo
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData());
                //Se cierra el archivo
                fos.close();
                //Se muestra un mensaje de confirmación
                Toast.makeText(context, "Firma guardada en la memoria " + (interna ? "interna" : "externa") + " del teléfono", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //Si no se ha creado el archivo
        } else {
            //Se muestra un mensaje de error
            Toast.makeText(context, "No se ha podido guardar la firma", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para obtener el nombre del archivo en base a la fecha y hora
    private String getNombreArchivo() {
        //Se crea un objeto de tipo SimpleDateFormat con el formato deseado
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        //Se obtiene la fecha y hora actual y se formatea
        String currentDateAndTime = sdf.format(new Date());
        //Se devuelve el nombre del archivo
        return "firma_" + currentDateAndTime + ".jpg";
    }

    //Método para obtener los datos de la imagen
    private byte[] getData() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);
        //Se crea un objeto de tipo Bitmap con la imagen
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Se comprime la imagen en JPEG
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        //Limpiar el DrawingCache
        this.destroyDrawingCache();
        //Se devuelve los datos de la imagen
        return baos.toByteArray();
    }
}
