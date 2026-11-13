package com.jeancarlos.unidad1.model;

public class Usuario {
    public String id;
    public String nombre;
    public String correo;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }
}
