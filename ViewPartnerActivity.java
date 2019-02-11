package com.example.adminportatil.tssapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.util.ArrayList;

public class ViewPartnerActivity extends AppCompatActivity {

    ListView lstOpciones;
    ArrayList<GestionarPartners> listaPartner;
    ArrayList<String> listaInformacion;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_partner);

        conn = new ConexionSQLiteHelper(getApplicationContext());

        lstOpciones = findViewById(R.id.LstPartner);

        consultarListaPartner();

        ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaInformacion);
        lstOpciones.setAdapter(adaptador);
    }

    private void consultarListaPartner() {
        SQLiteDatabase db = conn.getReadableDatabase();
        GestionarPartners partners = null;
        listaPartner = new ArrayList<GestionarPartners>();
        Cursor cursor = db.rawQuery("Select * From " + Utilidades.tabla_partner, null);
        while (cursor.moveToNext()) {
            partners = new GestionarPartners();
            partners.setDni(cursor.getString(0));
            partners.setPartner(cursor.getString(1));
            partners.setApellidos(cursor.getString(2));
            listaPartner.add(partners);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (int i = 0; i < listaPartner.size(); i++) {
            listaInformacion.add(listaPartner.get(i).getDni()+" - "+listaPartner.get(i).getPartner()+" "+listaPartner.get(i).getApellidos());
        }
    }
}