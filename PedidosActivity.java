package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.util.Collections;
import java.util.List;

public class PedidosActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText fecha;
    private TextView comercial;
    private Button carrito, catalogo, pedidos;
    int contadorBasico, contadorEstandar, contadorPremiun, precioTotalFactura;
    Spinner partners;
    String nombre, precioProducto, cantidadVenta;
    List<String> listaNombres;
    ArrayAdapter<String> comboAdapter;
    ConexionSQLiteHelper conn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        conn = new ConexionSQLiteHelper(getApplicationContext());
        contadorBasico = contadorEstandar = contadorPremiun = precioTotalFactura = 0;

        Bundle extras = getIntent().getExtras();
        String dato = extras.getString("usuario");

        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH);
        mes = mes + 1;
        int anio = cal.get(Calendar.YEAR);

        fecha = findViewById(R.id.edtFechaPedido);
        partners = findViewById(R.id.spPartner);
        carrito = findViewById(R.id.btnVerCarrito);
        catalogo = findViewById(R.id.button);
        comercial = findViewById(R.id.comercial);
        pedidos = findViewById(R.id.lstPedidos);

        comercial.setText(dato);
        fecha.setText(dia + "/" + mes + "/" + anio);

        //Activamos los escuchadores de cada Botón
        carrito.setOnClickListener(this);
        catalogo.setOnClickListener(this);
        pedidos.setOnClickListener(this);


        //================Datos cargados desde Array=====================//
        //llamada al metodo carcarSpinner. Le pasamos por parámentro el array con los nombres de los partners
        // y el spinner correspondiente
        cargarSpinner(consultarListaPartners(), partners);
    }

    //utilizamos onActivityResult para recibir la información de la otra actividad
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1235 && resultCode == Activity.RESULT_OK) {
            nombre = data.getExtras().getString("titulo");
            cantidadVenta = data.getExtras().getString("cantidad");
        }
    }


    //hemos creado una estructura if else para controlar de forma ordenada todos los botones de esta actividad
    @Override
    public void onClick(View v) {
        //Al hacer click al boton guardar se añadira la informacion de la compra
        if (v == carrito) {
            if (partners.getSelectedItem().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Tienes que seleccionar o crear un nuevo partner", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    SQLiteDatabase db = conn.getReadableDatabase();
                    String seleccion = "Select " + Utilidades.precio_bace_pAlmacen + " From " + Utilidades.tabla_almacen_deleg +
                            " Where " + Utilidades.nombre_producto_almacen + " = '" + nombre + "'";

                    Cursor cursor = db.rawQuery(seleccion, null);
                    while (cursor.moveToNext()) {
                        precioProducto = cursor.getString(0);
                    }

                    String partner = partners.getSelectedItem().toString();
                    String nomComercial = comercial.getText().toString();

                    int precioTotal = 0, precio = 0, cantidad = 0;
                    precio = Integer.parseInt(precioProducto);
                    cantidad = Integer.parseInt(cantidadVenta);
                    precioTotal = precio * cantidad;

                    //informacion que enviamos al Carrito.java.
                    //NOTA IMPORTANTE: El XML con los nombres de los comerciales y los partners
                    // hay que crearlo a mano (se supone que eso nos lo mandan desde la sede centrar)
                    // por lo que si los spinners de alguno de los dos están vacios la actividad no avanzará a la siguiente.
                    Intent intent = new Intent(PedidosActivity.this, Carrito.class);
                    intent.putExtra("titulo", nombre);
                    intent.putExtra("precio", precioProducto);
                    intent.putExtra("cantida", cantidad);
                    intent.putExtra("comercial", nomComercial);
                    intent.putExtra("precioTotal", precioTotal);
                    intent.putExtra("partner", partner);

                    startActivity(intent);

                } catch (Exception ex) {
                    if (ex == null) {
                        Toast.makeText(this, "Tienes que seleccionar un producto en catalogo. ", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(this, "¡Error! " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } else if (v == catalogo) {

            Intent intent = new Intent(PedidosActivity.this, CatalogoActivity.class);
            startActivityForResult(intent, 1235);

        } else if (v == pedidos) {
            Intent intent = new Intent(PedidosActivity.this, ListaPedidosActivity.class);
            startActivityForResult(intent, 1235);
        }

    }

    //metodo para cargar tanto el spinner de los partners
    private void cargarSpinner(String arrayNombres[], Spinner insertarSpinner) {
        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        insertarSpinner.setOnItemSelectedListener(this);
        //Convierto la variable List<> en un ArrayList<>()
        listaNombres = new ArrayList<>();
        //Agrego las partner del arreglo `strComercial` a la listaComerciales
        Collections.addAll(listaNombres, arrayNombres);
        //Implemento el adapter con el contexto, layout, listaComercial
        comboAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNombres);
        //Cargo el spinner con los datos
        insertarSpinner.setAdapter(comboAdapter);
    }

    //metodo obligatorio al implementar la interfaz AdapterView.OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    //metodo obligatorio al implementar la interfaz AdapterView.OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String[] consultarListaPartners() {
        String[] nombrePartner;
        int i = 0;
        nombrePartner = new String[cuantos()];
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.nombre_partner + "  FROM " + Utilidades.tabla_partner, null);
        while (cursor.moveToNext()) {
            nombrePartner[i] = cursor.getString(0);
            i++;
        }
        return nombrePartner;
    }

    public int cuantos() {

        int i = 0;
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Utilidades.nombre_partner + "  FROM " + Utilidades.tabla_partner, null);
        while (cursor.moveToNext()) {
            i++;
        }

        return i;
    }
}