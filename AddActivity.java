package com.example.adminportatil.tssapp;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nombre, ubicacion, deFecha, hastaFecha, deHora, hastaHora, descripcion;
    private Button cancelar, guardar, horade, horaHasta;
    private int hora, minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nombre = findViewById(R.id.edtNombreEvento);
        ubicacion = findViewById(R.id.edtUbicacion);
        deFecha = findViewById(R.id.edtFechaDesde);
        hastaFecha = findViewById(R.id.edtFechaHasta);
        deHora = findViewById(R.id.edtHoraDesde);
        hastaHora = findViewById(R.id.edtHoraHasta);
        descripcion = findViewById(R.id.edtDescripcion);
        guardar = findViewById(R.id.btnAgregar);
        cancelar = findViewById(R.id.btnCancelar);
        horade = findViewById(R.id.horaDesde);
        horaHasta = findViewById(R.id.horaHasta);

        Bundle bundle = getIntent().getExtras();
        int dia, mes, anio;
        dia = bundle.getInt("dia");
        mes = bundle.getInt("mes");
        anio = bundle.getInt("año");

        hastaFecha.setText(dia + "/" + mes + "/" + anio);
        deFecha.setText(dia + "/" + mes + "/" + anio);

        horade.setOnClickListener(this);
        horaHasta.setOnClickListener(this);
        guardar.setOnClickListener(this);
        cancelar.setOnClickListener(this);
    }

    @Override
    //hemos creado una estructura if else para controlar de forma ordenada todos los botones de esta actividad
    public void onClick(View v) {
        if (v == horade) {
            //Pedimos la hora inicial al usuario
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    deHora.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();

        } else if (v == horaHasta) {
            //Pedimos la hora final al usuario
            final Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hastaHora.setText(hourOfDay + ":" + minute);
                }
            }, hora, minutos, false);
            timePickerDialog.show();

        } else if (v == guardar) {
            //obtenemos la informacion que nos escriba el usuario y lo guardamos
            String nom, ubi, feDe, feHa, hoDe, hoHa, descrip,contiene, listado, texto, contenido;
            listado="";
            texto="";

            nom = nombre.getText().toString();
            ubi = ubicacion.getText().toString();
            feDe = deFecha.getText().toString();
            feHa = hastaFecha.getText().toString();
            hoDe = deHora.getText().toString();
            hoHa = hastaHora.getText().toString();
            descrip = descripcion.getText().toString();

            //guardamos los datos
            String ruta = "/storage/emulated/0/informacionTss/eventos.txt";
            File file = new File(ruta);

            try {
                // Si el archivo no existe es creado
                if (!file.exists()) {
                    file.createNewFile();
                }

                //leemos el archivo con una estructura repetitiva y lo añadimos a ala variable texto
                BufferedReader leer = new BufferedReader(new FileReader(ruta));
                while ((contiene = leer.readLine()) != null) {
                    texto += "\r\n" + contiene;
                }

                contenido = "Evento: " + nom + ": Ubicación: " + ubi + ": día: " + feDe +
                        ": Fecha Hasta: " + feHa + ": Hora Inicio: " + hoDe + ": Hora Hasta: " + hoHa + ": Descripción: " + descrip + "; ";

                listado += contenido + texto;

                //escribimos la informacion obtenida en la ruta especificada
                BufferedWriter bw = new BufferedWriter(new FileWriter("/storage/emulated/0/informacionTss/eventos.txt"));
                bw.write(listado);
                bw.newLine();
                bw.close();

                Toast.makeText(AddActivity.this, "Los datos han sido guardados, correctamente", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setTitle("¡Error!");
                builder.setMessage("Tienes un error de sintaxis," + e);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else if (v == cancelar) {
            finish();
        }
    }
}