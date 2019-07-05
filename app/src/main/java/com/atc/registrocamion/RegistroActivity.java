package com.atc.registrocamion;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atc.registrocamion.Adapter.AndenAdapter;
import com.atc.registrocamion.Adapter.TerminalAdapter;
import com.atc.registrocamion.Entidades.Anden;
import com.atc.registrocamion.Entidades.Terminal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


//public class RegistroActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
public class RegistroActivity extends AppCompatActivity  {

    //URL DINAMICO CAMBIAR VALOR EN res/values/strings
    private String URL_Registro,URL_Lista_Terminal,URL_Lista_Anden;
    private final String CARPETA_ROOT = "Imagenes/";
    private final String RUTA_IMAGEN = CARPETA_ROOT+"misFotos";
    private String path;

    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;

    final float WIDTH = 800f;
    final float HEIGHT = 600f;

    private ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    private float startXValue = 1;
    private  int posicion;

    private Uri pathImage;
    private Bitmap bitmapImagen;


    private Context context;
    private DatePickerDialog picker;
    private EditText etPatente,etTerminal,etAnden,etChofer,etHoraLlegadaCamion,etHoraIngresoTerminal,etHoraAperturaCamion;
    private Spinner spnrTerminal,spnrAnden;
    private Button btnFoto,btnEnviar;
    private ImageView ivFoto;
    private ProgressDialog pdDialogo;
    private ImageSwitcher isImagenes;

    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;

    private ArrayList<Terminal> listaTerminal;
    private ArrayList<Anden> listaAnden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //
        context = this;
        URL_Registro = getString(R.string.URL_Registro);
        URL_Lista_Terminal = getString(R.string.URL_ListaTerminal);
        URL_Lista_Anden = getString(R.string.URL_ListaAnden);
        request = Volley.newRequestQueue(context);
        listaTerminal = new ArrayList<>();
        listaAnden = new ArrayList<>();
        //Controles
        etPatente = findViewById(R.id.etPatente);
        //etTerminal = findViewById(R.id.etTerminal);
        //etAnden = findViewById(R.id.etAnden);
        etChofer = findViewById(R.id.etChofer);
        etHoraLlegadaCamion = findViewById(R.id.etHoraLlegadaCamion);
        etHoraIngresoTerminal = findViewById(R.id.etHoraIngresoTerminal);
        etHoraAperturaCamion = findViewById(R.id.etHoraAperturaCamion);

        btnEnviar = findViewById(R.id.btnEnviarRegistro);
        btnFoto = findViewById(R.id.btnFotoRegistro);

        ivFoto = findViewById(R.id.ivFoto);

        isImagenes = findViewById(R.id.isImagenes);

        spnrTerminal = findViewById(R.id.spnrTerminal);
        spnrAnden = findViewById(R.id.spnrAnden);
        // Carga de Lista
        cargarWebServiceListaTerminal();
        //Eventos Acciones

        //ActionTouchFecha(etFecha);
        ActionTouchHora(etHoraAperturaCamion);
        ActionTouchHora(etHoraIngresoTerminal);
        ActionTouchHora(etHoraLlegadaCamion);

        ActionButtonFoto(btnFoto);
        ActionButtonEnviar(btnEnviar);

        ActionSwipeImageSwitcher(isImagenes);
        ActionOnDropDown(spnrTerminal);
    }

    private void cargarWebServiceListaTerminal() {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_Lista_Terminal, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Terminal terminal = null;
                    JSONArray jsonArray = response.optJSONArray("terminal");

                    for(int index = 0; index<jsonArray.length();index++)
                    {
                        terminal = new Terminal();
                        JSONObject jsonObject = null;
                        jsonObject=jsonArray.getJSONObject(index);
                        terminal.setID(jsonObject.getInt("ID"));
                        terminal.setNombre(jsonObject.getString("Nombre"));
                        listaTerminal.add(terminal);
                    }

                    TerminalAdapter adapter = new TerminalAdapter(context,listaTerminal);
                    spnrTerminal.setAdapter(adapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se ha podido conectar, error al traer lista de terminales", Toast.LENGTH_SHORT).show();
                Log.e("ERROR:",error.toString());
            }
        });
        request.add(jsonObjectRequest);
    }

    private void cargarWebServiceListaAnden(String id) throws JSONException {

        JSONObject jo = new JSONObject();
        jo.put("id", "1");

        JSONArray ja = new JSONArray();
        ja.put(jo);

        JSONObject mainObj = new JSONObject();
        mainObj.put("terminal", ja);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_Lista_Anden,mainObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    Anden anden = null;
                    JSONArray jsonArray = response.optJSONArray("anden");

                    for(int index = 0; index<jsonArray.length();index++)
                    {
                        anden = new Anden();
                        JSONObject jsonObject = null;
                        jsonObject=jsonArray.getJSONObject(index);
                        anden.setID(jsonObject.getInt("ID"));
                        anden.setTerminal_id(jsonObject.getInt("Terminal_id"));
                        anden.setNombre(jsonObject.getString("Nombre"));
                        listaAnden.add(anden);
                    }

                    AndenAdapter adapter = new AndenAdapter(listaAnden,context);
                    spnrAnden.setAdapter(adapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No se ha podido conectar, error al traer lista de andenes, "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR:",error.toString());
            }
        });
        request.add(jsonObjectRequest);
    }

    private void ActionSwipeImageSwitcher(ImageSwitcher imageSwitcher)
    {
        imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float endXValue = 0;
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        startXValue = event.getAxisValue(MotionEvent.AXIS_X);

                        return true;

                    case (MotionEvent.ACTION_UP):
                        endXValue = event.getAxisValue(MotionEvent.AXIS_X);
                        if (endXValue > startXValue) {
                            if (endXValue - startXValue > 100) {
                                System.out.println("Left-Right");
                                if(posicion<=bitmapArray.size()-1 && posicion >0)
                                {
                                    posicion--;

                                    ImageViewAnimatedChange(context,ivFoto,bitmapArray.get(posicion),
                                            AnimationUtils.loadAnimation(context,R.anim.anim_slide_in_right),AnimationUtils.loadAnimation(context, R.anim.anim_slide_out_right));
                                }
                                else
                                {
                                    Toast.makeText(context, "No hay mas imagenes.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                            {
                            if (startXValue -endXValue> 100) {
                                System.out.println("Right-Left");
                                if(posicion<bitmapArray.size()-1)
                                {
                                    posicion++;
                                    ImageViewAnimatedChange(context,ivFoto,bitmapArray.get(posicion),
                                            AnimationUtils.loadAnimation(context,R.anim.anim_slide_in_left),AnimationUtils.loadAnimation(context, R.anim.anim_slide_out_left));
                                }
                                else
                                {
                                    Toast.makeText(context, "No hay mas imagenes.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        return true;

                    default:
                        return RegistroActivity.super.onTouchEvent(event);
                }
            }
        });
    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image,final Animation in,final Animation out) {
        final Animation anim_out = out;
        final Animation anim_in  = in;
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }


    private void ActionTouchFecha(final EditText fecha)
    {

        fecha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    picker = new DatePickerDialog(RegistroActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, year, month, day);
                    picker.show();
                    return true;
                }
                return false;
            }
        });
    }

    private void ActionTouchHora(final EditText hora)
    {

        hora.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            hora.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Seleccione la hora:");
                    mTimePicker.show();
                    return true;
                }
                return false;
            }
        });
    }

    private void ActionButtonFoto(Button boton)
        {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmapArray.size()<3)
                {
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
                                    //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                else
                {
                    Toast.makeText(context, "Maximo 3 imagenes", Toast.LENGTH_SHORT).show();
                }
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        } else {
            File file = new File(Uri.fromFile(imagen).getPath());
            Uri photoUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }
        //startActivityForResult(intent,COD_FOTO);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, COD_FOTO);
        }
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
                            bitmapImagen = RedimencionarImagen(RotateBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(),pathImage),90f),WIDTH,HEIGHT);
                            bitmapArray.add(bitmapImagen);
                            posicion = bitmapArray.lastIndexOf(bitmapImagen);
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

                        bitmapImagen = RedimencionarImagen(RotateBitmap(BitmapFactory.decodeFile(path),90f),WIDTH,HEIGHT);
                        bitmapArray.add(bitmapImagen);
                        posicion = bitmapArray.lastIndexOf(bitmapImagen);
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

                if(etPatente.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Debe ingresar la patente", Toast.LENGTH_SHORT).show();
                    return;
                }
/*                if(etSello.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Debe ingresar el sello", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etFecha.getText().toString().equals(""))
                {
                    Toast.makeText(context, "Debe ingresar la fecha", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(bitmapImagen==null)
                {
                    Toast.makeText(context, "Debe tomar foto del sello", Toast.LENGTH_SHORT).show();
                    return;
                }
                CargarWebService();
            }
        });
    }

    private void CargarWebService() {
        pdDialogo = new ProgressDialog(context);
        pdDialogo.setMessage(
                "Cargando..."
        );
        pdDialogo.setCancelable(false);
        pdDialogo.show();

/*        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);*/

        stringRequest= new StringRequest(Request.Method.POST, URL_Registro, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdDialogo.hide();
                if(response.trim().equalsIgnoreCase("registra"))
                {

                    Intent intent = new Intent(RegistroActivity.this,ProcesoActivity.class);
                    //intent.putExtra("sello",etSello.getText().toString());
                    //etFecha.setText("");
                    //etSello.setText("");
                    etPatente.setText("");

                    bitmapImagen = null;
                    startActivity(intent);
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
                String patente = etPatente.getText().toString();
                //String sello = etSello.getText().toString();
                //String fecha = etFecha.getText().toString();

                String imagen = ConvertirImagenString(bitmapImagen);

                Map<String,String> parameters = new HashMap<>();
                parameters.put("patente",patente);
                //parameters.put("sello",sello);
                //parameters.put("fecha",fecha);
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



    private static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap RedimencionarImagen(Bitmap bitmap,float width, float height)
    {
        int anchoOld = bitmap.getWidth();
        int altoOld = bitmap.getHeight();

        if(anchoOld>altoOld)
        {
            if(anchoOld >width || altoOld > height)
            {
                float escalaAncho = width / anchoOld;
                float escalaAlto = height / altoOld;

                Matrix matrix = new Matrix();
                matrix.postScale(escalaAncho,escalaAlto);

                return bitmap.createBitmap(bitmap,0,0,anchoOld,altoOld,matrix,false);
            }
            else
            {
                return bitmap;
            }
        }
        else
        {
            if( altoOld >width || anchoOld > height)
            {
                float escalaAncho = width / altoOld;
                float escalaAlto = height / anchoOld;

                Matrix matrix = new Matrix();
                matrix.postScale(escalaAncho,escalaAlto);

                return bitmap.createBitmap(bitmap,0,0,anchoOld,altoOld,matrix,false);
            }
            else
            {
                return bitmap;
            }
        }
    }

    private void ActionOnDropDown(Spinner spinner)
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Terminal terminal = (Terminal)parent.getItemAtPosition(position);
                try {
                    cargarWebServiceListaAnden(Integer.toString(terminal.getID()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
