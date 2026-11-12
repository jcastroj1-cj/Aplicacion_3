package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Usuario;

public interface ObtenerUsuarioUseCase {
    Usuario obtener(String id);
}
