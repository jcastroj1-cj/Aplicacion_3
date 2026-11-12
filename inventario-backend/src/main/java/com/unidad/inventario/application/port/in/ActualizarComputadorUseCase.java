package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Computador;

public interface ActualizarComputadorUseCase {
    Computador actualizar(String usuarioId, Long id, Computador datos);
}
