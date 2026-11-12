package com.unidad.inventario.application.port.out;

import com.unidad.inventario.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    List<Usuario> findAll();

    Optional<Usuario> findById(String id);

    boolean existsById(String id);

    Usuario save(Usuario usuario);
}
