package com.example.pmdm_to4;

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

    private Paint paint = new Paint();
    private Path path = new Path();
    private Context context;

    private LinearLayout layoutConfAlarma;
    private TextView tvConfTitulo;
    private TextView tvConfSubTitulo;
    private SeekBar confSeekBar;

    public Pizzara(Context context, LinearLayout layoutConfAlarma, TextView tvConfTitulo, TextView tvConfSubTitulo, SeekBar confSeekBar) {
        super(context);
        this.context = context;
        this.layoutConfAlarma = layoutConfAlarma;
        this.tvConfTitulo = tvConfTitulo;
        this.tvConfSubTitulo = tvConfSubTitulo;
        this.confSeekBar = confSeekBar;

        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);

        confSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paint.setStrokeWidth(progress);
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



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if(layoutConfAlarma.getVisibility() == View.VISIBLE) {
            if (y > 250) {
                ocultarConfiguracion();
            } else {
                return false;
            }
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    private void ocultarConfiguracion() {
        layoutConfAlarma.setVisibility(ConstraintLayout.GONE);
        tvConfTitulo.setVisibility(TextView.GONE);
        tvConfSubTitulo.setVisibility(TextView.GONE);
        confSeekBar.setVisibility(SeekBar.GONE);
    }

    public void limpiar() {
        path.reset();
        invalidate();
    }

    public void guardarFirma(boolean interna) {
        File file = null;
        if (!interna) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                file = new File(Environment.getExternalStorageDirectory(), getNombreArchivo());
            } else {
                Toast.makeText(context, "No se encuentra una memoria externa", Toast.LENGTH_SHORT).show();
            }
        } else {
            file = new File(getContext().getExternalFilesDir(null), getNombreArchivo());
        }

        if (file != null) {

            try {
                if(!file.exists()){ // Si no existe, crea el archivo.
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(getData());
                fos.close();
                Toast.makeText(context, "Firma guardada en la memoria " + (interna ? "interna" : "externa") + " del teléfono", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No se ha podido guardar la firma", Toast.LENGTH_SHORT).show();
        }
    }

    private String getNombreArchivo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        return "firma_" + currentDateAndTime + ".jpg";
    }

    private byte[] getData() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
