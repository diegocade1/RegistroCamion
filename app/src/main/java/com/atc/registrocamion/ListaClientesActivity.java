package com.atc.registrocamion;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.atc.registrocamion.Adapter.ClientesAdapter;
import com.atc.registrocamion.Entidades.Cliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaClientesActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    private RecyclerView recyclerClientes;
    private ArrayList<Cliente> listaClientes;
    private ProgressDialog progressDialog;
    private Context context;

    private String URL;

    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        context = this;
        URL =  getString(R.string.URL_ListaCliente);

        listaClientes = new ArrayList<>();

        recyclerClientes = findViewById(R.id.rvLista);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(context));
        recyclerClientes.setHasFixedSize(true);

        request = Volley.newRequestQueue(context);

        cargarWebService();

    }

    private void cargarWebService() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Consultando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
        progressDialog.hide();
        Log.e("ERROR:",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        try
        {
            Cliente cliente = null;
            JSONArray jsonArray = response.optJSONArray("cliente");

            for(int index = 0; index<jsonArray.length();index++)
            {
                cliente = new Cliente();
                JSONObject jsonObject = null;
                jsonObject=jsonArray.getJSONObject(index);
                cliente.setID(jsonObject.getInt("ID"));
                cliente.setNombre(jsonObject.getString("Nombre"));
                listaClientes.add(cliente);
            }

            progressDialog.hide();
            ClientesAdapter adapter = new ClientesAdapter(listaClientes,context);
            recyclerClientes.setAdapter(adapter);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "No se ha podido conectar", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            Log.e("ERROR:",e.toString());
        }
    }


}
