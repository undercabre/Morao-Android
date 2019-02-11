package com.example.adminportatil.tssapp;

public class GestionarPedidos {
    String dniPartner;
    String comercialPedido;
    String fechaPedido;
    String productos;
    String cantida;
    String preci;
    String id_cabecera;
    String id_linea;

    GestionarPedidos() {

    }

    public String getId_linea() {
        return id_linea;
    }

    public void setId_linea(String id_linea) {
        this.id_linea = id_linea;
    }

    public String getId_cabecera() {
        return id_cabecera;
    }

    public void setId_cabecera(String id_cabecera) {
        this.id_cabecera = id_cabecera;
    }

    public String getDniPartner() {
        return dniPartner;
    }

    public void setDniPartner(String dniPartner) {
        this.dniPartner = dniPartner;
    }

    public String getComercialPedido() {
        return comercialPedido;
    }

    public void setComercialPedido(String comercialPedido) {
        this.comercialPedido = comercialPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getCantida() {
        return cantida;
    }

    public void setCantida(String cantida) {
        this.cantida = cantida;
    }

    public String getPreci() {
        return preci;
    }

    public void setPreci(String preci) {
        this.preci = preci;
    }
}
