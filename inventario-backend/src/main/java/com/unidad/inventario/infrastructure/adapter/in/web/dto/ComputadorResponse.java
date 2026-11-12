package com.unidad.inventario.infrastructure.adapter.in.web.dto;

import com.unidad.inventario.domain.model.Computador;

public record ComputadorResponse(
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

    public static ComputadorResponse desde(Computador c) {
        return new ComputadorResponse(c.id(), c.usuarioId(), c.codigo(), c.marca(),
                c.categoria(), c.marcaCpu(), c.velocidadCpu(), c.tecnologiaRam(),
                c.capacidadRam(), c.capacidadDisco(), c.tecnologiaDisco(), c.numPuertosUsb(),
                c.numPuertosHdmi(), c.marcaMonitor(), c.pulgadas(), c.precio(),
                c.latitud(), c.longitud(), c.luz(), c.fecha());
    }
}
