package com.example.adminportatil.tssapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adminportatil.tssapp.Utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context) {
        super(context, Utilidades.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.crear_tabla_almacen_deleg);

        db.execSQL(Utilidades.crear_tabla_cab_pedido);

        db.execSQL(Utilidades.crear_tabla_comercial);

        db.execSQL(Utilidades.crear_tabla_lin_pedido);

        db.execSQL(Utilidades.crear_tabla_partner);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.tabla_almacen_deleg);
        db.execSQL(Utilidades.crear_tabla_almacen_deleg);

        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.tabla_cab_pedido);
        db.execSQL(Utilidades.crear_tabla_cab_pedido);

        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.tabla_comercial);
        db.execSQL(Utilidades.crear_tabla_comercial);

        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.tabla_lin_pedido);
        db.execSQL(Utilidades.crear_tabla_lin_pedido);

        db.execSQL("DROP TABLE IF EXISTS "+Utilidades.tabla_partner);
        db.execSQL(Utilidades.crear_tabla_partner);
    }
}
