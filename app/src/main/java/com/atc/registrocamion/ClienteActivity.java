package com.atc.registrocamion;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ArrowKeyMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atc.registrocamion.Entidades.Cliente;
import com.atc.registrocamion.Entidades.Usuario;

import java.io.Serializable;

public class ClienteActivity extends AppCompatActivity {

    private Context context;

    Button btnAceptar,btnCancerlar;
    EditText etCliente;

    final int COD_LISTA = 10;

    private Usuario usuario;
    private String cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        context = this;
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*-------Controles------*/
        btnAceptar = findViewById(R.id.btnAceptarCliente);
        btnCancerlar = findViewById(R.id.btnCancelarCliente);
        etCliente = findViewById(R.id.etCliente);

        //etCliente.setEnabled(false);
        /*---------Acciones-----------*/

        ActionBtnAceptar(btnAceptar);
        ActionBtnCancelar(btnCancerlar);
        //ActionKeyPressTextCliente(etCliente);
        ActionTouchCliente(etCliente);
        etCliente.requestFocus();
        disableEditText(etCliente);
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
                    cliente = data.getStringExtra("result");
                    etCliente.setText(cliente.split(";")[0]);
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

                String cl = etCliente.getText().toString();
                if(!cl.trim().equals(""))
                {
                    startActivity(new Intent(ClienteActivity.this,RegistroActivity.class)
                            .putExtra("usuario", (Serializable) usuario)
                            .putExtra("cliente",cliente.split(";")[1])
                            .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();

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

    /*-------------Metodo Esconder o Mostrar Teclado---------------*/

    public void enableButtonClick(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
            editText.setShowSoftInputOnFocus(true);
        } else { // API 11-20
            editText.setTextIsSelectable(false);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
            editText.setLongClickable(true);
            editText.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            editText.setText(editText.getText(), TextView.BufferType.SPANNABLE);
        }
    }

    // when keyboard is hidden it shouldn't respond when editText is clicked
    public void disableEditText(EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21 >
            editText.setShowSoftInputOnFocus(false);
        } else { // API 11-20
            editText.setTextIsSelectable(true);
        }
    }
}
