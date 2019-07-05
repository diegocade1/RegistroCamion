package com.atc.registrocamion;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atc.registrocamion.Entidades.Cliente;

public class ClienteActivity extends AppCompatActivity {

    private Context context;

    Button btnAceptar,btnCancerlar;
    EditText etCliente;

    final int COD_LISTA = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        context = this;
        /*-------Controles------*/
        btnAceptar = findViewById(R.id.btnAceptarCliente);
        btnCancerlar = findViewById(R.id.btnCancelarCliente);
        etCliente = findViewById(R.id.etCliente);
        /*---------Acciones-----------*/

        ActionBtnAceptar(btnAceptar);
        ActionBtnCancelar(btnCancerlar);
        //ActionKeyPressTextCliente(etCliente);
        ActionTouchCliente(etCliente);
        esconderKeyboard();
    }

    private void ActionTouchCliente(final EditText hora)
    {

        hora.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    startActivityForResult(new Intent(ClienteActivity.this,ListaClientesActivity.class),COD_LISTA);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case COD_LISTA:
                    etCliente.setText(data.getStringExtra("result"));
                    break;
                    default:
                        break;
            }
        }
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

                String cliente = etCliente.getText().toString();
                if(!cliente.trim().equals(""))
                {
                    startActivity(new Intent(ClienteActivity.this,RegistroActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    esconderKeyboard();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ingrese Cliente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ActionKeyPressTextCliente(final EditText text)
    {
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    String usuario = text.getText().toString();
                    if(!usuario.trim().equals(""))
                    {
                        startActivity(new Intent(ClienteActivity.this,RegistroActivity.class)
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
