package com.unidad.inventario.infrastructure.adapter.in.web.dto;

import com.unidad.inventario.domain.model.Usuario;

public record UsuarioResponse(String id, String nombre, String correo) {

    public static UsuarioResponse desde(Usuario u) {
        return new UsuarioResponse(u.id(), u.nombre(), u.correo());
    }
}
