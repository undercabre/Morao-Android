package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class CuatroOpcionesActivity extends Activity {
    private TextView txt1, usuario;
    private Button bt1, bt2, bt3, bt4;
    private String datos_Enviar, dato, pass;
    ConexionSQLiteHelper conn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuatro_opciones);

        //Obtenemos la informacion que nos manda al MainActivity.java
        Bundle extras = getIntent().getExtras();
        datos_Enviar = extras.getString("nombre");
        dato = extras.getString("usuario");
        pass = extras.getString("pass");

        txt1 = findViewById(R.id.provincia);
        usuario = findViewById(R.id.txtUsuario);
        bt1 = findViewById(R.id.agenda);
        bt2 = findViewById(R.id.partners);
        bt3 = findViewById(R.id.pedidos);
        bt4 = findViewById(R.id.envio);

        txt1.setText(datos_Enviar);
        usuario.setText(comprobarUsuario(dato, pass));
        conn = new ConexionSQLiteHelper(getApplicationContext());

        try {
            consulta();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        //botón paara que vaya a la agenda
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInten = new Intent(CuatroOpcionesActivity.this, AgendaActivity.class);
                startActivityForResult(myInten, 1234);
            }
        });

        //botón paara que vaya a los partners
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInten = new Intent(CuatroOpcionesActivity.this, PartnersActivity.class);
                myInten.putExtra("usuario", dato);
                myInten.putExtra("pass", pass);
                startActivityForResult(myInten, 1234);
            }
        });

        //botón paara que vaya a los pedidos
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInten = new Intent(CuatroOpcionesActivity.this, PedidosActivity.class);
                myInten.putExtra("usuario", dato);
                startActivityForResult(myInten, 1234);
            }
        });
        //botón paara que elija el uzuario el documento a enviar
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos un AlertDialog para que tenga tres opciones a elegir
                final AlertDialog.Builder builder = new AlertDialog.Builder(CuatroOpcionesActivity.this);
                CharSequence[] items = new CharSequence[3];

                items[0] = "Enviar Pedidos";
                items[1] = "Enviar Partners";
                items[2] = "Cancelar";

                builder.setTitle("¿Enviar toda la información de los Pedidos y los Partners?")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    //enviar pedidos
                                    //cargamos la informacion al correo desde el movil para poder enviarl
                                    String filelocation = "/storage/emulated/0/informacionTss/productos.txt";
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_CC, new String[]{"comercialestss@gmail.com"});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Envio de los nuevos Pedidos");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filelocation));
                                    intent.putExtra(Intent.EXTRA_TEXT, "Este correo se ha generado automáticamente igual que el adjunto");
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    try {
                                        startActivity(Intent.createChooser(intent, "Enviar email..."));
                                        finish();
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(CuatroOpcionesActivity.this,
                                                "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (i == 1) {
                                    //Enviar partners
                                    //cargamos la informacion al correo desde el movil para poder enviarlo
                                    String filelocation = "/storage/emulated/0/informacionTss/partners.txt";
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setType("text/plain");
                                    intent.putExtra(Intent.EXTRA_CC, new String[]{"comercialestss@gmail.com"});
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Envio de los nuevos Partners");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filelocation));
                                    intent.putExtra(Intent.EXTRA_TEXT, "Este correo se ha generado automáticamente igual que el adjunto");
                                    intent.setData(Uri.parse("mailto:"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    try {
                                        startActivity(Intent.createChooser(intent, "Enviar email..."));
                                        finish();
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        Toast.makeText(CuatroOpcionesActivity.this,
                                                "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    return;
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void consulta() throws ParserConfigurationException, IOException, SAXException {
        SQLiteDatabase db = conn.getWritableDatabase();

        db.execSQL("delete from " + Utilidades.tabla_almacen_deleg);
        db.execSQL("delete from sqlite_sequence where name ='" + Utilidades.tabla_almacen_deleg + "'");

        String[] nombres = new String[10];
        String[] descripcion = new String[10];
        int[] precio = new int[10];
        int[] stock = new int[10];


        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Procesamos el fichero XML y obtenemos nuestro objeto Document
        Document doc = documentBuilder.parse(new InputSource(new FileInputStream("/storage/emulated/0/listaProductos.xml")));


        // Obtenemos la etiqueta raiz
        Element elementRaiz = doc.getDocumentElement();
        // Iteramos sobre sus hijos
        NodeList hijos = elementRaiz.getChildNodes();
        for (int i = 0; i < hijos.getLength(); i++) {
            Node nodo = hijos.item(i);
        }


        NodeList listaNodos = doc.getElementsByTagName("nombre");
        for (int i = 0; i < listaNodos.getLength(); i++) {
            Node nodo = listaNodos.item(i);
            if (nodo instanceof Element) {
                nombres[i] = nodo.getTextContent();
            }
        }

        NodeList listaNodos2 = doc.getElementsByTagName("descripcion");
        for (int i = 0; i < listaNodos2.getLength(); i++) {
            Node nodo = listaNodos2.item(i);
            if (nodo instanceof Element) {
                descripcion[i] = nodo.getTextContent();
            }
        }

        NodeList listaNodos3 = doc.getElementsByTagName("precio");
        for (int i = 0; i < listaNodos3.getLength(); i++) {
            Node nodo = listaNodos3.item(i);
            if (nodo instanceof Element) {
                precio[i] = Integer.parseInt(nodo.getTextContent());
            }
        }

        NodeList listaNodos4 = doc.getElementsByTagName("stock");
        for (int i = 0; i < listaNodos4.getLength(); i++) {
            Node nodo = listaNodos4.item(i);
            if (nodo instanceof Element) {
                stock[i] = Integer.parseInt(nodo.getTextContent());
            }
        }
        for (int i = 0; i < 10; i++) {
            if (nombres[i] != null) {
                String inser1 = "INSERT INTO " + Utilidades.tabla_almacen_deleg + "(" + Utilidades.id_producto_almacen + "," + Utilidades.nombre_producto_almacen + "," +
                        "" + Utilidades.descipcion_producto_almacen + "," + Utilidades.precio_bace_pAlmacen + "," +
                        "" + Utilidades.stok_almacen_delegacion + ") VALUES(" + i + ",'" + nombres[i] + "','" + descripcion[i] + "','" + precio[i] + "'," + stock[i] + ")";

                db.execSQL(inser1);
            } else {

            }
        }
    }

    private String comprobarUsuario(String nombre, String pass) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext());
        String nommbreCompleto = "", apellido = "";
        SQLiteDatabase db = conn.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("Select " + Utilidades.apellido_comercial + " from " + Utilidades.tabla_comercial + " Where " + Utilidades.nombre_comercial + " = '" + nombre + "' and " + Utilidades.pass_comercial + " = '" + pass + "'", null);

            while (cursor.moveToNext()) {
                apellido = cursor.getString(0);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        nommbreCompleto = nombre + " " + apellido;

        return nommbreCompleto;
    }
}