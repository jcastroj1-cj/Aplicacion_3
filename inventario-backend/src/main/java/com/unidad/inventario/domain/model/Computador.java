package com.unidad.inventario.domain.model;

public record Computador(
        Long id,
        String usuarioId,
        String codigo,
        String marca,
        String categoria,
        String marcaCpu,
        String velocidadCpu,
        String tecnologiaRam,
        String capacidadRam,
        String capacidadDisco,
        String tecnologiaDisco,
        String numPuertosUsb,
        String numPuertosHdmi,
        String marcaMonitor,
        String pulgadas,
        String precio,
        Double latitud,
        Double longitud,
        Double luz,
        String fecha) {
}
