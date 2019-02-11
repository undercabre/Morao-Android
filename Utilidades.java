package com.example.adminportatil.tssapp.Utilidades;

public class Utilidades {
    //constantes de la tabla TSS
    //nombre de la base de datos
    public static final String DB_NAME = "TSSdb.db";

//Tabla  almacen delegacion
    public static final String tabla_almacen_deleg = "almacen_deleg";
    public static final String id_producto_almacen = "id_producto";
    public static final String nombre_producto_almacen = "nombre";
    public static final String descipcion_producto_almacen = "descripcion";
    public static final String precio_bace_pAlmacen = "precio_base";
    public static final String stok_almacen_delegacion = "stok";

    //Tabla del comercial
    public static final String tabla_comercial = "comercial";
    public static final String id_comercial = "id_comercial";
    public static final String nombre_comercial = "nombre_comercial";
    public static final String delegacion_comercial = "delegacion";
    public static final String apellido_comercial = "apellidos";
    public static final String pass_comercial = "pass";

//Tabla de los partner
    public static final String tabla_partner = "partner";
    public static final String dni_partner = "dni";
    public static final String nombre_partner = "nombre";
    public static final String apellido_partner = "apellidos";
    public static final String email_partner = "email";
    public static final String telefono_partner = "telefono";
    public static final String direccion_partner = "direccion";
    public static final String comercial_partner = "comercial";

    //Tabla de cabecera de pedidos
    public static final String tabla_cab_pedido = "cab_pedido";
    public static final String id_cab_pedido = "id_pedido";
    public static final String dni_partner_cab_pedido = "dni_partner";
    public static final String comercial_cab_pedido = "comercial";
    public static final String fecha_cab_pedido = "fecha_pedido";

    //Tabla de lineas de pedido
    public static final String tabla_lin_pedido = "lin_pedido";
    public static final String id_linea = "id_linea";
    public static final String id_pedido_linea_pedido = "id_pedido";
    public static final String producto_lin_pedido = "producto";
    public static final String cantidad_lin_pedido = "cantidad";
    public static final String precio_lin_pedido = "precio";

    //Tabla de Eventos
    public static final String tabla_evento= "evento";
    public static final String id_evento="id_evento";
    public static final String nombre_evento="nombre_evento";
    public static final String ubicacion_evento="ubicacion";
    public static final String fecha_desde="fecha_desde";
    public static final String fecha_hasta="fecha_hasta";
    public static final String hora_desde="hora_desde";
    public static final String hora_hasta="hora_hasta";
    public static final String descripcion_evento="descripcion";



    //creamos las tablas utilizando las variables que hemos creado anteriormente, y lo almacenamos en unas otras variables que utilizaremos mas adelante
    public static final String crear_tabla_almacen_deleg = "CREATE TABLE " + tabla_almacen_deleg + "(" + id_producto_almacen + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + nombre_producto_almacen + " TEXT, " + descipcion_producto_almacen + " TEXT, " + precio_bace_pAlmacen + " NUMBER(3), " + stok_almacen_delegacion + " Integer )";

    public static final String crear_tabla_comercial = "CREATE TABLE " + tabla_comercial + "(" + id_comercial + " Integer primary key autoincrement, " +
            "" + nombre_comercial + " text," + apellido_comercial + " text," + delegacion_comercial + " text," + pass_comercial + " text)";

    public static final String crear_tabla_partner = "CREATE TABLE " + tabla_partner + "(" + dni_partner + " TEXT PRIMARY KEY ," +
            "" + nombre_partner + " TEXT," + apellido_partner + " TEXT," + email_partner + " TEXT," + telefono_partner + " VARCHAR(9)," + direccion_partner + " TEXT," +
            "" + comercial_partner + " INTEGER)";

    public static final String crear_tabla_cab_pedido = "CREATE TABLE " + tabla_cab_pedido + "(" + id_cab_pedido + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + dni_partner_cab_pedido + " TEXT, " + comercial_cab_pedido + " INTEGER, " + fecha_cab_pedido + " TEXT, FOREIGN KEY(" + id_cab_pedido + ") REFERENCES " + tabla_partner + "(" + dni_partner + ")," +
            " FOREIGN KEY(" + comercial_cab_pedido + ") REFERENCES " + tabla_comercial + "(" + id_comercial + "))";

    public static final String crear_tabla_lin_pedido = "CREATE TABLE " + tabla_lin_pedido + "(" + id_linea + " INTEGER, " +
            id_pedido_linea_pedido + " INTEGER, " + "" + producto_lin_pedido + " INTEGER, " + cantidad_lin_pedido + " INTEGER, " + precio_lin_pedido + " INTEGER, " +
            "PRIMARY KEY(" + id_linea + ", " + id_pedido_linea_pedido + "), FOREIGN KEY(" + id_pedido_linea_pedido + ") REFERENCES " + tabla_cab_pedido + "(" + id_cab_pedido + "), " +
            "FOREIGN KEY(" + producto_lin_pedido + ") REFERENCES " + tabla_almacen_deleg + "(" + id_producto_almacen + "))";

}
