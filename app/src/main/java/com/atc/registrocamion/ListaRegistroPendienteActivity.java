package com.atc.registrocamion;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atc.registrocamion.Adapter.ClientesAdapter;
import com.atc.registrocamion.Adapter.RegistroAdapter;
import com.atc.registrocamion.Entidades.Cliente;
import com.atc.registrocamion.Entidades.Registro;
import com.atc.registrocamion.Entidades.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaRegistroPendienteActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    private RecyclerView recyclerRegistro;
    private ArrayList<Registro> listaRegistro;

    private Context context;
    private String URL_Registro;
    private ProgressDialog progressDialog;
    private Button btnRefresh;

    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_registro_pendiente);
        context = this;
        URL_Registro = getString(R.string.URL_RegistroPendiente);
        request = Volley.newRequestQueue(context);
        listaRegistro = new ArrayList<>();
        /*   ------   */
        btnRefresh = findViewById(R.id.btnRefrescar);
        recyclerRegistro = findViewById(R.id.rvListaRegistroPendiente);
        recyclerRegistro.setLayoutManager(new LinearLayoutManager(context));
        recyclerRegistro.setHasFixedSize(true);
        cargarWebService();
        ActionButtonRefrescar(btnRefresh);
    }

    private void cargarWebService() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Consultando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL_Registro,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context, "No hay registros pendientes por el momento.", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        Log.e("ERROR:",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        try
        {
            Registro registro = null;
            JSONArray jsonArray = response.optJSONArray("registro_pendiente");

            for(int index = 0; index<jsonArray.length();index++)
            {
                registro = new Registro();
                JSONObject jsonObject = null;
                jsonObject=jsonArray.getJSONObject(index);
                registro.setID(jsonObject.getString("ID"));
                registro.setAnden_id(jsonObject.getString("Anden"));
                registro.setTerminal_id(jsonObject.getString("Terminal"));
                registro.setChofer(jsonObject.getString("Chofer"));
                registro.setCliente_id(jsonObject.getString("Cliente"));
                registro.setFecha_creacion(jsonObject.getString("Fecha_creacion"));
                registro.setHora_apertura_camion(jsonObject.getString("Hora_apertura_camion"));
                registro.setHora_ingreso_terminal(jsonObject.getString("Hora_ingreso_terminal"));
                registro.setHora_llegada_camion(jsonObject.getString("Hora_llegada_camion"));
                registro.setPatente(jsonObject.getString("Patente"));
                registro.setUsuario_id_responsable(jsonObject.getString("Usuario"));

                listaRegistro.add(registro);
            }

            progressDialog.hide();
            RegistroAdapter adapter = new RegistroAdapter(listaRegistro,context);
            recyclerRegistro.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            Log.e("ERROR:",e.toString());
        }
    }

    private void ActionButtonRefrescar(Button btn)
    {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaRegistro.clear();
                RegistroAdapter adapter = new RegistroAdapter(listaRegistro,context);
                recyclerRegistro.setAdapter(adapter);
                cargarWebService();
            }
        });
    }
}
