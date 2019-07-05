package com.atc.registrocamion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atc.registrocamion.Entidades.Cliente;
import com.atc.registrocamion.R;

import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClientesHolder> {

    private List<Cliente> listaClientes;
    private Context context;

    public ClientesAdapter(List<Cliente> listaClientes,Context context)
    {
        this.listaClientes = listaClientes;
        this.context = context;
    }

    @NonNull
    @Override
    public ClientesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_lista,viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new ClientesHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientesHolder clientesHolder, final int i) {
        clientesHolder.tvID.setText(Integer.toString(listaClientes.get(i).getID()));
        clientesHolder.tvNombre.setText(listaClientes.get(i).getNombre());

        clientesHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(ComplexRecyclerViewAdapter.this, "Item no: "+ position, Toast.LENGTH_LONG).show;
                //Toast.makeText(v.getContext(), "General click !", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",listaClientes.get(i).getNombre());
                ((Activity) context).setResult(Activity.RESULT_OK,returnIntent);
                ((Activity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public class ClientesHolder extends RecyclerView.ViewHolder {

        TextView tvID,tvNombre;
        public ClientesHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvIDFilaLista);
            tvNombre = itemView.findViewById(R.id.tvNombreFilaLista);

        }
    }
}
