package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartnersActivity extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private EditText nombrePartner, telefono, correo, direccion, documentacion, apellido;
    private Button guardar, view, setting;
    TextView comerciales;
    Spinner partners;
    String[] strComercial;
    String dato, pass;
    List<String> listaNombres;
    ArrayAdapter<String> comboAdapter2;
    ConexionSQLiteHelper conn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);


        conn = new ConexionSQLiteHelper(getApplicationContext());

        nombrePartner = findViewById(R.id.etNombrePartner);
        telefono = findViewById(R.id.etTelefono);
        correo = findViewById(R.id.etCorreo);
        direccion = findViewById(R.id.etDireccion);
        guardar = findViewById(R.id.btnGuardad);
        view = findViewById(R.id.btnVerPartners);
        comerciales = findViewById(R.id.nombreComercial);
        documentacion = findViewById(R.id.DNI);
        apellido = findViewById(R.id.apellido);
        setting = findViewById(R.id.Opciones);

        int cont = 0;
        strComercial = new String[]{};


        Bundle extras = getIntent().getExtras();
        dato = extras.getString("usuario");
        pass = extras.getString("pass");
        comerciales.setText(obtenerNombreApellido(dato, pass));

        view.setOnClickListener(this);
        guardar.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    //obtenemos el apellido del comercial
    private String obtenerNombreApellido(String dato, String pass) {
        SQLiteDatabase db = conn.getReadableDatabase();
        String resultado = "";
        Cursor cursor = db.rawQuery("Select " + Utilidades.apellido_comercial + " From " + Utilidades.tabla_comercial
                + " Where " + Utilidades.nombre_comercial + " = '" + dato
                + "' AND " + Utilidades.pass_comercial + " = '" + pass + "'", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    resultado = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
        }

        return dato + " " + resultado;
    }


    @Override
    public void onClick(View v) {
        if (v == guardar) {
            //guardamos la informacion del partner
            String com, cor, dire, tlf, dni, apellidos;
            String contiene, listado, texto, contenido;

            listado = texto = "";
            com = nombrePartner.getText().toString();
            cor = correo.getText().toString();
            dire = direccion.getText().toString();
            tlf = telefono.getText().toString();
            dni = documentacion.getText().toString();
            apellidos = apellido.getText().toString();


            //validamos que ningun dato este vacío
            if (com.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Nombre comercial"), Toast.LENGTH_LONG).show();
                nombrePartner.requestFocus();
            } else if (cor.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Correo"), Toast.LENGTH_LONG).show();
                correo.requestFocus();
            } else if (tlf.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Teléfono"), Toast.LENGTH_LONG).show();
                telefono.requestFocus();
            } else if (dire.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Dirección"), Toast.LENGTH_LONG).show();
                direccion.requestFocus();
            } else if (dni.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Dni"), Toast.LENGTH_LONG).show();
                direccion.requestFocus();
            } else if (apellidos.isEmpty()) {
                Toast.makeText(PartnersActivity.this, avisos("Apellidos"), Toast.LENGTH_LONG).show();
                direccion.requestFocus();
            } else {
                Boolean insert;
                //metodo que utilizamos para insertar los datos obtenidos a la base de datos
                insert = insertar(dni, com,apellidos, cor, tlf,dire, dato);

                //com`probamos si al insertar se a completado o hay un fallo
                if (insert == true) {

                    try {
                        //se crea la carpeta para almacenar la informacion obtenida
                        Toast.makeText(PartnersActivity.this, "Se han guardado todos los datos", Toast.LENGTH_SHORT).show();
                        contenido = "Nombre Partner: " + com + "\n\rApellidos Partner: " + apellidos + ", \n\rCorreo: " + cor + "" +
                                ", \n\rTeléfono: " + tlf + ", \n\rDirección: " + dire + ", \n\rDni/NIF: " + dni + "\n\r"+
                                "\n\rNombre comercial: " + dato+" -------------------------";
                        File file = new File(ruta("partners.txt"));

                        // Si el archivo no existe es creado
                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        //leemos el documento y vamos añadiendo a la variable texto la información
                        BufferedReader leer = new BufferedReader(new FileReader(ruta("partners.txt")));
                        while ((contiene = leer.readLine()) != null) {
                            texto += "\r\n" + contiene;
                        }

                        listado += contenido + texto;
                        //escribimos en el archivo la información acumulada en la variable listado
                        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta("partners.txt")));
                        bw.write(listado);
                        bw.newLine();
                        bw.close();

                    } catch (Exception e) {
                        lanzarMensajeError("¡Error!\n Se ha encontrado un problema al guardar los datos, " + e.getMessage());
                    }

                    Toast.makeText(getApplicationContext(), "Has Registrado Correctamente un nuevo partner", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No ha sido posible guardar el partner", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v == view) {
//mostramos la informacion o listado de los partners
            Intent myInten = new Intent(getApplicationContext(), ViewPartnerActivity.class);
            startActivity(myInten);
        } else {
            Intent myInten = new Intent(getApplicationContext(), OpcionesPartner.class);
            startActivityForResult(myInten, 1234);
        }
    }

    //En este metodo cargamos los datos del XML al Spinner
    private void cargarSpinner(String arrayNombres[], Spinner insertarSpinner) {
        //Implemento el setOnItemSelectedListener: para realizar acciones cuando se seleccionen los ítems
        insertarSpinner.setOnItemSelectedListener(this);
        //Convierto la variable List<> en un ArrayList<>()
        listaNombres = new ArrayList<>();
        //Agrego las comerciales del arreglo `strComercial` a la listaComerciales
        Collections.addAll(listaNombres, arrayNombres);
        //Implemento el adapter con el contexto, layout, listaComercial
        comboAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaNombres);
        //Cargo el spinner con los datos
        insertarSpinner.setAdapter(comboAdapter2);
    }

    //metodo obligatorio al implementar la interfaz AdapterView.OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    //metodo obligatorio al implementar la interfaz AdapterView.OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean insertar(String dni, String nombrePartner, String apellidos, String email, String telefono, String direccion, String comercial) {

        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.dni_partner, dni);
        contentValues.put(Utilidades.nombre_partner, nombrePartner);
        contentValues.put(Utilidades.apellido_partner, apellidos);
        contentValues.put(Utilidades.email_partner, email);
        contentValues.put(Utilidades.telefono_partner, telefono);
        contentValues.put(Utilidades.direccion_partner, direccion);
        contentValues.put(Utilidades.comercial_partner, consulta(comercial));
        long ins = db.insert(Utilidades.tabla_partner, null, contentValues);
        if (ins == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Integer consulta(String nombre) {

        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametro = {nombre};
        String[] campos = {Utilidades.id_comercial};
        Integer numero = 0;

        Cursor cursor = db.query(Utilidades.tabla_comercial, campos, Utilidades.nombre_comercial + "=?", parametro, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    numero = cursor.getInt(cursor.getColumnIndex(Utilidades.id_comercial));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return numero;
    }

    public String ruta(String fichero) {
        return "/storage/emulated/0/" + fichero;
    }

    public String avisos(String nombre) {
        return "La casilla del" + nombre + " esta vacío";
    }

    public void lanzarMensajeError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("¡Error!");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}