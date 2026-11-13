package com.jeancarlos.unidad1.model;

/**
 * Los nombres de los campos coinciden con el JSON de la API,
 * así Gson convierte automáticamente en ambos sentidos.
 */
public class Computador {
    public Long id;
    public String usuarioId;
    public String codigo;
    public String marca;
    public String categoria;
    public String marcaCpu;
    public String velocidadCpu;
    public String tecnologiaRam;
    public String capacidadRam;
    public String capacidadDisco;
    public String tecnologiaDisco;
    public String numPuertosUsb;
    public String numPuertosHdmi;
    public String marcaMonitor;
    public String pulgadas;
    public String precio;
    public Double latitud;   // sensor GPS
    public Double longitud;  // sensor GPS
    public Double luz;       // sensor de luz (lux)
    public String fecha;

    public Computador() {
    }
}
