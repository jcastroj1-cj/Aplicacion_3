package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Usuario;

public interface CrearUsuarioUseCase {
    Usuario crear(Usuario usuario);
}
