package com.unidad.inventario.application.service;

import com.unidad.inventario.application.exception.RecursoDuplicadoException;
import com.unidad.inventario.application.exception.RecursoNoEncontradoException;
import com.unidad.inventario.application.port.in.ActualizarComputadorUseCase;
import com.unidad.inventario.application.port.in.CrearComputadorUseCase;
import com.unidad.inventario.application.port.in.EliminarComputadorUseCase;
import com.unidad.inventario.application.port.in.ListarComputadoresUseCase;
import com.unidad.inventario.application.port.out.ComputadorRepositoryPort;
import com.unidad.inventario.application.port.out.UsuarioRepositoryPort;
import com.unidad.inventario.domain.model.Computador;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComputadorService implements
        ListarComputadoresUseCase,
        CrearComputadorUseCase,
        ActualizarComputadorUseCase,
        EliminarComputadorUseCase {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ComputadorRepositoryPort computadorRepository;
    private final UsuarioRepositoryPort usuarioRepository;

    public ComputadorService(ComputadorRepositoryPort computadorRepository,
                             UsuarioRepositoryPort usuarioRepository) {
        this.computadorRepository = computadorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Computador> listarPorUsuario(String usuarioId) {
        verificarUsuario(usuarioId);
        return computadorRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Computador crear(String usuarioId, Computador datos) {
        verificarUsuario(usuarioId);
        if (computadorRepository.existsByUsuarioIdAndCodigo(usuarioId, datos.codigo())) {
            throw new RecursoDuplicadoException("Ya existe un computador con codigo " + datos.codigo());
        }
        String fecha = (datos.fecha() == null || datos.fecha().isBlank())
                ? LocalDateTime.now().format(FORMATO_FECHA)
                : datos.fecha();
        return computadorRepository.save(conDatos(null, usuarioId, datos, fecha));
    }

    @Override
    public Computador actualizar(String usuarioId, Long id, Computador datos) {
        verificarUsuario(usuarioId);
        Computador existente = computadorRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Computador " + id + " no encontrado"));

        boolean cambioCodigo = !existente.codigo().equals(datos.codigo());
        if (cambioCodigo && computadorRepository.existsByUsuarioIdAndCodigo(usuarioId, datos.codigo())) {
            throw new RecursoDuplicadoException("Ya existe un computador con codigo " + datos.codigo());
        }
        String fecha = (datos.fecha() == null || datos.fecha().isBlank()) ? existente.fecha() : datos.fecha();
        return computadorRepository.save(conDatos(existente.id(), usuarioId, datos, fecha));
    }

    @Override
    public void eliminar(String usuarioId, Long id) {
        verificarUsuario(usuarioId);
        computadorRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Computador " + id + " no encontrado"));
        computadorRepository.deleteById(id);
    }

    private Computador conDatos(Long id, String usuarioId, Computador d, String fecha) {
        return new Computador(id, usuarioId, d.codigo(), d.marca(), d.categoria(), d.marcaCpu(),
                d.velocidadCpu(), d.tecnologiaRam(), d.capacidadRam(), d.capacidadDisco(),
                d.tecnologiaDisco(), d.numPuertosUsb(), d.numPuertosHdmi(), d.marcaMonitor(),
                d.pulgadas(), d.precio(), d.latitud(), d.longitud(), d.luz(), fecha);
    }

    private void verificarUsuario(String usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario " + usuarioId + " no encontrado");
        }
    }
}
