package com.unidad.inventario.application.port.in;

import com.unidad.inventario.domain.model.Usuario;
import java.util.List;

public interface ListarUsuariosUseCase {
    List<Usuario> listar();
}
