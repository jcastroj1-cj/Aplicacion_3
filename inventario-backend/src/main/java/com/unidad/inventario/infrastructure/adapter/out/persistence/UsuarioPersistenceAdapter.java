package com.unidad.inventario.infrastructure.adapter.out.persistence;

import com.unidad.inventario.application.port.out.UsuarioRepositoryPort;
import com.unidad.inventario.domain.model.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final SpringDataUsuarioRepository repository;

    public UsuarioPersistenceAdapter(SpringDataUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity guardado = repository.save(
                new UsuarioEntity(usuario.id(), usuario.nombre(), usuario.correo()));
        return toDomain(guardado);
    }

    private Usuario toDomain(UsuarioEntity e) {
        return new Usuario(e.getId(), e.getNombre(), e.getCorreo());
    }
}
