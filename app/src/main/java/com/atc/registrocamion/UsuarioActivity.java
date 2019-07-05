package com.atc.registrocamion;

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

public class UsuarioActivity extends AppCompatActivity {

    private Context context;

    Button btnAceptar,btnCancerlar;
    EditText etUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        context = this;
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
                    startActivity(new Intent(UsuarioActivity.this,ClienteActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    esconderKeyboard();

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

    /*-------------Metodo Esconder Teclado---------------*/
    public void esconderKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
