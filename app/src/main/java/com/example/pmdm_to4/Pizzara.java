package com.example.pmdm_to4;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.MotionEvent;
import android.view.View;
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

    public Pizzara(Context context) {
        super(context);
        this.context = context;

        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
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

    public void limpiar() {
        path.reset();
        invalidate();
    }

    public String guardarFirma() {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        String fileName = "firma_" + currentDateAndTime + ".jpg";
        String baseUrl = String.valueOf(context.getFilesDir());
        return fileName;
        /*try {
            File file = new File(baseUrl + "/" + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void ocultarConfiguracion() {
        findViewById(R.id.layoutConfAlarma).setVisibility(ConstraintLayout.GONE);
        findViewById(R.id.tvConfTitulo).setVisibility(View.GONE);
        findViewById(R.id.tvConfSubTitulo).setVisibility(View.GONE);
        findViewById(R.id.confSeekBar).setVisibility(View.GONE);
    }

    // Método que se encarga de descargar la firma.
    /*private void janto() {
        // Comprobamos si la firma está vacía
        /*if (this.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.firmaVacia, Toast.LENGTH_SHORT).show();
            return;
        }
        // Permitimos que la vista se guarde en caché
        this.setDrawingCacheEnabled(true);
        // Obtenemos la imagen de la vista
        Bitmap signatureBitmap = this.getDrawingCache();
        // Creamos un objeto SimpleDateFormat con un formato de fecha específico y una localización local.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        // Formateamosla fecha actual en una cadena con el formato especificado.
        String currentDateAndTime = sdf.format(new Date());
        // Creamos el nombre del archivo con la fecha y hora actual
        String fileName = "firma_" + currentDateAndTime + ".jpg";
        // Verificamos si la memoria externa está disponible para escribir
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Obtenemos la ruta de la memoria externa
            File externalStorageDirectory = context.getExternalFilesDir(null);
            // Creamos un objeto File con la ruta y el nombre del archivo
            File signatureFile = new File(externalStorageDirectory, fileName);
            try {
                // Creamos un objeto FileOutputStream para escribir la imagen de la firma en el archivo.
                FileOutputStream fos = new FileOutputStream(signatureFile);
                // Comprimimos la imagen de la firma en el archivo
                signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                // Cerramos el objeto FileOutputStream.
                fos.close();
                // Mostramos un mensaje con la ruta del archivo
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.firmaGuardada) + signatureFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
