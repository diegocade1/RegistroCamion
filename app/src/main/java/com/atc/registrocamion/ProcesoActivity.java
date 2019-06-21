package com.atc.registrocamion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProcesoActivity extends AppCompatActivity {

    private final String CARPETA_ROOT = "Imagenes/";
    private final String RUTA_IMAGEN = CARPETA_ROOT+"misFotos";
    private String path;

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private Uri pathImage;
    private Bitmap bitmapImagen;

    private Context context;
    private EditText etPO;
    private Button btnFotoProceso, btnGuardarProceso;
    private ImageView ivFotoProceso;
    private ProgressDialog pdDialogo;

    private RequestQueue request;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso);
        //
        context = this;
        //Controles
        etPO = findViewById(R.id.etPO);
        btnFotoProceso = findViewById(R.id.btnFotoProceso);
        btnGuardarProceso=findViewById(R.id.btnGuardarProceso);
        // Eventos Acciones

    }

    private void ActionButtonFoto(Button boton)
    {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] opciones = {"Tomar Foto","Cargar Imagen","Cancelar"};
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Seleccione una opcion");
                alertDialog.setItems(opciones, new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(opciones[which].equals("Tomar Foto"))
                        {
                            TomarFoto();
                        }
                        else
                        {
                            if(opciones[which].equals("Cargar Imagen"))
                            {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/");
                                startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),COD_SELECCIONA);
                            }
                            else
                            {
                                dialog.dismiss();
                            }
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void TomarFoto()
    {
        File fileImagen = new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreated = fileImagen.exists();
        String nombreImagen ="";
        if(isCreated==false)
        {
            isCreated = fileImagen.mkdirs();
        }

        if(isCreated == true)
        {
            nombreImagen = (System.currentTimeMillis()/1000)+".jpg";
        }

        path = Environment.getExternalStorageDirectory()+File.separator+RUTA_IMAGEN+File.separator+nombreImagen;

        File imagen = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        startActivityForResult(intent,COD_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            switch(requestCode)
            {
                case COD_SELECCIONA:
                    pathImage = data.getData();
                    try {
                        bitmapImagen = MediaStore.Images.Media.getBitmap(context.getContentResolver(),pathImage);
                        ivFotoProceso.setImageBitmap(bitmapImagen);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento","Path:"+path);
                        }
                    });

                    bitmapImagen = BitmapFactory.decodeFile(path);
                    ivFotoProceso.setImageBitmap(bitmapImagen);
                    break;
            }

        }
    }

    private void ActionButtonEnviar(Button boton)
    {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarWebService();
            }
        });
    }

    private void CargarWebService() {
        pdDialogo = new ProgressDialog(context);
        pdDialogo.setMessage(
                "Cargando..."
        );
        pdDialogo.show();
        String url = "http://192.168.1.122:8080/RegistroCamion/wsJSONRegistroMobil.php";
/*        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);*/

        stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdDialogo.hide();
                if(response.trim().equalsIgnoreCase("registra"))
                {
                    etPO.setText("");
                    ivFotoProceso.setImageResource(0);
                    Toast.makeText(context, "Se ha ingresado con exito", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "No se pudo ingresar"+response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                pdDialogo.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //String patente = etPatente.getText().toString();
                //String sello = etSello.getText().toString();
                //String fecha = etFecha.getText().toString();

                String imagen = ConvertirImagenString(bitmapImagen);

                Map<String,String> parameters = new HashMap<>();
                //parameters.put("patente",patente);
                //parameters.put("sello",sello);
                //parameters.put("fecha",fecha);
                //parameters.put("imagen",imagen);
                return parameters;
            }
        };
        request.add(stringRequest);
    }

    private String ConvertirImagenString(Bitmap bitmapImagen) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmapImagen.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte [] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte,Base64.DEFAULT);
        return imagenString;
    }
}
