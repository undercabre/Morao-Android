package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PedidosActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText fecha, cantidad;
    private TextView informacion,  carro, titulo;
    private RadioButton basico,estandar,premium;
    private Button guardar, carrito, borrar;
    private RadioGroup radioGroup;
    int contadorBasico, contadorEstandar, contadorPremiun,precioTotalFactura;
    Spinner comerciales,partners;
    String[] strComercial,strPartners;
    String nombreComercial;
    List<String> listaNombres;
    ArrayAdapter<String> comboAdapter2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        contadorBasico = contadorEstandar = contadorPremiun = precioTotalFactura=0;

        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH);
        mes = mes + 1;
        int anio = cal.get(Calendar.YEAR);

        guardar = findViewById(R.id.guardar);
        fecha = findViewById(R.id.edtFechaPedido);
        basico = findViewById(R.id.radioButton3);
        estandar = findViewById(R.id.radioButton2);
        premium = findViewById(R.id.radioButton);
        cantidad = findViewById(R.id.editText3);
        informacion = findViewById(R.id.tvInformacion);
        radioGroup = findViewById(R.id.radioGroup);
        comerciales = findViewById(R.id.spComercial);
        partners = findViewById(R.id.spPartner);
        carrito = findViewById(R.id.btnVerCarrito);
        carro = findViewById(R.id.carro);
        borrar = findViewById(R.id.borrar);
        titulo = findViewById(R.id.txtTitulos);

        fecha.setText(dia + "/" + mes + "/" + anio);
        cantidad.setText("1");
        titulo.setText("Producto           Cantidad          Precio          Precio Total");

        //Activamos el escuchador del radioGroup en caso de cambiar la selección
        radioGroup.setOnCheckedChangeListener(this);
        //Activamos los escuchadores de cada Botón
        guardar.setOnClickListener(this);
        carrito.setOnClickListener(this);
        borrar.setOnClickListener(this);

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
        //llamada al metodo carcarSpinner. Le pasamos por parámentro el array con los nombres de los comerciales
        // y el spinner correspondiente
        cargarSpinner(strComercial, comerciales);

        int cont2 = 0;
        strPartners = new String[]{};
        try {

            // Implementación DOM por defecto de Java
            // Construimos nuestro DocumentBuilder
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Procesamos el fichero XML y obtenemos nuestro objeto Document
            Document doc = documentBuilder.parse(new InputSource(new FileInputStream("/storage/emulated/0/informacionTss/clientes.xml")));

            // Obtenemos la etiqueta raiz
            Element elementRaiz = doc.getDocumentElement();

            // Buscamos una etiqueta dentro del XML
            NodeList listaNodos = doc.getElementsByTagName("nombre");
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);
                if (nodo instanceof Element) {
                    cont2++;
                }
            }
            strPartners = new String[cont2];
            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodo = listaNodos.item(i);
                if (nodo instanceof Element) {
                    cont2++;
                    strPartners[i] = nodo.getTextContent();

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
        //llamada al metodo carcarSpinner. Le pasamos por parámentro el array con los nombres de los partners
        // y el spinner correspondiente
        cargarSpinner(strPartners, partners);
    }

    //Mostramos la informacion del producto segun la opcion selecciona
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (basico.isChecked()) {
            informacion.setText("TSS básico.\n Precio: 20€.\n Características: TSS versión básica, " +
                    "la mejor opción para clientes particulares, sencillo de utilizar\n");
        } else if (estandar.isChecked()) {
            informacion.setText("TSS estándar. \n Precio 55€, \n Características: " +
                    "si quieres estar a la última en seguridad esta es tu opción. Actualizaciones diarias, soporte técnico en horario laboral.\n");
        } else if (premium.isChecked()) {
            informacion.setText("TSS premium. \n Precio: 250€. \n Características: " +
                    "TSS versión premium, es la mejor opción para empresas, Siempre a la última, soporte 24 horas");
        } else {
            informacion.setText("Elige una opción para ver su información");
        }
    }

    //hemos creado una estructura if else para controlar de forma ordenada todos los botones de esta actividad
    @Override
    public void onClick(View v) {
        //Al hacer click al boton guardar se añadira la informacion de la compra
        if (v == guardar) {
            if (Integer.parseInt(cantidad.getText().toString()) > 0) {
                if (basico.isChecked()) {
                    if (contadorBasico == 0) {
                        int precio = 20;
                        int precioTotal = 0;
                        int cantidadProducto = Integer.parseInt(cantidad.getText().toString());
                        precioTotal = precio * Integer.parseInt(cantidad.getText().toString());
                        carro.append("TSS básico            " + cantidadProducto + "                    " + precio + "€                " + precioTotal + "€\n");
                        contadorBasico++;
                        precioTotalFactura+=precioTotal;
                    } else {
                        Toast.makeText(this, "Este producto ya se ha introducido, para corregirlo borre el producto y vuelva a introducirlo", Toast.LENGTH_SHORT).show();
                    }
                } else if (estandar.isChecked()) {
                    if (contadorEstandar == 0) {
                        int precio = 55;
                        int precioTotal = 0;
                        int cantidadProducto = Integer.parseInt(cantidad.getText().toString());
                        precioTotal = precio * Integer.parseInt(cantidad.getText().toString());
                        carro.append("TSS estándar        " + cantidadProducto + "                    " + precio + "€                " + precioTotal + "€\n");
                        contadorEstandar++;
                        precioTotalFactura+=precioTotal;
                    } else {
                        Toast.makeText(this, "Este producto ya se ha introducido, para corregirlo borre el producto y vuelva a introducirlo", Toast.LENGTH_SHORT).show();
                    }
                } else if (premium.isChecked()) {
                    if (contadorPremiun == 0) {
                        int precio = 250;
                        int precioTotal = 0;
                        int cantidadProducto = Integer.parseInt(cantidad.getText().toString());
                        precioTotal = precio * Integer.parseInt(cantidad.getText().toString());
                        carro.append("TSS premium        " + cantidadProducto + "                    " + precio + "€              " + precioTotal + "€\n");
                        contadorPremiun++;
                        precioTotalFactura+=precioTotal;
                    } else {
                        Toast.makeText(this, "Este producto ya se ha introducido, para corregirlo borre el producto y vuelva a introducirlo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PedidosActivity.this, "No se ha elegido ningún producto", Toast.LENGTH_SHORT).show();
                }
            }
            //Se borrara toda la informacion
        }else if(v==borrar){
            contadorBasico=contadorEstandar=contadorPremiun=precioTotalFactura=0;
            carro.setText("");
            // sino al hacer click en Ver Carrito Creamos una comunicacion entre PedidosActivity.java y Carrito.java
        } else {
            String nomComercial = comerciales.getSelectedItem().toString();
            String nomPartner = partners.getSelectedItem().toString();

            //informacion que enviamos al Carrito.java.
            //NOTA IMPORTANTE: El XML con los nombres de los comerciales y los partners
            // hay que crearlo a mano (se supone que eso nos lo mandan desde la sede centrar)
            // por lo que si los spinners de alguno de los dos están vacios la actividad no avanzará a la siguiente.
            Intent intent = new Intent(PedidosActivity.this, Carrito.class);
            intent.putExtra("informacion", carro.getText().toString());
            intent.putExtra("partners", nomPartner);
            intent.putExtra("comercial", nomComercial);
            intent.putExtra("factura",precioTotalFactura);
            startActivityForResult(intent, 1234);
        }
    }
    //contador para no elegir dos veces el mismo producto
    public int veces() {
        int suma = 1;
        if (basico.isChecked()) {
            suma++;
        }
        return suma;
    }
    //metodo para cargar tanto el spinner de los comerciales como el de los partners
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