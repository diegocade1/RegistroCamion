package com.atc.registrocamion;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.atc.registrocamion.Entidades.Usuario;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UsuarioActivity extends AppCompatActivity {

    private Context context;

    Button btnAceptar,btnCancerlar;
    EditText etUsuario;
    ProgressDialog pdDialogo;

    private String URL_ValidacionUsuario;

    private RequestQueue request;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        context = this;
        URL_ValidacionUsuario = getString(R.string.URL_ValidacionUsuario);
        request = Volley.newRequestQueue(context);
        /*-------Controles------*/
        btnAceptar = findViewById(R.id.btnAceptarUsuario);
        btnCancerlar = findViewById(R.id.btnCancelarUsuario);
        etUsuario = findViewById(R.id.etUsuario);
        /*---------Acciones-----------*/
        ActionBtnAceptar(btnAceptar);
        ActionBtnCancelar(btnCancerlar);
        ActionKeyPressTextUsuario(etUsuario);
    }

    private void ActionBtnCancelar(Button boton)
    {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ActionBtnAceptar(Button boton)
    {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = etUsuario.getText().toString();
                if(!usuario.trim().equals(""))
                {
                    CargarWebServiceValidacion(usuario.toLowerCase());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ingrese Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ActionKeyPressTextUsuario(final EditText text)
    {
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    String usuario = text.getText().toString();
                    if(!usuario.trim().equals(""))
                    {
                        startActivity(new Intent(UsuarioActivity.this,ClienteActivity.class)
                                .putExtra("usuario",usuario)
                                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Ingrese Usuario", Toast.LENGTH_SHORT).show();
                    }
                    esconderKeyboard();
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        return false;
    }

    private void CargarWebServiceValidacion(final String usuario) {
        pdDialogo = new ProgressDialog(context);
        pdDialogo.setMessage(
                "Cargando..."
        );
        pdDialogo.setCancelable(false);
        pdDialogo.show();


        stringRequest= new StringRequest(Request.Method.POST, URL_ValidacionUsuario, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdDialogo.hide();
                if(response.trim().trim().toLowerCase().contains("correcto"))
                {
                    String temp = response.trim().split(";")[1];
                    Usuario user = new Usuario(temp.split(",")[0],temp.split(",")[1],temp.split(",")[2]);
                    startActivity(new Intent(UsuarioActivity.this,ClienteActivity.class)
                            .putExtra("usuario", (Serializable) user)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    esconderKeyboard();
                }
                else
                {
                    Toast.makeText(context, "Usuario incorrecto. "+response.trim(), Toast.LENGTH_SHORT).show();
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
                String id_usuario = usuario;


                Map<String,String> parameters = new HashMap<>();
                parameters.put("id_usuario",usuario);

                return parameters;
            }
        };
        request.add(stringRequest);
    }

    /*-------------Metodo Esconder Teclado---------------*/
    public void esconderKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
