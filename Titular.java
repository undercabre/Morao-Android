package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.util.ArrayList;

public class Titular extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ProductoInformacion> items;

    public Titular(Activity activity, ArrayList<ProductoInformacion> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<ProductoInformacion> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item = inf.inflate(R.layout.listitem_titular, null);
        }

        ProductoInformacion dir = items.get(position);

        TextView lblTitulo = item.findViewById(R.id.LblTitulo);
        TextView lblPrecio = item.findViewById(R.id.LblPrecio);
        TextView lblCaracteristicas = item.findViewById(R.id.LblCAracteristicas);

        lblTitulo.setText(dir.getTitulo());
        lblPrecio.setText(dir.getPrecio());
        lblCaracteristicas.setText(dir.getDescripcion());

        return (item);
    }
}