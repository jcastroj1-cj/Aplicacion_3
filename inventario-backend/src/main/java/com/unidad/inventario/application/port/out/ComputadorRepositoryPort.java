package com.unidad.inventario.application.port.out;

import com.unidad.inventario.domain.model.Computador;
import java.util.List;
import java.util.Optional;

public interface ComputadorRepositoryPort {
    List<Computador> findByUsuarioId(String usuarioId);

    Optional<Computador> findByIdAndUsuarioId(Long id, String usuarioId);

    boolean existsByUsuarioIdAndCodigo(String usuarioId, String codigo);

    Computador save(Computador computador);

    void deleteById(Long id);
}
