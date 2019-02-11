package com.example.adminportatil.tssapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CatalogoActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lstOpciones;
    private Button bt0;
    private EditText cantidad;
    private TextView select;
    private String lblEtiqueta, caracteristicas, opcionSeleccionada, cantidadVenta;
    private int precio;
    ArrayList<String> listaInformacion;
    ArrayList<ProductoInformacion> productos, produc;
    ConexionSQLiteHelper conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        bt0 = findViewById(R.id.agregar);
        select = findViewById(R.id.seleccionado);
        cantidad = findViewById(R.id.cantidad);
        bt0.setOnClickListener(this);
        conn = new ConexionSQLiteHelper(getApplicationContext());
        lstOpciones = findViewById(R.id.LstOpciones);

        //listaInformacion = new ArrayList<String>();
        Titular titular = new Titular(this, consultarInformacionProductos());

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaInformacion);
        lstOpciones.setAdapter(titular);
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                opcionSeleccionada= productos.get(position).getTitulo();
                select.setText("Seleccionado: "+opcionSeleccionada);
            }
        });
    }

    private ArrayList consultarInformacionProductos() {
        SQLiteDatabase db = conn.getReadableDatabase();
        ProductoInformacion infor = null;
        productos = new ArrayList<ProductoInformacion>();
        Cursor cursor = db.rawQuery("Select * From " + Utilidades.tabla_almacen_deleg + "", null);

        try {
            while (cursor.moveToNext()) {
                infor = new ProductoInformacion();
                infor.setTitulo(cursor.getString(1));
                infor.setPrecio(cursor.getString(3));
                infor.setDescripcion(cursor.getString(2));
                productos.add(infor);
            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return productos;

    }


    @Override
    public void onClick(View v) {

        Toast.makeText(getApplicationContext(), "producto añadido al carrito", Toast.LENGTH_SHORT).show();
        cantidadVenta = cantidad.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("titulo", opcionSeleccionada);
        intent.putExtra("cantidad", cantidadVenta);
        setResult(RESULT_OK, intent);
        finish();

        BufferedWriter bw1;
        try {
            bw1 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaProductos.txt",true));
            bw1.write(opcionSeleccionada);
            bw1.newLine();
            bw1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bw2;
        try {
            bw2 = new BufferedWriter(new FileWriter("/storage/emulated/0/listaCantidad.txt",true));
            bw2.write(cantidadVenta);
            bw2.newLine();
            bw2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}