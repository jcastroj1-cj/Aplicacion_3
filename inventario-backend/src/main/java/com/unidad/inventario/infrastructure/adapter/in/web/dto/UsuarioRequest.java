package com.unidad.inventario.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
        @NotBlank String id,
        @NotBlank String nombre,
        String correo) {
}
