package com.atc.registrocamion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atc.registrocamion.Entidades.Terminal;
import com.atc.registrocamion.R;

import java.util.List;

public class TerminalAdapter extends ArrayAdapter<Terminal>{

    private List<Terminal> listaTerminales;
    private Context context;

    public TerminalAdapter(Context context, List<Terminal> listaTerminales) {
        super(context, android.R.layout.simple_list_item_1, listaTerminales);
        this.listaTerminales = listaTerminales;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.BLACK);
        v.setFocusable(false);
        v.setId(listaTerminales.get(position).getID());
        v.setText(listaTerminales.get(position).getNombre());
        return v;
    }

    @Override
    public Terminal getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.BLACK);
        v.setBackgroundResource(R.drawable.edittextbackground);
        v.setFocusable(false);
        v.setId(listaTerminales.get(position).getID());
        v.setText(listaTerminales.get(position).getNombre());
        return v;
    }
}
