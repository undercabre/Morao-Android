package com.example.adminportatil.tssapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.util.ArrayList;

public class OpcionesPedidos extends AppCompatActivity implements View.OnClickListener {
    private Button eliminar, actualizar, buscar;
    private EditText etbusqueda, partner, comercial, precio, cantidad, producto;
    private ListView lstOpciones;
    String n,n2;
    ArrayList<GestionarPedidos> lstPedidos;
    ArrayList<String> listaInformacion;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_pedidos);

        conn = new ConexionSQLiteHelper(getApplicationContext());

        eliminar = findViewById(R.id.btnEliminar);
        actualizar = findViewById(R.id.btnActualizar);
        buscar = findViewById(R.id.btnBusquedaPedidos);

        etbusqueda = findViewById(R.id.etBusqueda);
        partner = findViewById(R.id.etPartner);
        comercial = findViewById(R.id.etComercial);
        precio = findViewById(R.id.etPrecio);
        cantidad = findViewById(R.id.etCantidad);
        producto = findViewById(R.id.etProducto);

        lstOpciones = findViewById(R.id.listOpciones);

        buscar.setOnClickListener(this);
        actualizar.setOnClickListener(this);
        eliminar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String busqueda = etbusqueda.getText().toString();
        if (v == buscar) {
            //buscar
            if (!busqueda.isEmpty()) {
                obtenerListaCabeceraPedido(busqueda);

                ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listaInformacion);
                lstOpciones.setAdapter(adaptador);
                lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        partner.setText(lstPedidos.get(position).getDniPartner());
                        comercial.setText(lstPedidos.get(position).getComercialPedido());
                        producto.setText(lstPedidos.get(position).getProductos());
                        precio.setText(lstPedidos.get(position).getPreci());
                        cantidad.setText(lstPedidos.get(position).getCantida());
                        n2= lstPedidos.get(position).getId_linea();
                        n = lstPedidos.get(position).getId_cabecera();
                    }

                });


            } else {
                Toast.makeText(getApplicationContext(), "No se ha encontrado ningun pedido", Toast.LENGTH_SHORT).show();
            }
        } else if (v == actualizar) {
            //actualizar
            actualizarDatos(partner, comercial, producto, precio, cantidad, n,n2);
        } else {
            //eliminar
            borrarDatos(partner, comercial, producto, precio, cantidad, n,n2);
        }
    }

    //"Select " + Utilidades.cantidad_lin_pedido + " From " + Utilidades.tabla_cab_pedido + " JOIN " + Utilidades.tabla_lin_pedido
//                + " Where " + Utilidades.fecha_cab_pedido + "." + Utilidades.tabla_cab_pedido + " = '" + busqueda + "'"
    private void obtenerListaCabeceraPedido(String busqueda) {
        SQLiteDatabase db = conn.getReadableDatabase();
        GestionarPedidos pedidos = null;
        lstPedidos = new ArrayList<GestionarPedidos>();
        String sql = " Select * " +
                "from cab_pedido cp join lin_pedido lp " +
                "on cp.id_pedido = lp.id_pedido " +
                "where cp.fecha_pedido like '" + busqueda + "' or " +
                "cp.dni_partner like '" + busqueda + "' or " +
                "cp.comercial like '" + ObtenerIdComercial(busqueda) + "'";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            pedidos = new GestionarPedidos();
            pedidos.setId_cabecera(cursor.getString(0));
            pedidos.setDniPartner(cursor.getString(1));
            pedidos.setComercialPedido(ObtenerNombreComercial(cursor.getString(2)));
            pedidos.setId_linea(cursor.getString(4));
            pedidos.setProductos(cursor.getString(6));
            pedidos.setPreci(cursor.getString(8));
            pedidos.setCantida(cursor.getString(7));
            lstPedidos.add(pedidos);

        }
        obtenerLista();
    }

    private String ObtenerNombreComercial(String c) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.nombre_comercial + " FROM " + Utilidades.tabla_comercial + " WHERE " + Utilidades.id_comercial + " = '" + c + "'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<>();
        for (int i = 0; i < lstPedidos.size(); i++) {
            listaInformacion.add((i + 1) + "-> " + lstPedidos.get(i).getDniPartner() + "  Producto: " + lstPedidos.get(i).getProductos() + " Número pedido: " + lstPedidos.get(i).getId_cabecera());
        }
    }

    private String ObtenerIdComercial(String b) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.id_comercial + " FROM " + Utilidades.tabla_comercial + " WHERE " + Utilidades.nombre_comercial + " = '" + b + "'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }


    private void actualizarDatos(EditText e1, EditText e2, EditText e3, EditText e4, EditText e5, String dato,String dato2) {

        String partner, produc, prec, cantida, comercial, parametros[], parametros2[];
        GestionarPedidos pedidos = new GestionarPedidos();
        partner = e1.getText().toString();
        comercial = e2.getText().toString();
        produc = e3.getText().toString();
        prec = e4.getText().toString();
        cantida = e5.getText().toString();
        parametros = new String[]{dato};
        parametros2 = new String[]{dato2};
        SQLiteDatabase db = conn.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.dni_partner_cab_pedido, partner);
        contentValues.put(Utilidades.comercial_cab_pedido, cambiarDato(comercial));

        //String sql="Update "+Utilidades.tabla_cab_pedido+" set ............ where id_pedido = parametros and dni_partner = 98765432w";
        long ins = db.update(Utilidades.tabla_cab_pedido, contentValues, Utilidades.id_cab_pedido + " =? ", parametros);
        if (ins == -1) {
            Toast.makeText(this, "No se ha podido actualizar los campos a actualizar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Los campos se han actualizado correctamente", Toast.LENGTH_SHORT).show();

            ContentValues contentValues2 = new ContentValues();
            contentValues2.put(Utilidades.producto_lin_pedido, produc);
            contentValues2.put(Utilidades.precio_lin_pedido, obtenerPrecio(produc));
            contentValues2.put(Utilidades.cantidad_lin_pedido, cantida);
            long ins2 = db.update(Utilidades.tabla_lin_pedido, contentValues2, Utilidades.id_linea + " =? ", parametros2);
            if (ins2 == -1) {

            } else {
                Toast.makeText(this, "si hubo actualizacon", Toast.LENGTH_SHORT).show();
            }
            e1.setText("");
            e2.setText("");
            e3.setText("");
            e4.setText("");
            e5.setText("");
        }
    }

    private void borrarDatos(EditText e1, EditText e2, EditText e3, EditText e4, EditText e5, String dato,String dato2) {

        String parametros[], parametros2[];
        parametros = new String[]{dato};
        parametros2 = new String[]{dato2};
        SQLiteDatabase db = conn.getReadableDatabase();


        long ins = db.delete(Utilidades.tabla_cab_pedido, Utilidades.id_cab_pedido + " =? ", parametros);
        if (ins == -1) {
            Toast.makeText(this, "No se ha podido borrar los campos a actualizar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Los campos se han borrado correctamente", Toast.LENGTH_SHORT).show();

            long ins2 = db.delete(Utilidades.tabla_lin_pedido, Utilidades.id_linea + " =? ", parametros2);
            if (ins2 == -1) {

            } else {
                Toast.makeText(this, "si hubo borrado", Toast.LENGTH_SHORT).show();
            }
            e1.setText("");
            e2.setText("");
            e3.setText("");
            e4.setText("");
            e5.setText("");
        }
    }

    private String obtenerPrecio(String prec) {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + Utilidades.precio_bace_pAlmacen + " from " + Utilidades.tabla_almacen_deleg + " Where " + Utilidades.id_producto_almacen + " = '" + prec + "'", null);
        String numero = "";
        try {
            if (cursor.moveToFirst()) {
                do {
                    numero = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
        }
        return numero;
    }

    //cambiamos el dato(nombre del comercial) y lo cambiamos por su id_comercial, para poder guardarlo correctamente en la base de datos
    private String cambiarDato(String dato) {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + Utilidades.id_comercial + " from " + Utilidades.tabla_comercial + " Where " + Utilidades.nombre_comercial + " = '" + dato + "'", null);
        String numero = "";
        try {
            if (cursor.moveToFirst()) {
                do {
                    numero = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
        }
        return numero;
    }

}