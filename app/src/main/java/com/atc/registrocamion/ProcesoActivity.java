package com.atc.registrocamion;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProcesoActivity extends AppCompatActivity {

    //URL DINAMICO CAMBIAR VALOR EN res/values/strings
    private String URL;


    private Uri pathImage;

    private Context context;
    private EditText etGuiaAerea,etHoraIniciaDescarga,etHoraTerminoDescarga;
    private Button  btnGuardarProceso,btnTerminarProceso;
    private ProgressDialog pdDialogo;
    private String Sello;

    private RequestQueue request;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso);
        //
        context = this;
        URL = getString(R.string.URL_2);
        request = Volley.newRequestQueue(context);
        Sello = (String)getIntent().getSerializableExtra("sello");
        //Controles
        etGuiaAerea = findViewById(R.id.etGuiaAerea);
        etHoraIniciaDescarga = findViewById(R.id.etHoraInicioDescarga);
        etHoraTerminoDescarga = findViewById(R.id.etHoraTerminoDescarga);
        btnGuardarProceso=findViewById(R.id.btnGuardarProceso);
        btnTerminarProceso = findViewById(R.id.btnTerminarProceso);
        // Eventos Acciones
        ActionButtonGuardar(btnGuardarProceso);
        ActionButtonTerminarProceso(btnTerminarProceso);
        ActionTouchHora(etHoraIniciaDescarga);
        ActionTouchHora(etHoraTerminoDescarga);
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


    private void ActionButtonGuardar(Button boton)
    {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String po = etPO.getText().toString();
                String sello = Sello;
//                if(etPO.getText().toString().equals(""))
//                {
//                    Toast.makeText(context, "Debe ingresar la PO", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if(Sello.equals(""))
                {
                    Toast.makeText(context, "Debe ingresar el sello", Toast.LENGTH_SHORT).show();
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
        pdDialogo.show();

/*        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);*/

        stringRequest= new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdDialogo.hide();
                if(response.trim().equalsIgnoreCase("registra"))
                {
                    //etPO.setText("");
                    //bitmapImagen = null;
                    //ivFotoProceso.setImageResource(0);
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
                //String po = etPO.getText().toString();

                //String imagen = ConvertirImagenString(bitmapImagen);

                Map<String,String> parameters = new HashMap<>();
                //parameters.put("po",po);
                parameters.put("sello",Sello);
                //parameters.put("imagen",imagen);
                return parameters;
            }
        };
        request.add(stringRequest);
    }


    private void ActionButtonTerminarProceso(Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
