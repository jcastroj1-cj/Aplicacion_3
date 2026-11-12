package com.unidad.inventario.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataComputadorRepository extends JpaRepository<ComputadorEntity, Long> {

    List<ComputadorEntity> findByUsuarioId(String usuarioId);

    Optional<ComputadorEntity> findByIdAndUsuarioId(Long id, String usuarioId);

    boolean existsByUsuarioIdAndCodigo(String usuarioId, String codigo);
}
