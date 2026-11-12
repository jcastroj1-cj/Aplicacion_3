package com.unidad.inventario.infrastructure.adapter.in.web;

import com.unidad.inventario.application.port.in.CrearUsuarioUseCase;
import com.unidad.inventario.application.port.in.ListarUsuariosUseCase;
import com.unidad.inventario.application.port.in.ObtenerUsuarioUseCase;
import com.unidad.inventario.domain.model.Usuario;
import com.unidad.inventario.infrastructure.adapter.in.web.dto.UsuarioRequest;
import com.unidad.inventario.infrastructure.adapter.in.web.dto.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final ListarUsuariosUseCase listarUsuarios;
    private final CrearUsuarioUseCase crearUsuario;
    private final ObtenerUsuarioUseCase obtenerUsuario;

    public UsuarioController(ListarUsuariosUseCase listarUsuarios,
                             CrearUsuarioUseCase crearUsuario,
                             ObtenerUsuarioUseCase obtenerUsuario) {
        this.listarUsuarios = listarUsuarios;
        this.crearUsuario = crearUsuario;
        this.obtenerUsuario = obtenerUsuario;
    }

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public List<UsuarioResponse> listar() {
        return listarUsuarios.listar().stream().map(UsuarioResponse::desde).toList();
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{uid}")
    public UsuarioResponse obtener(@PathVariable String uid) {
        return UsuarioResponse.desde(obtenerUsuario.obtener(uid));
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        Usuario creado = crearUsuario.crear(new Usuario(request.id(), request.nombre(), request.correo()));
        return UsuarioResponse.desde(creado);
    }
}
