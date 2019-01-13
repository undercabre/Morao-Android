package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PartnersActivity extends Activity implements  AdapterView.OnItemSelectedListener{
    private EditText nombrePartner, telefono, correo, direccion;
    private Button guardar;
    Spinner comerciales,partners;
    String[] strComercial;
    String nombreComercial;
    List<String> listaNombres;
    ArrayAdapter<String> comboAdapter2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partners);

        nombrePartner = findViewById(R.id.etNombrePartner);
        telefono = findViewById(R.id.etTelefono);
        correo = findViewById(R.id.etCorreo);
        direccion = findViewById(R.id.etDireccion);
        guardar = findViewById(R.id.btnGuardad);
        comerciales = findViewById(R.id.spComerciante);

        int cont = 0;
        strComercial = new String[]{};

        try {

            // Implementación DOM por defecto de Java
            // Construimos nuestro DocumentBuilder
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Procesamos el fichero XML y obtenemos nuestro objeto Document
            Document doc = documentBuilder.parse(new InputSource(new FileInputStream("/storage/emulated/0/informacionTss/comercial.xml")));

            // Obtenemos la etiqueta raiz
            Element elementRaiz = doc.getDocumentElement();

            // Buscamos una etiqueta dentro del XML
            NodeList listaNodos = doc.getElementsByTagName("nombre");
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);
                if (nodo instanceof Element) {
                    cont++;
                }
            }

            //damos tamaño al array y lo rellenamos
            strComercial = new String[cont];
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);
                if (nodo instanceof Element) {
                    cont++;
                    strComercial[i] = nodo.getTextContent();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }



        //================Datos cargados desde Array=====================//
        //Hago referencia al spinner con el id `spComerciante`
        this.comerciales = (Spinner) findViewById(R.id.spComerciante);
        //llamada al metodo carcarSpinner. Le pasamos por parámentro el array con los nombres de los comerciales
        // y el spinner correspondiente
        cargarSpinner(strComercial, this.comerciales);

        //guardamos la informacion del partner
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String com, cor, dire, tlf;
                com = nombrePartner.getText().toString();
                cor = correo.getText().toString();
                dire = direccion.getText().toString();
                tlf = telefono.getText().toString();
                String nomComercial = comerciales.getSelectedItem().toString();

                //validamos que ningun dato este vacío
                if (com.isEmpty()) {
                    Toast.makeText(PartnersActivity.this, "La casilla del Nombre del Comercial esta vacío", Toast.LENGTH_LONG).show();
                    nombrePartner.requestFocus();
                } else if (cor.isEmpty()) {
                    Toast.makeText(PartnersActivity.this, "La casilla del correo esta vacío", Toast.LENGTH_LONG).show();
                    correo.requestFocus();
                } else if (tlf.isEmpty()) {
                    Toast.makeText(PartnersActivity.this, "La casilla del teléfono esta vacío", Toast.LENGTH_LONG).show();
                    telefono.requestFocus();
                } else if (dire.isEmpty()) {
                    Toast.makeText(PartnersActivity.this, "La casilla de dirección esta vacío", Toast.LENGTH_LONG).show();
                    direccion.requestFocus();
                } else {
                    Toast.makeText(PartnersActivity.this, "Se han guardado todos los datos", Toast.LENGTH_SHORT).show();

                    try {
                        //se crea la carpeta para almacenar la informacion obtenida
                        String contiene,listado,texto,contenido;
                        listado=texto="";

                        String ruta = "/storage/emulated/0/informacionTss/partners.txt";
                        contenido = "Nombre Partner: "+ com +", \n\rCorreo: "+cor+"" +
                                ", \n\rTeléfono: "+tlf+", \n\rDirección: "+dire+"" +
                                "\n\rNombre comercial: "+nomComercial+"\n\r-------------------------";
                        File file = new File(ruta);
                        // Si el archivo no existe es creado
                        if (!file.exists()) {
                            file.createNewFile();
                        }

                        //leemos el documento y vamos añadiendo a la variable texto la información
                        BufferedReader leer = new BufferedReader(new FileReader("/storage/emulated/0/informacionTss/partners.txt"));
                        while((contiene=leer.readLine())!=null){
                            texto+="\r\n"+contiene;
                        }

                        listado+=contenido+texto;
                        //escribimos en el archivo la información acumulada en la variable listado
                        BufferedWriter bw = new BufferedWriter(new FileWriter("/storage/emulated/0/informacionTss/partners.txt"));
                        bw.write(listado);
                        bw.newLine();
                        bw.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
        switch (parent.getId()) {
            case R.id.spComercial:
                //Almaceno el nombre del comercial seleccionado
                nombreComercial = strComercial[position];
                break;
        }
    }

    //metodo obligatorio al implementar la interfaz AdapterView.OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}