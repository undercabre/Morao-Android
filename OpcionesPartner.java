package com.example.adminportatil.tssapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

public class OpcionesPartner extends AppCompatActivity implements View.OnClickListener {
    private Button actualizar, buscar, eliminar;
    private EditText nombre, apellido, dni, telefono, direccion, correo, comercial;
    ConexionSQLiteHelper cnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        cnn = new ConexionSQLiteHelper(getApplicationContext());
        actualizar = findViewById(R.id.actualizar);
        buscar = findViewById(R.id.buscar);
        eliminar = findViewById(R.id.eliminar);
        nombre = findViewById(R.id.nombreP);
        apellido = findViewById(R.id.apellido);
        dni = findViewById(R.id.dni_nif);
        telefono = findViewById(R.id.telefono);
        direccion = findViewById(R.id.direccion);
        correo = findViewById(R.id.correo);
        comercial = findViewById(R.id.comercial);

        buscar.setOnClickListener(this);
        actualizar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
    }

    //metodo que utilizamos para controlar loz botones
    @Override
    public void onClick(View v) {
        String n = nombre.getText().toString();
        boolean estar;
        if (v == buscar) {
            estar = verificar(nombre);
            if (estar == false) {
                buscar(n, nombre, apellido, telefono, dni, direccion, correo, comercial);
            } else {
                Toast.makeText(getApplicationContext(), "El campo de busqueda esta vacío", Toast.LENGTH_SHORT).show();
            }
        } else if (v == actualizar) {
            estar = verificar(nombre);
            if (estar == false) {
                String part, cor, dire, tlf, nomComercial, document, apellidos;

                part = nombre.getText().toString();
                cor = correo.getText().toString();
                dire = direccion.getText().toString();
                tlf = telefono.getText().toString();
                document = dni.getText().toString();
                apellidos = apellido.getText().toString();
                nomComercial = comercial.getText().toString();

                SQLiteDatabase db = cnn.getReadableDatabase();
                String actualizar = "Select * From " + Utilidades.tabla_partner + " Where " + Utilidades.nombre_partner + " ='" + part + "'";
                Cursor cursor = db.rawQuery(actualizar, null);
                while (cursor.moveToNext()) {
                    //el if tiene que ser igual para todos los campos que hay en el xml
                    if (cursor.getString(0) != document || cursor.getString(1) != part || cursor.getString(2) != apellidos ||
                            cursor.getString(3) != cor || cursor.getString(4) != tlf || cursor.getString(5) != dire ||
                            cursor.getString(6) != nomComercial) {
                        actualizarDatos(document, nombre, apellido, telefono, dni, direccion, correo, comercial);
                        ///Toast.makeText(getApplicationContext(), "Los datos han sido actualizados", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No se ha encontrado el Partner con esta identificacion -> " + document, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "El campo de busqueda esta vacío", Toast.LENGTH_SHORT).show();
            }
        } else {
            estar = verificar(nombre);
            if (estar == false){
                String document;
                document = dni.getText().toString();
                eliminarRegistros(document, nombre, apellido, telefono, dni, direccion, correo, comercial);
                Toast.makeText(this, "Los datos se han borrado correctamente", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getApplicationContext(), "El campo de busqueda esta vacío", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean verificar(EditText e1) {
        boolean esta = false;
        String nombre;
        nombre = e1.getText().toString();

        if (nombre.isEmpty()) {
            esta = true;
        }

        return esta;
    }

    //metodo que utilizamos para buscar al partner por su identificador
    private void buscar(String dato, EditText e1, EditText e2, EditText e3, EditText e4, EditText e5, EditText e6, EditText e7) {
        SQLiteDatabase db = cnn.getReadableDatabase();
        String[] parametros = {dato};
        String[] campos = {Utilidades.nombre_partner, Utilidades.apellido_partner, Utilidades.telefono_partner
                , Utilidades.dni_partner, Utilidades.direccion_partner, Utilidades.email_partner, Utilidades.comercial_partner};


        Cursor cursor = db.query(Utilidades.tabla_partner, campos, Utilidades.dni_partner + "=?", parametros, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                e1.setText(cursor.getString(0));
                e2.setText(cursor.getString(1));
                e3.setText(cursor.getString(2));
                e4.setText(cursor.getString(3));
                e5.setText(cursor.getString(4));
                e6.setText(cursor.getString(5));
                buscarComercial(e7, cursor.getString(6));
            } while (cursor.moveToNext());
        }
    }

    //metodo que se utiliza para poder buscar el nombre del comercial
    private void buscarComercial(EditText e1, String dato) {
        SQLiteDatabase db = cnn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + Utilidades.nombre_comercial + " from " + Utilidades.tabla_comercial + " Where " + Utilidades.id_comercial + " = '" + dato + "'", null);

        if (cursor.moveToFirst()) {
            do {
                e1.setText(cursor.getString(0));
            } while (cursor.moveToNext());
        }
    }

    //metodo que utilizamos para actualizar el registro existente
    public void actualizarDatos(String dato, EditText e1, EditText e2, EditText e3, EditText e4, EditText e5, EditText e6, EditText e7) {
        String partner, apellido, dni, correo, telefono, direccion, comercial;

        partner = e1.getText().toString();
        apellido = e2.getText().toString();
        telefono = e3.getText().toString();
        dni = e4.getText().toString();
        direccion = e5.getText().toString();
        correo = e6.getText().toString();
        comercial = e7.getText().toString();

        SQLiteDatabase db = cnn.getReadableDatabase();
        String[] parametros = {dato};
        if (comprobarComercial(comercial) == false) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(Utilidades.nombre_partner, partner);
            contentValues.put(Utilidades.apellido_partner, apellido);
            contentValues.put(Utilidades.telefono_partner, telefono);
            contentValues.put(Utilidades.dni_partner, dni);
            contentValues.put(Utilidades.direccion_partner, direccion);
            contentValues.put(Utilidades.email_partner, correo);
            contentValues.put(Utilidades.comercial_partner, cambiarDato(comercial));

            long ins = db.update(Utilidades.tabla_partner, contentValues, Utilidades.dni_partner + " =? ", parametros);
            if (ins == -1) {
                Toast.makeText(this, "No se ha podido actualizar los campos a actualizar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Los campos se han actualizado correctamente", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No se ha podido actualizar los campos a actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    //cambiamos el dato(nombre del comercial) y lo cambiamos por su id_comercial, para poder guardarlo correctamente en la base de datos
    private String cambiarDato(String dato) {
        SQLiteDatabase db = cnn.getReadableDatabase();
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

    //berificamos si existe el nuevo comercial
    public boolean comprobarComercial(String nombre) {
        SQLiteDatabase db = cnn.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + Utilidades.id_comercial + " from " + Utilidades.tabla_comercial + " Where " + Utilidades.nombre_comercial + " = '" + nombre + "'", null);
        boolean esta = false;
        if (cursor.getCount() > 0) {
            esta = false;
        } else {
            esta = true;
        }
        return esta;
    }

    //eliminar el registro
    private void eliminarRegistros(String dato, EditText e1, EditText e2, EditText e3, EditText e4, EditText e5, EditText e6, EditText e7) {
        SQLiteDatabase db = cnn.getReadableDatabase();

        String[] parametros = {dato};
        db.delete(Utilidades.tabla_partner, Utilidades.dni_partner + " =? ", parametros);
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
        e7.setText("");
    }
}
