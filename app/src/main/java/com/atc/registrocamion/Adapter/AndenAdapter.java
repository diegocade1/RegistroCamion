package com.atc.registrocamion.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atc.registrocamion.Entidades.Anden;
import com.atc.registrocamion.R;

import java.util.List;

public class AndenAdapter extends ArrayAdapter<Anden> {
    private List<Anden> listaAnden;
    private Context context;

    public AndenAdapter(List<Anden> listaAnden,Context context)
    {
        super(context, android.R.layout.simple_list_item_1, listaAnden);
        this.listaAnden = listaAnden;
        this.context=context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        TextView v = (TextView) super.getView(position, convertView, parent);

        if (v == null) {
            v = new TextView(context);
        }
        v.setTextColor(Color.BLACK);
        v.setId(listaAnden.get(position).getID());
        v.setText(listaAnden.get(position).getNombre());
        return v;
    }

    @Override
    public Anden getItem(int position) {
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
        v.setId(listaAnden.get(position).getID());
        v.setText(listaAnden.get(position).getNombre());
        return v;
    }

}
