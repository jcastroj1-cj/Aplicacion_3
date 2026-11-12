package com.unidad.inventario.infrastructure.adapter.in.web;

import com.unidad.inventario.application.port.in.ActualizarComputadorUseCase;
import com.unidad.inventario.application.port.in.CrearComputadorUseCase;
import com.unidad.inventario.application.port.in.EliminarComputadorUseCase;
import com.unidad.inventario.application.port.in.ListarComputadoresUseCase;
import com.unidad.inventario.infrastructure.adapter.in.web.dto.ComputadorRequest;
import com.unidad.inventario.infrastructure.adapter.in.web.dto.ComputadorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Computadores", description = "API para gestión de los computadores de un usuario")
@RestController
@RequestMapping("/usuarios/{uid}/computadores")
public class ComputadorController {

    private final ListarComputadoresUseCase listar;
    private final CrearComputadorUseCase crear;
    private final ActualizarComputadorUseCase actualizar;
    private final EliminarComputadorUseCase eliminar;

    public ComputadorController(ListarComputadoresUseCase listar,
                                CrearComputadorUseCase crear,
                                ActualizarComputadorUseCase actualizar,
                                EliminarComputadorUseCase eliminar) {
        this.listar = listar;
        this.crear = crear;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
    }

    @Operation(summary = "Listar los computadores de un usuario")
    @GetMapping
    public List<ComputadorResponse> listar(@PathVariable String uid) {
        return listar.listarPorUsuario(uid).stream().map(ComputadorResponse::desde).toList();
    }

    @Operation(summary = "Registrar un nuevo computador")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComputadorResponse crear(@PathVariable String uid,
                                    @Valid @RequestBody ComputadorRequest request) {
        return ComputadorResponse.desde(crear.crear(uid, request.aDominio()));
    }

    @Operation(summary = "Actualizar un computador existente")
    @PutMapping("/{id}")
    public ComputadorResponse actualizar(@PathVariable String uid,
                                         @PathVariable Long id,
                                         @Valid @RequestBody ComputadorRequest request) {
        return ComputadorResponse.desde(actualizar.actualizar(uid, id, request.aDominio()));
    }

    @Operation(summary = "Eliminar computador por ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String uid, @PathVariable Long id) {
        eliminar.eliminar(uid, id);
    }
}
