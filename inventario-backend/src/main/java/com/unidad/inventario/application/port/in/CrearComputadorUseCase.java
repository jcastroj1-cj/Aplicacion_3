package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Computador;

public interface CrearComputadorUseCase {
    Computador crear(String usuarioId, Computador datos);
}
