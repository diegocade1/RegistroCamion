package com.atc.registrocamion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private final String CARPETA_ROOT = "Imagenes/";
    private final String RUTA_IMAGEN = CARPETA_ROOT+"misFotos";
    private String path;

    final int MIS_PERMISOS = 100;
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    private Uri pathImage;
    private Bitmap bitmapImagen;


    private Context context;
    private DatePickerDialog picker;
    private EditText etPatente,etSello,etFecha;
    private Button btnFoto,btnEnviar;
    private ImageView ivFoto;
    private ProgressDialog pdDialogo;

    private RequestQueue request;
    //private JsonObjectRequest jsonObjectRequest;

    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        context = this;

        request = Volley.newRequestQueue(context);
        //Controles
        etPatente = findViewById(R.id.txtPatente);
        etSello = findViewById(R.id.txtSello);
        etFecha = findViewById(R.id.txtFecha);

        btnEnviar = findViewById(R.id.btnEnviar);
        btnFoto = findViewById(R.id.btnFoto);

        ivFoto = findViewById(R.id.ivFoto);
        //Permisos
        if(ValidarPermisos())
        {
            btnFoto.setEnabled(true);
        }
        else
        {
            btnFoto.setEnabled(false);
        }
        //Eventos Acciones
        ActionClickFecha(etFecha);

        ActionButtonFoto(btnFoto);
        ActionButtonEnviar(btnEnviar);
    }


/*    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context, "No se pudo ingresar, "+error.getMessage(), Toast.LENGTH_SHORT).show();
        pdDialogo.hide();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(context, "Se ha ingresado exitosamente", Toast.LENGTH_SHORT).show();
        pdDialogo.hide();
        etFecha.setText("");
        etSello.setText("");
        etPatente.setText("");
    }*/

    private boolean ValidarPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(INTERNET)== PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(INTERNET)))
        {
            cargarDialogoRecomendacion();
        }
        else
        {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,INTERNET,READ_EXTERNAL_STORAGE},MIS_PERMISOS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MIS_PERMISOS)
        {
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED)
            {
                btnFoto.setEnabled(true);
            }
            else
            {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual()
    {
        final CharSequence[] opciones = {"Si","No"};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Desea configurar los permisos de forma manual");
        alertDialog.setItems(opciones, new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Si"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(context,"Los permisos no fueron aceptados",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void cargarDialogoRecomendacion()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Permisos Desactivados");
        dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app.");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,INTERNET,READ_EXTERNAL_STORAGE},MIS_PERMISOS);
            }
        });

    }

    private void ActionClickFecha(final EditText fecha)
    {
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
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
                        //ivFoto.setImageURI(pathImage);
                        try {
                            bitmapImagen = MediaStore.Images.Media.getBitmap(context.getContentResolver(),pathImage);
                            ivFoto.setImageBitmap(bitmapImagen);
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
                        ivFoto.setImageBitmap(bitmapImagen);
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
                    etFecha.setText("");
                    etSello.setText("");
                    etPatente.setText("");
                    Toast.makeText(context, "Se ha ingresado con exito", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
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
                String patente = etPatente.getText().toString();
                String sello = etSello.getText().toString();
                String fecha = etFecha.getText().toString();

                String imagen = ConvertirImagenString(bitmapImagen);

                Map<String,String> parameters = new HashMap<>();
                parameters.put("patente",patente);
                parameters.put("sello",sello);
                parameters.put("fecha",fecha);
                parameters.put("imagen",imagen);
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