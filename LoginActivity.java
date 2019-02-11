package com.example.adminportatil.tssapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ConexionSQLiteHelper conn;
    private Button entrar, registrar;
    private EditText usuario, contarsenia;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conn = new ConexionSQLiteHelper(getApplicationContext());
        entrar = (Button) findViewById(R.id.login);
        registrar = (Button) findViewById(R.id.registrar);
        usuario = (EditText) findViewById(R.id.User);
        contarsenia = (EditText) findViewById(R.id.Password);
        texto = findViewById(R.id.TextoInformacion);

        entrar.setOnClickListener(this);
        registrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String user, pass;
        user = usuario.getText().toString();
        pass = contarsenia.getText().toString();
        if (v == entrar) {
            conectar(user, pass);
        } else {
            Toast.makeText(getApplicationContext(), "conectado a registro de usuario", Toast.LENGTH_LONG).show();
            registro();
        }
    }

    public void conectar(String user, String pass) {
        Boolean chekUsuario = ckUsuario(user, pass);

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Los campos estan Vacíos", Toast.LENGTH_SHORT).show();
        } else {
            if (chekUsuario == false) {
                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(750);
                    Intent myInten = new Intent(getApplicationContext(), MainActivity.class);
                    myInten.putExtra("usuario", user);
                    myInten.putExtra("pass", pass);
                    startActivity(myInten);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                texto.setText("* El Usuario o la contraseña no coniciden, porfavor verifique si los datos estan bién introducidos");
            }
        }
    }

    private Boolean ckUsuario(String user, String pass) {
        SQLiteDatabase db = conn.getReadableDatabase();
        boolean esta = false;
        try {
            Cursor cursor = db.rawQuery("Select " + Utilidades.nombre_comercial + " from " + Utilidades.tabla_comercial + " Where " + Utilidades.nombre_comercial + " = '" + user + "' and " + Utilidades.pass_comercial + " = '" + pass + "'", null);

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


    public void registro() {
        try {
            entrar.setText("login");
            Thread.sleep(1000);
            Intent myInten = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(myInten);
        } catch (InterruptedException e) {

            mensajeError("Ha ocurrido un ploblema " + e.getMessage());
        }
    }

    public void mensajeError(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("ERROR");
        builder.setMessage(s);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
