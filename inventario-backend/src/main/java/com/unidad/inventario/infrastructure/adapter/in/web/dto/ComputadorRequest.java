package com.unidad.inventario.infrastructure.adapter.in.web.dto;

import com.unidad.inventario.domain.model.Computador;
import jakarta.validation.constraints.NotBlank;

public record ComputadorRequest(
        @NotBlank String codigo,
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

    public Computador aDominio() {
        return new Computador(null, null, codigo, marca, categoria, marcaCpu, velocidadCpu,
                tecnologiaRam, capacidadRam, capacidadDisco, tecnologiaDisco, numPuertosUsb,
                numPuertosHdmi, marcaMonitor, pulgadas, precio, latitud, longitud, luz, fecha);
    }
}
