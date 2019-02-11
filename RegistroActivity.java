package com.example.adminportatil.tssapp;

import android.app.AlertDialog;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUser, etPass, etCpass, etApellido, etDelegacion;
    private Button registro;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        conn = new ConexionSQLiteHelper(getApplicationContext());
        etUser = (EditText) findViewById(R.id.User);
        etApellido = (EditText) findViewById(R.id.apellido);
        etDelegacion = (EditText) findViewById(R.id.delegacion);
        etPass = (EditText) findViewById(R.id.Password);
        etCpass = (EditText) findViewById(R.id.cPassword);
        registro = (Button) findViewById(R.id.registro);

        registro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String e1, e2, e3, e4, e5;
        e1 = etUser.getText().toString();
        e2 = etApellido.getText().toString();
        e3 = etDelegacion.getText().toString();
        e4 = etPass.getText().toString();
        e5 = etCpass.getText().toString();

        if (e1.isEmpty() || e2.isEmpty() || e4.isEmpty() || e5.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Los campos estan Vacíos", Toast.LENGTH_SHORT).show();
        } else {
            if (e4.equals(e5)) {
                login(e1, e2, e3, e4);
            }
        }
    }

    public void login(String e1, String e2, String e3, String e4) {
        Boolean chekUsuario = ckUsuario(e1);
        if (chekUsuario == true) {
            Boolean insert = insert(e1, e2, e3, e4);
            if (insert == true) {
                Toast.makeText(getApplicationContext(), "Te has Registrado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Boolean insert(String e1, String e2, String e3, String e4) {
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utilidades.nombre_comercial, e1);
        contentValues.put(Utilidades.apellido_comercial, e2);
        contentValues.put(Utilidades.delegacion_comercial, e3);
        contentValues.put(Utilidades.pass_comercial, e4);
        long ins = db.insert(Utilidades.tabla_comercial, null, contentValues);
        if (ins == -1) {
            return false;
        } else {
            return true;
        }

    }

    private Boolean ckUsuario(String user) {
        SQLiteDatabase db = conn.getReadableDatabase();
        boolean esta = false;
        String[] parametro = {user};
        String[] campos = {Utilidades.nombre_comercial, Utilidades.pass_comercial};
        try {
            Cursor cursor = db.query(Utilidades.tabla_comercial, campos, Utilidades.pass_comercial + "=?", parametro, null, null, null);

            if (cursor.getCount() > 0) {
                esta = false;
            } else {
                esta = true;
            }

        } catch (Exception ex) {
            mensajeError("El documento no existe " + ex.getMessage());
        }
        return esta;
    }

    private void mensajeError(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("¡ERROR!");
        builder.setMessage(s);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}