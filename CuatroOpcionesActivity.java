package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CuatroOpcionesActivity extends Activity {
    private  TextView txt1;
    private Button  bt1, bt2, bt3, bt4;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuatro_opciones);

        String datos_Enviar;

        //Obtenemos la informacion que nos manda al MainActivity.java
        Bundle extras = getIntent().getExtras();
        datos_Enviar = extras.getString("nombre");

        txt1 = findViewById(R.id.provincia);
        bt1 = findViewById(R.id.agenda);
        bt2 = findViewById(R.id.partners);
        bt3 = findViewById(R.id.pedidos);
        bt4 = findViewById(R.id.envio);

        txt1.setText(datos_Enviar);

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
                startActivityForResult(myInten, 1234);
            }
        });

        //botón paara que vaya a los pedidos
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myInten = new Intent(CuatroOpcionesActivity.this, PedidosActivity.class);
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

                builder.setTitle("¿Estas seguro de enviar toda la información de los Pedidos y los Partners?")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    //enviar pedidos
                                    //cargamos la informacion al correo desde el movil para poder enviarlo
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
}