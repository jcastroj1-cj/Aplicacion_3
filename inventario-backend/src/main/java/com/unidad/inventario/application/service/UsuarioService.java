package com.unidad.inventario.application.service;

import com.unidad.inventario.application.exception.RecursoDuplicadoException;
import com.unidad.inventario.application.exception.RecursoNoEncontradoException;
import com.unidad.inventario.application.port.in.CrearUsuarioUseCase;
import com.unidad.inventario.application.port.in.ListarUsuariosUseCase;
import com.unidad.inventario.application.port.in.ObtenerUsuarioUseCase;
import com.unidad.inventario.application.port.out.UsuarioRepositoryPort;
import com.unidad.inventario.domain.model.Usuario;
import java.util.List;

public class UsuarioService implements ListarUsuariosUseCase, CrearUsuarioUseCase, ObtenerUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioService(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtener(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario " + id + " no encontrado"));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuarioRepository.existsById(usuario.id())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con id " + usuario.id());
        }
        return usuarioRepository.save(usuario);
    }
}
