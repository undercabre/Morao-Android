package com.example.adminportatil.tssapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.util.ArrayList;
import java.util.Calendar;

public class ListaPedidosActivity extends AppCompatActivity implements View.OnClickListener {
    String fecha;
    Button buscar;
    ListView lstOpciones;
    ArrayList<GestionarPedidos> listaPedidos;
    ArrayList<String> listaInformacion;
    ConexionSQLiteHelper conn;
    private String part, comercial, info, hoy, nombre, precio, descrip;
    private int pTotal, cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        conn = new ConexionSQLiteHelper(getApplicationContext());
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH);
        mes = mes + 1;
        int anio = cal.get(Calendar.YEAR);
        fecha = dia + "/" + mes + "/" + anio;

        lstOpciones = findViewById(R.id.listaPedidos);
        buscar=findViewById(R.id.buscarPedidos);
        consultarListaPartner();

        ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaInformacion);
        lstOpciones.setAdapter(adaptador);

        buscar.setOnClickListener(this);
    }

    private void consultarListaPartner() {
        SQLiteDatabase db = conn.getReadableDatabase();
        GestionarPedidos pedidos = null;
        listaPedidos = new ArrayList<GestionarPedidos>();
        Cursor cursor = db.rawQuery("Select * From " + Utilidades.tabla_cab_pedido+ " Where "+Utilidades.fecha_cab_pedido+" = '"+fecha+"'", null);
        while (cursor.moveToNext()) {
            pedidos = new GestionarPedidos();
            pedidos.setDniPartner(cursor.getString(1));
            pedidos.setComercialPedido(cursor.getString(2));
            listaPedidos.add(pedidos);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (int i = 0; i < listaPedidos.size(); i++) {
            listaInformacion.add(listaPedidos.get(i).getDniPartner() + " - " + listaPedidos.get(i).getComercialPedido());
        }
    }
    private String ObtenerPrecioProducto(String d) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.precio_bace_pAlmacen + " FROM " + Utilidades.tabla_almacen_deleg + " WHERE " + Utilidades.nombre_producto_almacen + " = '"+d+"'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    private String ObtenerIdProducto(String c) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.id_producto_almacen + " FROM " + Utilidades.tabla_almacen_deleg + " WHERE " + Utilidades.nombre_producto_almacen + " = '"+c+"'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    private String ObtenerIdComercial(String b) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.id_comercial + " FROM " + Utilidades.tabla_comercial + " WHERE " + Utilidades.nombre_comercial + " = '" + b+"'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    private String ObtenerIdPartner(String a) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.dni_partner + " FROM " + Utilidades.tabla_partner + " WHERE " + Utilidades.nombre_partner + " = '"+a+"'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);

        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    @Override
    public void onClick(View v) {
        Intent myInten = new Intent(getApplicationContext(), OpcionesPedidos.class);
        startActivity(myInten);
    }
}
