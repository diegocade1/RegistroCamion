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

import com.atc.registrocamion.Entidades.Cliente;
import com.atc.registrocamion.Entidades.Registro;
import com.atc.registrocamion.Entidades.Usuario;
import com.atc.registrocamion.ProcesoActivity;
import com.atc.registrocamion.R;

import java.util.List;

public class RegistroAdapter extends RecyclerView.Adapter<RegistroAdapter.RegistroHolder> {
    private List<Registro> listaRegistro;
    private Context context;
    private Usuario usuario;

    public RegistroAdapter(List<Registro> listaRegistro,Context context)
    {
        this.listaRegistro = listaRegistro;
        this.context = context;
        this.usuario = (Usuario)((Activity) context).getIntent().getSerializableExtra("usuario");
    }
    @NonNull
    @Override
    public RegistroAdapter.RegistroHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_lista_registro_pendiente,viewGroup,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new RegistroHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistroAdapter.RegistroHolder registroHolder, final int i) {
        registroHolder.tvID.setText(listaRegistro.get(i).getID());
        registroHolder.tvCliente.setText(listaRegistro.get(i).getCliente_id());
        registroHolder.tvChofer.setText(listaRegistro.get(i).getChofer());
        registroHolder.tvTerminal.setText(listaRegistro.get(i).getTerminal_id());
        registroHolder.tvAnden.setText(listaRegistro.get(i).getAnden_id());
        registroHolder.tvPatente.setText(listaRegistro.get(i).getPatente());
        registroHolder.tvUsuarioResponsable.setText(listaRegistro.get(i).getUsuario_id_responsable());
        registroHolder.tvFechaCreacion.setText(listaRegistro.get(i).getFecha_creacion());
        registroHolder.tvHoraAperturaCamion.setText(listaRegistro.get(i).getHora_apertura_camion());
        registroHolder.tvHoraIngresoTerminal.setText(listaRegistro.get(i).getHora_ingreso_terminal());
        registroHolder.tvHoraAperturaCamion.setText(listaRegistro.get(i).getHora_apertura_camion());
        registroHolder.tvHoraLLegadaCamion.setText(listaRegistro.get(i).getHora_llegada_camion());

        registroHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(ComplexRecyclerViewAdapter.this, "Item no: "+ position, Toast.LENGTH_LONG).show;
                //Toast.makeText(v.getContext(), "General click !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProcesoActivity.class);
                intent.putExtra("id_registro",listaRegistro.get(i).getID());
                intent.putExtra("usuario",usuario);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ((Activity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRegistro.size();
    }

    public class RegistroHolder extends RecyclerView.ViewHolder {
        TextView tvID,tvCliente,tvTerminal,tvAnden,tvPatente,tvChofer,tvHoraLLegadaCamion,tvHoraIngresoTerminal,tvHoraAperturaCamion,tvFechaCreacion,tvUsuarioResponsable;
        public RegistroHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvIDFilaRegistro);
            tvCliente = itemView.findViewById(R.id.tvClienteFilaRegistro);
            tvTerminal = itemView.findViewById(R.id.tvTerminalFilaRegistro);
            tvAnden = itemView.findViewById(R.id.tvAndenFilaRegistro);
            tvPatente = itemView.findViewById(R.id.tvPatenteFilaRegistro);
            tvChofer = itemView.findViewById(R.id.tvChoferFilaRegistro);
            tvHoraLLegadaCamion = itemView.findViewById(R.id.tvHoraLlegadaCamionFilaRegistro);
            tvHoraIngresoTerminal = itemView.findViewById(R.id.tvHoraIngresoTerminalFilaRegistro);
            tvHoraAperturaCamion = itemView.findViewById(R.id.tvHoraAperturaCamionFilaRegistro);
            tvFechaCreacion = itemView.findViewById(R.id.tvFechaCreacionFilaRegistro);
            tvUsuarioResponsable = itemView.findViewById(R.id.tvUsuarioResponsableFilaRegistro);
        }
    }
}
