package com.example.adminportatil.tssapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ViewActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    private ListView lstV;
    private ArrayAdapter<String> arrayAdapter;
    private String informacion = "", informaciones;
    private int suma = 0;
    private String nombreEvento[], info[];
    private int fecha, i;
    private String part2, part4, part6, part8, part10, part12, part14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        lstV = findViewById(R.id.ltvEventoVista);
        lstV.setOnItemLongClickListener(this);

        //obtenemos la informacio que nos envia AgendaActivity.java y lo guardamos en sus respectivas variables
        Bundle bundle = getIntent().getExtras();
        int dia, mes, anio;
        dia = bundle.getInt("dia");
        mes = bundle.getInt("mes");
        anio = bundle.getInt("año");


        try {
            nombreEvento = new String[cantidad()];
            info = new String[cantidad()];
            fecha = dia / mes / anio;
            informaciones = Integer.toString(dia) + "/" + Integer.toString(mes) + "/" + Integer.toString(anio);
            //damos valor a un arrayAdapter para poder adaptar la informacion y poder mostrarla en un ListView
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

            //leemos el documento mientras tenga mas lineas y su fecha coincida con la fecha seleccionada
            BufferedReader leer1 = new BufferedReader(new FileReader("/storage/emulated/0/informacionTss/eventos.txt"));
            while ((informacion = leer1.readLine()) != null) {
                if (informacion.contains(informaciones)) {

                    //Hacemos un split separando el texto guardado por : para extraer la información que nos interese
                    String[] parts = informacion.split(": ");
                    //String part1 = parts[0];
                    part2 = parts[1];
                    //String part3 = parts[2];
                    part4 = parts[3];
                    //String part5 = parts[4];
                    part6 = parts[5];
                    //String part7 = parts[6];
                    part8 = parts[7];
                    //String part9 = parts[8];
                    part10 = parts[9];
                    //String part11 = parts[10];
                    part12 = parts[11];
                    //String part13 = parts[12];
                    part14 = parts[13];

                    //Mientras se recorre el array se iran agregando su nosmbre del evento y su descripcion del día seleccionado
                    nombreEvento[i] = "Evento: " + part2 + "\n  Descripción: " + part14;

                    info[i] = nombreEvento[i];
                    arrayAdapter.add(info[i]);
                    i++;
                    lstV.setAdapter(arrayAdapter);
                }
            }
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
            builder.setTitle("¡ERROR!");
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    //Creamos un Alertdialog con tres opciones que se mostraran al tener un tiempo pulsada dicha información
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
        CharSequence items[] = new CharSequence[3];
        items[0] = "Eliminar Evento";
        items[1] = "Ver Información";
        items[2] = "Cancelar";
        builder.setTitle("Opciones")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0) {
                            //eliminar el evento
                            eliminar(parent.getItemAtPosition(i).toString());
                        } else if (i == 1) {
                            //ver informacion
                            informacion();
                        }else{
                            finish();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    //Este metodo elimina la informacion seleccionada
    private void eliminar(String dato) {
        String contenido = "", borrar = "", listado = "", nombre = dato;
        try {
            arrayAdapter.remove(dato);
            lstV.setAdapter(arrayAdapter);
            BufferedReader leer = new BufferedReader(new FileReader("/storage/emulated/0/informacionTss/eventos.txt"));
            while ((contenido = leer.readLine()) != null) {
                if (contenido.contains(nombre)) {
                    borrar = contenido;
                    if (contenido != borrar) {
                        listado += contenido + "\r\n";
                        BufferedWriter bw = new BufferedWriter(new FileWriter("/storage/emulated/0/informacionTss/eventos.txt"));
                        bw.write(listado);
                        bw.newLine();
                        bw.close();
                    } else {
                        Toast.makeText(this, "se ha borrado el dato encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para poder visualizar la información
    public void informacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewActivity.this);
        builder.setTitle("Información del evento");
        builder.setMessage("Nombre del evento: " + part2 + "\nUbicación: " + part4 + "\nFecha inicio: " + part6 + "\nFecha final: " + part8 + "\nHora inicial: " + part10 + "\nHora final: " + part12 + "\nDescripción: " + part14);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //cuenta las lineas que tien el documento para poder darle luego tamaño al array
    private int cantidad() {
        try {
            BufferedReader leer = new BufferedReader(new FileReader("/storage/emulated/0/informacionTss/eventos.txt"));
            while ((leer.readLine()) != null) {
                suma++;
            }
        } catch (IOException ex) {

        }
        return suma;
    }
}