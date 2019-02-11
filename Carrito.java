package com.example.adminportatil.tssapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Carrito extends AppCompatActivity implements View.OnClickListener {

    private Button confirmar, borrar;
    private TextView  precioP, cantidadP, nombreP;
    private String part, comercial, info, hoy, nombre, precio, partnerCabecera, comercialCabecera, fechaCabecera, idLineas, precioLinea, aux="PRODUCTO",aux2="CANTIDAD",aux3="PRECIO",cadena,cadena2,precioProducto;
    int cont, i,j;
    private String[] arrayProducto, arrayCantidad, arrayPrecio;
    private int pTotal, cantidad;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        //obtenemos la información obtenida de PedidosActiviti.java y la guardamos en sus variables correspondiente


        conn = new ConexionSQLiteHelper(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        nombre = bundle.getString("titulo");
        precio = bundle.getString("precio");
        // descrip = bundle.getString("descrip");
        cantidad = bundle.getInt("cantida");
        comercial = bundle.getString("comercial");
        pTotal = bundle.getInt("precioTotal");
        part = bundle.getString("partner");
        cont=0;
        i=0;
        j=0;

        confirmar = findViewById(R.id.btnConfirmar);
        borrar = findViewById(R.id.btnBorrar);

        precioP = findViewById(R.id.precioProducto);
        cantidadP = findViewById(R.id.cantidadProducto);
        nombreP = findViewById(R.id.nProducto);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("/storage/emulated/0/listaProductos.txt"));
            while ((cadena = br.readLine()) != null) {
                cont++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader b;
        arrayProducto = new String[cont];
        arrayPrecio = new String[cont];
        arrayCantidad = new String[cont];

        try {
            b = new BufferedReader(new FileReader("/storage/emulated/0/listaProductos.txt"));
            while ((cadena = b.readLine()) != null) {
                SQLiteDatabase db = conn.getReadableDatabase();
                String seleccion = "Select " + Utilidades.precio_bace_pAlmacen + " From " + Utilidades.tabla_almacen_deleg +
                        " Where " + Utilidades.nombre_producto_almacen + " = '" + cadena + "'";

                Cursor cursor = db.rawQuery(seleccion, null);
                while (cursor.moveToNext()) {
                    precioProducto = cursor.getString(0);

                }
                aux = aux + "\n" + cadena + "\n";
                arrayProducto[i] = cadena;

                aux3 = aux3 + "\n" + precioProducto + "\n";
                arrayPrecio[i]= precioProducto;
                i++;

            }
            b.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader b2;
        try {
            b2 = new BufferedReader(new FileReader("/storage/emulated/0/listaCantidad.txt"));
            while ((cadena2 = b2.readLine()) != null) {

                arrayCantidad[j]=cadena2;
                aux2 = aux2 + "\n" + cadena2 + "\n";
                j++;

            }
            b2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        precioP.setText(aux3);
        cantidadP.setText(aux2);
        nombreP.setText(aux);

           // Toast.makeText(Carrito.this, aux, Toast.LENGTH_SHORT).show();

        partnerCabecera = ObtenerIdPartner(part);
        comercialCabecera = ObtenerIdComercial(comercial);
        idLineas = ObtenerIdProducto(nombre);
        precioLinea = ObtenerPrecioProducto(nombre);

        //Activamos el escuchador del boton confirmar
        confirmar.setOnClickListener(this);
        borrar.setOnClickListener(this);
    }

    //Al hacer click confirmar escribiremos la informacion de la pTotal en la ruta especificada
    @Override
    public void onClick(View v) {
        if (v == confirmar) {
            Calendar cal = Calendar.getInstance();
            int dia = cal.get(Calendar.DAY_OF_MONTH);
            int mes = cal.get(Calendar.MONTH);
            int anio = cal.get(Calendar.YEAR);
            int cont = 0;
            mes = mes + 1;
            hoy = dia + "/" + mes + "/" + anio;
            String id;
            String idP;


            try {
                String data = "CABECERA DE PEDIDO \n\r " +
                        "Partner: " + partnerCabecera
                        + "\n\r Comercial: "
                        + comercialCabecera
                        + "\n\r Fecha del Pedido: " + hoy + "\n\r" +
                        "DETALLE DEL PEDIDO \n\r " +
                        "Producto - Cantidad - Precio - Preico Total\n\r "
                        + idLineas + " - " + cantidad + " - " + precioLinea + " - " + pTotal + "\n\r";
                File file = new File("/storage/emulated/0/informacionTss/productos.txt");

                // Si el archivo no existe, se crea!
                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader("/storage/emulated/0/listaProductos.txt"));
                    while ((cadena = br.readLine()) != null) {
                        cont++;
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedWriter bw1;
                try {
                    bw1 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaProductos.txt"));
                    bw1.write("");
                    bw1.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedWriter bw2;
                try {
                    bw2 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaCantidad.txt"));
                    bw2.write("");
                    bw2.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(data);
                bw.newLine();
                bw.close();
                fw.close();

                insertarCabeceraPedido(partnerCabecera, comercialCabecera, hoy);
                int i = 0;
                while (i < cont) {
                    SQLiteDatabase db = conn.getReadableDatabase();
                    id = "Select " + Utilidades.id_producto_almacen + " From " + Utilidades.tabla_almacen_deleg +
                            " Where " + Utilidades.nombre_producto_almacen + " = '" + arrayProducto[i] + "'";
                    Cursor cursor = db.rawQuery(id, null);
                    while (cursor.moveToNext()) {
                        idP = cursor.getString(0);
                        insertarLineaPedido(idP, Integer.parseInt(arrayCantidad[i]), arrayPrecio[i]);
                        i++;
                    }

                }

                Toast.makeText(Carrito.this, "Los datos han sido guardados, correctamente", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Carrito.this);
                builder.setTitle("¡Error!");
                builder.setMessage("Tienes un error de sintaxis," + e);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }else{
            //borrar
            precioP.setText("");
            cantidadP.setText("");
            nombreP.setText("");

            BufferedWriter bw1;
            try {
                bw1 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaProductos.txt"));
                bw1.write("");
                bw1.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            BufferedWriter bw2;
            try {
                bw2 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaCantidad.txt"));
                bw2.write("");
                bw2.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertarLineaPedido(String producto, int cantidad, String precio) {
        SQLiteDatabase db = conn.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.id_pedido_linea_pedido, ObtenerIdPedido());
        contentValues.put(Utilidades.id_linea, obtenerLineas());
        contentValues.put(Utilidades.producto_lin_pedido, producto);
        contentValues.put(Utilidades.cantidad_lin_pedido, cantidad);
        contentValues.put(Utilidades.precio_lin_pedido, precio);

    db.insert(Utilidades.tabla_lin_pedido, null, contentValues);
    }

    private Integer obtenerLineas() {
        int cantidad=0;
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select count() from "+Utilidades.tabla_lin_pedido,null);
        while (cursor.moveToNext()){
            cantidad=cursor.getInt(0);
        }
        return cantidad+1;
    }

    private int ObtenerIdPedido() {
        int caunt = 0;
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From " + Utilidades.tabla_cab_pedido, null);
        while (cursor.moveToNext()) {
            caunt++;
        }

        return caunt;
    }

    private void insertarCabeceraPedido(String part, String comer, String hoy) {
        SQLiteDatabase db = conn.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.dni_partner_cab_pedido, part);
        contentValues.put(Utilidades.comercial_cab_pedido, comer);
        contentValues.put(Utilidades.fecha_cab_pedido, hoy);

        long ins = db.insert(Utilidades.tabla_cab_pedido, null, contentValues);
        if (ins == -1) {
            Toast.makeText(getApplicationContext(), "No se insertado correctamente el pedido", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Se ha completado correctamente el pedido", Toast.LENGTH_SHORT).show();
        }
    }

    private String ObtenerPrecioProducto(String d) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.precio_bace_pAlmacen + " FROM " + Utilidades.tabla_almacen_deleg + " WHERE " + Utilidades.nombre_producto_almacen + " = '" + d + "'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }

    private String ObtenerIdProducto(String c) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.id_producto_almacen + " FROM " + Utilidades.tabla_almacen_deleg + " WHERE " + Utilidades.nombre_producto_almacen + " = '" + c + "'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);
        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
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

    private String ObtenerIdPartner(String a) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String seleccion = "SELECT " + Utilidades.dni_partner + " FROM " + Utilidades.tabla_partner + " WHERE " + Utilidades.nombre_partner + " = '" + a + "'";
        String resultado = "";
        Cursor cursor = db.rawQuery(seleccion, null);

        while (cursor.moveToNext()) {
            resultado = cursor.getString(0);
        }
        return resultado;
    }
}
