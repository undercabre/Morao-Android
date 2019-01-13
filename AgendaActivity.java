package com.example.adminportatil.tssapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CalendarView;

public class AgendaActivity extends Activity implements CalendarView.OnDateChangeListener {
    private CalendarView calendarioView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda);
        calendarioView = findViewById(R.id.CalendarView);
        calendarioView.setOnDateChangeListener(this);
    }

    @Override
    //este metodo crea un Alertdialog para crear tres opciones para que el usuario elija
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] items = new CharSequence[3];

        items[0] = "Agregar Evento";
        items[1] = "Ver Eventos";
        items[2] = "Calcelar";

        final int dia, mes, anio;
        //obtenemos el dia,mes y año y le damos valor a respectivas variables
        dia = dayOfMonth;
        mes = month + 1; //a la variable mes le sumamos 1, por defecto empieza en 0
        anio = year;

        builder.setTitle("Seleccione una Tarea")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0) {
                            //actividad agregar eventos
                            //se comunica con AddActiviti.java para poder enviar dicha información
                            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("dia", dia);
                            bundle.putInt("mes", mes);
                            bundle.putInt("año", anio);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (i == 1) {
                            //actividad ver eventos
                            //se comunica con AddActiviti.java para poder enviar dicha información
                            Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("dia", dia);
                            bundle.putInt("mes", mes);
                            bundle.putInt("año", anio);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            return;
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
