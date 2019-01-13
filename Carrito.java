package com.example.adminportatil.tssapp;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Carrito extends AppCompatActivity implements View.OnClickListener {

    private Button confirmar;
    private TextView mostrar,totalFactura;
    private String partner,comercial,info,hoy;
    private int factura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        //obtenemos la información obtenida de PedidosActiviti.java y la guardamos en sus variables correspondiente
        Bundle bundle = getIntent().getExtras();
        info = bundle.getString("informacion");
        comercial=bundle.getString("comercial");
        partner=bundle.getString("partners");
        factura=bundle.getInt("factura");

        mostrar = findViewById(R.id.tVCarrito);
        confirmar = findViewById(R.id.btnConfirmar);
        totalFactura=findViewById(R.id.tvFactura);
        totalFactura.setText(factura+" €");
        mostrar.setText(info);

        //Activamos el escuchador del boton confirmar
        confirmar.setOnClickListener(this);
    }

    //Al hacer click confirmar escribiremos la informacion de la factura en la ruta especificada
    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH);
        int anio = cal.get(Calendar.YEAR);
        mes = mes + 1;
        hoy=dia+"/"+mes+"/"+anio;

        try {
            String data = "CABECERA DE PEDIDO \nPartners: "+partner+ "\nComercial: "+comercial+ "\nFecha de pedido: "+hoy+"\n\nDETALLE DE PEDIDO \n\nProducto          Cantidad        Precio        Precio Total\n" +info+"\nPrecio total a pagar: "+factura+"€\n";
            File file = new File("/storage/emulated/0/informacionTss/productos.txt");

            // Si el archivo no existe, se crea!
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter bw = null;
            FileWriter fw = null;
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(data);
            bw.newLine();
            bw.close();
            fw.close();


            Toast.makeText(Carrito.this, "Los datos han sido guardados, correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Carrito.this);
            builder.setTitle("¡Error!");
            builder.setMessage("Tienes un error de sintaxis," + e);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
